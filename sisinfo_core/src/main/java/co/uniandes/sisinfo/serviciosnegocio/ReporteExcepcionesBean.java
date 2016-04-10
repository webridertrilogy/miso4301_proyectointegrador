/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.ExcepcionSisinfo;
import co.uniandes.sisinfo.entities.ListaBlancaErroresSisinfo;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ExcepcionSisinfoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ListaBlancaErroresSisinfoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class ReporteExcepcionesBean implements ReporteExcepcionesBeanRemote, ReporteExcepcionesBeanLocal {

    @EJB
    private ExcepcionSisinfoFacadeLocal facadeExcepcion;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ComandoAuditoriaBeanRemote comandoAuditoriaBean;
    @EJB
    private CorreoRemote correoBean;
    
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private ListaBlancaErroresSisinfoFacadeLocal listaBlancaFacade;
    @EJB
    private AuditoriaUsuarioBeanRemote auditoriaUsuariosBean;

    private ParserT parser;
    private ServiceLocator serviceLocator;

    public ReporteExcepcionesBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            comandoAuditoriaBean = (ComandoAuditoriaBeanRemote) serviceLocator.getRemoteEJB(ComandoAuditoriaBeanRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
          
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            auditoriaUsuariosBean = (AuditoriaUsuarioBeanRemote) serviceLocator.getRemoteEJB(AuditoriaUsuarioBeanRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * metodo que recbe todos los logs del sistema y en caso de que sea error guarda un log del mismo, crea la tarea para el rol de soporte
     * @param nombreBean: nombre del modulo llamado
     * @param nombreBean: nombre del metodo llamado
     * @param comandoRespuesta: comando respondido por el componente    
     * @param fecha: timestamp de la hora en que fue llamado el comando
     */
    public void crearLogMensaje(String nombreBean, String nombreMetodo, String respuesta, Timestamp fecha, String comando, String xmlEntrada) {

        try {
            Secuencia secuenciaPrincipal = parser.leerRespuesta(respuesta);
            validarRespuestaConsulta(secuenciaPrincipal);

            if( getComandoAuditoriaBean().getComandoAuditoriaEstado(comando).booleanValue() == (true) ){
                auditoriaUsuariosBean.crearRegistroAuditoriaUsuario(xmlEntrada, true);
            }

        } catch (Exception ex) {

            String idError = ex.getMessage();
            ListaBlancaErroresSisinfo error = listaBlancaFacade.findByIdError(idError);
            if (error == null) {
                //aca hacer algo con el problema: k problemas se debe guardar todos o los que sean generales ->
                ExcepcionSisinfo exeption = new ExcepcionSisinfo();
                exeption.setComandoSisinfo(comando);
                exeption.setFechaError(fecha);
                exeption.setMetodoSisinfo(nombreMetodo);
                exeption.setModuloSisinfo(nombreBean);
                exeption.setSolucionado(Boolean.FALSE);
                exeption.setRespuesta(respuesta);
                exeption.setEliminado(Boolean.FALSE);
                exeption.setXmlEntrada(xmlEntrada);
                facadeExcepcion.create(exeption);
                //buscar la exception dado el id y crear una tarea al rol segun sea el caso
                List<ExcepcionSisinfo> exs = facadeExcepcion.findByComandoFechaMetodo(comando, nombreMetodo, fecha);
                exeption = exs.iterator().next();
                //a quien se le pone la tarea??
                //---------------------------------------------------------------------------------------------------------------------------------------------------
                //4.1 notificacion:
                //Crear alerta de inscripciones
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SOLUCIONAR_EXCEPCION);
                String asunto = Notificaciones.ASUNTO_SOLUCIONAR_ERROR_SISINFO;
                //asunto=asunto.replaceFirst("%", nombreBean);
                String mensaje = Notificaciones.MENSAJE_SOLUCIONAR_ERROR_SISINFO;
                String correo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
            //    System.out.println("linea=" + mensaje);
                mensaje = mensaje.replace("%1", nombreBean);
                mensaje = mensaje.replace("%2", nombreMetodo);
                respuesta=respuesta.replaceAll("<", "&lt;");
                respuesta=respuesta.replaceAll(">", "&gt;");
                mensaje = mensaje.replace("%3", "<pre>" + respuesta + "</pre>");
                
                correoBean.enviarMail(correo, asunto, null, null, null, mensaje);

                if( getComandoAuditoriaBean().getComandoAuditoriaEstado(comando).booleanValue() == (true)){
                    auditoriaUsuariosBean.crearRegistroAuditoriaUsuario(xmlEntrada, false);
                }

                /*boolean activa = true;
                String intervalo = constanteBean.getConstante(Constantes.VAL_TIPO_INTERVALO_INTERVALO);
                long duracionT = 1000L * 60L * 60L * 24L * 7L;
                boolean enviaC = true;
                long periodicidad = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_MENSUAL)).getValor();
                Date fechaInicioDate = new Date();
                Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
                //4.2 crear tarea...
                String nombreV = "Solucionar Excepción en Sisinfo";// + carga.getId();
                String descripcion = "Se ha producido un error en el módulo: " + nombreBean + ", método " + nombreMetodo;
                int valPeriodicidad = 1;
                String cmd = getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCION_POR_ID);
                HashMap<String, String> parametros = new HashMap<String, String>();
                parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(exeption.getId()));
                SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String categoria = getConstanteBean().getConstante(Constantes.ROL_ADMINISTRADOR_SISINFO);
                Date fechaMaxAprobarInscripcion = new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 8 * 3);

                //4.3 enviar correo:
                
                //---------------------------------------------------------------------------------------------------------------------------------------------------
                 *
                 */
            }

        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ComandoAuditoriaBeanRemote getComandoAuditoriaBean() {
        return comandoAuditoriaBean;
    }

    

    /**
     * retorna las excepciones existentes
     * @param xml
     * @return
     */
    public String consultarExcepcionesPorEstado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secEstado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));
            if (secEstado != null) {
                String estado = secEstado.getValor();
                Boolean estadoSol = Boolean.getBoolean(estado);
                List<ExcepcionSisinfo> exs = facadeExcepcion.findByEstado(estadoSol);
                Secuencia secExceptions = pasarExcepcionesASecuencia(exs);
                //retornar:
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secExceptions);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCIONES_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            }
            throw new Exception("xml mal formado");
        } catch (Exception ex) {
            Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCIONES_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    /**
     * consulta las excepciones por id
     * @param xml
     * @return
     */
    public String consultarExcepcionPorId(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secEstado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            if (secEstado != null) {
                String estado = secEstado.getValor();
                Long id = Long.parseLong(estado);
                ExcepcionSisinfo exs = facadeExcepcion.find(id);
                Secuencia secExceptions = pasarExcepcionASecuencia(exs);
                //retornar:
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secExceptions);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCION_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

            }
            throw new Exception("xml mal formado");
        } catch (Exception ex) {
            try {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCION_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String solucionarExcepcion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secException = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXCEPCION));
            ExcepcionSisinfo ex = pasarSecuenciaAExcepcion(secException);
            if (ex.getId() == null) {
                System.out.println("EL id es null !!!!!!!!!!!");
            }
            ExcepcionSisinfo laExEx = facadeExcepcion.find(ex.getId());
            laExEx.setDescripcionErrorPorSoporte(ex.getDescripcionErrorPorSoporte());
            laExEx.setFechaSolucion(new Timestamp(new Date().getTime()));
            laExEx.setSolucionado(Boolean.TRUE);
            facadeExcepcion.edit(laExEx);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SOLUCIONAR_EXCEPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_SOLUCIONAR_EXCEPCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private Secuencia pasarExcepcionesASecuencia(List<ExcepcionSisinfo> exs) {
        Secuencia secExcepcioens = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXCEPCIONES), null);
        for (ExcepcionSisinfo elem : exs) {
            Secuencia sec = pasarExcepcionASecuencia(elem);
            secExcepcioens.agregarSecuencia(sec);
        }
        return secExcepcioens;
    }

    public ParserT getParser() {
        return parser;
    }

    private Secuencia pasarExcepcionASecuencia(ExcepcionSisinfo exs) {
        Secuencia secEx = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXCEPCION), null);
        if (exs.getId() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), exs.getId().toString()));
        }
        if (exs.getSolucionado() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLUCIONADO), exs.getSolucionado().toString()));
        }
        if (exs.getComandoSisinfo() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_SISINFO), exs.getComandoSisinfo()));
        }
        if (exs.getDescripcionErrorPorSoporte() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), exs.getDescripcionErrorPorSoporte()));
        }
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (exs.getFechaError() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ERROR), sdfHMS.format(exs.getFechaError())));
        }
        if (exs.getFechaSolucion() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SOLUCION), sdfHMS.format(exs.getFechaSolucion())));
        }
        if (exs.getMetodoSisinfo() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_METODO_SISINFO), exs.getMetodoSisinfo()));
        }
        if (exs.getModuloSisinfo() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MODULO_SISINFO), exs.getModuloSisinfo()));
        }
        if (exs.getRespuesta() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPUESTA), exs.getRespuesta()));
        }
        if (exs.getXmlEntrada() != null) {
            secEx.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_XML_ENTRADA), exs.getXmlEntrada()));
        }
        return secEx;
    }

    private ExcepcionSisinfo pasarSecuenciaAExcepcion(Secuencia exs) {
        ExcepcionSisinfo ex = new ExcepcionSisinfo();
        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(sec.getValor());
            ex.setId(id);
        }
        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLUCIONADO)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLUCIONADO));
            ex.setSolucionado(Boolean.parseBoolean(sec.getValor()));
        }
        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_SISINFO)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLUCIONADO));
            ex.setComandoSisinfo(sec.getValor());
        }
        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
            ex.setDescripcionErrorPorSoporte(sec.getValor());
        }
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd");
        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ERROR)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ERROR));
            try {
                ex.setFechaError(new Timestamp(sdfHMS.parse(sec.getValor()).getTime()));
            } catch (ParseException ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SOLUCION)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SOLUCION));
            try {
                ex.setFechaSolucion(new Timestamp(sdfHMS.parse(sec.getValor()).getTime()));
            } catch (ParseException ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }


        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_METODO_SISINFO)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_METODO_SISINFO));
            ex.setMetodoSisinfo(sec.getValor());
        }

        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MODULO_SISINFO)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MODULO_SISINFO));
            ex.setModuloSisinfo(sec.getValor());
        }

        if (exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPUESTA)) != null) {
            Secuencia sec = exs.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPUESTA));
            ex.setRespuesta(sec.getValor());
        }
        return ex;
    }

    public String eliminarExcepcionSisinfo(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor());
            ExcepcionSisinfo i = facadeExcepcion.find(id);
            i.setEliminado(true);
            facadeExcepcion.edit(i);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_EXCEPCION_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_EXCEPCION_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarExcepcionesSisinfo(String xml) {
        try {
            parser.leerXML(xml);

            List<ExcepcionSisinfo> exs = facadeExcepcion.findByNoBorradas();
            Secuencia secExceptions = pasarExcepcionesASecuencia(exs);
            //retornar:
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secExceptions);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCIONES_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_EXCEPCIONES_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Metodo que se encarga de validar si una respuesta recibida trae mensaje de error. <br>
     * Una respuesta trae mensaje de error cuando el tag respuesta es false.
     * @param secuenciaPrincipal Es la secuencia principal de la respuesta recibida (la raiz)
     * @throws co.edu.uniandes.sisinfo.client.excepciones.MensajeUsuarioException Se lanza una excepcion con el mensaje de error si se recibi� en la respuesta.
     */
    public void validarRespuestaConsulta(Secuencia secuenciaPrincipal) throws Exception {
        Secuencia secMensaje = secuenciaPrincipal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_MENSAJE));
        Secuencia secTipoMensaje = secMensaje.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_TIPO_MENSAJE));

        String condicionResp = secTipoMensaje.getValor();
        if (condicionResp.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR))) {
            String idMensaje = secMensaje.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_MENSAJE)).getValor();
            Secuencia secParams = secMensaje.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETROS_MENSAJE));
            Exception msjExc = new Exception(idMensaje);
            for (Secuencia secParam : secParams.getSecuencias()) {
                if (secParam.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE))) {
                    //msjExc.agregarParametro(secParam.getValor());
                }
            }
            throw msjExc;
        }
    }
}
