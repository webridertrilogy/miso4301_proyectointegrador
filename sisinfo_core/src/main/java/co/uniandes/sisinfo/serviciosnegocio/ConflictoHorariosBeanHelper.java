package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Archivo;
import co.uniandes.sisinfo.entities.PeticionConflictoHorario;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.serviciosfuncionales.ArchivoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeRemote;

/**
 * Servicios de creación de secuencias para ConflictoHorariosBean
 * @author Marcela Morales, Paola Andrea Gómez Barreto
 *
 */
public class ConflictoHorariosBeanHelper {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private ServiceLocator serviceLocator;
    @EJB
    private SeccionFacadeRemote seccionFacadeRemote;
    @EJB
    private ProgramaFacadeRemote programaFacadeRemote;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacadeRemote;
    @EJB
    private EstudianteFacadeRemote estudianteFacadeRemote;
    @EJB
    private ProfesorFacadeRemote profesorFacadeRemote;
    @EJB
    private CursoFacadeRemote cursoFacadeRemote;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private RolFacadeRemote rolFacade;
    @EJB
    private UsuarioFacadeRemote usuarioFacade;
    @EJB
    private ArchivoFacadeRemote archivoFacade;
    @EJB
    private PeticionConflictoHorario peticionConflictoHorarioRemote;
    private ConversorServiciosSoporteProcesos conversorHorario;
    public final static String PROGRAMA_ISIS = "INGENIERIA DE SISTEMAS";
    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------

    public ConflictoHorariosBeanHelper(ConstanteRemote constanteBean, CorreoRemote correoBean) {
        try {
            serviceLocator = new ServiceLocator();
            this.constanteBean = constanteBean;
            this.correoBean = correoBean;

            estudianteFacadeRemote = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            cursoFacadeRemote = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            seccionFacadeRemote = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            programaFacadeRemote = (ProgramaFacadeRemote) serviceLocator.getRemoteEJB(ProgramaFacadeRemote.class);
            nivelFormacionFacadeRemote = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            profesorFacadeRemote = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            rolFacade = (RolFacadeRemote) serviceLocator.getRemoteEJB(RolFacadeRemote.class);
            usuarioFacade = (UsuarioFacadeRemote) serviceLocator.getRemoteEJB(UsuarioFacadeRemote.class);
            archivoFacade = (ArchivoFacadeRemote) serviceLocator.getRemoteEJB(ArchivoFacadeRemote.class);
            conversorHorario = new ConversorServiciosSoporteProcesos();
        } catch (NamingException ex) {
            Logger.getLogger(ConflictoHorariosBeanHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A SECUENCIAS
    //----------------------------------------------
    public Secuencia crearSecuenciaPeticiones(Collection<PeticionConflictoHorario> peticiones) {
        Secuencia secPeticiones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PETICIONES_CONFLICTO_HORARIO), "");

        for (PeticionConflictoHorario peticion : peticiones) {
            Secuencia secPeticion = crearSecuenciaPeticion(peticion);
            secPeticiones.agregarSecuencia(secPeticion);
        }

        return secPeticiones;
    }

    public Secuencia crearSecuenciaPeticion(PeticionConflictoHorario peticion) {

        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PETICION_CONFLICTO_HORARIO), "");

        if (peticion == null) {
            return secPrincipal;
        }

