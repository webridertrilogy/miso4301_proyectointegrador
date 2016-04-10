package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.AsistenciaGraduada;
import co.uniandes.sisinfo.entities.EstudiantePosgrado;
import co.uniandes.sisinfo.entities.HojaVida;
import co.uniandes.sisinfo.entities.InformacionEmpresa;
import co.uniandes.sisinfo.entities.Oferta;
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.Proponente;
import co.uniandes.sisinfo.entities.TipoAsistenciaGraduada;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.soporte.Pais;
import co.uniandes.sisinfo.entities.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.EstudiantePosgradoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.HojaVidaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.InformacionEmpresaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.OfertaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ProponenteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TipoAsistenciaGraduadaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;
import java.sql.Timestamp;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.NamingException;

/**
 * Conversor del módulo de Asistencias Graduadas y Bolsa de Empleo
 * @author Marcela Morales
 */
public class ConversorOferta {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Útil
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ServiceLocator serviceLocator;
    //Servicios
    @EJB
    private EstudianteFacadeRemote estudianteFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    @EJB
    private TipoAsistenciaGraduadaFacadeRemote tipoAsistenciaFacade;
    @EJB
    private PaisFacadeRemote paisFacade;
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentoFacade;
    @EJB
    private InformacionAcademicaFacadeRemote informacionAcademicaFacade;
    @EJB
    private HojaVidaFacadeRemote hojaVidaFacade;
    @EJB
    private EstudiantePosgradoFacadeRemote estudiantePostgradoFacade;
    @EJB
    private ProponenteFacadeRemote proponenteFacade;
    @EJB
    private OfertaFacadeRemote ofertaFacade;
    @EJB
    private InformacionEmpresaFacadeRemote informacionEmpresaFacade;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------

