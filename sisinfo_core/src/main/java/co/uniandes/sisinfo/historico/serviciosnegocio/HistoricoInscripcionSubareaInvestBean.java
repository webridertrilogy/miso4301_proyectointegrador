package co.uniandes.sisinfo.historico.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.historico.entities.tesisM.h_curso_tomado;
import co.uniandes.sisinfo.historico.entities.tesisM.h_estudiante_maestria;
import co.uniandes.sisinfo.historico.entities.tesisM.h_inscripcion_subarea;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM.h_EstudianteMaestria_FacadeLocal;
import co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM.h_InscripcionSubareaInvest_FacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import co.uniandes.sisinfo.serviciosnegocio.HistoricoInscripcionSubareaInvestRemote;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicios de negocio para Histórico de Inscripción Subarea
 * @author Paola Gómez
 */
@Stateless
public class HistoricoInscripcionSubareaInvestBean implements HistoricoInscripcionSubareaInvestRemote,HistoricoInscripcionSubareaInvestLocal {

    @EJB
    private h_InscripcionSubareaInvest_FacadeLocal incripcionSubareaFacade;
    @EJB
    private h_EstudianteMaestria_FacadeLocal estudianteMaestriaFacade;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    private ParserT parser;

    public HistoricoInscripcionSubareaInvestBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(HistoricoInscripcionSubareaInvestBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String pasarInscripcionSubAreaInvestigacionAHistorico(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secInscripcionSubarea = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD));

            h_inscripcion_subarea inscripcionSubarea = pasarSecuenciaInscripcionSubareaInvestigacionHistorica(secInscripcionSubarea);

            System.out.println("\n\nINSCRIPCION:\n");
            System.out.println("\n\nLo que tiene inscripción subarea historico es: " + inscripcionSubarea.getCursos().size() + " cursos obligatorios");

            Secuencia secEstudiante = secInscripcionSubarea.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
            h_estudiante_maestria estudianteMaestria = pasarSecuenciaAEstudiante(secEstudiante);
            h_estudiante_maestria estudianteM = estudianteMaestriaFacade.findByCorreoEstudiante(estudianteMaestria.getCorreo());

