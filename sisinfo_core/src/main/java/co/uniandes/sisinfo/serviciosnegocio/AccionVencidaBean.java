/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.AccionVencida;
import co.uniandes.sisinfo.serviciosfuncionales.AccionVencidaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Juan Manuel Moreno B.
 */
@Stateless
public class AccionVencidaBean implements AccionVencidaBeanRemote, AccionVencidaBeanLocal {

    @EJB
    private AccionVencidaFacadeLocal accionVencidaFacade;

    private ParserT parser;

    private ServiceLocator serviceLocator;
    
    @EJB
    private ConstanteRemote constanteBean;

    public AccionVencidaBean() {
        try {
            
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (Exception ex) {

            Logger.getLogger(AccionVencidaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ParserT getParser() {
        return parser;
    }
    
    /**
     * Guarda una accion vencida
     * @param xml
     * @return
     */
    public void guardarAccionVencida(String xml) {

        AccionVencida accion = new AccionVencida();
        accionVencidaFacade.create(accion);
    }

    /**
     * Guarda una accion vencida
     * @return
     */
    public void guardarAccionVencida(
            Timestamp fechaAcordada, Timestamp fechaEjecucion,
            String accion, String usuario,
            String proceso, String modulo,
            String comando, String infoAdicional) {

        AccionVencida vencida = new AccionVencida();
        vencida.setFechaAcordada(fechaAcordada);
        vencida.setFechaEjecucion(fechaEjecucion);
        vencida.setAccion(accion);
        vencida.setUsuario(usuario);
        vencida.setProceso(proceso);
        vencida.setModulo(modulo);
        vencida.setComando(comando);
        vencida.setInfoAdicional(infoAdicional);
        accionVencidaFacade.create(vencida);
    }

    /**
     * Da lista de acciones vencidas
     * @param xml
     * @return
     */
    public String darListaAccionVencida(String xml) {

        try {

            parser.leerXML(xml);
            List<AccionVencida> acciones = accionVencidaFacade.findAll();
            Secuencia secAcciones = pasarAccionesASecuencia(acciones);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secAcciones);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LISTA_ACCION_VENCIDA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LISTA_ACCION_VENCIDA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Pasa una persona de soporte a una secuencia.
     * @param incidentes
     * @return
     */
    private Secuencia pasarAccionesASecuencia(List<AccionVencida> acciones) {
        Secuencia secAcciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACCION_VENCIDA), null);
        for (AccionVencida accion : acciones) {
            Secuencia secAccion = pasarAccionASecuencia(accion);
            secAcciones.agregarSecuencia(secAccion);
        }
        return secAcciones;
    }

    /**
     * Pasa una accion a secuencia.
     * @param p
     * @return
     */
    private Secuencia pasarAccionASecuencia(AccionVencida accion) {

        Secuencia secAccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA), null);
        if (accion.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), accion.getId().toString());
            secAccion.agregarSecuencia(secId);
        }
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (accion.getFechaAcordada() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA), accion.getFechaAcordada().toString());
            secAccion.agregarSecuencia(secNombres);
        }
        if (accion.getFechaEjecucion() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ERROR), accion.getFechaEjecucion().toString());
            secAccion.agregarSecuencia(secNombres);
        }
        if (accion.getAccion() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACCION), accion.getAccion().toString());
            secAccion.agregarSecuencia(secNombres);
        }
        if (accion.getUsuario() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO), accion.getUsuario().toString());
            secAccion.agregarSecuencia(secNombres);
        }
        if (accion.getProceso() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROCESO), accion.getProceso().toString());
            secAccion.agregarSecuencia(secNombres);
        }
        if (accion.getModulo() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MODULO_SISINFO), accion.getModulo().toString());
            secAccion.agregarSecuencia(secNombres);
        }
        if (accion.getComando() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO), accion.getComando().toString());
            secAccion.agregarSecuencia(secNombres);
        }
        if (accion.getInfoAdicional() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS), accion.getInfoAdicional().toString());
            secAccion.agregarSecuencia(secNombres);
        }
        return secAccion;
    }
}
