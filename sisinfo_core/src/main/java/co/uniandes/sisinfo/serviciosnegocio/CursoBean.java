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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Facultad;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.FacultadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicio de negocio: Administración de cursos
 */
@Stateless
@EJB(name = "CursoBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.CursoLocal.class)
public class CursoBean implements  CursoLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser 
     */
    private ParserT parser;
    /**
     * CursoFacade 
     */
    @EJB
    private CursoFacadeLocal cursoFacade;
    /**
     * SeccionFacade 
     */
    @EJB
    private SeccionFacadeLocal seccionFacade;
    /**
     * FacultadFacade 
     */
    @EJB
    private FacultadFacadeLocal facultadFacade;
    /**
     * ProfesorFacade
     */
    @EJB
    private ProfesorFacadeLocal profesorFacade;
    /**
     * ProgramaFacade
     */
    @EJB
    private ProgramaFacadeLocal programaFacade;
    /**
     * Monitoria bean
     */
    @EJB
    private MonitoriaLocal monitoriaBean;
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private ConvocatoriaLocal convocatoriaBean;
    private ConversorServiciosSoporteProcesos conversorServiciosSoporteProcesos;
    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de CursoBean
     */
    public CursoBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            profesorFacade = (ProfesorFacadeLocal) serviceLocator.getLocalEJB(ProfesorFacadeLocal.class);
//            seccionFacade = (SeccionFacadeLocal) serviceLocator.getLocalEJB(SeccionFacadeLocal.class);
//            programaFacade = (ProgramaFacadeLocal) serviceLocator.getLocalEJB(ProgramaFacadeLocal.class);
//            facultadFacade = (FacultadFacadeLocal) serviceLocator.getLocalEJB(FacultadFacadeLocal.class);
//            cursoFacade = (CursoFacadeLocal) serviceLocator.getLocalEJB(CursoFacadeLocal.class);
//            monitoriaBean = (MonitoriaLocal) serviceLocator.getLocalEJB(MonitoriaLocal.class);
//            conversorServiciosSoporteProcesos = new ConversorServiciosSoporteProcesos();
//        } catch (NamingException ex) {
//            Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String darCursosCupi2(String comando) {
        String retorno = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
        try {
            Collection<Curso> cursos = new ArrayList<Curso>();
            Curso c1 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_APOI));
            Curso c2 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_APOII));
            Curso c3 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_ESTRUCTURAS));
            Curso c4 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_LAB_APOI));
            Curso c5 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_LAB_APOII));
            Curso c6 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_LAB_ESTRUCTURAS));
            cursos.add(c1);
            cursos.add(c2);
            cursos.add(c3);
            cursos.add(c4);
            cursos.add(c5);
            cursos.add(c6);
            
            if (cursos.size() == 0) {
                try {
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CUPI2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0061, parametros);
                    return retorno;
                } catch (Exception ex1) {
                    Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            secuencias = getSecuenciaCursos(cursos);
            try {
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CUPI2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0060, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CUPI2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                } catch (Exception ex1) {
                    Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CUPI2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0075, parametros);
                return retorno;
            } catch (Exception ex1) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return retorno;
    }

    @Override
    public String darCursos(String xml) {
        String retorno = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
        try {
            Collection<Curso> cursos = getCursoFacade().findAll();
            if (cursos.size() == 0) {
                try {
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0061, parametros);
                    return retorno;
                } catch (Exception ex1) {
                    Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            secuencias = getSecuenciaCursos(cursos);
            try {
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0060, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                } catch (Exception ex1) {
                    Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0075, parametros);
                return retorno;
            } catch (Exception ex1) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return retorno;
    }

    @Override
    public String darFacultades(String xml) {
        try {
            getParser().leerXML(xml);
            List<Facultad> facultades = getFacultadFacade().findAll();
            if (facultades.size() == 0) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_FACULTADES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0080, new LinkedList<Secuencia>());
            } else {
                Collection<Secuencia> secuencias = getSecuenciasFacultades(facultades);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_FACULTADES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0018, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_FACULTADES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0026, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darProgramasFacultad(String xml) {
        try {
            getParser().leerXML(xml);
            String nombreFacultad = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD)).getValor();
            Facultad facultad = getFacultadFacade().findByNombre(nombreFacultad);
            if (facultad == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_FACULTAD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0081, new LinkedList<Secuencia>());
            } else {
                Collection<Programa> programas = facultad.getProgramas();
                if (programas.size() == 0) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_FACULTAD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0082, new LinkedList<Secuencia>());
                } else {
                    Collection<Secuencia> secuencias = getSecuenciasProgramas(programas);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_FACULTAD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0019, new LinkedList<Secuencia>());
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_FACULTAD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0029, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darProgramas(String xml) {
        try {
            getParser().leerXML(xml);
            Collection<Programa> programas = getProgramaFacade().findAll();
            if (programas.size() == 0) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0090, new LinkedList<Secuencia>());
            } else {
                Collection<Secuencia> secuencias = getSecuenciasProgramas(programas);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0026, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_PROGRAMAS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0043, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darFacultadesConProgramas(String xml) {
        try {
            getParser().leerXML(xml);
            Collection<Facultad> facultades = getFacultadFacade().findAll();
            if (facultades.size() == 0) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_FACULTADES_CON_PROGRAMAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0080, new LinkedList<Secuencia>());
            } else {
                Collection<Secuencia> secuencias = getSecuenciasFacultadesConProgramas(facultades);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_FACULTADES_CON_PROGRAMAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0018, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_FACULTADES_CON_PROGRAMAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0026, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darCursosSemestreActual(String comando) {
        String retorno = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
        ArrayList<Curso> cursos = new ArrayList<Curso>();
        try {
            getParser().leerXML(comando);
            Secuencia secPeriodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO));
            //Consulta si se expecifica un periodo
            if (secPeriodo != null) {

                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_SEMESTRE_ACTUAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0061, parametros);

            } //Consulta por defecto para el periodo actual
            else {
                cursos = (ArrayList<Curso>) getCursoFacade().findAll();
                if (cursos.size() == 0) {
                    try {
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_SEMESTRE_ACTUAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0061, parametros);
                        return retorno;
                    } catch (Exception ex1) {
                        Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
            try {
                //Ordena los cursos por orden alfabetico
                    Collections.sort(cursos, new Comparator<Curso>() {

                        public int compare(Curso o1, Curso o2) {
                            return o1.getNombre().compareTo(o2.getNombre());
                        }
                    });
                secuencias = getSecuenciaCursos(cursos);
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_SEMESTRE_ACTUAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0060, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_SEMESTRE_ACTUAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                } catch (Exception ex1) {
                    Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_SEMESTRE_ACTUAL), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0075, parametros);
                return retorno;
            } catch (Exception ex1) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return retorno;
    }

    @Override
    public String darCursosConVacantes(String root) {
        try {
            getParser().leerXML(root);
            List<Curso> cursos = getCursoFacade().findAll();
            if (cursos.size() == 0) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CON_VACANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0078, new LinkedList<Secuencia>());
            } else {
                List<Curso> cursosVacantes = new LinkedList<Curso>();
                cursosVacantes.addAll(cursos);
                Iterator<Curso> iterador = cursos.iterator();
                while (iterador.hasNext()) {
                    Curso curso = iterador.next();
                    Collection<Seccion> secciones = curso.getSecciones();
                    Iterator<Seccion> iteradorSecciones = secciones.iterator();
                    boolean hayCupos = false;
                    while (iteradorSecciones.hasNext()) {
                        Seccion seccion = iteradorSecciones.next();
                        boolean vacantes = monitoriaBean.hayVacantes(seccion.getCrn(), seccion.getMaximoMonitores());
                        hayCupos = hayCupos || vacantes;
                    }
                    if (!hayCupos) {
                        cursosVacantes.remove(curso);
                    }
                }
                if (cursosVacantes.size() == 0) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CON_VACANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0130, new LinkedList<Secuencia>());
                } else {

                    //Ordena los cursos por orden alfabetico
                    Collections.sort(cursosVacantes, new Comparator<Curso>() {

                        public int compare(Curso o1, Curso o2) {
                            return o1.getNombre().compareTo(o2.getNombre());
                        }
                    });
                    Collection<Secuencia> secuencias = getSecuenciaCursosConVacantes(cursosVacantes);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CON_VACANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0017, new LinkedList<Secuencia>());
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CON_VACANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0024, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darSeccionesConVacantesPorCurso(String root) {
        try {
            getParser().leerXML(root);
            Collection<String> seccionesConVacante = new ArrayList<String>();
            Secuencia secuencia = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO));
            String codigoCurso = secuencia.getValor();
            Curso curso = getCursoFacade().findByCodigo(codigoCurso);
            if (curso == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_CON_VACANTES_POR_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0083, new LinkedList<Secuencia>());
            } else {
                Collection<Seccion> secciones = curso.getSecciones();
                if (secciones.size() == 0) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_CON_VACANTES_POR_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0084, new LinkedList<Secuencia>());
                } else {
                    Iterator<Seccion> iteradorSecciones = secciones.iterator();
                    while (iteradorSecciones.hasNext()) {
                        Seccion seccion = iteradorSecciones.next();
                        boolean vacantes = monitoriaBean.hayVacantes(seccion.getCrn(), seccion.getMaximoMonitores());
                        if (vacantes) {
                            seccionesConVacante.add(seccion.getNumeroSeccion() + "/" + seccion.getProfesorPrincipal().getPersona().getNombres() + " " + seccion.getProfesorPrincipal().getPersona().getApellidos() + "/" + seccion.getCrn() + "/" + curso.isRequerido());
                        }
                    }
                    if (seccionesConVacante.size() == 0) {
                        return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_CON_VACANTES_POR_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0085, new LinkedList<Secuencia>());
                    } else {
                        Collection<Secuencia> secuencias = getSecuenciaSeccionesString(seccionesConVacante);
                        return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_CON_VACANTES_POR_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0020, new LinkedList<Secuencia>());
                    }
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_CON_VACANTES_POR_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0033, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public Collection<Seccion> darSeccionesConVacantesPorCodigoCurso(String codigoCurso) {
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

    @Override
    public String darSeccionesPorLoginProfesor(String xml) {
        try {
            getParser().leerXML(xml);
            String correoProfesor = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Profesor profesor = getProfesorFacade().findByCorreo(correoProfesor);
            if (profesor == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0086, new LinkedList<Secuencia>());
            } else {
                Collection<Seccion> secciones = getSeccionFacade().findByCorreoProfesor(correoProfesor);
                if (secciones.size() == 0) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0087, new LinkedList<Secuencia>());
                }
                Collection<Secuencia> secs = getSecuenciasCurso(secciones);
                return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0021, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0036, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darSeccionesPorLoginProfesorBasico(String xml) {
        try {
            getParser().leerXML(xml);
            String correoProfesor = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Profesor profesor = getProfesorFacade().findByCorreo(correoProfesor);
            if (profesor == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR_BASICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0086, new LinkedList<Secuencia>());
            } else {
                Collection<Seccion> secciones = getSeccionFacade().findByCorreoProfesor(correoProfesor);
                if (secciones.size() == 0) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR_BASICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0087, new LinkedList<Secuencia>());
                }
                Collection<Secuencia> secs = getSecuenciasCursoBasico(secciones);
                return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR_BASICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_POR_LOGIN_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0036, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public double darNumeroMonitoresAsignados(String codigo) {
        Curso curso = getCursoFacade().findByCodigo(codigo);
        Collection<Seccion> secciones = curso.getSecciones();
        double monitoresAsignados = 0;
        for (Seccion seccion : secciones) {
            monitoresAsignados += monitoriaBean.darMonitoresAsignados(seccion.getCrn());
        }
        return monitoresAsignados;
    }

    @Override
    public double darNumeroMaximoMonitores(String codigo) {
        Curso curso = getCursoFacade().findByCodigo(codigo);
        Collection<Seccion> secciones = curso.getSecciones();
        double monitoresAsignados = 0;
        for (Seccion seccion : secciones) {
            monitoresAsignados += seccion.getMaximoMonitores();
        }
        return monitoresAsignados;
    }

    @Override
    public String getCurso(String comando) {
        try {
            getParser().leerXML(comando);
            String codigo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor();
            Curso c = getCursoFacade().findByCodigo(codigo);
            Collection<Secuencia> secuencias = new Vector<Secuencia>();
            if (c != null) {
                Secuencia curso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), c.getCodigo()));
                curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre()));
                curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRESENCIAL), Boolean.toString(c.isPresencial())));
                curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO_REQUERIDO), Boolean.toString(c.isRequerido())));
                curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES), "" + c.getSecciones().iterator().next().getMaximoMonitores()));
                secuencias.add(curso);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0115, new Vector());
            } else {
                Secuencia param = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), codigo);
                Collection<Secuencia> atts = new Vector<Secuencia>();
                atts.add(param);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0083, atts);
            }

        } catch (Exception ex) {
            Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darSeccion(String comando) {
        try {
            getParser().leerXML(comando);
            String crn = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)).getValor();
            Seccion s = getSeccionFacade().findByCRN(crn);
            Collection<Secuencia> secuencias = new Vector<Secuencia>();
            if (s != null) {
                Curso c = getCursoFacade().findByCRNSeccion(crn);
                Secuencia curso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre()));
                curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), c.getCodigo()));
                Secuencia secciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
                Secuencia seccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
                Secuencia profesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
                profesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), s.getProfesorPrincipal().getPersona().getNombres()));
                profesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), s.getProfesorPrincipal().getPersona().getApellidos()));
                seccion.agregarSecuencia(profesor);
                seccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(s.getNumeroSeccion())));
                seccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), crn));
                Collection<Sesion> sesiones = s.getHorarios();
                System.out.println("//////////////////////////////////////////////////////");
                Secuencia horario = conversorServiciosSoporteProcesos.pasarSesionesASecuencia(sesiones);
                seccion.agregarSecuencia(horario);
                seccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES), Double.toString(s.getMaximoMonitores())));
                double cuenta = monitoriaBean.darCargaMonitoriasPorSeccion(crn);

                seccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANTIDAD_ACTUAL_MONITORES), Double.toString(cuenta)));
                secciones.agregarSecuencia(seccion);
                curso.agregarSecuencia(secciones);
                secuencias.add(curso);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0118, new Vector());
            } else {
                Secuencia param = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), crn);
                Collection<Secuencia> atts = new Vector<Secuencia>();
                atts.add(param);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0101, atts);
            }
        } catch (Exception ex) {
            Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darCursosPendientesPorMonitores(java.lang.String comando) {
        try {
            Collection<Secuencia> secuencias = new Vector<Secuencia>();
            Secuencia secCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), "");
            List<Curso> cursos = getCursoFacade().findAll();
            if (cursos.size() == 0) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_PENDIENTES_POR_MONITORES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0078, new LinkedList<Secuencia>());
            } else {
                List<Curso> cursosVacantes = new LinkedList<Curso>();
                cursosVacantes.addAll(cursos);
                Iterator<Curso> iterador = cursos.iterator();
                while (iterador.hasNext()) {
                    Curso curso = iterador.next();
                    Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                    Secuencia secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
                    Collection<Seccion> secciones = curso.getSecciones();
                    Iterator<Seccion> iteradorSecciones = secciones.iterator();
                    while (iteradorSecciones.hasNext()) {
                        Seccion seccion = iteradorSecciones.next();
                        boolean vacantes = monitoriaBean.hayVacantes(seccion.getCrn(), seccion.getMaximoMonitores());
                        if (vacantes == false) {
                            cursosVacantes.remove(curso);
                        } else {
                            Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
                            Secuencia profesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
                            profesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getPersona().getNombres()));
                            secSeccion.agregarSecuencia(profesor);
                            secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(seccion.getNumeroSeccion())));
                            secSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
                            secSecciones.agregarSecuencia(secSeccion);
                        }
                    }
                    if (!secSecciones.getSecuencias().isEmpty()) {
                        secCurso.agregarSecuencia(secSecciones);
                        secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
                        secCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
                        secCursos.agregarSecuencia(secCurso);
                    }
                }
                if (cursosVacantes.size() == 0) {
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_PENDIENTES_POR_MONITORES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0079, new LinkedList<Secuencia>());
                } else {
                    secuencias.add(secCursos);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_PENDIENTES_POR_MONITORES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0017, new LinkedList<Secuencia>());
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_CURSOS_CON_VACANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0024, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Retorna una colección de secuencias dada una colección de cursos
     * @param cursos Colección de secciocursosnes
     * @return Colección de secuencias
     */
    private Collection<Secuencia> getSecuenciaCursosConVacantes(List<Curso> cursos) {
        ArrayList<Secuencia> secuenciasCursos = new ArrayList<Secuencia>();
        Iterator<Curso> iterador = cursos.iterator();
        while (iterador.hasNext()) {
            Curso curso = iterador.next();
            ArrayList<Secuencia> secuenciasCurso = new ArrayList<Secuencia>();
            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRESENCIAL), Boolean.toString(curso.isPresencial())));
            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO_REQUERIDO), Boolean.toString(curso.isRequerido())));
            Secuencia secuenciaCurso = new Secuencia(new ArrayList<Atributo>(), secuenciasCurso);
            secuenciaCurso.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
            secuenciasCursos.add(secuenciaCurso);
        }
        Secuencia secuenciaCursos = new Secuencia(new ArrayList<Atributo>(), secuenciasCursos);
        secuenciaCursos.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS));
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        secuencias.add(secuenciaCursos);
        return secuencias;
    }

    /**
     * Retorna una colección de secuencias dada una colección de cursos
     * @param cursos Colección de cursos
     * @return Colección de secuencias
     */
    private Collection<Secuencia> getSecuenciaCursos(Collection<Curso> cursos) {
        ArrayList<Secuencia> secuenciasCursos = new ArrayList<Secuencia>();
        Iterator<Curso> iterador = cursos.iterator();
        while (iterador.hasNext()) {
            Curso curso = iterador.next();
            // Si alguno de los cursos no existe, no debe ser incluido
            if (curso == null) {
                continue;
            }
            ArrayList<Secuencia> secuenciasCurso = new ArrayList<Secuencia>();
            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRESENCIAL), Boolean.toString(curso.isPresencial())));
            Secuencia secuenciaCurso = new Secuencia(new ArrayList<Atributo>(), secuenciasCurso);
            secuenciaCurso.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
            secuenciasCursos.add(secuenciaCurso);
        }
        Secuencia secuenciaCursos = new Secuencia(new ArrayList<Atributo>(), secuenciasCursos);
        secuenciaCursos.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS));
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        secuencias.add(secuenciaCursos);
        return secuencias;
    }

    /**
     * Retorna una colección de secuencias dada una colección de cursos
     * @param cursos Colección de cursos
     * @return Colección de secuencias
     */
