package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Inscripcion;
import co.uniandes.sisinfo.entities.InscripcionAsistente;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.InscripcionAsistenteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InscripcionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicios de administración de inscripciones
 * @author Ivan Melo, Marcela Morales
 */
@Stateless
public class InscripcionesBean implements InscripcionesBeanRemote, InscripcionesBeanLocal {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Locales
    @EJB
    private InscripcionAsistenteFacadeLocal inscripcionAsistenteFacade;
    @EJB
    private InscripcionFacadeLocal inscripcionFacade;
    //Remotos
    @EJB
    private PersonaFacadeRemote personaFacade;
    //Útiles
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;
    @EJB
    private TareaSencillaRemote tareaSencillaBean;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    private ParserT parser;
    private ConversorInscripciones conversor;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public InscripcionesBean() {
        try {
            serviceLocator = new ServiceLocator();
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            parser = new ParserT();
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------------------------------------------
    // MÉTODOS
    //----------------------------------------------
    public ConversorInscripciones getConversor() {
        if (conversor == null) {
            conversor = new ConversorInscripciones(constanteBean, inscripcionFacade, inscripcionAsistenteFacade, personaFacade);
        }
        return conversor;
    }

    public String crearInscripcion(String comando) {
        try {
            getParser().leerXML(comando);
            //Creación de la inscripción
            Secuencia secInscripcion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCION));
            Inscripcion inscripcion = getConversor().crearInscripcionDesdeSecuencia(secInscripcion);

            Date hoy = new Date();
            /*if (hoy.before(inscripcion.getFechaInicio())) {*/
            crearTimerInicioInscripcion(inscripcion.getFechaInicio(), String.valueOf(inscripcion.getId()));
            /*} else {
                abrirInscripcion(inscripcion);
            }*/

            //Creación de timer para cancelar la inscripción
            crearTimerFinInscripcion(inscripcion.getFechaFin(), String.valueOf(inscripcion.getId()));

            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /* Convierte un arreglo de bytes a String usando valores hexadecimales
     * @param digest arreglo de bytes a convertir
     * @return String creado a partir de <code>digest</code>
     */
    private String toHexadecimal(byte[] digest) {

        String hash = "";
        for (byte aux : digest) {

            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;

    }

    private void completarTareasConfirmarInscripcion(Inscripcion inscripcion) {
        for (InscripcionAsistente asistente : inscripcion.getInvitados()) {
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INSCRIBIRSE);
            String correo = asistente.getPersona().getCorreo();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION), inscripcion.getId() + "");
            params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INVITADO), asistente.getId() + "");
            getTareaBean().realizarTareaPorCorreo(tipo, correo, params);
        }

    }

    private void crearTareaConfirmarInscripcion(Inscripcion inscripcion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (InscripcionAsistente asistente : inscripcion.getInvitados()) {
            String urlConfirmar = String.format(getConstanteBean().getConstante(Constantes.CTE_URL_INSCRIPCIONES), asistente.getHashInscrito(), "SI");
            String urlRechazar = String.format(getConstanteBean().getConstante(Constantes.CTE_URL_INSCRIPCIONES), asistente.getHashInscrito(), "NO");
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INSCRIBIRSE);
            String asunto = Notificaciones.ASUNTO_INSCRIBIRSE;
            try {

                //Crea la tarea al profesor para calificar la asistencia
                //Timestamp fI = new Timestamp(System.currentTimeMillis());
                //Timestamp fF = new Timestamp(System.currentTimeMillis() + (1000L * 60L * 60L * 24L * 3L));
                Timestamp fI = inscripcion.getFechaInicio();
                Timestamp fF = inscripcion.getFechaFin();
                String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_TODOS);
                String correoResponsable = asistente.getPersona().getCorreo();
                String nombreInvitado = asistente.getPersona().getNombres() !=null ? asistente.getPersona().getNombres() : correoResponsable;
                HashMap<String, String> paramsNew = new HashMap<String, String>();
                paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION), inscripcion.getId() + "");
                paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INVITADO), asistente.getId() + "");
                String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_INSCRIPCION_USUARIO_POR_ID);
                String header = String.format(Notificaciones.HEADER_INSCRIBIRSE, nombreInvitado);
                String footer = Notificaciones.FOOTER_INSCRIBIRSE;
                String mensajeCorreo = String.format(Notificaciones.MENSAJE_INSCRIBIRSE_CORREO, inscripcion.getNombreInscripcion(), urlConfirmar, urlRechazar, sdf.format(inscripcion.getFechaFin()));
                String mensajeDescripcion = String.format(Notificaciones.MENSAJE_INSCRIBIRSE, inscripcion.getNombreInscripcion(), sdf.format(inscripcion.getFechaFin()));
                getTareaBean().crearTareaPersona(mensajeCorreo, mensajeDescripcion, tipo, asistente.getPersona().getCorreo(), true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cierra una inscripción dado su id
     * Todas las tareas pendientes asociadas a la inscripción se vencen
     * @param idInscripcion Id de la inscripción a cerrar
     */
    private void cerrarInscripcionAuto(Long idInscripcion) {
        long id = idInscripcion.longValue();
        Inscripcion inscripcion = getInscripcionFacade().find(id);
        if (inscripcion != null) {
            inscripcion.setAbierta(false);
            getInscripcionFacade().edit(inscripcion);
            Collection<InscripcionAsistente> invitados = inscripcion.getInvitados();
            for (InscripcionAsistente inscripcionAsistente : invitados) {
                // Cambia todas las tareas pendientes a vencidas
                String tipo = getConstanteBean().getConstante((Constantes.TAG_PARAM_TIPO_INSCRIBIRSE));
                Properties params = new Properties();
                params.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_INVITADO)), inscripcionAsistente.getId().toString());
                TareaSencilla tarea = getTareaSencillaFacade().findById(getTareaBean().consultarIdTareaPorParametrosTipo(tipo, params));
                if (tarea != null) {
                    tarea.setEstado(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_VENCIDA));
                    getTareaSencillaFacade().edit(tarea);
                }
            }
        }
    }

    public String editarInscripcion(String xml) {
        try {
            getParser().leerXML(xml);
            boolean iniciada = false;
            boolean terminada = false;
            Timestamp hoy = new Timestamp(new Date().getTime());
            Secuencia secInscripcion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCION));
            Secuencia secIdInscripcion = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION));
            long idInscripcion = Long.parseLong(secIdInscripcion.getValor());
            Inscripcion inscripcion = getInscripcionFacade().find(idInscripcion);
            //Valida que la inscripción exista
            if (inscripcion == null) {
                getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS00124, new ArrayList());
            }
            //Valida que la inscripción esté abierta
            if (!inscripcion.isAbierta()) {
                getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS0048, new ArrayList());
            }
            //Actualiza la inscripción
            Secuencia nombreInscripcion = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            if (nombreInscripcion != null) {
                inscripcion.setNombreInscripcion(nombreInscripcion.getValor());
            }
            Secuencia descripcionInscripcion = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
            if (descripcionInscripcion != null) {
                inscripcion.setDetallesInscripcion(descripcionInscripcion.getValor());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Secuencia secFechaInicio = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
            if (secFechaInicio != null) {

                Date fechaInicioDate = sdf.parse(secFechaInicio.getValor());
                Timestamp fechaI = new Timestamp(fechaInicioDate.getTime());
                iniciada = hoy.after(inscripcion.getFechaInicio());
                inscripcion.setFechaInicio(fechaI);
            }
            Secuencia secFechaFin = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
            if (secFechaFin != null) {
                Date fechaFinDate = sdf.parse(secFechaFin.getValor());
                Timestamp fechaF = new Timestamp(fechaFinDate.getTime());
                terminada = hoy.after(inscripcion.getFechaFin());
                if (inscripcion.getFechaFin().compareTo(fechaF) != 0) {
                    inscripcion.setFechaFin(fechaF);
                }
            }
            Secuencia correoCreador = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CREADOR));
            if (correoCreador != null) {
                inscripcion.setCorreoCreador(correoCreador.getValor());
            }
            Secuencia correoNotificacion = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_NOTIFICACION));
            if (correoNotificacion != null) {
                inscripcion.setCorreoNotificacion(correoNotificacion.getValor());
            }
            Secuencia secFechaEvento = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_EVENTO));
            if (secFechaEvento != null) {
                Timestamp fechaEvento = Timestamp.valueOf(secFechaEvento.getValor());
                inscripcion.setFechaEvento(fechaEvento);
            }
            Secuencia lugarEvento = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_EVENTO));
            if (lugarEvento != null) {
                inscripcion.setLugarEvento(lugarEvento.getValor());
            }
            Secuencia secAbierta = secInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));
            if (secAbierta != null) {
                boolean abierta = (secAbierta.getValor().equals(getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA))) ? true : false;
                inscripcion.setAbierta(abierta);
            }

            if (hoy.after(inscripcion.getFechaFin())) {
                // Si la inscripcion finaliza antes de la fecha actual se efectua el cierre inmediatamente, independiente
                // de si ya ha iniciado o no
                cerrarInscripcionAuto(idInscripcion);
            } else if (!iniciada && hoy.after(inscripcion.getFechaInicio())) {
                // Si no ha iniciado y la fecha de hoy es posterior a la fecha de inicio, se debe iniciar la inscripcion
                abrirInscripcion(inscripcion);
                getTimerGenerico().eliminarTimerPorParametroExterno(getConstanteBean().getConstante(Constantes.CMD_CERRAR_INSCRIPCION) + "-" + idInscripcion);
                crearTimerFinInscripcion(inscripcion.getFechaFin(), "" + inscripcion.getId());
            } else if (!iniciada || hoy.before(inscripcion.getFechaInicio())) {
                // Si la inscripcion no ha iniciado o si ya inicio pero la fecha de
                // inicio se establecio despues de la fecha actual, entonces se regenera el timer de inicio y fin de la inscripcion
                inscripcion.setAbierta(false);
                completarTareasConfirmarInscripcion(inscripcion);
                getTimerGenerico().eliminarTimerPorParametroExterno(getConstanteBean().getConstante(Constantes.CMD_CREAR_INSCRIPCION) + "-" + idInscripcion);
                crearTimerInicioInscripcion(inscripcion.getFechaInicio(), "" + inscripcion.getId());
                getTimerGenerico().eliminarTimerPorParametroExterno(getConstanteBean().getConstante(Constantes.CMD_CERRAR_INSCRIPCION) + "-" + idInscripcion);
                crearTimerFinInscripcion(inscripcion.getFechaFin(), "" + inscripcion.getId());
            } else {
                // Si ya habia iniciado y la fecha de inicio se coloco despues de la fecha actual
                // Solo se debe regenerar el timer de fin
                crearTareaConfirmarInscripcion(inscripcion);
                getTimerGenerico().eliminarTimerPorParametroExterno(getConstanteBean().getConstante(Constantes.CMD_CERRAR_INSCRIPCION) + "-" + idInscripcion);
                crearTimerFinInscripcion(inscripcion.getFechaFin(), "" + inscripcion.getId());
            }

            getInscripcionFacade().edit(inscripcion);
            //Actualización de timer para cancelar la inscripción
            //getTimerGenerico().eliminarTimer(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION) + "-" + inscripcion.getId());
            //crearTimerFinInscripcion(inscripcion.getFechaFin(), String.valueOf(inscripcion.getId()));

            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0193, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String cerrarInscripcion(String xml) {
        try {
            getParser().leerXML(xml);

            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION));
            Inscripcion inscripcion = getInscripcionFacade().find(secId.getValorLong());
            //Valida que la inscripción exista
            if (inscripcion == null) {
                getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS00125, new ArrayList());
            }
            //Cierra la inscripción
            cerrarInscripcionAuto(secId.getValorLong());
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CERRAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0193, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CERRAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarInscripcion(String xml) {
        try {
            getParser().leerXML(xml);
            
            Secuencia secInscripcion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION));
            Inscripcion inscripcion = getInscripcionFacade().find(secInscripcion.getValorLong());
            //Valida que la inscripción exista
            if (inscripcion == null) {
                getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS00126, new ArrayList());
            }
            //Valida que la inscripción no esté abierta
            //if (!inscripcion.isAbierta()) {
            if (inscripcion.isAbierta()) {
                getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS00123, new ArrayList());
            }
            Collection<InscripcionAsistente> invitados = inscripcion.getInvitados();
            for (InscripcionAsistente asistente : invitados) {
                getInscripcionAsistenteFacade().remove(asistente);
            }
            //elimina las tareas asociadas a esta inscripcion
            if (inscripcion.getFechaInicio().after(new Date())) {
                eliminarTimersInscripcion(inscripcion.getId());
            }
            eliminarTareasInscribirse(inscripcion);
            getInscripcionFacade().remove(inscripcion);
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0193, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void eliminarTareasInscribirse(Inscripcion inscripcion) {
        Collection<InscripcionAsistente> invitados = inscripcion.getInvitados();
        for (InscripcionAsistente inscripcionAsistente : invitados) {
            // Realiza todas las tareas asociadas a la inscripción
            String tipo = getConstanteBean().getConstante((Constantes.TAG_PARAM_TIPO_INSCRIBIRSE));
            Properties params = new Properties();
            params.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_INVITADO)), inscripcionAsistente.getId().toString());
            TareaSencilla tarea = getTareaSencillaFacade().findById(getTareaBean().consultarIdTareaPorParametrosTipo(tipo, params));
            if (tarea != null) {
                getTareaSencillaBean().realizarTareaPorId(tarea.getId());
            }
        }
    }

    public String darDetallesAdmonInscripcion(String xml) {
        try {
            getParser().leerXML(xml);

            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION));
            Inscripcion inscripcion = getInscripcionFacade().find(secId.getValorLong());
            //Valida que la inscripción exista
            if (inscripcion == null) {
                getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS00127, new ArrayList());
            }
            //Extrae la información de la inscripción
            Secuencia secInformacionInscripcion = getConversor().crearSecuenciaAPartirDeInscripcion(inscripcion);
            Secuencia secInfoInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCIONES), "");
            secInfoInscripciones.agregarSecuencia(secInformacionInscripcion);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secInfoInscripciones);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_INSCRIPCION_ADMON_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_INSCRIPCION_ADMON_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darListaInscripcionesAdmin(String xml) {
        try {
            getParser().leerXML(xml);

            Secuencia secCorreo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CREADOR));
            List<Inscripcion> listaInscripciones = getInscripcionFacade().getInscripcionesPorCreador(secCorreo.getValor());
            Secuencia secInfoInscripciones = getConversor().crearSecuenciaAPartirDeInscripcionesAdminLigera(listaInscripciones);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secInfoInscripciones);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCION_ADMON), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCION_ADMON), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Crea una secuencia con la información básica de una inscripción dada una inscripción
     * @param inscripcion Inscripción
     * @return Secuencia construída a partir de la inscripción dada
     */
    private Secuencia crearSecuenciaInscripcionBasica(Inscripcion inscripcion) {
        Secuencia secInformacionInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_INSCRIPCION), "");

        Secuencia secIdInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION), (inscripcion.getId() != null) ? "" + inscripcion.getId() : "");
        Secuencia secNombreInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), (inscripcion.getNombreInscripcion() != null) ? inscripcion.getNombreInscripcion() : "");
        Secuencia secDescripcionInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), (inscripcion.getDetallesInscripcion() != null) ? inscripcion.getDetallesInscripcion() : "");
        Secuencia secCorreoNotificacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_NOTIFICACION), (inscripcion.getCorreoNotificacion() != null) ? inscripcion.getCorreoNotificacion() : "");
        Secuencia secFechaInicioInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), (inscripcion.getFechaInicio() != null) ? inscripcion.getFechaInicio().toString() : "");
        Secuencia secFechaFinInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), (inscripcion.getFechaFin() != null) ? inscripcion.getFechaFin().toString() : "");
        String valorEstado = inscripcion.isAbierta() ? getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA) : getConstanteBean().getConstante(Constantes.ESTADO_CERRADA);
        Secuencia secAbierta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), valorEstado);
        Secuencia secLugarEvento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_EVENTO), (inscripcion.getLugarEvento() != null) ? inscripcion.getLugarEvento() : "");
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secFechaEvento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_EVENTO), (inscripcion.getFechaEvento() != null) ? sdfHMS.format(inscripcion.getFechaEvento()) : "");

        secInformacionInscripcion.agregarSecuencia(secLugarEvento);
        secInformacionInscripcion.agregarSecuencia(secFechaEvento);
        secInformacionInscripcion.agregarSecuencia(secIdInscripcion);
        secInformacionInscripcion.agregarSecuencia(secNombreInscripcion);
        secInformacionInscripcion.agregarSecuencia(secDescripcionInscripcion);
        secInformacionInscripcion.agregarSecuencia(secFechaInicioInscripcion);
        secInformacionInscripcion.agregarSecuencia(secCorreoNotificacion);
        secInformacionInscripcion.agregarSecuencia(secFechaFinInscripcion);
        secInformacionInscripcion.agregarSecuencia(secAbierta);

        return secInformacionInscripcion;
    }

    /**
     * Crea una secuencia con la información de los invitados de una inscripción dada una inscripción
     * @param invitado Inscripción
     * @return Secuencia construída a partir de la inscripción dada
     */
    private Secuencia crearSecuenciaInscripcionInvitados(InscripcionAsistente invitado) {
        Secuencia secInfoInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADO), "");

        Secuencia secIdInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INVITADO), (invitado.getId() != null) ? String.valueOf(invitado.getId()) : "");
        Secuencia secNombreInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), (invitado.getPersona() != null && invitado.getPersona().getNombres() != null) ? invitado.getPersona().getNombres() : "");
        Secuencia secloginInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), (invitado.getPersona() != null && invitado.getPersona().getCorreo() != null) ? invitado.getPersona().getCorreo() : "");
        Secuencia secApellidoInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), (invitado.getPersona() != null && invitado.getPersona().getApellidos() != null) ? invitado.getPersona().getApellidos() : "");
        Secuencia secAtiendeInvitado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INSCRITO), (invitado.getInscrito() != null) ? invitado.getInscrito() : "");
        Secuencia secOtros = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OTROS), (invitado.getOtros() != null) ? invitado.getOtros() : "");

        secInfoInvitado.agregarSecuencia(secIdInvitado);
        secInfoInvitado.agregarSecuencia(secNombreInvitado);
        secInfoInvitado.agregarSecuencia(secApellidoInvitado);
        secInfoInvitado.agregarSecuencia(secloginInvitado);
        secInfoInvitado.agregarSecuencia(secAtiendeInvitado);
        secInfoInvitado.agregarSecuencia(secOtros);

        return secInfoInvitado;
    }

    @Deprecated
    public void manejarTimerCierreInscripcion(String info) {
        Long idInscripcion = Long.parseLong(info.split("-")[1].trim());
        Inscripcion inscripcion = getInscripcionFacade().find(idInscripcion);
        if (inscripcion != null) {
            cerrarInscripcionAuto(idInscripcion);
        }
    }

    @Override
    public void manejoTimersInscripcion(String info) {
        String[] splittedInfo = info.split("-");
        String cmd = splittedInfo[0];
        Long idInscripcion = Long.parseLong(splittedInfo[1].trim());
        Inscripcion inscripcion = getInscripcionFacade().find(idInscripcion);
        System.out.println(this.getClass() + "-manejoTimersInscripcion:" + info);
        if (inscripcion == null) {
            return;
        }
        if (cmd.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION))) {
            cerrarInscripcionAuto(idInscripcion);
        } else if (cmd.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_INSCRIPCION))) {
            abrirInscripcion(inscripcion);
        }
    }

    public void abrirInscripcion(Inscripcion inscripcion) {
        // Al momento de abrir la inscripción, esta debe estar vigente.
        if(inscripcion.getFechaFin().before(new Date())){
            return;
        }
        inscripcion.setAbierta(true);
        inscripcionFacade.edit(inscripcion);

        //Creación de tarea y envío de correo para confirmar inscripción
        crearTareaConfirmarInscripcion(inscripcion);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp fF = inscripcion.getFechaFin();
        for (InscripcionAsistente inscripcionAsistente : inscripcion.getInvitados()) {
            String urlConfirmar = String.format(getConstanteBean().getConstante(Constantes.CTE_URL_INSCRIPCIONES), inscripcionAsistente.getHashInscrito(), "SI");
            String urlRechazar = String.format(getConstanteBean().getConstante(Constantes.CTE_URL_INSCRIPCIONES), inscripcionAsistente.getHashInscrito(), "NO");
            String correoResponsable = inscripcionAsistente.getPersona().getCorreo();
            String asunto = Notificaciones.ASUNTO_INSCRIBIRSE;
            // Envia un correo informando sobre la nueva inscripcion
            String mensajeCompleto = String.format(Notificaciones.MENSAJE_INSCRIPCION_CREADA, inscripcionAsistente.getPersona().getNombres(), inscripcion.getNombreInscripcion(), inscripcion.getDetallesInscripcion(), sdf.format(fF), urlConfirmar, urlRechazar);
            getCorreoBean().enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
        }

    }

    private void crearTimerFinInscripcion(Timestamp fechaFin, String idInscripcion) {
        String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION) + "-" + idInscripcion;
        getTimerGenerico().crearTimer2("co.uniandes.sisinfo.serviciosnegocio.InscripcionesBeanRemote", "manejoTimersInscripcion", fechaFin, mensajeAsociado,
                "InscripcionesGen", this.getClass().getName(), "crearTimerFinInscripcion", "Este timer se crea cuando se crea o edita una inscripción para cerrarla en el momento en que esta expire");
    }

    private void crearTimerInicioInscripcion(Timestamp fechaInicio, String idInscripcion) {
        String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_CREAR_INSCRIPCION) + "-" + idInscripcion;
        getTimerGenerico().crearTimer2("co.uniandes.sisinfo.serviciosnegocio.InscripcionesBeanRemote", "manejoTimersInscripcion", fechaInicio, mensajeAsociado,
                "InscripcionesGen", this.getClass().getName(), "crearTimerInicioInscripcion", "Este timer se crea cuando se crea o edita una inscripción para abrirla una vez se cumpla su fecha de inicio");
    }

    public String darListaInscripcionesUsuarioPorCorreo(String xml) {
        try {
            getParser().leerXML(xml);

            Secuencia secCorreo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Collection<InscripcionAsistente> inscripcionesAsistente = getInscripcionAsistenteFacade().darInscripcionesAsistentePorCorreo(secCorreo.getValor());
            Secuencia secInscripcionesPadreUser = getConversor().crearSecuenciaLigeraAPartirDeInscripcionesAsistente(inscripcionesAsistente);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secInscripcionesPadreUser);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCIONES_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCIONES_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    @Override
    public String modificarInscripcionUsuarioPorCorreo(String xml) {
        try {
            getParser().leerXML(xml);

            String hashConfirm = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_INSCRIPCION)).getValor();
            String asiste = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INSCRITO)).getValor();

            InscripcionAsistente inscripcionAsistente = getInscripcionAsistenteFacade().findByHash(hashConfirm);
            //Valida que la inscripción exista
            if (inscripcionAsistente == null) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS00127, new ArrayList());
            }
            //Valida la fecha de la inscripción
            Timestamp fechaFin = inscripcionAsistente.getInscripcion().getFechaFin();
            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
            if (!fechaFin.after(fechaActual)) {
                ArrayList<Secuencia> params = new ArrayList<Secuencia>();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String mensaje = sdf.format(fechaFin);
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), mensaje);
                params.add(secParametro);
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_INS0049, params);
            }
            //Realiza la tarea
            realizarTareaInscribirseInvitado(inscripcionAsistente.getInscripcion().getId(), inscripcionAsistente.getId());
            //Actualiza la información de la inscripción
            inscripcionAsistente.setInscrito(asiste);
            String otros = (Boolean.parseBoolean(asiste)) ? "Confirmada no Asistencia por Correo" : "Confirmada Asistencia por Correo";
            inscripcionAsistente.setOtros(otros);
            if (asiste.equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_CONFIRMADO))) {
                informarNuevoInscrito(inscripcionAsistente);
            }
            getInscripcionAsistenteFacade().edit(inscripcionAsistente);
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String modificarInscripcionUsuario(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secInfoInscripcion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INVITADO));
            String idInscripcionUser = secInfoInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INVITADO)).getValor();
            String asiste = secInfoInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INSCRITO)).getValor();

            InscripcionAsistente asistente = getInscripcionAsistenteFacade().find(Long.parseLong(idInscripcionUser));
            //Valida que la inscripción exista
            if (asistente == null) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS00127, new ArrayList());
            }
            //Realiza la tarea
            realizarTareaInscribirseInvitado(asistente.getInscripcion().getId(), asistente.getId());


            //Actualiza la información
            InscripcionAsistente insAsist = getInscripcionAsistenteFacade().find(Long.parseLong(idInscripcionUser.trim()));
            Secuencia secOtros = secInfoInscripcion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OTROS));
            if (secOtros != null) {
                insAsist.setOtros(secOtros.getValor());
            }
            insAsist.setInscrito(asiste);
            if (asiste.equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_CONFIRMADO))) {
                informarNuevoInscrito(insAsist);
            }
            getInscripcionAsistenteFacade().edit(insAsist);

            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public String darInscripcionUsuarioPorId(String xml) {
        try {
            getParser().leerXML(xml);

            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INVITADO));
            InscripcionAsistente asistente = getInscripcionAsistenteFacade().find(secId.getValorLong());
            //Valida que la inscripción exista
            if (asistente == null) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_INS00127, new ArrayList());
            }
            Secuencia secInscripcionesPadreUser = getConversor().consultarSecuenciaAPartirDeInscripcionUsuario(asistente);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secInscripcionesPadreUser);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_INSCRIPCION_USUARIO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_INSCRIPCION_USUARIO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Envía un correo informando al correo de notificación de la inscripción que hay un nuevo inscrito
     * @param inscripcionAsistente Inscripción asistente
     */
    private void informarNuevoInscrito(InscripcionAsistente inscripcionAsistente) {
        Persona persona = inscripcionAsistente.getPersona();
        String nombreInscripcion = (inscripcionAsistente.getInscripcion() != null && inscripcionAsistente.getInscripcion().getNombreInscripcion() != null) ? inscripcionAsistente.getInscripcion().getNombreInscripcion() : "";
        String correoNotificacion = (inscripcionAsistente.getInscripcion() != null && inscripcionAsistente.getInscripcion().getCorreoNotificacion() != null) ? inscripcionAsistente.getInscripcion().getCorreoNotificacion() : "";

        String mensaje = Notificaciones.MENSAJE_NUEVO_INSCRITO;
        mensaje = mensaje.replaceFirst("%", (persona.getNombres() != null) ? persona.getNombres() : "");
        mensaje = mensaje.replaceFirst("%", (persona.getApellidos() != null) ? persona.getApellidos() : "");
        mensaje = mensaje.replaceFirst("%", (persona.getCorreo() != null) ? persona.getCorreo() : "");
        mensaje = mensaje.replaceFirst("%", nombreInscripcion);

        String asunto = Notificaciones.ASUNTO_NUEVO_INSCRITO;
        getCorreoBean().enviarMail(correoNotificacion, asunto, null, null, null, mensaje);
    }

    public String darInscripciones(String xml) {
        try {
            getParser().leerXML(xml);

            Collection<Inscripcion> listaInscripciones = getInscripcionFacade().findAll();
            Secuencia secInfoInscripciones = getConversor().crearSecuenciaAPartirDeInscripciones(listaInscripciones);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secInfoInscripciones);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(InscripcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    private InscripcionAsistenteFacadeLocal getInscripcionAsistenteFacade() {
        return inscripcionAsistenteFacade;
    }

    private InscripcionFacadeLocal getInscripcionFacade() {
        return inscripcionFacade;
    }

    private TareaMultipleRemote getTareaBean() {
        return tareaBean;
    }

    private TimerGenericoBeanRemote getTimerGenerico() {
        return timerGenerico;
    }

    private TareaSencillaFacadeRemote getTareaSencillaFacade() {
        return tareaSencillaFacade;
    }

    private TareaSencillaRemote getTareaSencillaBean() {
        return tareaSencillaBean;
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    public PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private void realizarTareaInscribirseInvitado(Long idInscripcion, Long idAsistente) {
        String tipo = getConstanteBean().getConstante((Constantes.TAG_PARAM_TIPO_INSCRIBIRSE));
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_INSCRIPCION)), idInscripcion.toString());
        params.put(getConstanteBean().getConstante((Constantes.TAG_PARAM_ID_INVITADO)), idAsistente.toString());
        TareaSencilla tarea = getTareaSencillaFacade().findById(getTareaBean().consultarIdTareaPorParametrosTipo(tipo, params));
        if (tarea != null) {
            getTareaSencillaBean().realizarTareaPorId(tarea.getId());
        }
    }

    private void eliminarTimersInscripcion(Long id) {
         String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_CREAR_INSCRIPCION) + "-" + id.toString();
         timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociado);

    }
}
