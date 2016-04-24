package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.RangoFechasGeneral;
import co.uniandes.sisinfo.nucleo.services.NucleoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.RangoFechasGeneralFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicios de administración de rangos de fechas generales
 * @author Marcela Morales
 */
@Stateless
public class RangoFechasGeneralBean implements RangoFechasGeneralLocal {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.RangoFechasGeneralLocal";
    private final static String NOMBRE_METODO_TIMER = "manejoTimersRangoFechasGeneral";
    //---------------------------------------
    // Atributos
    //---------------------------------------
    // Key: Nombre del rango
    // Value: Comando que se llama en la fecha de inicio/fin del rango respectivo
    private HashMap<String, String> hashRangoYComandosInicio;
    private HashMap<String, String> hashRangoYComandosFin;
    //Servicios Locales
    @EJB
    RangoFechasGeneralFacadeLocal rangoFechasGeneralFacade;
    //Servicios Remotos
    //Servicios Útiles
    @EJB
    private NucleoLocal nucleoBean;
    @EJB
    private TimerGenericoBeanLocal timerGenerico;
    @EJB
    private ConstanteLocal constanteBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de RangoFechasGeneralBean
     */
    public RangoFechasGeneralBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            timerGenerico = (TimerGenericoBeanLocal) serviceLocator.getLocalEJB(TimerGenericoBeanLocal.class);
//            nucleoBean = (NucleoLocal) serviceLocator.getLocalEJB(NucleoLocal.class);
//
//            inicializarTiposRangoFechasGeneral();
//        } catch (NamingException ex) {
//            Logger.getLogger(RangoFechasGeneralBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * Método que inicializa la información de los rangos y comandos respectivos
     */
    private void inicializarTiposRangoFechasGeneral() {
        //Configuración de rangos y comandos de inicio
        hashRangoYComandosInicio = new HashMap<String, String>();
        hashRangoYComandosInicio.put(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_PROGRAMA), getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_PROGRAMA_PLANEACION_ACADEMICA));
        hashRangoYComandosInicio.put(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_CIERRE), getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_ARCHIVO_CIERRE_PLANEACION_ACADEMICA));
        hashRangoYComandosInicio.put(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_TREINTA_POR_CIENTO), getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_NOTAS_TREINTAP_PLANEACION_ACADEMICA));
        hashRangoYComandosInicio.put(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_CALIFICAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.CMD_INICIALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS));

        //Configuración de rangos y comandos de fin
        hashRangoYComandosFin = new HashMap<String, String>();
        hashRangoYComandosFin.put(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_PROGRAMA), getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FINRANGOFECHAS_SUBIR_PROGRAMA));
        hashRangoYComandosFin.put(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_CIERRE), getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_CIERRE_CURSOS));
        hashRangoYComandosFin.put(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_TREINTA_POR_CIENTO), getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_30PORCIENTO));
        hashRangoYComandosFin.put(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_CALIFICAR_ASISTENCIA_GRADUADA), getConstanteBean().getConstante(Constantes.CMD_FINALIZAR_RANGO_FECHAS_CALIFICAR_ASISTENCIAS_GRADUADAS));
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public String crearRangoFechasGeneral(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secRango = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO));
            RangoFechasGeneral rango = crearRangoFechasGeneralDesdeSecuencia(secRango);

            //Crea y asigna los timers
            Long timerInicio = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, rango.getFechaInicial(),
                    getHashRangoYComandosInicio().get(rango.getNombre()), "ServiciosSoporteProcesos", "RangoFechasGeneralBean", "crearRangoFechasGeneral", "Timer de inicio para el rango de fechas generales " + rango.getNombre());
            Long timerFin = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, rango.getFechaFinal(),
                    getHashRangoYComandosFin().get(rango.getNombre()), "ServiciosSoporteProcesos", "RangoFechasGeneralBean", "crearRangoFechasGeneral", "Timer de fin para el rango de fechas generales " + rango.getNombre());
            rango.setIdTimerInicial(timerInicio);
            rango.setIdTimerFinal(timerFin);

            //Valida que no exista un rango con el mismo nombre
            RangoFechasGeneral rangoExiste = getRangoFechasGeneralFacade().findByNombre(rango.getNombre());
            if (rangoExiste != null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RANGO_ERR_0001, new LinkedList<Secuencia>());
            }

            //Persiste el rango
            getRangoFechasGeneralFacade().create(rango);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String editarRangoFechasGeneral(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secRango = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO));
            RangoFechasGeneral rango = crearRangoFechasGeneralDesdeSecuencia(secRango);
            RangoFechasGeneral rangoAActualizar = getRangoFechasGeneralFacade().findById(rango.getId());

            //Valida que exista el rango
            if (rangoAActualizar == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RANGO_ERR_0002, new LinkedList<Secuencia>());
            }

            //Actualiza la información 
            getTimerGenerico().eliminarTimer(rangoAActualizar.getIdTimerInicial());
            getTimerGenerico().eliminarTimer(rangoAActualizar.getIdTimerFinal());

            rangoAActualizar = rango;
            Long timerInicio = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, rango.getFechaInicial(),
                    getHashRangoYComandosInicio().get(rango.getNombre()), "ServiciosSoporteProcesos", "RangoFechasGeneralBean", "crearRangoFechasGeneral", "Timer de inicio para el rango de fechas generales " + rango.getNombre());
            Long timerFin = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, rango.getFechaFinal(),
                    getHashRangoYComandosFin().get(rango.getNombre()), "ServiciosSoporteProcesos", "RangoFechasGeneralBean", "crearRangoFechasGeneral", "Timer de fin para el rango de fechas generales " + rango.getNombre());
            rangoAActualizar.setIdTimerInicial(timerInicio);
            rangoAActualizar.setIdTimerFinal(timerFin);

            getRangoFechasGeneralFacade().edit(rangoAActualizar);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String editarRangosFechasGeneral(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secRangos = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGOS));

            //Elimina todos los rangos existentes, junto con los timers
            Collection<RangoFechasGeneral> rangosAELiminar = getRangoFechasGeneralFacade().findAll();
            for (RangoFechasGeneral rangoAEliminar : rangosAELiminar) {
                getTimerGenerico().eliminarTimer(rangoAEliminar.getIdTimerInicial());
                getTimerGenerico().eliminarTimer(rangoAEliminar.getIdTimerFinal());
                getRangoFechasGeneralFacade().remove(rangoAEliminar);
            }

            //Crea los rangos que vienen como parámetro
            Collection<RangoFechasGeneral> rangos = crearRangosFechasGeneralDesdeSecuencia(secRangos);
            for (RangoFechasGeneral rango : rangos) {
                Long timerInicio = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, rango.getFechaInicial(),
                        getHashRangoYComandosInicio().get(rango.getNombre()), "ServiciosSoporteProcesos", "RangoFechasGeneralBean", "crearRangoFechasGeneral", "Timer de inicio para el rango de fechas generales " + rango.getNombre());
                Long timerFin = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, rango.getFechaFinal(),
                        getHashRangoYComandosFin().get(rango.getNombre()), "ServiciosSoporteProcesos", "RangoFechasGeneralBean", "crearRangoFechasGeneral", "Timer de fin para el rango de fechas generales " + rango.getNombre());
                rango.setIdTimerInicial(timerInicio);
                rango.setIdTimerFinal(timerFin);

                getRangoFechasGeneralFacade().create(rango);

            }
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_RANGOS_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_RANGOS_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarRangoFechasGeneralPorNombre(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secRango = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO));
            RangoFechasGeneral rango = crearRangoFechasGeneralDesdeSecuencia(secRango);
            RangoFechasGeneral rangoAConsultar = getRangoFechasGeneralFacade().findByNombre(rango.getNombre());

            //Valida que exista el rango
            if (rangoAConsultar == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_FECHAS_GENERALES_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RANGO_ERR_0003, new LinkedList<Secuencia>());
            }

            //Consulta la información
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secResultado = crearSecuenciaRangoFechaGeneral(rangoAConsultar);
            secuencias.add(secResultado);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_FECHAS_GENERALES_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_FECHAS_GENERALES_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarRangoFechasGeneralPorId(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secRango = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO));
            RangoFechasGeneral rango = crearRangoFechasGeneralDesdeSecuencia(secRango);
            RangoFechasGeneral rangoAConsultar = getRangoFechasGeneralFacade().findById(rango.getId());

            //Valida que exista el rango
            if (rangoAConsultar == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RANGO_ERR_0002, new LinkedList<Secuencia>());
            }

            //Consulta la información
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secResultado = crearSecuenciaRangoFechaGeneral(rangoAConsultar);
            secuencias.add(secResultado);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarRangosFechasGeneral(String xml) {
        try {
            getParser().leerXML(xml);
            Collection<RangoFechasGeneral> rangos = getRangoFechasGeneralFacade().findAll();

            //Consulta la información
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secResultado = crearSecuenciaRangosFechasGenerales(rangos);
            secuencias.add(secResultado);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGOS_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGOS_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String eliminarRangoFechasGeneral(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secRango = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO));
            RangoFechasGeneral rango = crearRangoFechasGeneralDesdeSecuencia(secRango);
            RangoFechasGeneral rangoAEliminar = getRangoFechasGeneralFacade().findById(rango.getId());

            //Valida que exista el rango
            if (rangoAEliminar == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RANGO_ERR_0002, new LinkedList<Secuencia>());
            }

            //Elimina los timers y el rango
            getTimerGenerico().eliminarTimer(rangoAEliminar.getIdTimerInicial());
            getTimerGenerico().eliminarTimer(rangoAEliminar.getIdTimerFinal());
            getRangoFechasGeneralFacade().remove(rangoAEliminar);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_RANGO_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String eliminarRangosFechasGeneral(String xml) {
        try {
            getParser().leerXML(xml);

            //Elimina los rangos
            Collection<RangoFechasGeneral> rangos = getRangoFechasGeneralFacade().findAll();
            for (RangoFechasGeneral rango : rangos) {
                getTimerGenerico().eliminarTimer(rango.getIdTimerInicial());
                getTimerGenerico().eliminarTimer(rango.getIdTimerFinal());
                getRangoFechasGeneralFacade().remove(rango);
            }

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_RANGOS_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_RANGOS_FECHAS_GENERALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarTiposRangoFechasGeneral(String xml) {
        try {
            getParser().leerXML(xml);

            //Consulta la información
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secResultado = crearSecuenciaRangosYComandos();
            secuencias.add(secResultado);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGOS_Y_COMANDOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGOS_Y_COMANDOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public void manejoTimersRangoFechasGeneral(String nombreComando) {
        System.out.println("MANEJO TIMERS RANGO FECHAS GENERAL: "+nombreComando);
        try {
            String comando = getParser().crearComando(nombreComando, getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA),getConstanteBean().getConstante(Constantes.ROL_ADMINISTRADOR_SISINFO), new ArrayList<Secuencia>());
            nucleoBean.resolverComando(comando);

        } catch (Exception ex) {
            Logger.getLogger(RangoFechasGeneralBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private RangoFechasGeneralFacadeLocal getRangoFechasGeneralFacade() {
        return rangoFechasGeneralFacade;
    }

    private HashMap<String, String> getHashRangoYComandosFin() {
        return hashRangoYComandosFin;
    }

    private HashMap<String, String> getHashRangoYComandosInicio() {
        return hashRangoYComandosInicio;
    }

    private TimerGenericoBeanLocal getTimerGenerico() {
        return timerGenerico;
    }

    //---------------------------------------
    // Métodos para el conversor
    //---------------------------------------
    /**
     * Crea una secuencia dado un conjunto de rangos de fechas generales
     * @param rangos Colección de rangos de fechas generales
     * @return Secuencia construída a partir de la colección de rangos de fechas generales dada
     */
    public Secuencia crearSecuenciaRangosFechasGenerales(Collection<RangoFechasGeneral> rangos) {
        Secuencia secRangos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGOS), "");
        for (RangoFechasGeneral rango : rangos) {
            Secuencia secRango = crearSecuenciaRangoFechaGeneral(rango);
            secRangos.agregarSecuencia(secRango);
        }
        return secRangos;
    }

    /**
     * Crea una secuencia dado un rango de fechas generales
     * @param rango Rango de fechas generales
     * @return Secuencia construída a partir del rango de fechas generales dado
     */
    public Secuencia crearSecuenciaRangoFechaGeneral(RangoFechasGeneral rango) {
        if (rango == null) {
            return null;
        }
        Secuencia secRango = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO), "");
        if (rango.getId() != null) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), rango.getId().toString()));
        }
        if (rango.getNombre() != null) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), rango.getNombre()));
        }
        if (rango.getFechaInicial() != null) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), rango.getFechaInicial().toString()));
        }
        if (rango.getFechaFinal() != null) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), rango.getFechaFinal().toString()));
        }
        if (rango.getIdTimerInicial() != null) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TIMER_INICIO), rango.getIdTimerInicial().toString()));
        }
        if (rango.getIdTimerFinal() != null) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TIMER_FIN), rango.getIdTimerFinal().toString()));
        }
        if (getHashRangoYComandosInicio().containsKey(rango.getNombre())) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_INICIO), hashRangoYComandosInicio.get(rango.getNombre())));
        }
        if (getHashRangoYComandosFin().containsKey(rango.getNombre())) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_FIN), hashRangoYComandosFin.get(rango.getNombre())));
        }
        return secRango;
    }

    /**
     * Crea un conjunto de rangos de fechas generales a partir de una secuencia
     * @param secRangos Secuencia
     * @return Conjunto de rangos de fechas generales construído a partir de la secuencia dada
     */
    public Collection<RangoFechasGeneral> crearRangosFechasGeneralDesdeSecuencia(Secuencia secRangos) {
        if (secRangos == null) {
            return null;
        }
        Collection<RangoFechasGeneral> rangos = new ArrayList<RangoFechasGeneral>();
        for (Secuencia secRango : secRangos.getSecuencias()) {
            RangoFechasGeneral rango = crearRangoFechasGeneralDesdeSecuencia(secRango);
            rangos.add(rango);
        }
        return rangos;
    }

    /**
     * Crea un rango de fechas generales a partir de una secuencia
     * @param secRango Secuencia
     * @return Rango de fechas generales construído a partir de la secuencia dada
     */
    public RangoFechasGeneral crearRangoFechasGeneralDesdeSecuencia(Secuencia secRango) {
        if (secRango == null) {
            return null;
        }
        RangoFechasGeneral rango = new RangoFechasGeneral();
        Secuencia nombre = secRango.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (nombre != null) {
            rango.setNombre(nombre.getValor());
        }
        Secuencia fechaInicio = secRango.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
        if (fechaInicio != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = sdf.parse(fechaInicio.getValor());
                rango.setFechaInicial(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(RangoFechasGeneralBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Secuencia fechaFin = secRango.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
        if (fechaFin != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = sdf.parse(fechaFin.getValor());
                rango.setFechaFinal(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(RangoFechasGeneralBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Secuencia idTimerInicio = secRango.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TIMER_INICIO));
        if (idTimerInicio != null) {
            rango.setIdTimerInicial(idTimerInicio.getValorLong());
        }
        Secuencia idTimerFin = secRango.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TIMER_FIN));
        if (idTimerFin != null) {
            rango.setIdTimerFinal(idTimerFin.getValorLong());
        }
        return rango;
    }

    /**
     * Crea una secuencia con los tipos de rango de fechas generales
     * @return Secuencia construída a partir de los tipos de rango existentes en los HashMap
     */
    public Secuencia crearSecuenciaRangosYComandos() {
        Secuencia secRangos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGOS), "");
        for (String nombre : getHashRangoYComandosInicio().keySet()) {
            Secuencia secRango = crearSecuenciaRangoYComandos(nombre);
            secRangos.agregarSecuencia(secRango);
        }
        return secRangos;
    }

    /**
     * Crea una secuencia dado el nombre de un rango de fechas generales
     * @param nombre Nombre del rango de fechas generales
     * @return Secuencia construída a partir del nombre del rango de fechas generales
     */
    public Secuencia crearSecuenciaRangoYComandos(String nombre) {
        if (nombre == null) {
            return null;
        }

        Secuencia secRango = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RANGO), "");
        secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), nombre));
        String comandoInicio = getHashRangoYComandosInicio().get(nombre);
        if (comandoInicio != null) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_INICIO), comandoInicio));
        }
        String comandoFin = getHashRangoYComandosFin().get(nombre);
        if (comandoFin != null) {
            secRango.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_FIN), comandoFin));
        }
        return secRango;
    }
}
