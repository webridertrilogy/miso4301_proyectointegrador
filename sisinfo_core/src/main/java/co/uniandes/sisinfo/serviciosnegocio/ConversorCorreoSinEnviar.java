/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.CorreoSinEnviar;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Asistente
 */
public class ConversorCorreoSinEnviar {

        private ConstanteLocal constanteBean;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public ConversorCorreoSinEnviar(ConstanteLocal constanteBean) {
        this.constanteBean = constanteBean;
    }

    public Secuencia crearSecuenciaCorreosSinEnviar(Collection<CorreoSinEnviar> correos){
        Secuencia secCorreos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREOS_SIN_ENVIAR),"");
        for (CorreoSinEnviar correo : correos) {
            secCorreos.agregarSecuencia(crearSecuenciaCorreo(correo));
        }
        return secCorreos;
    }

    public Secuencia crearSecuenciaCorreo(CorreoSinEnviar correo){
        Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_SIN_ENVIAR),"");
        secCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CORREO),correo.getId()+""));
        secCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_PARA),correo.getPara()));
        secCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ASUNTO),correo.getAsunto()));
        secCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CC),correo.getCc()));
        secCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CCO),correo.getCco()));
        secCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ARCHIVO),correo.getArchivo()));
        secCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_MENSAJE),correo.getMensaje()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        secCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA),correo.getFechaEnvio()!=null?sdf.format(correo.getFechaEnvio()):""));
        return secCorreo;
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    public CorreoSinEnviar crearCorreoDeSecuencia(Secuencia sec){
        CorreoSinEnviar correo = new CorreoSinEnviar();
        String strId = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CORREO))).getValor();
        Long id = Long.parseLong(strId);
        correo.setId(id);
        String strPara = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_PARA))).getValor();
        correo.setPara(strPara);
        String strAsunto = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ASUNTO))).getValor();
        correo.setAsunto(strAsunto);
        String strCC = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CC))).getValor();
        correo.setCc(strCC);
        String strCCO = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CCO))).getValor();
        correo.setCco(strCCO);
        String strArchivo = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ARCHIVO))).getValor();
        correo.setArchivo(strArchivo);
        String strMensaje = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_MENSAJE))).getValor();
        correo.setMensaje(strMensaje);
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String strFecha = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA))).getValor();
            correo.setFechaEnvio(new Timestamp(sdf.parse(strFecha).getTime()));
        }catch(Exception e){
            correo.setFechaEnvio(null);
        }
        return correo;
    }

    public Collection<CorreoSinEnviar> crearCorreosDeSecuencia(Secuencia sec){
        Collection<CorreoSinEnviar> correos = new ArrayList();
        for (Secuencia secuencia : sec.getSecuencias()) {
            correos.add(crearCorreoDeSecuencia(secuencia));
        }
        return correos;
    }


}
