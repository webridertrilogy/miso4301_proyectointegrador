/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.CorreoSinEnviar;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoSinEnviarFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Asistente
 */
@Stateless
public class CorreoSinEnviarBean implements CorreoSinEnviarBeanRemote, CorreoSinEnviarBeanLocal {

    private ParserT parser;

    private ServiceLocator serviceLocator;

    private ConversorCorreoSinEnviar conversorCorreoSinEnviar;

    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private CorreoSinEnviarFacadeLocal correoSinEnviarFacade;
    @EJB
    private CorreoLocal correoBean;

    public CorreoSinEnviarBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            conversorCorreoSinEnviar = new ConversorCorreoSinEnviar(constanteBean);
        } catch (NamingException ex) {
            Logger.getLogger(CorreoSinEnviarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void agregarCorreo(String para, String asunto, String cc, String cco, String archivo, String mensaje) {
        CorreoSinEnviar correo = new CorreoSinEnviar();
        correo.setArchivo(archivo);
        correo.setAsunto(asunto);
        correo.setCc(cc);
        correo.setCco(cco);
        correo.setMensaje(mensaje);
        correo.setPara(para);
        correo.setFechaEnvio(new Timestamp(new Date().getTime()));
        correoSinEnviarFacade.create(correo);

    }

    @Override
    public String darCorreosSinEnviar(String xml) {
        try {
            parser.leerXML(xml);
            Collection<CorreoSinEnviar> correosSinEnviar = correoSinEnviarFacade.findAll();
            Secuencia secCorreosSinEnviar = conversorCorreoSinEnviar.crearSecuenciaCorreosSinEnviar(correosSinEnviar);
            Collection<Secuencia> secuencias = new ArrayList();
            secuencias.add(secCorreosSinEnviar);
            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CORREOS_SIN_ENVIAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(CorreoSinEnviarBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }





    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    @Override
    public String eliminarCorreosSinEnviar(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secuenciaCorreos = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREOS_SIN_ENVIAR));
            for (Secuencia secuencia : secuenciaCorreos.getSecuencias()) {
                Long id = Long.parseLong(secuencia.getValor());
                CorreoSinEnviar correo = correoSinEnviarFacade.find(id);
                correoSinEnviarFacade.remove(correo);
            }
            return parser.generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CORREOS_SIN_ENVIAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(CorreoSinEnviarBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String enviarCorreosSinEnviar(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secuenciaCorreos = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREOS_SIN_ENVIAR));
            for (Secuencia secuencia : secuenciaCorreos.getSecuencias()) {
                Long id = Long.parseLong(secuencia.getValor());
                CorreoSinEnviar correo = correoSinEnviarFacade.find(id);
                correoBean.enviarMail(correo.getPara(), correo.getAsunto(), correo.getCc(), correo.getCco(), correo.getArchivo(), correo.getMensaje());
                correoSinEnviarFacade.remove(correo);
            }
            return parser.generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREOS_SIN_ENVIAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(CorreoSinEnviarBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darCorreoSinEnviar(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secuenciaId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secuenciaId.getValor());
            CorreoSinEnviar correo = correoSinEnviarFacade.find(id);

            Secuencia secCorreosSinEnviar = conversorCorreoSinEnviar.crearSecuenciaCorreo(correo);
            Collection<Secuencia> secuencias = new ArrayList();
            secuencias.add(secCorreosSinEnviar);
            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CORREOS_SIN_ENVIAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(CorreoSinEnviarBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String editarCorreoSinEnviar(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secuenciaCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_SIN_ENVIAR));
            CorreoSinEnviar correoNuevo = conversorCorreoSinEnviar.crearCorreoDeSecuencia(secuenciaCorreo);
            CorreoSinEnviar correo = correoSinEnviarFacade.find(correoNuevo.getId());

            correo.setArchivo(parsearValor(correoNuevo.getArchivo()));
            correo.setAsunto(parsearValor(correoNuevo.getAsunto()));
            correo.setCc(parsearValor(correoNuevo.getCc()));
            correo.setCco(parsearValor(correoNuevo.getCco()));
            correo.setFechaEnvio(correoNuevo.getFechaEnvio());
            correo.setMensaje(parsearValor(correoNuevo.getMensaje()));
            correo.setPara(parsearValor(correoNuevo.getPara()));
            
            correoSinEnviarFacade.edit(correo);
            Collection<Secuencia> secuencias = new ArrayList();
            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_CORREO_SIN_ENVIAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(CorreoSinEnviarBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Metodo utilizado para obtener los valores del xml. Si el parametro es una
     * cadena de caracteres vacia, retorna nulo
     * @return
     */
    public String parsearValor(String val){
        return val.trim().isEmpty()?null:val;
    }











    
    
 
}
