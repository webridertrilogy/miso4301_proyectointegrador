/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.bo.AccionBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.CalificacionJurado;
import co.uniandes.sisinfo.entities.HorarioSustentacionTesis;
import co.uniandes.sisinfo.entities.InscripcionSubareaInvestigacion;
import co.uniandes.sisinfo.entities.PeriodoTesis;
import co.uniandes.sisinfo.entities.SubareaInvestigacion;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.TemaTesis1;
import co.uniandes.sisinfo.entities.Tesis1;
import co.uniandes.sisinfo.entities.Tesis2;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.GrupoInvestigacion;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.serviciosfuncionales.ComentarioTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.CursoMaestriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CursoTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InscripcionSubareaInvestigacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReportesFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TemaTesis1FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis1FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis2FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.GrupoInvestigacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Ivan Melo
 */
@Stateless
public class TesisBean implements TesisBeanRemote, TesisBeanLocal {

    //----CONSTANTES-------------
    private final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.TesisBeanRemote";
    private final static String NOMBRE_METODO_TIMER = "manejoTimmersTesisMaestria";
    //--------------------------
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ProfesorFacadeRemote profesorFacade;
    @EJB
    private EstudianteFacadeRemote estudianteFacadeRemote;
    @EJB
    private PeriodoTesisFacadeLocal periodoFacadelocal;
    @EJB
    private InscripcionSubareaInvestigacionFacadeLocal inscrippcionsubFacadeLocal;
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private Tesis1FacadeLocal tesis1Facade;
    @EJB
    private CursoMaestriaFacadeLocal cursoMaestriaFacade;
    @EJB
    private Tesis2FacadeLocal tesis2Facade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private CursoTesisFacadeLocal cursoTesisFacade;
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    @EJB
    private HistoricosTesisBeanRemote historicoTesis;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    @EJB
    private ReportesFacadeRemote reporteFacadeRemote;
    @EJB
    private TemaTesis1FacadeLocal temaTesisFacade;
    @EJB
    private GrupoInvestigacionFacadeRemote grupoInvestigacionFacade;
    @EJB
    private ComentarioTesisFacadeLocal comentarioTesisFacade;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacade;
    @EJB
    private Tesis2BeanLocal tesis2Bean;
    @EJB
    private Tesis1BeanLocal tesis1Bean;
    @EJB
    private AccionVencidaBeanRemote accionVencidaBean;
    //---OTROS--------------------
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorTesisMaestria conversor;
    /*
     * nuevo manejo de tareas
     */
    @EJB
    private TareaSencillaRemote tareaSencillaBean;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;
    @EJB
    private TareaMultipleFacadeRemote tareaMultipleFacade;
    @EJB
    private InscripcionSubAreaInvestiBeanLocal inscripcionesSubareaBean;
    Collection<AccionBO> acciones;

