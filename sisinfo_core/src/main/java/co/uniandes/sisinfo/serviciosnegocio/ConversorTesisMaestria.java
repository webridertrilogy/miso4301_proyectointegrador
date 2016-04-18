/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.CalificacionCriterio;
import co.uniandes.sisinfo.entities.CalificacionJurado;
import co.uniandes.sisinfo.entities.CategoriaCriterioJurado;
import co.uniandes.sisinfo.entities.Coasesor;
import co.uniandes.sisinfo.entities.ComentarioTesis;
import co.uniandes.sisinfo.entities.CursoMaestria;
import co.uniandes.sisinfo.entities.CursoTesis;
import co.uniandes.sisinfo.entities.HorarioSustentacionTesis;
import co.uniandes.sisinfo.entities.InscripcionSubareaInvestigacion;
import co.uniandes.sisinfo.entities.JuradoExternoUniversidad;
import co.uniandes.sisinfo.entities.PeriodoTesis;
import co.uniandes.sisinfo.entities.SubareaInvestigacion;
import co.uniandes.sisinfo.entities.Tesis1;
import co.uniandes.sisinfo.entities.Tesis2;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.serviciosfuncionales.CursoMaestriaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoTesisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SubareaInvestigacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
public class ConversorTesisMaestria {

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ProfesorFacadeRemote profesorFacade;
    @EJB
    private EstudianteFacadeRemote estudianteFacadeRemote;
    @EJB
    private PeriodoTesisFacadeRemote periodoFacadeRemote;
    @EJB
    private SubareaInvestigacionFacadeRemote subareaInvestigacionFacadeRemote;
    @EJB
    private CursoMaestriaFacadeRemote cursoMaestriaRemote;
    private ServiceLocator serviceLocator;

    public ConversorTesisMaestria() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            estudianteFacadeRemote = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            subareaInvestigacionFacadeRemote = (SubareaInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(SubareaInvestigacionFacadeRemote.class);
            periodoFacadeRemote = (PeriodoTesisFacadeRemote) serviceLocator.getRemoteEJB(PeriodoTesisFacadeRemote.class);
            cursoMaestriaRemote = (CursoMaestriaFacadeRemote) serviceLocator.getRemoteEJB(CursoMaestriaFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ConversorTesisMaestria.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * metodo que convierte una  coleccion de inscripcion de subarea a secuencia
     * @param ins:Collection<InscripcionSubareaInvestigacion>
     * @return Secuencia
     */
    public Secuencia pasarInscripcionesSubareaASecuencia(Collection<InscripcionSubareaInvestigacion> ins) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), "");

        if (ins != null) {
            for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : ins) {
                Secuencia sec = pasarInscripcionSubareaASecuencia(inscripcionSubareaInvestigacion);
                secPrincipal.agregarSecuencia(sec);
            }
        }
        return secPrincipal;


    }

    /**
     * convieret una coleccion de  curso de maestria a secuencia
     * @param cursos: colleccion de cursos de maestria (los que estan en el programa)
     * @return Secuencia
     */
    public Secuencia pasarCursosMaestriaAsecuencia(Collection<CursoMaestria> cursos) {
        Secuencia secCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), "");

        for (CursoMaestria cursoMaestria : cursos) {
            Secuencia secCurso = pasarCursoMaestriaASecuencia(cursoMaestria);
            secCursos.agregarSecuencia(secCurso);
        }
        return secCursos;
    }

    /**
     * metodo que convierte un curso de maestria en secuencia
     * @param otraSubArea: curso de maestria
     * @return Secuencia
     */
    public Secuencia pasarCursoMaestriaASecuencia(CursoMaestria cursoMaestria) {
        Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
        Secuencia secID = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), cursoMaestria.getId().toString());
        secCurso.agregarSecuencia(secID);
        Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), cursoMaestria.getNombre());
        secCurso.agregarSecuencia(secNombre);
        Secuencia secdescrip = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), cursoMaestria.getDescricpion());
        secCurso.agregarSecuencia(secdescrip);
        Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), cursoMaestria.getCodigo());
        secCurso.agregarSecuencia(secCodigo);
        Secuencia secClasificacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CLASIFICACION), cursoMaestria.getClasificacion());
        secCurso.agregarSecuencia(secClasificacion);
        return secCurso;
    }

    public CursoMaestria pasarSecuenciaACursoMaestria(Secuencia sec) {
        CursoMaestria curso = new CursoMaestria();
        String strId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor();
        curso.setId(!strId.isEmpty() ? Long.parseLong(strId) : null);
        curso.setCodigo(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO)).getValor());
        curso.setClasificacion(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CLASIFICACION)).getValor());
        curso.setDescricpion(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor());
        curso.setNombre(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor());
        return curso;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    /**
     * metodo que convierte una inscripcion a subarea a Secuencia
     * @param inscripcion: InscripcionSubareaInvestigacion
     * @return Secuencia
     */
    public Secuencia pasarInscripcionSubareaASecuencia(InscripcionSubareaInvestigacion inscripcion) {
        Secuencia secuenciaPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), "");
        if (inscripcion == null) {
            return secuenciaPrincipal;
        }
        // Collection<Secuencia>secsDePrincipal= new ArrayList<Secuencia>();


        Long id = inscripcion.getId();


        if (id != null) {
            secuenciaPrincipal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscripcion.getId().toString()));


        } /*
         *     <temaTesis>sadfdfadsf</temaTesis>*/
        String temaTesis = inscripcion.getTemaTesis();


        if (temaTesis != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), temaTesis);
            secuenciaPrincipal.agregarSecuencia(secTemaTesis);


        }
        /*
         *     <fechaTerminacion>2010-12-12</fechaTerminacion>*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp fechaCreacion = inscripcion.getFechaCreacion();


        if (fechaCreacion != null) {
            Secuencia secFechaInicio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), fechaCreacion.toString());
            secuenciaPrincipal.agregarSecuencia(secFechaInicio);


        } //        Timestamp fechaFin = inscripcion.getFechaTerminacion();
        //        if (fechaFin != null) {
        //            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), fechaFin.toString());
        //            secuenciaPrincipal.agregarSecuencia(secFechaFin);
        //        }
        /*
         *     <aprobadoSubarea>PENDIENTE</aprobadoSubarea>*/
        String nombreAprobSubarea = inscripcion.isAprobadoSubArea();


        if (nombreAprobSubarea != null) {
            Secuencia secAprobSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_SUBAREA), nombreAprobSubarea);
            secuenciaPrincipal.agregarSecuencia(secAprobSubarea);


        }


        String nombreSubarea = inscripcion.getSubareaInvestigacion().getNombreSubarea();
        if (nombreSubarea != null) {
            Secuencia secNombreSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_SUBAREA_INVESTIGACION), nombreSubarea);
            secuenciaPrincipal.agregarSecuencia(secNombreSubarea);
        }



        /*
         *     <aprobadoAsesor>TRUE</aprobadoAsesor>*/
        String nombreAprobAsesor = inscripcion.isAprobadoAsesor();


        if (nombreAprobAsesor != null) {
            Secuencia secAprobAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR), nombreAprobAsesor);
            secuenciaPrincipal.agregarSecuencia(secAprobAsesor);


        }
        String estadoSolicitud = inscripcion.getEstadoSolicitud();




        if (estadoSolicitud != null) {
            Secuencia secEstadoSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_SOLICITUD), estadoSolicitud);

            secuenciaPrincipal.agregarSecuencia(secEstadoSolicitud);


        }
        if (inscripcion.getEstudiante() != null) {
            Secuencia secEstudiante = pasarEstudianteaSecuencia(inscripcion.getEstudiante());
            secuenciaPrincipal.agregarSecuencia(secEstudiante);


        }

        if (inscripcion.getAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
            Secuencia secProfesor = pasarProfesorASecuencia(inscripcion.getAsesor());
            secAsesor.agregarSecuencia(secProfesor);
            secuenciaPrincipal.agregarSecuencia(secAsesor);


        }

