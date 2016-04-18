/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.util.ArrayList;
import java.util.Collection;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.CondicionFiltroCorreo;
import co.uniandes.sisinfo.entities.FiltroCorreo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Asistente
 */
public class ConversorFiltrosCorreo {

    private ConstanteRemote constanteBean;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public ConversorFiltrosCorreo(ConstanteRemote constanteBean) {
        this.constanteBean = constanteBean;
    }

    public Secuencia crearSecuenciasFiltros(Collection<FiltroCorreo> filtros){
        Secuencia secFiltros = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FILTROS_CORREO),"");
        for (FiltroCorreo filtro : filtros) {
            secFiltros.agregarSecuencia(crearSecuenciaFiltro(filtro));
        }
        return secFiltros;
    }

    public Secuencia crearSecuenciaFiltro(FiltroCorreo filtro){
        Secuencia secFiltro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FILTRO_CORREO),"");
        secFiltro.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_FILTRO),filtro.getId()+""));
        secFiltro.agregarSecuencia(crearSecuenciaCondicionesFiltro(filtro.getCondiciones()));
        if(filtro.getRedireccion()!=null)
            secFiltro.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REDIRECCION),filtro.getRedireccion()));
        if(filtro.getDescripcion()!=null)
            secFiltro.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION),filtro.getDescripcion()));
        return secFiltro;
    }

    public Secuencia crearSecuenciaCondicionFiltro(CondicionFiltroCorreo condicion){
        Secuencia secCondicion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONDICION_FILTRO_CORREO),"");
        secCondicion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO),condicion.getTipo()));
        secCondicion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR),condicion.getValor()));
        secCondicion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERADOR),condicion.getOperacion()));
        return secCondicion;
    }

    public Secuencia crearSecuenciaCondicionesFiltro(Collection<CondicionFiltroCorreo> condiciones){
        Secuencia secCondiciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONDICIONES_FILTRO_CORREO),"");
        for (CondicionFiltroCorreo condicion : condiciones) {
            secCondiciones.agregarSecuencia(crearSecuenciaCondicionFiltro(condicion));
        }
        return secCondiciones;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public FiltroCorreo crearFiltroCorreoDeSecuencia(Secuencia sec){
        FiltroCorreo filtro = new FiltroCorreo();
        String strId = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_FILTRO))).getValor();
        try{
            Long id = Long.parseLong(strId);
            filtro.setId(id);
        }catch(Exception e){
            filtro.setId(null);
        }
        Secuencia secuenciaCondiciones = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_CONDICIONES_FILTRO_CORREO)));
        filtro.setCondiciones(crearCondicionesFiltroCorreoDesdeSecuencia(secuenciaCondiciones));
        Secuencia secRedireccion = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_REDIRECCION)));
        if(secRedireccion!=null){
            String strRedireccion = secRedireccion.getValor();
            filtro.setRedireccion(strRedireccion);
        }
        Secuencia secDescripcion = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)));
        if(secDescripcion!=null){
            String strDescripcion = secDescripcion.getValor();
            filtro.setDescripcion(strDescripcion);
        }

        return filtro;
    }
    public CondicionFiltroCorreo crearCondicionFiltroCorreoDesdeSecuencia(Secuencia sec){
        CondicionFiltroCorreo condicion = new CondicionFiltroCorreo();
        String strTipo = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO))).getValor();
        condicion.setTipo(strTipo);
        String strValor = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR))).getValor();
        condicion.setValor(strValor);
        String strOperador = sec.obtenerSecuenciaHija((getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERADOR))).getValor();
        condicion.setOperacion(strOperador);
        return condicion;
    }

    public Collection<CondicionFiltroCorreo> crearCondicionesFiltroCorreoDesdeSecuencia(Secuencia sec){
        Collection<CondicionFiltroCorreo> condiciones = new ArrayList<CondicionFiltroCorreo>();
        for (Secuencia secuencia : sec.getSecuencias()) {
            condiciones.add(crearCondicionFiltroCorreoDesdeSecuencia(secuencia));
        }
        return condiciones;
    }

}
