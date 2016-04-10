/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.SubareaInvestigacion;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SubareaInvestigacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.SubareaInvestigacionBeanRemote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Asistente
 */
@Stateless
public class SubareaInvestigacionBean implements SubareaInvestigacionBeanRemote, SubareaInvestigacionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * ProfesorFacade
     */
    @EJB
    private SubareaInvestigacionFacadeLocal subareaFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    private ServiceLocator serviceLocator;

    /**
     * constructor
     */
    public SubareaInvestigacionBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(SubareaInvestigacionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String obtenerSubareasInvestigacion(String xml) {
        try {
            parser.leerXML(xml);
            Collection<SubareaInvestigacion> subareas = subareaFacade.findAll();
            Secuencia secSubareas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREAS), "");
            for (SubareaInvestigacion subareaInvestigacion : subareas) {
                Secuencia secSubA = pasarSubareaASecuencia(subareaInvestigacion);
                secSubareas.agregarSecuencia(secSubA);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secSubareas);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SUBAREAS_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new LinkedList<Secuencia>());

        } catch (Exception ex) {
            Logger.getLogger(SubareaInvestigacionBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ParserT getParser() {
        return parser;
    }

    private Secuencia pasarSubareaASecuencia(SubareaInvestigacion subareaInvestigacion) {
        Secuencia secSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AREA), "");

        Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), subareaInvestigacion.getId().toString());
        secSubarea.agregarSecuencia(secId);

        Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), subareaInvestigacion.getNombreSubarea());
        secSubarea.agregarSecuencia(secNombre);

        if (subareaInvestigacion.getDescripcion() != null) {
            Secuencia secDescripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), subareaInvestigacion.getDescripcion());
            secSubarea.agregarSecuencia(secDescripcion);
        }

        return secSubarea;

    }
}