            if(estudianteM == null){
                estudianteMaestria.setId(null);
                inscripcionSubarea.setId(null);
                Collection <h_inscripcion_subarea> inscripciones = new ArrayList();
                inscripciones.add(inscripcionSubarea);
                estudianteMaestria.setInscripcionSubarea(inscripciones);
                System.out.println("\n\ninscripcion subarea - Va a crear estudiante maestria: ");
                getEstudianteMaestriaFacade().create(estudianteMaestria);
            }else{

                Collection <h_inscripcion_subarea> inscripciones = estudianteM.getInscripcionSubarea();
                boolean estaMigrada = false;

                if(inscripciones == null){
                    System.out.println("\n\ninscripcion subarea - inscripciones esta en null");
                    estaMigrada = false;
                }else{
                    if(inscripciones.isEmpty()){
                        System.out.println("\n\ninscripcion subarea - inscripciones esta vacia");
                        estaMigrada = false;
                    }else{
                        System.out.println("\n\ninscripcion subarea - inscripciones no esta vacia");
                        for(h_inscripcion_subarea inscripcionTemp : inscripciones){
                            System.out.println("\n\n\n\ninscripcion subarea - fecha inscripcion temporal: " + inscripcionTemp.getFechaCreacion().toString());
                            System.out.println("\n\n\n\ninscripcion subarea - fecha inscripcion : " + inscripcionSubarea.getFechaCreacion().toString());
                            if(inscripcionTemp.getFechaCreacion().equals(inscripcionSubarea.getFechaCreacion())){
                                System.out.println("\n\ninscripcion subarea - esta migrada ahora es : " + estaMigrada);
                                estaMigrada = true;
                                break;
                            }
                        }
                    }
                }

                System.out.println("\n\ninscripcion subarea - esta migrada: " + estaMigrada);
                
                if(estaMigrada == false){
                    inscripcionSubarea.setId(null);
                    if(inscripciones == null){
                        inscripciones = new ArrayList();
                        inscripciones.add(inscripcionSubarea);
                    }else{
                        inscripciones.add(inscripcionSubarea);
                    }
                    estudianteM.setInscripcionSubarea(inscripciones);
                    System.out.println("\n\ninscripcion subarea - Va a editar estudiante maestria: ");
                    getEstudianteMaestriaFacade().edit(estudianteM);
                }
                
            }

            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_MIGRAR_INSCRIPCION_SUBAREA_A_HISTORICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(HistoricoInscripcionSubareaInvestBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Métodos auxiliares para paso de secuencias a entidades históricas de proyecto de grado

    private h_inscripcion_subarea pasarSecuenciaInscripcionSubareaInvestigacionHistorica(Secuencia secInscripcionSubareaInvest){
        
        try {
            h_inscripcion_subarea inscripcionSubarea = new h_inscripcion_subarea();
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            /*Secuencia secEstudiante = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
            if (secEstudiante != null){
                pasarSecuenciaAEstudiante(secEstudiante, inscripcionSubarea);
            }*/

            /*
            Secuencia secAsesor = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));
            if (secAsesor != null){
                Secuencia secAsesorProfesor = secAsesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
                pasarSecuenciaAEstudianteAsesor(secAsesorProfesor, inscripcionSubarea);
            }
             * 
             */

            Secuencia secNombreSubareaInvestigacion = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_SUBAREA_INVESTIGACION));
            if (secNombreSubareaInvestigacion != null){
                inscripcionSubarea.setNombreSubarea(secNombreSubareaInvestigacion.getValor());
            }

            Secuencia secFechaCreacion = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION));
            if (secFechaCreacion != null){
                Date d = sdfHMS.parse(secFechaCreacion.getValor());

                inscripcionSubarea.setFechaCreacion(new Timestamp(d.getTime()));
            }

            Secuencia secCursosObligatorios = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OBLIGATORIOS));
            if (secCursosObligatorios != null){
                pasarSecuenciaACursosObligatorios(secCursosObligatorios, inscripcionSubarea);
            }

            Secuencia secCursosSubarea = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_SUBAREAS));
            if (secCursosSubarea != null){
                pasarSecuenciaACursosSubarea(secCursosSubarea, inscripcionSubarea);
            }

            Secuencia secCursosOtraSubarea = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OTRA_SUBAREA));
            if (secCursosOtraSubarea != null){
                pasarSecuenciaACursosOtraSubarea(secCursosOtraSubarea, inscripcionSubarea);
            }

            Secuencia secCursosOtraMaestria = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OTRA_MAESTRIA));
            if (secCursosOtraMaestria != null){
                pasarSecuenciaACursosOtraMaestria(secCursosOtraMaestria, inscripcionSubarea);
            }

            Secuencia secCursosNivelatorios = secInscripcionSubareaInvest.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_NIVELATORIOS));
            if (secCursosNivelatorios != null){
                pasarSecuenciaACursosNivelatorios(secCursosNivelatorios, inscripcionSubarea);
            }

            return inscripcionSubarea;
            
        }catch (ParseException ex) {
            Logger.getLogger(HistoricoInscripcionSubareaInvestBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    private h_estudiante_maestria pasarSecuenciaAEstudiante(Secuencia secEstudiante) {   //(Secuencia secEstudiante, h_inscripcion_subarea inscripcion) {

        h_estudiante_maestria estudiante = new h_estudiante_maestria();


        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            estudiante.setNombres(secNombre.getValor());
        }

        Secuencia secApellidos = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApellidos != null) {
            estudiante.setApellidos(secApellidos.getValor());
        }

        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            estudiante.setCorreo(secCorreo.getValor());
        }

        Secuencia secCodigo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE));
        if (secCodigo != null) {
            estudiante.setCodigo(secCodigo.getValor());
        }

        return estudiante;
        //inscripcion.setEstudiante(estudiante);
    }