//        if (inscripcion.getCoordinadorSubArea() != null) {
//            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COORDINADOR_SUBAREA), "");
//            Secuencia secProfesor = pasarProfesorASecuencia(inscripcion.getCoordinadorSubArea());
//            secAsesor.agregarSecuencia(secProfesor);
//            secuenciaPrincipal.agregarSecuencia(secAsesor);
//        }

        if (inscripcion.getSubareaInvestigacion() != null) {
            Secuencia secGrupoInvestigacion = pasarSubareaInvestigacionASecuencia(inscripcion.getSubareaInvestigacion());
            secuenciaPrincipal.agregarSecuencia(secGrupoInvestigacion);


        }
        if (inscripcion.getObligatorias() != null) {
            Secuencia secCursosObligatorios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OBLIGATORIOS), "");
            Secuencia secCursosObligatoriosLLenos = pasarCursosASecuencia(secCursosObligatorios, inscripcion.getObligatorias());
            secuenciaPrincipal.agregarSecuencia(secCursosObligatoriosLLenos);


        }
        if (inscripcion.getSubArea() != null) {
            Secuencia secCursosObligatorios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_SUBAREAS), "");
            Secuencia secCursosObligatoriosLLenos = pasarCursosASecuencia(secCursosObligatorios, inscripcion.getSubArea());
            secuenciaPrincipal.agregarSecuencia(secCursosObligatoriosLLenos);


        }
        if (inscripcion.getOtraSubArea() != null) {
            Secuencia secCursosObligatorios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OTRA_SUBAREA), "");
            Secuencia secCurso = pasarCursoASecuencia(inscripcion.getOtraSubArea());
            secCursosObligatorios.agregarSecuencia(secCurso);
            secuenciaPrincipal.agregarSecuencia(secCursosObligatorios);


        }

        if (inscripcion.getOtraMaestria() != null) {
            Secuencia secCursosObligatorios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OTRA_MAESTRIA), "");
            Secuencia secCurso = pasarCursoASecuencia(inscripcion.getOtraMaestria());
            secCursosObligatorios.agregarSecuencia(secCurso);
            secuenciaPrincipal.agregarSecuencia(secCursosObligatorios);


        }

        if (inscripcion.getNivelatorios() != null) {
            Secuencia secCursosObligatorios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_NIVELATORIOS), "");
            Secuencia secCursosObligatoriosLLenos = pasarCursosASecuencia(secCursosObligatorios, inscripcion.getNivelatorios());
            secuenciaPrincipal.agregarSecuencia(secCursosObligatoriosLLenos);


        }

        if (inscripcion.getSemestreInicioTesis1() != null) {
            Secuencia secInicioTesis1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_INICIO_TESIS1), inscripcion.getSemestreInicioTesis1().getPeriodo());
            secuenciaPrincipal.agregarSecuencia(secInicioTesis1);

        }

        if (inscripcion.getSemestreInicioTesis2() != null) {
            Secuencia secInicioTesis1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_INICIO_TESIS2), inscripcion.getSemestreInicioTesis2().getPeriodo());
            secuenciaPrincipal.agregarSecuencia(secInicioTesis1);
        }



        //        if (inscripcion.getSemestreIniciacion() != null) {
        //            Secuencia secSemestre = pasarSemestreASecuencia(inscripcion.getSemestreIniciacion());
        //            secuenciaPrincipal.agregarSecuencia(secSemestre);
        //        }
        return secuenciaPrincipal;


    }

    /**
     * metodo que  convierte un Estudiante a Secuencia
     * @param estudiante:Estudiante
     * @return Secuencia
     */
    public Secuencia pasarEstudianteaSecuencia(Estudiante estudiante) {

        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        Secuencia secIdGeneral = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), estudiante.getId().toString());
        secEstudiante.agregarSecuencia(secIdGeneral);

        Secuencia secNombreEstud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudiante.getPersona().getNombres());
        secEstudiante.agregarSecuencia(secNombreEstud);

        Secuencia secApellido = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudiante.getPersona().getApellidos());
        secEstudiante.agregarSecuencia(secApellido);

        Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudiante.getPersona().getCorreo());
        secEstudiante.agregarSecuencia(secCorreo);

        Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), estudiante.getPersona().getCodigo());
        secEstudiante.agregarSecuencia(secCodigo);


        return secEstudiante;



    }

    /**
     * metodo que  convierte un Profesor a Secuencia
     * @param profesor:Profesor
     * @return Secuencia
     */
    public Secuencia pasarProfesorASecuencia(Profesor profesor) {

        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
        Secuencia secIdGeneral = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), profesor.getId().toString());
        secEstudiante.agregarSecuencia(secIdGeneral);

        Secuencia secNombreEstud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), profesor.getPersona().getNombres());
        secEstudiante.agregarSecuencia(secNombreEstud);

        Secuencia secApellido = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), profesor.getPersona().getApellidos());
        secEstudiante.agregarSecuencia(secApellido);

        Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), profesor.getPersona().getCorreo());
        secEstudiante.agregarSecuencia(secCorreo);



        return secEstudiante;



    }

    /**
     * metodo que convierte una SubareaInvestigacion en secuencia
     * @param subGrupoInvestigacion:SubareaInvestigacion
     * @return Secuencia
     */
    public Secuencia pasarSubareaInvestigacionASecuencia(SubareaInvestigacion subGrupoInvestigacion) {

        Secuencia secGrupoInv = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), "");

        Secuencia secID = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO_INVESTIGACION), subGrupoInvestigacion.getId().toString());
        secGrupoInv.agregarSecuencia(secID);
        Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), subGrupoInvestigacion.getNombreSubarea());
        secGrupoInv.agregarSecuencia(secNombre);



        return secGrupoInv;


    }

    /**
     * metodo que convierte una coleccion de cursos a secuencia, y lo mete dentro de una secuencia que diga que tipo de cursos son
     * @param secCursosObligatorios: secuencia principal a la cual se van a agregar los cursos
     * @param obligatorias: Collection<CursoTesis>
     * @return Secuencia
     */
    public Secuencia pasarCursosASecuencia(Secuencia secCursosObligatorios, Collection<CursoTesis> obligatorias) {

        for (CursoTesis cursoTesis : obligatorias) {
            secCursosObligatorios.agregarSecuencia(pasarCursoASecuencia(cursoTesis));


        }
        return secCursosObligatorios;


    }

    /**
     * metodo que convierte un curso de maestria a secuencia
     * @param cursoMaestria
     * @return
     */
    public Secuencia pasarCursoASecuencia(CursoTesis otraSubArea) {

        Secuencia secCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
        Secuencia secID = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), otraSubArea.getId().toString());
        secCurso.agregarSecuencia(secID);


        if (otraSubArea.getCurso() == null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), otraSubArea.getNombre());
            secCurso.agregarSecuencia(secNombre);


        } else {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), otraSubArea.getCurso().getNombre());
            secCurso.agregarSecuencia(secNombre);
            Secuencia secdescrip = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), otraSubArea.getCurso().getDescricpion());
            secCurso.agregarSecuencia(secdescrip);
            Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), otraSubArea.getCurso().getCodigo());
            secCurso.agregarSecuencia(secCodigo);


        }

        String estado = otraSubArea.isVisto() ? getConstanteBean().getConstante(Constantes.TRUE) : getConstanteBean().getConstante(Constantes.FALSE);
        Secuencia secVisto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VISTO), estado);
        secCurso.agregarSecuencia(secVisto);
        Secuencia semestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), otraSubArea.getSemestre());
        secCurso.agregarSecuencia(semestre);


        return secCurso;


    }

    /**
     * metodo que recibe una secuencia y devuelbe una inscripcion de subarea
     * @param secuencia
     * @return InscripcionSubareaInvestigacion
     */
    public InscripcionSubareaInvestigacion pasarSecuenciaAInscripcionSubarea(Secuencia secuencia) { /*

         * <solicitud>
         *     <idgeneral>asfsdf<idGeneral>/**/

        /*
         * <solicitud>
         *     <idgeneral>asfsdf<idGeneral>/**/
        InscripcionSubareaInvestigacion inscripcion = new InscripcionSubareaInvestigacion();
        Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim()) : null;
        inscripcion.setId(id);
        /*
         *     <temaTesis>sadfdfadsf</temaTesis>*/
        String temaTesis = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS)) != null ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS)).getValor() : null;
        inscripcion.setTemaTesis(temaTesis);
        /*
         *     <fechaTerminacion>2010-12-12</fechaTerminacion>*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date fechaInicioDate = sdfHMS.parse(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN)).getValor());
//            Timestamp fec = new Timestamp(fechaInicioDate.getTime());
//            inscripcion.setFechaTerminacion(fec);
        inscripcion.setFechaCreacion(new Timestamp(new Date().getTime()));
        /*
         *     <aprobadoSubarea>PENDIENTE</aprobadoSubarea>*/
        String nombreAprobSubarea = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_SUBAREA)) != null ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_SUBAREA)).getValor() : null;
        inscripcion.setAprobadoSubArea(nombreAprobSubarea);

        /*
         *     <aprobadoAsesor>TRUE</aprobadoAsesor>*/
        String nombreAprobAsesor = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR)) != null
                ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR)).getValor() : null;
        inscripcion.setAprobadoAsesor(nombreAprobAsesor);
        /*
         *     <estadoSolicitud>CONSTANTE</estadoSolicitud>*/
        Secuencia secEstadoSolicitud = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_SOLICITUD));


        if (secEstadoSolicitud != null) {
            inscripcion.setEstadoSolicitud(secEstadoSolicitud.getValor());


        }

        Secuencia secEstudiante = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));


        if (secEstudiante != null) {
            inscripcion.setEstudiante(pasarSecuenciaAEstudiante(secEstudiante));


        } /*
         *     <asesor>
         *
         *     </asesor>*/
        Secuencia secAsesor = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));


        if (secAsesor != null) {
            Secuencia secProfesor = secAsesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
            inscripcion.setAsesor(pasarSecuenciaAProfesor(secProfesor));


        } /*
         *     <grupoInvestigcion>
         *         <idGrupo>234</idGrupo>
         *         <nombreGrupo>asdfasdfsdf</nombreGrupo>
         *     </grupoInvestigcion>*/
        Secuencia secGrupo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION));


        if (secGrupo != null) {
            SubareaInvestigacion g = pasarSecuenciaASubareaInvestigacion(secGrupo);
            inscripcion.setSubareaInvestigacion(g);


        }
        Secuencia secCursosObligatorios = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OBLIGATORIOS));


        if (secCursosObligatorios != null) {
            Collection<CursoTesis> cursos = pasarSecuenciaACursos(secCursosObligatorios.getSecuencias());
            inscripcion.setObligatorias(cursos);


        } else {
            Collection<CursoTesis> cursos = new ArrayList<CursoTesis>();
            inscripcion.setObligatorias(cursos);


        } /*
         *     <cursosObligatorios>
         *     </cursosObligatorios>*/
        Secuencia secCursossubArea = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_SUBAREAS));


        if (secCursossubArea != null) {
            Collection<CursoTesis> cursos = pasarSecuenciaACursos(secCursossubArea.getSecuencias());
            inscripcion.setSubArea(cursos);


        } else {
            Collection<CursoTesis> cursos = new ArrayList<CursoTesis>();
            inscripcion.setSubArea(cursos);


        } /*
         *     <cursosSubarea>
         *     </cursosSubarea>*/
        Secuencia secCursosOTRAsubArea = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OTRA_SUBAREA));


        if (secCursosOTRAsubArea != null) {
            Collection<CursoTesis> cursos = pasarSecuenciaACursos(secCursosOTRAsubArea.getSecuencias());
            inscripcion.setOtraSubArea(cursos.iterator().next());
        } /*
         *     <cursootraSubarea>
         *     </cursoOtraSubarea>*/


        Secuencia secCursosOTRAMaestria = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OTRA_MAESTRIA));
        if (secCursosOTRAMaestria != null) {
            Collection<CursoTesis> cursos = pasarSecuenciaACursos(secCursosOTRAMaestria.getSecuencias());
            inscripcion.setOtraMaestria(cursos.iterator().next());
        } /*
         *     <cursoOtraMaestria>
         *     </cursoOtraMaestria>*/
        Secuencia secCursosNivelatorios = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_NIVELATORIOS));


        if (secCursosNivelatorios != null) {
            Collection<CursoTesis> cursos = pasarSecuenciaACursos(secCursosNivelatorios.getSecuencias());
            inscripcion.setNivelatorios(cursos);


        } else {
            Collection<CursoTesis> cursos = new ArrayList<CursoTesis>();
            inscripcion.setNivelatorios(cursos);


        }

        //ACA COLOCAR NUEVOS ATRIBUTOS:
        Secuencia secInicioTesis1 = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_INICIO_TESIS1));
        if (secInicioTesis1 != null) {
            inscripcion.setSemestreInicioTesis1(pasarSecuenciaAPeriodo(secInicioTesis1));
        }

        Secuencia secInicioTesis2 = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_INICIO_TESIS2));
        if (secInicioTesis1 != null) {
            inscripcion.setSemestreInicioTesis2(pasarSecuenciaAPeriodo(secInicioTesis2));
        }


        /*
         *     <cursosNivelatorios>
         *     </cursosNivelatorios>*/ //        Secuencia secSemestre = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
        //        if (secSemestre != null) {
        //            PeriodoTesis sem = pasarSecuenciaAPeriodo(secSemestre);
        //            inscripcion.setSemestreIniciacion(sem);
        //        }

        return inscripcion;
    }

    /**
     * metodo que busca el estudiante en la BD
     * @param secEstudiante
     * @return
     */
    public Estudiante pasarSecuenciaAEstudiante(Secuencia secEstudiante) {
        Long id = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;


        if (id != null) {
            Estudiante e = estudianteFacadeRemote.find(id);


            return e;


        } else {
            String correo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)) != null
                    ? secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor().trim()
                    : null;



            if (correo != null) {
                Estudiante e = estudianteFacadeRemote.findByCorreo(correo);


                return e;


            }
            return null;


        }
    }

    /**
     * metodo que busca el profesor
     * @param secProfesor
     * @return
     */
    public Profesor pasarSecuenciaAProfesor(Secuencia secuencia) {

        Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                : null;


        if (id != null) {
            Profesor p = profesorFacade.find(id);
            return p;
        } else {
            String correo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)) != null
                    ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor().trim()
                    : null;
            if (correo != null) {
                Profesor p = profesorFacade.findByCorreo(correo);
                return p;
            }
            return null;
        }
    }

    /**
     * metodo que saca la secuencia del grupo
     * @param secGrupo
     * @return
     */
    public SubareaInvestigacion pasarSecuenciaASubareaInvestigacion(Secuencia secGrupo) {
        /*
         *  /*
         *     <grupoInvestigcion>
         *         <idGrupo>234</idGrupo>
         *         <nombreGrupo>asdfasdfsdf</nombreGrupo>
         *     </grupoInvestigcion>
         */
        Long id = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO_INVESTIGACION)) != null
                ? Long.parseLong(secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO_INVESTIGACION)).getValor().trim())
                : null;


        if (id != null) {
            SubareaInvestigacion grupo = subareaInvestigacionFacadeRemote.find(id);


            return grupo;


        } else {
            String nombreGrupo = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)) != null
                    ? secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor().trim()
                    : null;



            if (nombreGrupo != null) {
                SubareaInvestigacion g = subareaInvestigacionFacadeRemote.findByNombreSubarea(nombreGrupo);


                return g;


            }
            return null;


        }

    }

    /**
     * metodo que recibe una secuencia con cursos de tesis y la convierte en CursoTesis
     * @param cursos
     * @return Collection<CursoTesis>
     */
    public Collection<CursoTesis> pasarSecuenciaACursos(Collection<Secuencia> cursos) {

        Collection<CursoTesis> cursosEntities = new ArrayList<CursoTesis>();


        for (Secuencia secuencia : cursos) {

            CursoTesis curso = new CursoTesis();
            Long id = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null
                    ? Long.parseLong(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor().trim())
                    : null;
            curso.setId(id);

            String nombreCurso = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_MATERIA)) != null
                    ? secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_MATERIA)).getValor().trim()
                    : null;
            // curso.setNombre(nombreCurso);
            CursoMaestria cursoCurso = cursoMaestriaRemote.findByNombre(nombreCurso);


            if (cursoCurso != null) {
                curso.setCurso(cursoCurso);


            } else {
                curso.setNombre(nombreCurso);
                curso.setCurso(null);


            }
            Secuencia periodoSec = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO));
            if (periodoSec != null) {
                curso.setSemestre(periodoSec.getValor());


            } // falta el boolean
            Secuencia secVisto = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VISTO));

            if (secVisto != null) {
                boolean estado = secVisto.getValor().equals(getConstanteBean().getConstante(Constantes.TRUE)) ? true : false;
                curso.setVisto(estado);


            }
            cursosEntities.add(curso);



        }


        return cursosEntities;


    }

    /**
     * metodo que conviwerte una secuencia en periodo
     * @param secSemestre
     * @return
     */
    public PeriodoTesis pasarSecuenciaAPeriodo(Secuencia secSemestre) {
        // System.out.println("LLego al metodo :pasarSecuenciaAPeriodo ");
        String nombreSemestre = secSemestre.getValor();
        PeriodoTesis p = periodoFacadeRemote.findByPeriodo(nombreSemestre);


        if (p == null) {
            p = new PeriodoTesis();
            p.setPeriodo(nombreSemestre);
            p.setActual(false);
            periodoFacadeRemote.create(p);
            p = periodoFacadeRemote.findByPeriodo(nombreSemestre);
            // System.out.println("Lo creo y lo va a devolver...");
        }
        return p;
    }

    /**
     * metodo que convierte una secuencia en tesis 1
     * @param sec: Secuencia
     * @return  Tesis1
     */
    public Tesis1 pasarSecuenciaATesis1(Secuencia sec) {
        try {
            Tesis1 tesis = new Tesis1();
            Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));


            if (secId != null) {
                Long id = Long.parseLong(secId.getValor().trim());
                tesis.setId(id);


            }
            Secuencia secTemaTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));


            if (secTemaTesis != null) {
                tesis.setTemaTesis(secTemaTesis.getValor());


            }
            Secuencia secFechaFin = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



            if (secFechaFin != null) {

                Date d = sdfHMS.parse(secFechaFin.getValor());
                tesis.setFechaTerminacion(new Timestamp(d.getTime()));


            }
            tesis.setFechaCreacion(new Timestamp(new Date().getTime()));

            Secuencia SecEstudiante = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));


            if (SecEstudiante != null) {
                tesis.setEstudiante(pasarSecuenciaAEstudiante(SecEstudiante));


            }
            Secuencia secAsesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));


            if (secAsesor != null) {
                tesis.setAsesor(pasarSecuenciaAProfesor(secAsesor));


            }
            Secuencia secGrupoInvestigacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION));


            if (secGrupoInvestigacion != null) {
                tesis.setSubareaInvestigacion(pasarSecuenciaASubareaInvestigacion(secGrupoInvestigacion));


            }
            Secuencia secSemestre = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));


            if (secSemestre != null) {
                PeriodoTesis sem = pasarSecuenciaAPeriodo(secSemestre);
                tesis.setSemestreIniciacion(sem);


            }
            Secuencia secAprobadoasesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR));


            if (secAprobadoasesor != null) {
                tesis.setAprobadoAsesor(secAprobadoasesor.getValor().trim());


            }
            Secuencia secNotaTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));


            if (secNotaTesis != null) {
                tesis.setCalificacionTesis(secNotaTesis.getValor());


            }

            Secuencia secRutaArticulo = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA));
            if (secRutaArticulo != null) {
                tesis.setRutaArticuloTesis1(secRutaArticulo.getValor());
            }

            Secuencia secRutaCartaPend = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_CARTA_PENDIENTE_TESIS1));
            if (secRutaCartaPend != null) {
                tesis.setRutaCartaPendienteTesis1(secRutaCartaPend.getValor());
            }

            Secuencia secEstadoTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));
            if (secEstadoTesis != null) {
                tesis.setEstadoTesis(secEstadoTesis.getValor());
            }

            Secuencia secComentsExtemp = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_EXTEMPORAL));
            if (secComentsExtemp != null) {

                tesis.setComentsIngresoExtemporal(secComentsExtemp.getValor());
            }

            Secuencia secAprobarParaParadigma = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA));
            if (secAprobarParaParadigma != null) {
                tesis.setAprobadoParaParadigma(Boolean.parseBoolean(secAprobarParaParadigma.getValor()));
            }

            return tesis;

        } catch (ParseException ex) {
            Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    /**
     * metodo que convierte una secuencia de tesis1 a secuencias
     * @param tesises
     * @return
     */
    public Secuencia pasarTesises1ASecuencias(Collection<Tesis1> tesises) {
        Secuencia secTesises = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");


        for (Tesis1 tesis1 : tesises) {
            Secuencia secTesis1 = pasarTesis1ASecuencia(tesis1);
            secTesises.agregarSecuencia(secTesis1);


        }
        return secTesises;


    }

    /**
     * metodo que convierte una Tesis1 a Secuencia
     * @param tesis: Tesis1
     * @return Secuencia
     */
    public Secuencia pasarTesis1ASecuencia(Tesis1 tesis) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1), "");
        if (tesis == null) {
            return secPrincipal;
        }


        if (tesis.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
            secPrincipal.agregarSecuencia(secId);


        }
        if (tesis.getTemaTesis() != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTemaTesis());
            secPrincipal.agregarSecuencia(secTemaTesis);


        }

        if (tesis.getFechaTerminacion() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), tesis.getFechaTerminacion().toString());
            secPrincipal.agregarSecuencia(secFechaFin);


        }
        if (tesis.getFechaCreacion() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), tesis.getFechaCreacion().toString());
            secPrincipal.agregarSecuencia(secFechaFin);


        }

        if (tesis.getEstudiante() != null) {
            Secuencia SecEstudiante = pasarEstudianteaSecuencia(tesis.getEstudiante());
            secPrincipal.agregarSecuencia(SecEstudiante);


        }

        if (tesis.getAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
            secAsesor.agregarSecuencia(pasarProfesorASecuencia(tesis.getAsesor()));
            secPrincipal.agregarSecuencia(secAsesor);


        }

        if (tesis.getSubareaInvestigacion() != null) {
            secPrincipal.agregarSecuencia(pasarSubareaInvestigacionASecuencia(tesis.getSubareaInvestigacion()));


        }

        if (tesis.getSemestreIniciacion() != null) {
            Secuencia secSemestre = pasarSemestreASecuencia(tesis.getSemestreIniciacion());
            secPrincipal.agregarSecuencia(secSemestre);


        }

        if (tesis.isAprobadoAsesor() != null) {
            Secuencia secAprobadoasesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR), tesis.isAprobadoAsesor());
            secPrincipal.agregarSecuencia(secAprobadoasesor);


        }

        if (tesis.getCalificacionTesis() != null) {
            Secuencia secNotaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), tesis.getCalificacionTesis());
            secPrincipal.agregarSecuencia(secNotaTesis);


        }

        if (tesis.getRutaArticuloTesis1() != null) {
            Secuencia secRutaArticulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA), tesis.getRutaArticuloTesis1());
            secPrincipal.agregarSecuencia(secRutaArticulo);
        }

        if (tesis.getRutaCartaPendienteTesis1() != null) {
            Secuencia secRutaCartaPend = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO_CARTA_PENDIENTE_TESIS1), tesis.getRutaCartaPendienteTesis1());
            secPrincipal.agregarSecuencia(secRutaCartaPend);
        }

        if (tesis.getEstadoTesis() != null) {
            Secuencia secEstadoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis.getEstadoTesis());
            secPrincipal.agregarSecuencia(secEstadoTesis);
        }

        if (tesis.getComentsIngresoExtemporal() != null) {
            Secuencia secAprobadoasesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_EXTEMPORAL), tesis.getComentsIngresoExtemporal());
            secPrincipal.agregarSecuencia(secAprobadoasesor);
        }

        if (tesis.getAprobadoParaParadigma() != null) {
            Secuencia secAprobadoParadigma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA), tesis.getAprobadoParaParadigma().toString());
            secPrincipal.agregarSecuencia(secAprobadoParadigma);
        }

        return secPrincipal;


    }

    /**
     * metodo que convierte un semestreTesis a secuencia
     * @param semestreIniciacion: semestre
     * @return Secuencia
     */
    public Secuencia pasarSemestreASecuencia(PeriodoTesis semestreIniciacion) {
        Secuencia semestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), semestreIniciacion.getPeriodo());


        return semestre;


    }

    /**
     * metodo que verifica la existencia de un archivo en el servidor
     * @param ruta: ruta al archivo
     * @param valor: nombre del archivo
     * @return true si el archivo existe, false de lo contrario
     */
    public boolean comprobarExistenciaarchivo(String ruta, String nombreArchivo) {

        String rutaArchivo = ruta + nombreArchivo;
        File fArchivo = new File(rutaArchivo);

        if (fArchivo.exists()) {
            if (fArchivo.length() > 0) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * metodo que convierte una secuencia a Tesis2
     * @param secTesis:Secuencia
     * @return Tesis2
     */
    public Tesis2 pasarSecuenciaATesis2(Secuencia secTesis) {

        Tesis2 tesis = new Tesis2();
        Secuencia sec = secTesis;
        //-------------------------------------------------------------------------
        Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));


        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            tesis.setId(id);

        }
        Secuencia secTemaTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));


        if (secTemaTesis != null) {
            tesis.setTemaProyecto(secTemaTesis.getValor());


        }
        Secuencia secFechaFin = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm");



        if (secFechaFin != null) {
            try {
                Date d = sdfHMS.parse(secFechaFin.getValor());
                tesis.setFechaPrevistaTerminacion(new Timestamp(d.getTime()));



            } catch (ParseException ex) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
        tesis.setFecha(new Timestamp(new Date().getTime()));

        Secuencia SecEstudiante = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));


        if (SecEstudiante != null) {
            tesis.setEstudiante(pasarSecuenciaAEstudiante(SecEstudiante));


        }
        Secuencia secAsesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));


        if (secAsesor != null) {
            tesis.setAsesor(pasarSecuenciaAProfesor(secAsesor));


        }
        Secuencia secGrupoInvestigacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION));


        if (secGrupoInvestigacion != null) {
            tesis.setSubareaInvestigacion(pasarSecuenciaASubareaInvestigacion(secGrupoInvestigacion));


        }
        Secuencia secSemestre = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));


        if (secSemestre != null) {
            PeriodoTesis sem = pasarSecuenciaAPeriodo(secSemestre);
            tesis.setSemestreInicio(sem);


        }
        Secuencia secAprobadoasesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR));


        if (secAprobadoasesor != null) {
            tesis.setAprobadoAsesor(secAprobadoasesor.getValor().trim());


        }
        Secuencia secNotaTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));


        if (secNotaTesis != null) {
            tesis.setCalificacion(Double.parseDouble(secNotaTesis.getValor().trim()));


        }
        Secuencia secRutaArticulo = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA));


        if (secRutaArticulo != null) {
            tesis.setRutaArchivoAdjuntoInicioTesis(secRutaArticulo.getValor());


        }
        Secuencia secRutaArticuloFinal = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARTICULO_FIN_TESIS_2));


        if (secRutaArticuloFinal != null) {
            tesis.setRutaArticuloTesis(secRutaArticuloFinal.getValor());


        }
        Secuencia secEstadoTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));


        if (secEstadoTesis != null) {
            tesis.setEstadoTesis(secEstadoTesis.getValor());


        }
        Secuencia secJUradoTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADOS_TESIS));


        if (secJUradoTesis != null) {
            Tesis2 temp = pasarSecuenciaJuradosATesis(tesis, secJUradoTesis);

            tesis.setJuradoTesis(temp.getJuradoTesis());
            tesis.setJurados(temp.getJurados());


        }
        Secuencia secHorarioTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO_SUSTENTACION));


        if (secHorarioTesis != null) {
            HorarioSustentacionTesis h = pasarSecuenciaAHorario(secHorarioTesis);
            tesis.setHorarioSustentacion(h);


        } //--------------------------------------------------------------------------

        Secuencia secAprobadoHorarioAsesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_HORARIO_SUSTENTACION_TESIS2));


        if (secAprobadoHorarioAsesor != null) {
            tesis.setEstadoHorario(secAprobadoHorarioAsesor.getValor());
        }
        Secuencia secNotaSusten = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_SUSTENTACION_TESIS2));
        if (secNotaSusten != null) {
            tesis.setNotaSustentacion(Double.parseDouble(secNotaSusten.getValor()));
        }

        Secuencia secRutaArchivoPendiente = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_SOLICITUDUD_PENDIENTE_ESPECIAL));
        if (secRutaArchivoPendiente != null) {
            String ruta = secRutaArchivoPendiente.getValor();
            tesis.setRutaArchivoSolicitudPendienteEspecial(ruta);
        }
        Secuencia secCoasesores = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_COASESORES_TESIS_2));
        //la secuencia es = tagCoasesoresTesis2
        if (secCoasesores != null) {
            Collection<Coasesor> coasersores = pasarSecuenciaACoasesores(secCoasesores);
            tesis.setCoasesor(coasersores);
        }

        Secuencia secComentsExtemp = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_EXTEMPORAL));
        if (secComentsExtemp != null) {

            tesis.setComentsIngresoExtemporal(secComentsExtemp.getValor());
        }

        Secuencia secRutaExtempr = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_EXTEMPORAL));
        if (secRutaExtempr != null) {
            tesis.setRutaArchivoIngresoExtemporal(secRutaExtempr.getValor());
        }

        Secuencia secEnPendienteE = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_SI_ESTA_EN_PENDIENTE));
        if (secEnPendienteE != null) {
            tesis.setEstaEnPendienteEspecial(Boolean.parseBoolean(secEnPendienteE.getValor()));
        }

        Secuencia secAprobarParaParadigma = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA));
        if (secAprobarParaParadigma != null) {
            tesis.setAprobadoParaParadigma(Boolean.parseBoolean(secAprobarParaParadigma.getValor()));
        }

        //TAG_PARAM_SI_ESTA_EN_PENDIENTE


        return tesis;
    }

    public Tesis2 pasarSecuenciaJuradosATesis(Tesis2 tesis, Secuencia secJUradoTesis) {
        //ingresar todos los jurados a la tesis qe llega como parametro
        Secuencia secInternos = secJUradoTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADOS_TESIS_INTERNOS));

        if (secInternos != null) {
            Collection<Profesor> jurados = new ArrayList<Profesor>();
            Collection<Secuencia> secsProf = secInternos.getSecuencias();
            for (Secuencia secuencia : secsProf) {
                Profesor p = pasarSecuenciaAProfesor(secuencia);
                if (p != null) {
                    jurados.add(p);
                }
            }
            tesis.setJuradoTesis(jurados);
        }
        Secuencia secExternos = secJUradoTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADOS_TESIS_EXTERNOS));
        Collection<JuradoExternoUniversidad> juradosEX = new ArrayList<JuradoExternoUniversidad>();
        if (secExternos != null) {
            Collection<Secuencia> secsEXS = secExternos.getSecuencias();
            for (Secuencia secuencia : secsEXS) {
                JuradoExternoUniversidad p = pasarSecuenciaAJuradoExternoUniversidad(secuencia);
                juradosEX.add(p);
            }
        }
        tesis.setJurados(juradosEX);
        return tesis;

    }

    public HorarioSustentacionTesis pasarSecuenciaAHorario(Secuencia secH) {

        Secuencia secId = secH.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        HorarioSustentacionTesis h = new HorarioSustentacionTesis();


        if (secId != null) {
            h.setId(Long.parseLong(secId.getValor().trim()));


        }
        Secuencia secFS = secH.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SUSTENTACION));


        if (secFS != null) {
            try {
                SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdfHMS.parse(secFS.getValor().trim());
                h.setFechaSustentacion(new Timestamp(d.getTime()));




            } catch (ParseException ex) {
                Logger.getLogger(TesisBean.class.getName()).log(Level.SEVERE, null, ex);
                h.setFechaSustentacion(
                        null);
            }


        }
        Secuencia secVC = secH.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VIDEO_CONFERENCIA));


        if (secVC != null) {
            h.setVideoConferencia(secVC.getValor());


        }

        Secuencia secSKYP = secH.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SKYPE));


        if (secSKYP != null) {
            h.setVideoConferenciaSkyPe(secSKYP.getValor());


        }

        Secuencia secSalon = secH.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SALON_SUSTENTACION));


        if (secSalon != null) {
            h.setSalonSustentacion(secSalon.getValor());


        }
        return h;


    }

    public Secuencia pasarJuradoExternoASecuencia(JuradoExternoUniversidad juradoExternoUniversidad) {
        Secuencia secJurado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS_EXTERNO), "");


        if (juradoExternoUniversidad.getId() != null) {
            Secuencia secIdGeneral = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), juradoExternoUniversidad.getId().toString());
            secJurado.agregarSecuencia(secIdGeneral);


        }
        if (juradoExternoUniversidad.getNombres() != null) {
            Secuencia secNombreEstud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), juradoExternoUniversidad.getNombres());
            secJurado.agregarSecuencia(secNombreEstud);


        }
        if (juradoExternoUniversidad.getApellidos() != null) {
            Secuencia secApellido = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), juradoExternoUniversidad.getApellidos());
            secJurado.agregarSecuencia(secApellido);


        }
        if (juradoExternoUniversidad.getCorreo() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), juradoExternoUniversidad.getCorreo());
            secJurado.agregarSecuencia(secCorreo);
        }

        if (juradoExternoUniversidad.getOrganizacion() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EMPRESA), juradoExternoUniversidad.getOrganizacion());
            secJurado.agregarSecuencia(secCorreo);
        }
        if (juradoExternoUniversidad.getCargo() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGO), juradoExternoUniversidad.getCargo());
            secJurado.agregarSecuencia(secCorreo);
        }
        if (juradoExternoUniversidad.getTelefono() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO), juradoExternoUniversidad.getTelefono());
            secJurado.agregarSecuencia(secCorreo);
        }
        if (juradoExternoUniversidad.getDireccion() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION), juradoExternoUniversidad.getDireccion());
            secJurado.agregarSecuencia(secCorreo);
        }
        return secJurado;


    }

    public JuradoExternoUniversidad pasarSecuenciaAJuradoExternoUniversidad(Secuencia secuencia) {

        JuradoExternoUniversidad u = new JuradoExternoUniversidad();
        Secuencia sID = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (sID != null) {
            u.setId(Long.parseLong(sID.getValor().trim()));
        }
        Secuencia sName = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (sName != null) {
            u.setNombres(sName.getValor());
        }
        Secuencia sAp = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (sAp != null) {
            u.setApellidos(sAp.getValor());
        }
        Secuencia sMail = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (sMail != null) {
            u.setCorreo(sMail.getValor());
        }

        Secuencia secOrganizacion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EMPRESA));
        if (secOrganizacion != null) {
            u.setOrganizacion(secOrganizacion.getValor());
        }
        Secuencia secCargo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGO));
        if (secCargo != null) {
            u.setCargo(secCargo.getValor());
        }
        Secuencia secTel = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO));
        if (secTel != null) {
            u.setTelefono(secTel.getValor());
        }
        Secuencia secDireccion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION));
        if (secDireccion != null) {
            u.setDireccion(secDireccion.getValor());
        }
        return u;
    }

    public Secuencia pasarTesis2JuradosASecuencia(Tesis2 tesis) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADOS_TESIS), "");
        Secuencia secInternos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADOS_TESIS_INTERNOS), "");
        Collection<Profesor> juradosInternos = tesis.getJuradoTesis();


        for (Profesor elem : juradosInternos) {
            Secuencia secProfesor = pasarProfesorASecuencia(elem);
            secInternos.agregarSecuencia(secProfesor);


        }
        secPrincipal.agregarSecuencia(secInternos);

        Secuencia secExterno = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADOS_TESIS_EXTERNOS), "");
        Collection<JuradoExternoUniversidad> jurado = tesis.getJurados();


        for (JuradoExternoUniversidad juradoExternoUniversidad : jurado) {
            Secuencia secExto = pasarJuradoExternoASecuencia(juradoExternoUniversidad);
            secExterno.agregarSecuencia(secExto);


        }
        secPrincipal.agregarSecuencia(secExterno);



        return secPrincipal;



    }

    public Secuencia pasarHorarioSustentacionASecuencia(HorarioSustentacionTesis horarioSustentacion) {
        Secuencia secHorario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO_SUSTENTACION), "");



        if (horarioSustentacion.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), horarioSustentacion.getId().toString());
            secHorario.agregarSecuencia(secId);


        }
        if (horarioSustentacion.getFechaSustentacion() != null) {

            Date d = horarioSustentacion.getFechaSustentacion();
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SUSTENTACION), sdfHMS.format(d));
            secHorario.agregarSecuencia(sec);


        }
        if (horarioSustentacion.getVideoConferencia() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VIDEO_CONFERENCIA), horarioSustentacion.getVideoConferencia());
            secHorario.agregarSecuencia(sec);


        }
        if (horarioSustentacion.getVideoConferenciaSkyPe() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SKYPE), horarioSustentacion.getVideoConferenciaSkyPe());
            secHorario.agregarSecuencia(sec);


        }
        if (horarioSustentacion.getSalonSustentacion() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SALON_SUSTENTACION), horarioSustentacion.getSalonSustentacion());
            secHorario.agregarSecuencia(sec);


        }
        return secHorario;



    }

    /**
     * Método que convierte una colleccion de Tesis2 a Secuencias ligeras, solo
     * cargando la información básica.
     * @param tesises
     * @return
     */
    public Secuencia pasarTesises2ASecuenciaLigera(Collection<Tesis2> tesises) {
        //   asdfsadfsdaf
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");


        for (Tesis2 tesis2 : tesises) {
            Secuencia secTesis = pasarTesis2ASecuenciaLigera(tesis2);
            secPrincipal.agregarSecuencia(secTesis);


        }
        return secPrincipal;


    }

    /**
     * metodo que convierte una colleccion de Tesis2 a Secuencias
     * @param tesises
     * @return
     */
    public Secuencia pasarTesises2ASecuencia(Collection<Tesis2> tesises) {
        //   asdfsadfsdaf
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");

        for (Tesis2 tesis2 : tesises) {
            Secuencia secTesis = pasarTesis2ASecuencia(tesis2);
            secPrincipal.agregarSecuencia(secTesis);
        }
        return secPrincipal;
    }

    public Secuencia pasarTesises2ASecuenciaMinima(Collection<Tesis2> tesises) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");

        for (Tesis2 tesis2 : tesises) {
            Secuencia secTesis = pasarTesis2ASecuenciaLigera(tesis2);
            secPrincipal.agregarSecuencia(secTesis);
        }
        return secPrincipal;
    }

    /**
     * Metodo quie convierte una Tesis2 cargando solo la información básica
     * @param tesis:Tesis2
     * @return Secuencia
     */
    public Secuencia pasarTesis2ASecuenciaLigera(Tesis2 tesis) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2), "");

        if (tesis != null) {
            if (tesis.getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
                secPrincipal.agregarSecuencia(secId);
            }
            if (tesis.getTemaProyecto() != null) {
                Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTemaProyecto());
                secPrincipal.agregarSecuencia(secTemaTesis);
            }

            if (tesis.getFechaPrevistaTerminacion() != null) {
                Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), tesis.getFechaPrevistaTerminacion().toString());
                secPrincipal.agregarSecuencia(secFechaFin);
            }
            if (tesis.getFecha() != null) {
                Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), tesis.getFecha().toString());
                secPrincipal.agregarSecuencia(secFechaFin);
            }

            if (tesis.getEstudiante() != null) {
                Secuencia SecEstudiante = pasarEstudianteaSecuencia(tesis.getEstudiante());
                secPrincipal.agregarSecuencia(SecEstudiante);
            }

            if (tesis.getAsesor() != null) {
                Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
                secAsesor.agregarSecuencia(pasarProfesorASecuencia(tesis.getAsesor()));
                secPrincipal.agregarSecuencia(secAsesor);
            }



            if (tesis.getSemestreInicio() != null) {
                Secuencia secSemestre = pasarSemestreASecuencia(tesis.getSemestreInicio());
                secPrincipal.agregarSecuencia(secSemestre);
            }

            if (tesis.getAprobadoAsesor() != null) {
                Secuencia secAprobadoasesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR), tesis.getAprobadoAsesor());
                secPrincipal.agregarSecuencia(secAprobadoasesor);
            }

            if (tesis.getCalificacion() != null) {
                Secuencia secNotaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), String.valueOf(tesis.getCalificacion()));
                secPrincipal.agregarSecuencia(secNotaTesis);
            }

            if (tesis.getEstadoTesis() != null) {
                Secuencia secEstadoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis.getEstadoTesis());
                secPrincipal.agregarSecuencia(secEstadoTesis);
            }

            if (tesis.getHorarioSustentacion() != null) {
                Secuencia secHorarioSustentacion = pasarHorarioSustentacionASecuencia(tesis.getHorarioSustentacion());
                secPrincipal.agregarSecuencia(secHorarioSustentacion);
            }

            if (tesis.getAprobadoParaParadigma() != null) {
                Secuencia secAprobadoParadigma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA), tesis.getAprobadoParaParadigma().toString());
                secPrincipal.agregarSecuencia(secAprobadoParadigma);
            }


            Secuencia secEnPendienteEspecial = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SI_ESTA_EN_PENDIENTE), tesis.getEstaEnPendienteEspecial() != null ? tesis.getEstaEnPendienteEspecial().toString() : Boolean.FALSE.toString());
            secPrincipal.agregarSecuencia(secEnPendienteEspecial);

        }


        return secPrincipal;
    }

    /**
     * metodo que convieret una Tesis2 a Secuencia
     * @param tesis:Tesis2
     * @return Secuencia
     */
    public Secuencia pasarTesis2ASecuencia(Tesis2 tesis) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2), "");

        if (tesis != null) {
            if (tesis.getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
                secPrincipal.agregarSecuencia(secId);
            }
            if (tesis.getTemaProyecto() != null) {
                Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTemaProyecto());
                secPrincipal.agregarSecuencia(secTemaTesis);
            }

            if (tesis.getFechaPrevistaTerminacion() != null) {
                Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), tesis.getFechaPrevistaTerminacion().toString());
                secPrincipal.agregarSecuencia(secFechaFin);
            }
            if (tesis.getFecha() != null) {
                Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), tesis.getFecha().toString());
                secPrincipal.agregarSecuencia(secFechaFin);
            }

            if (tesis.getEstudiante() != null) {
                Secuencia SecEstudiante = pasarEstudianteaSecuencia(tesis.getEstudiante());
                secPrincipal.agregarSecuencia(SecEstudiante);
            }

            if (tesis.getAsesor() != null) {
                Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
                secAsesor.agregarSecuencia(pasarProfesorASecuencia(tesis.getAsesor()));
                secPrincipal.agregarSecuencia(secAsesor);
            }

            if (tesis.getSubareaInvestigacion() != null) {
                secPrincipal.agregarSecuencia(pasarSubareaInvestigacionASecuencia(tesis.getSubareaInvestigacion()));
            }

            if (tesis.getSemestreInicio() != null) {
                Secuencia secSemestre = pasarSemestreASecuencia(tesis.getSemestreInicio());
                secPrincipal.agregarSecuencia(secSemestre);
            }

            if (tesis.getAprobadoAsesor() != null) {
                Secuencia secAprobadoasesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR), tesis.getAprobadoAsesor());
                secPrincipal.agregarSecuencia(secAprobadoasesor);
            }

            if (tesis.getCalificacion() != null) {
                Secuencia secNotaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), String.valueOf(tesis.getCalificacion()));
                secPrincipal.agregarSecuencia(secNotaTesis);


            }

            if (tesis.getRutaArchivoAdjuntoInicioTesis() != null) {
                Secuencia secRutaArticulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA), tesis.getRutaArchivoAdjuntoInicioTesis());
                secPrincipal.agregarSecuencia(secRutaArticulo);


            }
            if (tesis.getRutaArticuloTesis() != null) {
                Secuencia secRutaArticulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARTICULO_FIN_TESIS_2), tesis.getRutaArticuloTesis());
                secPrincipal.agregarSecuencia(secRutaArticulo);


            }

            if (tesis.getEstadoTesis() != null) {
                Secuencia secEstadoTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis.getEstadoTesis());
                secPrincipal.agregarSecuencia(secEstadoTesis);
            }
            if (tesis.getJuradoTesis() != null || tesis.getJurados() != null) {
                Secuencia secJurados = pasarTesis2JuradosASecuencia(tesis);
                secPrincipal.agregarSecuencia(secJurados);
            }
            if (tesis.getHorarioSustentacion() != null) {
                Secuencia secHorarioSustentacion = pasarHorarioSustentacionASecuencia(tesis.getHorarioSustentacion());
                secPrincipal.agregarSecuencia(secHorarioSustentacion);
            }
            if (tesis.getEstadoHorario() != null) {
                Secuencia secHorarioAprobado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_HORARIO_SUSTENTACION_TESIS2), tesis.getEstadoHorario().toString());// = pasarHorarioSustentacionASecuencia(tesis.getHorarioAprobadoPorAsesor());
                secPrincipal.agregarSecuencia(secHorarioAprobado);
            }
            if (tesis.getCalificacionesJurados() != null) {
                Secuencia secCalificacionesJurados = pasarCalificacionesJuradosASecuencia(tesis.getCalificacionesJurados());
                secPrincipal.agregarSecuencia(secCalificacionesJurados);
            }
            if (tesis.getNotaSustentacion() != null) {
                Secuencia secNotaSusten = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_SUSTENTACION_TESIS2), tesis.getNotaSustentacion().toString());
                secPrincipal.agregarSecuencia(secNotaSusten);
            }//rutaArchivoSolicitudPendienteEspecial
            if (tesis.getRutaArchivoSolicitudPendienteEspecial() != null) {
                Secuencia secRutaArchivoPendienteEspecial = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_SOLICITUDUD_PENDIENTE_ESPECIAL), tesis.getRutaArchivoSolicitudPendienteEspecial().toString());
                secPrincipal.agregarSecuencia(secRutaArchivoPendienteEspecial);
            }
            //Secuencia secCoasesores = sec.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_COASESORES_TESIS_2));
            if (tesis.getCoasesor() != null) {
                Secuencia secCoasesores = pasarCoasesoresASecuencia(tesis.getCoasesor());
                secPrincipal.agregarSecuencia(secCoasesores);
            }

            if (tesis.getComentsIngresoExtemporal() != null) {
                Secuencia secAprobadoasesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_EXTEMPORAL), tesis.getComentsIngresoExtemporal());
                secPrincipal.agregarSecuencia(secAprobadoasesor);
            }

            if (tesis.getRutaArchivoIngresoExtemporal() != null) {
                Secuencia secAprobadoasesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_EXTEMPORAL), tesis.getRutaArchivoIngresoExtemporal());
                secPrincipal.agregarSecuencia(secAprobadoasesor);
            }

            if (tesis.getAprobadoParaParadigma() != null) {
                Secuencia secAprobadoParadigma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA), tesis.getAprobadoParaParadigma().toString());
                secPrincipal.agregarSecuencia(secAprobadoParadigma);
            }

            Secuencia secEnPendienteEspecial = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SI_ESTA_EN_PENDIENTE), tesis.getEstaEnPendienteEspecial() != null ? tesis.getEstaEnPendienteEspecial().toString() : Boolean.FALSE.toString());
            secPrincipal.agregarSecuencia(secEnPendienteEspecial);
        }


        return secPrincipal;


    }

    public Secuencia pasarPeriodosASecuencia(Collection<PeriodoTesis> periodods) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRES), "");


        for (PeriodoTesis periodoTesis : periodods) {
            Secuencia secPeriodo = pasarPeriodoASecuencia(periodoTesis);
            secPrincipal.agregarSecuencia(secPeriodo);


        }
        return secPrincipal;


    }

    public Secuencia pasarPeriodoASecuencia(PeriodoTesis periodoTesis) {
        Secuencia secPeriodo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), periodoTesis.getPeriodo());


        return secPeriodo;


    }

    public ComentarioTesis pasarSecuenciaAComentarioTesis(Secuencia secComentarioTesis) {
        Secuencia secId = secComentarioTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        ComentarioTesis coment = new ComentarioTesis();
        if (secId != null) {
            coment.setId(Long.parseLong(secId.getValor().trim()));
        }
        Secuencia secTxt = secComentarioTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO));
        if (secTxt != null) {
            coment.setComentario(secTxt.getValor());
        }
        Secuencia secDebeRetirar = secComentarioTesis.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DEBE_RETIRAR));
        if (secDebeRetirar != null) {
            Boolean resp = Boolean.parseBoolean(secDebeRetirar.getValor());
            coment.setDebeRetirar(resp);
        }
        return coment;

    }

    public Secuencia pasarComentariosTesisASecuencia(Collection<ComentarioTesis> coments) {
        Secuencia secComentarios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_TESIS), null);
        for (ComentarioTesis comentarioTesis : coments) {
            Secuencia secComent = pasarComentarioTesisASecuencia(comentarioTesis);
            secComentarios.agregarSecuencia(secComent);
        }
        return secComentarios;
    }

    public Secuencia pasarComentarioTesisASecuencia(ComentarioTesis coments) {
        Secuencia secComentario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO_TESIS), null);
        if (coments.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), coments.getId().toString());
            secComentario.agregarSecuencia(secId);
        }

        if (coments.getComentario() != null) {
            Secuencia secTxt = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO), coments.getComentario());
            secComentario.agregarSecuencia(secTxt);
        }

        if (coments.getDebeRetirar() != null) {
            Secuencia secDebeRetirar = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DEBE_RETIRAR), coments.getDebeRetirar().toString().toUpperCase());
            secComentario.agregarSecuencia(secDebeRetirar);
        }
        if (coments.getFecha() != null) {
            Secuencia secfecha = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), coments.getFecha().toString());
            secComentario.agregarSecuencia(secfecha);
        }
        return secComentario;
    }

    public Secuencia pasarCalificacionesJuradosASecuencia(Collection<CalificacionJurado> calificacionesJurados) {
        Secuencia secCalificaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACIONES_JURADOS), null);
        for (CalificacionJurado elem : calificacionesJurados) {
            Secuencia secCalJurado = pasarCalificacionJuradoASecuencia(elem);
            secCalificaciones.agregarSecuencia(secCalJurado);
        }
        return secCalificaciones;
    }

    public Secuencia pasarCalificacionJuradoASecuencia(CalificacionJurado elem) {

        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secCalificacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_JURADO), null);

        if (elem.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), elem.getId().toString());
            secCalificacion.agregarSecuencia(secId);
        }
        if (elem.getHash() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_CALIFICACION), elem.getHash());
            secCalificacion.agregarSecuencia(secId);
        }
        if (elem.getFechaCalificacion() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ACTUALIZACION), sdfHMS.format(elem.getFechaCalificacion()));
            secCalificacion.agregarSecuencia(secId);
        }
        if (elem.getCancelada() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANCELADA), elem.getCancelada().toString());
            secCalificacion.agregarSecuencia(secId);
        }

        if (elem.getJuradoExterno() != null) {
            Secuencia secId = pasarJuradoExternoASecuencia(elem.getJuradoExterno());
            secCalificacion.agregarSecuencia(secId);
        }

        if (elem.getJuradoInterno() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS_INTERNO), null);
            secId.agregarSecuencia(pasarProfesorASecuencia(elem.getJuradoInterno()));
            secCalificacion.agregarSecuencia(secId);
        }
        if (elem.getNotaJurado() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), elem.getNotaJurado().toString());
            secCalificacion.agregarSecuencia(secId);
        }
        if (elem.getCategoriaCriteriosJurado() != null) {

            Collection<CategoriaCriterioJurado> catCriterios = elem.getCategoriaCriteriosJurado();
            Secuencia secCategoriasCriterios = pasarCategoriasCriteriosJuradoASecuencia(catCriterios);
            secCalificacion.agregarSecuencia(secCategoriasCriterios);
            //--------------------
        }
        if (elem.getTerminado() != null) {
            String isTerminado = (elem.getTerminado()) ? getConstanteBean().getConstante(Constantes.TRUE) : getConstanteBean().getConstante(Constantes.FALSE);
            Secuencia secTerminado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TERINADO), isTerminado);
            secCalificacion.agregarSecuencia(secTerminado);
        }
        if (elem.getRolJurado() != null) {
            Secuencia secTerminado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL), elem.getRolJurado());
            secCalificacion.agregarSecuencia(secTerminado);
        }
        if (elem.getCoasesor() != null) {
            Secuencia secTerminado = pasarCoasesorASecuencia(elem.getCoasesor());
            secCalificacion.agregarSecuencia(secTerminado);
        }
        return secCalificacion;
    }

    public Secuencia pasarCalificacionesCriteriosASecuencia(Collection<CalificacionCriterio> calCriterios) {
        Secuencia secP = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_CRITERIOS), null);
        for (CalificacionCriterio elem : calCriterios) {
            Secuencia sec = pasarCalificacionCriterioASecuencia(elem);
            secP.agregarSecuencia(sec);
        }
        return secP;

    }

    public Secuencia pasarCalificacionCriterioASecuencia(CalificacionCriterio elem) {
        Secuencia secCriterio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_CRITERIO), null);

        if (elem.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), elem.getId().toString());
            secCriterio.agregarSecuencia(secId);
        }

        if (elem.getValor() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), elem.getValor().toString());
            secCriterio.agregarSecuencia(secId);
        }
        if (elem.getNombre() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), elem.getNombre());
            secCriterio.agregarSecuencia(secId);
        }
        if (elem.getDescripcion() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), elem.getDescripcion());
            secCriterio.agregarSecuencia(secId);
        }
        if (elem.getPeso() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PESO), elem.getPeso().toString());
            secCriterio.agregarSecuencia(secId);
        }

        if (elem.getOrdenCriterio() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ORDEN_CRITERIO), elem.getOrdenCriterio().toString());
            secCriterio.agregarSecuencia(secId);
        }
        if (elem.getComentario() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO), elem.getComentario());
            secCriterio.agregarSecuencia(secId);
        }

        return secCriterio;
    }

    public CalificacionJurado pasarSecuenciaACalificacionJurado(Secuencia s) {

        CalificacionJurado c = new CalificacionJurado();
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Secuencia secCalificacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_JURADO), null);

        Secuencia sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

        if (sec != null) {
            c.setId(Long.parseLong(sec.getValor()));
        }
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_CALIFICACION));
        if (sec != null) {
            c.setHash(sec.getValor());
        }


        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS_EXTERNO));
        if (sec != null) {
            JuradoExternoUniversidad califics = pasarSecuenciaAJuradoExternoUniversidad(sec);
            c.setJuradoExterno(califics);
        }

        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS_INTERNO));
        if (sec != null) {
            Profesor p = pasarSecuenciaAProfesor(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR)));
            c.setJuradoInterno(p);
        }

        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
        if (sec != null) {
            c.setNotaJurado(Double.parseDouble(sec.getValor()));
        }

        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_TERINADO));
        if (sec != null) {
            c.setTerminado(Boolean.parseBoolean(sec.getValor()));
        }

        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL));
        if (sec != null) {
            c.setRolJurado(sec.getValor());
        }

        //  sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_CRITERIOS));
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIAS_CRITERIOS_TESIS_2));
        if (sec != null) {
            // aca en adelante corregir
            Collection<CategoriaCriterioJurado> califics = pasarSecuenciaACategoriasCriterios(sec);
            c.setCategoriaCriteriosJurado(califics);
        }
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_COASESOR));
        if (sec != null) {
            // aca en adelante corregir
            Coasesor coasesor = pasarSecuenciaACoasesor(sec);
            c.setCoasesor(coasesor);
        }
        return c;
    }

    public Collection<CalificacionCriterio> pasarSecuenciaACalificacionCriterios(Secuencia sec) {
        Collection<CalificacionCriterio> cals = new ArrayList<CalificacionCriterio>();
        Collection<Secuencia> secs = sec.getSecuencias();
        for (Secuencia secuencia : secs) {
            CalificacionCriterio c = pasarSecuenciaACalificacioCriterio(secuencia);
            cals.add(c);
        }
        return cals;
    }

    public CalificacionCriterio pasarSecuenciaACalificacioCriterio(Secuencia s) {

        CalificacionCriterio c = new CalificacionCriterio();

        Secuencia sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

        if (sec != null) {
            c.setId(Long.parseLong(sec.getValor()));
        }
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO));
        if (sec != null) {
            c.setComentario(sec.getValor());
        }
        //-------------------
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR));
        if (sec != null) {
            c.setValor(Double.parseDouble(sec.getValor()));
        }
        //-----------------------------------------------------
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (sec != null) {
            c.setNombre(sec.getValor());
        }

        //-------------------------------
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
        if (sec != null) {
            c.setDescripcion(sec.getValor());
        }
        //------------------------------
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_PESO));
        if (sec != null) {
            c.setPeso(Double.parseDouble(sec.getValor()));
        }
