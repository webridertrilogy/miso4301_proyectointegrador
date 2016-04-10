package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.Archivo;
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.serviciosfuncionales.ArchivoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ConfiguracionFechasCHFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeticionConflictoHorarioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacade;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SesionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicios de administración de cursos y secciones
 * @author German Florez, Marcela Morales
 */
@Stateless
public class CarteleraCursosBean implements CarteleraCursosBeanLocal, CarteleraCursosBeanRemote {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Remotos
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private ProgramaFacadeRemote programaFacade;
    @EJB
    private SeccionFacadeRemote seccionFacade;
    @EJB
    private SesionFacadeRemote horarioFacade;
    @EJB
    private CursoFacadeRemote cursoFacade;
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    @EJB
    private ProfesorFacadeRemote profesorFacade;
    @EJB
    private IntegracionConflictoHorariosBeanRemote serviciosIntegracion;
    @EJB
    private ArchivoFacadeRemote archivoFacade;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacade;
    //Locales
    @EJB
    private ConfiguracionFechasCHFacadeLocal configuracionFechasFacade;
    @EJB
    private PeticionConflictoHorarioFacadeLocal peticionConflictoHorarioFacade;
    @EJB
    private ConflictoHorariosBeanLocal conflictoHorarios;
    @EJB
    private ConstanteRemote constanteBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConflictoHorariosBeanHelper conversor;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public CarteleraCursosBean() throws NamingException {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            programaFacade = (ProgramaFacadeRemote) serviceLocator.getRemoteEJB(ProgramaFacadeRemote.class);
            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            horarioFacade = (SesionFacadeRemote) serviceLocator.getRemoteEJB(SesionFacadeRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            serviciosIntegracion = (IntegracionConflictoHorariosBeanRemote) serviceLocator.getRemoteEJB(IntegracionConflictoHorariosBeanRemote.class);
            archivoFacade = (ArchivoFacadeRemote) serviceLocator.getRemoteEJB(ArchivoFacadeRemote.class);
            nivelFormacionFacade = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            conversor = new ConflictoHorariosBeanHelper(getConstanteBean(), getCorreoBean());
        } catch (NamingException ex) {
            Logger.getLogger(CarteleraCursosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------------------------------------------
    // MÉTODOS DE NEGOCIO
    //----------------------------------------------
    //CURSOS
    public String consultarCursosISIS(String xml) {
        try {
            getParser().leerXML(xml);
            //Consulta todos los cursos (sólo se manejan cursos de ISIS)
            ArrayList<Curso> cursos = (ArrayList<Curso>) getCursoFacade().findAll();
            
            //Ordena los cursos por orden alfabetico
            Collections.sort(cursos, new Comparator<Curso>() {

                public int compare(Curso o1, Curso o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });

            Secuencia secCursos = getConversor().crearSecuenciaCursos(cursos);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCursos);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MATERIAS_PROGRAMA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_002, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MATERIAS_PROGRAMA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_010, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    //SECCIONES
    public String consultarSeccionesPorCodigoCurso(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secCurso = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
            Curso curso = getConversor().crearCursoDesdeSecuencia(secCurso);

            //Consulta el curso acorde al código dado
            Curso cursoExiste = getCursoFacade().findByCodigo(curso.getCodigo());
            if (cursoExiste == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCIONES_MATERIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_008, new LinkedList<Secuencia>());
            }
            //Consulta las secciones del curso
            Collection<Seccion> secciones = cursoExiste.getSecciones();
            Secuencia secSecciones = getConversor().crearSecuenciaSecciones(secciones);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secSecciones);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCIONES_MATERIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_003, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCIONES_MATERIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_011, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String crearSeccionACurso(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secCurso = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
            Secuencia secSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
            String codigoCurso = secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor();
            Seccion seccion = getConversor().crearNuevaSeccionDesdeSecuencia(secSeccion);

            //1. Valida que el curso exista
            Curso curso = getCursoFacade().findByCodigo(codigoCurso);
            if (curso == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_026, new LinkedList<Secuencia>());
            }
            //2. Valida que no exista una sección con el mismo CRN
            Seccion seccionExiste = getSeccionFacade().findByCRN(seccion.getCrn());
            if (seccionExiste != null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_003, new LinkedList<Secuencia>());
            }

            //Persiste la sección y actualiza el curso
            getSeccionFacade().create(seccion);
            Collection<Seccion> secciones = curso.getSecciones();
            Seccion seccionPersist = getSeccionFacade().findByCRN(seccion.getCrn());
            secciones.add(seccionPersist);
            curso.setSecciones(secciones);
            getCursoFacade().edit(curso);

            //Informa a monitorías la adición de la nueva sección
            serviciosIntegracion.crearSeccion(seccion.getCrn());

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_010, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_019, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String editarDatosSeccion(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
            Seccion seccion = getConversor().crearNuevaSeccionDesdeSecuencia(secSeccion);

            //1. Valida que la sección exista
            Seccion seccionExiste = getSeccionFacade().findByCRN(seccion.getCrn());
            if (seccionExiste == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_003, new LinkedList<Secuencia>());
            }
            // 2. Valida que el nuevo numero de monitores sea consistente con los monitores que ya han sido preseleccionados
            if (!serviciosIntegracion.validarCambioNumeroMonitores(seccion.getCrn(), seccion.getMaximoMonitores())) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_033, new LinkedList<Secuencia>());
            }

            //Guarda en variables temporales la información anterior al cambio
            Collection<Sesion> horarioAnterior = seccionExiste.getHorarios();
            Double monitoresAnterior = seccionExiste.getMaximoMonitores();
            String profesorAnterior = seccionExiste.getProfesorPrincipal() != null ? seccionExiste.getProfesorPrincipal().getPersona().getCorreo() : null;

            //Edita la sección
            seccionExiste.setCrn(seccion.getCrn());
            seccionExiste.setHorarios(seccion.getHorarios());
            seccionExiste.setMaximoMonitores(seccion.getMaximoMonitores());
            seccionExiste.setNumeroSeccion(seccion.getNumeroSeccion());
            seccionExiste.setProfesorPrincipal(seccion.getProfesorPrincipal());
            seccionExiste.setProfesores(seccion.getProfesores());

            //Actualiza la sección
            getSeccionFacade().edit(seccionExiste);

            //Valida si hubo cambios en la información de la sección
            //1. Horario
            if (!compararHorario(horarioAnterior, seccion.getHorarios())) {
                serviciosIntegracion.cambiarHorarioSeccion(seccion.getCrn());
            }
            //2. Número monitores
            if (monitoresAnterior != seccion.getMaximoMonitores()) {
                serviciosIntegracion.cambiarNumeroMonitores(seccion.getCrn());
            }
            //3. profesor
            if (profesorAnterior == null || !profesorAnterior.equals(seccion.getProfesorPrincipal().getPersona().getCorreo())) {
                serviciosIntegracion.cambiarProfesor(seccion.getCrn(), profesorAnterior);
            }

            ArrayList<Secuencia> params = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_DATOS_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, params);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_DATOS_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private boolean compararHorario(Collection<Sesion> sesiones1, Collection<Sesion> sesiones2) {
        Collection<DiaCompleto> dias1 = new ArrayList(), dias2 = new ArrayList();
        if (dias1 == null && dias2 == null) {
            return true;
        } else if (dias1 == null || dias2 == null) {
            return false;
        }
        for (Sesion sesion : sesiones1) {
            dias1.addAll(sesion.getDias());
        }
        for (Sesion sesion : sesiones2) {
            dias2.addAll(sesion.getDias());
        }

        // La cantidad de dias en los dos horarios es diferente
        if (dias1.size() != dias2.size()) {
            return false;
        }
        for (DiaCompleto diaCompleto : dias1) {
            boolean found = false;
            for (DiaCompleto diaCompleto2 : dias2) {
                if (diaCompleto.getHoras().equals(diaCompleto2.getHoras()) && diaCompleto.getDia_semana().equals(diaCompleto2.getDia_semana())) {
                    found = true;
                    break;
                }
            }
            // Alguno de los dias en dias2 no esta en dias1 entonces los horarios no coinciden
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public String eliminarSeccion(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
            Seccion seccion = getConversor().crearSeccionDesdeSecuencia(secSeccion);

            //1. Valida que la sección exista
            if (seccion == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_003, new LinkedList<Secuencia>());
            }

            //2. Valida que el curso exista
            Curso curso = getCursoFacade().findByCRNSeccion(seccion.getCrn());
            if (curso == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_026, new LinkedList<Secuencia>());
            }

            //Verificar si hay peticiones asociadas a la sección, si la hay se van eliminar
            conflictoHorarios.eliminarPeticionesPorSeccion(seccion.getId());

            //Informa a monitorías para que revierta las solicitudes, etc.
            serviciosIntegracion.eliminarSeccion(seccion.getCrn());

            //Desvincula la seccion del curso
            Collection<Seccion> secciones = curso.getSecciones();
            Seccion seccionPersist = getSeccionFacade().findByCRN(seccion.getCrn());
            secciones.remove(seccionPersist);
            curso.setSecciones(secciones);
            getCursoFacade().edit(curso);

            //Se eliminan los archivos asociados a la seccion
            ArrayList<Archivo> archivosDeSeccion = new ArrayList<Archivo>();
            archivosDeSeccion = (ArrayList<Archivo>) getArchivoFacade().findBySeccion(seccion.getCrn());
            System.out.println(archivosDeSeccion.size());
            for (Archivo archivo : archivosDeSeccion) {
                System.out.println(archivo.getTipoMime());
                getArchivoFacade().remove(archivo);
            }

            //Actualiza la sección
            getSeccionFacade().remove(seccion);

            ArrayList<Secuencia> params = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, params);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarSeccion(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros

            Secuencia secSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
            Seccion seccion = getConversor().crearSeccionDesdeSecuencia(secSeccion);

            //Consulta la seccion acorde al crn dado
            Seccion seccionExiste = getSeccionFacade().findByCRN(seccion.getCrn());
            if (seccionExiste == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_003, new LinkedList<Secuencia>());
            }