//    private Collection<Secuencia> getSecuenciaCursosConSecciones(Collection<Curso> cursos) {
//        ArrayList<Secuencia> secuenciasCursos = new ArrayList<Secuencia>();
//        Iterator<Curso> iterador = cursos.iterator();
//        while (iterador.hasNext()) {
//            Curso curso = iterador.next();
//            ArrayList<Secuencia> secuenciasCurso = new ArrayList<Secuencia>();
//            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
//            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
//            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRESENCIAL), "" + curso.isPresencial()));
//            Collection<Secuencia> secsSecciones = getSecuenciaSecciones(curso.getSecciones());
//            for (Iterator<Secuencia> it = secsSecciones.iterator(); it.hasNext();) {
//                Secuencia secuencia = it.next();
//                secuenciasCurso.add(secuencia);
//            }
//            Secuencia secuenciaCurso = new Secuencia(new ArrayList<Atributo>(), secuenciasCurso);
//            secuenciaCurso.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
//            secuenciasCursos.add(secuenciaCurso);
//        }
//        Secuencia secuenciaCursos = new Secuencia(new ArrayList<Atributo>(), secuenciasCursos);
//        secuenciaCursos.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS));
//        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
//        secuencias.add(secuenciaCursos);
//        return secuencias;
//    }
    /**
     * Retorna una colección de secuencias dada una colección de cursos diferentes a los actuales
     * @param cursos Colección de cursos
     * @return Colección de secuencias
     */
