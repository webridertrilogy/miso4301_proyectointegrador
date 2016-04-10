/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y ComputacióndarCiudadesPorDepartamento
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.nucleo.services;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.despachador.services.DespachadorLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.AutorizacionRemote;
import co.uniandes.sisinfo.serviciosnegocio.AccionVencidaBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.AccionesBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.AlertaMultipleRemote;
import co.uniandes.sisinfo.serviciosnegocio.ArchivosRemote;
import co.uniandes.sisinfo.serviciosnegocio.AspiranteRemote;
import co.uniandes.sisinfo.serviciosnegocio.AuditoriaBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.CargaYCompromisosBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.CarteleraRemote;
import co.uniandes.sisinfo.serviciosnegocio.ConfirmacionRemote;
import co.uniandes.sisinfo.serviciosnegocio.ConflictoHorariosBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import co.uniandes.sisinfo.serviciosnegocio.ConsultaRemote;
import co.uniandes.sisinfo.serviciosnegocio.ContactoBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.ConvenioRemote;
import co.uniandes.sisinfo.serviciosnegocio.ConvocatoriaRemote;
import co.uniandes.sisinfo.serviciosnegocio.CursoRemote;
import co.uniandes.sisinfo.serviciosnegocio.DatosRemote;
import co.uniandes.sisinfo.serviciosnegocio.DocumentoPrivadoRemote;
import co.uniandes.sisinfo.serviciosnegocio.EstudiantePostgradoRemote;
import co.uniandes.sisinfo.serviciosnegocio.ListaNegraRemote;
import co.uniandes.sisinfo.serviciosnegocio.MonitoriaRemote;
import co.uniandes.sisinfo.serviciosnegocio.OfertaRemote;
import co.uniandes.sisinfo.serviciosnegocio.PensumRemote;
import co.uniandes.sisinfo.serviciosnegocio.PreseleccionRemote;
import co.uniandes.sisinfo.serviciosnegocio.ProfesorRemote;
import co.uniandes.sisinfo.serviciosnegocio.ProponenteRemote;
import co.uniandes.sisinfo.serviciosnegocio.ReglaRemote;
import co.uniandes.sisinfo.serviciosnegocio.ReservasBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.SolicitudRemote;
import co.uniandes.sisinfo.serviciosnegocio.VotacionesRemote;
import co.uniandes.sisinfo.serviciosnegocio.GrupoRemote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoRemote;
import co.uniandes.sisinfo.serviciosnegocio.InscripcionesBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.MaterialBibliograficoRemote;
import co.uniandes.sisinfo.serviciosnegocio.NivelFormacionRemote;
import co.uniandes.sisinfo.serviciosnegocio.NivelPlantaBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.ProyectoDeGradoBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.RangoFechasBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.ReportesRemote;
import co.uniandes.sisinfo.serviciosnegocio.TesisBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.CargaGrupoBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.CarteleraCursosBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.ConsultaRolBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.EstudianteBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoTesis1Remote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoTesis2Remote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricosTesisBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoTesisPregradoRemote;
import co.uniandes.sisinfo.serviciosnegocio.IncidenteBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.InscripcionSubAreaInvestiBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.JuradosTesisBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.PeriodoRemote;
import co.uniandes.sisinfo.serviciosnegocio.PersonaRemote;
import co.uniandes.sisinfo.serviciosnegocio.PersonaSoporteBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.ReporteExcepcionesBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.SubareaInvestigacionBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.Tesis1BeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.Tesis2BeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.TimerGenericoBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.AsistenciaGraduadaBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.AuditoriaUsuarioBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.CorreoAuditoriaRemote;
import co.uniandes.sisinfo.serviciosnegocio.CorreoSinEnviarBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.EventoExternoBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.FiltroCorreoBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.LaboratorioBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.ListaNegraReservaCitasRemote;
import co.uniandes.sisinfo.serviciosnegocio.RangoFechasGeneralRemote;
import co.uniandes.sisinfo.serviciosnegocio.ReservaInventarioBeanRemote;
import co.uniandes.sisinfo.serviciosnegocio.TareaMultipleRemote;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Servicios Núcleo
 */
@Stateless
public class NucleoBean implements NucleoRemote, NucleoLocal {

    //----------------------------------------------
    // SERVICIOS
    //----------------------------------------------
    @EJB
    private PersonaRemote personaBean;
    @EJB
    private ProfesorRemote profesorBean;
    @EJB
    private ConsultaRemote consultaBean;
    @EJB
    private ListaNegraRemote listaNegraBean;
    @EJB
    private SolicitudRemote solicitudBean;
    @EJB
    private CursoRemote cursoBean;
    @EJB
    private AspiranteRemote aspiranteBean;
    @EJB
    private MonitoriaRemote monitoriaBean;
    @EJB
    private PreseleccionRemote preseleccionBean;
    @EJB
    private HistoricoRemote historicoBean;
    @EJB
    private HistoricoTesisPregradoRemote historicoTesisPregradoBean;
    @EJB
    private HistoricoTesis1Remote historicoTesis1;
    @EJB
    private HistoricoTesis2Remote historicoTesis2;
    @EJB
    private HistoricosTesisBeanRemote historicoTesis;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private AutorizacionRemote autorizacionBean;
    @EJB
    private DespachadorLocal despachadorBean;
    @EJB
    private DatosRemote datosBean;
    @EJB
    private ConvenioRemote convenioBean;
    @EJB
    private ConfirmacionRemote confirmacionBean;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private PensumRemote pensumBean;
    @EJB
    private EstudiantePostgradoRemote estudiantePostgradoBean;
    @EJB
    private EstudianteBeanRemote estudianteBean;
    @EJB
    private OfertaRemote ofertaBean;
    @EJB
    private ProponenteRemote proponenteBean;
    @EJB
    private ArchivosRemote archivosBean;
    @EJB
    private AlertaMultipleRemote alertaBean;
    @EJB
    private DocumentoPrivadoRemote documentoPrivadoBean;
    @EJB
    private ReglaRemote reglaBean;
    @EJB
    private CarteleraRemote carteleraBean;
    @EJB
    private VotacionesRemote votacionesBean;
    @EJB
    private InscripcionesBeanRemote inscripcionesBean;
    @EJB
    private GrupoRemote grupoBean;
    @EJB
    private MaterialBibliograficoRemote materialBibliograficoBean;
    @EJB
    private ContactoBeanRemote contactoBean;
    @EJB
    private ReportesRemote reportesBean;
    @EJB
    private PeriodoRemote periodoBean;
    @EJB
    private NivelFormacionRemote nivelFormacionBean;
    @EJB
    private NivelPlantaBeanRemote nivelPlantaBean;
    @EJB
    private RangoFechasBeanRemote rangoFechasBean;
    @EJB
    private CargaYCompromisosBeanRemote compromisosBean;
    @EJB
    private ConvocatoriaRemote convocatoriaBean;
    @EJB
    private SubareaInvestigacionBeanRemote subareaBean;
    @EJB
    private TesisBeanRemote tesisBean;
    @EJB
    private ReservasBeanRemote reservasBean;
    @EJB
    private TimerGenericoBeanRemote timerGenericoBean;
    @EJB
    private ConflictoHorariosBeanRemote conflictosBean;
    @EJB
    private CarteleraCursosBeanRemote carteleraCursosRemote;
    @EJB
    private ProyectoDeGradoBeanRemote pgBean;
    @EJB
    private CargaGrupoBeanRemote cargaGrupoBean;
    @EJB
    private ConsultaRolBeanRemote consultaRolBean;
    @EJB
    private ReporteExcepcionesBeanRemote reporteExcepcionesBean;
    @EJB
    private IncidenteBeanRemote incidenteBean;
    @EJB
    private Tesis1BeanRemote tesis1Bean;
    @EJB
    private Tesis2BeanRemote tesis2Bean;
    @EJB
    private JuradosTesisBeanRemote juradoTesisBean;
    @EJB
    private InscripcionSubAreaInvestiBeanRemote inscripcionSubareaBean;
    @EJB
    private PersonaSoporteBeanRemote personaSoporteBean;
    @EJB
    private AuditoriaBeanRemote auditoriaBean;
    @EJB
    private AccionVencidaBeanRemote accionVencidaBean;
    @EJB
    private RangoFechasGeneralRemote rangoFechasGeneralesBean;
    @EJB
    private ListaNegraReservaCitasRemote listaNegraReservaCitasBean;
    @EJB
    private AsistenciaGraduadaBeanRemote asistenciaGraduadaBean;
    @EJB
    private CorreoAuditoriaRemote correoAuditoriaBean;
    @EJB
    private AuditoriaUsuarioBeanRemote auditoriaUsuarioBean;
    @EJB
    private CorreoSinEnviarBeanRemote correoSinEnviarBean;
    @EJB
    private FiltroCorreoBeanRemote filtroCorreoBean;
    @EJB
    private LaboratorioBeanRemote laboratorioBean;
    @EJB
    private ReservaInventarioBeanRemote reservaInventarioBean;
    @EJB
    private AccionesBeanRemote accionesBean;
    @EJB
    private EventoExternoBeanRemote eventoExternoBean;
    private ParserT parserBean;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public NucleoBean() {
        parserBean = new ParserT();
    }

