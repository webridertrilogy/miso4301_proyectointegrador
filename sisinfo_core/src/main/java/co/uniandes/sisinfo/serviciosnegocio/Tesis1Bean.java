/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.ComentarioTesis;
import co.uniandes.sisinfo.entities.InscripcionSubareaInvestigacion;
import co.uniandes.sisinfo.entities.PeriodoTesis;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.Tesis1;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
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
import co.uniandes.sisinfo.serviciosfuncionales.SubareaInvestigacionFacadeRemote;
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
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class Tesis1Bean implements Tesis1BeanRemote, Tesis1BeanLocal {

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
    private SubareaInvestigacionFacadeRemote subareaInvestigacionFacadeRemote;
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
    /*@EJB
    private AlertaFacadeRemote alertaFacadeRemote;*/
    @EJB
    private ComentarioTesisFacadeLocal comentarioTesisFacade;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacade;
    @EJB
    private HistoricoTesis1Remote historicoBean;
    @EJB
    private AccionVencidaBeanRemote accionVencidaBean;
    //---OTROS--------------------
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorTesisMaestria conversor;
    @EJB
    private TareaSencillaRemote tareaSencillaBean;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;

    public Tesis1Bean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            estudianteFacadeRemote = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            subareaInvestigacionFacadeRemote = (SubareaInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(SubareaInvestigacionFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            historicoTesis = (HistoricosTesisBeanRemote) serviceLocator.getRemoteEJB(HistoricosTesisBeanRemote.class);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            reporteFacadeRemote = (ReportesFacadeRemote) serviceLocator.getRemoteEJB(ReportesFacadeRemote.class);
            grupoInvestigacionFacade = (GrupoInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(GrupoInvestigacionFacadeRemote.class);
//            alertaFacadeRemote = (AlertaFacadeRemote) serviceLocator.getRemoteEJB(AlertaFacadeRemote.class);
            nivelFormacionFacade = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            historicoBean = (HistoricoTesis1Remote) serviceLocator.getRemoteEJB(HistoricoTesis1Remote.class);
            accionVencidaBean = (AccionVencidaBeanRemote) serviceLocator.getRemoteEJB(AccionVencidaBeanRemote.class);
            conversor = new ConversorTesisMaestria();
            /*
             * nuevo manejo de tareas
             */
            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);

        } catch (Exception e) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //=====================================================================================================
    //===================================  TESIS 1  =======================================================
    //=====================================================================================================
    /**
     * metodo llamado por un estudiante cuando va a agregar una solicitud de tesis 1
     * @param xml
     * @return
     */
    public String agregarSolicitudTesis1(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secPrincipal = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1));
            Tesis1 tesis = conversor.pasarSecuenciaATesis1(secPrincipal);
            //-Verificar que no exista----
            //TODO: verificar que no exista otra solicitud de tesis en el mismo periodo!!!
            Collection<Tesis1> listaOtrasTesis1 = tesis1Facade.findByCorreoEstudiante(tesis.getEstudiante().getPersona().getCorreo());
            for (Tesis1 laOtra : listaOtrasTesis1) {
                if (laOtra.getSemestreIniciacion().getPeriodo().equals(tesis.getSemestreIniciacion().getPeriodo())) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00016, new ArrayList());
                }
            }
            //--VERIFICAR REGLAS DE NEGOCIO:-------------
            InscripcionSubareaInvestigacion inscrip = inscrippcionsubFacadeLocal.findByEstadoYCorreoEstudiante(tesis.getEstudiante().getPersona().getCorreo(), getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
            if (inscrip == null) {/*error si:
                 **                      - estudiante no tiene inscripcion de subarea aprobada*/

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0004, new ArrayList());
            }
            if (!inscrip.getSubareaInvestigacion().getNombreSubarea().equals(tesis.getSubareaInvestigacion().getNombreSubarea())) {

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0005, new ArrayList());
            }
            if (!inscrip.getAsesor().getId().equals(tesis.getAsesor().getId())) {

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0006, new ArrayList());
            }
            /*
             * UPDATE  profesor SET  nivelPlanta_id =  '2' WHERE  profesor.id =profesor.id ;
             */
            if (inscrip.getAsesor().getNivelPlanta() == null || (inscrip.getAsesor().getNivelPlanta().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_INSTRUCTOR)))) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00013, new ArrayList());
            }

            //se valida que las fechas estén configuradas
            if (tesis.getSemestreIniciacion().getMaxFechaInscripcionT1() == null
                    || tesis.getSemestreIniciacion().getMaxFechaSubirNotaTesis1() == null) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00024, new ArrayList());
            }

            //la fecha máxima para poner terminación debe ser antes a la de levantar pendiente, al menos
            if (tesis.getFechaTerminacion().after(tesis.getSemestreIniciacion().getMaxFechaLevantarPendienteTesis1())) {
                //formatea la fecha para retornarla como información para el error
                SimpleDateFormat sdfFechaMax = new SimpleDateFormat("dd/MM/yyyy");
                String strMaxFecha = sdfFechaMax.format(tesis.getSemestreIniciacion().getMaxFechaLevantarPendienteTesis1());
                //construye el parámetro para el error
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), strMaxFecha);
                //agrega el parámetro a la lista
                parametros.add(secParametro);
                //retorna
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00020, parametros);
            }
            PeriodoTesis semestre = tesis.getSemestreIniciacion();
            PeriodoTesis real = periodoFacadelocal.findByPeriodo(semestre.getPeriodo());
            Date hoy = new Date();
            Timestamp hoyTime = new Timestamp(hoy.getTime());
            if (real.getMaxFechaInscripcionT1() == null) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00015, new ArrayList());
            }
            if (real.getMaxFechaInscripcionT1().before(hoyTime)) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00014, new ArrayList());
            }
            //-------------------------------------------
            //terminar de completar la tesis, poner el estado en el que se encuentra, nota en pendiente, etc.
            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_POR_APROBAR_COORDINACION_MAESTRIA));
            tesis.setCalificacionTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
            tesis.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
            tesis1Facade.create(tesis);
            crearTareaAprobarTesisCoordinadorMaestria(tesis);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * crea una solicitud de ingreso a tesis 1 con permisos de coordinacion (no revisa reglas)
     * @param xml
     * @return
     */
    public String agregarSolicitudTesis1Director(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secPrincipal = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1));
            Tesis1 tesis = conversor.pasarSecuenciaATesis1(secPrincipal);
            //-Verificar que no exista----
            //TODO: verificar que no exista otra solicitud de tesis en el mismo periodo!!!
            Collection<Tesis1> listaOtrasTesis1 = tesis1Facade.findByCorreoEstudiante(tesis.getEstudiante().getPersona().getCorreo());
            for (Tesis1 laOtra : listaOtrasTesis1) {
                if (laOtra.getSemestreIniciacion().getPeriodo().equals(tesis.getSemestreIniciacion().getPeriodo())) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0004, new ArrayList());
                }
            }
            //--VERIFICAR REGLAS DE NEGOCIO:-------------
            InscripcionSubareaInvestigacion inscrip = inscrippcionsubFacadeLocal.findByEstadoYCorreoEstudiante(tesis.getEstudiante().getPersona().getCorreo(), getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
            if (inscrip == null) {
                /*error si:
                estudiante no tiene inscripcion de subarea aprobada*/
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0004, new ArrayList());
            }
            /*verificacion misma subarea*/
//            if (!inscrip.getSubareaInvestigacion().getNombreSubarea().equals(tesis.getSubareaInvestigacion().getNombreSubarea())) {
//
//                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
//                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0005, new ArrayList());
//            }
            /*verificacion mismo asesor*/
