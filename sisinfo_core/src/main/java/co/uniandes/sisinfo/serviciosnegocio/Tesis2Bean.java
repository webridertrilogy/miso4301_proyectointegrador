/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.CalificacionCriterio;
import co.uniandes.sisinfo.entities.CalificacionJurado;
import co.uniandes.sisinfo.entities.CategoriaCriterioJurado;
import co.uniandes.sisinfo.entities.CategoriaCriterioTesis2;
import co.uniandes.sisinfo.entities.Coasesor;
import co.uniandes.sisinfo.entities.ComentarioTesis;
import co.uniandes.sisinfo.entities.CriterioCalificacion;
import co.uniandes.sisinfo.entities.HorarioSustentacionTesis;
import co.uniandes.sisinfo.entities.InscripcionSubareaInvestigacion;
import co.uniandes.sisinfo.entities.JuradoExternoUniversidad;
import co.uniandes.sisinfo.entities.PeriodoTesis;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.Tesis1;
import co.uniandes.sisinfo.entities.Tesis2;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.serviciosfuncionales.CalificacionCriterioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CalificacionJuradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CategoriaCriterioTesis2FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CoasesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ComentarioTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.CriterioCalificacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InscripcionSubareaInvestigacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.JuradoExternoUniversidadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis1FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.Tesis2FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeRemote;
import java.io.File;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class Tesis2Bean implements Tesis2BeanRemote, Tesis2BeanLocal {

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
    private CorreoRemote correoBean;
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private InscripcionSubareaInvestigacionFacadeLocal inscripcionSubAreaFacade;
    @EJB
    private Tesis1FacadeLocal tesis1Facade;
    @EJB
    private Tesis2FacadeLocal tesis2Facade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    @EJB
    private HistoricosTesisBeanRemote historicoTesis;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    @EJB
    private ComentarioTesisFacadeLocal comentarioTesisFacade;
    @EJB
    private JuradoExternoUniversidadFacadeLocal juradoFacade;
    @EJB
    private CriterioCalificacionFacadeLocal criteriosFacade;
    @EJB
    private CalificacionJuradoFacadeLocal calificacionJuradoFacade;
    @EJB
    private CategoriaCriterioTesis2FacadeLocal catCriteriosFacade;
    @EJB
    private CalificacionCriterioFacadeLocal calificacionCriterioFacade;
    @EJB
    private TesisBeanLocal tesisBean;
    @EJB
    private CoasesorFacadeLocal coasesorFacade;
    @EJB
    private HistoricoTesis2Remote historicoBean;
    @EJB
    private HistoricoTesis1Remote historicoTesis1Bean;
    @EJB
    private HistoricoInscripcionSubareaInvestRemote historicoInscripcionSubareaInvestBean;
    @EJB
    private JuradosTesisBeanLocal juradoTesisFacade;
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
    @EJB
    private PeriodoTesisFacadeLocal periodoTesis2Facade;
    @EJB
    private UsuarioFacadeRemote usuarioFacade;

    public Tesis2Bean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            estudianteFacadeRemote = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
//          subareaInvestigacionFacadeRemote = (SubareaInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(SubareaInvestigacionFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            historicoTesis = (HistoricosTesisBeanRemote) serviceLocator.getRemoteEJB(HistoricosTesisBeanRemote.class);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            //           reporteFacadeRemote = (ReportesFacadeRemote) serviceLocator.getRemoteEJB(ReportesFacadeRemote.class);
            //          grupoInvestigacionFacade = (GrupoInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(GrupoInvestigacionFacadeRemote.class);
            //           nivelFormacionFacade = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            historicoBean = (HistoricoTesis2Remote) serviceLocator.getRemoteEJB(HistoricoTesis2Remote.class);
            historicoTesis1Bean = (HistoricoTesis1Remote) serviceLocator.getRemoteEJB(HistoricoTesis1Remote.class);
            historicoInscripcionSubareaInvestBean = (HistoricoInscripcionSubareaInvestRemote) serviceLocator.getRemoteEJB(HistoricoInscripcionSubareaInvestRemote.class);
            accionVencidaBean = (AccionVencidaBeanRemote) serviceLocator.getRemoteEJB(AccionVencidaBeanRemote.class);
            conversor = new ConversorTesisMaestria();
            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);
            usuarioFacade = (UsuarioFacadeRemote) serviceLocator.getRemoteEJB(UsuarioFacadeRemote.class);

        } catch (Exception e) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Retiro de tesis 2 por parte del estudiante (informa una vez que se retira vía banner)
     * @param xml
     * @return
     */
    public String retirarTesis2(String xml) {
        try {
            parser.leerXML(xml);

            Secuencia secIdTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secCorreoEstudiante = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            //verifica que se hayan enviado los parámetros correctamente
            if (secIdTesis != null && secCorreoEstudiante != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                Tesis2 tesis = tesis2Facade.find(id);
                //verifica que el correo corresponda con el estudiante
                String correo = secCorreoEstudiante.getValor();
                if (!tesis.getEstudiante().getPersona().getCorreo().equals(correo)) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00033, new ArrayList());
                }
                if (tesis != null) {
                    tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_2_RETIRADA));
                    tesis2Facade.edit(tesis);
                    //: enviar correo estudiante informando retiro
                    String asuntoPendEstudiante = Notificaciones.ASUNTO_RETIRO_TESIS2_ESTUDIANTE;
                    String mensajePendEstudiante = Notificaciones.MENSAJE_RETIRO_TESIS2_ESTUDIANTE;
                    mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%2", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoPendEstudiante, null, null, null, mensajePendEstudiante);
                    //: enviar correo asesor informando retiro
                    String asuntoPendAsesor = Notificaciones.ASUNTO_RETIRO_TESIS2_ASESOR;
                    asuntoPendAsesor = asuntoPendAsesor.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    String mensajePendAsesor = Notificaciones.MENSAJE_RETIRO_TESIS2_ASESOR;
                    mensajePendAsesor = mensajePendAsesor.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoPendAsesor, null, null, null, mensajePendAsesor);
                    //: enviar correo coordinación informando retiro
                    String asuntoPendCoord = Notificaciones.ASUNTO_RETIRO_TESIS2_COORDINACION;
                    asuntoPendCoord = asuntoPendCoord.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    String mensajePendCoord = Notificaciones.MENSAJE_RETIRO_TESIS2_COORDINACION;
                    mensajePendCoord = mensajePendCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                    mensajePendCoord = mensajePendCoord.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoPendCoord, null, null, null, mensajePendCoord);

                    //retorna
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
                //enviar error tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            }
            //xml mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETIRAR_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que se llama para que un estudiante cree una solicitud de tesis2
     * @param xml
     * @return
     */
    public String crearSolicitudTesis2Estudiante(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));

            Tesis2 tesis2 = conversor.pasarSecuenciaATesis2(secTesis);
            PeriodoTesis pt = periodoFacadelocal.findByPeriodo(tesis2.getSemestreInicio().getPeriodo());

            //verifica que se hayan configurado fechas para tesis 2
            if (pt.getFechaUltimaSolicitarTesis2() == null || tesis2.getSemestreInicio().getMaxFechaSubirNotaTesis1() == null) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00025, new ArrayList());
            }
            if (pt.getFechaUltimaSolicitarTesis2().before(new Date())) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
            }

            String correoEst = tesis2.getEstudiante().getPersona().getCorreo();
            Collection<Tesis2> tesises2 = tesis2Facade.findByCorreoEstudiante(correoEst);
            for (Tesis2 t2 : tesises2) {
                if (t2.getSemestreInicio().getPeriodo().equals(tesis2.getSemestreInicio().getPeriodo())) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00016, new ArrayList());
                }
            }

            Collection<Tesis1> t = tesis1Facade.findByCorreoEstudiante(correoEst);
            Tesis1 laAnterior = null;
            //revisar que el estudiante pueda meter tesis 2:
            boolean puedeInscribir = false;
            //Bandera para saber si vio tesis1 en el semestre pasado. Por defecto no la ha visto
            boolean vioTesis1SemestreAnterior = false;
            for (Tesis1 tesis1 : t) {
                //Revisar que haya aprobado tesis 1
                if (tesis1.getCalificacionTesis().equals(getConstanteBean().getConstante(Constantes.CTE_NOTA_APROBADA)) && tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS1_TERMINADA))) {
                    //revisar que el periodo no sea el mismo
                    String periodoAnterior;
                    String periodoActual = tesis2.getSemestreInicio().getPeriodo();
                    int anio = Integer.parseInt(periodoActual.substring(0, 4));
                    int semestre = Integer.parseInt(periodoActual.substring(4, 6));

                    if (semestre == 20) {
                        periodoAnterior = Integer.toString(anio) + "10";
                    } else {
                        periodoAnterior = Integer.toString(anio - 1) + "20";
                    }

                    //revisar que el periodo no sea el mismo
                    if (tesis1.getSemestreIniciacion().equals(tesis2.getSemestreInicio())) {
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00030, new ArrayList());
                    }
                    //revisar que el periodo te tesis1 sea el anterior al que se esta inscribiendo tesis2
                    if (!tesis1.getSemestreIniciacion().getPeriodo().equals(periodoAnterior)) {
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00045, new ArrayList());

                    }


                    //revisar que el periodo sea el anterior
                    puedeInscribir = true;
                    laAnterior = tesis1;
                    String periodoSiguienteATesis1 = obtenerSiguientePeriodo(tesis1.getSemestreIniciacion());
                    if (periodoSiguienteATesis1.equals(tesis2.getSemestreInicio().getPeriodo())) {
                        vioTesis1SemestreAnterior = true;
                        break;
                    }
                }
            }

            if (tesis2.getAsesor().getNivelPlanta() != null && tesis2.getAsesor().getNivelPlanta().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_INSTRUCTOR))) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00013, new ArrayList());
            }

            //si la fecha de terminación prevista está antes de la fecha máxima para solicitar o después de la fecha de sustentación, retorna error
            if (tesis2.getFechaPrevistaTerminacion().before(tesis2.getSemestreInicio().getFechaUltimaSolicitarTesis2())
                    || tesis2.getFechaPrevistaTerminacion().after(tesis2.getSemestreInicio().getFechaUltimaSustentarTesis2())) {
                //formatea la fecha máxima (sustentación) y mínima (solicitud) para retornarla como información para el error
                SimpleDateFormat sdfFechaMax = new SimpleDateFormat("dd/MM/yyyy");
                String strMinFecha = sdfFechaMax.format(tesis2.getSemestreInicio().getFechaUltimaSolicitarTesis2());
                String strMaxFecha = sdfFechaMax.format(tesis2.getSemestreInicio().getFechaUltimaSustentarTesis2());
                //construye el parámetro para el error
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParamMinFecha = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), strMinFecha);
                Secuencia secParamMaxFecha = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), strMaxFecha);
                //agrega los parámetros a la lista
                parametros.add(secParamMinFecha);
                parametros.add(secParamMaxFecha);
                //retorna
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00027, parametros);
            }

            if (puedeInscribir) {
                String nombreArchivo = tesis2.getRutaArchivoAdjuntoInicioTesis();
                String ruta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_TEXTO_PROPUESTA_TESIS2);
                boolean existeArchivo = conversor.comprobarExistenciaarchivo(ruta, nombreArchivo);

                //retorna error en caso de que no exista el archivo de propuesta
                if (!existeArchivo) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00021, new ArrayList());
                }

                if (laAnterior.getAsesor().getId().equals(tesis2.getAsesor().getId())) {
                    // ponerle estado, etc
                    tesis2.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
                    tesis2.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
                    tesis2.setEstadoHorario(getConstanteBean().getConstante(Constantes.CTE_HORARIO_PENDIENTE));
                    tesis2.setEstaEnPendienteEspecial(Boolean.FALSE);
                    tesis2Facade.create(tesis2);

                    crearTareaAsesorAprobarTesis2(tesis2, vioTesis1SemestreAnterior);
                    enviarCorreoAsesorAprobarTesis2(tesis2);
                    // crearle tarea al asesor :----------------------------------------------------------------
                    //4.1 notificacion:
                    //Crear alerta de inscripciones

                    //------------------------------------------------------------------------------------------
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } else {
                    //error, el asesor de tesis 1 no corresponde al asesor de tesis 2
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00010, new ArrayList());

                }
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00011, new ArrayList());
                /*msj; de que; no puede; inscribir por; que no; hay tesis1; en semestre anterior;
                (ERROR);*/
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void enviarCorreoAsesorAprobarTesis2(Tesis2 tesis) {
        Persona personaEstudiante = tesis.getEstudiante().getPersona();
        Persona personaAsesor = tesis.getAsesor().getPersona();
        String nombresEstudiante = personaEstudiante.getNombres() + " " + personaEstudiante.getApellidos();
        String nombresProfesor = personaAsesor.getNombres() + " " + personaAsesor.getApellidos();
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_INSCRIPCION_TESIS2_CORREO, nombresEstudiante);
        String mensaje = String.format(Notificaciones.MENSAJE_APROBAR_INSCRIPCION_TESIS2_CORREO, nombresProfesor, nombresEstudiante);
        correoBean.enviarMail(personaAsesor.getCorreo(), asunto, null, null, null, mensaje);
    }

    /**
     * metodo que crea una solicitud de ingreso a tesis2 con permisos de coordinacion(no verifica existencia tesis1 en periodo inmediatamente anterior)
     * @param xml
     * @return
     */
    public String crearSolicitudIngresoTesis2PermisosCoordinador(String xml) {
        try {

            parser.leerXML(xml);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Tesis2 tesis = conversor.pasarSecuenciaATesis2(secTesis);
            //String ruta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_EXTEMPORANEO_TESIS2);
            String ruta2 = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_TEXTO_PROPUESTA_TESIS2);
            if (tesis.getRutaArchivoAdjuntoInicioTesis() == null || tesis.getRutaArchivoAdjuntoInicioTesis().equals(" ")
                    || !conversor.comprobarExistenciaarchivo(ruta2, tesis.getRutaArchivoAdjuntoInicioTesis())) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00021, new ArrayList());
            }
            //comprob hay una tesis 1--------------------------------------------------------------------------------------------------------

            Collection<Tesis1> t = tesis1Facade.findByCorreoEstudiante(tesis.getEstudiante().getPersona().getCorreo());
            boolean hayTesis1 = false;
            for (Tesis1 tesis1 : t) {
                //Revisar que haya aprobado tesis 1
                if (tesis1.getCalificacionTesis().equals(getConstanteBean().getConstante(Constantes.CTE_NOTA_APROBADA)) && tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS1_TERMINADA))) {
                    //revisar que el periodo no sea el mismo
                    Long periodoTesis1 = Long.parseLong(tesis1.getSemestreIniciacion().getPeriodo().trim());
                    Long periodoTesis2 = Long.parseLong(tesis.getSemestreInicio().getPeriodo().trim());

                    if (periodoTesis1 >= periodoTesis2) {
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00030, new ArrayList());
                    }
                    hayTesis1 = true;
                }
            }
            if (!hayTesis1) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00030, new ArrayList());
            }
            //-------------------------------------------------------------------------------------------------------------------------------
            //no puede existir otra tesis en el mismo periodo
            String correoEst = tesis.getEstudiante().getPersona().getCorreo();
            Collection<Tesis2> tesises2 = tesis2Facade.findByCorreoEstudiante(correoEst);
            for (Tesis2 tesis2 : tesises2) {
                if (tesis2.getSemestreInicio().getPeriodo().equals(tesis.getSemestreInicio().getPeriodo())) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00016, new ArrayList());
                }
            }

            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
            tesis.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.TRUE));//getConstanteBean().getConstante(Constantes.TRUE))
            tesis.setEstadoHorario(getConstanteBean().getConstante(Constantes.CTE_HORARIO_PENDIENTE));
            tesis.setEstaEnPendienteEspecial(Boolean.FALSE);
            tesis2Facade.create(tesis);
            enviarCorreoAprobadoExtemporaneo(tesis);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_TESIS_2_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo llamado cuando el aseosr de tesis acepta o rechaza una solicitud de tesis
     * @param xml
     * @return
     */
    public String aprobarSolicitudTesis2Asesor(String xml) {
        //------------------------------------------------------------------------------------------------------
        try {
            parser.leerXML(xml);
            Secuencia secIdGeneral = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secAprobadoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR));
            Secuencia secCorreoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secIdGeneral != null && secAprobadoAsesor != null && secCorreoAsesor != null) {
                Long id = Long.parseLong(secIdGeneral.getValor().trim());
                String valor = secAprobadoAsesor.getValor().trim();
                String correo = secCorreoAsesor.getValor().trim();
                Tesis2 tesis = tesis2Facade.find(id);
                if (tesis != null) {
                    if (tesis.getAsesor().getPersona().getCorreo().equals(correo)) {
                        //aca meter logica....--------------------------------------------
                        //--------terminar tarea coordinador subarea------------------------------------------
                        Properties propiedades = new Properties();
                        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR), propiedades);
                        //FIN--------terminar tarea coordinador subarea------------------------------------------
                        PeriodoTesis pt = periodoFacadelocal.findByPeriodo(tesis.getSemestreInicio().getPeriodo());
                        if (pt.getMaxFechaAprobacionTesis2().before(new Date())) {
                            String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS2);
                            String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_APROBAR_SOLICITUD_TESIS_2_ASESOR);
                            String login = tesis.getAsesor().getPersona().getCorreo();
                            String infoAdicional = "La solicitud de tesis 2 del estudiante " + tesis.getEstudiante().getPersona().getCorreo() + " fue " + (valor.equals(getConstanteBean().getConstante(Constantes.TRUE)) ? "aprobada" : "reprobada");
                            reportarEntregaTardia(pt.getMaxFechaAprobacionTesis2(), comando, accion, login, infoAdicional);
                        }

                        if (valor.equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                            tesis.setAprobadoAsesor(valor);
                            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_INSCRIPCION_BANNER));
                            tesis2Facade.edit(tesis);
                            //Crear correo para informar que se aprobó la inscripción
                            String asunto = Notificaciones.ASUNTO_APROBACION_INSCRIPCION_TESIS_2;
                            String mensaje = Notificaciones.MENSAJE_APROBACION_INSCRIPCION_TESIS_2;

                            mensaje = mensaje.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                            mensaje = mensaje.replaceFirst("%", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());

                            correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                            //Correo informando a coordinación de maestría
                            String asuntoCoord = Notificaciones.ASUNTO_APROBACION_INSCRIPCION_TESIS_2_COORDINACION;
                            asuntoCoord = asuntoCoord.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                            String mensajeCoord = Notificaciones.MENSAJE_APROBACION_INSCRIPCION_TESIS_2_COORDINACION;
                            mensajeCoord = mensajeCoord.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                            mensajeCoord = mensajeCoord.replaceFirst("%2", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());

                            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCoord, null, null, null, mensajeCoord);
                            crearTareaPendienteIncripcionBanner(tesis);
                        } else if (valor.equals(getConstanteBean().getConstante(Constantes.FALSE))) {
                            tesis.setAprobadoAsesor(valor);
                            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR));
                            tesis2Facade.edit(tesis);
                            migrarTesisRechazada(tesis);
                            //Crear correo para informar que se rechazó la inscripción
                            String asuntoCreacion = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_TESIS_2;
                            String mensajeCreacion = Notificaciones.MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_TESIS_2;

                            mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());

                            String correoCordinacion = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);

                            correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoCreacion, tesis.getAsesor().getPersona().getCorreo(), correoCordinacion, null, mensajeCreacion);
                            //pasarSolicitudTesis2ARechazadas(tesis);
                            //FIN-------------Crear correo para informar que se rechazo la inscripción
                        } //------------------------------------------------------------------
                    } else {
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0007, new ArrayList());

                    }
                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                }
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

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
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_TESIS1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public void crearTareaPendienteIncripcionBanner(Tesis2 tesis) {

        String nombreV = "Aprobar Inscripción de Tesis 2";// + carga.getId();
        String descripcion = "Se debe realizar la inscripción en Banner  de tesis 2 para el estudiante: " + tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos() + " (" + tesis.getEstudiante().getPersona().getCorreo() + ")";
        Long undia = 1000L * 60L * 60L * 24L;

        /*
         * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

        String tipo = getConstanteBean().getConstante(Constantes.TAREA_INSCRIBIR_TESIS2_BANNER_POR_COODINACION);
        Persona profesor = tesis.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Timestamp fInicio = new Timestamp(fechaInicioDate.getTime());
        Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
        if (tesis.getSemestreInicio().getMaxFechaAprobacionTesis2() != null) {
            fechaFinDate = new Date(tesis.getSemestreInicio().getMaxFechaAprobacionTesis2().getTime() + undia);
        }
        String mensajeBulletTarea = String.format(Notificaciones.MENSAJE_BULLET_INSCRIPCION_BANNER_TESIS2_COORDINACION, tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos(), tesis.getEstudiante().getPersona().getCorreo(), profesor.getNombres() + " " + profesor.getApellidos(), profesor.getCorreo());
        boolean agrupable = true;
        String header = Notificaciones.MENSAJE_HEADER_INSCRIPCION_BANNER_TESIS2_COORDINACION;
        String footer = Notificaciones.MENSAJE_FOOTER_INSCRIPCION_BANNER_TESIS2_COORDINACION;
        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS2_PARA_COORDINACION);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));
        String rol = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_BANNER_TESIS2_COORDINACION;
        tareaBean.crearTareaRol(mensajeBulletTarea, tipo, rol, agrupable, header, footer, fInicio, fFin, comando, parametros, asunto);
    }

    /**
     * Devuelbe las solicitudes de tesis asignadas a un asesor
     * @param xml
     * @return
     */
    public String darSolicitudesTesis2Asesor(String xml) {
        try {
            /**
             * consulta:
             * <correo>correo del asesor</correo>
             */
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));


            if (secCorreo != null && secCorreo.getValor() != null) {
                String correo = secCorreo.getValor();
                Collection<Tesis2> tesises = tesis2Facade.findByCorreoAsesor(correo);

                Secuencia secTesises = conversor.pasarTesises2ASecuencia(tesises);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secTesises);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS_2_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                // xml mal formado
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS_2_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS_2_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public String confirmarTesis2EnBanner(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null) {
                Long idTesis = Long.parseLong(secId.getValor().trim());
                Tesis2 tesis = tesis2Facade.find(idTesis);
                if (tesis != null) {

                    Secuencia secEstadoTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));
                    String estado = secEstadoTesis.getValor();
                    tesis.setEstadoTesis(estado);
                    tesis2Facade.edit(tesis);

                    //--------terminar tarea coordinador ------------------------------------------
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), idTesis.toString());
                    completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAREA_INSCRIBIR_TESIS2_BANNER_POR_COODINACION), propiedades);
                    //FIN--------terminar tarea coordinador ------------------------------------------

                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                } else {
                    //tirar error
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                }
            } else {
                //tirar exception xml mal formado
                throw new Exception("xml mal formado en agregarComentarioTesis1 : TesisBean ");
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_INSCRIPCION_BANNER_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * asigna una calificacion a la tesis de un estudiante
     * @param xml
     * @return
     */
    public String darUbicacionHoraTesis2(String xml) {
        try {

            ConversorTesisMaestria conversor = new ConversorTesisMaestria();
            getParser().leerXML(xml);
            String periodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();
            Collection<Tesis2> tesis2 = tesis2Facade.findDetallesSustentacionByPeriodoEstado(periodo);
            if (tesis2.size() == 0) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_UBICACION_HORA_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0080, new LinkedList<Secuencia>());
            } else {
                Secuencia secTesises = conversor.pasarTesises2ASecuenciaMinima(tesis2);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secTesises);

                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_UBICACION_HORA_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0150, new LinkedList<Secuencia>());
                //sube en la secuencia en forma de XML
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_UBICACION_HORA_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MSJ_0151, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Devuelve detalles de las sustentaciones de Tesis 2 ya asignadas
     * @param xml
     * @return
     */
    public String colocarNotaTesis2(String xml) {
        /**
         * consulta:
         * <idGeneral>1</idGeneral>
         * <calificacion>asdf</calificacion>
         * <correo>correo del asesor</correo>
         *
         */
        try {
            parser.leerXML(xml);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Secuencia secIdTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            if (secIdTesis != null) {

                Long id = Long.parseLong(secIdTesis.getValor().trim());
                Tesis2 tesis = tesis2Facade.find(id);
                if (tesis != null) {
//                    pendiente

                    if (!tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_ESPERANDO_NOTA_FINAL_TESIS_2))) {
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00022, new ArrayList());
                    }

                    String ruta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS2);
                    String nombreArchivo = tesis.getRutaArticuloTesis();



                    if (conversor.comprobarExistenciaarchivo(ruta, nombreArchivo)) {
                        //colocar nota
                        Secuencia secNotaTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
                        Double d = Double.parseDouble(secNotaTesis.getValor().trim());
                        tesis.setRutaArticuloTesis(ruta + nombreArchivo);
                        tesis.setCalificacion(d);
                        tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS2_TERMINADA));

                        tesis2Facade.edit(tesis);
                        //: enviar correo estudiante informando nota de tesis
                        String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_TESIS_2;
                        String mensajeCreacion = Notificaciones.MENSAJE_CALIFICACION_TESIS_2;
                        mensajeCreacion = mensajeCreacion.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeCreacion = mensajeCreacion.replaceFirst("%2", secNotaTesis.getValor());
                        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                        //: enviar correo asesor informando nota de tesis
                        String asuntoCreacionAsesor = Notificaciones.ASUNTO_CALIFICACION_TESIS_2_ASESOR;
                        asuntoCreacionAsesor = asuntoCreacionAsesor.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajeCreacionAsesor = Notificaciones.MENSAJE_CALIFICACION_TESIS_2_ASESOR;
                        mensajeCreacionAsesor = mensajeCreacionAsesor.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeCreacionAsesor = mensajeCreacionAsesor.replaceFirst("%2", secNotaTesis.getValor());
                        correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoCreacionAsesor, null, null, null, mensajeCreacionAsesor);
                        //
                        crearTimerDeMigracionDeTesisFinalizada(id);
                        completarTareaCoordinacionSubirNotaTesis2(tesis);
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                    } else {
                        //devolver error no se encontro archivo=> no se puede colocar nota
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0009, new ArrayList());
                    }
                }
                //enviar error tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            } //xml mal formado COM_ERR_0003
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que modifica los jurados seleccionados para una tesis
     * @param xml
     * @return
     */
    public String modificarJuradosTesis2(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Tesis2 tesisWeb = conversor.pasarSecuenciaATesis2(secTesis);
            Tesis2 tesisBD = tesis2Facade.find(tesisWeb.getId());
            String rol = getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_ROL));//parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ROL)).getValor();

            Long maxCoasesores = Long.parseLong(getConstanteBean().getConstante(Constantes.CTE_TESIS_MAXIMO_NUM_COASESORES));
            if (tesisWeb.getCoasesor().size() > maxCoasesores) {
                //aca lanzar error diciendo que se exedio el maximo de coasesores
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_COASESORES_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00037, new ArrayList());
            }

            //si la tesis esta en curso, o si el rol es coordinador y la tesis esta en curso o esperando nota
            if ((tesisBD.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO)))
                    || ((tesisBD.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO)) || tesisBD.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_ESPERANDO_NOTA_FINAL_TESIS_2))) && rol.equals("coordinacion"))) {

                // Revisar que no hayan conflictos (un profesor con mas de un rol (asesor,jurado,co-asesor, etc)
                HashMap<String, Boolean> agregados = new HashMap<String, Boolean>();
                String correoAsesor = tesisBD.getAsesor().getPersona().getCorreo();
                agregados.put(correoAsesor, Boolean.TRUE);
                Collection<Coasesor> coasesores = tesisWeb.getCoasesor();
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                for (Coasesor coasesor : coasesores) {
                    String correoCoasesor = coasesor.getCorreo();
                    if (agregados.containsKey(correoCoasesor)) {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), "coasesores");
                        //agrega el parámetro a la lista
                        parametros.add(secParametro);
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_COASESORES_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00043, parametros);
                    } else {
                        agregados.put(correoCoasesor, Boolean.TRUE);
                    }
                }
                Collection<Profesor> jurados = tesisWeb.getJuradoTesis();
                if (jurados != null) {
                    for (Profesor jurado : jurados) {
                        String correoJurado = jurado.getPersona().getCorreo();
                        if (agregados.containsKey(correoJurado)) {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), "jurados");
                            //agrega el parámetro a la lista
                            parametros.add(secParametro);
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_COASESORES_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00043, parametros);
                        } else {
                            agregados.put(correoJurado, Boolean.TRUE);
                        }
                    }
                }
                Collection<JuradoExternoUniversidad> juradosExt = tesisWeb.getJurados();
                if (juradosExt != null) {
                    for (JuradoExternoUniversidad juradoExt : juradosExt) {
                        String correoJurado = juradoExt.getCorreo();
                        if (agregados.containsKey(correoJurado)) {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), "jurados externos");
                            //agrega el parámetro a la lista
                            parametros.add(secParametro);
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_COASESORES_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00043, parametros);
                        } else {
                            agregados.put(correoJurado, Boolean.TRUE);
                        }
                    }
                }
                //-----------------------------
                /*
                 * si no estan ya en la tesis los agrega si ya estan no hace nada (codigo no util pero probando que sirva)
                 */
                //------------------------------
                tesisBD.setJuradoTesis(new ArrayList<Profesor>());

                Collection<Profesor> losNuevosJuradosInternos = new ArrayList<Profesor>();
                //si existen jurados de web entonces los coloca como internos
                Collection<Profesor> profesoresJurados = tesisWeb.getJuradoTesis();
                if (profesoresJurados != null) {
                    for (Profesor profesor : profesoresJurados) {
                        losNuevosJuradosInternos.add(profesorFacade.findByCorreo(profesor.getPersona().getCorreo()));
                    }
                }
                tesisBD.setJuradoTesis(losNuevosJuradosInternos);
                tesis2Facade.edit(tesisBD);
                //mirar si existen en bD si no crearlos...----------------------
                Collection<JuradoExternoUniversidad> juradosExerni = tesisWeb.getJurados();
                if (juradosExerni != null) {
                    Collection<JuradoExternoUniversidad> juradosExternosDeVerdad = new ArrayList<JuradoExternoUniversidad>();
                    for (JuradoExternoUniversidad juradoExternoUniversidad : juradosExerni) {
                        //si no existe en BD toca crearlo:

                        JuradoExternoUniversidad enBDJurado = juradoFacade.findByCorreo(juradoExternoUniversidad.getCorreo());
                        if (enBDJurado != null) {
                            juradosExternosDeVerdad.add(enBDJurado);
                        } else {
                            juradoFacade.create(juradoExternoUniversidad);
                            enBDJurado = juradoFacade.findByCorreo(juradoExternoUniversidad.getCorreo());
                            juradosExternosDeVerdad.add(enBDJurado);
                        }
                    }
                    tesisBD.setJurados(juradosExternosDeVerdad);
                    tesis2Facade.edit(tesisBD);
                }
                //-------------------------------------------------------------------------------
                //Collection<Coasesor> coasesores = tesisWeb.getCoasesor();
                Collection<Coasesor> coasesoresAGuardar = new ArrayList<Coasesor>();
                for (Coasesor coasesor : coasesores) {
                    //busca a los coasesores por correo en la BD
                    Coasesor a = coasesorFacade.findByCorreo(coasesor.getCorreo());
                    if (a == null) {
                        coasesorFacade.create(coasesor);
                        a = coasesorFacade.findByCorreo(coasesor.getCorreo());
                    } //lo agrega a la lista
                    else {
                        coasesor.setId(a.getId());

                        coasesorFacade.edit(coasesor);
                    }
                    coasesoresAGuardar.add(a);
                }
                // asiganarlos a la tesis:
                tesisBD.setCoasesor(coasesoresAGuardar);
                tesis2Facade.edit(tesisBD);

                //-------------------------------------------------------------------------------
                //-------------------------------------------------------------------------------
                //Si la tesis esta en estado de esperando nota final y se elimino un jurado, se debe eliminar tambien la
                //calificacion_jurado generada para ese jurado que se esta eliminando. Si por el contrario se esta agregando
                //un nuevo jurado, se debe generar una calificacion_jurado para que el jurado pueda calificar la tesis


                //Si la tesis tiene estado de esperando por nota final y el que esta realizando la modificacion es el rol coordinador
                if (tesisBD.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_ESPERANDO_NOTA_FINAL_TESIS_2)) && rol.equals("coordinacion")) {

                    //-----------Si se elimino un jurado
                    Collection<CalificacionJurado> calificaciones = new ArrayList<CalificacionJurado>();
                    ArrayList<CalificacionJurado> calificacionesDeVerdad = new ArrayList<CalificacionJurado>();
                    if (tesisBD.getCalificacionesJurados() != null) {
                        calificaciones = tesisBD.getCalificacionesJurados();
                    }

                    //Se busca que la calificacion este asociada a algun jurado
                    for (CalificacionJurado calificacionJurado : calificaciones) {
                        //bandera de si se encuentra
                        boolean esta = false;

                        if (calificacionJurado.getJuradoInterno() != null) {
                            if (tesisBD.getJuradoTesis().contains(calificacionJurado.getJuradoInterno()) || tesisBD.getAsesor().equals(calificacionJurado.getJuradoInterno())) {
                                esta = true;
                            }
                        }
                        if (calificacionJurado.getJuradoExterno() != null) {
                            if (tesisBD.getJurados().contains(calificacionJurado.getJuradoExterno())) {
                                esta = true;
                            }
                        }
                        if (calificacionJurado.getCoasesor() != null) {
                            if (tesisBD.getCoasesor().contains(calificacionJurado.getCoasesor())) {
                                esta = true;
                            }
                        }

                        //Si se encuentra se agrega a la lista de calificaciones de verdad, si no se encuentra es porque no existe
                        //ningun jurado asociado, por lo que no se tiene en cuenta (se elimina)
                        if (esta) {
                            calificacionesDeVerdad.add(calificacionJurado);
                        }

                    }
                    tesisBD.setCalificacionesJurados(calificacionesDeVerdad);
                    tesis2Facade.edit(tesisBD);

                    //------------Si se agrego un nuevo jurado verifica que todos tengan una calificacion asociada y si no se agrega
                    Collection<CalificacionJurado> nuevasCalificaciones = tesisBD.getCalificacionesJurados();

                    //Para el asesor
                    //si no encontro la calificacion para el asesor le crea una
                    boolean esta = false;
                    //busca la calificacion del asesor
                    for (CalificacionJurado calificacion : tesisBD.getCalificacionesJurados()) {
                        if (calificacion.getJuradoInterno() != null && calificacion.getJuradoInterno().equals(tesisBD.getAsesor())) {
                            esta = true;
                        }
                    }
                    if (!esta) {
                        CalificacionJurado calJurado = crearTareaCalificacionJuradoAsesor(tesisBD);
                        nuevasCalificaciones.add(calJurado);
                    }
                    //para jurado Interno
                    for (Profesor profesor : tesisBD.getJuradoTesis()) {
                        esta = false;
                        //busca la calificacion del jurado interno
                        for (CalificacionJurado calificacion : tesisBD.getCalificacionesJurados()) {
                            if (calificacion.getJuradoInterno() != null && calificacion.getJuradoInterno().equals(profesor)) {
                                esta = true;
                            }
                        }
                        //si no encontro la calificacion para el jurado interno le crea una
                        if (!esta) {
                            CalificacionJurado calJurado = crearTareaCalificacionJuradoInterno(tesisBD, profesor);
                            nuevasCalificaciones.add(calJurado);
                        }
                    }
                    //para jurado externo
                    for (JuradoExternoUniversidad juradoExterno : tesisBD.getJurados()) {
                        esta = false;
                        //busca la calificacion del jurado externo
                        for (CalificacionJurado calificacion : tesisBD.getCalificacionesJurados()) {
                            if (calificacion.getJuradoExterno() != null && calificacion.getJuradoExterno().equals(juradoExterno)) {
                                esta = true;
                            }
                        }
                        //si no encontro la calificacion para el jurado externo le crea una
                        if (!esta) {
                            CalificacionJurado calJurado = crearTareaCalificacionJuradoExterno(juradoExterno, tesisBD);
                            nuevasCalificaciones.add(calJurado);
                        }
                    }
                    //para coasesor
                    for (Coasesor coasesor : tesisBD.getCoasesor()) {
                        esta = false;
                        //busca la calificacion del coasesor
                        for (CalificacionJurado calificacion : tesisBD.getCalificacionesJurados()) {
                            if (calificacion.getCoasesor() != null && calificacion.getCoasesor().equals(coasesor)) {
                                esta = true;
                            }
                        }
                        //si no encontro la calificacion para el coasesor le crea una
                        if (!esta) {
                            CalificacionJurado calJurado = crearTareaCalificacionJuradoCoasesor(coasesor, tesisBD);
                            nuevasCalificaciones.add(calJurado);
                        }
                    }
                    tesisBD.setCalificacionesJurados(nuevasCalificaciones);
                    tesis2Facade.edit(tesisBD);
                }


                //-------------------------------------------------------------------------------
                secuencias = new ArrayList<Secuencia>();
                Persona persona = tesisBD.getEstudiante().getPersona();
                SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd");
                //avisar a asesor y estudiante cambio en jurado...----------------------------------------------------------------------------
                String asuntoCreacion = Notificaciones.ASUNTO_CAMBIO_JURADOS_SUSTENTACION;
                String mensajeCreacion = Notificaciones.MENSAJE_CAMBIO_JURADOS_SUSTENTACION;
                mensajeCreacion = mensajeCreacion.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
                mensajeCreacion = mensajeCreacion.replaceFirst("%", tesisBD.getSemestreInicio().getPeriodo());
                if (tesisBD.getEstaEnPendienteEspecial() == null || !tesisBD.getEstaEnPendienteEspecial()) {
                    mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(tesisBD.getSemestreInicio().getFechaUltimaSustentarTesis2()));
                } else {
                    if (tesisBD.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial() != null) {
                        mensajeCreacion = mensajeCreacion.replaceFirst("%", sdfHMS.format(tesisBD.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial()));
                    } else {
                        mensajeCreacion = mensajeCreacion.replaceFirst("%", " Por Definir ");
                    }
                }
                correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, tesisBD.getAsesor().getPersona().getCorreo(), null, null, mensajeCreacion);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_JURADO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                //:enviar error la tesis no esta en curso!
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_JURADO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00012, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_JURADO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que devuelve una tesis dado un id
     * @param xml
     * @return
     */
    public String darDetallesTesis2(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secCorreoEstudiante = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secIdTesis != null) {
                Long idTesis = Long.parseLong(secIdTesis.getValor());
                Tesis2 tesis = tesis2Facade.find(idTesis);
                juradoTesisFacade.calcularNotaSustencion(tesis);

                Secuencia secTesis2 = conversor.pasarTesis2ASecuencia(tesis);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secTesis2);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TESIS2_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            }
            if (secCorreoEstudiante != null) {
                String correoEstud = secCorreoEstudiante.getValor();
                Collection<Tesis2> tesises = tesis2Facade.findByCorreoEstudiante(correoEstud);
                Tesis2 laUltima = null;
                for (Tesis2 tesis2 : tesises) {
                    if (laUltima == null || tesis2.getFecha().after(laUltima.getFecha())) {
                        laUltima = tesis2;
                    }
                }
                if (laUltima != null) {
                    juradoTesisFacade.calcularNotaSustencion(laUltima);
                }
                Secuencia secTesis2 = conversor.pasarTesis2ASecuencia(laUltima);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secTesis2);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TESIS2_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                throw new Exception("XML mal Formado en darDetallesTesis2- TesisBean");
            }

        } catch (Exception e) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, e);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TESIS2_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo que devuelve una tesis dado un id
     * @param xml
     * @return
     */
    public String darDetallesUbicacionHoraTesis2(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            if (secIdTesis != null) {

                Long idTesis = Long.parseLong(secIdTesis.getValor());

                Tesis2 tesis = tesis2Facade.find(idTesis);
                Secuencia secTesis2 = conversor.pasarTesis2ASecuencia(tesis);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

                secuencias.add(secTesis2);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DETALLES_UBICACION_HORA_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                throw new Exception("XML mal Formado en darDetallesTesis2- TesisBean");
            }

        } catch (Exception e) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, e);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TESIS2_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * metodo llamado por el estudiante cuando cambia el horario de sustentacion de tesis 2
     * @param xml asdf
     * @return
     * REFACTOR;
     */
    public String guardarHorarioSustentacionTesis2(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTesis2 = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Long id = Long.parseLong(secTesis2.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor());
            Tesis2 tesis2 = tesis2Facade.find(id);
            Secuencia secHorario = secTesis2.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO_SUSTENTACION));
            HorarioSustentacionTesis horario = conversor.pasarSecuenciaAHorario(secHorario);
            //en caso de que el estado de la tesis no sea el esperado se envia error
            if (!tesis2.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO))) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00034, new ArrayList());
            }
            //REVISAR REGLAS DEL NEGOCIO:::
            //fecha <3dias && si fecha.skype0true=> fecha<2semanas, y conferencia = 2 semanas
            if (tesis2.getEstaEnPendienteEspecial() == null || !tesis2.getEstaEnPendienteEspecial()) {
                if (horario.getFechaSustentacion().before(tesis2.getSemestreInicio().getFechaUltimaSustentarTesis2()) || horario.getFechaSustentacion().equals(tesis2.getSemestreInicio().getFechaUltimaSustentarTesis2())) {
                    //revisar skype o videoconferencia
                    if (horario.getVideoConferenciaSkyPe().equals(getConstanteBean().getConstante(Constantes.TRUE)) || horario.getVideoConferencia().equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                        //1seg*60minuto*24horas*7dias*2semanas
                        Long dosSemanas = Long.parseLong("1") * 1000 * 60 * 60 * 24 * 7 * 2;
                        Long semanasAntes = horario.getFechaSustentacion().getTime() - dosSemanas;
                        Timestamp fecha2semanasAntesSustentacion = new Timestamp(semanasAntes);
                        if (fecha2semanasAntesSustentacion.after(horario.getFechaSustentacion())) {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
                        } else {
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());

                            completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2), propiedades);
                            actualizarHorarioSalonSustentacion(tesis2, horario);
                            //return ok:
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                        }
                    } else {
                        actualizarHorarioSalonSustentacion(tesis2, horario);
                        //  eliminar la tearea
                        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2);
                        Properties propiedades = new Properties();
                        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
                        completarTareaSencilla(tipo, propiedades);

                        //return ok:
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                    }
                } else {
                    //formatea la fecha para retornarla como información para el error
                    SimpleDateFormat sdfFechaMax = new SimpleDateFormat("dd/MM/yyyy");
                    String strMaxFecha = sdfFechaMax.format(tesis2.getSemestreInicio().getFechaUltimaSustentarTesis2());
                    //construye el parámetro para el error
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), strMaxFecha);
                    //agrega el parámetro a la lista
                    parametros.add(secParametro);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00029, parametros);
                }
            } else //esta en pendiente especial misma logica diferentes fechas:
            {

                if (horario.getFechaSustentacion().before(tesis2.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial()) || horario.getFechaSustentacion().equals(tesis2.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial())) {
                    //revisar skype o videoconferencia
                    if (horario.getVideoConferenciaSkyPe().equals(getConstanteBean().getConstante(Constantes.TRUE)) || horario.getVideoConferencia().equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                        //1seg*60minuto*24horas*7dias*2semanas
                        Long dosSemanas = Long.parseLong("1") * 1000 * 60 * 60 * 24 * 7 * 2;
                        Long semanasAntes = horario.getFechaSustentacion().getTime() - dosSemanas;
                        Timestamp fecha2semanasAntesSustentacion = new Timestamp(semanasAntes);
                        if (fecha2semanasAntesSustentacion.after(horario.getFechaSustentacion())) {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
                        } else {
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
                            completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2), propiedades);
                            actualizarHorarioSalonSustentacion(tesis2, horario);
                            //return ok:
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                        }
                    } else {
                        actualizarHorarioSalonSustentacion(tesis2, horario);
                        //  eliminar la tearea
                        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2);
                        Properties propiedades = new Properties();
                        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
                        completarTareaSencilla(tipo, propiedades);

                        //return ok:
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                    }
                } else {
                    //formatea la fecha para retornarla como información para el error
                    SimpleDateFormat sdfFechaMax = new SimpleDateFormat("dd/MM/yyyy");
                    String strMaxFecha = sdfFechaMax.format(tesis2.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial());
                    //construye el parámetro para el error
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), strMaxFecha);
                    //agrega el parámetro a la lista
                    parametros.add(secParametro);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00029, parametros);
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darTesisConHorarioSustentacion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secPeriodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
            if (secPeriodo != null) {
                Collection<Tesis2> tesises = tesis2Facade.findByHorarioSustentacion(secPeriodo.getValor());
                Secuencia sec = conversor.pasarTesises2ASecuencia(tesises);
                ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                param.add(sec);
                return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_BUSCAR__TESIS_2_POR_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                throw new Exception("xml mal formado:darTesisConHorarioSustentacion!TesisBean ");
            }
        } catch (Exception ex) {

            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_BUSCAR__TESIS_2_POR_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darTodasLasSolicitudesTesis2(String xml) {
        try {
            parser.leerXML(xml);
            Collection<Tesis2> tesises = tesis2Facade.findAllOrderBySemestre();
            Secuencia secSoli = conversor.pasarTesises2ASecuenciaLigera(tesises);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darTesis2AMigrar(String xml) {
        try {
            parser.leerXML(xml);

            Collection<Tesis2> tesises = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_2_RETIRADA));
            Collection<Tesis2> tesisesP = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
            Collection<Tesis2> tesisesT = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS2_TERMINADA));
            //Collection<Tesis2> tesisesR = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR));

            tesises.addAll(tesisesP);
            tesises.addAll(tesisesT);
            //tesises.addAll(tesisesR);

            Secuencia secSoli = conversor.pasarTesises2ASecuenciaLigera(tesises);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_2_A_MIGRAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TODAS_LAS_TESIS_2_A_MIGRAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String comportamientoEmergenciaMigrarTesis2(String xml) {
        try {
            String retorno = null;
            Boolean migracionExitosa = null;
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null && secId.getValor() != null) {
                Long idTesis = Long.parseLong(secId.getValor());
                Tesis2 tesis = tesis2Facade.find(idTesis);

                System.out.println("EN TESIS2BEAN ENTRO CON TESIS ID:\n" + idTesis);
                if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_2_RETIRADA))) {
                    System.out.println("EN TESIS2BEAN ENTRO A migrarTesisRetirada");
                    migracionExitosa = migrarTesisRetirada(tesis.getId());
                } else if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA))) {
                    System.out.println("EN TESIS2BEAN ENTRO A migrarTesisPerdida");
                    migracionExitosa = migrarTesisPerdida(tesis.getId());
                } else if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS2_TERMINADA))) {
                    System.out.println("EN TESIS2BEAN ENTRO A migrarTesisTerminada");
                    migracionExitosa = migrarTesisTerminada(tesis.getId());
                }

                if (migracionExitosa == false) {
                    retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00042, new LinkedList<Secuencia>());
                    return retorno;
                }

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                throw new Exception("Tesis2Bean - comportamientoEmergenciaMigrarTesis2");
            }
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public String subirArticuloFinalizacionTesis2(String xml) {
        try {
            /*
             * xml:
             * /**
             * consulta:
             * <idGeneral>1</idGeneral>         *
             * <rutaArchivoFinal>/u2/dsfsdfsdffsd/sda/sdf/sdf/sdfa</rutaArchivoFinal>
             */
            parser.leerXML(xml);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Secuencia secId = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secNombreArchivo = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARTICULO_FIN_TESIS_2));
            if (secId == null || secNombreArchivo == null) {
                throw new Exception("xml mal formado en:subirArticuloFinalizacionTesis2|TesisBean ");
            }
            String ruta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS2);
            String nombreArchivo = secNombreArchivo.getValor();

            if (conversor.comprobarExistenciaarchivo(ruta, nombreArchivo)) {
                Long idTesis = Long.parseLong(secId.getValor());

                Tesis2 tesis = tesis2Facade.find(idTesis);
                if (tesis == null) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARTICULO_TESIS_2_FINAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00023, new ArrayList());
                }

                tesis.setRutaArticuloTesis(nombreArchivo);
                tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_ESPERANDO_NOTA_FINAL_TESIS_2));
                tesis2Facade.edit(tesis);

                //quitar tarea
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARTICULO_TESIS_2);
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
                completarTareaSencilla(tipo, propiedades);

                /*
                 * String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2);
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
                Collection<Tarea> tareas = tareaFacade.darTareasEstadoTipoYParametros(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE), tipo, propiedades);
                for (Tarea tarea : tareas) {
                tareaBean.realizarTarea(tarea.getId());
                break;
                }
                 */

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARTICULO_TESIS_2_FINAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                //devolver error no se encontro archivo=> no se puede colocar nota
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0009, new ArrayList());

            }
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARTICULO_TESIS_2_FINAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ParserT getParser() {
        return parser;
    }

    /**
     * metodo que dado un periodo busca el siguiente
     * @param semestreIniciacion: semetre del que desea conocer el siguiente
     * @return nombre del periodo
     */
    public String obtenerSiguientePeriodo(PeriodoTesis semestreIniciacion) {

        String[] periodods = darPeriodosCercanos();

        for (int i = 0; i < periodods.length; i++) {
            String string = periodods[i];
            if (string.equals(semestreIniciacion.getPeriodo())) {
                //hay más períodos disponibles, que no sean mitad de semestre (*19)?
                if (i + 1 < periodods.length) {
                    String siguientePeriodo = periodods[i + 1];
                    if (!siguientePeriodo.endsWith("19")) {
                        return siguientePeriodo;
                    }
                }
                //hay más períodos disponibles, que no sean mitad de semestre (*19)?
                if (i + 2 < periodods.length) {
                    String siguientePeriodo = periodods[i + 2];
                    if (!siguientePeriodo.endsWith("19")) {
                        return siguientePeriodo;
                    }
                }
            }
        }
        return null;
    }

    /**
     * metodo que devuelve 3 semestres al rededor del actual (3 para atras y 3 para adelante)
     * @param xml
     * @return XML:String
     * No sse usa TODO:borrar
     */
    @Deprecated
    public String darSemestresCercanos(String xml) {
        return null;
    }

    private String[] darPeriodosCercanos() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String periodoA = sdf.format(d);
        Integer anio = new Integer(periodoA.split("-")[0]);
        Integer mes = new Integer(periodoA.split("-")[1]);
        String[] periodos = new String[15];

        periodos[0] = anio - 2 + "10";
        periodos[1] = anio - 2 + "19";
        periodos[2] = anio - 2 + "20";

        periodos[3] = anio - 1 + "10";
        periodos[4] = anio - 1 + "19";
        periodos[5] = anio - 1 + "20";

        periodos[6] = anio + "10";
        periodos[7] = anio + "19";
        periodos[8] = anio + "20";

        periodos[9] = anio + 1 + "10";
        periodos[10] = anio + 1 + "19";
        periodos[11] = anio + 1 + "20";

        periodos[12] = anio + 2 + "10";
        periodos[13] = anio + 2 + "19";
        periodos[14] = anio + 2 + "20";

        return periodos;


    }

    public void crearTimerDeMigracionDeTesisFinalizada(Long idTesis2) {
        Date d = new Date();
        Long fechaActual = d.getTime();
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        Long unaSemana = undia * 7;
        Long unMes = unaSemana * 4;
        Long fechaF = unMes * 8 + fechaActual;
        String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_CUMPLIDA_A_HISTORICOS) + "-" + idTesis2;
        //timerGenerico.crearTimer(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(fechaF), mensajeAsociado);
    }

    /**
     * metodo que migra a historicos una tesis2 rechazada
     * @param tesis
     */
    /*public void pasarSolicitudTesis2ARechazadas(Tesis2 tesis) {
    // asdfsdfa
    //eliminar el archivo del servidor
    //colocar la feha de terminacion como hoy
    //:IMPLEMENTAR
    try {
    String resp = historicoTesis.migrarTesis2Rechazadas("");
    parser.leerRespuesta(resp);
    Secuencia secEstado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));
    if (secEstado != null && secEstado.getValor().equals(getConstanteBean().getConstante(Constantes.TRUE))) {
    Collection<Tesis2> tesises = tesis2Facade.findAll();
    for (Tesis2 tesis1 : tesises) {
    if (tesis1.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR))) {
    tesis2Facade.remove(tesis1);
    }
    }
    return;
    }
    } catch (Exception ex) {
    Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
     * 
     */
    private void eliminarTimerAnteriorSustentacionTesis2(Long idTesis2) {
        String linea = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ARCHIVOS_JURADOS_SUSTENTACION_TESIS_2) + "-" + idTesis2.toString();
        timerGenerico.eliminarTimerPorParametroExterno(linea);
        String linea2 = getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_ESTADO_SUSTENTACION_TESIS_2_A_ESPERAR_ARTICULO) + "-" + idTesis2.toString();
        timerGenerico.eliminarTimerPorParametroExterno(linea2);
    }

    /*
     * Metodo que retorna true si el tipo de cambio es diferente a salon
     */
    private boolean verificarCambioHorarioSustentacion(Tesis2 tesis2, HorarioSustentacionTesis horario) {
        HorarioSustentacionTesis antiguo = tesis2.getHorarioSustentacion();
        if (antiguo == null) {
            return true;
        } else if (antiguo.getFechaSustentacion().getTime() != horario.getFechaSustentacion().getTime()) {
            return true;
        } else if (!antiguo.getVideoConferencia().equals(horario.getVideoConferencia())) {
            return true;
        } else if (!antiguo.getVideoConferenciaSkyPe().equals(horario.getVideoConferenciaSkyPe())) {
            return true;
        }
        return false;
    }

    /**
     * Elimina los timers y tareas asociadas a una cierta sustentacion de tesis.
     * @param tesis2
     */
    private void eliminarTimersYTareasSustentacionTesis2(Tesis2 tesis2) {
        /**
         * Elimina los timers
         */
        String linea2 = getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_ESTADO_SUSTENTACION_TESIS_2_A_ESPERAR_ARTICULO) + "-" + tesis2.getId().toString();
        timerGenerico.eliminarTimerPorParametroExterno(linea2);
        String linea3 = getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_JURADOD_TESIS) + "-" + tesis2.getId().toString();
        timerGenerico.eliminarTimerPorParametroExterno(linea3);

        /**
         * Completa las tareas de subir articulo y calificar
         */
        String tipoSubirArticulo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARTICULO_TESIS_2);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
        tareaBean.realizarTareaPorCorreo(tipoSubirArticulo, tesis2.getAsesor().getPersona().getCorreo(), params);

        String tipoCalificar = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_SUSTENTACION_TESIS2);
        for (CalificacionJurado calificacion : tesis2.getCalificacionesJurados()) {
            String correo = null;
            params = new HashMap<String, String>();
            params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_CALIFICACION), calificacion.getHash());
            if (calificacion.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_ASESOR_TESIS))) {
                Profesor asesor = tesis2.getAsesor();
                correo = asesor.getPersona().getCorreo();
            } else if (calificacion.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_JURADO_TESIS_INTERNO))) {
                Profesor profesor = calificacion.getJuradoInterno();
                correo = profesor.getPersona().getCorreo();
            } else if (calificacion.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS2_COASESOR))) {
                if (calificacion.getCoasesor().getInterno()) {
                    Profesor profesor = calificacion.getCoasesor().getCoasesor();
                    correo = profesor.getPersona().getCorreo();
                }
            }
            if (correo != null) {
                tareaBean.realizarTareaPorCorreo(tipoCalificar, correo, params);
            }
        }



    }

    private void crearTimersSustentacioTesis2(Tesis2 tesis2) {

        //  hacer timmer de sustentacion. . .
        HorarioSustentacionTesis horario = tesis2.getHorarioSustentacion();

        //aca colocar timmer para cambiar de estado la tesis a estado esperando articulo finalizacion...
        //
        String linea2 = getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_ESTADO_SUSTENTACION_TESIS_2_A_ESPERAR_ARTICULO) + "-" + tesis2.getId().toString();
        timerGenerico.eliminarTimerPorParametroExterno(linea2);
        Timestamp fechaTimer2 = new Timestamp(horario.getFechaSustentacion().getTime() - 1000 * 60 * 60 * 24 * 5);
        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, fechaTimer2, linea2,
                "Tesis2Bean", this.getClass().getName(), "crearTimersSustentacioTesis2", "Este timer se crea porque se ha aprobado el horario de sustentación por el asesor");
        /*
         * coloca la tesis en estado esperando articulo
         * crea la tarea al asesor: de subir articulo tesis
         *-----------------------------------------------------------------------------------------------------------------------------*/
        //CMD_CAMBIAR_ESTADO_SUSTENTACION_TESIS_2_A_ESPERAR_ARTICULO
        String linea3 = getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_JURADOD_TESIS) + "-" + tesis2.getId().toString();
        timerGenerico.eliminarTimerPorParametroExterno(linea3);
        Timestamp fechaTimer3 = new Timestamp(horario.getFechaSustentacion().getTime() - (1000 * 60 * 60 * 2));
        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, fechaTimer3, linea3,
                "Tesis2Bean", this.getClass().getName(), "crearTimersSustentacioTesis2", "Este timer se crea porque se ha aprobado el horario de sustentación por el asesor");
        //----------------------------------------------------------------------------------------------------------
    }

    public void avisarACoordinacionDeTareaREservarSalonSustentacionTesis2(Tesis2 tesis2) {


        SimpleDateFormat otroFormato = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        String nombreV = "Reservar salon sustentacion Tesis 2";// + carga.getId();
        String descripcion = "Se debe Reservar un salon para la Sustentacion de los estudiantes: ";

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2);

        Date fechaInicioDate = new Date();
        Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
        Timestamp fechaFinalfinal = new Timestamp(fechaFinDate.getTime());
        //solo para estar seguro que si este la fecha aunque por logica deberia estar, si no no se llamaria al metodo
        if (tesis2.getHorarioSustentacion() != null && tesis2.getHorarioSustentacion().getFechaSustentacion() != null) {
            fechaFinalfinal = tesis2.getHorarioSustentacion().getFechaSustentacion();
        }
        String mensajeBulletTarea = String.format(Notificaciones.MENSAJE_BULLET_SRESERVAR_SALON_PARA_SUSTENTACION_TESIS_2, tesis2.getEstudiante().getPersona().getNombres() + " "
                + tesis2.getEstudiante().getPersona().getApellidos(), tesis2.getEstudiante().getPersona().getCorreo(), otroFormato.format(tesis2.getHorarioSustentacion().getFechaSustentacion()));

        boolean agrupable = true;
        String header = Notificaciones.MENSAJE_HEADER_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2;
        String footer = Notificaciones.MENSAJE_FOOTER_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2;
        Timestamp fFin = new Timestamp(fechaFinalfinal.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHA_SUSTENTACION_TESIS_2);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis2.getId()));
        String rol = getConstanteBean().getConstante(Constantes.ROL_SECRETARIO_COORDINACION);
        String asunto = Notificaciones.ASUNTO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2;
        tareaBean.crearTareaRol(mensajeBulletTarea, tipo, rol, agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, parametros, asunto);


    }

    private void notificarAsesorYEstudianteDeSalonSustentacionTesis2(Tesis2 tesis2) {
        SimpleDateFormat otroFormato = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        Persona persona = tesis2.getEstudiante().getPersona();
        String asuntoCreacion = Notificaciones.ASUNTO_SALON_SUSTENTACION_TESIS;
        String mensajeCreacion = Notificaciones.MENSAJE_SALON_SUSTENTACION_TESIS;
        mensajeCreacion = mensajeCreacion.replace("%1", persona.getNombres() + " " + persona.getApellidos());
        if (tesis2.getHorarioSustentacion().getSalonSustentacion() != null) {
            mensajeCreacion = mensajeCreacion.replace("%2", tesis2.getHorarioSustentacion().getSalonSustentacion());//getSalonSustentacion
        } else {
            mensajeCreacion = mensajeCreacion.replace("%2", " Desconocido ");
        }
        mensajeCreacion = mensajeCreacion.replace("%3", otroFormato.format(tesis2.getHorarioSustentacion().getFechaSustentacion()));

        correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, tesis2.getAsesor().getPersona().getCorreo(), null, null, mensajeCreacion);

    }

    public String agregarComentarioTesis2(String xml) {
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
                Tesis2 tesis = tesis2Facade.find(idTesis);
                if (tesis != null) {
                    if (tesis.getSemestreInicio().getFechaDel30Porciento().before(new Date())) {
                        String comando = getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_2);
                        String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_REALIZAR_INFORME_30);
                        String login = tesis.getAsesor().getPersona().getCorreo();
                        String infoAdicional = "Se envio el reporte de 30% del estudiante " + tesis.getEstudiante().getPersona().getCorreo();
                        reportarEntregaTardia(tesis.getSemestreInicio().getFechaDel30Porciento(), comando, accion, login, infoAdicional);
                    }
                    terminarTarea30PorcientoTesis2(tesis);
                    Secuencia secComentarioTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO_TESIS));
                    ComentarioTesis coment = conversor.pasarSecuenciaAComentarioTesis(secComentarioTesis);
                    if (coment.getId() != null) {
                        coment.setFecha(new Timestamp(new Date().getTime()));
                        comentarioTesisFacade.edit(coment);
                        ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                    } else {
                        coment.setFecha(new Timestamp(new Date().getTime()));
                        comentarioTesisFacade.create(coment);
                        Collection<ComentarioTesis> comentsTesis = tesis.getComentariosAsesor();
                        comentsTesis.add(coment);
                        tesis2Facade.edit(tesis);
                        ArrayList<Secuencia> param = new ArrayList<Secuencia>();

                        //envía mensaje al estudiante
                        String asuntoEstudiante = Notificaciones.ASUNTO_INFORME_30PORCIENTO_TESIS_2;
                        String mensajeEstudiante = Notificaciones.MENSAJE_INFORME_30PORCIENTO_TESIS_2;
                        mensajeEstudiante = mensajeEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeEstudiante = mensajeEstudiante.replaceFirst("%2", coment.getDebeRetirar() ? "<strong>[LE HA SUGERIDO QUE RETIRE TESIS 2]</strong>" : "");
                        mensajeEstudiante = mensajeEstudiante.replaceFirst("%3", coment.getComentario());
                        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoEstudiante, null, null, null, mensajeEstudiante);

                        //envía mensaje a coordinación
                        String asuntoCoord = Notificaciones.ASUNTO_INFORME_30PORCIENTO_TESIS_2_COORD;
                        asuntoCoord = asuntoCoord.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        String mensajeCoord = Notificaciones.MENSAJE_INFORME_30PORCIENTO_TESIS_2_COORD;
                        mensajeCoord = mensajeCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                        mensajeCoord = mensajeCoord.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                        mensajeCoord = mensajeCoord.replaceFirst("%3", coment.getDebeRetirar() ? "<strong>[LE HA SUGERIDO QUE RETIRE TESIS 2]</strong>" : "");
                        mensajeCoord = mensajeCoord.replaceFirst("%4", coment.getComentario());
                        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCoord, null, null, null, mensajeCoord);

                        return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                    }
                } else {
                    //tirar error
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());

                }
            } else {
                //tirar exception xml mal formado
                throw new Exception("sml mal foramdo en agregarComentarioTesis2 : TesisBean ");
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darComentariosPorTesis2(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null) {
                Long idTesis = Long.parseLong(secId.getValor().trim());
                Tesis2 tesis = tesis2Facade.find(idTesis);

                Collection<ComentarioTesis> coments = tesis.getComentariosAsesor();
                Secuencia seccoemtns = conversor.pasarComentariosTesisASecuencia(coments);

                ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                param.add(seccoemtns);
                return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                //tirar error
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());

            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex2) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex2);
                return null;
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    cambios para que el asesor apruebe el horario de sustentacion del estudainte
     * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * metodo que crea la tarea para que el asesor apruebe la fecha de sustentacion de tessis 2
     * @param tesis2
     */
    public void notificarAsesorFechaHoraSustentacionTesis2(Tesis2 tesis2) {     /*
         *revisar que no exista otra tarea igual en cuyo caso se elimina
         */
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR), propiedades);

        // crearle tarea al asesor :----------------------------------------------------------------
        //4.1 notificacion:
        //Crear alerta de inscripciones
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR);
        String asunto = Notificaciones.ASUNTO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR;

        String mensajeHEader = String.format(Notificaciones.MENSAJE_HEADER_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR, tesis2.getAsesor().getPersona().getNombres() + " " + tesis2.getAsesor().getPersona().getApellidos());


        SimpleDateFormat otroFormato = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));

        String mensajeBUllet = Notificaciones.MENSAJE_BULLET_SRESERVAR_SALON_PARA_SUSTENTACION_TESIS_2;
        mensajeBUllet = String.format(mensajeBUllet, tesis2.getEstudiante().getPersona().getNombres() + " " + tesis2.getEstudiante().getPersona().getApellidos(),
                tesis2.getEstudiante().getPersona().getCorreo(), otroFormato.format(tesis2.getHorarioSustentacion().getFechaSustentacion()));

        Date fechaInicioDate = new Date();
        Timestamp fFin = new Timestamp(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);

        //4.2 crear tarea...
        String nombreV = "Aprobar Horario Sustentación Tesis 2";// + carga.getId();
        String descripcion = "Se debe Aceptar/Rechazarla el horario de sustentación de la tesis de los estudiantes: ";
        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);

        String cmd = getConstanteBean().getConstante(Constantes.TAREA_TESIS_PROFESOR_APROBAR_RECHAZAR_HORARIO_SUSTENTACION_TESIS2);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis2.getId()));
        Profesor profesor = tesis2.getAsesor();
        String correo = profesor.getPersona().getCorreo();

        Timestamp fInicio = new Timestamp(fechaInicioDate.getTime());

        tareaBean.crearTareaPersona(mensajeBUllet, tipo, correo, true, mensajeHEader,
                Notificaciones.MENSAJE_FOOTER_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR, fInicio, fFin, cmd, rol, parametros, asunto);
        //--------------------------------------------------------------------------------------------------------------

    }

    /**
     * metodo llamado por el asesor para aceptar o rechazar el horario de sustentacion ingreado por el estudiante, en caso de aceptado se le crea la tarea a secretaria de que busque salon,
     * en caso contrario la tesis queda en el estado anterior y el estudiante tiene que volver a seleccionar un horario
     * @param xml
     * @return
     */
    public String aprobarHorarioSustentacionPorAsesor(String xml) {
        try {
            //
            parser.leerXML(xml);
            Secuencia secAprobado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR));
            Secuencia secIdTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            if (secAprobado != null && secIdTesis != null) {
                Long idTesis = Long.parseLong(secIdTesis.getValor());
                Tesis2 tesis = tesis2Facade.find(idTesis);
                String aprobado = secAprobado.getValor();
                // quitar tarea al asesor...
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), idTesis.toString());
                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR), propiedades);

                if (aprobado.equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                    tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
                    tesis.setEstadoHorario(getConstanteBean().getConstante(Constantes.CTE_HORARIO_APROBADO));
                    tesis2Facade.edit(tesis);
                    eliminarTimersYTareasSustentacionTesis2(tesis);
                    avisarACoordinacionDeTareaREservarSalonSustentacionTesis2(tesis);
                    enviarCorreoEstudianteInformandoAprobacionHorarioSustentacion(tesis);
                    crearTimersSustentacioTesis2(tesis);
                } else {
                    avisarAEstudianteDeSeleccionarHorarioSustentacionTesis2YCrearTarea(tesis, true);
                    tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
                    tesis.setHorarioSustentacion(null);
                    tesis.setEstadoHorario(getConstanteBean().getConstante(Constantes.CTE_HORARIO_PENDIENTE));
                    tesis2Facade.edit(tesis);


                }
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_HORARIO_SUSTENTACION_TESIS2_POR_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                throw new Exception("Xml mal formado en:aprobarHorarioSustentacionPorAsesor - Tesis2Bean ");
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_HORARIO_SUSTENTACION_TESIS2_POR_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }



    }

    private void actualizarHorarioSalonSustentacion(Tesis2 tesis2, HorarioSustentacionTesis horario) {
        boolean cambioFechaHoraSustentacion = verificarCambioHorarioSustentacion(tesis2, horario);


        tesis2.setHorarioSustentacion(horario);
        tesis2Facade.edit(tesis2);

        if (cambioFechaHoraSustentacion) {
            eliminarTimerAnteriorSustentacionTesis2(tesis2.getId());
            notificarAsesorFechaHoraSustentacionTesis2(tesis2);
            tesis2.setEstadoHorario(getConstanteBean().getConstante(Constantes.CTE_HORARIO_SELECCIONADO));
            tesis2Facade.edit(tesis2);

        } else {
            //realiza tarea de secretario de coordinación
            Properties propiedades = new Properties();
            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
            completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2), propiedades);
            //notifica a interesados
            notificarAsesorYEstudianteDeSalonSustentacionTesis2(tesis2);
        }
    }

    /**
     * le coloca a un estudainte la tarea de seleccionar un horario de sustentacion para su tesis
     * @param tesis
     */
    public void avisarAEstudianteDeSeleccionarHorarioSustentacionTesis2YCrearTarea(Tesis2 tesis, boolean rechazado) {


        if (rechazado) {
            SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));

            String asunto = Notificaciones.ASUNTO_RECHAZO_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
            String mensaje = Notificaciones.MENSAJE_RECHAZO_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
            Persona persona = tesis.getEstudiante().getPersona();
            mensaje = mensaje.replaceFirst("%1", persona.getNombres() + " " + persona.getApellidos());
            mensaje = mensaje.replaceFirst("%2", sdfHMS.format(tesis.getHorarioSustentacion().getFechaSustentacion()));

            correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
        }


        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        //4.1 notificacion:
        //Crear alerta de inscripciones
        String tipoHorario = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2);
        String tipoJurado = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_JURADOS_SUSTENTACION_TESIS2);

        String asuntoHorario = Notificaciones.ASUNTO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
        String mensajeHorario = Notificaciones.MENSAJE_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;

        String asuntoJurado = Notificaciones.ASUNTO_SELECCIONAR_JURADOS_SUSTENTACION_TESIS2_ESTUDIANTE;
        String mensajeJurado = Notificaciones.MENSAJE_SELECCIONAR_JURADOS_SUSTENTACION_TESIS2_ESTUDIANTE;

        Persona persona = tesis.getEstudiante().getPersona();
        mensajeHorario = mensajeHorario.replaceFirst("%1", persona.getNombres() + " " + persona.getApellidos());
        mensajeHorario = mensajeHorario.replaceFirst("%2", tesis.getSemestreInicio().getPeriodo());
        mensajeHorario = mensajeHorario.replaceFirst("%3", sdfHMS.format(tesis.getSemestreInicio().getFechaUltimaSustentarTesis2()));

        mensajeJurado = mensajeJurado.replaceFirst("%1", persona.getNombres() + " " + persona.getApellidos());
        mensajeJurado = mensajeJurado.replaceFirst("%2", tesis.getSemestreInicio().getPeriodo());
        mensajeJurado = mensajeJurado.replaceFirst("%3", sdfHMS.format(tesis.getSemestreInicio().getFechaUltimaSustentarTesis2()));

        String cmdHorario = getConstanteBean().getConstante(Constantes.TAREA_TESIS_2_ESTUDAINTE_MODIFICAR_HORARIO);//TODO:cambiar el comando segun sea
        String cmdJurado = getConstanteBean().getConstante(Constantes.TAREA_TESIS_2_ESTUDIANTE_SELECCIONAR_JURADOS);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));

        //ver que ya no exista la tarea en caso que si la realiza y crea otra:
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2), propiedades);
        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2), propiedades);

        Timestamp fInicio = new Timestamp(new Date().getTime());
        Timestamp fFin = tesis.getSemestreInicio().getFechaUltimaSustentarTesis2();

        String rol = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);

        String mensajeCreacionHorario = mensajeHorario;
        String mensajeCreacionJurado = mensajeJurado;

        tareaBean.crearTareaPersona(null, tipoHorario, persona.getCorreo(), false, mensajeCreacionHorario, "", fInicio, fFin, cmdHorario, rol, parametros, asuntoHorario);
        tareaBean.crearTareaPersona(null, tipoJurado, persona.getCorreo(), false, mensajeCreacionJurado, "", fInicio, fFin, cmdJurado, rol, parametros, asuntoJurado);

        if (!rechazado) {
            correoBean.enviarMail(persona.getCorreo(), asuntoHorario, null, null, null, mensajeHorario);
        }
    }

    private void enviarCorreoEstudianteInformandoAprobacionHorarioSustentacion(Tesis2 tesis) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));

        String asunto = Notificaciones.ASUNTO_APROBACION_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
        String mensaje = Notificaciones.MENSAJE_APROBACION_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
        Persona persona = tesis.getEstudiante().getPersona();
        mensaje = mensaje.replaceFirst("%1", persona.getNombres() + " " + persona.getApellidos());
        mensaje = mensaje.replaceFirst("%2", sdfHMS.format(tesis.getHorarioSustentacion().getFechaSustentacion()));

        correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
    }

    //------------------
    public String guardarSalonSustentacion(String xml) {
        //-----------Sustentacion
        try {
            parser.leerXML(xml);
            Secuencia secTesis2 = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Long id = Long.parseLong(secTesis2.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor());
            Tesis2 tesis2 = tesis2Facade.find(id);
            Secuencia secHorario = secTesis2.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO_SUSTENTACION));
            HorarioSustentacionTesis horario = conversor.pasarSecuenciaAHorario(secHorario);

            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2);
            Properties propiedades = new Properties();
            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
            completarTareaSencilla(tipo, propiedades);
            actualizarHorarioSalonSustentacion(tesis2, horario);

            //Envía correo a coordinación
            if (tesis2.getJuradoTesis().size() == 0 || tesis2.getJurados().size() == 0) {
                String asunto = Notificaciones.ASUNTO_HORARIO_SUSTENTACION_TESIS2_ASIGNADO_SIN_JURADOS;
                String mensaje = Notificaciones.MENSAJE_HORARIO_SUSTENTACION_TESIS2_ASIGNADO_SIN_JURADOS;

                mensaje = mensaje.replaceFirst("%1", tesis2.getEstudiante().getPersona().getNombres() + " " + tesis2.getEstudiante().getPersona().getApellidos());
                if (tesis2.getJuradoTesis() == null && tesis2.getJurados() == null) {
                    mensaje = mensaje.replaceFirst("%2", "jurado interno ni externo.");
                } else {
                    if (tesis2.getJuradoTesis() == null) {
                        mensaje = mensaje.replaceFirst("%2", "jurado interno.");
                    } else {
                        mensaje = mensaje.replaceFirst("%2", "jurado externo.");
                    }
                }

                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asunto, null, null, null, mensaje);
            }

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_SALON_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_SALON_SUSTENTACION_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /***

     * Convierte un arreglo de bytes a String usando valores hexadecimales
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

    private String crearHash(Persona persona, Tesis2 tesis) {
        try {
            String stringHash = persona.getCorreo().split("@")[0] + "__" + tesis.getId().toString();
            byte[] bytesCadena = stringHash.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesCadena);
            String cadenaHash = toHexadecimal(thedigest);
            return cadenaHash;
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String crearHashExterno(JuradoExternoUniversidad e, Tesis2 tesis) {
        try {
            String stringHash = e.getCorreo().split("@")[0] + "__" + tesis.getId().toString();
            byte[] bytesCadena = stringHash.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesCadena);
            String cadenaHash = toHexadecimal(thedigest);
            return cadenaHash;
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String crearTareaProfesoresJuradoSustentacion(Profesor profesor, Tesis2 tesis) {

        //---------------------------------------------------------
        Persona persona = profesor.getPersona();
        String hashSTring = crearHash(persona, tesis);

        crearTareaProfesorJuradocalificarTesis(tesis, hashSTring, persona);

        return hashSTring;
    }

    public void comportamientoJuradosDiaSustentacion(Long id) {
        Tesis2 tesis = tesis2Facade.find(id);

        // Elimina las calificaciones anteriores realizadas por los jurados
        Collection<CalificacionJurado> calificacionesJurados = tesis.getCalificacionesJurados();
        if (calificacionesJurados != null) {
            for (CalificacionJurado calificacionJurado : calificacionesJurados) {
                calificacionJuradoFacade.remove(calificacionJurado);
            }
        }

        //PARA JURADOS INTERNOS CREAR TAREA POR SISINFO
        Collection<Profesor> profesores = tesis.getJuradoTesis();
        calificacionesJurados = new ArrayList<CalificacionJurado>();
        for (Profesor profesor : profesores) {
            if (!profesor.getPersona().getCorreo().equals(tesis.getAsesor().getPersona().getCorreo())) {
                CalificacionJurado calJurado = crearTareaCalificacionJuradoInterno(tesis, profesor);
                calificacionesJurados.add(calJurado);
            }
        }
        //PARA ASESOR CREAR TAREA ASESOR
        if (true) {
            CalificacionJurado calJurado = crearTareaCalificacionJuradoAsesor(tesis);
            calificacionesJurados.add(calJurado);
        }
        //-----------------------fin asesor----------------------------------------------------------------------
        //PARA JURADOS EXTERNOS CREAR HASH Y ENVIAR CORREO
        Collection<JuradoExternoUniversidad> juradosExteernos = tesis.getJurados();
        for (JuradoExternoUniversidad juradoExternoUniversidad : juradosExteernos) {
            CalificacionJurado calJurado = crearTareaCalificacionJuradoExterno(juradoExternoUniversidad, tesis);
            calificacionesJurados.add(calJurado);
        }
        //------------------------------------------------------------------------------------------------
        //---------COMPORTAMIENTO PARA COASEOSRES ------------------------------------------------
        //------------------------------------------------------------------------------------------------
        //TODO;
        Collection<Coasesor> coasesores = tesis.getCoasesor();
        for (Coasesor coasesor : coasesores) {
            CalificacionJurado calJurado = crearTareaCalificacionJuradoCoasesor(coasesor, tesis);
            calificacionesJurados.add(calJurado);
        }
        tesis.setCalificacionesJurados(calificacionesJurados);
        tesis2Facade.edit(tesis);

        //Crea el timer
        //Creacion del timer para reportar al director las notas faltantes de los juraods dentro de 3 dias.
        int numvecesejecutado = 0;
        Long undia = 1000L * 60 * 60 * 24;
        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 3)),
                getConstanteBean().getConstante(Constantes.CMD_RECORDATORIO_A_DIRECTOR_NOTAS_FALTANTES) + "-" + id.toString() + "-" + numvecesejecutado, "TesisMaestria", this.getClass().getName(), "comportamientoJuradosDiaSustentacion", "Este timer se crea para recordar al director de tesis que existen notas faltantes de los jurados");


    }

    private String crearCorreoJuradoExternosSustentacion(JuradoExternoUniversidad juradoExternoUniversidad, Tesis2 tesis) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        String asunto = Notificaciones.ASUNTO_CALIFICAR_SUSTENTACION_TESIS2_ESTUDIANTE_JURADO_EXTERNO;
        String mensaje = Notificaciones.MENSAJE_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_JURADO_EXTERNOE;
        //---------------------------------------------------------
        String hashSTring = crearHashExterno(juradoExternoUniversidad, tesis);
        //---------------------------------------------------------
        mensaje = mensaje.replaceFirst("%1", juradoExternoUniversidad.getNombres() + " " + juradoExternoUniversidad.getApellidos());
        mensaje = mensaje.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
        mensaje = mensaje.replaceFirst("%3", sdfHMS.format(tesis.getHorarioSustentacion().getFechaSustentacion()));
        if (tesis.getHorarioSustentacion().getSalonSustentacion() != null) {
            mensaje = mensaje.replaceFirst("%4", tesis.getHorarioSustentacion().getSalonSustentacion());
        } else {
            mensaje = mensaje.replaceFirst("%4", "Desconocido");
        }

        String urlPlantillaCalificacionExternos = getConstanteBean().getConstante(Constantes.CTE_URL_CALIFICACION_JURADOS_EXTERNOS);
        urlPlantillaCalificacionExternos = urlPlantillaCalificacionExternos.replace("hash", hashSTring);

        mensaje = mensaje.replaceFirst("%5", urlPlantillaCalificacionExternos);

        correoBean.enviarMail(juradoExternoUniversidad.getCorreo(), asunto, null, null, null, mensaje);

        return hashSTring;
    }

    public String modificarCoasesor(String xml) {
        try {
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Tesis2 tesisWeb = conversor.pasarSecuenciaATesis2(secTesis);
            Tesis2 tesisBD = tesis2Facade.find(tesisWeb.getId());
            if (!tesisBD.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS2_TERMINADA))
                    && tesisBD.getSemestreInicio().getFechaUltimaSustentarTesis2().before(new Date()) && tesisBD.getHorarioSustentacion() != null
                    && tesisBD.getHorarioSustentacion().getFechaSustentacion().before(new Date(new Date().getTime() - 1000L * 60 * 60 * 30))) {
                Long maxCoasesores = Long.parseLong(getConstanteBean().getConstante(Constantes.CTE_TESIS_MAXIMO_NUM_COASESORES));
                if (tesisWeb.getCoasesor().size() > maxCoasesores) {
                    //aca lanzar error diciendo que se exedio el maximo de coasesores
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_COASESORES_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00037, new ArrayList());

                }
                Collection<Coasesor> coasesores = tesisWeb.getCoasesor();
                System.out.println("TAMANHO COASESORES ===================================================" + coasesores.size());
                Collection<Coasesor> coasesoresAGuardar = new ArrayList<Coasesor>();
                for (Coasesor coasesor : coasesores) {
                    //busca a los coasesores por correo en la BD
                    Coasesor a = coasesorFacade.findByCorreo(coasesor.getCorreo());
                    if (a == null) {
                        a = new Coasesor();

                        if (a.getCoasesor() != null) {
                            a.setCoasesor(coasesor.getCoasesor());
                            a.setCorreo(coasesor.getCoasesor().getPersona().getCorreo());
                        } else {
                            a.setCargo(coasesor.getCargo());
                            a.setApellidos(coasesor.getApellidos());
                            a.setCorreo(coasesor.getCorreo());
                            a.setDireccion(coasesor.getDireccion());
                            a.setInterno(Boolean.FALSE);
                            a.setNombres(coasesor.getNombres());
                            a.setOrganizacion(coasesor.getOrganizacion());
                            a.setTelefono(coasesor.getTelefono());
                        }
                        //si no estan los agrega a la BD
                        System.out.println("CR");
                        coasesorFacade.create(a);
                    }
                    //lo agrega a la lista
                    coasesoresAGuardar.add(a);
                }
                // asiganarlos a la tesis:
                tesisBD.setCoasesor(coasesoresAGuardar);
                tesis2Facade.edit(tesisBD);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_COASESORES_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            }

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_COASESORES_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_COASESORES_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }

        }
    }
    //-----------------------------------------------------------------------------------------------------
    //------------------PENDIENTE ESPECIAL-----------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------

    public String cambiarEstadoTesis2PendienteEspecial(String xml) {
        try {
            parser.leerXML(xml);
            String id = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor();
            Tesis2 tesis = tesis2Facade.find(Long.parseLong(id));

            //Valida la fecha máxima para realizar la acción
            if (tesis.getSemestreInicio().getFechaUltimaPendienteEspecialTesis2().before(new Date())) {
                String comando = getConstanteBean().getConstante(Constantes.CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR);
                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SOLICITAR_PENDIENTE_ESPECIAL);
                String login = "Coordinacion";
                String infoAdicional = "Coordinación aprobo el pendiente especial para la tesis del estudiante " + tesis.getEstudiante().getPersona().getCorreo();
                reportarEntregaTardia(tesis.getSemestreInicio().getFechaUltimaPendienteEspecialTesis2(), comando, accion, login, infoAdicional);
                //Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                //return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_TESIS2_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
            }
            //Hace la tarea de coordinación
            Properties propiedades = new Properties();
            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id);
            completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PENDIENTE_ESPECIAL_TESIS2_COORDINACION), propiedades);
            //Respuesta de coordinación (aprobado/no aprobado). Si aprueba queda en estado pendiente especial, si no, sigue en curso
            String apruebaONo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_COORDINADOR_MAESTRIA)).getValor();
            Boolean siONo = Boolean.parseBoolean(apruebaONo);
            if (siONo) {
                tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
                tesis.setEstaEnPendienteEspecial(Boolean.TRUE);
            } else {
                String nuevoEstado = (tesis.getEstadoAnterior() != null) ? tesis.getEstadoAnterior() : getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO);
                tesis.setEstadoTesis(nuevoEstado);
            }
            tesis2Facade.edit(tesis);

            //Envia correo al asesor y al estudiatne informado la aprobación/no aprobación del pendiente especial
            if (siONo) {
                //: enviar correo estudiante informando aprobación
                String asuntoPendEstudiante = Notificaciones.ASUNTO_APROBACION_PENDIENTE_ESPECIAL_TESIS2_ESTUDIANTE;
                String mensajePendEstudiante = Notificaciones.MENSAJE_APROBACION_PENDIENTE_ESPECIAL_TESIS2_ESTUDIANTE;
                mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoPendEstudiante, null, null, null, mensajePendEstudiante);

                //: enviar correo asesor informando aprobación
                String asuntoPendAsesor = Notificaciones.ASUNTO_APROBACION_PENDIENTE_ESPECIAL_TESIS2_ASESOR;
                asuntoPendAsesor = asuntoPendAsesor.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                String mensajePendAsesor = Notificaciones.MENSAJE_APROBACION_PENDIENTE_ESPECIAL_TESIS2_ASESOR;
                mensajePendAsesor = mensajePendAsesor.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoPendAsesor, null, null, null, mensajePendAsesor);

                //: enviar correo coordinación informando aprobación
                String asuntoPendCoord = Notificaciones.ASUNTO_APROBACION_PENDIENTE_ESPECIAL_TESIS2_COORDINACION;
                asuntoPendCoord = asuntoPendCoord.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                String mensajePendCoord = Notificaciones.MENSAJE_APROBACION_PENDIENTE_ESPECIAL_TESIS2_COORDINACION;
                mensajePendCoord = mensajePendCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                mensajePendCoord = mensajePendCoord.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoPendCoord, null, null, null, mensajePendCoord);

            } else {
                //: enviar correo estudiante informando rechazo
                String asuntoPendEstudiante = Notificaciones.ASUNTO_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_ESTUDIANTE;
                String mensajePendEstudiante = Notificaciones.MENSAJE_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_ESTUDIANTE;
                mensajePendEstudiante = mensajePendEstudiante.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoPendEstudiante, null, null, null, mensajePendEstudiante);

                //: enviar correo asesor informando rechazo
                String asuntoPendAsesor = Notificaciones.ASUNTO_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_ASESOR;
                asuntoPendAsesor = asuntoPendAsesor.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                String mensajePendAsesor = Notificaciones.MENSAJE_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_ASESOR;
                mensajePendAsesor = mensajePendAsesor.replaceFirst("%1", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoPendAsesor, null, null, null, mensajePendAsesor);

                //: enviar correo coordinación informando rechazo
                String asuntoPendCoord = Notificaciones.ASUNTO_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_COORDINACION;
                asuntoPendCoord = asuntoPendCoord.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                String mensajePendCoord = Notificaciones.MENSAJE_RECHAZO_PENDIENTE_ESPECIAL_TESIS2_COORDINACION;
                mensajePendCoord = mensajePendCoord.replaceFirst("%1", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());
                mensajePendCoord = mensajePendCoord.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoPendCoord, null, null, null, mensajePendCoord);
            }

            //Retorno exitoso
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_TESIS2_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception e) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, e);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_TESIS2_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String pedirPendienteEspecialParaTesis2(String xml) {
        try {
            parser.leerXML(xml);
            String idSolicitud = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor();
            String rutaArchivo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA)).getValor();
            Tesis2 tesis = tesis2Facade.find(Long.parseLong(idSolicitud));

            //Valida la fecha máxima para realizar la acción
            if (tesis.getSemestreInicio().getFechaUltimaPendienteEspecialTesis2().before(new Date())) {
                String comando = getConstanteBean().getConstante(Constantes.CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR);
                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SOLICITAR_PENDIENTE_ESPECIAL);
                String login = tesis.getAsesor().getPersona().getCorreo();
                String infoAdicional = "El asesor solicito pendiente especial para la tesis del estudiante " + tesis.getEstudiante().getPersona().getCorreo();
                reportarEntregaTardia(tesis.getSemestreInicio().getFechaUltimaPendienteEspecialTesis2(), comando, accion, login, infoAdicional);
                //Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                //return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00038, new ArrayList());
            }
            //Valida la existencia del archivo
            if (!conversor.comprobarExistenciaarchivo(getConstanteBean().getConstante(Constantes.CTE_TESIS2_RUTA_PENDIENTES_ESPECIALES), rutaArchivo)) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00039, new ArrayList());
            }
            //Actualiza el estado y la ruta del archivo de pendiente especial
            tesis.setEstadoAnterior(tesis.getEstadoTesis());
            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL));
            tesis.setRutaArchivoSolicitudPendienteEspecial(rutaArchivo);
            tesis2Facade.edit(tesis);

            //Envía correo a coordinación
            crearTareaACoordinacionDeAprobarPendienteEspecialTesis2(tesis);
            //Retorno exitoso
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_PEDIR_PENDIENTE_ESPECIAL_TESIS2_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private String crearCorreoCOasesorExternosSustentacion(Coasesor coasesor, Tesis2 tesis) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        String asunto = Notificaciones.ASUNTO_CALIFICAR_SUSTENTACION_TESIS2_ESTUDIANTE_JURADO_EXTERNO;
        String mensaje = Notificaciones.MENSAJE_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_JURADO_EXTERNOE;
        //---------------------------------------------------------
        String hashSTring = crearHashCoasesorExterno(coasesor, tesis);
        //---------------------------------------------------------
        mensaje = mensaje.replaceFirst("%1", coasesor.getNombres() + " " + coasesor.getApellidos());
        mensaje = mensaje.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
        mensaje = mensaje.replaceFirst("%3", sdfHMS.format(tesis.getHorarioSustentacion().getFechaSustentacion()));
        if (tesis.getHorarioSustentacion().getSalonSustentacion() != null) {
            mensaje = mensaje.replaceFirst("%4", tesis.getHorarioSustentacion().getSalonSustentacion());
        } else {
            mensaje = mensaje.replaceFirst("%4", " Desconocido ");
        }

        String urlPlantillaCalificacionExternos = getConstanteBean().getConstante(Constantes.CTE_URL_CALIFICACION_JURADOS_EXTERNOS);
        urlPlantillaCalificacionExternos = urlPlantillaCalificacionExternos.replace("hash", hashSTring);

        mensaje = mensaje.replaceFirst("%5", urlPlantillaCalificacionExternos);

        correoBean.enviarMail(coasesor.getCorreo(), asunto, null, null, null, mensaje);

        return hashSTring;
    }

    private String crearHashCoasesorExterno(Coasesor e, Tesis2 tesis) {
        try {
            String stringHash = e.getCorreo().split("@")[0] + "__" + tesis.getId().toString();
            byte[] bytesCadena = stringHash.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesCadena);
            String cadenaHash = toHexadecimal(thedigest);
            return cadenaHash;
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //-----------------------------------------------------------------------------
    // TIMERS
    //-----------------------------------------------------------------------------
    @Deprecated
    public void manejoTimmersTesisMaestria(String comando) {
        System.out.println("TIMMERS: " + comando);
        String[] pamrametros = comando.split("-");

        if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RECHAZADAS_A_HISTORICOS))) {
            migrarTesisRechazada(tesis2Facade.find(Long.parseLong(pamrametros[2].trim())));
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS))) {
            migrarTesisRetiradas();
        } else if (pamrametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS))) {
            migrarTesisTerminadas();

        }
    }

    @Deprecated
    private void editarTimersPeriodo(PeriodoTesis semestre, boolean editado) {
        System.out.println("EDITANDO TIMERS PG");
        Long undia = 2000L * 60 * 60 * 24;
        Date hoy = new Date();

        //TIMER Migrar
        //TODO: Crear un panel para configuración de estas fechas (por ahora queda quemado)
        String mensajeAsociadoMigrarTesis2Retirados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS) + "-" + semestre.getId();
        if (new Timestamp(semestre.getMaxFechaAprobacionTesis2().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getMaxFechaAprobacionTesis2().getTime() + (undia * 25)), mensajeAsociadoMigrarTesis2Retirados,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer no se crea desde el metodo porque nadie lo invoca");
        }
        //TIMERS PARA MIGRAR PROYECTOS DE GRADO TERMINADOS
        //TODO: Crear un panel para configuración de estas fechas
        String mensajeAsociadoMigrarTesis2Terminados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS) + "-" + semestre.getId();
        if (new Timestamp(semestre.getFechaUltimaReportarNotaReprobadaSSTesis2().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getFechaUltimaReportarNotaReprobadaSSTesis2().getTime() + (undia * 25)), mensajeAsociadoMigrarTesis2Terminados,
                    "TesisMaestria", this.getClass().getName(), "editarTimersPeriodo", "Este timer no se crea desde el metodo porque nadie lo invoca");
        }
    }

    //-----------------------------------------------------------------------------
    // HISTÓRICOS TESIS 2
    //-----------------------------------------------------------------------------
    public void migrarTesisRechazada(Long tesisId) {
        migrarTesisRechazada(tesis2Facade.find(tesisId));
    }

    private void migrarTesisRechazada(Tesis2 tesis) {
        if (tesis != null) {
            try {
                ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
                Secuencia secTesis = conversor.pasarTesis2ASecuencia(tesis);

                secs.add(secTesis);
                String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RECHAZADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                String resp = historicoBean.pasarTesis2RechazadaAHistorico(comandoXML);
                if (resp == null) {
                    String mensajeAsociadoMigrarTesis2 = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RECHAZADAS_A_HISTORICOS) + "-" + tesis.getId();
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 2000 * 60 * 20), mensajeAsociadoMigrarTesis2,
                            "TesisMaestria", this.getClass().getName(), "migrarTesisRechazada", "Este timer se crea porque el proceso de migrar la tesis 2 rechazada no fue exitoso");
                    return;
                } else {
                    tesis2Facade.remove(tesis);
                }
            } catch (Exception ex) {
                Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean migrarTesisRetirada(Long tesisId) {

        boolean migracionCompletaExitosa = false;
        try {
            migracionCompletaExitosa = true;
            String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);

            Collection<Tesis2> tesis2 = new ArrayList<Tesis2>();
            Tesis2 t = tesis2Facade.find(tesisId);
            tesis2.add(t);

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            Secuencia secTesis = conversor.pasarTesises2ASecuencia(tesis2);
            secs.add(secTesis);
            String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
            String respTesis2 = historicoBean.pasarTesis2RetiradasAHistorico(comandoXML);

            if (respTesis2 == null) {
                migracionCompletaExitosa = false;
                String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                asunto = asunto.replaceFirst("%1", "2 retirada");
                asunto = asunto.replaceFirst("%2", " no migro tesis 2 retirada");
                String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                mensaje = mensaje.replaceFirst("%1", "2 Retirada");
                mensaje = mensaje.replaceFirst("%2", t.getEstudiante().getPersona().getNombres() + " " + t.getEstudiante().getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%3", " sin embargo la tesis2 retirada no pudo se migrada, por tanto la información asociada no fue borrada de producción.");
                correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
            } else {
                tesis2Facade.remove(tesis2.iterator().next());
            }
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return migracionCompletaExitosa;

    }

    public void migrarTesisRetiradas() {

        Collection<Tesis2> tesises = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_2_RETIRADA));

        String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        boolean migracionCompletaExitosa = true;

        if (tesises.isEmpty()) {
            String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS);
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                    "TesisMaestria", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque no hay tesis 2 retiradas para migrar");

            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_RETIRADAS_A_HISTORICOS_SIN_TESISES;
            asunto = asunto.replaceFirst("%1", "sin tesis retiradas por migrar");
            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_RETIRADAS_A_HISTORICOS_SIN_TESISES;
            mensaje = mensaje.replaceFirst("%1", "no habia tesis retiradas a migrar");
            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
        } else {
            try {

                for (Tesis2 t : tesises) {
                    Boolean rta = migrarTesisRetirada(t.getId());
                    migracionCompletaExitosa = migracionCompletaExitosa || rta;
                }

                if (migracionCompletaExitosa == false) {
                    String mensajeAsociadoMigrarTesis2 = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS);
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrarTesis2,
                            "TesisMaestria", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque la migracion de tesis 2 retiradas a historicos no fue exitoso");

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                    asunto = asunto.replaceFirst("%1", "produjo error");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                    mensaje = mensaje.replaceFirst("%2", "sin embargo se produjo un error");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {

                    Collection<Tesis2> tesisEnCurso = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));

                    if (tesisEnCurso.size() > 0) {
                        String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_RETIRADAS_A_HISTORICOS);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                                "TesisMaestria", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque la migracion de tesis 2 retiradas a historicos se realizo, "
                                + "sin embargo hay algunas tesis 2 en curso y que podrían ser retiradas luego no fueron migradas");
                        String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                        asunto = asunto.replaceFirst("%1", "con tesis que aún no han sido terminadas");
                        String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                        mensaje = mensaje.replaceFirst("%1", "sin embargo hay algunas tesis en curso y que podrían ser retiradas luego no fueron migradas");
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

            System.out.println("ESTA EN migrarTesisPerdida, entro con tesisId: " + tesisId);
            Collection<Tesis2> tesis2 = new ArrayList<Tesis2>();
            Tesis2 t = tesis2Facade.find(tesisId);
            System.out.println("ESTA EN migrarTesisPerdida, consulto tesis con id: " + t.getId());
            tesis2.add(t);

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            Secuencia secTesis = conversor.pasarTesises2ASecuencia(tesis2);
            secs.add(secTesis);
            String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
            String respTesis2 = historicoBean.pasarTesis2PerdidasAHistorico(comandoXML);

            if (respTesis2 == null) {
                migracionCompletaExitosa = false;
                String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                asunto = asunto.replaceFirst("%1", "2 perdida");
                asunto = asunto.replaceFirst("%2", " no migro tesis 2 perdida");
                String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                mensaje = mensaje.replaceFirst("%1", "2 Perdida");
                mensaje = mensaje.replaceFirst("%2", t.getEstudiante().getPersona().getNombres() + " " + t.getEstudiante().getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%3", " sin embargo la tesis2 perdida no pudo se migrada, por tanto la información asociada no fue borrada de producción");
                correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
            } else {
                System.out.println("Va a eliminar tesis perdida");
                tesis2Facade.remove(tesis2.iterator().next());
            }
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return migracionCompletaExitosa;

    }

    public void migrarTesisPerdidas() {
        //----------Reprobar tesis de otros periodos automaticamente-------
        //Se cambia a estado "Tesis perdida" aquellas tesis que aun se encuentran en estado "en curso" que pertenecen a semestres anteriores al actual
        //y que no posee pendiente especial.-----------------------------------------
        try {
            PeriodoTesis periodoTesis = periodoTesis2Facade.findByPeriodo(periodoFacade.findActual().getPeriodo());
            //Las tesis2  NO se migran durante los primeros 20 dias despues de la fecha maxima de aprobacion de tesis2 del periodo actual. Esto con el fin de
            //prevenir la migracion de tesis2 del periodo anterior que tienen sustentacion durante la primera semana del periodo actual
            Long tiempoAdicional = Long.parseLong(getConstanteBean().getConstante(Constantes.VAL_DIAS_REPROBAR_TESIS_2_AUTOMATICAMENTE));//Long.parseLong("1000") * 60 * 60 * 24;
            System.out.println(periodoTesis.getPeriodo() + "  -  " + periodoTesis.getMaxFechaAprobacionTesis2());
            System.out.println(new Date(periodoTesis.getMaxFechaAprobacionTesis2().getTime() + tiempoAdicional));
            System.out.println(new Date().after(new Date(periodoTesis.getMaxFechaAprobacionTesis2().getTime() + tiempoAdicional)));
            if (new Date().after(new Date(periodoTesis.getMaxFechaAprobacionTesis2().getTime() + tiempoAdicional))) {
                Collection<Tesis2> tesisDePeriodosPasados = tesis2Facade.findByDiferentePeriodoTesis(periodoFacade.findActual().getPeriodo(), getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
                for (Tesis2 tesis : tesisDePeriodosPasados) {

                    //Se reprueba la tesis con nota de 1.5
                    tesis.setCalificacion(1.5);
                    tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
                    tesis2Facade.edit(tesis);

                    // Realizar tareas
                    realizarTareasPendientes(tesis.getId().toString(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR));
                    realizarTareasPendientes(tesis.getId().toString(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2));
                    realizarTareasPendientes(tesis.getId().toString(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2));
                    realizarTareasPendientes(tesis.getId().toString(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR));
                    realizarTareasPendientes(tesis.getId().toString(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PENDIENTE_ESPECIAL_TESIS2_COORDINACION));

                    eliminarTimersYTareasSustentacionTesis2(tesis);

                    // Eliminar timers de tesis2
                    eliminarTimerAnteriorSustentacionTesis2(tesis.getId());

                    //: enviar correo a Sisinfo
                    String asunto = String.format(Notificaciones.ASUNTO_REPROBAR_TESIS_2_POR_SISINFO, tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    String mensaje = String.format(Notificaciones.MENSAJE_REPROBAR_TESIS_2_POR_SISINFO, tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos() + " (" + tesis.getEstudiante().getPersona().getCorreo() + ") ", tesis.getSemestreInicio().getPeriodo(), "1.5");
                    correoBean.enviarMail(getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO), asunto, null, null, null, mensaje);

                    //: enviar correo a las personas con rol coordinacion
                    List<Usuario> coordinacion = usuarioFacade.findByRol(getConstanteBean().getConstante(Constantes.ROL_COORDINACION));
                    for (Usuario usuario : coordinacion) {
                        correoBean.enviarMail(usuario.getPersona().getCorreo(), asunto, null, null, null, mensaje);
                    }
                }
            }
        } catch (Exception ex) {
            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO), Notificaciones.ASUNTO_ERROR_REPROBAR_TESIS_2, null, null, null, Notificaciones.MENSAJE_ERROR_REPROBAR_TESIS_2);
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
        }

        //--------------------------Migracion de Tesis Perdidas -------------------------------
        //Trae las tesis que tienen estado "perdida"
        Collection<Tesis2> tesises = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));

        String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        boolean migracionCompletaExitosa = true;

        if (tesises.isEmpty()) {
            String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS);
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                    "TesisMaestria", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque no hay tesis 2 perdidas para migrar");

            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_PERDIDAS_A_HISTORICOS_SIN_TESISES;
            asunto = asunto.replaceFirst("%1", "sin tesis perdidas por migrar");
            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_PERDIDAS_A_HISTORICOS_SIN_TESISES;
            mensaje = mensaje.replaceFirst("%1", "no habia tesis perdidas a migrar");
            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
        } else {
            try {

                System.out.println("ESTA EN migrarTesisPerdidas, cantidad teseses: " + tesises.size());
                for (Tesis2 t : tesises) {
                    System.out.println("ESTA EN migrarTesisPerdidas, entro a tesis con " + t.getId());
                    Boolean rta = migrarTesisPerdida(t.getId());
                    migracionCompletaExitosa = migracionCompletaExitosa || rta;
                }

                if (migracionCompletaExitosa == false) {
                    String mensajeAsociadoMigrarTesis2 = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS);
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrarTesis2,
                            "TesisMaestria", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque la migracion de tesis 2 perdidas a historicos no fue exitoso en su totalidad");

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                    asunto = asunto.replaceFirst("%1", "produjo error");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                    mensaje = mensaje.replaceFirst("%1", "sin embargo se produjo un error");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {
                    Collection<Tesis2> tesisEnCurso = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));
                    if (tesisEnCurso.size() > 0) {
                        String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_PERDIDAS_A_HISTORICOS);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                                "TesisMaestria", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque la migracion de tesis 2 perdidas a historicos se realizo, "
                                + "sin embargo hay algunas tesis 2 en curso y podrían ser perdidas, luego no fueron migradas");
                        String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                        asunto = asunto.replaceFirst("%1", "con tesis que aún no han sido terminadas");
                        String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                        mensaje = mensaje.replaceFirst("%1", "sin embargo hay algunas tesis 2 en curso y podrían convertise en perdidas en el futuro, luego no fueron migradas");
                        correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean migrarTesisTerminada(Long tesisId) {
        boolean migracionCompletaExitosa = false;
        try {
            migracionCompletaExitosa = true;
            String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);

            Collection<Tesis2> tesis2 = new ArrayList<Tesis2>();
            Tesis2 t2 = tesis2Facade.find(tesisId);
            tesis2.add(t2);
            Collection<Tesis1> tesis1 = tesis1Facade.findByCorreoEstudiante(t2.getEstudiante().getPersona().getCorreo());
            InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion = inscripcionSubAreaFacade.findByCorreoEstudiante(t2.getEstudiante().getPersona().getCorreo());

            ArrayList<Secuencia> secsTesis1 = new ArrayList<Secuencia>();
            ArrayList<Secuencia> secsInscripcion = new ArrayList<Secuencia>();
            String respInscripcion = "";
            String respTesis1 = "";
            String respTesis2 = "";

            if (inscripcionSubareaInvestigacion == null) {
                String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                asunto = asunto.replaceFirst("%1", "2 terminada");
                asunto = asunto.replaceFirst("%2", "produjo error");
                String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                mensaje = mensaje.replaceFirst("%1", "2 Terminada");
                mensaje = mensaje.replaceFirst("%2", t2.getEstudiante().getPersona().getNombres() + " " + t2.getEstudiante().getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%3", " se produjo un error. " + "La inscripcion a la subarea de investigación "
                        + "del estudiante " + t2.getEstudiante().getPersona().getNombres() + " " + t2.getEstudiante().getPersona().getApellidos() + " no existe, "
                        + "por tanto no se migró la tesis");
                correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
            } else {
                if (tesis1.isEmpty()) {
                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                    asunto = asunto.replaceFirst("%1", "2 terminada");
                    asunto = asunto.replaceFirst("%2", " migro información de tesis 2 sin tesis 1");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                    mensaje = mensaje.replaceFirst("%1", "2 Terminada");
                    mensaje = mensaje.replaceFirst("%2", t2.getEstudiante().getPersona().getNombres() + " " + t2.getEstudiante().getPersona().getApellidos());
                    mensaje = mensaje.replaceFirst("%3", " sin embargo la tesis no tenia información asociada en tesis 1");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {
                    Secuencia secTesis1 = conversor.pasarTesises1ASecuencias(tesis1);
                    secsTesis1.add(secTesis1);
                    String comandoXML_Tesis1 = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS1_TERMINADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secsTesis1);
                    respTesis1 = historicoTesis1Bean.pasarTesis1TerminadasAHistorico(comandoXML_Tesis1);
                }

                if (respTesis1 == null) {
                    migracionCompletaExitosa = false;
                } else {
                    Secuencia secInscripcion = conversor.pasarInscripcionSubareaASecuencia(inscripcionSubareaInvestigacion);
                    secsInscripcion.add(secInscripcion);
                    String comandoXML_Inscripcion = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_INSCRIPCION_SUBAREA_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secsInscripcion);
                    respInscripcion = historicoInscripcionSubareaInvestBean.pasarInscripcionSubAreaInvestigacionAHistorico(comandoXML_Inscripcion);

                    if (respInscripcion == null) {
                        migracionCompletaExitosa = false;
                    } else {
                        ArrayList<Secuencia> secsTesis2 = new ArrayList<Secuencia>();
                        Secuencia secTesis2 = conversor.pasarTesises2ASecuencia(tesis2);
                        secsTesis2.add(secTesis2);
                        String comandoXML_Tesis2 = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secsTesis2);
                        respTesis2 = historicoBean.pasarTesis2TerminadasAHistorico(comandoXML_Tesis2);
                        if (respTesis2 == null) {
                            migracionCompletaExitosa = false;
                            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                            asunto = asunto.replaceFirst("%1", "2 terminada");
                            asunto = asunto.replaceFirst("%2", " no migro tesis 2 terminada");
                            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_A_HISTORICOS_PROBLEMA;
                            mensaje = mensaje.replaceFirst("%1", "2 Perdida");
                            mensaje = mensaje.replaceFirst("%2", t2.getEstudiante().getPersona().getNombres() + " " + t2.getEstudiante().getPersona().getApellidos());
                            mensaje = mensaje.replaceFirst("%3", " sin embargo la tesis2 terminada no pudo se migrada, por tanto la información asociada no fue borrada de producción");
                            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                        } else {
                            if (!respTesis1.isEmpty()) {
                                tesis1Facade.remove(tesis1.iterator().next());
                            }
                            if (!respInscripcion.isEmpty()) {
                                inscripcionSubAreaFacade.remove(inscripcionSubareaInvestigacion);
                            }
                            if (!respTesis2.isEmpty()) {
                                tesis2Facade.remove(tesis2.iterator().next());
                            }
                        }
                    }
                }
            }


        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return migracionCompletaExitosa;
    }

    public void migrarTesisTerminadas() {

        Collection<Tesis2> tesis = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS2_TERMINADA));
        String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        boolean migracionCompletaExitosa = true;

        if (tesis.isEmpty()) {
            String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS);
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                    "TesisMaestria", this.getClass().getName(), "migrarTesisTerminadas", "Este timer se crea porque no hubo tesis 2 terminadas en el momento de realizar la migración");

            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_SIN_TESISES;
            asunto = asunto.replaceFirst("%1", "sin tesis terminadas por migrar");
            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_SIN_TESISES;
            mensaje = mensaje.replaceFirst("%1", "no habia tesis terminadas a migrar");
            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
        } else {
            try {
                for (Tesis2 t : tesis) {
                    Boolean rta = migrarTesisTerminada(t.getId());
                    migracionCompletaExitosa = migracionCompletaExitosa || rta;
                }

                if (migracionCompletaExitosa == false) {

                    System.out.println("\n\n La migración completa no fue exitosa.");

                    String mensajeAsociadoMigrarTesis2 = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS);
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrarTesis2,
                            "TesisMaestria", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque la migracion de tesis 2 terminadas a historicos no fue exitoso en su totalidad, no fue posible migrar todas las tesis 2 terminadas");

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_SIN_TESISES;
                    asunto = asunto.replaceFirst("%1", "produjo error");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_SIN_TESISES;
                    mensaje = mensaje.replaceFirst("%1", " se produjo un error");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {
                    Collection<Tesis2> tesisEnCurso = tesis2Facade.findByEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO));

                    if (tesisEnCurso.size() > 0) {

                        System.out.println("\n\nSe migraron tesis 2 pero aun hay tesis en curso.");

                        String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS2_TERMINADAS_A_HISTORICOS);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                                "TesisMaestria", this.getClass().getName(), "migrarTesisTerminadas", "Este timer se crea si se migraron todas las tesis 2 terminadas pero aún hay tesis en curso");

                        String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_SIN_TESISES;
                        asunto = asunto.replaceFirst("%1", "con tesis que aún no han sido terminadas");
                        String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_2_TERMINADAS_A_HISTORICOS_SIN_TESISES;
                        mensaje = mensaje.replaceFirst("%1", " hay algunas tesis que no estan terminadas y no fueron migradas");
                        correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void enviarCorreoAprobadoExtemporaneo(Tesis2 tesis) {
        //Crear correo para informar que se aprobó la inscripción
        String asunto = Notificaciones.ASUNTO_APROBACION_INSCRIPCION_TESIS_2;
        String mensaje = Notificaciones.MENSAJE_APROBACION_INSCRIPCION_TESIS_2_EXTEMPORANEO;

        mensaje = mensaje.replaceFirst("%0", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
        mensaje = mensaje.replaceFirst("%1", tesis.getSemestreInicio().getPeriodo());

        String correoCoordinador = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asunto, tesis.getAsesor().getPersona().getCorreo(), correoCoordinador, null, mensaje);

    }

    /**
     * le coloca a un estudainte la tarea de seleccionar un horario de sustentacion para su tesis en pendiente especial
     * @param tesis
     */
    public void avisarAEstudianteDeSeleccionarHorarioSustentacionTesis2PendienteEspecialYCrearTarea(Tesis2 tesis, boolean rechazado) {
        if (rechazado) {
            SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));

            String asunto = Notificaciones.ASUNTO_RECHAZO_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
            String mensaje = Notificaciones.MENSAJE_RECHAZO_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
            Persona persona = tesis.getEstudiante().getPersona();
            mensaje = mensaje.replaceFirst("%1", persona.getNombres() + " " + persona.getApellidos());
            mensaje = mensaje.replaceFirst("%2", sdfHMS.format(tesis.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial()));
            correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
        }
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        //4.1 notificacion:
        //Crear alerta de inscripciones

        String asunto = Notificaciones.ASUNTO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
        String mensaje = Notificaciones.MENSAJE_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
        Persona persona = tesis.getEstudiante().getPersona();
        mensaje = mensaje.replaceFirst("%1", persona.getNombres() + " " + persona.getApellidos());
        mensaje = mensaje.replaceFirst("%2", tesis.getSemestreInicio().getPeriodo());
        mensaje = mensaje.replaceFirst("%3", sdfHMS.format(tesis.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial()));
        //bau.out.println("MEnsaje creacion enviar mail=" + mensajeCreacion);
        //  correoBean.enviarMail(persona.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

        //4.2 crear tarea...
        String nombreV = "Seleccionar Horario Sustentación Tesis 2 Pendiente Especial";// + carga.getId();
        String descripcion = "Se debe seleccionar un horario de sustentacion para tesis 2 pendiente especial ";

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2);

        String rol = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        String cmd = getConstanteBean().getConstante(Constantes.TAREA_TESIS_2_ESTUDAINTE_MODIFICAR_HORARIO);//TODO:cambiar el comando segun sea
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));


        String mensajeCreacion = mensaje;

        Timestamp fInicio = new Timestamp(new Date().getTime());
        Timestamp fFin = tesis.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial();

        tareaBean.crearTareaPersona(null, tipo, persona.getCorreo(), false, mensajeCreacion, "", fInicio, fFin, cmd, rol, parametros, asunto);



        if (!rechazado) {
            correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
        }
    }

    private CalificacionJurado crearTareaCalificacionJuradoInterno(Tesis2 tesis, Profesor profesor) {

        String hash = crearTareaProfesoresJuradoSustentacion(profesor, tesis);
        //----------
        Collection<CategoriaCriterioTesis2> categorias = catCriteriosFacade.findAll();
        Collection<CategoriaCriterioJurado> categoriasJurado = new ArrayList<CategoriaCriterioJurado>();
        for (CategoriaCriterioTesis2 categoriaCriterioTesis2 : categorias) {
            //---------------------------------------------------------------------------------
            Collection<CriterioCalificacion> criterios = categoriaCriterioTesis2.getCriterios();
            Collection<CalificacionCriterio> calificacionesCriterios = new ArrayList<CalificacionCriterio>();
            for (CriterioCalificacion criterioCalificacion : criterios) {
                CalificacionCriterio c = new CalificacionCriterio();
                c.setNombre(criterioCalificacion.getNombre());
                c.setDescripcion(criterioCalificacion.getDescripcion());
                c.setPeso(criterioCalificacion.getPeso());
//                        c.setNombreCategoriaCriterio(criterioCalificacion.getCategoria().getNombre());
//                        c.setOrdenCategoria(criterioCalificacion.getCategoria().getOrdenCategoria());
                c.setOrdenCriterio(criterioCalificacion.getOrdenCriterio());
//                        c.setPorcentajeCategoria(criterioCalificacion.getCategoria().getPorcentaCategoria());
                calificacionesCriterios.add(c);
            }
            CategoriaCriterioJurado cat = new CategoriaCriterioJurado();
            cat.setCalCriterios(calificacionesCriterios);
            cat.setDescripcion(categoriaCriterioTesis2.getDescripcion());
            cat.setNombreCategoriaCriterio(categoriaCriterioTesis2.getNombre());
            cat.setOrdenCategoria(categoriaCriterioTesis2.getOrdenCategoria());
            cat.setPorcentajeCategoria(categoriaCriterioTesis2.getPorcentaCategoria());
            categoriasJurado.add(cat);
            //---------------------------------------------------------------------------------
        }
        //----------

        CalificacionJurado calJurado = new CalificacionJurado();
        calJurado.setCategoriaCriteriosJurado(categoriasJurado);
        calJurado.setHash(hash);
        calJurado.setJuradoInterno(profesor);
        calJurado.setTerminado(Boolean.FALSE);
        calJurado.setCancelada(Boolean.FALSE);
        calJurado.setRolJurado(getConstanteBean().getConstante(Constantes.CTE_JURADO_TESIS_INTERNO));
        calJurado.setTesisCalificada(tesis);
        calificacionJuradoFacade.create(calJurado);

        return calJurado;
    }

    private CalificacionJurado crearTareaCalificacionJuradoAsesor(Tesis2 tesis) {
        Profesor profesor = tesis.getAsesor();
        String hash = crearTareaProfesoresJuradoSustentacion(profesor, tesis);
        //---------------
        Collection<CategoriaCriterioTesis2> categorias = catCriteriosFacade.findAll();
        Collection<CategoriaCriterioJurado> categoriasJurado = new ArrayList<CategoriaCriterioJurado>();
        for (CategoriaCriterioTesis2 categoriaCriterioTesis2 : categorias) {
            //---------------------------------------------------------------------------------
            Collection<CriterioCalificacion> criterios = categoriaCriterioTesis2.getCriterios();
            Collection<CalificacionCriterio> calificacionesCriterios = new ArrayList<CalificacionCriterio>();
            for (CriterioCalificacion criterioCalificacion : criterios) {
                CalificacionCriterio c = new CalificacionCriterio();
                c.setNombre(criterioCalificacion.getNombre());
                c.setDescripcion(criterioCalificacion.getDescripcion());
                c.setPeso(criterioCalificacion.getPeso());
//                        c.setNombreCategoriaCriterio(criterioCalificacion.getCategoria().getNombre());
//                        c.setOrdenCategoria(criterioCalificacion.getCategoria().getOrdenCategoria());
                c.setOrdenCriterio(criterioCalificacion.getOrdenCriterio());
//                        c.setPorcentajeCategoria(criterioCalificacion.getCategoria().getPorcentaCategoria());
                calificacionesCriterios.add(c);
            }
            CategoriaCriterioJurado cat = new CategoriaCriterioJurado();
            cat.setCalCriterios(calificacionesCriterios);
            cat.setDescripcion(categoriaCriterioTesis2.getDescripcion());
            cat.setNombreCategoriaCriterio(categoriaCriterioTesis2.getNombre());
            cat.setOrdenCategoria(categoriaCriterioTesis2.getOrdenCategoria());
            cat.setPorcentajeCategoria(categoriaCriterioTesis2.getPorcentaCategoria());
            categoriasJurado.add(cat);
            //---------------------------------------------------------------------------------
        }
        //----------

        CalificacionJurado calJurado = new CalificacionJurado();
        calJurado.setCategoriaCriteriosJurado(categoriasJurado);
        //--------------
        calJurado.setHash(hash);
        calJurado.setJuradoInterno(profesor);
        calJurado.setTerminado(Boolean.FALSE);
        calJurado.setCancelada(Boolean.FALSE);
        calJurado.setRolJurado(getConstanteBean().getConstante(Constantes.CTE_ASESOR_TESIS));
        calJurado.setTesisCalificada(tesis);
        calificacionJuradoFacade.create(calJurado);
        return calJurado;
    }

    private CalificacionJurado crearTareaCalificacionJuradoExterno(JuradoExternoUniversidad juradoExternoUniversidad, Tesis2 tesis) {
        String hash = crearCorreoJuradoExternosSustentacion(juradoExternoUniversidad, tesis);
        //---------------------------------------------------------------------------------------------
        Collection<CategoriaCriterioTesis2> categorias = catCriteriosFacade.findAll();
        Collection<CategoriaCriterioJurado> categoriasJurado = new ArrayList<CategoriaCriterioJurado>();
        for (CategoriaCriterioTesis2 categoriaCriterioTesis2 : categorias) {
            //---------------------------------------------------------------------------------
            Collection<CriterioCalificacion> criterios = categoriaCriterioTesis2.getCriterios();
            Collection<CalificacionCriterio> calificacionesCriterios = new ArrayList<CalificacionCriterio>();
            for (CriterioCalificacion criterioCalificacion : criterios) {
                CalificacionCriterio c = new CalificacionCriterio();
                c.setNombre(criterioCalificacion.getNombre());
                c.setDescripcion(criterioCalificacion.getDescripcion());
                c.setPeso(criterioCalificacion.getPeso());
//                        c.setNombreCategoriaCriterio(criterioCalificacion.getCategoria().getNombre());
//                        c.setOrdenCategoria(criterioCalificacion.getCategoria().getOrdenCategoria());
                c.setOrdenCriterio(criterioCalificacion.getOrdenCriterio());
//                        c.setPorcentajeCategoria(criterioCalificacion.getCategoria().getPorcentaCategoria());
                calificacionesCriterios.add(c);
            }
            CategoriaCriterioJurado cat = new CategoriaCriterioJurado();
            cat.setCalCriterios(calificacionesCriterios);
            cat.setDescripcion(categoriaCriterioTesis2.getDescripcion());
            cat.setNombreCategoriaCriterio(categoriaCriterioTesis2.getNombre());
            cat.setOrdenCategoria(categoriaCriterioTesis2.getOrdenCategoria());
            cat.setPorcentajeCategoria(categoriaCriterioTesis2.getPorcentaCategoria());
            categoriasJurado.add(cat);
            //---------------------------------------------------------------------------------
        }
        //----------

        CalificacionJurado calJurado = new CalificacionJurado();
        calJurado.setCategoriaCriteriosJurado(categoriasJurado);
        //---------------------------------------------------------------------------------------------
        calJurado.setHash(hash);
        calJurado.setJuradoExterno(juradoExternoUniversidad);
        calJurado.setTerminado(Boolean.FALSE);
        calJurado.setCancelada(Boolean.FALSE);
        calJurado.setRolJurado(getConstanteBean().getConstante(Constantes.CTE_JURADO_EXTERNO));
        calJurado.setTesisCalificada(tesis);
        calJurado.setTesisCalificada(tesis);
        calificacionJuradoFacade.create(calJurado);
        return calJurado;
    }

    private CalificacionJurado crearTareaCalificacionJuradoCoasesor(Coasesor coasesor, Tesis2 tesis) {
        String hash = "";
        if (coasesor.getCoasesor() != null) {
            hash = crearTareaProfesoresJuradoSustentacion(coasesor.getCoasesor(), tesis);//crearHash(coasesor.getCoasesor().getPersona(), tesis);
        } else {
            hash = crearCorreoCOasesorExternosSustentacion(coasesor, tesis);
        }
        //---------------------------------------------------------------------------------------------
        Collection<CategoriaCriterioTesis2> categorias = catCriteriosFacade.findAll();
        Collection<CategoriaCriterioJurado> categoriasJurado = new ArrayList<CategoriaCriterioJurado>();
        for (CategoriaCriterioTesis2 categoriaCriterioTesis2 : categorias) {
            //---------------------------------------------------------------------------------
            Collection<CriterioCalificacion> criterios = categoriaCriterioTesis2.getCriterios();
            Collection<CalificacionCriterio> calificacionesCriterios = new ArrayList<CalificacionCriterio>();
            for (CriterioCalificacion criterioCalificacion : criterios) {
                CalificacionCriterio c = new CalificacionCriterio();
                c.setNombre(criterioCalificacion.getNombre());
                c.setDescripcion(criterioCalificacion.getDescripcion());
                c.setPeso(criterioCalificacion.getPeso());
//                        c.setNombreCategoriaCriterio(criterioCalificacion.getCategoria().getNombre());
//                        c.setOrdenCategoria(criterioCalificacion.getCategoria().getOrdenCategoria());
                c.setOrdenCriterio(criterioCalificacion.getOrdenCriterio());
//                        c.setPorcentajeCategoria(criterioCalificacion.getCategoria().getPorcentaCategoria());
                calificacionesCriterios.add(c);
            }
            CategoriaCriterioJurado cat = new CategoriaCriterioJurado();
            cat.setCalCriterios(calificacionesCriterios);
            cat.setDescripcion(categoriaCriterioTesis2.getDescripcion());
            cat.setNombreCategoriaCriterio(categoriaCriterioTesis2.getNombre());
            cat.setOrdenCategoria(categoriaCriterioTesis2.getOrdenCategoria());
            cat.setPorcentajeCategoria(categoriaCriterioTesis2.getPorcentaCategoria());
            categoriasJurado.add(cat);
            //---------------------------------------------------------------------------------
        }
        //----------

        CalificacionJurado calJurado = new CalificacionJurado();
        calJurado.setCategoriaCriteriosJurado(categoriasJurado);
        //---------------------------------------------------------------------------------------------
        calJurado.setHash(hash);
        calJurado.setCoasesor(coasesor);
        calJurado.setTerminado(Boolean.FALSE);
        calJurado.setCancelada(Boolean.FALSE);
        calJurado.setRolJurado(getConstanteBean().getConstante(Constantes.CTE_TESIS2_COASESOR));
        calJurado.setTesisCalificada(tesis);
        calJurado.setTesisCalificada(tesis);
        calificacionJuradoFacade.create(calJurado);
        return calJurado;
    }

    /*
     * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *  //METODOS DE CONSUKTA PARA EXTERNOS------------------------------------
     * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public String consultarTesisProfesorParaExternos(String xml) {
        try {
            /**
             * consulta:
             * <correo>correo del asesor</correo>
             */
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null && secCorreo.getValor() != null) {
                String correo = secCorreo.getValor();
                Collection<Tesis2> tesisesREspuesta = new ArrayList<Tesis2>();
                Collection<Tesis2> tesises = tesis2Facade.findByCorreoAsesor(correo);
                for (Tesis2 tesis2 : tesises) {
                    Tesis2 t = new Tesis2();
                    t.setAsesor(tesis2.getAsesor());
                    t.setEstudiante(tesis2.getEstudiante());
                    t.setTemaProyecto(tesis2.getTemaProyecto());
                    t.setEstadoTesis(t.getEstadoTesis());
                    tesisesREspuesta.add(t);
                }
                Secuencia secTesises = conversor.pasarTesises2ASecuencia(tesisesREspuesta);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secTesises);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS_2_ASESOR_CONSULTA_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                // xml mal formado
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS_2_ASESOR_CONSULTA_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS_2_ASESOR_CONSULTA_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Metodo que genera las calificaciones de forma manual para el dia de la sustentacion
     * @param xml
     * @return
     */
    public String comportamientoEmergenciaJuradosTesis(String xml) {
        try {
            String retorno = null;
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null && secId.getValor() != null) {
                Long idTesis = Long.parseLong(secId.getValor());

                //System.out.println("Lo que tiene idTesis en Tesis2Bean:" + idTesis);
                //   de pronto seria mejor no llamar a ese metodo si no hacer lo mismo pero para las calificaciones no creadas...
                //comportamientoJuradosDiaSustentacion(idTesis);-----------------------------------------------------------------------------

                Tesis2 tesis = tesis2Facade.find(idTesis);


                Collection<CalificacionJurado> calificacionesJuradosExistente = tesis.getCalificacionesJurados();


                if ((tesis.getJuradoTesis().isEmpty() && tesis.getJurados().isEmpty()) && tesis.getHorarioSustentacion() == null) {
                    retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_JURADOS_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00042, new LinkedList<Secuencia>());
                    return retorno;
                } else {

                    //PARA JURADOS INTERNOS CREAR TAREA POR SISINFO
                    Collection<Profesor> profesores = tesis.getJuradoTesis();
                    Collection<CalificacionJurado> calificacionesJuradosNuevas = new ArrayList<CalificacionJurado>();
                    //recorre los jurados internos
                    for (Profesor profesor : profesores) {
                        //revisa que no sea el asesor
                        if (!profesor.getPersona().getCorreo().equals(tesis.getAsesor().getPersona().getCorreo())) {//asume que no tiene calificacion
                            boolean existeCalificacion = false;
                            //busca en todas las calificaciones existentes si esta o no la del jurado

                            if (!calificacionesJuradosExistente.isEmpty()) {

                                for (CalificacionJurado calificacionJurado : calificacionesJuradosExistente) {
                                    //si es del mismo rol y correo ya existe
                                    if (calificacionJurado.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_JURADO_TESIS_INTERNO))) {
                                        if (calificacionJurado.getJuradoInterno().getPersona().getCorreo().equals(profesor.getPersona().getCorreo())) {
                                            existeCalificacion = true;
                                        }
                                    }
                                }
                            }

                            //si cuando termina de recorrer las calificaciones existentes, se da cuenta que no existe la crea y agrega
                            if (!existeCalificacion) {
                                CalificacionJurado calJurado = crearTareaCalificacionJuradoInterno(tesis, profesor);
                                calificacionesJuradosNuevas.add(calJurado);
                            }
                        }
                    }


                    //PARA ASESOR CREAR TAREA ASESOR
                    if (true) {
                        //-------------
                        boolean existeCalificacion = false;

                        if (!calificacionesJuradosExistente.isEmpty()) {

                            for (CalificacionJurado calificacionJurado : calificacionesJuradosExistente) {
                                //si es del mismo rol y correo ya existe
                                if (calificacionJurado.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_ASESOR_TESIS))) {
                                    existeCalificacion = true;
                                }
                            }
                        }
                        //-------------
                        if (!existeCalificacion) {
                            CalificacionJurado calJurado = crearTareaCalificacionJuradoAsesor(tesis);
                            calificacionesJuradosNuevas.add(calJurado);
                        }
                    }


                    //-----------------------fin asesor----------------------------------------------------------------------
                    //PARA JURADOS EXTERNOS CREAR HASH Y ENVIAR CORREO
                    Collection<JuradoExternoUniversidad> juradosExteernos = tesis.getJurados();
                    for (JuradoExternoUniversidad juradoExternoUniversidad : juradosExteernos) {

                        //asume que no tiene calificacion
                        boolean existeCalificacion = false;
                        //busca en todas las calificaciones existentes si esta o no la del jurado

                        if (!calificacionesJuradosExistente.isEmpty()) {

                            for (CalificacionJurado calificacionJurado : calificacionesJuradosExistente) {   //si es del mismo rol y correo ya existe
                                if (calificacionJurado.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_JURADO_EXTERNO))) {
                                    if (calificacionJurado.getJuradoExterno().getCorreo().equals(juradoExternoUniversidad.getCorreo())) {
                                        existeCalificacion = true;
                                    }
                                }
                            }

                        }

                        //si no existe la calificacion la crea
                        if (!existeCalificacion) {
                            CalificacionJurado calJurado = crearTareaCalificacionJuradoExterno(juradoExternoUniversidad, tesis);
                            calificacionesJuradosNuevas.add(calJurado);
                        }
                    }
                    //------------------------------------------------------------------------------------------------
                    //---------COMPORTAMIENTO PARA COASEOSRES ------------------------------------------------
                    //------------------------------------------------------------------------------------------------
                /*
                     *
                     */
                    Collection<Coasesor> coasesores = tesis.getCoasesor();
                    //para ´cada coaaesor
                    for (Coasesor coasesor : coasesores) {
                        //si es interno
                        if (coasesor.getInterno()) {
                            boolean existeCalificacion = false;

                            if (!calificacionesJuradosExistente.isEmpty()) {

                                for (CalificacionJurado calificacionJurado : calificacionesJuradosExistente) {
                                    //si es del mismo rol y correo ya existe
                                    if (calificacionJurado.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS2_COASESOR))) {
                                        if (calificacionJurado.getCoasesor().getInterno()) {
                                            if (calificacionJurado.getCoasesor().getCoasesor().getPersona().getCorreo().equals(coasesor.getCoasesor().getPersona().getCorreo())) {
                                                existeCalificacion = true;
                                            }
                                        }
                                    }
                                }

                            }

                            if (!existeCalificacion) {
                                CalificacionJurado calJurado = crearTareaCalificacionJuradoCoasesor(coasesor, tesis);
                                calificacionesJuradosNuevas.add(calJurado);
                            }
                        } else {
                            boolean existeCalificacion = false;


                            if (!calificacionesJuradosExistente.isEmpty()) {

                                for (CalificacionJurado calificacionJurado : calificacionesJuradosExistente) {
                                    //si es del mismo rol y correo ya existe
                                    if (calificacionJurado.getRolJurado().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS2_COASESOR))) {
                                        if (!calificacionJurado.getCoasesor().getInterno()) {
                                            if (calificacionJurado.getCoasesor().getCorreo().equals(coasesor.getCorreo())) {
                                                existeCalificacion = true;
                                            }
                                        }
                                    }
                                }

                            }


                            if (!existeCalificacion) {
                                CalificacionJurado calJurado = crearTareaCalificacionJuradoCoasesor(coasesor, tesis);
                                calificacionesJuradosNuevas.add(calJurado);
                            }
                        }
                    }


                    try {
                        timerGenerico.eliminarTimerPorParametroExterno("CMD_CREAR_TAREAS_JURADOD_TESIS-" + tesis.getId());
                    } catch (Exception ex) {
                        Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    calificacionesJuradosNuevas.addAll(tesis.getCalificacionesJurados());
                    tesis.setCalificacionesJurados(calificacionesJuradosNuevas);
                    tesis2Facade.edit(tesis);
                    //-------------------------------------------------------------------------------------------------------------------------------

                }


                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_JURADOS_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                throw new Exception("Tesis2Bean - comportamientoEmergenciaJuradosTesis");
            }
        } catch (Exception ex) {
            Logger.getLogger(Tesis2Bean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_JURADOS_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }



    }

    public String reprobarTesis2(String xml) {
        /**
         * consulta:
         * <idGeneral>1</idGeneral>
         * <calificacion>asdf</calificacion>
         * <correo>correo del asesor</correo>
         *
         */
        try {
            parser.leerXML(xml);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Secuencia secIdTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            if (secIdTesis != null) {

                Long id = Long.parseLong(secIdTesis.getValor().trim());
                Tesis2 tesis = tesis2Facade.find(id);
                if (tesis != null) {
//                    pendiente

                    if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_ESPERANDO_NOTA_FINAL_TESIS_2))) {
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00041, new ArrayList());
                    }

                    //colocar nota
                    Secuencia secNotaTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
                    Double d = Double.parseDouble(secNotaTesis.getValor().trim());
                    tesis.setCalificacion(d);
                    tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
                    tesis2Facade.edit(tesis);

                    // Realizar tareas
                    realizarTareasPendientes(secIdTesis.getValor(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR));
                    realizarTareasPendientes(secIdTesis.getValor(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SELECCIONAR_HORARIO_SUSTENTACION_TESIS2));
                    realizarTareasPendientes(secIdTesis.getValor(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_RESERVAR_SALON_PARA_SUSTENTACION_TESIS_2));
                    realizarTareasPendientes(secIdTesis.getValor(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_HORARIO_SUSTENTACION_TESIS2_ASESOR));
                    realizarTareasPendientes(secIdTesis.getValor(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PENDIENTE_ESPECIAL_TESIS2_COORDINACION));

                    eliminarTimersYTareasSustentacionTesis2(tesis);

                    // Eliminar timers de tesis2
                    eliminarTimerAnteriorSustentacionTesis2(id);

                    //: enviar correo estudiante informando nota de tesis
                    String asuntoCreacion = Notificaciones.ASUNTO_REPROBAR_TESIS_2;
                    String mensajeCreacion = String.format(Notificaciones.MENSAJE_REPROBAR_TESIS_2, tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos(), secNotaTesis.getValor());
                    correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                    //: enviar correo asesor informando nota de tesis
                    String asuntoCreacionAsesor = String.format(Notificaciones.ASUNTO_REPROBAR_TESIS_2_ASESOR, tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                    String mensajeCreacionAsesor = String.format(Notificaciones.MENSAJE_REPROBAR_TESIS_2_ASESOR, tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos(), secNotaTesis.getValor());
                    correoBean.enviarMail(tesis.getAsesor().getPersona().getCorreo(), asuntoCreacionAsesor, null, null, null, mensajeCreacionAsesor);

                    //crearTimerDeMigracionDeTesisFinalizada(id);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                }
                //enviar error tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            } //xml mal formado COM_ERR_0003
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String actualizarDetallesTesis2(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2));
            Secuencia secIdTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secTxtProyecto = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));
            Secuencia secHorarioTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));

            if (secIdTesis != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                Tesis2 tesis = tesis2Facade.find(id);
                tesis.setTemaProyecto(secTxtProyecto.getValor());
                tesis.setFechaPrevistaTerminacion(Timestamp.valueOf(secHorarioTesis.getValor()));
                tesis2Facade.edit(tesis);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), null, new ArrayList());
            } //xml mal formado COM_ERR_0003
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_TESIS_2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void realizarTareasPendientes(String idPG, String tipoTarea) {
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), idPG);
        completarTareaSencilla(tipoTarea, propiedades);
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

    public void crearTareaAsesorAprobarTesis2(Tesis2 tesis2, boolean vioTesis1SemestreAnterior) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS2_ASESOR);
        Persona profesor = tesis2.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Date fechaMaxAprobarInscripcion = new Date(tesis2.getSemestreInicio().getMaxFechaAprobacionTesis2().getTime());
        String mensajeBulletTarea = "";
        if (vioTesis1SemestreAnterior) {
            mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO, tesis2.getEstudiante().getPersona().getNombres() + " "
                    + tesis2.getEstudiante().getPersona().getApellidos(), tesis2.getEstudiante().getPersona().getCorreo());
        } else {
            mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO_ADVERTENCIA, tesis2.getEstudiante().getPersona().getNombres() + " "
                    + tesis2.getEstudiante().getPersona().getApellidos(), tesis2.getEstudiante().getPersona().getCorreo(), "No vio Tesis 1 en el periodo anterior");
        }
        boolean agrupable = true;
        String header = String.format(Notificaciones.MENSAJE_HEADER_APROBAR_INSCRIPCION_TESIS2_ASESOR, profesor.getNombres() + " " + profesor.getApellidos());
        String footer = Notificaciones.MENSAJE_FOOTER_APROBAR_INSCRIPCION_TESIS2_ASESOR;
        Timestamp fFin = new Timestamp(fechaMaxAprobarInscripcion.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_TESIS2_POR_ID);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis2.getId()));

        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);

        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_TESIS2_ASESOR;
        tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, parametros, asunto);
    }

    private void crearTareaACoordinacionDeAprobarPendienteEspecialTesis2(Tesis2 tesis) {

        //Crea tarea a coordinación para aprobar pendiente especial
        String nombreV = "Aprobar Pendiente Especial de Tesis 2";
        String descripcion = "Se debe Aceptar/Rechazar laS solicitudes de pendiente especial de Tesis 2 de los estudiantes: " + tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos() + " (" + tesis.getEstudiante().getPersona().getCorreo() + ")";

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PENDIENTE_ESPECIAL_TESIS2_COORDINACION);
        Persona profesor = tesis.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
        String mensajeBulletTarea = String.format(Notificaciones.MENSAJE_BULLET_SOLICITUD_PENDIENTE_TESIS1_COORDINACION, tesis.getEstudiante().getPersona().getNombres() + " "
                + tesis.getEstudiante().getPersona().getApellidos(), tesis.getEstudiante().getPersona().getCorreo(), profesor.getNombres() + " " + profesor.getApellidos(), profesor.getCorreo());

        boolean agrupable = true;
        String header = Notificaciones.MENSAJE_HEADER_SOLICITUD_PENDIENTE_ESPECIAL_TESIS2_COORDINACION;
        String footer = Notificaciones.MENSAJE_FOOTER_PENDIENTE_ESPECIAL_TESIS2_COORDINACION;
        Timestamp fFin = new Timestamp(fechaFinDate.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_TESIS2_PENDIENTE);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(tesis.getId()));
        String rol = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String asunto = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_TESIS2_COORDINACION;
        tareaBean.crearTareaRol(mensajeBulletTarea, tipo, rol, agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, parametros, asunto);
    }

    private void terminarTarea30PorcientoTesis2(Tesis2 tesis) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS_2);
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
        completarTareaSencilla(tipo, propiedades);
    }

    public void completarTareaCoordinacionSubirNotaTesis2(Tesis2 tesis) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_TESIS_2_COORDINACION);
        String rol = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
        tareaBean.realizarTareaPorRol(tipo, rol, params);
    }

    public void crearTareaProfesorJuradocalificarTesis(Tesis2 tesis, String hashSTring, Persona persona) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
        String mensajeHeader = String.format(Notificaciones.MENSAJE_HEADER_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE, persona.getNombres() + " " + persona.getApellidos());
        String asunto = Notificaciones.ASUNTO_CALIFICAR_SUSTENTACION_TESIS2_ESTUDIANTE;


        String nombreEstud = tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos();
        //---------------------------------------------------------
        String mensajeBullet = String.format(Notificaciones.MENSAJE_BULLET_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE, nombreEstud, sdfHMS.format(tesis.getHorarioSustentacion().getFechaSustentacion()),
                tesis.getHorarioSustentacion().getSalonSustentacion() != null ? tesis.getHorarioSustentacion().getSalonSustentacion() : " Pendiente");

        //----------------
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_SUSTENTACION_TESIS2);

        Date fechaInicioDate = new Date();
        //4.2 crear tarea...
        String nombreV = "Calificar Sustentación Tesis 2 " + tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos();
        String descripcion = "Se debe calificar la tesis.";
        String cmd = getConstanteBean().getConstante(Constantes.TAREA_TESIS_2_JURADO_INTERNO_CALIFICAR_SUSTENTACION);//TODO:cambiar el comando segun sea
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_CALIFICACION), hashSTring);
        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);

        //fecha fin
        Timestamp fFin = new Timestamp(tesis.getSemestreInicio().getFechaMaximaCalificarTesis2().getTime());

        //si la tesis esta en pendiente especial cambie la fecha fin
        if (tesis.getEstaEnPendienteEspecial()) {
            fFin = new Timestamp(tesis.getSemestreInicio().getFechaMaxSustentacionT2PendEspecial().getTime());
        }

        tareaBean.crearTareaPersona(mensajeBullet, tipo, persona.getCorreo(), false, mensajeHeader, Notificaciones.MENSAJE_FOOTER_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE, new Timestamp(fechaInicioDate.getTime()), fFin, cmd, rol, parametros, asunto);
        //----------------

        String correoMensaje = mensajeHeader + mensajeBullet + Notificaciones.MENSAJE_FOOTER_CALIFICAR_HORARIO_SUSTENTACION_TESIS2_ESTUDIANTE;
        correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, correoMensaje);
    }

    public void avisarAEstudianteProfesorYCoordinacionVencimientoTiempoSeleccionHorario(String periodo) {
        Collection<Tesis2> colTesis = tesis2Facade.findByEstadoYPeriodoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO), periodo);
        SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy", new Locale("es"));

        String estado = getConstanteBean().getConstante(Constantes.CTE_HORARIO_PENDIENTE);
        String correoCoordinador = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String estudiantesSinHorario = "";

        for (Tesis2 tesis : colTesis) {
            String asunto = Notificaciones.ASUNTO_VENCIMIENTO_PLAZO_SOLICITUD_HORARIO_SUSTENTACION;
            String mensaje = Notificaciones.MENSAJE_VENCIMIENTO_PLAZO_SOLICITUD_HORARIO_SUSTENTACION;
            if (tesis.getEstadoHorario() != null && tesis.getEstadoHorario().equals(estado)) {
                Persona persona = tesis.getEstudiante().getPersona();
                Persona asesor = tesis.getAsesor().getPersona();
                mensaje = mensaje.replaceFirst("%1", persona.getNombres() + " " + persona.getApellidos());
                mensaje = mensaje.replaceFirst("%2", sdfHMS.format(tesis.getSemestreInicio().getFechaUltimaSustentarTesis2()));
                correoBean.enviarMail(persona.getCorreo(), asunto, asesor.getCorreo() + "," + correoCoordinador, null, null, mensaje);
                estudiantesSinHorario += "<li>" + persona.getNombres() + " " + persona.getApellidos() + " , correo: " + persona.getCorreo() + "</li>";
            }
        }

        if (!estudiantesSinHorario.isEmpty()) {
            String asunto = Notificaciones.ASUNTO_VENCIMIENTO_PLAZO_SOLICITUD_HORARIO_SUSTENTACION_COORDINACION;
            String mensaje = Notificaciones.MENSAJE_VENCIMIENTO_PLAZO_SOLICITUD_HORARIO_SUSTENTACION_COORDINACION;

            correoBean.enviarMail(correoCoordinador, asunto, null, null, null, String.format(mensaje, estudiantesSinHorario));
        }
    }

    public String establecerAprobacionParadigma(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secAprobacionParadigma = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA));
            Secuencia secIdTesis = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            boolean aprobacionParadigma = secAprobacionParadigma.getValor().equals(getConstanteBean().getConstante(Constantes.TRUE)) ? true : false;
            Long idTesis2 = Long.parseLong(secIdTesis.getValor());
            Tesis2 tesis2 = tesis2Facade.find(idTesis2);



            //Para saber si tesis2.getRutaArticuloTesis() contiene solo el nombre del archivo o la ruta completa.
            String rutaCompleta = "";
            String rutaCarpeta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS2);
            if (tesis2.getRutaArticuloTesis().startsWith("/") || tesis2.getRutaArticuloTesis().startsWith("\\")) {
                rutaCompleta = tesis2.getRutaArticuloTesis();
            } else {
                rutaCompleta = rutaCarpeta + tesis2.getRutaArticuloTesis();
            }

            //comprueba si el archivo se encuentra en la ruta especificada
            File archivoArticulo = new File(rutaCompleta);
            if (archivoArticulo.exists()) {
                tesis2.setAprobadoParaParadigma(aprobacionParadigma);
                tesis2Facade.edit(tesis2);
                Persona asesor = tesis2.getAsesor().getPersona();
                Persona estudiante = tesis2.getEstudiante().getPersona();
                String mensajeCorreo = String.format(Notificaciones.MENSAJE_APROBACION_REVISTA_PARADIGMA_TESIS2, asesor.getNombres() + " " + asesor.getApellidos(), estudiante.getNombres() + " " + estudiante.getApellidos(), tesis2.getTemaProyecto(), tesis2.getAsesor().getGrupoInvestigacion().getNombre());
                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CORREO_REVISTA_PARADIGMA), Notificaciones.ASUNTO_APROBACION_REVISTA_PARADIGMA_TESIS2, null, null, rutaCompleta, mensajeCorreo);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0150, new LinkedList<Secuencia>());
            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00046, new ArrayList());
            }

        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ESTABLECER_APROBACION_REVISTA_PARADIGMA_TESIS2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MSJ_0151, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Cuando una tesis aún no ha sido calificada por los jurados, se ejecuta el timer que le envia al director de
     * la tesis una notificacion con los jurandos pendientes por asignar la nota, y programa nuevamente el timer
     * para dentro de los 3 dias siguientes.
     * @param id
     * @param vecesEjecutado
     */
    public void recordatorioDirectorNotasFaltantes(String id, String vecesEjecutado) {
        int veces = Integer.parseInt(vecesEjecutado) + 1;
        Tesis2 tesis = tesis2Facade.find(Long.parseLong(id));
        //coleccion de jurandos que faltan por dar su nota
        Collection<CalificacionJurado> faltantes = new ArrayList<CalificacionJurado>();

        Collection<CalificacionJurado> calificacionesJurados = tesis.getCalificacionesJurados();
        for (CalificacionJurado calificacionJurado : calificacionesJurados) {
            if (calificacionJurado.getNotaJurado() == null || calificacionJurado.getNotaJurado() <= 0) {
                faltantes.add(calificacionJurado);
            }
        }

        if (!faltantes.isEmpty()) {
            String juradosfaltantes = " <ul> ";
            for (CalificacionJurado faltante : faltantes) {
                if (faltante.getRolJurado().equals("Jurado Interno") || faltante.getRolJurado().equals("Asesor")) {
                    juradosfaltantes += " <li> " + faltante.getJuradoInterno().getPersona().getNombres() + " " + faltante.getJuradoInterno().getPersona().getApellidos()
                            + " (" + faltante.getRolJurado() + ") " + "<br />Correo: " + faltante.getJuradoInterno().getPersona().getCorreo();
                } else if (faltante.getRolJurado().equals("Jurado Externo")) {
                    juradosfaltantes += " <li> " + faltante.getJuradoExterno().getNombres() + " " + faltante.getJuradoExterno().getApellidos() + " ("
                            + faltante.getRolJurado() + ") " + "<br /> Correo: " + faltante.getJuradoExterno().getCorreo();
                } else if (faltante.getRolJurado().equals("Coasesor Tesis")) {
                    if (faltante.getCoasesor().getCoasesor() != null) {
                        juradosfaltantes += "<li> " + faltante.getCoasesor().getCoasesor().getPersona().getNombres() + " " + faltante.getCoasesor().getCoasesor().getPersona().getApellidos()
                                + " (" + faltante.getRolJurado() + ") " + "<br /> Correo: " + faltante.getCoasesor().getCoasesor().getPersona().getCorreo();
                    } else {
                        juradosfaltantes += "<li> " + faltante.getCoasesor().getNombres() + " " + faltante.getCoasesor().getApellidos() + " (" + faltante.getRolJurado() + ") "
                                + "<br /> Correo: " + faltante.getCoasesor().getCorreo();
                    }
                }
            }
            juradosfaltantes += " </ul>";

            String asunto = Notificaciones.ASUNTO_RECORDATORIO_NOTAS_FALTANTES;
            String mensaje = Notificaciones.MENSAJE_RECORDATORIO_NOTAS_FALTANTES;
            mensaje = mensaje.replaceFirst("%1", "\"" + tesis.getTemaProyecto() + "\"");
            mensaje = mensaje.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " "
                    + tesis.getEstudiante().getPersona().getApellidos());
            mensaje = mensaje.replaceFirst("%3", juradosfaltantes);
            String correodirector = tesis.getAsesor().getPersona().getCorreo();
            correoBean.enviarMail(correodirector, asunto, null, null, null, mensaje);
            Long undia = 1000L * 60 * 60 * 24;

            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 3)), getConstanteBean().getConstante(Constantes.CMD_RECORDATORIO_A_DIRECTOR_NOTAS_FALTANTES)
                    + "-" + id.toString() + "-" + veces, "TesisMaestria", this.getClass().getName(),
                    "comportamientoJuradosDiaSustentacion", "Este timer se crea para recordar al director de tesis que existen "
                    + "notas faltantes de los jurados");
        }
    }
}