    public TesisBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            estudianteFacadeRemote = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);

            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);

            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            historicoTesis = (HistoricosTesisBeanRemote) serviceLocator.getRemoteEJB(HistoricosTesisBeanRemote.class);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            reporteFacadeRemote = (ReportesFacadeRemote) serviceLocator.getRemoteEJB(ReportesFacadeRemote.class);
            grupoInvestigacionFacade = (GrupoInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(GrupoInvestigacionFacadeRemote.class);

            nivelFormacionFacade = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            accionVencidaBean = (AccionVencidaBeanRemote) serviceLocator.getRemoteEJB(AccionVencidaBeanRemote.class);

            conversor = new ConversorTesisMaestria();

            /*
             * Manejo nuevo tareas y alertas
             */
            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);

            tareaMultipleFacade = (TareaMultipleFacadeRemote) serviceLocator.getRemoteEJB(TareaMultipleFacadeRemote.class);

        } catch (Exception e) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //=====================================================================================================
    //===================================  METODOS AUXILARES: OTROS =======================================================
    //=====================================================================================================
    public ConstanteRemote getConstanteBean() {
        return constanteBean;


    }

    private ParserT getParser() {
        return parser;


    }

    public String darSemestresTesis(String xml) {
        try {
            Collection<PeriodoTesis> periodos = periodoFacadelocal.findAll();
            Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS), "");
            for (PeriodoTesis periodoTesis : periodos) {
                Secuencia sec = conversor.pasarSemestreASecuencia(periodoTesis);
                secPrincipal.agregarSecuencia(sec);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secPrincipal);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PERIDOS_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception e) {
            return null;


        }
    }

    /**
     * metodo llamado para pasar a Historicos una solicitud de tesis rechazada
     * @param tesis
     */
    /*
    public void pasarSolicitudTesis1ARechazadas() {
    try {
    //System.out.println("va a correr metodo migrar...");
    String resp = historicoTesis.migrarTesis1Rechazadas("");
    Collection<Tesis1> tesises = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR));
    Collection<Tesis1> tesises2 = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_COORDINACION_MAESTRIA));
    
    if (resp != null) {
    tesises = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR));
    tesises2 = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_COORDINACION_MAESTRIA));
    for (Tesis1 tesis1 : tesises2) {
    tesis1Facade.remove(tesis1);
    }
    for (Tesis1 tesis1 : tesises) {
    tesis1Facade.remove(tesis1);
    }
    return;
    }
    } catch (Exception ex) {
    Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
     *
     */
    /*
    //: este metdodo quien lo llama,...
    private void pasarInformacionEstudianteAHistoricos(Tesis2 tesis) {
    try {
    String correoEstud = tesis.getEstudiante().getPersona().getCorreo();
    InscripcionSubareaInvestigacion subarea = inscrippcionsubFacadeLocal.findByCorreoEstudiante(correoEstud);
    Tesis1 tesis1 = tesis1Facade.findByCorreoEstudiante(correoEstud).iterator().next();
    ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
    Secuencia secID = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
    secs.add(secID);
    String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_PASAR_DATOS_TESIS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
    String resp = historicoTesis.migrarTesisCumplidas(comandoXML);
    if (resp != null) {
    //verificar exito if(){
    inscrippcionsubFacadeLocal.remove(subarea);
    tesis1Facade.remove(tesis1);
    tesis2Facade.remove(tesis);
    }
    } catch (Exception ex) {
    Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
     * 
     */
    public void manejoTimmersTesisMaestria(String comando) {
        String[] pamrametros = comando.split("-");
        if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_SUBAREA))) {
            enviarCorreoInscripcionSubarea(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_1))) {
            generarCorreoVencimientoInscripcionTesis1FechaAsesor(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_1_OPCIONAL))) {
            generarCorreoVencimientoInscripcionTesis1Opcional(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_SUBIR_NOTAS_TESIS_1))) {
            enviarCorreoUltimodiaNotasTesis1(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_COLOCAR_PENDIENTE_NOTAS_TESIS_1))) {
            enviarCorreoUltimoDiaPendientesTesis1(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_QUITAR_PENDIENTE_NOTAS_TESIS_1))) {
            enviarCorreoUltimoDiaLevantarPendienteTesis1(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ARCHIVOS_JURADOS_SUSTENTACION_TESIS_2))) {
            //     generarCorreoSustentacionTesis(Long.parseLong(pamrametros[1].trim()));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_2))) {
            generarCorreoUltimaFechaInscripcionTesis2(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_REPROBAR_TESIS_2_SIN_SUSTENTACION))) {
            generarCorreoUltimaFechaReprobarTesis2SinSustentacion(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_PEDIR_PENDIENTE_ESPECIAL_TESIS_2))) {
            generarCorreoUltimaFechaPendienteEspecialTesis2(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_SUSTENTACION_TESIS_2))) {
            generarCorreoUltimaFechaSustentacionTesis2(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_SUBAREA_REGANHO_CORREO))) {
            enviarCorreosAsesoresConInscripcionesASubareaPorAprobarYFechaVencida(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_SUBAREA))) {
            rechazoAutomaticoInscripcionesSubareaPorFechaAsesor(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } //------coordinador subarea
        else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_SUBAREA_COORDINADOR_SUBAREA_CORREO_REGANHO))) {
            enviarCorreosCoordinadoresSubareaConInscripcionesASubareaPorAprobarYFechaVencida(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_SUBAREA_COORDINADOR_SUBAREA))) {
            rechazoAutomaticoInscripcionesSubareaCoordinacion(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } //inscripcion tesis 1:---------------------------
        //se le vencio el tiempo al asesor:
        else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_TESIS_1_REGANHO_ASESOR))) {
            enviarCorreosAsesoresConInscripcionesATesis1PorAprobarYFechaVencida(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_TESIS_1))) {
            rechazoAutomaticoInscripcionTesis1Asesor(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
            //eliminarTemasDeTesis1();
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION_CORREO_REGANHO))) {
            enviarCorreoCoordinacionPersonasPendientesPorAprobarYFechaTerminadaTesis1(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } //se le vencio el tiempo a coordinacion
        else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION))) {
            rechazoAutomaticoInscripcionesTesis1Coordinacion(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_SUBIR_NOTAS_TESIS_1))) {
            // les coloca perdido automaticamente no creo k este bien...
            calificacionAutomaticaTesis1Perdida(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
            // les coloca perdido automaticamente no creo k este bien...
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_QUITAR_PENDIENTE_NOTAS_TESIS_1))) {
            calificacionAutomaticaTesis1Pendiente(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_INSCRIPCION_TESIS_2_REGANHO_ASESOR))) {
            enviarCorreoAsesoresTesis2FechaAprobacionVencidaYGentePorAprobar(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_INSCRIPCION_TESIS_2))) {
            rechazoAutomaticoTesis2Asesor(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_SUSTENTACION_TESIS_2))) {
            calificacionAutomaticaTesis2NoSustentada(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_ESTADO_SUSTENTACION_TESIS_2_A_ESPERAR_ARTICULO))) {
            crearTareaTesis2SubirArticuloFin(tesis2Facade.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMA_FECHA_PROPUESTAS_TESIS))) {
            enviarCorreosPropuestasTesis(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_COMENTARIOS_TESIS_30_PORCIENTO))) {
            crearTareasAAsesoresTesisSobreComentariosTesis30Porciento(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FECHA_RETIROS_TESIS))) {
            enviarCorreosDeFechaMaximaRetirosTesis(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_JURADOD_TESIS))) {
            tesis2Bean.comportamientoJuradosDiaSustentacion(Long.parseLong(pamrametros[1].trim()));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_CREAR_TAREAS_TESIS1_CALIFICACION))) {
            tesis1Bean.crearTareaCalificarTesis1(pamrametros[1].trim());
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_CREAR_TAREAS_TESIS1_CALIFICACION_PENDIENTE))) {
            tesis1Bean.crearTareaCalificarTesis1Pendiente(pamrametros[1].trim());
        } // aca fecha pendeinte especial: CMD_ENVIAR_CORREOS_SUSTENTACIONES_TESIS_2_PENDIENTE_ESPECIAL
        else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREOS_SUSTENTACIONES_TESIS_2_PENDIENTE_ESPECIAL))) {
            generarCorreoUltimaFechaSustentacionTesis2PendienteEspecial(periodoFacadelocal.find(Long.parseLong(pamrametros[1].trim())));
        } //HISTORICOS TESIS 1 Y TESIS 2
        else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_1_RECHAZADAS_A_HISTORICOS))) {
            tesis1Bean.migrarTesisRechazada(Long.parseLong(pamrametros[1].trim()));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS))) {
            tesis1Bean.migrarTesisRetiradas();
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS))) {
            tesis1Bean.migrarTesisPerdidas();
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RECHAZADAS_A_HISTORICOS))) {
            tesis2Bean.migrarTesisRechazada(Long.parseLong(pamrametros[1].trim()));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS))) {
            tesis2Bean.migrarTesisRetiradas();
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS))) {
            tesis2Bean.migrarTesisPerdidas();
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS))) {
            tesis2Bean.migrarTesisTerminadas();
        }//Se vencio el tiempo para seleccionar horario
        else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_SELECCIONAR_HORARIO))) {
            tesis2Bean.avisarAEstudianteProfesorYCoordinacionVencimientoTiempoSeleccionHorario(pamrametros[1]);
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_RECORDATORIO_A_DIRECTOR_NOTAS_FALTANTES))) {
            tesis2Bean.recordatorioDirectorNotasFaltantes(pamrametros[1].trim(), pamrametros[2].trim());
        }

    }

    public String establecerFechasPeriodo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secSemestre = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
            PeriodoTesis semestre = pasarSecuenciaAPeriodoConfiguracion(secSemestre);

            //TODO: aca cambiar las fechas para que sea hasta las 23:59:59 del dia antes seleccionado
            if (semestre == null) {
                throw new Exception("Error Parceando periodo");
            }
            PeriodoTesis perBD = periodoFacadelocal.findByPeriodo(semestre.getPeriodo());
            boolean editado = false;
            if (perBD != null) {
                semestre.setId(perBD.getId());
                periodoFacadelocal.edit(semestre);
                editado = true;
            } else {
                periodoFacadelocal.create(semestre);
            }
            editarTimersPeriodo(semestre, editado);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHAS_PERIODO_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHAS_PERIODO_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private PeriodoTesis pasarSecuenciaAPeriodoConfiguracion(Secuencia secSemestre) {
        try {

            PeriodoTesis periodo = new PeriodoTesis();
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Secuencia secId = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));


            if (secId != null) {
                periodo.setId(Long.parseLong(secId.getValor()));
            }
            Secuencia secPeriodo = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO));
            if (secPeriodo != null) {
                periodo.setPeriodo(secPeriodo.getValor());
            }
            Secuencia secMaxFechaInscripcionSubarea = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INSCRIPCION_SUBAREA));
            if (secMaxFechaInscripcionSubarea != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaInscripcionSubarea.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaInscripcionSubarea(new Timestamp(fechaInicioDate.getTime()));
            }
            Secuencia secMaxFechaAprobacionInscripcionSubarea = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_INSCRIPCION_SUBAREA));
            if (secMaxFechaAprobacionInscripcionSubarea != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaAprobacionInscripcionSubarea.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaAprobacionInscripcionSubarea(new Timestamp(fechaInicioDate.getTime()));
            }
            //-------------------------
            Secuencia secMaxFechaAprobacionInscripcionSubareaCoordinacion = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_INSCRIPCION_SUBAREA_COORDINACION));
            if (secMaxFechaAprobacionInscripcionSubareaCoordinacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaAprobacionInscripcionSubareaCoordinacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaAprobacionInscripcionSubareaCoordinacion(new Timestamp(fechaInicioDate.getTime()));
            }
            //--------------------------
            Secuencia secMaxFechaInscripcionT1 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INSCRIPCIONT1));
            if (secMaxFechaInscripcionT1 != null) {

                //como sea pasar a Date
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaInscripcionT1.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaInscripcionT1(new Timestamp(fechaInicioDate.getTime()));
            }
            Secuencia secMaxFechaAprobacionTesis1 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_T1));
            if (secMaxFechaAprobacionTesis1 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaAprobacionTesis1.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaAprobacionTesis1(new Timestamp(fechaInicioDate.getTime()));
            }
            Secuencia secMaxFechaSubirTesis1 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NOTA_T1));
            if (secMaxFechaSubirTesis1 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaSubirTesis1.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaSubirNotaTesis1(new Timestamp(fechaInicioDate.getTime()));
            }
            Secuencia secMaxPonerPendienteT1 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_PENDIENTE_T1));
            if (secMaxPonerPendienteT1 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxPonerPendienteT1.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaPedirPendienteTesis1(new Timestamp(fechaInicioDate.getTime()));
            }
            Secuencia secMaxQuitarPendienteT1 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_LEVANTAR_PENDIENTE_T1));

            if (secMaxQuitarPendienteT1 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxQuitarPendienteT1.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaLevantarPendienteTesis1(new Timestamp(fechaInicioDate.getTime()));
            }

            Secuencia secUltimaFehcaInscripcionTesis2 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN_INSCRIPCION_TESIS_2));
            if (secUltimaFehcaInscripcionTesis2 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secUltimaFehcaInscripcionTesis2.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaUltimaSolicitarTesis2(new Timestamp(fechaInicioDate.getTime()));
            }

            Secuencia secMaxFechaAprobacionTesis2 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_T2));
            if (secMaxFechaAprobacionTesis2 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaAprobacionTesis2.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaAprobacionTesis2(new Timestamp(fechaInicioDate.getTime()));
            }
            Secuencia secFechaUltimaPendienteEspecialTesis2 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ULTIMA_PENDIENTE_ESPECIAL_TESIS_2));

            if (secFechaUltimaPendienteEspecialTesis2 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secFechaUltimaPendienteEspecialTesis2.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaUltimaPendienteEspecialTesis2(new Timestamp(fechaInicioDate.getTime()));
            }

            Secuencia secFechaUltimaReportarNotaReprobadaSSTesis2 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ULTIMA_REPORTAR_REPROBADA_SS_TESIS_2));

            if (secFechaUltimaReportarNotaReprobadaSSTesis2 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secFechaUltimaReportarNotaReprobadaSSTesis2.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaUltimaReportarNotaReprobadaSSTesis2(new Timestamp(fechaInicioDate.getTime()));
            }

            Secuencia secFechaUltimaSustentarTesis2 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ULTIMA_SUSTENTACION_TESIS_2));

            if (secFechaUltimaSustentarTesis2 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secFechaUltimaSustentarTesis2.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaUltimaSustentarTesis2(new Timestamp(fechaInicioDate.getTime()));
            }
            secFechaUltimaSustentarTesis2 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_SUBIR_TEMAS_TESIS));
            if (secFechaUltimaSustentarTesis2 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secFechaUltimaSustentarTesis2.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaMaximaPublicacionTemasTesis(new Timestamp(fechaInicioDate.getTime()));
            }

            secFechaUltimaSustentarTesis2 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_30_PORCIENTO));
            if (secFechaUltimaSustentarTesis2 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secFechaUltimaSustentarTesis2.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaDel30Porciento(new Timestamp(fechaInicioDate.getTime()));
            }//---------------maxFechaAprobacionTesis1Coordinacion

            Secuencia secMaxFechaAprobacionTesis1Coordinacion = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_T1_COODINACION));
            if (secMaxFechaAprobacionTesis1 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaAprobacionTesis1Coordinacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setMaxFechaAprobacionTesis1Coordinacion(new Timestamp(fechaInicioDate.getTime()));
            }

            Secuencia secMaxFechaRetiros = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_RETIRAR_TESIS));
            if (secMaxFechaRetiros != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaRetiros.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaMaximaRetiro(new Timestamp(fechaInicioDate.getTime()));
            }
            //----------------
            Secuencia secMaxFechaSustentacionPendiente = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_SUSTENTACION_PENDIENTE_ESPECIAL_TESIS_2));
            if (secMaxFechaSustentacionPendiente != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secMaxFechaSustentacionPendiente.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaMaxSustentacionT2PendEspecial(new Timestamp(fechaInicioDate.getTime()));
            }

            Secuencia secFechaUltimaHorarioSustentacion = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_SOLICITAR_HORARIO_SUSTENTACION));
            if (secFechaUltimaHorarioSustentacion != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secFechaUltimaHorarioSustentacion.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaMaximaSolicitarHorario(new Timestamp(fechaInicioDate.getTime()));
            }

            Secuencia secFechaUltimaCalificacionTesis2 = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_CALIFICAR_TESIS_2));
            if (secFechaUltimaCalificacionTesis2 != null) {
                Date fechaInicioDateTemp = sdfHMS.parse(secFechaUltimaCalificacionTesis2.getValor());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaInicioDateTemp);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                Date fechaInicioDate = calendar.getTime();
                periodo.setFechaMaximaCalificarTesis2(new Timestamp(fechaInicioDate.getTime()));
            }

            return periodo;
        } catch (ParseException ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private Secuencia pasarPeriodoConfiguracionASecuencia(PeriodoTesis periodo) {

        Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long id = periodo.getId();

        if (id != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), periodo.getId().toString());
            secSemestre.agregarSecuencia(secId);
        }
        if (periodo.getPeriodo() != null) {
            Secuencia secPeriodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), periodo.getPeriodo());
            secSemestre.agregarSecuencia(secPeriodo);
        }

        if (periodo.getMaxFechaInscripcionSubarea() != null) {
            Secuencia secMaxFechaInscripcionSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INSCRIPCION_SUBAREA), sdf.format(new Date(periodo.getMaxFechaInscripcionSubarea().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaInscripcionSubarea);
        }

        if (periodo.getMaxFechaAprobacionInscripcionSubarea() != null) {
            Secuencia secMaxFechaAprobacionInscripcionSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_INSCRIPCION_SUBAREA), sdf.format(new Date(periodo.getMaxFechaAprobacionInscripcionSubarea().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaAprobacionInscripcionSubarea);
        }
        //-*------------------------------------------------------
        if (periodo.getMaxFechaAprobacionInscripcionSubareaCoordinacion() != null) {
            Secuencia secMaxFechaAprobacionInscripcionSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_INSCRIPCION_SUBAREA_COORDINACION), sdf.format(new Date(periodo.getMaxFechaAprobacionInscripcionSubareaCoordinacion().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaAprobacionInscripcionSubarea);
        }
        //--------------------------------------------------------

        if (periodo.getMaxFechaInscripcionT1() != null) {

            Secuencia secMaxFechaInscripcionT1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INSCRIPCIONT1), sdf.format(new Date(periodo.getMaxFechaInscripcionT1().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaInscripcionT1);

        }

        if (periodo.getMaxFechaAprobacionTesis1() != null) {
            Secuencia secMaxFechaAprobacionTesis1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_T1), sdf.format(new Date(periodo.getMaxFechaAprobacionTesis1().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaAprobacionTesis1);
        }

        //-------
        if (periodo.getMaxFechaAprobacionTesis1Coordinacion() != null) {
            Secuencia secMaxFechaAprobacionTesis1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_T1_COODINACION), sdf.format(new Date(periodo.getMaxFechaAprobacionTesis1Coordinacion().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaAprobacionTesis1);
        }
        //-------

        if (periodo.getMaxFechaSubirNotaTesis1() != null) {

            Secuencia secMaxFechaInscripcionT1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NOTA_T1), sdf.format(new Date(periodo.getMaxFechaSubirNotaTesis1().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaInscripcionT1);
        }
        if (periodo.getMaxFechaPedirPendienteTesis1() != null) {
            Secuencia secMaxFechaInscripcionT1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_PENDIENTE_T1), sdf.format(new Date(periodo.getMaxFechaPedirPendienteTesis1().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaInscripcionT1);
        }
        if (periodo.getMaxFechaLevantarPendienteTesis1() != null) {
            Secuencia secMaxFechaInscripcionT1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_LEVANTAR_PENDIENTE_T1), sdf.format(new Date(periodo.getMaxFechaLevantarPendienteTesis1().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaInscripcionT1);
        }
        //aca colocar lo mismo para tesis2

        if (periodo.getFechaUltimaSolicitarTesis2() != null) {

            Secuencia secUltimaFehcaInscripcionTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN_INSCRIPCION_TESIS_2), sdf.format(new Date(periodo.getFechaUltimaSolicitarTesis2().getTime())));
            secSemestre.agregarSecuencia(secUltimaFehcaInscripcionTesis2);
        }

        if (periodo.getMaxFechaAprobacionTesis2() != null) {
            Secuencia secMaxFechaAprobacionTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_APROBAR_T2), sdf.format(new Date(periodo.getMaxFechaAprobacionTesis2().getTime())));
            secSemestre.agregarSecuencia(secMaxFechaAprobacionTesis2);
        }

        if (periodo.getFechaUltimaPendienteEspecialTesis2() != null) {

            Secuencia secUltimaFehcaInscripcionTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ULTIMA_PENDIENTE_ESPECIAL_TESIS_2), sdf.format(new Date(periodo.getFechaUltimaPendienteEspecialTesis2().getTime())));
            secSemestre.agregarSecuencia(secUltimaFehcaInscripcionTesis2);
        }


        if (periodo.getFechaUltimaReportarNotaReprobadaSSTesis2() != null) {

            Secuencia secUltimaFehcaInscripcionTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ULTIMA_REPORTAR_REPROBADA_SS_TESIS_2), sdf.format(new Date(periodo.getFechaUltimaReportarNotaReprobadaSSTesis2().getTime())));
            secSemestre.agregarSecuencia(secUltimaFehcaInscripcionTesis2);
        }

        if (periodo.getFechaUltimaSustentarTesis2() != null) {

            Secuencia secUltimaFehcaInscripcionTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ULTIMA_SUSTENTACION_TESIS_2), sdf.format(new Date(periodo.getFechaUltimaSustentarTesis2().getTime())));
            secSemestre.agregarSecuencia(secUltimaFehcaInscripcionTesis2);
        }
        if (periodo.getFechaMaximaPublicacionTemasTesis() != null) {

            Secuencia secUltimaFehcaInscripcionTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_SUBIR_TEMAS_TESIS), sdf.format(new Date(periodo.getFechaMaximaPublicacionTemasTesis().getTime())));
            secSemestre.agregarSecuencia(secUltimaFehcaInscripcionTesis2);
        }
        if (periodo.getFechaDel30Porciento() != null) {

            Secuencia secUltimaFehcaInscripcionTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_30_PORCIENTO), sdf.format(new Date(periodo.getFechaDel30Porciento().getTime())));
            secSemestre.agregarSecuencia(secUltimaFehcaInscripcionTesis2);
        }
        if (periodo.getFechaMaximaRetiro() != null) {
            Secuencia secUltimaFehcaRetiroTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_RETIRAR_TESIS), sdf.format(new Date(periodo.getFechaMaximaRetiro().getTime())));
            secSemestre.agregarSecuencia(secUltimaFehcaRetiroTesis);
        }
        if (periodo.getFechaMaxSustentacionT2PendEspecial() != null) {
            Secuencia secUltimaFehcaRetiroTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_SUSTENTACION_PENDIENTE_ESPECIAL_TESIS_2), sdf.format(new Date(periodo.getFechaMaxSustentacionT2PendEspecial().getTime())));
            secSemestre.agregarSecuencia(secUltimaFehcaRetiroTesis);
        }

        if (periodo.getFechaMaximaSolicitarHorario() != null) {
            Secuencia secFechaMaximaSolicitarHorario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_SOLICITAR_HORARIO_SUSTENTACION), sdf.format(new Date(periodo.getFechaMaximaSolicitarHorario().getTime())));
            secSemestre.agregarSecuencia(secFechaMaximaSolicitarHorario);
        }

        if (periodo.getFechaMaximaCalificarTesis2() != null) {
            Secuencia secFechaMaximaCalificarTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_MAXIMA_CALIFICAR_TESIS_2), sdf.format(new Date(periodo.getFechaMaximaCalificarTesis2().getTime())));
            secSemestre.agregarSecuencia(secFechaMaximaCalificarTesis2);
        }

        return secSemestre;
    }

    private void editarTimersPeriodo(PeriodoTesis semestre, boolean editado) {

        String mensajeAsociadoInscripcionTesis1 = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_1) + "-" + semestre.getId();

        String mensajeAsociadoInscripcionTesis1Opcional = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_1_OPCIONAL) + "-" + semestre.getId();

        String mensajeAsociadoSubirNotasTesis1 = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_SUBIR_NOTAS_TESIS_1) + "-" + semestre.getId();

        String mensajeAsociadoColocarPendienteNotasTesis1 = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_COLOCAR_PENDIENTE_NOTAS_TESIS_1) + "-" + semestre.getId();

        String mensajeAsociadoQuitarPendienteNotasTesis1 = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_QUITAR_PENDIENTE_NOTAS_TESIS_1) + "-" + semestre.getId();

        String msjUltimaFechaSolicitarInscT2 = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_TESIS_2) + "-" + semestre.getId();

        String msjUltimaFechaPerderTesis2SinSustentacion = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_REPROBAR_TESIS_2_SIN_SUSTENTACION) + "-" + semestre.getId();

        String msjUltimaFechaPendienteEspecialT2 = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_PEDIR_PENDIENTE_ESPECIAL_TESIS_2) + "-" + semestre.getId();

        String msjUltimaFechaSustentacionT2 = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_SUSTENTACION_TESIS_2) + "-" + semestre.getId();

        String msjUltimaFechaInscripcionSubarea = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_INSCRIPCION_SUBAREA) + "-" + semestre.getId();

        //TIMERS DE COMPORTAMIENTO
        String comportamientoAsociadoInscripcionSubarea = getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_SUBAREA) + "-" + semestre.getId();

        String comportamientoAsociadoInscripcionSubareaEnvioCorreoReganho = getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_SUBAREA_REGANHO_CORREO) + "-" + semestre.getId();

        String comportamientoAsociadoInscripcionSubareaCoordinacion = getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_SUBAREA_COORDINADOR_SUBAREA) + "-" + semestre.getId();

        String comportamientoAsociadoInscripcionSubareaCoordinacionCorreoReganho = getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_SUBAREA_COORDINADOR_SUBAREA_CORREO_REGANHO) + "-" + semestre.getId();

        String comportamientoAsociadoInscripcionTesis1 = getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_TESIS_1) + "-" + semestre.getId();

        String comportamientoAsociadoInscripcionTesis1ReanhoAsesor = getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_TESIS_1_REGANHO_ASESOR) + "-" + semestre.getId();

        String comportamientoAsociadoInscripcionTesis1Coordinacion = getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION) + "-" + semestre.getId();

        String comportamientoAsociadoInscripcionTesis1CoordinacionReganho = getConstanteBean().getConstante(Constantes.CMD_FIN_INSCRIPCION_TESIS_1_COORDINACION_CORREO_REGANHO) + "-" + semestre.getId();

        String comportamientoAsociadoSubirNotasTesis1 = getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_SUBIR_NOTAS_TESIS_1) + "-" + semestre.getId();

        String comportamientoAsociadoQuitarPendienteNotasTesis1 = getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_QUITAR_PENDIENTE_NOTAS_TESIS_1) + "-" + semestre.getId();

        String comportamientoUltimaFechaSolicitarInscT2 = getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_INSCRIPCION_TESIS_2) + "-" + semestre.getId();

        String comportamientoUltimaFechaSolicitarInscT2REganhoAsesor = getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_INSCRIPCION_TESIS_2_REGANHO_ASESOR) + "-" + semestre.getId();

        String comportamientoUltimaFechaSustentacionT2 = getConstanteBean().getConstante(Constantes.CMD_FIN_DIA_SUSTENTACION_TESIS_2) + "-" + semestre.getId();

        String msjUltimoPlazoSubirPropuestasTesis = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMA_FECHA_PROPUESTAS_TESIS) + "-" + semestre.getId();

        String msjPlazoComentarios30Porciento = getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_COMENTARIOS_TESIS_30_PORCIENTO) + "-" + semestre.getId();

        String msjVencimientoFechaRetiros = getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FECHA_RETIROS_TESIS) + "-" + semestre.getId();

        String comportamientoCrearTareaSubirNotasTesis1 = getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_CREAR_TAREAS_TESIS1_CALIFICACION) + "-" + semestre.getId();

        String comportamientoCrearTareaSubirNotasTesis1Pendiente = getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_CREAR_TAREAS_TESIS1_CALIFICACION_PENDIENTE) + "-" + semestre.getId();

        String comportamientoEnvioCorreosSustentacionesPendienteEpecialTesis2 = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREOS_SUSTENTACIONES_TESIS_2_PENDIENTE_ESPECIAL) + "-" + semestre.getId();

        String mensajeAsociadoMigrarTesis2Terminados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS) + "-" + semestre.getId();

        String mensajeAsociadoMigrarTesis2Retirados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS) + "-" + semestre.getId();

        String mensajeAsociadoMigrarTesis2Perdidas = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS) + "-" + semestre.getId();

        String mensajeAsociadoMigrarTesis1Retirados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS) + "-" + semestre.getId();

        String mensajeAsociadoMigrarTesis1Perdidas = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS) + "-" + semestre.getId();

        String comportamientoUltimaFechaSolicitarHorario = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_SELECCIONAR_HORARIO) + "-" + semestre.getId();

        String comportamientoUltimoDiaCalificarTesis2 = getConstanteBean().getConstante(Constantes.CMD_ULTIMO_DIA_CALIFICAR_TESIS_2) + "-" + semestre.getId();


        //si se edito el periodo se debe eliminar los timmer anteriores
        if (editado) {
            //  TIMERS DE NOTIFICACION
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoInscripcionTesis1);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoInscripcionTesis1Opcional);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoSubirNotasTesis1);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoColocarPendienteNotasTesis1);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoQuitarPendienteNotasTesis1);

            timerGenerico.eliminarTimerPorParametroExterno(msjUltimaFechaSolicitarInscT2);
            timerGenerico.eliminarTimerPorParametroExterno(msjUltimaFechaPerderTesis2SinSustentacion);
            timerGenerico.eliminarTimerPorParametroExterno(msjUltimaFechaPendienteEspecialT2);
            timerGenerico.eliminarTimerPorParametroExterno(msjUltimaFechaSustentacionT2);
            timerGenerico.eliminarTimerPorParametroExterno(msjUltimaFechaInscripcionSubarea);

            //TIMERS DE COMPORTAMIENTO
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoInscripcionSubarea);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoInscripcionSubareaEnvioCorreoReganho);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoInscripcionSubareaCoordinacion);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoInscripcionSubareaCoordinacionCorreoReganho);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoInscripcionTesis1);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoInscripcionTesis1ReanhoAsesor);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoInscripcionTesis1Coordinacion);//
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoInscripcionTesis1CoordinacionReganho);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoSubirNotasTesis1);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoAsociadoQuitarPendienteNotasTesis1);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoUltimaFechaSolicitarInscT2);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoUltimaFechaSolicitarInscT2REganhoAsesor);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoUltimaFechaSustentacionT2);

            // nuevos requerimientos:
            timerGenerico.eliminarTimerPorParametroExterno(msjUltimoPlazoSubirPropuestasTesis);

            timerGenerico.eliminarTimerPorParametroExterno(msjPlazoComentarios30Porciento);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoUltimaFechaSolicitarHorario);

            //retiros
            timerGenerico.eliminarTimerPorParametroExterno(msjVencimientoFechaRetiros);
            //tareas subir nota
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoCrearTareaSubirNotasTesis1);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoCrearTareaSubirNotasTesis1Pendiente);
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoUltimoDiaCalificarTesis2);

            //PENDIENTE ESPECIAL
            timerGenerico.eliminarTimerPorParametroExterno(comportamientoEnvioCorreosSustentacionesPendienteEpecialTesis2);

            //HISTORICOS
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoMigrarTesis2Terminados);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoMigrarTesis2Retirados);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoMigrarTesis2Perdidas);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoMigrarTesis1Retirados);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoMigrarTesis1Perdidas);

        }

        //TODO: cambiar esto:


        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