        if (peticion.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), peticion.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }

        if (peticion.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ESTADO_SECCION_CONFLICTO_HORARIO), peticion.getEstado());
            secPrincipal.agregarSecuencia(secEstado);
        }

        if (peticion.getComentarios() != null) {
            Secuencia secComentarios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_COMENTARIOS), peticion.getComentarios());
            secPrincipal.agregarSecuencia(secComentarios);
        }

        if (peticion.getComentariosResolucion() != null) {
            Secuencia secComentariosResolucion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_COMENTARIOS_RESOLUCION), peticion.getComentariosResolucion());
            secPrincipal.agregarSecuencia(secComentariosResolucion);
        }

        if (peticion.getTipo() != null) {
            Secuencia secTipo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_TIPO), peticion.getTipo());
            secPrincipal.agregarSecuencia(secTipo);
        }

        Timestamp fechaCreacion = peticion.getFechaCreacion();
        if (fechaCreacion != null) {
            Secuencia secFechaCreacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), fechaCreacion.toString());
            secPrincipal.agregarSecuencia(secFechaCreacion);
        }

        Timestamp fechaResolucion = peticion.getFechaResolucion();
        if (fechaResolucion != null) {
            Secuencia secFechaResolucion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_RESOLUCION), fechaResolucion.toString());
            secPrincipal.agregarSecuencia(secFechaResolucion);
        }

        if (peticion.getEstudiante() != null) {
            Secuencia secEst = crearSecuenciaEstudiante(peticion.getEstudiante());
            secPrincipal.agregarSecuencia(secEst);
        }

        Secuencia secCursoOrigen = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_CURSO_ORIGEN), null);
        if (peticion.getCursoOrigen() != null) {
            Secuencia secCurso = crearSecuenciaCurso(peticion.getCursoOrigen());
            secCursoOrigen.agregarSecuencia(secCurso);
            secPrincipal.agregarSecuencia(secCursoOrigen);
        }

        Secuencia secCursoDestino = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_CURSO_DESTINO), null);
        if (peticion.getCursoDestino() != null) {
            Secuencia secCurso = crearSecuenciaCurso(peticion.getCursoDestino());
            secCursoDestino.agregarSecuencia(secCurso);
            secPrincipal.agregarSecuencia(secCursoDestino);
        }


        Secuencia secSeccionOrigen = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_SECCION_ORIGEN), null);
        if (peticion.getOrigen() != null) {
            Secuencia secSeccion = crearSecuenciaSeccion(peticion.getOrigen());
            secSeccionOrigen.agregarSecuencia(secSeccion);
            secPrincipal.agregarSecuencia(secSeccionOrigen);
        }

        Secuencia secSeccionDestino = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_SECCION_DESTINO), null);
        if (peticion.getDestino() != null) {
            Secuencia secSeccion = crearSecuenciaSeccion(peticion.getDestino());
            secSeccionDestino.agregarSecuencia(secSeccion);
            secPrincipal.agregarSecuencia(secSeccionDestino);
        }

        return secPrincipal;
    }

    public Secuencia crearSecuenciaCursos(Collection<Curso> cursos) {
        Secuencia secCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), "");

        for (Curso curso : cursos) {
            Secuencia secCurso = crearSecuenciaCurso(curso);
            secCursos.agregarSecuencia(secCurso);
        }

        return secCursos;
    }

    public Secuencia crearSecuenciaCurso(Curso curso) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");


        if (curso == null) {

            return secPrincipal;
        }

        if (curso.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), curso.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }

        if (curso.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), curso.getNombre());
            secPrincipal.agregarSecuencia(secNombre);
        }

        if ((Double) curso.getCreditos() != null) {
            Secuencia secCreditos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_CREDITOS), Double.toString(curso.getCreditos()));
            secPrincipal.agregarSecuencia(secCreditos);
        }

        if (curso.getCodigo() != null) {
            Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo());
            secPrincipal.agregarSecuencia(secCodigo);
        }

        if ((Boolean) curso.isPresencial() != null) {
            Secuencia secPresencial = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_PRESENCIAL), Boolean.toString(curso.isPresencial()));
            secPrincipal.agregarSecuencia(secPresencial);
        }

        if (curso.getCursosRelacionados() != null) {
            Secuencia secCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_RELACIONADOS), "");
            Collection<Curso> cursosRelacionados = new ArrayList<Curso>();
            cursosRelacionados.addAll(curso.getCursosRelacionados());

            for (Curso cursoRelacionado : cursosRelacionados) {
                Secuencia secCursoRelacionado = crearSecuenciaCursoRelacionado(cursoRelacionado);
                secCursos.agregarSecuencia(secCursoRelacionado);
            }

            secPrincipal.agregarSecuencia(secCursos);
        }

        if (curso.getPrograma() != null) {
            Secuencia secPrograma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA), "");

            if (curso.getPrograma().getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(curso.getPrograma().getId()));
                secPrograma.agregarSecuencia(secId);
            }

            if (curso.getPrograma().getNombre() != null) {
                Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), curso.getPrograma().getNombre());
                secPrograma.agregarSecuencia(secNombre);
            }

            secPrincipal.agregarSecuencia(secPrograma);
        }

        if (curso.getNivelPrograma() != null) {
            Secuencia secNivelPrograma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PROGRAMA), "");

            if (curso.getNivelPrograma().getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(curso.getNivelPrograma().getId()));
                secNivelPrograma.agregarSecuencia(secId);
            }

            if (curso.getNivelPrograma().getNombre() != null) {
                Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), curso.getNivelPrograma().getNombre());
                secNivelPrograma.agregarSecuencia(secNombre);
            }

            secPrincipal.agregarSecuencia(secNivelPrograma);
        }

        return secPrincipal;
    }

    public Secuencia crearSecuenciaCursoRelacionado(Curso curso) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");


        if (curso == null) {

            return secPrincipal;
        }

        if (curso.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), curso.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }

        if (curso.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), curso.getNombre());
            secPrincipal.agregarSecuencia(secNombre);
        }

        if (curso.getCodigo() != null) {
            Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo());
            secPrincipal.agregarSecuencia(secCodigo);
        }

        return secPrincipal;
    }

    public Secuencia crearSecuenciaSecciones(Collection<Seccion> secciones) {
        Secuencia secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");

        for (Seccion seccion : secciones) {
            Secuencia secSeccion = crearSecuenciaSeccion(seccion);
            secSecciones.agregarSecuencia(secSeccion);
        }

        return secSecciones;
    }

    public Secuencia crearSecuenciaSeccion(Seccion seccion) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");


        if (seccion == null) {
            return secPrincipal;
        }

        if (seccion.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), seccion.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }

        if (seccion.getCrn() != null) {
            Secuencia secCrn = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn());
            secPrincipal.agregarSecuencia(secCrn);
            Curso c = cursoFacadeRemote.findByCRNSeccion(seccion.getCrn());
            if (c != null) {
                Secuencia secCurso = crearSecuenciaCurso(c);
                secPrincipal.agregarSecuencia(secCurso);
            }
        }

        if ((Double) seccion.getMaximoMonitores() != null) {
            Secuencia secMaximoMonitores = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES), seccion.getMaximoMonitores() + "");
            secPrincipal.agregarSecuencia(secMaximoMonitores);
        }


        if ((Integer) seccion.getNumeroSeccion() != null) {
            Secuencia secNumeroSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), seccion.getNumeroSeccion() + "");
            secPrincipal.agregarSecuencia(secNumeroSeccion);
        }


        if (seccion.getHorarios() != null) {
            Collection<Sesion> horarios = seccion.getHorarios();
            secPrincipal.agregarSecuencia(conversorHorario.pasarSesionesASecuencia(horarios));
        }

        if (seccion.getProfesorPrincipal() != null) {
            Secuencia secProfesorPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_PRINCIPAL), "");

            Secuencia secProfesor = crearSecuenciaProfesor(seccion.getProfesorPrincipal());
            secProfesorPrincipal.agregarSecuencia(secProfesor);

            secPrincipal.agregarSecuencia(secProfesor);
        }

        if (seccion.getProfesores() != null) {
            Secuencia secProfesores = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESORES), "");
            Collection<Profesor> profesores = new ArrayList<Profesor>();
            profesores.addAll(seccion.getProfesores());

            for (Profesor profesor : profesores) {
                Secuencia secProfesor = crearSecuenciaProfesor(profesor);
                secProfesores.agregarSecuencia(secProfesor);
            }

            secPrincipal.agregarSecuencia(secProfesores);
        }
        Secuencia secArchivos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVOS), "");
        Secuencia secArchivo;
        String[] tipos = {getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_PROGRAMA), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_TREINTA_POR_CIENTO), getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CIERRE)};
        String[] cursosNoRequeridos = new String[3];
        cursosNoRequeridos[0] = getConstanteBean().getConstante(Constantes.CONFIGURACION_PARAM_CURSOS_SIN_PROGRAMA);
        cursosNoRequeridos[1] = getConstanteBean().getConstante(Constantes.CONFIGURACION_PARAM_CURSOS_SIN_TREINTA_POR_CIENTO);
        cursosNoRequeridos[2] = getConstanteBean().getConstante(Constantes.CONFIGURACION_PARAM_CURSOS_SIN_CIERRE);

        for (int i = 0; i < tipos.length; i++) {
            Archivo arch = getArchivoFacade().findBySeccionAndTipo(seccion.getCrn(), tipos[i]);
            Curso c = cursoFacadeRemote.findByCRNSeccion(seccion.getCrn());
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

        secPrincipal.agregarSecuencia(secArchivos);


        return secPrincipal;
    }

    public Secuencia crearSecuenciaProfesor(Profesor profesor) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");

        if (profesor == null) {
            return secPrincipal;
        }

        if (profesor.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), profesor.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }

        if (profesor.getPersona().getCorreo() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), profesor.getPersona().getCorreo());
            secPrincipal.agregarSecuencia(secCorreo);
        }

        if (profesor.getPersona().getNombres() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), profesor.getPersona().getNombres());
            secPrincipal.agregarSecuencia(secNombres);
        }

        if (profesor.getPersona().getApellidos() != null) {
            Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), profesor.getPersona().getApellidos());
            secPrincipal.agregarSecuencia(secApellidos);
        }

        return secPrincipal;
    }

    public Secuencia crearSecuenciaEstudiante(Estudiante estudiante) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");

        if (estudiante == null) {
            return secPrincipal;
        }

        if (estudiante.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), estudiante.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }

        if (estudiante.getPersona().getCodigo() != null) {
            Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), estudiante.getPersona().getCodigo());
            secPrincipal.agregarSecuencia(secCodigo);
        }

        if (estudiante.getPersona().getCorreo() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudiante.getPersona().getCorreo());
            secPrincipal.agregarSecuencia(secCorreo);
        }

        if (estudiante.getPersona().getNombres() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudiante.getPersona().getNombres());
            secPrincipal.agregarSecuencia(secNombres);
        }

        if (estudiante.getPersona().getApellidos() != null) {
            Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudiante.getPersona().getApellidos());
            secPrincipal.agregarSecuencia(secApellidos);
        }



        if (estudiante.getPrograma() != null) {
            Secuencia secPrograma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA), "");

            if (estudiante.getPrograma().getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(estudiante.getPrograma().getId()));
                secPrograma.agregarSecuencia(secId);
            }

            if (estudiante.getPrograma().getNombre() != null) {
                Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), estudiante.getPrograma().getNombre());
                secPrograma.agregarSecuencia(secNombre);
            }

            secPrincipal.agregarSecuencia(secPrograma);
        }




        return secPrincipal;
    }

    public Secuencia crearSecuenciaCantidadPeticionesPorCurso(Curso curso, int totalAdicion, int totalRetiro, int totalCambio) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");


        if (curso == null) {

            return secPrincipal;
        }

        if (curso.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), curso.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }

        if (curso.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), curso.getNombre());
            secPrincipal.agregarSecuencia(secNombre);
        }

        if (curso.getCodigo() != null) {
            Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo());
            secPrincipal.agregarSecuencia(secCodigo);
        }

        Secuencia secTotalPeticiones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_TOTAL_PETICIONES), "");
        Secuencia secTotalAdiciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_TOTAL_ADICIONES), Integer.toString(totalAdicion));
        secTotalPeticiones.agregarSecuencia(secTotalAdiciones);
        Secuencia secTotalRetiro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_TOTAL_RETIRO), Integer.toString(totalRetiro));
        secTotalPeticiones.agregarSecuencia(secTotalRetiro);
        Secuencia secTotalCambio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_TOTAL_CAMBIO), Integer.toString(totalCambio));
        secTotalPeticiones.agregarSecuencia(secTotalCambio);


        secPrincipal.agregarSecuencia(secTotalPeticiones);

        return secPrincipal;
    }

    public Secuencia crearSecuenciaProgramas(Collection<Programa> programas) {
        Secuencia secProgramas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMAS), "");
        for (Programa programa : programas) {
            Secuencia secPrograma = crearSecuenciaPrograma(programa);
            secProgramas.agregarSecuencia(secPrograma);
        }
        return secProgramas;
    }

    public Secuencia crearSecuenciaPrograma(Programa programa) {
        Secuencia secPrograma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA), "");
        if (programa == null) {
            return secPrograma;
        }
        if (programa.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), programa.getId().toString());
            secPrograma.agregarSecuencia(secId);
        }
        if (programa.getNombre() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), programa.getNombre().toString());
            secPrograma.agregarSecuencia(secId);
        }
        return secPrograma;
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A ENTIDADES
    //----------------------------------------------
    Collection<PeticionConflictoHorario> crearPeticionesDesdeSecuencia(Secuencia secPeticiones) {

        Collection<PeticionConflictoHorario> peticionCH = new ArrayList<PeticionConflictoHorario>();
        for (Secuencia secuencia : secPeticiones.getSecuencias()) {
            PeticionConflictoHorario p = crearPeticionDesdeSecuencia(secuencia);
            peticionCH.add(p);
        }
        return peticionCH;

    }

    public PeticionConflictoHorario crearPeticionDesdeSecuencia(Secuencia secuencia) {
        try {
            PeticionConflictoHorario peticion = new PeticionConflictoHorario();
            Secuencia secId = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            if (secId != null) {
                Long id = Long.parseLong(secId.getValor().trim());
                peticion.setId(id);
            }

            Secuencia secEstado = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ESTADO_SECCION_CONFLICTO_HORARIO));
            if (secEstado != null) {
                peticion.setEstado(secEstado.getValor());
            }

            Secuencia secComentarios = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_COMENTARIOS));
            if (secComentarios != null) {
                peticion.setComentarios(secComentarios.getValor());
            }

            Secuencia secComentariosR = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_COMENTARIOS_RESOLUCION));
            if (secComentariosR != null) {
                peticion.setComentariosResolucion(secComentariosR.getValor());
            }

            Secuencia secTipo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_TIPO));
            if (secTipo != null) {
                peticion.setTipo(secTipo.getValor());
            }

            Secuencia secFechaCreacion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION));
            SimpleDateFormat sdfHMS = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'COT' yyyy", new Locale("en"));

            if (secFechaCreacion != null) {
                Date d = sdfHMS.parse(secFechaCreacion.getValor());
                peticion.setFechaCreacion(new Timestamp(d.getTime()));
            } else {
                peticion.setFechaCreacion(new Timestamp(new Date().getTime()));
            }

            Secuencia secFechaResolucion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_RESOLUCION));
            if (secFechaResolucion != null) {
                Date d = sdfHMS.parse(secFechaCreacion.getValor());
                peticion.setFechaResolucion(new Timestamp(d.getTime()));
                Date d1 = sdfHMS.parse(peticion.getFechaResolucion().toString());
                peticion.setFechaResolucion(new Timestamp(d1.getTime()));
            }


            Secuencia secEstudiante = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
            if (secEstudiante != null) {
                Estudiante e = crearEstudianteDesdeSecuencia(secEstudiante);
                peticion.setEstudiante(e);
            }


            Secuencia secCursoOrigen = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_CURSO_ORIGEN));
            if (secCursoOrigen != null) {
                Secuencia secCurso = secCursoOrigen.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
                if (secCurso != null) {
                    Curso c = crearCursoDesdeSecuencia(secCurso);
                    peticion.setCursoOrigen(c);
                }
            }


            Secuencia secCursoDestino = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_CURSO_DESTINO));
            if (secCursoDestino != null) {
                Secuencia secCurso = secCursoDestino.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
                if (secCurso != null) {
                    Curso c = crearCursoDesdeSecuencia(secCurso);
                    peticion.setCursoDestino(c);
                }
            }


            Secuencia secSeccionOrigen = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_SECCION_ORIGEN));
            if (secSeccionOrigen != null) {
                Secuencia secSeccion = secSeccionOrigen.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
                if (secSeccion != null) {
                    Seccion s = crearSeccionDesdeSecuencia(secSeccion);
                    peticion.setOrigen(s);
                }
            }


            Secuencia secSeccionDestino = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PETICION_SECCION_DESTINO));
            if (secSeccionDestino != null) {
                Secuencia secSeccion = secSeccionDestino.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
                if (secSeccion != null) {
                    Seccion s = crearSeccionDesdeSecuencia(secSeccion);
                    peticion.setDestino(s);
                }
            }

            return peticion;

        } catch (Exception ex) {
            Logger.getLogger(ConflictoHorariosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public Curso crearCursoDesdeSecuencia(Secuencia secuencia) {
        try {
            Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                    ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                    : null;
            if (id != null) {
                Curso c = cursoFacadeRemote.find(id);
                return c;
            } else {
                String codigoCurso = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)) != null
                        ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor().trim()
                        : null;

                if (codigoCurso != null) {
                    Curso c = cursoFacadeRemote.findByCodigo(codigoCurso);
                    return c;
                } else {
                    String nombreCurso = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null
                            ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor().trim()
                            : null;
                    if (nombreCurso != null) {
                        Curso c = cursoFacadeRemote.findByNombre(nombreCurso);
                        return c;
                    }
                }
                return null;
            }

        } catch (Exception ex) {

            Logger.getLogger(ConflictoHorariosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    public Seccion crearSeccionDesdeSecuencia(Secuencia secuencia) {

        Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;

        if (id != null) {
            Seccion s = seccionFacadeRemote.find(id);
            return s;
        } else {
            String nombre = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)) != null
                    ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION)).getValor().trim()
                    : null;
            if (nombre != null) {
                Seccion s = seccionFacadeRemote.findByCRN(nombre);
                return s;
            } else {
                Secuencia secCurso = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO));
                if (secCurso != null) {
                    String nombreCurso = secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null
                            ? secCurso.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor().trim()
                            : null;
                    int numeroSeccion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION)) != null
                            ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION)).getValorInt()
                            : -1;
                    if (nombreCurso != null && numeroSeccion != -1) {
                        Curso curso = cursoFacadeRemote.findByNombre(nombreCurso);
                        Collection<Seccion> secciones = curso.getSecciones();
                        for (Seccion seccion : secciones) {
                            if (seccion.getNumeroSeccion() == numeroSeccion) {
                                return seccion;
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    public Estudiante crearEstudianteDesdeSecuencia(Secuencia secuencia) {
        try {
            Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                    ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                    : null;
            if (id != null) {
                Estudiante e = estudianteFacadeRemote.find(id);
                return e;
            } else {
                String correo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)) != null
                        ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor().trim()
                        : null;

                if (correo != null) {
                    Estudiante e = estudianteFacadeRemote.findByCorreo(correo);
                    return e;
                }
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(ConflictoHorariosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public Programa crearProgramaDesdeSecuencia(Secuencia secuencia) {
        Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;

        if (id != null) {
            Programa p = programaFacadeRemote.find(id);
            return p;
        } else {
            String nombre = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null
                    ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor().trim()
                    : null;
            if (nombre != null) {
                Programa p = programaFacadeRemote.findByNombre(nombre);
                return p;
            }
            return null;
        }
    }

    public NivelFormacion crearNivelProgramaDesdeSecuencia(Secuencia secuencia) {
        Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;

        if (id != null) {
            NivelFormacion n = nivelFormacionFacadeRemote.find(id);
            return n;
        } else {
            String nombre = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null
                    ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor().trim()
                    : null;
            if (nombre != null) {
                NivelFormacion n = nivelFormacionFacadeRemote.findByName(nombre);
                return n;
            }
            return null;
        }
    }

    public Profesor crearProfesorDesdeSecuencia(Secuencia secuencia) {
        try {
            Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                    ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                    : null;
            if (id != null) {
                Profesor p = profesorFacadeRemote.find(id);
                return p;
            } else {
                String correo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)) != null
                        ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor().trim()
                        : null;

                if (correo != null) {
                    Profesor p = profesorFacadeRemote.findByCorreo(correo);
                    return p;
                }
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(ConflictoHorariosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public Seccion crearNuevaSeccionDesdeSecuencia(Secuencia secuencia) {
        try {
            Seccion seccion = new Seccion();
            Secuencia secId = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

            if (secId != null) {
                Long id = Long.parseLong(secId.getValor().trim());
                seccion.setId(id);
            }


            Secuencia secCrn = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION));
            if (secCrn != null) {
                seccion.setCrn(secCrn.getValor());
            }


            Secuencia secMaxMonitores = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES));
            if (secMaxMonitores != null) {
                seccion.setMaximoMonitores(Double.parseDouble(secMaxMonitores.getValor()));
            }


            Secuencia secNumSeccion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION));
            if (secNumSeccion != null) {
                seccion.setNumeroSeccion(Integer.parseInt(secNumSeccion.getValor()));
            }


            Secuencia secProfesorPrincpal = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_PRINCIPAL));
            if (secProfesorPrincpal != null) {
                Secuencia secProfesor = secProfesorPrincpal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
                if (secProfesor != null) {
                    Profesor p = crearProfesorDesdeSecuencia(secProfesor);
                    seccion.setProfesorPrincipal(p);
                }
            }


            Secuencia secProfesores = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESORES));
            if (secProfesores != null) {
                Collection<Secuencia> secs = secProfesores.getSecuencias();
                Collection<Profesor> profesores = new ArrayList<Profesor>();
                for (Secuencia sec : secs) {
                    if (sec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR))) {
                        Profesor p = crearProfesorDesdeSecuencia(sec);
                        if (seccion.getProfesorPrincipal() == null) {
                            seccion.setProfesorPrincipal(p);
                        }
                        profesores.add(p);
                    }
                }
                seccion.setProfesores(profesores);
            }

            Secuencia secHorarios = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SESIONES));
            if (secHorarios != null) {
                Collection<Secuencia> secsH = secHorarios.getSecuencias();
                Collection<Sesion> horarios = new ArrayList<Sesion>();

                for (Secuencia sec : secsH) {
                    if (sec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_SESION))) {
                        Sesion s = new Sesion();

                        Secuencia secS = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SALON));
                        if (secS != null) {
                            s.setSalon(secS.getValor());
                        }

                        Secuencia secSS = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO));
                        if (secSS.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO))) {
                            s.setDias(conversorHorario.pasarSecuenciaADiasCompletos(secSS));
                        }

                        horarios.add(s);
                    }
                }
                seccion.setHorarios(horarios);
            }

            return seccion;

        } catch (Exception ex) {
            Logger.getLogger(ConflictoHorariosBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    //----------------------------------------------
    // MÉTODOS HTML
    //----------------------------------------------
    //Código HTML que se incluye en el correo
    public String getPeticionHTML(PeticionConflictoHorario peticion) {
        String doc = "";
        SimpleDateFormat df = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy, hh':'mm a", new Locale("es"));
        doc += "<b> Fecha de creación: </b>" + df.format(peticion.getFechaCreacion()) + "<br/>";
        if (peticion.getFechaResolucion() != null) {
            doc += "<b> Fecha de resolución: </b>" + df.format(peticion.getFechaResolucion()) + "<br/>";
        }
        doc += "<b> Estado: </b>" + peticion.getEstado() + "<br/>";
        doc += "<b> Materia destino: </b> " + peticion.getCursoDestino().getNombre() + " - " + peticion.getCursoDestino().getCodigo() + " <br/>";
        doc += "<b> CRN Seccion: </b> " + peticion.getDestino().getCrn() + " <br/>" + "<b> Profesor (es): </b>";
        if (peticion.getOrigen() != null && peticion.getCursoOrigen() != null) {
            doc += "<b> Materia origen: </b> " + peticion.getCursoOrigen().getNombre() + " - " + peticion.getCursoOrigen().getCodigo() + " <br/>";
            doc += "<b> CRN: </b> " + peticion.getOrigen().getCrn() + " <br/>" + "<b> Profesor (es): </b>";
        }
        return doc;
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public CorreoRemote getCorreoBean() {
        return correoBean;
    }

    void crearPrograma(String codigo, String nombre) {
        Programa programa = programaFacadeRemote.findByNombre(nombre);
        if (programa == null) {
            programa = new Programa();
            programa.setId(null);
            programa.setNombre(nombre);
            programa.setFacultad(null);
            programa.setCodigo(codigo);
            programaFacadeRemote.create(programa);
        }
    }

    void crearSeccion(int crnSeccion, String codigoCurso, int numeroSeccion, double creditos, String nombreCurso, double monitores, String esPresencial, String esObligatoria, String nombreNivel, String horaInicio, String horaFin, String salon, String dias, String horaInicio2, String horaFin2, String salon2, String dias2, String horaInicio3, String horaFin3, String salon3, String dias3, String loginProfesor1, String loginProfesor2, String loginProfesor3, String relacionados) throws Exception {
        //Creación de curso
        Curso curso = cursoFacadeRemote.findByCodigo(codigoCurso);
        if (curso == null) {
            crearCurso(codigoCurso, creditos, nombreNivel, nombreCurso, esPresencial, esObligatoria, relacionados);
        }

        //Creación de sección
        Seccion seccion = new Seccion();
        seccion.setCrn(crnSeccion + "");
        seccion.setId(null);
        seccion.setMaximoMonitores(monitores);
        seccion.setNumeroSeccion(numeroSeccion);
        //Horarios
        Collection<Sesion> sesiones = new ArrayList<Sesion>();
        //Horario 1
        Sesion sesion1 = crearHorario(horaInicio, horaFin, salon, dias);
        if (sesion1 != null) {
            sesiones.add(sesion1);
        }
        //Horario 2
        Sesion sesion2 = crearHorario(horaInicio2, horaFin2, salon2, dias2);
        if (sesion2 != null) {
            sesiones.add(sesion2);
        }
        //Horario 3
        Sesion sesion3 = crearHorario(horaInicio3, horaFin3, salon3, dias3);
        if (sesion3 != null) {

            sesiones.add(sesion3);
        }
        seccion.setHorarios(sesiones);
        //Profesores
        Collection<Profesor> profesores = new ArrayList<Profesor>();
        if (!loginProfesor1.equals("")) {
            Profesor profesor1 = profesorFacadeRemote.findByCorreo(loginProfesor1 + "@uniandes.edu.co");
            if (profesor1 == null) {
                getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.CONFIG_MAIL_USER) + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO), "Profesor Faltante", null, null, null, "El profesor con correo " + loginProfesor1 + " no se encuentra registrado en el sistema. "
                        + "Se ha agregado como profesor de catedra, por favor actualizar la información.");

                //se envia el correo de bienvenida al profesor
                correoBean.enviarMail(loginProfesor1 + "@uniandes.edu.co", Notificaciones.ASUNTO_BIENVENIDA_PROFESOR_SISINFO, null, null, null, Notificaciones.MENSAJE_BIENVENIDA_PROFESOR_SISINFO);

                crearProfesor(loginProfesor1);
            }
            seccion.setProfesorPrincipal(profesor1);
            profesores.add(profesor1);
        }
        if (!loginProfesor2.equals("")) {
            Profesor profesor2 = profesorFacadeRemote.findByCorreo(loginProfesor2 + "@uniandes.edu.co");
            if (profesor2 == null) {
                getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.CONFIG_MAIL_USER) + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO), "Profesor Faltante", null, null, null, "El profesor con correo " + loginProfesor2 + " no se encuentra registrado en el sistema. "
                        + "Se ha agregado como profesor de planta, por favor actualizar la información.");
                crearProfesor(loginProfesor2);
            }
            profesores.add(profesor2);
        }
        if (!loginProfesor3.equals("")) {
            Profesor profesor3 = profesorFacadeRemote.findByCorreo(loginProfesor3 + "@uniandes.edu.co");
            if (profesor3 == null) {
                getCorreoBean().enviarMail(getConstanteBean().getConstante(Constantes.CONFIG_MAIL_USER) + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO), "Profesor Faltante", null, null, null, "El profesor con correo " + loginProfesor3 + " no se encuentra registrado en el sistema. "
                        + "Se ha agregado como profesor de planta, por favor actualizar la información.");
                crearProfesor(loginProfesor3);
            }
            profesores.add(profesor3);
        }
        seccion.setProfesores(profesores);
        seccionFacadeRemote.create(seccion);

        //Agrega la sección al curso
        curso = cursoFacadeRemote.findByCodigo(codigoCurso);
        seccion = seccionFacadeRemote.findByCRN(crnSeccion + "");
        Collection<Seccion> secciones = curso.getSecciones();
        secciones.add(seccion);
        curso.setSecciones(secciones);
        cursoFacadeRemote.edit(curso);
    }

    void crearCurso(String codigoCurso, double creditos, String nombreNivel, String nombreCurso, String esPresencial, String esObligatoria, String relacionados) {
        Curso curso = new Curso();
        curso.setCodigo(codigoCurso);
        curso.setCreditos(creditos);
        curso.setId(null);
        NivelFormacion nivel = nivelFormacionFacadeRemote.findByName(nombreNivel);
        curso.setNivelPrograma(nivel);
        curso.setNivel(Integer.parseInt(nivel.getId().toString()));
        curso.setNombre(nombreCurso);
        curso.setPresencial(Boolean.parseBoolean(esPresencial));
        Programa programa = programaFacadeRemote.findByNombre(PROGRAMA_ISIS);
        curso.setPrograma(programa);
        curso.setRequerido(Boolean.parseBoolean(esObligatoria));
        curso.setSecciones(new ArrayList<Seccion>());
        Collection<Curso> cursosRelacionados = new ArrayList<Curso>();
        for (String nombreRelacionado : relacionados.split(", ")) {
            Curso cursoRelacionado = cursoFacadeRemote.findByNombre(nombreRelacionado);
            if (cursoRelacionado != null) {
                cursosRelacionados.add(cursoRelacionado);
            }
        }
        curso.setCursosRelacionados(cursosRelacionados);
        cursoFacadeRemote.create(curso);
    }

    private void crearProfesor(String correo) {

        String sufijo = getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
        if (!correo.contains(sufijo)) {
            correo = correo + sufijo;
        }

        //Se crea un nuevo profesor
        Profesor profesor = new Profesor();
        Persona persona = personaFacade.findByCorreo(correo);
        boolean personaExiste = false;
        if (persona == null) {
            persona = new Persona();
        } else {
            personaExiste = true;
        }
        persona.setActivo(true);
        persona.setCorreo(correo);

        //Se agrega el rol de profesor de cátedra (por defecto)
        profesor.setTipo(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA));

        Rol rolProfesor = rolFacade.findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA));
        Rol rolEstudiante = rolFacade.findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));

        //Se persiste el profesor
        if (personaExiste) {
            personaFacade.edit(persona);
        } else {
            personaFacade.create(persona);
        }

        profesor.setPersona(personaFacade.findByCorreo(correo));
        profesor.setActivo(true);
        profesorFacadeRemote.create(profesor);

        //Se busca al usuario con este correo
        Usuario usuario = usuarioFacade.findByLogin(correo);
        //Si el usuario es nulo, se crea un nuevo usuario
        if (usuario == null) {
            //Se agrega un nuevo usuario con el identificador de este profesor
            usuario = new Usuario();
            Persona p = personaFacade.findByCorreo(correo);
            p.setCorreo(correo);
            usuario.setPersona(p);
            usuario.setEsPersona(true);

            Collection<Rol> listaRoles = new ArrayList<Rol>();
            listaRoles.add(rolProfesor);
            listaRoles.add(rolEstudiante);
            usuario.setRoles(listaRoles);
            usuarioFacade.create(usuario);
        } //De lo contrario se agrega el rol del profesor al usuario
        else {
            Collection<Rol> listaRoles = usuario.getRoles();
            listaRoles.add(rolProfesor);
            usuario.setRoles(listaRoles);
            usuarioFacade.edit(usuario);
        }
    }

    /**
     * Crea la una sesion correspondiente al horario de una materia en una cierta franja de horas en unos ciertos dias.
     * @param horaInicio Un string con la hora y minuto de inicio en formato militar (24h). Ejemplo: 1700
     * @param horaFin Un string con la hora y minuto de fin en formato militar (24h). Ejemplo: 1820
     * @param salon El salon en el cual se lleva a cabo la sesion
     * @param dias Un string con caracteres para cada uno de los dias ocupados. El string solo debe tener los siguientes caracteres:
     * { L M I J V }
     * @return Un objeto de tipo sesion con toda la información.
     */
    Sesion crearHorario(String horaInicio, String horaFin, String salon, String dias) {
        Sesion sesion = new Sesion();
        if (horaInicio.equals("") || horaFin.equals("") || salon.equals("") || dias.equals("")) {
            return null;
        }
        sesion.setSalon(salon);

        Collection<DiaCompleto> collDias = new ArrayList<DiaCompleto>();
        if (dias.contains("L")) {
            collDias.add(crearDiaCompleto(horaInicio, horaFin, "L"));
        }
        if (dias.contains("M")) {
            collDias.add(crearDiaCompleto(horaInicio, horaFin, "M"));
        }
        if (dias.contains("I")) {
            collDias.add(crearDiaCompleto(horaInicio, horaFin, "I"));
        }
        if (dias.contains("J")) {
            collDias.add(crearDiaCompleto(horaInicio, horaFin, "J"));
        }
        if (dias.contains("V")) {
            collDias.add(crearDiaCompleto(horaInicio, horaFin, "V"));
        }
        if (dias.contains("S")) {
            collDias.add(crearDiaCompleto(horaInicio, horaFin, "S"));
        }
        sesion.setDias(collDias);
        return sesion;
    }

    /**
     * Crea la estructura correspondiente a un dia completo basado en la hora inicio, hora fin y el dia.<br>
     * La información se guarda como un binario donde cada digito representa una franja de 30 minutos entre las 0 las 24.
     * Los 0s representan horarios libres y los 1s horarios ocupados.
     * @param horaInicio Un string con la hora y minuto de inicio en formato militar (24h). Ejemplo: 1700
     * @param horaFin Un string con la hora y minuto de fin en formato militar (24h). Ejemplo: 1820
     * @param nombreDia Un string que contiene el nombre del dia que se esta creando.
     * @return Un objeto de tipo dia completo con toda la información.
     */
    DiaCompleto crearDiaCompleto(String horaInicio, String horaFin, String nombreDia) {
        DiaCompleto dia = new DiaCompleto();
        dia.setDia_semana(nombreDia);

        //Hora inicio
        int hi = -1;
        int mi = -1;
        if (horaInicio.length() == 3) {
            hi = Integer.parseInt(horaInicio.substring(0, 1));
            mi = Integer.parseInt(horaInicio.substring(1, 3));
        } else {
            hi = Integer.parseInt(horaInicio.substring(0, 2));
            mi = Integer.parseInt(horaInicio.substring(2, 4));
        }

        //Hora fin
        int hf = -1;
        int mf = -1;
        if (horaFin.length() == 3) {
            hf = Integer.parseInt(horaFin.substring(0, 1));
            mf = Integer.parseInt(horaFin.substring(1, 3));
        } else {
            hf = Integer.parseInt(horaFin.substring(0, 2));
            mf = Integer.parseInt(horaFin.substring(2, 4));
        }

        int init = hi * 2 + (mi / 30);
        int fin = hf * 2 + (mf / 30);
        String horas = "";
        for (int i = 0; i < init; i++) {
            horas += "0";
        }
        for (int i = init; i <= fin; i++) {
            horas += "1";
        }
        for (int i = fin + 1; i < 48; i++) {
            horas += "0";
        }
        dia.setHoras(horas);
        return dia;
    }

    Secuencia crearSecuenciaProfesores(Collection<Profesor> profesores) {
        Secuencia secProfesores = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PROFESORES_CONFLICTO_HORARIO), "");
        for (Profesor profesor : profesores) {
            Secuencia secProfesor = crearSecuenciaProfesor(profesor);
            secProfesores.agregarSecuencia(secProfesor);
        }
        return secProfesores;
    }


    public ArchivoFacadeRemote getArchivoFacade() {
        return archivoFacade;
    }
}
