/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.util.Collection;

import co.uniandes.sisinfo.bo.AccionBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Asistente
 */
public class ConversorAcciones {
    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    private ConstanteLocal constanteBean;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public ConversorAcciones(ConstanteLocal constanteBean) {
        this.constanteBean = constanteBean;
    }

    public Secuencia crearSecuenciaAccion(AccionBO accionBO){
        Secuencia secuenciaAccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACCION),"");
        secuenciaAccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ACCION),accionBO.getNombre()));
        secuenciaAccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION_ACCION),accionBO.getDescripcion()));
        secuenciaAccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_ACCION),accionBO.getComando()));
        secuenciaAccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION_ACCION),accionBO.getSeccion()));
        return secuenciaAccion;
    }

    public Secuencia crearSecuenciaAcciones(Collection<AccionBO> acciones){
        Secuencia secuenciaAcciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACCIONES),"");
        for (AccionBO accionBO : acciones) {
            secuenciaAcciones.agregarSecuencia(crearSecuenciaAccion(accionBO));
        }
        return secuenciaAcciones;
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }


}
