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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicio de negocio: Resolución
 */
@Stateless
@EJB(name = "ResolucionBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ResolucionLocal.class)
public class ResolucionBean implements ResolucionRemote, ResolucionLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * CorreoBean
     */
    @EJB
    private CorreoRemote correoBean;
    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeRemote cursoFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    @EJB
    private MonitoriaRemote monitoriaBean;

    private ServiceLocator serviceLocator;

    private ConversorServiciosSoporteProcesos conversorServiciosSoporteProcesos;
    //---------------------------------------
    // Constructor
    //---------------------------------------

    /**
     * Constructor de ResolucionBean
     */
    public ResolucionBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            monitoriaBean = (MonitoriaRemote) serviceLocator.getRemoteEJB(MonitoriaRemote.class);
            conversorServiciosSoporteProcesos = new ConversorServiciosSoporteProcesos();
        } catch (NamingException ex) {
            Logger.getLogger(ResolucionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String hacerResolucion(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secCurso = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
            String codigoCurso = secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor();
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            //Verificar si las secciones del curso tienen vacantes
            Collection<Seccion> secciones = darSeccionesConVacantesPorCodigoCurso(codigoCurso);
            Secuencia cursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), "");
            if (secciones.size() == 0) {
                //No hay secciones con vacantes
                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), codigoCurso);
                sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CODIGO_CURSO));
                secuencias.add(sec);
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_RESOLUCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0085, secuencias);
            }
            Curso c = getCursoFacade().findByCodigo(codigoCurso);
            Secuencia secuCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
            secuCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), codigoCurso));
            secuCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre()));
            secuCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRESENCIAL), Boolean.toString(c.isPresencial())));
            Secuencia secuSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
            Iterator<Seccion> itSecciones = secciones.iterator();
            ArrayList<String> seccionesEnviarCorreo = new ArrayList<String>();
            while (itSecciones.hasNext()) {
                //Datos Seccion
                Seccion seccion = itSecciones.next();
                Secuencia s = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
                s.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), seccion.getProfesorPrincipal().getPersona().getNombres()));
                s.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(seccion.getNumeroSeccion())));
                s.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
                Collection<Sesion> sesiones = seccion.getHorarios();

                // Agrega un conjunto de sesiones con dia completo
                Secuencia secHorario = conversorServiciosSoporteProcesos.pasarSesionesASecuencia(sesiones);
                s.agregarSecuencia(secHorario);
                
                Secuencia solicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), "");
                Collection<Solicitud> aspirantesSeccion = darSolicitudesAspirantesSeccion(seccion);
                //No tiene aspirantes
                if (aspirantesSeccion.isEmpty()) {
                    //Mira en solicitudes a otros cursos según criterios
                    int maxNumeroSolicitudesPorResolucion = Integer.parseInt(getConstanteBean().getConstante(Constantes.MAX_NUMERO_SOLICITUDES_POR_RESOLUCION));
                    aspirantesSeccion = buscarOtrosAspirantes(codigoCurso, maxNumeroSolicitudesPorResolucion);
                    if (aspirantesSeccion.isEmpty()) {
                        //Aún así no hay aspirantes - Se añade la sección a la
                        //lista de secciones para enviar correos.
                        seccionesEnviarCorreo.add(seccion.getCrn());
                    }
                    // Tengo aspirantes, verifico que llenen la sección.
                    // Si no la llenan, no hay nada que hacer, manda correos.
                } else {
                    //Tiene aspirantes: Verifica que tenga el mínimo de aspirantes
                    //para llenar la sección
                    seccion.getMaximoMonitores();
                }
                for (Solicitud soli : aspirantesSeccion) {
                    Secuencia secSol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), "");
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(soli.getId())));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA), soli.getFechaCreacion().toString()));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO), ""));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), soli.getEstudiante().getPersona().getCodigo()));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), soli.getEstudiante().getPersona().getNombres()));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), soli.getEstudiante().getPersona().getApellidos()));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), ""));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_MATERIA), Double.toString(soli.getMonitoria_solicitada().getNota_materia())));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), Double.toString(soli.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioTotal())));
                    secSol.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), Double.toString(soli.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosSemestreActual())));
                    solicitudes.agregarSecuencia(secSol);
                }
                s.agregarSecuencia(solicitudes);
                //Se agrega la seccion a la secuencia de secciones
                secuSecciones.agregarSecuencia(s);
            }
            //Se envía el correo con las secciones
            if (!seccionesEnviarCorreo.isEmpty()) {
                String seccionesCorreo = "";
                for (String se : seccionesEnviarCorreo) {
                    seccionesCorreo = seccionesCorreo.concat(se);
                    seccionesCorreo = seccionesCorreo.concat(",");
                }
                String mensaje = Notificaciones.MENSAJE_RESOLUCION_CURSO;
                String[] split = mensaje.split("%");
                mensaje = split[0];
                mensaje = mensaje.concat(c.getNombre());
                mensaje = mensaje.concat(split[1]);
                mensaje = mensaje.concat(seccionesCorreo);
                mensaje = mensaje.concat(split[2]);
                getCorreoBean().enviarMail(Notificaciones.CORREO_ESTUDIANTES_PREG_SISTEMAS + Notificaciones.DOMINIO_CUENTA_UNIANDES, Notificaciones.ASUNTO_RESOLUCION_CURSO + c.getNombre(), null, null, null, mensaje);
            } else {
                secuCurso.agregarSecuencia(secuSecciones);
                cursos.agregarSecuencia(secuCurso);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_RESOLUCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0063, new Vector());
            }
            secuCurso.agregarSecuencia(secuSecciones);
            cursos.agregarSecuencia(secuCurso);
            secuencias.add(cursos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_RESOLUCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, secuencias);
        } catch (Exception e) {
            try {
                Collection<Secuencia> secuencias = new Vector<Secuencia>();
                e.printStackTrace();
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_RESOLUCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, secuencias);
            } catch (Exception ex) {
                Logger.getLogger(ResolucionBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }


    /**
     * Retorna una colección con las solicitudes aspirantes de una sección dada
     * @param seccion Sección
     * @return Colección de solicitudes
     */
    private Collection<Solicitud> darSolicitudesAspirantesSeccion(Seccion seccion) {
        Collection<Solicitud> aspirantes = new LinkedList<Solicitud>();
        /*Collection<Solicitud> solicitudes = getSolicitudFacade().findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE));
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            Solicitud solicitud = iterator.next();
            Collection<Seccion> secciones = solicitud.getMonitoria_solicitada().getSeccionesEscogidas();
            Iterator<Seccion> iteratorSeccion = secciones.iterator();
            while (iteratorSeccion.hasNext()) {
                Seccion s = iteratorSeccion.next();
                if (seccion.equals(s)) {
                    aspirantes.add(solicitud);
                }
            }
        }*/
        return aspirantes;
    }

    /**
     * Recorre otras materias en el pensum en búsqueda de solicitudes. Es deseable
     * haber preseleccionado las materias posteriores antes de hacer resolución,
     * para así evitar que estudiantes sean preseleccionados en otras materias sin
     * darles la oportunidad en el curso al que se presentaron.
     * @param codigoCurso
     * @return
     */
    private Collection<Solicitud> buscarOtrosAspirantes(String codigoCurso, int solicitudesNecesarias) {
        /*List<Pensum> pensums = getPensumFacade().findAll();
        Vector<Solicitud> solicitudes = new Vector<Solicitud>();
        if (pensums.isEmpty()) {
            return solicitudes;
        }
        //Busca el pensum más reciente
        Pensum actual = pensums.get(0);
        for (Pensum p : pensums) {
            if (p.getFechaCreacion().getTime() > actual.getFechaCreacion().getTime()) {
                actual = p;
            }
        }
        //Obtiene las materias de las que esta es prerrequisito
        Collection<Relacion> relaciones = getPensumFacade().findRelacionesByIdAndCodigoCurso(actual.getId(), codigoCurso);
        if (relaciones.isEmpty()) {
            //llegué al final del árbol
            return solicitudes;
        }
        //Por cada materia que está más adelante en el pensum, busco solicitudes.
        int solicitudesHastaAhora = 0;
        solicitudes.addAll(recorrerNivel(relaciones));
        solicitudesHastaAhora += solicitudes.size();
        if (solicitudesHastaAhora >= solicitudesNecesarias) {
            return solicitudes;
        }
        //No se han encontrado todas las solicitudes en el nivel; recorro cada rama
        for (Relacion r : relaciones) {
            Materia m = r.getAMateria();
            for (Curso c : m.getCursosValidos()) {
                Collection<Solicitud> sols =
                        buscarOtrosAspirantes(c.getCodigo(),
                        solicitudesNecesarias - solicitudesHastaAhora);
                if (sols == null) {
                    sols = new Vector<Solicitud>();
                }
                solicitudes.addAll(sols);
                solicitudesHastaAhora += sols.size();
                if (solicitudesHastaAhora >= solicitudesNecesarias) {
                    break;
                }
            }
            if (solicitudesHastaAhora >= solicitudesNecesarias) {
                break;
            }
        }
        return solicitudes;*/return null;
    }

    /**
     * Retorna todas las solicitudes que pertenecen a un nivel de acuerdo a una colección de relaciones dada
     * @param relaciones Colección de relaciones
     * @return Colección de solicitudes
     */
    /*private Collection<Solicitud> recorrerNivel(Collection<Relacion> relaciones) {
        Collection<Solicitud> solicitudes = new Vector<Solicitud>();
        for (Relacion r : relaciones) {
            Materia m = r.getAMateria();
            Collection<Curso> cursos = m.getCursosValidos();
            for (Curso c : cursos) {
                Collection<Seccion> secciones = c.getSecciones();
                for (Seccion seccion : secciones) {
                    Collection<Solicitud> solicitudesSeccion = darSolicitudesAspirantesSeccion(seccion);
                    if (c.isPresencial()) {
                        for (Solicitud s : solicitudesSeccion) {
                            // Verifico horario
                            if (getConsultaBean().verificarConflicto(s.getEstudiante().getPersona().getCorreo(), seccion.getCrn()) == false) {
                                solicitudes.add(s);
                            }
                        }
                    } else {
                        solicitudes.addAll(solicitudesSeccion);
                    }
                }
            }
        }
        return solicitudes;
    }*/

    /**
     * Retorna CorreoBean
     * @return correoBean CorreoBean
     */
    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    /**
     * Retorna CursoFacade
     * @return cursoFacade CursoFacade
     */
    private CursoFacadeRemote getCursoFacade() {
        return cursoFacade;
    }

    /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private Collection<Seccion> darSeccionesConVacantesPorCodigoCurso(String codigoCurso) {
        Collection<Seccion> seccionesConVacante = new ArrayList<Seccion>();
        Curso curso = getCursoFacade().findByCodigo(codigoCurso);
        if (curso != null) {
            Collection<Seccion> secciones = curso.getSecciones();
            Iterator<Seccion> iteradorSecciones = secciones.iterator();
            while (iteradorSecciones.hasNext()) {
                Seccion seccion = iteradorSecciones.next();
                boolean vacantes = monitoriaBean.hayVacantes(seccion.getCrn(), seccion.getMaximoMonitores());
                System.out.println(vacantes);
                if (vacantes) {
                    seccionesConVacante.add(seccion);
                }
            }
        }
        return seccionesConVacante;
    }
}
