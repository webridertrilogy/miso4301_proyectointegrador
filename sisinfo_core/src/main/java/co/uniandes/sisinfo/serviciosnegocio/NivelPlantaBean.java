/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.datosmaestros.NivelPlanta;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelPlantaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Administrador
 */
@Stateless
public class NivelPlantaBean implements NivelPlantaBeanRemote, NivelPlantaBeanLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private NivelPlantaFacadeRemote nivelPlantaFacade;
    private ServiceLocator serviceLocator;

    //---------------------------------------
    //Métodos
    //---------------------------------------
    public NivelPlantaBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            nivelPlantaFacade = (NivelPlantaFacadeRemote) serviceLocator.getRemoteEJB(NivelPlantaFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(NivelPlantaBean.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }

    public String consultarNivelesPlanta(String comando) {
        try {
            getParser().leerXML(comando);
            List<NivelPlanta> lista = nivelPlantaFacade.findAll();
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

            Secuencia secNivelesPlanta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVELES_PLANTA), getConstanteBean().getConstante(Constantes.NULL));

            Iterator<NivelPlanta> iterador = lista.iterator();
            while (iterador.hasNext()) {
                NivelPlanta nivel = iterador.next();
                Secuencia secNivelPlanta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PLANTA), nivel.getNombre());
                secNivelesPlanta.agregarSecuencia(secNivelPlanta);
            }
            secuencias.add(secNivelesPlanta);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_NIVELES_PLANTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(NivelPlantaBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

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
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
}
