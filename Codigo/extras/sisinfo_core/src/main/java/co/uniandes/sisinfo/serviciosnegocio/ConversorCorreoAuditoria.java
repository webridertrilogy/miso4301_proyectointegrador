package co.uniandes.sisinfo.serviciosnegocio;

import java.text.SimpleDateFormat;
import java.util.Collection;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.CorreoAuditoria;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Conversor de Correo Auditoría
 * @author Marcela Morales
 */
public class ConversorCorreoAuditoria {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Útil
    private ConstanteLocal constanteBean;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public ConversorCorreoAuditoria(ConstanteLocal constanteBean) {
        this.constanteBean = constanteBean;
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A SECUENCIAS
    //----------------------------------------------
    /**
     * Crea una secuencia dado un conjunto de correos de auditoría
     * @param correos Colección de correos de auditoría
     * @return Secuencia construída a partir de la colección de correos de auditoría dada
     */
    public Secuencia crearSecuenciaCorreos(Collection<CorreoAuditoria> correos) {
        Secuencia secCorreos = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREOS), "");
        for (CorreoAuditoria correo : correos) {
            Secuencia secCorreo = crearSecuenciaCorreoLite(correo);
            secCorreos.agregarSecuencia(secCorreo);
        }
        return secCorreos;
    }

    /**
     * Crea una secuencia dado un correo de auditoría
     * @param correo Correo de auditoría
     * @return Secuencia construída a partir del correo de auditoría dado
     */
    public Secuencia crearSecuenciaCorreo(CorreoAuditoria correo){
        if(correo == null)
            return null;
        Secuencia secCorreo = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), "");
        if(correo.getId() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_GENERAL), correo.getId().toString()));
        }
        if(correo.getAsunto() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ASUNTO), correo.getAsunto()));
        }
        if(correo.getDestinatarios() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DESTINATARIOS), correo.getDestinatarios()));
        }
        if(correo.getDestinatariosCC() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DESTINATARIOSCC), correo.getDestinatariosCC()));
        }
        if(correo.getDestinatariosCCO() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DESTINATARIOSCCO), correo.getDestinatariosCCO()));
        }
        if(correo.getMensaje() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_MENSAJE), correo.getMensaje()));
        }
        if(correo.getNombreAdjunto() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ARCHIVO), correo.getNombreAdjunto()));
        }
        if(correo.getEnviado() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ENVIA_CORREO), correo.getEnviado().toString()));
        }else{
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ARCHIVO), "false"));
        }
        if(correo.getFechaEnvio() != null){
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA), sdf.format(correo.getFechaEnvio())));
        }
        return secCorreo;
    }

    /**
     * Crea una secuencia dado un correo de auditoría
     * @param correo Correo de auditoría
     * @return Secuencia construída a partir del correo de auditoría dado
     */
    public Secuencia crearSecuenciaCorreoLite(CorreoAuditoria correo){
        if(correo == null)
            return null;
        Secuencia secCorreo = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), "");
        if(correo.getId() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_GENERAL), correo.getId().toString()));
        }
        if(correo.getAsunto() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ASUNTO), correo.getAsunto()));
        }
        if(correo.getDestinatarios() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DESTINATARIOS), correo.getDestinatarios()));
        }
        if(correo.getNombreAdjunto() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ARCHIVO), correo.getNombreAdjunto()));
        }
        if(correo.getEnviado() != null){
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ENVIA_CORREO), correo.getEnviado().toString()));
        }else{
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ARCHIVO), "false"));
        }
        if(correo.getFechaEnvio() != null){
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            secCorreo.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA), sdf.format(correo.getFechaEnvio())));
        }
        return secCorreo;
    }
}
