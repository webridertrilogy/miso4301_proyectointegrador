package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.AsistenciaGraduada;
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.RangoFechasGeneral;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.TipoAsistenciaGraduada;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.AsistenciaGraduadaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.EstudiantePosgradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.HojaVidaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InformacionEmpresaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.OfertaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ProponenteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.RangoFechasGeneralFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TipoAsistenciaGraduadaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeLocal;

/**
 * Servicios de administración de asistencias graduadas
 * @author Marcela Morales
 */
@Stateless
public class AsistenciaGraduadaBean implements AsistenciaGraduadaBeanLocal {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Remotos
    @EJB
    private EstudianteFacadeLocal estudianteFacade;
    @EJB
    private PersonaFacadeLocal personaFacade;
    @EJB
    private PeriodoFacadeLocal periodoFacade;
    @EJB
    private PaisFacadeLocal paisFacade;
    @EJB
    private TipoDocumentoFacadeLocal tipoDocumentoFacade;
    @EJB
    private InformacionAcademicaFacadeLocal informacionAcademicaFacade;
    //Locales
    @EJB
    private AsistenciaGraduadaFacadeLocal asistenciaGraduadaFacade;
    @EJB
    private TipoAsistenciaGraduadaFacadeLocal tipoAsistenciaGraduadaFacade;
    @EJB
    private HojaVidaFacadeLocal hojaVidaFacade;
    @EJB
    private EstudiantePosgradoFacadeLocal estudiantePostgradoFacade;
    @EJB
    private ProponenteFacadeLocal proponenteFacade;
    @EJB
    private OfertaFacadeLocal ofertaFacade;
    @EJB
    private InformacionEmpresaFacadeLocal informacionEmpresaFacade;
    //Útiles
    @EJB
    private RangoFechasGeneralFacadeLocal rangoFechasGeneralFacade;
    @EJB
    private TareaSencillaFacadeLocal tareaSencillaFacade;
    @EJB
    private TareaSencillaLocal tareaSencillaBean;
    @EJB
    private CorreoLocal correoBean;
    @EJB
    private TareaMultipleLocal tareaBean;
    @EJB
    private ConstanteLocal constanteBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorBolsaEmpleo conversor;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    /**
     * Constructor de AsistenciaGraduadaBean
     */
    public AsistenciaGraduadaBean() throws NamingException {
//        try {
//            parser = new ParserT();
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            correoBean = (CorreoLocal) serviceLocator.getLocalEJB(CorreoLocal.class);
//            periodoFacade = (PeriodoFacadeLocal) serviceLocator.getLocalEJB(PeriodoFacadeLocal.class);
//            personaFacade = (PersonaFacadeLocal) serviceLocator.getLocalEJB(PersonaFacadeLocal.class);
//            estudianteFacade = (EstudianteFacadeLocal) serviceLocator.getLocalEJB(EstudianteFacadeLocal.class);
//            tareaBean = (TareaMultipleLocal) serviceLocator.getLocalEJB(TareaMultipleLocal.class);
//            paisFacade = (PaisFacadeLocal) serviceLocator.getLocalEJB(PaisFacadeLocal.class);
//            tipoDocumentoFacade = (TipoDocumentoFacadeLocal) serviceLocator.getLocalEJB(TipoDocumentoFacadeLocal.class);
//            informacionAcademicaFacade = (InformacionAcademicaFacadeLocal) serviceLocator.getLocalEJB(InformacionAcademicaFacadeLocal.class);
//            tareaSencillaFacade = (TareaSencillaFacadeLocal) serviceLocator.getLocalEJB(TareaSencillaFacadeLocal.class);
//            tareaSencillaBean = (TareaSencillaLocal) serviceLocator.getLocalEJB(TareaSencillaLocal.class);
//            rangoFechasGeneralFacade = (RangoFechasGeneralFacadeLocal) serviceLocator.getLocalEJB(RangoFechasGeneralFacadeLocal.class);
//            conversor = new ConversorBolsaEmpleo(getConstanteBean(), estudianteFacade, personaFacade, periodoFacade, paisFacade, tipoAsistenciaGraduadaFacade, tipoDocumentoFacade, informacionAcademicaFacade, hojaVidaFacade, estudiantePostgradoFacade, proponenteFacade, ofertaFacade, informacionEmpresaFacade);
//        } catch (NamingException ex) {
//            Logger.getLogger(AsistenciaGraduadaBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //----------------------------------------------
    // MÉTODOS DE NEGOCIO
    //----------------------------------------------
    public String crearAsistenciaGraduada(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secAsistencia = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASISTENCIA_GRADUADA));
            AsistenciaGraduada asistencia = getConversor().crearAsistenciaDesdeSecuencia(secAsistencia);

