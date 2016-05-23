/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Carlos Morales
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.AlertaMultiple;
import co.uniandes.sisinfo.entities.Periodicidad;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.serviciosfuncionales.AlertaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeLocal;

/**
 * Servicio de negocio: Administración de alertas
 */
@Stateless
@EJB(name = "AlertaMultipleBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.AlertaMultipleLocal.class)
public class AlertaMultipleBean implements  AlertaMultipleLocal {
    //---------------------------------------
    // Atributos
    //---------------------------------------

    /**
     * Parser
     */
    private ParserT parser;
    /**
     * AlertaFacade
     */
    @EJB
    private AlertaMultipleFacadeLocal alertaMultipleFacade;
    /**
     * ResponsableFacade
     */
    @EJB
    private ConstanteLocal constanteBean;
    /**
     * CorreoBean
     */
    @EJB
    private CorreoLocal correoBean;
    @EJB
    private TimerGenericoBeanLocal timerGenericoBean;
    /**
     *  ConstanteBean
     */
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private PeriodicidadFacadeLocal periodicidadFacade;
    @EJB
    private TareaMultipleLocal tareaMultipleBean;
    @EJB
    private TareaMultipleFacadeLocal tareaMultipleFacade;
    private ConversorAlertaMultiple conversorAlertaMultiple;
    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de AlertaBean
     */
    public AlertaMultipleBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            timerGenericoBean = (TimerGenericoBeanLocal) serviceLocator.getLocalEJB(TimerGenericoBeanLocal.class);
//            correoBean = (CorreoLocal) serviceLocator.getLocalEJB(CorreoLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    
    public String cargarAlertas(String comando) {
        try {
            getParser().leerXML(comando);
            getConversorAlertaMultiple().setPeriodicidadFacade(periodicidadFacade);
            Secuencia secuenciaAlertas = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTAS));
            Collection<AlertaMultiple> alertasMultiples = getConversorAlertaMultiple().pasarSecuenciasAAlertas(secuenciaAlertas.getSecuencias());
            for (AlertaMultiple alertaMultiple : alertasMultiples) {
                crearAlertaMultiple(alertaMultiple.getTipo(), alertaMultiple.getNombre(), alertaMultiple.getDescripcion(), alertaMultiple.getComando(), alertaMultiple.getPeriodicidad(), alertaMultiple.isActiva(), alertaMultiple.isEnviaCorreo());
            }
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ALERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0149, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public AlertaMultiple crearAlertaAutomatica(String tipo, String comando) {
        Periodicidad periodicidad = periodicidadFacade.findByNombre(constanteBean.getConstante(Constantes.VAL_PERIODICIDAD_SEMANAL));
        AlertaMultiple am = crearAlertaMultiple(tipo, "Nombre", "Descripcion", comando,
                periodicidad, false, false);

        String asunto = Notificaciones.ASUNTO_CREACION_AUTOMATICA_ALERTA;
        String mensaje = String.format(Notificaciones.MENSAJE_CREACION_AUTOMATICA_ALERTA, tipo);
        String para = constanteBean.getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        correoBean.enviarMail(para, asunto, null, null, null, mensaje);

        return am;
    }

    @Deprecated
    @Override
    public String borrarAlerta(String comando) {
        try {
            getParser().leerXML(comando);
            String tipoAlerta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA)).getValor();
            AlertaMultiple alerta = alertaMultipleFacade.findByTipo(tipoAlerta);
            alerta.setActiva(false);
            alertaMultipleFacade.edit(alerta);
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ALERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0149, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String consultarAlertas(String root) {
        try {
            getParser().leerXML(root);
            List<AlertaMultiple> alertas = alertaMultipleFacade.findAll();
            Secuencia secuencia = getConversorAlertaMultiple().getSecuenciaAlertas(alertas);
            Collection<Secuencia> secuencias = new ArrayList();
            secuencias.add(secuencia);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0068, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0084, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarAlertasTipoTarea(String root) {
        try {
            getParser().leerXML(root);
            String tipo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA)).getValor();
            List<AlertaMultiple> alertas = new Vector();
            alertas.add(alertaMultipleFacade.findByTipo(tipo));
            Secuencia secuencia = getConversorAlertaMultiple().getSecuenciaAlertas(alertas);
            Collection<Secuencia> secuencias = new ArrayList();
            secuencias.add(secuencia);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS_TIPO_TAREA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0064, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS_TIPO_TAREA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0076, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarAlertasSinTareas(String root) {
        try {
            getParser().leerXML(root);
            List<AlertaMultiple> alertas = alertaMultipleFacade.findAll();
            Secuencia secuencia = getConversorAlertaMultiple().getSecuenciaAlertasSinTareas(alertas);
            Collection<Secuencia> secuencias = new ArrayList();
            secuencias.add(secuencia);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS_SIN_TAREAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0068, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS_SIN_TAREAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0084, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String crearAlertas(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secAlertas = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTAS));
            ArrayList<Secuencia> alertas = secAlertas.getSecuencias();
            for (Secuencia s : alertas) {

                String tipoAlerta = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA)).getValor();

                String asunto = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO)).getValor();
                String periodicidad = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDAD)).getValor();

                String nombre = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();

                String descripcion = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor();

                Periodicidad per = periodicidadFacade.findByNombre(periodicidad);

                boolean activa = Boolean.parseBoolean(s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA)).getValor());
                boolean enviaCorreo = Boolean.parseBoolean(s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENVIA_CORREO)).getValor());

                crearAlertaMultiple(tipoAlerta, nombre, descripcion, comando, per, activa, enviaCorreo);
            }

            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ALERTAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0147, new Vector());
        } catch (Exception ex) {
            try {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ALERTAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0121, new Vector());
            } catch (Exception ex1) {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public void ejecutarAlertaPorTipo(String tipo) {

        try {
            AlertaMultiple am = alertaMultipleFacade.findByTipo(tipo);
            // Si la alerta multiple no existe al momento de ejecutar el timer es necesario notificar que la alerta
            // no ha sido creada
            if (am == null) {
                throw new Exception("La alerta de tipo " + tipo + " no existe dentro del sistema");
            }

            Collection<TareaMultiple> tareas = am.getTareas();
            Timestamp hoy = new Timestamp(new Date().getTime());
            String estadoVencida = constanteBean.getConstante(Constantes.ESTADO_TAREA_VENCIDA);
            String estadoPendiente = constanteBean.getConstante(Constantes.ESTADO_TAREA_PENDIENTE);
            String estadoPendienteVencida = constanteBean.getConstante(Constantes.ESTADO_TAREA_PENDIENTE_VENCIDA);
            String estadoTerminada = constanteBean.getConstante(Constantes.ESTADO_TAREA_TERMINADA);
            Integer numEjecuciones = Integer.parseInt(constanteBean.getConstante(Constantes.VAL_NUMERO_EJECUCIONES_PENDIENTES));
            for (TareaMultiple tareaMultiple : tareas) {
                if (tareaMultiple.getFechaCaducacion().before(hoy)) {
                    if (am.getPermitePendiente()!=null && am.getPermitePendiente()) {
                    
                        // La tarea ya ha caducado y se le debe cambiar el estado a todas sus
                        // subtareas pendientes a estado pendiente vencido
                        Collection<TareaSencilla> tareasSencillas = tareaMultiple.getTareasSencillas();
                        for (TareaSencilla tareaSencilla : tareasSencillas) {
                            if(tareaSencilla.getEstado().equals(estadoPendiente)){
                                // Si la tarea se encuentra pendiente se actualiza su estado a pendientevencida
                                tareaSencilla.setEstado(estadoPendienteVencida);
                                tareaSencilla.setEjecucionesPendientes(numEjecuciones);
                            }
                        }
                    }else{
                        // La tarea ya ha caducado y se le debe cambiar el estado a todas sus
                        // subtareas pendientes a estado vencido
                        Collection<TareaSencilla> tareasSencillas = tareaMultiple.getTareasSencillas();
                        for (TareaSencilla tareaSencilla : tareasSencillas) {
                            if(!tareaSencilla.getEstado().equals(estadoTerminada))
                                tareaSencilla.setEstado(estadoVencida);
                        }
                    }
                } else if (tareaMultiple.getFechaInicio().before(hoy)) {
                    if (am.isEnviaCorreo()) {
                        // La tarea se encuentra vigente, entonces sera ejecutada
                        // La tarea solo debe ejecutarse si existe al menos una tarea sencilla que este en estado pendiente
                        if (!tareaMultipleBean.calcularEstadoTarea(tareaMultiple).equals(estadoPendiente)) {
                            continue;
                        }
                        String mensaje = tareaMultiple.getHeader();
                        String asunto = tareaMultiple.getAsunto();
                        mensaje += tareaMultipleBean.obtenerMensajeTareaCorreo(tareaMultiple, estadoPendiente);
                        mensaje += tareaMultiple.getFooter();
                        String para = "";
                        if (tareaMultiple.isPersonal()) {
                            para = tareaMultiple.getPersona().getCorreo();
                            correoBean.enviarMail(para, asunto, null, null, null, mensaje);
                        } else {
                            Collection<Usuario> usuarios = usuarioFacade.findByRol(tareaMultiple.getRol().getRol());
                            for (Usuario usuario : usuarios) {
                                correoBean.enviarMail(usuario.getPersona().getCorreo(),
                                        asunto, null, null, null, mensaje);
                            }
                        }

                    }

                }
            }

            try {
                //Regenerar el timer
                timerGenericoBean.eliminarTimer(am.getIdTimer());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Periodicidad periodicidad = am.getPeriodicidad();
            if (periodicidad == null) {
                throw new Exception("La periodicidad para la alerta de tipo " + tipo + " no existe dentro del sistema");
            }
            Timestamp fechaInicioTimer = new Timestamp(new Date().getTime() + periodicidad.getValor());
            Long idTimer = timerGenericoBean.crearTimer2("co.uniandes.sisinfo.serviciosnegocio.AlertaMultipleLocal", "ejecutarAlertaPorTipo", fechaInicioTimer, tipo, "Alertas y tareas", this.getClass().toString(), "ejecutarAlertaPorTipo", "El timer es creado periodicamente para ejecutar las tareas de una alerta");
            am.setIdTimer(idTimer);
            alertaMultipleFacade.edit(am);
            // Se verifica que el timer de tareas vencidas se encuentre creado
            tareaMultipleBean.regenerarTimerManejoTareasVencidas();

        } catch (Exception e) {
            // Si ha ocurrido un error, se debe enviar un correo al administrador notificandole del error
            String mensaje = String.format(Notificaciones.MENSAJE_ERROR_EJECUCION_ALERTA, tipo, e.getMessage());
            String asunto = Notificaciones.ASUNTO_ERROR_EJECUCION_ALERTA;
            String para = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
            correoBean.enviarMail(para, asunto, null, null, null, mensaje);
        }

    }

    public String regenerarAlerta(String comando) {
        try {
            getParser().leerXML(comando);
            String tipoAlerta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA)).getValor();
            AlertaMultiple am = alertaMultipleFacade.findByTipo(tipoAlerta);
            if (am == null) {
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_REGENERAR_ALERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MSJ_0105, new ArrayList());
            }

            timerGenericoBean.eliminarTimer(am.getIdTimer());
            Periodicidad periodicidad = am.getPeriodicidad();
            if (periodicidad == null) {
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_REGENERAR_ALERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFIGURAR_ALERTAS_GENERICAS_ERR_0003, new ArrayList());
            }
            Timestamp fechaInicioTimer = new Timestamp(new Date().getTime() + periodicidad.getValor());
            Long idTimer = timerGenericoBean.crearTimer2("co.uniandes.sisinfo.serviciosnegocio.AlertaMultipleLocal", "ejecutarAlertaPorTipo", fechaInicioTimer, tipoAlerta, "Alertas y tareas", this.getClass().toString(), "ejecutarAlertaPorTipo", "El timer es creado periodicamente para ejecutar las tareas de una alerta");
            am.setIdTimer(idTimer);
            alertaMultipleFacade.edit(am);
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_REGENERAR_ALERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darPeriodicidades(String comando) {

        List<Periodicidad> periodicidades = periodicidadFacade.findAll();
        Secuencia secPers = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDADES), "");
        for (Periodicidad p : periodicidades) {
            Secuencia secPer = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDAD), "");
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), p.getNombre());
            Secuencia secValor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), p.getValor() + "");
            secPer.agregarSecuencia(secNombre);
            secPer.agregarSecuencia(secValor);
            secPers.agregarSecuencia(secPer);
        }
        Collection<Secuencia> secuencias = new Vector<Secuencia>();
        secuencias.add(secPers);
        try {
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODICIDADES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String editarAlertaGenerica(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secAlerta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTA));
            Long idAlerta = Long.parseLong(obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ALERTA)));
            boolean activaAlerta = new Boolean(obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA)));
            String comandoAlerta = obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO));
            boolean enviaCorreoAlerta = new Boolean(obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_ENVIA_CORREO)));
            boolean permitePendiente = new Boolean(obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_PERMITE_PENDIENTE)));
            String pAlerta = obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDAD));
            String tipoAlerta = obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA));
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nombre = obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            String descripcion = obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
            Periodicidad periodicidad = periodicidadFacade.findByNombre(obtenerContenido(secAlerta, getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDAD)));
            if (periodicidad == null) {
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_ALERTA_GENERICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFIGURAR_ALERTAS_GENERICAS_ERR_0003, new Vector());
            }
            AlertaMultiple alerta = crearAlertaMultiple(tipoAlerta, nombre, descripcion, comandoAlerta, periodicidad, activaAlerta, enviaCorreoAlerta);
            alerta.setPermitePendiente(permitePendiente);
            alerta.setPeriodicidad(periodicidad);
            alertaMultipleFacade.edit(alerta);
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_ALERTA_GENERICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0147, new Vector());
        } catch (Exception ex) {
            try {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_ALERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0121, new Vector());
            } catch (Exception ex1) {
                Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }


    }

    @Override
    public String darTiposAlertas(String comando) {

        Collection<Secuencia> secuencias = new Vector<Secuencia>();
        Secuencia secuenciasAlertas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTAS), "");
        List<AlertaMultiple> alertas = alertaMultipleFacade.findAll();
        for (AlertaMultiple alerta : alertas) {
            Secuencia secAlerta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTA), "");
            secAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA), alerta.getTipo()));

            secuenciasAlertas.agregarSecuencia(secAlerta);
        }
        secuencias.add(secuenciasAlertas);
        try {
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_ALERTAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String pausarAlerta(String comando) {
        try {
            getParser().leerXML(comando);
            String tipoAlerta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA)).getValor();
            AlertaMultiple alerta = alertaMultipleFacade.findByTipo(tipoAlerta);
            alerta.setActiva(false);
            alertaMultipleFacade.edit(alerta);
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_PAUSAR_ALERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0150, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String reanudarAlerta(String comando) {
        try {
            getParser().leerXML(comando);
            String tipoAlerta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA)).getValor();
            AlertaMultiple alerta = alertaMultipleFacade.findByTipo(tipoAlerta);
            alerta.setActiva(true);
            alertaMultipleFacade.edit(alerta);
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_PAUSAR_ALERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0150, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(AlertaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private AlertaMultiple crearAlertaMultiple(String tipo, String nombre, String descripcion, String comando, Periodicidad periodicidad, boolean activa, boolean enviaCorreo) {
        AlertaMultiple alertaMultiple = alertaMultipleFacade.findByTipo(tipo);
        if (alertaMultiple == null) {
            alertaMultiple = new AlertaMultiple();
            alertaMultipleFacade.create(alertaMultiple);
            alertaMultiple.setTareas(new ArrayList());
        } else {
            timerGenericoBean.eliminarTimer(alertaMultiple.getIdTimer());
        }
        alertaMultiple.setActiva(activa);
        alertaMultiple.setEnviaCorreo(enviaCorreo);
        alertaMultiple.setPeriodicidad(periodicidad);
        alertaMultiple.setTipo(tipo);
        alertaMultiple.setComando(comando);
        alertaMultiple.setDescripcion(descripcion);
        alertaMultiple.setNombre(nombre);

        Timestamp fechaInicioTimer = new Timestamp(new Date().getTime() + periodicidad.getValor());
        Long idTimer = timerGenericoBean.crearTimer2("co.uniandes.sisinfo.serviciosnegocio.AlertaMultipleLocal", "ejecutarAlertaPorTipo", fechaInicioTimer, tipo, "Alertas y tareas", this.getClass().toString(), "crearAlertaMultiple", "El timer es creado cuando se crea la alerta por primera vez y es usado para realizar la ejecución de las tareas de la alerta");
        alertaMultiple.setIdTimer(idTimer);
        alertaMultipleFacade.edit(alertaMultiple);
        return alertaMultiple;

    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    /**
     * Retorna el Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }


    private String obtenerContenido(Secuencia secuencia, String nombre) {
        Secuencia objetivo = secuencia.obtenerPrimeraSecuencia(nombre);
        if (objetivo == null) {
            return "";
        } else {
            return objetivo.getValor();
        }
    }

    public ConversorAlertaMultiple getConversorAlertaMultiple() {
//        if (conversorAlertaMultiple == null) {
//            conversorAlertaMultiple = new ConversorAlertaMultiple(constanteBean, alertaMultipleFacade, tareaMultipleBean, periodicidadFacade, timerGenericoBean);
//        }
        return conversorAlertaMultiple;
    }



   

}
