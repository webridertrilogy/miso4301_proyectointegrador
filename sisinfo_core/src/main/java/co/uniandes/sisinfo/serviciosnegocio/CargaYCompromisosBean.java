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
import co.uniandes.sisinfo.entities.CargaProfesor;
import co.uniandes.sisinfo.entities.CursoPlaneado;
import co.uniandes.sisinfo.entities.DescargaProfesor;
import co.uniandes.sisinfo.entities.DireccionTesis;
import co.uniandes.sisinfo.entities.Evento;
import co.uniandes.sisinfo.entities.IntencionPublicacion;
import co.uniandes.sisinfo.entities.NivelTesis;
import co.uniandes.sisinfo.entities.OtrasActividades;
import co.uniandes.sisinfo.entities.PeriodoPlaneacion;
import co.uniandes.sisinfo.entities.ProyectoDeGrado;
import co.uniandes.sisinfo.entities.ProyectoFinanciado;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.Tesis1;
import co.uniandes.sisinfo.entities.Tesis2;
import co.uniandes.sisinfo.entities.TipoPublicacion;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.serviciosfuncionales.CargaProfesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.CursoPlaneadoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.DescargaProfesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.DireccionTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.IntencionPublicacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.NivelTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.OtrasActividadesFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoPlaneacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ProyectoDeGradoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ProyectoFinanciadoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReportesFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis1FacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis2FacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TipoPublicacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Ivan Melo, Marcela Morales
 */
@Stateless
public class CargaYCompromisosBean implements CargaYCompromisosBeanRemote, CargaYCompromisosBeanLocal {

    //-----------------------------------------------------------------
    // CONSTANTES
    //-----------------------------------------------------------------
    private final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.CargaYCompromisosBeanRemote";
    private final static String NOMBRE_METODO_TIMER = "manejarTimerCYC";
    //--------BEANS---------
    @EJB
    private CargaProfesorFacadeLocal cargaProfesorFacade;
    @EJB
    private CursoPlaneadoFacadeLocal cursoPlaneadoFacade;
    @EJB
    private IntencionPublicacionFacadeLocal intencionPublicacionFacade;
    @EJB
    private PeriodoPlaneacionFacadeLocal periodoPlaneacionFacade;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ProfesorFacadeRemote profesorFacade;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionRemote;
    @EJB
    private TipoPublicacionFacadeRemote tipoPublicacionFacade;
    @EJB
    private DireccionTesisFacadeLocal direccionTesisFacade;
    @EJB
    private Tesis1FacadeRemote tesis1Facade;
    @EJB
    private Tesis2FacadeRemote tesis2Facade;
    @EJB
    private ProyectoDeGradoFacadeRemote proyectoDeGradoFacade;
    @EJB
    private ProyectoFinanciadoFacadeLocal proyectoFinanciadoFacadeLocal;
    @EJB
    private OtrasActividadesFacadeLocal otrasActividadesFacadeLocal;
    @EJB
    private DescargaProfesorFacadeLocal descargaProfesorFacadeLocal;
    @EJB
    private EstudianteFacadeRemote estudianteFacadeRemote;
    @EJB
    private SeccionFacadeRemote seccionFacadeRemote;
    @EJB
    private CursoFacadeRemote cursoFacadeRemote;
    @EJB
    private NivelTesisFacadeLocal nivelTesisFacadeRemote;
    @EJB
    private HistoricoCargaYCompromisosBeanRemote beanCargaHistoricas;
    @EJB
    private ReportesFacadeRemote reporteFacadeRemote;
    //---OTROS--------------------
    private ParserT parser;
    private ServiceLocator serviceLocator;
    @EJB
    private TareaSencillaRemote tareaSencillaBean;
    private ConversorCargaYCompromisos conversor;

