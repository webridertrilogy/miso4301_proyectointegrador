package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.EstudiantePosgrado;
import co.uniandes.sisinfo.entities.HojaVida;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.soporte.Pais;
import co.uniandes.sisinfo.entities.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.EstudiantePosgradoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.HojaVidaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 * Conversor del módulo de Asistencias Graduadas y Bolsa de Empleo
 * @author Marcela Morales
 */
public class ConversorEstudiante {

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
    private PaisFacadeRemote paisFacade;
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentoFacade;
    @EJB
    private InformacionAcademicaFacadeRemote informacionAcademicaFacade;
    @EJB
    private HojaVidaFacadeRemote hojaVidaFacade;
    @EJB
    private EstudiantePosgradoFacadeRemote estudiantePostgradoFacade;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------

    /**
     * Conversor del módulo de asistencias graduadas
     * @param constanteBean Referencia a los servicios de las constantes
     */
    public ConversorEstudiante(ConstanteRemote constanteBean,
            EstudianteFacadeRemote estudianteFacade,
            PersonaFacadeRemote personaFacade,
            PaisFacadeRemote paisFacade,
            TipoDocumentoFacadeRemote tipoDocumentofacade,
            InformacionAcademicaFacadeRemote informacionAcademicaFacade,
            HojaVidaFacadeRemote hojaVidaFacade,
            EstudiantePosgradoFacadeRemote estudiantePosgradoFacade) {
        //try {
            serviceLocator = new ServiceLocator();
            this.constanteBean = constanteBean;
            this.estudianteFacade = estudianteFacade;
            this.personaFacade = personaFacade;
            this.paisFacade = paisFacade;
            this.tipoDocumentoFacade = tipoDocumentofacade;
            this.informacionAcademicaFacade = informacionAcademicaFacade;
            this.hojaVidaFacade = hojaVidaFacade;
            this.estudiantePostgradoFacade = estudiantePosgradoFacade;
        /*} catch (NamingException ex) {
            Logger.getLogger(ConversorEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A SECUENCIAS
    //----------------------------------------------

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
        secuenciaEstudiante.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ES_EXTERNO), (estudiantePosgrado.isEsExterno() != null) ? estudiantePosgrado.isEsExterno().toString() : ""));

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

        Secuencia secuenciaInformacionPersonal = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), "");
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getApellidos() != null) ? estudiante.getPersona().getApellidos() : "" ));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NACIONALIDAD), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getPais() != null && estudiante.getPersona().getPais().getNombre() != null) ? estudiante.getPersona().getPais().getNombre() : "" ));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getNombres() != null) ? estudiante.getPersona().getNombres(): ""));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DOCUMENTO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getNumDocumentoIdentidad() != null) ? estudiante.getPersona().getNumDocumentoIdentidad(): ""));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getCelular() != null) ? estudiante.getPersona().getCelular(): ""));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO_FIJO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getTelefono() != null) ? estudiante.getPersona().getTelefono(): ""));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), (estudiante != null && estudiante.getPersona() != null && estudiante.getPersona().getTipoDocumento() != null && estudiante.getPersona().getTipoDocumento().getDescripcion() != null) ? estudiante.getPersona().getTipoDocumento().getDescripcion(): ""));
        secuenciaEstudiante.agregarSecuencia(secuenciaInformacionPersonal);

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
            Secuencia secEstudiante = crearSecuenciaEstudiante(estudiante);
            secEstudiantes.agregarSecuencia(secEstudiante);
        }
        return secEstudiantes;
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
                    Logger.getLogger(ConversorEstudiante.class.getName()).log(Level.SEVERE, null, ex);
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
}