//            if (!inscrip.getAsesor().getId().equals(tesis.getAsesor().getId())) {
//
//                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
//                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0006, new ArrayList());
//            }

            /*  if (inscrip.getAsesor().getNivelPlanta() != null && !(inscrip.getAsesor().getNivelPlanta().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_INSTRUCTOR)))) {
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00013, new ArrayList());
            }*/
            PeriodoTesis semestre = tesis.getSemestreIniciacion();
            PeriodoTesis real = periodoFacadelocal.findByPeriodo(semestre.getPeriodo());
            Date hoy = new Date();
            Timestamp hoyTime = new Timestamp(hoy.getTime());

            //terminar de completar la tesis, poner el estado en el que se encuentra, nota en pendiente, etc.
            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
            tesis.setCalificacionTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
            tesis.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.TRUE));
            //  tesis.set(getConstanteBean().getConstante(Constantes.TRUE));
            tesis1Facade.create(tesis);

            enviarCorreoCreacionTesisExtemporanea(tesis);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1_EXTEMPORANEA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo llamado por el asesor para confirmar o rechazar una asesoria de tesis
     * @param xml
     * @return
     */
    public String aprobarORechazarAsesoriaTesis(String xml) {
        try {
            /**
             * consulta:
             * <idGeneral>id de la tesis </idGeneral>
             * <aprobadoAsesor>TRUE||FALSE</aprobadoAsesor>
             * <correo>asf@uniandes.edu.co</correo> (correo del asesor == usuario)
             */
            parser.leerXML(xml);
            Secuencia secIdGeneral = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secAprobadoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR));
            Secuencia secCorreoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secIdGeneral != null && secAprobadoAsesor != null && secCorreoAsesor != null) {
                Long id = Long.parseLong(secIdGeneral.getValor().trim());
                String valor = secAprobadoAsesor.getValor().trim();
                String correo = secCorreoAsesor.getValor().trim();
                Tesis1 tesis = tesis1Facade.find(id);
                if (tesis != null) {
                    if (tesis.getAsesor().getPersona().getCorreo().equals(correo)) {
                        //aca meter logica....--------------------------------------------
                        //--------terminar tarea asesor------------------------------------------
                        Properties propiedades = new Properties();
                        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_ASESOR), propiedades);
                        //FIN--------terminar tarea ASESOR------------------------------------------
                        if (tesis.getSemestreIniciacion().getMaxFechaAprobacionTesis1().before(new Date())) {
                            String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1);
                            String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_APROBAR_INSCRIPCION_TESIS1_ASESOR);
                            String login = tesis.getAsesor().getPersona().getCorreo();
                            String infoAdicional = "La solicitud de tesis1 del estudiante " + tesis.getEstudiante().getPersona().getCorreo() + " fue " + (valor.equals(getConstanteBean().getConstante(Constantes.TRUE)) ? "aprobada" : "reprobada");
                            reportarEntregaTardia(tesis.getSemestreIniciacion().getMaxFechaAprobacionTesis1(), comando, accion, login, infoAdicional);
                        }
                        //notificar resultado
                        if (valor.equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                            tesis.setAprobadoAsesor(valor);
                            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_INSCRIPCION_BANNER));
                            tesis1Facade.edit(tesis);
                            notificarEstudianteAprobacionTesis1(tesis);
                            crearTareaPendienteIncripcionBanner(tesis);
                            //TODO Crear Tarea
                        } else if (valor.equals(getConstanteBean().getConstante(Constantes.FALSE))) {
                            if (true) {
                                //Tesis1 tesis = tesis1Facade.find(id);
                                tesis.setAprobadoAsesor(valor);
                                tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR));
                                tesis.setFechaTerminacion(new Timestamp(new Date().getTime()));
                                tesis1Facade.edit(tesis);
                            }
                            tesis = tesis1Facade.find(id);
                            migrarTesisRechazada(tesis);

                            //Crear correo para informar que se rechazo la inscripción
                            String asuntoCreacion = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_TESIS_1;
                            String mensajeCreacion = Notificaciones.MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_TESIS_1;

                            mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());

                            correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                            //FIN-------------Crear correo para informar que se rechazo la inscripción
                        }
                        //------------------------------------------------------------------
                    } else {
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0007, new ArrayList());
                    }
                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                }
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                throw new Exception("faltan parametros en metodo: aprobarORechazarAsesoriaTesis: TesisBean");
            }
            /**
             * respuesta:
             * >vacio< si ok
             * error si:
             *          -id de la tesis no existe
             *          - el asesor no es el que se envia en el correo
             */
        } catch (Exception ex) {

            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    /**
     * metodo llamado para listar las solicitudes de tesis en un periodo que esten pendientes por aprobar por coordinacion (falta verificar que hayan cumplido el requisito de ingles)
     * @param sml
     * @return
     */
    public String darTesisPorAprobarCoordinadorMaestria(String sml) {
        try {
            //se obtienen todas las inscripciones, el coordinación debe ver todas
            Collection<Tesis1> tesises = tesis1Facade.findAll();
            Secuencia secPrincipal = conversor.pasarTesises1ASecuencias(tesises);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secPrincipal);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PARA_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PARA_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Metodo llamado por el coordinador de maestria para aprobar una solicitud de tesis.
     * en caso de ser rechazada se avisara al estudiante  por correo, la solicitud se pasar a historicos
     * @param xml: estructura igual al de aprobar tesis de asesor
     * @return xml con mensaje vacio si todo esta bien o error de lo contrario.
     */
    public String aprobarTesis1CoordinacionMaestria(String xml) {
        try {
            /**
             * consulta:
             * <idGeneral>id de la tesis </idGeneral>
             * <aprobadoAsesor>TRUE||FALSE</aprobadoAsesor>
             * <correo>asf@uniandes.edu.co</correo> (correo del asesor == usuario)
             */
            parser.leerXML(xml);
            Secuencia secIdGeneral = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secAprobadoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_COORDINADOR_MAESTRIA));

            if (secIdGeneral != null && secAprobadoAsesor != null) {
                Long id = Long.parseLong(secIdGeneral.getValor().trim());
                String valor = secAprobadoAsesor.getValor().trim();

                Tesis1 tesis = tesis1Facade.find(id);
                //PeriodoTesis periodoCasual = tesis.getSemestreIniciacion();
                //if (periodoCasual.getMaxFechaAprobacionTesis1Coordinacion().before(new Date())) {
                //    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                //    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
                //}
                if (tesis != null) {
                    //--------terminar tarea coordinador subarea------------------------------------------
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_COORDINADOR_MAESTRIA), propiedades);
                    //FIN--------terminar tarea coordinador subarea------------------------------------------

                    if (tesis.getSemestreIniciacion().getMaxFechaAprobacionTesis1Coordinacion().before(new Date())) {
                        String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1_COORDINADOR);
                        String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_APROBAR_INSCRIPCION_TESIS1_COORDINACION);
                        String login = "Coordinación";
                        String infoAdicional = "La solicitud de tesis1 del estudiante " + tesis.getEstudiante().getPersona().getCorreo() + " fue " + (valor.equals(getConstanteBean().getConstante(Constantes.TRUE)) ? "aprobada" : "reprobada");
                        reportarEntregaTardia(tesis.getSemestreIniciacion().getMaxFechaAprobacionTesis1Coordinacion(), comando, accion, login, infoAdicional);
                    }
                    if (valor.equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                        tesis.setAprobadoAsesor(valor);
                        tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
                        tesis1Facade.edit(tesis);
                        crearTareaAsesorAprobacionTesis1(tesis);

                    } else if (valor.equals(getConstanteBean().getConstante(Constantes.FALSE))) {

                        Secuencia secComentarios = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_TESIS));
                        String comentarios = secComentarios != null ? secComentarios.getValor() : "N/A";

                        tesis.setAprobadoAsesor(valor);
                        tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_COORDINACION_MAESTRIA));//CTE_INSCRIPCION_RECHAZADA_POR_ASESOR
                        tesis.setFechaTerminacion(new Timestamp(new Date().getTime()));
                        tesis1Facade.edit(tesis);

                        migrarTesisRechazada(tesis);

                        //Crear correo para informar que se rechazo la inscripción--ESTUDIANTE--
                        String asuntoCreacion = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_TESIS_1;
                        String mensajeCreacion = Notificaciones.MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_TESIS_1_COORDINACION;

                        mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeCreacion = mensajeCreacion.replaceFirst("%", comentarios);
                        mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());

                        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoCreacion, tesis.getAsesor().getPersona().getCorreo(), null, null, mensajeCreacion);
                        //FIN-------------Crear correo para informar que se rechazo la inscripción
                    }
                    //------------------------------------------------------------------

                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                }
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS1_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                throw new Exception("faltan parametros en metodo: aprobarORechazarAsesoriaTesis: TesisBean");
            }
            /**
             * respuesta:
             * >vacio< si ok
             * error si:
             *          -id de la tesis no existe
             *          - el asesor no es el que se envia en el correo
             */
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
        //------------------------------------------------------------------------------------------------------------------------------------------------------
    }

    /**
     * metodo que devuelve un objeto tesis 1 dado el id
     * @param xml
     * @return
     */
    public String darEstadoSolicitudTesis1(String xml) {
        try {
            /**
             * xml consulta:
             * <vacio>
             */
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null) {
                Long id = Long.parseLong(secId.getValor().trim());
                Tesis1 tesis = tesis1Facade.find(id);
                if (tesis != null) {
                    Secuencia secTesis = conversor.pasarTesis1ASecuencia(tesis);
                    //Secuencia secTesises = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1), "");
                    // secTesises.agregarSecuencia(secTesis);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(secTesis);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            }
            throw new Exception("xml mal formado en metodo:darEstadoSolicitudTesis1-TesisBean ");
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que devuelbe las solicitudes de tesis 1 de un asesor que no hayan sido rechazadas dado un periodo
     * @param xml
     * @return
     */
    public String darSolicitudesTesis1ParaProfesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo.getValor() != null) {
                String correo = secCorreo.getValor();
                Collection<Tesis1> tesises = tesis1Facade.findByCorreoAsesor(correo);
                if (tesises != null) {
                    Secuencia secTesises = conversor.pasarTesises1ASecuencias(tesises);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(secTesises);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), ""));
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    /**
     * metodo que devuelve la solicitud de tesis de un estudiante dado su correo
     * @param xml
     * @return
     */
    public String darSolicitudTesisParaEstudiante(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo.getValor() != null) {
                String correo = secCorreo.getValor();
                Collection<Tesis1> tesises = tesis1Facade.findByCorreoEstudiante(correo);
                if (tesises != null && tesises.isEmpty() == false) {
                    Iterator<Tesis1> i = tesises.iterator();
                    Tesis1 laUktima = i.next();
                    while (i.hasNext()) {
                        Tesis1 ttemp = i.next();
                        if (ttemp.getSemestreIniciacion().getMaxFechaInscripcionT1() != null
                                && laUktima.getSemestreIniciacion().getMaxFechaInscripcionT1() != null
                                && ttemp.getSemestreIniciacion().getMaxFechaInscripcionT1().after(laUktima.getSemestreIniciacion().getMaxFechaInscripcionT1())) {
                            laUktima = ttemp;
                        } else if (ttemp.getSemestreIniciacion().getMaxFechaInscripcionT1() != null
                                && laUktima.getSemestreIniciacion().getMaxFechaInscripcionT1() == null) {
                            laUktima = ttemp;
                        }
                    }
                    Secuencia secTesis = conversor.pasarTesis1ASecuencia(laUktima);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(secTesis);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1), ""));
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo para cambiar el estado de tesis1 (solicitar pendiente)
     * @param xml
     * @return
     */
    public String cambiarEstadoTesis1(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secNotatesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1));

            Secuencia secIdTesis = secNotatesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            //verifica que se haya enviado el nombre del archivo
            if (secIdTesis != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                Tesis1 tesis = tesis1Facade.find(id);
                if (tesis != null) {
                    //valida fecha máxima para pedir pendiente de tesis 1
                    if (tesis.getSemestreIniciacion().getMaxFechaPedirPendienteTesis1().before(new Date())) {
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COLOCAR_PENDIENTE_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
                    }

                    tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE));
                    tesis1Facade.edit(tesis);

                    //: enviar correo estudiante informando solicitud de pendiente
                    String asuntoPendEstudiante = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_TESIS1_ESTUDIANTE;
                    String mensajePendEstudiante = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_TESIS1_ESTUDIANTE;
                    mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%2", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoPendEstudiante, null, null, null, mensajePendEstudiante);

                    //: enviar correo asesor informando solicitud de pendiente
                    String asuntoPendAsesor = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_TESIS1_ASESOR;
                    asuntoPendAsesor = asuntoPendAsesor.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    String mensajePendAsesor = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_TESIS1_ASESOR;
                    mensajePendAsesor = mensajePendAsesor.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoPendAsesor, null, null, null, mensajePendAsesor);

                    //crea alerta
                    crearTareaCoordinadorAprobarPendienteTesis1(tesis);
                    //retorna
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1), ""));
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COLOCAR_PENDIENTE_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
                //enviar error tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COLOCAR_PENDIENTE_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            }
            //xml mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COLOCAR_PENDIENTE_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COLOCAR_PENDIENTE_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * devuelve las soliciutes aceptadas de tesis 1
     * @param xml
     * @return
     */
    public String darSolicitudesAceptadasTesis1(String xml) {
        try {
            parser.leerXML(xml);
            Collection<Tesis1> tesises = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
            if (tesises != null) {
                Secuencia secTesises = conversor.pasarTesises1ASecuencias(tesises);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secTesises);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_APROBADAS_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), ""));
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_APROBADAS_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_APROBADAS_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo llamado para colocar nota de tesis 1
     * @param xml
     * @return
     */
    public String subirNotaTesis1(String xml) {
        try {

            /*
             * consulta:
             *          <idGeneral>id de la tesis</idGeneral>
             *          <calificacionTesis>4.5</calificacionTesis>
             *
             */
            //1. encontrar la tesis
            parser.leerXML(xml);
            Secuencia secNotatesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1));
            Secuencia secIdTesis = secNotatesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secNombreArchivo = secNotatesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA));
            if (secIdTesis != null && secNombreArchivo != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                Tesis1 tesis = tesis1Facade.find(id);
                if (tesis != null) {
                    String ruta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS1);
                    String nombreArchivo = secNombreArchivo.getValor();
                    boolean tesisEnPendiente = tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE));
                    //se valida que no haya pasado la fecha de subir nota ó que la tesis se encuentre pendiente y no haya pasdo la fecha de levantar el pendiente
                    if (!tesis.getSemestreIniciacion().getMaxFechaSubirNotaTesis1().after(new Date()) || tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE)) && tesis.getSemestreIniciacion().getMaxFechaLevantarPendienteTesis1().after(new Date())) {
                        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_1);
                        String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_REPORTAR_NOTA);
                        String login = tesis.getAsesor().getPersona().getCorreo();
                        String infoAdicional = "Se reporto la nota de tesis1 del estudiante " + tesis.getEstudiante().getPersona().getCorreo();
                        Date fechaLim;
                        if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE))) {
                            fechaLim = tesis.getSemestreIniciacion().getMaxFechaLevantarPendienteTesis1();
                        } else {
                            fechaLim = tesis.getSemestreIniciacion().getMaxFechaSubirNotaTesis1();
                        }
                        reportarEntregaTardia(fechaLim, comando, accion, login, infoAdicional);
                    }

                    //colocar nota
                    Secuencia secNotaTesis = secNotatesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
                    if (secNotaTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_NOTA_APROBADA))) {
                        if (!conversor.comprobarExistenciaarchivo(ruta, nombreArchivo)) {
                            //devolver error no se encontro archivo=> no se puede colocar nota
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0009, new ArrayList());
                        }

                        tesis.setRutaArticuloTesis1(nombreArchivo);
                        tesis.setCalificacionTesis(secNotaTesis.getValor());
                        tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS1_TERMINADA));

                        tesis1Facade.edit(tesis);
                        //: enviar correo estudiante informando nota de tesis
                        String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_TESIS;
                        String mensajeCreacion = Notificaciones.MENSAJE_CALIFICACION_TESIS;
                        mensajeCreacion = mensajeCreacion.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeCreacion = mensajeCreacion.replaceFirst("%2", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        mensajeCreacion = mensajeCreacion.replaceFirst("%3", secNotaTesis.getValor());
                        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                        //: enviar correo coordinación informando nota de tesis
                        String asuntoCreacionCoord = Notificaciones.ASUNTO_CALIFICACION_TESIS1_COORDINACION;
                        asuntoCreacionCoord = asuntoCreacionCoord.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajeCreacionCoord = Notificaciones.MENSAJE_CALIFICACION_TESIS1_COORDINACION;
                        mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", secNotaTesis.getValor());
                        mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%3", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCreacionCoord, null, null, null, mensajeCreacionCoord);
                        //
                        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1);
                        if (tesisEnPendiente) {
                            tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1_PENDIENTE);
                        }
                        Properties parametros = new Properties();
                        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));
                        completarTareaSencilla(tipo, parametros);
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                    } //----------------------------------------------------------------------------------------------------
                    else if (secNotaTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_NOTA_REPROBADA))) {
                        tesis.setRutaArticuloTesis1(null);
                        tesis.setCalificacionTesis(secNotaTesis.getValor());
                        tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS1_TERMINADA));

                        tesis1Facade.edit(tesis);
                        //: enviar correo estudiante informando nota de tesis
                        String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_TESIS;
                        String mensajeCreacion = Notificaciones.MENSAJE_CALIFICACION_TESIS;
                        mensajeCreacion = mensajeCreacion.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeCreacion = mensajeCreacion.replaceFirst("%2", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        mensajeCreacion = mensajeCreacion.replaceFirst("%3", secNotaTesis.getValor());
                        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                        //: enviar correo coordinación informando nota de tesis
                        String asuntoCreacionCoord = Notificaciones.ASUNTO_CALIFICACION_TESIS1_COORDINACION;
                        asuntoCreacionCoord = asuntoCreacionCoord.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajeCreacionCoord = Notificaciones.MENSAJE_CALIFICACION_TESIS1_COORDINACION;
                        mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", secNotaTesis.getValor());
                        mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%3", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCreacionCoord, null, null, null, mensajeCreacionCoord);
                        //
                        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1);
                        if (tesisEnPendiente) {
                            tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1_PENDIENTE);
                        }
                        Properties parametros = new Properties();
                        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));
                        completarTareaSencilla(tipo, parametros);

                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                    } else {
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                    }
                }
                //enviar error tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            }
            //xml mal formado COM_ERR_0001
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String aprobarRechazarPendienteTesis1(String xml) {
        try {
            parser.leerXML(xml);

            Secuencia secIdTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secAprobacion = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_COORDINADOR_MAESTRIA));
            Secuencia secCorreoCoord = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));

            if (secIdTesis != null && secAprobacion != null && secCorreoCoord != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                Tesis1 tesis = tesis1Facade.find(id);

                if (tesis != null) {
                    //valida que la fecha actual no sobrepase la fecha máxima para levantar pendiente
                    if (!tesis.getSemestreIniciacion().getMaxFechaLevantarPendienteTesis1().after(new Date())) {
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_PENDIENTE_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());

                    } //valida que la tesis se encuentre pendiente de aprobación de pendiente
                    if (!tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE))) {
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_PENDIENTE_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00032, new ArrayList());

                    } //acepta/rechaza?
                    //si acepta, la tesis queda en estado pendiente
                    boolean solicitudAceptada = secAprobacion.getValor().equals(getConstanteBean().getConstante(Constantes.TRUE));

                    if (solicitudAceptada) {
                        tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE));

                    } else { //de lo contrario vuelve a estar en curso
                        tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));

                    }
                    tesis1Facade.edit(tesis);
                    //termina tarea del coordinador
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PENDIENTE_TESIS1_COORDINACION), propiedades);
                    //envía correos
                    if (solicitudAceptada) {
                        //: enviar correo estudiante informando aprobación
                        String asuntoPendEstudiante = Notificaciones.ASUNTO_APROBACION_PENDIENTE_TESIS1_ESTUDIANTE;
                        String mensajePendEstudiante = Notificaciones.MENSAJE_APROBACION_PENDIENTE_TESIS1_ESTUDIANTE;
                        mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%2", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoPendEstudiante, null, null, null, mensajePendEstudiante);
                        //: enviar correo asesor informando aprobación
                        String asuntoPendAsesor = Notificaciones.ASUNTO_APROBACION_PENDIENTE_TESIS1_ASESOR;
                        asuntoPendAsesor = asuntoPendAsesor.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajePendAsesor = Notificaciones.MENSAJE_APROBACION_PENDIENTE_TESIS1_ASESOR;
                        mensajePendAsesor = mensajePendAsesor.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        SimpleDateFormat sdfHMS = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                        mensajePendAsesor = mensajePendAsesor.replaceFirst("%2", sdfHMS.format(tesis.getSemestreIniciacion().getMaxFechaLevantarPendienteTesis1()));
                        correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoPendAsesor, null, null, null, mensajePendAsesor);
                        //: enviar correo coordinación informando aprobación
                        String asuntoPendCoord = Notificaciones.ASUNTO_APROBACION_PENDIENTE_TESIS1_COORDINACION;
                        asuntoPendCoord = asuntoPendCoord.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajePendCoord = Notificaciones.MENSAJE_APROBACION_PENDIENTE_TESIS1_COORDINACION;
                        mensajePendCoord = mensajePendCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        mensajePendCoord = mensajePendCoord.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoPendCoord, null, null, null, mensajePendCoord);
                    } else {
                        //: enviar correo estudiante informando rechazo
                        String asuntoPendEstudiante = Notificaciones.ASUNTO_RECHAZO_PENDIENTE_TESIS1_ESTUDIANTE;
                        String mensajePendEstudiante = Notificaciones.MENSAJE_RECHAZO_PENDIENTE_TESIS1_ESTUDIANTE;
                        mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%2", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoPendEstudiante, null, null, null, mensajePendEstudiante);

                        //: enviar correo asesor informando rechazo
                        String asuntoPendAsesor = Notificaciones.ASUNTO_RECHAZO_PENDIENTE_TESIS1_ASESOR;
                        asuntoPendAsesor = asuntoPendAsesor.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajePendAsesor = Notificaciones.MENSAJE_RECHAZO_PENDIENTE_TESIS1_ASESOR;
                        mensajePendAsesor = mensajePendAsesor.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoPendAsesor, null, null, null, mensajePendAsesor);

                        //: enviar correo coordinación informando rechazo
                        String asuntoPendCoord = Notificaciones.ASUNTO_RECHAZO_PENDIENTE_TESIS1_COORDINACION;
                        asuntoPendCoord = asuntoPendCoord.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajePendCoord = Notificaciones.MENSAJE_RECHAZO_PENDIENTE_TESIS1_COORDINACION;
                        mensajePendCoord = mensajePendCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        mensajePendCoord = mensajePendCoord.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoPendCoord, null, null, null, mensajePendCoord);

                        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
                        Timestamp fechaTimer = new Timestamp(tesis.getSemestreIniciacion().getMaxFechaSubirNotaTesis1().getTime() - (8 * undia));
                        Date hoy = new Date();
                        //si ya se crearon las tareas para calificar y a esta no por lo que no estaba en el estado correcto, se le crea la tarea.
                        if (hoy.after(fechaTimer)) {
                            creatTareaTesis1CalificarATiempo(tesis);
                        }

                    }

                    //retorna correctamente
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1), ""));


                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_PENDIENTE_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


                } //enviar error tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();


                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_PENDIENTE_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());


            } //xml mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();


            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_PENDIENTE_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());





        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();





                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_PENDIENTE_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);



                return null;
            }


        }
    }

    /**
     * Retiro de tesis 1 por parte del estudiante (informa una vez que se retira vía banner)
     * @param xml
     * @return
     */
    public String retirarTesis1(String xml) {
        try {
            parser.leerXML(xml);

            Secuencia secIdTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secCorreoEstudiante = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            //verifica que se hayan enviado los parámetros correctamente


            if (secIdTesis != null && secCorreoEstudiante != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                Tesis1 tesis = tesis1Facade.find(id);

                //verifica que el correo corresponda con el estudiante
                String correo = secCorreoEstudiante.getValor();


                if (!tesis.getEstudiante().getPersona().getCorreo().equals(correo)) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();


                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00033, new ArrayList());


                }

                if (tesis != null) {
                    tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_1_RETIRADA));
                    tesis1Facade.edit(tesis);

                    //: enviar correo estudiante informando retiro
                    String asuntoPendEstudiante = Notificaciones.ASUNTO_RETIRO_TESIS1_ESTUDIANTE;
                    String mensajePendEstudiante = Notificaciones.MENSAJE_RETIRO_TESIS1_ESTUDIANTE;
                    mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%2", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoPendEstudiante, null, null, null, mensajePendEstudiante);

                    //: enviar correo asesor informando retiro
                    String asuntoPendAsesor = Notificaciones.ASUNTO_RETIRO_TESIS1_ASESOR;
                    asuntoPendAsesor = asuntoPendAsesor.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    String mensajePendAsesor = Notificaciones.MENSAJE_RETIRO_TESIS1_ASESOR;
                    mensajePendAsesor = mensajePendAsesor.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoPendAsesor, null, null, null, mensajePendAsesor);

                    //: enviar correo coordinación informando retiro
                    String asuntoPendCoord = Notificaciones.ASUNTO_RETIRO_TESIS1_COORDINACION;
                    asuntoPendCoord = asuntoPendCoord.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    String mensajePendCoord = Notificaciones.MENSAJE_RETIRO_TESIS1_COORDINACION;
                    mensajePendCoord = mensajePendCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                    mensajePendCoord = mensajePendCoord.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoPendCoord, null, null, null, mensajePendCoord);

                    //retorna
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();


                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


                } //enviar error tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();


                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());


            } //xml mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();


            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());





        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();





                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);



                return null;
            }


        }
    }

    public ParserT getParser() {
        return parser;


    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;


    }

    private void crearTimmerParaMigracionTesis1Rechazadas() {
        try {

            Date d = new Date();
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechadia = sdf.format(d);
            Date antes = sdf.parse(fechadia);
            Timestamp fechaManana1AM = new Timestamp(new Date().getTime() + 1000 * 60 * 24);
            String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_1_RECHAZADAS_A_HISTORICOS) + "-" + fechaManana1AM;
            boolean yaExiste = timerGenerico.existeTimerCompletamenteIgual(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, fechaManana1AM, mensajeAsociado);
            if (!yaExiste) {
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, fechaManana1AM, mensajeAsociado,
                        "TesisMaestria", this.getClass().getName(), "crearTimmerParaMigracionTesis1Rechazadas", "Este timer no se crea desde el metodo porque nadie lo invoca");
            }
        } catch (ParseException ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Requerimiento desarrollo tesis 1 y 2
     */
    public String agregarComentario30PorcientoTesis1(String xml) {
        try {
            /*
             * xml:
             * <idgeneral>idde la tesis </idgeneral>
             * <cmentarioTesis>
             * <comentario> el comentario del asesor </comentario>
             * <debeRetirar>BOOLEAN de si debe retirar</debeRetirar>
            </cmentarioTesis>
             */
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));


            if (secId != null) {
                Long idTesis = Long.parseLong(secId.getValor().trim());
                Tesis1 tesis = tesis1Facade.find(idTesis);
                if (tesis != null) {
                    //despues de la fecha se debe reportar que la accion fue realizada despues de la fecha permitida
                    if (tesis.getSemestreIniciacion().getFechaDel30Porciento().before(new Date())) {
                        String comando = getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_1);
                        String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_REALIZAR_INFORME_30);
                        String login = tesis.getAsesor().getPersona().getCorreo();
                        String infoAdicional = "Se envio el reporte de 30% del estudiante " + tesis.getEstudiante().getPersona().getCorreo();
                        reportarEntregaTardia(tesis.getSemestreIniciacion().getFechaDel30Porciento(), comando, accion, login, infoAdicional);
                        //Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        //return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
                    }
                    Secuencia secComentarioTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO_TESIS));
                    ComentarioTesis coment = conversor.pasarSecuenciaAComentarioTesis(secComentarioTesis);

                    terminarTareaTesis1Comentario30Porciento(tesis);

                    if (coment.getId() != null) {
                        coment.setFecha(new Timestamp(new Date().getTime()));
                        comentarioTesisFacade.edit(coment);
                        ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                    } else {
                        coment.setFecha(new Timestamp(new Date().getTime()));
                        comentarioTesisFacade.create(coment);
                        Collection<ComentarioTesis> comentsTesis = tesis.getComentariosAsesor();
                        comentsTesis.add(coment);
                        tesis1Facade.edit(tesis);
                        ArrayList<Secuencia> param = new ArrayList<Secuencia>();

                        //envía mensaje al estudiante
                        String asuntoEstudiante = Notificaciones.ASUNTO_INFORME_30PORCIENTO_TESIS_1;
                        String mensajeEstudiante = Notificaciones.MENSAJE_INFORME_30PORCIENTO_TESIS_1;
                        mensajeEstudiante = mensajeEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeEstudiante = mensajeEstudiante.replaceFirst("%2", coment.getDebeRetirar() ? "<strong>[LE HA SUGERIDO QUE RETIRE TESIS 1]</strong>" : "");
                        mensajeEstudiante = mensajeEstudiante.replaceFirst("%3", coment.getComentario());
                        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoEstudiante, null, null, null, mensajeEstudiante);

                        //envía mensaje a coordinación
                        String asuntoCoord = Notificaciones.ASUNTO_INFORME_30PORCIENTO_TESIS_1_COORD;
                        asuntoCoord = asuntoCoord.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajeCoord = Notificaciones.MENSAJE_INFORME_30PORCIENTO_TESIS_1_COORD;
                        mensajeCoord = mensajeCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        mensajeCoord = mensajeCoord.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeCoord = mensajeCoord.replaceFirst("%3", coment.getDebeRetirar() ? "<strong>[LE HA SUGERIDO QUE RETIRE TESIS 1]</strong>" : "");
                        mensajeCoord = mensajeCoord.replaceFirst("%4", coment.getComentario());
                        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCoord, null, null, null, mensajeCoord);
                        return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                    }
                } else {
                    //tirar error
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                }
            } else {
                //tirar exception xml mal formado
                throw new Exception("xml mal formado en agregarComentarioTesis1 : TesisBean ");
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darComentariosPorTesis1(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));


            if (secId != null) {
                Long idTesis = Long.parseLong(secId.getValor().trim());
                Tesis1 tesis = tesis1Facade.find(idTesis);

                Collection<ComentarioTesis> coments = tesis.getComentariosAsesor();
                Secuencia seccoemtns = conversor.pasarComentariosTesisASecuencia(coments);

                ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                param.add(seccoemtns);


                return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());



            } else {
                //tirar error
                //tirar error
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();


                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());






            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();





                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);



                return null;
            }


        }
    }

    public void crearTareaAsesorAprobacionTesis1(Tesis1 tesis) {
        //--crear tarea y notificacion--------------------------------------------------------------------------
        //4.1 notificacion:
        //4.2 crear tarea...
        /*String nombreV = "Aprobar Inscripción de Tesis 1";// + carga.getId();
        String descripcion = "Se debe Aceptar/Rechazarla asesoría en la tesis de los estudiantes: " + tesis.getEstudiante().getPersona().getNombres() + " "
        + tesis.getEstudiante().getPersona().getApellidos() + " (" + tesis.getEstudiante().getPersona().getCorreo() + ")";
        String categoria = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);*/
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_ASESOR);
        Persona profesor = tesis.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Date fechaMaxAprobarInscripcion = new Date(tesis.getSemestreIniciacion().getMaxFechaAprobacionTesis1().getTime());
        String mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO, tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos(), tesis.getEstudiante().getPersona().getCorreo());
        boolean agrupable = true;
        String header = String.format(Notificaciones.MENSAJE_HEADER_APROBAR_INSCRIPCION_TESIS1_ASESOR, profesor.getNombres() + " " + profesor.getApellidos());
        String footer = Notificaciones.MENSAJE_FOOTER_APROBAR_INSCRIPCION_TESIS1_ASESOR;
        Timestamp fFin = new Timestamp(fechaMaxAprobarInscripcion.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1);//
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));

        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);

        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_TESIS1_ASESOR;
        tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, parametros, asunto);



    }

    public void crearTareaAprobarTesisCoordinadorMaestria(Tesis1 tesis) {

//                            Rol rol = rolFacade.findByRol(getConstanteBean().getConstante(Constantes.ROL_COORDINACION));
//                            Usuario usuario = usuarioFacade.findByRol(rol.getRol()).iterator().next();
//                            String correoCOordinadorMaestria = usuario.getPersona().getCorreo();// getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String nombreV = "Aprobar Inscripción de Tesis 1";// + carga.getId();
        String descripcion = "Se debe Aceptar/Rechazarla asesoría en la tesis del estudiante: " + tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos() + " (" + tesis.getEstudiante().getPersona().getCorreo() + ")";
        Long undia = 1000L * 60L * 60L * 24L;

        /*
         * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS1_COORDINADOR_MAESTRIA);
        Persona profesor = tesis.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Timestamp fInicio = new Timestamp(fechaInicioDate.getTime());
        Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
        if (tesis.getSemestreIniciacion().getMaxFechaAprobacionTesis1Coordinacion() != null) {
            fechaFinDate = new Date(tesis.getSemestreIniciacion().getMaxFechaAprobacionTesis1Coordinacion().getTime() + undia);
        }
        String mensajeBulletTarea = String.format(Notificaciones.MENSAJE_BULLET_SOLICITUD_PENDIENTE_TESIS1_COORDINACION, tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos(), tesis.getEstudiante().getPersona().getCorreo(), profesor.getNombres() + " " + profesor.getApellidos(), profesor.getCorreo());
        boolean agrupable = true;
        String header = Notificaciones.MENSAJE_HEADER_APROBAR_INSCRIPCION_TESIS1_COORDINACION;
        String footer = Notificaciones.MENSAJE_FOOTER_APROBAR_INSCRIPCION_TESIS1_COORDINACION;
        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PARA_COORDINACION);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));
        String rol = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_TESIS1_COORDINACION;
        tareaBean.crearTareaRol(mensajeBulletTarea, tipo, rol, agrupable, header, footer, fInicio, fFin, comando, parametros, asunto);
    }

    private void notificarEstudianteAprobacionTesis1(Tesis1 tesis) {
        String asunto = Notificaciones.ASUNTO_APROBACION_INSCRIPCION_TESIS_1;
        String mensaje = Notificaciones.MENSAJE_APROBACION_INSCRIPCION_TESIS_1;
        mensaje = mensaje.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asunto, tesis.getAsesor().getPersona().getCorreo(), null, null, mensaje);
    }

    public void crearTareaPendienteIncripcionBanner(Tesis1 tesis) {

        String nombreV = "Aprobar Inscripción de Tesis 1";// + carga.getId();
        String descripcion = "Se debe realizar la inscripción en Banner  de tesis 1 para el estudiante: " + tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos() + " (" + tesis.getEstudiante().getPersona().getCorreo() + ")";
        Long undia = 1000L * 60L * 60L * 24L;

        /*
         * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

        String tipo = getConstanteBean().getConstante(Constantes.TAREA_INSCRIBIR_TESIS1_BANNER_POR_COODINACION);
        Persona profesor = tesis.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Timestamp fInicio = new Timestamp(fechaInicioDate.getTime());
        Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
        if (tesis.getSemestreIniciacion().getMaxFechaAprobacionTesis1Coordinacion() != null) {
            fechaFinDate = new Date(tesis.getSemestreIniciacion().getMaxFechaSubirNotaTesis1().getTime() + undia);
        }
        String mensajeBulletTarea = String.format(Notificaciones.MENSAJE_BULLET_INSCRIPCION_BANNER_TESIS1_COORDINACION, tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos(), tesis.getEstudiante().getPersona().getCorreo(), profesor.getNombres() + " " + profesor.getApellidos(), profesor.getCorreo());
        boolean agrupable = true;
        String header = Notificaciones.MENSAJE_HEADER_INSCRIPCION_BANNER_TESIS1_COORDINACION;
        String footer = Notificaciones.MENSAJE_FOOTER_INSCRIPCION_BANNER_TESIS1_COORDINACION;
        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_PARA_COORDINACION);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));
        String rol = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_BANNER_TESIS1_COORDINACION;
        tareaBean.crearTareaRol(mensajeBulletTarea, tipo, rol, agrupable, header, footer, fInicio, fFin, comando, parametros, asunto);
    }

    public void crearTareaCalificarTesis1(String idPeriodo) {
        {
            Long idP = Long.parseLong(idPeriodo);
            PeriodoTesis periodo = periodoFacadelocal.find(idP);
            String nombrePeriodo = periodo.getPeriodo();
            Collection<Tesis1> tesises = tesis1Facade.findByPeriodoYEstado(nombrePeriodo, getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
            // System.out.println("NUMERO DE TESIS= " + tesises.size() + "Periodo=" + nombrePeriodo + " Estado=" + getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
            for (Tesis1 tesis1 : tesises) {
                creatTareaTesis1CalificarATiempo(tesis1);
            }
        }


    }

    public void crearTareaCalificarTesis1Pendiente(String idPeriodo) {

        System.out.println("LLEGO AL METODO DE TESIS1BEAN crearTareaCalificarTesis1Pendiente ");
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        Long idP = Long.parseLong(idPeriodo);
        PeriodoTesis periodo = periodoFacadelocal.find(idP);
        String nombrePeriodo = periodo.getPeriodo();
        Collection<Tesis1> tesises = tesis1Facade.findByPeriodoYEstado(nombrePeriodo, getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_PENDIENTE));
        for (Tesis1 tesis1 : tesises) {

            creatTareaCalificarTesis1Pendiente(tesis1);

        }
    }

    //-----------------------------------------------------------------------------
    // TIMERS
    //-----------------------------------------------------------------------------
    @Deprecated
    public void manejoTimmersTesisMaestria(String comando) {
        System.out.println("TIMMERS: " + comando);
        String[] pamrametros = comando.split("-");
        if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RECHAZADAS_A_HISTORICOS))) {
            migrarTesisRechazada(tesis1Facade.find(Long.parseLong(pamrametros[1].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS))) {
            migrarTesisRetiradas();
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_TERMINADAS_A_HISTORICOS))) {
            migrarTesisTerminadas();
        }
    }

    @Deprecated
    private void editarTimersPeriodo(PeriodoTesis semestre, boolean editado) {
        System.out.println("EDITANDO TIMERS PG");
        Long undia = 1000L * 60 * 60 * 24;
        Date hoy = new Date();

        //TIMER Migrar
        //TODO: Crear un panel para configuración de estas fechas (por ahora queda quemado)
        String mensajeAsociadoMigrarTesis1Retirados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS) + "-" + semestre.getId();
        if (new Timestamp(semestre.getMaxFechaAprobacionTesis1().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionTesis1().getTime() + (undia * 15)), mensajeAsociadoMigrarTesis1Retirados,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer no se crea desde el metodo porque nadie lo invoca");
        }
        //TIMERS PARA MIGRAR PROYECTOS DE GRADO TERMINADOS
        //TODO: Crear un panel para configuración de estas fechas
        /*
        String mensajeAsociadoMigrarTesis1Terminados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_TERMINADAS_A_HISTORICOS) + "-" + semestre.getId();
        if (new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime()).after(hoy)) {
        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaSubirNotaTesis1().getTime() + (undia * 15)), mensajeAsociadoMigrarTesis1Terminados,
        "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer no se crea desde el metodo porque nadie lo invoca");
        }*/
    }

    public String confirmarTesis1EnBanner(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null) {
                Long idTesis = Long.parseLong(secId.getValor().trim());
                Tesis1 tesis = tesis1Facade.find(idTesis);
                if (tesis != null) {

                    Secuencia secEstadoTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));
                    String estado = secEstadoTesis.getValor();
                    tesis.setEstadoTesis(estado);
                    tesis1Facade.edit(tesis);

                    //--------terminar tarea coordinador ------------------------------------------
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), idTesis.toString());
                    completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAREA_INSCRIBIR_TESIS1_BANNER_POR_COODINACION), propiedades);
                    //FIN--------terminar tarea coordinador ------------------------------------------



                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                } else {
                    //tirar error
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                }
            } else {
                //tirar exception xml mal formado
                throw new Exception("xml mal formado en agregarComentarioTesis1 : TesisBean ");
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    //-----------------------------------------------------------------------------
    // HISTÓRICOS TESIS 1
    //-----------------------------------------------------------------------------
    public void migrarTesisRechazada(Long tesisId) {
        migrarTesisRechazada(tesis1Facade.find(tesisId));
    }

    public void migrarTesisRechazada(Tesis1 tesis) {
        if (tesis != null) {
            try {
                ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
                Secuencia secTesis = conversor.pasarTesis1ASecuencia(tesis);

                secs.add(secTesis);
                String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RECHAZADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                String resp = historicoBean.pasarTesis1RechazadaAHistorico(comandoXML);
                if (resp == null) {
                    String mensajeAsociadoMigrarTesis1 = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RECHAZADAS_A_HISTORICOS) + "-" + tesis.getId();
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarTesis1,
                            "TesisMaestria", this.getClass().getName(), "migrarTesisRechazada", "Este timer se crea porque el proceso de migrar la tesis 1 rechazada no fue exitoso");
                    return;
                } else {
                    //ya esta en historicos entonces se procede a borrarla de prod:
                    tesis1Facade.remove(tesis);
                }
            } catch (Exception ex) {
                Logger.getLogger(Tesis1Bean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean migrarTesisRetirada(Long tesisId) {
        boolean migracionCompletaExitosa = false;
        try {
            migracionCompletaExitosa = true;
            String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);

            Collection<Tesis1> tesis1 = new ArrayList<Tesis1>();
            Tesis1 t = tesis1Facade.find(tesisId);
            tesis1.add(t);

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            Secuencia secTesis = conversor.pasarTesises1ASecuencias(tesis1);
            secs.add(secTesis);
            String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
            String respTesis1 = historicoBean.pasarTesis1RetiradasAHistorico(comandoXML);

            if (respTesis1 == null) {
                migracionCompletaExitosa = false;
                String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                asunto = asunto.replaceFirst("%1", "1 retirada");
                asunto = asunto.replaceFirst("%2", " no migro tesis 1 retirada");
                String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                mensaje = mensaje.replaceFirst("%1", "1 Retirada");
                mensaje = mensaje.replaceFirst("%2", t.getEstudiante().getPersona().getNombres() + " " + t.getEstudiante().getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%3", " sin embargo la tesis1 retirada no pudo se migrada, por tanto la información asociada no fue borrada de producción.");
                correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
            } else {
                tesis1Facade.remove(tesis1.iterator().next());
            }
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return migracionCompletaExitosa;
    }

    public void migrarTesisRetiradas() {

        //Collection<Tesis1> tesises = tesis1Facade.findByEstadoYPeriodoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_1_RETIRADA),idPeriodo);
        String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;

        Collection<Tesis1> tesises = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_1_RETIRADA));
        boolean migracionCompletaExitosa = true;

        if (tesises.isEmpty()) {
            String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS);
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                    "TesisMaestria", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque no hay tesis 1 retiradas para migrar");

            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_1_RETIRADAS_A_HISTORICOS_SIN_TESISES;
            asunto = asunto.replaceFirst("%1", "sin tesis retiradas por migrar");
            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_1_RETIRADAS_A_HISTORICOS_SIN_TESISES;
            mensaje = mensaje.replaceFirst("%1", "no habia tesis retiradas a migrar");
            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
        } else {
            try {

                for (Tesis1 t : tesises) {
                    Boolean rta = migrarTesisRetirada(t.getId());
                    migracionCompletaExitosa = migracionCompletaExitosa || rta;
                }

                if (migracionCompletaExitosa == false) {
                    String mensajeAsociadoMigrarTesis1 = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS);
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarTesis1,
                            "TesisMaestria", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque la migracion de tesis 1 retiradas a historicos no fue exitoso");

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_1_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                    asunto = asunto.replaceFirst("%1", "produjo error");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_1_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                    mensaje = mensaje.replaceFirst("%1", "sin embargo se produjo un error");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {

                    Collection<Tesis1> tesisEnCurso = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));

                    if (tesisEnCurso.size() > 0) {
                        String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_RETIRADAS_A_HISTORICOS);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                                "TesisMaestria", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque la migracion de tesis 1 retiradas a historicos se realizo, "
                                + "sin embargo hay algunas tesis 1 en curso y podrían ser retiradas luego no fueron migradas");
                        String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_1_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                        asunto = asunto.replaceFirst("%1", "con tesis que aún no han sido terminadas");
                        String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_1_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                        mensaje = mensaje.replaceFirst("%1", "sin embargo hay algunas tesis no estan terminadas y que podrían ser retiradas luego no fueron migradas");
                        correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean migrarTesisPerdida(Long tesisId) {
        boolean migracionCompletaExitosa = false;
        try {
            migracionCompletaExitosa = true;
            String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);

            Collection<Tesis1> tesis1 = new ArrayList<Tesis1>();
            Tesis1 t = tesis1Facade.find(tesisId);
            tesis1.add(t);

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            Secuencia secTesis = conversor.pasarTesises1ASecuencias(tesis1);
            secs.add(secTesis);
            String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
            String respTesis1 = historicoBean.pasarTesis1PerdidasAHistorico(comandoXML);

            if (respTesis1 == null) {
                migracionCompletaExitosa = false;
                String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                asunto = asunto.replaceFirst("%1", "1 perdida");
                asunto = asunto.replaceFirst("%2", " no migro tesis 1 perdida");
                String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                mensaje = mensaje.replaceFirst("%1", "1 Perdida");
                mensaje = mensaje.replaceFirst("%2", t.getEstudiante().getPersona().getNombres() + " " + t.getEstudiante().getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%3", " sin embargo la tesis1 perdida no pudo se migrada, por tanto la información asociada no fue borrada de producción.");
                correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
            } else {
                tesis1Facade.remove(tesis1.iterator().next());
            }
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return migracionCompletaExitosa;
    }

    public void migrarTesisPerdidas() {

        String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;

        Collection<Tesis1> tesises = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
        boolean migracionCompletaExitosa = true;

        if (tesises.isEmpty()) {
            String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS);
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                    "TesisMaestria", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque no hay tesis 1 perdidas para migrar");

            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_1_PERDIDAS_A_HISTORICOS_SIN_TESISES;
            asunto = asunto.replaceFirst("%1", "sin tesis perdidas por migrar");
            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_1_PERDIDAS_A_HISTORICOS_SIN_TESISES;
            mensaje = mensaje.replaceFirst("%1", "no habia tesis perdidas a migrar");
            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
        } else {
            try {
                for (Tesis1 t : tesises) {
                    Boolean rta = migrarTesisPerdida(t.getId());
                    migracionCompletaExitosa = migracionCompletaExitosa || rta;
                }

                if (migracionCompletaExitosa == false) {
                    String mensajeAsociadoMigrarTesis1 = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS);
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarTesis1,
                            "TesisMaestria", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque la migracion de tesis 1 perdidas a historicos no fue exitoso");

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_1_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                    asunto = asunto.replaceFirst("%1", "produjo error");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_1_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                    mensaje = mensaje.replaceFirst("%1", "sin embargo se produjo un error");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {
                    Collection<Tesis1> tesisEnCurso = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));

                    if (tesisEnCurso.size() > 0) {
                        String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_PERDIDAS_A_HISTORICOS);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                                "TesisMaestria", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque la migracion de tesis 1 perdidas a historicos se realizo, "
                                + "sin embargo hay algunas tesis 1 en curso y podrían ser perdidas luego no fueron migradas");
                        String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_1_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                        asunto = asunto.replaceFirst("%1", "con tesis que aún no han sido terminadas");
                        String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_1_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                        mensaje = mensaje.replaceFirst("%1", "sin embargo hay algunas tesis no estan terminadas y que podrían ser perdidas, luego no fueron migradas");
                        correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String darTesis1AMigrar(String xml) {
        try {
            parser.leerXML(xml);

            Collection<Tesis1> tesises = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_1_RETIRADA));
            Collection<Tesis1> tesisesP = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));

            tesises.addAll(tesisesP);

            Secuencia secSoli = conversor.pasarTesises1ASecuencias(tesises);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_1_A_MIGRAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_1_A_MIGRAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String comportamientoEmergenciaMigrarTesis1(String xml) {
        try {
            String retorno = null;
            Boolean migracionExitosa = null;
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null && secId.getValor() != null) {
                Long idTesis = Long.parseLong(secId.getValor());
                Tesis1 tesis = tesis1Facade.find(idTesis);

                if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_1_RETIRADA))) {
                    migracionExitosa = migrarTesisRetirada(tesis.getId());
                } else if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA))) {
                    migracionExitosa = migrarTesisPerdida(tesis.getId());
                }

                if (migracionExitosa == false) {
                    retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00042, new LinkedList<Secuencia>());
                    return retorno;
                }

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                throw new Exception("Tesis1Bean - comportamientoEmergenciaMigrarTesis1");
            }
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    @Deprecated
    public void migrarTesisTerminadas() {
        Collection<Tesis1> tesis = tesis1Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS1_TERMINADA));
        try {
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            Secuencia secTesis = conversor.pasarTesises1ASecuencias(tesis);
            secs.add(secTesis);
            String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_TERMINADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
            String resp = historicoBean.pasarTesis1TerminadasAHistorico(comandoXML);
            if (resp == null) {
                String mensajeAsociadoMigrarTesisPregrado = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_TERMINADAS_A_HISTORICOS) + "-";
                timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarTesisPregrado,
                        "TesisMaestria", this.getClass().getName(), "migrarTesisTerminadas", "Este timer se crea si se migraron tesis 1 del periodo pero aún hay tesis en curso");
            } else {
                //ya esta en historicos entonces se procede a borrarla de prod:
                //TODO:QUITARtesis1Facade.remove(tesis);
            }
        } catch (Exception ex) {
            Logger.getLogger(Tesis1Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarCorreoCreacionTesisExtemporanea(Tesis1 tesis) {
        //Crear correo para informar que se rechazo la inscripción--ESTUDIANTE--
        String asunto = Notificaciones.ASUNTO_APROBACION_INSCRIPCION_TESIS_1;
        String mensaje = Notificaciones.MENSAJE_APROBACION_INSCRIPCION_TESIS_1_EXTEMPORAL;
        String correoCoordinacion = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        mensaje = mensaje.replaceFirst("%0", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
        mensaje = mensaje.replaceFirst("%1", tesis.getSemestreIniciacion().getPeriodo());
        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asunto, tesis.getAsesor().getPersona().getCorreo(), correoCoordinacion, null, mensaje);
    }

    public String consultaExternaTesis1(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo.getValor() != null) {
                String correo = secCorreo.getValor();
                Collection<Tesis1> tesises = tesis1Facade.findByCorreoAsesor(correo);
                if (tesises != null) {

                    Collection<Tesis1> tesisesExternos = new ArrayList<Tesis1>();
                    for (Tesis1 t : tesises) {
                        Tesis1 r = new Tesis1();
                        r.setAsesor(t.getAsesor());
                        r.setEstudiante(t.getEstudiante());
                        r.setFechaTerminacion(t.getFechaTerminacion());
                        r.setTemaTesis(t.getTemaTesis());
                        r.setSemestreIniciacion(t.getSemestreIniciacion());
                        tesisesExternos.add(r);
                    }
                    Secuencia secTesises = conversor.pasarTesises1ASecuencias(tesisesExternos);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(secTesises);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), ""));
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
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

    public void crearTareaCoordinadorAprobarPendienteTesis1(Tesis1 tesis) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PENDIENTE_TESIS1_COORDINACION);
        Persona profesor = tesis.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);

        String mensajeBulletTarea = String.format(Notificaciones.MENSAJE_BULLET_SOLICITUD_PENDIENTE_TESIS1_COORDINACION, tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos(), tesis.getEstudiante().getPersona().getCorreo(), profesor.getNombres() + " " + profesor.getApellidos(), profesor.getCorreo());

        boolean agrupable = true;
        String header = Notificaciones.MENSAJE_HEADER_SOLICITUD_PENDIENTE_TESIS1_COORDINACION;
        String footer = Notificaciones.MENSAJE_FOOTER_SOLICITUD_PENDIENTE_TESIS1_COORDINACION;
        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));

        String rol = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);

        String asunto = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_TESIS1_COORDINACION;

        tareaBean.crearTareaRol(mensajeBulletTarea, tipo, rol, agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, parametros, asunto);

    }

    private void terminarTareaTesis1Comentario30Porciento(Tesis1 tesis) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_1);//TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_1
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
        completarTareaSencilla(tipo, propiedades);
    }

    public void creatTareaTesis1CalificarATiempo(Tesis1 tesis1) {
        //aca crear tarea a los asesores---------------------------------------------------------
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        PeriodoTesis periodo = tesis1.getSemestreIniciacion();
        String nombrePeriodo = periodo.getPeriodo();

        String mensaje = Notificaciones.MENSAJE_HEADER_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR;
        Persona p = tesis1.getAsesor().getPersona();
        mensaje = mensaje.replaceFirst("%", p.getNombres() + " " + p.getApellidos());
        mensaje = mensaje.replaceFirst("%", nombrePeriodo);
        mensaje = mensaje.replaceFirst("%", sdfHMS.format(periodo.getMaxFechaSubirNotaTesis1()));

        //4.2 crear tarea...
        String nombreV = "Calificar Tesis 1";// + carga.getId();
        String descripcion = "Se debe Calificar las tesis de los siguientes estudiantes: ";

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1);


        //--------terminar tarea anterior ------------------------------------------
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis1.getId().toString());
        completarTareaSencilla(tipo, propiedades);

        Persona profesor = tesis1.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Date fechaFinDate = periodo.getMaxFechaSubirNotaTesis1();
        String mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO, tesis1.getEstudiante().getPersona().getNombres() + " "
                + tesis1.getEstudiante().getPersona().getApellidos(), tesis1.getEstudiante().getPersona().getCorreo());
        boolean agrupable = true;
        String header = mensaje;
        String footer = Notificaciones.MENSAJE_FOOTER_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR;
        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_COLOCAR_NOTA_TESIS_1_ASESOR);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis1.getId()));

        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);

        String asunto = Notificaciones.ASUNTO_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR;
        tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, parametros, asunto);

    }

    public void creatTareaCalificarTesis1Pendiente(Tesis1 tesis1) {
        //aca crear tarea a los asesores---------------------------------------------------------
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        PeriodoTesis periodo = tesis1.getSemestreIniciacion();
        //-----------------------************************************************************************************************
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_TESIS1_PENDIENTE);
        Date fechaFinDate = periodo.getMaxFechaLevantarPendienteTesis1();
        String mensaje = Notificaciones.MENSAJE_HEADER_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR;
        Persona p = tesis1.getAsesor().getPersona();
        mensaje = mensaje.replaceFirst("%", p.getNombres() + " " + p.getApellidos());
        mensaje = mensaje.replaceFirst("%", periodo.getPeriodo());
        mensaje = mensaje.replaceFirst("%", sdfHMS.format(periodo.getMaxFechaLevantarPendienteTesis1()));

        //4.2 crear tarea...
        String nombreV = "Calificar Tesis 1 en Pendiente";// + carga.getId();
        String descripcion = "Se debe Calificar las tesis de los siguientes estudiantes: ";
        //--------terminar tarea anterior ------------------------------------------
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis1.getId().toString());
        completarTareaSencilla(tipo, propiedades);

        Persona profesor = tesis1.getAsesor().getPersona();
        Date fechaInicioDate = new Date();

        String mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO, tesis1.getEstudiante().getPersona().getNombres() + " "
                + tesis1.getEstudiante().getPersona().getApellidos(), tesis1.getEstudiante().getPersona().getCorreo());
        boolean agrupable = true;
        String header = mensaje;
        String footer = Notificaciones.MENSAJE_FOOTER_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR;
        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_COLOCAR_NOTA_TESIS_1_ASESOR);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis1.getId()));

        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);

        String asunto = Notificaciones.ASUNTO_ULTIMA_FECHA_SUBIR_NOTAS_TESIS_1_ASESOR;
        tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, parametros, asunto);

    }

    public String establecerAprobacionParadigma(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secAprobacionParadigma = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA));
            Secuencia secIdTesis = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            boolean aprobacionParadigma = secAprobacionParadigma.getValor().equals(getConstanteBean().getConstante(Constantes.TRUE)) ? true : false;

            Long idTesis1 = Long.parseLong(secIdTesis.getValor());
            Tesis1 tesis1 = tesis1Facade.find(idTesis1);

            String rutaCompleta = "";
            String rutaCarpeta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS1);

            if (tesis1.getRutaArticuloTesis1().startsWith("/") || tesis1.getRutaArticuloTesis1().startsWith("\\")) {
                rutaCompleta = tesis1.getRutaArticuloTesis1();
            } else {
                rutaCompleta = rutaCarpeta + tesis1.getRutaArticuloTesis1();
            }

            File archivoArticulo = new File(rutaCompleta);
            if (archivoArticulo.exists()) {

                tesis1.setAprobadoParaParadigma(aprobacionParadigma);
                tesis1Facade.edit(tesis1);
                Persona asesor = tesis1.getAsesor().getPersona();
                Persona estudiante = tesis1.getEstudiante().getPersona();
                String mensajeCorreo = String.format(Notificaciones.MENSAJE_APROBACION_REVISTA_PARADIGMA_TESIS1, asesor.getNombres() + " " + asesor.getApellidos(), estudiante.getNombres() + " " + estudiante.getApellidos(), tesis1.getTemaTesis(), tesis1.getAsesor().getGrupoInvestigacion().getNombre());
                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CORREO_REVISTA_PARADIGMA), Notificaciones.ASUNTO_APROBACION_REVISTA_PARADIGMA_TESIS1, null, null, rutaCompleta, mensajeCorreo);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0150, new LinkedList<Secuencia>());
            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00046, new ArrayList());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MSJ_0151, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
}