/*
    private void pasarSecuenciaAEstudianteAsesor(Secuencia secAsesorProfesor, h_inscripcion_subarea inscripcion) {

        h_estudiante_maestria estudiante = new h_estudiante_maestria();

        if(inscripcion.getEstudiante() != null){
            estudiante = inscripcion.getEstudiante();
        }

        Secuencia secNombre = secAsesorProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            inscripcion.getEstudiante().setNombresAsesor(secNombre.getValor());
        }

        Secuencia secApellidos = secAsesorProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApellidos != null) {
            inscripcion.getEstudiante().setApellidosAsesor(secApellidos.getValor());
        }

        Secuencia secCorreo = secAsesorProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            inscripcion.getEstudiante().setCorreoAsesor(secCorreo.getValor());
        }

        inscripcion.setEstudiante(estudiante);
    }
*/
    
    private void pasarSecuenciaACursosObligatorios(Secuencia secCursosObligatorios, h_inscripcion_subarea inscripcion) {
        
        Collection<h_curso_tomado> cursosTomados = new ArrayList<h_curso_tomado>();

        if(inscripcion.getCursos() != null){
            cursosTomados = inscripcion.getCursos();
        }

        for(Secuencia secCursoTomado : secCursosObligatorios.getSecuencias()){
            h_curso_tomado cursoTomado = new h_curso_tomado();
            pasarSecuenciaACursoObligatorio(secCursoTomado, cursoTomado);
            cursoTomado.setClasificacion(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OBLIGATORIOS));
            cursosTomados.add(cursoTomado);
        }

        inscripcion.setCursos(cursosTomados);
    }

    private void pasarSecuenciaACursosSubarea(Secuencia secCursosSubarea, h_inscripcion_subarea inscripcion) {

        Collection<h_curso_tomado> cursosTomados = new ArrayList<h_curso_tomado>();

        if(inscripcion.getCursos() != null){
            cursosTomados = inscripcion.getCursos();
        }

        for(Secuencia secCursoTomado : secCursosSubarea.getSecuencias()){
            h_curso_tomado cursoTomado = new h_curso_tomado();
            pasarSecuenciaACurso(secCursoTomado, cursoTomado);
            cursoTomado.setClasificacion(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_SUBAREAS));
            cursosTomados.add(cursoTomado);
        }

        inscripcion.setCursos(cursosTomados);
    }

    private void pasarSecuenciaACursosOtraSubarea(Secuencia secCursosOtraSubarea, h_inscripcion_subarea inscripcion) {

        Collection<h_curso_tomado> cursosTomados = new ArrayList<h_curso_tomado>();

        if(inscripcion.getCursos() != null){
            cursosTomados = inscripcion.getCursos();
        }

        for(Secuencia secCursoTomado : secCursosOtraSubarea.getSecuencias()){
            h_curso_tomado cursoTomado = new h_curso_tomado();
            pasarSecuenciaACurso(secCursoTomado, cursoTomado);
            cursoTomado.setClasificacion(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OTRA_SUBAREA));
            cursosTomados.add(cursoTomado);
        }

        inscripcion.setCursos(cursosTomados);
    }

    private void pasarSecuenciaACursosOtraMaestria(Secuencia secCursosOtraMaestria, h_inscripcion_subarea inscripcion) {

        Collection<h_curso_tomado> cursosTomados = new ArrayList<h_curso_tomado>();

        if(inscripcion.getCursos() != null){
            cursosTomados = inscripcion.getCursos();
        }

        for(Secuencia secCursoTomado : secCursosOtraMaestria.getSecuencias()){
            h_curso_tomado cursoTomado = new h_curso_tomado();
            pasarSecuenciaACurso(secCursoTomado, cursoTomado);
            cursoTomado.setClasificacion(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OTRA_MAESTRIA));
            cursosTomados.add(cursoTomado);
        }

        inscripcion.setCursos(cursosTomados);
    }

    private void pasarSecuenciaACursosNivelatorios(Secuencia secCursosNivelatorios, h_inscripcion_subarea inscripcion) {

        Collection<h_curso_tomado> cursosTomados = new ArrayList<h_curso_tomado>();

        if(inscripcion.getCursos() != null){
            cursosTomados = inscripcion.getCursos();
        }

        for(Secuencia secCursoTomado : secCursosNivelatorios.getSecuencias()){
            h_curso_tomado cursoTomado = new h_curso_tomado();
            pasarSecuenciaACurso(secCursoTomado, cursoTomado);
            cursoTomado.setClasificacion(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_NIVELATORIOS));
            cursosTomados.add(cursoTomado);
        }

        inscripcion.setCursos(cursosTomados);
    }
    
    private void pasarSecuenciaACurso(Secuencia secCursoTomado, h_curso_tomado cursoTomado) {

        Secuencia secNombre = secCursoTomado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secNombre != null) {
            cursoTomado.setNombre(secNombre.getValor());
        }

        Secuencia secSemestre = secCursoTomado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
        if (secSemestre != null) {
            cursoTomado.setSemestre(secSemestre.getValor());
        }

        Secuencia secCursoVisto = secCursoTomado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO));
        if (secCursoVisto != null) {
            Boolean visto = Boolean.parseBoolean(secCursoVisto.getValor());
            cursoTomado.setCursoVisto(visto);
        }


    }

    private void pasarSecuenciaACursoObligatorio(Secuencia secCursoTomado, h_curso_tomado cursoTomado) {

        Secuencia secNombre = secCursoTomado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secNombre != null) {
            cursoTomado.setNombre(secNombre.getValor());
        }

        Secuencia secSemestre = secCursoTomado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
        if (secSemestre != null) {
            cursoTomado.setSemestre(secSemestre.getValor());
        }

        Secuencia secCursoVisto = secCursoTomado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO));
        if (secCursoVisto != null) {
            Boolean visto = Boolean.parseBoolean(secCursoVisto.getValor());
            cursoTomado.setCursoVisto(visto);
        }

        cursoTomado.setClasificacion(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_OBLIGATORIOS));
    }




    //Métodos auxiliares de retorno de atributos
    
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        return (parser == null) ? new ParserT() : parser;
    }

    private h_InscripcionSubareaInvest_FacadeLocal getInscripcionSubareaFacade() {
        return incripcionSubareaFacade;
    }

    private h_EstudianteMaestria_FacadeLocal getEstudianteMaestriaFacade() {
        return estudianteMaestriaFacade;
    }


}