//    private Collection<Secuencia> getSecuenciaCursosHistoricosConSecciones(Collection<H_Curso> cursos) {
//        ArrayList<Secuencia> secuenciasCursos = new ArrayList<Secuencia>();
//        Iterator<H_Curso> iterador = cursos.iterator();
//        while (iterador.hasNext()) {
//            H_Curso curso = iterador.next();
//            ArrayList<Secuencia> secuenciasCurso = new ArrayList<Secuencia>();
//            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
//            secuenciasCurso.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
//            Collection<Secuencia> secsSecciones = getSecuenciaSeccionesHistoricos(curso.getSecciones());
//            for (Iterator<Secuencia> it = secsSecciones.iterator(); it.hasNext();) {
//                Secuencia secuencia = it.next();
//                secuenciasCurso.add(secuencia);
//            }
//            Secuencia secuenciaCurso = new Secuencia(new ArrayList<Atributo>(), secuenciasCurso);
//            secuenciaCurso.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
//            secuenciasCursos.add(secuenciaCurso);
//        }
//        Secuencia secuenciaCursos = new Secuencia(new ArrayList<Atributo>(), secuenciasCursos);
//        secuenciaCursos.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS));
//        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
//        secuencias.add(secuenciaCursos);
//        return secuencias;
//    }
    /**
     * Retorna una colección de secuencias dada una colección de secciones
     * @param secciones Colección de secciones
     * @return Colección de secuencias
     */
    private Collection<Secuencia> getSecuenciaSeccionesString(Collection<String> secciones) {
        ArrayList<Secuencia> secuenciasSecciones = new ArrayList<Secuencia>();
        Iterator<String> iterador = secciones.iterator();
        while (iterador.hasNext()) {
            String seccion = iterador.next();
            ArrayList<Secuencia> secuenciasSeccion = new ArrayList<Secuencia>();
            ArrayList<Secuencia> secuenciasProfesor = new ArrayList<Secuencia>();
            secuenciasProfesor.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.split("/")[1]));
            Secuencia secuenciaProfesor = new Secuencia(new ArrayList<Atributo>(), secuenciasProfesor);
            secuenciaProfesor.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
            secuenciasSeccion.add(secuenciaProfesor);
            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), seccion.split("/")[0]));
            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.split("/")[2]));
            //secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO_REQUERIDO), seccion.split("/")[3]));
            Secuencia secuenciaSeccion = new Secuencia(new ArrayList<Atributo>(), secuenciasSeccion);
            secuenciaSeccion.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
            secuenciasSecciones.add(secuenciaSeccion);
        }
        Secuencia secuenciaSecciones = new Secuencia(new ArrayList<Atributo>(), secuenciasSecciones);
        secuenciaSecciones.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        secuencias.add(secuenciaSecciones);
        return secuencias;
    }

    /**
     * Retorna una colección de secuencias dada una colección de secciones
     * @param secciones Colección de secciones
     * @return Colección de secuencias
     */
    private Collection<Secuencia> getSecuenciaSecciones(Collection<Seccion> secciones) {
        ArrayList<Secuencia> secuenciasSecciones = new ArrayList<Secuencia>();
        Iterator<Seccion> iterador = secciones.iterator();
        while (iterador.hasNext()) {
            Seccion seccion = iterador.next();
            ArrayList<Secuencia> secuenciasSeccion = new ArrayList<Secuencia>();
            ArrayList<Secuencia> secuenciasProfesor = new ArrayList<Secuencia>();
            secuenciasProfesor.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getPersona().getNombres()));
            Secuencia secuenciaProfesor = new Secuencia(new ArrayList<Atributo>(), secuenciasProfesor);
            secuenciaProfesor.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
            secuenciasSeccion.add(secuenciaProfesor);
            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + seccion.getNumeroSeccion()));
            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES), "" + seccion.getMaximoMonitores()));
            double cuenta = monitoriaBean.darCargaMonitoriasPorSeccion(seccion.getCrn());

            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANTIDAD_ACTUAL_MONITORES), Double.toString(cuenta)));
            Secuencia secuenciaSeccion = new Secuencia(new ArrayList<Atributo>(), secuenciasSeccion);
            secuenciaSeccion.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
            secuenciasSecciones.add(secuenciaSeccion);
        }
        Secuencia secuenciaSecciones = new Secuencia(new ArrayList<Atributo>(), secuenciasSecciones);
        secuenciaSecciones.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        secuencias.add(secuenciaSecciones);
        return secuencias;
    }

    /**
     * Retorna una colección de secuencias dada una colección de secciones de tipo histórico
     * @param secciones Colección de secciones
     * @return Colección de secuencias
     */
