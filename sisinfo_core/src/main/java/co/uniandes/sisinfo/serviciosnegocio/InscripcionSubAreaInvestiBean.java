/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.CursoMaestria;
import co.uniandes.sisinfo.entities.CursoTesis;
import co.uniandes.sisinfo.entities.InscripcionSubareaInvestigacion;
import co.uniandes.sisinfo.entities.PeriodoTesis;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.CursoMaestriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CursoTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InscripcionSubareaInvestigacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoTesisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SubareaInvestigacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
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
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class InscripcionSubAreaInvestiBean implements InscripcionSubAreaInvestiBeanRemote, InscripcionSubAreaInvestiBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
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
    /*   @EJB
    private AlertaRemote alertaBean;
    @EJB
    private TareaRemote tareaBean;
    @EJB
    private TareaFacadeRemote tareaFacade;*/
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private CursoMaestriaFacadeLocal cursoMaestriaFacade;
    @EJB
    private CursoTesisFacadeLocal cursoTesisFacade;
    @EJB
    private AccionVencidaBeanRemote accionVencidaBean;
    //---OTROS--------------------
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorTesisMaestria conversor;
    /**
     * -------------nuevo manejo de historicos
     **/
    @EJB
    private TareaSencillaRemote tareaSencillaBean;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;

    public InscripcionSubAreaInvestiBean() {

        try {
            conversor = new ConversorTesisMaestria();
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            estudianteFacadeRemote = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            subareaInvestigacionFacadeRemote = (SubareaInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(SubareaInvestigacionFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            /*   alertaBean = (AlertaRemote) serviceLocator.getRemoteEJB(AlertaRemote.class);
            tareaFacade = (TareaFacadeRemote) serviceLocator.getRemoteEJB(TareaFacadeRemote.class);
            tareaBean = (TareaRemote) serviceLocator.getRemoteEJB(TareaRemote.class);*/
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            accionVencidaBean = (AccionVencidaBeanRemote) serviceLocator.getRemoteEJB(AccionVencidaBeanRemote.class);
            /*
             * nuevo manejo de tareas
             */
            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);

        } catch (Exception e) {
            Logger.getLogger(InscripcionSubAreaInvestiBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //=====================================================================================================
    //===================================  Ingreso Subarea Investigacion  =======================================================
    //=====================================================================================================
    /**
     * metodo que devuelve todas las materias existentes de maestria
     * @param xml
     * @return
     */
    public String obtenerMateriasMaestria(String xml) {
        try {
            parser.leerXML(xml);
            Collection<CursoMaestria> cursos = cursoMaestriaFacade.findAll();
            Secuencia secCursosMaestria = conversor.pasarCursosMaestriaAsecuencia(cursos);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCursosMaestria);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_MAESTRIA_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * metodo que busca materias de maestria segun su clasificacion
     * @param xml
     * @return
     */
    public String obtenerMateriasMaestriaPorClasificacion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secClasificacion = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CLASIFICACION));
            Collection<CursoMaestria> cursos = cursoMaestriaFacade.findByClasificacion(secClasificacion.getValor());
            Secuencia secCursosMaestria = conversor.pasarCursosMaestriaAsecuencia(cursos);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCursosMaestria);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_MAESTRIA_TESIS_POR_CLASIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * metodo que retorna una solicitud de ingreso a subarea por correo de estudainte
     * @param xml
     * @return
     */
    public String darSlicitudIngresosubareaEstudiantePorCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null) {
                String correo = secCorreo.getValor();
                InscripcionSubareaInvestigacion inscripcion = inscrippcionsubFacadeLocal.findByCorreoEstudiante(correo);
                if (inscripcion != null) {
                    Secuencia secInscripcionSubarea = conversor.pasarInscripcionSubareaASecuencia(inscripcion);

                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(secInscripcionSubarea);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SOLICITUD_ESTUDIANTE_SUBAREA_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    Secuencia secuenciaPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), "");
                    secuencias.add(secuenciaPrincipal);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SOLICITUD_ESTUDIANTE_SUBAREA_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            } else {
                throw new Exception("Error- xml mal formado en metodo darSlicitudIngresosubareaEstudiantePorCorreo : TesisBean");
            }

        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * metodo llamado por el director de un depto para aprobar el ingreso de un estudiante
     * a una subarea de investigacion
     * @param xml
     * @return
     */
    public String aprobarInscripcionASubareaDirector(String xml) {
        try {
            /*
             * xml:
             * <idgeneral>asfsdf<idGeneral>
             * <aprobadoSubarea>TRUE/FALSE</aprobadoSubarea>
             */
            parser.leerXML(xml);
            Secuencia idInscripcion = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia valorAprobado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_SUBAREA));

            Long id = Long.parseLong(idInscripcion.getValor().trim());
            InscripcionSubareaInvestigacion inscrip = inscrippcionsubFacadeLocal.find(id);
            if (inscrip != null) {
                //--------terminar tarea coordinador subarea------------------------------------------
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());
                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR), propiedades);

                /* Collection<Tarea> tareas = tareaFacade.darTareasEstadoTipoYParametros(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR), propiedades);
                for (Tarea tarea : tareas) {
                tareaBean.realizarTarea(tarea.getId());
                //     tareaFacade.remove(tarea);
                break;
                }*/

                //FIN--------terminar tarea coordinador subarea------------------------------------------
                String valor = valorAprobado.getValor();
                if (inscrip.getSemestreInicioTesis1().getMaxFechaAprobacionInscripcionSubareaCoordinacion().before(new Date())) {
                    String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_INSCRIPCION_A_SUBAREA_DIRECTOR);
                    String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINACION);
                    String login = inscrip.getSubareaInvestigacion().getCoordinadorSubarea().getCorreo();
                    String infoAdicional = "La inscripcion a subarea del estudiante " + inscrip.getEstudiante().getPersona().getCorreo() + " fue " + (valor.equals(getConstanteBean().getConstante(Constantes.TRUE)) ? "aprobada" : "reprobada");
                    reportarEntregaTardia(inscrip.getSemestreInicioTesis1().getMaxFechaAprobacionInscripcionSubareaCoordinacion(), comando, accion, login, infoAdicional);
                }

                if (valor.equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                    inscrip.setAprobadoSubArea(valor);
                    inscrip.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
                    inscrippcionsubFacadeLocal.edit(inscrip);
                    //Mandar un correo al estudiante informándole que se aprobó la inscripción
                    String asunto = Notificaciones.ASUNTO_APROBACION_INSCRIPCION_SUBAREA;
                    String mensaje = Notificaciones.MENSAJE_APROBACION_INSCRIPCION_SUBAREA;

                    mensaje = mensaje.replaceFirst("%", inscrip.getEstudiante().getPersona().getNombres() + " " + inscrip.getEstudiante().getPersona().getApellidos());
                    mensaje = mensaje.replaceFirst("%", inscrip.getSubareaInvestigacion().getNombreSubarea());

                    correoBean.enviarMail(inscrip.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);

                } else if (valor.equals(getConstanteBean().getConstante(Constantes.FALSE))) {
                    inscrip.setAprobadoSubArea(valor);
                    inscrip.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_COORDINADOR_SUBAREA));
                    inscrippcionsubFacadeLocal.edit(inscrip);
                    //Crear correo para informar que se rechazo la inscripción
                    String asuntoCreacion = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_SUBAREA_INVESTIGACION;
                    String mensajeCreacion = Notificaciones.MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_SUBAREA_INVESTIGACION;

                    mensajeCreacion = mensajeCreacion.replaceFirst("%", inscrip.getEstudiante().getPersona().getNombres());
                    mensajeCreacion = mensajeCreacion.replaceFirst("%", inscrip.getSubareaInvestigacion().getNombreSubarea());
                    mensajeCreacion = mensajeCreacion.replaceFirst("%", inscrip.getSubareaInvestigacion().getCoordinadorSubarea().getNombres() + " " + inscrip.getSubareaInvestigacion().getCoordinadorSubarea().getApellidos());

                    correoBean.enviarMail(inscrip.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                    //FIN-------------Crear correo para informar que se rechazo la inscripción
                }
            }

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_INSCRIPCION_A_SUBAREA_DIRECTOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * metodo llamado por el asesor para aprobar el ingreso de un estudiante
     * a una subarea de investigacion
     * @param xml
     * @return
     */
    public String aprobarInscripcionASubareaAsesor(String xml) {
        try {
            /*
             * xml:
             * <idgeneral>asfsdf<idGeneral>
             * <aprobadoSubarea>TRUE/FALSE</aprobadoSubarea>
             */
            parser.leerXML(xml);
            Secuencia idInscripcion = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia valorAprobado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_SUBAREA));
            Long id = Long.parseLong(idInscripcion.getValor().trim());
            InscripcionSubareaInvestigacion inscrip = inscrippcionsubFacadeLocal.find(id);
            if (inscrip != null) {
                //terminar TAREA del asesor:------------------------------------------------------------------------------------
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), id.toString());

                completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR), propiedades);

                /*TODO: BORRAR  Collection<Tarea> tareas = tareaFacade.darTareasEstadoTipoYParametros(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR), propiedades);
                for (Tarea tarea : tareas) {
                tareaBean.realizarTarea(tarea.getId());
                break;
                }*/


                //---------------------------------------------------------------------------------------------------
                String valor = valorAprobado.getValor();
                if (inscrip.getSemestreInicioTesis1() != null && inscrip.getSemestreInicioTesis1().getMaxFechaAprobacionInscripcionSubarea() != null && inscrip.getSemestreInicioTesis1().getMaxFechaAprobacionInscripcionSubarea().before(new Date())) {
                    String comando = getConstanteBean().getConstante(Constantes.CMD_APROBAR_INSCRIPCION_A_SUBAREA_ASESOR);
                    String accion = getConstanteBean().getConstante(Constantes.VAL_ACCION_APROBAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR);
                    String login = inscrip.getAsesor().getPersona().getCorreo();
                    String infoAdicional = "La inscripcion a subarea del estudiante " + inscrip.getEstudiante().getPersona().getCorreo() + " fue " + (valor.equals(getConstanteBean().getConstante(Constantes.TRUE)) ? "aprobada" : "reprobada");
                    reportarEntregaTardia(inscrip.getSemestreInicioTesis1().getMaxFechaAprobacionInscripcionSubarea(), comando, accion, login, infoAdicional);
                }

                if (valor.equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                    inscrip.setAprobadoAsesor(valor);

                    //si la inscripcion ya habia sido aprobada por el coordinador de subarea no se le vuelve a pasar, se coloca como en curso y fin
                    if (inscrip.isAprobadoSubArea().equals(getConstanteBean().getConstante(Constantes.TRUE))) {
                        inscrip.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
                    } else {
                        inscrip.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_COORDINADOR_SUB_AREA));

                        // crearle tarea al coordinador del grpo----------------------------------------------
                        //4.1 notificacion:
                        //Crear alerta de inscripciones
                        crearTareaCoordinadorSubareaAprobarInscripcionSubarea(inscrip);
                    }

                } else if (valor.equals(getConstanteBean().getConstante(Constantes.FALSE))) {
                    inscrip.setAprobadoAsesor(valor);
                    inscrip.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_RECHAZADA_POR_ASESOR));
                    //enviar correo al estudiante, de rechazo por parte del asesor
                    //Crear correo para informar que se rechazo la inscripción
                    String asuntoCreacion = Notificaciones.ASUNTO_RECHAZO_INSCRIPCION_SUBAREA_INVESTIGACION;
                    String mensajeCreacion = Notificaciones.MENSAJE_NOTIFICAR_RECHAZO_INSCRIPCION_SUBAREA_INVESTIGACION;
                    mensajeCreacion = mensajeCreacion.replaceFirst("%", inscrip.getEstudiante().getPersona().getNombres());
                    mensajeCreacion = mensajeCreacion.replaceFirst("%", inscrip.getSubareaInvestigacion().getNombreSubarea());
                    mensajeCreacion = mensajeCreacion.replaceFirst("%", inscrip.getAsesor().getPersona().getNombres() + " " + inscrip.getAsesor().getPersona().getApellidos());
                    correoBean.enviarMail(inscrip.getEstudiante().getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                }
                inscrippcionsubFacadeLocal.edit(inscrip);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_APROBAR_INSCRIPCION_A_SUBAREA_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retorna todas las solicitudes a subárea
     * @param xml
     * @return
     */
    public String darSolicitudesIngresoSubarea(String xml) {
        try {
            parser.leerXML(xml);
            Collection<InscripcionSubareaInvestigacion> inscripciones = inscrippcionsubFacadeLocal.findAll();
            Secuencia secInscripciones = conversor.pasarInscripcionesSubareaASecuenciaLigero(inscripciones);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secInscripciones);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_INGRESO_SUBAREA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * metodo que segun el correo del coordinador devuelve las solicitudes de ingreso a subarea segun un correo
     * @param xml
     * @return
     */
    public String darSolicitudesIngresoSubareaParaCoordinador(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secTipoSolicitud = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_SOLICITUD));
            if (secCorreo != null && secTipoSolicitud != null) {
                Collection<InscripcionSubareaInvestigacion> ins = inscrippcionsubFacadeLocal.findByCorreoCoordinadorYEstado(secCorreo.getValor(), secTipoSolicitud.getValor());
                Secuencia secInscripciones = conversor.pasarInscripcionesSubareaASecuencia(ins);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secInscripciones);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else if (secCorreo != null) {
                Collection<InscripcionSubareaInvestigacion> ins = inscrippcionsubFacadeLocal.findByCorreoCoordinador(secCorreo.getValor());
                Secuencia secInscripciones = conversor.pasarInscripcionesSubareaASecuencia(ins);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secInscripciones);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_COORDINADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            }
            throw new Exception("Formato xml invalido en metodo(falto el correo del coordinador): darSolicitudesIngresoSubareaParaCoordinador-TesisBean");
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * devuelve las solicitudes que debe aprobar un asesor(por correo), para que despues sean pasadas a coordinacion
     * del depto
     * @param xml
     * @return
     */
    public String darSolicitudesIngresoSubAreaAsesor(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secTipoSolicitud = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_SOLICITUD));
            if (secCorreo != null && secTipoSolicitud != null) {
                Collection<InscripcionSubareaInvestigacion> ins = inscrippcionsubFacadeLocal.findByCorreoAsesorYestado(secCorreo.getValor(), secTipoSolicitud.getValor());
                Secuencia secInscripciones = conversor.pasarInscripcionesSubareaASecuencia(ins);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secInscripciones);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else if (secCorreo != null) {
                Collection<InscripcionSubareaInvestigacion> ins = inscrippcionsubFacadeLocal.findByCorreoAsesor(secCorreo.getValor());
                Secuencia secInscripciones = conversor.pasarInscripcionesSubareaASecuencia(ins);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secInscripciones);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_INGRESO_SUBAREA_ASESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            }
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * metodo que crea una solicitud para un estudiante
     * @param xml
     * @return
     */
    public String crearSolicitudIngresoSubareaEstudiante(String xml) {
        try {

            parser.leerXML(xml);
            //Secuencia secSolicitudes = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            Secuencia secSolicitud = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD));
            InscripcionSubareaInvestigacion inscripcion = conversor.pasarSecuenciaAInscripcionSubarea(secSolicitud);

            //Verificamos si se puede hacer una inscripción a subarea dada la fecha
            PeriodoTesis periodoTesis = inscripcion.getSemestreInicioTesis1();
            long fechaActual = new Date().getTime();
            if (periodoTesis != null && periodoTesis.getMaxFechaInscripcionSubarea() != null
                    && fechaActual >= periodoTesis.getMaxFechaInscripcionSubarea().getTime()) {
                //formatea la fecha para retornarla como información para el error
                SimpleDateFormat sdfFechaMax = new SimpleDateFormat("dd/MM/yyyy");
                String strMaxFecha = sdfFechaMax.format(new Date(periodoTesis.getMaxFechaInscripcionSubarea().getTime()));
                //construye el parámetro para el error
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), strMaxFecha);
                //agrega el parámetro a la lista
                parametros.add(secParametro);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00028, parametros);
            }

            // verificar que exista el estudiante, profesor, asesor, y # correcto de cursos...


            int numCursos = inscripcion.getSubArea().size();
            if (inscripcion.getOtraMaestria() == null) {
                System.out.println("**********************************Es null");
            } else {
                System.out.println("**********************************NO Es null");
            }

            System.out.println("********** numCursos = " + numCursos);



            if ((inscripcion.getOtraMaestria() == null && inscripcion.getSubArea().size() < 7) || (inscripcion.getOtraMaestria() != null && inscripcion.getSubArea().size() < 6)) {
                //:enviar error faltan cursos...
                System.out.println("**********************************ENTRÓ");
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0002, new ArrayList());
            }


            /*
            if (inscripcion.getObligatorias().size() != 2 || inscripcion.getSubArea().size() != 3 || inscripcion.getOtraSubArea() == null || inscripcion.getOtraMaestria() == null) {
            //:enviar error faltan cursos...
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0002, new ArrayList());
            }
             */

            if (inscripcion.getAsesor() == null || inscripcion.getEstudiante() == null) {
                //:enviar error profesor no existe... o
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0003, new ArrayList());
            }
            if (inscripcion.getAsesor().getNivelPlanta() == null || inscripcion.getAsesor().getNivelPlanta().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_INSTRUCTOR))) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00013, new ArrayList());
            }
            //-----------
            //en caso de que la inscripcion no exista, se crea e inicia proceso 1
            InscripcionSubareaInvestigacion inscripcionEnBD = inscrippcionsubFacadeLocal.findByCorreoEstudiante(inscripcion.getEstudiante().getPersona().getCorreo());
            String estabaAprobadoAsesor = "";
            if (inscripcionEnBD != null) {
                estabaAprobadoAsesor = inscripcionEnBD.isAprobadoAsesor();
            }
            inscripcion.setId(inscripcionEnBD != null ? inscripcionEnBD.getId() : null);
            //si la inscripcion no existe
            if (inscripcion.getId() == null && inscripcionEnBD == null) {
                //=>LA INSCRIPCION ES NUEVA:
                inscripcion.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
                inscripcion.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
                inscripcion.setAprobadoSubArea(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
                //Nivelatorios
                Collection<CursoTesis> cursos = inscripcion.getNivelatorios();
                if (cursos != null) {
                    for (CursoTesis cursoTesis : cursos) {
                        cursoTesisFacade.create(cursoTesis);
                    }
                }
                //Obligatorios
                cursos = inscripcion.getObligatorias();
                if (cursos != null) {
                    for (CursoTesis cursoTesis : cursos) {
                        cursoTesisFacade.create(cursoTesis);
                    }
                }
                //subarea
                cursos = inscripcion.getSubArea();
                if (cursos != null) {
                    for (CursoTesis cursoTesis : cursos) {
                        cursoTesisFacade.create(cursoTesis);
                    }
                }
                if (inscripcion.getOtraMaestria() != null) {
                    cursoTesisFacade.create(inscripcion.getOtraMaestria());
                }
                if (inscripcion.getOtraSubArea() != null) {
                    cursoTesisFacade.create(inscripcion.getOtraSubArea());
                }
                inscrippcionsubFacadeLocal.create(inscripcion);
                //: crear tarea para el Asesosr de aprobar---------------------------------------------------
                //4.1 notificacion:
                //Crear alerta de inscripciones
                {

                    crearTareaAsesorDeAprobarSubareaInscripcion(inscripcion, periodoTesis, Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO);
                    //---------------------------------------------------------------------------------------------------------------------

                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            } else {//la inscripcion ya existe:
                //=> si la subarea es diferente o no a sido aprobada por el coordinador de subarea, sigue el proceso normal a asesor y despues coordinacion
                if (!inscripcionEnBD.getSubareaInvestigacion().getNombreSubarea().equals(inscripcion.getSubareaInvestigacion().getNombreSubarea())
                        || inscripcionEnBD.isAprobadoAsesor().equals(getConstanteBean().getConstante(Constantes.FALSE))
                        || inscripcion.getAsesor().getId() != inscripcionEnBD.getAsesor().getId()) {
                    //--------terminar tarea antiguo coordinador subarea------------------------------------------
                    {
                        Properties propiedades = new Properties();
                        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscripcionEnBD.getId().toString());
                        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR), propiedades);

                    }
                    //quitar tarea antiguo asesor----------------
                    //terminar TAREA del asesor:------------------------------------------------------------------------------------
                    {
                        Properties propiedades = new Properties();
                        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscripcionEnBD.getId().toString());
                        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR), propiedades);
                    }
                    //EDITAR LA INSCRIPCION--------------------------------------------------------------------------------------
                    inscripcion.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
                    inscripcion.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
                    inscripcion.setAprobadoSubArea(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
                    inscrippcionsubFacadeLocal.edit(inscripcion);
                    //: crear tarea para el Asesosr de aprobar---------------------------------------------------
                    //4.1 notificacion:
                    //Crear alerta de inscripciones
                    crearTareaAsesorDeAprobarSubareaInscripcion(inscripcion, periodoTesis, Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO);

                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } //Entra aca en caso de que la inscripcion exista y la subarea sea la misma...
                else {
                    // como es la misma subarea y ya fue aprobada no debe volver a pasar por el coordinador por que este ya lo aprobo
                    //--------------------------------------------------------------------------------------
                    inscripcion.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_POR_APROBAR_ASESOR));
                    inscripcion.setAprobadoAsesor(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_PENDIENTE));
                    inscripcion.setAprobadoSubArea(getConstanteBean().getConstante(Constantes.TRUE));
                    inscrippcionsubFacadeLocal.edit(inscripcion);
                    //: crear tarea para el Asesosr de aprobar---------------------------------------------------                   
                    String mensaje = "";
                    if (inscripcion.getAsesor().getId() != inscripcionEnBD.getAsesor().getId()) {
                        mensaje = Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO;
                    } else {
                        mensaje = Notificaciones.MENSAJE_BULLET_MODIFICADO_APROBAR_INSCRIPCION_SUBAREA_DIRECTOR_SUBAREA;
                    }
                    crearTareaAsesorDeAprobarSubareaInscripcion(inscripcion, periodoTesis, mensaje);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        /**
         * xml respuesta:
         *   ok|| errror si:
         *                  -el profesor no puede ser asesor (nivelFormacion>doctorado)=>PREguntar no se a habladono seguro
         */
    }

    /**
     * Método que ingresa a un estudiante a una subárea (casos especiales)
     * @param xml
     * @return
     */
    public String crearSolicitudIngresoSubareaEstudiantePorCoordinacion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secSolicitud = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD));
            InscripcionSubareaInvestigacion inscripcion = conversor.pasarSecuenciaAInscripcionSubarea(secSolicitud);

            boolean existe = false;
            Secuencia secEsAprobacionDeExistente = secSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_COORDINADOR_MAESTRIA));
            if (secEsAprobacionDeExistente != null && secEsAprobacionDeExistente.getValor() != null) {
                existe = Boolean.parseBoolean(secEsAprobacionDeExistente.getValor());
            }

            if (existe) {
                InscripcionSubareaInvestigacion inscripcionEnBD = inscrippcionsubFacadeLocal.find(inscripcion.getId());
                if (inscripcionEnBD != null) {
                    inscripcionEnBD.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
                    inscripcionEnBD.setAprobadoAsesor(getConstanteBean().getConstante(getConstanteBean().getConstante(Constantes.TRUE)));
                    inscripcionEnBD.setAprobadoSubArea(getConstanteBean().getConstante(getConstanteBean().getConstante(Constantes.TRUE)));
                    inscrippcionsubFacadeLocal.edit(inscripcionEnBD);
                }

                terminarTareasAsociadasAInscripcionSubarea(inscripcionEnBD);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            } else {
                //Verificar que exista el estudiante, profesor, asesor, y # correcto de cursos

                int numCursos = inscripcion.getSubArea().size();

                if (inscripcion.getOtraMaestria() == null) {
                    System.out.println("**********************************Es null");
                } else {
                    System.out.println("**********************************NO Es null");
                }

                System.out.println("numCursos = " + numCursos);

                if ((inscripcion.getOtraMaestria() == null && inscripcion.getSubArea().size() < 7) || (inscripcion.getOtraMaestria() != null && inscripcion.getSubArea().size() < 6)) {
                    System.out.println("**********************************ENTRÓ");
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0002, new ArrayList());
                }

                /*if (inscripcion.getObligatorias().size() != 2 || inscripcion.getSubArea().size() != 3 || inscripcion.getOtraSubArea() == null || inscripcion.getOtraMaestria() == null) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0002, new ArrayList());
                }
                 * */

                if (inscripcion.getAsesor() == null || inscripcion.getEstudiante() == null) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_0003, new ArrayList());
                }
                if (inscripcion.getAsesor().getNivelPlanta() == null || inscripcion.getAsesor().getNivelPlanta().getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_INSTRUCTOR))) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.TESIS_ERR_00013, new ArrayList());
                }

                InscripcionSubareaInvestigacion inscripcionEnBD = inscrippcionsubFacadeLocal.findByCorreoEstudiante(inscripcion.getEstudiante().getPersona().getCorreo());

                //Si la inscripción no existe
                if (inscripcion.getId() == null && inscripcionEnBD == null) {
                    //=>LA INSCRIPCION ES NUEVA:
                    inscripcion.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
                    inscripcion.setAprobadoAsesor(getConstanteBean().getConstante(getConstanteBean().getConstante(Constantes.TRUE)));
                    inscripcion.setAprobadoSubArea(getConstanteBean().getConstante(getConstanteBean().getConstante(Constantes.TRUE)));
                    //Nivelatorios
                    Collection<CursoTesis> cursos = inscripcion.getNivelatorios();
                    if (cursos != null) {
                        for (CursoTesis cursoTesis : cursos) {
                            cursoTesisFacade.create(cursoTesis);
                        }
                    }
                    //Obligatorios
                    cursos = inscripcion.getObligatorias();
                    if (cursos != null) {
                        for (CursoTesis cursoTesis : cursos) {
                            cursoTesisFacade.create(cursoTesis);
                        }
                    }
                    //Subárea
                    cursos = inscripcion.getSubArea();
                    if (cursos != null) {
                        for (CursoTesis cursoTesis : cursos) {
                            cursoTesisFacade.create(cursoTesis);
                        }
                    }
                    if (inscripcion.getOtraMaestria() != null) {
                        cursoTesisFacade.create(inscripcion.getOtraMaestria());
                    }
                    if (inscripcion.getOtraSubArea() != null) {
                        cursoTesisFacade.create(inscripcion.getOtraSubArea());
                    }
                    inscrippcionsubFacadeLocal.create(inscripcion);

                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

                } else { //Si la inscripción ya existe, se adelanta el proceso

                    terminarTareasAsociadasAInscripcionSubarea(inscripcionEnBD);

                    inscripcion.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
                    inscripcion.setAprobadoAsesor(getConstanteBean().getConstante(getConstanteBean().getConstante(Constantes.TRUE)));
                    inscripcion.setAprobadoSubArea(getConstanteBean().getConstante(getConstanteBean().getConstante(Constantes.TRUE)));

                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_SOLICITUD_INGRESO_SUBAREA_ESTUDIANTE_POR_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * devuelve las solicitudes aprobadas por los diferentes coordinadores de subarea
     * @param xml
     * @return
     */
    public String darSolicitudesAprobadasIngresoCoordinacion(String xml) {
        try {
            Collection<InscripcionSubareaInvestigacion> inscripciones = inscrippcionsubFacadeLocal.findByEstadoInscripcion(getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
            Secuencia secInscripciones = conversor.pasarInscripcionesSubareaASecuencia(inscripciones);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secInscripciones);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_APROBADAS_INGRESO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * metodo que deveulve los detalles de una solicitud por id
     * @param xml
     * @return
     */
    public String darSolicitudIngresoSubareaPorId(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secId != null) {
                Long id = Long.parseLong(secId.getValor());
                InscripcionSubareaInvestigacion inscripcion = inscrippcionsubFacadeLocal.find(id);
                Secuencia secInscripcion = conversor.pasarInscripcionSubareaASecuencia(inscripcion);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secInscripcion);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_INGRESOSUBAREA_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            }
            throw new Exception("Secuencia mal Formada en metodo : darSolicitudIngresoSubareaPorId-TesisBean");
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ParserT getParser() {
        return parser;
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

    public void crearTareaAsesorDeAprobarSubareaInscripcion(InscripcionSubareaInvestigacion inscripcion, PeriodoTesis periodoTesis, String mensajeBulletSinFormato) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR);
        Persona profesor = inscripcion.getAsesor().getPersona();
        Date fechaInicioDate = new Date();
        Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
        Date fechaMaxAprobarInscripcion = null;
        if (periodoTesis != null && periodoTesis.getMaxFechaAprobacionInscripcionSubarea() != null) {
            fechaMaxAprobarInscripcion = new Date(periodoTesis.getMaxFechaAprobacionInscripcionSubarea().getTime());
        } else {
            fechaMaxAprobarInscripcion = fechaFinDate;
        }
        String mensajeBulletTarea = String.format(mensajeBulletSinFormato, inscripcion.getEstudiante().getPersona().getNombres() + " "
                + inscripcion.getEstudiante().getPersona().getApellidos(), inscripcion.getEstudiante().getPersona().getCorreo());
        boolean agrupable = true;
        String header = String.format(Notificaciones.MENSAJE_HEADER_APROBAR_INSCRIPCION_SUBAREA_ASESOR, profesor.getNombres() + " " + profesor.getApellidos());
        String footer = Notificaciones.MENSAJE_FOOTER_APROBAR_INSCRIPCION_SUBAREA_ASESOR;
        Timestamp fFin = new Timestamp(fechaMaxAprobarInscripcion.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_INGRESOSUBAREA_POR_ID);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(inscripcion.getId()));
        String rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);

        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_SUBAREA_ASESOR;
        tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, parametros, asunto);

    }

    private void terminarTareasAsociadasAInscripcionSubarea(InscripcionSubareaInvestigacion inscripcionEnBD) {
        Properties propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscripcionEnBD.getId().toString());
        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_ASESOR), propiedades);

        propiedades = new Properties();
        propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscripcionEnBD.getId().toString());
        completarTareaSencilla(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR), propiedades);
    }

    public void crearTareaCoordinadorSubareaAprobarInscripcionSubarea(InscripcionSubareaInvestigacion inscrip) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TERMINAR_INSCRIPCION_SUBAREA_INVESTIGACION_COORDINADOR);
        Persona profesor = inscrip.getSubareaInvestigacion().getCoordinadorSubarea();
        Date fechaInicioDate = new Date();
        Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PeriodoTesis periodoTesis = inscrip.getSemestreInicioTesis1();
        Date fechaMaxAprobarInscripcion = null;
        if (periodoTesis != null && periodoTesis.getMaxFechaAprobacionInscripcionSubareaCoordinacion() != null) {
            Long undia = 1000L * 60L * 60L * 24L;
            fechaMaxAprobarInscripcion = new Date(periodoTesis.getMaxFechaAprobacionInscripcionSubareaCoordinacion().getTime() + undia);
        } else {
            fechaMaxAprobarInscripcion = fechaFinDate;
        }
        String mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_ESTUDIANTE_CORREO, inscrip.getEstudiante().getPersona().getNombres() + " "
                + inscrip.getEstudiante().getPersona().getApellidos(), inscrip.getEstudiante().getPersona().getCorreo());
        boolean agrupable = true;
        String header = String.format(Notificaciones.MENSAJE_HEADER_APROBAR_INSCRIPCION_SUBAREA_DIRECTOR_SUBAREA, profesor.getNombres() + " " + profesor.getApellidos());
        String footer = Notificaciones.MENSAJE_FOOTER_APROBAR_INSCRIPCION_SUBAREA_DIRECTOR_SUBAREA;
        Timestamp fFin = new Timestamp(fechaMaxAprobarInscripcion.getTime());
        String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_INGRESOSUBAREA_POR_ID);
        String rol = getConstanteBean().getConstante(Constantes.ROL_COORDINADOR_SUBAREA_INVESTIGACION);
        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(inscrip.getId()));
        String asunto = Notificaciones.ASUNTO_APROBAR_INSCRIPCION_SUBAREA_DIRECTOR_SUBAREA;
        tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, parametros, asunto);

    }

    @Override
    public String agregarCursoMaestria(String comando) {
        try {
            parser.leerXML(comando);
            CursoMaestria cursoMaestria = conversor.pasarSecuenciaACursoMaestria(parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO)));
            if (cursoMaestria.getId() != null) {
                cursoMaestriaFacade.edit(cursoMaestria);
            } else {
                cursoMaestriaFacade.create(cursoMaestria);
            }

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CURSOS_MAESTRIA_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String obtenerMateriaMaestriaPorId(String comando) {
        try {
            parser.leerXML(comando);
            Long id = Long.parseLong(parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor());
            CursoMaestria cm = cursoMaestriaFacade.find(id);
            Secuencia secCurso = conversor.pasarCursoMaestriaASecuencia(cm);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCurso);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_MAESTRIA_TESIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
