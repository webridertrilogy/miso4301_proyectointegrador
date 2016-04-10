/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.Convocatoria;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.RangoFechas;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.RangoFechasFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Administrador
 */
@Stateless
public class RangoFechasBean implements RangoFechasBeanLocal, RangoFechasBeanRemote {

    public final static String RUTA_INTER = "co.uniandes.sisinfo.serviciosnegocio.RangoFechasBeanRemote";
    public final static String NOMBRE_METODO_MANEJO_TIMER = "manejoTimersMonitorias";
    @EJB
    private RangoFechasFacadeLocal rangoFechasFacade;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ConvocatoriaRemote convocatoriaBean;
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private CursoFacadeRemote cursoFacade;
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    @EJB
    private SeccionFacadeRemote seccionFacade;
    @EJB
    private SolicitudFacadeLocal solicitudFacade;
    @EJB
    private TareaMultipleFacadeRemote tareaFacade;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    private ServiceLocator serviceLocator;   
    @EJB
    private MonitoriaFacadeLocal monitoriaAceptadaFacade;
    @EJB
    private PreseleccionLocal preseleccionBean;

    @EJB
    private TareaSencillaRemote  tareaSencillaBean;



    public RangoFechasBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            convocatoriaBean = (ConvocatoriaRemote) serviceLocator.getRemoteEJB(ConvocatoriaRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            tareaFacade = (TareaMultipleFacadeRemote) serviceLocator.getRemoteEJB(TareaMultipleFacadeRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(RangoFechasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Parser
     */
    private ParserT parser;

    @Override
    public String esRangoValidoXML(String xml) {
        try {
            getParser().leerXML(xml);
            String rango = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO)).getValor();
            boolean esValido = esRangoValido(rango);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secTieneSolicitudes = null;
            if (esValido) {
                secTieneSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), "true");
            } else {
                secTieneSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), "false");
            }
            secuencias.add(secTieneSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ES_RANGO_VALIDO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0193, new LinkedList<Secuencia>());

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            return null;//getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_PERIODO_DE_DESPRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0132, new LinkedList<Secuencia>());
        }
    }

    @Override
    public boolean esRangoValido(String nombre) {
        boolean esRango = false;
        RangoFechas r = rangoFechasFacade.findByNombre(nombre);
        if (r != null) {
            long tiempoActual = System.currentTimeMillis();
            Timestamp fechaInicio = r.getFechaInicio();
            Timestamp fechaFin = r.getFechaFin();
            Timestamp fechaActual = new Timestamp(tiempoActual);

            if (fechaActual.compareTo(fechaInicio) >= 0 && fechaActual.compareTo(fechaFin) <= 0) {
                esRango = true;
            }
        }
        return esRango;
    }

    @Override
    public RangoFechas consultarRangoFechaPorNombre(String nombre) {
        RangoFechas r = rangoFechasFacade.findByNombre(nombre);
        return r;
    }

    @Override
    public void eliminarRangosFechas() {
        Collection<RangoFechas> lista = rangoFechasFacade.findAll();
        Iterator<RangoFechas> iterator = lista.iterator();
        while (iterator.hasNext()) {
            RangoFechas r = iterator.next();
            rangoFechasFacade.remove(r);
        }
    }

    @Override
    public String crearRangosFechas(String comando) {
        try {
            // Preparamos un rango dado el comando.
            RangoFechas[] rangoFechasTmp = null;
            try {
                rangoFechasTmp = this.pasarComandoARangoFechas(comando);
            } catch (Exception ex) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(
                        secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_RANGOS_FECHAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
            // Consultamos rango.
            RangoFechas rangoFechas = rangoFechasFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
            if (rangoFechas == null) {
                try {
                    //se crean los rangos
                    rangoFechasFacade.create(rangoFechasTmp[0]);
                    rangoFechas = rangoFechasTmp[0];
                } catch (Exception ex) {
                    Logger.getLogger(RangoFechasBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

                rangoFechas.setFechaInicio(rangoFechasTmp[0].getFechaInicio());
                rangoFechas.setFechaFin(rangoFechasTmp[0].getFechaFin());
                rangoFechasFacade.edit(rangoFechas);
                
                

            }

            /*si la fecha de inicio del proceso no ha pasado y la convocatoria no se ha abierto, se borra el timer de crear anterior y se crea el nuevo*/
            if (rangoFechas.getFechaInicio().after(new Timestamp(System.currentTimeMillis())) && !convocatoriaBean.hayConvocatoriaAbierta()) {
                    try {
                        timerGenerico.eliminarTimerPorParametroExterno(getConstanteBean().getConstante(Constantes.TIMER_CREAR_RANGOFECHAS));
                    } catch (Exception e) {
                        Logger.getLogger(RangoFechasBean.class.getName()).log(Level.SEVERE, null, e);
                    }
                }  else { /*si la fecha ya paso borra las tareas y las vuelve  a crear*/
                    borrarTareasPreseleccion();
                    crearTareasProfesoresPreseleccionMonitores(rangoFechas);
                    /*TODO: hacer lo mismo para el resto de tareas*/
                    crearTareasConfirmacionMonitoria();
                    crearTareasTraerPapelesRegistrarConvenio();

                    crearTareasVerificarDatosEstudiantesCoordinacion();
                    crearTareasFirmarConveniosEstudiantes();
            }

            // Timer.
            //solo crea el timer si la convocatoria esta cerrrada y la fecha de inicio es despues de hoy
            if (!convocatoriaBean.hayConvocatoriaAbierta() && rangoFechasTmp[0].getFechaInicio().after(new Date())) {
                timerGenerico.crearTimer2(RUTA_INTER, NOMBRE_METODO_MANEJO_TIMER, rangoFechasTmp[0].getFechaInicio(), getConstanteBean().getConstante(Constantes.TIMER_CREAR_RANGOFECHAS),
                    "Monitorias", this.getClass().getName(), "crearRangosFechas", "Este timer se crea al crear el rango de fechas para ejecutarse cuando el inicio del rango de fechas establecido se presente");
            }

            // Se envia un correo a sisinfo informando del cambio de fechas
            getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO), Notificaciones.ASUNTO_CAMBIO_RANGO_FECHAS, null, null, null, Notificaciones.MENSAJE_CAMBIO_RANGO_FECHAS);

            //1.eliminar timer anterior de cerrar convocatoria
            timerGenerico.eliminarTimerPorParametroExterno(getConstanteBean().getConstante(Constantes.TIMER_CERRAR_CONVOCATORIA));
            //2.crear timer de cerrar convocatoria
            timerGenerico.crearTimer2(RUTA_INTER, NOMBRE_METODO_MANEJO_TIMER, rangoFechasTmp[0].getFechaFin(), getConstanteBean().getConstante(Constantes.TIMER_CERRAR_CONVOCATORIA),
                    "Monitorias", this.getClass().getName(), "crearRangosFechas", "Este timer se crea al crear el rango de fechas para ejecutarse cuando el fin del rango de fechas establecido se presente");

            // Fecha para subir nota de monitores
            // Consultamos rango.
            rangoFechas = rangoFechasFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_NOTAS_MONITORES));
            if (rangoFechas == null) {
                try {
                    //se crean los rangos
                    rangoFechasFacade.create(rangoFechasTmp[1]);
                    rangoFechas = rangoFechasTmp[1];
                } catch (Exception ex) {
                    Logger.getLogger(RangoFechasBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

                rangoFechas.setFechaInicio(rangoFechasTmp[1].getFechaInicio());
                rangoFechas.setFechaFin(rangoFechasTmp[1].getFechaFin());
                rangoFechasFacade.edit(rangoFechas);
                
            }

            /*si la fecha de inicio del proceso no ha pasado y la convocatoria no se ha abierto, se borra el timer de crear anterior y se crea el nuevo*/
            if (rangoFechas.getFechaInicio().after(new Timestamp(System.currentTimeMillis())) && !convocatoriaBean.hayConvocatoriaAbierta()) {
                    try {
                        timerGenerico.eliminarTimerPorParametroExterno(getConstanteBean().getConstante(Constantes.TIMER_SUBIR_NOTAS_MONITORES));
                    } catch (Exception e) {
                        Logger.getLogger(RangoFechasBean.class.getName()).log(Level.SEVERE, null, e);
                    }
                }  else { /*si la fecha ya paso borra las tareas y las vuelve  a crear*/
                generarTareasNotaMonitor();
            }
            //solo crea el timer para crear las tareas de subir notas si la fecha de inicio del rango es posterior a la actual
            if (rangoFechasTmp[1].getFechaInicio().after(new Date())) {
                timerGenerico.crearTimer2(RUTA_INTER, NOMBRE_METODO_MANEJO_TIMER, rangoFechasTmp[1].getFechaInicio(), getConstanteBean().getConstante(Constantes.TIMER_SUBIR_NOTAS_MONITORES),
                    "Monitorias", this.getClass().getName(), "crearTareasSubirMonitores", "Este timer se crea al crear el rango de fechas para ejecutarse cuando la fecha de inicio de subir notas de monitres se cumpla");
            }

            

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RANGOS_FECHAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(RangoFechasBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_RANGOS_FECHAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(RangoFechasBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }

        }
    }

    @Override
    public void generarTareasNotaMonitor() {
        List<Solicitud> solicitudes = solicitudFacade.findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_ASIGNADO));
        Iterator<Solicitud> iterator = solicitudes.iterator();
        Timestamp fechaInicio = darFechaInicialRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_NOTAS_MONITORES));
        Timestamp fechaFin = darFechaFinalRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_NOTAS_MONITORES));
        Periodo periodoActual = periodoFacade.findActual();
        while (iterator.hasNext()) {
            Solicitud solicitud = iterator.next();
            Aspirante estudiante = solicitud.getEstudiante();
            Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
            Iterator<MonitoriaAceptada> iteratorM = monitorias.iterator();
            while (iteratorM.hasNext()) {
                MonitoriaAceptada monitoriaAceptada= iteratorM.next();
                if(monitoriaAceptada.getNota()!= 0) // La monitoria ya tiene nota y no es necesario calificarla
                    break;
                Seccion seccion = monitoriaAceptada.getSeccion();
                Profesor profesor = seccion.getProfesorPrincipal();
                if (profesor != null) {
                    String correo = profesor.getPersona().getCorreo();
                    String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_NOTAS_MONITORES);

                    // Busca si existe una tarea del mismo tipo y correo
                    // que contenga la misma sección entre sus parámetros
                    Properties prop = new Properties();
                    prop.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn());
                    prop.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), periodoActual);
                    long idTarea = tareaBean.consultarIdTareaPorParametrosTipo(tipo,prop);

                    // Si no existe la tarea, es necesario crearla
                    if (idTarea == -1) {
                        //Alerta alerta = alertaFacade.findByTipoTarea(tipo);
                        Curso curso = cursoFacade.findByCRNSeccion(seccion.getCrn());

                        HashMap<String, String> parametrosTarea = new HashMap<String, String>();
                        parametrosTarea.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn());
                        parametrosTarea.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD),""+solicitud.getId());

                        String comando = getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_NOTA_MONITOR);
                        String asunto = Notificaciones.ASUNTO_SUBIR_NOTAS_MONITOR;
                        String header = Notificaciones.HEADER_SUBIR_NOTAS_MONITOR;
                        String footer = Notificaciones.FOOTER_SUBIR_NOTAS_MONITOR;
                        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_NOTAS_MONITOR, estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos(), curso.getNombre());

                        String nombreRol;
                        if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))) {
                            nombreRol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
                        } else {
                            nombreRol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA);
                        }

                        tareaBean.crearTareaPersona(mensaje, tipo, correo, true, header, footer, fechaInicio, fechaFin, comando, nombreRol, parametrosTarea, asunto);
                    }else{
                        // Si la tarea ya existe para ese periodo y ese curso, entonces solo es necesario actualizar las nuevas fechas
                        Collection<TareaMultiple> tareas = tareaFacade.findByCorreoYTipo(tipo, correo);
                        TareaMultiple tarea = tareas.iterator().next();
                        tarea.setFechaCaducacion(fechaFin);
                        tarea.setFechaInicio(fechaInicio);
                        tareaFacade.edit(tarea);
                    }
                }
            }
        }
    }
    /**
     * Pasa un comando XML a un RangoFechas
     */
    private RangoFechas[] pasarComandoARangoFechas(String xml) throws Exception {
        RangoFechas[] rangoFechasResult = new RangoFechas[2];
        getParser().leerXML(xml);
        String nombre = getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL);
        Secuencia secRangoFechas = getParser().obtenerSecuencia(nombre);
        nombre = getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_NOTAS_MONITORES);
        Secuencia secRangoFechasNotas = getParser().obtenerSecuencia(nombre);
        rangoFechasResult[0] = pasarSecuenciaARangoFecha(secRangoFechas);
        rangoFechasResult[1] = pasarSecuenciaARangoFecha(secRangoFechasNotas);

        return rangoFechasResult;
    }

    private RangoFechas pasarSecuenciaARangoFecha(Secuencia secRangoFechas) throws Exception{
        RangoFechas rangoFechasResult = null;
        if (secRangoFechas != null) {
            Secuencia secFechaInicio = secRangoFechas.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
            Secuencia secFechaFin = secRangoFechas.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
            if (secFechaInicio != null && secFechaFin != null) {

                SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dFechaInicio = sdfHMS.parse(secFechaInicio.getValor());
                Date dFechaFin = sdfHMS.parse(secFechaFin.getValor());
                Timestamp fechaInicio = new Timestamp(dFechaInicio.getTime());
                Timestamp fechaFin = new Timestamp(dFechaFin.getTime());

                rangoFechasResult = new RangoFechas();
                rangoFechasResult.setFechaInicio(fechaInicio);
                rangoFechasResult.setFechaFin(fechaFin);
                rangoFechasResult.setNombre(secRangoFechas.getNombre());
            }
        }
        return rangoFechasResult;
    }

    /**
     * Se abre la convocatoria para el periodo actual. Si ocurre un problema se le informa al administrador.
     */
    @Override
    public void abrirConvocatoria() {

        //Verifica que exista un solo periodo actual
        Periodo periodo = periodoFacade.findActual();
        //Si el periodo es nulo, significa que no hay un periodo actual. Se debe de cargar una cartelera antes
        if (!sePuedeAbrirConvocatoriaPeriodo(periodo)) {
            return;
        } else {
            Convocatoria convocatoria = new Convocatoria();
            convocatoria.setEstado(getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA));
            convocatoria.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            periodo.setConvocatoria(convocatoria);
            periodoFacade.edit(periodo);
            enviarCorreosConvocatoriaAbierta(periodo);

        }
    }

    private void completarTareasPorEstadoYTipo(String estado, String tipo){

        Collection<TareaMultiple> tareas = tareaFacade.findByTipo(tipo);
        for (TareaMultiple tarea : tareas) {
            Collection<TareaSencilla> tareasSencillas = tarea.getTareasSencillas();
            for (TareaSencilla tareaSencilla : tareasSencillas) {
                if(tareaSencilla.getEstado().equals(estado))
                    tareaSencillaBean.realizarTareaPorId(tareaSencilla.getId());
            }
        }
    }

    /**
     * metodo que termina las tareas anteriores de confirmacion de monitorias
     * y vuelve a crear las que hagan falta segun las solicitudes
     */
    @Override
    public void crearTareasConfirmacionMonitoria() {
        /*
         * busca todas las tareas cuyo tipo sea confirmar estudiante y esten en pendiente y terminarlas
         * para poder volver a crearlas
         */
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE);
        String estado = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE);
        completarTareasPorEstadoYTipo(estado, tipo);

        if (!existeRangoFechas()) {
            return;
        }
        //buscar las solicitudes con estado preseleccion:
        Collection<Solicitud> listaSolicitudes = solicitudFacade.findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE));
        for (Solicitud solicitud : listaSolicitudes) {
            Long idSolicitud = solicitud.getId();
            crearTareaMonitorPreseleccionadoConfirmarSeleccion(idSolicitud);
        }
    }

    private void crearTareaConfirmarEstudianteT1(Long idSolicitud, Seccion seccion, boolean esT1) {//
        //Es creada por la tarea Preselección Monitor cuando su estado cambia a Terminada
        Solicitud solicitud = getSolicitudFacade().findById(idSolicitud);

        if (solicitud != null) {
            Aspirante aspirante = solicitud.getEstudiante();
            String correo = aspirante.getPersona().getCorreo();
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE);
            Curso curso = getCursoFacade().findByCRNSeccion(seccion.getCrn());
            Profesor profesor = seccion.getProfesorPrincipal();


            if (true) {
                long tiempoTresDias = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_TRES_DIAS)).getValor();
                Timestamp fechaInicio = new Timestamp(System.currentTimeMillis());
                Timestamp fechaCaducacion = new Timestamp(System.currentTimeMillis() + tiempoTresDias);
                HashMap<String, String> parametros = new HashMap<String, String>();
                parametros.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId()));
                String comando = getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION);
                String asunto = Notificaciones.ASUNTO_CONFIRMACION;
                //Se manda un correo al estudiante para que realiza la tarea de confirmar la preselección
                String mensajeCorreo = Notificaciones.MENSAJE_CONFIRMACION_ASPIRANTE;
                String infoCurso;
                if (esT1) {
                    infoCurso = curso.getNombre() + "-" + seccion.getNumeroSeccion();
                } else {
                    infoCurso = curso.getNombre() + "-" + seccion.getNumeroSeccion() + " (Monitoría T2)";
                }
                mensajeCorreo = String.format(mensajeCorreo,infoCurso);
                String header = Notificaciones.HEADER_CONFIRMACION_ASPIRANTE;
                String footer = Notificaciones.FOOTER_CONFIRMACION_ASPIRANTE;
                String nombreRol = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
                
                getTareaBean().crearTareaPersona(mensajeCorreo, tipo, correo, false, header, footer, fechaInicio, fechaCaducacion, comando, nombreRol, parametros, asunto);
                
                String contenidoCorreo = String.format(Notificaciones.MENSAJE_INFORMAR_PRESELECCION,mensajeCorreo);
                String asuntoCorreo = String.format(Notificaciones.ASUNTO_INFORMAR_PRESELECCION,infoCurso);
                getCorreoBean().enviarMail(correo,asuntoCorreo , null, null, null, contenidoCorreo);
                //Se crea un timer para rechazar automaticamente la solicitud si no ha sido confirmada
                crearTimerRevertirSeleccionMonitorPorNoConfirmacionEn3Dias(fechaCaducacion, solicitud);
            }
        }
    }

    private void crearTareaConfirmarEstudianteT2(Long idSolicitud, Seccion seccion1, Seccion seccion2) {
        //Es creada por la tarea Preselección Monitor cuando su estado cambia a Terminada
        Solicitud solicitud = getSolicitudFacade().findById(idSolicitud);
        if (solicitud != null) {
            Aspirante aspirante = solicitud.getEstudiante();
            String correo = aspirante.getPersona().getCorreo();
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE);
            Curso curso1 = getCursoFacade().findByCRNSeccion(seccion1.getCrn());
            Profesor profesor1 = seccion1.getProfesorPrincipal();
            Profesor profesor2 = seccion2.getProfesorPrincipal();

            long tiempoTresDias = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_TRES_DIAS)).getValor();
            Timestamp fechaInicio = new Timestamp(System.currentTimeMillis());
            Timestamp fechaCaducacion = new Timestamp(System.currentTimeMillis() + tiempoTresDias);
            HashMap<String, String> parametros = new HashMap<String, String>();
            parametros.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId()));
            String comando = getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION);

            String asunto = Notificaciones.ASUNTO_CONFIRMACION;
            String mensajeCorreo = Notificaciones.MENSAJE_CONFIRMACION_ASPIRANTE;

            mensajeCorreo = String.format(mensajeCorreo,"la sección " + seccion1.getNumeroSeccion() + " y " + seccion2.getNumeroSeccion() + " del curso " + curso1.getNombre());

            String header = Notificaciones.HEADER_CONFIRMACION_ASPIRANTE;
            String footer = Notificaciones.FOOTER_CONFIRMACION_ASPIRANTE;
            String nombreRol = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);

            getTareaBean().crearTareaPersona(mensajeCorreo, tipo, correo, false, header, footer, fechaInicio, fechaCaducacion, comando,nombreRol , parametros, asunto);

            //Se manda un correo al estudiante para que realiza la tarea de confirmar la preselección
            
            String contenidoCorreo = String.format(Notificaciones.MENSAJE_INFORMAR_PRESELECCION,mensajeCorreo);
            String asuntoCorreo = String.format(Notificaciones.ASUNTO_INFORMAR_PRESELECCION,curso1.getNombre());
            getCorreoBean().enviarMail(correo, asuntoCorreo, null, null, null, contenidoCorreo);
            crearTimerRevertirSeleccionMonitorPorNoConfirmacionEn3Dias(fechaCaducacion, solicitud);
        }
    }

    /**
     * Método que retorna el valor de un parámetro, el cuál se encuentra en la lista
     * de parámetros dada, cuyo nombre del campo es igual al que llega como parámetro
     * @param parametros Colección de parámetros
     * @param campo Nombre del campo del parámetro
     * @return Valor del parámetro consultado. Null en caso de que el parámetro no sea encontrado
     */
    private String consultarValorParametro(Collection<Parametro> parametros, String campo) {
        Iterator<Parametro> iterator = parametros.iterator();
        while (iterator.hasNext()) {
            Parametro parametro = iterator.next();
            if (parametro.getCampo().equals(campo)) {
                return parametro.getValor();
            }
        }
        return null;
    }

    @Override
    public void crearTareasTraerPapelesRegistrarConvenio() {

        //Se recuperan todas las tareas que tienen estado Pendiente de  tipo "tipo_actualizarDatosVerificacion" y se pasan a vencidas
        completarTareasPorEstadoYTipo(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE), getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_REGISTRO_CONVENIO));

        /* volver a crear las tareas de registrar convenio estudiantes...
         */
        if (!existeRangoFechas()) {
            return;
        }
        Collection<Solicitud> listaSolicitudes = solicitudFacade.findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_REGISTRO_CONVENIO));

        for (Solicitud solicitud : listaSolicitudes) {
            if (solicitud != null) {
                crearTareaTraerPapelesRegistrarConvenio(solicitud.getId());
            }
        }
    }

    @Override
    @Deprecated
    public String editarRangosFechas(String comando) {
        return null;
    }

    @Override
    public String consultarRangos(String comando) {
        try {
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secRangos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGOS), getConstanteBean().getConstante(Constantes.NULL));
            Collection<RangoFechas> listaRangos = rangoFechasFacade.findAll();
            Secuencia secGeneral = null;

            Iterator<RangoFechas> iterator = listaRangos.iterator();
            while (iterator.hasNext()) {
                RangoFechas rango = iterator.next();
                if (rango.getNombre().equals(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL))) {
                    secGeneral = new Secuencia(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL), getConstanteBean().getConstante(Constantes.NULL));
                    Secuencia secFechaIni = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), rango.getFechaInicio().toString());
                    Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), rango.getFechaFin().toString());
                    secGeneral.agregarSecuencia(secFechaIni);
                    secGeneral.agregarSecuencia(secFechaFin);
                    secRangos.agregarSecuencia(secGeneral);
                }else if (rango.getNombre().equals(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_NOTAS_MONITORES))) {
                    secGeneral = new Secuencia(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_NOTAS_MONITORES), getConstanteBean().getConstante(Constantes.NULL));
                    Secuencia secFechaIni = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), rango.getFechaInicio().toString());
                    Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), rango.getFechaFin().toString());
                    secGeneral.agregarSecuencia(secFechaIni);
                    secGeneral.agregarSecuencia(secFechaFin);
                    secRangos.agregarSecuencia(secGeneral);
                }
            }
            secuencias.add(secRangos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGOS_FECHAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());

        } catch (Exception e) {
            Logger.getLogger(RangoFechasBean.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public TareaMultipleRemote getTareaBean() {
        return tareaBean;
    }

    /**
     * Retorna CursoFacade
     * @return cursoFacade CursoFacade
     */
    private CursoFacadeRemote getCursoFacade() {
        return cursoFacade;
    }

    /**
     * Retorna CorreoBean
     * @return correoBean CorreoBean
     */
    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    /**
     * Retorna SolicitudFacade
     * @return solicitudFacade SolicitudFacade
     */
    private SolicitudFacadeLocal getSolicitudFacade() {
        return solicitudFacade;
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
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    @Override
    public Timestamp darFechaInicialRangoPorNombre(String nombre) {
        RangoFechas rango = rangoFechasFacade.findByNombre(nombre);
        return rango.getFechaInicio();
    }

    @Override
    public Timestamp darFechaFinalRangoPorNombre(String nombre) {
        RangoFechas rango = rangoFechasFacade.findByNombre(nombre);
        return rango.getFechaFin();
    }

    @Override
    public boolean rangosCreados() {
        Collection<RangoFechas> listaRangos = rangoFechasFacade.findAll();
        for (RangoFechas rangoFechas : listaRangos) {
            if (rangoFechas.getNombre().equals(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL))) {
                return true;
            }
        }
        return false;
    }

    private void crearTareasProfesoresPreseleccionMonitores(RangoFechas rangoFechas) {
        Collection<Seccion> seccionescursos = seccionFacade.findAll();
        for (Seccion seccion : seccionescursos) {
            crearTareaProfesorPreseleccionarPorSeccion(seccion);
        }

    }

    @Override
    public void crearTareaProfesorPreseleccionarPorSeccion(String crn) {
        Seccion seccion = seccionFacade.findByCRN(crn);
        if(seccion!=null)
            crearTareaProfesorPreseleccionarPorSeccion(seccion);
    }

    @Override
    public void crearTareaProfesorPreseleccionarPorSeccion(Seccion seccion) {
        Profesor profesor = seccion.getProfesorPrincipal();
        /*
        completa la tarea anterior de preselefccionar
         */
        //quitar tarea
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PRESELECCION_MONITOR);
        String estado = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE);        
        //-------------------------------------------------------------------------------------------------------------------------------
        /*
         *
         */
        if (!existeRangoFechas()) {
            return;
        }
        RangoFechas rangoFechas = rangoFechasFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
        double numMonitoresEsperado = seccion.getMaximoMonitores();
        Collection<MonitoriaAceptada> monitoriasAceptadas = monitoriaAceptadaFacade.findByCRNSeccion(seccion.getCrn());
        double numMonitoresSeleccionados = 0;
        for (MonitoriaAceptada monitoriaAceptada : monitoriasAceptadas) {
            /*una carga de 2 ()quiere decir que es 1 monitor
             * una carga de 1 quiere decir que es medio monitor
             */
            if (monitoriaAceptada.getCarga() >= Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1))) {
                numMonitoresSeleccionados += 1;
            } else if (monitoriaAceptada.getCarga() == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T2))) {
                numMonitoresSeleccionados += 0.5;
            }
        }
        //si todavia no tiene igual numero de monitores a los que deberia tener el curso
        //se le crea la tarea de seleccionar (numMonitoresEsperado-numMonitoresSeleccionados) monitores
            /***************************************************************************/
        double cantidadMonitoresPorSeleccionar = numMonitoresEsperado - numMonitoresSeleccionados;
        boolean aCargoCupi2 = seleccionMedioMonitorNoACargoCupi2(seccion);
        if (cantidadMonitoresPorSeleccionar >= 1 || (cantidadMonitoresPorSeleccionar > 0 && !aCargoCupi2)) { //cantidadMonitoresPorSeleccionar < 1 &&
            if (profesor != null) {
                String correo = profesor.getPersona().getCorreo();
                if (cantidadMonitoresPorSeleccionar > 0) {
                    Curso curso = cursoFacade.findByCRNSeccion(seccion.getCrn());
                    String numMonitoresPorSeleccionar = (aCargoCupi2 ? ("" + (int) cantidadMonitoresPorSeleccionar) : ("" + cantidadMonitoresPorSeleccionar));
                    Timestamp fechaInicio = rangoFechas.getFechaInicio();
                    Timestamp fechaCaducacion = rangoFechas.getFechaFin();
                    String categoriaResponsable = "";

                    if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))) {
                        categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
                    } else {
                        categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA);
                    }

                    String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION);
                    HashMap<String, String> parametros = new HashMap<String, String>();
                    parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn());

                    String asunto = Notificaciones.ASUNTO_PRESELECCION_MONITOR_SECCION;
                    String mensaje = String.format(Notificaciones.MENSAJE_PRESELECCION_MONITOR_SECCION, numMonitoresPorSeleccionar, curso.getNombre(), seccion.getNumeroSeccion());
                    String header = Notificaciones.HEADER_PRESELECCION_MONITOR_SECCION;
                    String footer = Notificaciones.FOOTER_PRESELECCION_MONITOR_SECCION;
                    getTareaBean().crearTareaPersona(mensaje, tipo, correo, true, header, footer, fechaInicio, fechaCaducacion, comando, categoriaResponsable, parametros, asunto);
                    /****************************************************************************/
                }
                /*de lo contrario el profesor ya selecciono a todos los que debia y no se le crea la tarea*/
            }
        } else {
            realizarTareaSeccion(tipo, seccion.getCrn());
        }
    }

    private void borrarTareasPreseleccion() {
        //quitar tarea
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PRESELECCION_MONITOR);
        String estado = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE);
        completarTareasPorEstadoYTipo(estado, tipo);
    }

    private boolean sePuedeAbrirConvocatoriaPeriodo(Periodo periodo) {
        if (periodo == null) {
            //Se le notifica al asistente de coordinación del problema
            getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION) + "," + getConstanteBean().getConstante(Constantes.CONFIG_MAIL_MAILER) + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_PROBLEMA_APERTURA_CONVOCATORIA, null, null, null, Notificaciones.MENSAJE_NO_HAY_PERIODO_ACTUAL);
            return false;
        }

        //Verifica que no exista una convocatoria abierta.
        Convocatoria convocatoriaPeriodo = periodo.getConvocatoria();
        if (convocatoriaPeriodo != null) {
            //Se le notifica al asistente que no pueden haber dos convocatorias abiertas para el periodo actual
            getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION) + "," + getConstanteBean().getConstante(Constantes.CONFIG_MAIL_MAILER) + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_PROBLEMA_APERTURA_CONVOCATORIA, null, null, null, Notificaciones.MENSAJE_YA_EXISTE_CONVOCATORIA);
            return false;
        }

        //Verifica que hayan cursos registrados
        Collection<Curso> cursos = getCursoFacade().findAll();
        if (cursos.isEmpty()) {
            //Se le notifica al asistente que no deben de existir cursos registrados y debe de cargar la cartelera
            getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION) + "," + getConstanteBean().getConstante(Constantes.CONFIG_MAIL_MAILER) + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_PROBLEMA_APERTURA_CONVOCATORIA, null, null, null, Notificaciones.MENSAJE_NO_EXISTEN_CURSOS);
            return false;
        }
        //Verifica que los rangos estén creados
        Collection<RangoFechas> listaRangos = rangoFechasFacade.findAll();
        if (listaRangos.isEmpty()) {
            //Se le notifica al asistente que se deben de configurar los rangos primero
            getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION) + "," + getConstanteBean().getConstante(Constantes.CONFIG_MAIL_MAILER) + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_PROBLEMA_APERTURA_CONVOCATORIA, null, null, null, Notificaciones.MENSAJE_PLAZOS_NO_CONFIGURADOS);
            return false;
        }
        //Verifica que las alertas esten creadas
        // Se asume que las alertas ya estan creadas!!!
        /*Collection<Alerta> listaAlertas = alertaFacade.findAlertasCoordinacion();
        if (listaAlertas.size() != 5) {
            //Se le notifica al asistente que debe de configurar las alertas del proceso
            getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION) + "," + getConstanteBean().getConstante(Constantes.CONFIG_MAIL_MAILER) + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_PROBLEMA_APERTURA_CONVOCATORIA, null, null, null, Notificaciones.MENSAJE_ALERTAS_NO_CREADAS);
            return false;
        }*/

        return true;
    }

    private void enviarCorreosConvocatoriaAbierta(Periodo periodo) {

        RangoFechas rangoFechas = rangoFechasFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
        crearTareasProfesoresPreseleccionMonitores(rangoFechas);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaFinConvocatoria = new Date(this.darFechaFinalRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL)).getTime());
        String strFechaFinConvocatoria = sdf.format(fechaFinConvocatoria);

        String mensaje = Notificaciones.MENSAJE_APERTURA_CONVOCATORIA_PROFESORES;
        mensaje = mensaje.replaceFirst("%", periodo.getPeriodo());
        mensaje = mensaje.replaceFirst("%", strFechaFinConvocatoria);

        //Informa a profesores
        getCorreoBean().enviarMail(Notificaciones.CORREO_PROFESORES_SISTEMAS + Notificaciones.DOMINIO_CUENTA_UNIANDES + ", " + Notificaciones.CORREO_PROFESORES_CATEDRA_SISTEMA + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_APERTURA_CONVOCATORIA, null, null, null, mensaje);
        //Informa a estudiantes
        mensaje = Notificaciones.MENSAJE_APERTURA_CONVOCATORIA_ESTUDIANTES;
        mensaje = mensaje.replaceFirst("%", periodo.getPeriodo());
        mensaje = mensaje.replaceFirst("%", strFechaFinConvocatoria);
        mensaje = mensaje.replaceFirst("%", strFechaFinConvocatoria);
        getCorreoBean().enviarMail(Notificaciones.CORREO_ESTUDIANTES_PREG_SISTEMAS + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_APERTURA_CONVOCATORIA, null, null, null, mensaje);
        //Informa al asistente del éxito de la apertura.
        getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION) + "," + getConstanteBean().getConstante(Constantes.CONFIG_MAIL_MAILER) + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_EXITO_APERTURA_CONVOCATORIA, null, null, null, Notificaciones.MENSAJE_EXITO_APERTURA_CONVOCATORIA);
    }

    private void crearTareasVerificarDatosEstudiantesCoordinacion() {
        /*
         * terminar tareas anteriorires:
         */
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ACTUALIZAR_DATOS_VERIFICACION);
        String estado = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE);
        //completarTareasPorEstadoYTipo(estado, tipo);
        /*
         * crear de nuevo las tareas:------------------------------------------------------------------------------------------------------------------
         */
        Collection<Solicitud> solicitudes = solicitudFacade.findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_VERIFICACION_DATOS));
        for (Solicitud solicitud : solicitudes) {
            crearTareaVerificarDatosEstudiantePorParteCoordinacion(solicitud.getId());
        }
        //---------------------------------------------------------------------------------------------------------------------------------------------
    }

    private void crearTareasFirmarConveniosEstudiantes() {

        /*
         * terminar tareas anteriorires:
         */
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_FIRMA_CONVENIO_ESTUDIANTE);
        String estado = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE);
        completarTareasPorEstadoYTipo(estado, tipo);
        /*
         * crear de nuevo las tareas:------------------------------------------------------------------------------------------------------------------
         */
        Collection<Solicitud> solicitudes = solicitudFacade.findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE));
        for (Solicitud solicitud : solicitudes) {
            crearTareaRegistarFirmaConveniosEstudiantes(solicitud.getId());
        }
    }

    @Override
    public void crearTareaVerificarDatosEstudiantePorParteCoordinacion(Long idSol) {

        Solicitud solicitud = solicitudFacade.find(idSol);
        Aspirante aspirante = solicitud.getEstudiante();
        Persona monitor = aspirante.getPersona();
        Collection<MonitoriaAceptada> listaMon = solicitud.getMonitorias();
        String strMonitoria = "";
        /* verifica que no exista otra tarea igual, en caso contrario termina la anterior y crea una nueva:
         */
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ACTUALIZAR_DATOS_VERIFICACION);
        String correoCoordinacion = getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD),solicitud.getId());
        long id = tareaBean.consultarIdTareaPorParametrosTipo(tipo, params);
        if(id != -1)
            tareaSencillaBean.realizarTareaPorId(id);
        
        if (!existeRangoFechas()) {
            return;
        }
        /*en caso de que sea un moniotr tipo T1 */
        if (listaMon.size() == 1) {
            MonitoriaAceptada mon = listaMon.iterator().next();
            if (mon.getCarga() == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T2))) {
                strMonitoria = "Monitoría T2: ";
            } else {
                strMonitoria = "Monitoria T1: ";
            }
            Curso curso = solicitud.getMonitoria_solicitada().getCurso();
            Seccion seccion = mon.getSeccion();
            strMonitoria += curso.getNombre() + " - " + seccion.getNumeroSeccion();
        } else if (listaMon.size() == 2) {
            strMonitoria += "Monitoría T2: ";
            Curso curso = solicitud.getMonitoria_solicitada().getCurso();
            Iterator<MonitoriaAceptada> ite = listaMon.iterator();
            MonitoriaAceptada mon1 = ite.next();
            MonitoriaAceptada mon2 = ite.next();
            Seccion seccion1 = mon1.getSeccion();
            Seccion seccion2 = mon2.getSeccion();
            strMonitoria += curso.getNombre() + " - " + seccion1.getNumeroSeccion() + " - " + seccion2.getNumeroSeccion();

        }
        //String nombre = "Actualización de datos para verificación (Solicitud: " + solicitud.getId() + ")";
        String asunto = Notificaciones.ASUNTO_ACTUALIZACION_DATOS;

        //String descripcion = "Se debe actualizar la información de la solicitud de " + monitor.getNombres() + " " + monitor.getApellidos() + "<" + monitor.getCorreo() + ">. "
        //        + strMonitoria;
        String mensaje = Notificaciones.MENSAJE_ACTUALIZACION_DATOS;
        mensaje = String.format(mensaje,solicitud.getId(),monitor.getNombres()+ " "+monitor.getApellidos(),strMonitoria);

        Timestamp fechaCaducacion = darFechaFinalRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
        Timestamp fechaInicio = darFechaInicialRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION);
        String header = Notificaciones.HEADER_ACTUALIZACION_DATOS;
        String footer = Notificaciones.FOOTER_ACTUALIZACION_DATOS;

        getTareaBean().crearTareaRol(mensaje, tipo, categoriaResponsable, true, header, footer, fechaInicio, fechaCaducacion, comando, parametros, asunto);
        
    }

    @Override
    public void crearTareaTraerPapelesRegistrarConvenio(Long id) {
        if (!existeRangoFechas()) {
            return;
        }
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_CONVENIO_ESTUDIANTE);
        Solicitud solicitud = solicitudFacade.find(id);
        Aspirante aspirante = solicitud.getEstudiante();
        String correo = aspirante.getPersona().getCorreo();
        String nombres = aspirante.getPersona().getNombres()+" "+aspirante.getPersona().getApellidos();
        int cantMonitorias = solicitud.getMonitorias().size();
        String detalleMonitorias = "";
        if (cantMonitorias == 1) {
            MonitoriaAceptada mon = solicitud.getMonitorias().iterator().next();
            Seccion sec = mon.getSeccion();
            int numeroSeccion = sec.getNumeroSeccion();
            String crnSeccion = sec.getCrn();
            Curso curso = cursoFacade.findByCRNSeccion(crnSeccion);
            detalleMonitorias += curso.getNombre() + " - " + numeroSeccion;
        } else if (cantMonitorias == 2) {
            Iterator<MonitoriaAceptada> iterator = solicitud.getMonitorias().iterator();
            MonitoriaAceptada mon1 = iterator.next();
            MonitoriaAceptada mon2 = iterator.next();

            Seccion sec1 = mon1.getSeccion();
            int numeroSeccion1 = sec1.getNumeroSeccion();
            String crnSeccion1 = sec1.getCrn();
            Curso curso = cursoFacade.findByCRNSeccion(crnSeccion1);
            detalleMonitorias += curso.getNombre() + " - " + numeroSeccion1 + "<br>";

            Seccion sec2 = mon2.getSeccion();
            int numeroSeccion2 = sec2.getNumeroSeccion();
            detalleMonitorias += curso.getNombre() + " - " + numeroSeccion2;
        }

        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_ESTUDIANTE);

        String asunto;
        String mensaje;
        asunto = Notificaciones.ASUNTO_PAPELES_CONVENIO;
        
        mensaje = String.format(Notificaciones.MENSAJE_TAREA_PAPELES,nombres,detalleMonitorias);

        String nombreRol = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        Timestamp fechaCaducacion = darFechaFinalRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
        Timestamp fechaInicio = darFechaInicialRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));

        tareaBean.crearTareaPersona(null, tipo, correo, false, mensaje,
                null, fechaInicio, fechaCaducacion, comando, nombreRol, parametros, asunto);


    }

    @Override
    public void crearTareaMonitorPreseleccionadoConfirmarSeleccion(Long idSolicitud) {
        if (!existeRangoFechas()) {
            return;
        }
        Solicitud solicitud = solicitudFacade.find(idSolicitud);
        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        Iterator<MonitoriaAceptada> iteratorMon = monitorias.iterator();
        //Para cada una de las monitorias se debe de generar una tarea
        //Si hay una sola monitoría, significa que la monitoria preseleccionada es de tipo T1 o hay solo una monitoria de tipo T2
        if (monitorias.size() == 1) {
            MonitoriaAceptada mon = iteratorMon.next();
            //Si la carga de la monitoría es T1
            if (mon.getCarga() == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1))) {
                Seccion sec = mon.getSeccion();
                crearTareaConfirmarEstudianteT1(idSolicitud, sec, true);
            } //Si la carga de la monitoría es T2
            else if (mon.getCarga() == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T2))) {
                Seccion sec = mon.getSeccion();
                crearTareaConfirmarEstudianteT1(idSolicitud, sec, false);
            }
        } //Si hay dos monitorias, significa que la monitoría es de tipo T2
        else {
            MonitoriaAceptada mon1 = iteratorMon.next();
            MonitoriaAceptada mon2 = iteratorMon.next();
            Seccion sec1 = mon1.getSeccion();
            Seccion sec2 = mon2.getSeccion();
            crearTareaConfirmarEstudianteT2(idSolicitud, sec1, sec2);
        }
    }

    @Override
    public void crearTareaRegistarFirmaConveniosEstudiantes(Long idSolicitud) {
        Solicitud solicitud = solicitudFacade.find(idSolicitud);

        if (solicitud != null) {
            Aspirante aspirante = solicitud.getEstudiante();
            String correo = aspirante.getPersona().getCorreo();
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_FIRMA_CONVENIO_ESTUDIANTE);
            // Busca si existe una tarea del mismo tipo y correo
            // que contenga la misma solicitud entre sus parámetros
            Properties params = new Properties();
            params.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD),solicitud.getId());
            long id = tareaBean.consultarIdTareaPorParametrosTipo(tipo, params);
            if(id != -1)
                tareaSencillaBean.realizarTareaPorId(id);

            if (existeRangoFechas()) {
                Timestamp fechaCaducacion = darFechaFinalRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
                Timestamp fechaInicio = darFechaInicialRangoPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
                String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
                HashMap<String, String> parametros = new HashMap<String, String>();
                parametros.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId()));
                String comando = getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_DEPARTAMENTO);

                String asunto = Notificaciones.ASUNTO_FIRMA;
                String mensaje = Notificaciones.MENSAJE_FIRMA;

                getTareaBean().crearTareaPersona(null, tipo, correo, false, mensaje, null, fechaInicio, fechaCaducacion, comando, categoriaResponsable, parametros, asunto);
                getCorreoBean().enviarMail(aspirante.getPersona().getCorreo(), asunto, /*CC*/ null,/*CCO*/ null,/*ARCHIVO*/ null, mensaje);
            }
        }
    }

    /**
     * MEtodo que crea un timer para revertir la seleccion de un estudiante por que no confirmo la monitoria en 3 dias.
     * @param fechaCaducacion: fecha cuando se vence el timer
     * @param solicitud: solicitud a revertir.
     * @param seccion: seccion a devolver estado.
     */
    @Override
    public void crearTimerRevertirSeleccionMonitorPorNoConfirmacionEn3Dias(Date fechaCaducacion, Solicitud solicitud) {
        //String infoTimer = getConstanteBean().getConstante(Constantes.CMD_TIMER_CONFIRMACION_MONITOR_3_DIAS) + "-" + solicitud.getId() + "-" + getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION) + seccion.getCrn();
        String infoTimer = construirInformacionTimerRevertirSeleccion(solicitud);
        timerGenerico.crearTimer2(RUTA_INTER, NOMBRE_METODO_MANEJO_TIMER, new Timestamp(fechaCaducacion.getTime()), infoTimer,
                    "Monitorias", this.getClass().getName(), "crearTimerRevertirSeleccionMonitorPorNoConfirmacionEn3Dias", "Al crear tarea para confirmar monitoria estudiante T1, se crea este timer para verificar pasados tres dias si la confirmacion se ha realizado.");
    }

    /**
     * MEtodo que crea un timer para revertir la seleccion de un estudiante por que no confirmo la monitoria en 3 dias.
     * @param fechaCaducacion: fecha cuando se vence el timer
     * @param solicitud: solicitud a revertir.
     * @param seccion: seccion a devolver estado.
     */
    /*@Override
    public void crearTimerRevertirSeleccionMonitorPorNoConfirmacionEn3DiasMonitorT2APO(Date fechaCaducacion, Solicitud solicitud) {
    //                                                                         0                                        1                                                                2                                3                                       4                                                         5
    //String infoTimer = getConstanteBean().getConstante(Constantes.CMD_TIMER_CONFIRMACION_MONITOR_3_DIAS) + "-" + solicitud.getId() + "-" + getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION) + "-" + seccion1.getCrn() + getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION) + "-" + seccion2.getCrn();
    String infoTimer =construirInformacionTimerRevertirSeleccion(solicitud);
    timerGenerico.crearTimer(RUTA_INTER, NOMBRE_METODO_MANEJO_TIMER, new Timestamp(fechaCaducacion.getTime()), infoTimer);
    }*/
    @Override
    public String construirInformacionTimerRevertirSeleccion(Solicitud solicitud) {
        String infoTimer = getConstanteBean().getConstante(Constantes.CMD_TIMER_CONFIRMACION_MONITOR_3_DIAS) + "-" + solicitud.getId();
        /*Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        Iterator<MonitoriaAceptada> it = monitorias.iterator();
        infoTimer += getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION) + "-" + it.next().getSeccion().getCrn();
        if(monitorias.size()>=1){
        infoTimer += "-"+getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION) + "-" + it.next().getSeccion().getCrn();
        }*/
        return infoTimer;
    }

    @Override
    public void manejoTimersMonitorias(String comando) {
        //TODO: manjeo de timers: pendiente el de 3 dias sin confirmar monitoria.

        String[] parametros = comando.split("-");

        if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_TIMER_CONFIRMACION_MONITOR_3_DIAS))) {

            System.out.println(RangoFechasBean.class + ":REVERTIR SOLICITUD:" + comando);
            //Long idSolicitud = Long.parseLong(parametros[1].trim());
            //String crnSeccion1 = pamrametros[3].trim();
            //String crnSeccion2 = pamrametros.length == 5 ? pamrametros[5].trim() : null;
            preseleccionBean.revertirPreseleccionAutomaticamente(parametros[1].trim());
            //TODO: mirar a que metodo se dee llamar
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.TIMER_CREAR_RANGOFECHAS))) {
            abrirConvocatoria();
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.TIMER_SUBIR_NOTAS_MONITORES))) {
            generarTareasNotaMonitor();
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.TIMER_CERRAR_CONVOCATORIA))) {
            convocatoriaBean.cerrarConvocatoriaActual();
        }

    }


    /**
     * Metodo que informa si para una seccion la seleccion de medio monitor o menos esta a cargo de cupi2 o no
     * @param seccion: seccion que se desea conocer si la seleccion del medio monitor esta o no cargo de cupi2
     * @return TRUE si la seleccion esta a cargo de Cupi2: False si no esta a cargo de cupi2
     */
    @Override
    public boolean seleccionMedioMonitorNoACargoCupi2(Seccion seccion) {
        Curso c = cursoFacade.findByCRNSeccion(seccion.getCrn());
        String codigoCurso = c.getCodigo();
        String cursosACargoCupi2 = getConstanteBean().getConstante(Constantes.CTE_MONITORIAS_CURSOS_MONITOR_A_CARGO_CUPI2);
        return cursosACargoCupi2.contains(codigoCurso);
    }

    @Override
    public void eliminarTimerConfirmacionEstudiante(Long idSolicitud) {
        Solicitud solicitud = solicitudFacade.findById(idSolicitud);
        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();


        Seccion[] sec = new Seccion[2];
        int i = 0;
        for (MonitoriaAceptada monitoriaAceptada : monitorias) {
            sec[i] = monitoriaAceptada.getSeccion();
            i++;
        }


        String infoTimer = construirInformacionTimerRevertirSeleccion(solicitud);
        timerGenerico.eliminarTimerPorParametroExterno(infoTimer);
    }

    @Override
    public void realizarTareaVerificacionDatosEstudianteCoordinacion(Long idSol) {
        Solicitud solicitud = solicitudFacade.find(idSol);
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ACTUALIZAR_DATOS_VERIFICACION);        
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD),""+solicitud.getId());
        long id = tareaBean.consultarIdTareaPorParametrosTipo(tipo, params);
        if(id != -1)
            tareaSencillaBean.realizarTareaPorId(id);
    }

    @Override
    public void realizarTareaSolicitud(String tipo, Long idSol) {
        Solicitud solicitud = solicitudFacade.find(idSol);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD),""+solicitud.getId());
        long id = tareaBean.consultarIdTareaPorParametrosTipo(tipo, params);
        if(id != -1)
            tareaSencillaBean.realizarTareaPorId(id);

    }

    @Override
    public void realizarTareaSeccionSolicitud(String tipo, String crn,Long idSol) {
        Solicitud solicitud = solicitudFacade.find(idSol);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD),""+solicitud.getId());
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION),crn);
        long id = tareaBean.consultarIdTareaPorParametrosTipo(tipo, params);
        if(id != -1)
            tareaSencillaBean.realizarTareaPorId(id);

    }

    @Override
    public void realizarTareaSeccion(String tipo, String crn) {
        Properties params = new Properties();        
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION),crn);
        /*long id = tareaBean.consultarIdTareaPorParametrosTipo(tipo, params);
        if(id != -1)
            tareaSencillaBean.realizarTareaPorId(id);*/
        tareaSencillaBean.realizarTareasPorTipoYParametros(tipo, params);

    }

    public boolean existeRangoFechas() {
        RangoFechas rf = rangoFechasFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
        return rf != null;
    }

    @Override
    public void completarTareasPendientes(String responsable, String tipo, String idSolicitud) {
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD),idSolicitud);
        long id = tareaBean.consultarIdTareaPorParametrosTipo(tipo, params);
        if(id != -1)
            tareaSencillaBean.realizarTareaPorId(id);
    }

    @Override
    public String rangoIniciado(String xml){
         try {
            getParser().leerXML(xml);
            String rango = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO)).getValor();
            boolean esValido = rangoYaInicio(rango);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secTieneSolicitudes = null;
            if (esValido) {
                secTieneSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), "true");
            } else {
                secTieneSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), "false");
            }
            secuencias.add(secTieneSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RANGO_INICIADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0193, new LinkedList<Secuencia>());

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            return null;//getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_PERIODO_DE_DESPRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0132, new LinkedList<Secuencia>());
        }
    }

    public boolean rangoYaInicio(String nombre) {
        boolean esRango = false;
        RangoFechas r = rangoFechasFacade.findByNombre(nombre);
        if (r != null) {
            long tiempoActual = System.currentTimeMillis();
            Timestamp fechaInicio = r.getFechaInicio();
            Timestamp fechaFin = r.getFechaFin();
            Timestamp fechaActual = new Timestamp(tiempoActual);

            if (fechaActual.compareTo(fechaInicio) >= 0) {
                esRango = true;
            }
        }
        return esRango;
    }
}