//        Long fechaHaceUndia = fechaActual - undia;
//        Long fecha2Dias = fechaHaceUndia - undia;
//        Long fecha3Dias = fecha2Dias - undia;

        /*
         * crear los timers de recordatorio para asesores y estudiantes
         */
        Date hoy = new Date();
        if (semestre.getMaxFechaInscripcionT1().after(hoy)) {
            //if (new Timestamp(semestre.getMaxFechaInscripcionT1().getTime() - (2 * undia)).after(hoy))
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaInscripcionT1().getTime() - undia), mensajeAsociadoInscripcionTesis1,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de inscripción a tesis 1 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getMaxFechaInscripcionT1().getTime() - (2 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaInscripcionT1().getTime() - (2 * undia)), mensajeAsociadoInscripcionTesis1,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de inscripción a tesis 1 ha cambiado y se enviara correo faltando dos dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getMaxFechaInscripcionT1().getTime() - (3 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaInscripcionT1().getTime() - (3 * undia)), mensajeAsociadoInscripcionTesis1,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de inscripción a tesis 1 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaInscripcionT1().getTime() - (10 * undia)), mensajeAsociadoInscripcionTesis1Opcional,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea para informar a los estudiantes de maestria que no pertenecen a MISIS que si opcionalmente desean inscribir tesis1, tienen 10 dias para hacerlo");
            }
        }
        if (semestre.getMaxFechaSubirNotaTesis1().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime() - undia), mensajeAsociadoSubirNotasTesis1,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para subir nota de tesis 1 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime() - (2 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime() - (2 * undia)), mensajeAsociadoSubirNotasTesis1,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para subir nota de tesis 1 ha cambiado y se enviara correo faltando dos dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime() - (3 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime() - (3 * undia)), mensajeAsociadoSubirNotasTesis1,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para subir nota de tesis 1 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            }
        }
        if (semestre.getMaxFechaPedirPendienteTesis1().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaPedirPendienteTesis1().getTime() - undia), mensajeAsociadoColocarPendienteNotasTesis1,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar pendiente especial de tesis 1 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getMaxFechaPedirPendienteTesis1().getTime() - (2 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaPedirPendienteTesis1().getTime() - (2 * undia)), mensajeAsociadoColocarPendienteNotasTesis1,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar pendiente especial de tesis 1 ha cambiado y se enviara correo faltando dos dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getMaxFechaPedirPendienteTesis1().getTime() - (3 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaPedirPendienteTesis1().getTime() - (3 * undia)), mensajeAsociadoColocarPendienteNotasTesis1,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar pendiente especial de tesis 1 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            }
        }
        if (semestre.getMaxFechaLevantarPendienteTesis1().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaLevantarPendienteTesis1().getTime() - undia), mensajeAsociadoQuitarPendienteNotasTesis1,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para levantar pendiente especial de tesis 1 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getMaxFechaLevantarPendienteTesis1().getTime() - (2 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaLevantarPendienteTesis1().getTime() - (2 * undia)), mensajeAsociadoQuitarPendienteNotasTesis1,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para levantar pendiente especial de tesis 1 ha cambiado y se enviara correo faltando dos dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getMaxFechaLevantarPendienteTesis1().getTime() - (3 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaLevantarPendienteTesis1().getTime() - (3 * undia)), mensajeAsociadoQuitarPendienteNotasTesis1,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para levantar pendiente especial de tesis 1 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            }
        }
        //   crear timmers tesis2 ...-----------------------------------------------------------------------------------------------------------------------------------------------------------------
        if (semestre.getFechaUltimaSolicitarTesis2().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSolicitarTesis2().getTime() - undia), msjUltimaFechaSolicitarInscT2,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar inscripción de tesis 2 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getFechaUltimaSolicitarTesis2().getTime() - (2 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSolicitarTesis2().getTime() - (2 * undia)), msjUltimaFechaSolicitarInscT2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar inscripción de tesis 2 ha cambiado y se enviara correo faltando dos dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaUltimaSolicitarTesis2().getTime() - (3 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSolicitarTesis2().getTime() - (3 * undia)), msjUltimaFechaSolicitarInscT2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar inscripción de tesis 2 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            }
        }
        if (semestre.getFechaUltimaReportarNotaReprobadaSSTesis2().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaReportarNotaReprobadaSSTesis2().getTime() - undia), msjUltimaFechaPerderTesis2SinSustentacion,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para reportar nota reprobada de tesis 2 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getFechaUltimaReportarNotaReprobadaSSTesis2().getTime() - (2 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaReportarNotaReprobadaSSTesis2().getTime() - (2 * undia)), msjUltimaFechaPerderTesis2SinSustentacion,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para reportar nota reprobada de tesis 2 ha cambiado y se enviara correo faltando dos dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaUltimaReportarNotaReprobadaSSTesis2().getTime() - (3 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaReportarNotaReprobadaSSTesis2().getTime() - (3 * undia)), msjUltimaFechaPerderTesis2SinSustentacion,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para reportar nota reprobada de tesis 2 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            }
        }
        if (semestre.getFechaUltimaPendienteEspecialTesis2().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaPendienteEspecialTesis2().getTime() - undia), msjUltimaFechaPendienteEspecialT2,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar pendiente especial de tesis 2 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getFechaUltimaPendienteEspecialTesis2().getTime() - (2 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaPendienteEspecialTesis2().getTime() - (2 * undia)), msjUltimaFechaPendienteEspecialT2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar pendiente especial de tesis 2 ha cambiado y se enviara correo faltando dos dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaUltimaPendienteEspecialTesis2().getTime() - (3 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaPendienteEspecialTesis2().getTime() - (3 * undia)), msjUltimaFechaPendienteEspecialT2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar pendiente especial de tesis 2 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            }
        }
        //tDO

        if (semestre.getFechaUltimaSustentarTesis2().after(hoy)) {
//             if (new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (undia * 30)).after(hoy))
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (undia * 30)), msjUltimaFechaSustentacionT2,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación para tesis 2 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (undia * 3)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (undia * 3)), msjUltimaFechaSustentacionT2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación para tesis 2 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (4 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (4 * undia)), msjUltimaFechaSustentacionT2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación para tesis 2 ha cambiado y se enviara correo faltando cuatro dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (5 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (5 * undia)), msjUltimaFechaSustentacionT2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación para tesis 2 ha cambiado y se enviara correo faltando cinco dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (15 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (15 * undia)), msjUltimaFechaSustentacionT2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación para tesis 2 ha cambiado y se enviara correo faltando cinco quince para que esa fecha máxima llegue");
            }
        }
        if (semestre.getMaxFechaInscripcionSubarea().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaInscripcionSubarea().getTime() - (undia * 3)), msjUltimaFechaInscripcionSubarea,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de inscripción a subárea ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getMaxFechaInscripcionSubarea().getTime() - (undia * 3)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaInscripcionSubarea().getTime() - (4 * undia)), msjUltimaFechaInscripcionSubarea,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de inscripción a subárea ha cambiado y se enviara correo faltando cuatro dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getMaxFechaInscripcionSubarea().getTime() - (undia * 5)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaInscripcionSubarea().getTime() - (5 * undia)), msjUltimaFechaInscripcionSubarea,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de inscripción a subárea ha cambiado y se enviara correo faltando cinco dias para que esa fecha máxima llegue");
            }
        }
        if (semestre.getMaxFechaAprobacionInscripcionSubarea().after(hoy)) {
            // crear timers de comportamiento como: rechazar tesis que queden en pendiente 2 dias despues de la fecha maxima.
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionInscripcionSubarea().getTime()), comportamientoAsociadoInscripcionSubareaEnvioCorreoReganho,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de aprobación a subárea por parte del asesor ha cambiado");
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionInscripcionSubarea().getTime() + undia * 14), comportamientoAsociadoInscripcionSubarea,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de aprobación a subárea por parte del asesor ha cambiado");

        }
        //TIMER para pasar a rechazado inscripcion a subarea por que el coordinador de subarea no acepto.
        if (semestre.getMaxFechaAprobacionInscripcionSubareaCoordinacion().after(hoy)) {
            // crear timers de comportamiento como: rechazar tesis que queden en pendiente 2 dias despues de la fecha maxima.
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionInscripcionSubareaCoordinacion().getTime()), comportamientoAsociadoInscripcionSubareaCoordinacionCorreoReganho,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de aprobación a subárea por parte de coordinación ha cambiado");
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp((undia * 14) + semestre.getMaxFechaAprobacionInscripcionSubareaCoordinacion().getTime()), comportamientoAsociadoInscripcionSubareaCoordinacion,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de aprobación a subárea por parte de coordinación ha cambiado");
        }
        if (semestre.getMaxFechaAprobacionTesis1().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionTesis1().getTime()), comportamientoAsociadoInscripcionTesis1ReanhoAsesor,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para la aprobación de la inscripción de tesis 1 por parte del asesor ha cambiado");

            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp((undia * 10) + semestre.getMaxFechaAprobacionTesis1().getTime()), comportamientoAsociadoInscripcionTesis1,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para la aprobación de la inscripción de tesis 1 por parte del asesor ha cambiado");
        }
        if (semestre.getMaxFechaAprobacionTesis1Coordinacion().after(hoy)) {

            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionTesis1Coordinacion().getTime()), comportamientoAsociadoInscripcionTesis1CoordinacionReganho,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para la aprobación de la inscripción de tesis 1 por parte del coordinador ha cambiado");

            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp((undia * 10) + semestre.getMaxFechaAprobacionTesis1Coordinacion().getTime()), comportamientoAsociadoInscripcionTesis1Coordinacion,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para la aprobación de la inscripción de tesis 1 por parte del coordinador ha cambiado");
        }
        if (new Timestamp((undia * 15) + semestre.getMaxFechaSubirNotaTesis1().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp((undia * 15) + semestre.getMaxFechaSubirNotaTesis1().getTime()), comportamientoAsociadoSubirNotasTesis1,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para subir nota de tesis 1 ha cambiado");

        }
        if (semestre.getMaxFechaSubirNotaTesis1().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime() - (undia * 7)), comportamientoCrearTareaSubirNotasTesis1,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para subir nota de tesis 1 ha cambiado");
        }

        if (new Timestamp((undia * 15) + semestre.getMaxFechaLevantarPendienteTesis1().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp((undia * 15) + semestre.getMaxFechaLevantarPendienteTesis1().getTime()), comportamientoAsociadoQuitarPendienteNotasTesis1,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para levantar pendiente especial de tesis 1 ha cambiado");
        }
        if (semestre.getMaxFechaLevantarPendienteTesis1().after(hoy)) {

            System.out.println("CREO TIMER PRA NOTAS TESIS 1 PENDIENTE ESPECIAL");
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaLevantarPendienteTesis1().getTime() - (undia * 7)), comportamientoCrearTareaSubirNotasTesis1Pendiente,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para levantar pendiente especial de tesis 1 ha cambiado");
        }
        if (semestre.getMaxFechaAprobacionTesis2().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionTesis2().getTime()), comportamientoUltimaFechaSolicitarInscT2REganhoAsesor,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de aprobación para tesis 2 ha cambiado");
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp((undia * 10) + semestre.getMaxFechaAprobacionTesis2().getTime()), comportamientoUltimaFechaSolicitarInscT2,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de aprobación para tesis 2 ha cambiado");
        }
        if (semestre.getFechaUltimaSustentarTesis2().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp((undia * 10) + semestre.getFechaUltimaSustentarTesis2().getTime()), comportamientoUltimaFechaSustentacionT2,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación para tesis 2 ha cambiado");
        }
        if (semestre.getFechaMaximaPublicacionTemasTesis().after(hoy)) {
            if (new Timestamp(semestre.getFechaMaximaPublicacionTemasTesis().getTime() - (undia * 3)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaPublicacionTemasTesis().getTime() - (undia * 3)), msjUltimoPlazoSubirPropuestasTesis,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para publicar los temas de tesis 1 ha cambiado y se enviara correo faltando tres dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaMaximaPublicacionTemasTesis().getTime() - (undia * 2)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaPublicacionTemasTesis().getTime() - (undia * 2)), msjUltimoPlazoSubirPropuestasTesis,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para publicar los temas de tesis 1 ha cambiado y se enviara correo faltando dos dias para que esa fecha máxima llegue");
            }

            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaPublicacionTemasTesis().getTime() - (undia * 1)), msjUltimoPlazoSubirPropuestasTesis,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para publicar los temas de tesis 1 ha cambiado y se enviara correo faltando un dia para que esa fecha máxima llegue");
        }
        if (semestre.getFechaDel30Porciento().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaDel30Porciento().getTime() - (undia * 7)), msjPlazoComentarios30Porciento,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para realizar informe del 30% ha cambiado y los comportamientos asociados de ejecutan faltando 7 dias para que esta fecha máxima llegue");
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaDel30Porciento().getTime() + (undia)), msjPlazoComentarios30Porciento,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para realizar informe del 30% ha cambiado y los comportamientos asociados de ejecutan faltando 1 dia para que esta fecha máxima llegue");

        }//msjVencimientoFechaRetiros

        if (semestre.getFechaMaximaRetiro().after(hoy)) {
            if (new Timestamp(semestre.getFechaMaximaRetiro().getTime() - (undia * 7)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaRetiro().getTime() - (undia * 7)), msjVencimientoFechaRetiros,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para retirar tesis 1 y tesis 2 en registro e informar por sisinfo ha cambiado y los comportamientos asociados se ejecutan faltando 7 dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaMaximaRetiro().getTime() - (undia * 5)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaRetiro().getTime() - (undia * 5)), msjVencimientoFechaRetiros,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para retirar tesis 1 y tesis 2 en registro e informar por sisinfo ha cambiado y los comportamientos asociados se ejecutan faltando 5 dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaMaximaRetiro().getTime() - (undia * 3)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaRetiro().getTime() - (undia * 3)), msjVencimientoFechaRetiros,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para retirar tesis 1 y tesis 2 en registro e informar por sisinfo ha cambiado y los comportamientos asociados se ejecutan faltando 3 dias para que esa fecha máxima llegue");
            }
            //if (new Timestamp(semestre.getFechaMaximaRetiro().getTime() - (undia * 1)).after(hoy))
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaRetiro().getTime() - (undia * 1)), msjVencimientoFechaRetiros,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para retirar tesis 1 y tesis 2 en registro e informar por sisinfo ha cambiado y los comportamientos asociados se ejecutan faltando 1 dia para que esa fecha máxima llegue");
        }

        /*
         * COMPORTAMIENTO SUSTENTACION TESIS 2 PENDIENTE ESPECIAL
         */

        if (semestre.getFechaMaxSustentacionT2PendEspecial().after(hoy)) {
//             if (new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() - (undia * 30)).after(hoy))
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (undia * 30)), comportamientoEnvioCorreosSustentacionesPendienteEpecialTesis2,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación con pendiente especial para tesis 2 ha cambiado y se enviara correo faltando 1 dia para que esa fecha máxima llegue");
            if (new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (undia * 3)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (undia * 3)), comportamientoEnvioCorreosSustentacionesPendienteEpecialTesis2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación con pendiente especial para tesis 2 ha cambiado y se enviara correo faltando 3 dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (4 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (4 * undia)), comportamientoEnvioCorreosSustentacionesPendienteEpecialTesis2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación con pendiente especial para tesis 2 ha cambiado y se enviara correo faltando 4 dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (5 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (5 * undia)), comportamientoEnvioCorreosSustentacionesPendienteEpecialTesis2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación con pendiente especial para tesis 2 ha cambiado y se enviara correo faltando 5 dias para que esa fecha máxima llegue");
            }
            if (new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (15 * undia)).after(hoy)) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaxSustentacionT2PendEspecial().getTime() - (15 * undia)), comportamientoEnvioCorreosSustentacionesPendienteEpecialTesis2,
                        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación con pendiente especial para tesis 2 ha cambiado y se enviara correo faltando 15 dias para que esa fecha máxima llegue");
            }

        }
        if (semestre.getFechaMaximaSolicitarHorario().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaSolicitarHorario().getTime()), comportamientoUltimaFechaSolicitarHorario,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para solicitar el horario de sustentacion  y los comportamientos asociados de ejecutan la fecha deleccionada");

        }
        /*
         *
         */

        Collection<TareaMultiple> listaTareas = tareaMultipleFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR));
        for (TareaMultiple tareaMultiple : listaTareas) {
            tareaMultiple.setFechaCaducacion(semestre.getMaxFechaAprobacionInscripcionSubarea());
            tareaMultipleFacade.edit(tareaMultiple);
        }

        //Aprobación de inscripción a subarea por parte del director
        listaTareas = tareaMultipleFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR));
        for (TareaMultiple tareaMultiple : listaTareas) {
            tareaMultiple.setFechaCaducacion(new Timestamp(semestre.getMaxFechaAprobacionInscripcionSubareaCoordinacion().getTime()));
            tareaMultipleFacade.edit(tareaMultiple);
        }
        //Aprobación de inscripción a tesis 1 por parte del asesor
        listaTareas = tareaMultipleFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_ASESOR));
        for (TareaMultiple tareaMultiple : listaTareas) {
            tareaMultiple.setFechaCaducacion(semestre.getMaxFechaAprobacionTesis1());
            tareaMultipleFacade.edit(tareaMultiple);
        }

        //Aprobación de inscripción a tesis 1 por parte del coordinador
        listaTareas = tareaMultipleFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_COORDINADOR_MAESTRIA));
        for (TareaMultiple tareaMultiple : listaTareas) {
            tareaMultiple.setFechaCaducacion(semestre.getMaxFechaAprobacionTesis1Coordinacion());
            tareaMultipleFacade.edit(tareaMultiple);
        }
        //Aprobación de inscripción a tesis 2 por parte del asesor
        listaTareas = tareaMultipleFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR));
        for (TareaMultiple tareaMultiple : listaTareas) {
            tareaMultiple.setFechaCaducacion(semestre.getMaxFechaAprobacionTesis2());
            tareaMultipleFacade.edit(tareaMultiple);
        }
        listaTareas = tareaMultipleFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_SUSTENTACION_TESIS2));
        for (TareaMultiple tareaMultiple : listaTareas) {
            tareaMultiple.setFechaCaducacion(semestre.getFechaMaximaCalificarTesis2());
            tareaMultipleFacade.edit(tareaMultiple);
        }

        listaTareas = tareaMultipleFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARTICULO_TESIS_2));
        for (TareaMultiple tareaMultiple : listaTareas) {
            tareaMultiple.setFechaCaducacion(semestre.getFechaUltimaSustentarTesis2());
            tareaMultipleFacade.edit(tareaMultiple);
        }



        //HISTORICOS DE TESIS 1 Y TESIS 2

        if (new Timestamp(semestre.getMaxFechaAprobacionTesis1().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionTesis1().getTime() + (undia * 15)), mensajeAsociadoMigrarTesis1Retirados,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para aprobacion de inscripción de tesis 1 ha cambiado");
        }

        if (new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime() + (undia * 30 * 1)), mensajeAsociadoMigrarTesis1Perdidas,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para subir nota de tesis 1 ha cambiado");
        }

        if (new Timestamp(semestre.getFechaMaximaRetiro().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaMaximaRetiro().getTime() + (undia * 30)), mensajeAsociadoMigrarTesis2Retirados,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima para retirar tesis 2 en registro e informar por sisinfo ha cambiado");
        }

        if (semestre.getFechaUltimaSustentarTesis2().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() + ((undia * 30 * 1) + (undia * 2))), mensajeAsociadoMigrarTesis2Perdidas,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación para tesis 2 ha cambiado");
        }

        if (semestre.getFechaUltimaSustentarTesis2().after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaSustentarTesis2().getTime() + (undia * 30 * 8)), mensajeAsociadoMigrarTesis2Terminados,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea porque la fecha máxima de sustentación para tesis 2 ha cambiado");
        }


    }

    private void enviarCorreoInscripcionSubarea(PeriodoTesis find) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        Collection<Estudiante> estudiantesMagister = darEstudiantesPendientesPorTesis();
        for (Estudiante estudiante : estudiantesMagister) {
            String asunto = Notificaciones.ASUNTO_ULTIMA_FECHA_INSCRIPCION_SUBAREA_ESTUDIANTE;
            String mensaje = Notificaciones.MENSAJE_ULTIMA_FECHA_INSCRIPCION_SUBAREA_ESTUDIANTE;
            mensaje = mensaje.replaceFirst("%", estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos());
            mensaje = mensaje.replaceFirst("%", find.getPeriodo());
            mensaje = mensaje.replaceFirst("%", sdfHMS.format(find.getMaxFechaInscripcionSubarea()));
            correoBean.enviarMail(estudiante.getPersona().getCorreo(), asunto, null, null, null, mensaje);
        }
    }

    private void generarCorreoVencimientoInscripcionTesis1FechaAsesor(PeriodoTesis find) {
        // if(find.get)

        // SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        HashMap<String, Persona> invitados = new HashMap<String, Persona>();
        Collection<Tesis1> tesisPendientes = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
        for (Tesis1 tesis1 : tesisPendientes) {
            Persona p = tesis1.getAsesor().getPersona();
            invitados.put(p.getCorreo(), p);
        } //--------------------------------------
        Collection<Persona> profs = invitados.values();
        for (Persona persona : profs) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ASESOR;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ASESOR;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", find.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(find.getMaxFechaInscripcionT1()));
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
        //---------------------------------------
        HashMap<String, Persona> noInvitados = new HashMap<String, Persona>();
        Collection<Tesis2> tesisNOPendientes = tesis2Facade.findAll();//getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
        for (Tesis2 tesis2 : tesisNOPendientes) {
            Persona p = tesis2.getEstudiante().getPersona();
            noInvitados.put(p.getCorreo(), p);
        }

        NivelFormacion nf = nivelFormacionFacade.findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_MAESTRIA));
        Collection<Estudiante> estudiantesMagister = estudianteFacadeRemote.findByTipo(nf.getNombre());
        for (Estudiante estudiante : estudiantesMagister) {
            if ((!noInvitados.containsKey(estudiante.getPersona().getCorreo())) && (estudiante.getPrograma().getCodigo().equals("ISIS"))) {
                String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ESTUDIANTE;
                String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ESTUDIANTE;
                mensajeCreacion = mensajeCreacion.replaceFirst("%", estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos());
                mensajeCreacion = mensajeCreacion.replaceFirst("%", find.getPeriodo());
                mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(find.getMaxFechaInscripcionT1()));
                correoBean.enviarMail(estudiante.getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }
        }
    }

    private void enviarCorreoUltimodiaNotasTesis1(PeriodoTesis find) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));

        HashMap<String, Persona> invitados = new HashMap<String, Persona>();
        Collection<Tesis1> tesisPendientes = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
        for (Tesis1 tesis1 : tesisPendientes) {
            if (tesis1.getSemestreIniciacion().getId() == find.getId()) {
                Persona p = tesis1.getAsesor().getPersona();
                invitados.put(p.getCorreo(), p);
                Persona pp = tesis1.getEstudiante().getPersona();
                invitados.put(pp.getCorreo(), pp);
            }
        }
        Collection<Persona> profs = invitados.values();
        for (Persona persona : profs) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", find.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(find.getMaxFechaSubirNotaTesis1()));
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }

    }

    private void enviarCorreoUltimoDiaPendientesTesis1(PeriodoTesis find) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        HashMap<String, Persona> profesores = new HashMap<String, Persona>();
        HashMap<String, Persona> estudiantes = new HashMap<String, Persona>();
        Collection<Tesis1> tesisPendientes = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));

        for (Tesis1 tesis1 : tesisPendientes) {
            if (tesis1.getSemestreIniciacion().getId() == find.getId()) {
                Persona p = tesis1.getAsesor().getPersona();
                profesores.put(p.getCorreo(), p);
                Persona pp = tesis1.getEstudiante().getPersona();
                estudiantes.put(pp.getCorreo(), pp);


            }
        }
        Collection<Persona> profs = profesores.values();
        for (Persona persona : profs) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_SOLICITAR_PENDIENTE_TESIS_1;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_SOLICITAR_PENDIENTE_TESIS_1_ASESOR;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", find.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(find.getMaxFechaPedirPendienteTesis1()));
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }

        Collection<Persona> ests = estudiantes.values();
        for (Persona persona : ests) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_SOLICITAR_PENDIENTE_TESIS_1;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_SOLICITAR_PENDIENTE_TESIS_1_ESTUDIANTE;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", find.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(find.getMaxFechaPedirPendienteTesis1()));
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
    }

    private void enviarCorreoUltimoDiaLevantarPendienteTesis1(PeriodoTesis find) {
        // SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        HashMap<String, Persona> invitados = new HashMap<String, Persona>();
        HashMap<String, Persona> estudiantes = new HashMap<String, Persona>();
        Collection<Tesis1> tesisPendientes = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE));
        for (Tesis1 tesis1 : tesisPendientes) {
            if (tesis1.getSemestreIniciacion().getId() == find.getId()) {
                Persona p = tesis1.getAsesor().getPersona();
                Persona est = tesis1.getEstudiante().getPersona();
                invitados.put(p.getCorreo(), p);
                estudiantes.put(est.getCorreo(), est);
            }
        }
        Collection<Persona> profs = invitados.values();
        for (Persona persona : profs) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_LEVANTAR_PENDIENTE_TESIS_1_ASESOR;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_LEVANTAR_PENDIENTE_TESIS_1_ASESOR;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", find.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(find.getMaxFechaLevantarPendienteTesis1()));
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
        Collection<Persona> estuds = invitados.values();
        for (Persona persona : estuds) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_LEVANTAR_PENDIENTE_TESIS_1_ESTUDIANTE;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_LEVANTAR_PENDIENTE_TESIS_1_ESTUDIANTE;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", find.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(find.getMaxFechaLevantarPendienteTesis1()));
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
    }

    public String darConfiguracionPeriodoTesisPorNombre(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secNombrePEriodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO));
            PeriodoTesis elSemestre = conversor.pasarSecuenciaAPeriodo(secNombrePEriodo);
            Secuencia secConfigPeriodo = pasarPeriodoConfiguracionASecuencia(elSemestre);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secConfigPeriodo);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CONFIGURACION_PERIODO_TESIS_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CONFIGURACION_PERIODO_TESIS_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void generarCorreoUltimaFechaInscripcionTesis2(PeriodoTesis periodo) {

        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        Collection<Tesis1> tesises1 = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS1_TERMINADA));
        tesises1.addAll(tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE)));

        HashMap<String, Persona> invitados = new HashMap<String, Persona>();
        //meter las personas que tengan tesis1 acabada o en curso y no tengan tesis2
        for (Tesis1 tesis : tesises1) {
            if (!tesis.getSemestreIniciacion().getId().equals(periodo.getId())) {
                Persona estud = tesis.getEstudiante().getPersona();
                //si tiene tesis en 2 en caulquier estado no lo mete
                Collection<Tesis2> tesisDeEstudiatne = tesis2Facade.findByCorreoEstudiante(estud.getCorreo());
                if (tesisDeEstudiatne == null || tesisDeEstudiatne.isEmpty()) {
                    invitados.put(estud.getCorreo(), estud);
                }
            }
        }
        HashMap<String, Persona> invitadosAsesores = new HashMap<String, Persona>();
        Collection<Tesis2> tesisess = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
        //meter las tesis de este periodo que no hayan sido aprobadas aun por el asesor
        for (Tesis2 tesis2 : tesisess) {
            if (tesis2.getSemestreInicio().getId().equals(periodo.getId())) {
                Persona p = tesis2.getAsesor().getPersona();
                Persona estud = tesis2.getEstudiante().getPersona();
                invitadosAsesores.put(p.getCorreo(), p);
                invitados.put(estud.getCorreo(), estud);
            }
        }
        Collection<Persona> aseosres = invitadosAsesores.values();
        for (Persona persona : aseosres) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_2_ASESOR;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_2_ASESOR;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", periodo.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(periodo.getFechaUltimaSolicitarTesis2()));
            //bau.out.println("MEnsaje creacion enviar mail=" + mensajeCreacion);
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }

        Collection<Persona> estuds = invitados.values();
        for (Persona persona : estuds) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_2_ESTUDIANTE;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_2_ESTUDIANTE;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", periodo.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(periodo.getFechaUltimaSolicitarTesis2()));
            //bau.out.println("MEnsaje creacion enviar mail=" + mensajeCreacion);
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
        //a quienes: personas que ya hayan terminado tesis1 y no tengan una tesis 2...
        //asesores que no han aprobado alguna tesis
        //estudaintes de esos asesores
    }

    private void generarCorreoUltimaFechaReprobarTesis2SinSustentacion(PeriodoTesis periodo) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        HashMap<String, Persona> invitados = new HashMap<String, Persona>();
        Collection<Tesis2> tesisess = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
        //meter los asesores de tessis en curso de este semsstre
        for (Tesis2 tesis2 : tesisess) {
            if (periodo.getId().equals(tesis2.getSemestreInicio().getId())) {
                Persona p = tesis2.getAsesor().getPersona();
                invitados.put(p.getCorreo(), p);
            }
        }
        //asesores con tesis2  en curso
        Collection<Persona> pers = invitados.values();
        for (Persona persona : pers) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_REPROBAR_ESTUDIANTE_SIN_SUSTENTACION;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_REPROBAR_ESTUDIANTE_SIN_SUSTENTACION;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", periodo.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(periodo.getFechaUltimaReportarNotaReprobadaSSTesis2()));
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
    }

    private void generarCorreoUltimaFechaPendienteEspecialTesis2(PeriodoTesis periodo) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        HashMap<String, Persona> invitados = new HashMap<String, Persona>();
        Collection<Tesis2> tesisess = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
        //meter los asesores de tessis en curso de este semsstre
        for (Tesis2 tesis2 : tesisess) {
            if (periodo.getId().equals(tesis2.getSemestreInicio().getId())) {
                Persona p = tesis2.getAsesor().getPersona();
                invitados.put(p.getCorreo(), p);
            }
        }
        //asesores con tesis2  en curso
        Collection<Persona> pers = invitados.values();
        for (Persona persona : pers) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_PARA_PEDIR_PENDIENE_ESPECIAL_TESIS_2;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_PARA_PEDIR_PENDIENE_ESPECIAL_TESIS_2;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", periodo.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(periodo.getFechaUltimaPendienteEspecialTesis2()));
            //bau.out.println("MEnsaje creacion enviar mail=" + mensajeCreacion);
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
        //asesores con tesis2  en curso
    }

    private void generarCorreoUltimaFechaSustentacionTesis2(PeriodoTesis periodo) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        Collection<Persona> estudiantes = new ArrayList<Persona>();
        Collection<Persona> asesores = new ArrayList<Persona>();

        Collection<Tesis2> tesisess = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
        //meter los asesores de tessis en curso de este semsstre
        for (Tesis2 tesis2 : tesisess) {
            if (periodo.getId().equals(tesis2.getSemestreInicio().getId())) {
                if ((tesis2.getEstaEnPendienteEspecial() == null || !tesis2.getEstaEnPendienteEspecial())
                        && (tesis2.getEstadoHorario() == null || tesis2.getEstadoHorario().equals(getConstanteBean().getConstante(Constantes.CTE_HORARIO_PENDIENTE)))) {
                    Persona asesor = tesis2.getAsesor().getPersona();
                    asesores.add(asesor);
                    Persona estudiante = tesis2.getEstudiante().getPersona();
                    estudiantes.add(estudiante);
                    tesis2Bean.avisarAEstudianteDeSeleccionarHorarioSustentacionTesis2YCrearTarea(tesis2, false);
                }
            }
        }

        //se eliminan los asesores repetidos
        Set<Persona> set = new HashSet<Persona>();
        set.addAll(asesores);
        asesores.clear();
        asesores.addAll(set);
        //enviar correo asesores
        for (Persona asesor : asesores) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_PARA_SUSTENTAR_TESIS_2;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_PARA_SUSTENTAR_TESIS_2;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", asesor.getNombres() + " " + asesor.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", periodo.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(periodo.getFechaUltimaSustentarTesis2()));
            correoBean.enviarMail(asesor.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
    }

    private void crearTareaTesis2SubirArticuloFin(Tesis2 find) {


        if (find.getRutaArticuloTesis() == null) {
            String nombreV = "Subir articulo final Tesis 2";// + carga.getId();
            //System.out.println("3");
            String descripcion = "Se debe subir el archivo de finalizacion del estudiante: " + find.getEstudiante().getPersona().getNombres() + " " + find.getEstudiante().getPersona().getApellidos()
                    + " (" + find.getEstudiante().getPersona().getCorreo() + ")";
            SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
            String asunto = Notificaciones.ASUNTO_FINAL_TESIS_2;
            Persona persona = find.getAsesor().getPersona();
            Persona estudiante = find.getEstudiante().getPersona();

            String mensajeHeader = String.format(Notificaciones.MENSAJE_HEADER_ASUNTO_FINAL_TESIS_2, persona.getNombres() + " " + persona.getApellidos());
            String mensajeBullet = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO, estudiante.getNombres() + " " + estudiante.getApellidos(), estudiante.getCorreo());
            String mensajeFooter = Notificaciones.MENSAJE_FOOTER_FINAL_TESIS_2;

            String mensaje = mensajeHeader + mensajeBullet + mensajeFooter;
            correoBean.enviarMail(persona.getCorreo(), asunto, find.getEstudiante().getPersona().getCorreo(), null, null, mensaje);
            //----------------
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARTICULO_TESIS_2);
            Date fechaInicioDate = new Date();
            Date fechaFinDate = new Date(find.getSemestreInicio().getFechaUltimaSustentarTesis2().getTime() + 1000L * 60L * 60L * 24L * 3);
            String cmd = getConstanteBean().getConstante(Constantes.TAREA_SUBIR_ARTICULO_TESIS2);//TODO:cambiar el comando segun sea
            HashMap<String, String> parametros = new HashMap<String, String>();
            parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), find.getId().toString());
            Timestamp fFin = new Timestamp(fechaFinDate.getTime());
            String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);

            tareaBean.realizarTareaPorCorreo(tipo, persona.getCorreo(), parametros);
            if (find.getRutaArticuloTesis() == null || find.getRutaArticuloTesis().isEmpty()) {
                tareaBean.crearTareaPersona(mensajeBullet, tipo, persona.getCorreo(), false, mensajeHeader, mensajeFooter, new Timestamp(fechaInicioDate.getTime()), fFin, cmd, rol, parametros, asunto);
            }
        }
    }

    /*
     * aca nuevo requerimientos: revisar las fechas...
     *                          1.un asesor crear/editar un tema de tesis/eliminar-si no hay tesis con ese tema.
     *                          2.consultar una lista de asesores y sus temas de tesis
     *                          3.modificar el metodo de crear tesis1, para que incluya
     */
    public String agregarTemaTesis(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTemaTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));

            TemaTesis1 tema = pasarSecuenciaATemaTesis1(secTemaTesis);
            PeriodoTesis per = tema.getPeriodo();
            if (per == null) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00024, new ArrayList());
            }
            if (per.getFechaMaximaPublicacionTemasTesis() != null && per.getFechaMaximaPublicacionTemasTesis().before(new Date())) {
                //Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                //return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
                String comando = getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1);
                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_AGREGAR_TEMA_TESIS);
                String login = tema.getAsesor().getPersona().getCorreo();
                String infoAdicional = "Se agrego el tema de tesis: " + tema.getNombreTema() + " para el periodo " + tema.getPeriodo().getPeriodo();
                reportarEntregaTardia(per.getFechaMaximaPublicacionTemasTesis(), comando, accion, login, infoAdicional);
            }
            if (tema.getId() != null) {
                temaTesisFacade.edit(tema);
            } else {
                Profesor p = tema.getAsesor();
                if (p.getNivelPlanta() == null || p.getNivelPlanta().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_INSTRUCTOR))) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00013, new ArrayList());
                } else {
                    temaTesisFacade.create(tema);
                }
            }
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darTemasDeTesis(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secPeriodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
            if (secPeriodo == null) {
                throw new Exception("xml mal formado en TESISBEAN metodo: darTemasDeTesis");
            }
            Collection<TemaTesis1> lista = temaTesisFacade.findByPeriodoTesis(secPeriodo.getValor());
            //se podria agrupar por profesor
            Secuencia secTemasTesis = pasarTemasTesisASecuencia(lista);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secTemasTesis);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darTemasDeTesisPorAsesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreoAsesor != null) {
                Collection<TemaTesis1> lista = temaTesisFacade.findByCorreoAsesor(secCorreoAsesor.getValor());

                Secuencia secTemasTesis = pasarTemasTesisASecuencia(lista);
                ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                param.add(secTemasTesis);
                return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_1_POR_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            }
            throw new Exception("xml mal formado en darTemasDeTesisPorAsesor");
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_1_POR_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarTemaTesisAsesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdTemaTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secIdTemaTesis != null) {
                TemaTesis1 lista = temaTesisFacade.find(Long.parseLong(secIdTemaTesis.getValor().trim()));

                Collection<Tesis1> tesisEnConflicto = tesis1Facade.findByTemaTesis(lista.getNombreTema());
                if (!tesisEnConflicto.isEmpty()) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TEMA_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00026, new ArrayList());
                }
                temaTesisFacade.remove(lista);
                ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TEMA_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            }
            throw new Exception("xml mal formado en darTemasDeTesisPorAsesor");
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TEMA_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void eliminarTemasDeTesis1() {
        Collection<TemaTesis1> lista = temaTesisFacade.findAll();
        for (TemaTesis1 temaTesis1 : lista) {
            temaTesisFacade.remove(temaTesis1);
        }
    }

    private Secuencia pasarTemasTesisASecuencia(Collection<TemaTesis1> lista) {
        Secuencia secTemasTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMAS_TESIS), null);
        for (TemaTesis1 temaTesis1 : lista) {
            Secuencia secTemaTesis = pasarTemaTesisASecuencia(temaTesis1);
            secTemasTesis.agregarSecuencia(secTemaTesis);
        }
        return secTemasTesis;
    }

    private Secuencia pasarTemaTesisASecuencia(TemaTesis1 tema) {
        Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), null);
        if (tema.getId() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tema.getId().toString()));
        }
        if (tema.getNombreTema() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), tema.getNombreTema()));
        }
        if (tema.getDescripcion() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), tema.getDescripcion()));
        }
        if (tema.getAreasInteres() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREA), tema.getAreasInteres()));
        }
        if (tema.getNumeroEstudiantes() != null) {
            secTemaTesis.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CANDIDATOS), tema.getNumeroEstudiantes().toString()));
        }
        if (tema.getPeriodo() != null) {
            Secuencia periodo = conversor.pasarPeriodoASecuencia(tema.getPeriodo());
            secTemaTesis.agregarSecuencia(periodo);
        }
        if (tema.getAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
            secAsesor.agregarSecuencia(conversor.pasarProfesorASecuencia(tema.getAsesor()));
            secTemaTesis.agregarSecuencia(secAsesor);
        }
        if (tema.getSubarea() != null) {
            secTemaTesis.agregarSecuencia(conversor.pasarSubareaInvestigacionASecuencia(tema.getSubarea()));
        }
        if (tema.getGrupoInvestigacion() != null) {
            Secuencia secGrupoIn = pasarGrupoInvestigacionASecuencia(tema.getGrupoInvestigacion());
            secTemaTesis.agregarSecuencia(secGrupoIn);
        }
        return secTemaTesis;


    }

    private TemaTesis1 pasarSecuenciaATemaTesis1(Secuencia secTemaTesis) {
        TemaTesis1 tema = new TemaTesis1();
        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null) {
            Long id = Long.parseLong(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim());
            tema.setId(id);
        }
        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null) {
            tema.setNombreTema(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor());

        }
        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)) != null) {
            tema.setDescripcion(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor());

        }
        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREA)) != null) {
            tema.setAreasInteres(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREA)).getValor());

        }
        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CANDIDATOS)) != null) {
            tema.setNumeroEstudiantes(Integer.parseInt(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CANDIDATOS)).getValor()));

        }
        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE)) != null) {

            PeriodoTesis periodo = conversor.pasarSecuenciaAPeriodo(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE)));
            tema.setPeriodo(periodo);

        }


        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS)) != null) {
            Profesor periodo = conversor.pasarSecuenciaAProfesor(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS)));
            tema.setAsesor(periodo);
        }
        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION)) != null) {

            SubareaInvestigacion periodo = conversor.pasarSecuenciaASubareaInvestigacion(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION)));
            tema.setSubarea(periodo);
        }

        if (secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO)) != null) {

            GrupoInvestigacion periodo = pasarSecuenciaAGrupoInvestigacion(secTemaTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO)));
            tema.setGrupoInvestigacion(periodo);
        }
        return tema;
    }

    private Secuencia pasarGrupoInvestigacionASecuencia(GrupoInvestigacion grupoInvestigacion) {
        Secuencia secGrupo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO), null);
        if (grupoInvestigacion.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), grupoInvestigacion.getId().toString());
            secGrupo.agregarSecuencia(secId);
        }
        if (grupoInvestigacion.getNombre() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), grupoInvestigacion.getNombre());
            secGrupo.agregarSecuencia(secId);
        }
        return secGrupo;

    }

    private GrupoInvestigacion pasarSecuenciaAGrupoInvestigacion(Secuencia obtenerPrimeraSecuencia) {
        Secuencia secId = obtenerPrimeraSecuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            return grupoInvestigacionFacade.find(Long.parseLong(secId.getValor().trim()));
        } else {
            GrupoInvestigacion g = new GrupoInvestigacion();
            Secuencia secNombre = obtenerPrimeraSecuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            if (secNombre != null) {
                g.setNombre(g.getNombre());
            }
            return g;
        }
    }

    private void enviarCorreosPropuestasTesis(PeriodoTesis find) {
        Collection<Profesor> profesores = profesorFacade.findByNivelPlanta(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TITULAR));
        profesores.addAll(profesorFacade.findByNivelPlanta(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ASOCIADO)));
        profesores.addAll(profesorFacade.findByNivelPlanta(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ASISTENTE)));
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd");
        for (Profesor profesor : profesores) {
            Persona persona = profesor.getPersona();
            Collection<TemaTesis1> temas = temaTesisFacade.findByCorreoAsesor(persona.getCorreo());
            if (temas == null || temas.isEmpty()) {
                String asunto = Notificaciones.ASUNTO_SUBIR_PROPUESTAS_TESIS;
                String mensaje = Notificaciones.MENSAJE_SUBIR_PROPUESTAS_TESIS;
                mensaje = mensaje.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
                mensaje = mensaje.replaceFirst("%", find.getPeriodo());
                mensaje = mensaje.replaceFirst("%", sdfHMS.format(find.getFechaMaximaPublicacionTemasTesis()));
                correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
            }
        }
    }

    private void crearTareasAAsesoresTesisSobreComentariosTesis30Porciento(PeriodoTesis find) {
        //crear tareas asesores tesis registrar comentario tesis...
        //para tesis 1:
        if (find.getFechaDel30Porciento().before(new Date())) {
            enviarCorreoIncumplidos30Porciento(find);
        }

        Collection<Tesis1> tesises1 = tesis1Facade.findByPeriodoTesis(find.getPeriodo());
        for (Tesis1 tesis1 : tesises1) {

            if (tesis1.getComentariosAsesor() == null || tesis1.getComentariosAsesor().isEmpty()) {
                crearTarea30PorcientoAsesores(tesis1);
            }
        }
        //para tesis 2:

        Collection<Tesis2> tesises2 = tesis2Facade.findByPeriodo(find.getPeriodo());
        for (Tesis2 tesis2 : tesises2) {
            if (tesis2.getComentariosAsesor() == null || tesis2.getComentariosAsesor().isEmpty()) {
                crearTarea30PorcientoAsesoresTesis2(tesis2);
            }
        }
    }

    public String darTemadeTesisPorId(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secID = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secID != null) {
                TemaTesis1 tema = temaTesisFacade.find(Long.parseLong(secID.getValor()));
                Secuencia secTEma = pasarTemaTesisASecuencia(tema);
                ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                param.add(secTEma);
                return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMA_TESIS_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                throw new Exception("error de xml en TesisBean- darTemadeTesisPorId");
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMA_TESIS_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void enviarCorreosDeFechaMaximaRetirosTesis(PeriodoTesis per) {

        Collection<Tesis1> tesis1Estud = tesis1Facade.findByPeriodoTesis(per.getPeriodo());
        Collection<Persona> personas = new ArrayList<Persona>();
        for (Tesis1 tesis1 : tesis1Estud) {
            if (tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO))) {
                personas.add(tesis1.getEstudiante().getPersona());
            }
        }
        Collection<Tesis2> tesis2 = tesis2Facade.findByPeriodo(per.getPeriodo());
        for (Tesis2 tesis21 : tesis2) {
            if (tesis21.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO))) {
                personas.add(tesis21.getEstudiante().getPersona());
            }
        }
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd");
        for (Persona persona : personas) {
            String asunto = Notificaciones.ASUNTO_FECHA_RETIRO_TESIS;
            String mensaje = Notificaciones.MENSAJE_FECHA_RETIRO_TESIS;
            mensaje = mensaje.replace("%1", persona.getNombres() + " " + persona.getApellidos());
            mensaje = mensaje.replace("%2", sdfHMS.format(per.getFechaMaximaRetiro()));
            correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);

        }
    }

    private void enviarCorreoIncumplidos30Porciento(PeriodoTesis find) {
        //Tesis 1:
        String msjsT1 = "Tesis 1: <br/>";
        Collection<Tesis1> tesises1 = tesis1Facade.findBySincomentariosTesis(find.getPeriodo());
        if (tesises1.isEmpty()) {
            msjsT1 = msjsT1 + " <br/> No hay notas Pendientes <br/><br/>  ";
        } else {
            msjsT1 = "<UL> ";
            String asesor = "";
            for (Tesis1 tesis1 : tesises1) {
                if (tesis1.getSemestreIniciacion().getPeriodo().equals(find.getPeriodo())) {
                    if (!asesor.equals(tesis1.getAsesor().getPersona().getNombres() + tesis1.getAsesor().getPersona().getApellidos())) {
                        msjsT1 += "<LI> "
                                + tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos() + "<br/> ";
                    } else {
                        String linea = " </UL> <br/> <b> Asesor: " + tesis1.getAsesor().getPersona().getNombres() + tesis1.getAsesor().getPersona().getApellidos() + ":  <br/>  </b>  Estudiantes:  <br/> <UL>"
                                + "<LI> " + tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos() + "<br/> ";
                        msjsT1 += linea;
                    }
                }
            }
            msjsT1 = msjsT1 + " </UL>";
        }

        System.out.println("IMPRIMIO T1=" + msjsT1);

        //Tesis 2:
        String msjsT2 = "Tesis 2: <br/> <br/>";
        Collection<Tesis2> tesises2 = tesis2Facade.findBySincomentariosTesis(find.getPeriodo());
        if (tesises2.isEmpty()) {
            msjsT2 = msjsT2 + "No hay notas Pendientes";
        } else {
            msjsT2 = msjsT2 + "<UL> ";
            String asesor = "";
            for (Tesis2 tesis1 : tesises2) {
                if (tesis1.getSemestreInicio().getPeriodo().equals(find.getPeriodo())) {
                    if (!asesor.equals(tesis1.getAsesor().getPersona().getNombres() + tesis1.getAsesor().getPersona().getApellidos())) {
                        msjsT2 = msjsT2 + "<LI> "
                                + tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos() + "<br/> ";
                    } else {
                        String linea = " </UL> <br/> <b> Asesor: " + tesis1.getAsesor().getPersona().getNombres() + tesis1.getAsesor().getPersona().getApellidos() + ":   Estudiantes: </b> <br/> <UL>"
                                + "<LI> " + tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos() + "<br/> ";
                        msjsT2 = msjsT2 + linea;
                    }
                }
            }
            msjsT2 = msjsT2 + " </UL>";
        }

        Persona p = personaFacade.findByCorreo(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA));

        String asunto = Notificaciones.ASUNTO_FECHA_30_PORCIENTO_PERSONAS_PENDIENTES_POR_NOTA;
        String mensaje = Notificaciones.MENSAJE_FECHA_30_PORCIENTO_PERSONAS_PENDIENTES_POR_NOTA;
        mensaje = mensaje.replace("%1", p.getNombres() + " " + p.getApellidos());
        mensaje = mensaje.replace("%2", (msjsT1 + msjsT2));
        mensaje = mensaje.replace("%3", find.getPeriodo());
        System.out.println("MENSAJE=====================================> \n " + (msjsT1 + msjsT2));
        correoBean.enviarMail(p.getCorreo(), asunto, null, null, null, mensaje);
    }

    private void enviarCorreoCoordinacionPersonasPendientesPorAprobarYFechaTerminadaTesis1(PeriodoTesis periodo) {

        Collection<Tesis1> tesises = tesis1Facade.findByPeriodoYEstado(periodo.getPeriodo(), getConstanteBean().getConstante(Constantes.CTE_TESIS_POR_APROBAR_COORDINACION_MAESTRIA));
        if (!tesises.isEmpty()) {
            String estudiantes = "";
            for (Tesis1 tesis1 : tesises) {
                estudiantes = estudiantes + tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos() + " <br/>";
            }
            //aca enviar correo a coordinaciojn los estudiantes que hacen falta aprobar tesis1:
            Persona p = personaFacade.findByCorreo(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA));
            String asunto = Notificaciones.ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_COORDINACION;
            String mensaje = Notificaciones.MENSAJE_ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_COORDINACION;
            mensaje = mensaje.replace("%1", p.getNombres() + " " + p.getApellidos());
            mensaje = mensaje.replace("%2", estudiantes);
            correoBean.enviarMail(p.getCorreo(), asunto, null, null, null, mensaje);
        }
    }

    /*
     * metodo que envia un correo de alerta a los aseosres que todavia tienen personas por aprobar o rechazar
     * ademas coloca el periodo en curso
     */
    private void enviarCorreosAsesoresConInscripcionesATesis1PorAprobarYFechaVencida(PeriodoTesis periodo) {
        // cambiarEstadoPeriodoAActual(periodo);
        Collection<Persona> profesores = tesis1Facade.findAsesoresByPeriodoEstado(periodo.getPeriodo(), getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
        if (!profesores.isEmpty()) {
            String interiorMensajeCoordinacion = "";
            for (Persona persona : profesores) {

                Collection<Tesis1> tesisPendientesProfesor = tesis1Facade.findByPeriodoEstadoAsesor(periodo.getPeriodo(), getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR), persona.getCorreo());
                if (!tesisPendientesProfesor.isEmpty()) {
                    interiorMensajeCoordinacion = interiorMensajeCoordinacion + persona.getNombres() + " " + persona.getApellidos() + " <br/>";
                    String estudiantesPensientes = "";
                    for (Tesis1 tesis1 : tesisPendientesProfesor) {
                        estudiantesPensientes += tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos() + " <br/>";
                    }
                    //envio correo asesor con las personas que le quedaron penidentes
                    String asunto = Notificaciones.ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_ASESOR_A_ASESOR;
                    String mensaje = Notificaciones.MENSAJE_ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_ASESOR_A_ASESOR;
                    mensaje = mensaje.replace("%1", persona.getNombres() + " " + persona.getApellidos());
                    mensaje = mensaje.replace("%2", estudiantesPensientes);
                    correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
                }
            }
            //envio correo coordinacion con asesores con estudiantes pendientes por aprobar
            Persona p = personaFacade.findByCorreo(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA));
            String asunto = Notificaciones.ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_ASESOR_A_COORDINACION;
            String mensaje = Notificaciones.MENSAJE_ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_1_POR_ASESOR_A_COORDINACION;
            mensaje = mensaje.replace("%1", p.getNombres() + " " + p.getApellidos());
            mensaje = mensaje.replace("%2", interiorMensajeCoordinacion);
            correoBean.enviarMail(p.getCorreo(), asunto, null, null, null, mensaje);
        }
    }

    private void enviarCorreosCoordinadoresSubareaConInscripcionesASubareaPorAprobarYFechaVencida(PeriodoTesis periodo) {
        Collection<Persona> coordinadoresSubarea = inscrippcionsubFacadeLocal.findByCoordinadoresSubAreaConEstado(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_COORDINADOR_SUB_AREA));

        for (Persona persona : coordinadoresSubarea) {
            String interiorMsj = "";
            List<InscripcionSubareaInvestigacion> inscripciones = inscrippcionsubFacadeLocal.findByCorreoCoordinador(persona.getCorreo());
            for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : inscripciones) {
                if (inscripcionSubareaInvestigacion.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_COORDINADOR_SUB_AREA))) {
                    Persona e = inscripcionSubareaInvestigacion.getEstudiante().getPersona();
                    interiorMsj += e.getNombres() + " " + e.getApellidos() + " <br/>";
                }
            }
            if (!interiorMsj.equals("")) {
                String asunto = Notificaciones.ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_POR_COORDINADOR_SUBAREA;
                String mensaje = Notificaciones.MENSAJE_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_POR_COORDINADOR_SUBAREA;
                mensaje = mensaje.replace("%1", persona.getNombres() + " " + persona.getApellidos());
                mensaje = mensaje.replace("%2", interiorMsj);
                correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
            }
        }
    }

    private void enviarCorreosAsesoresConInscripcionesASubareaPorAprobarYFechaVencida(PeriodoTesis periodo) {
        Collection<Persona> coordinadoresSubarea = inscrippcionsubFacadeLocal.findByAsesoresSubAreaConEstado(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));

        for (Persona persona : coordinadoresSubarea) {
            String interiorMsj = "";
            List<InscripcionSubareaInvestigacion> inscripciones = inscrippcionsubFacadeLocal.findByCorreoCoordinador(persona.getCorreo());
            for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : inscripciones) {
                if (inscripcionSubareaInvestigacion.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR))) {
                    Persona e = inscripcionSubareaInvestigacion.getEstudiante().getPersona();
                    interiorMsj += e.getNombres() + " " + e.getApellidos() + " <br/>";
                }
            }
            if (!interiorMsj.equals("")) {
                String asunto = Notificaciones.ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_POR_ASESOR;
                String mensaje = Notificaciones.MENSAJE_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_POR_ASESOR;
                mensaje = mensaje.replace("%1", persona.getNombres() + " " + persona.getApellidos());
                mensaje = mensaje.replace("%2", interiorMsj);
                correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
            }
        }
    }

    private void enviarCorreoAsesoresTesis2FechaAprobacionVencidaYGentePorAprobar(PeriodoTesis periodo) {
        //CTE_INSCRIPCION_POR_APROBAR_ASESOR
        Collection<Persona> asesoresSinAprobar = tesis2Facade.findAsesoresByEstadoYPeriodo(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR), periodo.getPeriodo());
        for (Persona persona : asesoresSinAprobar) {
            Collection<Tesis2> tesiesA = tesis2Facade.findByCorreoAsesor(persona.getCorreo());
            String msj = "";
            for (Tesis2 tesis2 : tesiesA) {
                if (tesis2.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR))) {
                    Persona e = tesis2.getEstudiante().getPersona();
                    msj += e.getNombres() + " " + e.getApellidos() + " <br/>";
                }
            }
            if (!msj.equals("")) {
                String asunto = Notificaciones.ASUNTO_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_2_POR_ASESOR;
                String mensaje = Notificaciones.MENSAJE_PERSONAS_PENDIENTES_POR_APROBAR_INSCRIPCION_TESIS_2_POR_ASESOR;
                mensaje = mensaje.replace("%1", persona.getNombres() + " " + persona.getApellidos());
                mensaje = mensaje.replace("%2", msj);
                correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
            }
        }
    }

    @Deprecated
    public void generarCorreoSustentacionTesis(Long idTesis2) {
        return;
    }

    private void rechazoAutomaticoInscripcionesSubareaPorFechaAsesor(PeriodoTesis per) {
        List<InscripcionSubareaInvestigacion> inscripciones = inscrippcionsubFacadeLocal.findByPeriodoTesis1(per.getPeriodo());
        for (InscripcionSubareaInvestigacion inscrip : inscripciones) {
            if (inscrip.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR))) {
                inscrip.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.FALSE));
                inscrip.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_TIMEOUT_DE_APROBACION));
                //enviar correo al estudiante, de rechazo por timeout de la aprobación
                //Crear correo para informar que se rechazo la inscripción por timeout de la aprobación

                //quitar tarea
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscrip.getId().toString());
                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR), propiedades);

                String asunto = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_SUBAREA_INVESTIGACION;
                String mensaje = Notificaciones.MENSAJE_NOTIFICAR_RECHAZO_AUTOMATICO_INSCRIPCION_SUBAREA_INVESTIGACION;

                mensaje = mensaje.replaceFirst("%", inscrip.getEstudiante().getPersona().getNombres() + " " + inscrip.getEstudiante().getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%", inscrip.getSubareaInvestigacion().getNombreSubarea());
                inscrippcionsubFacadeLocal.edit(inscrip);
                correoBean.enviarMail(inscrip.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);
            }
        }
    }

    private void rechazoAutomaticoInscripcionesSubareaCoordinacion(PeriodoTesis per) {
        List<InscripcionSubareaInvestigacion> inscripciones = inscrippcionsubFacadeLocal.findByPeriodoTesis1(per.getPeriodo());
        for (InscripcionSubareaInvestigacion inscrip : inscripciones) {
            if (inscrip.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_COORDINADOR_SUB_AREA))) {
                inscrip.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.FALSE));
                inscrip.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_TIMEOUT_DE_APROBACION));
                //enviar correo al estudiante, de rechazo por timeout de la aprobación
                //Crear correo para informar que se rechazo la inscripción por timeout de la aprobación

                //quitar tarea
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscrip.getId().toString());


                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR), propiedades);

                //------------------
                String asunto = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_SUBAREA_INVESTIGACION;
                String mensaje = Notificaciones.MENSAJE_NOTIFICAR_RECHAZO_AUTOMATICO_INSCRIPCION_SUBAREA_INVESTIGACION;

                mensaje = mensaje.replaceFirst("%", inscrip.getEstudiante().getPersona().getNombres() + " " + inscrip.getEstudiante().getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%", inscrip.getSubareaInvestigacion().getNombreSubarea());
                inscrippcionsubFacadeLocal.edit(inscrip);
                correoBean.enviarMail(inscrip.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);
            }
        }
    }

    private void rechazoAutomaticoInscripcionTesis1Asesor(PeriodoTesis per) {
        Collection<Tesis1> tesises = tesis1Facade.findByPeriodoTesis(per.getPeriodo());
        for (Tesis1 tesis1 : tesises) {
            if (tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR))) {
                tesis1.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_RECHAZADA));
                tesis1Facade.edit(tesis1);

                //quitar tarea asesosres
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis1.getId().toString());
                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_ASESOR), propiedades);

                //tesis1Bean.migrarTesisRechazada(tesis1.getId());
                //---------------------------------------------
                //enviar correo al estudiante, de rechazo por timeout de la aprobación
                //Crear correo para informar que se rechazo la inscripción por timeout de la aprobación
                String asunto = Notificaciones.ASUNTO_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_1;
                String mensaje = Notificaciones.MENSAJE_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_1;
                mensaje = mensaje.replaceFirst("%", tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(tesis1.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);
            }
        }
    }

    private void rechazoAutomaticoInscripcionesTesis1Coordinacion(PeriodoTesis per) {

        Collection<Tesis1> tesises = tesis1Facade.findByPeriodoTesis(per.getPeriodo());
        for (Tesis1 tesis1 : tesises) {
            if (tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_POR_APROBAR_COORDINACION_MAESTRIA))) {
                tesis1.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_RECHAZADA));
                tesis1Facade.edit(tesis1);
                //------------------------------quitar tareaas a caoordinacion
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis1.getId().toString());
                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_COORDINADOR_MAESTRIA), propiedades);

                //-------------------------------------------------------------------
                //enviar correo al estudiante, de rechazo por timeout de la aprobación
                //Crear correo para informar que se rechazo la inscripción por timeout de la aprobación
                String asunto = Notificaciones.ASUNTO_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_1;
                String mensaje = Notificaciones.MENSAJE_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_1;

                mensaje = mensaje.replaceFirst("%", tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(tesis1.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);
            }
        }
        //eliminarTemasDeTesis1();
    }

    private void calificacionAutomaticaTesis1Perdida(PeriodoTesis per) {
        Collection<Tesis1> tesises = tesis1Facade.findByPeriodoTesis(per.getPeriodo());
        for (Tesis1 tesis1 : tesises) {
            if (tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO))) {
                tesis1.setCalificacionTesis(getConstanteBean().getConstante(Constantes.CTE_NOTA_REPROBADA));
                tesis1.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
                tesis1Facade.edit(tesis1);
                //--------
                //  quitar tarea a asesors
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1);
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis1.getId().toString());
                completarTareaSencilla(tipo, propiedades);

                //----------------------------------------------------------------------------
                String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_TESIS_AUTOMATICA;
                String mensajeCreacion = Notificaciones.MENSAJE_CALIFICACION_TESIS_AUTOMATICA;
                mensajeCreacion = mensajeCreacion.replaceFirst("%1", tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos());
                mensajeCreacion = mensajeCreacion.replaceFirst("%2", tesis1.getAsesor().getPersona().getNombres() + " " + tesis1.getAsesor().getPersona().getApellidos());
                correoBean.enviarMail(tesis1.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }
        }
    }

    private void calificacionAutomaticaTesis1Pendiente(PeriodoTesis per) {

        Collection<Tesis1> tesises = tesis1Facade.findByPeriodoTesis(per.getPeriodo());
        for (Tesis1 tesis1 : tesises) {
            if (tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE))) {
                tesis1.setCalificacionTesis(getConstanteBean().getConstante(Constantes.CTE_NOTA_REPROBADA));
                tesis1.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
                tesis1Facade.edit(tesis1);
                // aka quitar tarea al asesor...-------------------------
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1_PENDIENTE);
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis1.getId().toString());
                completarTareaSencilla(tipo, propiedades);
                //------------------------------------------------------
                String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_TESIS_AUTOMATICA;
                String mensajeCreacion = Notificaciones.MENSAJE_CALIFICACION_TESIS_AUTOMATICA;
                mensajeCreacion = mensajeCreacion.replaceFirst("%1", tesis1.getEstudiante().getPersona().getNombres() + " " + tesis1.getEstudiante().getPersona().getApellidos());
                mensajeCreacion = mensajeCreacion.replaceFirst("%2", tesis1.getAsesor().getPersona().getNombres() + " " + tesis1.getAsesor().getPersona().getApellidos());
                correoBean.enviarMail(tesis1.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }
        }
    }

    private void rechazoAutomaticoTesis2Asesor(PeriodoTesis per) {
        Collection<Tesis2> tesises = tesis2Facade.findByPeriodo(per.getPeriodo());
        String correoCordinacion = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        for (Tesis2 tesis2 : tesises) {

            if (tesis2.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR))) {
                tesis2.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_RECHAZADA));
                tesis2Facade.edit(tesis2);
                // quitar tareaa a asesores-----------------------------------
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR), propiedades);
                //-----------------------------------------------------------
                String asunto = Notificaciones.ASUNTO_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_2;
                String mensaje = Notificaciones.MENSAJE_RECHAZO_AUTOMATICO_INSCRIPCION_TESIS_2;

                mensaje = mensaje.replaceFirst("%", tesis2.getEstudiante().getPersona().getNombres() + " " + tesis2.getEstudiante().getPersona().getApellidos());
                /*colocar copia a coordinacion y asesor
                 */
                correoBean.enviarMail(tesis2.getEstudiante().getPersona().getCorreo(), asunto, tesis2.getAsesor().getPersona().getCorreo(), correoCordinacion, null, mensaje);


            }
        }
    }

    private void calificacionAutomaticaTesis2NoSustentada(PeriodoTesis per) {
        Collection<Tesis2> tesises = tesis2Facade.findByPeriodo(per.getPeriodo());
        for (Tesis2 tesis2 : tesises) {
            if (tesis2.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO))
                    && tesis2.getEstaEnPendienteEspecial() != null && tesis2.getEstaEnPendienteEspecial() == false) {
                tesis2.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
                tesis2.setCalificacion(1.5);
                tesis2Facade.edit(tesis2);
                String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_TESIS_2_SIN_SUSTENTACION;
                String mensajeCreacion = Notificaciones.ASUNTO_CALIFICACION_TESIS_2_SIN_SUSTENTACION;
                mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis2.getEstudiante().getPersona().getNombres() + " " + tesis2.getEstudiante().getPersona().getApellidos());
                String correoCoordiancion = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
                correoBean.enviarMail(tesis2.getEstudiante().getPersona().getCorreo(), asuntoCreacion, tesis2.getAsesor().getPersona().getCorreo(), correoCoordiancion, null, mensajeCreacion);
                tesis2Bean.crearTimerDeMigracionDeTesisFinalizada(tesis2.getId());
            }
        }
    }

    private void generarCorreoUltimaFechaSustentacionTesis2PendienteEspecial(PeriodoTesis periodo) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        HashMap<String, Persona> invitados = new HashMap<String, Persona>();

        Collection<Tesis2> tesisess = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
        //meter los asesores de tessis en curso de este semsstre
        int i = 1;
        for (Tesis2 tesis2 : tesisess) {
            if (periodo.getId().equals(tesis2.getSemestreInicio().getId())) {
                if (tesis2.getEstaEnPendienteEspecial() && (tesis2.getEstadoHorario() == null || tesis2.getEstadoHorario() =="Horario Pendiente")) {
                    Persona p = tesis2.getAsesor().getPersona();
                    invitados.put(p.getCorreo(), p);
                    Persona estud = tesis2.getEstudiante().getPersona();
                    invitados.put(estud.getCorreo(), estud);
                    //System.out.println("PENDIENTE ESPECIAL SIN HORARIO SELECCIONADO ESTUDIANTE: " + (i) + ". " + estud.getNombres() + " " + estud.getApellidos() + "\n");
                    //System.out.println("PENDIENTE ESPECIAL SIN HORARIO SELECCIONADO ASESOR    : " + (i++) + ". " + p.getNombres() + " " + p.getApellidos() + "\n");
                    tesis2Bean.avisarAEstudianteDeSeleccionarHorarioSustentacionTesis2PendienteEspecialYCrearTarea(tesis2, false);
                }
            }
        }
        //asesores con tesis2  en curso
        Collection<Persona> pers = invitados.values();
        for (Persona persona : pers) {
            String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_PARA_SUSTENTAR_TESIS_2_PE;
            String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_PARA_SUSTENTAR_TESIS_2_PE;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", periodo.getPeriodo());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(periodo.getFechaUltimaPendienteEspecialTesis2()));
            //bau.out.println("MEnsaje creacion enviar mail=" + mensajeCreacion);
            correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
        }
        //asesores con tesis2  en curso
        //estud
    }

    /**
     * metodo que cambia el periodo actual al que llega como parametro y se encarga de dejar el resto en no actual
     * @param periodo
     */
    private void cambiarEstadoPeriodoAActual(PeriodoTesis periodo) {

        Collection<PeriodoTesis> periodos = periodoFacadelocal.findAll();
        for (PeriodoTesis periodoTesis : periodos) {
            if (periodoTesis.getId().equals(periodo.getId())) {
                periodoTesis.setActual(true);
                periodoFacadelocal.edit(periodoTesis);
            } else {
                if (periodoTesis.isActual()) {
                    periodoTesis.setActual(true);
                    periodoFacadelocal.edit(periodoTesis);
                }
            }
        }
    }

    private void reportarEntregaTardia(Date fechaLimite, String comando, String accion, String login, String infoAdicional) {
        Timestamp actual = new Timestamp(System.currentTimeMillis());
        Timestamp acordada = new Timestamp(fechaLimite.getTime());
        String proceso = getConstanteBean().getConstante(Constantes.VAL_PROCESO_TESIS);
        String modulo = getConstanteBean().getConstante(Constantes.VAL_MODULO_TESIS);
        accionVencidaBean.guardarAccionVencida(acordada, actual, accion, login, proceso, modulo, comando, infoAdicional);
    }

    /*
     * Metodos para las nuevas tareaas:...
     */
    private void completarTareaSencilla(String tipo, Properties propiedades) {

        TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, propiedades));
        if (tarea != null) {
            tareaSencillaBean.realizarTareaPorId(tarea.getId());
        }
    }

    private void completarListaTareas(String tipo) {
        Collection<TareaSencilla> listaTareas = tareaSencillaFacade.findByTipo(tipo);
        for (TareaSencilla t : listaTareas) {
            Collection<Parametro> params = t.getParametros();
            Properties parametros = new Properties();
            for (Parametro parametro : params) {
                parametros.put(parametro.getCampo(), parametro.getValor());
            }
            completarTareaSencilla(tipo, parametros);

        }
    }

    private void crearTarea30PorcientoAsesores(Tesis1 tesis1) {
        String nombreV = "Ingresar Comentario Tesis 1";// + carga.getId();
        String descripcion = "Se debe entregar una apreciación cualitativa de los estudiantes:";


        String asunto = Notificaciones.ASUNTO_COMENTARIOS_TESIS1;
        Persona persona = tesis1.getAsesor().getPersona();
        Persona estudiante = tesis1.getEstudiante().getPersona();

        String mensajeHeader = String.format(Notificaciones.MENSAJE_HEADER_COMENTARIOS_TESIS, persona.getNombres() + " " + persona.getApellidos());
        String mensajeBullet = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO, estudiante.getNombres() + " " + estudiante.getApellidos(), estudiante.getCorreo());
        String mensajeFooter = Notificaciones.MENSAJE_FOOTER_COMENTARIOS_TESIS;
        //----------------
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_1);
        Date fechaInicioDate = new Date();
        Date fechaFinDate = new Date(tesis1.getSemestreIniciacion().getFechaDel30Porciento().getTime() + 1000L * 60L * 60L * 24L * 3);
        String cmd = getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1_PARA_COMENTARIO);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis1.getId()));

        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        boolean esAgrupable = true;
        tareaBean.crearTareaPersona(mensajeBullet, tipo, persona.getCorreo(), esAgrupable, mensajeHeader, mensajeFooter, new Timestamp(fechaInicioDate.getTime()), fFin, cmd, rol, parametros, asunto);
    }

    private void crearTarea30PorcientoAsesoresTesis2(Tesis2 tesis1) {
        String nombreV = "Ingresar Comentario Tesis 2";// + carga.getId();
        String descripcion = "Se debe entregar una apreciación cualitativa de los estudiantes:";

        String asunto = Notificaciones.ASUNTO_COMENTARIOS_TESIS2;
        Persona persona = tesis1.getAsesor().getPersona();
        Persona estudiante = tesis1.getEstudiante().getPersona();

        String mensajeHeader = String.format(Notificaciones.MENSAJE_HEADER_COMENTARIOS_TESIS, persona.getNombres() + " " + persona.getApellidos());
        String mensajeBullet = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO, estudiante.getNombres() + " " + estudiante.getApellidos(), estudiante.getCorreo());
        String mensajeFooter = Notificaciones.MENSAJE_FOOTER_COMENTARIOS_TESIS;


        //----------------
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_2);
        Date fechaInicioDate = new Date();
        Date fechaFinDate = new Date(tesis1.getSemestreInicio().getFechaDel30Porciento().getTime() + 1000L * 60L * 60L * 24L * 3);
        String cmd = getConstanteBean().getConstante(Constantes.CMD_DAR_TESIS2_POR_ID_PARA_30_PORCIENTO);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis1.getId()));

        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        boolean esAgrupable = true;
        tareaBean.crearTareaPersona(mensajeBullet, tipo, persona.getCorreo(), esAgrupable, mensajeHeader, mensajeFooter, new Timestamp(fechaInicioDate.getTime()), fFin, cmd, rol, parametros, asunto);


    }