    //-----------------------------------------------------------------
    // CONSTRUCTOR
    //-----------------------------------------------------------------
    public CargaYCompromisosBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            //Tesis de Maestría
            tesis1Facade = (Tesis1FacadeRemote) serviceLocator.getRemoteEJB(Tesis1FacadeRemote.class);
            tesis2Facade = (Tesis2FacadeRemote) serviceLocator.getRemoteEJB(Tesis2FacadeRemote.class);
            //Proyecto de Grado
            proyectoDeGradoFacade = (ProyectoDeGradoFacadeRemote) serviceLocator.getRemoteEJB(ProyectoDeGradoFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            nivelFormacionRemote = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            tipoPublicacionFacade = (TipoPublicacionFacadeRemote) serviceLocator.getRemoteEJB(TipoPublicacionFacadeRemote.class);
            estudianteFacadeRemote = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            seccionFacadeRemote = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            cursoFacadeRemote = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            reporteFacadeRemote = (ReportesFacadeRemote) serviceLocator.getRemoteEJB(ReportesFacadeRemote.class);
            beanCargaHistoricas = (HistoricoCargaYCompromisosBeanRemote) serviceLocator.getRemoteEJB(HistoricoCargaYCompromisosBeanRemote.class);
            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            conversor = new ConversorCargaYCompromisos(seccionFacadeRemote, cursoFacadeRemote, tesis1Facade, tesis2Facade, proyectoDeGradoFacade, nivelTesisFacadeRemote, tipoPublicacionFacade, nivelFormacionRemote, estudianteFacadeRemote, cargaProfesorFacade, constanteBean);
        } catch (NamingException ex) {
            Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //-----------------------------------------------------------------
    // MÉTODOS
    //-----------------------------------------------------------------
    /**
     * inicia el proceso de carga
     * @param xml:String
     * @return confirmacion creacion o de errror(en principio no hay razon para que pase)
     */
    public String inicarProcesoCarga(String xml) {
        try {
            parser.leerXML(xml);
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //------ obtener datos del xml...
            Secuencia sec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));

            Date fechaInicioDate = sdfHMS.parse(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO)).getValor());
            Date fechaFinDate = sdfHMS.parse(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN)).getValor());
            String periodo = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();

            //0. Cierra el periodo anterior
            manejoCierreTotalPeriodoPlaneacionAnterior();
            PeriodoPlaneacion periodoP = new PeriodoPlaneacion();
            periodoP.setActual(true);
            periodoP.setPeriodo(periodo);
            periodoP.setFechaInicio(new Timestamp(fechaInicioDate.getTime()));
            periodoP.setFechaFin(new Timestamp(fechaFinDate.getTime()));

            //1. Saca los profesores de tipo Planta
            Collection<Profesor> profesoresPlanta = getProfesorFacade().findByTipo(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA));
            Collection<CargaProfesor> cargasProfesores = new ArrayList<CargaProfesor>();
            DescargaProfesor descarga = descargaProfesorFacadeLocal.findByNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SIN_DESCARGA));
            for (Profesor profesor : profesoresPlanta) {

                //2. Crea una carga para cada profesor
                Double cargaProfr = 0.0;
                Integer numCursosPregradoMaestria = 0;
                Integer numeroprivateacionesPlaneadas = 0;
                Double cargaEfectiva = 1.0;
                Collection<OtrasActividades> otros = new ArrayList<OtrasActividades>();
                Collection<CursoPlaneado> cursos = new ArrayList<CursoPlaneado>();
                Collection<Evento> eventos = new ArrayList<Evento>();
                Collection<ProyectoFinanciado> proyectosFinanciados = new ArrayList<ProyectoFinanciado>();
                Collection<DireccionTesis> tesisAcargo = new ArrayList<DireccionTesis>();

                //3. Saca las reglas de hasta que nivel pueden hacer tesis
                NivelFormacion maximoNivelTesis = definirReglaNivelTesis(profesor);
                Collection<IntencionPublicacion> intencionesPublica = new ArrayList<IntencionPublicacion>();
                CargaProfesor carga = new CargaProfesor(cargaProfr, numCursosPregradoMaestria, numeroprivateacionesPlaneadas, cargaEfectiva, otros, descarga, cursos, eventos, proyectosFinanciados, tesisAcargo, profesor, periodoP, maximoNivelTesis, intencionesPublica);
                cargaProfesorFacade.create(carga);
                cargasProfesores.add(carga);

                //4. Crea la tarea
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_CARGA_Y_COMPROMISOS);
                String asunto = Notificaciones.ASUNTO_LLENAR_CARGA;
                String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
                String cmd = getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR);

                HashMap<String, String> paramsNew = new HashMap<String, String>();
                paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CARGA), Long.toString(carga.getId()));

                String mensajeCreacion = Notificaciones.MENSAJE_PROCESO_CARGA_INCIADO;
                mensajeCreacion = mensajeCreacion.replaceFirst("%1", profesor.getPersona().getNombres() + " " + profesor.getPersona().getApellidos());
                mensajeCreacion = mensajeCreacion.replaceFirst("%2", sdfHMS.format(periodoP.getFechaFin()));
                tareaBean.crearTareaPersona(null, tipo, profesor.getPersona().getCorreo(), false, mensajeCreacion, "", new Timestamp(fechaInicioDate.getTime()), new Timestamp(fechaFinDate.getTime()), cmd, rol, paramsNew, asunto);
            }

            //5. Guardar periodo con sus cargas
            periodoP.setCargaProfesores(cargasProfesores);
            getPeriodoPlaneacion().create(periodoP);

            //6. crearle timers para el director: uno para aviso de fin del proceso y otro para finalizar el proceso
            // uno para que cierre el proceso de carga
            String valId = String.valueOf(periodoP.getId());
            crearTimersAvisoFinProcesoCYC(new Timestamp(fechaFinDate.getTime()), valId);
            crearTimerCerrarProcesoCyC(new Timestamp(fechaFinDate.getTime()), valId);

            String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_INICIAR_PROCESO_CARGA_COMPROMISOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0001, new ArrayList());
            return respuesta;

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_INICIAR_PROCESO_CARGA_COMPROMISOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Crea una carga de compromisos vacía para un profesor en un periodo dado
     * @param xml:String
     * @return xml:String
     * @author Marcela Morales
     */
    public String crearCargaVaciaAProfesor(String xml) {
        try {
            parser.leerXML(xml);
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //Obtiene los datos: periodo y correo del profesor
            String periodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();
            String correo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();

            //1. Busca el periodo
            PeriodoPlaneacion periodoP = getPeriodoPlaneacion().findByNombre(periodo);
            if (periodoP == null) {
                getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_CARGA_VACIA_A_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CYC_ERR_0001, new ArrayList());
            }
            //2. Busca el profesor
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            if (profesor == null) {
                getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_CARGA_VACIA_A_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CYC_ERR_0002, new ArrayList());
            }
            //3. Crea una carga vacía
            DescargaProfesor descarga = descargaProfesorFacadeLocal.findByNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SIN_DESCARGA));
            Double cargaProfr = 0.0;
            Integer numCursosPregradoMaestria = 0;
            Integer numeroprivateacionesPlaneadas = 0;
            Double cargaEfectiva = 1.0;
            Collection<OtrasActividades> otros = new ArrayList<OtrasActividades>();
            Collection<CursoPlaneado> cursos = new ArrayList<CursoPlaneado>();
            Collection<Evento> eventos = new ArrayList<Evento>();
            Collection<ProyectoFinanciado> proyectosFinanciados = new ArrayList<ProyectoFinanciado>();
            Collection<DireccionTesis> tesisAcargo = new ArrayList<DireccionTesis>();
            //Obtiene las reglas de hasta que nivel pueden hacer tesis
            NivelFormacion maximoNivelTesis = definirReglaNivelTesis(profesor);
            Collection<IntencionPublicacion> intencionesPublica = new ArrayList<IntencionPublicacion>();
            CargaProfesor carga = new CargaProfesor(cargaProfr, numCursosPregradoMaestria, numeroprivateacionesPlaneadas, cargaEfectiva, otros, descarga, cursos, eventos, proyectosFinanciados, tesisAcargo, profesor, periodoP, maximoNivelTesis, intencionesPublica);
            cargaProfesorFacade.create(carga);

            //4. Crear tarea y alerta
            //4.1 Crea la alerta
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_CARGA_Y_COMPROMISOS);
            String asunto = Notificaciones.ASUNTO_LLENAR_CARGA;
            //4.2 Crea la tarea
            String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
            String cmd = getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR);
            //4.3 Crea correo para informar que se inicio proceso de carga
            HashMap<String, String> paramsNew = new HashMap<String, String>();
            paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CARGA), Long.toString(carga.getId()));
            String asuntoCreacion = Notificaciones.ASUNTO_PROCESO_CARGA_INCIADO;
            String mensajeCreacion = Notificaciones.MENSAJE_PROCESO_CARGA_INCIADO;
            mensajeCreacion = mensajeCreacion.replaceFirst("%1", profesor.getPersona().getNombres() + " " + profesor.getPersona().getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%2", sdfHMS.format(periodoP.getFechaFin()));
            tareaBean.crearTareaPersona(null, tipo, profesor.getPersona().getCorreo(), false, mensajeCreacion, "", periodoP.getFechaInicio(), periodoP.getFechaFin(), cmd, rol, paramsNew, asunto);
            correoBean.enviarMail(profesor.getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

            //5. Actualiza el periodo con las nuevas cargas
            Collection<CargaProfesor> cargasProfesores = periodoP.getCargaProfesores();
            cargasProfesores.add(carga);
            periodoP.setCargaProfesores(cargasProfesores);
            getPeriodoPlaneacion().edit(periodoP);

            String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_CARGA_VACIA_A_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            return respuesta;

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_CARGA_VACIA_A_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_ERR_0003, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * configura los timmers para enviar al director del depto los correos de aviso de fin del proceso de carga y compromisos
     * @param timestamp
     * @param valId
     */
    private void crearTimersAvisoFinProcesoCYC(Timestamp timestamp, String valId) {
        String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ADMON_CARGRA_Y_C) + "-" + valId;

        Long fechaActual = timestamp.getTime();
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        Long fechaHaceUndia = fechaActual - undia;
        Long fecha2Dias = fechaHaceUndia - undia;
        Long fecha3Dias = fecha2Dias - undia;

        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(fechaHaceUndia), mensajeAsociado,
                "CargaYCompromisosBean", this.getClass().getName(), "crearTimersAvisoFinProcesoCYC", "Este timer se crea porque al comenzar el proceso de carga se prepara el momento para avisar un dia antes el fin de este proceso");
        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(fecha2Dias), mensajeAsociado,
                "CargaYCompromisosBean", this.getClass().getName(), "crearTimersAvisoFinProcesoCYC", "Este timer se crea porque al comenzar el proceso de carga se prepara el momento para avisar dos dias antes el fin de este proceso");
        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(fecha3Dias), mensajeAsociado,
                "CargaYCompromisosBean", this.getClass().getName(), "crearTimersAvisoFinProcesoCYC", "Este timer se crea porque al comenzar el proceso de carga se prepara el momento para avisar tres dias antes el fin de este proceso");
    }

    /**
     * crea el timer para el cierre automatico del periodo de carga y compromisos
     * @param fechaF
     * @param valId
     */
    private void crearTimerCerrarProcesoCyC(Timestamp fechaF, String valId) {

        String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_CERRAR_PROCESO_CARGRA_Y_C) + "-" + valId;
        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, fechaF, mensajeAsociado,
                "CargaYCompromisosBean", this.getClass().getName(), "crearTimerCerrarProcesoCyC", "Este timer se crea porque al comenzar el proceso de carga se prepara el momento para el cierre automatico de este proceso");
    }

    /**
     * metodo llamado por los timers cuando se cumplen segun el caso cierra el proceso o envia correo al director
     * @param parametro
     */
    public void manejarTimerCYC(String parametro) {
        String[] pamrametros = parametro.split("-");
        if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ADMON_CARGRA_Y_C))) {
            enviarCorreoRecordatorioDireector(Long.parseLong(pamrametros[1].trim()));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_CERRAR_PROCESO_CARGRA_Y_C))) {
            cerrarProcesoCargaYCompromisosPerido(Long.parseLong(pamrametros[1].trim()));
        }
    }

    /**
     * envia correo al director del depto, para notificar el cierre o que la fecha de cierre se acerque
     * @param parseLong
     */
    private void enviarCorreoRecordatorioDireector(long parseLong) {
        Long id = new Long(parseLong);
        PeriodoPlaneacion periodoNot = periodoPlaneacionFacade.find(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (periodoNot != null && periodoNot.isActual() == true) {
            //Crear correo para informar que se creó la inscripción
            String correoDirectorCYC = getConstanteBean().getConstante(Constantes.VAL_CORREO_DIRECTOR_MB);
            Persona p = personaFacade.findByCorreo(correoDirectorCYC);
            String asuntoCreacion = Notificaciones.ASUNTO_FINAL_PROCESO_CARGA_CERCA;
            String mensajeCreacion = Notificaciones.MENSAJE_FINAL_PROCESO_CARGA_CERCA;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", p.getNombres() + " " + p.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", periodoNot.getPeriodo());

            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdf.format(periodoNot.getFechaFin()));
            correoBean.enviarMail(correoDirectorCYC, asuntoCreacion, null, null, null, mensajeCreacion);
        }
    }

    /**
     * cierra el proceso de carga y compromisos  de un periodo, y termina sus tareaas...
     * @param idperiodo
     */
    private void cerrarProcesoCargaYCompromisosPerido(long idPeriodo) {
        Long id = new Long(idPeriodo);
        PeriodoPlaneacion periodoNot = periodoPlaneacionFacade.find(id);
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_CARGA_Y_COMPROMISOS);
        if (periodoNot != null && periodoNot.isActual() == true) {
            //cerrar periodo
            periodoNot.setActual(false);
            periodoPlaneacionFacade.edit(periodoNot);
            //poner tarea en vencida
            Collection<CargaProfesor> cargaProfesores = periodoNot.getCargaProfesores();
            for (CargaProfesor cargaProfesorrr : cargaProfesores) {
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CARGA), cargaProfesorrr.getId().toString());
                TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, propiedades));
                if (tarea != null) {
                    tareaSencillaBean.realizarTareaPorId(tarea.getId());
                }
            }
        }
    }

    /**
     * busca el ultimo periodo de carga y compromisos llenado por un profesor
     * @param xml
     * @return
     */
    public String darCargaUltimoPeriodoProfesor(String xml) {
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Collection<CargaProfesor> cargasPRof = cargaProfesorFacade.findByCorreo(secCorreo.getValor());
            CargaProfesor mejor = null;
            //busca en el periodo actual la carga del profesor
            for (CargaProfesor cargaProfesor : cargasPRof) {
                PeriodoPlaneacion ptemp = cargaProfesor.getPeriodoPlaneacion();
                if (mejor != null && ptemp.getFechaFin().after(mejor.getPeriodoPlaneacion().getFechaFin())) {
                    mejor = cargaProfesor;
                } else if (mejor == null) {
                    mejor = cargaProfesor;
                }
            }
            if (mejor != null) {
                Secuencia secCargaProfesor = getConversor().crearSecuenciaCargaProfesor(mejor);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secCargaProfesor);

                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            } else {
                Secuencia secCargaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR), "");
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secCargaProfesor);

                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_ERR_0003, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarCargaProfesorPorCorreoYNombrePeriodo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secNombrePeriodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO));

            String correoProf = secCorreo.getValor();
            String nombrePeriodo = secNombrePeriodo.getValor();
            //busca en el periodo actual (prod) si esta el periodo buscado
            PeriodoPlaneacion periodoP = periodoPlaneacionFacade.findByNombre(nombrePeriodo);
            //si el periodo esta busca la carga y la devuelve
            if (periodoP != null) {
                CargaProfesor cargaP = cargaProfesorFacade.findCargaByCorreoProfesorYNombrePeriodo(correoProf, nombrePeriodo);
                if (cargaP != null) {
                    Secuencia secCargaProfesor = getConversor().crearSecuenciaCargaProfesor(cargaP);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(secCargaProfesor);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_POR_CORREO_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
                }

                Secuencia secCargaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR), "");
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secCargaProfesor);

                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_POR_CORREO_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            } else {
                //: llamar a historicos para buscar la carga
                String resp = beanCargaHistoricas.consultarCargaPorCorreoPeriodo(xml);
                return resp;
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_POR_CORREO_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * obtiene la carga del profesor segun el id(solo se usa para el periodo actual)
     * @param xml
     * @return
     */
    public String consultarCargaProfesor(String xml) {
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia idsec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCarga = Long.parseLong(idsec.getValor().trim());
            CargaProfesor carga = cargaProfesorFacade.find(idCarga);
            Secuencia secCargaProfesor = getConversor().crearSecuenciaCargaProfesor(carga);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCargaProfesor);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * agrega un curso a la carga de un profesor
     * @param xml
     * @return
     */
    public String agregarCurso(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSOS));

            Collection<CursoPlaneado> cursos = getConversor().obtenerCursos(secCursos);
            CursoPlaneado curso = cursos.iterator().next();
            curso.setId(null);
            curso.setProfesor(cargaProf);
            cursoPlaneadoFacade.create(curso);
            Collection<CursoPlaneado> cursosProfesor = cargaProf.getCursos();
            cursosProfesor.add(curso);
            cargaProf.setCursos(cursosProfesor);
            cargaProf.setNumCursosPregradoMaestria(cargaProf.getNumCursosPregradoMaestria() + 1);
            cargaProfesorFacade.edit(cargaProf);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * agrega una intencion de publicacion a las cargas con las que se deba asociar
     * @param xml
     * @return
     */
    public String agregarIntencionPublicacion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            Secuencia secPeriodo = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));
            Secuencia secIDperiodo = secPeriodo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idPeriodo = Long.parseLong(secIDperiodo.getValor().trim());

            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secPublicaciones = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PUBLICACIONES));

            Collection<IntencionPublicacion> publicaciones = getConversor().obtenerPublicacionesDeSecuencia(secPublicaciones, idPeriodo);
            IntencionPublicacion intencionP = publicaciones.iterator().next();
            Collection<CargaProfesor> coautores = intencionP.getCoAutores();

            coautores.add(cargaProf);
            intencionP.setCoAutores(coautores);
            intencionPublicacionFacade.create(intencionP);
            IntencionPublicacion intencionPersistida = intencionPublicacionFacade.find(intencionP.getId());

            Collection<CargaProfesor> cargas = intencionP.getCoAutores();
            for (CargaProfesor cargaProfe : cargas) {
                Collection<IntencionPublicacion> publicacionesProf = cargaProfe.getIntencionPublicaciones();
                publicacionesProf.add(intencionPersistida);
                cargaProfe.setIntencionPublicaciones(publicacionesProf);
                TipoPublicacion tipoP = intencionPersistida.getTipoPublicacion();
                if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_REVISTA))) {
                    cargaProfe.setNumRevistas(cargaProfe.getNumRevistas() + 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CONGRESO_INTERNACIONAL))) {
                    cargaProfe.setNumCongresosInternacio(cargaProfe.getNumCongresosInternacio() + 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CONGRESO_NACIONAL))) {
                    cargaProfe.setNumCongresosNacionales(cargaProfe.getNumCongresosNacionales() + 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CAPITULO_LIBRO))) {
                    cargaProfe.setNumCapitulosLibro(cargaProfe.getNumCapitulosLibro() + 1);
                }
                cargaProfe.setNumeroPublicacionesPlaneadas(cargaProfe.getNumeroPublicacionesPlaneadas() + 1);
                cargaProfesorFacade.edit(cargaProfe);
            }

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_INTENCION_PUBLICACION_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_INTENCION_PUBLICACION_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    /**
     * agrega una tesis a un profesor
     * @param xml
     * @return
     */
    public String agregarAsesoriaTesis(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secInfoTesises = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));

            Collection<DireccionTesis> infoTesises = getConversor().obtenerListaAsesoriaTesisDeSecuencia(secInfoTesises);
            DireccionTesis nuevaTesis = infoTesises.iterator().next();

            if (nuevaTesis.getDirectorTesis() == null) {
                nuevaTesis.setDirectorTesis(cargaProf);
            }
            direccionTesisFacade.create(nuevaTesis);
            Collection<DireccionTesis> tesisACargo = cargaProf.getTesisAcargo();
            tesisACargo.add(nuevaTesis);
            cargaProf.setTesisAcargo(tesisACargo);

            //suma 1 al contador de tesis correspondiente
            if (nuevaTesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_1))) {
                cargaProf.setNumeroEstudiantesTesis1(cargaProf.getNumeroEstudiantesTesis1() + 1);
            } else if (nuevaTesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_2))) {
                cargaProf.setNumeroEstudiantesTesis2(cargaProf.getNumeroEstudiantesTesis2() + 1);
            } else if (nuevaTesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_2_PENDIENTE))) {
                cargaProf.setNumeroEstudiantesTesis2Pendiente(cargaProf.getNumeroEstudiantesTesis2Pendiente() + 1);
            } else if (nuevaTesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_PROYECTO_DE_GRADO))) {
                cargaProf.setNumeroEstudiantesPregrado(cargaProf.getNumeroEstudiantesPregrado() + 1);
            } else if (nuevaTesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_DOCTORADO))) {
                cargaProf.setNumeroEstudiantesDoctorado(cargaProf.getNumeroEstudiantesDoctorado() + 1);
            }
            cargaProfesorFacade.edit(cargaProf);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_ASESORIA_TESIS_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_ASESORIA_TESIS_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * agrega un proyecto financiado a todas las cargas correspondientes
     * @param xml
     * @return
     */
    public String agregarProyectoFinanciado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            Secuencia secPeriodo = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));
            Secuencia secIDperiodo = secPeriodo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idPeriodo = Long.parseLong(secIDperiodo.getValor().trim());

            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secInfoTesises = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS));
            Collection<ProyectoFinanciado> infoTesises = getConversor().obtenerListaProyectosFinanciadosDeSecuencia(secInfoTesises, idPeriodo);
            ProyectoFinanciado nuevaProyecto = infoTesises.iterator().next();
            Collection<CargaProfesor> cargasProyecto = nuevaProyecto.getProfesores();
            cargasProyecto.add(cargaProf);
            nuevaProyecto.setProfesores(cargasProyecto);
            proyectoFinanciadoFacadeLocal.create(nuevaProyecto);

            //agrega el proyecto a todos los profesores que participan en el
            for (CargaProfesor cargaProfesor : cargasProyecto) {
                Collection<ProyectoFinanciado> proyectosDelProfesor = cargaProfesor.getProyectosFinanciados();
                proyectosDelProfesor.add(nuevaProyecto);
                cargaProfesor.setProyectosFinanciados(proyectosDelProfesor);
                cargaProfesor.setNumeroProyectosFinanciados(cargaProfesor.getNumeroProyectosFinanciados() + 1);
                cargaProfesorFacade.edit(cargaProfesor);
            }

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROYECTO_FINANCIADO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROYECTO_FINANCIADO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * agrega otro tipo de actividad a la carga de un profesor
     * @param xml
     * @return
     */
    public String agregarOtraActividad(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS));

            Collection<OtrasActividades> cursos = getConversor().obtenerOtrasActividadesDeSecuencia(secCursos);
            OtrasActividades curso = cursos.iterator().next();
            curso.setId(null);
            otrasActividadesFacadeLocal.create(curso);

            Collection<OtrasActividades> cursosProfesor = cargaProf.getOtros();
            cursosProfesor.add(curso);
            cargaProf.setOtros(cursosProfesor);

            cargaProfesorFacade.edit(cargaProf);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_OTRA_ACTIVIDAD_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_OTRA_ACTIVIDAD_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * agrega una descarga al profesor correspondiente
     * @param xml
     * @return
     */
    public String agregarDescarga(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DESCARGA_PROFESOR));
            if (secCursos != null) {
                DescargaProfesor descarga = descargaProfesorFacadeLocal.findByNombre(secCursos.getValor());
                cargaProf.setDescarga(descarga);
                cargaProf.setCargaEfectiva((1.0) - descarga.getValor());
                cargaProfesorFacade.edit(cargaProf);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_DESCARGA_PROFESOR_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

            } else {
                DescargaProfesor d = descargaProfesorFacadeLocal.findByNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SIN_DESCARGA));
                cargaProf.setDescarga(d);
                cargaProf.setCargaEfectiva(1.0);
                cargaProfesorFacade.edit(cargaProf);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_DESCARGA_PROFESOR_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_DESCARGA_PROFESOR_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * elimina un curso de la carga correspondiente
     * @param xml
     * @return
     */
    public String eliminarCursoCargaYCompromisos(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());

            //busca la carga correspondiente
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSOS));
            Collection<CursoPlaneado> cursos = getConversor().obtenerCursos(secCursos);
            CursoPlaneado cursoP = cursos.iterator().next();

            //elimina el curso
            cursoPlaneadoFacade.remove(cursoP);
            cargaProf.setNumCursosPregradoMaestria(cargaProf.getNumCursosPregradoMaestria() - 1);
            Collection<CursoPlaneado> cursosProfesor = cargaProf.getCursos();
            cursosProfesor.remove(cursoP);
            cargaProf.setCursos(cursosProfesor);
            cargaProfesorFacade.edit(cargaProf);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * elimina una publicacion de la carga correspondiente(si mas profesores tienen asociada dicha carga, a los otros no se les elimina)
     * @param xml
     * @return
     */
    public String eliminarPublicacion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            Secuencia secPeriodo = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));
            Secuencia secIDperiodo = secPeriodo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idPeriodo = Long.parseLong(secIDperiodo.getValor().trim());

            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secPublicaciones = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PUBLICACIONES));

            Collection<IntencionPublicacion> publicaciones = getConversor().obtenerPublicacionesDeSecuencia(secPublicaciones, idPeriodo);
            IntencionPublicacion intencionP = publicaciones.iterator().next();
            //eliminar el profesor de la publicacion y la publicacion del profesor
            intencionP = intencionPublicacionFacade.find(intencionP.getId());
            Collection<CargaProfesor> cargaDePubliacion = intencionP.getCoAutores();
            cargaDePubliacion.remove(cargaProf);
            intencionP.setCoAutores(cargaDePubliacion);

            publicaciones = cargaProf.getIntencionPublicaciones();
            publicaciones.remove(intencionP);
            cargaProf.setIntencionPublicaciones(publicaciones);
            //cambiar el numero de pub del profesor
            cargaProf.setNumeroPublicacionesPlaneadas(cargaProf.getNumeroPublicacionesPlaneadas() - 1);
            // guardar
            intencionPublicacionFacade.edit(intencionP);
            //----------quitar numero de carga------------------------------------------
            TipoPublicacion tipoP = intencionP.getTipoPublicacion();
            if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_REVISTA))) {
                cargaProf.setNumRevistas(cargaProf.getNumRevistas() - 1);
            } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CONGRESO_INTERNACIONAL))) {
                cargaProf.setNumCongresosInternacio(cargaProf.getNumCongresosInternacio() - 1);
            } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CONGRESO_NACIONAL))) {
                cargaProf.setNumCongresosNacionales(cargaProf.getNumCongresosNacionales() - 1);
            } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CAPITULO_LIBRO))) {
                cargaProf.setNumCapitulosLibro(cargaProf.getNumCapitulosLibro() - 1);
            }
            //==========================================================================
            cargaProfesorFacade.edit(cargaProf);

            //revisar si la publicacion no tiene coautores borrarla
            if (intencionP.getCoAutores().isEmpty()) {
                intencionPublicacionFacade.remove(intencionP);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INTENCION_PUBLICACION_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * elimina una tesis de la caga correspondiente
     * @param xml
     * @return
     */
    public String eliminarAsesoriaTesisCYC(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
            Collection<DireccionTesis> cursos = getConversor().obtenerListaAsesoriaTesisDeSecuencia(secCursos);
            DireccionTesis tesis = cursos.iterator().next();
            Collection<DireccionTesis> dirsProf = cargaProf.getTesisAcargo();
            dirsProf.remove(tesis);
            cargaProf.setTesisAcargo(dirsProf);

            // resta uno al contador correspondiente
            if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_1))) {
                cargaProf.setNumeroEstudiantesTesis1(cargaProf.getNumeroEstudiantesTesis1() - 1);
            } else if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_2))) {
                cargaProf.setNumeroEstudiantesTesis2(cargaProf.getNumeroEstudiantesTesis2() - 1);
            } else if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_2_PENDIENTE))) {
                cargaProf.setNumeroEstudiantesTesis2Pendiente(cargaProf.getNumeroEstudiantesTesis2Pendiente() - 1);
            } else if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_PROYECTO_DE_GRADO))) {
                cargaProf.setNumeroEstudiantesPregrado(cargaProf.getNumeroEstudiantesPregrado() - 1);
            } else if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_DOCTORADO))) {
                cargaProf.setNumeroEstudiantesDoctorado(cargaProf.getNumeroEstudiantesDoctorado() - 1);
            }
            //elimina la tesis y actualiza la carga
            direccionTesisFacade.remove(tesis);
            cargaProfesorFacade.edit(cargaProf);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ASESORIA_TESIS_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ASESORIA_TESIS_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * elimina un proyecto financiado de la carga correspondiente (si mas profesores participan en el proyecto, a estos no se les elimina)
     * @param xml
     * @return
     */
    public String eliminarProyectosFinanciados(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            Secuencia secPeriodo = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));
            Secuencia secIDperiodo = secPeriodo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idPeriodo = Long.parseLong(secIDperiodo.getValor().trim());

            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secPublicaciones = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS));

            Collection<ProyectoFinanciado> publicaciones = getConversor().obtenerListaProyectosFinanciadosDeSecuencia(secPublicaciones, idPeriodo);
            ProyectoFinanciado proyectoP = publicaciones.iterator().next();
            //eliminar el profesor de la publicacion y la publicacion del profesor
            proyectoP = proyectoFinanciadoFacadeLocal.find(proyectoP.getId());
            Collection<CargaProfesor> cargaDePubliacion = proyectoP.getProfesores();
            cargaDePubliacion.remove(cargaProf);
            proyectoP.setProfesores(cargaDePubliacion);
            publicaciones = cargaProf.getProyectosFinanciados();
            publicaciones.remove(proyectoP);
            cargaProf.setProyectosFinanciados(publicaciones);
            //cambiar el numero de pub del profesor
            cargaProf.setNumeroPublicacionesPlaneadas(cargaProf.getNumeroProyectosFinanciados() - 1);
            // guardar
            proyectoFinanciadoFacadeLocal.edit(proyectoP);
            cargaProfesorFacade.edit(cargaProf);
            //revisar si la publicacion no tiene coautores borrarla
            if (proyectoP.getProfesores().isEmpty()) {
                proyectoFinanciadoFacadeLocal.remove(proyectoP);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROYECTO_FINANCIADO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROYECTO_FINANCIADO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * elimina una actividad a la carga correspondiente
     * @param xml
     * @return
     */
    public String eliminarOtrasActividades(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS));
            Collection<OtrasActividades> cursos = getConversor().obtenerOtrasActividadesDeSecuencia(secCursos);
            OtrasActividades tesis = cursos.iterator().next();

            Collection<OtrasActividades> cursosProfesor = cargaProf.getOtros();
            cursosProfesor.remove(tesis);
            cargaProf.setOtros(cursosProfesor);

            otrasActividadesFacadeLocal.remove(tesis);
            cargaProfesorFacade.edit(cargaProf);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_OTRA_ACTIVIDAD_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_OTRA_ACTIVIDAD_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public String editarCurso(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));

            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CURSOS));
            Collection<CursoPlaneado> cursos = getConversor().obtenerCursos(secCursos);
            CursoPlaneado cursoP = cursos.iterator().next();

            CursoPlaneado cursoAnterior = cursoPlaneadoFacade.find(cursoP.getId());
            cursoP.setProfesor(cursoAnterior.getProfesor());

            cursoPlaneadoFacade.edit(cursoP);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String editarPublicacion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            Secuencia secPeriodo = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));
            Secuencia secIDperiodo = secPeriodo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idPeriodo = Long.parseLong(secIDperiodo.getValor().trim());

            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProfe = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secPublicaciones = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PUBLICACIONES));
            if (secPublicaciones != null) {
                Collection<IntencionPublicacion> publicaciones = getConversor().obtenerPublicacionesDeSecuencia(secPublicaciones, idPeriodo);
                IntencionPublicacion intencionP = publicaciones.iterator().next();
                IntencionPublicacion intencionDBD = intencionPublicacionFacade.find(intencionP.getId());
                //------------------disminuir numero de publicaciones---------------
                TipoPublicacion tipoP = intencionDBD.getTipoPublicacion();
                if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_REVISTA))) {
                    cargaProfe.setNumRevistas(cargaProfe.getNumRevistas() - 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CONGRESO_INTERNACIONAL))) {
                    cargaProfe.setNumCongresosInternacio(cargaProfe.getNumCongresosInternacio() - 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CONGRESO_NACIONAL))) {
                    cargaProfe.setNumCongresosNacionales(cargaProfe.getNumCongresosNacionales() - 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CAPITULO_LIBRO))) {
                    cargaProfe.setNumCapitulosLibro(cargaProfe.getNumCapitulosLibro() - 1);
                }
                //------------------------------------------------------------------
                //--------colocar lo nuevo------------------------------------------
                tipoP = intencionP.getTipoPublicacion();
                if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_REVISTA))) {
                    cargaProfe.setNumRevistas(cargaProfe.getNumRevistas() + 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CONGRESO_INTERNACIONAL))) {
                    cargaProfe.setNumCongresosInternacio(cargaProfe.getNumCongresosInternacio() + 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CONGRESO_NACIONAL))) {
                    cargaProfe.setNumCongresosNacionales(cargaProfe.getNumCongresosNacionales() + 1);
                } else if (tipoP.getTipoPublicacion().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION_CAPITULO_LIBRO))) {
                    cargaProfe.setNumCapitulosLibro(cargaProfe.getNumCapitulosLibro() + 1);
                }
                //------------------------------------------------------------------
                Collection<CargaProfesor> cargaDePubliacion = intencionDBD.getCoAutores();
                intencionP.setCoAutores(cargaDePubliacion);
                intencionPublicacionFacade.edit(intencionP);
                if (intencionP.getCoAutores().isEmpty()) {
                    intencionPublicacionFacade.remove(intencionP);
                }
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_INTENCION_PUBLICACION_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

            } else {
                throw new Exception("xml mal formado en eliminarPublicacion");
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_INTENCION_PUBLICACION_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String editarDireccionTesis(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);

            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES));
            Collection<DireccionTesis> cursos = getConversor().obtenerListaAsesoriaTesisDeSecuencia(secCursos);

            DireccionTesis tesis = cursos.iterator().next();
            DireccionTesis tesisBD = direccionTesisFacade.find(tesis.getId());
            tesis.setDirectorTesis(tesisBD.getDirectorTesis());
            //---------------------kitar estudiante anterior-----------------
            if (tesisBD.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_1))) {
                cargaProf.setNumeroEstudiantesTesis1(cargaProf.getNumeroEstudiantesTesis1() - 1);
            } else if (tesisBD.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_2))) {
                cargaProf.setNumeroEstudiantesTesis2(cargaProf.getNumeroEstudiantesTesis2() - 1);
            } else if (tesisBD.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_2_PENDIENTE))) {
                cargaProf.setNumeroEstudiantesTesis2Pendiente(cargaProf.getNumeroEstudiantesTesis2Pendiente() - 1);
            } else if (tesisBD.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_PROYECTO_DE_GRADO))) {
                cargaProf.setNumeroEstudiantesPregrado(cargaProf.getNumeroEstudiantesPregrado() - 1);
            } else if (tesisBD.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_DOCTORADO))) {
                cargaProf.setNumeroEstudiantesDoctorado(cargaProf.getNumeroEstudiantesDoctorado() - 1);
            }
            //---------aGREGAR NUMERO NUEVO ESTUDIANTE-----------------------
            if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_1))) {
                cargaProf.setNumeroEstudiantesTesis1(cargaProf.getNumeroEstudiantesTesis1() + 1);
            } else if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_2))) {
                cargaProf.setNumeroEstudiantesTesis2(cargaProf.getNumeroEstudiantesTesis2() + 1);
            } else if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_2_PENDIENTE))) {
                cargaProf.setNumeroEstudiantesTesis2Pendiente(cargaProf.getNumeroEstudiantesTesis2Pendiente() + 1);
            } else if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_PROYECTO_DE_GRADO))) {
                cargaProf.setNumeroEstudiantesPregrado(cargaProf.getNumeroEstudiantesPregrado() + 1);
            } else if (tesis.getEstadoTesis().getNivelTesis().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TESIS_DOCTORADO))) {
                cargaProf.setNumeroEstudiantesDoctorado(cargaProf.getNumeroEstudiantesDoctorado() + 1);
            }
            //---------------------------------------------------------------
            direccionTesisFacade.edit(tesis);
            cargaProfesorFacade.edit(cargaProf);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_ASESORIA_TESIS_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_ASESORIA_TESIS_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public String editarProyectoFinanciado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia secPeriodo = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));
            Secuencia secIDperiodo = secPeriodo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idPeriodo = Long.parseLong(secIDperiodo.getValor().trim());

            Secuencia secPublicaciones = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS));

            Collection<ProyectoFinanciado> publicaciones = getConversor().obtenerListaProyectosFinanciadosDeSecuencia(secPublicaciones, idPeriodo);
            ProyectoFinanciado proyectoP = publicaciones.iterator().next();

            // guardar cambios
            ProyectoFinanciado proyectoBD = proyectoFinanciadoFacadeLocal.find(proyectoP.getId());
            proyectoP.setProfesores(proyectoBD.getProfesores());
            proyectoFinanciadoFacadeLocal.edit(proyectoP);

            //revisar si la publicacion no tiene coautores borrarla
            if (proyectoP.getProfesores().isEmpty()) {
                proyectoFinanciadoFacadeLocal.remove(proyectoP);
            }

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_PROYECTO_FINANCIADO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_PROYECTO_FINANCIADO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String editarOtraActividad(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));

            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS));
            Collection<OtrasActividades> actividades = getConversor().obtenerOtrasActividadesDeSecuencia(secCursos);
            OtrasActividades otraActividad = actividades.iterator().next();

            otrasActividadesFacadeLocal.edit(otraActividad);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_OTRA_ACTIVIDAD_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_OTRA_ACTIVIDAD_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String editarDescargaProfesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);
            Secuencia secCursos = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DESCARGA_PROFESOR));
            if (secCursos != null) {
                DescargaProfesor descarga = descargaProfesorFacadeLocal.findByNombre(secCursos.getValor());
                cargaProf.setDescarga(descarga);
                cargaProf.setCargaEfectiva((1.0) - descarga.getValor());
                cargaProfesorFacade.edit(cargaProf);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_DESCARGA_PROFESOR_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            } else {
                DescargaProfesor descarga = descargaProfesorFacadeLocal.findByNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SIN_DESCARGA));
                cargaProf.setDescarga(descarga);
                cargaProf.setCargaEfectiva(1.0);
                cargaProfesorFacade.edit(cargaProf);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CURSO_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /***
     * metodo que decide para un profesor, hasta de que nivel de tesis puede ser asesor:
     * si el profesor es de doctorado todos, si es de maestria solo pregrado
     * @param profesor
     * @return
     */
    private NivelFormacion definirReglaNivelTesis(Profesor profesor) {
        if (profesor.getNivelPlanta() != null && profesor.getNivelPlanta().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_INSTRUCTOR))) {
            return nivelFormacionRemote.findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PREGRADO));
        } else {
            return nivelFormacionRemote.findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_DOCTORADO));
        }
    }

    /**
     * metodo que elimina la tarea de llenar la carga para un profesor
     * @param xml
     * @return
     */
    public String terminarCargaProfesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia idCarga = infoBasica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long idCargaProfesor = Long.parseLong(idCarga.getValor().trim());
            CargaProfesor cargaProf = cargaProfesorFacade.find(idCargaProfesor);

            //: no esta ne xml pero es para cuando un profesor termina la tarea no le sigan llegando notificaciones mientras
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_CARGA_Y_COMPROMISOS);
            Properties propiedades = new Properties();
            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CARGA), cargaProf.getId().toString());

            TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, propiedades));
            if (tarea != null) {
                tareaSencillaBean.realizarTareaPorId(tarea.getId());
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_TERMINAR_TAREA_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_TERMINAR_TAREA_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_ERR_0003, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }
    //----------------------------------------------------------------
    //---               OTROS SERVICIOS                 --------------
    //----------------------------------------------------------------

    /**
     * metodo que devuelve los tipos de publicacion existentes
     * @param xml
     * @return
     */
    public String darTiposDePublicacion(String xml) {
        try {
            Collection<TipoPublicacion> tiposPubli = tipoPublicacionFacade.findAll();
            Secuencia secTiposPublicacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_PUBLICACION), "");
            for (TipoPublicacion elem : tiposPubli) {
                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PUBLICACION), elem.getTipoPublicacion());
                secTiposPublicacion.agregarSecuencia(sec);
            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            secs.add(secTiposPublicacion);
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_PUBLICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_PUBLICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_ERR_0003, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que devuelve los periodos existentes de carga y compromisos, consulta tanto en prod como en historicos
     * @param xml
     * @return
     */
    public String darPeriodosPlaneacion(String xml) {
        try {
            //consulta secuencia de períodos históricos
            String respPeriodosHistoricos = beanCargaHistoricas.consultarPeriodos(xml);
            Secuencia secRespuestaPeriodosHistoricos = parser.leerRespuesta(respPeriodosHistoricos);
            Secuencia secPeriodosRespuesta = secRespuestaPeriodosHistoricos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS));

            //construye secuencia de salida
            Secuencia secPeriodos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS), "");
            Collection<Secuencia> periodosHistoricosSecuencias = secPeriodosRespuesta.getSecuencias();
            if (periodosHistoricosSecuencias != null && !periodosHistoricosSecuencias.isEmpty()) {
                for (Secuencia secPeriodoRespuesta : periodosHistoricosSecuencias) {
                    //extrae valores de la respuesta
                    String id = secPeriodoRespuesta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor();
                    String nombre = secPeriodoRespuesta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();
                    String fechaInicio = secPeriodoRespuesta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO)).getValor();
                    String fechaFin = secPeriodoRespuesta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN)).getValor();
                    String estado = secPeriodoRespuesta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)).getValor();

                    //construye secuencia de salida
                    Secuencia secPeriodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION), "");
                    secPeriodo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id));
                    secPeriodo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), nombre));
                    secPeriodo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), fechaInicio));
                    secPeriodo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), fechaFin));
                    secPeriodo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), estado));
                    secPeriodos.agregarSecuencia(secPeriodo);
                }
            }

            //agrega los períodos actuales
            Collection<PeriodoPlaneacion> perios = periodoPlaneacionFacade.findAll();
            if (perios != null && perios.size() > 0) {
                for (PeriodoPlaneacion periodoPlaneacion : perios) {
                    Secuencia secP = getConversor().getSecuenciaPeriodo(periodoPlaneacion);
                    secPeriodos.agregarSecuencia(secP);
                }
            }

            //construye lista de secuencia de salida
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            secs.add(secPeriodos);

            return parser.generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_PLANEACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_PLANEACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_ERR_0003, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que devuelve los diferentes nivles de tesis (proyecto de grado, tesis1, tesis 2, tesis2 pendiente, doctorado)
     * @param xml
     * @return
     */
    public String darNivelesTesis(String xml) {
        try {
            Collection<NivelTesis> nivelesT = nivelTesisFacadeRemote.findAll();
            Secuencia secNivelesTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVELES_ESTADO_TESIS), "");
            for (NivelTesis elem : nivelesT) {
                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS), elem.getNivelTesis());
                secNivelesTesis.agregarSecuencia(sec);
            }
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            secs.add(secNivelesTesis);
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_NIVELES_ESTADO_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_NIVELES_ESTADO_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_ERR_0003, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que devuelbe los tipos de descarga existentes en el departamento (director, coordinador, vice-decanatura)
     * @param xml
     * @return
     */
    public String darTiposDeDescargaProfesor(String xml) {
        try {
            Collection<DescargaProfesor> descargas = descargaProfesorFacadeLocal.findAll();
            Collection<Secuencia> secs = new ArrayList<Secuencia>();
            Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DESCARGAS_PROFESOR), "");
            for (DescargaProfesor d : descargas) {
                secPrincipal.agregarSecuencia(getConversor().getSecuenciaDescarga(d));
            }
            secs.add(secPrincipal);
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_DESCARGAS_PROFESOR_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_DESCARGAS_PROFESOR_CYC), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_ERR_0003, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public CargaProfesorFacadeLocal getCargaProfesor() {
        return cargaProfesorFacade;
    }

    public ParserT getParser() {
        return parser;
    }

    public PeriodoPlaneacionFacadeLocal getPeriodoPlaneacion() {
        return periodoPlaneacionFacade;
    }

    public ProfesorFacadeRemote getProfesorFacade() {
        return profesorFacade;
    }

    public ConversorCargaYCompromisos getConversor(){
        return new ConversorCargaYCompromisos(seccionFacadeRemote, cursoFacadeRemote, tesis1Facade, tesis2Facade, proyectoDeGradoFacade, nivelTesisFacadeRemote, tipoPublicacionFacade, nivelFormacionRemote, estudianteFacadeRemote, cargaProfesorFacade, constanteBean);
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    /**
     * metodo llamado cuando se va a crear un nuevo periodo de planeacion,
     * @throws Exception
     */
    private void manejoCierreTotalPeriodoPlaneacionAnterior() throws Exception {

        //1.crear reporte final del periodo en cuestion.
        String rutaHistoricos = getConstanteBean().getConstante(Constantes.RUTA_REPORTES_HISTORICOS_CARGA_Y_COMPROMISOS);
        PeriodoPlaneacion periodod = periodoPlaneacionFacade.darUltimoPeriodoPlaneacion();

        if (periodod != null) {
            //Migra a históricos
            migrarCargaYCompromisosPorPeriodo(periodod.getPeriodo());
            //cierra el periodo y finaliza las tareas
            cerrarProcesoCargaYCompromisosPerido(periodod.getId());
            //crea el reporte del periodo en la carpeta de historicos finales, solo si no esta en prueba
            String resp = null;
            Boolean enPrueba = Boolean.parseBoolean(constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_PRUEBA));
            if (enPrueba != null && enPrueba) {
                resp = "TEST";
            } else {
                resp = reporteFacadeRemote.hacerReporteCargaYcompromisosDepto(periodod.getId(), periodod.getPeriodo(), rutaHistoricos, rutaHistoricos);
            }

            if (resp == null || resp.equals("")) {
                throw new Exception("ERROR: pasando a historicos y creando reporte");
            }
            //3.borrar los datos de produccion
            Collection<CargaProfesor> cargas = periodod.getCargaProfesores();
            for (CargaProfesor cargaProfesor : cargas) {
                CargaProfesor carga = cargaProfesorFacade.find(cargaProfesor.getId());
                carga.setEventos(null);
                carga.setOtros(null);
                carga.setCursos(null);
                carga.setTesisAcargo(null);
                carga.setProyectosFinanciados(null);
                carga.setIntencionPublicaciones(null);
                carga.setPeriodoPlaneacion(null);
                cargaProfesorFacade.edit(cargaProfesor);
            }
            Collection<CursoPlaneado> cursos = cursoPlaneadoFacade.findAll();
            for (CursoPlaneado cursoPlaneado : cursos) {
                cursoPlaneadoFacade.remove(cursoPlaneado);
            }
            Collection<OtrasActividades> actividades = otrasActividadesFacadeLocal.findAll();
            for (OtrasActividades otrasActividades : actividades) {
                otrasActividadesFacadeLocal.remove(otrasActividades);
            }
            Collection<DireccionTesis> tesises = direccionTesisFacade.findAll();
            for (DireccionTesis direccionTesis : tesises) {
                direccionTesisFacade.remove(direccionTesis);
            }
            Collection<ProyectoFinanciado> PROYESTOS = proyectoFinanciadoFacadeLocal.findAll();
            for (ProyectoFinanciado proyectoFinanciado : PROYESTOS) {
                proyectoFinanciadoFacadeLocal.remove(proyectoFinanciado);
            }
            Collection<IntencionPublicacion> intencionesP = intencionPublicacionFacade.findAll();
            for (IntencionPublicacion intencionPublicacion : intencionesP) {
                intencionPublicacionFacade.remove(intencionPublicacion);
            }

            periodod.setCargaProfesores(null);
            periodoPlaneacionFacade.edit(periodod);
            for (CargaProfesor cargaProfesor : cargas) {
                cargaProfesorFacade.remove(cargaProfesor);
            }
            eliminarTimersPeriodo(periodod.getId());
            periodoPlaneacionFacade.remove(periodod);
        }
    }

    public String eliminarProfesorBaseDatos(String xml) {
        String correo = xml.split("=")[0];
        String nombrePeriodo = xml.split("=")[1];
        CargaProfesor carga = cargaProfesorFacade.findCargaByCorreoProfesorYNombrePeriodo(correo, nombrePeriodo);
        Long idCarga = carga.getId();

        String clave = xml.split("=")[2];
        if (clave.equals("TEST16022011")) {
            //sacar ids de las cosas que toca borrar
            Collection<Long> idsOtros = darIdsOtros(carga.getOtros());
            Collection<Long> idsCursos = darIdsCursos(carga.getCursos());
            Collection<Long> idsTesis = darIdsTesis(carga.getTesisAcargo());
            Collection<Long> idsProyectosFinanciados = darIdsProyectosFinanciados(carga.getProyectosFinanciados());
            Collection<Long> idsIntencionPublicaciones = darIdsIntencionPublicaciones(carga.getIntencionPublicaciones());

            //elimina la carga del profesor del periodo
            PeriodoPlaneacion periodoP = carga.getPeriodoPlaneacion();
            Collection<CargaProfesor> cargas = periodoP.getCargaProfesores();
            Collection<CargaProfesor> cargas2 = new ArrayList<CargaProfesor>();
            for (CargaProfesor cargaProfesor : cargas) {
                if (cargaProfesor.getId() != idCarga) {
                    cargas2.add(cargaProfesor);
                }
            }
            periodoP.setCargaProfesores(cargas2);
            periodoPlaneacionFacade.edit(periodoP);

            //desconecta de la carga las cosas que tiene
            carga.setEventos(null);
            carga.setOtros(null);
            carga.setCursos(null);
            carga.setTesisAcargo(null);
            carga.setProyectosFinanciados(null);
            carga.setIntencionPublicaciones(null);
            carga.setPeriodoPlaneacion(null);
            cargaProfesorFacade.edit(carga);

            //elimina los cursos del profesor que se desea borrar
            for (Long idCurso : idsCursos) {
                CursoPlaneado cursoPlaneado = cursoPlaneadoFacade.find(idCurso);
                cursoPlaneadoFacade.remove(cursoPlaneado);
            }

            //elimina las actividadse del profesor que se va a borrar
            for (Long idOtro : idsOtros) {
                OtrasActividades otra = otrasActividadesFacadeLocal.find(idOtro);
                otrasActividadesFacadeLocal.remove(otra);
            }

            //elimina las tesis del profesor que se va a borrar
            for (Long idTesis : idsTesis) {
                DireccionTesis dir = direccionTesisFacade.find(idTesis);
                direccionTesisFacade.remove(dir);
            }

            //elimina de cada proyecto financiado en el que el profesor este la carga asociada: si el proyecto no tiene mas profesores lo elimina
            for (Long idproyecto : idsProyectosFinanciados) {
                ProyectoFinanciado py = proyectoFinanciadoFacadeLocal.find(idproyecto);
                Collection<CargaProfesor> profesores = py.getProfesores();
                Collection<CargaProfesor> profesores2 = new ArrayList<CargaProfesor>();
                for (CargaProfesor cargaProfesorFor : profesores) {
                    if (cargaProfesorFor.getId() != idCarga) {
                        profesores2.add(cargaProfesorFor);
                    }
                }
                py.setProfesores(profesores2);
                proyectoFinanciadoFacadeLocal.edit(py);
                if (py.getProfesores().isEmpty()) {
                    proyectoFinanciadoFacadeLocal.remove(py);
                }
            }

            //elimina de las publicaciones en que el profesor esta de las mismas; si la publicacion no tiene mas coautores la elimina
            for (Long idIntencionPublicacion : idsIntencionPublicaciones) {

                IntencionPublicacion publicacion = intencionPublicacionFacade.find(idIntencionPublicacion);
                Collection<CargaProfesor> cargasprofesoresNueva = new ArrayList<CargaProfesor>();
                Collection<CargaProfesor> cargasprofesoresPublicacion = publicacion.getCoAutores();
                for (CargaProfesor cargaProfesor : cargasprofesoresPublicacion) {
                    if (cargaProfesor.getId() != idCarga) {
                        cargasprofesoresNueva.add(cargaProfesor);
                    }
                }
                publicacion.setCoAutores(cargasprofesoresNueva);
                intencionPublicacionFacade.edit(publicacion);
                if (publicacion.getCoAutores().isEmpty()) {
                    intencionPublicacionFacade.remove(publicacion);
                }
            }

            carga = cargaProfesorFacade.find(idCarga);
            cargaProfesorFacade.remove(carga);

            return "ok";

        } else {
            return "Operacion no autorizada";
        }
    }

    private Collection<Long> darIdsOtros(Collection<OtrasActividades> otros) {
        Collection<Long> ids = new ArrayList<Long>();
        for (OtrasActividades otrasActividades : otros) {
            ids.add(otrasActividades.getId());
        }
        return ids;
    }

    private Collection<Long> darIdsCursos(Collection<CursoPlaneado> cursos) {
        Collection<Long> ids = new ArrayList<Long>();
        for (CursoPlaneado otrasActividades : cursos) {
            ids.add(otrasActividades.getId());
        }
        return ids;
    }

    private Collection<Long> darIdsTesis(Collection<DireccionTesis> tesisAcargo) {
        Collection<Long> ids = new ArrayList<Long>();
        for (DireccionTesis otrasActividades : tesisAcargo) {
            ids.add(otrasActividades.getId());
        }
        return ids;
    }

    private Collection<Long> darIdsProyectosFinanciados(Collection<ProyectoFinanciado> proyectosFinanciados) {
        Collection<Long> ids = new ArrayList<Long>();
        for (ProyectoFinanciado otrasActividades : proyectosFinanciados) {
            ids.add(otrasActividades.getId());
        }
        return ids;
    }

    private Collection<Long> darIdsIntencionPublicaciones(Collection<IntencionPublicacion> intencionPublicaciones) {
        Collection<Long> ids = new ArrayList<Long>();
        for (IntencionPublicacion otrasActividades : intencionPublicaciones) {
            ids.add(otrasActividades.getId());
        }
        return ids;
    }

    public String darProyectosFinanciados(String xml) {
        try {
            parser.leerXML(xml);
            Collection<ProyectoFinanciado> proyectos = proyectoFinanciadoFacadeLocal.findAll();
            Secuencia sec = getConversor().getSecuenciaProyectosFinanciados(proyectos);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(sec);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROYECTOS_FINANCIADOS_ULTIMO_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROYECTOS_FINANCIADOS_ULTIMO_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String vincularProfesorAProyectosFinanciados(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia infoBasica = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGA_PROFESOR));
            Secuencia secId = infoBasica.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor().trim());

            CargaProfesor cargaBD = cargaProfesorFacade.find(id);
            Collection<ProyectoFinanciado> losProyectosDelProfesor = cargaBD.getProyectosFinanciados();
            HashMap<Long, ProyectoFinanciado> proyectosProfesor = new HashMap<Long, ProyectoFinanciado>();
            for (ProyectoFinanciado proyectoFinanciado : losProyectosDelProfesor) {
                proyectosProfesor.put(proyectoFinanciado.getId(), proyectoFinanciado);
            }
            HashMap<Long, ProyectoFinanciado> proyectos = new HashMap<Long, ProyectoFinanciado>();
            Secuencia secProyectos = infoBasica.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PROYECTOS_FINANCIADOS));
            Collection<Secuencia> secProyecto = secProyectos.getSecuencias();

            for (Secuencia secuencia : secProyecto) {
                Secuencia secIdProyecto = secuencia.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
                Long idProyecto = Long.parseLong(secIdProyecto.getValor().trim());
                ProyectoFinanciado pory = proyectoFinanciadoFacadeLocal.find(idProyecto);
                //revisar que el profesor no este aun en el proyecto
                Collection<CargaProfesor> profs = pory.getProfesores();
                boolean noEsta = true;
                for (CargaProfesor cargaProfesor : profs) {
                    if (cargaProfesor.getId().equals(id)) {
                        noEsta = false;
                    }
                }
                //si el profesor no esta en la lista de porfesores del proyecto lo mete
                if (noEsta) {
                    profs.add(cargaBD);
                    pory.setProfesores(profs);
                    proyectos.put(idProyecto, pory);
                    //revisa que el profesor no tenga ese proyecto en su lista, si no esta lo mete
                    ProyectoFinanciado delLadoDelProfesor = proyectosProfesor.get(idProyecto);
                    if (delLadoDelProfesor == null) {
                        losProyectosDelProfesor.add(pory);
                    }
                    proyectoFinanciadoFacadeLocal.edit(pory);
                }
            }
            cargaBD = cargaProfesorFacade.find(id);
            cargaBD.setProyectosFinanciados(losProyectosDelProfesor);

            cargaProfesorFacade.edit(cargaBD);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_VINCULAR_CARGAYCOMPROMISOS_PROFESOR_PROYECTO_FINANCIADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_VINCULAR_CARGAYCOMPROMISOS_PROFESOR_PROYECTO_FINANCIADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public void actualizarIndicadoresCarga() {
        List<CargaProfesor> listaCargas = cargaProfesorFacade.findAll();

        for (CargaProfesor cargaProfesor : listaCargas) {

            String correoProfesor = cargaProfesor.getProfesor().getPersona().getCorreo();
            String nombrePeriodo = cargaProfesor.getPeriodoPlaneacion().getPeriodo();
            // 1. Actualizar número de estudiantes en Proyecto de Grado
            Collection<ProyectoDeGrado> listaPregrado = proyectoDeGradoFacade.findByCorreoAsesor(correoProfesor);
            int numPregrado = 0;
            for (ProyectoDeGrado proyectoDeGrado : listaPregrado) {
                if (proyectoDeGrado.getSemestreIniciacion().getNombre().equals(nombrePeriodo)) {
                    /*ANTES estaba tambien la condicion: proyectoDeGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA))
                    pero no tiene sentido, que solo las de un estado se cuenten, si el profesor rechaza la tesis, esta se borra, entonces no hay nececidad de hacer esto*/
                    numPregrado++;
                }
            }
            cargaProfesor.setNumeroEstudiantesPregrado(numPregrado);
            // 2. Actualizar número de estudiantes en Tesis 1
            Collection<Tesis1> listaTesis1 = tesis1Facade.findByCorreoAsesor(correoProfesor);
            int numTesis1 = 0;
            for (Tesis1 tesis1 : listaTesis1) {
                if (tesis1.getSemestreIniciacion().getPeriodo().equals(nombrePeriodo)) {
                    numTesis1++;
                }
            }
            cargaProfesor.setNumeroEstudiantesTesis1(numTesis1);
            // 3. Actualizar número de estudiantes en Tesis 2
            Collection<Tesis2> listaTesis2 = tesis2Facade.findByCorreoAsesor(correoProfesor);
            int numTesis2 = 0;
            for (Tesis2 tesis2 : listaTesis2) {
                if (tesis2.getSemestreInicio().getPeriodo().equals(nombrePeriodo)) {
                    numTesis2++;
                }
            }
            cargaProfesor.setNumeroEstudiantesTesis2(numTesis2);
            //Hacer commit de cambios
            cargaProfesorFacade.edit(cargaProfesor);
        }
    }

    //HISTÓRICOS
    public void migrarCargaYCompromisosPorPeriodo(String periodo) {
        Collection<CargaProfesor> cargas = getCargaProfesor().findByPeriodo(periodo);
        try {
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            Secuencia secCargas = getConversor().crearSecuenciaCargasProfesor(cargas);
            secs.add(secCargas);
            String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_CARGAS_PROFESORES_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
            String resp = beanCargaHistoricas.pasarCargasProfesoresAHistoricos(comandoXML);
            System.out.println("Carga Y cOmromisos:migrarCargaYCompromisosPorPeriodo resp=" + resp);
            if (resp == null) {
                System.out.println("CREO TIMER LINEA 2696 CYC BEAN");
                String mensajeAsociadoMigrarCargas = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_CARGAS_PROFESORES_A_HISTORICOS) + "-";
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarCargas,
                        "CargaYCompromisosBean", this.getClass().getName(), "migrarCargaYCompromisosPorPeriodo", "Este timer se crea porque al comenzar el proceso de carga se prepara el momento para migracion y al ejecutarse esta migración no fue existosa, luego se programa un timer para seguir intendadolo hasta que sea exitoso");
            }
        } catch (Exception ex) {
            Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void eliminarTimersPeriodo(Long idPeriodo) {
        String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ADMON_CARGRA_Y_C) + "-" + idPeriodo;
        String mensajeAsociado2 = getConstanteBean().getConstante(Constantes.CMD_CERRAR_PROCESO_CARGRA_Y_C) + "-" + idPeriodo;

        timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociado);
        timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociado2);
    }

    public String migrarCargasPorFinPeriodo(String xml) {
        try {
            System.out.println("LLEGO A: migrarCargasPorFinPeriodo + CYC");
            parser.leerXML(xml);
            manejoCierreTotalPeriodoPlaneacionAnterior();
            System.out.println("DONE: LLEGO A: migrarCargasPorFinPeriodo + CYC");
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias,
                    getConstanteBean().getConstante(Constantes.CMD_MIGRAR_CYC),
                    getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE),
                    Mensajes.CYC_MSJ_0000, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex);
                String respuesta = getParser().generarRespuesta(new ArrayList(),
                        getConstanteBean().getConstante(Constantes.CMD_MIGRAR_CYC),
                        getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR),
                        Mensajes.CYC_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex1) {
                Logger.getLogger(CargaYCompromisosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }
}
