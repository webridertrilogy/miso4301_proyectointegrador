package co.uniandes.sisinfo.serviciosnegocio;

import java.text.SimpleDateFormat;
import java.util.Collection;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.AuditoriaUsuario;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Conversor de Auditoría Usuario
 * @author Paola Gómez
 */
public class ConversorAuditoriaUsuario {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Útil
    private ConstanteLocal constanteBean;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public ConversorAuditoriaUsuario(ConstanteLocal constanteBean) {
        this.constanteBean = constanteBean;
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A SECUENCIAS
    //----------------------------------------------
    /**
     * Crea una secuencia dado un conjunto de actividades de auditoría
     * @param actividadesAuditoria Colección de actividades de auditoría
     * @return Secuencia construída a partir de la colección de actividades de auditoría dada
     */
    public Secuencia crearSecuenciaActividadesAuditoria(Collection<AuditoriaUsuario> actividadesAuditoria) {
        Secuencia secActividadesAuditoria = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ACTIVIDADES_AUDITORIA), "");
        for (AuditoriaUsuario actividadAuditoria : actividadesAuditoria) {
            Secuencia secActividadAuditoria = crearSecuenciaActividadAuditoriaLite(actividadAuditoria);
            secActividadesAuditoria.agregarSecuencia(secActividadAuditoria);
        }
        return secActividadesAuditoria;
    }

    /**
     * Crea una secuencia dado una activida de auditoría
     * @param actividadAuditoria Actividad de auditoría
     * @return Secuencia construída a partir de la actividad de auditoría dada
     */
    public Secuencia crearSecuenciaActividadAuditoria(AuditoriaUsuario actividadAuditoria){
        if(actividadAuditoria == null)
            return null;
        Secuencia secActividadAuditoria = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ACTIVIDAD_AUDITORIA), "");
        if(actividadAuditoria.getId() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_GENERAL), actividadAuditoria.getId().toString()));
        }
        if(actividadAuditoria.getRol() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ROL_AUDITORIA), actividadAuditoria.getRol()));
        }
        if(actividadAuditoria.getUsuario() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), actividadAuditoria.getUsuario()));
        }
        if(actividadAuditoria.getComando() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_COMANDO_AUDITORIA), actividadAuditoria.getComando()));
        }
        if(actividadAuditoria.getAccionExitosa() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ACCION_EXITOSA_AUDITORIA), actividadAuditoria.getAccionExitosa().toString()));
        }else{
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ACCION_EXITOSA_AUDITORIA), "false"));
        }
        if(actividadAuditoria.getFecha() != null){
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA), sdf.format(actividadAuditoria.getFecha())));
        }
        if(actividadAuditoria.getParametros() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PARAMETROS_AUDITORIA), actividadAuditoria.getParametros()));
        }
        return secActividadAuditoria;
    }

    /**
     * Crea una secuencia dado una activida de auditoría
     * @param actividadAuditoria Actividad de auditoría
     * @return Secuencia construída a partir de la actividad de auditoría dada
     */
    public Secuencia crearSecuenciaActividadAuditoriaLite(AuditoriaUsuario actividadAuditoria){
        if(actividadAuditoria == null)
            return null;
        Secuencia secActividadAuditoria = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ACTIVIDAD_AUDITORIA), "");
        if(actividadAuditoria.getId() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_GENERAL), actividadAuditoria.getId().toString()));
        }
        if(actividadAuditoria.getRol() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ROL_AUDITORIA), actividadAuditoria.getRol()));
        }
        if(actividadAuditoria.getUsuario() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), actividadAuditoria.getUsuario()));
        }
        if(actividadAuditoria.getComando() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_COMANDO_AUDITORIA), actividadAuditoria.getComando()));
        }
        if(actividadAuditoria.getAccionExitosa() != null){
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ACCION_EXITOSA_AUDITORIA), actividadAuditoria.getAccionExitosa().toString()));
        }else{
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ACCION_EXITOSA_AUDITORIA), "false"));
        }
        if(actividadAuditoria.getFecha() != null){
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            secActividadAuditoria.agregarSecuencia(new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FECHA), sdf.format(actividadAuditoria.getFecha())));
        }
        return secActividadAuditoria;
    }
}