//--------------------

        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ORDEN_CRITERIO));
        if (sec != null) {
            c.setOrdenCriterio(Integer.parseInt(sec.getValor()));
        }
        return c;
    }

    public Collection<CategoriaCriterioJurado> pasarSecuenciaACategoriasCriterios(Secuencia sec) {
        Collection<Secuencia> secuencias = sec.getSecuencias();
        Collection<CategoriaCriterioJurado> categorias = new ArrayList();
        for (Secuencia secs : secuencias) {
            CategoriaCriterioJurado cat = pasarSecuenciaACategoriasriterio(secs);
            categorias.add(cat);
        }
        return categorias;
    }

    public CategoriaCriterioJurado pasarSecuenciaACategoriasriterio(Secuencia s) {
        CategoriaCriterioJurado c = new CategoriaCriterioJurado();
        Secuencia sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (sec != null) {
            c.setId(Long.parseLong(sec.getValor()));
        }
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO));
        if (sec != null) {
            c.setComentario(sec.getValor());
        }
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CATEGORIA));
        if (sec != null) {
            c.setNombreCategoriaCriterio(sec.getValor());
        }//---------------------------------
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ORDEN_CATEGORIA));
        if (sec != null) {
            c.setOrdenCategoria(Integer.parseInt(sec.getValor()));
        }
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
        if (sec != null) {
            c.setDescripcion(sec.getValor());
        }
        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_PORCENTAJE_CATEGORIA));
        if (sec != null) {
            c.setPorcentajeCategoria(Double.parseDouble(sec.getValor().trim()));
        }
        Collection<CalificacionCriterio> criterios = new ArrayList<CalificacionCriterio>();

        sec = s.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_CRITERIOS));
        if (sec != null) {
            criterios = pasarSecuenciaACalificacionCriterios(sec);
        }
        c.setCalCriterios(criterios);
        return c;


    }

    public Secuencia pasarCategoriasCriteriosJuradoASecuencia(Collection<CategoriaCriterioJurado> catCriterios) {

        Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIAS_CRITERIOS_TESIS_2), null);
        for (CategoriaCriterioJurado categoriaCriterioJurado : catCriterios) {
            Secuencia secCategoria = pasarCategoriaCriterioJuradoASecuencia(categoriaCriterioJurado);
            sec.agregarSecuencia(secCategoria);
        }
        return sec;
    }

    public Secuencia pasarCategoriaCriterioJuradoASecuencia(CategoriaCriterioJurado elem) {
        //Secuencia secCategoriaCriterioJurado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIA_CRITERIO_TESIS_2), null);
        Secuencia secCriterio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIA_CRITERIO_TESIS_2), null);



        if (elem.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), elem.getId().toString());
            secCriterio.agregarSecuencia(secId);
        }
        if (elem.getComentario() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO), elem.getComentario());
            secCriterio.agregarSecuencia(secId);
        }

        if (elem.getDescripcion() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), elem.getDescripcion());
            secCriterio.agregarSecuencia(secId);
        }

        if (elem.getNombreCategoriaCriterio() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CATEGORIA), elem.getNombreCategoriaCriterio());
            secCriterio.agregarSecuencia(secId);
        }
        if (elem.getOrdenCategoria() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ORDEN_CATEGORIA), elem.getOrdenCategoria().toString());
            secCriterio.agregarSecuencia(secId);
        }

        if (elem.getPorcentajeCategoria() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PORCENTAJE_CATEGORIA), elem.getPorcentajeCategoria().toString());
            secCriterio.agregarSecuencia(secId);
        }

        if (elem.getCalCriterios() != null) {
            Secuencia secCalCriterios = pasarCalificacionesCriteriosASecuencia(elem.getCalCriterios());
            secCriterio.agregarSecuencia(secCalCriterios);
        }
        return secCriterio;

    }

    public Collection<Coasesor> pasarSecuenciaACoasesores(Secuencia secCoasesores) {
        Collection<Secuencia> secs = secCoasesores.getSecuencias();
        //recibe las secuencias coasesor
        Collection<Coasesor> coas = new ArrayList<Coasesor>();
        for (Secuencia secuencia : secs) {
            System.out.println("Paso la secuencia = " + secuencia.getNombre());
            if (secuencia.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_COASESOR))) {
                Coasesor c = pasarSecuenciaACoasesor(secuencia);
                coas.add(c);
            }

        }
        return coas;
    }

    public Coasesor pasarSecuenciaACoasesor(Secuencia secuencia) {
        Coasesor c = new Coasesor();

        Secuencia sID = secuencia.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (sID != null) {
            c.setId(Long.parseLong(sID.getValor().trim()));
        }
        Secuencia interna = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
        if (interna != null) {
            System.out.println("entro a profesor -->" + interna.getNombre());
            Profesor p = pasarSecuenciaAProfesor(interna);
            c.setCoasesor(p);
            System.out.println("a el coasesor le agrego el profesor =" + (p.getId() != null ? p.getId().toString() : "null"));
            c.setCorreo(p.getPersona().getCorreo());
            c.setInterno(Boolean.TRUE);

        } else {
            c.setInterno(Boolean.FALSE);
            Secuencia sName = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
            if (sName != null) {
                c.setNombres(sName.getValor());
            }
            Secuencia sAp = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
            if (sAp != null) {
                c.setApellidos(sAp.getValor());
            }
            Secuencia sMail = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (sMail != null) {
                c.setCorreo(sMail.getValor());
            }

            Secuencia secOrganizacion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EMPRESA));
            if (secOrganizacion != null) {
                c.setOrganizacion(secOrganizacion.getValor());
            }
            Secuencia secCargo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGO));
            if (secCargo != null) {
                c.setCargo(secCargo.getValor());
            }
            Secuencia secTel = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO));
            if (secTel != null) {
                c.setTelefono(secTel.getValor());
            }
            Secuencia secDireccion = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION));
            if (secDireccion != null) {
                c.setDireccion(secDireccion.getValor());
            }

        }
        return c;
    }

    public Secuencia pasarCoasesoresASecuencia(Collection<Coasesor> coasesor) {
        Secuencia secCoasesores = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COASESORES_TESIS_2), null);
        for (Coasesor coasesor1 : coasesor) {
            Secuencia secCoa = pasarCoasesorASecuencia(coasesor1);
            secCoasesores.agregarSecuencia(secCoa);
        }
        return secCoasesores;
    }

    public Secuencia pasarCoasesorASecuencia(Coasesor coasesor1) {

        System.out.println("va a pasar coasesor a secuencia -----------------------------------------------------------");
        Secuencia secPrincipalCoasesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COASESOR), null);
        if (coasesor1.getId() != null) {
            Secuencia secIdGeneral = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), coasesor1.getId().toString());
            secPrincipalCoasesor.agregarSecuencia(secIdGeneral);
        }
        System.out.println("------------- el coasesor es externo" + coasesor1.getInterno());
        if (coasesor1.getInterno() != null && coasesor1.getInterno()) {
            //asesor interno
            System.out.println("es un coasesor externo y agrego el profesor");
            Secuencia secProfesor = pasarProfesorASecuencia(coasesor1.getCoasesor());
            secPrincipalCoasesor.agregarSecuencia(secProfesor);
        } else {
            //asesor externo
            if (coasesor1.getNombres() != null) {
                Secuencia secNombreEstud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), coasesor1.getNombres());
                secPrincipalCoasesor.agregarSecuencia(secNombreEstud);
            }
            if (coasesor1.getApellidos() != null) {
                Secuencia secApellido = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), coasesor1.getApellidos());
                secPrincipalCoasesor.agregarSecuencia(secApellido);
            }
            if (coasesor1.getCorreo() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), coasesor1.getCorreo());
                secPrincipalCoasesor.agregarSecuencia(secCorreo);
            }

            if (coasesor1.getOrganizacion() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EMPRESA), coasesor1.getOrganizacion());
                secPrincipalCoasesor.agregarSecuencia(secCorreo);
            }
            if (coasesor1.getCargo() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGO), coasesor1.getCargo());
                secPrincipalCoasesor.agregarSecuencia(secCorreo);
            }
            if (coasesor1.getTelefono() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO), coasesor1.getTelefono());
                secPrincipalCoasesor.agregarSecuencia(secCorreo);
            }
            if (coasesor1.getDireccion() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION), coasesor1.getDireccion());
                secPrincipalCoasesor.agregarSecuencia(secCorreo);
            }
        }

        return secPrincipalCoasesor;

    }

    public Secuencia pasarInscripcionesSubareaASecuenciaLigero(Collection<InscripcionSubareaInvestigacion> ins) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), "");

        System.out.println("\n\n\n\n\n\n\n\nYEah \n\n\n\n\n\n\n\n");
        if (ins != null) {
            for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : ins) {
                Secuencia sec = pasarInscripcionSubareaASecuenciaLigero(inscripcionSubareaInvestigacion);
                secPrincipal.agregarSecuencia(sec);


            }
        }
        return secPrincipal;


    }

    /**
     * metodo que convierte una inscripcion a subarea a Secuencia
     * @param inscripcion: InscripcionSubareaInvestigacion
     * @return Secuencia
     */
    public Secuencia pasarInscripcionSubareaASecuenciaLigero(InscripcionSubareaInvestigacion inscripcion) {
        Secuencia secuenciaPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), "");
        if (inscripcion == null) {
            return secuenciaPrincipal;
        }
        // Collection<Secuencia>secsDePrincipal= new ArrayList<Secuencia>();


        Long id = inscripcion.getId();


        if (id != null) {
            secuenciaPrincipal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), inscripcion.getId().toString()));


        } /*
         *     <temaTesis>sadfdfadsf</temaTesis>*/
        String temaTesis = inscripcion.getTemaTesis();


        if (temaTesis != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), temaTesis);
            secuenciaPrincipal.agregarSecuencia(secTemaTesis);


        }
        /*
         *     <fechaTerminacion>2010-12-12</fechaTerminacion>*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp fechaCreacion = inscripcion.getFechaCreacion();


        if (fechaCreacion != null) {
            Secuencia secFechaInicio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), fechaCreacion.toString());
            secuenciaPrincipal.agregarSecuencia(secFechaInicio);


        }
        String nombreAprobSubarea = inscripcion.isAprobadoSubArea();


        if (nombreAprobSubarea != null) {
            Secuencia secAprobSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_SUBAREA), nombreAprobSubarea);
            secuenciaPrincipal.agregarSecuencia(secAprobSubarea);


        }

        String estadoSolicitud = inscripcion.getEstadoSolicitud();

        if (estadoSolicitud != null) {
            Secuencia secEstadoSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_SOLICITUD), estadoSolicitud);

            secuenciaPrincipal.agregarSecuencia(secEstadoSolicitud);


        }
        if (inscripcion.getEstudiante() != null) {
            Secuencia secEstudiante = pasarEstudianteaSecuencia(inscripcion.getEstudiante());
            secuenciaPrincipal.agregarSecuencia(secEstudiante);

        }

        if (inscripcion.getAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
            Secuencia secProfesor = pasarProfesorASecuencia(inscripcion.getAsesor());
            secAsesor.agregarSecuencia(secProfesor);
            secuenciaPrincipal.agregarSecuencia(secAsesor);


        }
        return secuenciaPrincipal;


    }
}