//    private void cambiarFechaFinTareas(String tipo, Timestamp fFin)
//    {
//         Collection<TareaSencilla> listaTareas = tareamu
//         for (TareaSencilla tareaSencilla : listaTareas) {
//            TareaMultiple tarea;
//            tarea.
//        }
//    }

    public void crearTareasTesis() {
        crearTareasInscripcionSubarea();
        crearTareasTesis1();
        crearTareasTesisII();
    }

    private void crearTareasInscripcionSubarea() {

        Collection<InscripcionSubareaInvestigacion> subareas = inscrippcionsubFacadeLocal.findAll();
        for (InscripcionSubareaInvestigacion inscripcion : subareas) {
            if (true) {
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscripcion.getId().toString());
                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR), propiedades);
            }
            if (true) {
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscripcion.getId().toString());
                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR), propiedades);
            }
            //revisar periodo
            PeriodoTesis periodo2 = inscripcion.getSemestreInicioTesis1();
            System.out.println("inscripcion con id: " + inscripcion.getId());
            System.out.println("en crearTareasInscripcionSubarea lo que hay en getMaxFechaAprobacionInscripcionSubarea es: " + periodo2.getMaxFechaAprobacionInscripcionSubarea());
            if (periodo2.getMaxFechaAprobacionInscripcionSubarea() != null && periodo2.getMaxFechaAprobacionInscripcionSubarea().after(new Date())) {
                if (inscripcion.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR))) {
                    inscripcionesSubareaBean.crearTareaAsesorDeAprobarSubareaInscripcion(inscripcion, periodo2, Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO);
                }
            }
            if (periodo2.getMaxFechaAprobacionInscripcionSubareaCoordinacion() != null && periodo2.getMaxFechaAprobacionInscripcionSubareaCoordinacion().after(new Date())) {
                if (inscripcion.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_COORDINADOR_SUB_AREA))) {
                    inscripcionesSubareaBean.crearTareaCoordinadorSubareaAprobarInscripcionSubarea(inscripcion);
                }
            }

        }
    }

    private void crearTareasTesis1() {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_ASESOR);
        completarListaTareas(tipo);
        tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_COORDINADOR_MAESTRIA);
        completarListaTareas(tipo);
        tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_1);
        completarListaTareas(tipo);
        tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PENDIENTE_TESIS1_COORDINACION);
        completarListaTareas(tipo);
        tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1);
        completarListaTareas(tipo);
        tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1_PENDIENTE);
        completarListaTareas(tipo);
        //crear tarea aprobar por coordinacion
        Collection<Tesis1> tesises1 = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_POR_APROBAR_COORDINACION_MAESTRIA));
        for (Tesis1 tesis1 : tesises1) {
            tesis1Bean.crearTareaAprobarTesisCoordinadorMaestria(tesis1);
        }
        //crear tarea asesor para aprobar tesis
        tesises1 = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
        for (Tesis1 tesis1 : tesises1) {
            tesis1Bean.crearTareaAsesorAprobacionTesis1(tesis1);
        }

        //crear tarea coordinacion  para aprobar Pendiente tesis
        tesises1 = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE));
        for (Tesis1 tesis1 : tesises1) {
            tesis1Bean.crearTareaCoordinadorAprobarPendienteTesis1(tesis1);
        }

        //tareas por timer:
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        tesises1 = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
        for (Tesis1 tesis1 : tesises1) {
            PeriodoTesis p = tesis1.getSemestreIniciacion();
            if (true) {   //tareas 30%
                Timestamp FechaInicioTimer = new Timestamp(p.getFechaDel30Porciento().getTime() - (undia * 7));
                Date hoy = new Date();
                if (hoy.after(FechaInicioTimer) && hoy.before(p.getFechaDel30Porciento()) && (tesis1.getComentariosAsesor() == null || tesis1.getComentariosAsesor().isEmpty())) {
                    crearTarea30PorcientoAsesores(tesis1);
                }
                Timestamp FechaInicioTimerCalificarTesis = new Timestamp(p.getMaxFechaSubirNotaTesis1().getTime() - (undia * 7));
                if (hoy.after(FechaInicioTimerCalificarTesis) && hoy.before(p.getMaxFechaSubirNotaTesis1()) && tesis1.getCalificacionTesis().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE))) {
                    tesis1Bean.creatTareaTesis1CalificarATiempo(tesis1);
                }
            }
        }

        /*
         * Tareas Pendiente levantar pendiente
         */
        tesises1 = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE));
        for (Tesis1 tesis1 : tesises1) {
            PeriodoTesis p = tesis1.getSemestreIniciacion();
            Timestamp FechaInicioTimerCalificarTesisPendiente = new Timestamp(p.getMaxFechaLevantarPendienteTesis1().getTime() - (undia * 7));
            Date hoy = new Date();
            if (hoy.after(FechaInicioTimerCalificarTesisPendiente) && hoy.before(p.getMaxFechaLevantarPendienteTesis1()) && tesis1.getCalificacionTesis().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE))) {
                tesis1Bean.creatTareaCalificarTesis1Pendiente(tesis1);
            }
        }
    }

    private void crearTareasTesisII() {


        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR));
        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_2));
        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2));
        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR));
        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2));
        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARTICULO_TESIS_2));
        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_SUSTENTACION_TESIS2));

        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PENDIENTE_ESPECIAL_TESIS2_COORDINACION));
        completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_TESIS_2_COORDINACION));


        //Tareas iniciales de aprobar tesis asesor
        Collection<Tesis2> tesises2 = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
        for (Tesis2 tesis : tesises2) {
            Collection<Tesis1> t = tesis1Facade.findByCorreoEstudiante(tesis.getEstudiante().getPersona().getCorreo());
            boolean vioTesis1SemestreAnterior = vioTesisSemestreanterior(t, tesis);
            tesis2Bean.crearTareaAsesorAprobarTesis2(tesis, vioTesis1SemestreAnterior);
        }

        //
        tesises2 = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        for (Tesis2 tesis : tesises2) {
            PeriodoTesis p = tesis.getSemestreInicio();
            //tareas 30%, ignoarar el if(true) es solo para mejorar identacion
            if (true) {
                Timestamp FechaInicioTimer = new Timestamp(p.getFechaDel30Porciento().getTime() - (undia * 7));
                Date hoy = new Date();
                if (hoy.after(FechaInicioTimer) && hoy.before(p.getFechaDel30Porciento()) && (tesis.getComentariosAsesor() == null || tesis.getComentariosAsesor().isEmpty())) {
                    crearTarea30PorcientoAsesoresTesis2(tesis);
                }
            }
            //tareas seleccionar horario sustentacion
            if (true) {
                Timestamp FechaInicioTimer = new Timestamp(p.getFechaUltimaSustentarTesis2().getTime() - (undia * 30));
                Date hoy = new Date();
                if (hoy.after(FechaInicioTimer) && hoy.before(p.getFechaUltimaSustentarTesis2()) && (tesis.getEstadoHorario() == null || tesis.getEstadoHorario().equals(getConstanteBean().getConstante(Constantes.CTE_HORARIO_PENDIENTE)))) {
                    tesis2Bean.avisarAEstudianteDeSeleccionarHorarioSustentacionTesis2YCrearTarea(tesis, false);
                }
            }
            //()
            if (true) {
                Timestamp FechaInicioTimer = new Timestamp(p.getFechaMaxSustentacionT2PendEspecial().getTime() - (undia * 30));
                Date hoy = new Date();
                if (hoy.after(FechaInicioTimer) && hoy.before(p.getFechaMaxSustentacionT2PendEspecial()) && (tesis.getEstadoHorario() == null || tesis.getEstadoHorario().equals(getConstanteBean().getConstante(Constantes.CTE_HORARIO_PENDIENTE)))) {
                    tesis2Bean.avisarAEstudianteDeSeleccionarHorarioSustentacionTesis2PendienteEspecialYCrearTarea(tesis, false);
                }
            }
            /*
             * Las otras tareas relacionadas con horario como es aprbar por asesor y asignar salon secretario coordinacion.
             */
            if (true) {
                if (tesis.getEstadoHorario() != null) {
                    if (tesis.getEstadoHorario().equals(getConstanteBean().getConstante(Constantes.CTE_HORARIO_SELECCIONADO))) {
                        tesis2Bean.notificarAsesorFechaHoraSustentacionTesis2(tesis);
                    }
                    if (tesis.getEstadoHorario().equals(getConstanteBean().getConstante(Constantes.CTE_HORARIO_APROBADO))
                            && tesis.getHorarioSustentacion().getSalonSustentacion() == null) {
                        tesis2Bean.avisarACoordinacionDeTareaREservarSalonSustentacionTesis2(tesis);
                    }
                }
            }
            /*
             * tarea asesor subir articulo
             */
            if (true) {
                Date hoy = new Date();
                HorarioSustentacionTesis horario = tesis.getHorarioSustentacion();
                if (horario != null) {
                    Timestamp fechaTimer2 = new Timestamp(horario.getFechaSustentacion().getTime() - 1000 * 60 * 60 * 24 * 5);
                    if (fechaTimer2.before(hoy) && tesis.getRutaArticuloTesis() == null) {
                        crearTareaTesis2SubirArticuloFin(tesis);
                    } else {
                        String linea2 = getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_ESTADO_SUSTENTACION_TESIS_2_A_ESPERAR_ARTICULO) + "-" + tesis.getId().toString();
                        timerGenerico.eliminarTimerPorParametroExterno(linea2);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, fechaTimer2, linea2,
                                "Tesis2Bean", this.getClass().getName(), "crearTimersSustentacioTesis2", "Este timer se crea porque se ha aprobado el horario de sustentación por el asesor");
                    }
                    /*
                     * tareas jurados y asesor calificar tesis 2
                     */
                    Timestamp fechaTimerCrearTareasJurados = new Timestamp(horario.getFechaSustentacion().getTime() - (1000 * 60 * 60 * 2));
                    if (fechaTimerCrearTareasJurados.before(hoy)) {
                        //si la fecha ya paso se regeneran las tareas =>
                        Collection<CalificacionJurado> calsj = tesis.getCalificacionesJurados();
                        for (CalificacionJurado c : calsj) {
                            if (c.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_ASESOR_TESIS)) && c.getTerminado() != null && c.getTerminado() == false) {
                                Profesor asesor = tesis.getAsesor();
                                tesis2Bean.crearTareaProfesorJuradocalificarTesis(tesis, c.getHash(), asesor.getPersona());
                            } else if (c.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_JURADO_TESIS_INTERNO)) && c.getTerminado() != null && c.getTerminado() == false) {
                                Profesor profesor = c.getJuradoInterno();
                                tesis2Bean.crearTareaProfesorJuradocalificarTesis(tesis, c.getHash(), profesor.getPersona());
                            } else if (c.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS2_COASESOR)) && c.getTerminado() != null && c.getTerminado() == false) {
                                if (c.getCoasesor().getInterno()) {
                                    Profesor profesor = c.getCoasesor().getCoasesor();
                                    tesis2Bean.crearTareaProfesorJuradocalificarTesis(tesis, c.getHash(), profesor.getPersona());
                                }
                            }
                        }
                    }
                }
            }




        }
        tesises2 = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_ESPERANDO_NOTA_FINAL_TESIS_2));
        tesises2.addAll(tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_ESPERANDO_ARTICULO_FINAL_TESIS_2)));
        for (Tesis2 tesis2 : tesises2) {
            /*
             * Tarea coordinacion calificar tesis
             */
            if (true) {
                if (tesisListaParaCalificacion(tesis2)) {
                    crearTareaCoordinacionSubirNotaTesis2(tesis2);
                }
            }
        }

    }

    private void crearTareaCoordinacionSubirNotaTesis2(Tesis2 tesis) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_TESIS_2_COORDINACION);
        Persona personaEstudiante = tesis.getEstudiante().getPersona();
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_NOTA_TESIS_2, personaEstudiante.getNombres() + " " + personaEstudiante.getApellidos());
        String nombreRol = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String header = Notificaciones.HEADER_SUBIR_NOTA_TESIS_2;
        String footer = Notificaciones.FOOTER_SUBIR_NOTA_TESIS_2;
        String asunto = Notificaciones.ASUNTO_SUBIR_NOTA_TESIS_2;
        String cmd = getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
        Calendar calendar = Calendar.getInstance();
        Timestamp fechaInicio = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 6);
        Timestamp fechaFin = new Timestamp(calendar.getTimeInMillis());
        tareaBean.crearTareaRol(mensaje, tipo, nombreRol, true, header, footer, fechaInicio, fechaFin, cmd, params, asunto);
    }

    private boolean tesisListaParaCalificacion(Tesis2 t) {
        Collection<CalificacionJurado> calificaiones = t.getCalificacionesJurados();
        for (CalificacionJurado calificacionJurado : calificaiones) {
            if (calificacionJurado.getRolJurado().equals(Constantes.CTE_ASESOR_TESIS) && !calificacionJurado.getTerminado()) {
                return false;
            }

            if (!calificacionJurado.getRolJurado().equals(Constantes.CTE_ASESOR_TESIS) && !calificacionJurado.getTerminado() && calificacionJurado.getCancelada() == false) {
                return false;
            }

        }
        return true;
    }

    private boolean vioTesisSemestreanterior(Collection<Tesis1> t, Tesis2 tesis2) {
        boolean vioTesis1SemestreAnterior = false;
        for (Tesis1 tesis1 : t) {
            //Revisar que haya aprobado tesis 1
            if (tesis1.getCalificacionTesis().equals(getConstanteBean().getConstante(Constantes.CTE_NOTA_APROBADA))
                    && tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS1_TERMINADA))) {
                //revisar que el periodo sea el anterior
                String periodoSiguienteATesis1 = tesis2Bean.obtenerSiguientePeriodo(tesis1.getSemestreIniciacion());
                if (periodoSiguienteATesis1.equals(tesis2.getSemestreInicio().getPeriodo())) {
                    vioTesis1SemestreAnterior = true;
                    break;
                }
            }
        }
        return vioTesis1SemestreAnterior;
    }

    public Collection<Estudiante> darEstudiantesPendientesPorTesis() {

        HashMap<String, Persona> noInvitados = new HashMap<String, Persona>();
        List<Tesis1> listaTesis1 = tesis1Facade.findAll();
        Iterator<Tesis1> iteTesis1 = listaTesis1.iterator();
        while (iteTesis1.hasNext()) {
            Persona p = iteTesis1.next().getEstudiante().getPersona();
            noInvitados.put(p.getCorreo(), p);
        }

        List<Tesis2> listaTesis2 = tesis2Facade.findAll();
        Iterator<Tesis2> iteTesis2 = listaTesis2.iterator();
        while (iteTesis2.hasNext()) {
            Persona p = iteTesis2.next().getEstudiante().getPersona();
            noInvitados.put(p.getCorreo(), p);
        }

        String programasConTesis = getConstanteBean().getConstante(Constantes.CTE_CODIGOS_MAESTRIAS_CON_TESIS);
        NivelFormacion nf = nivelFormacionFacade.findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_MAESTRIA));
        Collection<Estudiante> estudiantesMagister = estudianteFacadeRemote.findByTipo(nf.getNombre());
        Collection<Estudiante> estudiantesMagisterConTesis = new ArrayList<Estudiante>();
        for (Estudiante estudiante : estudiantesMagister) {
            if (!noInvitados.containsKey(estudiante.getPersona().getCorreo()) && programasConTesis.contains(estudiante.getPrograma().getCodigo())) {
                estudiantesMagisterConTesis.add(estudiante);
            }
        }
        return estudiantesMagisterConTesis;
    }

    public Collection<AccionBO> darAcciones(String rol, String login) {
        acciones = new ArrayList<AccionBO>();

        if (!login.contains("@")) {
            login += getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
        }

        Estudiante estudiante = estudianteFacadeRemote.findByCorreo(login);
        NivelFormacion nivelDeFormacion = estudiante.getTipoEstudiante();

        String cteNivelMaestría = getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_MAESTRIA);
        String cteNivelPregrado = getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PREGRADO);
        String cteNivelDoctorado = getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_DOCTORADO);
        String cteNivelEspecializacion = getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESPECIALIZACION);
        String cteRolEstudiante = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);


        if (rol.equals(cteRolEstudiante)) {
            if (nivelDeFormacion != null) {
                if (nivelDeFormacion.getNombre().equals(cteNivelMaestría)) {
                    agregarAccionesMaestría(login);
                } else if (!nivelDeFormacion.getNombre().equals(cteNivelDoctorado) && !nivelDeFormacion.getNombre().equals(cteNivelEspecializacion) && !nivelDeFormacion.getNombre().equals(cteNivelPregrado)) {
                    //si el nivel de formación es diferente de null y además es diferente de doctorado, especialización y pregrado. agregue las acciones por seguridad.
                    agregarAccionesMaestría(login);
                }
            } else {
                agregarAccionesMaestría(login);
            }
        }
        return acciones;
    }

    private void agregarAccionesMaestría(String login) {

        acciones.add(new AccionBO(
                Notificaciones.NOMBRE_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_SUBAREA,
                Notificaciones.DESCRIPCION_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_SUBAREA,
                getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_SUBAREA),
                getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));
