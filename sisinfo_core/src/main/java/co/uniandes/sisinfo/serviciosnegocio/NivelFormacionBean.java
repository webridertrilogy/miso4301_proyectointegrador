/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
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
public class NivelFormacionBean implements NivelFormacionRemote, NivelFormacionLocal {

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
    private NivelFormacionFacadeRemote nivelFormacionFacade;

    private ServiceLocator serviceLocator;
    //---------------------------------------
    //Métodos
    //---------------------------------------

    public NivelFormacionBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB( ConstanteRemote.class);
            nivelFormacionFacade = (NivelFormacionFacadeRemote)serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            } catch (NamingException ex) {
            Logger.getLogger(NivelFormacionBean.class.getName()).log(Level.SEVERE, null, ex);
          
        }
    }

    public String consultarNivelesFormacion(String comando) {
        try {
            getParser().leerXML(comando);            
            List<NivelFormacion> lista = nivelFormacionFacade.findAll();
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

            Secuencia secNivelesFormacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVELES_FORMACION), getConstanteBean().getConstante(Constantes.NULL));

            Iterator<NivelFormacion> iterador = lista.iterator();
            while(iterador.hasNext())
            {
                NivelFormacion nivel = iterador.next();
                Secuencia secNivelFormacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), nivel.getNombre());
                secNivelesFormacion.agregarSecuencia(secNivelFormacion);
            }
            secuencias.add(secNivelesFormacion);            
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_NIVELES_FORMACION),getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE) , Mensajes.COM_MSJ_0001, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(NivelFormacionBean.class.getName()).log(Level.SEVERE, null, ex);
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