//    private Collection<Secuencia> getSecuenciaSeccionesHistoricos(Collection<H_Seccion> secciones) {
//        ArrayList<Secuencia> secuenciasSecciones = new ArrayList<Secuencia>();
//        Iterator<H_Seccion> iterador = secciones.iterator();
//        while (iterador.hasNext()) {
//            H_Seccion seccion = iterador.next();
//            ArrayList<Secuencia> secuenciasSeccion = new ArrayList<Secuencia>();
//            ArrayList<Secuencia> secuenciasProfesor = new ArrayList<Secuencia>();
//            secuenciasProfesor.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getNombres()));
//            Secuencia secuenciaProfesor = new Secuencia(new ArrayList<Atributo>(), secuenciasProfesor);
//            secuenciaProfesor.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
//            secuenciasSeccion.add(secuenciaProfesor);
//            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + seccion.getNumero()));
//            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
//            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES), "" + seccion.getMaximoMonitores()));
//            double cuenta = 0;
//            List<H_Monitoria> monitorias = getHMonitoriaFacade().findByCRNSeccion(seccion.getCrn());
//            for (H_Monitoria m : monitorias) {
//                cuenta += m.getCarga();
//            }
//            secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANTIDAD_ACTUAL_MONITORES), Double.toString(cuenta)));
//            Secuencia secuenciaSeccion = new Secuencia(new ArrayList<Atributo>(), secuenciasSeccion);
//            secuenciaSeccion.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
//            secuenciasSecciones.add(secuenciaSeccion);
//        }
//        Secuencia secuenciaSecciones = new Secuencia(new ArrayList<Atributo>(), secuenciasSecciones);
//        secuenciaSecciones.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
//        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
//        secuencias.add(secuenciaSecciones);
//        return secuencias;
//    }
    /**
     * Retorna una colección de secuencias dada una colección de facultades
     * @param facultades Colección de facultades
     * @return Colección de secuencias
     */
    private Collection<Secuencia> getSecuenciasFacultades(Collection<Facultad> facultades) {
        ArrayList<Secuencia> secuenciasFacultades = new ArrayList<Secuencia>();
        Iterator<Facultad> iterador = facultades.iterator();
        while (iterador.hasNext()) {
            Facultad facultad = iterador.next();
            ArrayList<Atributo> atributosFacultad = new ArrayList<Atributo>();
            atributosFacultad.add(new Atributo(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), facultad.getNombre()));
            Secuencia secuenciaFacultad = new Secuencia(atributosFacultad, new ArrayList<Secuencia>());
            secuenciaFacultad.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD));
            secuenciasFacultades.add(secuenciaFacultad);
        }
        Secuencia secuenciaFacultades = new Secuencia(new ArrayList<Atributo>(), secuenciasFacultades);
        secuenciaFacultades.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTADES));
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        secuencias.add(secuenciaFacultades);
        return secuencias;
    }

    /**
     * Retorna una colección de secuencias dada una colección de facultades
     * @param facultades Colección de facultades
     * @return Colección de secuencias
     */
    private Collection<Secuencia> getSecuenciasFacultadesConProgramas(Collection<Facultad> facultades) {
        ArrayList<Secuencia> secuenciasFacultades = new ArrayList<Secuencia>();
        Iterator<Facultad> iterador = facultades.iterator();
        while (iterador.hasNext()) {
            Facultad facultad = iterador.next();
            ArrayList<Secuencia> secuenciasFacultad = new ArrayList<Secuencia>();
            ArrayList<Atributo> atributosFacultad = new ArrayList<Atributo>();
            atributosFacultad.add(new Atributo(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), facultad.getNombre()));
            Iterator<Programa> it = facultad.getProgramas().iterator();
            while (it.hasNext()) {
                Programa programa = it.next();
                secuenciasFacultad.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), programa.getNombre()));
            }
            Secuencia secuenciaFacultad = new Secuencia(atributosFacultad, secuenciasFacultad);
            secuenciaFacultad.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD));
            secuenciasFacultades.add(secuenciaFacultad);
        }
        Secuencia secuenciaFacultades = new Secuencia(new ArrayList<Atributo>(), secuenciasFacultades);
        secuenciaFacultades.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTADES));
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        secuencias.add(secuenciaFacultades);
        return secuencias;
    }

    /**
     * Retorna una colección de secuencias dada una colección de programas
     * @param programas Colección de programas
     * @return Colección de secuencias
     */
    private Collection<Secuencia> getSecuenciasProgramas(Collection<Programa> programas) {
        ArrayList<Secuencia> secuenciasProgramas = new ArrayList<Secuencia>();
        Iterator<Programa> iterador = programas.iterator();
        while (iterador.hasNext()) {
            Programa programa = iterador.next();
            ArrayList<Atributo> atributosPrograma = new ArrayList<Atributo>();
            atributosPrograma.add(new Atributo(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), programa.getNombre()));
            Secuencia secuenciaPrograma = new Secuencia(atributosPrograma, new ArrayList<Secuencia>());
            secuenciaPrograma.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA));
            secuenciasProgramas.add(secuenciaPrograma);
        }
        Secuencia secuenciaProgramas = new Secuencia(new ArrayList<Atributo>(), secuenciasProgramas);
        secuenciaProgramas.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMAS));
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        secuencias.add(secuenciaProgramas);
        return secuencias;
    }

    /**
     * Retorna una colección de secuencias dada una colección de secciones
     * @param secciones Colección de secciones
     * @return Colección de secuencias con la respuesta determinada
     */
    private Collection<Secuencia> getSecuenciasCurso(Collection<Seccion> secciones) {
        ArrayList<Secuencia> secuenciasCursos = new ArrayList<Secuencia>();
        Iterator<Seccion> iterador = secciones.iterator();
        while (iterador.hasNext()) {
            Secuencia secuenciaCurso = null;
            Seccion seccion = iterador.next();
            Curso c = getCursoFacade().findByCRNSeccion(seccion.getCrn());
            String codigo = c.getCodigo();
            String nombre = c.getNombre();
            for (Secuencia secuencia : secuenciasCursos) {
                Secuencia tmp = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO));
                if (tmp.getValor().equals(codigo)) {
                    secuenciaCurso = secuencia;
                }
            }
            if (secuenciaCurso == null) {
                secuenciaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                Secuencia tmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), nombre);
                secuenciaCurso.agregarSecuencia(tmp);
                tmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), codigo);
                secuenciaCurso.agregarSecuencia(tmp);
                tmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
                secuenciaCurso.agregarSecuencia(tmp);
                secuenciasCursos.add(secuenciaCurso);
            }
            Secuencia secuenciaSecciones = secuenciaCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
            Secuencia secuenciaSeccion = construirSecuenciaSeccion(seccion);
            secuenciaSecciones.agregarSecuencia(secuenciaSeccion);
        }
        Secuencia secuenciaCurso = new Secuencia(new ArrayList<Atributo>(), secuenciasCursos);
        secuenciaCurso.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS));
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        secuencias.add(secuenciaCurso);
        return secuencias;
    }

    private Collection<Secuencia> getSecuenciasCursoBasico(Collection<Seccion> secciones) {
        ArrayList<Secuencia> secuenciasCursos = new ArrayList<Secuencia>();
        Iterator<Seccion> iterador = secciones.iterator();
        while (iterador.hasNext()) {
            Secuencia secuenciaCurso = null;
            Seccion seccion = iterador.next();
            Curso c = getCursoFacade().findByCRNSeccion(seccion.getCrn());
            String codigo = c.getCodigo();
            String nombre = c.getNombre();

            secuenciaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
            Secuencia tmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), nombre);
            secuenciaCurso.agregarSecuencia(tmp);
            tmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), codigo);
            secuenciaCurso.agregarSecuencia(tmp);
            tmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
            secuenciaCurso.agregarSecuencia(tmp);
            secuenciasCursos.add(secuenciaCurso);
            Secuencia secuenciaSecciones = secuenciaCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
            Secuencia secuenciaSeccion = construirSecuenciaSeccionBasico(seccion);
            secuenciaSecciones.agregarSecuencia(secuenciaSeccion);
        }
        Secuencia secuenciaCurso = new Secuencia(new ArrayList<Atributo>(), secuenciasCursos);
        secuenciaCurso.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS));
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        secuencias.add(secuenciaCurso);
        return secuencias;
    }

    /**
     * Retorna una secuencia dada una sección
     * @param s Sección
     * @return Colección de secuencias
     */
    private Secuencia construirSecuenciaSeccion(Seccion s) {
        Secuencia secuenciaSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
        Secuencia secuenciaValor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + s.getNumeroSeccion());
        secuenciaSeccion.agregarSecuencia(secuenciaValor);
        secuenciaValor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), s.getCrn());
        secuenciaSeccion.agregarSecuencia(secuenciaValor);
        Secuencia secuenciaHorario = conversorServiciosSoporteProcesos.pasarSesionesASecuencia(s.getHorarios());
        System.out.println("***********************************************************");
        secuenciaSeccion.agregarSecuencia(secuenciaHorario);
        return secuenciaSeccion;
    }

    private Secuencia construirSecuenciaSeccionBasico(Seccion s) {
        Secuencia secuenciaSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
        Secuencia secuenciaValor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + s.getNumeroSeccion());
        secuenciaSeccion.agregarSecuencia(secuenciaValor);
        secuenciaValor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), s.getCrn());
        secuenciaSeccion.agregarSecuencia(secuenciaValor);
        return secuenciaSeccion;
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
     * Retorna CursoFacade
     * @return cursoFacade CursoFacade
     */
    private CursoFacadeLocal getCursoFacade() {
        return cursoFacade;
    }

    /**
     * Retorna SeccionFacade
     * @return seccionFacade SeccionFacade
     */
    private SeccionFacadeLocal getSeccionFacade() {
        return seccionFacade;
    }

    /**
     * Retorna FacultadFacade
     * @return facultadFacade FacultadFacade
     */
    private FacultadFacadeLocal getFacultadFacade() {
        return facultadFacade;
    }

    /**
     * Retorna ProfesorFacade
     * @return profesorFacade ProfesorFacade
     */
    private ProfesorFacadeLocal getProfesorFacade() {
        return profesorFacade;
    }

    /**
     * Retorna ProgramaFacade
     * @return programaFacade ProgramaFacade
     */
    private ProgramaFacadeLocal getProgramaFacade() {
        return programaFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    public String cambiarCantidadMonitoresSeccion(String comando) {
        try {
            getParser().leerXML(comando);
            String crn = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)).getValor();
            String cant = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES)).getValor();

            Seccion s = getSeccionFacade().findByCRN(crn);
            if (s != null) {
                s.setMaximoMonitores(Double.parseDouble(cant));
                getSeccionFacade().edit(s);
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_CANTIDAD_MONITORES_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_CANTIDAD_MONITORES_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0129, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(CursoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String modificarDatosCurso(String xml) {
        try {

            getParser().leerXML(xml);

            Secuencia secuencia;
            Curso curso;

            //si la convocatoria esta abierta, solo se puede modificar la presencialidad del curso
            if (convocatoriaBean.hayConvocatoriaAbierta()) {
                secuencia = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
                String presencialidad2 = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRESENCIAL)).getValor();
                String codigoCurso2 = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor();
                curso = getCursoFacade().findByCodigo(codigoCurso2);
                if (curso == null) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_DATOS_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0077, new LinkedList<Secuencia>());
                } else {
                    curso.setPresencial(Boolean.parseBoolean(presencialidad2));
                    getCursoFacade().edit(curso);
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_DATOS_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0015, new LinkedList<Secuencia>());
                }
                //si la convocatoria esta cerrada se permite modificar todos los atributos del curso
            } else {
                secuencia = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
                String nombreCurso = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO)).getValor();
                String codigoCurso = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor();
                String presencialidad = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PRESENCIAL)).getValor();
                String requerido = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO_REQUERIDO)).getValor();
                String maximoMonitores = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES)).getValor();
                curso = getCursoFacade().findByCodigo(codigoCurso);
                if (curso == null) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_DATOS_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0077, new LinkedList<Secuencia>());
                } else {
                    // Se omitio ya que la convoctoria esta cerrada y no hay solicitudes
                    //if(!integracionConflictoLocal.validarCambioCaracteristicasMonitoria(curso.getCodigo()))
                    //    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_DATOS_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0028, new LinkedList<Secuencia>());



                    double cantMaxMonitores = Double.parseDouble(maximoMonitores);
                    Collection<Seccion> secciones = curso.getSecciones();
                    Iterator<Seccion> iterator = secciones.iterator();
                    while (iterator.hasNext()) {
                        Seccion seccion = iterator.next();
                        seccion.setMaximoMonitores(cantMaxMonitores);
                        getSeccionFacade().edit(seccion);
                    }
                    curso.setPresencial(Boolean.parseBoolean(presencialidad));
                    curso.setRequerido(Boolean.parseBoolean(requerido));
                    curso.setNombre(nombreCurso);
                    curso.setCodigo(codigoCurso);
                    getCursoFacade().edit(curso);
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_DATOS_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0015, new LinkedList<Secuencia>());
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_DATOS_CURSO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0020, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvocatoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
}