    /**
     * Conversor del módulo de asistencias graduadas
     * @param constanteBean Referencia a los servicios de las constantes
     */
    public ConversorOferta(ConstanteRemote constanteBean) {
        try {
            serviceLocator = new ServiceLocator();
            this.constanteBean = constanteBean;
            estudianteFacade = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            periodoFacade = (PeriodoFacadeRemote) serviceLocator.getRemoteEJB(PeriodoFacadeRemote.class);
            paisFacade = (PaisFacadeRemote) serviceLocator.getRemoteEJB(PaisFacadeRemote.class);
            tipoAsistenciaFacade = (TipoAsistenciaGraduadaFacadeRemote) serviceLocator.getRemoteEJB(TipoAsistenciaGraduadaFacadeRemote.class);
            tipoDocumentoFacade = (TipoDocumentoFacadeRemote) serviceLocator.getRemoteEJB(TipoDocumentoFacadeRemote.class);
            informacionAcademicaFacade = (InformacionAcademicaFacadeRemote) serviceLocator.getRemoteEJB(InformacionAcademicaFacadeRemote.class);
            hojaVidaFacade = (HojaVidaFacadeRemote) serviceLocator.getRemoteEJB(HojaVidaFacadeRemote.class);
            estudiantePostgradoFacade = (EstudiantePosgradoFacadeRemote) serviceLocator.getRemoteEJB(EstudiantePosgradoFacadeRemote.class);
            proponenteFacade = (ProponenteFacadeRemote) serviceLocator.getRemoteEJB(ProponenteFacadeRemote.class);
            ofertaFacade = (OfertaFacadeRemote) serviceLocator.getRemoteEJB(OfertaFacadeRemote.class);
            informacionEmpresaFacade = (InformacionEmpresaFacadeRemote) serviceLocator.getRemoteEJB(InformacionEmpresaFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ConversorOferta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A SECUENCIAS
    //----------------------------------------------
    /**
     * Crea una secuencia dado un conjunto de asistencias graduadas
     * @param asistencias Colección de asistencias graduadas
     * @return Secuencia construída a partir de la colección de asistencias graduadas dada
     */
    public Secuencia crearSecuenciaAsistencias(Collection<AsistenciaGraduada> asistencias) {
        Secuencia secAsistencias = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ASISTENCIAS_GRADUADAS), "");
        for (AsistenciaGraduada asistencia : asistencias) {
            Secuencia secAsistencia = crearSecuenciaAsistencia(asistencia);
            secAsistencias.agregarSecuencia(secAsistencia);
        }
        return secAsistencias;
    }

    /**
     * Crea una secuencia dada una asistencia graduada
     * @param asistencia Asistencia graduada
     * @return Secuencia construída a partir de la asistencia graduada dada
     */
    public Secuencia crearSecuenciaAsistencia(AsistenciaGraduada asistencia){
        if(asistencia == null)
            return null;
        Secuencia secAsistencia = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ASISTENCIA_GRADUADA), "");
        if(asistencia.getId() != null){
            secAsistencia.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_GENERAL), asistencia.getId().toString()));
        }
        if(asistencia.getPeriodo() != null){
            if(asistencia.getPeriodo().getPeriodo() != null){
                Secuencia secPeriodo = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PERIODO), "");
                secPeriodo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE), asistencia.getPeriodo().getPeriodo()));
                secAsistencia.agregarSecuencia(secPeriodo);
            }
        }
        if(asistencia.getEncargado() != null){
            secAsistencia.agregarSecuencia(crearSecuenciaEncargado(asistencia.getEncargado()));
        }
        if(asistencia.getEstudiante() != null){
            secAsistencia.agregarSecuencia(crearSecuenciaEstudiante(asistencia.getEstudiante()));
        }
        if(asistencia.getTipo() != null){
            secAsistencia.agregarSecuencia(crearSecuenciaTipo(asistencia.getTipo(), false));
        }
        if(asistencia.getSubtipo() != null){
            secAsistencia.agregarSecuencia(crearSecuenciaTipo(asistencia.getSubtipo(), true));
        }
        if(asistencia.getInfoTipo() != null){
            secAsistencia.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DESCRIPCION), asistencia.getInfoTipo()));
        }
        if(asistencia.getNota() != null){
            secAsistencia.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOTA), asistencia.getNota().toString()));
        }
        if(asistencia.getObservaciones() != null){
            secAsistencia.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_OBSERVACIONES), asistencia.getObservaciones()));
        }
        return secAsistencia;
    }

    /**
     * Crea una secuencia dada una persona
     * @param persona Persona
     * @return Secuencia construída a partir de la persona dada
     */
    public Secuencia crearSecuenciaEncargado(Persona persona) {
        if(persona == null)
            return null;
        Secuencia secPersona = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ENCARGADO), "");
        if(persona.getNombres() != null){
            secPersona.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES), persona.getNombres()));
        }
        if(persona.getApellidos() != null){
            secPersona.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS), persona.getApellidos()));
        }
        if(persona.getCorreo() != null){
            secPersona.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), persona.getCorreo()));
        }
        return secPersona;
    }

    /**
     * Crea una secuencia dado un estudiante
     * @param estudiante Estudiante
     * @return Secuencia construída a partir del estudiante dado
     */
    public Secuencia crearSecuenciaEstudiante(Estudiante estudiante) {
        if(estudiante == null)
            return null;
        Secuencia secEstudiante = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        if(estudiante.getPersona() != null){
            if(estudiante.getPersona().getNombres() != null){
                secEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES), estudiante.getPersona().getNombres()));
            }
            if(estudiante.getPersona().getApellidos() != null){
                secEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS), estudiante.getPersona().getApellidos()));
            }
            if(estudiante.getPersona().getCorreo() != null){
                secEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), estudiante.getPersona().getCorreo()));
            }
        }
        return secEstudiante;
    }

    /**
     * Crea una secuencia incompleta dado un tipo de asistencia
     * @param tipo El tipo de asistencia graduada
     * @param esSubtipo Indica si el tipo es un subtipo. La construcción de la Secuencia cambia dependiendo de éste parámetro.
     * @return Secuencia incompleta construída a partir del tipo de asistencia dado
     */
    public Secuencia crearSecuenciaTipo(TipoAsistenciaGraduada tipo, boolean esSubtipo){
        if(tipo == null)
            return null;
        Secuencia secTipo = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO), "");
        if(esSubtipo)
            secTipo = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_SUBTIPO), "");
        if(tipo.getTipo() != null){
            secTipo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE), tipo.getTipo()));
        }
        return secTipo;
    }

    /**
     * Crea una secuencia dado un conjunto de tipos de asistencia
     * @param tipos Conjunto de tipos de asistencia graduada
     * @return Secuencia construída a partir del conjunto de tipos de asistencia dado
     */
    public Secuencia crearSecuenciaTiposAsistencias(Collection<TipoAsistenciaGraduada> tipos) {
        Secuencia secTipos = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPOS_ASISTENCIAS_GRADUADAS), "");
        for (TipoAsistenciaGraduada tipo : tipos) {
            Secuencia secTipo = crearSecuenciaTipoAsistencia(tipo);
            secTipos.agregarSecuencia(secTipo);
        }
        return secTipos;
    }

    /**
     * Crea una secuencia dado un tipo de asistencia
     * @param tipo El tipo de asistencia graduada
     * @param esSubtipo Indica si el tipo es un subtipo. La construcción de la Secuencia cambia dependiendo de éste parámetro.
     * @return Secuencia construída a partir del tipo de asistencia dado
     */
    public Secuencia crearSecuenciaTipoAsistencia(TipoAsistenciaGraduada tipo){
        if(tipo == null)
            return null;
        Secuencia secTipo = crearSecuenciaTipo(tipo, false);
        if(tipo.getInfoRequerida() != null){
            secTipo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INFORMACION_REQUERIDA), tipo.getInfoRequerida()));
        }
        Secuencia secSubtipos = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_SUBTIPOS), "");
        for(TipoAsistenciaGraduada subtipo : tipo.getSubtipos()){
            Secuencia secSubtipo = crearSecuenciaTipo(subtipo, true);
            secSubtipos.agregarSecuencia(secSubtipo);
        }
        secTipo.agregarSecuencia(secSubtipos);
        return secTipo;
    }

    /**
     * Crea una secuencia dado un estudiante de postgrado
     * @param estudiantePosgrado Estudiante de postgrado
     * @return Secuencia construída a partir del estudiante de postgrado
     */
    public Secuencia crearSecuenciaEstudiante(EstudiantePosgrado estudiantePosgrado){

        Estudiante estudiante = estudiantePosgrado.getEstudiante();
        Secuencia secuenciaEstudiante = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_ESTUDIANTE), (estudiantePosgrado.getId() != null) ? estudiantePosgrado.getId().toString() : ""));
        secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getCorreo() != null) ? estudiante.getPersona().getCorreo() : ""));
        secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getCorreoAlterno() != null) ? estudiante.getPersona().getCorreoAlterno() : ""));
        //secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EXTERNO), (estudiantePosgrado.isEsExterno() != null) ? estudiantePosgrado.isEsExterno().toString() : ""));
        secuenciaEstudiante.agregarSecuencia(this.darSecuenciaConExcepcion(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EXTERNO), estudiantePosgrado.isEsExterno(), estudiantePosgrado.isEsExterno()));

        Secuencia secuenciaHojaVida = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_HOJA_VIDA), "");
        secuenciaHojaVida.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CONTENIDO), (estudiantePosgrado.getHojaVida() != null && estudiantePosgrado.getHojaVida().getContenido() != null) ? estudiantePosgrado.getHojaVida().getContenido() : ""));
        secuenciaHojaVida.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_CREACION), (estudiantePosgrado.getHojaVida() != null && estudiantePosgrado.getHojaVida().getFechaCreacion() != null) ? estudiantePosgrado.getHojaVida().getFechaCreacion().toString() : ""));
        secuenciaHojaVida.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_ACTUALIZACION), (estudiantePosgrado.getHojaVida() != null && estudiantePosgrado.getHojaVida().getFechaActualizacion() != null) ? estudiantePosgrado.getHojaVida().getFechaActualizacion().toString() : ""));
        secuenciaEstudiante.agregarSecuencia(secuenciaHojaVida);

        Secuencia secuenciaInformacionAcademica = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), "");
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CIUDAD), (estudiantePosgrado.getCiudadUniversidadPregrado() != null) ? estudiantePosgrado.getCiudadUniversidadPregrado() : ""));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_GRADUACION), (estudiantePosgrado.getFechaGraduacion() != null) ? estudiantePosgrado.getFechaGraduacion().toString() : ""));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE_UNIVERSIDAD), (estudiantePosgrado.getUniversidadPregrado() != null) ? estudiantePosgrado.getUniversidadPregrado() : ""));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PAIS), (estudiantePosgrado.getPaisUniversidadPregrado() != null) ? estudiantePosgrado.getPaisUniversidadPregrado().getNombre() : ""));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TITULO), (estudiantePosgrado.getTitulo() != null) ? estudiantePosgrado.getTitulo() : ""));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_PRIMER_SEMESTRE_MAESTRIA), (estudiantePosgrado.getEsPrimerSemestreMaestria() != null) ? estudiantePosgrado.getEsPrimerSemestreMaestria().toString() : ""));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), (estudiantePosgrado.getEstudiante() != null && estudiantePosgrado.getEstudiante().getInformacion_Academica() != null && estudiantePosgrado.getEstudiante().getInformacion_Academica().getPromedioTotal() != null) ? estudiantePosgrado.getEstudiante().getInformacion_Academica().getPromedioTotal().toString() : ""));
        secuenciaEstudiante.agregarSecuencia(secuenciaInformacionAcademica);

        if (estudiante.getPersona() != null) {

            Secuencia secuenciaInformacionPersonal = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), "");
            secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getApellidos() == null) ? "" : estudiante.getPersona().getApellidos()));
            secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NACIONALIDAD), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getPais() == null) ? "" : estudiante.getPersona().getPais().getNombre()));
            secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getNombres() == null) ? "" : estudiante.getPersona().getNombres()));
            secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DOCUMENTO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getNumDocumentoIdentidad() == null) ? "" : estudiante.getPersona().getNumDocumentoIdentidad()));
            secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getCelular() == null) ? "" : estudiante.getPersona().getCelular()));
            secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO_FIJO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getTelefono() == null) ? "" : estudiante.getPersona().getTelefono()));
            secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), (estudiante.getPersona() != null && estudiante.getPersona().getTipoDocumento() == null) ? "" : estudiante.getPersona().getTipoDocumento().getDescripcion()));
            secuenciaEstudiante.agregarSecuencia(secuenciaInformacionPersonal);
        }

        return secuenciaEstudiante;
    }

    /**
     * Da secuencias con excepcion
     */
    public Secuencia darSecuenciaConExcepcion (String key, Object nullable, Object value) {

        System.out.println("inside darSecuenciaConExcepcion para " + key);
        Secuencia secuencia = null;
        try {

            secuencia = new Secuencia(key, nullable != null ? value.toString() : "");
        } catch (Exception e) {

            System.out.println("¿QUE PASO?");
            e.printStackTrace();
        }
        return secuencia;
    }

    /**
     * Crea una secuencia dado un estudiante de postgrado
     * @param estudiantePosgrado Estudiante de postgrado
     * @return Secuencia construída a partir del estudiante de postgrado
     */
    public Secuencia crearSecuenciaEstudianteNuevo(EstudiantePosgrado estudiantePosgrado){

        System.out.println("NUEVA FORMA DE CREAR SECUENCIA DE ESTUDIANTE");
        Estudiante estudiante = estudiantePosgrado.getEstudiante();
        Secuencia secuenciaEstudiante = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        if (estudiante != null && estudiante.getPersona() != null) {

            secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_ESTUDIANTE), (estudiantePosgrado.getId() != null) ? estudiantePosgrado.getId().toString() : ""));
            secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getCorreo() != null) ? estudiante.getPersona().getCorreo() : ""));
            secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getCorreoAlterno() != null) ? estudiante.getPersona().getCorreoAlterno() : ""));
            if (estudiantePosgrado.isEsExterno() != null) {

                System.out.println(estudiantePosgrado.isEsExterno().toString());
                //secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EXTERNO), estudiantePosgrado.isEsExterno().toString()));
            }
        }
        return secuenciaEstudiante;
    }
    
    /**
     * Crea una secuencia dado un conjunto de estudiantes de postgrado
     * @param estudiantes Colección de estudiantes de postgrado
     * @return Secuencia construída a partir de la colección de estudiantes de postgrado dada
     */
    public Secuencia crearSecuenciaEstudiantesPostgrado(Collection<EstudiantePosgrado> estudiantes) {
        Secuencia secEstudiantes = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ESTUDIANTES), "");
        for (EstudiantePosgrado estudiante : estudiantes) {
            Secuencia secEstudiante = crearSecuenciaEstudianteNuevo(estudiante);
            //secEstudiantes.agregarSecuencia(secEstudiante);
        }
        return secEstudiantes;
    }

    /**
     * Crea una secuencia dada una hoja de vida
     * @param hojaVida Hoja de vida
     * @return Secuencia construída a partir de la hoja de vida
     */
    public Secuencia crearSecuenciaHojaVida(HojaVida hojaVida){
        Secuencia secuenciaHojaVida = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_HOJA_VIDA), "");
        secuenciaHojaVida.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CONTENIDO), (hojaVida == null || hojaVida.getContenido() == null) ? "" : hojaVida.getContenido()));
        secuenciaHojaVida.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_CREACION), (hojaVida == null || hojaVida.getFechaCreacion() == null) ? "" : hojaVida.getFechaCreacion().toString()));
        secuenciaHojaVida.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_ACTUALIZACION), (hojaVida == null || hojaVida.getFechaActualizacion() == null) ? "" : hojaVida.getFechaActualizacion().toString()));
        return secuenciaHojaVida;
    }

    /**
     * Crea una secuencia de la información académica de un estudiante dado el estudiante
     * @param estudiante Estudiante de postgrado
     * @return Secuencia construída a partir de la información académica del estudiante dado
     */
    public Secuencia crearSecuenciaInformacionAcademica(EstudiantePosgrado estudiante){
        Secuencia secuenciaInformacionAcademica = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), "");
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CIUDAD), (estudiante.getCiudadUniversidadPregrado() == null) ? "" : estudiante.getCiudadUniversidadPregrado()));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_GRADUACION), (estudiante.getFechaGraduacion() == null) ? "" : estudiante.getFechaGraduacion().toString()));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE_UNIVERSIDAD), (estudiante.getUniversidadPregrado() == null) ? "" : estudiante.getUniversidadPregrado()));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PAIS), (estudiante.getPaisUniversidadPregrado() == null) ? "" : estudiante.getPaisUniversidadPregrado().getNombre()));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TITULO), (estudiante.getTitulo() == null) ? "" : estudiante.getTitulo()));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_PRIMER_SEMESTRE_MAESTRIA), (estudiante.getEsPrimerSemestreMaestria() == null || estudiante.getEsPrimerSemestreMaestria().equals(Boolean.FALSE)) ? Boolean.FALSE.toString() : Boolean.TRUE.toString()));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), (estudiante.getEstudiante().getInformacion_Academica() == null || estudiante.getEstudiante().getInformacion_Academica().getPromedioTotal() == null) ? "" : estudiante.getEstudiante().getInformacion_Academica().getPromedioTotal().toString()));
        return secuenciaInformacionAcademica;
    }

    /**
     * Crea una secuencia de la información personal de un estudiante dado el estudiante
     * @param estudiante Estudiante de postgrado
     * @return Secuencia construída a partir de la información personal del estudiante dado
     */
    public Secuencia crearSecuenciaInformacionPersonal(EstudiantePosgrado estudiante){
        Persona persona = estudiante.getEstudiante().getPersona();
        Secuencia secuenciaInformacionPersonal = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), "");
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS), (estudiante.getEstudiante() == null || estudiante.getEstudiante().getPersona() == null || persona.getApellidos() == null) ? "" : persona.getApellidos()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NACIONALIDAD), (estudiante.getEstudiante() == null || estudiante.getEstudiante().getPersona() == null || persona.getPais() == null) ? "" : persona.getPais().getNombre()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES), (estudiante.getEstudiante() == null || estudiante.getEstudiante().getPersona() == null || persona.getNombres() == null) ? "" : persona.getNombres()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DOCUMENTO), (estudiante.getEstudiante() == null || estudiante.getEstudiante().getPersona() == null || persona.getNumDocumentoIdentidad() == null) ? "" : persona.getNumDocumentoIdentidad()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR), (estudiante.getEstudiante() == null || estudiante.getEstudiante().getPersona() == null || persona.getCelular() == null) ? "" : persona.getCelular()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO_FIJO), (estudiante.getEstudiante() == null || estudiante.getEstudiante().getPersona() == null || persona.getTelefono() == null) ? "" : persona.getTelefono()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), (estudiante.getEstudiante() == null || estudiante.getEstudiante().getPersona() == null || persona.getTipoDocumento() == null) ? "" : persona.getTipoDocumento().getDescripcion()));
        return secuenciaInformacionPersonal;
    }

    /**
     * Crea una secuencia de la información de una oferta dada
     * @param oferta Oferta
     * @return Secuencia construída a partir de la información de la oferta dada
     */
    public Secuencia crearSecuenciaOferta(Oferta oferta){
        Secuencia secuenciaOferta = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_OFERTA), "");
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_OFERTA), (oferta.getId() == null ) ? "" : Long.toString(oferta.getId())));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE), (oferta.getNombre() == null ) ? "" : oferta.getNombre()));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TITULO), (oferta.getTitulo() == null ) ? "" : oferta.getTitulo()));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_DESCRIPCION), (oferta.getDescripcion() == null ) ? "" : oferta.getDescripcion()));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DIRECCION_WEB), (oferta.getDireccionWeb() == null ) ? "" : oferta.getDireccionWeb()));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_CREACION), (oferta.getFechaCreacion() == null ) ? "" : oferta.getFechaCreacion().toString()));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_FIN), (oferta.getFechaFinOferta() == null ) ? "" : oferta.getFechaFinOferta().toString()));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PERIODO_VINCULACION), (oferta.getPeriodoVinculacion() == null ) ? "" : oferta.getPeriodoVinculacion()));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_REQUISITOS), (oferta.getRequisitos() == null) ? "" : oferta.getRequisitos()));
        secuenciaOferta.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO_CONTACTO), (oferta.getCorreoContacto() == null ) ? "" : oferta.getCorreoContacto()));
        Proponente proponente = proponenteFacade.findByIdOferta(oferta.getId());
        Secuencia secuenciaProponente = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PROPONENTE), "");
        if (proponente != null) {
            secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_PROPONENTE), (proponente.getId() == null) ? "" : proponente.getId().toString()));
            secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CARGO),
                    (proponente.getInformacionEmpresa() != null && proponente.getInformacionEmpresa().getCargoContactoEmpresa() != null) ? proponente.getInformacionEmpresa().getCargoContactoEmpresa() : ""));
            secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EMPRESA), (proponente.isEsEmpresa() == null) ? "" : Boolean.toString(proponente.isEsEmpresa())));
            Persona persona = proponente.getPersona();
            if(persona != null){
                secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES), (persona.getNombres() == null) ? "" : persona.getNombres()));
                secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS), (persona.getApellidos() == null) ? "" : persona.getApellidos()));
                secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO_FIJO), (persona.getTelefono() == null) ? "" : persona.getTelefono()));
                secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR), (persona.getCelular() == null) ? "" : persona.getCelular()));
                secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_EXTENSION), (persona.getExtension() == null) ? "" : persona.getExtension()));
                secuenciaProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), (persona.getCorreo() == null) ? "" : persona.getCorreo()));
            }
            secuenciaOferta.agregarSecuencia(secuenciaProponente);
        }
        return secuenciaOferta;
    }

    /**
     * Crea una secuencia dado un conjunto de ofertas
     * @param ofertas Conjunto deofertas
     * @return Secuencia construída a partir del conjunto de ofertas dado
     */
    public Secuencia crearSecuenciaOfertas(Collection<Oferta> ofertas) {
        Secuencia secOfertas = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_OFERTAS), "");
        for (Oferta oferta : ofertas) {
            Secuencia secOferta = crearSecuenciaOferta(oferta);
            secOfertas.agregarSecuencia(secOferta);
        }
        return secOfertas;
    }

    /**
     * Crea una secuencia dado un proponente
     * @param proponente Proponente
     * @return Secuencia construída a partir del proponente dado
     */
    public Secuencia crearSecuenciaProponente(Proponente proponente){
        Secuencia secProponente = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PROPONENTE), "");

        //Información de la persona
        Persona persona = proponente.getPersona();
        secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), (persona.getCorreo() != null) ? persona.getCorreo() : ""));
        secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES), (persona.getNombres() != null) ? persona.getNombres() : ""));
        secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS), (persona.getApellidos() != null) ? persona.getApellidos() : ""));
        secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO), (persona.getTelefono() != null) ? persona.getTelefono() : ""));
        secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR), (persona.getCelular() != null) ? persona.getCelular() : ""));
        secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_EXTENSION), (persona.getExtension() != null) ? persona.getExtension() : ""));
        secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EMPRESA), Boolean.toString(proponente.isEsEmpresa())));
        secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_PROPONENTE), (proponente.getId() != null) ? proponente.getId().toString() : ""));
        
        //Información empresarial
        Secuencia secEmpresa = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_EMPRESA), "");
        InformacionEmpresa infoEmpresa = proponente.getInformacionEmpresa();
        if (proponente.isEsEmpresa()) {
            secProponente.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EMPRESA), Boolean.toString(proponente.isEsEmpresa())));
            secEmpresa.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE_EMPRESA), (infoEmpresa.getNombreEmpresa() != null) ? infoEmpresa.getNombreEmpresa() : ""));
            Secuencia secContacto = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CONTACTO_EMPRESA), "");
            secContacto.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CARGO), (infoEmpresa.getCargoContactoEmpresa() != null)? infoEmpresa.getCargoContactoEmpresa() : ""));
            secContacto.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ROL), (infoEmpresa.getCargoContactoEmpresa() != null)? infoEmpresa.getCargoContactoEmpresa() : ""));
            secEmpresa.agregarSecuencia(secContacto);
            secProponente.agregarSecuencia(secEmpresa);
        } 
        return secProponente;
    }

    /**
     * Crea una secuencia dado un conjunto de proponentes
     * @param proponentes Conjunto de proponentes
     * @return Secuencia construída a partir del conjunto de proponentes dado
     */
    public Secuencia crearSecuenciaProponentes(Collection<Proponente> proponentes) {
        Secuencia secProponentes = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PROPONENTES), "");
        for (Proponente proponente : proponentes) {
            Secuencia secProponente = crearSecuenciaProponente(proponente);
            secProponentes.agregarSecuencia(secProponente);
        }
        return secProponentes;
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A ENTIDADES
    //----------------------------------------------
    /**
     * Crea una colección de asistencias graduadas a partir de una secuencia
     * @param secAsistencias Secuencia
     * @return Colección de asistencias graduadas construída a partir de la secuencia dada
     */
    public Collection<AsistenciaGraduada> crearAsistenciasDesdeSecuencia(Secuencia secAsistencias) {
        Collection<AsistenciaGraduada> asistencias = new ArrayList<AsistenciaGraduada>();
        for (Secuencia secuencia : secAsistencias.getSecuencias()) {
            AsistenciaGraduada asistencia = crearAsistenciaDesdeSecuencia(secuencia);
            asistencias.add(asistencia);
        }
        return asistencias;
    }

   /**
    * Crea una asistencia graduada a partir de una secuencia
    * @param secAsistencia  Secuencia
    * @return Asistencia graduada construída a partir de la secuencia dada
    */
    public AsistenciaGraduada crearAsistenciaDesdeSecuencia(Secuencia secAsistencia){
        if(secAsistencia == null)
            return null;
        AsistenciaGraduada asistencia = new AsistenciaGraduada();
        Secuencia secId = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if(secId != null){
            asistencia.setId(secId.getValorLong());
        }
        Secuencia secPeriodo = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PERIODO));
        if(secPeriodo != null){
            asistencia.setPeriodo(consultarPeriodoPorPeriodo(secPeriodo));
        }
        Secuencia secProfesor = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ENCARGADO));
        if(secProfesor != null){
            asistencia.setEncargado(consultarPersonaPorCorreo(secProfesor));
        }
        Secuencia secEstudiante = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
        if(secEstudiante != null){
            asistencia.setEstudiante(consultarEstudiantePorCorreo(secEstudiante));
        }
        Secuencia secTipo = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO));
        if(secTipo != null){
            asistencia.setTipo(consultarTipo(secTipo));
        }
        Secuencia secSubtipo = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_SUBTIPO));
        if(secSubtipo != null){
            asistencia.setSubtipo(consultarTipo(secSubtipo));
        }
        Secuencia secDescripcion = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DESCRIPCION));
        if(secDescripcion != null){
            asistencia.setInfoTipo(secDescripcion.getValor());
        }
        Secuencia secNota = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOTA));
        if(secNota != null){
            asistencia.setNota(secNota.getValorDouble());
        }
        Secuencia secObservaciones = secAsistencia.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_OBSERVACIONES));
        if(secObservaciones != null){
            asistencia.setObservaciones(secObservaciones.getValor());
        }
        return asistencia;
    }

    /**
    * Crea un estudiante graduado a partir de una secuencia
    * @param secEstudiante   Secuencia
    * @return Estudiante graduado construído a partir de la secuencia dada
    */
    public EstudiantePosgrado crearEstudiantePostgradoDesdeSecuencia(Secuencia secEstudiante) {
        if(secEstudiante == null)
            return null;
        EstudiantePosgrado estudiantePostgrado = new EstudiantePosgrado();

        //Información EstudiantePostgrado
        estudiantePostgrado.setId(null);
        Secuencia secuenciaInformacionAcademica = secEstudiante.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA));
        if(secuenciaInformacionAcademica != null){
            Secuencia secCiudad = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CIUDAD));
            if(secCiudad != null){
                estudiantePostgrado.setCiudadUniversidadPregrado(secCiudad.getValor());
            }
            Secuencia secEsPrimerSemestre = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_PRIMER_SEMESTRE_MAESTRIA));
            if(secEsPrimerSemestre != null){
                estudiantePostgrado.setEsPrimerSemestreMaestria(Boolean.parseBoolean(secEsPrimerSemestre.getValor()));
            }
            Secuencia secFechaGraduacion = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_GRADUACION));
            if(secFechaGraduacion != null){
                SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    estudiantePostgrado.setFechaGraduacion(sdfHMS.parse(secFechaGraduacion.getValor()));
                } catch (ParseException ex) {
                    Logger.getLogger(ConversorOferta.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Secuencia secPais = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PAIS));
            if(secPais != null){
                Pais paisEntitY = paisFacade.findByNombre(secPais.getValor().trim());
                estudiantePostgrado.setPaisUniversidadPregrado(paisEntitY);
            }
            Secuencia secTitulo = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TITULO));
            if(secTitulo != null){
                estudiantePostgrado.setTitulo(secTitulo.getValor());
            }
            Secuencia secNombreUniversidad = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE_UNIVERSIDAD));
            if(secNombreUniversidad != null){
                estudiantePostgrado.setUniversidadPregrado(secNombreUniversidad.getValor());
            }
         }
        Secuencia secEsExterno = secEstudiante.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EXTERNO));
        if(secEsExterno != null){
            estudiantePostgrado.setEsExterno(Boolean.parseBoolean(secEsExterno.getValor()));
        }

        //Información Persona
        Persona persona = consultarPersonaPorCorreo(secEstudiante);
        if(persona == null){
            persona = new Persona();
            Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO));
            if(secCorreo != null){
                persona.setCorreo(secCorreo.getValor());
            }
            personaFacade.create(persona);
        }
        Secuencia secuenciaInformacionPersonal = secEstudiante.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL));
        if(secuenciaInformacionPersonal != null){
            Secuencia secNombres = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES));
            if(secNombres != null){
                persona.setNombres(secNombres.getValor());
            }
            Secuencia secApellidos = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS));
            if(secApellidos != null){
                persona.setApellidos(secApellidos.getValor());
            }
            Secuencia secNacionalidad = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NACIONALIDAD));
            if(secNacionalidad != null){
                Pais paisEntitY = paisFacade.findByNombre(secNacionalidad.getValor().trim());
                persona.setPais(paisEntitY);
            }
            Secuencia secTelefonoFijo = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO_FIJO));
            if(secTelefonoFijo != null){
                persona.setTelefono(secTelefonoFijo.getValor());
            }
            Secuencia secTelefonoCelular = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR));
            if(secTelefonoCelular != null){
                persona.setCelular(secTelefonoCelular.getValor());
            }
            Secuencia secTipoDocumento = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO));
            if(secTipoDocumento != null){
                TipoDocumento tipo = tipoDocumentoFacade.findByTipo(secTipoDocumento.getValor());
                persona.setTipoDocumento(tipo);
            }
            Secuencia secNumeroDocumento = secuenciaInformacionPersonal.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DOCUMENTO));
            if(secNumeroDocumento != null){
                persona.setNumDocumentoIdentidad(secNumeroDocumento.getValor());
            }
        }
        Secuencia secCorreoAlterno = secEstudiante.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO));
        if(secCorreoAlterno != null){
            persona.setCorreoAlterno(secCorreoAlterno.getValor());
        }
        personaFacade.edit(persona);

        //Información Estudiante
        Estudiante estudiante = consultarEstudiantePorCorreo(secEstudiante);
        if(estudiante == null){
            estudiante = new Estudiante();
            estudiante.setId(null);
            estudianteFacade.create(estudiante);
        }
        estudiante.setPersona(persona);

        //Información académica estudiante
        InformacionAcademica infoAcademica = new InformacionAcademica();
        Secuencia secPromedio = secuenciaInformacionAcademica.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL));
        if(secPromedio != null){
            infoAcademica.setPromedioTotal(secPromedio.getValorDouble());
        }
        informacionAcademicaFacade.create(infoAcademica);

        estudiante.setInformacion_Academica(infoAcademica);
        estudianteFacade.edit(estudiante);
        estudiantePostgrado.setEstudiante(estudiante);

        //Información de hoja de vida
        HojaVida hojaVida = new HojaVida();
        Secuencia secuenciaHojaVida = secEstudiante.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_HOJA_VIDA));
        Secuencia secContenido = secuenciaHojaVida.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CONTENIDO));
        hojaVida.setContenido(secContenido.getValor());
        hojaVida.setFechaActualizacion(new Date(System.currentTimeMillis()));
        hojaVida.setFechaCreacion(new Date(System.currentTimeMillis()));
        hojaVidaFacade.create(hojaVida);
        estudiantePostgrado.setHojaVida(hojaVida);

        estudiantePostgradoFacade.create(estudiantePostgrado);
        return estudiantePostgrado;
    }

    /**
    * Crea una oferta a partir de una secuencia
    * @param secOferta Secuencia
    * @return Oferta construída a partir de la secuencia dada
    */
    public Oferta crearOfertaDesdeSecuencia(Secuencia secOferta){
        if(secOferta == null)
            return null;
        Oferta oferta = new Oferta();
        Secuencia nombre = secOferta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE));
        if(nombre != null){
            oferta.setNombre(nombre.getValor());
        }
        Secuencia titulo = secOferta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TITULO));
        if(titulo != null){
            oferta.setTitulo(titulo.getValor());
        }
        Secuencia descripcion = secOferta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_DESCRIPCION));
        if(descripcion != null){
            oferta.setDescripcion(descripcion.getValor());
        }
        Secuencia direccionWeb = secOferta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DIRECCION_WEB));
        if(direccionWeb != null){
            oferta.setDireccionWeb(direccionWeb.getValor());
        }
        Secuencia fechaFinOferta = secOferta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA_FIN));
        if(fechaFinOferta != null){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaFin = sdf.parse(fechaFinOferta.getValor());
                 oferta.setFechaFinOferta(new Timestamp(fechaFin.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(ConversorOferta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Secuencia periodoVinculacion = secOferta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PERIODO_VINCULACION));
        if(periodoVinculacion != null){
            oferta.setPeriodoVinculacion(periodoVinculacion.getValor());
        }
        Secuencia requisitos = secOferta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_REQUISITOS));
        if(requisitos != null){
            oferta.setRequisitos(requisitos.getValor());
        }
        Secuencia correoContacto = secOferta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO_CONTACTO));
        if(correoContacto != null){
            oferta.setCorreoContacto(correoContacto.getValor());
        }
        oferta.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        oferta.setId(null);
        ofertaFacade.create(oferta);
        return oferta;
    }

    /**
    * Crea un proponente a partir de una secuencia
    * @param secProponente Secuencia
    * @return Proponente construído a partir de la secuencia dada
    */
    public Proponente crearProponenteAPartirDeSecuencia(Secuencia secProponente){
        if(secProponente == null)
            return null;
        Proponente proponente = new Proponente();

        //Información de la persona
        Secuencia secCorreo = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO));
        Persona persona = personaFacade.findByCorreo(secCorreo.getValor());
        if(persona == null){
            persona = new Persona();
            persona.setCorreo(secCorreo.getValor());
            personaFacade.create(persona);
        }
        Secuencia secNombres = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES));
        if(secNombres != null){
            persona.setNombres(secNombres.getValor());
        }
        Secuencia secApellidos = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if(secApellidos != null){
            persona.setApellidos(secApellidos.getValor());
        }
        Secuencia secTelefono = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO));
        if(secTelefono != null){
            persona.setTelefono(secTelefono.getValor());
        }
        Secuencia secCelular = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR));
        if(secCelular != null){
            persona.setCelular(secCelular.getValor());
        }
        Secuencia secExtension = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_EXTENSION));
        if(secExtension != null){
            persona.setExtension(secExtension.getValor());
        }
        personaFacade.edit(persona);
        proponente.setPersona(persona);

        //Información empresarial
        Secuencia secEsEmpresa = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EMPRESA));
        if(secEsEmpresa != null){
            proponente.setEsEmpresa(Boolean.parseBoolean(secEsEmpresa.getValor()));
        }
        if (proponente.isEsEmpresa()) {
            Secuencia secEmpresa = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_EMPRESA));
            InformacionEmpresa infoEmpresa = new InformacionEmpresa();
            Secuencia secNombreEmpresa = secEmpresa.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE_EMPRESA));
            if(secNombreEmpresa != null){
                infoEmpresa.setNombreEmpresa(secNombreEmpresa.getValor());
            }
            Secuencia secContacto = secProponente.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CONTACTO_EMPRESA));
            Secuencia secCargo = secContacto.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CARGO));
            if(secCargo != null){
                infoEmpresa.setCargoContactoEmpresa(secCargo.getValor());
            }
            informacionEmpresaFacade.create(infoEmpresa);
            proponente.setInformacionEmpresa(infoEmpresa);
        }
        proponenteFacade.create(proponente);
        return proponente;
    }



    /**
    * Crea un proponente a partir de un correo dado.
    * Si existe una persona dado el correo, éste la asigna al proponente respectivo.
    * En caso contrario, crea una nueva persona con el correo dado.
    * @param secProponente Secuencia
    * @return Proponente construído a partir de la secuencia dada
    */
    public Proponente crearProponenteAPartirDePersona(String correo){
        Proponente proponente = new Proponente();

        //Información de la persona
        Persona persona = personaFacade.findByCorreo(correo);
        if(persona == null){
            persona = new Persona();
            persona.setCorreo(correo);
            personaFacade.create(persona);
        }
        proponente.setPersona(persona);
        proponente.setEsEmpresa(false);
        proponente.setId(null);
        proponenteFacade.create(proponente);
        return proponente;
    }

    //----------------------------------------------
    // MÉTODOS DE CONSULTA DE ENTIDADES
    //----------------------------------------------
    /**
     * Retorna a una persona dada una secuencia con información de la misma
     * La persona es buscada por: correo electrónico
     * @param secPersona Secuencia
     * @return Persona cuyos atributos coinciden con la información incluída en la Secuencia
     */
    public Persona consultarPersonaPorCorreo(Secuencia secPersona) {
        if(secPersona == null)
            return null;
        Persona persona = null;
        Secuencia secCorreo = secPersona.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO));
        if(secCorreo != null){
            persona = personaFacade.findByCorreo(secCorreo.getValor());
        }
        return persona;
    }

    /**
     * Retorna a un estudiante dada una secuencia con información del mismo
     * El estudiante es buscado por: correo electrónico
     * @param secEstudiante Secuencia
     * @return Estudiante cuyos atributos coinciden con la información incluída en la Secuencia
     */
    public Estudiante consultarEstudiantePorCorreo(Secuencia secEstudiante) {
        if(secEstudiante == null)
            return null;
        Estudiante estudiante = null;
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO));
        if(secCorreo != null){
            estudiante = estudianteFacade.findByCorreo(secCorreo.getValor());
        }
        return estudiante;
    }

    /**
     * Retorna a un período dada una secuencia con información del mismo
     * El período es buscado por: nombre
     * @param secPeriodo Secuencia
     * @return Periodo cuyos atributos coinciden con la información incluída en la Secuencia
     */
    public Periodo consultarPeriodoPorPeriodo(Secuencia secPeriodo){
        if(secPeriodo == null)
            return null;
        Periodo periodo = null;
        Secuencia secNombrePeriodo = secPeriodo.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE));
        if(secNombrePeriodo != null){
            periodo = periodoFacade.findByPeriodo(secNombrePeriodo.getValor());
        }
        return periodo;
    }

    /**
     * Retorna a un tipo de asistencia gradada dada una secuencia con información del mismo
     * El tipo de asistencia es buscado por: nombre
     * @param secTipo  Secuencia
     * @return Tipo de asistencia graduada cuyos atributos coinciden con la información incluída en la Secuencia
     */
    private TipoAsistenciaGraduada consultarTipo(Secuencia secTipo) {
        if(secTipo == null)
            return null;
        TipoAsistenciaGraduada tipoAsistenciaGraduada = null;
        Secuencia secNombreTipo = secTipo.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRE));
        if(secNombreTipo != null){
            tipoAsistenciaGraduada = tipoAsistenciaFacade.findByTipo(secNombreTipo.getValor());
        }
        return tipoAsistenciaGraduada;
    }
}
