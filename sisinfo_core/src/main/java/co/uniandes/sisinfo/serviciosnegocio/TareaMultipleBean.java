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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.bo.TareaPendienteVencidaBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.AlertaMultiple;
import co.uniandes.sisinfo.entities.Periodicidad;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.serviciosfuncionales.AlertaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeLocal;

/**
 * Servicio de negocio: Administración de tareas
 */
@Stateless
@EJB(name = "TareaMultipleBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.TareaMultipleLocal.class)
public class TareaMultipleBean implements TareaMultipleLocal {

    private final static String DIRECCION_INTERFAZ = "co.uniandes.sisinfo.serviciosnegocio.TareaMultipleLocal";

    private final static String METODO_MANEJO_TIMER_TAREAS_VENCIDAS = "manejoTimerTareasVencidas";



    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * TareaFacade
     */
    @EJB
    private TareaMultipleFacadeLocal tareaMultipleFacade;
    /**
     * ResponsableFacade
     */
    @EJB
    private PersonaFacadeLocal personaFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private AlertaMultipleFacadeLocal alertaMultipleFacade;
    @EJB
    private AlertaMultipleLocal alertaMultipleBean;
    @EJB
    private RolFacadeLocal rolFacade;
    @EJB
    private TareaSencillaFacadeLocal tareaSencillaFacade;
    @EJB
    private CorreoLocal correoBean;
    @EJB
    private TimerGenericoBeanLocal timerGenericoBean;
    @EJB
    private PeriodicidadFacadeLocal periodicidadFacade;
    private ServiceLocator serviceLocator;
    private ConversorTareaMultiple conversorTareaMultiple;

    public TareaMultipleBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            personaFacade = (PersonaFacadeLocal) serviceLocator.getLocalEJB(PersonaFacadeLocal.class);
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            correoBean = (CorreoLocal) serviceLocator.getLocalEJB(CorreoLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public void crearTareaPersona(String mensaje, String tipo, String correo, boolean agrupable, String header, String footer, Timestamp fechaInicio, Timestamp fechaFin, String comando, String nombreRol, HashMap<String, String> parametros, String asunto) {
        crearTareaPersona(mensaje, mensaje, tipo, correo, agrupable, header, footer, fechaInicio, fechaFin, comando, nombreRol, parametros, asunto);
    }

    @Override
    public void crearTareaPersona(String mensajeCorreo, String mensajeDescripcion, String tipo, String correo, boolean agrupable, String header, String footer, Timestamp fechaInicio, Timestamp fechaFin, String comando, String nombreRol, HashMap<String, String> parametros, String asunto) {
        // Primero localizo la alerta asociada al tipo de tarea especificada
        AlertaMultiple alertaM = alertaMultipleFacade.findByTipo(tipo);
        if (alertaM == null) {
            alertaM = alertaMultipleBean.crearAlertaAutomatica(tipo, comando);
        }

        if (!agrupable) {
            // Si no es agrupable, se debe crear la tareaMultiple directamente
            TareaMultiple tm = crearTareaMultiple(tipo, fechaFin, fechaInicio, footer, header, nombreRol, correo, comando, asunto);
            TareaSencilla ts = crearTareaSencilla(mensajeCorreo, mensajeDescripcion, parametros, tipo);
            Collection<TareaSencilla> tareasSencillas = new Vector();
            tareasSencillas.add(ts);
            tm.setTareasSencillas(tareasSencillas);

            Collection<TareaMultiple> tareasMultiples = alertaM.getTareas();
            tareasMultiples.add(tm);

            alertaM.setTareas(tareasMultiples);
            alertaMultipleFacade.edit(alertaM);
        } else {

            // Se busca si existe una tarea multiple para la persona especificado
            Iterator<TareaMultiple> iterator = tareaMultipleFacade.findByCorreoYTipo(tipo, correo).iterator();

            TareaMultiple tm;
            if (!iterator.hasNext()) {

                // Si la tarea multiple no existe, debe ser creada
                tm = crearTareaMultiple(tipo, fechaFin, fechaInicio, footer, header, nombreRol, correo, comando, asunto);

                Collection<TareaMultiple> tareasMultiples = alertaM.getTareas();
                tareasMultiples.add(tm);
                alertaM.setTareas(tareasMultiples);
                alertaMultipleFacade.edit(alertaM);
            } else {

                tm = iterator.next();
                // Se actualiza la fecha de caducacion en caso de ser necesario
                System.out.println(this.getClass() + "-" + fechaFin);
                if (fechaFin.after(tm.getFechaCaducacion())) {

                    tm.setFechaCaducacion(fechaFin);
                }
                tm.setAsunto(asunto);
                tm.setHeader(header);
                tm.setFooter(footer);
            }
            TareaSencilla ts = encontrarTareaSencillaPorParametros(tm, parametros);
            if (ts == null) {
                //Crea la tarea sencilla y la asocia a la tarea multiple
                ts = crearTareaSencilla(mensajeCorreo, mensajeDescripcion, parametros, tipo);
                Collection<TareaSencilla> tareasSencillas = tm.getTareasSencillas();
                tareasSencillas.add(ts);
                tm.setTareasSencillas(tareasSencillas);

                tareaMultipleFacade.edit(tm);
            } else {
                // Modifica la informacion existente de la tarea (esta tarea ya se encuentra asociada
                // a la tarea multiple
                ts.setMensajeCorreo(mensajeCorreo);
                ts.setMensajeDescripcion(mensajeDescripcion);
                ts.setEstado(asunto);
                ts.setEstado(constanteBean.getConstante(Constantes.ESTADO_TAREA_PENDIENTE));
                tareaSencillaFacade.edit(ts);
            }

        }

    }

    private TareaSencilla encontrarTareaSencillaPorParametros(TareaMultiple tm, HashMap<String, String> params) {
        for (TareaSencilla ts : tm.getTareasSencillas()) {
            if (tareaTieneParametros(ts, params)) {
                return ts;
            }
        }
        return null;
    }

    private boolean tareaTieneParametros(TareaSencilla ts, HashMap<String, String> params) {
        Collection<Parametro> parametros = ts.getParametros();
        for (String key : params.keySet()) {
            boolean found = false;
            for (Parametro parametro : parametros) {
                if (parametro.getCampo().equals(key) && parametro.getValor().equals(params.get(key))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void crearTareaRol(String mensaje, String tipo, String nombreRol, boolean agrupable, String header, String footer, Timestamp fechaInicio, Timestamp fechaFin, String comando, HashMap<String, String> parametros, String asunto) {
        crearTareaRol(mensaje, mensaje, tipo, nombreRol, agrupable, header, footer, fechaInicio, fechaFin, comando, parametros, asunto);
    }

    @Override
    public void crearTareaRol(String mensajeCorreo, String mensajeDescripcion, String tipo, String nombreRol, boolean agrupable, String header, String footer, Timestamp fechaInicio, Timestamp fechaFin, String comando, HashMap<String, String> parametros, String asunto) {
        // Primero localizo la alerta asociada al tipo de tarea especificada
        AlertaMultiple alertaM = alertaMultipleFacade.findByTipo(tipo);

        if (alertaM == null) {
            alertaM = alertaMultipleBean.crearAlertaAutomatica(tipo, comando);
        }

        if (!agrupable) {
            // Si no es agrupable, se debe crear la tareaMultiple directamente
            TareaMultiple tm = crearTareaMultiple(tipo, fechaFin, fechaInicio, footer, header, nombreRol, null, comando, asunto);

            TareaSencilla ts = crearTareaSencilla(mensajeCorreo, mensajeDescripcion, parametros, tipo);
            Collection<TareaSencilla> tareasSencillas = new Vector();
            tareasSencillas.add(ts);
            tm.setTareasSencillas(tareasSencillas);

            Collection<TareaMultiple> tareasMultiples = alertaM.getTareas();
            tareasMultiples.add(tm);

            alertaM.setTareas(tareasMultiples);
            alertaMultipleFacade.edit(alertaM);
        } else {
            // Se busca si existe una tarea multiple para la persona especificado
            Iterator<TareaMultiple> iterator = tareaMultipleFacade.findByRolYTipo(tipo, nombreRol).iterator();

            TareaMultiple tm;
            if (!iterator.hasNext()) {
                // Si la tarea multiple no existe, debe ser creada
                tm = crearTareaMultiple(tipo, fechaFin, fechaInicio, footer, header, nombreRol, null, comando, asunto);

                Collection<TareaMultiple> tareasMultiples = alertaM.getTareas();
                tareasMultiples.add(tm);
                alertaM.setTareas(tareasMultiples);
                alertaMultipleFacade.edit(alertaM);
            } else {
                tm = iterator.next();
                // Se actualiza la fecha de caducacion en caso de ser necesario
                System.out.println(this.getClass() + "-" + fechaFin);
                if (fechaFin.after(tm.getFechaCaducacion())) {
                    tm.setFechaCaducacion(fechaFin);
                }

            }

            TareaSencilla ts = encontrarTareaSencillaPorParametros(tm, parametros);
            if (ts == null) {
                //Crea la tarea sencilla y la asocia a la tarea multiple
                ts = crearTareaSencilla(mensajeCorreo, mensajeDescripcion, parametros, tipo);
                Collection<TareaSencilla> tareasSencillas = tm.getTareasSencillas();
                tareasSencillas.add(ts);
                tm.setTareasSencillas(tareasSencillas);

                tareaMultipleFacade.edit(tm);
            } else {
                // Modifica la informacion existente de la tarea (esta tarea ya se encuentra asociada
                // a la tarea multiple
                ts.setMensajeCorreo(mensajeCorreo);
                ts.setMensajeDescripcion(mensajeDescripcion);
                ts.setEstado(asunto);
                ts.setEstado(constanteBean.getConstante(Constantes.ESTADO_TAREA_PENDIENTE));
                tareaSencillaFacade.edit(ts);
            }

        }

    }

    private TareaMultiple crearTareaMultiple(String tipo, Timestamp fechaFin, Timestamp fechaInicio, String footer, String header, String nombreRol, String correo, String comando, String asunto) {
        TareaMultiple tm = new TareaMultiple();
        tm.setFechaCaducacion(fechaFin);
        tm.setFechaInicio(fechaInicio);
        tm.setFooter(footer);
        tm.setHeader(header);
        tm.setAsunto(asunto);
        if (correo != null) {
            Persona persona = personaFacade.findByCorreo(correo);
            tm.setPersona(persona);
        } else {
            tm.setPersona(null);
        }
        tm.setPersonal(correo != null);
        Rol rol = rolFacade.findByRol(nombreRol);
        tm.setRol(rol);
        tm.setTareasSencillas(new ArrayList<TareaSencilla>());
        tm.setComando(comando);
        tm.setTipo(tipo);
        tareaMultipleFacade.create(tm);
        return tm;
    }

    private TareaSencilla crearTareaSencilla(String mensajeCorreo, String mensajeDescripcion, HashMap<String, String> parametros, String tipo) {
        TareaSencilla ts = new TareaSencilla();
        ts.setEstado(constanteBean.getConstante(Constantes.ESTADO_TAREA_PENDIENTE));
        ts.setMensajeCorreo(mensajeCorreo);
        ts.setMensajeDescripcion(mensajeDescripcion);
        Collection<Parametro> params = new ArrayList();
        for (String key : parametros.keySet()) {
            Parametro p = new Parametro();
            p.setCampo(key);
            p.setValor(parametros.get(key));
            params.add(p);
        }
        ts.setParametros(params);
        ts.setTipo(tipo);
        tareaSencillaFacade.create(ts);
        return ts;
    }

    public void realizarTareaPorId(Long id) {
        TareaSencilla tarea = tareaSencillaFacade.findById(id);
        tarea.setEstado(constanteBean.getConstante(Constantes.ESTADO_TAREA_TERMINADA));
        tareaSencillaFacade.edit(tarea);
    }

    
    public void realizarTareaPorCorreo(String tipo, String correo, HashMap<String, String> params) {
        Collection<TareaMultiple> tareas = tareaMultipleFacade.findByCorreoYTipo(tipo, correo);

        for (TareaMultiple tareaMultiple : tareas) {
            Collection<TareaSencilla> tareasSencilla = tareaMultiple.getTareasSencillas();
            for (TareaSencilla tareaSencilla : tareasSencilla) {
                Collection<Parametro> keys = tareaSencilla.getParametros();
                boolean realizar = true;
                for (Parametro parametro : keys) {
                    if (!parametro.getValor().equals(params.get(parametro.getCampo()))) {
                        realizar = false;
                        break;
                    }
                }
                if (realizar) {
                    tareaSencilla.setEstado(constanteBean.getConstante(Constantes.ESTADO_TAREA_TERMINADA));
                }
            }
        }
    }

    @Override
    public String darTareasCorreoEstado(String root) {
        try {
            getParser().leerXML(root);
            String rol = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL)).getValor();
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String estado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)).getValor();
            Persona responsable = personaFacade.findByCorreo(correo);
            if (responsable == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0103, new LinkedList<Secuencia>());
            } else {
                Collection<TareaMultiple> tareasResponsable = tareaMultipleFacade.findByCorreo(responsable.getCorreo());
                Collection<TareaMultiple> tareasPorRol = tareaMultipleFacade.findByRolNoPersonal(rol);
                tareasResponsable.addAll(tareasPorRol);
                return retornarTareasCorreoEstado(tareasResponsable, estado, rol);
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0072, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private String retornarTareasCorreoEstado(Collection<TareaMultiple> tareas, String estado, String rol) throws Exception {
        Collection<TareaMultiple> tareasCorreoEstado = new LinkedList<TareaMultiple>();
        Iterator<TareaMultiple> iterator = tareas.iterator();
        while (iterator.hasNext()) {
            TareaMultiple tarea = iterator.next();
            Rol r = tarea.getRol();
            if (calcularEstadoTarea(tarea).equals(estado) && r.getRol().equals(rol)) {
                tareasCorreoEstado.add(tarea);
            }
        }
        Collection<Secuencia> secuencias = getConversorTareaMultiple().getSecuenciasTareasEstado(tareasCorreoEstado, estado);
        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0059, new LinkedList<Secuencia>());
    }

    @Override
    public String darTareasCorreoEstadoTipo(String root) {
        try {
            getParser().leerXML(root);
            String rol = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL)).getValor();
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String estado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)).getValor();
            String tipo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TAREA)).getValor();

            Persona responsable = personaFacade.findByCorreo(correo);
            if (responsable == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0103, new LinkedList<Secuencia>());
            } else {
                Collection<TareaMultiple> tareasResponsable = tareaMultipleFacade.findByCorreoYTipo(tipo, responsable.getCorreo());
                Collection<TareaMultiple> tareasPorRol = tareaMultipleFacade.findByRolYTipoNoPersonal(tipo, rol);
                tareasResponsable.addAll(tareasPorRol);
                return retornarTareasCorreoEstado(tareasResponsable, estado, rol);
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0072, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public long consultarIdTareaPorParametrosTipo(String tipo, Properties params) {
        Collection<TareaSencilla> listaTareas = tareaSencillaFacade.findByTipo(tipo);
        TareaSencilla tarea = consultarTareaPorParametros(listaTareas, params);
        return tarea != null ? tarea.getId() : -1;
    }

    public TareaSencilla consultarTareaPorParametrosTipo(String tipo, Properties params) {
        Collection<TareaSencilla> listaTareas = tareaSencillaFacade.findByTipo(tipo);
        TareaSencilla tarea = consultarTareaPorParametros(listaTareas, params);
        return tarea;
    }

    private TareaSencilla consultarTareaPorParametros(Collection<TareaSencilla> tareas, Properties parametros) {
        Iterator<TareaSencilla> iteradorTareas = tareas.iterator();
        TareaSencilla tareaBuscada = null;
        while (iteradorTareas.hasNext()) {
            TareaSencilla tarea = iteradorTareas.next();
            boolean encontrada = esTareaBuscada(tarea, parametros);
            if (encontrada) {
                tareaBuscada = tarea;
            }
        }
        return tareaBuscada;
    }

    @Override
    public String consultarIdTareaPorParametrosTipo(String xmlComando) {
        try {
            getParser().leerXML(xmlComando);
            String tipoTarea = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TAREA)).getValor();
            Secuencia paramsTarea = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PARAMETROS_TAREA));
            Properties listaParametros = getConversorTareaMultiple().convertirSecuenciaAParametros(paramsTarea);

            Collection<TareaSencilla> listaTareas = tareaSencillaFacade.findByTipo(tipoTarea);
            if (listaTareas != null && listaTareas.size() != 0) {
                TareaSencilla tareaBuscada = consultarTareaPorParametros(listaTareas, listaParametros);

                if (tareaBuscada == null) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ID_TAREA_PARAMETROS_TIPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0128, new LinkedList<Secuencia>());
                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    Secuencia secIdTarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TAREA), "" + tareaBuscada.getId());
                    secuencias.add(secIdTarea);

                    //Generamos una respuesta
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ID_TAREA_PARAMETROS_TIPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0160, new LinkedList<Secuencia>());
                }
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ID_TAREA_PARAMETROS_TIPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0127, new LinkedList<Secuencia>());
            }

        } catch (Exception ex) {
            Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean esTareaBuscada(TareaSencilla tarea, Properties listaParametros) {
        boolean esTareaBuscada = false;
        int cantidadParametros = listaParametros.size();
        Collection<Parametro> parametros = tarea.getParametros();
        Iterator<Parametro> iteradorTarea = parametros.iterator();
        int contador = 0;
        boolean continuar = true;
        while (iteradorTarea.hasNext() && continuar) {
            Parametro parametro = iteradorTarea.next();
            if (listaParametros.containsKey(parametro.getCampo().toString()) && listaParametros.getProperty(parametro.getCampo()) != null && listaParametros.getProperty(parametro.getCampo()).equals(parametro.getValor())) {
                contador++;
            } else {
                continuar = false;
            }
        }
        if (contador == cantidadParametros) {
            esTareaBuscada = true;
        }
        return esTareaBuscada;
    }

    @Override
    public String consultarTareaId(String root) {
        try {
            getParser().leerXML(root);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TAREA)).getValor();
            TareaSencilla tarea = tareaSencillaFacade.findById(Long.parseLong(id));
            if (tarea == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREA_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0081, new LinkedList<Secuencia>());
            } else {
                Collection<TareaMultiple> tareasMultiples = tareaMultipleFacade.findByTipo(tarea.getTipo());
                TareaMultiple tareaMultiple = null;
                for (TareaMultiple tareaM : tareasMultiples) {
                    Collection<TareaSencilla> tareasS = tareaM.getTareasSencillas();
                    for (TareaSencilla tareaSencilla : tareasS) {
                        if (tareaSencilla.getId().toString().equals(id)) {
                            tareaMultiple = tareaM;
                        }
                        if (tareaMultiple != null) {
                            break;
                        }
                    }
                    if (tareaMultiple != null) {
                        break;
                    }
                }
                Secuencia secuenciaTarea = getConversorTareaMultiple().getSecuenciaTarea(tareaMultiple, tarea);
                Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
                secuencias.add(secuenciaTarea);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREA_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0066, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREA_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0080, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String calcularEstadoTarea(TareaMultiple tm) {
        if (tm.getFechaCaducacion().before(new Timestamp(System.currentTimeMillis()))) {
            return constanteBean.getConstante(Constantes.ESTADO_TAREA_VENCIDA);
        }
        Collection<TareaSencilla> tareasSencilla = tm.getTareasSencillas();
        boolean pendiente = false;
        for (TareaSencilla tareaSencilla : tareasSencilla) {
            if (tareaSencilla.getEstado().equals(constanteBean.getConstante(Constantes.ESTADO_TAREA_PENDIENTE))) {
                pendiente = true;
                break;
            }
        }
        if (pendiente) {
            return constanteBean.getConstante(Constantes.ESTADO_TAREA_PENDIENTE);
        } else {
            return constanteBean.getConstante(Constantes.ESTADO_TAREA_TERMINADA);
        }
    }

    @Override
    public String darTareasCorreoEstadoSinCaducar(String xmlComando) {
        try {
            getParser().leerXML(xmlComando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String estado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)).getValor();
            String rol = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL)).getValor();
            Persona responsable = personaFacade.findByCorreo(correo);
            if (responsable == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO_SIN_CADUCAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0103, new LinkedList<Secuencia>());
            } else {
                List<TareaMultiple> tareas = tareaMultipleFacade.findByCorreoYFechaAntesDeCaducacion(correo, new Timestamp(System.currentTimeMillis()), rol);
                List<TareaMultiple> tareasNoPersonales = tareaMultipleFacade.findByRolNoPersonalYFechaAntesDeCaducacion(new Timestamp(System.currentTimeMillis()), rol);
                tareas.addAll(tareasNoPersonales);
                Collection<Secuencia> secuencias = getConversorTareaMultiple().getSecuenciasTareasEstado(tareas, estado);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO_SIN_CADUCAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO_SIN_CADUCAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darParametrosTipoTarea(String xmlComando) {
        try {
            getParser().leerXML(xmlComando);
            String tipoTarea = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TAREA)).getValor();

            Collection<TareaMultiple> tareas = tareaMultipleFacade.findByTipo(tipoTarea);
            Iterator<TareaMultiple> iterator = tareas.iterator();
            //Si la tarea no existe se retorna un mensaje de error
            if (!iterator.hasNext()) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_PARAMETROS_TIPO_TAREA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0127, new LinkedList<Secuencia>());
            } //De lo contrario se extraen los parámetros de la tarea y se retornan
            else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                Secuencia secParametrosTarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PARAMETROS_TAREA), "");

                TareaMultiple tarea = iterator.next();
                Collection<Parametro> listaParametros = tarea.getTareasSencillas().iterator().next().getParametros();
                Iterator<Parametro> iterador = listaParametros.iterator();
                while (iterador.hasNext()) {
                    Parametro parametro = iterador.next();
                    String campo = parametro.getCampo();
                    Secuencia secParametroTarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PARAMETRO_TAREA), "" + campo);
                    secParametrosTarea.agregarSecuencia(secParametroTarea);
                }
                secuencias.add(secParametrosTarea);

                //Generamos una respuesta
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PARAMETROS_TIPO_TAREA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0159, new LinkedList<Secuencia>());
            }
        } catch (Exception ex) {
            Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retorna el correo que sera enviado por una tarea dado su tipo. Este método
     * debe ser usado si se esta buscando el correo de una tarea agrupable.
     * @param tipo
     * @return
     */
    public String darMensajeTareaPersonalPorTipoYCorreo(String tipo, String correo) {
        return obtenerMensajeTareaCorreo(tareaMultipleFacade.findByCorreoYTipo(tipo, correo).iterator().next(), getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE));
    }

    public String darMensajeTareaNoPersonalPorTipoYRol(String tipo, String rol) {
        return obtenerMensajeTareaCorreo(tareaMultipleFacade.findByRolYTipo(tipo, rol).iterator().next(), getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE));
    }

    /**
     * Retorna el correo que sera enviado por una tarea dado su tipo y los
     * parametros. Este método debe ser usado si se esta buscando el correo
     * de una tarea no agrupable
     * @param tipo Tipo de la tarea
     * @param parametros Parametros de la tarea
     * @return
     */
    public String darCorreoTareaPorTipoYParametros(String tipo, HashMap<String, String> parametros) {
        Collection<TareaMultiple> tareas = tareaMultipleFacade.findByTipo(tipo);
        Set<String> keys = parametros.keySet();
        for (TareaMultiple tareaMultiple : tareas) {
            for (TareaSencilla tareaS : tareaMultiple.getTareasSencillas()) {
                boolean valid = true;
                if (!tareaS.getEstado().equals(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE))) {
                    continue;
                }
                for (Parametro param : tareaS.getParametros()) {
                    for (String string : keys) {
                        if (!string.equals(param)) {
                            valid = false;
                            break;
                        }
                    }
                    if (!valid) {
                        break;
                    }
                }
                if (valid) {
                    return obtenerMensajeTareaCorreo(tareaMultiple, getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE));
                }
            }

        }
        return null;
    }

    @Override
    public String obtenerMensajeTareaCorreo(TareaMultiple tm, String estado) {
        Collection<TareaSencilla> ts = tm.getTareasSencillas();
        String mensaje = "<ul>";
        for (TareaSencilla tareaSencilla : ts) {
            if (tareaSencilla.getEstado().equals(estado) && tareaSencilla.getMensajeCorreo() != null) {
                mensaje += "<li>" + tareaSencilla.getMensajeCorreo() + "<br />";
            }
        }
        mensaje += "</ul>";
        return mensaje;
    }

    @Override
    public String obtenerMensajeTareaDescripcion(TareaMultiple tm, String estado) {
        Collection<TareaSencilla> ts = tm.getTareasSencillas();
        String mensaje = "<ul>";
        for (TareaSencilla tareaSencilla : ts) {
            if (tareaSencilla.getEstado().equals(estado) && tareaSencilla.getMensajeDescripcion() != null) {
                mensaje += "<li>" + tareaSencilla.getMensajeDescripcion() + "<br />";
            }
        }
        mensaje += "</ul>";
        return mensaje;
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    public ConversorTareaMultiple getConversorTareaMultiple() {
//        if (conversorTareaMultiple == null) {
//            conversorTareaMultiple = new ConversorTareaMultiple(constanteBean, alertaMultipleFacade, this);
//        }
        return conversorTareaMultiple;
    }

    public void realizarTareaPorRol(String tipo, String rol, java.util.HashMap<String, String> params) {
        Collection<TareaMultiple> tareas = tareaMultipleFacade.findByRolYTipo(tipo, rol);

        for (TareaMultiple tareaMultiple : tareas) {
            Collection<TareaSencilla> tareasSencilla = tareaMultiple.getTareasSencillas();
            for (TareaSencilla tareaSencilla : tareasSencilla) {
                Collection<Parametro> keys = tareaSencilla.getParametros();
                boolean realizar = true;
                for (Parametro parametro : keys) {
                    if (!parametro.getValor().equals(params.get(parametro.getCampo()))) {
                        realizar = false;
                        break;
                    }
                }
                if (realizar) {
                    tareaSencilla.setEstado(constanteBean.getConstante(Constantes.ESTADO_TAREA_TERMINADA));
                }
            }
        }
    }

    /**
     * Retorna un comandoXML con el historial de tareas de una persona dado un login
     * @param comandoXML: contiene el login por el cual se buscan las tareas
     * @return el historial de tareas dado el login
     */
    public String darTareaCorreo(String comandoXML) {


        try {

            getParser().leerXML(comandoXML);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Persona responsable = personaFacade.findByCorreo(correo);
            if (responsable == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0103, new LinkedList<Secuencia>());
            } else {
                Collection<TareaMultiple> tareasResponsable = tareaMultipleFacade.findByCorreo(responsable.getCorreo());

                Collection<Secuencia> secuencias = getConversorTareaMultiple().getSecuenciasHistorialTareas(tareasResponsable);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0059, new LinkedList<Secuencia>());

            }
        } catch (Exception e) {
            try {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0072, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public void manejoTimerTareasVencidas(String infoTimer){
        String estadoPendienteVencida = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE_VENCIDA);
        String estadoVencida = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_VENCIDA);
        // Consulto las tareas multiples que tienen una o mas tareas en estado pendiente_vencida
        Collection<TareaMultiple> tareasPendientes = tareaMultipleFacade.findByEstadoTareaSencilla(estadoPendienteVencida);
        // Se inicializa la tabla donde se van a agrupar las tareas de acuerdo al correo
        HashMap<String,ArrayList> mapaCorreos = new HashMap();
        // Se inicializa una tabla con las alertas utilizadas para evitar consultas adicionales a la BD
        HashMap<String,AlertaMultiple> mapaAlertas = new HashMap();
        for (TareaMultiple tareaMultiple : tareasPendientes) {

            Collection<TareaSencilla> tareasSencillas = tareaMultiple.getTareasSencillas();

            // Agrego la alerta a la tabla si no la he agregado aun
            if(mapaAlertas.get(tareaMultiple.getTipo())==null){
                AlertaMultiple am = alertaMultipleFacade.findByTipo(tareaMultiple.getTipo());
                mapaAlertas.put(am.getTipo(), am);
            }
            for (TareaSencilla tareaSencilla : tareasSencillas) {
                // Solo debo procesas las tareas sencillas en estado pendiente vencida
                if(tareaSencilla.getEstado().equals(estadoPendienteVencida)){
                    if(tareaSencilla.getEjecucionesPendientes()!=null && tareaSencilla.getEjecucionesPendientes()>0){
                        // Si la tarea aun tiene ejecuciones pendientes entonces la agrego a la lista de tareas para el usuario
                        // y disminuyo el numero de ejecuciones pendientes en 1
                        ArrayList lista= mapaCorreos.get(tareaMultiple.getPersona().getCorreo());
                        boolean agregada = false;
                        if(lista==null)
                            lista = new ArrayList();
                        else{
                            // Se revisa que la tarea no haya sido agregada a la lista de
                            // tareas para ese usuario
                            for (Object object : lista) {
                                TareaSencilla ts = (TareaSencilla)object;
                                if(tareaSencilla.getId().equals(ts.getId())){
                                    agregada = true;
                                    break;
                                }
                            }
                        }
                        if(!agregada){
                            lista.add(tareaSencilla);
                            mapaCorreos.put(tareaMultiple.getPersona().getCorreo(), lista);
                            tareaSencilla.setEjecucionesPendientes(tareaSencilla.getEjecucionesPendientes()-1);
                        }
                    }else{
                        // Si la tarea no tiene ejecuciones pendientes entonces la marco como vencida
                        tareaSencilla.setEstado(estadoVencida);
                    }
                }
            }
            tareaMultipleFacade.edit(tareaMultiple);
        }

        // Obtengo la lista de correos que tienen una o mas tareas en estado pendiente vencida
        // con ejecuciones pendientes
        Collection<String> correos = mapaCorreos.keySet();
        for (String correo : correos) {
            // Construyo un mensaje para el usuario con la información de todas sus tareas en estado pendiente vigente
            ArrayList<TareaSencilla> tareasSencillas= mapaCorreos.get(correo);
            String mensaje = "<ul>";
            for (TareaSencilla tareaSencilla : tareasSencillas) {
                AlertaMultiple alertaTarea = mapaAlertas.get(tareaSencilla.getTipo());
                mensaje+="<li>"+alertaTarea.getNombre()+"  -  "+tareaSencilla.getMensajeCorreo()+"</li>";
            }
            mensaje+="</ul>";
            String mensajeCorreo = String.format(Notificaciones.MENSAJE_TAREAS_VENCIDAS,mensaje);
            String asunto = Notificaciones.ASUNTO_TAREAS_VENCIDAS;
            getCorreoBean().enviarMail(correo, asunto, null, null, null, mensajeCorreo);
        }
        // Regenero el timer de manejo de tareas
        crearTimerManejoTareasVencidas();
    }
  
    public CorreoLocal getCorreoBean() {
        return correoBean;
    }

    /**
     * Retorna las tareas vencidas de un usuario
     * @param xml
     * @return
     */
    public String darTareasVencidasUsuario(String xml) {
        try {

            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String rol = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL)).getValor();
            String estadoTareaPendienteVencida = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE_VENCIDA);
            Collection<TareaMultiple> tareasMultiples = tareaMultipleFacade.findByCorreoRolAndEstadoTareaSencilla(estadoTareaPendienteVencida,correo,rol);
            tareasMultiples.addAll(tareaMultipleFacade.findByRolAndEstadoTareaSencillaNoPersonal(estadoTareaPendienteVencida,rol));
            ArrayList<TareaPendienteVencidaBO> tareasVencidas = new ArrayList();
            HashMap<String,AlertaMultiple> mapaAlertas = new HashMap();
            for (TareaMultiple tareaMultiple : tareasMultiples) {
                Collection<TareaSencilla> tareasSencillas = tareaMultiple.getTareasSencillas();
                AlertaMultiple am = mapaAlertas.get(tareaMultiple.getTipo());
                if(am==null){
                    am = alertaMultipleFacade.findByTipo(tareaMultiple.getTipo());
                    mapaAlertas.put(am.getTipo(), am);
                }
                for (TareaSencilla tareaSencilla : tareasSencillas) {
                    if(tareaSencilla.getEstado().equals(estadoTareaPendienteVencida)){
                        String msj = am.getNombre()+" - "+tareaSencilla.getMensajeDescripcion();

                        TareaPendienteVencidaBO tarea = new TareaPendienteVencidaBO(tareaSencilla.getId(), tareaSencilla.getTipo(), msj, tareaMultiple.getComando(),tareaSencilla.getParametros());
                        tareasVencidas.add(tarea);
                    }
                }
            }
            Secuencia secuenciaTareasPendientes = getConversorTareaMultiple().getSecuenciasTareasPendientes(tareasVencidas);
            ArrayList<Secuencia> secuencias = new ArrayList();
            secuencias.add(secuenciaTareasPendientes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TAREAS_PENDIENTES_VENCIDAS_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0072, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(TareaMultipleBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public void crearTimerManejoTareasVencidas() {
        Long id = timerGenericoBean.darIdTimer(DIRECCION_INTERFAZ, METODO_MANEJO_TIMER_TAREAS_VENCIDAS, "Manejo tareas vencidas");
        if(id!=null && id>0)
            timerGenericoBean.eliminarTimer(id);
        Periodicidad p = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_TAREAS_VENCIDAS));
        long valorP = 1209600000L;
        if(p!=null){
            valorP = p.getValor();
        }
        Timestamp timestampFin = new Timestamp(System.currentTimeMillis()+valorP);
        timerGenericoBean.crearTimer2(DIRECCION_INTERFAZ, METODO_MANEJO_TIMER_TAREAS_VENCIDAS, timestampFin, "Manejo tareas vencidas", "ServiciosSoporteProcesos","TareaMultipleBean", "crearTimerManejoTareasVencidas", "Timer utilizado para manejar las notificaciones de las tareas vencidas");
    }


    public void regenerarTimerManejoTareasVencidas() {
        Long id = timerGenericoBean.darIdTimer(DIRECCION_INTERFAZ, METODO_MANEJO_TIMER_TAREAS_VENCIDAS, "Manejo tareas vencidas");
        if(id!=null && id>0)
            return;
        Periodicidad p = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_TAREAS_VENCIDAS));
        long valorP = 1209600000L;
        if(p!=null){
            valorP = p.getValor();
        }
        Timestamp timestampFin = new Timestamp(System.currentTimeMillis()+valorP);
        timerGenericoBean.crearTimer2(DIRECCION_INTERFAZ, METODO_MANEJO_TIMER_TAREAS_VENCIDAS, timestampFin, "Manejo tareas vencidas", "ServiciosSoporteProcesos","TareaMultipleBean", "crearTimerManejoTareasVencidas", "Timer utilizado para manejar las notificaciones de las tareas vencidas");
    }


}
