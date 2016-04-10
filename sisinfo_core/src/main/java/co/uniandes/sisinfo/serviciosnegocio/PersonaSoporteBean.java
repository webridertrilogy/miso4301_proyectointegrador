package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.PersonaSoporte;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PersonaSoporteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Juan Manuel Moreno B.
 */
@Stateless
public class PersonaSoporteBean implements PersonaSoporteBeanLocal, PersonaSoporteBeanRemote {

    /**
     * Facade.
     */
    @EJB
    private PersonaSoporteFacadeLocal facadePersonaSoporte;

    /**
     * Constantes.
     */
    @EJB
    private ConstanteRemote constanteBean;

    /**
     * ParserT.
     */
    private ParserT parser;

    /**
     * Correo.
     */
    @EJB
    private CorreoRemote correoBean;
    
    /**
     * Locator.
     */
    private ServiceLocator serviceLocator;
    public PersonaSoporteBean() {

        try {
            
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            /*alertaBean = (AlertaRemote) serviceLocator.getRemoteEJB(AlertaRemote.class);
            tareaFacade = (TareaFacadeRemote) serviceLocator.getRemoteEJB(TareaFacadeRemote.class);
            tareaBean = (TareaRemote) serviceLocator.getRemoteEJB(TareaRemote.class);*/
        } catch (NamingException ex) {
            Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Consulta personas de soporte.
     */
    public String getListPersonaSoporte(String xml) {
        
        try {

            parser.leerXML(xml);
            List<PersonaSoporte> listPersona = facadePersonaSoporte.findAll();
            Secuencia secListPersonaSoporte = pasarListPersonaSoporteASecuencia(listPersona);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secListPersonaSoporte);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_PERSONA_SOPORTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_PERSONA_SOPORTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
      * Consulta personas de soporte por su Id.
      */
    public String getPersonaSoportePorId(String xml) {

        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor());
            PersonaSoporte personaSoporte = facadePersonaSoporte.find(id);
            Secuencia secP = pasarPersonaSoporteASecuencia(personaSoporte);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secP);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PERSONA_SOPORTE_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PERSONA_SOPORTE_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }
    
    /**
     * Pasa una lista de personas a una secuencia XML.
     * @param incidentes
     * @return
     */
    private Secuencia pasarListPersonaSoporteASecuencia(List<PersonaSoporte> personas) {
        Secuencia secIncidentes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_PERSONA_SOPORTE), null);
        for (PersonaSoporte persona : personas) {
            
            Secuencia secInc = pasarPersonaSoporteASecuencia(persona);
            secIncidentes.agregarSecuencia(secInc);
        }
        return secIncidentes;
    }

    /**
     * Pasa una persona de soporte a una secuencia.
     * @param incidentes
     * @return
     */
    private Secuencia pasarPersonaSoporteASecuencia(PersonaSoporte personaSoporte) {
        Secuencia secPersonaSoporte = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA_SOPORTE), null);
        Secuencia secTmp = null;
        if (personaSoporte.getId() != null) {

            secTmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), personaSoporte.getId().toString());
            secPersonaSoporte.agregarSecuencia(secTmp);
        }
        if (personaSoporte.getPersona() != null) {
            
            secTmp = pasarPersonaASecuencia(personaSoporte.getPersona());
            secPersonaSoporte.agregarSecuencia(secTmp);
        }
        return secPersonaSoporte;
    }

    /**
     * Pasa una persona a secuencia.
     * TODO: Ver como no reescribirlo.
     * @param p
     * @return
     */
    private Secuencia pasarPersonaASecuencia(Persona p) {

        Secuencia secPersona = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA), null);
        if (p.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), p.getId().toString());
            secPersona.agregarSecuencia(secId);
        }
        if (p.getNombres() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), p.getNombres());
            secPersona.agregarSecuencia(secNombres);
        }
        if (p.getApellidos() != null) {
            Secuencia secApellido = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), p.getApellidos());
            secPersona.agregarSecuencia(secApellido);
        }
        if (p.getCorreo() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), p.getCorreo());
            secPersona.agregarSecuencia(secCorreo);
        }
        return secPersona;
    }
        
    /**
     * Da constanteBean.
     * @return
     */
    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    /**
     * Da parser.
     * @return
     */
    public ParserT getParser() {
        return parser;
    }
}
