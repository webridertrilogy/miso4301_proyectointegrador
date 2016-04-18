/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.Convocatoria;
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.ConvocatoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicio de negocio: Administración de convocatoria
 */
@Stateless
@EJB(name = "ConvocatoriaBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ConvocatoriaLocal.class)
public class ConvocatoriaBean implements ConvocatoriaRemote, ConvocatoriaLocal {

    @EJB
    private ConvocatoriaFacadeLocal convocatoriaFacade;
    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * PeriodoFacade
     */
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeRemote cursoFacade;
    /**
     * CorreoBean
     */
    @EJB
    private CorreoRemote correoBean;
    /**
     * SeccionFacade
     */
    @EJB
    private SeccionFacadeRemote seccionFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    
    private ServiceLocator serviceLocator;
   /* @EJB
    private TareaFacadeRemote tareaFacade;
    @EJB
    private TareaRemote tareaBean;
    @EJB
    private AlertaFacadeRemote alertaFacade;*/

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ConvocatoriaBean
     */
    public ConvocatoriaBean() {
        try {
            serviceLocator = new ServiceLocator();
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            //alertaFacade = (AlertaFacadeRemote) serviceLocator.getRemoteEJB(AlertaFacadeRemote.class);
            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
          //  tareaFacade = (TareaFacadeRemote) serviceLocator.getRemoteEJB(TareaFacadeRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
          //  tareaBean = (TareaRemote)serviceLocator.getRemoteEJB(TareaRemote.class);

        } catch (NamingException ex) {
            Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String iniciarPeriodo(String xml) {
        try {
            getParser().leerXML(xml);
            String periodoAcademico = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO)).getValor();
            Periodo periodo = new Periodo();
            periodo.setPeriodo(periodoAcademico);
            periodo.setActual(true);
            List<Periodo> periodos = getPeriodoFacade().findAll();
            for (Periodo p : periodos) {
                p.setActual(false);
                getPeriodoFacade().edit(p);
            }
            getPeriodoFacade().create(periodo);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_INICIAR_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0021, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_INICIAR_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0016, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    @Override
    public String abrirConvocatoria(String xml) {
        try {
            getParser().leerXML(xml);
            Periodo periodo = getPeriodoFacade().findActual();
            
            if (periodo == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ABRIR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0074, new LinkedList<Secuencia>());
            } else {
                Collection<Curso> cursos = getCursoFacade().findAll();
                if (cursos.size() == 0) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ABRIR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0075, new LinkedList<Secuencia>());
                } else {
                    Convocatoria convocatoriaPeriodo=periodo.getConvocatoria();
                    if(convocatoriaPeriodo!=null){
                        if(convocatoriaPeriodo.getEstado().equals(getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA))){
                            Collection<Secuencia> listaParametrosError = new LinkedList<Secuencia>();
                            Secuencia secParam = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), periodo.getPeriodo());
                            listaParametrosError.add(secParam);
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ABRIR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0018, listaParametrosError);
                        }else{
                            Collection<Secuencia> listaParametrosError = new LinkedList<Secuencia>();
                            Secuencia secParam = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), periodo.getPeriodo());
                            listaParametrosError.add(secParam);
                            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ABRIR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0016, listaParametrosError);
                        }
                    }

                    Convocatoria convocatoria = new Convocatoria();
                    convocatoria.setEstado(getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA));
                    convocatoria.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    periodo.setConvocatoria(convocatoria);
                    getPeriodoFacade().edit(periodo);
                    

