/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.EJB;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;

/**
 *
 * @author Asistente
 */
public class ConversorGeneral {

    /**
     * Parser
     */
    private ParserT parser;

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

     /**
     * Constructor.
     */
    public ConversorGeneral(String xml) {
        try{
            getParser().leerXML(xml);
            ServiceLocator serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String obtenerResponsable(){
        return getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL));
    }

        /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }


}
