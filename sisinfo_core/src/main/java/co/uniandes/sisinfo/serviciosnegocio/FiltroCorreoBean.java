/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.CondicionFiltroCorreo;
import co.uniandes.sisinfo.entities.FiltroCorreo;
import co.uniandes.sisinfo.serviciosfuncionales.FiltroCorreoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Asistente
 */
@Stateless
public class FiltroCorreoBean implements FiltroCorreoBeanRemote, FiltroCorreoBeanLocal {
    
    private ParserT parser;

    private ServiceLocator serviceLocator;
 
    private ConversorFiltrosCorreo conversorFiltrosCorreo;
            
    @EJB
    private ConstanteRemote constanteBean;
    
    @EJB
    private FiltroCorreoFacadeLocal filtroCorreoFacade;

    public FiltroCorreoBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            conversorFiltrosCorreo = new ConversorFiltrosCorreo(constanteBean);
        } catch (NamingException ex) {
            Logger.getLogger(CorreoSinEnviarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    @Override
    public String darFiltrosCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Collection<FiltroCorreo> filtrosCorreo = filtroCorreoFacade.findAll();
            Secuencia secFiltros = conversorFiltrosCorreo.crearSecuenciasFiltros(filtrosCorreo);
            Collection<Secuencia> secuencias = new ArrayList();
            secuencias.add(secFiltros);
            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_FILTROS_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(FiltroCorreoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String agregarFiltroCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secFiltro = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FILTRO_CORREO));
            FiltroCorreo filtro = conversorFiltrosCorreo.crearFiltroCorreoDeSecuencia(secFiltro);
            filtroCorreoFacade.create(filtro);
            return parser.generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_FILTRO_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(FiltroCorreoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String eliminarFiltroCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secFiltro = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_FILTRO));
            Long id = Long.parseLong(secFiltro.getValor());
            FiltroCorreo filtro = filtroCorreoFacade.find(id);
            filtroCorreoFacade.remove(filtro);
            return parser.generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_FILTRO_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(FiltroCorreoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String editarFiltroCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secFiltro = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FILTRO_CORREO));
            FiltroCorreo filtro = conversorFiltrosCorreo.crearFiltroCorreoDeSecuencia(secFiltro);
            FiltroCorreo filtroAnterior = filtroCorreoFacade.find(filtro.getId());
            filtroAnterior.setCondiciones(filtro.getCondiciones());
            filtroAnterior.setRedireccion(filtro.getRedireccion());
            filtroCorreoFacade.edit(filtroAnterior);
            return parser.generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_FILTRO_CORRREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(FiltroCorreoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darFiltroCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_FILTRO));
            Long id = Long.parseLong(secId.getValor());
            FiltroCorreo filtro = filtroCorreoFacade.find(id);
            Secuencia secFiltro = conversorFiltrosCorreo.crearSecuenciaFiltro(filtro);
            Collection<Secuencia> secuencias = new ArrayList();
            secuencias.add(secFiltro);
            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_FILTRO_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(FiltroCorreoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darTiposFiltroCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Collection<Secuencia> secuencias = new ArrayList();
            Secuencia secTipos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_FILTRO_CORREO),"");

            secTipos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO),getConstanteBean().getConstante(Constantes.VAL_FILTRO_DESTINATARIO)));
            secTipos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO),getConstanteBean().getConstante(Constantes.VAL_FILTRO_ASUNTO)));
            secTipos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO),getConstanteBean().getConstante(Constantes.VAL_FILTRO_MENSAJE)));
            secuencias.add(secTipos);
            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_FILTRO_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(FiltroCorreoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String darOperacionesFiltroCorreo(String xml) {
        try {
            parser.leerXML(xml);
            Collection<Secuencia> secuencias = new ArrayList();
            Secuencia secOperaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERACIONES_FILTRO_CORREO),"");
            secOperaciones.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERACION_FILTRO_CORREO),getConstanteBean().getConstante(Constantes.OP_IGUAL)));
            secOperaciones.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERACION_FILTRO_CORREO),getConstanteBean().getConstante(Constantes.OP_CONTIENE)));
            secOperaciones.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERACION_FILTRO_CORREO),getConstanteBean().getConstante(Constantes.OP_EMPIEZA_POR)));
            secOperaciones.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERACION_FILTRO_CORREO),getConstanteBean().getConstante(Constantes.OP_TERMINA_EN)));
            secuencias.add(secOperaciones);
            return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_OPERACIONES_FILTRO_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(FiltroCorreoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public FiltroCorreo evaluarFiltros(String para, String asunto, String cc, String cco, String archivo, String mensaje) {
        FiltroCorreo filtro = null;

        Collection<FiltroCorreo> filtros = filtroCorreoFacade.findAll();
        for (FiltroCorreo filtroCorreo : filtros) {
            if(filtro!=null)
                break;
            if(evaluarFiltro(filtroCorreo, para, asunto, cc, cco, archivo, mensaje))
                filtro = filtroCorreo;
        }
        return filtro;
    }

    private boolean evaluarFiltro(FiltroCorreo filtro,String para, String asunto, String cc, String cco, String archivo, String mensaje){
        Collection<CondicionFiltroCorreo> condiciones = filtro.getCondiciones();
        boolean valid = true;
        for (CondicionFiltroCorreo condicionFiltroCorreo : condiciones) {
            if(condicionFiltroCorreo.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_FILTRO_DESTINATARIO))){
                valid = valid && evaluarCondicionFiltro(condicionFiltroCorreo.getOperacion(), condicionFiltroCorreo.getValor(), para);
            }
            if(condicionFiltroCorreo.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_FILTRO_ASUNTO))){
                valid = valid && evaluarCondicionFiltro(condicionFiltroCorreo.getOperacion(), condicionFiltroCorreo.getValor(), asunto);
            }
            if(condicionFiltroCorreo.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_FILTRO_MENSAJE))){
                valid = valid && evaluarCondicionFiltro(condicionFiltroCorreo.getOperacion(), condicionFiltroCorreo.getValor(), mensaje);
            }
        }
        return valid;
    }

    private boolean evaluarCondicionFiltro(String operacion, String valorFiltro, String valorCorreo){
        if(operacion.equals(getConstanteBean().getConstante(Constantes.OP_IGUAL))){
            return valorCorreo.equals(valorFiltro);
        } else if(operacion.equals(getConstanteBean().getConstante(Constantes.OP_CONTIENE))){
            return valorCorreo.contains(valorFiltro);
        } else if(operacion.equals(getConstanteBean().getConstante(Constantes.OP_EMPIEZA_POR))){
            return valorCorreo.startsWith(valorFiltro);
        } else if(operacion.equals(getConstanteBean().getConstante(Constantes.OP_TERMINA_EN))){
            return valorCorreo.endsWith(valorFiltro);
        }
        return false;
    }





}
