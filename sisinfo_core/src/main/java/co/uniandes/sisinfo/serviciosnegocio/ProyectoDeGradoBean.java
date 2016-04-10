package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.bo.AccionBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.CategoriaProyectoDeGrado;
import co.uniandes.sisinfo.entities.ComentarioTesisPregrado;
import co.uniandes.sisinfo.entities.PeriodoTesisPregrado;
import co.uniandes.sisinfo.entities.ProyectoDeGrado;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.TemaTesisPregrado;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.serviciosfuncionales.CategoriaProyectoDeGradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ComentarioTesisPregradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoTesisPregradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ProyectoDeGradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TemaTesisPregradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacade;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Servicios de administración de proyectos de grado
 * @author Ivan Mauricio Melo S, Marcela Morales, Paola Gómez
 */
@Stateless
public class ProyectoDeGradoBean implements ProyectoDeGradoBeanRemote, ProyectoDeGradoBeanLocal {

    //----------------------------------------------
    // CONSTANTES
    //----------------------------------------------
    private final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.ProyectoDeGradoBeanRemote";
    private final static String NOMBRE_METODO_TIMER = "manejoTimmersTesisPregrado";
    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Remotos
    @EJB
    private HistoricoTesisPregradoRemote historicoBean;
    @EJB
    private ProfesorFacadeRemote profesorFacade;
    @EJB
    private EstudianteFacadeRemote estudianteFacadeRemote;
    @EJB
    private PeriodoTesisPregradoFacadeLocal periodoFacadelocal;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacade;
    @EJB
    private PeriodoFacadeRemote periodoBaseFacadeRemote;
    //Locales
    @EJB
    private ProyectoDeGradoFacadeLocal proyectoGradoFacade;
    @EJB
    private CategoriaProyectoDeGradoFacadeLocal categoriaTesisFacade;
    @EJB
    private ComentarioTesisPregradoFacadeLocal comentarioTesisFacade;
    @EJB
    private TemaTesisPregradoFacadeLocal temaTesisFacade;
    //Útiles
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    @EJB
    private AccionVencidaBeanRemote accionVencidaBean;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TareaMultipleFacadeRemote tareaFacade;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;
    @EJB
    private TareaSencillaRemote tareaSencillaBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorProyectoDeGrado conversor;
    Collection<AccionBO> acciones;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    /**
     * Inicializa ProyectoDeGradoBean
     */
    public ProyectoDeGradoBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            estudianteFacadeRemote = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);
            nivelFormacionFacade = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            historicoBean = (HistoricoTesisPregradoRemote) serviceLocator.getRemoteEJB(HistoricoTesisPregradoRemote.class);
            accionVencidaBean = (AccionVencidaBeanRemote) serviceLocator.getRemoteEJB(AccionVencidaBeanRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            tareaFacade = (TareaMultipleFacadeRemote) serviceLocator.getRemoteEJB(TareaMultipleFacadeRemote.class);
            periodoBaseFacadeRemote = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            conversor = new ConversorProyectoDeGrado(getConstanteBean(), profesorFacade, categoriaTesisFacade, periodoFacadelocal, estudianteFacadeRemote);
        } catch (Exception e) {
            Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //----------------------------------------------
    // MÉTODOS
    //----------------------------------------------
    public String crearSolicitudTesisProyectoGrado(String xml) {
        try {
            getParser().leerXML(xml);

            Secuencia secProyectoGrado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            ProyectoDeGrado proyecto = getConversorProyectoDeGrado().pasarSecuenciaAProyectoDeGrado(secProyectoGrado);
            Estudiante estudiante = proyecto.getEstudiante();
            if (estudiante.getTipoEstudiante() == null) {
                NivelFormacion nivelFormacion = nivelFormacionFacade.findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PREGRADO));
                estudiante.setTipoEstudiante(nivelFormacion);
                estudianteFacadeRemote.edit(estudiante);
            }

            //Valida que el estudiante sea de pregrado
            if (!estudiante.getTipoEstudiante().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PREGRADO))) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0005, new ArrayList());
            }

            //Valida que la fecha se encuentre dentro de lo permitido
            PeriodoTesisPregrado periodo = proyecto.getSemestreIniciacion();
            Date hoy = new Date();
            if (hoy.after(periodo.getInscripcionTesisEstudiante())) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0001, new ArrayList());
            }

            //Valida que el tema esté entre los temas del semestre
            TemaTesisPregrado tema = temaTesisFacade.findByNombreYCorreoAsesorYPeriodo(proyecto.getTemaTesis(), proyecto.getAsesor().getPersona().getCorreo(), proyecto.getSemestreIniciacion().getNombre());
            if (tema == null) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0002, new ArrayList());
            }

            //Valida que el estudiante no tenga otro proyecto de grado en curso
            Collection<ProyectoDeGrado> otrasTesis = proyectoGradoFacade.findByCorreoEstudiante(proyecto.getEstudiante().getPersona().getCorreo());
            for (ProyectoDeGrado proyectoDeGrado : otrasTesis) {
                if (proyectoDeGrado.getSemestreIniciacion().getNombre().equals(proyecto.getSemestreIniciacion().getNombre())) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0003, new ArrayList());
                }
            }

            proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_APROBACION_COORDINACION));
            proyecto.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.FALSE));
            proyectoGradoFacade.create(proyecto);

            //Crea la tarea para que Coordinación apruebe la inscripción a proyecto de grado
            //regenerarTareaAprobarCoordinacion(proyecto.getSemestreIniciacion().getNombre());
            crearTareaAprobarProyectoGradoPorCoordinacion(proyecto);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void regenerarTareaAprobarCoordinacion(String periodo) {
        Collection<ProyectoDeGrado> proyectos = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_APROBACION_COORDINACION));
        PeriodoTesisPregrado periodoTesis = periodoFacadelocal.findByPeriodo(periodo);

        for (ProyectoDeGrado proyecto : proyectos) {
            crearTareaAprobarProyectoGradoPorCoordinacion(proyecto);
        }
    }

    public String aceptarSolicitudProyectoGradoAsesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdGeneral = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secAprobadoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR));
            Secuencia secCorreoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secIdGeneral != null && secAprobadoAsesor != null && secCorreoAsesor != null) {
                Long id = Long.parseLong(secIdGeneral.getValor().trim());
                String valor = secAprobadoAsesor.getValor().trim();
                String correo = secCorreoAsesor.getValor().trim();
                ProyectoDeGrado tesis = proyectoGradoFacade.find(id);
                if (tesis != null) {
                    if (tesis.getAsesor().getPersona().getCorreo().equals(correo)) {

                        //Realiza la tarea
                        Properties propiedades = new Properties();
                        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                        TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_ASESOR), propiedades));
                        if (borrar != null) {
                            tareaSencillaBean.realizarTareaPorId(borrar.getId());
                        }

                        Date hoy = new Date();
                        PeriodoTesisPregrado elPeriodoDeTesis = tesis.getSemestreIniciacion();
                        if (hoy.after(elPeriodoDeTesis.getAcesorAceptePy())) {
                            String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                            String login = tesis.getAsesor().getPersona().getCorreo();
                            String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ACEPTAR_SOLICITUD_PROYECTO_GRADO_ASESOR);
                            reportarEntregaTardia(elPeriodoDeTesis.getAcesorAceptePy(), comando, accion, login);
                        }

                        //Actualiza la información de la tesis según corresponda
                        if (valor.equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                            tesis.setAprobadoAsesor(valor);
                            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_INSCRIPCION_BANNER));
                            // tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA));
                            proyectoGradoFacade.edit(tesis);
                            // Crea la tarea de recordar al estudiante enviar documento propuesta.
                            //crearTareaEstudianteDocumentoPropuesta(tesis);
                            crearTareaCoordinacionInscripcionBanner(tesis);
                        } else if (valor.equals(getConstanteBean().getConstante(Constantes.FALSE))) {
                            tesis.setAprobadoAsesor(valor);
                            tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR));
                            proyectoGradoFacade.edit(tesis);

                            //Migra la tesis
                            tesis = proyectoGradoFacade.find(id);
                            migrarTesisRechazada(tesis);

                            //Crea correo para informar que se rechazo la inscripción
                            String asuntoCreacion = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO;
                            String mensajeCreacion = Notificaciones.MENSAJE_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO;
                            mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%", tesis.getAsesor().getPersona().getNombres() + " " + tesis.getAsesor().getPersona().getApellidos());

                            correoBean.enviarMail(tesis.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                        }
                        //ERROR: No se tienen permisos para aprobar la tesis
                    } else {
                        return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0007, new ArrayList());
                    }
                    //ERROR: No se encuentra la tesis con el id dado
                } else {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                }
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                //ERROR: Comando mal formado
            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Crea una tarea de enviar correo al estudiante para que envie documento de propuesta
     * @param proyecto
     */
    public String establecerFechasPeriodoPregrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secSemestre = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
            PeriodoTesisPregrado semestre = getConversorProyectoDeGrado().pasarSecuenciaAPeriodoConfiguracion(secSemestre);
            //TODO: aca cambiar las fechas para que sea hasta las 23:59:59 del dia antes seleccionado
            if (semestre == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHAS_PERIODO_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
            PeriodoTesisPregrado periodo = periodoFacadelocal.findByPeriodo(semestre.getNombre());
            boolean editado = false;
            if (periodo != null) {
                semestre.setId(periodo.getId());
                periodoFacadelocal.edit(semestre);
                editado = true;
            } else {
                periodoFacadelocal.create(semestre);
            }
            editarTimersPeriodo(semestre, editado);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHAS_PERIODO_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_FECHAS_PERIODO_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0002, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String confirmarInscripcionBannerProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdGeneral = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            if (secIdGeneral != null) {
                Long id = Long.parseLong(secIdGeneral.getValor().trim());
                ProyectoDeGrado tesis = proyectoGradoFacade.find(id);
                if (tesis != null) {


                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INSCRIBIR_BANNER), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }
                    tesis.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA));
                    proyectoGradoFacade.edit(tesis);
                    // Crea la tarea de recordar al estudiante enviar documento propuesta.
                    crearTareaEstudianteDocumentoPropuesta(tesis);

                } else {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                }
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                //ERROR: Comando mal formado
            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_SOLICITUD_TESIS_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void editarTimersPeriodo(PeriodoTesisPregrado semestre, boolean editado) {
        System.out.println("EDITANDO TIMERS PROYECTO DE GRADO");

        //MENSAJES ASOCIADOS A LOS TIMERS
        String mensajeAsociadoEnviarCorreoPlazoTemasProfesores = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_SUBIR_TEMAS_TESIS_PREGRADO) + "-" + semestre.getId();
        String msjRechazarTesisNoAceptadasParaProyectoDeGrado = getConstanteBean().getConstante(Constantes.CMD_RECHAZAR_TESIS_PENDIENTES_PERIODO) + "-" + semestre.getId();
        String mensajeAsociadoSubirTreintaPorCiento = getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO) + "-" + semestre.getId();
        String mensajeAsociadoEnviarInformeRetiro = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_RETIRO_PROYECTO_GRADO) + "-" + semestre.getId();
        String mensajeAsociadoSubirAfiche = getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO) + "-" + semestre.getId();
        String mensajeAsociadoMigrarProyectosGradoRetirados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS) + "-" + semestre.getId();
        String mensajeAsociadoMigrarProyectosGradoTerminados = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS) + "-" + semestre.getId();
        String mensajeAsociadoMigrarProyectosGradoPerdidos = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS) + "-" + semestre.getId();

        //SI SE EDITO EL PERIODO, SE DEDE ELIMINAR LOS TIMERS ANTERIORES
        if (editado) {
            //  TIMERS DE NOTIFICACION
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoEnviarCorreoPlazoTemasProfesores);
            timerGenerico.eliminarTimerPorParametroExterno(msjRechazarTesisNoAceptadasParaProyectoDeGrado);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoSubirTreintaPorCiento);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoEnviarInformeRetiro);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoSubirAfiche);
            //  TIMERS DE MIGRACION A HISTORICOS
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoMigrarProyectosGradoRetirados);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoMigrarProyectosGradoTerminados);
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociadoMigrarProyectosGradoPerdidos);
        }

        Long undia = 1000L * 60 * 60 * 24;

        //TIMERS PARA PUBLICACIÓN DE TEMAS
        Date hoy = new Date();
        if (new Timestamp(semestre.getPublicacionTemasTesis().getTime() - undia).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getPublicacionTemasTesis().getTime() - undia), mensajeAsociadoEnviarCorreoPlazoTemasProfesores,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea cuando se establecen nuevas fechas para el periodo y se enviara correo para asesores y estudiantes 1 dia antes recordando el Ãºltimo dia para subir temas tesis de pregrado");
        }
        if (new Timestamp(semestre.getPublicacionTemasTesis().getTime() - (undia * 3)).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getPublicacionTemasTesis().getTime() - (undia * 3)), mensajeAsociadoEnviarCorreoPlazoTemasProfesores,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea cuando se establecen nuevas fechas para el periodo y se enviara correo para asesores y estudiantes 3 dias antes recordando el Ãºltimo dia para subir temas tesis de pregrado");
        }
        if (new Timestamp(semestre.getPublicacionTemasTesis().getTime() - (undia * 5)).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getPublicacionTemasTesis().getTime() - (undia * 5)), mensajeAsociadoEnviarCorreoPlazoTemasProfesores,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea cuando se establecen nuevas fechas para el periodo y se enviara correo para asesores y estudiantes 5 dias antes recordando el Ãºltimo dia para subir temas tesis de pregrado");
        }
        if (new Timestamp(semestre.getPublicacionTemasTesis().getTime() - (undia * 7)).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getPublicacionTemasTesis().getTime() - (undia * 7)), mensajeAsociadoEnviarCorreoPlazoTemasProfesores,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "Este timer se crea cuando se establecen nuevas fechas para el periodo y se enviara correo para asesores y estudiantes 7 dias antes recordando el Ãºltimo dia para subir temas tesis de pregrado");
        }

        //TIMERS PARA RECHAZAR AUTOMATICAMENTE 3 DIAS DESPUÉS DE PASADA LA FECHA PARA ACEPTAR TESIS
        if (new Timestamp(semestre.getAcesorAceptePy().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getAcesorAceptePy().getTime() + (undia * 3)), msjRechazarTesisNoAceptadasParaProyectoDeGrado,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "Se crea timer para 3 dias despues de pasada la fecha para aceptar tesis, estas se rechazen automaticamente y se borren");
        }

        //TIMER PARA QUE EL PROFESOR SUBA EL 30%
        if (new Timestamp(semestre.getApreciacionCualitativa().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getApreciacionCualitativa().getTime() - (undia * 7)), mensajeAsociadoSubirTreintaPorCiento,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "La fecha mÃ¡xima para enviar la apreciaciÃ³n por parte del profesor cambiÃ³, se crea timer para 7 dias antes de la nueva fecha ejecutar acciones pertinentes");
        }

        //TIMER PARA QUE EL ESTUDIANTE ENVIE EL INFORME DE RETIRO
        if (new Timestamp(semestre.getInformeRetiro().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getInformeRetiro().getTime() - (undia * 7)), mensajeAsociadoEnviarInformeRetiro,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "La fecha mÃ¡xima para que el estudiante informe retiro cambiÃ³, se crea timer para 7 dias antes de la nueva fecha ejecutar acciones pertinentes");
        }

        //TIMER PARA QUE EL ESTUDIANTE SUBA EL AFICHE
        if (new Timestamp(semestre.getEntregaPoster().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getEntregaPoster().getTime() - (undia * 7)), mensajeAsociadoSubirAfiche,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "La fecha mÃ¡xima para que el estudiante entrege el poster cambiÃ³, se crea timer para 7 dias antes de la nueva fecha ejecutar acciones pertinentes");
        }

        //HISTORICOS DE TESIS PREGRADO
        if (new Timestamp(semestre.getLevantarPendiente().getTime()).after(hoy)) {
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getLevantarPendiente().getTime() + (undia * 20)), mensajeAsociadoMigrarProyectosGradoRetirados,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "La fecha mÃ¡xima para  cambiÃ³, se crea timer para ejecutar migraciÃ³n de tesis retiradas a histÃ³ricos 25 dias despues de la nueva fecha ");
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getLevantarPendiente().getTime() + (undia * 20)), mensajeAsociadoMigrarProyectosGradoTerminados,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "La fecha mÃ¡xima para cambiÃ³, se crea timer para ejecutar migraciÃ³n de tesis terminadas a histÃ³ricos 25 dias despues de la nueva fecha ");
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(semestre.getLevantarPendiente().getTime() + (undia * 20)), mensajeAsociadoMigrarProyectosGradoPerdidos,
                    "ProyectoDeGradoPregrado", this.getClass().getName(), "editarTimersPeriodo", "La fecha mÃ¡xima para cambiÃ³, se crea timer para ejecutar migraciÃ³n de tesis perdidas a histÃ³ricos 25 dias despues de la nueva fecha ");
        }

        //REGENERAR TAREAS
        regenerarTareas(semestre);
    }

    public String crearTemaProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTemaTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));

            TemaTesisPregrado tema = getConversorProyectoDeGrado().pasarSecuenciaATemaTesisPregrado(secTemaTesis);
            PeriodoTesisPregrado periodo = tema.getPeriodo();
            //Valida que el periodo exista
            if (periodo == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0004, new ArrayList());
            }
            //Valida que la fecha de publicación de tema de tesis exista
            if (periodo.getPublicacionTemasTesis() == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00024, new ArrayList());
            }
            //Valida que el periodo para publicación de tema se encuentre abierta
            if (periodo.getPublicacionTemasTesis().before(new Date())) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00017, new ArrayList());
            }

            if (tema.getId() != null) {
                temaTesisFacade.edit(tema);
            } else {
                temaTesisFacade.create(tema);
            }
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                ex.printStackTrace();
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_MODIFICAR_TEMA_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarTemaProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdTemaTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            TemaTesisPregrado lista = temaTesisFacade.find(Long.parseLong(secIdTemaTesis.getValor().trim()));
            Collection<ProyectoDeGrado> tesisEnConflicto = proyectoGradoFacade.findByTemaTesis(lista.getNombreTema());
            if (!tesisEnConflicto.isEmpty()) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TEMA_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00026, new ArrayList());
            }
            temaTesisFacade.remove(lista);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TEMA_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_TEMA_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darTemasProyectoGradoPorAsesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));

            Collection<TemaTesisPregrado> lista = temaTesisFacade.findByCorreoAsesor(secCorreoAsesor.getValor());
            Secuencia secTemasTesis = getConversorProyectoDeGrado().pasarTemasTesisPregradoASecuencia(lista);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secTemasTesis);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_PREGRADO_POR_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_PREGRADO_POR_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darTemaProyectoGradoPorId(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            TemaTesisPregrado tema = temaTesisFacade.find(secId.getValorLong());
            if (tema == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_TEMA_TESIS_PREGRADO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00044, new ArrayList());
            }
            Secuencia secTemaTesis = getConversorProyectoDeGrado().pasarTemaTesisPregradoASecuencia(tema);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secTemaTesis);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMA_TESIS_PREGRADO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMA_TESIS_PREGRADO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darProyectosDeGradoPorAsesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));

            Collection<ProyectoDeGrado> proyectos = proyectoGradoFacade.findByCorreoAsesor(secCorreo.getValor());
            Secuencia secPryectos = getConversorProyectoDeGrado().pasarProyectosDeGradoASecuencia(proyectos);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secPryectos);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darProyectoDeGradoPorId(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            ProyectoDeGrado proyecto = proyectoGradoFacade.find(secId.getValorLong());
            if (proyecto == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_PROYECTO_DE_GRADO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            }
            Secuencia secPryecto = getConversorProyectoDeGrado().pasarProyectoDeGradoASecuencia(proyecto);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secPryecto);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_PROYECTO_DE_GRADO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PROYECTO_DE_GRADO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darProyectoDegradoEstudainte(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));

            Collection<ProyectoDeGrado> proyectos = proyectoGradoFacade.findByCorreoEstudiante(secCorreo.getValor());
            Secuencia secPryectos = getConversorProyectoDeGrado().pasarProyectosDeGradoASecuencia(proyectos);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secPryectos);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darProyectosDegradoPorEstado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secEstado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));

            Collection<ProyectoDeGrado> proyectos = proyectoGradoFacade.findByEstado(secEstado.getValor());
            Secuencia secproyectos = getConversorProyectoDeGrado().pasarProyectosDeGradoASecuencia(proyectos);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secproyectos);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darConfiguracionPeriodo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secPeriodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));

            String nombrePeriodo = secPeriodo.getValor();
            PeriodoTesisPregrado periodo = periodoFacadelocal.findByPeriodo(nombrePeriodo);
            if (periodo == null) {
                PeriodoTesisPregrado periodoNuevo = new PeriodoTesisPregrado();
                periodoNuevo.setNombre(nombrePeriodo);
                periodoFacadelocal.create(periodoNuevo);
                periodo = periodoFacadelocal.findByPeriodo(nombrePeriodo);
            }
            Secuencia secResp = getConversorProyectoDeGrado().pasarPeriodoPregradoConfiguracionASecuencia(periodo);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secResp);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_CONFIGURACION_PERIODO_TESIS_PREGRADO_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CONFIGURACION_PERIODO_TESIS_PREGRADO_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void migrarTesisRechazada(ProyectoDeGrado tesis) {
        if (tesis != null) {
            try {
                ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
                Secuencia secTesis = getConversorProyectoDeGrado().pasarProyectoDeGradoASecuencia(tesis);
                secs.add(secTesis);

                String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RECHAZADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                String resp = historicoBean.pasarTesisPregradoRechazadaAHistorico(comandoXML);
                if (resp == null) {
                    String mensajeAsociadoMigrarTesisPregrado = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RECHAZADAS_A_HISTORICOS) + "-" + tesis.getId();
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarTesisPregrado,
                            "ProyectoDeGrado", this.getClass().getName(), "migrarTesisRechazada", "Este timer se crea porque el proceso de migrar la tesis de pregrado rechazada no fue exitoso");
                } else {
                    proyectoGradoFacade.remove(tesis);
                }
            } catch (Exception ex) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void migrarTesisRetiradas() {
        String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;

        Collection<ProyectoDeGrado> tesises = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PREGRADO_RETIRADA));
        if (tesises.isEmpty()) {
            String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS);
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                    "ProyectoDeGrado", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque no hay tesis de pregrado retiradas para migrar");
            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS_SIN_TESISES;
            asunto = asunto.replaceFirst("%1", "sin tesis retiradas por migrar");
            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS_SIN_TESISES;
            mensaje = mensaje.replaceFirst("%2", "no habia tesis retiradas a migrar");
            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
        } else {
            try {
                ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
                Secuencia secTesis = conversor.pasarProyectosDeGradoASecuencia(tesises);
                secs.add(secTesis);
                String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                String resp = historicoBean.pasarTesisPregradoRetiradaAHistorico(comandoXML);
                if (resp == null) {
                    String mensajeAsociadoMigrarTesisPregrado = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS);
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarTesisPregrado,
                            "ProyectoDeGrado", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque la migracion de tesis pregrado retiradas a historicos no fue exitoso");
                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                    asunto = asunto.replaceFirst("%1", "produjo error");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                    mensaje = mensaje.replaceFirst("%1", "sin embargo se produjo un error");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {
                    for (ProyectoDeGrado t : tesises) {
                        proyectoGradoFacade.remove(t);
                    }
                    //Collection<Tesis1> tesisEnCurso = tesis1Facade.findByEstadoYPeriodoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO), periodo);
                    Collection<ProyectoDeGrado> tesisEnCurso = proyectoGradoFacade.findByEstado("Esperando%");
                    if (tesisEnCurso.size() > 0) {
                        String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                                "ProyectoDeGrado", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque la migracion de tesis pregrado retiradas a historicos se realizo, "
                                + "sin embargo hay algunas tesis pregrado en curso y podrían ser retiradas luego no fueron migradas");
                        String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                        asunto = asunto.replaceFirst("%1", "con tesis que aún no han sido terminadas");
                        String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS_SIN_TESISES;
                        mensaje = mensaje.replaceFirst("%1", "sin embargo hay algunas tesis no estan terminadas y que podrían ser retiradas luego no fueron migradas");
                        correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                    }

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_A_HISTORICOS;
                    asunto = asunto.replaceFirst("%1", "retirados");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_A_HISTORICOS;
                    mensaje = mensaje.replaceFirst("%1", "retirados");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                }
            } catch (Exception ex) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void migrarTesisPerdidas() {
        String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        Double notaI = 0.0;
        Double notaF = 2.9;
        Collection<ProyectoDeGrado> tesises = proyectoGradoFacade.findByEstadoYRangoNota(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA), notaI.toString(), notaF.toString());

        if (tesises.isEmpty()) {
            String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS);
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                    "ProyectoDeGrado", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque no hay tesis de pregrado perdidas para migrar");

            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS_SIN_TESISES;
            asunto = asunto.replaceFirst("%1", "sin tesis perdidas por migrar");
            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS_SIN_TESISES;
            mensaje = mensaje.replaceFirst("%1", "no habia tesis perdidas a migrar");
            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
        } else {
            try {
                ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
                Secuencia secTesis = conversor.pasarProyectosDeGradoASecuencia(tesises);
                secs.add(secTesis);
                String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                String resp = historicoBean.pasarTesisPregradoPerdidaAHistorico(comandoXML);
                if (resp == null) {
                    String mensajeAsociadoMigrarTesisPregrado = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS);
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarTesisPregrado,
                            "ProyectoDeGrado", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque la migracion de tesis pregrado perdidas a historicos no fue exitoso");

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                    asunto = asunto.replaceFirst("%1", "produjo error");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                    mensaje = mensaje.replaceFirst("%1", "sin embargo se produjo un error");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {

                    for (ProyectoDeGrado t : tesises) {
                        proyectoGradoFacade.remove(t);
                    }

                    //Collection<Tesis1> tesisEnCurso = tesis1Facade.findByEstadoYPeriodoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO), periodo);
                    Collection<ProyectoDeGrado> tesisEnCurso = proyectoGradoFacade.findByEstado("Esperando%");


                    if (tesisEnCurso.size() > 0) {
                        String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                                "ProyectoDeGrado", this.getClass().getName(), "migrarTesisPerdidas", "Este timer se crea porque la migracion de tesis pregrado perdidas a historicos se realizo, "
                                + "sin embargo hay algunas tesis pregrado en curso y podrían ser perdidas luego no fueron migradas");
                        String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                        asunto = asunto.replaceFirst("%1", "con tesis que aún no han sido terminadas");
                        String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS_SIN_TESISES;
                        mensaje = mensaje.replaceFirst("%1", "sin embargo hay algunas tesis no estan terminadas y que podrían ser perdidas luego no fueron migradas");
                        correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                    }

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_A_HISTORICOS;
                    asunto = asunto.replaceFirst("%1", "perdidos");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_A_HISTORICOS;
                    mensaje = mensaje.replaceFirst("%2", "perdidos");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                }
            } catch (Exception ex) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void migrarTesisTerminadas() {
        String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        Long undia = Long.parseLong("1000") * 60 * 60 * 24;
        Double notaI = 3.0;
        Double notaF = 5.0;
        Collection<ProyectoDeGrado> tesises = proyectoGradoFacade.findByEstadoYRangoNota(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA), notaI.toString(), notaF.toString());

        if (tesises.isEmpty()) {
            String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS);
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                    "ProyectoDeGrado", this.getClass().getName(), "migrarTesisRetiradas", "Este timer se crea porque no hay tesis de pregrado terminadas para migrar");
            String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS_SIN_TESISES;
            asunto = asunto.replaceFirst("%1", "sin tesis terminadas por migrar");
            String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS_SIN_TESISES;
            mensaje = mensaje.replaceFirst("%1", "no habia tesis terminadas a migrar");
            correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
        } else {
            try {
                ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
                Secuencia secTesis = conversor.pasarProyectosDeGradoASecuencia(tesises);
                secs.add(secTesis);
                String comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                String resp = historicoBean.pasarTesisPregradoTerminadaAHistorico(comandoXML);
                if (resp == null) {
                    String mensajeAsociadoMigrarTesisPregrado = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS);
                    timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + 1000 * 60 * 10), mensajeAsociadoMigrarTesisPregrado,
                            "ProyectoDeGrado", this.getClass().getName(), "migrarTesisTerminadas", "Este timer se crea porque la migracion de tesis pregrado terminadas a historicos no fue exitoso");
                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS_SIN_TESISES;
                    asunto = asunto.replaceFirst("%1", "produjo error");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS_SIN_TESISES;
                    mensaje = mensaje.replaceFirst("%1", "sin embargo se produjo un error");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                } else {
                    for (ProyectoDeGrado t : tesises) {
                        proyectoGradoFacade.remove(t);
                    }
                    //Collection<Tesis1> tesisEnCurso = tesis1Facade.findByEstadoYPeriodoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO), periodo);
                    Collection<ProyectoDeGrado> tesisEnCurso = proyectoGradoFacade.findByEstado("Esperando%");
                    if (tesisEnCurso.size() > 0) {
                        String mensajeAsociadoMigrar = getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS);
                        timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(new Date().getTime() + (undia * 30)), mensajeAsociadoMigrar,
                                "ProyectoDeGrado", this.getClass().getName(), "migrarTesisTerminadas", "Este timer se crea porque la migracion de tesis pregrado terminadas a historicos se realizo, "
                                + "sin embargo hay algunas tesis pregrado en curso y podrían ser terminadas luego no fueron migradas");
                        String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS_SIN_TESISES;
                        asunto = asunto.replaceFirst("%1", "con tesis que aún no han sido terminadas");
                        String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS_SIN_TESISES;
                        mensaje = mensaje.replaceFirst("%1", "sin embargo hay algunas tesis no estan terminadas y que podrían ser terminadas luego no fueron migradas");
                        correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                    }

                    String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_A_HISTORICOS;
                    asunto = asunto.replaceFirst("%1", "terminados");
                    String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_A_HISTORICOS;
                    mensaje = mensaje.replaceFirst("%1", "terminados");
                    correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
                }
            } catch (Exception ex) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String darProyectosPregradoAMigrar(String xml) {
        try {
            parser.leerXML(xml);

            Collection<ProyectoDeGrado> tesises = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PREGRADO_RETIRADA));
            Collection<ProyectoDeGrado> tesisesT = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA));
            tesises.addAll(tesisesT);

            Secuencia secSoli = conversor.pasarProyectosDeGradoASecuencia(tesises);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secSoli);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TODOS_LOS_PROYECTOS_PREGRADO_A_MIGRAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TODOS_LOS_PROYECTOS_PREGRADO_A_MIGRAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String comportamientoEmergenciaMigrarProyectoDeGrado(String xml) {
        try {
            String retorno = null;
            Boolean migracionExitosa = null;
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null && secId.getValor() != null) {
                Long idTesis = Long.parseLong(secId.getValor());
                migracionExitosa = migrarProyectoDeGrado(idTesis);

                if (migracionExitosa == false) {
                    retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00042, new LinkedList<Secuencia>());
                    return retorno;
                }

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                throw new Exception("ProyectoDeGradoBean - comportamientoEmergenciaMigrarProyectoDeGrado");
            }
        } catch (Exception ex) {
            Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_LANZAR_COMPORTAMIENTO_EMERGENCIA_MIGRAR_TESIS_1), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public boolean migrarProyectoDeGrado(Long proyectoId) {
        boolean migracionCompletaExitosa = false;
        try {
            String correoSoporteSisinfo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);

            Collection<ProyectoDeGrado> tesises = new ArrayList<ProyectoDeGrado>();
            ProyectoDeGrado tesis = proyectoGradoFacade.find(proyectoId);
            tesises.add(tesis);

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            Secuencia secTesis = conversor.pasarProyectosDeGradoASecuencia(tesises);
            secs.add(secTesis);

            String estado = "";
            String comandoXML = "";
            String resp = "";
            if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PREGRADO_RETIRADA))) {
                estado = tesis.getEstadoTesis();
                comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                resp = historicoBean.pasarTesisPregradoRetiradaAHistorico(comandoXML);
            } else if (tesis.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA))) {
                if (tesis.getCalificacionTesis() < 3.0) {
                    estado = "Perdida";
                    comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                    resp = historicoBean.pasarTesisPregradoPerdidaAHistorico(comandoXML);
                } else {
                    estado = tesis.getEstadoTesis();
                    comandoXML = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secs);
                    resp = historicoBean.pasarTesisPregradoTerminadaAHistorico(comandoXML);
                }
            }

            if (resp == null) {
                String asunto = Notificaciones.ASUNTO_MIGRACION_TESIS_PREGRADO_A_HISTORICOS;
                asunto = asunto.replaceFirst("%1", estado);
                asunto = asunto.replaceFirst("%2", " no migro");
                String mensaje = Notificaciones.MENSAJE_MIGRACION_TESIS_PREGRADO_A_HISTORICOS_PROBLEMA;
                mensaje = mensaje.replaceFirst("%1", estado);
                mensaje = mensaje.replaceFirst("%2", tesis.getEstudiante().getPersona().getNombres() + " " + tesis.getEstudiante().getPersona().getApellidos());
                mensaje = mensaje.replaceFirst("%3", " sin embargo, el proyecto de pregrado no pudo se migrada, por tanto la información asociada no fue borrada de producción.");
                correoBean.enviarMail(correoSoporteSisinfo, asunto, null, null, null, mensaje);
            } else {
                proyectoGradoFacade.remove(tesises.iterator().next());
                migracionCompletaExitosa = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return migracionCompletaExitosa;
    }

    public void manejoTimmersTesisPregrado(String comando) {
        System.out.println("TIMMERS: " + comando);
        String[] parametros = comando.split("-");
        if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_ULTIMO_DIA_SUBIR_TEMAS_TESIS_PREGRADO))) {
            enviarCorreosPropuestasTesis(periodoFacadelocal.find(Long.parseLong(parametros[1].trim())));
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RECHAZADAS_A_HISTORICOS))) {
            migrarTesisRechazada(proyectoGradoFacade.find(Long.parseLong(parametros[1].trim())));
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_RETIRADAS_A_HISTORICOS))) {
            migrarTesisRetiradas();
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_TERMINADAS_A_HISTORICOS))) {
            migrarTesisTerminadas();
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_MIGRAR_TESIS_PREGRADO_PERDIDAS_A_HISTORICOS))) {
            migrarTesisPerdidas();
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_RECHAZAR_TESIS_PENDIENTES_PERIODO))) {
            rechazarTesisPendientesPregrado(periodoFacadelocal.find(Long.parseLong(parametros[1].trim())));
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO))) {
            crearTareasSubirTreintaPorCiento();
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_RETIRO_PROYECTO_GRADO))) {
            crearTareasEnviarInformeRetiroProyectoGrado(periodoFacadelocal.find(Long.parseLong(parametros[1].trim())));
        } else if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO))) {
            enviarInformeRetiroProyectoGradoPorDefecto();
            crearTareasSubirAfiche(periodoFacadelocal.find(Long.parseLong(parametros[1].trim())));
        }
    }

    private void enviarCorreosPropuestasTesis(PeriodoTesisPregrado find) {
        Collection<Profesor> profesores = profesorFacade.findByNivelPlanta(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_TITULAR));
        profesores.addAll(profesorFacade.findByNivelPlanta(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ASOCIADO)));
        profesores.addAll(profesorFacade.findByNivelPlanta(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ASISTENTE)));
        profesores.addAll(profesorFacade.findByNivelPlanta(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_INSTRUCTOR)));
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd");
        for (Profesor profesor : profesores) {
            Persona persona = profesor.getPersona();
            Collection<TemaTesisPregrado> temas = temaTesisFacade.findByPeriodoYCorreoAsesor(persona.getCorreo(), find.getNombre());
            if (temas == null || temas.isEmpty()) {
                String asunto = Notificaciones.ASUNTO_SUBIR_PROPUESTAS_TESIS_PREGRADO;
                String mensaje = Notificaciones.MENSAJE_SUBIR_PROPUESTAS_TESIS_PEGRADO;
                mensaje = mensaje.replaceFirst("%", persona.getNombres() + " " + persona.getApellidos());
                mensaje = mensaje.replaceFirst("%", find.getNombre());
                mensaje = mensaje.replaceFirst("%", sdfHMS.format(find.getPublicacionTemasTesis()));
                correoBean.enviarMail(persona.getCorreo(), asunto, null, null, null, mensaje);
            }
        }
    }

    public String darPeriodosConfigurados(String xml) {
        try {
            parser.leerXML(xml);
            Collection<PeriodoTesisPregrado> periodos = periodoFacadelocal.findAll();
            Secuencia secPeriodos = getConversorProyectoDeGrado().pasarPeriodosPregradoASecuencia(periodos);

            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secPeriodos);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_CONFIGURADOS_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_CONFIGURADOS_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public String consultarCateoriasProyectosDeGrado(String xml) {
        try {
            parser.leerXML(xml);
            Collection<CategoriaProyectoDeGrado> categorias = categoriaTesisFacade.findAll();
            Secuencia secCategorias = getConversorProyectoDeGrado().pasarCategoriasProyectoDeGradoASecuencias(categorias);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secCategorias);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_CATEGORIAS_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CATEGORIAS_TESIS_PREGRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public String darTodosLosTemasDeProyectoDeGrado(String xml) {
        try {
            parser.leerXML(xml);
            Collection<TemaTesisPregrado> lista = temaTesisFacade.findAll();
            Secuencia secTemasTesis = getConversorProyectoDeGrado().pasarTemasTesisPregradoASecuencia(lista);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secTemasTesis);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_PREGRADO_TODOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TEMAS_TESIS_PREGRADO_TODOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void rechazarTesisPendientesPregrado(PeriodoTesisPregrado find) {
        Collection<ProyectoDeGrado> proyectos = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
        for (ProyectoDeGrado proyectoDeGrado : proyectos) {
            if (proyectoDeGrado.getSemestreIniciacion().getId().equals(find.getId())) {
                proyectoDeGrado.setAprobadoAsesor(Boolean.FALSE.toString());
                proyectoDeGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR));
                proyectoGradoFacade.edit(proyectoDeGrado);
                proyectoDeGrado = proyectoGradoFacade.find(proyectoDeGrado.getId());
                migrarTesisRechazada(proyectoDeGrado);

                //Crear correo para informar que se rechazo la inscripción
                String asuntoCreacion = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO;
                String mensajeCreacion = Notificaciones.MENSAJE_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO;
                mensajeCreacion = mensajeCreacion.replaceFirst("%", proyectoDeGrado.getEstudiante().getPersona().getNombres() + " " + proyectoDeGrado.getEstudiante().getPersona().getApellidos());
                mensajeCreacion = mensajeCreacion.replaceFirst("%", proyectoDeGrado.getAsesor().getPersona().getNombres() + " " + proyectoDeGrado.getAsesor().getPersona().getApellidos());
                correoBean.enviarMail(proyectoDeGrado.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }
        }
    }

    public String enviarPropuestaProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTesisPregrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            Secuencia secIdTesis = secTesisPregrado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secNombreArchivoPropuesta = secTesisPregrado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_PROPUESTA_TESIS_PREGRADO));
            Long id = Long.parseLong(secIdTesis.getValor().trim());
            ProyectoDeGrado proyecto = proyectoGradoFacade.find(id);
            String rutaPropuesta = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVOS_TESIS_PREGRADO) + secNombreArchivoPropuesta.getValor();
            boolean existe = getConversorProyectoDeGrado().existeArchivo(rutaPropuesta);

            if (existe) {
                //Revisa que la tesis tenga el estado esperado para esta operacion
                if (!proyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA))) {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROPUESTA_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0006, new ArrayList());
                }

                Date hoy = new Date();
                PeriodoTesisPregrado elPeriodoDeTesis = proyecto.getSemestreIniciacion();
                if (hoy.after(elPeriodoDeTesis.getEnvioPropuestaPyEstud())) {
                    String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                    String login = proyecto.getEstudiante().getPersona().getCorreo();
                    String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_PROPUESTA_PROYECTO_GRADO);
                    reportarEntregaTardia(elPeriodoDeTesis.getEnvioPropuestaPyEstud(), comando, accion, login);
                }

                //Actualiza la información del proyecto de grado
                proyecto.setRutaArchivoPropuesta(rutaPropuesta);
                proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_PROPUESTA_ENVIADA_ESPERANDO_APROBACION_ASESOR));
                proyectoGradoFacade.edit(proyecto);

                //Realiza la tarea
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR), propiedades));
                if (borrar != null) {
                    tareaSencillaBean.realizarTareaPorId(borrar.getId());
                }

                //Crea la tarea al asesor para que apruebe la propuesta
                crearTareaAprobarReprobarPropuesta(proyecto);

                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROPUESTA_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROPUESTA_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROPUESTA_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String enviarAficheProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTesisPregrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            Secuencia secIdTesis = secTesisPregrado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secNombreArchivoPropuesta = secTesisPregrado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_POSTER));
            Long id = Long.parseLong(secIdTesis.getValor().trim());
            ProyectoDeGrado proyecto = proyectoGradoFacade.find(id);

            if (proyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER))) {
                String rutaAficheTesis = getConstanteBean().getConstante(Constantes.RUTA_AFICHES_TESIS_PREGRADO) + proyecto.getSemestreIniciacion().getNombre() + "/" + secNombreArchivoPropuesta.getValor();
                boolean existe = getConversorProyectoDeGrado().existeArchivo(rutaAficheTesis);
                if (existe) {
                    Date hoy = new Date();
                    PeriodoTesisPregrado elPeriodoDeTesis = proyecto.getSemestreIniciacion();
                    if (hoy.after(elPeriodoDeTesis.getEntregaPoster())) {
                        String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                        String login = proyecto.getAsesor().getPersona().getCorreo();
                        String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_AFICHE_PROYECTO_GRADO);
                        reportarEntregaTardia(elPeriodoDeTesis.getEntregaPoster(), comando, accion, login);
                    }

                    //Actualiza la información
                    proyecto.setRutaPosterTesis(rutaAficheTesis);
                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR));
                    proyectoGradoFacade.edit(proyecto);

                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Crea la tarea al asesor para que apruebe el afiche
                    crearTareaAprobarReprobarAfiche(proyecto);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } //Estado no válido
                else {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00035, new ArrayList());
                }
            } //Validación de estados (PENDIENTE)
            else if (proyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE))) {
                String rutaAficheTesis = getConstanteBean().getConstante(Constantes.RUTA_AFICHES_TESIS_PREGRADO) + proyecto.getSemestreIniciacion().getNombre() + "/" + secNombreArchivoPropuesta.getValor();

                boolean existe = getConversorProyectoDeGrado().existeArchivo(rutaAficheTesis);
                if (existe) {
                    Date hoy = new Date();
                    PeriodoTesisPregrado elPeriodoDeTesis = proyecto.getSemestreIniciacion();
                    if (hoy.after(elPeriodoDeTesis.getEntregaPosterPendiente())) {
                        String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                        String login = proyecto.getAsesor().getPersona().getCorreo();
                        String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_AFICHE_PROYECTO_GRADO);
                        reportarEntregaTardia(elPeriodoDeTesis.getEntregaPosterPendiente(), comando, accion, login);
                    }

                    //Actualiza la información
                    proyecto.setRutaPosterTesis(rutaAficheTesis);
                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE));
                    proyectoGradoFacade.edit(proyecto);

                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Crea la tarea al asesor para que apruebe el afiche en pendiente
                    crearTareaAprobarReprobarAfichePendiente(proyecto);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } //Estado no válido
                else {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00035, new ArrayList());
                }
            } //PENDIENTE ESPECIAL
            else if (proyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE_ESPECIAL))) {
                String rutaAficheTesis = getConstanteBean().getConstante(Constantes.RUTA_AFICHES_TESIS_PREGRADO) + proyecto.getSemestreIniciacion().getNombre() + "/" + secNombreArchivoPropuesta.getValor();
                boolean existe = getConversorProyectoDeGrado().existeArchivo(rutaAficheTesis);

                if (existe) {
                    Date hoy = new Date();
                    PeriodoTesisPregrado elPeriodoDeTesis = proyecto.getSemestreIniciacion();
                    if (hoy.after(elPeriodoDeTesis.getEntregaPosterPendienteEspecial())) {
                        String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                        String login = proyecto.getAsesor().getPersona().getCorreo();
                        String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_AFICHE_PROYECTO_GRADO);
                        reportarEntregaTardia(elPeriodoDeTesis.getEntregaPosterPendienteEspecial(), comando, accion, login);
                    }

                    //Actualiza la información
                    proyecto.setRutaPosterTesis(rutaAficheTesis);
                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE_ESPECIAL));
                    proyectoGradoFacade.edit(proyecto);

                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE_ESPECIAL), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Crea la tarea al asesor para que apruebe el afiche en pendiente especial
                    crearTareaAprobarReprobarAfichePendienteEspecial(proyecto);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE_ESPECIAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } //Estado no válido
                else {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE_ESPECIAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00035, new ArrayList());
                }
            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00035, new ArrayList());
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String enviarAprobacionAficheProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdProyectoDeGrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secAprobado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADA_PROPUESTA_ASESOR));

            Long id = Long.parseLong(secIdProyectoDeGrado.getValor().trim());
            Boolean aprobado = Boolean.parseBoolean(secAprobado.getValor().trim());
            ProyectoDeGrado elProyecto = proyectoGradoFacade.find(id);
            PeriodoTesisPregrado elPeriodoDeTEsis = elProyecto.getSemestreIniciacion();
            Date hoy = new Date();

            if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR))) {
                PeriodoTesisPregrado elPeriodoDeTesis = elProyecto.getSemestreIniciacion();
                if (hoy.after(elPeriodoDeTesis.getDarVistoBuenoPoster())) {
                    String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                    String login = elProyecto.getEstudiante().getPersona().getCorreo();
                    String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_APROBACION_AFICHE_PROYECTO_GRADO);
                    reportarEntregaTardia(elPeriodoDeTesis.getDarVistoBuenoPoster(), comando, accion, login);
                }

                if (aprobado) {
                    //Actualiza la información
                    elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA));
                    elProyecto.setEstadoPoster(true);
                    proyectoGradoFacade.edit(elProyecto);

                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Notifica al estudiante que el asesor aprobó la propuesta de Afiche
                    String asunto = Notificaciones.ASUNTO_AFICHE_PROYECTO_DE_GRADO_APROBADO;
                    String mensaje = Notificaciones.MENSAJE_AFICHE_PROYECTO_DE_GRADO_APROBADO;
                    mensaje = mensaje.replaceFirst("%", elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos());
                    mensaje = mensaje.replaceFirst("%", elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(elProyecto.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                    //Crea la tarea al asesor para que suba la nota
                    crearTareaSubirNotaProyectoGrado(elProyecto);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                } else {
                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Actualiza la información
                    elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER));
                    elProyecto.setEstadoPoster(false);
                    proyectoGradoFacade.edit(elProyecto);

                    //Notifica al estudiante que el asesor rechazo el Afiche
                    String asunto = Notificaciones.ASUNTO_AFICHE_PROYECTO_DE_GRADO_RECHAZADO;
                    String mensaje = Notificaciones.MENSAJE_AFICHE_PROYECTO_DE_GRADO_RECHAZADO;
                    mensaje = mensaje.replaceFirst("%", elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos());
                    mensaje = mensaje.replaceFirst("%", elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(elProyecto.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                    //Recrea la tarea al estudiante para que suba el afiche
                    crearTareaSubirAfiche(elProyecto);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            } //PENDIENTE
            else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE))) {
                PeriodoTesisPregrado elPeriodoDeTesis = elProyecto.getSemestreIniciacion();
                if (hoy.after(elPeriodoDeTesis.getDarVistoBuenoPosterPendiente())) {
                    String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                    String login = elProyecto.getAsesor().getPersona().getCorreo();
                    String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_APROBACION_AFICHE_PROYECTO_GRADO);
                    reportarEntregaTardia(elPeriodoDeTesis.getDarVistoBuenoPosterPendiente(), comando, accion, login);
                }

                if (aprobado) {
                    //Actualiza la información
                    elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE));
                    proyectoGradoFacade.edit(elProyecto);

                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Crea la tarea para subir nota de pendiente
                    crearTareaSubirNotaProyectoGradoPendiente(elProyecto);

                    //Notifica al estudiante que el asesor aprobó la propuesta de Afiche
                    String asunto = Notificaciones.ASUNTO_AFICHE_PROYECTO_DE_GRADO_APROBADO;
                    String mensaje = Notificaciones.MENSAJE_AFICHE_PROYECTO_DE_GRADO_APROBADO;
                    mensaje = mensaje.replaceFirst("%", elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos());
                    mensaje = mensaje.replaceFirst("%", elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(elProyecto.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                } else {
                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Actualiza la información
                    elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE));
                    proyectoGradoFacade.edit(elProyecto);

                    //Notifica al estudiante que el asesor rechazo el Afiche en pendiente
                    String asunto = Notificaciones.ASUNTO_AFICHE_PROYECTO_DE_GRADO_RECHAZADO;
                    String mensaje = Notificaciones.MENSAJE_AFICHE_PROYECTO_DE_GRADO_RECHAZADO;
                    mensaje = mensaje.replaceFirst("%", elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos());
                    mensaje = mensaje.replaceFirst("%", elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(elProyecto.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                    //Recrea la tarea al estudiante para que suba el afiche en pendiente
                    crearTareaSubirAfichePendiente(elProyecto);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            } //PENDIENTE ESPECIAL
            else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE_ESPECIAL))) {
                PeriodoTesisPregrado elPeriodoDeTesis = elProyecto.getSemestreIniciacion();
                if (hoy.after(elPeriodoDeTesis.getDarVistoBuenoPosterPendienteEspecial())) {
                    String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                    String login = elProyecto.getAsesor().getPersona().getCorreo();
                    String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_APROBACION_AFICHE_PROYECTO_GRADO);
                    reportarEntregaTardia(elPeriodoDeTesis.getDarVistoBuenoPosterPendienteEspecial(), comando, accion, login);
                }

                if (aprobado) {
                    //cambiar estado a esperando por nota
                    elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE_ESPECIAL));
                    proyectoGradoFacade.edit(elProyecto);

                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE_ESPECIAL), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Crea la tarea para subir nota de pendiente especial
                    crearTareaSubirNotaProyectoGradoPendienteEspecial(elProyecto);

                    //Notifica al estudiante que el asesor aprobó la propuesta de Afiche
                    String asunto = Notificaciones.ASUNTO_AFICHE_PROYECTO_DE_GRADO_APROBADO;
                    String mensaje = Notificaciones.MENSAJE_AFICHE_PROYECTO_DE_GRADO_APROBADO;
                    mensaje = mensaje.replaceFirst("%", elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos());
                    mensaje = mensaje.replaceFirst("%", elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(elProyecto.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE_ESPECIAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                } else {
                    //Realiza la tarea
                    Properties propiedades = new Properties();
                    propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                    TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE_ESPECIAL), propiedades));
                    if (borrar != null) {
                        tareaSencillaBean.realizarTareaPorId(borrar.getId());
                    }

                    //Actualiza la información
                    elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE_ESPECIAL));
                    proyectoGradoFacade.edit(elProyecto);

                    //Notifica al estudiante que el asesor rechazo el Afiche
                    String asunto = Notificaciones.ASUNTO_AFICHE_PROYECTO_DE_GRADO_RECHAZADO;
                    String mensaje = Notificaciones.MENSAJE_AFICHE_PROYECTO_DE_GRADO_RECHAZADO;
                    mensaje = mensaje.replaceFirst("%", elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos());
                    mensaje = mensaje.replaceFirst("%", elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(elProyecto.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                    //Recrea la tarea al estudiante para que suba el afiche en pendiente especial
                    crearTareaSubirAfichePendienteEspecial(elProyecto);

                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE_ESPECIAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            } else {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0010, new ArrayList());
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String aceptarPropuestaTesisPorAsesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdProyectoDeGrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secAprobado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADA_PROPUESTA_ASESOR));

            Long id = Long.parseLong(secIdProyectoDeGrado.getValor().trim());
            Boolean aprobado = Boolean.parseBoolean(secAprobado.getValor().trim());
            ProyectoDeGrado elProyecto = proyectoGradoFacade.find(id);
            PeriodoTesisPregrado elPeriodoDeTEsis = elProyecto.getSemestreIniciacion();
            Date hoy = new Date();
            if (hoy.after(elPeriodoDeTEsis.getValidacionAsesorPropuestaDeProyecto())) {
                String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                String correo = elProyecto.getAsesor().getPersona().getCorreo();
                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ACEPTAR_PROPUESTA_TESIS_POR_ASESOR);
                reportarEntregaTardia(elPeriodoDeTEsis.getValidacionAsesorPropuestaDeProyecto(), comando, accion, correo);
            }

            if (aprobado) {
                //Actualiza la información
                elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_REPORTE_TREINTA_POR_CIENTO));
                proyectoGradoFacade.edit(elProyecto);

                //Realiza la tarea
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR), propiedades));
                if (borrar != null) {
                    tareaSencillaBean.realizarTareaPorId(borrar.getId());
                }

                //Envía un correo informando que la propuesta fue aprobada
                String asunto = Notificaciones.ASUNTO_PROPUESTA_PROYECTO_DE_GRADO_APROBADA;
                String mensaje = Notificaciones.MENSAJE_PROPUESTA_PROYECTO_DE_GRADO_APROBADA;
                mensaje = mensaje.replaceFirst("%", elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(elProyecto.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACEPTAR_PROPUESTA_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                //Realiza la tarea
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR), propiedades));
                if (borrar != null) {
                    tareaSencillaBean.realizarTareaPorId(borrar.getId());
                }

                //Actualiza la información
                elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA));
                proyectoGradoFacade.edit(elProyecto);


                //Envía un correo informando que la propuesta fue reprobada
                String asunto = Notificaciones.ASUNTO_PROPUESTA_PROYECTO_DE_GRADO_RECHAZADA;
                String mensaje = Notificaciones.MENSAJE_PROPUESTA_PROYECTO_DE_GRADO_RECHAZADA;
                mensaje = mensaje.replaceFirst("%", elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos());
                correoBean.enviarMail(elProyecto.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                //Recrea la tarea al estudiante para que suba la propuesta
                crearTareaEstudianteDocumentoPropuesta(elProyecto);

                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACEPTAR_PROPUESTA_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACEPTAR_PROPUESTA_PROYECTO_DE_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darProyectosDeGradoCoordinador(String xml) {
        try {
            parser.leerXML(xml);
            Collection<ProyectoDeGrado> proyectos = proyectoGradoFacade.findAll();
            Secuencia secproyectos = getConversorProyectoDeGrado().pasarProyectosDeGradoASecuencia(proyectos);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secproyectos);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_PROYECTOS_DE_GRADO_POR_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_PROYECTOS_DE_GRADO_POR_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String agregarComentarioProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null) {
                Long idTesis = Long.parseLong(secId.getValor().trim());
                ProyectoDeGrado proyectoGrado = proyectoGradoFacade.find(idTesis);
                if (proyectoGrado != null) {

                    //Validación de estados
                    if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_REPORTE_TREINTA_POR_CIENTO))) {

                        Secuencia secComentarioTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO_PROYECTO_GRADO));
                        ComentarioTesisPregrado coment = getConversorProyectoDeGrado().pasarSecuenciaAComentarioProyectoGrado(secComentarioTesis);
                        Date hoy = new Date();
                        PeriodoTesisPregrado elPeriodoDeTesis = proyectoGrado.getSemestreIniciacion();
                        if (hoy.after(elPeriodoDeTesis.getApreciacionCualitativa())) {
                            String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                            String login = proyectoGrado.getAsesor().getPersona().getCorreo();
                            String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_AGREGAR_COMENTARIO_PROYECTO_GRADO);
                            reportarEntregaTardia(elPeriodoDeTesis.getApreciacionCualitativa(), comando, accion, login);
                        }

                        if (coment.getId() != null) {
                            coment.setFecha(new Timestamp(new Date().getTime()));
                            comentarioTesisFacade.edit(coment);
                            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                            proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO));
                            proyectoGradoFacade.edit(proyectoGrado);

                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), idTesis.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                        } else {
                            coment.setFecha(new Timestamp(new Date().getTime()));
                            comentarioTesisFacade.create(coment);
                            Collection<ComentarioTesisPregrado> comentsPG = proyectoGrado.getComentariosAsesor();
                            comentsPG.add(coment);
                            proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO));
                            proyectoGradoFacade.edit(proyectoGrado);
                            ArrayList<Secuencia> param = new ArrayList<Secuencia>();

                            //envía mensaje al estudiante
                            String asuntoEstudiante = Notificaciones.ASUNTO_INFORME_30PORCIENTO_PROYECTO_GRADO;
                            String mensajeEstudiante = Notificaciones.MENSAJE_INFORME_30PORCIENTO_PROYECTO_GRADO;
                            mensajeEstudiante = mensajeEstudiante.replaceFirst("%1", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            mensajeEstudiante = mensajeEstudiante.replaceFirst("%2", coment.getDebeRetirar() ? "<strong>[LE HA SUGERIDO QUE RETIRE PROYECTO DE GRADO]</strong>" : "");
                            mensajeEstudiante = mensajeEstudiante.replaceFirst("%3", coment.getComentario());
                            correoBean.enviarMail(proyectoGrado.getEstudiante().getPersona().getCorreo(), asuntoEstudiante, null, null, null, mensajeEstudiante);

                            //envía mensaje a coordinación
                            String asuntoCoord = Notificaciones.ASUNTO_INFORME_30PORCIENTO_PROYECTO_GRADO_COORD;
                            asuntoCoord = asuntoCoord.replaceFirst("%1", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            String mensajeCoord = Notificaciones.MENSAJE_INFORME_30PORCIENTO_PROYECTO_GRADO_COORD;
                            mensajeCoord = mensajeCoord.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                            mensajeCoord = mensajeCoord.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            mensajeCoord = mensajeCoord.replaceFirst("%3", coment.getDebeRetirar() ? "<strong>[LE HA SUGERIDO QUE RETIRE PROYECTO DE GRADO 1]</strong>" : "");
                            mensajeCoord = mensajeCoord.replaceFirst("%4", coment.getComentario());
                            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCoord, null, null, null, mensajeCoord);

                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), idTesis.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                        }
                    } else {
                        //ERROR: El estado debería ser CTE_TESIS_PY_ESPERANDO_REPORTE_TREINTA_POR_CIENTO.
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0009, new ArrayList());
                    }
                }
                //ERROR: Tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0008, new ArrayList());
            }
            //ERROR: XML mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darComentariosProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null) {
                Long idPG = Long.parseLong(secId.getValor().trim());
                ProyectoDeGrado proyectoGrado = proyectoGradoFacade.find(idPG);
                Collection<ComentarioTesisPregrado> coments = proyectoGrado.getComentariosAsesor();
                Secuencia seccoemtns = getConversorProyectoDeGrado().pasarComentariosTesisPregradoASecuencia(coments);
                ArrayList<Secuencia> param = new ArrayList<Secuencia>();
                param.add(seccoemtns);
                return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String subirDocumentoABETProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secTesisPregrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVO_ABET));
            Secuencia secIdTesis = secTesisPregrado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secNombreArchivoABET = secTesisPregrado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_ABET));
            if (secIdTesis != null && secNombreArchivoABET != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                ProyectoDeGrado proyecto = proyectoGradoFacade.find(id);
                if (proyecto != null) {

                    //Validación de estados (NORMAL)
                    if (proyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET))) {
                        Date hoy = new Date();
                        PeriodoTesisPregrado elPeriodoDeTesis = proyecto.getSemestreIniciacion();
                        if (hoy.after(elPeriodoDeTesis.getRubricaABET())) {

                            String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                            String login = proyecto.getAsesor().getPersona().getCorreo();
                            String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SUBIR_DOCUMENTO_ABET_PROYECTO_GRADO);
                            reportarEntregaTardia(elPeriodoDeTesis.getRubricaABET(), comando, accion, login);
                        }
                        String rutaABET = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ABET) + secNombreArchivoABET.getValor();
                        boolean existe = getConversorProyectoDeGrado().existeArchivo(rutaABET);
                        if (existe) {
                            proyecto.setRutaABET(rutaABET);
                            Double nota = proyecto.getCalificacionTesis();
                            if (nota != null) {
                                if (nota >= 3) {
                                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA));
                                } else {
                                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
                                }
                            } else {
                                proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA));
                            }
                            proyectoGradoFacade.edit(proyecto);

                            //Enviar correo a coordinación notificando
                            String asunto = Notificaciones.ASUNTO_INFORME_ABET_COORD;
                            String mensaje = Notificaciones.MENSAJE_INFORME_ABET_COORD;
                            asunto = asunto.replaceFirst("%1", proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos());
                            mensaje = mensaje.replaceFirst("%1", proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos());
                            mensaje = mensaje.replaceFirst("%2", proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos());
                            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asunto, null, null, null, mensaje);

                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                        }
                    } //Validación de estados (PENDIENTE)
                    else if (proyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE))) {
                        String rutaABET = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ABET) + secNombreArchivoABET.getValor();
                        boolean existe = getConversorProyectoDeGrado().existeArchivo(rutaABET);
                        if (existe) {

                            Date hoy = new Date();
                            PeriodoTesisPregrado elPeriodoDeTesis = proyecto.getSemestreIniciacion();
                            if (hoy.after(elPeriodoDeTesis.getLevantarPendiente())) {

                                String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                                String login = proyecto.getAsesor().getPersona().getCorreo();
                                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SUBIR_DOCUMENTO_ABET_PROYECTO_GRADO);
                                reportarEntregaTardia(elPeriodoDeTesis.getLevantarPendiente(), comando, accion, login);
                            }

                            proyecto.setRutaABET(rutaABET);
                            Double nota = proyecto.getCalificacionTesis();
                            if (nota != null) {
                                if (nota >= 3) {
                                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA));
                                } else {
                                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
                                }
                            } else {
                                proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA));
                            }
                            proyectoGradoFacade.edit(proyecto);

                            //Enviar correo a coordinación notificando
                            String asunto = Notificaciones.ASUNTO_INFORME_ABET_COORD;
                            String mensaje = Notificaciones.MENSAJE_INFORME_ABET_COORD;
                            asunto = asunto.replaceFirst("%1", proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos());
                            mensaje = mensaje.replaceFirst("%1", proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos());
                            mensaje = mensaje.replaceFirst("%2", proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos());
                            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asunto, null, null, null, mensaje);

                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                        }
                    } //Validación de estados (PENDIENTE ESPECIAL)
                    else if (proyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE_ESPECIAL))) {
                        String rutaABET = getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ABET) + secNombreArchivoABET.getValor();
                        boolean existe = getConversorProyectoDeGrado().existeArchivo(rutaABET);
                        if (existe) {

                            Date hoy = new Date();
                            PeriodoTesisPregrado elPeriodoDeTesis = proyecto.getSemestreIniciacion();
                            if (hoy.after(elPeriodoDeTesis.getLevantarPendienteEspecial())) {

                                String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                                String login = proyecto.getAsesor().getPersona().getCorreo();
                                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SUBIR_DOCUMENTO_ABET_PROYECTO_GRADO);
                                reportarEntregaTardia(elPeriodoDeTesis.getLevantarPendienteEspecial(), comando, accion, login);
                            }

                            proyecto.setRutaABET(rutaABET);
                            Double nota = proyecto.getCalificacionTesis();
                            if (nota != null) {
                                if (nota >= 3) {
                                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA));
                                } else {
                                    proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PERDIDA));
                                }
                            } else {
                                proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA));
                            }
                            proyectoGradoFacade.edit(proyecto);

                            //Enviar correo a coordinación notificando
                            String asunto = Notificaciones.ASUNTO_INFORME_ABET_COORD;
                            String mensaje = Notificaciones.MENSAJE_INFORME_ABET_COORD;
                            asunto = asunto.replaceFirst("%1", proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos());
                            mensaje = mensaje.replaceFirst("%1", proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos());
                            mensaje = mensaje.replaceFirst("%2", proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos());
                            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asunto, null, null, null, mensaje);

                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0008, new ArrayList());
                        }
                    } else {
                        //ERROR: El estado debería ser CTE_TESIS_PY_ESPERANDO_ABET.
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0009, new ArrayList());
                    }
                }
                //ERROR: Tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0008, new ArrayList());
            }
            //ERROR: XML mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String subirNotaProyectoGrado(String comandoXML) {
        try {
            parser.leerXML(comandoXML);
            Secuencia secNotatesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            Secuencia secIdTesis = secNotatesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secIdTesis != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                ProyectoDeGrado proyectoGrado = proyectoGradoFacade.find(id);
                if (proyectoGrado != null) {

                    //Validación de estados (Caso normal del proceso)
                    if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA))) {

                        Secuencia secNotaTesis = secNotatesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
                        if (secNotaTesis.getValor() != null) {
                            Date hoy = new Date();
                            PeriodoTesisPregrado elPeriodoDeTesis = proyectoGrado.getSemestreIniciacion();
                            if (hoy.after(elPeriodoDeTesis.getReporteNotas())) {

                                String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                                String login = proyectoGrado.getAsesor().getPersona().getCorreo();
                                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SUBIR_NOTA_PROYECTO_GRADO);
                                reportarEntregaTardia(elPeriodoDeTesis.getReporteNotas(), comando, accion, login);
                            }

                            //Asigna la nota
                            proyectoGrado.setCalificacionTesis(Double.parseDouble(secNotaTesis.getValor()));
                            proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET));
                            proyectoGradoFacade.edit(proyectoGrado);

                            //: enviar correo estudiante informando nota de tesis
                            String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_PROYECTO_GRADO;
                            String mensajeCreacion = Notificaciones.MENSAJE_CALIFICACION_PROYECTO_GRADO;
                            mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%3", secNotaTesis.getValor());
                            correoBean.enviarMail(proyectoGrado.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                            //: enviar correo coordinación informando nota de tesis
                            String asuntoCreacionCoord = Notificaciones.ASUNTO_CALIFICACION_PROYECTO_GRADO_COORDINACION;
                            asuntoCreacionCoord = asuntoCreacionCoord.replaceFirst("%", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            String mensajeCreacionCoord = Notificaciones.MENSAJE_CALIFICACION_PROYECTO_GRADO_COORDINACION;
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", secNotaTesis.getValor());
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%3", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCreacionCoord, null, null, null, mensajeCreacionCoord);

                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            //Le crea la tarea al asesor para que suba el archivo abet
                            crearTareaSubirDocumentoABET(proyectoGrado);

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                        }
                    } //PENDIENTE
                    else if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE))) {

                        Secuencia secNotaTesis = secNotatesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
                        if (secNotaTesis.getValor() != null) {
                            Date hoy = new Date();
                            PeriodoTesisPregrado elPeriodoDeTesis = proyectoGrado.getSemestreIniciacion();
                            if (hoy.after(elPeriodoDeTesis.getLevantarPendiente())) {

                                String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                                String login = proyectoGrado.getAsesor().getPersona().getCorreo();
                                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SUBIR_NOTA_PROYECTO_GRADO);
                                reportarEntregaTardia(elPeriodoDeTesis.getLevantarPendiente(), comando, accion, login);
                            }

                            //Asigna la nota
                            proyectoGrado.setCalificacionTesis(Double.parseDouble(secNotaTesis.getValor()));
                            proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE));
                            proyectoGradoFacade.edit(proyectoGrado);

                            //: enviar correo estudiante informando nota de tesis
                            String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_PROYECTO_GRADO;
                            String mensajeCreacion = Notificaciones.MENSAJE_CALIFICACION_PROYECTO_GRADO;
                            mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%3", secNotaTesis.getValor());
                            correoBean.enviarMail(proyectoGrado.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                            //: enviar correo coordinación informando nota de tesis
                            String asuntoCreacionCoord = Notificaciones.ASUNTO_CALIFICACION_PROYECTO_GRADO_COORDINACION;
                            asuntoCreacionCoord = asuntoCreacionCoord.replaceFirst("%", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            String mensajeCreacionCoord = Notificaciones.MENSAJE_CALIFICACION_PROYECTO_GRADO_COORDINACION;
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", secNotaTesis.getValor());
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%3", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCreacionCoord, null, null, null, mensajeCreacionCoord);

                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            //Crea la tarea para subir el ABET en pendiente
                            crearTareaSubirDocumentoABETPendiente(proyectoGrado);

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                        }
                    } //PENDIENTE ESPECIAL
                    else if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE_ESPECIAL))) {

                        Secuencia secNotaTesis = secNotatesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
                        if (secNotaTesis.getValor() != null) {
                            Date hoy = new Date();
                            PeriodoTesisPregrado elPeriodoDeTesis = proyectoGrado.getSemestreIniciacion();
                            if (hoy.after(elPeriodoDeTesis.getLevantarPendienteEspecial())) {

                                String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                                String login = proyectoGrado.getAsesor().getPersona().getCorreo();
                                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_SUBIR_NOTA_PROYECTO_GRADO);
                                reportarEntregaTardia(elPeriodoDeTesis.getLevantarPendienteEspecial(), comando, accion, login);
                            }

                            //Asigna la nota
                            proyectoGrado.setCalificacionTesis(Double.parseDouble(secNotaTesis.getValor()));
                            proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE_ESPECIAL));
                            proyectoGradoFacade.edit(proyectoGrado);

                            //: enviar correo estudiante informando nota de tesis
                            String asuntoCreacion = Notificaciones.ASUNTO_CALIFICACION_PROYECTO_GRADO;
                            String mensajeCreacion = Notificaciones.MENSAJE_CALIFICACION_PROYECTO_GRADO;
                            mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                            mensajeCreacion = mensajeCreacion.replaceFirst("%3", secNotaTesis.getValor());
                            correoBean.enviarMail(proyectoGrado.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                            //: enviar correo coordinación informando nota de tesis
                            String asuntoCreacionCoord = Notificaciones.ASUNTO_CALIFICACION_PROYECTO_GRADO_COORDINACION;
                            asuntoCreacionCoord = asuntoCreacionCoord.replaceFirst("%", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            String mensajeCreacionCoord = Notificaciones.MENSAJE_CALIFICACION_PROYECTO_GRADO_COORDINACION;
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", secNotaTesis.getValor());
                            mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%3", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                            correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCreacionCoord, null, null, null, mensajeCreacionCoord);

                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            //Crea la tarea para subir el ABET en pendiente especial
                            crearTareaSubirDocumentoABETPendienteEspecial(proyectoGrado);

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                        }
                    } else {
                        //ERROR: El estado debería ser CTE_TESIS_PY_ESPERANDO_NOTA.
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0009, new ArrayList());
                    }
                }
                //ERROR: Tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0008, new ArrayList());
            }
            //ERROR: XML mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    //Revisa los proyectos de grado que esten en espera del informe de retiro y los pone en el siguiente estado esperando poster
    public void enviarInformeRetiroProyectoGradoPorDefecto() {
        Collection<ProyectoDeGrado> proyectosDeGrado = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO));
        for (ProyectoDeGrado elProyecto : proyectosDeGrado) {
            enviarInformeRetiroProyectoGradoPorDefecto(elProyecto);
        }
    }

    public void enviarInformeRetiroProyectoGradoPorDefecto(ProyectoDeGrado elProyecto) {
        elProyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER));
        proyectoGradoFacade.edit(elProyecto);
    }

    public String enviarInformeRetiroProyectoGrado(String comandoXML) {
        try {
            parser.leerXML(comandoXML);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            Secuencia secIdTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secIdTesis != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                ProyectoDeGrado proyectoGrado = proyectoGradoFacade.find(id);
                if (proyectoGrado != null) {

                    //Validación de estados
                    if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO))) {
                        Secuencia secRetiroTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RETIRO));

                        if (secRetiroTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_RETIRO_NO)) || secRetiroTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_RETIRO_SI))) {

                            Date hoy = new Date();
                            PeriodoTesisPregrado elPeriodoDeTesis = proyectoGrado.getSemestreIniciacion();
                            if (hoy.after(elPeriodoDeTesis.getInformeRetiro())) {

                                String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                                String login = proyectoGrado.getEstudiante().getPersona().getCorreo();
                                String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_INFORME_RETIRO_PROYECTO_GRADO);
                                reportarEntregaTardia(elPeriodoDeTesis.getInformeRetiro(), comando, accion, login);
                            }

                            //Si NO retira, el estado de la tesis cambia a esperando poster
                            if (secRetiroTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_RETIRO_NO))) {
                                proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER));
                                proyectoGrado.setEstadoPoster(false);
                                proyectoGradoFacade.edit(proyectoGrado);

                                //Si retira, el estado de la tesis cambia a retirada
                            } else if (secRetiroTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_RETIRO_SI))) {
                                proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PREGRADO_RETIRADA));
                                proyectoGradoFacade.edit(proyectoGrado);

                                //Enviar correo estudiante informando que informó que retiró pero que tiene que retirar en BANNER para que sea válido
                                String asuntoCreacion = Notificaciones.ASUNTO_INFORME_RETIRO_ESTUDIANTE;
                                String mensajeCreacion = Notificaciones.MENSAJE_INFORME_RETIRO_ESTUDIANTE;
                                mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                correoBean.enviarMail(proyectoGrado.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                                //Enviar correo profesor/coordinación informando que retiró el estudiante
                                String asuntoCreacionCoord = Notificaciones.ASUNTO_INFORME_RETIRO_PROFESOR;
                                String mensajeCreacionCoord = Notificaciones.MENSAJE_INFORME_RETIRO_PROFESOR;
                                mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                correoBean.enviarMail(proyectoGrado.getAsesor().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                                asuntoCreacionCoord = Notificaciones.ASUNTO_INFORME_RETIRO_COORD;
                                mensajeCreacionCoord = Notificaciones.MENSAJE_INFORME_RETIRO_COORD;
                                mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCreacionCoord, null, null, null, mensajeCreacionCoord);

                            }
                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_INFORME_RETIRO), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_RETIRO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_RETIRO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                        }
                    } else {
                        //ERROR: El estado debería ser CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO.
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0009, new ArrayList());
                    }
                }
                //ERROR: Tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0008, new ArrayList());
            }
            //ERROR: XML mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String enviarInformePendienteProyectoGrado(String comandoXML) {
        try {
            parser.leerXML(comandoXML);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            Secuencia secIdTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secIdTesis != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                ProyectoDeGrado proyectoGrado = proyectoGradoFacade.find(id);
                if (proyectoGrado != null) {

                    //Validación de estados
                    if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_PENDIENTE))
                            || proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA))
                            || proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET))
                            || proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER))) {
                        Secuencia secPendienteTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PENDIENTE));

                        Date hoy = new Date();
                        PeriodoTesisPregrado elPeriodoDeTesis = proyectoGrado.getSemestreIniciacion();
                        if (hoy.after(elPeriodoDeTesis.getPedirPendiente())) {

                            String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                            String login = proyectoGrado.getAsesor().getPersona().getCorreo();
                            String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO);
                            reportarEntregaTardia(elPeriodoDeTesis.getPedirPendiente(), comando, accion, login);
                        }

                        if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_NO))
                                || secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_SI))) {

                            //Si NO solicita pendiente, el estado de la tesis cambia a esperando nota
                            if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_NO))) {
                                // Si esta esperando informe de pendiente y este no es solicitado, pasa a esperando nota, en caso contrario
                                // conserva el estado anterior
                                if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_PENDIENTE))) {
                                    proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA));
                                    proyectoGradoFacade.edit(proyectoGrado);
                                }
                                //Si solicita pendiente, el estado de la tesis cambia a en pendiente
                            } else if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_SI))) {
                                proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE));
                                proyectoGradoFacade.edit(proyectoGrado);

                                //Enviar correo al profesor informando que solicitó pendiente pero tiene que esperar la aprobación de coordinación
                                String asuntoCreacion = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_PROFESOR;
                                String mensajeCreacion = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_PROFESOR;
                                mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                correoBean.enviarMail(proyectoGrado.getAsesor().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                                //Enviar correo a coordinación informando la solicitud de pendiente
                                String asuntoCreacionCoord = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_COORD;
                                String mensajeCreacionCoord = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_COORD;
                                mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCreacionCoord, null, null, null, mensajeCreacionCoord);

                                //Crear la tarea al coordinador para que apruebe el pendiente
                                crearTareaAprobarPendienteProyectoGrado(proyectoGrado);

                            }
                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_INFORME_PENDIENTE), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                        }
                    } else {
                        //ERROR: El estado debería ser CTE_TESIS_PY_ESPERANDO_INFORME_PENDIENTE.
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0009, new ArrayList());
                    }
                }
                //ERROR: Tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0008, new ArrayList());
            }
            //ERROR: XML mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String enviarInformePendienteEspecialProyectoGrado(String comandoXML) {
        try {
            parser.leerXML(comandoXML);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            Secuencia secIdTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secNombreArchivo = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_PENDIENTE_ESPECIAL));
            if (secIdTesis != null) {
                Long id = secIdTesis.getValorLong();
                ProyectoDeGrado proyectoGrado = proyectoGradoFacade.find(id);
                if (proyectoGrado != null) {
                    //Validación de estados
                    if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_PENDIENTE))
                            || proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA))
                            || proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET))
                            || proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER))) {
                        Secuencia secPendienteTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PENDIENTE));

                        Date hoy = new Date();
                        PeriodoTesisPregrado elPeriodoDeTesis = proyectoGrado.getSemestreIniciacion();
                        if (hoy.after(elPeriodoDeTesis.getPedirPendienteEspecial())) {

                            String comando = parser.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
                            String login = proyectoGrado.getAsesor().getPersona().getCorreo();
                            String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO);
                            reportarEntregaTardia(elPeriodoDeTesis.getPedirPendienteEspecial(), comando, accion, login);
                        }

                        if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_NO))
                                || secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_SI))) {

                            if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_SI))) {
                                String rutaArchivo = getConstanteBean().getConstante(Constantes.RUTA_PENDIENTES_ESPECIALES_TESIS_PREGRADO) + secNombreArchivo.getValor();

                                boolean existe = getConversorProyectoDeGrado().existeArchivo(rutaArchivo);
                                if (existe) {

                                    proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL));
                                    proyectoGrado.setRutaArchivoPendienteEspecial(rutaArchivo);
                                    proyectoGradoFacade.edit(proyectoGrado);

                                    //Enviar correo al profesor informando que solicitó pendiente especial pero tiene que esperar la aprobación de coordinación
                                    String asuntoCreacion = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_PROFESOR;
                                    String mensajeCreacion = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_ESPECIAL_PROFESOR;
                                    mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                    mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                    correoBean.enviarMail(proyectoGrado.getAsesor().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                                    //Enviar correo a coordinación informando la solicitud de pendiente
                                    String asuntoCreacionCoord = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_COORD;
                                    String mensajeCreacionCoord = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_ESPECIAL_COORD;
                                    mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                    mensajeCreacionCoord = mensajeCreacionCoord.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                    correoBean.enviarMail(getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA), asuntoCreacionCoord, null, null, null, mensajeCreacionCoord);

                                    //Crear tarea al coordinador para que apruebe el pendiente especial
                                    crearTareaAprobarPendienteEspecialProyectoGrado(proyectoGrado);

                                } else {
                                    //ERROR: Archivo no existe
                                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0014, new ArrayList());
                                }
                                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                            }
                        } else {
                            //ERROR: Debería decir SI/NO
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                        }
                    } else {
                        //ERROR: Error en el estado
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0009, new ArrayList());
                    }
                }
            }
            //ERROR: XML mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String enviarAprobacionPendienteProyectoGrado(String comandoXML) {
        try {
            parser.leerXML(comandoXML);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            Secuencia secIdTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secIdTesis != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                ProyectoDeGrado proyectoGrado = proyectoGradoFacade.find(id);
                if (proyectoGrado != null) {

                    //Validación de estados
                    if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE))) {
                        Secuencia secPendienteTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PENDIENTE));

                        if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_NO)) || secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_SI))) {

                            //Si NO aprueba, la tesis continúa en el estado anterior esperando poster o esperando nota y abet
                            if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_NO))) {

                                if (proyectoGrado.getEstadoPoster() == false) {
                                    proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER));
                                } else {
                                    proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA));
                                }

                                proyectoGradoFacade.edit(proyectoGrado);

                                //Enviar correo al profesor informando que la solicitud de pendiente fue rechazada
                                String asuntoCreacion = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_RECHAZADA_PROFESOR;
                                String mensajeCreacion = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_RECHAZADA_PROFESOR;
                                mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%3", "");
                                correoBean.enviarMail(proyectoGrado.getAsesor().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                                //Si SI aprueba, la tesis queda en estado pendiente
                            } else if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_SI))) {
                                proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE));
                                proyectoGradoFacade.edit(proyectoGrado);

                                //Se crea la tarea para que el estudiante suba el afiche en pendiente
                                crearTareaSubirAfichePendiente(proyectoGrado);

                                //Enviar correo al profesor informando que la solicitud de pendiente fue rechazada
                                String asuntoCreacion = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_APROBADA_PROFESOR;
                                String mensajeCreacion = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_APROBADA_PROFESOR;
                                mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%3", "NO");
                                correoBean.enviarMail(proyectoGrado.getAsesor().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                            }
                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                        }
                    } else {
                        //ERROR: El estado debería ser CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE.
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0009, new ArrayList());
                    }
                }
                //ERROR: Tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0008, new ArrayList());
            }
            //ERROR: XML mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String enviarAprobacionPendienteEspecialProyectoGrado(String comandoXML) {
        try {
            parser.leerXML(comandoXML);
            Secuencia secTesis = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            Secuencia secIdTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secIdTesis != null) {
                Long id = Long.parseLong(secIdTesis.getValor().trim());
                ProyectoDeGrado proyectoGrado = proyectoGradoFacade.find(id);
                if (proyectoGrado != null) {
                    //Validación de estados
                    if (proyectoGrado.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL))) {
                        Secuencia secPendienteTesis = secTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PENDIENTE));

                        if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_NO)) || secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_SI))) {

                            //Si NO aprueba, la tesis continúa en estado esperando nota y abet
                            if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_NO))) {

                                if (proyectoGrado.getEstadoPoster().equals(false)) {
                                    proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER));
                                } else {
                                    proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA));
                                }

                                proyectoGradoFacade.edit(proyectoGrado);

                                //Enviar correo al profesor informando que la solicitud de pendiente fue rechazada
                                String asuntoCreacion = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_RECHAZADA_PROFESOR;
                                String mensajeCreacion = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_ESPECIAL_RECHAZADA_PROFESOR;
                                mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%3", "");
                                correoBean.enviarMail(proyectoGrado.getAsesor().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

                                //Si SI aprueba, la tesis queda en estado pendiente
                            } else if (secPendienteTesis.getValor().equals(getConstanteBean().getConstante(Constantes.CTE_PENDIENTE_SI))) {
                                proyectoGrado.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE_ESPECIAL));
                                proyectoGradoFacade.edit(proyectoGrado);

                                //Se crea la tarea para que el estudiante suba el afiche en pendiente especial
                                crearTareaSubirAfichePendienteEspecial(proyectoGrado);

                                //Enviar correo al profesor informando que la solicitud de pendiente fue rechazada
                                String asuntoCreacion = Notificaciones.ASUNTO_SOLICITUD_PENDIENTE_ESPECIAL_APROBADA_PROFESOR;
                                String mensajeCreacion = Notificaciones.MENSAJE_SOLICITUD_PENDIENTE_ESPECIAL_APROBADA_PROFESOR;
                                mensajeCreacion = mensajeCreacion.replaceFirst("%1", proyectoGrado.getAsesor().getPersona().getNombres() + " " + proyectoGrado.getAsesor().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%2", proyectoGrado.getEstudiante().getPersona().getNombres() + " " + proyectoGrado.getEstudiante().getPersona().getApellidos());
                                mensajeCreacion = mensajeCreacion.replaceFirst("%3", "NO");
                                correoBean.enviarMail(proyectoGrado.getAsesor().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                            }
                            //Realiza la tarea
                            Properties propiedades = new Properties();
                            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE_ESPECIAL), propiedades));
                            if (borrar != null) {
                                tareaSencillaBean.realizarTareaPorId(borrar.getId());
                            }

                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                        } else {
                            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00018, new ArrayList());
                        }
                    } else {
                        //ERROR: El estado debería ser CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE.
                        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0009, new ArrayList());
                    }
                }
                //ERROR: Tesis no existe
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0008, new ArrayList());
            }
            //ERROR: XML mal formado
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }


    /*
     * Metodos para las tareas
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

    private void crearTareaEstudianteDocumentoPropuesta(ProyectoDeGrado proyecto) {

        crearTareaEstudianteDocumentoPropuestaSoloTarea(proyecto);

        String correoResponsable = proyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaEnvioPropuesta = sdfHMS.format(proyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud());
        String asunto = Notificaciones.ASUNTO_PROYECTO_DE_GRADO_TAREA;
        //Envía un correo informando que hay una propuesta por subir
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_PROYECTO_DE_GRADO_ACEPTADO, nombreCompletoEstudiante, nombreCompletoAsesor, fechaEnvioPropuesta);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    private void crearTareaEstudianteDocumentoPropuestaSoloTarea(ProyectoDeGrado proyecto) {
        // Busca si existe ya existe la tarea
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        String correoResponsable = proyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaEnvioPropuesta = sdfHMS.format(proyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud());
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
        String comando = getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE);
        String asunto = Notificaciones.ASUNTO_PROYECTO_DE_GRADO_TAREA;
        String header = String.format(Notificaciones.HEADER_PROYECTO_DE_GRADO_ACEPTADO, nombreCompletoEstudiante, nombreCompletoAsesor, fechaEnvioPropuesta);
        String footer = Notificaciones.FOOTER_PROYECTO_DE_GRADO_ACEPTADO;

        tareaBean.crearTareaPersona(null, tipo, correoResponsable, false, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    private void crearTareaCoordinacionInscripcionBanner(ProyectoDeGrado proyecto) {

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INSCRIBIR_BANNER);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud().getTime());

        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String correoResponsable = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaEnvioPropuesta = sdfHMS.format(proyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud());
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
        String comando = getConstanteBean().getConstante(Constantes.TAREA_INSCRIBIR_PROYECTO_BANNER_POR_COODINACION);
        String mensaje = String.format(Notificaciones.MENSAJE_INCRIPCION_BANNER_PROYECTO_DE_GRADO_TAREA, nombreCompletoEstudiante);
        String header = Notificaciones.HEADER_INCRIPCION_BANNER_PROYECTO_DE_GRADO_ACEPTADO;
        String footer = Notificaciones.FOOTER_INCRIPCION_BANNER_PROYECTO_DE_GRADO_ACEPTADO;
        String asunto = Notificaciones.ASUNTO_INCRIPCION_BANNER_PROYECTO_DE_GRADO_TAREA;
        //Crea la tarea a coordinación para que apruebe la solicitud de proyecto de grado

        tareaBean.crearTareaRol(mensaje, tipo, categoriaResponsable, true, header, footer, fI, fF, comando, paramsNew, asunto);

    }

    private void crearTareaCoordinacionInscripcionBannerSoloTarea(ProyectoDeGrado proyecto) {
        // Busca si existe ya existe la tarea
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        String correoResponsable = proyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaEnvioPropuesta = sdfHMS.format(proyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud());
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
        String comando = getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE);
        String asunto = Notificaciones.ASUNTO_PROYECTO_DE_GRADO_TAREA;
        String header = String.format(Notificaciones.HEADER_PROYECTO_DE_GRADO_ACEPTADO, nombreCompletoEstudiante, nombreCompletoAsesor, fechaEnvioPropuesta);
        String footer = Notificaciones.FOOTER_PROYECTO_DE_GRADO_ACEPTADO;

        tareaBean.crearTareaPersona(null, tipo, correoResponsable, false, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaAprobarReprobarPropuesta(ProyectoDeGrado proyecto) {

        crearTareaAprobarReprobarPropuestaSoloTarea(proyecto);

        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        String asunto = Notificaciones.ASUNTO_APROBAR_PROPUESTA_PROYECTO_DE_GRADO;
        //Envía un correo informando que hay una asistencia graduada por calificar
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_APROBAR_PROPUESTA_PROYECTO_DE_GRADO, nombreCompletoAsesor, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaAprobarReprobarPropuestaSoloTarea(ProyectoDeGrado proyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getValidacionAsesorPropuestaDeProyecto().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE);
        String asunto = Notificaciones.ASUNTO_APROBAR_PROPUESTA_PROYECTO_DE_GRADO;
        String header = String.format(Notificaciones.HEADER_APROBAR_PROPUESTA_PROYECTO_DE_GRADO, nombreCompletoAsesor);
        String footer = Notificaciones.FOOTER_APROBAR_PROPUESTA_PROYECTO_DE_GRADO;
        String mensaje = String.format(Notificaciones.MENSAJE_APROBAR_PROPUESTA_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaAprobarReprobarAfiche(ProyectoDeGrado proyecto) {

        crearTareaAprobarReprobarAficheSoloTarea(proyecto);

        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        //Envía un correo informando que hay una asistencia graduada por calificar
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoAsesor, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaAprobarReprobarAficheSoloTarea(ProyectoDeGrado proyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getDarVistoBuenoPoster().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE);
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        String header = String.format(Notificaciones.HEADER_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoAsesor);
        String footer = Notificaciones.FOOTER_APROBAR_AFICHE_PROYECTO_DE_GRADO;
        String mensaje = String.format(Notificaciones.MENSAJE_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaAprobarReprobarAfichePendiente(ProyectoDeGrado proyecto) {

        crearTareaAprobarReprobarAfichePendienteSoloTarea(proyecto);

        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoAsesor, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaAprobarReprobarAfichePendienteSoloTarea(ProyectoDeGrado proyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getDarVistoBuenoPosterPendiente().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE);
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        String header = String.format(Notificaciones.HEADER_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoAsesor);
        String footer = Notificaciones.FOOTER_APROBAR_AFICHE_PROYECTO_DE_GRADO;
        String mensaje = String.format(Notificaciones.MENSAJE_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaAprobarReprobarAfichePendienteEspecial(ProyectoDeGrado proyecto) {

        crearTareaAprobarReprobarAfichePendienteEspecialSoloTarea(proyecto);

        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        //Envía un correo informando que hay una asistencia graduada por calificar
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoAsesor, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);

    }

    public void crearTareaAprobarReprobarAfichePendienteEspecialSoloTarea(ProyectoDeGrado proyecto) {

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE_ESPECIAL);
        Properties params = new Properties();
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getDarVistoBuenoPosterPendienteEspecial().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_RECHAZAR_AFICHE_ESTUDIANTE_PENDIENTE_ESPECIAL);
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        String header = String.format(Notificaciones.HEADER_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoAsesor);
        String footer = Notificaciones.FOOTER_APROBAR_AFICHE_PROYECTO_DE_GRADO;
        String mensaje = String.format(Notificaciones.MENSAJE_APROBAR_AFICHE_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareasSubirAfiche(PeriodoTesisPregrado find) {
        Collection<ProyectoDeGrado> proyectosDeGrado = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER));
        for (ProyectoDeGrado proyectoDeGrado : proyectosDeGrado) {
            crearTareaSubirAfiche(proyectoDeGrado);
        }
    }

    public void crearTareaSubirAfiche(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPoster().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        String correoResponsable = elProyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp fechaAfiche = new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPoster().getTime());
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        String header = String.format(Notificaciones.HEADER_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        String footer = Notificaciones.FOOTER_SUBIR_AFICHE_ESTUDIANTE;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(null, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);

        //Envía un correo informando que hay una tarea para subir el afiche
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public String crearTareaEnviarPropuestaPY() {
        Collection<ProyectoDeGrado> proyectosDeGrado = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_PROPUESTA_ENVIADA_ESPERANDO_APROBACION_ASESOR));
        for (ProyectoDeGrado elProyecto : proyectosDeGrado) {
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR);
            Properties params = new Properties();
            params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
            completarTareaSencilla(tipo, params);

            Timestamp fI = new Timestamp(System.currentTimeMillis());
            Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud().getTime());
            String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
            String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
            String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
            HashMap<String, String> paramsNew = new HashMap<String, String>();
            paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
            String comando = getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE);
            String asunto = String.format(Notificaciones.ASUNTO_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO);
            String header = String.format(Notificaciones.HEADER_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
            String footer = Notificaciones.FOOTER_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO;
            String mensaje = String.format(Notificaciones.MENSAJE_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
            tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);

            //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
            String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_ENVIAR_PROPUESTA_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
            correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);

        }

        return "OK";
    }

    public void crearTareasSubirTreintaPorCiento() {
        Collection<ProyectoDeGrado> proyectosDeGrado = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_REPORTE_TREINTA_POR_CIENTO));
        for (ProyectoDeGrado elProyecto : proyectosDeGrado) {
            crearTareaSubirTreintaPorCiento(elProyecto);
        }
    }

    public void crearTareaSubirTreintaPorCiento(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getApreciacionCualitativa().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        System.out.println("con id " + elProyecto.getId());
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_AGREGAR_COMENTARIO_PROYECTO_GRADO);
        String asunto = Notificaciones.ASUNTO_SUBIR_TREINTA_POR_CIENTO;
        System.out.println("con notificacion " + Notificaciones.HEADER_SUBIR_TREINTA_POR_CIENTO);
        System.out.println("con profesor " + nombreCompletoProfesor);
        String header = Notificaciones.HEADER_SUBIR_TREINTA_POR_CIENTO;
        header = header.replace("%1", nombreCompletoProfesor);
        String footer = Notificaciones.FOOTER_SUBIR_TREINTA_POR_CIENTO;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_TREINTA_POR_CIENTO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);

        //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
        //String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_TREINTA_POR_CIENTO, nombreCompletoProfesor, nombreCompletoEstudiante);
        String mensajeCompleto = Notificaciones.MENSAJE_COMPLETO_SUBIR_TREINTA_POR_CIENTO;
        mensajeCompleto = mensajeCompleto.replace("%1", nombreCompletoProfesor);
        mensajeCompleto = mensajeCompleto.replace("%2", nombreCompletoEstudiante);

        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaSubirNotaProyectoGrado(ProyectoDeGrado elProyecto) {
        crearTareaSubirNotaProyectoGradoSoloTarea(elProyecto);

        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_NOTA_PROYECTO_GRADO);
        //Envía un correo informando que hay una tarea para subir la nota de proyecto de grado
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoProfesor, nombreCompletoEstudiante, correoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaSubirNotaProyectoGradoSoloTarea(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getReporteNotas().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_NOTA_PROYECTO_GRADO);
        String header = String.format(Notificaciones.HEADER_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoProfesor);
        String footer = Notificaciones.FOOTER_SUBIR_NOTA_PROYECTO_GRADO;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaSubirDocumentoABET(ProyectoDeGrado elProyecto) {

        crearTareaSubirDocumentoABETSoloTarea(elProyecto);

        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_ARCHIVO_ABET);
        //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_ARCHIVO_ABET, nombreCompletoProfesor, nombreCompletoEstudiante, correoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);

    }

    public void crearTareaSubirDocumentoABETSoloTarea(ProyectoDeGrado elProyecto) {

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getRubricaABET().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_ARCHIVO_ABET);
        String header = String.format(Notificaciones.HEADER_SUBIR_ARCHIVO_ABET, nombreCompletoProfesor, nombreCompletoEstudiante, correoEstudiante);
        String footer = Notificaciones.FOOTER_SUBIR_ARCHIVO_ABET;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_ARCHIVO_ABET, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, false, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareasEnviarInformeRetiroProyectoGrado(PeriodoTesisPregrado find) {
        Collection<ProyectoDeGrado> proyectosDeGrado = proyectoGradoFacade.findByEstado(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO));
        for (ProyectoDeGrado elProyecto : proyectosDeGrado) {
            crearTareaEnviarInformeRetiroProyectoGrado(elProyecto);
        }
    }

    public void crearTareaEnviarInformeRetiroProyectoGrado(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_INFORME_RETIRO);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getInformeRetiro().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        String correoResponsable = elProyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_INFORME_RETIRO_PROYECTO_GRADO);
        String asunto = String.format(Notificaciones.ASUNTO_ENVIAR_INFORME_RETIRO_ESTUDIANTE);
        String header = String.format(Notificaciones.HEADER_ENVIAR_INFORME_RETIRO_ESTUDIANTE, nombreCompletoEstudiante);
        String footer = Notificaciones.FOOTER_ENVIAR_INFORME_RETIRO_ESTUDIANTE;
        String mensaje = String.format(Notificaciones.MENSAJE_ENVIAR_INFORME_RETIRO_ESTUDIANTE, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(null, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);

        //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_ENVIAR_INFORME_RETIRO_ESTUDIANTE, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaAprobarProyectoGradoPorCoordinacion(ProyectoDeGrado proyecto) {

        crearTareaAprobarProyectoGradoPorCoordinacionSoloTarea(proyecto);

        String correoResponsable = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION);
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaAprobarProyectoGradoPorCoordinacionSoloTarea(ProyectoDeGrado proyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_COORDINACION);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
        completarTareaSencilla(tipo, params);

        //Crea la tarea a coordinación para que apruebe la solicitud de proyecto de grado
        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getAcesorAceptePy().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String correoResponsable = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
        String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_PROYECTO_GRADO_COORDINACION);
        String asunto = String.format(Notificaciones.ASUNTO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION);
        String header = String.format(Notificaciones.HEADER_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION);
        String footer = Notificaciones.FOOTER_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION;
        String mensaje = String.format(Notificaciones.MENSAJE_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION, nombreCompletoEstudiante);
        //tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
        tareaBean.crearTareaRol(mensaje, tipo, categoriaResponsable, true, header, footer, fI, fF, comando, paramsNew, asunto);
    }

    public void crearTareaAprobarProyectoGradoPorAsesor(ProyectoDeGrado proyecto) {

        crearTareaAprobarProyectoGradoPorAsesorSoloTarea(proyecto);

        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO;
        //Envía un correo informando que hay una tarea para subir el afiche
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO, nombreCompletoAsesor, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaAprobarProyectoGradoPorAsesorSoloTarea(ProyectoDeGrado proyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_ASESOR);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(proyecto.getSemestreIniciacion().getAcesorAceptePy().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = proyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoEstudiante = proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(proyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_PROYCTOS_DE_GRADO_POR_ESTUDIANTE);
        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO;
        String header = String.format(Notificaciones.HEADER_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO, nombreCompletoAsesor);
        String footer = Notificaciones.FOOTER_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO;
        String mensaje = String.format(Notificaciones.MENSAJE_APROBAR_INSCRIPCION_PROYECTO_DE_GRADO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaAprobarPendienteProyectoGrado(ProyectoDeGrado elProyecto) {

        crearTareaAprobarPendienteProyectoGradoSoloTarea(elProyecto);

        String correoResponsable = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String asunto = Notificaciones.ASUNTO_ENVIAR_INFORME_PENDIENTE_COORD;
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_ENVIAR_INFORME_PENDIENTE_COORD, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaAprobarPendienteProyectoGradoSoloTarea(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String correoResponsable = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_PROYECTO_GRADO);
        String asunto = Notificaciones.ASUNTO_ENVIAR_INFORME_PENDIENTE_COORD;
        String header = Notificaciones.HEADER_ENVIAR_INFORME_PENDIENTE_COORD;
        String footer = Notificaciones.FOOTER_ENVIAR_INFORME_PENDIENTE_COORD;
        String mensaje = String.format(Notificaciones.MENSAJE_ENVIAR_INFORME_PENDIENTE_COORD, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaAprobarPendienteEspecialProyectoGrado(ProyectoDeGrado elProyecto) {

        crearTareaAprobarPendienteEspecialProyectoGradoSoloTarea(elProyecto);

        String correoResponsable = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String asunto = Notificaciones.ASUNTO_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD;
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD, nombreCompletoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaAprobarPendienteEspecialProyectoGradoSoloTarea(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE_ESPECIAL);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_COORDINACION);
        String correoResponsable = getConstanteBean().getConstante(Constantes.CTE_CORREO_COORDINADOR_MAESTRTIA);
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_APROBACION_PENDIENTE_ESPECIAL_PROYECTO_GRADO);
        String asunto = Notificaciones.ASUNTO_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD;
        String header = Notificaciones.HEADER_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD;
        String footer = Notificaciones.FOOTER_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD;
        String mensaje = String.format(Notificaciones.MENSAJE_ENVIAR_INFORME_PENDIENTE_ESPECIAL_COORD, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaSubirNotaProyectoGradoPendiente(ProyectoDeGrado elProyecto) {
        crearTareaSubirNotaProyectoGradoPendienteSoloTarea(elProyecto);

        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_NOTA_PROYECTO_GRADO);
        //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoProfesor, nombreCompletoEstudiante, correoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaSubirNotaProyectoGradoPendienteSoloTarea(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_NOTA_PROYECTO_GRADO);
        String header = String.format(Notificaciones.HEADER_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoProfesor);
        String footer = Notificaciones.FOOTER_SUBIR_NOTA_PROYECTO_GRADO;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaSubirNotaProyectoGradoPendienteEspecial(ProyectoDeGrado elProyecto) {

        crearTareaSubirNotaProyectoGradoPendienteEspecialSoloTarea(elProyecto);

        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_NOTA_PROYECTO_GRADO);
        //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoProfesor, nombreCompletoEstudiante, correoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaSubirNotaProyectoGradoPendienteEspecialSoloTarea(ProyectoDeGrado elProyecto) {

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_NOTA_PROYECTO_GRADO);
        String header = String.format(Notificaciones.HEADER_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoProfesor);
        String footer = Notificaciones.FOOTER_SUBIR_NOTA_PROYECTO_GRADO;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_NOTA_PROYECTO_GRADO, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaSubirDocumentoABETPendiente(ProyectoDeGrado elProyecto) {

        crearTareaSubirDocumentoABETPendienteSoloTarea(elProyecto);

        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_ARCHIVO_ABET);
        //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_ARCHIVO_ABET, nombreCompletoProfesor, nombreCompletoEstudiante, correoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaSubirDocumentoABETPendienteSoloTarea(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET_PENDIENTE);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_ARCHIVO_ABET);
        String header = String.format(Notificaciones.HEADER_SUBIR_ARCHIVO_ABET, nombreCompletoProfesor, nombreCompletoEstudiante, correoResponsable);
        String footer = Notificaciones.FOOTER_SUBIR_ARCHIVO_ABET;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_ARCHIVO_ABET, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void crearTareaSubirDocumentoABETPendienteEspecial(ProyectoDeGrado elProyecto) {

        crearTareaSubirDocumentoABETPendienteEspecialSoloTarea(elProyecto);

        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_ARCHIVO_ABET);
        //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_ARCHIVO_ABET, nombreCompletoProfesor, nombreCompletoEstudiante, correoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    public void crearTareaSubirDocumentoABETPendienteEspecialSoloTarea(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
        String correoResponsable = elProyecto.getAsesor().getPersona().getCorreo();
        String nombreCompletoProfesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String correoEstudiante = elProyecto.getEstudiante().getPersona().getCorreo();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_ARCHIVO_ABET);
        String header = String.format(Notificaciones.HEADER_SUBIR_ARCHIVO_ABET, nombreCompletoProfesor);
        String footer = Notificaciones.FOOTER_SUBIR_ARCHIVO_ABET;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_ARCHIVO_ABET, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(mensaje, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);

        //Envía un correo informando que hay una tarea para enviar la propuesta de proyecto de grado
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_ARCHIVO_ABET, nombreCompletoProfesor, nombreCompletoEstudiante, correoEstudiante);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    private void crearTareaSubirAfichePendiente(ProyectoDeGrado elProyecto) {

        crearTareaSubirAfichePendienteSoloTarea(elProyecto);

        String correoResponsable = elProyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp fechaAfiche = elProyecto.getSemestreIniciacion().getEntregaPosterPendiente();
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));

        //Envía un correo informando que hay una asistencia graduada por calificar
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);

    }

    private void crearTareaSubirAfichePendienteSoloTarea(ProyectoDeGrado elProyecto) {

        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPosterPendiente().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        String correoResponsable = elProyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp fechaAfiche = elProyecto.getSemestreIniciacion().getEntregaPosterPendiente();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        String header = String.format(Notificaciones.HEADER_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        String footer = Notificaciones.FOOTER_SUBIR_AFICHE_ESTUDIANTE;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(null, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    private void crearTareaSubirAfichePendienteEspecial(ProyectoDeGrado elProyecto) {

        crearTareaSubirAfichePendienteEspecialSoloTarea(elProyecto);

        String correoResponsable = elProyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp fechaAfiche = elProyecto.getSemestreIniciacion().getEntregaPosterPendienteEspecial();
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));

        //Envía un correo informando que hay una tarea para subir el afiche
        String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
    }

    private void crearTareaSubirAfichePendienteEspecialSoloTarea(ProyectoDeGrado elProyecto) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE_ESPECIAL);
        Properties params = new Properties();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        completarTareaSencilla(tipo, params);

        Timestamp fI = new Timestamp(System.currentTimeMillis());
        Timestamp fF = new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPosterPendienteEspecial().getTime());
        String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        String correoResponsable = elProyecto.getEstudiante().getPersona().getCorreo();
        String nombreCompletoEstudiante = elProyecto.getEstudiante().getPersona().getNombres() + " " + elProyecto.getEstudiante().getPersona().getApellidos();
        String nombreCompletoAsesor = elProyecto.getAsesor().getPersona().getNombres() + " " + elProyecto.getAsesor().getPersona().getApellidos();
        SimpleDateFormat sdfymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp fechaAfiche = elProyecto.getSemestreIniciacion().getEntregaPosterPendienteEspecial();
        HashMap<String, String> paramsNew = new HashMap<String, String>();
        paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(elProyecto.getId()));
        String comando = getConstanteBean().getConstante(Constantes.CMD_SUBIR_ARCHIVO_AFICHE_PROYECTO_GRADO_PENDIENTE_ESPECIAL);
        String asunto = String.format(Notificaciones.ASUNTO_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        String header = String.format(Notificaciones.HEADER_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante, sdfymd.format(fechaAfiche));
        String footer = Notificaciones.FOOTER_SUBIR_AFICHE_ESTUDIANTE;
        String mensaje = String.format(Notificaciones.MENSAJE_SUBIR_AFICHE_ESTUDIANTE, nombreCompletoEstudiante);
        tareaBean.crearTareaPersona(null, tipo, correoResponsable, true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);
    }

    public void regenerarTareasCambioVersionSisinfo(String xml) {

        try {
            parser.leerXML(xml);
            Secuencia secSemestre = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
            Secuencia secId = secSemestre.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            System.out.println("lo que tiene secId:" + secId.getValor());
            Long semestreId = Long.parseLong(secId.getValor().trim());

            Date hoy = new Date();
            Long undia = 1000L * 60 * 60 * 24;

            // Completa las tareas asociadas con proyecto de grado
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_COORDINACION));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_ASESOR));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS));   //BATCH
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_INFORME_RETIRO));       //BATCH
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE));                //BATCH
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE_ESPECIAL));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE_ESPECIAL));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE_ESPECIAL));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE));
            completarListaTareas(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL));


            Collection<ProyectoDeGrado> proyectosDeGrado = proyectoGradoFacade.findAll();
            for (ProyectoDeGrado elProyecto : proyectosDeGrado) {

                if (elProyecto.getEstadoPoster() == null) {
                    elProyecto.setEstadoPoster(false);
                }

                if ((elProyecto.getSemestreIniciacion().getId() >= semestreId)) {

                    if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_APROBACION_COORDINACION))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getAcesorAceptePy().getTime())).after(hoy)) {
                            crearTareaAprobarProyectoGradoPorCoordinacionSoloTarea(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getAcesorAceptePy().getTime())).after(hoy)) {
                            crearTareaAprobarProyectoGradoPorAsesor(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud().getTime())).after(hoy)) {
                            crearTareaEstudianteDocumentoPropuesta(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_PROPUESTA_ENVIADA_ESPERANDO_APROBACION_ASESOR))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getValidacionAsesorPropuestaDeProyecto().getTime())).after(hoy)) {
                            crearTareaAprobarReprobarPropuesta(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getDarVistoBuenoPoster().getTime())).after(hoy)) {
                            crearTareaAprobarReprobarAfiche(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getReporteNotas().getTime())).after(hoy)) {
                            crearTareaSubirNotaProyectoGrado(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getRubricaABET().getTime())).after(hoy)) {
                            crearTareaSubirDocumentoABET(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime())).after(hoy)) {
                            crearTareaAprobarPendienteProyectoGradoSoloTarea(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime())).after(hoy)) {
                            crearTareaAprobarPendienteEspecialProyectoGradoSoloTarea(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getDarVistoBuenoPosterPendiente().getTime())).after(hoy)) {
                            crearTareaAprobarReprobarAfichePendiente(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE_ESPECIAL))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getDarVistoBuenoPosterPendienteEspecial().getTime())).after(hoy)) {
                            crearTareaAprobarReprobarAfichePendienteEspecial(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPosterPendiente().getTime())).after(hoy)) {
                            crearTareaSubirAfichePendiente(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE_ESPECIAL))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPosterPendienteEspecial().getTime())).after(hoy)) {
                            crearTareaSubirAfichePendienteEspecial(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime())).after(hoy)) {
                            crearTareaSubirNotaProyectoGradoPendiente(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE_ESPECIAL))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime())).after(hoy)) {
                            crearTareaSubirNotaProyectoGradoPendienteEspecial(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime())).after(hoy)) {
                            crearTareaSubirDocumentoABETPendiente(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE_ESPECIAL))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime())).after(hoy)) {
                            crearTareaSubirDocumentoABETPendienteEspecial(elProyecto);
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_REPORTE_TREINTA_POR_CIENTO))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getApreciacionCualitativa().getTime() - (undia * 7))).before(hoy)) {
                            if (new Timestamp(elProyecto.getSemestreIniciacion().getApreciacionCualitativa().getTime()).after(hoy)) {
                                crearTareaSubirTreintaPorCiento(elProyecto);
                            }
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_INFORME_RETIRO))) {

                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getInformeRetiro().getTime() - (undia * 7))).before(hoy)) {

                            if (new Timestamp(elProyecto.getSemestreIniciacion().getInformeRetiro().getTime()).after(hoy)) {
                                crearTareaEnviarInformeRetiroProyectoGrado(elProyecto);
                            } else {
                                if ((new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPoster().getTime() - (undia * 7))).before(hoy)) {
                                    enviarInformeRetiroProyectoGradoPorDefecto(elProyecto);
                                    if (new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPoster().getTime()).after(hoy)) {
                                        crearTareaSubirAfiche(elProyecto);
                                    }
                                }
                            }
                        }
                    } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER))) {
                        if ((new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPoster().getTime() - (undia * 7))).before(hoy)) {
                            if (new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPoster().getTime()).after(hoy)) {
                                crearTareaSubirAfiche(elProyecto);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void regenerarTareas(PeriodoTesisPregrado periodo) {

        Date hoy = new Date();

        Collection<ProyectoDeGrado> proyectosDeGrado = proyectoGradoFacade.findByPeriodoTesis(periodo.getNombre());

        for (ProyectoDeGrado elProyecto : proyectosDeGrado) {

            if (elProyecto.getSemestreIniciacion().getId() == periodo.getId()) {

                if (elProyecto.getEstadoPoster() == null) {
                    elProyecto.setEstadoPoster(false);
                }

                if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_APROBACION_COORDINACION))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getAcesorAceptePy().getTime())).after(hoy)) {
                        crearTareaAprobarProyectoGradoPorCoordinacionSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getAcesorAceptePy().getTime())).after(hoy)) {
                        crearTareaAprobarProyectoGradoPorAsesorSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getEnvioPropuestaPyEstud().getTime())).after(hoy)) {
                        crearTareaEstudianteDocumentoPropuestaSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_PROPUESTA_ENVIADA_ESPERANDO_APROBACION_ASESOR))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getValidacionAsesorPropuestaDeProyecto().getTime())).after(hoy)) {
                        crearTareaAprobarReprobarPropuestaSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getDarVistoBuenoPoster().getTime())).after(hoy)) {
                        crearTareaAprobarReprobarAficheSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getReporteNotas().getTime())).after(hoy)) {
                        crearTareaSubirNotaProyectoGradoSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getRubricaABET().getTime())).after(hoy)) {
                        crearTareaSubirDocumentoABETSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_ESPERANDO_APROBACION_PENDIENTE))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime())).after(hoy)) {
                        crearTareaAprobarPendienteProyectoGradoSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_APROBACION_PENDIENTE_ESPECIAL))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime())).after(hoy)) {
                        crearTareaAprobarPendienteEspecialProyectoGradoSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getDarVistoBuenoPosterPendiente().getTime())).after(hoy)) {
                        crearTareaAprobarReprobarAfichePendienteSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_POSTER_ENVIADO_ESPERANDO_APROBACION_ASESOR_PENDIENTE_ESPECIAL))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getDarVistoBuenoPosterPendienteEspecial().getTime())).after(hoy)) {
                        crearTareaAprobarReprobarAfichePendienteEspecialSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPosterPendiente().getTime())).after(hoy)) {
                        crearTareaSubirAfichePendienteSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_POSTER_PENDIENTE_ESPECIAL))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getEntregaPosterPendienteEspecial().getTime())).after(hoy)) {
                        crearTareaSubirAfichePendienteEspecialSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime())).after(hoy)) {
                        crearTareaSubirNotaProyectoGradoPendienteSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_NOTA_PENDIENTE_ESPECIAL))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime())).after(hoy)) {
                        crearTareaSubirNotaProyectoGradoPendienteEspecialSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendiente().getTime())).after(hoy)) {
                        crearTareaSubirDocumentoABETPendienteSoloTarea(elProyecto);
                    }
                } else if (elProyecto.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_ESPERANDO_ABET_PENDIENTE_ESPECIAL))) {
                    if ((new Timestamp(elProyecto.getSemestreIniciacion().getLevantarPendienteEspecial().getTime())).after(hoy)) {
                        crearTareaSubirDocumentoABETPendienteEspecialSoloTarea(elProyecto);
                    }
                }

            }


        }

    }

    //------------------------------------------------------
    // MÉTODOS PARA EXTERNOS (VERSIÓN LIGHT)
    //------------------------------------------------------
    public String consultarProyectosDeGradoParaExternos(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreoAsesor = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));

            //Valida que llegue la secuencia del correo, y que esta no esté nula o en blanco
            if (secCorreoAsesor == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROYECTOS_DE_GRADO_PARA_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
            if (secCorreoAsesor.getValor() == null || secCorreoAsesor.getValor().equals("")) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROYECTOS_DE_GRADO_PARA_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            }

            //Realiza la consulta de proyectos de grado por correo del asesor
            Collection<ProyectoDeGrado> proyectos = proyectoGradoFacade.findByCorreoAsesor(secCorreoAsesor.getValor());
            Secuencia secProyectos = getConversorProyectoDeGrado().pasarProyectosDeGradoASecuenciaLight(proyectos);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secProyectos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROYECTOS_DE_GRADO_PARA_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROYECTOS_DE_GRADO_PARA_EXTERNOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    //-------------------------------------------------------------------------------
    // MÉTODOS DE SECUENCIAS PARA EXTERNOS (VERSIÓN LIGHT)
    //-------------------------------------------------------------------------------
    public String actualizarProyectoGradoEstado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdProyectoDeGrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secEstado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESTADO_TESIS));

            // Actualiza con facade.
            ProyectoDeGrado proyecto = this.proyectoGradoFacade.find(new Long(secIdProyectoDeGrado.getValor()));
            proyecto.setEstadoTesis(secEstado.getValor());
            this.proyectoGradoFacade.edit(proyecto);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_PROYECTO_GRADO_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_PROYECTO_GRADO_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public String reprobarProyectoGrado(String xml) {
        try {
            parser.leerXML(xml);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secNota = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
            Secuencia secIdProyectoDeGrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            double nota = Double.parseDouble(secNota.getValor());
            String id = secIdProyectoDeGrado.getValor();
            ProyectoDeGrado pg = proyectoGradoFacade.find(new Long(id));
            if (pg.getEstadoTesis().equals(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA))) {
                // No puedo reprobar un pg que ya finalizo
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0012, new ArrayList());
            }

            // Actualiza con facade.
            pg.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_TERMINADA));
            pg.setCalificacionTesis(nota);
            this.proyectoGradoFacade.edit(pg);

            // Completa las tareas correspondientes a este proyecto de grado
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_ASESOR));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_PROPUESTA_TESIS_PROYECTO_DE_GRADO_ASESOR));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_INGRESAR_COMENTARIO_TESIS));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_INFORME_RETIRO));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ENVIAR_APROBACION_PENDIENTE_ESPECIAL));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_AFICHE_PROYECTO_DE_GRADO_ASESOR_PENDIENTE_ESPECIAL));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_AFICHE_PENDIENTE_ESPECIAL));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_NOTA_PROYECTO_GRADO_PENDIENTE_ESPECIAL));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE));
            realizarTareasPendientes(id, getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SUBIR_ARCHIVO_ABET_PENDIENTE_ESPECIAL));

            // Envio de correos
            String para;
            String asunto;
            String mensaje;
            String nombresEst = pg.getEstudiante().getPersona().getNombres() + " " + pg.getEstudiante().getPersona().getApellidos();
            String nombresProfesor = pg.getAsesor().getPersona().getNombres() + pg.getAsesor().getPersona().getApellidos();

            //Correo notificando al estudiante
            para = pg.getEstudiante().getPersona().getCorreo();
            asunto = Notificaciones.ASUNTO_PROYECTO_GRADO_REPROBADO_ESTUDIANTE;
            mensaje = String.format(Notificaciones.MENSAJE_PROYECTO_GRADO_REPROBADO_ESTUDIANTE, nombresEst, nombresProfesor, "" + nota);
            correoBean.enviarMail(para, asunto, null, null, null, mensaje);

            //Correo confirmando la nota al profesor
            para = pg.getAsesor().getPersona().getCorreo();
            asunto = String.format(Notificaciones.ASUNTO_PROYECTO_GRADO_REPROBADO_PROFESOR, nombresEst);
            mensaje = String.format(Notificaciones.MENSAJE_PROYECTO_GRADO_REPROBADO_PROFESOR, nombresEst, "" + nota);
            correoBean.enviarMail(para, asunto, null, null, null, mensaje);

            //Correo notificando a coordinacion
            para = getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION);
            asunto = String.format(Notificaciones.ASUNTO_PROYECTO_GRADO_REPROBADO_COORDINACION, nombresEst);
            mensaje = String.format(Notificaciones.MENSAJE_PROYECTO_GRADO_REPROBADO_COORDINACION, nombresEst, "" + nota);
            correoBean.enviarMail(para, asunto, null, null, null, mensaje);

            // Respuesta.
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPROBAR_PROYECTO_GRADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void realizarTareasPendientes(String idPG, String tipoTarea) {
        //Realiza la tarea
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), idPG);
        TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipoTarea, propiedades));
        if (borrar != null) {
            tareaSencillaBean.realizarTareaPorId(borrar.getId());
        }
    }

    private void reportarEntregaTardia(Date fechaLimite, String comando, String accion, String login) {
        Timestamp actual = new Timestamp(System.currentTimeMillis());
        Timestamp acordada = new Timestamp(fechaLimite.getTime());
        String proceso = getConstanteBean().getConstante(Constantes.VAL_PROCESO_PROYECTO_GRADO);
        String modulo = getConstanteBean().getConstante(Constantes.VAL_MODULO_PROYECTO_GRADO);
        accionVencidaBean.guardarAccionVencida(acordada, actual, accion, login, proceso, modulo, comando, "");
    }

    public String aprobarProyectoGradoCoordinacion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIdProyectoDeGrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secAprobado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_COORDINADOR_MAESTRIA));

            boolean aprobado = Boolean.parseBoolean(secAprobado.getValor());
            ProyectoDeGrado proyecto = this.proyectoGradoFacade.find(new Long(secIdProyectoDeGrado.getValor()));

            //Realiza la tarea
            Properties propiedades = new Properties();
            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), proyecto.getId().toString());
            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_APROBAR_INSCRIPCION_TESIS_PROYECTO_DE_GRADO_COORDINACION), propiedades));
            if (borrar != null) {
                tareaSencillaBean.realizarTareaPorId(borrar.getId());
            }

            if (aprobado) {
                proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));

                crearTareaAprobarProyectoGradoPorAsesor(proyecto);

            } else {
                Secuencia secRazon = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RAZON));
                String correo = Notificaciones.MENSAJE_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION;
                String nombres = proyecto.getEstudiante().getPersona().getNombres();
                String razon = (secRazon.getValor() != null && !secRazon.getValor().equals("") && !secRazon.getValor().trim().isEmpty()) ? "La razón del rechazo fue:<br>" + secRazon.getValor() : "";
                correo = String.format(correo, nombres, proyecto.getTemaTesis(), proyecto.getSemestreIniciacion().getNombre(), razon);
                proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_PY_RECHAZADA_POR_COORDINACION));
                correoBean.enviarMail(proyecto.getEstudiante().getPersona().getCorreo(), Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_PROYECTO_DE_GRADO_COORDINACION, null, null, null, correo);
            }
            this.proyectoGradoFacade.edit(proyecto);

            if (!aprobado) {
                migrarTesisRechazada(proyecto);
            }
            //regenerarTareaAprobarCoordinacion(proyecto.getSemestreIniciacion().getNombre());

            // Respuesta.
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_PROYECTO_GRADO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_PROYECTO_GRADO_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String crearSolicitudProyectoXCoordinacion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secProyectoGrado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROYECTO_DE_GRADO));
            ProyectoDeGrado proyecto = getConversorProyectoDeGrado().pasarSecuenciaAProyectoDeGrado(secProyectoGrado);
            Estudiante estudiante = proyecto.getEstudiante();

            //valida que el estudiante este activo
            if (estudiante == null) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0013, new ArrayList());
            }
            if (estudiante.getTipoEstudiante() == null) {
                NivelFormacion nf = nivelFormacionFacade.findByName(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PREGRADO));
                estudiante.setTipoEstudiante(nf);
                estudianteFacadeRemote.edit(estudiante);
            }

            //valida que el estudiante sea de pregrado
            if (!estudiante.getTipoEstudiante().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PREGRADO))) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0005, new ArrayList());

            }

            Collection<ProyectoDeGrado> otrasTesis = proyectoGradoFacade.findByCorreoEstudiante(proyecto.getEstudiante().getPersona().getCorreo());
            for (ProyectoDeGrado proyectoDeGrado : otrasTesis) {
                if (proyectoDeGrado.getSemestreIniciacion().getNombre().equals(proyecto.getSemestreIniciacion().getNombre())) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_PREGRADO_ERR_0003, new ArrayList());

                }
            }

            // Queda en estado espera documento propuesta, mismo estado despues que aprueba el profesor.
            proyecto.setEstadoTesis(getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA));
            proyecto.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.TRUE));
            proyectoGradoFacade.create(proyecto);

            // Crea la tarea de recordar al estudiante enviar documento propuesta.
            crearTareaEstudianteDocumentoPropuesta(proyecto);

            // Envia un correo al asesor.
            String asunto = Notificaciones.ASUNTO_PROYECTO_DE_GRADO_CREADO_COORDINACION;
            String mensaje = Notificaciones.MENSAJE_PROYECTO_DE_GRADO_CREADO_COORDINACION;
            mensaje = mensaje.replaceFirst("%", proyecto.getAsesor().getPersona().getNombres() + " " + proyecto.getAsesor().getPersona().getApellidos());
            mensaje = mensaje.replaceFirst("%", proyecto.getEstudiante().getPersona().getNombres() + " " + proyecto.getEstudiante().getPersona().getApellidos());
            correoBean.enviarMail(proyecto.getAsesor().getPersona().getCorreo(), asunto, null, null, null, mensaje);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_AGREGAR_SOLICITUD_PROYECTO_DE_GRADO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        return parser;
    }

    private void setParser(ParserT parser) {
        this.parser = parser;
    }

    private ConversorProyectoDeGrado getConversorProyectoDeGrado() {
        return new ConversorProyectoDeGrado(getConstanteBean(), profesorFacade, categoriaTesisFacade, periodoFacadelocal, estudianteFacadeRemote);
    }

    public Collection<AccionBO> darAcciones(String rol, String login) {

        acciones = new ArrayList<AccionBO>();
        Estudiante estudiante = estudianteFacadeRemote.findByCorreo(login);
        String cteRolEstudiante = getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE);
        NivelFormacion nivelDeFormacion = estudiante.getTipoEstudiante();
        String cteNivelPregrado = getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PREGRADO);
        String cteNivelDoctorado = getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_DOCTORADO);
        String cteNivelEspecializacion = getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESPECIALIZACION);
        String cteNivelMaestría = getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_MAESTRIA);

        if (rol.equals(cteRolEstudiante)) {

            if (nivelDeFormacion != null) {

                if (nivelDeFormacion.getNombre().equals(cteNivelPregrado)) {

                    agregarAccionesPregrado();
                } else if (!nivelDeFormacion.getNombre().equals(cteNivelDoctorado) && !nivelDeFormacion.getNombre().equals(cteNivelMaestría) && !nivelDeFormacion.getNombre().equals(cteNivelEspecializacion)) {
                    //si el nivel de formación es diferente de null y además es diferente de doctorado, maestría y especialización, agregue las acciones por seguridad

                    agregarAccionesPregrado();
                }
            } else {
                agregarAccionesPregrado();
            }
        }
        return acciones;
    }

    public String darFechasProyectoGradoPeriodoActual(String xml) {

        try {
            parser.leerXML(xml);
            Secuencia secPeriodo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
            String nombrePeriodo = secPeriodo.getValor();
            PeriodoTesisPregrado periodo = periodoFacadelocal.findByPeriodo(nombrePeriodo);
            if (periodo == null) {
                PeriodoTesisPregrado periodoNuevo = new PeriodoTesisPregrado();
                periodoNuevo.setNombre(nombrePeriodo);
                periodoFacadelocal.create(periodoNuevo);
                periodo = periodoFacadelocal.findByPeriodo(nombrePeriodo);
            }

            Secuencia secResp = getConversorProyectoDeGrado().pasarPeriodoPregradoConfiguracionASecuencia(periodo);
            ArrayList<Secuencia> param = new ArrayList<Secuencia>();
            param.add(secResp);
            return getParser().generarRespuesta(param, getConstanteBean().getConstante(Constantes.CMD_FECHA_PROYECTO_GRADO_POR_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_FECHA_PROYECTO_GRADO_POR_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ProyectoDeGradoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

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

        acciones.add(new AccionBO(
                Notificaciones.NOMBRE_ACCION_CONSULTAR_FECHA_TESIS_PREGRADO,
                Notificaciones.DESCRIPCION_ACCION_CONSULTAR_FECHA_TESIS_PREGRADO,
                getConstanteBean().getConstante(Constantes.VAL_ACCION_CONSULTAR_FECHA_TESIS_PREGRADO),
                getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_TESIS_PREGRADO)));
    }
}