    //----------------------------------------------
    // RESOLUCIÓN DE COMANDOS
    //----------------------------------------------
    @Override
    public String resolverComando(String comandoXML) throws Exception {
        String respuesta = getConstanteBean().getConstante(Constantes.MSJ_TIPO_COMANDO_INVALIDO);
        // Parseo del comando
        parserBean.leerXML(comandoXML);
        String nombreComando = parserBean.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
        String tipoComando = parserBean.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_TIPO_COMANDO));

        try {
            if (!nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION))) {
                Logger.getLogger(NucleoBean.class.getName()).log(Level.INFO, "--->NUCLEOBEAN COMANDO: " + comandoXML);
            }
        } catch (Exception e) {
        }

        if (tipoComando.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA))) {
            //Enruta el comando de consulta
            respuesta = enRutarComandoConsulta(nombreComando, comandoXML);
        } else if (tipoComando.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_PROCESO))) {
            //En ruta el comando al despachador
            parserBean.leerXML(comandoXML);
            Timestamp fa = new Timestamp(new Date().getTime());
            respuesta = despachadorBean.resolverComando(comandoXML);
            reportarALogExcepciones(despachadorBean.getClass().getName(), "resolverComando", respuesta, fa, nombreComando, comandoXML);
        } else {
            respuesta = getConstanteBean().getConstante(Constantes.MSJ_TIPO_COMANDO_INVALIDO);
        }
        try {
            Logger.getLogger(NucleoBean.class.getName()).log(Level.INFO, "--->NUCLEOBEAN RESPUESTA: " + respuesta);
        } catch (Exception e) {
        }
        System.out.println("Retornó respuesta de NucleoBean");
        return respuesta;
    }

    /**
     * Enruta el comando de consulta y retorna la respuesta de acuerdo al servicio pedido
     * @param nombreComando Nombre del comando
     * @param comandoXML Comando en XML
     * @return Respuesta al servicio
     * @throws java.lang.Exception
     */
    private String enRutarComandoConsulta(String nombreComando, String comandoXML) throws Exception {
        String respuesta = getConstanteBean().getConstante(Constantes.MSJ_CONSULTA_INVALIDA);
        Timestamp fa = new Timestamp(new Date().getTime());

        /*
         * Enviamos comando a log de comandos.
         */
        auditoriaBean.guardarXML(nombreComando, comandoXML);

        if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_NIVELES_FORMACION))) {
            respuesta = nivelFormacionBean.consultarNivelesFormacion(comandoXML);
            reportarALogExcepciones(nivelFormacionBean.getClass().getCanonicalName(), "consultarNivelesFormacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_NIVELES_PLANTA))) {
            respuesta = nivelPlantaBean.consultarNivelesPlanta(comandoXML);
            reportarALogExcepciones(nivelFormacionBean.getClass().getCanonicalName(), "consultarNivelesPlanta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DETERMINAR_RESULTADO_VERIFICACION))) {
            respuesta = confirmacionBean.determinarResultadoVerificacion(comandoXML);
            reportarALogExcepciones(confirmacionBean.getClass().getCanonicalName(), "determinarResultadoVerificacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DETERMINAR_SI_HAY_CONVOCATORIA_ABIERTA))) {
            respuesta = convocatoriaBean.determinarSiHayConvocatoriaAbierta(comandoXML);
            reportarALogExcepciones(convocatoriaBean.getClass().getCanonicalName(), "determinarSiHayConvocatoriaAbierta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RETORNAR_PERIODOS_ACADEMICOS))) {
            respuesta = periodoBean.retornarPeriodosAcademicos(comandoXML);
            reportarALogExcepciones(periodoBean.getClass().getCanonicalName(), "retornarPeriodosAcademicos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RECARGAR_TIMERS))) {
            respuesta = timerGenericoBean.recargarTimers(comandoXML);
            reportarALogExcepciones(timerGenericoBean.getClass().getCanonicalName(), "recargarTimers", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ARREGLAR_TIMERS))) {
            respuesta = timerGenericoBean.arreglarTimers(comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIMERS))) {
            respuesta = timerGenericoBean.consultarTimers(comandoXML);
            reportarALogExcepciones(timerGenericoBean.getClass().getCanonicalName(), "consultarTimers", respuesta, fa, nombreComando, comandoXML);
            //Administración de timers
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_TIMER))) {
            respuesta = timerGenericoBean.editarTimer(comandoXML);
            reportarALogExcepciones(timerGenericoBean.getClass().getCanonicalName(), "editarTimer", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DETENER_TIMER))) {
            respuesta = timerGenericoBean.detenerTimerEnMemoria(comandoXML);
            reportarALogExcepciones(timerGenericoBean.getClass().getCanonicalName(), "detenerTimerEnMemoria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TIMER))) {
            respuesta = timerGenericoBean.eliminarTimer(comandoXML);
            reportarALogExcepciones(timerGenericoBean.getClass().getCanonicalName(), "eliminarTimer", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EJECUTAR_TIMER_EN_MEMORIA))) {
            respuesta = timerGenericoBean.ejecutarTimerEnMemoria(comandoXML);
            reportarALogExcepciones(timerGenericoBean.getClass().getCanonicalName(), "ejecutarTimerEnMemoria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIMER))) {
            respuesta = timerGenericoBean.consultarTimer(comandoXML);
            reportarALogExcepciones(timerGenericoBean.getClass().getCanonicalName(), "consultarTimer", respuesta, fa, nombreComando, comandoXML);
        }// Comandos Consultas Estudiante
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_ACADEMICOS))) {
            respuesta = aspiranteBean.darDatosAcademicos(comandoXML);
            reportarALogExcepciones(aspiranteBean.getClass().getCanonicalName(), "darDatosAcademicos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_PERSONALES))) {
            respuesta = aspiranteBean.darDatosPersonales(comandoXML);
            reportarALogExcepciones(aspiranteBean.getClass().getCanonicalName(), "darDatosPersonales", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HORARIO_ESTUDIANTE_POR_LOGIN))) {
            respuesta = consultaBean.darHorarioEstudiantePorLogin(comandoXML);
            reportarALogExcepciones(consultaBean.getClass().getCanonicalName(), "darHorarioEstudiantePorLogin", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO))) {
            respuesta = solicitudBean.darSolicitudesEstudiantePorCodigo(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudesEstudiantePorCodigo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN))) {
            respuesta = solicitudBean.darSolicitudesEstudiantePorLogin(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudesEstudiantePorLogin", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_POR_ID))) {
            respuesta = solicitudBean.darSolicitudPorId(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudPorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_LISTA_NEGRA_POR_CODIGO))) {
            respuesta = listaNegraBean.estaEnListaNegra(comandoXML);
            reportarALogExcepciones(listaNegraBean.getClass().getCanonicalName(), "estaEnListaNegra", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_LISTA_NEGRA_POR_LOGIN))) {
            respuesta = listaNegraBean.estaEnListaNegraLogin(comandoXML);
            reportarALogExcepciones(listaNegraBean.getClass().getCanonicalName(), "estaEnListaNegraLogin", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_NEGRA))) {
            respuesta = listaNegraBean.darListaNegra(comandoXML);
            reportarALogExcepciones(listaNegraBean.getClass().getCanonicalName(), "darListaNegra", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_POR_ESTADO))) {
            respuesta = solicitudBean.darSolicitudesPorEstado(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudesPorEstado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_RESUELTAS_SECRETARIA))) {
            respuesta = solicitudBean.consultarSolicitudesResueltas(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "consultarSolicitudesResueltas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_POR_ESTADO_SECRETARIA))) {
            respuesta = convenioBean.darConveniosSecretaria(comandoXML);
            reportarALogExcepciones(convenioBean.getClass().getCanonicalName(), "darConveniosSecretaria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES))) {
            respuesta = solicitudBean.darSolicitudes(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudes", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_EMERGENCIA))) {
            respuesta = aspiranteBean.darDatosEmergencia(comandoXML);
            reportarALogExcepciones(aspiranteBean.getClass().getCanonicalName(), "darDatosEmergencia", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_OTROS_DEPTOS))) {
            respuesta = aspiranteBean.darMonitoriasOtrosDeptos(comandoXML);
            reportarALogExcepciones(aspiranteBean.getClass().getCanonicalName(), "darMonitoriasOtrosDeptos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_TIENE_SOLICITUDES_POR_LOGIN))) {
            respuesta = solicitudBean.tieneSolicitudesPorLogin(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "tieneSolicitudesPorLogin", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD))) {
            respuesta = solicitudBean.actualizarSolicitud(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "crearLogMensaje", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES))) {
            respuesta = solicitudBean.darSolicitudes(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudes", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_MENSAJE_REGLAS_MONITORIAS))) {
            respuesta = consultaBean.darMensajeReglasMonitorias(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darMensajeReglasMonitorias", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_MONITORIAS))) {
            respuesta = solicitudBean.consultarSolicitudes(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "consultarSolicitudes", respuesta, fa, nombreComando, comandoXML);
            //COMANDOS DESPRESELECCION
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PRESELECCION))) {
            respuesta = preseleccionBean.despreseleccionar(comandoXML);
            reportarALogExcepciones(preseleccionBean.getClass().getCanonicalName(), "despreseleccionar", respuesta, fa, nombreComando, comandoXML);
            //Comandos Rangos Fechas
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGOS_FECHAS))) {

            respuesta = rangoFechasBean.consultarRangos(comandoXML);
            reportarALogExcepciones(rangoFechasBean.getClass().getCanonicalName(), "consultarRangos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RANGO_INICIADO))) {

            respuesta = rangoFechasBean.rangoIniciado(comandoXML);
            reportarALogExcepciones(rangoFechasBean.getClass().getCanonicalName(), "consultarRangos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_RANGOS_FECHAS))) {
            respuesta = rangoFechasBean.crearRangosFechas(comandoXML);
            reportarALogExcepciones(rangoFechasBean.getClass().getCanonicalName(), "crearRangosFechas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_RANGOS_FECHAS))) {
            respuesta = rangoFechasBean.editarRangosFechas(comandoXML);
            reportarALogExcepciones(rangoFechasBean.getClass().getCanonicalName(), "editarRangosFechas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ES_RANGO_VALIDO))) {
            respuesta = rangoFechasBean.esRangoValidoXML(comandoXML);
            reportarALogExcepciones(rangoFechasBean.getClass().getCanonicalName(), "esRangoValidoXML", respuesta, fa, nombreComando, comandoXML);
        }// Comandos Consultas Sesion
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION))) {
            respuesta = autorizacionBean.autorizarYDarDatosBasicosSesion(comandoXML);
            reportarALogExcepciones(autorizacionBean.getClass().getCanonicalName(), "autorizarYDarDatosBasicosSesion", respuesta, fa, nombreComando, comandoXML);
        } // Comandos de Solicitud
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CON_VACANTES))) {
            respuesta = cursoBean.darCursosConVacantes(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darCursosConVacantes", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_FACULTADES_CON_PROGRAMAS))) {
            respuesta = cursoBean.darFacultadesConProgramas(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darFacultadesConProgramas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_FACULTADES))) {
            respuesta = cursoBean.darFacultades(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darFacultades", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_FACULTAD))) {
            respuesta = cursoBean.darProgramasFacultad(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darProgramasFacultad", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_ACADEMICOS))) {
            respuesta = cursoBean.darProgramas(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darProgramas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_CON_VACANTES_POR_CURSO))) {
            respuesta = cursoBean.darSeccionesConVacantesPorCurso(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darSeccionesConVacantesPorCurso", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_SEMESTRE_ACTUAL))) {
            respuesta = cursoBean.darCursosSemestreActual(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darCursosSemestreActual", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_PENDIENTES_POR_MONITORES))) {
            respuesta = cursoBean.darCursosPendientesPorMonitores(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darCursosPendientesPorMonitores", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_DATOS_CURSO))) {
            respuesta = cursoBean.modificarDatosCurso(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "modificarDatosCurso", respuesta, fa, nombreComando, comandoXML);
        }// Comandos para coordinacion
        //else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_RESOLUCION)))
        //  respuesta = resolucionBean.hacerResolucion(comandoXML);
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ALERTA))) {
            respuesta = alertaBean.borrarAlerta(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "borrarAlerta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS))) {
            respuesta = alertaBean.consultarAlertas(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "consultarAlertas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS_TIPO_TAREA))) {
            respuesta = alertaBean.consultarAlertasTipoTarea(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "consultarAlertasTipoTarea", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS_SIN_TAREAS))) {
            respuesta = alertaBean.consultarAlertasSinTareas(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "consultarAlertasSinTareas", respuesta, fa, nombreComando, comandoXML);
            //} else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PARAMETROS_ALERTA))) {
            //   respuesta = alertaBean.consultarParametrosAlerta(comandoXML);
            //   reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "consultarParametrosAlerta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_ALERTAS))) {
            respuesta = alertaBean.crearAlertas(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "crearAlertas", respuesta, fa, nombreComando, comandoXML);
            //} else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_ALERTA))) {
            //  respuesta = alertaBean.editarAlerta(comandoXML);
            //  reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "editarAlerta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODICIDADES))) {
            respuesta = alertaBean.darPeriodicidades(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "darPeriodicidades", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_ALERTAS))) {
            respuesta = alertaBean.darTiposAlertas(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "darTiposAlertas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_PAUSAR_ALERTA))) {
            respuesta = alertaBean.pausarAlerta(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "pausarAlerta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REANUDAR_ALERTA))) {
            respuesta = alertaBean.reanudarAlerta(comandoXML);
            reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "reanudarAlerta", respuesta, fa, nombreComando, comandoXML);
            //} else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ALERTAS_SIN_TAREAS_PARA_COORDINACION))) {
            //respuesta = alertaBean.consultarAlertasSinTareasParaCoordinacion(comandoXML);
            //reportarALogExcepciones(alertaBean.getClass().getCanonicalName(), "consultarAlertasSinTareasParaCoordinacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PROFESOR))) {
            respuesta = profesorBean.actualizarDatosProfesor(comandoXML);
            reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "actualizarDatosProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_MONITORIA_POR_ID_SOLICITUD))) {
            respuesta = monitoriaBean.darDatosMonitoriaPorIdSolicitud(comandoXML);
            reportarALogExcepciones(monitoriaBean.getClass().getCanonicalName(), "darDatosMonitoriaPorIdSolicitud", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_PERIODO_DE_DESPRESELECCION))) {
            respuesta = rangoFechasBean.esRangoValidoXML(comandoXML);
            reportarALogExcepciones(rangoFechasBean.getClass().getCanonicalName(), "esRangoValidoXML", respuesta, fa, nombreComando, comandoXML);
        } //Comandos para profesor
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_PROFESOR))) {
            respuesta = profesorBean.consultarDatosProfesor(comandoXML);
            reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "consultarDatosProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLE_SOLICITUD))) {
            respuesta = solicitudBean.darSolicitudPorId(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudPorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR))) {
            respuesta = cursoBean.darSeccionesPorLoginProfesor(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darSeccionesPorLoginProfesor", respuesta, fa, nombreComando, comandoXML);
        } //CUPI2 201020
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_T2))) {
            respuesta = solicitudBean.consultarMonitoresT2(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "consultarMonitoresT2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_PRESELECCIONAR_MONITORES_T2))) {
            respuesta = preseleccionBean.preseleccionarMonitoresT2(comandoXML);
            reportarALogExcepciones(preseleccionBean.getClass().getCanonicalName(), "preseleccionarMonitoresT2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_CANDIDATOS_T2))) {
            respuesta = solicitudBean.darSolicitudesMonitoresCandidatosT2(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudesMonitoresCandidatosT2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCIONES_SIN_MONITOR_T2))) {
            respuesta = solicitudBean.consultarSeccionesSinMonitorT2(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "consultarSeccionesSinMonitorT2", respuesta, fa, nombreComando, comandoXML);
        } //FIN CUPI2 201020
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_SIN_PRESELECCION))) {
            respuesta = preseleccionBean.darSeccionesSinPreseleccion(comandoXML);
            reportarALogExcepciones(preseleccionBean.getClass().getCanonicalName(), "darSeccionesSinPreseleccion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION))) {
            respuesta = solicitudBean.darSolicitudesEnAspiracionPorSeccion(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudesEnAspiracionPorSeccion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION))) {
            respuesta = solicitudBean.darSolicitudesPreseleccionadasPorSeccion(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudesPreseleccionadasPorSeccion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_SIN_MONITORIA))) {
            respuesta = solicitudBean.darSolicitudesSinMonitoria(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSolicitudesSinMonitoria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_APOI_POR_CORREO))) {
            respuesta = solicitudBean.darSeccionesAPOIPorCorreo(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "darSeccionesAPOIPorCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_NOTA_MONITOR))) {
            respuesta = monitoriaBean.actualizarNotaMonitor(comandoXML);
            reportarALogExcepciones(monitoriaBean.getClass().getCanonicalName(), "actualizarNotaMonitor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_ARCHIVOS_PROFESOR))) {
            respuesta = archivosBean.darArchivosProfesor(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "darArchivosProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_ARCHIVO))) {
            respuesta = archivosBean.darInfoArchivo(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "darInfoArchivo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_OBTENER_RUTA_DIRECTORIO_ARCHIVO))) {
            respuesta = archivosBean.darRutaDirectorioArchivo(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "darRutaDirectorioArchivo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_REEMPLAZO_ARCHIVO))) {
            respuesta = archivosBean.confirmarReemplazoArchivo(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "confirmarReemplazoArchivo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_RUTA_ARCHIVO_POR_CRN_Y_TIPO))) {
            respuesta = archivosBean.darRutaArchivoPorCRNyTipo(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "darRutaArchivoPorCRNyTipo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_PLANEACION_ACADEMICA))) {
            respuesta = archivosBean.darPeriodosConPlaneacionAcademica(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "darPeriodosConPlaneacionAcademica", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPOS_INVESTIGACION))) {
            respuesta = profesorBean.consultarGruposInvestigacion(comandoXML);
            reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "consultarGruposInvestigacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_PROFESORES))) {
            respuesta = profesorBean.consultarDatosProfesores(comandoXML);
            reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "consultarDatosProfesores", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_TIPO))) {
            respuesta = profesorBean.consultarProfesoresPorTipo(comandoXML);
            reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "consultarProfesoresPorTipo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SUBAREAS_INVESTIGACION))) {
            respuesta = subareaBean.obtenerSubareasInvestigacion(comandoXML);
            reportarALogExcepciones(subareaBean.getClass().getCanonicalName(), "obtenerSubareasInvestigacion", respuesta, fa, nombreComando, comandoXML);
        }// ComandoHistorico
        //        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_HISTORICO)))
        //          respuesta = historicoBean.consultarHistorico(comandoXML);
        //        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CRITERIOS_BUSQUEDA_HISTORICO)))
        //          respuesta = historicoBean.darCriteriosBusquedaHistorico(comandoXML);
        //Comandos paises
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_NACIONALIDADES))) {
            respuesta = datosBean.darPaises(comandoXML);
            reportarALogExcepciones(datosBean.getClass().getCanonicalName(), "darPaises", respuesta, fa, nombreComando, comandoXML);
        } //Comandos tipos de documento
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_DOCUMENTO))) {
            respuesta = datosBean.darTiposDocumento(comandoXML);
            reportarALogExcepciones(datosBean.getClass().getCanonicalName(), "darTiposDocumento", respuesta, fa, nombreComando, comandoXML);
        } //Comando tarea
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO))) {
            respuesta = tareaBean.darTareasCorreoEstado(comandoXML);
            reportarALogExcepciones(tareaBean.getClass().getCanonicalName(), "darTareasCorreoEstado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO_TIPO))) {
            respuesta = tareaBean.darTareasCorreoEstadoTipo(comandoXML);
            reportarALogExcepciones(tareaBean.getClass().getCanonicalName(), "darTareasCorreoEstadoTipo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_CORREO_ESTADO_SIN_CADUCAR))) {
            respuesta = tareaBean.darTareasCorreoEstadoSinCaducar(comandoXML);
            reportarALogExcepciones(tareaBean.getClass().getCanonicalName(), "darTareasCorreoEstadoSinCaducar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TAREAS_POR_LOGIN))) {
            respuesta = tareaBean.darTareaCorreo(comandoXML);
            reportarALogExcepciones(tareaBean.getClass().getCanonicalName(), "darTareaCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TAREAS_PENDIENTES_VENCIDAS_USUARIO))) {
            respuesta = tareaBean.darTareasVencidasUsuario(comandoXML);
            reportarALogExcepciones(tareaBean.getClass().getCanonicalName(), "darTareasVencidasUsuario", respuesta, fa, nombreComando, comandoXML);
        } //Comandos secretaria
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CONVENIOS))) {
            respuesta = convenioBean.darConvenios(comandoXML);
            reportarALogExcepciones(convenioBean.getClass().getCanonicalName(), "darConvenios", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA))) {
            respuesta = solicitudBean.cancelarSolicitudPorCarga(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "cancelarSolicitudPorCarga", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CURSO))) {
            respuesta = cursoBean.getCurso(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "getCurso", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SECCION))) {
            respuesta = cursoBean.darSeccion(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darSeccion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PENSUM))) {
            respuesta = pensumBean.darPensum(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darPensum", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_ESTUDIANTE))) {
            respuesta = convenioBean.registrarConvenioPendienteEstudiante(comandoXML);
            reportarALogExcepciones(convenioBean.getClass().getCanonicalName(), "registrarConvenioPendienteEstudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA))) {
            respuesta = solicitudBean.cancelarSolicitudPorCarga(comandoXML);
            reportarALogExcepciones(solicitudBean.getClass().getCanonicalName(), "cancelarSolicitudPorCarga", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_DEPARTAMENTO))) {
            respuesta = convenioBean.registrarConvenioPendienteDepartamento(comandoXML);
            reportarALogExcepciones(convenioBean.getClass().getCanonicalName(), "registrarConvenioPendienteDepartamento", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_FIRMAS_ESTUDIANTES))) {
            respuesta = convenioBean.registrarFirmasEstudiantes(comandoXML);
            reportarALogExcepciones(convenioBean.getClass().getCanonicalName(), "registrarFirmasEstudiantes", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_FIRMAS_DEPARTAMENTO))) {
            respuesta = convenioBean.registrarFirmasDepartamento(comandoXML);
            reportarALogExcepciones(convenioBean.getClass().getCanonicalName(), "registrarFirmasDepartamento", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_RADICACION))) {
            respuesta = convenioBean.registrarConvenioPendienteRadicacion(comandoXML);
            reportarALogExcepciones(convenioBean.getClass().getCanonicalName(), "registrarConvenioPendienteRadicacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RADICAR_CONVENIOS))) {

            respuesta = convenioBean.radicarConvenio(comandoXML);
            reportarALogExcepciones(convenioBean.getClass().getCanonicalName(), "radicarConvenio", respuesta, fa, nombreComando, comandoXML);
        } //Comandos Cupi2
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CUPI2))) {
            respuesta = cursoBean.darCursosCupi2(comandoXML);
            reportarALogExcepciones(cursoBean.getClass().getCanonicalName(), "darCursosCupi2", respuesta, fa, nombreComando, comandoXML);
        }//Comandos utilitarios
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PARAMETROS_TIPO_TAREA))) {
            respuesta = tareaBean.darParametrosTipoTarea(comandoXML);
            reportarALogExcepciones(tareaBean.getClass().getCanonicalName(), "darParametrosTipoTarea", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ID_TAREA_PARAMETROS_TIPO))) {
            respuesta = tareaBean.consultarIdTareaPorParametrosTipo(comandoXML);
            reportarALogExcepciones(tareaBean.getClass().getCanonicalName(), "consultarIdTareaPorParametrosTipo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_ARCHIVO))) {
            respuesta = archivosBean.confirmarSubidaArchivo(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "confirmarSubidaArchivo", respuesta, fa, nombreComando, comandoXML);
        } //COMANDOS BOLSA EMPLEO
        //Comandos Estudiante
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_HOJA_VIDA))) {
            respuesta = estudiantePostgradoBean.actualizarHojaVida(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "actualizarHojaVida", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_INFORMACION_ACADEMICA))) {
            respuesta = estudiantePostgradoBean.actualizarInformacionAcademica(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "actualizarInformacionAcademica", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_INFORMACION_PERSONAL))) {
            respuesta = estudiantePostgradoBean.actualizarInformacionPersonal(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "actualizarInformacionPersonal", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE))) {

            respuesta = estudiantePostgradoBean.consultarEstudiante(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "consultarEstudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE_POR_ID))) {
            respuesta = estudiantePostgradoBean.consultarEstudiantePorId(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "consultarEstudiantePorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTES))) {
            respuesta = estudiantePostgradoBean.consultarEstudiantes(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "consultarEstudiantes", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_HOJA_VIDA))) {
            respuesta = estudiantePostgradoBean.consultarHojaVida(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "consultarHojaVida", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INFORMACION_ACADEMICA))) {
            respuesta = estudiantePostgradoBean.consultarInformacionAcademica(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "consultarInformacionAcademica", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INFORMACION_PERSONAL))) {
            respuesta = estudiantePostgradoBean.consultarInformacionPersonal(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "consultarInformacionPersonal", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_ESTUDIANTE))) {

            respuesta = estudiantePostgradoBean.crearEstudiante(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "crearEstudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ESTUDIANTE))) {
            respuesta = estudiantePostgradoBean.eliminarEstudiante(comandoXML);
            reportarALogExcepciones(estudiantePostgradoBean.getClass().getCanonicalName(), "eliminarEstudiante", respuesta, fa, nombreComando, comandoXML);
        } //Comandos Oferta
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_OFERTA))) {
            respuesta = ofertaBean.actualizarOferta(comandoXML);
            reportarALogExcepciones(ofertaBean.getClass().getCanonicalName(), "actualizarOferta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTA))) {
            respuesta = ofertaBean.consultarOferta(comandoXML);
            reportarALogExcepciones(ofertaBean.getClass().getCanonicalName(), "consultarOferta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTAS))) {
            respuesta = ofertaBean.consultarOfertas(comandoXML);
            reportarALogExcepciones(ofertaBean.getClass().getCanonicalName(), "consultarOfertas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTAS_PROPONENTE))) {
            respuesta = ofertaBean.consultarOfertasProponente(comandoXML);
            reportarALogExcepciones(ofertaBean.getClass().getCanonicalName(), "consultarOfertasProponente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_OFERTA))) {
            respuesta = ofertaBean.crearOferta(comandoXML);
            reportarALogExcepciones(ofertaBean.getClass().getCanonicalName(), "crearLogMensaje", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_OFERTA))) {
            respuesta = ofertaBean.eliminarOferta(comandoXML);
            reportarALogExcepciones(ofertaBean.getClass().getCanonicalName(), "eliminarOferta", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APLICAR_OFERTA))) {
            respuesta = ofertaBean.aplicarAOferta(comandoXML);
            reportarALogExcepciones(ofertaBean.getClass().getCanonicalName(), "aplicarAOferta", respuesta, fa, nombreComando, comandoXML);
        } //Comandos Proponente
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_PROPONENTE))) {
            respuesta = proponenteBean.actualizarProponente(comandoXML);
            reportarALogExcepciones(proponenteBean.getClass().getCanonicalName(), "actualizarProponente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE))) {
            respuesta = proponenteBean.consultarProponente(comandoXML);
            reportarALogExcepciones(proponenteBean.getClass().getCanonicalName(), "consultarProponente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTES))) {
            respuesta = proponenteBean.consultarProponentes(comandoXML);
            reportarALogExcepciones(proponenteBean.getClass().getCanonicalName(), "consultarProponentes", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_PROPONENTE))) {

            respuesta = proponenteBean.crearProponente(comandoXML);
            reportarALogExcepciones(proponenteBean.getClass().getCanonicalName(), "crearProponente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROPONENTE))) {
            respuesta = proponenteBean.eliminarProponente(comandoXML);
            reportarALogExcepciones(proponenteBean.getClass().getCanonicalName(), "eliminarProponente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE_POR_LOGIN))) {
            respuesta = proponenteBean.consultarProponentePorLogin(comandoXML);
            reportarALogExcepciones(proponenteBean.getClass().getCanonicalName(), "consultarProponentePorLogin", respuesta, fa, nombreComando, comandoXML);
        } //**********************************************************************
        //COMANDOS Documentos Privados
        //**********************************************************************
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_METADATOS_DOCPRIVADO))) {
            respuesta = documentoPrivadoBean.actualizarMetadatosDocumentoPrivado(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "actualizarMetadatosDocumentoPrivado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_DOCPRIVADO))) {
            respuesta = documentoPrivadoBean.confirmarSubidaDocumentoPrivado(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "confirmarSubidaDocumentoPrivado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_DOCPRIVADO))) {
            respuesta = documentoPrivadoBean.consultarDatosDocumentoPrivado(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "consultarDatosDocumentoPrivado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DOCUMENTOS_PRIVADOS))) {
            respuesta = documentoPrivadoBean.consultarDocumentosPrivados(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "consultarDocumentosPrivados", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_DESCARGA_DOCPRIVADO))) {
            respuesta = documentoPrivadoBean.darInfoDescargaDocumentoPrivado(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "darInfoDescargaDocumentoPrivado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_DOCPRIVADO))) {//documentoPrivadoBean
            respuesta = documentoPrivadoBean.eliminarDocumentoPrivado(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "eliminarDocumentoPrivado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_METADATOS_DOCPRIVADO))) {
            respuesta = documentoPrivadoBean.subirMetadatosDocumentoPrivado(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "subirMetadatosDocumentoPrivado", respuesta, fa, nombreComando, comandoXML);
        }//NUEVOS PARA ARBOL DOCUMENTOS
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_NODO_ARBOL_DOCUMENTOS))) {
            respuesta = documentoPrivadoBean.eliminarNodoArbolDocumentos(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "eliminarNodoArbolDocumentos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_NODO_ARBOL_DOCUMENTOS))) {
            respuesta = documentoPrivadoBean.editarNodoArbolDocumentos(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "editarNodoArbolDocumentos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_ARBOL_DOCUMENTOS))) {
            respuesta = documentoPrivadoBean.darArbolDocumentos(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "darArbolDocumentos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_NODO_ARBOL_DOCUMENTOS))) {
            respuesta = documentoPrivadoBean.agregarNodoArbolDocumentos(comandoXML);
            reportarALogExcepciones(documentoPrivadoBean.getClass().getCanonicalName(), "agregarNodoArbolDocumentos", respuesta, fa, nombreComando, comandoXML);
        } /*else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ARBOL_DOCUMENTOS))){
        respuesta = documentoPrivadoBean.eliminarArbolDocumentos(comandoXML);
        }*/ //Reglas
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_REGLA))) {
            respuesta = reglaBean.crearRegla(comandoXML);
            reportarALogExcepciones(reglaBean.getClass().getCanonicalName(), "crearRegla", respuesta, fa, nombreComando, comandoXML);
        } //Cartelera
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA))) {
            //TODO: arreglar bien esto:
            respuesta = conflictosBean.actualizarFechasConflictoHorario(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "actualizarFechasConflictoHorario", respuesta, fa, nombreComando, comandoXML);
        } /**
         * VOTACIONES
         */
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_VOTACION))) {
            respuesta = votacionesBean.crearVotacion(comandoXML);
            reportarALogExcepciones(votacionesBean.getClass().getCanonicalName(), "crearVotacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_GRUPOS))) {
            respuesta = grupoBean.darGrupos(comandoXML);
            reportarALogExcepciones(grupoBean.getClass().getCanonicalName(), "darGrupos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_VOTAR))) {
            respuesta = votacionesBean.votar(comandoXML);
            reportarALogExcepciones(votacionesBean.getClass().getCanonicalName(), "votar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPO_POR_NOMBRE))) {
            respuesta = grupoBean.darGrupoPorNombre(comandoXML);
            reportarALogExcepciones(grupoBean.getClass().getCanonicalName(), "darGrupoPorNombre", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PERSONAS_SIN_VOTACION))) {
            respuesta = votacionesBean.darPersonasSinVotacion(comandoXML);
            reportarALogExcepciones(votacionesBean.getClass().getCanonicalName(), "darPersonasSinVotacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACION_CON_CANDIDATOS))) {
            respuesta = votacionesBean.darVotacionConCandidatos(comandoXML);
            reportarALogExcepciones(votacionesBean.getClass().getCanonicalName(), "darVotacionConCandidatos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACIONES))) {
            respuesta = votacionesBean.darVotaciones(comandoXML);
            reportarALogExcepciones(votacionesBean.getClass().getCanonicalName(), "darVotaciones", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACIONES_POR_CORREO))) {
            respuesta = votacionesBean.darVotacionesPorCorreo(comandoXML);
            reportarALogExcepciones(votacionesBean.getClass().getCanonicalName(), "darVotacionesPorCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_VOTACION))) {
            respuesta = votacionesBean.darResultadoVotacion(comandoXML);
            reportarALogExcepciones(votacionesBean.getClass().getCanonicalName(), "darResultadoVotacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_ESTADO_VOTANTES_POR_ID_VOTACION))) {
            respuesta = votacionesBean.darEstadoVotantesPorIdVotacion(comandoXML);
            reportarALogExcepciones(votacionesBean.getClass().getCanonicalName(), "darEstadoVotantesPorIdVotacion", respuesta, fa, nombreComando, comandoXML);
        } /**
         * INSCRIPCIONES
         */
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_INSCRIPCION))) {
            respuesta = inscripcionesBean.crearInscripcion(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "crearInscripcion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_CORREO))) {
            respuesta = inscripcionesBean.modificarInscripcionUsuarioPorCorreo(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "modificarInscripcionUsuarioPorCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_INSCRIPCION_USUARIO_POR_ID))) {
            respuesta = inscripcionesBean.darInscripcionUsuarioPorId(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "darInscripcionUsuarioPorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION_USUARIO))) {
            respuesta = inscripcionesBean.modificarInscripcionUsuario(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "modificarInscripcionUsuario", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_INSCRIPCION))) {
            respuesta = inscripcionesBean.editarInscripcion(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "editarInscripcion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CERRAR_INSCRIPCION))) {
            respuesta = inscripcionesBean.cerrarInscripcion(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "cerrarInscripcion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION))) {
            respuesta = inscripcionesBean.eliminarInscripcion(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "eliminarInscripcion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_INSCRIPCION_ADMON_POR_ID))) {
            respuesta = inscripcionesBean.darDetallesAdmonInscripcion(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "darDetallesAdmonInscripcion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCION_ADMON))) {
            respuesta = inscripcionesBean.darListaInscripcionesAdmin(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "darListaInscripcionesAdmin", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCIONES_USUARIO))) {
            respuesta = inscripcionesBean.darListaInscripcionesUsuarioPorCorreo(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "darListaInscripcionesUsuarioPorCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_INSCRIPCIONES))) {
            respuesta = inscripcionesBean.darInscripciones(comandoXML);
            reportarALogExcepciones(inscripcionesBean.getClass().getCanonicalName(), "darInscripciones", respuesta, fa, nombreComando, comandoXML);
        } /**
         * SOLICITUD MATERIAL BIBLIOGRAFICO
         */
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_DEPARTAMENTO_POR_ESTADO))) {
            respuesta = materialBibliograficoBean.consultarSolicitudesDepartamentoPorEstado(comandoXML);
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "consultarSolicitudesDepartamentoPorEstado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUDES_SOLICITANTE))) {
            respuesta = materialBibliograficoBean.consultarSolicitudesSolicitante(comandoXML);
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "consultarSolicitudesSolicitante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO))) {
            respuesta = materialBibliograficoBean.crearSolicitudCompraMaterialBibliografico(comandoXML);
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "crearSolicitudCompraMaterialBibliografico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD_MATERIAL_BIBLIOGRAFICO))) {
            respuesta = materialBibliograficoBean.eliminarSolicitudMaterialBibliografico(comandoXML);
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "eliminarSolicitudMaterialBibliografico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_AUTORIZACION_COMPRA_MATERIAL_BIBLIOGRAFICO))) {
            respuesta = materialBibliograficoBean.enviarAutorizacionCompraMaterialBibliografico(comandoXML);
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "enviarAutorizacionCompraMaterialBibliografico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO_POR_ID_SOLICITUD))) {
            respuesta = materialBibliograficoBean.consultarSolicitudPorIdSolicitud(comandoXML);
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "consultarSolicitudPorIdSolicitud", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_COMPRA_MATERIAL_BIBLIOGRAFICO_BIBLIOTECA))) {
            respuesta = materialBibliograficoBean.enviarConfirmacionCompraMaterialBibliograficoBiblioteca(comandoXML);//	CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIA
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "enviarConfirmacionCompraMaterialBibliograficoBiblioteca", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CONFIRMACION_LLEGO_A_BIBLIOTECA_MATERIAL_BIBLIOGRAFICO))) {
            respuesta = materialBibliograficoBean.enviarConfirmacionLLegoMaterialABiblioteca(comandoXML);
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "enviarConfirmacionLLegoMaterialABiblioteca", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_SOLICITUD_COMPRA_MATERIAL_BIBLIOGRAFICO))) {
            respuesta = materialBibliograficoBean.editarSolicitudCompraMaterialBibliografico(comandoXML);
            reportarALogExcepciones(materialBibliograficoBean.getClass().getCanonicalName(), "editarSolicitudCompraMaterialBibliografico", respuesta, fa, nombreComando, comandoXML);
        } /**--------------------------------------------------------
         * REPORTES
         *----------------------------------------------------------*/
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GENERAR_REPORTES))) {
            respuesta = reportesBean.hacerReporte(comandoXML);
            reportarALogExcepciones(reportesBean.getClass().getCanonicalName(), "hacerReporte", respuesta, fa, nombreComando, comandoXML);
        } /**------------------------------------------------------------------------
         * CRM
         *-----------------------------------------------------------------------*/
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_ARCHIVO_TERMINOS_Y_CONDICIONES))) {
            respuesta = contactoBean.darInformacionArchivoTerminosYCondiciones(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "darInformacionArchivoTerminosYCondiciones", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS_LIGHT))) {
            respuesta = contactoBean.consultarContactosLight(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "consultarContactosLight", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS))) {
            respuesta = contactoBean.consultarContactos(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "consultarContactos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTO))) {
            respuesta = contactoBean.consultarContacto(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "consultarContacto", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CONTACTO))) {
            respuesta = contactoBean.eliminarContacto(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "eliminarContacto", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_CONTACTO))) {
            respuesta = contactoBean.editarContacto(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "editarContacto", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO))) {
            respuesta = contactoBean.agregarContacto(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "agregarContacto", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_CONTACTOS))) {
            respuesta = contactoBean.enviarCorreo(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "enviarCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECTORES_CORPORATIVOS))) {
            respuesta = contactoBean.consultarSectoresCorporativos(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "consultarSectoresCorporativos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ARCHIVO_ADJUNTO))) {
            respuesta = contactoBean.borrarArchivo(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "borrarArchivo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CARGOS))) {
            respuesta = contactoBean.darCargos(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "cmdDarCargos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GUARDAR_INSCRIPCION))) {
            respuesta = contactoBean.agregarInscripcion(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "cmdGuardarInscripcion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_LOGIN_PUBLICO))) {
            respuesta = contactoBean.loginPublico(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "cmdLoginPublico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_USUARIO_PUBLICO))) {
            respuesta = contactoBean.registrarUsuarioPublico(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "cmdRegistrarUsuarioPublico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTIVAR_USUARIO_PUBLICO))) {
            respuesta = contactoBean.activarUsuarioPublico(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "cmdActivarUsuarioPublico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION_EVENTO_EXTERNO))) {
            respuesta = contactoBean.eliminarInscripcion(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "eliminarInscripcion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS_CON_PARAMETROS))) {
            respuesta = contactoBean.consultarContactosFiltrados(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "consultarContactosFiltrados", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INSCRITOS_EVENTO_EXTERNO))) {
            respuesta = contactoBean.consultarInscritrosEventoExterno(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "consultarInscritrosEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } /**
         * COMPROMISOS
         */
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_INICIAR_PROCESO_CARGA_COMPROMISOS))) {
            respuesta = compromisosBean.inicarProcesoCarga(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "inicarProcesoCarga", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CERRAR_PROCESO_CARGRA_Y_C))) {
            respuesta = compromisosBean.terminarCargaProfesor(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "terminarCargaProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR))) {
            respuesta = compromisosBean.consultarCargaProfesor(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "consultarCargaProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_POR_CORREO_PERIODO))) {
            respuesta = compromisosBean.consultarCargaProfesorPorCorreoYNombrePeriodo(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "consultarCargaProfesorPorCorreoYNombrePeriodo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CARGAYCOMPROMISOS_PROFESOR_ULTIMO_PERIODO))) {
            respuesta = compromisosBean.darCargaUltimoPeriodoProfesor(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "darCargaUltimoPeriodoProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DESCARGAS_PROFESOR_CYC))) {
            respuesta = compromisosBean.darTiposDeDescargaProfesor(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "darTiposDeDescargaProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_PUBLICACION))) {
            respuesta = compromisosBean.darTiposDePublicacion(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "darTiposDePublicacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_NIVELES_ESTADO_TESIS))) {
            respuesta = compromisosBean.darNivelesTesis(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "darNivelesTesis", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_PLANEACION))) {
            respuesta = compromisosBean.darPeriodosPlaneacion(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "darPeriodosPlaneacion", respuesta, fa, nombreComando, comandoXML);
            //cursos
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CURSO_CYC))) {
            respuesta = compromisosBean.agregarCurso(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "agregarCurso", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_CURSO_CYC))) {
            respuesta = compromisosBean.editarCurso(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "editarCurso", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CURSO_CYC))) {
            respuesta = compromisosBean.eliminarCursoCargaYCompromisos(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "eliminarCursoCargaYCompromisos", respuesta, fa, nombreComando, comandoXML);
            //publicaciones
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_INTENCION_PUBLICACION_CYC))) {
            respuesta = compromisosBean.agregarIntencionPublicacion(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "agregarIntencionPublicacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_INTENCION_PUBLICACION_CYC))) {
            respuesta = compromisosBean.editarPublicacion(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "editarPublicacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INTENCION_PUBLICACION_CYC))) {
            respuesta = compromisosBean.eliminarPublicacion(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "eliminarPublicacion", respuesta, fa, nombreComando, comandoXML);
            //asesorías de tesis
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_ASESORIA_TESIS_CYC))) {
            respuesta = compromisosBean.agregarAsesoriaTesis(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "agregarAsesoriaTesis", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_ASESORIA_TESIS_CYC))) {
            respuesta = compromisosBean.editarDireccionTesis(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "editarDireccionTesis", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ASESORIA_TESIS_CYC))) {
            respuesta = compromisosBean.eliminarAsesoriaTesisCYC(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "eliminarAsesoriaTesisCYC", respuesta, fa, nombreComando, comandoXML);
            //proyectos financiados
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROYECTO_FINANCIADO_CYC))) {
            respuesta = compromisosBean.agregarProyectoFinanciado(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "agregarProyectoFinanciado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_PROYECTO_FINANCIADO_CYC))) {
            respuesta = compromisosBean.editarProyectoFinanciado(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "editarProyectoFinanciado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROYECTO_FINANCIADO_CYC))) {
            respuesta = compromisosBean.eliminarProyectosFinanciados(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "eliminarProyectosFinanciados", respuesta, fa, nombreComando, comandoXML);
            //otras actividades
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_OTRA_ACTIVIDAD_CYC))) {
            respuesta = compromisosBean.agregarOtraActividad(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "agregarOtraActividad", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_OTRA_ACTIVIDAD_CYC))) {
            respuesta = compromisosBean.editarOtraActividad(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "editarOtraActividad", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_OTRA_ACTIVIDAD_CYC))) {
            respuesta = compromisosBean.eliminarOtrasActividades(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "eliminarOtrasActividades", respuesta, fa, nombreComando, comandoXML);
            //descarga laboral
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_DESCARGA_PROFESOR_CYC))) {
            respuesta = compromisosBean.agregarDescarga(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "agregarDescarga", respuesta, fa, nombreComando, comandoXML);
        } //-----
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROYECTOS_FINANCIADOS_ULTIMO_PERIODO))) {
            respuesta = compromisosBean.darProyectosFinanciados(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "darProyectosFinanciados", respuesta, fa, nombreComando, comandoXML);
            //descarga laboral
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_VINCULAR_CARGAYCOMPROMISOS_PROFESOR_PROYECTO_FINANCIADO))) {
            respuesta = compromisosBean.vincularProfesorAProyectosFinanciados(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "vincularProfesorAProyectosFinanciados", respuesta, fa, nombreComando, comandoXML);
            //descarga laboral
        } //--------
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_DESCARGA_PROFESOR_CYC))) {
            respuesta = compromisosBean.editarDescargaProfesor(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "editarDescargaProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_CARGA_VACIA_A_PROFESOR))) {
            respuesta = compromisosBean.crearCargaVaciaAProfesor(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "crearCargaVaciaAProfesor", respuesta, fa, nombreComando, comandoXML);
            //Historicos
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_PASAR_MONITORIAS_A_HISTORICOS))) {
            respuesta = compromisosBean.migrarCargasPorFinPeriodo(comandoXML);
            reportarALogExcepciones(compromisosBean.getClass().getCanonicalName(), "migrarCargasPorFinPeriodo", respuesta, fa, nombreComando, comandoXML);
            respuesta = historicoBean.pasarMonitoriasAHistoricos(comandoXML);
            reportarALogExcepciones(historicoBean.getClass().getCanonicalName(), "pasarMonitoriasAHistoricos", respuesta, fa, nombreComando, comandoXML);
            archivosBean.cerrarPeriodo();
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "cerrarPeriodo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_HISTORICO_POR_CORREO))) {
            respuesta = historicoBean.consultarMonitoriasEnHistorico(comandoXML);
            reportarALogExcepciones(historicoBean.getClass().getCanonicalName(), "consultarMonitoriasEnHistorico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA))) {
            respuesta = historicoTesis.darHistoricoEstudiantesTesis(comandoXML);
            reportarALogExcepciones(historicoTesis.getClass().getCanonicalName(), "darHistoricoEstudiantesTesis", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_MAESTRIA_PROFESOR))) {
            respuesta = historicoTesis.darHistoricosEstudiantesTesisMaestriaProfesor(comandoXML);
            reportarALogExcepciones(historicoTesis.getClass().getCanonicalName(), "darHistoricoEstudiantesTesisProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_MAESTRIA))) {
            respuesta = historicoTesis.darHistoricoEstudianteTesis(comandoXML);
            reportarALogExcepciones(historicoTesis.getClass().getCanonicalName(), "darHistoricoEstudianteTesis", respuesta, fa, nombreComando, comandoXML);
            //Consulta de archivos profesor por período (Incluye históricos)
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO))) {
            respuesta = historicoTesisPregradoBean.darHistoricoEstudiantesTesisPregrado(comandoXML);
            reportarALogExcepciones(historicoTesisPregradoBean.getClass().getCanonicalName(), "darHistoricoEstudiantesTesisPregrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTES_TESIS_DE_PREGRADO_POR_PROFESOR))) {
            respuesta = historicoTesisPregradoBean.darHistoricoEstudiantesTesisPregradoProfesor(comandoXML);
            reportarALogExcepciones(historicoTesisPregradoBean.getClass().getCanonicalName(), "darHistoricoEstudiantesTesisPregradoProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_ESTUDIANTE_TESIS_DE_PREGRADO))) {
            respuesta = historicoTesisPregradoBean.darHistoricoEstudianteTesisPregrado(comandoXML);
            reportarALogExcepciones(historicoTesisPregradoBean.getClass().getCanonicalName(), "darHistoricoEstudianteTesisPregrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_ARCHIVOS_PROFESOR_POR_PERIODO))) {
            respuesta = archivosBean.darArchivosProfesorPorPeriodo(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "darArchivosProfesorPorPeriodo", respuesta, fa, nombreComando, comandoXML);
            //Es estudiante?
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ES_ESTUDIANTE_POR_CORREO))) {
            respuesta = estudianteBean.consultarEsEstudiantePorCorreo(comandoXML);
            reportarALogExcepciones(estudianteBean.getClass().getCanonicalName(), "consultarEsEstudiantePorCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORIAS_HISTORICAS_NUEVAS))) {
            respuesta = monitoriaBean.darMonitoriasRealizadasPorCorreo(comandoXML);
            reportarALogExcepciones(monitoriaBean.getClass().getCanonicalName(), "darMonitoriasRealizadasPorCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RETORNAR_PERIODOS_RANGO))) {
            respuesta = periodoBean.retornarPeriodosRango(comandoXML);
            reportarALogExcepciones(periodoBean.getClass().getCanonicalName(), "retornarPeriodosRango", respuesta, fa, nombreComando, comandoXML);
            //TESIS
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PARA_COORDINACION))) {
            respuesta = tesis1Bean.darTesisPorAprobarCoordinadorMaestria(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "darTesisPorAprobarCoordinadorMaestria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1))) {
            respuesta = tesis1Bean.darEstadoSolicitudTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "darEstadoSolicitudTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1))) {
            respuesta = tesis1Bean.aprobarORechazarAsesoriaTesis(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "aprobarORechazarAsesoriaTesis", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1_COORDINADOR))) {
            respuesta = tesis1Bean.aprobarTesis1CoordinacionMaestria(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "aprobarTesis1CoordinacionMaestria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_1))) {
            respuesta = tesis1Bean.subirNotaTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "subirNotaTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE))) {
            respuesta = inscripcionSubareaBean.crearSolicitudIngresoSubareaEstudiante(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "crearSolicitudIngresoSubareaEstudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_INSCRIPCION_A_SUBAREA_ASESOR))) {
            respuesta = inscripcionSubareaBean.aprobarInscripcionASubareaAsesor(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "aprobarInscripcionASubareaAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_INSCRIPCION_A_SUBAREA_DIRECTOR))) {
            respuesta = inscripcionSubareaBean.aprobarInscripcionASubareaDirector(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "aprobarInscripcionASubareaDirector", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION))) {
            respuesta = inscripcionSubareaBean.crearSolicitudIngresoSubareaEstudiantePorCoordinacion(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "crearSolicitudIngresoSubareaEstudiantePorCoordinacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_INGRESO_SUBAREA))) {
            respuesta = inscripcionSubareaBean.darSolicitudesIngresoSubarea(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "darSolicitudesIngresoSubarea", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1))) {
            respuesta = tesis1Bean.agregarSolicitudTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "agregarSolicitudTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_MAESTRIA_TESIS))) {
            respuesta = inscripcionSubareaBean.obtenerMateriasMaestria(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "obtenerMateriasMaestria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CURSOS_MAESTRIA_TESIS))) {
            respuesta = inscripcionSubareaBean.agregarCursoMaestria(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "agregarMateriaMaestria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CURSO_MAESTRIA_POR_ID))) {
            respuesta = inscripcionSubareaBean.obtenerMateriaMaestriaPorId(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "darMateriaMaestriaPorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_MAESTRIA_TESIS_POR_CLASIFICACION))) {
            respuesta = inscripcionSubareaBean.obtenerMateriasMaestriaPorClasificacion(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "obtenerMateriasMaestriaPorClasificacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_COORDINADOR))) {
            respuesta = inscripcionSubareaBean.darSolicitudesIngresoSubareaParaCoordinador(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "darSolicitudesIngresoSubareaParaCoordinador", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_ASESOR))) {
            respuesta = inscripcionSubareaBean.darSolicitudesIngresoSubAreaAsesor(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "darSolicitudesIngresoSubAreaAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_INGRESOSUBAREA_POR_ID))) {
            respuesta = inscripcionSubareaBean.darSolicitudIngresoSubareaPorId(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "darSolicitudIngresoSubareaPorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SOLICITUD_ESTUDIANTE_SUBAREA_POR_CORREO))) {
            respuesta = inscripcionSubareaBean.darSlicitudIngresosubareaEstudiantePorCorreo(comandoXML);
            reportarALogExcepciones(inscripcionSubareaBean.getClass().getCanonicalName(), "darSlicitudIngresosubareaEstudiantePorCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PROFESOR))) {
            respuesta = tesis1Bean.darSolicitudesTesis1ParaProfesor(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "darSolicitudesTesis1ParaProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHAS_PERIODO_TESIS))) {
            respuesta = tesisBean.establecerFechasPeriodo(comandoXML);
            reportarALogExcepciones(tesisBean.getClass().getCanonicalName(), "", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CONFIGURACION_PERIODO_TESIS_POR_NOMBRE))) {
            respuesta = tesisBean.darConfiguracionPeriodoTesisPorNombre(comandoXML);
            reportarALogExcepciones(tesisBean.getClass().getCanonicalName(), "darConfiguracionPeriodoTesisPorNombre", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_COLOCAR_PENDIENTE_TESIS_1))) {
            respuesta = tesis1Bean.cambiarEstadoTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "cambiarEstadoTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1_ESTUDIANTE))) {
            respuesta = tesis1Bean.darSolicitudTesisParaEstudiante(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "darSolicitudTesisParaEstudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REPROBAR_TESIS_2))) {
            respuesta = tesis2Bean.reprobarTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "reprobarTesis2Estudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE))) {
            respuesta = tesis2Bean.crearSolicitudTesis2Estudiante(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "crearSolicitudTesis2Estudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS_2_ASESOR))) {
            respuesta = tesis2Bean.darSolicitudesTesis2Asesor(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darSolicitudesTesis2Asesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TESIS2_POR_ID))) {
            respuesta = tesis2Bean.darDetallesTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darDetallesTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS2))) {
            respuesta = tesis2Bean.aprobarSolicitudTesis2Asesor(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "aprobarSolicitudTesis2Asesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_JURADO_TESIS_2))) {
            respuesta = tesis2Bean.modificarJuradosTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "modificarJuradosTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2))) {
            respuesta = tesis2Bean.guardarHorarioSustentacionTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "guardarHorarioSustentacionTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_TESIS2_PENDIENTE))) {
            respuesta = tesis2Bean.cambiarEstadoTesis2PendienteEspecial(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "cambiarEstadoTesis2PendienteEspecial", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR))) {
            respuesta = tesis2Bean.pedirPendienteEspecialParaTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "pedirPendienteEspecialParaTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_BUSCAR__TESIS_2_POR_HORARIO))) {
            respuesta = tesis2Bean.darTesisConHorarioSustentacion(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darTesisConHorarioSustentacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARTICULO_TESIS_2_FINAL))) {
            respuesta = tesis2Bean.subirArticuloFinalizacionTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_2))) {
            respuesta = tesis2Bean.darTodasLasSolicitudesTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darTodasLasSolicitudesTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_2_A_MIGRAR))) {
            respuesta = tesis2Bean.darTesis2AMigrar(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darTesis2AMigrar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_2))) {
            respuesta = tesis2Bean.comportamientoEmergenciaMigrarTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "comportamientoEmergenciaMigrarTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_1_A_MIGRAR))) {
            respuesta = tesis1Bean.darTesis1AMigrar(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darTesis1AMigrar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_1))) {
            respuesta = tesis1Bean.comportamientoEmergenciaMigrarTesis1(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "comportamientoEmergenciaMigrarTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2))) {
            respuesta = tesis2Bean.colocarNotaTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "colocarNotaTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1))) {
            respuesta = tesisBean.agregarTemaTesis(comandoXML);
            reportarALogExcepciones(tesisBean.getClass().getCanonicalName(), "agregarTemaTesis", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_1_POR_ASESOR))) {
            respuesta = tesisBean.darTemasDeTesisPorAsesor(comandoXML);
            reportarALogExcepciones(tesisBean.getClass().getCanonicalName(), "darTemasDeTesisPorAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TEMA_TESIS_POR_ID))) {
            respuesta = tesisBean.darTemadeTesisPorId(comandoXML);
            reportarALogExcepciones(tesisBean.getClass().getCanonicalName(), "darTemadeTesisPorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_1))) {
            respuesta = tesisBean.darTemasDeTesis(comandoXML);
            reportarALogExcepciones(tesisBean.getClass().getCanonicalName(), "darTemasDeTesis", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_1))) {
            respuesta = tesis1Bean.agregarComentario30PorcientoTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "agregarComentarioTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_2))) {
            respuesta = tesis2Bean.agregarComentarioTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "agregarComentarioTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_TESIS_1))) {
            respuesta = tesis1Bean.darComentariosPorTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "darComentariosPorTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_TESIS_2))) {
            respuesta = tesis2Bean.darComentariosPorTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darComentariosPorTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TEMA_TESIS))) {
            respuesta = tesisBean.eliminarTemaTesisAsesor(comandoXML);
            reportarALogExcepciones(tesisBean.getClass().getCanonicalName(), "eliminarTemaTesisAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_PENDIENTE_TESIS1))) {
            respuesta = tesis1Bean.aprobarRechazarPendienteTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "aprobarRechazarPendienteTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS1))) {
            respuesta = tesis1Bean.retirarTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "retirarTesis1", respuesta, fa, nombreComando, comandoXML);//**********************************************************************************************************************************************************************************************************
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS2))) {
            respuesta = tesis2Bean.retirarTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "retirarTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_HORARIO_SUSTENTACION_TESIS2_POR_ASESOR))) {
            respuesta = tesis2Bean.aprobarHorarioSustentacionPorAsesor(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "aprobarHorarioSustentacionPorAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_JURADOS_TESIS_2))) {
            respuesta = tesis2Bean.comportamientoEmergenciaJuradosTesis(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "comportamientoEmergenciaJuradosTesis", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_JURADO_TESIS_2_EXTERNO))) {
            respuesta = juradoTesisBean.actualizarJuradoExterno(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "actualizarJuradoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_EVALUACION_JURADO_POR_HASH))) {
            respuesta = juradoTesisBean.darEvaluacionJuradoPorHash(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darEvaluacionJuradoPorHash", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GUARDAR_EVALUACION_POR_HASH))) {
            respuesta = juradoTesisBean.guardarEvaluacionJuradoPorHash(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "guardarEvaluacionJuradoPorHash", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_SALON_SUSTENTACION_TESIS_2))) {
            respuesta = tesis2Bean.guardarSalonSustentacion(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "guardarSalonSustentacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA))) {
            respuesta = tesis1Bean.agregarSolicitudTesis1Director(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "agregarSolicitudTesis1Director", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_COORDINADOR))) {
            respuesta = tesis2Bean.crearSolicitudIngresoTesis2PermisosCoordinador(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "crearSolicitudIngresoTesis2PermisosCoordinador", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_UBICACION_HORA_TESIS2))) {
            respuesta = tesis2Bean.darUbicacionHoraTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darUbicacionHoraTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_UBICACION_HORA_TESIS2))) {
            respuesta = tesis2Bean.darDetallesUbicacionHoraTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "darDetallesUbicacionHoraTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_TESIS_2))) {
            respuesta = tesis2Bean.actualizarDetallesTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "cmdActualizarTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS1))) {
            respuesta = tesis1Bean.confirmarTesis1EnBanner(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "cmdConfirmarInscripcionBannerTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS2))) {
            respuesta = tesis2Bean.confirmarTesis2EnBanner(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "cmdConfirmarInscripcionBannerTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HISTORICO_TESIS2_POR_ESTADO_SEMESTRE_Y_CORREO_ASESOR))) {
            respuesta = historicoTesis2.consultarTesis2PorEstadoYSemestreYCorreoAsesor(comandoXML);
            reportarALogExcepciones(historicoTesis2.getClass().getCanonicalName(), "consultarTesis2PorEstadoYSemestreYCorreoAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS2))) {
            respuesta = tesis2Bean.establecerAprobacionParadigma(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "establecerAprobacionParadigmaTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS1))) {
            respuesta = tesis1Bean.establecerAprobacionParadigma(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "establecerAprobacionParadigmaTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_2))) {
            respuesta = historicoTesis2.establecerAprobacionParadigmaHistoricoTesis2(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "establecerAprobacionParadigmaHistoricoTesis2", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_HISTORICOS_TESIS_1))) {
            respuesta = historicoTesis1.establecerAprobacionParadigmaHistoricoTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "establecerAprobacionParadigmaHistoricoTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RESERVAR_CITA))) {
            respuesta = reservasBean.reservarCita(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "reservarCita", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_HORARIO_COORDINACION))) {
            respuesta = reservasBean.guardarHorarioCoordinacion(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "guardarHorarioCoordinacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_LOGIN))) {
            respuesta = reservasBean.consultarReservasPersona(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "consultarReservasPersona", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_ACTUALES))) {
            respuesta = reservasBean.consultarReservas(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "consultarReservas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_HORARIO_COORDINACION))) {
            respuesta = reservasBean.consultarHorarioCoordinacion(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "consultarHorarioCoordinacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVA))) {
            respuesta = reservasBean.cancelarReserva(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "cancelarReserva", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_RESERVA))) {
            respuesta = reservasBean.modificarReserva(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "modificarReserva", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_CONTACTO))) {
            respuesta = reservasBean.consultarDatosContacto(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "consultarDatosContacto", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_DIA))) {
            respuesta = reservasBean.consultarReservasDia(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "consultarReservasDia", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_RESERVAS))) {
            respuesta = reservasBean.guardarEstadoReservas(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "guardarEstadoReservas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LISTA_NEGRA_RESERVA_CITAS))) {
            respuesta = listaNegraReservaCitasBean.consultarListaNegraReservaCitas(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "consultarListaNegraReservaCitas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_LISTA_NEGRA_RESERVA_CITAS))) {
            respuesta = listaNegraReservaCitasBean.crearListaNegraReservaCitas(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "agregarListaNegraReservaCitas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_LISTA_NEGRA_RESERVA_CITAS))) {
            respuesta = listaNegraReservaCitasBean.editarListaNegraReservaCitas(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "editarListaNegraReservaCitas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_LISTA_NEGRA_RESERVA_CITAS))) {
            respuesta = listaNegraReservaCitasBean.eliminarListaNegraReservaCitas(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "eliminarListaNegraReservaCitas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_RESERVAS))) {
            respuesta = reservasBean.consultarRangoReservas(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "cmdConsultarRangoReservas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS))) {
            respuesta = reservasBean.cancelarGrupoReservas(comandoXML);
            reportarALogExcepciones(reservasBean.getClass().getCanonicalName(), "cmdCancelarGrupoReservas", respuesta, fa, nombreComando, comandoXML);
        } //Conflicto de Horarios...
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO))) {
            respuesta = conflictosBean.crearPeticionConflictoHorario(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "crearPeticionConflicto", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MATERIAS_PROGRAMA))) {
            respuesta = carteleraCursosRemote.consultarCursosISIS(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "consultarMaterias", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCIONES_MATERIA))) {
            respuesta = carteleraCursosRemote.consultarSeccionesPorCodigoCurso(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "consultarSecciones", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_CONFLICTO_HORARIO_ESTUDIANTE))) {
            respuesta = conflictosBean.consultarPeticionesPorCorreo(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "consultarPeticiones", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PETICION_CONFLICTO_HORARIO_ESTUDIANTE))) {
            respuesta = conflictosBean.cancelarPeticionPorCorreo(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "cancelarPeticion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_MATERIAS_CONFLICTO_HORARIO))) {
            respuesta = conflictosBean.consultarCantidadPeticionesCursosISIS(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "consultarPeticionesMaterias", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_POR_MATERIA_CONFLICTO_HORARIO))) {
            respuesta = conflictosBean.consultarPeticionesPorCodigoCursoYTipo(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "consultarPeticionesPorMateria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_RESUELTAS_CONFLICTO_HORARIO))) {
            respuesta = conflictosBean.consultarPeticionesResueltas(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "consultarPeticionesMaterias", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_PETICIONES_CONFLICTO_HORARIO))) {
            respuesta = conflictosBean.actualizarEstadoYResolucionPeticiones(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "guardarEstadoPeticiones", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_CURSO_CONFLICTO_HORARIO))) {
            respuesta = carteleraCursosRemote.crearCurso(comandoXML);
            reportarALogExcepciones(carteleraCursosRemote.getClass().getCanonicalName(), "crearSeccionMateria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_SECCION_CONFLICTO_HORARIO))) {
            respuesta = carteleraCursosRemote.crearSeccionACurso(comandoXML);
            reportarALogExcepciones(carteleraCursosRemote.getClass().getCanonicalName(), "crearSeccionMateria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_FECHAS_CONFLICTO_HORARIO))) {
            respuesta = conflictosBean.consultarFechasConflictoHorario(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "consultarFechas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GUARDAR_FECHAS_CONFLICTO_HORARIO))) {
            respuesta = conflictosBean.actualizarFechasConflictoHorario(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "guardarFechas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA_CONFLICTO_HORARIOS))) {
            respuesta = carteleraCursosRemote.cargarCarteleraWeb(comandoXML);
            reportarALogExcepciones(carteleraCursosRemote.getClass().getCanonicalName(), "cargarCarteleraWeb", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_DATOS_SECCION_CONFLICTO_HORARIO))) {
            respuesta = carteleraCursosRemote.editarDatosSeccion(comandoXML);
            reportarALogExcepciones(carteleraCursosRemote.getClass().getCanonicalName(), "editarDatosSeccion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROGRAMAS_CONFLICTO_HORARIO))) {
            respuesta = conflictosBean.consultarProgramas(comandoXML);
            reportarALogExcepciones(conflictosBean.getClass().getCanonicalName(), "consultarProgramas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCION))) {
            respuesta = carteleraCursosRemote.consultarSeccion(comandoXML);
            reportarALogExcepciones(carteleraCursosRemote.getClass().getCanonicalName(), "consultarSeccion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_ISIS))) {
            respuesta = carteleraCursosRemote.consultarProfesoresISIS(comandoXML);
            reportarALogExcepciones(carteleraCursosRemote.getClass().getCanonicalName(), "consultarProfesoresISIS", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SECCION_CONFLICTO_HORARIO))) {
            respuesta = carteleraCursosRemote.eliminarSeccion(comandoXML);
            reportarALogExcepciones(carteleraCursosRemote.getClass().getCanonicalName(), "eliminarSeccion", respuesta, fa, nombreComando, comandoXML);
        } //seccion pg
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REPROBAR_PROYECTO_GRADO))) {
            respuesta = pgBean.reprobarProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "reprobarProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ASESOR))) {
            respuesta = pgBean.darProyectosDeGradoPorAsesor(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darProyectosDeGradoPorAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_CONFIGURADOS_TESIS_PREGRADO))) {
            respuesta = pgBean.darPeriodosConfigurados(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darPeriodosConfigurados", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO))) {
            respuesta = pgBean.crearSolicitudTesisProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "crearSolicitudTesisProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO_COORDINACION))) {
            respuesta = pgBean.crearSolicitudProyectoXCoordinacion(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "crearSolicitudProyectoXCoordinacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_PREGRADO_POR_ASESOR))) {
            respuesta = pgBean.darTemasProyectoGradoPorAsesor(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darTemasProyectoGradoPorAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CATEGORIAS_TESIS_PREGRADO))) {
            respuesta = pgBean.consultarCateoriasProyectosDeGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "consultarCateoriasProyectosDeGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_PREGRADO_TODOS))) {
            respuesta = pgBean.darTodosLosTemasDeProyectoDeGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darTodosLosTemasDeProyectoDeGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_PREGRADO))) {
            respuesta = pgBean.crearTemaProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "crearTemaProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TEMA_TESIS_PREGRADO))) {
            respuesta = pgBean.eliminarTemaProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "eliminarTemaProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE))) {
            respuesta = pgBean.darProyectoDegradoEstudainte(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darProyectoDegradoEstudainte", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO))) {
            respuesta = pgBean.aceptarSolicitudProyectoGradoAsesor(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "aceptarSolicitudProyectoGradoAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CONFIGURACION_PERIODO_TESIS_PREGRADO_POR_NOMBRE))) {
            respuesta = pgBean.darConfiguracionPeriodo(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darConfiguracionPeriodo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHAS_PERIODO_PREGRADO))) {
            respuesta = pgBean.establecerFechasPeriodoPregrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "establecerFechasPeriodoPregrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PROYECTO_DE_GRADO_POR_ID))) {
            respuesta = pgBean.darProyectoDeGradoPorId(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darProyectoDeGradoPorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROPUESTA_PROYECTO_DE_GRADO))) {
            respuesta = pgBean.enviarPropuestaProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "enviarPropuestaProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO))) {
            respuesta = pgBean.enviarAficheProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "enviarAficheProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE))) {
            respuesta = pgBean.enviarAprobacionAficheProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "aprobarRechazarAficheProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACEPTAR_PROPUESTA_PROYECTO_DE_GRADO))) {
            respuesta = pgBean.aceptarPropuestaTesisPorAsesor(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "aceptarPropuestaTesisPorAsesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TEMA_TESIS_PREGRADO_POR_ID))) {
            respuesta = pgBean.darTemaProyectoGradoPorId(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darTemaProyectoGradoPorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_PROYECTOS_DE_GRADO_POR_COORDINADOR))) {
            respuesta = pgBean.darProyectosDeGradoCoordinador(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darProyectosDeGradoCoordinador", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TODOS_LOS_PROYECTOS_PREGRADO_A_MIGRAR))) {
            respuesta = pgBean.darProyectosPregradoAMigrar(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darProyectosPregradoAMigrar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_PROYECTO_DE_GRADO))) {
            respuesta = pgBean.comportamientoEmergenciaMigrarProyectoDeGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darProyectosPregradoAMigrar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO))) {
            respuesta = pgBean.agregarComentarioProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "agregarComentarioProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_PROYECTO_GRADO))) {
            respuesta = pgBean.darComentariosProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darComentariosProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET))) {
            respuesta = pgBean.subirDocumentoABETProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "subirDocumentoABETProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO))) {
            respuesta = pgBean.subirNotaProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "subirNotaProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_FECHA_PROYECTO_GRADO_POR_PERIODO))) {
            respuesta = pgBean.darFechasProyectoGradoPeriodoActual(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "darFechasProyectoGradoPeriodoActual", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_RETIRO_PROYECTO_GRADO))) {
            respuesta = pgBean.enviarInformeRetiroProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "enviarInformeRetiroProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO))) {
            respuesta = pgBean.enviarInformePendienteProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "enviarInformePendienteProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO))) {
            respuesta = pgBean.enviarAprobacionPendienteProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "enviarAprobacionPendienteProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO))) {
            respuesta = pgBean.enviarAprobacionPendienteEspecialProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "enviarAprobacionPendienteEspecialProyectoGrado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_PROYECTO_GRADO_ESTADO))) {
            //respuesta = pgBean.actualizarProyectoGradoEstado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "actualizarProyectoGradoEstado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_APROBAR_PROYECTO_GRADO_COORDINACION))) {
            respuesta = pgBean.aprobarProyectoGradoCoordinacion(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "aprobarProyectoGradoCoordinacion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO))) {
            respuesta = pgBean.enviarInformePendienteEspecialProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "enviarInformePendienteEspecialProyectoGrado", respuesta, fa, nombreComando, comandoXML);
            //persona
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_PROYECTO_GRADO))) {
            respuesta = pgBean.confirmarInscripcionBannerProyectoGrado(comandoXML);
            reportarALogExcepciones(pgBean.getClass().getCanonicalName(), "enviarInformePendienteEspecialProyectoGrado", respuesta, fa, nombreComando, comandoXML);
            //persona
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_PERSONA_POR_CORREO))) {
            respuesta = personaBean.darPersonaPorCorreo(comandoXML);
            reportarALogExcepciones(personaBean.getClass().getCanonicalName(), "darPersonaPorCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ESTA_REGISTRADO_LDAP))) {
            respuesta = autorizacionBean.estaRegistradoEnLDAP(comandoXML);
            reportarALogExcepciones(personaBean.getClass().getCanonicalName(), "darPersonaPorCorreo", respuesta, fa, nombreComando, comandoXML);
            //ADMINISTRADOR SISINFO
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_CARGA_GRUPO))) {
            respuesta = cargaGrupoBean.cargarGrupo(comandoXML);
            reportarALogExcepciones(cargaGrupoBean.getClass().getCanonicalName(), "cargarGrupo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ROLES))) {
            respuesta = consultaRolBean.consultarConstantesRoles(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "consultarConstantesRoles", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_USUARIOS))) {
            respuesta = consultaRolBean.consultarUsuarios(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "consultarUsuarios", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_USUARIO))) {
            respuesta = consultaRolBean.consultarUsuario(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "consultarUsuario", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GUARDAR_CAMBIOS_USUARIO))) {
            respuesta = consultaRolBean.guardarRolUsuarios(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "guardarRolUsuarios", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_USUARIO))) {
            respuesta = consultaRolBean.crearUsuario(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "crearUsuario", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_DOCUMENTO))) {
            respuesta = consultaRolBean.consultarConstantesTipoDocumento(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "consultarConstantesTipoDocumento", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PAISES))) {
            respuesta = consultaRolBean.consultarConstantesPais(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "consultarConstantesPais", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_ALERTA_GENERICA))) {
            respuesta = alertaBean.editarAlertaGenerica(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "editarAlertaGenerica", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGENERAR_ALERTA))) {
            respuesta = alertaBean.regenerarAlerta(comandoXML);
            reportarALogExcepciones(consultaRolBean.getClass().getCanonicalName(), "regenerarAlerta", respuesta, fa, nombreComando, comandoXML);


            //-- Estudiantes
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_ESTUDIANTE_PERSONA))) {
            respuesta = estudianteBean.crearEstudiante(comandoXML);
            reportarALogExcepciones(estudianteBean.getClass().getCanonicalName(), "crearEstudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTES_PERSONAS))) {
            respuesta = estudianteBean.consultarEstudiantes(comandoXML);
            reportarALogExcepciones(estudianteBean.getClass().getCanonicalName(), "consultarEstudiantes", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ESTUDIANTE_PERSONA))) {
            respuesta = estudianteBean.eliminarEstudiante(comandoXML);
            //reportarALogExcepciones(estudianteBean.getClass().getCanonicalName(), "eliminarEstudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE_PERSONA))) {
            respuesta = estudianteBean.consultarEstudiante(comandoXML);
            //reportarALogExcepciones(estudianteBean.getClass().getCanonicalName(), "consultarEstudiante", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_ESTADO_ACTIVO_ESTUDIANTE))) {
            respuesta = estudianteBean.actualizarEstadoActivoEstudiante(comandoXML);
            //reportarALogExcepciones(estudianteBean.getClass().getCanonicalName(), "actualizarEstadoActivoEstudiante", respuesta, fa, nombreComando, comandoXML);
            //--Profesores
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_PROFESOR_PERSONA))) {
            respuesta = profesorBean.crearProfesor(comandoXML);
            reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "crearProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_PERSONAS))) {
            respuesta = profesorBean.consultarProfesores(comandoXML);
            reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "consultarProfesores", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROFESOR_PERSONA))) {
            respuesta = profesorBean.eliminarProfesor(comandoXML);
            //reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "eliminarProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESOR_PERSONA))) {
            respuesta = profesorBean.consultarProfesor(comandoXML);
            //reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "consultarProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_ESTADO_ACTIVO_PROFESOR))) {
            respuesta = profesorBean.actualizarEstadoActivoProfesor(comandoXML);
            //reportarALogExcepciones(profesorBean.getClass().getCanonicalName(), "actualizarEstadoActivoProfesor", respuesta, fa, nombreComando, comandoXML);
            //--Grupos
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_GRUPO_PERSONAS))) {
            respuesta = grupoBean.crearGrupo(comandoXML);
            reportarALogExcepciones(grupoBean.getClass().getCanonicalName(), "crearGrupo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_GRUPOS))) {
            respuesta = grupoBean.darGrupos(comandoXML);
            reportarALogExcepciones(grupoBean.getClass().getCanonicalName(), "darGrupos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPO_PERSONAS))) {
            respuesta = grupoBean.eliminarGrupo(comandoXML);
            //reportarALogExcepciones(grupoBean.getClass().getCanonicalName(), "eliminarGrupo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPO_POR_ID))) {
            respuesta = grupoBean.darGrupoPorID(comandoXML);
            //reportarALogExcepciones(grupoBean.getClass().getCanonicalName(), "darGrupoPorID", respuesta, fa, nombreComando, comandoXML);
            //--Correo Auditoría
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREOS_AUDITORIA))) {
            respuesta = correoAuditoriaBean.consultarCorreosAuditoria(comandoXML);
            reportarALogExcepciones(personaBean.getClass().getCanonicalName(), "consultarCorreosAuditoria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREO_AUDITORIA))) {
            respuesta = correoAuditoriaBean.consultarCorreoAuditoria(comandoXML);
            reportarALogExcepciones(personaBean.getClass().getCanonicalName(), "consultarCorreoAuditoria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CORREO_AUDITORIA))) {
            respuesta = correoAuditoriaBean.eliminarCorreoAuditoria(comandoXML);
            reportarALogExcepciones(personaBean.getClass().getCanonicalName(), "eliminarCorreoAuditoria", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO))) {
            respuesta = correoAuditoriaBean.consultarCorreoAuditoriaPorDestinatariosFechaYAsunto(comandoXML);
            reportarALogExcepciones(personaBean.getClass().getCanonicalName(), "consultarCorreoAuditoriaPorDestinatariosFechaYAsunto", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO))) {
            respuesta = correoAuditoriaBean.eliminarCorreoAuditoriaPorDestinatariosFechaYAsunto(comandoXML);
            reportarALogExcepciones(personaBean.getClass().getCanonicalName(), "eliminarCorreoAuditoriaPorDestinatariosFechaYAsunto", respuesta, fa, nombreComando, comandoXML);
            // auditoria usuario
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_AUDITORIA_USUARIO_POR_USUARIO_FECHA_ROL_Y_COMANDO))) {
            respuesta = auditoriaUsuarioBean.consultarAuditoriaUsuarioPorUsuarioFechaRolYComando(comandoXML);
            reportarALogExcepciones(auditoriaUsuarioBean.getClass().getCanonicalName(), "consultarAuditoriaUsuarioPorUsuarioFechaRolYComando", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_AUDITORIA_USUARIO_ACTIVIDAD))) {
            respuesta = auditoriaUsuarioBean.consultarAuditoriaUsuarioActividad(comandoXML);
            reportarALogExcepciones(auditoriaUsuarioBean.getClass().getCanonicalName(), "consultarAuditoriaUsuarioActividad", respuesta, fa, nombreComando, comandoXML);
            // correos sin enviar y filtros de correo
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CORREOS_SIN_ENVIAR))) {
            respuesta = correoSinEnviarBean.darCorreosSinEnviar(comandoXML);
            reportarALogExcepciones(correoSinEnviarBean.getClass().getCanonicalName(), "darCorreosSinEnviar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_CORREO_SIN_ENVIAR))) {
            respuesta = correoSinEnviarBean.darCorreoSinEnviar(comandoXML);
            reportarALogExcepciones(correoSinEnviarBean.getClass().getCanonicalName(), "darCorreoSinEnviar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_CORREO_SIN_ENVIAR))) {
            respuesta = correoSinEnviarBean.editarCorreoSinEnviar(comandoXML);
            reportarALogExcepciones(correoSinEnviarBean.getClass().getCanonicalName(), "editarCorreoSinEnviar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CORREOS_SIN_ENVIAR))) {
            respuesta = correoSinEnviarBean.eliminarCorreosSinEnviar(comandoXML);
            reportarALogExcepciones(correoSinEnviarBean.getClass().getCanonicalName(), "eliminarCorreosSinEnviar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREOS_SIN_ENVIAR))) {
            respuesta = correoSinEnviarBean.enviarCorreosSinEnviar(comandoXML);
            reportarALogExcepciones(correoSinEnviarBean.getClass().getCanonicalName(), "enviarCorreosSinEnviar", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_FILTROS_CORREO))) {
            respuesta = filtroCorreoBean.darFiltrosCorreo(comandoXML);
            reportarALogExcepciones(filtroCorreoBean.getClass().getCanonicalName(), "darFiltrosCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_FILTRO_CORREO))) {
            respuesta = filtroCorreoBean.darFiltroCorreo(comandoXML);
            reportarALogExcepciones(filtroCorreoBean.getClass().getCanonicalName(), "darFiltroCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_FILTRO_CORREO))) {
            respuesta = filtroCorreoBean.agregarFiltroCorreo(comandoXML);
            reportarALogExcepciones(filtroCorreoBean.getClass().getCanonicalName(), "agregarFiltroCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_FILTRO_CORREO))) {
            respuesta = filtroCorreoBean.eliminarFiltroCorreo(comandoXML);
            reportarALogExcepciones(filtroCorreoBean.getClass().getCanonicalName(), "eliminarFiltroCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_FILTRO_CORRREO))) {
            respuesta = filtroCorreoBean.editarFiltroCorreo(comandoXML);
            reportarALogExcepciones(filtroCorreoBean.getClass().getCanonicalName(), "editarFiltroCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_FILTRO_CORREO))) {
            respuesta = filtroCorreoBean.darTiposFiltroCorreo(comandoXML);
            reportarALogExcepciones(filtroCorreoBean.getClass().getCanonicalName(), "darTiposFiltroCorreo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_OPERACIONES_FILTRO_CORREO))) {
            respuesta = filtroCorreoBean.darOperacionesFiltroCorreo(comandoXML);
            reportarALogExcepciones(filtroCorreoBean.getClass().getCanonicalName(), "darOperacionesFiltroCorreo", respuesta, fa, nombreComando, comandoXML);
            //acciones rol admon sisinfo/soporte
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SOLUCIONAR_EXCEPCION))) {
            respuesta = reporteExcepcionesBean.solucionarExcepcion(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "solucionarExcepcion", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_INCIDENTE_SISINFO))) {
            respuesta = incidenteBean.modificarEstadoIncidente(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "modificarEstadoIncidente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INCIDENTE_SISINFO_POR_ID))) {
            respuesta = incidenteBean.EliminarIncidente(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "EliminarIncidente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_EXCEPCION_POR_ID))) {
            respuesta = reporteExcepcionesBean.eliminarExcepcionSisinfo(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "eliminarExcepcionSisinfo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REPORTAR_INCIDENTE_SISINFO))) {
            respuesta = incidenteBean.reportarIncidente(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "reportarIncidente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_SOPORTE))) {
            respuesta = incidenteBean.darIncidentesSoporte(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "darIncidentesSoporte", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ID))) {
            respuesta = incidenteBean.consultarIncidentePorId(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "consultarIncidentePorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCIONES_SISINFO_SOPORTE))) {
            respuesta = reporteExcepcionesBean.consultarExcepcionesSisinfo(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarExcepcionesSisinfo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_CORREO_CREADOR))) {
            respuesta = incidenteBean.consultarIncidentePorCorreoReportado(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "consultarIncidentePorCorreoReportado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MODULOS_SISINFO))) {
            respuesta = incidenteBean.consultarModulosPublicosSisinfo(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "consultarModulosPublicosSisinfo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_BLANCA))) {
            respuesta = auditoriaBean.getListaBlanca(comandoXML);
            reportarALogExcepciones(auditoriaBean.getClass().getCanonicalName(), "getListaBlanca", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_COMANDO))) {
            respuesta = auditoriaBean.getListaComandoXML(comandoXML);
            reportarALogExcepciones(auditoriaBean.getClass().getCanonicalName(), "getListaComandoXML", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ERROR_LISTA_BLANCA))) {
            respuesta = auditoriaBean.eliminarErrorListaBlanca(comandoXML);
            reportarALogExcepciones(auditoriaBean.getClass().getCanonicalName(), "eliminarErrorListaBlanca", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_COMANDO_LISTA_COMANDO))) {
            respuesta = auditoriaBean.eliminarComandoListaComando(comandoXML);
            reportarALogExcepciones(auditoriaBean.getClass().getCanonicalName(), "eliminarComandoListaComando", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ASIGNAR_INCIDENTE))) {
            respuesta = incidenteBean.asignarIncidente(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "asignarIncidente", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PERSONA_SOPORTE_POR_ID))) {
            respuesta = personaSoporteBean.getPersonaSoportePorId(comandoXML);
            reportarALogExcepciones(personaSoporteBean.getClass().getCanonicalName(), "getPersonaSoportePorId", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTES_PERSONA_SOPORTE))) {
            respuesta = incidenteBean.darIncidentesXPersonaSoporte(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "darIncidentesXPersonaSoporte", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LISTA_ACCION_VENCIDA))) {
            respuesta = accionVencidaBean.darListaAccionVencida(comandoXML);
            reportarALogExcepciones(accionVencidaBean.getClass().getCanonicalName(), "darListaAccionVencida", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_PERSONA_SOPORTE))) {
            respuesta = personaSoporteBean.getListPersonaSoporte(comandoXML);
            reportarALogExcepciones(incidenteBean.getClass().getCanonicalName(), "getListPersonaSoporte", respuesta, fa, nombreComando, comandoXML);
        } //
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCIONES_POR_ESTADO))) {
            respuesta = reporteExcepcionesBean.consultarExcepcionesSisinfo(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarExcepcionesSisinfo", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCION_POR_ID))) {
            respuesta = reporteExcepcionesBean.consultarExcepcionPorId(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarExcepcionPorId", respuesta, fa, nombreComando, comandoXML);

            //ASISTENCIAS GRADUADAS
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_ASISTENCIA_GRADUADA))) {
            respuesta = asistenciaGraduadaBean.crearAsistenciaGraduada(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "crearAsistenciaGraduada", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS))) {
            respuesta = asistenciaGraduadaBean.consultarAsistenciasGraduadas(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarAsistenciasGraduadas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_PROFESOR))) {
            respuesta = asistenciaGraduadaBean.consultarAsistenciasGraduadasPorProfesor(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarAsistenciasGraduadasPorProfesor", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIA_GRADUADA))) {
            respuesta = asistenciaGraduadaBean.consultarAsistenciaGraduada(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarAsistenciaGraduada", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CALIFICAR_ASISTENCIA_GRADUADA))) {
            respuesta = asistenciaGraduadaBean.calificarAsistenciaGraduada(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "calificarAsistenciaGraduada", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_ASISTENCIAS_GRADUADAS))) {
            respuesta = asistenciaGraduadaBean.consultarTiposAsistenciasGraduadas(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarTiposAsistenciasGraduadas", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS))) {
            respuesta = asistenciaGraduadaBean.crearTareaProfesorCalificarAsistenciaGraduada(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "crearTareaProfesorCalificarAsistenciaGraduada", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_FINALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS))) {
            respuesta = asistenciaGraduadaBean.comportamientoFinRangoCalificarAsistenciaGraduada(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "comportamientoFinRangoCalificarAsistenciaGraduada", respuesta, fa, nombreComando, comandoXML);
        } //RANGOS FECHAS GENERALES
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_RANGOS_FECHAS_GENERALES))) {
            respuesta = rangoFechasGeneralesBean.editarRangosFechasGeneral(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "editarRangosFechasGeneral", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGOS_FECHAS_GENERALES))) {
            respuesta = rangoFechasGeneralesBean.consultarRangosFechasGeneral(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarRangosFechasGeneral", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGOS_Y_COMANDOS))) {
            respuesta = rangoFechasGeneralesBean.consultarTiposRangoFechasGeneral(comandoXML);
            reportarALogExcepciones(reporteExcepcionesBean.getClass().getCanonicalName(), "consultarTiposRangoFechasGeneral", respuesta, fa, nombreComando, comandoXML);
        } /*
         * tareas Planeacion Academica
         */ else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_PROGRAMA_PLANEACION_ACADEMICA))) {
            respuesta = archivosBean.comportamientoInicioTareaPrograma(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "comportamientoInicioTareaPrograma", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_NOTAS_TREINTAP_PLANEACION_ACADEMICA))) {
            respuesta = archivosBean.comportamientoInicioTarea30Porciento(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "comportamientoInicioTarea30Porciento", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_ARCHIVO_CIERRE_PLANEACION_ACADEMICA))) {
            respuesta = archivosBean.comportamientoInicioTareaCierreCursos(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "comportamientoInicioTareaCierreCursos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FINRANGOFECHAS_SUBIR_PROGRAMA))) {
            respuesta = archivosBean.comportamientoFinRangoFechasSubirPrograma(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "comportamientoFinRangoFechasSubirPrograma", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_30PORCIENTO))) {
            respuesta = archivosBean.comportamientoFinRangoFechas30Porciento(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "comportamientoFinRangoFechas30Porciento", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_CIERRE_CURSOS))) {
            respuesta = archivosBean.comportamientoFinRangoFechasCierreCursos(comandoXML);
            reportarALogExcepciones(archivosBean.getClass().getCanonicalName(), "comportamientoFinRangoFechasCierreCursos", respuesta, fa, nombreComando, comandoXML);
            // Reserva inventario
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_LABORATORIO))) {
            respuesta = laboratorioBean.editarLaboratorio(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "editarlaboratorio", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.crearEditarEventoExterno(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "crearEditarEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.consultarEventoExterno(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "consultarEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CATEGORIAS_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.consultarCategorias(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "consultarCategoriasEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_ESTADO_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.cambiarEstadoEvento(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "cambiarEstadoEvento", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DEPARTAMENTOS))) {
            respuesta = datosBean.darDepartamentos(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "darDepartamentos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CIUDADES_POR_DEPARTAMENTO))) {
            respuesta = datosBean.darCiudadesPorDepartamento(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "darCiudadesPorDepartamento", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GUARDAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.editarTiposCampo(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "consultarTiposCampoEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.eliminarEventoExterno(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "eliminarEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CATEGORIA_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.eliminarCategoria(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "eliminarCategoriaEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TIPO_CAMPO_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.eliminarTipoCampo(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "eliminarTipoCampoEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_GUARDAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.editarTiposCampo(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "consultarTiposCampoEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_IMAGEN_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.subirImagenEventoExterno(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "subirImagenEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_CAMPO_ADICIONAL_EVENTO_EXTERNO))) {
            respuesta = eventoExternoBean.consultarTiposCampoAdicional(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "consultarTiposCampoAdicionalEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INSCRITOS))) {
            respuesta = contactoBean.consultarInscritos(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "consultarInscritosEventoExterno", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_OLVIDO_CONTRASENA_PUBLICO))) {

            respuesta = contactoBean.olvidoContrasena(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "cmdOlvidoContrasenaPublico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_USUARIO_HASH_PUBLICO))) {

            respuesta = contactoBean.olvidoContrasena(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "cmdConsultarUsuarioHashPublico", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_CONTRASENA))) {

            respuesta = contactoBean.cambiarContrasena(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "cmdCambiarContrasena", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_INSCRIBIR_USUARIOS_VIP))) {

            respuesta = contactoBean.inscribirUsuariosVIP(comandoXML);
            reportarALogExcepciones(contactoBean.getClass().getCanonicalName(), "inscribirUsuariosVIP", respuesta, fa, nombreComando, comandoXML);
        } ///FIN CONTACTOS CRM
        else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EVENTOS_EXTERNOS))) {
            respuesta = eventoExternoBean.consultarEventosExternos(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "consultarEventosExternos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_CATEGORIAS))) {
            respuesta = eventoExternoBean.editarCategorias(comandoXML);
            reportarALogExcepciones(eventoExternoBean.getClass().getCanonicalName(), "actualizarCategoriasContacto", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_LABORATORIO))) {
            respuesta = laboratorioBean.eliminarLaboratorio(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "eliminarlaboratorio", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_LABORATORIO))) {
            respuesta = reservaInventarioBean.consultarReservasLaboratorio(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "consultarReservasLaboratorio", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIO))) {
            respuesta = laboratorioBean.consultarLaboratorio(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "consultarLaboratorio", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIOS_AUTORIZADOS))) {
            respuesta = laboratorioBean.consultarLaboratoriosAutorizados(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "consultarLaboratoriosAutorizados", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_AUTORIZADO_LABORATORIO))) {
            respuesta = laboratorioBean.consultarAutorizadoLaboratorio(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "consultarAutorizadoLaboratorio", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO))) {
            respuesta = reservaInventarioBean.crearReserva(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "crearReserva", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIOS))) {
            respuesta = laboratorioBean.consultarLaboratorios(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "consultarLaboratorios", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HORARIO_DISPONIBLE_LABORATORIO))) {
            respuesta = laboratorioBean.darHorarioDisponibleLaboratorio(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "darHorarioDisponiblelaboratorio", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_PERSONA))) {
            respuesta = reservaInventarioBean.consultarReservasPersona(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "consultarReservasPersona", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVA_INVENTARIO))) {
            respuesta = reservaInventarioBean.cancelarReserva(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "cancelarReserva", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVA_INVENTARIO))) {
            respuesta = reservaInventarioBean.consultarReserva(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "consultarReserva", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_RESERVAS_LABORATORIO))) {
            respuesta = reservaInventarioBean.consultarRangoReservas(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "consultarRangoReserva", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO))) {
            respuesta = reservaInventarioBean.cancelarGrupoReservas(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "cancelarRangoReserva", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIOS_ENCARGADO))) {
            respuesta = laboratorioBean.consultarLaboratoriosEncargado(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "consultarLaboratoriosEncargado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_MARCAR_GRUPO_RESERVAS_LABORATORIO))) {
            respuesta = reservaInventarioBean.marcarGrupoReservas(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "marcarGrupoReserva", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_HORARIO_OCUPADO_LABORATORIO))) {
            respuesta = laboratorioBean.darOcupacionLaboratorio(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "consultarLaboratoriosEncargado", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_ACCIONES))) {
            respuesta = accionesBean.darAcciones(comandoXML);
            reportarALogExcepciones(accionesBean.getClass().getCanonicalName(), "darAcciones", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTIVAR_LABORATORIO))) {
            respuesta = laboratorioBean.activarLaboratorio(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "activarLaboratorio", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIOS_ADMINISTRADOR))) {
            respuesta = laboratorioBean.consultarLaboratoriosAdministrador(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "consultarLaboratoriosAdministrador", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVA_ISMULTIPLE))) {
            respuesta = reservaInventarioBean.isMultiple(comandoXML);
            reportarALogExcepciones(laboratorioBean.getClass().getCanonicalName(), "isMultiple", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVAMULTIPLE))) {
            respuesta = reservaInventarioBean.cancelarReservaMultiple(comandoXML);
            reportarALogExcepciones(reservaInventarioBean.getClass().getCanonicalName(), "cancelarReservaMultiple", respuesta, fa, nombreComando, comandoXML);
        } /*else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_STAFF_POR_NOMBRE))) {
        respuesta = staffDepartamentoBean.consultarStaffPorNombres(comandoXML);
        reportarALogExcepciones(staffDepartamentoBean.getClass().getCanonicalName(), "consultarStaffPorNombres", respuesta, fa, nombreComando, comandoXML);//acciones rol reportante sisinfo // cargadores compartidos
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_STAFF_POR_APELLIDOS))) {
        respuesta = staffDepartamentoBean.consultarStaffPorApelllidos(comandoXML);
        reportarALogExcepciones(staffDepartamentoBean.getClass().getCanonicalName(), "consultarStaffPorApellidos", respuesta, fa, nombreComando, comandoXML);//acciones rol reportante sisinfo // cargadores compartidos
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_STAFF_POR_CORREO))) {
        System.out.println("\n\n*********LLEGO A enRutarComandoConsulta-CMD_CONSULTAR_STAFF_POR_CORREO en NucleoBean *********\n\n");
        respuesta = staffDepartamentoBean.consultarStaffPorCorreo(comandoXML);
        reportarALogExcepciones(staffDepartamentoBean.getClass().getCanonicalName(), "consultarStaffPorCorreo", respuesta, fa, nombreComando, comandoXML);//acciones rol reportante sisinfo // cargadores compartidos
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MIEMBRO_STAFF_DEPARTAMENTO))) {
        System.out.println("\n\n*********LLEGO A enRutarComandoConsulta-CMD_AGREGAR_MIEMBRO_STAFF_DEPARTAMENTO en NucleoBean *********\n\n");
        respuesta = staffDepartamentoBean.agregarMiembroStaff1(comandoXML);
        reportarALogExcepciones(staffDepartamentoBean.getClass().getCanonicalName(), "agregarMiembroStaff", respuesta, fa, nombreComando, comandoXML);//acciones rol reportante sisinfo // cargadores compartidos
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_EDITAR_MIEMBRO_STAFF_DEPARTAMENTO))) {
        System.out.println("\n\n*********LLEGO A enRutarComandoConsulta-CMD_EDITAR_MIEMBRO_STAFF_DEPARTAMENTO en NucleoBean *********\n\n");
        respuesta = staffDepartamentoBean.actualizarMiembroStaff1(comandoXML);
        reportarALogExcepciones(staffDepartamentoBean.getClass().getCanonicalName(), "editarMiembroStaff", respuesta, fa, nombreComando, comandoXML);//acciones rol reportante sisinfo // cargadores compartidos
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_STAFF_TODOS))) {
        respuesta = staffDepartamentoBean.consultarMiembrosStaff(comandoXML);
        reportarALogExcepciones(staffDepartamentoBean.getClass().getCanonicalName(), "consultarMiembrosStaff", respuesta, fa, nombreComando, comandoXML);//acciones rol reportante sisinfo // cargadores compartidos
        }else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_STAFF))) {
        System.out.println("\n\n*********LLEGO A enRutarComandoConsulta-CMD_CONSULTAR_TIPOS_STAFF en NucleoBean *********\n\n");
        respuesta = staffDepartamentoBean.consultarTiposStaff(comandoXML);
        System.out.println("\n\n*********LLEGO A enRutarComandoConsulta-reportarALogExcepciones en NucleoBean *********\n\n");
        reportarALogExcepciones(staffDepartamentoBean.getClass().getCanonicalName(), "consultarTiposStaff", respuesta, fa, nombreComando, comandoXML);//acciones rol reportante sisinfo // cargadores compartidos
        System.out.println("\n\n*********SALIO DE enRutarComandoConsulta-reportarALogExcepciones en NucleoBean *********\n\n");
        }*/ else {
            System.err.println("COMANDO NO ENCONTRADO:1 " + nombreComando);
            respuesta = getConstanteBean().getConstante(Constantes.MSJ_CONSULTA_INVALIDA);
            reportarALogExcepciones(constanteBean.getClass().getCanonicalName(), "getConstante", respuesta, fa, nombreComando, comandoXML);
        }
        return respuesta;
    }

    private void reportarALogExcepciones(String nombreBean, String nombreMetodo, String respuesta, Timestamp fa, String nombreComanddo, String xmlEntrada) {
        System.out.println("Entro a reportar log excepciones");
        reporteExcepcionesBean.crearLogMensaje(nombreBean, nombreMetodo, respuesta, fa, nombreComanddo, xmlEntrada);
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
}
