/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.ComandoXML;
import co.uniandes.sisinfo.entities.ListaBlancaErroresSisinfo;
import co.uniandes.sisinfo.entities.ListaComandoXML;
import co.uniandes.sisinfo.serviciosfuncionales.ComandoXMLFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ListaBlancaErroresSisinfoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ListaComandoXMLFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Juan Manuel Moreno B.
 */
@Stateless
public class AuditoriaBean implements  AuditoriaBeanLocal {

    @EJB
    private ComandoXMLFacadeLocal comandoXMLFacade;

    @EJB
    private ListaComandoXMLFacadeLocal listaComandoXMLFacade;

    @EJB
    private ListaBlancaErroresSisinfoFacadeLocal listaBlancaFacade;
    
    private ParserT parser;
    private ServiceLocator serviceLocator;
    @EJB
    private ConstanteLocal constanteBean;

    public AuditoriaBean() {
//        try {
//            
//            parser = new ParserT();
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//        } catch (Exception ex) {
//
//            Logger.getLogger(AuditoriaBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    public ParserT getParser() {
        return parser;
    }
    
    /**
     * Guarda un comando
     * @param xml
     * @return
     */
    public void guardarXML(String nombre, String xml) {

        // Busca el comando en la lista de comandos.
        List<ListaComandoXML> listaComando = listaComandoXMLFacade.findByNombre(nombre);
        if (listaComando != null && !listaComando.isEmpty()) {

            // Guarda el comando.
            ComandoXML comando = new ComandoXML();
            comando.setNombre(nombre);
            comando.setContenido(xml);
            comando.setFecha(new Timestamp(new Date().getTime()));
            comandoXMLFacade.create(comando);
        }
    }

    /**
     * Pasa lista blanca a una secuencia XML.
     * @param lista blanca
     * @return
     */
    private Secuencia pasarListaBlancaASecuencia(List<ListaBlancaErroresSisinfo> listaBlanca) {
        Secuencia secListaBlanca = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_BLANCA), null);
        for (ListaBlancaErroresSisinfo error : listaBlanca) {

            Secuencia secError = pasarErrorListaBlancaASecuencia(error);
            secListaBlanca.agregarSecuencia(secError);
        }
        return secListaBlanca;
    }

    /**
     * Pasa un error de lista blanca a una secuencia.
     * @param error
     * @return
     */
    private Secuencia pasarErrorListaBlancaASecuencia(ListaBlancaErroresSisinfo error) {
        Secuencia secError = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ERROR_LISTA_BLANCA), null);
        Secuencia secTmp = null;
        if (error.getId() != null) {

            secTmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), error.getId().toString());
            secError.agregarSecuencia(secTmp);
        }
        if (error.getIdError() != null) {

            secTmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ERROR), error.getIdError());
            secError.agregarSecuencia(secTmp);
        }
        if (error.getExplicacion() != null) {

            secTmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXPLICACION), error.getExplicacion());
            secError.agregarSecuencia(secTmp);
        }
        return secError;
    }
    
    /**
     * Consulta lista blanca.
     */
    public String getListaBlanca(String xml) {

        try {

            parser.leerXML(xml);
            List<ListaBlancaErroresSisinfo> listaBlanca = listaBlancaFacade.findAll();
            Secuencia secListaBlanca = pasarListaBlancaASecuencia(listaBlanca);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secListaBlanca);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_BLANCA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {

            try {
                
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_BLANCA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Elimina un error de lista blanca.
     * @param xml
     * @return
     */
    public String eliminarErrorListaBlanca(String xml) {
        try {
            
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor());
            ListaBlancaErroresSisinfo error = listaBlancaFacade.find(id);
            listaBlancaFacade.remove(error);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ERROR_LISTA_BLANCA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {

            try {
                
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ERROR_LISTA_BLANCA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Pasa lista de comandos a una secuencia XML.
     * @param lista de comandos
     * @return
     */
    private Secuencia pasarListaComandoXMLASecuencia(List<ListaComandoXML> listaComando) {
        Secuencia secListaBlanca = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_COMANDO), null);
        for (ListaComandoXML comando : listaComando) {

            Secuencia secError = pasarEntityListaComandoXMLASecuencia(comando);
            secListaBlanca.agregarSecuencia(secError);
        }
        return secListaBlanca;
    }
    
    /**
     * Pasa un comando de lista de comandos a una secuencia.
     * @param comando
     * @return
     */
    private Secuencia pasarEntityListaComandoXMLASecuencia(ListaComandoXML comando) {
        Secuencia secComando = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_LISTA_COMANDO), null);
        Secuencia secTmp = null;
        if (comando.getId() != null) {

            secTmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), comando.getId().toString());
            secComando.agregarSecuencia(secTmp);
        }
        if (comando.getNombre() != null) {

            secTmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMANDO), comando.getNombre());
            secComando.agregarSecuencia(secTmp);
        }
        return secComando;
    }
    
    /**
      * Consulta lista de comandos para guardar.
      */
    public String getListaComandoXML(String xml) {

        try {

            parser.leerXML(xml);
            List<ListaComandoXML> listaComando = listaComandoXMLFacade.findAll();
            Secuencia secListaBlanca = pasarListaComandoXMLASecuencia(listaComando);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secListaBlanca);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_COMANDO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {

            try {

                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_COMANDO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {

                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Elimina un comando de lista de comandos.
     * @param xml
     * @return
     */
    public String eliminarComandoListaComando(String xml) {
        try {

            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor());
            ListaComandoXML comando = listaComandoXMLFacade.find(id);
            listaComandoXMLFacade.remove(comando);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ERROR_LISTA_BLANCA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {

            try {

                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ERROR_LISTA_BLANCA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {

                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }
}