/*
        acciones.add(new AccionBO(
                Notificaciones.NOMBRE_ACCION_VER_ESTADO_DE_SOLICITUD_INSCRIPCION_SUBAREA,
                Notificaciones.DESCRIPCION_ACCION_VER_ESTADO_DE_SOLICITUD_INSCRIPCION_SUBAREA,
                getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_VER_ESTADO_DE_SOLICITUD_INSCRIPCION_SUBAREA),
                getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));
*/

        acciones.add(new AccionBO(
                Notificaciones.NOMBRE_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS1,
                Notificaciones.DESCRIPCION_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS1,
                getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_TESIS1),
                getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));

        acciones.add(new AccionBO(
                Notificaciones.NOMBRE_ACCION_VER_ESTADO_SOLICITUD_TESIS1,
                Notificaciones.DESCRIPCION_ACCION_VER_ESTADO_SOLICITUD_TESIS1,
                getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_VER_ESTADO_SOLICITUD_TESIS1),
                getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));

        if (tieneTesis1(login)) {
            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS2,
                    Notificaciones.DESCRIPCION_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS2,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_ENVIAR_SOLICITUD_INSCRIPCION_TESIS2),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));

            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_VER_ESTADO_SOLICITUD_TESIS2,
                    Notificaciones.DESCRIPCION_ACCION_VER_ESTADO_SOLICITUD_TESIS2,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_VER_ESTADO_SOLICITUD_TESIS2),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));
        }

        acciones.add(new AccionBO(
                Notificaciones.NOMBRE_ACCION_CONSULTAR_TEMAS_TESIS,
                Notificaciones.DESCRIPCION_ACCION_CONSULTAR_TEMAS_TESIS,
                getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_CONSULTAR_TEMAS_TESIS),
                getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));

        acciones.add(new AccionBO(
                Notificaciones.NOMBRE_ACCION_CONSULTAR_FECHAS_DE_TESIS,
                Notificaciones.DESCRIPCION_ACCION_CONSULTAR_FECHAS_DE_TESIS,
                getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_CONSULTAR_FECHAS_TESIS),
                getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));

        if (tieneTesis1(login)) {
            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_VER_HORARIOS_DE_SUSTENTACION_TESIS2,
                    Notificaciones.DESCRIPCION_ACCION_UBICACION_HORA_TESIS2,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_VER_HORARIOS_DE_SUSTENTACION_TESIS2),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_MAESTRIA)));
        }
    }

    /*
    private void agregarAccionesPregrado() {
    acciones.add(new AccionBO(
    Notificaciones.NOMBRE_ACCION_CONSULTAR_TEMAS_TESIS_PREGRADO,
    Notificaciones.DESCRIPCION_ACCION_CONSULTAR_TEMAS_TESIS_PREGRADO,
    getConstanteBean().getConstante(Constantes.VAL_ACCION_ESTUDIANTE_CONSULTAR_TEMAS_TESIS_PREGRADO),
    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_PREGRADO)));

    acciones.add(new AccionBO(
    Notificaciones.NOMBRE_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS_PREGRADO,
    Notificaciones.DESCRIPCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS_PREGRADO,
    getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_SOLICITUD_INSCRIPCION_TESIS_PREGRADO),
    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_PREGRADO)));

    acciones.add(new AccionBO(
    Notificaciones.NOMBRE_ACCION_VER_ESTADO_SOLICITUD_INSCRIPCION_TESIS_PREGRADO,
    Notificaciones.DESCRIPCION_VER_ESTADO_SOLICITUD_INSCRIPCION_TESIS_PREGRADO,
    getConstanteBean().getConstante(Constantes.VAL_ACCION_VER_ESTADO_SOLICITUD_INSCRIPCION_TESIS_PREGRADO),
    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_PREGRADO)));
    }
     */
    //revisa si tiene alguna tesis1 asociada al estudiante
    private Boolean tieneTesis1(String login) {
        Boolean tieneTesis1 = false;
        Collection<Tesis1> tesises1 = tesis1Facade.findByCorreoEstudiante(login);
        if (tesises1 != null) {
            for (Tesis1 tesis1 : tesises1) {
                if (tesis1 != null) {
                    tieneTesis1 = true;
                }
            }
        }
        return tieneTesis1;
    }

    private void generarCorreoVencimientoInscripcionTesis1Opcional(PeriodoTesis find) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        HashMap<String, Persona> invitados = new HashMap<String, Persona>();

        //---------------------------------------
        HashMap<String, Persona> noInvitados = new HashMap<String, Persona>();
        Collection<Tesis2> tesisNOPendientes = tesis2Facade.findAll();//getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
        for (Tesis2 tesis2 : tesisNOPendientes) {
            Persona p = tesis2.getEstudiante().getPersona();
            noInvitados.put(p.getCorreo(), p);
        }

        NivelFormacion nf = nivelFormacionFacade.findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_MAESTRIA));
        Collection<Estudiante> estudiantesMagister = estudianteFacadeRemote.findByTipo(nf.getNombre());
        for (Estudiante estudiante : estudiantesMagister) {
            if ((!noInvitados.containsKey(estudiante.getPersona().getCorreo())) && (!estudiante.getPrograma().getCodigo().equals("ISIS"))) {
                String asuntoCreacion = Notificaciones.ASUNTO_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ESTUDIANTE_OPCIONAL;
                String mensajeCreacion = Notificaciones.MENSAJE_ULTIMA_FECHA_INSCRIPCION_TESIS_1_ESTUDIANTE_OPCIONAL;
                mensajeCreacion = mensajeCreacion.replaceFirst("%", estudiante.getPersona().getNombres() + " " + estudiante.getPersona().getApellidos());
                mensajeCreacion = mensajeCreacion.replaceFirst("%", find.getPeriodo());
                mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(find.getMaxFechaInscripcionT1()));
                correoBean.enviarMail(estudiante.getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }
        }

    }
}