//                    crearTareaPreseleccionarMonitor();
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ABRIR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0013, new LinkedList<Secuencia>());
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ABRIR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0016, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    @Deprecated
    public boolean abrirConvocatoriaPeriodo(String periodoAcademico) {
//        System.out.println("ConvocatoriaBean::abrirConvocatarioPeriodo(String) Started...::Param:" + periodoAcademico);
//        try {
//            Periodo periodo = getPeriodoFacade().findByPeriodo(periodoAcademico);
//            if (periodo != null) {
//                Collection<Curso> cursos = getCursoFacade().findAll();
//                if (cursos.size() != 0) {
//                    Iterator<Curso> iterador = cursos.iterator();
//                    while (iterador.hasNext()) {
//                        Curso curso = iterador.next();
//                        Collection<Seccion> secciones = curso.getSecciones();
//                        Iterator<Seccion> iteradorS = secciones.iterator();
//                        while (iteradorS.hasNext()) {
//                            Seccion seccion = iteradorS.next();
//                            Profesor profesor = seccion.getProfesorPrincipal();
//                            if (profesor != null && seccion.getMaximoMonitores() > 0) {
//                                //al profesor y los estudiantes
//                                //getCorreoBean().enviarMail(null, null, null, null, null, null);
//                            }
//                        }
//                    }
//                    //getCorreoBean().enviarMail(Notificaciones.CORREO_PROFESORES_SISTEMAS + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_APERTURA_CONVOCATORIA, null, null, null, Notificaciones.MENSAJE_APERTURA_CONVOCATORIA);
//                    //getCorreoBean().enviarMail(Notificaciones.CORREO_PROFESORES_SISTEMAS + Notificaciones.DOMINIO_CUENTA_UNIANDES + ", " + Notificaciones.CORREO_ESTUDIANTES_PREG_SISTEMAS + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_APERTURA_CONVOCATORIA, null, null, null, Notificaciones.MENSAJE_APERTURA_CONVOCATORIA);
//                    Convocatoria convocatoria = new Convocatoria();
//                    convocatoria.setEstado(getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA));
//                    periodo.setConvocatoria(convocatoria);
//                    periodo.setActual(true);
//                    List<Periodo> periodos = getPeriodoFacade().findAll();
//                    for (Periodo p : periodos) {
//                        p.setActual(false);
//                        getPeriodoFacade().edit(p);
//                    }
//                    getPeriodoFacade().edit(periodo);
//                    return true;
//                }
//            }
//            return false;
//        } catch (Exception e) {
//            return false;
//        }
        return false;
    }

    @Override
    public String cerrarConvocatoria(String xml) {
        try {
            getParser().leerXML(xml);
            String periodoAcademico = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO)).getValor();
            Periodo periodo = getPeriodoFacade().findByPeriodo(periodoAcademico);
            if (periodo == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CERRAR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0076, new LinkedList<Secuencia>());
            } else {
                Convocatoria convocatoria = periodo.getConvocatoria();
                convocatoria.setEstado(getConstanteBean().getConstante(Constantes.ESTADO_CERRADA));
                periodo.setConvocatoria(convocatoria);
                getPeriodoFacade().edit(periodo);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CERRAR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0014, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CERRAR_CONVOCATORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0018, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public void cerrarConvocatoriaActual() {
        Periodo periodo = getPeriodoFacade().findActual();
        Convocatoria convocatoria = periodo.getConvocatoria();
        convocatoria.setEstado(getConstanteBean().getConstante(Constantes.ESTADO_CERRADA));
        convocatoriaFacade.edit(convocatoria);
    }


    @Override
    public String modificarDatosSecciones(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secciones = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
            Collection<Secuencia> secs = secciones.getSecuencias();
            for (Secuencia s : secs) {
                int max = Integer.parseInt(s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES)).getValor());
                String crn = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)).getValor();
                Seccion seccion = getSeccionFacade().findByCRN(crn);
                if (seccion != null) {
                    seccion.setMaximoMonitores(max);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return null;
    }

    @Override
    public boolean hayConvocatoriaAbierta() {
        boolean hay = false;
        List<Convocatoria> convocatorias = convocatoriaFacade.findAll();

        for (Iterator<Convocatoria> it = convocatorias.iterator(); it.hasNext() && !hay;) {
            Convocatoria c = it.next();
            if (c.getEstado().equals(getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA))) {
                hay = true;
            }
        }
        return hay;
    }

    public String determinarSiHayConvocatoriaAbierta(String comando) {
        try {
            boolean abierta = hayConvocatoriaAbierta();
            LinkedList<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secAbierta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ABIERTA), "" + abierta);
            secuencias.add(secAbierta);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DETERMINAR_SI_HAY_CONVOCATORIA_ABIERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DETERMINAR_SI_HAY_CONVOCATORIA_ABIERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Retorna el Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna PeriodoFacade
     * @return periodoFacade PeriodoFacade
     */
    private PeriodoFacadeRemote getPeriodoFacade() {
        return periodoFacade;
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
     * Retorna SeccionFacade
     * @return seccionFacade SeccionFacade
     */
    private SeccionFacadeRemote getSeccionFacade() {
        return seccionFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ConvocatoriaFacadeLocal getConvocatoriaFacade() {
        return convocatoriaFacade;
    }

    /*public TareaRemote getTareaBean() {
        return tareaBean;
    }

    public TareaFacadeRemote getTareaFacade() {
        return tareaFacade;
    }*/

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

 /*   private void crearTareaPreseleccionarMonitor() {
        List<Seccion> secciones = getSeccionFacade().findAll();
        Iterator<Seccion> iterator = secciones.iterator();
        while (iterator.hasNext()) {
            Seccion seccion = iterator.next();
            double cantidadMonitores = Math.floor(seccion.getMaximoMonitores());
            //Validamos que la sección en cuestión deba registrar monitores
            if (cantidadMonitores >= 1) {
                Profesor profesor = seccion.getProfesorPrincipal();
                if (profesor != null) {
                    String correo = profesor.getPersona().getCorreo();
                    String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PRESELECCION_MONITOR);

                    // Busca si existe una tarea del mismo tipo y correo
                    // que contenga la misma sección entre sus parámetros
                    List<Tarea> tareas = getTareaFacade().findByCorreoYTipo(correo, tipo);
                    Iterator<Tarea> it = tareas.iterator();
                    boolean encontro = false;
                    while (it.hasNext() && !encontro) {
                        Tarea tarea = it.next();
                        String crn = consultarValorParametro(tarea.getParametros(), getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION));
                        if (seccion.getCrn().equals(crn)) {
                            encontro = true;
                        }
                    }
                    if (!encontro) {
                        Curso curso = cursoFacade.findByCRNSeccion(seccion.getCrn());
                        String nombre = "Preseleccionar " + cantidadMonitores + " monitor(es) para la sección" + seccion.getNumeroSeccion() + " del curso " + curso.getNombre();
                        String descripcion = "Al preseleccionar un monitor usted manifiesta su voluntad que el estudiante seleccionado sea monitor de la sección en cuestión. \n"
                                + "Al preseleccionar a un estudiante se le notificará y se le solitará que acepte o rechace la preselección. Si el estudiante acepta, se procederá a revisar los datos suministrados y a crear el convenio. \n"
                                + "En caso de rechazo se le notificará a usted y se le habilitará de nuevo una tarea para preseleccionar otro monitor.";
                        Alerta alerta = alertaFacade.findByTipoTarea(tipo);

                        Date fechaCaducacion = null;
                        if (alerta.getTipoIntervalo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_INTERVALO_INTERVALO))) {
                            fechaCaducacion = new Date(alerta.getDuracionTarea() + System.currentTimeMillis());
                        } else {
                            fechaCaducacion = new Date(alerta.getFechaFin().getTime());
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String nombresResponsable = profesor.getPersona().getNombres();
                        String categoriaResponsable = "";
                        if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))) {
                            categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
                        } else {
                            categoriaResponsable = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA);
                        }
                        HashMap<String,String> parametros = new HashMap<String,String>();
                        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn());
                        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORES), "" + cantidadMonitores);
                        parametros.put(getConstanteBean().getConstante(Constantes.VAL_PARAMETRO_CRN_SECCION), seccion.getCrn());
                        parametros.put(getConstanteBean().getConstante(Constantes.VAL_PARAMETRO_NUMERO_SECCION), Integer.toString(seccion.getNumeroSeccion()));
                        parametros.put(getConstanteBean().getConstante(Constantes.VAL_PARAMETRO_NOMBRE_CURSO), curso.getNombre());
                        parametros.put(getConstanteBean().getConstante(Constantes.VAL_PARAMETRO_CODIGO_CURSO), curso.getCodigo());
                        parametros.put(getConstanteBean().getConstante(Constantes.VAL_PARAMETRO_NOMBRE_PROFESOR), profesor.getPersona().getNombres() + " " + profesor.getPersona().getApellidos());
                        parametros.put(getConstanteBean().getConstante(Constantes.VAL_PARAMETRO_CANTIDAD_MONITORES), Double.toString(cantidadMonitores));
                        String comando = getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION);
                        String asunto = "Preseleccionar monitor para la sección (CRN: " + seccion.getCrn() + ")";
                        String mensaje = "Se debe preseleccionar un monitor para la sección con CRN " + seccion.getCrn();
                        int periodicidad = 1;
                        getTareaBean().crearTarea(nombre, descripcion, sdf.format(fechaCaducacion), nombresResponsable, categoriaResponsable, correo, parametros, tipo, comando, asunto, mensaje, periodicidad);
                    }
                }
            }
        }
    }*/

}