            Secuencia secSeccionExiste = getConversor().crearSecuenciaSeccion(seccionExiste);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secSeccionExiste);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_011, new LinkedList<Secuencia>()); // VALIDAR TAG
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    //CARTELERA
    public String cargarCarteleraWeb(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secRuta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA));
            if (secRuta == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA_CONFLICTO_HORARIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_030, new ArrayList());
            }
            Secuencia secPeriodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO));
            if (secPeriodo == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA_CONFLICTO_HORARIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_030, new ArrayList());
            }
            String rutaCartelera = secRuta.getValor();
            String nombrePeriodo = secPeriodo.getValor();

            //Verifica que no exista una convocatoria abierta
            if (serviciosIntegracion.existeConvocatoriaAbierta()) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA_CONFLICTO_HORARIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_032, new ArrayList());
            }

            //Elimina toda la información asociada a la cartelera y a los conflictos de horario
            getPeticionConflictoHorarioFacade().removeAll();
            getSeccionFacade().removeAll();
            getCursoFacade().removeAll();
            getHorarioFacade().removeAll();

            //Crea el periodo
            Periodo periodo = getPeriodoFacade().findByPeriodo(nombrePeriodo);
            if (periodo == null) {
                periodo = new Periodo();
                periodo.setActual(true);
                periodo.setConvocatoria(null);
                periodo.setId(null);
                periodo.setPeriodo(nombrePeriodo);
                getPeriodoFacade().create(periodo);
            } else {
                periodo.setActual(true);
                periodo.setConvocatoria(null);
                periodo.setPeriodo(nombrePeriodo);
                getPeriodoFacade().edit(periodo);
            }

            //Carga la cartelera

            new LectorCartelera(getConstanteBean(), getCorreoBean(), getProgramaFacade()).cargarCartelera(new FileInputStream(rutaCartelera));

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA_CONFLICTO_HORARIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_014, secuencias);

        } catch (Exception e) {
            try {
                e.printStackTrace();
                System.out.println("--" + e.getMessage() + "--");
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                String mensaje = e.getMessage();
                ArrayList<Secuencia> params = new ArrayList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), mensaje);
                params.add(secParametro);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA_CONFLICTO_HORARIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_005, params);
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarProfesoresISIS(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros

            Collection<Profesor> profesores = getProfesorFacade().findAll();
            Secuencia secProfesores = getConversor().crearSecuenciaProfesores(profesores);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secProfesores);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_ISIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_001, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_ISIS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_011, new LinkedList<Secuencia>()); // VALIDAR TAG
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private ConfiguracionFechasCHFacadeLocal getConfiguracionFechasFacade() {
        return configuracionFechasFacade;
    }

    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    private CursoFacadeRemote getCursoFacade() {
        return cursoFacade;
    }

    public NivelFormacionFacadeRemote getNivelFormacionFacade() {
        return nivelFormacionFacade;
    }

    private ConflictoHorariosBeanHelper getConversor() {
        return conversor;
    }

    private SesionFacadeRemote getHorarioFacade() {
        return horarioFacade;
    }

    private PeticionConflictoHorarioFacadeLocal getPeticionConflictoHorarioFacade() {
        return peticionConflictoHorarioFacade;
    }

    private ProgramaFacadeRemote getProgramaFacade() {
        return programaFacade;
    }

    private SeccionFacadeRemote getSeccionFacade() {
        return seccionFacade;
    }

    private PeriodoFacadeRemote getPeriodoFacade() {
        return periodoFacade;
    }

    private ProfesorFacadeRemote getProfesorFacade() {
        return profesorFacade;
    }

    public ArchivoFacadeRemote getArchivoFacade() {
        return archivoFacade;
    }

    public String crearCurso(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);
            //Se extraen los parámetros
            Secuencia secCurso = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
            //Secuencia secSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
            Curso curso = new Curso();
            curso.setNombre(secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor());
            curso.setCodigo(secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor());
            curso.setCreditos(Double.parseDouble(secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSO)).getValor()));
            curso.setPresencial(Boolean.parseBoolean(secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_PRESENCIAL)).getValor()));
            curso.setRequerido(Boolean.parseBoolean(secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_REQUERIDA)).getValor()));

            NivelFormacion nivel = getNivelFormacionFacade().findByName(secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_CURSO)).getValor());
            curso.setNivelPrograma(nivel);
            curso.setNivel(nivel.getId().intValue());

            //1. Valida que el curso exista
            Curso cursoBD = getCursoFacade().findByCodigo(curso.getCodigo());
            if (cursoBD != null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_CURSO_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_034, new LinkedList<Secuencia>());
            }

            //Si el curso no existe, lo crea
            getCursoFacade().create(curso);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_CURSO_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_010, secuencias);
        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_CURSO_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_035, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
}
