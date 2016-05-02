package co.uniandes.sisinfo.serviciosnegocio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeLocal;

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
    private ConstanteLocal constanteBean;
    @EJB
    private ServiceLocator serviceLocator;
    //Servicios
    @EJB
    private EstudianteFacadeLocal estudianteFacade;
    @EJB
    private PersonaFacadeLocal personaFacade;
    @EJB
    private PaisFacadeLocal paisFacade;
    @EJB
    private TipoDocumentoFacadeLocal tipoDocumentoFacade;
    @EJB
    private InformacionAcademicaFacadeLocal informacionAcademicaFacade;
   
    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------

    /**
     * Conversor del módulo de asistencias graduadas
     * @param constanteBean Referencia a los servicios de las constantes
     */
    public ConversorEstudiante(ConstanteLocal constanteBean,
            EstudianteFacadeLocal estudianteFacade,
            PersonaFacadeLocal personaFacade,
            PaisFacadeLocal paisFacade,
            TipoDocumentoFacadeLocal tipoDocumentofacade,
            InformacionAcademicaFacadeLocal informacionAcademicaFacade
            ) {
        //try {
            serviceLocator = new ServiceLocator();
            this.constanteBean = constanteBean;
            this.estudianteFacade = estudianteFacade;
            this.personaFacade = personaFacade;
            this.paisFacade = paisFacade;
            this.tipoDocumentoFacade = tipoDocumentofacade;
            this.informacionAcademicaFacade = informacionAcademicaFacade;
           
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
