package co.uniandes.sisinfo.serviciosnegocio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.CorreoAuditoria;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoAuditoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicios de negocio para administración de correos de auditoría
 * @author Marcela Morales
 */
@Stateless
@EJB(name = "CorreoAuditoriaBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.CorreoAuditoriaLocal.class)
public class CorreoAuditoriaBean implements CorreoAuditoriaLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @EJB
    private CorreoAuditoriaFacadeLocal correoAuditoriaFacade;
    @EJB
    private ConstanteLocal constanteBean;
    private ServiceLocator serviceLocator;
    private ParserT parser;
    private ConversorCorreoAuditoria conversor;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    public CorreoAuditoriaBean() {
        parser = new ParserT();
        serviceLocator = new ServiceLocator();
//        try {
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            conversor = new ConversorCorreoAuditoria(constanteBean);
//        } catch (NamingException ex) {
//            Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public String consultarCorreosAuditoria(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);
            
            Collection<CorreoAuditoria> correos = correoAuditoriaFacade.findAll();
            Secuencia secCorreos = getConversor().crearSecuenciaCorreos(correos);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCorreos);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREOS_AUDITORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREOS_AUDITORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarCorreoAuditoria(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);
            
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            CorreoAuditoria correo = correoAuditoriaFacade.find(secId.getValorLong());
            Secuencia secCorreo = getConversor().crearSecuenciaCorreo(correo);
            
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCorreo);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREO_AUDITORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREO_AUDITORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarCorreoAuditoriaPorDestinatariosFechaYAsunto(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);

            Secuencia secDestinatario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secFechaDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
            Secuencia secFechaHasta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
            Secuencia secAsunto = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO));

            boolean esBusquedaPorDestinatario = secDestinatario != null && secDestinatario.getValor() != null && !secDestinatario.getValor().equals("");
            boolean esBusquedaPorFecha = (secFechaDesde != null && secFechaDesde.getValor() != null && !secFechaDesde.getValor().equals("")) ||
                    (secFechaHasta != null && secFechaHasta.getValor() != null && !secFechaHasta.getValor().equals(""));
            boolean esBusquedaPorAsunto = secAsunto != null && secAsunto.getValor() != null && !secAsunto.getValor().equals("");
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'COT' yyyy", new Locale("en"));
            List<CorreoAuditoria> correos = new ArrayList<CorreoAuditoria>();
            if(esBusquedaPorDestinatario && esBusquedaPorFecha && esBusquedaPorAsunto){
                correos = correoAuditoriaFacade.findByDestinatariosFechaYAsunto(secDestinatario.getValor(), sdf.parse(secFechaDesde.getValor()), sdf.parse(secFechaHasta.getValor()), secAsunto.getValor());
            } else if(esBusquedaPorDestinatario && esBusquedaPorFecha){
                correos = correoAuditoriaFacade.findByDestinatariosYFecha(secDestinatario.getValor(), sdf.parse(secFechaDesde.getValor()), sdf.parse(secFechaHasta.getValor()));
            } else if(esBusquedaPorDestinatario && esBusquedaPorAsunto){
                correos = correoAuditoriaFacade.findByDestinatariosYAsunto(secDestinatario.getValor(), secAsunto.getValor());
            } else if(esBusquedaPorAsunto && esBusquedaPorFecha){
                correos = correoAuditoriaFacade.findByAsuntoYFecha(secAsunto.getValor(),sdf.parse(secFechaDesde.getValor()), sdf.parse(secFechaHasta.getValor()));
            } else if(esBusquedaPorDestinatario){
                correos = correoAuditoriaFacade.findByDestinatarios(secDestinatario.getValor());
            } else if(esBusquedaPorFecha){
                correos = correoAuditoriaFacade.findByFecha(sdf.parse(secFechaDesde.getValor()), sdf.parse(secFechaHasta.getValor()));
            } else if(esBusquedaPorAsunto){
                correos = correoAuditoriaFacade.findByAsunto(secAsunto.getValor());
            } else {
                correos = correoAuditoriaFacade.findAll();
            }
            Secuencia secCorreoAuditoria = getConversor().crearSecuenciaCorreos(correos);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCorreoAuditoria);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarCorreoAuditoriaPorDestinatariosFechaYAsunto(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);

            Secuencia secDestinatario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secFecha = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA));
            Secuencia secAsunto = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO));

            boolean esBusquedaPorDestinatario = secDestinatario != null && secDestinatario.getValor() != null && !secDestinatario.getValor().equals("");
            boolean esBusquedaPorFecha = secFecha != null && secFecha.getValor() != null && !secFecha.getValor().equals("");
            boolean esBusquedaPorAsunto = secAsunto != null && secAsunto.getValor() != null && !secAsunto.getValor().equals("");

            Date fechaInicio = new Date();
            Date fechaFin = new Date();
            if(esBusquedaPorFecha){
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", new Locale("en"));
                Date fecha = sdf.parse(secFecha.getValor());

                Calendar inicio = Calendar.getInstance();
                inicio.setTime(fecha);
                inicio.set(Calendar.HOUR_OF_DAY, 0);
                inicio.set(Calendar.MINUTE, 0);
                inicio.set(Calendar.SECOND, 0);
                fechaInicio.setTime(inicio.getTimeInMillis());

                Calendar fin = Calendar.getInstance();
                fin.setTime(fecha);
                fin.set(Calendar.HOUR_OF_DAY, 23);
                fin.set(Calendar.MINUTE, 59);
                fin.set(Calendar.SECOND, 59);
                fechaFin.setTime(fin.getTimeInMillis());
            }

            List<CorreoAuditoria> correos = new ArrayList<CorreoAuditoria>();
            if(esBusquedaPorDestinatario && esBusquedaPorFecha && esBusquedaPorAsunto){
                correos = correoAuditoriaFacade.findByDestinatariosFechaYAsunto(secDestinatario.getValor(), fechaInicio, fechaFin, secAsunto.getValor());
            } else if(esBusquedaPorDestinatario && esBusquedaPorFecha){
                correos = correoAuditoriaFacade.findByDestinatariosYFecha(secDestinatario.getValor(), fechaInicio, fechaFin);
            } else if(esBusquedaPorDestinatario && esBusquedaPorAsunto){
                correos = correoAuditoriaFacade.findByDestinatariosYAsunto(secDestinatario.getValor(), secAsunto.getValor());
            } else if(esBusquedaPorAsunto && esBusquedaPorFecha){
                correos = correoAuditoriaFacade.findByDestinatariosYAsunto(secDestinatario.getValor(), secAsunto.getValor());
            } else if(esBusquedaPorDestinatario){
                correos = correoAuditoriaFacade.findByDestinatarios(secDestinatario.getValor());
            } else if(esBusquedaPorFecha){
                correos = correoAuditoriaFacade.findByFecha(fechaInicio, fechaFin);
            } else if(esBusquedaPorAsunto){
                correos = correoAuditoriaFacade.findByAsunto(secAsunto.getValor());
            } else {
                correos = correoAuditoriaFacade.findAll();
            }
            for(CorreoAuditoria correo : correos){
                correoAuditoriaFacade.remove(correo);
            }

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CORREO_AUDITORIA_POR_DESTINATARIOS_FECHA_Y_ASUNTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarCorreoAuditoria(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            CorreoAuditoria correo = correoAuditoriaFacade.find(secId.getValorLong());
            correoAuditoriaFacade.remove(correo);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CORREO_AUDITORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CORREO_AUDITORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
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

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    private ConversorCorreoAuditoria getConversor(){
        return new ConversorCorreoAuditoria(constanteBean);
    }
}
