/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Aspirante;

import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.Horario_DisponibleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TimerEstudianteLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TimerProfesorLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Preselección
 */
@Stateless
@EJB(name = "PreseleccionBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.PreseleccionLocal.class)
public class PreseleccionBean implements PreseleccionRemote, PreseleccionLocal {

    private enum EstadoPreseleccion {

        PRESELECCION_EXITOSA,
        SOLICITUD_NO_ENCONTRADA,
        SECCION_SIN_VACANTES,
        CONFLICTO_HORARIO_ASPIRANTE,
        SECCION_NO_ENCONTRADA,
        SOLICITUD_NO_VALIDA
    };
    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * SolicitudFacade
     */
    @EJB
    private SolicitudFacadeLocal solicitudFacade;
    /**
     * SeccionFacade
     */
    @EJB
    private SeccionFacadeRemote seccionFacade;
    /**
     * ConsultaBean
     */
    @EJB
    private ConsultaLocal consultaBean;
    /**
     * MonitoriaFacade
     */
    @EJB
    private MonitoriaFacadeLocal monitoriaFacade;
    /**
     * ProfesorFacade
     */
    @EJB
    private ProfesorFacadeRemote profesorFacade;
    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeRemote cursoFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    /**
     * CorreoBean
     */
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private EstudianteFacadeRemote estudianteFacade;
    @EJB
    private RangoFechasBeanLocal rangoFechasBean;
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private TimerGenericoBeanRemote timerGenericoBean;
    @EJB
    private SolicitudLocal solicitudBean;
    @EJB
    private InformacionAcademicaFacadeRemote informacionAcademicaFacade;
    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de PreseleccionBean
     */
    public PreseleccionBean() {
        try {
            serviceLocator = new ServiceLocator();
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            estudianteFacade = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            timerGenericoBean = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            informacionAcademicaFacade = (InformacionAcademicaFacadeRemote) serviceLocator.getRemoteEJB(InformacionAcademicaFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(PreseleccionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String preseleccionar(String root) {
        try {
            Collection<Secuencia> secuencias;
            ConversorPreseleccion conversor = new ConversorPreseleccion(root);

            Solicitud solicitud = conversor.pasarComandoASolicitudT1();

            if (solicitud == null) {
                secuencias = conversor.darSecuenciaRespuestaSolicitudNoExiste(Long.toString(conversor.pasarComandoAIdSolicitudT1()));
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0032, secuencias);
            }
            Collection<Seccion> secciones = conversor.pasarComandoASecciones("" + solicitud.getId());

            String responsable = conversor.pasarComandoAResponsable();

            Seccion secc = secciones.iterator().next();

            if (secc == null) {
                secuencias = conversor.darSecuenciaRespuestaSolicitudNoExiste(Long.toString(solicitud.getId()));
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0032, secuencias);
            }

            String crn = secc.getCrn();
            Collection<Solicitud> solicitudesSeccion = getSolicitudFacade().findByCrnSeccion(crn);

            double cargaFinal = secc.getMaximoMonitores();

            double numMonitoresActual = 0;
            for (Solicitud solicitud1 : solicitudesSeccion) {
                Collection<MonitoriaAceptada> monitoriasAceptadas = solicitud1.getMonitorias();
                for (MonitoriaAceptada monitoriaAceptada : monitoriasAceptadas) {
                    if (monitoriaAceptada.getSeccion().getCrn().equals(crn)) {
                        if (monitoriaAceptada.getCarga() == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1))) {
                            numMonitoresActual += 1;
                        } else {
                            numMonitoresActual += 0.5;
                        }

                    }
                }
            }

            double carga;
            if (cargaFinal - numMonitoresActual >= 1) {
                carga = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1));
            } else {
                carga = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T2));
            }

            EstadoPreseleccion respuesta = preseleccionar(secciones, solicitud, responsable, carga, false);
            switch (respuesta) {
                case CONFLICTO_HORARIO_ASPIRANTE:
                    secuencias = conversor.darSecuenciaRespuestaConflictoHorario(solicitud.getEstudiante());
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0093, secuencias);
                case SECCION_SIN_VACANTES:
                    secuencias = conversor.darSecuenciaRespuestaNoHayVacantes(secc.getNumeroSeccion(), secc.getProfesorPrincipal());
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0031, secuencias);
                case SOLICITUD_NO_ENCONTRADA:
                    secuencias = conversor.darSecuenciaRespuestaSolicitudNoExiste(Long.toString(conversor.pasarComandoAIdSolicitudT1()));
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0032, secuencias);
                case SECCION_NO_ENCONTRADA:
                    secuencias = conversor.darSecuenciaRespuestaSolicitudNoExiste(Long.toString(solicitud.getId()));
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0033, secuencias);
                case SOLICITUD_NO_VALIDA:
                    secuencias = conversor.darSecuenciaRespuestaSolicitudNoExiste(Long.toString(solicitud.getId()));
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0030, secuencias);
            }

            Iterator<MonitoriaAceptada> monitorias = solicitud.getMonitorias().iterator();
            long tiempoTresDias = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_TRES_DIAS)).getValor();
            rangoFechasBean.crearTareaMonitorPreseleccionadoConfirmarSeleccion(solicitud.getId());
            rangoFechasBean.crearTareaProfesorPreseleccionarPorSeccion(secc);

            secuencias = conversor.darSecuenciaRespuestaPreseleccionar(solicitud.getEstudiante(), secc);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0027, secuencias);

        } catch (Exception ex) {
            Logger.getLogger(PreseleccionBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String preseleccionarMonitoresT2(String comando) {
        try {
            ConversorPreseleccion conversor = new ConversorPreseleccion(comando);

            Collection<Solicitud> solicitudes = conversor.pasarComandoASolicitudT2();

            String responsable = conversor.pasarComandoAResponsable();

            double carga = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T2));

            for (Solicitud solicitud : solicitudes) {
                Collection<Seccion> secciones = conversor.pasarComandoASecciones("" + solicitud.getId());
                EstadoPreseleccion respuesta = preseleccionar(secciones, solicitud, responsable, carga, true);

                switch (respuesta) {
                    case CONFLICTO_HORARIO_ASPIRANTE:
                        Collection<Secuencia> secuencias = conversor.darSecuenciaRespuestaConflictoHorario(solicitud.getEstudiante());
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_PRESELECCIONAR_MONITORES_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0093, secuencias);
                    case SECCION_SIN_VACANTES:
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_PRESELECCIONAR_MONITORES_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0031, new ArrayList());
                    case SOLICITUD_NO_ENCONTRADA:
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_PRESELECCIONAR_MONITORES_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0032, new ArrayList());
                    case SECCION_NO_ENCONTRADA:
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_PRESELECCIONAR_MONITORES_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0033, new ArrayList());
                    case SOLICITUD_NO_VALIDA:
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_PRESELECCIONAR_MONITORES_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0030, new ArrayList());
                }
                Iterator<MonitoriaAceptada> monitorias = solicitud.getMonitorias().iterator();
                long tiempoTresDias = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_TRES_DIAS)).getValor();
                rangoFechasBean.crearTareaMonitorPreseleccionadoConfirmarSeleccion(solicitud.getId());
            }

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_PRESELECCIONAR_MONITORES_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0027, new ArrayList());

        } catch (Exception ex) {
            Logger.getLogger(PreseleccionBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    private EstadoPreseleccion preseleccionar(Collection<Seccion> secciones, Solicitud solicitud, String responsable, double carga, boolean cupi2) throws Exception {
        if (solicitud == null) {
            return EstadoPreseleccion.SOLICITUD_NO_ENCONTRADA;
            //Collection<Secuencia> secuencias = conversor.darSecuenciaRespuestaSolicitudNoExiste(Long.toString(solicitud.getId()));
            //return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, secuencias);
        }
        if (!solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE))) {
            return EstadoPreseleccion.SOLICITUD_NO_VALIDA;
        }
        //Se busca al aspirante
        Aspirante aspirante = solicitud.getEstudiante();
        Curso curso = solicitud.getMonitoria_solicitada().getCurso();
        String correo = aspirante.getEstudiante().getPersona().getCorreo();
        for (Seccion seccion : secciones) {
            if (seccion == null) {
                return EstadoPreseleccion.SECCION_NO_ENCONTRADA;
            }

            //Verifica que hayan cupos para monitorias dentro de la seccion.
            if (!hayVacantes(seccion, cupi2)) {

                return EstadoPreseleccion.SECCION_SIN_VACANTES;
            }
            //Verificar que no haya conflicto de horario
            if (curso.isPresencial() && consultaBean.verificarConflicto(correo, seccion.getCrn())) {

                return EstadoPreseleccion.CONFLICTO_HORARIO_ASPIRANTE;
            }
        }

        //Agregar responsable preselección a solicitud
        solicitud.setResponsablePreseleccion(responsable);
        getSolicitudFacade().edit(solicitud);
        //Modificar créditos
        InformacionAcademica ia = aspirante.getEstudiante().getInformacion_Academica();

        double cargaT = carga * secciones.size();
        double creditos = 0;
        if (cargaT == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1))) {
            creditos = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CREDITOS_MONITORIA_T1T));
        } else {
            creditos = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CREDITOS_MONITORIA_T2));
        }
        if (ia.getCreditosMonitoriasISISEsteSemestre() != null) {
            ia.setCreditosMonitoriasISISEsteSemestre(ia.getCreditosMonitoriasISISEsteSemestre() + creditos);
        } else {
            ia.setCreditosMonitoriasISISEsteSemestre(cargaT);
        }


        getInformacionAcademicaFacade().edit(ia);

        Estudiante e = aspirante.getEstudiante();
        e.setInformacion_Academica(ia);

        getEstudianteFacade().edit(e);

        //Reservar horario
        if (curso.isPresencial()) {
            for (Seccion seccion : secciones) {
                solicitudBean.agregarHorarioSeccionAAspirante(seccion.getCrn(), correo);
            }
        }
        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        for (Seccion seccion : secciones) {
            MonitoriaAceptada monitoria = new MonitoriaAceptada();
            monitoria.setCarga(carga);
            monitoria.setSecciones(seccion);
            monitoria.setSolicitud(solicitud);
            monitoria.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            //Agregar la monitoria a la seccion
            monitorias.add(monitoria);
        }

        solicitud.setMonitorias(monitorias);

        //Cambiar estado solicitud a preseleccionada
        solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE));

        getSolicitudFacade().edit(solicitud);

        // Actualizar la tarea del profesor para cada una de las secciones
        for (Seccion seccion : secciones) {
            rangoFechasBean.crearTareaProfesorPreseleccionarPorSeccion(seccion);
        }

        return EstadoPreseleccion.PRESELECCION_EXITOSA;

    }

    @Override
    public String despreseleccionar(String comando) {
        try {
            getParser().leerXML(comando);
            String idSol = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();

            String responsable = getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL));
            boolean respuesta = revertirPreseleccion(idSol, responsable, null, Notificaciones.MENSAJE_REVERSION_SOLICITUD_ASPIRANTE);
            if (respuesta) {
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0148, new Vector());
            } else {
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0122, new Vector());
            }

        } catch (Exception ex) {
            Logger.getLogger(PreseleccionBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean revertirPreseleccion(String idSolicitud, String responsable, String motivo, String mensaje) {

        Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
        Aspirante aspirante = solicitud.getEstudiante();
        if (solicitud == null) {
            return false;
        }

        // La solicitud ya se encuentra en estado aspirante
        if (solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE))) {
            return true;
        }

        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        // Se elimina el horario de las monitorias del horario ocupado del aspirante
        for (MonitoriaAceptada m : monitorias) {
            String crn = m.getSeccion().getCrn();
            Curso curso = getCursoFacade().findByCRNSeccion(crn);
            if (curso.isPresencial()) {
                solicitudBean.removerHorarioSeccionAAspirante(crn, aspirante.getEstudiante().getPersona().getCorreo());
            }
        }

        Collection<Seccion> secciones = new ArrayList();
        // Se guardan las secciones sobre las que se agrego la monitoria para
        // despues regenerar las tareas correspondientes
        for (MonitoriaAceptada monitoriaAceptada : monitorias) {
            secciones.add(monitoriaAceptada.getSeccion());
        }
        enviarCorreosRevertirSolicitud(solicitud, motivo, responsable);

        Persona estudiante = solicitud.getEstudiante().getPersona();

        // Dependiendo del estado de la solicitud, se completan las tareas correspondientes
        String estado = solicitud.getEstadoSolicitud();
        if (estado.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE))) {
            rangoFechasBean.completarTareasPendientes(estudiante.getCorreo(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE), idSolicitud);
            //Intenta  borrar el timer de revertirPreseleccionAutomaticamente
            Long idTimerBorrar = timerGenericoBean.darIdTimer(RangoFechasBean.RUTA_INTER, RangoFechasBean.NOMBRE_METODO_MANEJO_TIMER, rangoFechasBean.construirInformacionTimerRevertirSeleccion(solicitud));
            if (idTimerBorrar != null) {
                timerGenericoBean.eliminarTimer(idTimerBorrar);
            }
        } else if (estado.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_VERIFICACION_DATOS))) {
            rangoFechasBean.completarTareasPendientes(estudiante.getCorreo(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ACTUALIZAR_DATOS_VERIFICACION), idSolicitud);
            rangoFechasBean.completarTareasPendientes(getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ACTUALIZAR_DATOS_VERIFICACION), idSolicitud);
        } else if (estado.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_REGISTRO_CONVENIO))) {
            rangoFechasBean.completarTareasPendientes(estudiante.getCorreo(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_CONVENIO_ESTUDIANTE), idSolicitud);
        } else if (estado.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE))) {
            rangoFechasBean.completarTareasPendientes(estudiante.getCorreo(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_FIRMA_CONVENIO_ESTUDIANTE), idSolicitud);
        } else if (estado.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO))) {
        } else if (estado.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_RADICACION))) {
            // TODO completar tareas involucradas cuando la monitoria esta pendiente de radicacion
        } else if (estado.equals(getConstanteBean().getConstante(Constantes.ESTADO_ASIGNADO))) {
            // TODO completar tareas involucradas cuando la monitoria ha sido asignada
        }

        // Se calcula la carga debido a las monitorias de la solicitud
        double carga = 0;
        for (MonitoriaAceptada monitoriaAceptada : monitorias) {
            carga += monitoriaAceptada.getCarga();
        }

        Collection<MonitoriaAceptada> toEliminate = new ArrayList<MonitoriaAceptada>();

        // Se eliminan las monitorias aceptadas de la solicitud
        for (MonitoriaAceptada monitoriaAceptada : monitorias) {

            toEliminate.add(monitoriaAceptada);
        }

        solicitud.getMonitorias().removeAll(toEliminate);

        for (MonitoriaAceptada monitoriaAceptada : toEliminate) {
            getMonitoriaFacade().remove(monitoriaAceptada);
        }

        double creditos = 0;
        if (carga == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1))) {
            creditos = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CREDITOS_MONITORIA_T1T));
        } else {
            creditos = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CREDITOS_MONITORIA_T2));
        }

        //Restar Créditos
        InformacionAcademica ia = aspirante.getEstudiante().getInformacion_Academica();
        ia.setCreditosMonitoriasISISEsteSemestre(ia.getCreditosMonitoriasISISEsteSemestre() - creditos);
        Estudiante e = aspirante.getEstudiante();
        e.setInformacion_Academica(ia);
        getEstudianteFacade().edit(e);
        //Cambiar estado solicitud
        solicitud.setResponsablePreseleccion(null);
        solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE));
        getSolicitudFacade().edit(solicitud);

        // Se regenera la tarea para el profesor
        for (Seccion seccion : secciones) {
            rangoFechasBean.crearTareaProfesorPreseleccionarPorSeccion(seccion);
        }

        if (mensaje != null) {
            String cursoInfo = solicitud.getMonitoria_solicitada().getCurso().getNombre() + " ";
            Iterator<Seccion> sec = secciones.iterator();
            if (secciones.size() == 1) {
                cursoInfo += "Sección " + sec.next().getNumeroSeccion();
            } else if (secciones.size() > 1) {
                cursoInfo += "secciones " + sec.next().getNumeroSeccion() + " y " + sec.next().getNumeroSeccion();
            }

            mensaje = mensaje.replaceFirst("%", cursoInfo);
            getCorreoBean().enviarMail(e.getPersona().getCorreo(), Notificaciones.ASUNTO_REVERSION_SOLICITUD.replaceFirst("%", ""), null, null, null, mensaje);

        }

        return true;

    }

    @Override
    public boolean revertirPreseleccionAutomaticamente(String idSolicitud) {
        Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
        Aspirante aspirante = solicitud.getEstudiante();
        if (solicitud == null || !solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE))) {
            return false;
        }

        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        // Se elimina el horario de las monitorias del horario ocupado del aspirante
        for (MonitoriaAceptada m : monitorias) {
            String crn = m.getSeccion().getCrn();
            Curso curso = getCursoFacade().findByCRNSeccion(crn);
            if (curso.isPresencial()) {
                solicitudBean.removerHorarioSeccionAAspirante(crn, aspirante.getEstudiante().getPersona().getCorreo());
            }
        }

        Persona estudiante = solicitud.getEstudiante().getPersona();

        enviarCorreosRevertirSolicitud(solicitud, Notificaciones.MENSAJE_REVERSION_SOLICITUD_MOTIVO_AUTOMATICA, "sisinfo");

        // Completa las tareas pendientes de la solicitud
        rangoFechasBean.completarTareasPendientes(estudiante.getCorreo(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE), idSolicitud);

        // Se calcula la carga debido a las monitorias de la solicitud
        double carga = 0;
        for (MonitoriaAceptada monitoriaAceptada : monitorias) {
            carga += monitoriaAceptada.getCarga();
        }

        // Se eliminan las monitorias aceptadas de la solicitud
        Collection<MonitoriaAceptada> toEliminate = new ArrayList<MonitoriaAceptada>();

        // Se eliminan las monitorias aceptadas de la solicitud
        for (MonitoriaAceptada monitoriaAceptada : monitorias) {

            toEliminate.add(monitoriaAceptada);
        }

        solicitud.getMonitorias().removeAll(toEliminate);

        for (MonitoriaAceptada monitoriaAceptada : toEliminate) {
            getMonitoriaFacade().remove(monitoriaAceptada);
        }

        //Restar Créditos
        InformacionAcademica ia = aspirante.getEstudiante().getInformacion_Academica();
        ia.setCreditosMonitoriasISISEsteSemestre(ia.getCreditosMonitoriasISISEsteSemestre() - carga);
        Estudiante e = aspirante.getEstudiante();
        e.setInformacion_Academica(ia);
        getEstudianteFacade().edit(e);
        //Cambiar estado solicitud
        solicitud.setResponsablePreseleccion(null);
        solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE));
        getSolicitudFacade().edit(solicitud);

        // Regenerar tarea preseleccion
        for (MonitoriaAceptada monitoriaAceptada : toEliminate) {
            rangoFechasBean.crearTareaProfesorPreseleccionarPorSeccion(monitoriaAceptada.getSeccion());
        }

        return true;

    }

    @Override
    public String darSeccionesSinPreseleccion(String xml) {
        String respuesta = null;
        String login = null;
        Vector<Secuencia> secuencias = new Vector<Secuencia>();
        ConversorGeneral conversorGeneral = new ConversorGeneral(xml);
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            login = correo.split("@")[0];
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            if (profesor == null) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), login);
                    Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_PROFESOR);
                    secParametro.agregarAtributo(atrParametro);
                    parametros.add(secParametro);
                    respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_SIN_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0086, parametros);
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_SIN_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                }
            }
            //recurpera las secciones de un profesor
            Collection<Seccion> seccionesProfesor = getSeccionFacade().findByCorreoProfesor(correo);
            Iterator<Seccion> itSeccionesProfesor = seccionesProfesor.iterator();
            Vector<Seccion> seccionesSinPreseleccion = new Vector<Seccion>();
            //recorre las secciones del profesor
            while (itSeccionesProfesor.hasNext()) {
                Seccion s = itSeccionesProfesor.next();
                Collection<MonitoriaAceptada> monitoriasSeccion = getMonitoriaFacade().findByCRNSeccion(s.getCrn());
                //si no tiene monitores asignados se agrega a la lista
                if (monitoriasSeccion == null) {
                    seccionesSinPreseleccion.add(s);
                } else {
                    if (hayVacantes(s, conversorGeneral.obtenerResponsable().equals(getConstanteBean().getConstante(Constantes.ROL_CUPI2)))) {
                        seccionesSinPreseleccion.add(s);
                    }
                }
            }
            Secuencia cursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), getConstanteBean().getConstante(Constantes.NULL));
            for (Seccion se : seccionesSinPreseleccion) {
                Curso c = getCursoFacade().findByCRNSeccion(se.getCrn());
                Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
                secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), c.getCodigo()));
                secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre()));
                Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
                secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), se.getCrn()));
                secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(se.getNumeroSeccion())));
                secCurso.agregarSecuencia(secSeccion);

                cursos.agregarSecuencia(secCurso);
            }
            secuencias.add(cursos);
            respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_SIN_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0042, new Vector<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(PreseleccionBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), login);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_PROFESOR);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_SIN_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0062, parametros);
            } catch (Exception exe) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_SIN_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
            }
            return respuesta;
        }
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

    /**
     * Retorna SolicitudFacade
     * @return solicitudFacade SolicitudFacade
     */
    private SolicitudFacadeLocal getSolicitudFacade() {
        return solicitudFacade;
    }

    /**
     * Retorna SeccionFacade
     * @return seccionFacade SeccionFacade
     */
    private SeccionFacadeRemote getSeccionFacade() {
        return seccionFacade;
    }

    /**
     * Retorna MonitoriaFacade
     * @return monitoriaFacade MonitoriaFacade
     */
    private MonitoriaFacadeLocal getMonitoriaFacade() {
        return monitoriaFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    /**
     * Retorna CursoFacade
     * @return cursoFacade CursoFacade
     */
    private CursoFacadeRemote getCursoFacade() {
        return cursoFacade;
    }

    /**
     * Retorna ProfesorFacade
     * @return profesorFacade ProfesorFacade
     */
    private ProfesorFacadeRemote getProfesorFacade() {
        return profesorFacade;
    }

    /**
     * Retorna CorreoBean
     * @return correoBean CorreoBean
     */
    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    /**
     * Metodo que retorna si hay vacantes para monitores en una seccion
     * especifica
     * @param seccion Seccion que se quiere recibir
     * @param esCupi2 Booleano indicando si las vacantes son para una preseleccion
     * de cupi2
     * @return
     */
    private boolean hayVacantes(Seccion seccion, boolean esCupi2) {

        if (seccion == null) {
            return false;
        }
        List<MonitoriaAceptada> monitorias = getMonitoriaFacade().findByCRNSeccion(seccion.getCrn());
        double sumaCargas = 0;
        for (MonitoriaAceptada m : monitorias) {
            double carga = m.getCarga();
            double valCargaMonitoriaT1 = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1));
            if (carga == valCargaMonitoriaT1) {
                sumaCargas += 1;
            } else {
                sumaCargas += 0.5;
            }
        }
        double vacantes = seccion.getMaximoMonitores() - sumaCargas;
        boolean esCursoCupi2 = rangoFechasBean.seleccionMedioMonitorNoACargoCupi2(seccion);
        if (esCupi2) {
            if (vacantes > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (vacantes >= 1 || (vacantes > 0 && !esCursoCupi2)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public EstudianteFacadeRemote getEstudianteFacade() {
        return estudianteFacade;
    }

    public InformacionAcademicaFacadeRemote getInformacionAcademicaFacade() {
        return informacionAcademicaFacade;
    }

    private void enviarCorreosRevertirSolicitud(Solicitud solicitud, String motivo, String responsable) {
        String responsablePreseleccion = solicitud.getResponsablePreseleccion();
        Persona estudiante = solicitud.getEstudiante().getPersona();
        String correoInteresado;
        String mensaje;
        String asunto = Notificaciones.ASUNTO_REVERSION_SOLICITUD.replaceFirst("%", estudiante.getNombres() + " " + estudiante.getApellidos());
        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        if (responsablePreseleccion.equals(getConstanteBean().getConstante(Constantes.ROL_CUPI2))) {
            // El encargado de la preseleccion fue cupi2 entonces es necesario enviar correo informando que la solicitud fue revertida
            correoInteresado = "cupi2@uniandes.edu.co";
            mensaje = Notificaciones.MENSAJE_REVERSION_SOLICITUD_CUPI2;
            if (motivo == null) {
                motivo = "";
            }
            int numSec1 = -1, numSec2 = -1;
            for (MonitoriaAceptada monitoriaAceptada : monitorias) {
                if (numSec1 == -1) {
                    numSec1 = monitoriaAceptada.getSeccion().getNumeroSeccion();
                } else {
                    numSec2 = monitoriaAceptada.getSeccion().getNumeroSeccion();
                }
            }
            String secs = "";
            if (monitorias.size() == 2) {
                secs = "las secciones <b>" + numSec1 + "</b> y <b>" + numSec2 + "</b>";
            } else {
                secs = "la sección <b>" + numSec1 + "</b>";
            }
            String nombresEstudiante = estudiante.getNombres() + " " + estudiante.getApellidos();
            mensaje = mensaje.replaceFirst("%", nombresEstudiante);

            mensaje = mensaje.replaceFirst("%", secs);
            Curso curso = solicitud.getMonitoria_solicitada().getCurso();
            String datosCurso = curso.getCodigo() + " " + curso.getNombre();
            mensaje = mensaje.replaceFirst("%", datosCurso);
            mensaje = mensaje.replaceFirst("%", responsable);
            mensaje = mensaje.replaceFirst("%", motivo);
            getCorreoBean().enviarMail(correoInteresado, asunto, "sisinfo@uniandes.edu.co", null, null, mensaje);
        } else {
            String mensajeModificado;
            for (MonitoriaAceptada monitoriaAceptada : monitorias) {
                // Se le debe enviar correo al profesor informando que la solicitud de su monitor fue cancelada
                correoInteresado = monitoriaAceptada.getSeccion().getProfesorPrincipal().getPersona().getCorreo();
                mensaje = Notificaciones.MENSAJE_REVERSION_SOLICITUD_PROFESOR_GENERICA;
                if (motivo == null) {
                    motivo = "";
                }
                String nombresEstudiante = estudiante.getNombres() + " " + estudiante.getApellidos();

                Curso curso = cursoFacade.findByCRNSeccion(monitoriaAceptada.getSeccion().getCrn());
                String datosCurso = " CRN " + monitoriaAceptada.getSeccion().getCrn() + curso.getCodigo() + " " + curso.getNombre() + " Sección " + monitoriaAceptada.getSeccion().getNumeroSeccion() + " ";
                mensajeModificado = mensaje.replaceFirst("%", datosCurso);
                mensajeModificado = mensajeModificado.replaceFirst("%", nombresEstudiante);
                mensajeModificado = mensajeModificado.replaceFirst("%", responsable);
                mensajeModificado = mensajeModificado.replaceFirst("%", motivo);

                // Se envian los correos a los profesores correspondientes con copia a sisinfo
                getCorreoBean().enviarMail(correoInteresado, asunto, "sisinfo@uniandes.edu.co", null, null, mensajeModificado);
            }
        }
    }
}
