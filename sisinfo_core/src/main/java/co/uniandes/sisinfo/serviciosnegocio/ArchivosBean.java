/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Archivo;
import co.uniandes.sisinfo.entities.Periodicidad;
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.RangoFechasGeneral;
import co.uniandes.sisinfo.entities.TareaSencilla;

import co.uniandes.sisinfo.entities.TipoArchivo;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;

import co.uniandes.sisinfo.serviciosfuncionales.ArchivoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.RangoFechasGeneralFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TipoArchivoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Administración de archivos
 */
@Stateless
@EJB(name = "ArchivosBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ArchivosLocal.class)
public class ArchivosBean implements ArchivosRemote, ArchivosLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    private final static String DIRECCION_INTERFAZ = "co.uniandes.sisinfo.serviciosnegocio.ArchivosRemote";
    private final static String NOMBRE_METODO = "manejoTimersArchivos";
    /**
     * Parser
     */
    private ParserT parser;
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
    /**
     * ArchivoFacade
     */
    @EJB
    private ArchivoFacadeLocal archivoFacade;
    @EJB
    private TipoArchivoFacadeLocal tipoArchivoFacade;
    @EJB
    private CursoFacadeRemote cursoFacade;
    @EJB
    private CorreoRemote correoBean;
    /**
     * PeriodoFacade
     */
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    private ServiceLocator serviceLocator;
    private String[] tipos;
    /**
     * Histórico
     */
    private HistoricoRemote historicoBean;
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;

    /*
     * cambio de tareas y alertas
     */
    @EJB
    private TareaSencillaRemote tareaSencillaBean;
    @EJB
    private TareaMultipleRemote tareaMultipleBean;
    @EJB
    private TareaMultipleRemote tareaBean;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;
    @EJB
    private RangoFechasGeneralFacadeRemote rangoFechasFacade;
    @EJB
    private TimerGenericoBeanRemote timerGenericoBean;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ArchivosBean
     */
    public ArchivosBean() {
        try {
            serviceLocator = new ServiceLocator();

            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);

            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            String[] tip = {getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA),
                getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TREINTA_POR_CIENTO), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CIERRE)};
            tipos = tip;
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            historicoBean = (HistoricoRemote) serviceLocator.getRemoteEJB(HistoricoRemote.class);
            timerGenericoBean = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);


            rangoFechasFacade = (RangoFechasGeneralFacadeRemote) serviceLocator.getRemoteEJB(RangoFechasGeneralFacadeRemote.class);

            tareaSencillaBean = (TareaSencillaRemote) serviceLocator.getRemoteEJB(TareaSencillaRemote.class);
            tareaMultipleBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            tareaBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);

            parser = new ParserT();
        } catch (NamingException ex) {
            Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String confirmarSubidaArchivo(String root) {
        try {

            getParser().leerXML(root);

            String rutaDirectorio = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CAMPO_RUTA_DIRECTORIO)).getValor();
            String crnSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)).getValor();
            String tipo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO)).getValor();
            String periodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();
            String mime = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MIME)).getValor();


            Seccion seccion = seccionFacade.findByCRN(crnSeccion);

            // Busca si existe una tarea del mismo tipo y correo
            // que contenga la misma sección entre sus parámetros

            HashMap<String, String> paramsNew = crearParametrosTareaSubirArchivo(crnSeccion, periodo, tipo);
            Properties propsTarea = pasarHashAProp(paramsNew);
            TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, propsTarea));
            //tipoNombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_TIPO_STAFF)).getValor();

            if (seccion == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0089, new LinkedList<Secuencia>());
            } else {
                ArchivoFacadeLocal facade = getArchivoFacade();
                String crn = seccion.getCrn();
                //Si el documento ya existe reemplaza los datos
                Archivo archivo = archivoFacade.findBySeccionAndTipo(crn, tipo);
                TipoArchivo tipoArchivo = tipoArchivoFacade.findByTipo(tipo);
                if (archivo != null) {

                    archivo.setRuta(rutaDirectorio);
                    archivo.setTipoArchivo(tipoArchivo);
                    archivo.setSeccion(seccion);
                    archivo.setTipoMime(mime);
                    facade.edit(archivo);
                } //Si el documento no existe lo crea
                else {
                    Archivo archivoSubir = new Archivo();
                    archivoSubir.setTipoArchivo(tipoArchivo);
                    archivoSubir.setTipoMime(mime);
                    archivoSubir.setSeccion(seccion);
                    archivoSubir.setRuta(rutaDirectorio);

                    facade.create(archivoSubir);
                }

                if (tarea != null) {

                    tareaSencillaBean.realizarTareaPorId(tarea.getId());
                }
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0108, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0088, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

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
     * Retorna SeccionFacade
     * @return seccionFacade SeccionFacade
     */
    private SeccionFacadeRemote getSeccionFacade() {
        return seccionFacade;
    }

    /**
     * Retorna ArchivoFacade
     * @return archivoFacade ArchivoFacade
     */
    private ArchivoFacadeLocal getArchivoFacade() {
        return archivoFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    @Override
    public String darArchivosProfesor(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Vector<Secuencia> secuencias = new Vector<Secuencia>();
            List<Seccion> secciones = seccionFacade.findByCorreoProfesor(correo);

            Curso curso = new Curso();
            curso.setCodigo("");
            Secuencia secCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), "");
            Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");

            Secuencia secSecciones = null;
            String[] cursosNoRequeridos = new String[3];
            cursosNoRequeridos[0] = getConstanteBean().getConstante(Constantes.CONFIGURACION_PARAM_CURSOS_SIN_PROGRAMA);
            cursosNoRequeridos[1] = getConstanteBean().getConstante(Constantes.CONFIGURACION_PARAM_CURSOS_SIN_TREINTA_POR_CIENTO);
            cursosNoRequeridos[2] = getConstanteBean().getConstante(Constantes.CONFIGURACION_PARAM_CURSOS_SIN_CIERRE);
            for (Seccion s : secciones) {
                Curso c = cursoFacade.findByCRNSeccion(s.getCrn());

                boolean encontrado = false;
                for (Iterator<Secuencia> it = secCursos.getSecuencias().iterator(); it.hasNext();) {
                    Secuencia secTemp = it.next();
                    if (secTemp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO)).getValor().equals(c.getNombre())) {
                        secSecciones = secTemp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    curso = c;
                    secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), c.getCodigo()));
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre()));
                    secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
                    secCurso.agregarSecuencia(secSecciones);
                    secCursos.agregarSecuencia(secCurso);
                }
                Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
                secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), s.getCrn()));
                secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + s.getNumeroSeccion()));
                Secuencia secArchivos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVOS), "");
                Secuencia secArchivo;
                for (int i = 0; i < tipos.length; i++) {
                    Archivo arch = archivoFacade.findBySeccionAndTipo(s.getCrn(), tipos[i]);
                    boolean requerido = !cursosNoRequeridos[i].contains(c.getCodigo());
                    secArchivo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVO), "");
                    if (arch != null) {
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUBIDO), "true"));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO), arch.getId().longValue() + ""));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO), "Archivo Descarga - " + tipos[i]));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO), arch.getTipoArchivo().getTipo()));
                    } else {
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO), ""));
                        if (requerido) {
                            secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO), "No disponible"));
                        } else {
                            secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO), "No requerido"));
                        }
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUBIDO), "false"));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO), tipos[i]));

                    }
                    secArchivos.agregarSecuencia(secArchivo);
                }

                secSeccion.agregarSecuencia(secArchivos);
                secSecciones.agregarSecuencia(secSeccion);
            }
            secuencias.add(secCursos);
            String respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_ARCHIVOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new Vector());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darArchivosProfesorPorPeriodo(String comando) {
        try {
            getParser().leerXML(comando);
            String periodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();

            //verifica si se trata del período actual
            Periodo periodoConsulta = periodoFacade.findByPeriodo(periodo);
            List<Seccion> secciones = new ArrayList<Seccion>();
            if (periodoConsulta != null && periodoConsulta.isActual()) {
                //si se trata del período actual, se traen todas
                secciones = seccionFacade.findAll();
            } else {
                //si no es el período actual se consulta el bean para consultar el histórico por período
                return historicoBean.darArchivosProfesorPorPeriodoEnHistorico(comando);
            }

            Secuencia secCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), "");
            Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");

            Secuencia secSecciones = null;

            for (Seccion s : secciones) {
                Curso c = cursoFacade.findByCRNSeccion(s.getCrn());

                boolean encontrado = false;
                for (Iterator<Secuencia> it = secCursos.getSecuencias().iterator(); it.hasNext();) {
                    Secuencia secTemp = it.next();

                    if (secTemp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO)).getValor().equals(c.getNombre())) {
                        secSecciones = secTemp.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), c.getCodigo()));
                    secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre()));
                    secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
                    secCurso.agregarSecuencia(secSecciones);
                    secCursos.agregarSecuencia(secCurso);
                }

                Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
                secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), s.getCrn()));
                secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + s.getNumeroSeccion()));
                Secuencia secArchivos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVOS), "");
                Secuencia secArchivo;

                for (int i = 0; i < tipos.length; i++) {
                    Archivo arch = archivoFacade.findBySeccionAndTipo(s.getCrn(), tipos[i]);
                    secArchivo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVO), "");
                    if (arch != null) {
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUBIDO), "true"));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO), arch.getId().longValue() + ""));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO), "Archivo Descarga - " + tipos[i]));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO), arch.getTipoArchivo().getTipo()));
                    } else {
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO), ""));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO), "No disponible"));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUBIDO), "false"));
                        secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO), tipos[i]));

                    }
                    secArchivos.agregarSecuencia(secArchivo);
                }
                secSeccion.agregarSecuencia(secArchivos);

                //agrega profesor a la secuencia
                Profesor profesorSeccion = s.getProfesorPrincipal();
                if (profesorSeccion != null) {
                    Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
                    secProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), profesorSeccion.getPersona().getNombres()));
                    secProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), profesorSeccion.getPersona().getApellidos()));
                    secProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), profesorSeccion.getPersona().getCorreo()));

                    secSeccion.agregarSecuencia(secProfesor);
                }

                secSecciones.agregarSecuencia(secSeccion);

            }

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCursos);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_ARCHIVOS_PROFESOR_POR_PERIODO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darInfoArchivo(String comando) {
        try {
            getParser().leerXML(comando);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO)).getValor();
            Vector<Secuencia> secuencias = new Vector<Secuencia>();

            Archivo a = archivoFacade.find(new Long(id));

            if (a != null) {
                Secuencia secArchivo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVO), "");
                secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MIME), a.getTipoMime()));
                secArchivo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_DIRECTORIO), a.getRuta()));
                secuencias.add(secArchivo);
            } else {
                return historicoBean.darInfoArchivoHistorico(comando);
            }

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darRutaDirectorioArchivo(String comando) {
        try {
            getParser().leerXML(comando);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO)).getValor();
            Vector<Secuencia> secuencias = new Vector<Secuencia>();
            Archivo a = archivoFacade.findById(new Long(id));

            if (a != null) {
                String ruta = a.getRuta();

                if (ruta.lastIndexOf("/") != -1) { //si la ruta esta separadas por /
                    secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_DIRECTORIO), ruta.substring(0, ruta.lastIndexOf("/") + 1)));
                } else { // si la ruta esta separada con \
                    secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_DIRECTORIO), ruta.substring(0, ruta.lastIndexOf("\\") + 1)));
                }


            }
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_OBTENER_RUTA_DIRECTORIO_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String confirmarReemplazoArchivo(String comando) {
        try {
            getParser().leerXML(comando);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ARCHIVO)).getValor();
            String rutaArchivo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_DIRECTORIO)).getValor();
            String tipoMime = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MIME)).getValor();
            Vector<Secuencia> secuencias = new Vector<Secuencia>();

            Archivo a = archivoFacade.findById(new Long(id));

            if (a != null) {
                a.setRuta(rutaArchivo);
                a.setTipoMime(tipoMime);
                archivoFacade.edit(a);
            }
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_REEMPLAZO_ARCHIVO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void generarTareasPrograma(Timestamp fDFin) {
        List<Seccion> secciones = seccionFacade.findAll();
        Iterator<Seccion> iterator = secciones.iterator();
        while (iterator.hasNext()) {
            Seccion seccion = iterator.next();
            Profesor profesor = seccion.getProfesorPrincipal();
            if (profesor != null) {
                Curso curso = cursoFacade.findByCRNSeccion(seccion.getCrn());

                crearTareaCursoPrograma(curso, fDFin, seccion);
            }
        }
    }

    public void generarTareasTreintaPorCiento(Date dFin) {
        List<Seccion> secciones = seccionFacade.findAll();
        Iterator<Seccion> iterator = secciones.iterator();
        while (iterator.hasNext()) {
            Seccion seccion = iterator.next();
            Profesor profesor = seccion.getProfesorPrincipal();
            if (profesor != null) {
                Curso curso = cursoFacade.findByCRNSeccion(seccion.getCrn());
                crearTareaCurso30Porciento(curso, dFin, seccion);
            }
        }

    }

    public void generarTareasCierre(Date dFin) {

        List<Seccion> secciones = seccionFacade.findAll();
        Iterator<Seccion> iterator = secciones.iterator();
        while (iterator.hasNext()) {
            Seccion seccion = iterator.next();
            Profesor profesor = seccion.getProfesorPrincipal();
            if (profesor != null) {
                Curso curso = cursoFacade.findByCRNSeccion(seccion.getCrn());
                crearTareaCursoCierre(curso, dFin, seccion);
            }
        }
    }

    private Properties pasarHashAProp(HashMap<String, String> paramsNew) {
        Collection<String> llaves = paramsNew.keySet();
        Properties p = new Properties();
        for (String llave : llaves) {
            p.put(llave, paramsNew.get(llave));
        }
        return p;
    }

    public void manejoTimersArchivos(String info) {
        if (info.startsWith(constanteBean.getConstante(Constantes.VAL_NOTIFICAR_DOCUMENTOS_PENDIENTES))) {
            notificarDocumentosPendientes();
        }
    }

    public void notificarDocumentosPendientes() {
        Collection<Seccion> secciones = seccionFacade.findAll();
        String contenidoMensaje = "";
        HashMap<String, Boolean> mapaSeccionesConPrograma = new HashMap();
        boolean incomplete = false;
        for (Seccion seccion : secciones) {
            Archivo archivoPrograma = archivoFacade.findBySeccionAndTipo(seccion.getCrn(), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA));
            if (archivoPrograma != null) {
                mapaSeccionesConPrograma.put(seccion.getCrn(), Boolean.TRUE);
            } else {
                incomplete = true;
            }
        }
        if (!incomplete) {
            return;
        }
        String codigosSinPrograma = constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_CURSOS_SIN_PROGRAMA);
        for (Seccion seccion : secciones) {
            if (mapaSeccionesConPrograma.get(seccion.getCrn()) != null) {
                continue;
            }
            Curso curso = cursoFacade.findByCRNSeccion(seccion.getCrn());
            if (codigosSinPrograma.contains(curso.getCodigo())) {
                continue;
            }
            Profesor profesor = seccion.getProfesorPrincipal();
            String nombresProfesor = "Por definir";
            if (profesor != null) {
                nombresProfesor = profesor.getPersona().getApellidos() + " " + profesor.getPersona().getNombres();
            }

            contenidoMensaje += String.format(Notificaciones.CONTENIDO_MENSAJE_DOCUMENTOS_FALTANTES_PLANEACION_ACADEMICA,
                    curso.getNombre(), seccion.getNumeroSeccion(), nombresProfesor, getConstanteBean().getConstante(Constantes.VAL_PLANEACION_ACADEMICA_TIPO_ARCHIVO_PROGRAMA));

        }
        correoBean.enviarMail(getConstanteBean().getConstante(Constantes.VAG_TAG_ROL_COORDINACION),
                Notificaciones.ASUNTO_DOCUMENTOS_FALTANTES_PLANEACION_ACADEMICA,
                null, null, null,
                String.format(Notificaciones.MENSAJE_DOCUMENTOS_FALTANTES_PLANEACION_ACADEMICA, contenidoMensaje));

        Periodicidad p = periodicidadFacade.findByNombre(constanteBean.getConstante(Constantes.VAL_PERIODICIDAD_NOTIFICACION_DOCUMENTOS_PENDIENTES));
        Timestamp tsFin = new Timestamp(new Date().getTime() + p.getValor());
        timerGenericoBean.crearTimer2(DIRECCION_INTERFAZ, NOMBRE_METODO,
                tsFin, constanteBean.getConstante(Constantes.VAL_NOTIFICAR_DOCUMENTOS_PENDIENTES),
                "PlaneacionAcademica",
                "ArchivosBean",
                "notificarDocumentosPendientes",
                "Este timer notifica a la coordinación sobre los documentos que se encuentran pendientes por subir. El timer se refresca automaticamente hasta que se cierre el periodo o se suban todos los documentos pendientes");
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

    private boolean esCursoSinProgramaCierreOTreintaPorCiento(String codigoCurso, String constante) {
        String cursosSin = getConstanteBean().getConstante(constante);
        if (cursosSin.equals("N/A")) {
            return false;
        }

        String[] cursos = cursosSin.split("_");
        for (String curso : cursos) {
            if (curso.equals(codigoCurso)) {
                return true;
            }
        }
        return false;
    }

    private boolean archivoSubido(String tipo, String crnSeccion) {
        Archivo archivo = archivoFacade.findBySeccionAndTipo(crnSeccion, tipo);
        if (archivo != null) {
            return true;
        }
        return false;
    }

    private void crearTareaCursoPrograma(Curso curso, Date fDFin, Seccion seccion) {

        boolean sinPrograma = esCursoSinProgramaCierreOTreintaPorCiento(curso.getCodigo(), Constantes.CONFIGURACION_PARAM_CURSOS_SIN_PROGRAMA);
        boolean archivoSubido = archivoSubido(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA), seccion.getCrn());

        if (!sinPrograma && !archivoSubido) {
            Profesor profesor = seccion.getProfesorPrincipal();
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA);
            Periodo periodo = periodoFacade.findActual();
            // Busca si existe una tarea del mismo tipo y correo
            // que contenga la misma sección entre sus parámetros
            HashMap<String, String> paramsNew = crearParametrosTareaSubirArchivo(seccion.getCrn(), periodo.getPeriodo(), tipo);
            Properties propsTarea = pasarHashAProp(paramsNew);
            TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, propsTarea));
            if (tarea != null) {
                completarTareaSencilla(tipo, propsTarea);
            }
            /*     String nombre = "Subir Programa para la sección " + seccion.getNumeroSeccion() + " del curso " + curso.getNombre();
            String descripcion = "Se debe subir el archivo del programa del curso " + curso.getNombre();*/
            //Date fechaCaducacion = null;
            SimpleDateFormat otroFormato = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
            String rol = "";
            if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))) {
                rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
            } else {
                rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA);
            }
            //-----------------inicio cambio tarea-------------------------------------
            Date fechaInicioDate = new Date();
            String mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_CURSO_SECCION, curso.getNombre(), String.valueOf(seccion.getNumeroSeccion()));
            String comando = getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_ARCHIVO);

            String header = String.format(Notificaciones.MENSAJE_HEADER_SUBIR_PROGRAMAS_CURSOS, profesor.getPersona().getNombres() + " " + profesor.getPersona().getApellidos());
            String footer = String.format(Notificaciones.MENSAJE_FOOTER_SUBIR_ARCHIVOS_CURSOS, otroFormato.format(fDFin));
            Timestamp fFin = new Timestamp(fDFin.getTime());
            //-------------------------------------------------------------------------
            String asunto = Notificaciones.ASUNTO_SUBIR_PROGRAMAS_CURSOS;
            boolean agrupable = true;
            tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getPersona().getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, paramsNew, asunto);
        }
    }

    private void crearTareaCurso30Porciento(Curso curso, Date fDFin, Seccion seccion) {

        boolean sinTreinta = esCursoSinProgramaCierreOTreintaPorCiento(curso.getCodigo(), Constantes.CONFIGURACION_PARAM_CURSOS_SIN_TREINTA_POR_CIENTO);
        boolean archivoSubido = archivoSubido(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TREINTA_POR_CIENTO), seccion.getCrn());

        if (!sinTreinta && !archivoSubido) {
            Profesor profesor = seccion.getProfesorPrincipal();
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TREINTA_POR_CIENTO);
            Periodo periodo = periodoFacade.findActual();
            // Busca si existe una tarea del mismo tipo y correo
            // que contenga la misma sección entre sus parámetros
            HashMap<String, String> paramsNew = crearParametrosTareaSubirArchivo(seccion.getCrn(), periodo.getPeriodo(), tipo);
            Properties propsTarea = pasarHashAProp(paramsNew);
            TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, propsTarea));
            if (tarea != null) {
                completarTareaSencilla(tipo, propsTarea);
            }
            SimpleDateFormat otroFormato = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
            String rol = "";
            if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))) {
                rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
            } else {
                rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA);
            }
            //-----------------inicio cambio tarea-------------------------------------
            Date fechaInicioDate = new Date();
            String mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_CURSO_SECCION, curso.getNombre(), seccion.getNumeroSeccion());
            String comando = getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_ARCHIVO);

            String header = String.format(Notificaciones.MENSAJE_HEADER_SUBIR_TREINTA_POR_CIENTO, profesor.getPersona().getNombres() + " " + profesor.getPersona().getApellidos());
            String footer = String.format(Notificaciones.MENSAJE_FOOTER_SUBIR_ARCHIVOS_CURSOS, otroFormato.format(fDFin));
            Timestamp fFin = new Timestamp(fDFin.getTime());
            //-------------------------------------------------------------------------
            String asunto = Notificaciones.ASUNTO_SUBIR_TREINTA_POR_CIENTO_CURSOS;
            boolean agrupable = true;
            tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getPersona().getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, paramsNew, asunto);
        }
    }

    private void crearTareaCursoCierre(Curso curso, Date fDFin, Seccion seccion) {

        boolean sinCierre = esCursoSinProgramaCierreOTreintaPorCiento(curso.getCodigo(), Constantes.CONFIGURACION_PARAM_CURSOS_SIN_CIERRE);
        boolean archivoSubido = archivoSubido(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CIERRE), seccion.getCrn());

        if (!sinCierre && !archivoSubido) {
            Profesor profesor = seccion.getProfesorPrincipal();
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CIERRE);
            Periodo periodo = periodoFacade.findActual();
            // Busca si existe una tarea del mismo tipo y correo
            // que contenga la misma sección entre sus parámetros
            HashMap<String, String> paramsNew = crearParametrosTareaSubirArchivo(seccion.getCrn(), periodo.getPeriodo(), tipo);
            Properties propsTarea = pasarHashAProp(paramsNew);
            TareaSencilla tarea = tareaSencillaFacade.findById(tareaBean.consultarIdTareaPorParametrosTipo(tipo, propsTarea));
            if (tarea != null) {
                completarTareaSencilla(tipo, propsTarea);
            }
            SimpleDateFormat otroFormato = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));
            String rol = "";
            if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))) {
                rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA);
            } else {
                rol = getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA);
            }
            //-----------------inicio cambio tarea-------------------------------------
            Date fechaInicioDate = new Date();
            String mensajeBulletTarea = String.format(Notificaciones.FORMATOE_BULLET_NOMBRE_CURSO_SECCION, curso.getNombre(), seccion.getNumeroSeccion());
            String comando = getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_ARCHIVO);

            String header = String.format(Notificaciones.MENSAJE_HEADER_SUBIR_CIERRE_CURSOS, profesor.getPersona().getNombres() + " " + profesor.getPersona().getApellidos());

            String footer = String.format(Notificaciones.MENSAJE_FOOTER_SUBIR_ARCHIVOS_CURSOS, otroFormato.format(fDFin));
            Timestamp fFin = new Timestamp(fDFin.getTime());
            //-------------------------------------------------------------------------
            String asunto = Notificaciones.ASUNTO_SUBIR_CIERRE_CURSOS;
            boolean agrupable = true;
            tareaBean.crearTareaPersona(mensajeBulletTarea, tipo, profesor.getPersona().getCorreo(), agrupable, header, footer, new Timestamp(fechaInicioDate.getTime()), fFin, comando, rol, paramsNew, asunto);
        }
    }

    private HashMap<String, String> crearParametrosTareaSubirArchivo(String crnSeccion, String periodo, String tipo) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), crnSeccion);
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), periodo);
        params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO), tipo);
        return params;
    }

    public String comportamientoInicioTareaPrograma(String xml) {
        try {
            parser.leerXML(xml);
            RangoFechasGeneral rf = rangoFechasFacade.findByNombre(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_PROGRAMA));
            if (rf != null && rf.getFechaFinal().after(new Date())) {
                Timestamp fFin = rf.getFechaFinal();
                generarTareasPrograma(fFin);

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_PROGRAMA_PLANEACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                String msj = "";
                if (rf == null) {
                    msj = Notificaciones.MENSAJE_ERROR_INICIANDO_TAREAS_FECHAS_POR_RANGO_INVALIDO;
                    msj = msj.replace("%2", getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_PROGRAMA));
                } else {
                    msj = Notificaciones.MENSAJE_ERROR_INICIANDO_TAREAS_FECHAS_POR_FECHA_FIN_INVALIDA;
                }
                msj = msj.replace("%1", "Subir programa");

                //enviar msje de correo a admon sisinfo, informando error en rango fechas...

                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO),
                        String.format(Notificaciones.ASUNTO_ERROR_CREANDO_TAREAS, "Subir Programa"), null, null, null, msj);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_VOTAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());

            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_PROGRAMA_PLANEACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String comportamientoInicioTarea30Porciento(String xml) {
        try {
            parser.leerXML(xml);
            RangoFechasGeneral rf = rangoFechasFacade.findByNombre(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_TREINTA_POR_CIENTO));
            if (rf != null && rf.getFechaFinal().after(new Date())) {
                Timestamp fFin = rf.getFechaFinal();
                generarTareasTreintaPorCiento(fFin);

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_NOTAS_TREINTAP_PLANEACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                String msj = "";
                if (rf == null) {
                    msj = Notificaciones.MENSAJE_ERROR_INICIANDO_TAREAS_FECHAS_POR_RANGO_INVALIDO;
                    msj = msj.replace("%2", getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_TREINTA_POR_CIENTO));
                } else {
                    msj = Notificaciones.MENSAJE_ERROR_INICIANDO_TAREAS_FECHAS_POR_FECHA_FIN_INVALIDA;
                }
                msj = msj.replace("%1", "Subir notas 30%");

                //enviar msje de correo a admon sisinfo, informando el error en rango fechas
                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO),
                        String.format(Notificaciones.ASUNTO_ERROR_CREANDO_TAREAS, "Subir notas 30"), null, null, null, msj);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_NOTAS_TREINTAP_PLANEACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());

            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_NOTAS_TREINTAP_PLANEACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String comportamientoInicioTareaCierreCursos(String xml) {
        try {
            parser.leerXML(xml);
            RangoFechasGeneral rf = rangoFechasFacade.findByNombre(getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_CIERRE));
            if (rf != null && rf.getFechaFinal().after(new Date())) {
                Timestamp fFin = rf.getFechaFinal();
                generarTareasCierre(fFin);

                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_ARCHIVO_CIERRE_PLANEACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                String msj = "";
                if (rf == null) {
                    msj = Notificaciones.MENSAJE_ERROR_INICIANDO_TAREAS_FECHAS_POR_RANGO_INVALIDO;
                    msj = msj.replace("%2", getConstanteBean().getConstante(Constantes.RANGO_FECHAS_GENERAL_CIERRE));

                } else {
                    msj = Notificaciones.MENSAJE_ERROR_INICIANDO_TAREAS_FECHAS_POR_FECHA_FIN_INVALIDA;
                }
                msj = msj.replace("%1", "Cierre Cursos");
                //enviar msje de correo a admon sisinfo, informando error en rango fechas...
                correoBean.enviarMail(getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO),
                        String.format(Notificaciones.ASUNTO_ERROR_CREANDO_TAREAS, "Cierre Cursos"), null, null, null, msj);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_ARCHIVO_CIERRE_PLANEACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
            }
        } catch (Exception ex) {
            try {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_TAREAS_SUBIR_ARCHIVO_CIERRE_PLANEACION_ACADEMICA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String comportamientoFinRangoFechasSubirPrograma(String xml) {
        try {
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

            // Completa las tareas correspondientes a los profesores
            Collection<TareaSencilla> tareas = tareaSencillaFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA));
            for (TareaSencilla tareaSencilla : tareas) {
                tareaSencillaBean.realizarTareaPorId(tareaSencilla.getId());
            }

            // Crea el timer para notificar los documentos pendientes a coordinacion
            notificarDocumentosPendientes();

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FINRANGOFECHAS_SUBIR_PROGRAMA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FINRANGOFECHAS_SUBIR_PROGRAMA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    public String comportamientoFinRangoFechas30Porciento(String xml) {
        try {
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Collection<TareaSencilla> tareas = tareaSencillaFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TREINTA_POR_CIENTO));
            for (TareaSencilla tareaSencilla : tareas) {
                tareaSencillaBean.realizarTareaPorId(tareaSencilla.getId());
            }
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_30PORCIENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_30PORCIENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String comportamientoFinRangoFechasCierreCursos(String xml) {
        try {
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Collection<TareaSencilla> tareas = tareaSencillaFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CIERRE));
            for (TareaSencilla tareaSencilla : tareas) {
                tareaSencillaBean.realizarTareaPorId(tareaSencilla.getId());
            }
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_CIERRE_CURSOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_COMPORTAMIENTO_FIN_RANGOFECHAS_TAREAS_CIERRE_CURSOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    //@Override
    public void cerrarPeriodo() {
        // Al momento de cerrar periodo se deben dejar todas las tareas pendient
        //es asociadas al programa, 30% y cierre como vencidas
        String[] tipos = new String[]{getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA),
            getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TREINTA_POR_CIENTO),
            getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CIERRE)};
        for (int i = 0; i < tipos.length; i++) {
            Collection<TareaSencilla> tareasSencillas = tareaSencillaFacade.findByTipo(tipos[i]);
            for (TareaSencilla tareaSencilla : tareasSencillas) {
                if (tareaSencilla.getEstado().equals(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE))) {
                    tareaSencilla.setEstado(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_VENCIDA));
                    tareaSencillaFacade.edit(tareaSencilla);
                }
            }

        }

    }

    public String darRutaArchivoPorCRNyTipo(String xml) {
        try {
            getParser().leerXML(xml);
            String crnSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)).getValor();
            String tipoArchivo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO)).getValor();
            String periodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();

            Curso curso = cursoFacade.findByCRNSeccion(crnSeccion);
            Seccion seccion = seccionFacade.findByCRN(crnSeccion);

            String carpetaTipoArchivo;
            System.out.println(this.getClass() + "-->" + tipoArchivo);
            if (tipoArchivo.equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CIERRE))) {
                carpetaTipoArchivo = getConstanteBean().getConstante(Constantes.VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_CIERRE);
            } else if (tipoArchivo.equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA))) {
                carpetaTipoArchivo = getConstanteBean().getConstante(Constantes.VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_PROGRAMA);
            } else if (tipoArchivo.equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TREINTA_POR_CIENTO))) {
                carpetaTipoArchivo = getConstanteBean().getConstante(Constantes.VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_TREINTA_PORCIENTO);
            } else {
                carpetaTipoArchivo = getConstanteBean().getConstante(Constantes.VAL_PLANEACION_ACADEMICA_CARPETA_ARCHIVO_POR_DEFECTO);
            }

            String ruta = carpetaTipoArchivo + File.separator
                    + periodo + File.separator
                    + curso.getCodigo() + File.separator
                    + seccion.getNumeroSeccion() + File.separator;
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();

            Secuencia secRuta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA), ruta);
            secuencias.add(secRuta);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_RUTA_ARCHIVO_POR_CRN_Y_TIPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String darPeriodosConPlaneacionAcademica(String xml) {
        try {
            Collection<String> periodos = historicoBean.darPeriodosPlaneacionAcademicaHistoricos();
            Periodo periodoActual = periodoFacade.findActual();
            periodos.add(periodoActual.getPeriodo());
            ArrayList<Secuencia> secuencias = new ArrayList();
            Secuencia secuenciaPeriodos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS), "");
            for (String periodo : periodos) {
                secuenciaPeriodos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), periodo));
            }
            String comando = getConstanteBean().getConstante(Constantes.CMD_DAR_PERIODOS_PLANEACION_ACADEMICA);
            secuencias.add(secuenciaPeriodos);
            return getParser().generarRespuesta(secuencias, comando, getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE),
                    Mensajes.MSJ_0001, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(ArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