            //Valida que exista el estudiante
            if (asistencia.getEstudiante() == null || asistencia.getEstudiante().getPersona() == null || asistencia.getEstudiante().getPersona().getCorreo() == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ASISTENCIA_ERR_0001, new LinkedList<Secuencia>());
            }

            //Valida que exista el encargado
            if (asistencia.getEncargado() == null || asistencia.getEncargado().getCorreo() == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ASISTENCIA_ERR_0003, new LinkedList<Secuencia>());
            }

            //Valida que no exista ya una asistencia del estudiante para el periodo dado
            String periodo = asistencia.getPeriodo().getPeriodo();
            String correoEstudiante = asistencia.getEstudiante().getPersona().getCorreo();
            AsistenciaGraduada existe = getAsistenciaGraduadaFacade().findByPeriodoYCorreoEstudiante(periodo, correoEstudiante);
            if (existe != null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ASISTENCIA_ERR_0002, new LinkedList<Secuencia>());
            }

            //Persiste la asistencia
            getAsistenciaGraduadaFacade().create(asistencia);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarAsistenciasGraduadas(String xml) {
        try {
            getParser().leerXML(xml);

            //Consulta todas las asistencias graduadas
            Collection<AsistenciaGraduada> asistencias = getAsistenciaGraduadaFacade().findAll();
            Secuencia secAsistencias = getConversor().crearSecuenciaAsistencias(asistencias);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secAsistencias);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarAsistenciaGraduada(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = secId.getValorLong();

            //Valida que exista la asistencia graduada
            AsistenciaGraduada asistencia = getAsistenciaGraduadaFacade().findById(id);
            if (asistencia == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ASISTENCIA_ERR_0005, new LinkedList<Secuencia>());
            }

            Secuencia secAsistencia = getConversor().crearSecuenciaAsistencia(asistencia);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secAsistencia);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarAsistenciasGraduadasPorEstudiante(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secCorreoEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            String correoEstudiante = secCorreoEstudiante.getValor();

            //Valida que exista un estudiante con el correo dado
            Estudiante estudiante = getEstudianteFacade().findByCorreo(correoEstudiante);
            if (estudiante == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ASISTENCIA_ERR_0001, new LinkedList<Secuencia>());
            }

            //Consulta todas las asistencias graduadas del estudiante
            Collection<AsistenciaGraduada> asistencias = getAsistenciaGraduadaFacade().findByCorreoEstudiante(correoEstudiante);
            Secuencia secAsistencias = getConversor().crearSecuenciaAsistencias(asistencias);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secAsistencias);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarAsistenciasGraduadasPorProfesor(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secCorreoEncargado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            String correo = secCorreoEncargado.getValor();

            //Valida que exista una persona con el correo dado
            Persona persona = getPersonaFacade().findByCorreo(correo);
            if (persona == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ASISTENCIA_ERR_0003, new LinkedList<Secuencia>());
            }

            //Consulta todas las asistencias graduadas de la persona encargada
            Collection<AsistenciaGraduada> asistencias = getAsistenciaGraduadaFacade().findByCorreoProfesor(correo);
            Secuencia secAsistencias = getConversor().crearSecuenciaAsistencias(asistencias);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secAsistencias);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ASISTENCIAS_GRADUADAS_POR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String calificarAsistenciaGraduada(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secAsistencia = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASISTENCIA_GRADUADA));
            AsistenciaGraduada asistencia = getConversor().crearAsistenciaDesdeSecuencia(secAsistencia);

            //Valida que exista la asitencia graduada
            AsistenciaGraduada existe = getAsistenciaGraduadaFacade().findById(asistencia.getId());
            if (existe == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CALIFICAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ASISTENCIA_ERR_0004, new LinkedList<Secuencia>());
            }

            //Actualiza la asistencia
            existe.setNota(asistencia.getNota());
            existe.setObservaciones(asistencia.getObservaciones());
            getAsistenciaGraduadaFacade().edit(existe);

            //Realiza la tarea
            Properties propiedades = new Properties();
            propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), asistencia.getId().toString());
            TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_ASISTENCIA_GRADUADA), propiedades));
            if (borrar != null) {
                tareaSencillaBean.realizarTareaPorId(borrar.getId());
            }

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CALIFICAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CALIFICAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String crearTareaProfesorCalificarAsistenciaGraduada(String xml) {
        try {
            getParser().leerXML(xml);

            //Elimina todas las tareas existentes
            Collection<TareaSencilla> tareas = tareaSencillaFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_ASISTENCIA_GRADUADA));
            for (TareaSencilla tarea : tareas) {
                tareaSencillaBean.realizarTareaPorId(tarea.getId());
            }

            //Crea la tarea a todas las asistencias graduadas que no tienen nota asignada para el periodo actual
            RangoFechasGeneral rango = getRangoFechasGeneralFacade().findByNombre(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_CALIFICAR_ASISTENCIA_GRADUADA));
            crearTareaProfesorCalificarAsistenciaGraduada(rango.getFechaInicial(), rango.getFechaFinal());

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String comportamientoFinRangoCalificarAsistenciaGraduada(String xml) {
        try {
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_FINALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(AsistenciaGraduadaBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_FINALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(AsistenciaGraduadaBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public void crearTareaProfesorCalificarAsistenciaGraduada(Timestamp fI, Timestamp fF) {
        //Consulta las asistencias del periodo actual
        Periodo periodo = getPeriodoFacade().findActual();
        Collection<AsistenciaGraduada> asistencias = getAsistenciaGraduadaFacade().findByPeriodo(periodo.getPeriodo());

        //Crea una tarea por cada asistencia graduada
        for (AsistenciaGraduada asistencia : asistencias) {
            Persona persona = asistencia.getEncargado();
            if (persona != null && asistencia.getNota() == null) {

                //Realiza la tarea en caso de existir
                Properties propiedades = new Properties();
                propiedades.setProperty(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), asistencia.getId().toString());
                TareaSencilla borrar = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_ASISTENCIA_GRADUADA), propiedades));
                if (borrar != null) {
                    tareaSencillaBean.realizarTareaPorId(borrar.getId());
                }

                // Busca si ya existe la tarea
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_ASISTENCIA_GRADUADA);
                Properties params = new Properties();
                params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), asistencia.getId().toString());
                long idTarea = getTareaBean().consultarIdTareaPorParametrosTipo(tipo, params);

                if (idTarea == -1) {
                    //Crea la tarea al profesor para calificar la asistencia
                    String categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR);
                    String correoResponsable = asistencia.getEncargado().getCorreo();
                    String nombreCompletoAsistente = asistencia.getEstudiante().getPersona().getNombres() + " " + asistencia.getEstudiante().getPersona().getApellidos();
                    HashMap<String, String> paramsNew = new HashMap<String, String>();
                    paramsNew.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), asistencia.getId().toString());
                    String comando = getConstanteBean().getConstante(Constantes.CMD_CALIFICAR_ASISTENCIA_GRADUADA);
                    String asunto = Notificaciones.ASUNTO_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR;
                    String header = String.format(Notificaciones.HEADER_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR, correoResponsable);
                    String footer = Notificaciones.FOOTER_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR;
                    String mensaje = String.format(Notificaciones.MENSAJE_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR, nombreCompletoAsistente);
                    getTareaBean().crearTareaPersona(mensaje, tipo, asistencia.getEncargado().getCorreo(), true, header, footer, fI, fF, comando, categoriaResponsable, paramsNew, asunto);

                    //Envía un correo informando que hay una asistencia graduada por calificar
                    String mensajeCompleto = String.format(Notificaciones.MENSAJE_COMPLETO_CALIFICAR_ASISTENCIA_GRADUADA_PROFESOR, correoResponsable, nombreCompletoAsistente);
                    getCorreoBean().enviarMail(correoResponsable, asunto, null, null, null, mensajeCompleto);
                }
            }
        }
    }

    public String regenerarTareasProfesorCalificarAsistenciaGraduada(String xml) {
        try {
            if (xml != null) {
                getParser().leerXML(xml);
            }

            //Elimina todas las tareas existentes
            Collection<TareaSencilla> tareas = tareaSencillaFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CALIFICAR_ASISTENCIA_GRADUADA));
            for (TareaSencilla tarea : tareas) {
                tareaSencillaBean.realizarTareaPorId(tarea.getId());
            }

            //Regenera la tarea a todas las asistencias graduadas que no tienen nota asignada para el periodo actual
            RangoFechasGeneral rango = getRangoFechasGeneralFacade().findByNombre(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_CALIFICAR_ASISTENCIA_GRADUADA));
            if (rango != null) {
                crearTareaProfesorCalificarAsistenciaGraduada(rango.getFechaInicial(), rango.getFechaFinal());

                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());
            } else {
                //enviar correo a sisinfo informando que las tareas no fueron creadas por que el rango XX no existe en la BD
                String msj  = "Error al crear Tareas de Calificar Asistencias Graduadas por que el rango == null Nombre Rango =RANGO_FECHAS_GENERAL_CALIFICAR_ASISTENCIA_GRADUADA ";
                correoBean.enviarMail("sisinfo@uniandes.edu.co", "Error al crear Tareas de Calificar Asistencias Graduadas", null, null, null, msj);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            }

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarTiposAsistenciasGraduadas(String xml) {
        try {
            getParser().leerXML(xml);

            //Consulta todas las asistencias graduadas
            Collection<TipoAsistenciaGraduada> tipos = getTipoAsistenciaGraduadaFacade().findAll();
            Secuencia secTipos = getConversor().crearSecuenciaTiposAsistencias(tipos);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secTipos);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_ASISTENCIAS_GRADUADAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private CorreoLocal getCorreoBean() {
        return correoBean;
    }

    private PeriodoFacadeLocal getPeriodoFacade() {
        return periodoFacade;
    }

    private PersonaFacadeLocal getPersonaFacade() {
        return personaFacade;
    }

    private AsistenciaGraduadaFacadeLocal getAsistenciaGraduadaFacade() {
        return asistenciaGraduadaFacade;
    }

    private TipoAsistenciaGraduadaFacadeLocal getTipoAsistenciaGraduadaFacade() {
        return tipoAsistenciaGraduadaFacade;
    }

    private EstudianteFacadeLocal getEstudianteFacade() {
        return estudianteFacade;
    }

    private ConversorBolsaEmpleo getConversor() {
        return null;// new ConversorBolsaEmpleo(getConstanteBean(), estudianteFacade, personaFacade, periodoFacade, paisFacade, tipoAsistenciaGraduadaFacade, tipoDocumentoFacade, informacionAcademicaFacade, hojaVidaFacade, estudiantePostgradoFacade, proponenteFacade, ofertaFacade, informacionEmpresaFacade);
    }

    private TareaMultipleLocal getTareaBean() {
        return tareaBean;
    }

    public RangoFechasGeneralFacadeLocal getRangoFechasGeneralFacade() {
        return rangoFechasGeneralFacade;
    }
}
