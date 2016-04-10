/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Incidente;
import co.uniandes.sisinfo.entities.ModuloSisinfo;
import co.uniandes.sisinfo.entities.PersonaSoporte;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.IncidenteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ModuloSisinfoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PersonaSoporteFacadeRemote;
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
public class IncidenteBean implements IncidenteBeanRemote, IncidenteBeanLocal {

    @EJB
    private IncidenteFacadeLocal facadeIncidente;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private CorreoRemote correoBean;
  
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private ModuloSisinfoFacadeLocal moduloFacade;

    /**
     * Facade persona soporte.
     */
    @EJB
    private PersonaSoporteFacadeRemote personaSoporteFacade;
    
    private ParserT parser;
    private ServiceLocator serviceLocator;

    public IncidenteBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            personaSoporteFacade = (PersonaSoporteFacadeRemote) serviceLocator.getRemoteEJB(PersonaSoporteFacadeRemote.class);

        } catch (NamingException ex) {
            Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String reportarIncidente(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIncidente = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REPORTEINCIDENTE));
            Incidente i = pasarSecuenciaAIncidente(secIncidente);
            i.setEliminado(Boolean.FALSE);

            facadeIncidente.create(i);

            i = facadeIncidente.findByDescripcionyFecha(i.getDescripcionIncidente(), i.getFechaIncidente());

            //------------------
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_SOLUCIONAR_EXCEPCION);
            String asunto = Notificaciones.ASUNTO_SOLUCIONAR_INCIDENTE_SISINFO;
            String mensaje = Notificaciones.MENSAJE_SOLUCIONAR_INCIDENTE_SISINFO;
            String correo = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
            if (i.getReportadoPor() == null) {
                System.out.println("ERROR1: por alguna extraña razon persona es null");
            }
            mensaje = mensaje.replace("%1", i.getReportadoPor().getNombres() + " " + i.getReportadoPor().getApellidos());
            mensaje = mensaje.replace("%2", i.getDescripcionIncidente());
            Persona encargado = personaFacade.findByCorreo(correo);

            correoBean.enviarMail(correo, asunto, null, null, null, mensaje);


            boolean activa = true;
            String intervalo = constanteBean.getConstante(Constantes.VAL_TIPO_INTERVALO_INTERVALO);
            long duracionT = 1000L * 60L * 60L * 24L * 7L;
            boolean enviaC = true;
            long periodicidad = periodicidadFacade.findByNombre(getConstanteBean().getConstante(Constantes.VAL_PERIODICIDAD_MENSUAL)).getValor();
            Date fechaInicioDate = new Date();
            Date fechaFinDate = new Date(fechaInicioDate.getTime() + 1000L * 60L * 60L * 24L * 7 * 4);
           //4.2 crear tarea...
            String nombreV = "Solucionar Incidente en Sisinfo";// + carga.getId();
            String descripcion = "Solucionar Incidente Reportado Por: " + i.getReportadoPor().getNombres() + " " + i.getReportadoPor().getApellidos();
            int valPeriodicidad = 1;
            String cmd = getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_POR_ID);
            HashMap<String, String> parametros = new HashMap<String, String>();
            parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), Long.toString(i.getId()));


            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String categoria = getConstanteBean().getConstante(Constantes.ROL_ADMINISTRADOR_SISINFO);

            Long inicio = new Date().getTime();
            Timestamp fechaMaxAprobarInscripcion = new Timestamp(inicio + (1000 * 60 * 60 * 24 * 8 * 3));
            System.out.println("La fecha de la tarea es=" + sdfHMS.format(fechaMaxAprobarInscripcion));

            //4.3 enviar correo:
            correoBean.enviarMail(correo, asunto, null, null, null, mensaje);

            //--------------------------------------------
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPORTAR_INCIDENTE_SISINFO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_REPORTAR_INCIDENTE_SISINFO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarIncidentePorId(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor());
            Incidente i = facadeIncidente.find(id);
            Secuencia secI = pasarIncidenteASecuencia(i);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secI);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarIncidentePorCorreoReportado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            String correo = secCorreo.getValor();
            System.out.println("LLego el correo = " + correo);
            Collection<Incidente> incidentes = facadeIncidente.findByCorreoReportante(correo);
            System.out.println("INCIDENTE BEAN = ENCONTRO " + incidentes.size());
            Secuencia secIncidentes = pasarIncidentesASecuencia(incidentes);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secIncidentes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_CORREO_CREADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_CORREO_CREADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarIncidentesPorEstado(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secEstado = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));
            String estado = secEstado.getValor();
            Collection<Incidente> incidentes = facadeIncidente.findByEstado(estado);
            Secuencia secIncidentes = pasarIncidentesASecuencia(incidentes);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secIncidentes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String modificarEstadoIncidente(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secIncidente = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REPORTEINCIDENTE));
            Incidente i = pasarSecuenciaAIncidente(secIncidente);
            //---------------------------------------------------
            Incidente anterior = facadeIncidente.find(i.getId());

            Date d = new Date();
            Timestamp hoy = new Timestamp(d.getTime());
            anterior.setDescripcionSolucion(i.getDescripcionSolucion());
            anterior.setEstadoIncidente(i.getEstadoIncidente());
            anterior.setFechaSolucion(hoy);
            anterior.setSolucionado(i.getSolucionado());
            String correoReporta=anterior.getReportadoPor().getCorreo();
            String asuntoCorreo=Notificaciones.ASUNTO_RESOLUCION_INCIDENTE;
            String mensajeCorreo=Notificaciones.MENSAJE_RESOLUCION_INCIDENTE;
            System.out.println(""+mensajeCorreo);
            mensajeCorreo=mensajeCorreo.replaceFirst("%1", anterior.getReportadoPor().getNombres());
            mensajeCorreo=mensajeCorreo.replaceFirst("%2", anterior.getEstadoIncidente());
            mensajeCorreo=mensajeCorreo.replaceFirst("%3", anterior.getDescripcionSolucion());
            correoBean.enviarMail(correoReporta, asuntoCorreo, null, null, null, mensajeCorreo);

            facadeIncidente.edit(anterior);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_INCIDENTE_SISINFO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_INCIDENTE_SISINFO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ParserT getParser() {
        return parser;
    }

    private Secuencia pasarIncidentesASecuencia(Collection<Incidente> incidentes) {
        Secuencia secIncidentes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REPORTES_INCIDENTES), null);
        for (Incidente incidente : incidentes) {
            Secuencia secInc = pasarIncidenteASecuencia(incidente);
            secIncidentes.agregarSecuencia(secInc);
        }
        return secIncidentes;
    }

    private Secuencia pasarIncidenteASecuencia(Incidente i) {
        Secuencia secInc = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REPORTEINCIDENTE), null);
        if (i.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), i.getId().toString());
            secInc.agregarSecuencia(secId);
        }
        if (i.getSolucionado() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLUCIONADO), i.getSolucionado().toString());
            secInc.agregarSecuencia(sec);
        }
        if (i.getDescripcionIncidente() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION_INCIDENTE), i.getDescripcionIncidente());
            secInc.agregarSecuencia(sec);
        }
        if (i.getDescripcionSolucion() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION_SOLUCION), i.getDescripcionSolucion());
            secInc.agregarSecuencia(sec);
        }
        if (i.getEstadoIncidente() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), i.getEstadoIncidente());
            secInc.agregarSecuencia(sec);
        }
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (i.getFechaIncidente() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ERROR), sdfHMS.format(i.getFechaIncidente()));
            secInc.agregarSecuencia(sec);
        }
        if (i.getFechaSolucion() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SOLUCION), sdfHMS.format(i.getFechaSolucion()));
            secInc.agregarSecuencia(sec);
        }
        if (i.getModuloIncididente() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MODULO_SISINFO), i.getModuloIncididente());
            secInc.agregarSecuencia(sec);
        }
        if (i.getReportadoPor() != null) {
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA), null);
            if (i.getReportadoPor().getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PERSONA), i.getReportadoPor().getId().toString());
                sec.agregarSecuencia(secId);
            }
            if (i.getReportadoPor().getNombres() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), i.getReportadoPor().getNombres());
                sec.agregarSecuencia(secId);
            }
            if (i.getReportadoPor().getApellidos() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), i.getReportadoPor().getApellidos());
                sec.agregarSecuencia(secId);
            }
            if (i.getReportadoPor().getCorreo() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), i.getReportadoPor().getCorreo());
                sec.agregarSecuencia(secId);
            }

            secInc.agregarSecuencia(sec);
        }
        if (i.getPersonaSoporte() != null) {

            secInc.agregarSecuencia(pasarPersonaSoporteASecuencia(i.getPersonaSoporte()));
        }
        return secInc;
    }

    /**
     * Pasa una persona de soporte a una secuencia.
     * @param incidentes
     * @return
     */
    private Secuencia pasarPersonaSoporteASecuencia(PersonaSoporte personaSoporte) {
        Secuencia secPersonaSoporte = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA_SOPORTE), null);
        Secuencia secTmp = null;
        if (personaSoporte.getId() != null) {

            secTmp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), personaSoporte.getId().toString());
            secPersonaSoporte.agregarSecuencia(secTmp);
        }
        if (personaSoporte.getPersona() != null) {

            secTmp = pasarPersonaASecuencia(personaSoporte.getPersona());
            secPersonaSoporte.agregarSecuencia(secTmp);
        }
        return secPersonaSoporte;
    }

    /**
     * Pasa una persona a secuencia.
     * TODO: Ver como no reescribirlo.
     * @param p
     * @return
     */
    private Secuencia pasarPersonaASecuencia(Persona p) {

        Secuencia secPersona = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA), null);
        if (p.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), p.getId().toString());
            secPersona.agregarSecuencia(secId);
        }
        if (p.getNombres() != null) {
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), p.getNombres());
            secPersona.agregarSecuencia(secNombres);
        }
        if (p.getApellidos() != null) {
            Secuencia secApellido = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), p.getApellidos());
            secPersona.agregarSecuencia(secApellido);
        }
        if (p.getCorreo() != null) {
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), p.getCorreo());
            secPersona.agregarSecuencia(secCorreo);
        }
        return secPersona;
    }

    private Incidente pasarSecuenciaAIncidente(Secuencia secIncidente) {
        Incidente i = new Incidente();
        Secuencia secId = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            i.setId(id);
        }
        Secuencia secSolu = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLUCIONADO));
        if (secSolu != null) {
            i.setSolucionado(Boolean.parseBoolean(secSolu.getValor()));
        }
        Secuencia secDescrip = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION_INCIDENTE));
        if (secDescrip != null) {
            i.setDescripcionIncidente(secDescrip.getValor());
        }
        secDescrip = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION_SOLUCION));
        if (secDescrip != null) {
            i.setDescripcionSolucion(secDescrip.getValor());
        }
        secDescrip = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));
        if (secDescrip != null) {
            i.setEstadoIncidente(secDescrip.getValor());
        }
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        secDescrip = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ERROR));
        if (secDescrip != null) {
            try {
                Date d = sdfHMS.parse(secDescrip.getValor());
                i.setFechaIncidente(new Timestamp(d.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //---------------------
        secDescrip = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SOLUCION));
        if (secDescrip != null) {
            try {
                Date d = sdfHMS.parse(secDescrip.getValor());
                i.setFechaSolucion(new Timestamp(d.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        secDescrip = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_MODULO_SISINFO));
        if (secDescrip != null) {
            i.setModuloIncididente(secDescrip.getValor());
        }
        secDescrip = secIncidente.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA));
        if (secDescrip != null) {
            String correo = secDescrip.getValor();
            Persona p = personaFacade.findByCorreo(correo);
            i.setReportadoPor(p);
        }
        return i;
    }

    public String EliminarIncidente(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Long id = Long.parseLong(secId.getValor());
            Incidente i = facadeIncidente.find(id);
            i.setEliminado(true);
            facadeIncidente.edit(i);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INCIDENTE_SISINFO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INCIDENTE_SISINFO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darIncidentesSoporte(String xml) {
        try {
            parser.leerXML(xml);

            Collection<Incidente> incidentes = facadeIncidente.findByNoBorrado();
            Secuencia secIncidentes = pasarIncidentesASecuencia(incidentes);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secIncidentes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_SOPORTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_SOPORTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarModulosPublicosSisinfo(String xml) {
        try {
            parser.leerXML(xml);

            Collection<ModuloSisinfo> modulos = moduloFacade.buscarModulosPublicos();
            Secuencia secModulos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MODULOS_SISINFO), null);
            for (ModuloSisinfo elem : modulos) {
                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MODULO_SISINFO), elem.getNombreModulo());
                secModulos.agregarSecuencia(sec);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secModulos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MODULOS_SISINFO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());


        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_SOPORTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Asigna un incidente a una persona de soporte.
     * @param xml
     * @return
     */
    public String asignarIncidente(String xml) {

        try {

            /* Incidente. */
            parser.leerXML(xml);
            Secuencia secIdIncidente = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            Secuencia secPersonaSoporte = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA_SOPORTE));
            if (secIdIncidente == null || secPersonaSoporte == null) {

                throw new Exception("faltan parametros en metodo: asignarIncidente: IncidenteBean");
            } else {

                System.out.println("PERSISTIENDO...");
                Incidente incidente = facadeIncidente.find(Long.parseLong(secIdIncidente.getValor().trim()));

                /* Personas de soporte. */
                PersonaSoporte personaSoporte = personaSoporteFacade.findByEmail(secPersonaSoporte.getValor().trim());
                PersonaSoporte personaAnterior = incidente.getPersonaSoporte();

                if (!personaSoporte.equals(personaAnterior)) {

                    /* Actualiza bean persona soporte anterior. */
                    boolean found = false;
                    for (Incidente incidenteTmp : personaAnterior.getIncidentes()) {

                        if (incidente.equals(incidenteTmp)) {

                            System.out.println("YA LO TENIA ASOCIADO");
                            found = true;
                            break;
                        }
                    }
                    if (found) {

                        System.out.println("YA LO QUITAMOS");
                        personaAnterior.getIncidentes().remove(incidente);
                        personaSoporteFacade.edit(personaAnterior);
                    }

                    /* Actualiza bean persona soporte nueva. */
                    System.out.println("YA LO ASOCIAMOS");
                    personaSoporte.getIncidentes().add(incidente);
                    personaSoporteFacade.edit(personaSoporte);

                    incidente.setPersonaSoporte(personaSoporte);
                    facadeIncidente.edit(incidente);
                    
                    /* Correo. */
                    String correoReporta = secPersonaSoporte.getValor().trim();
                    String asuntoCorreo = Notificaciones.ASUNTO_ASIGNAR_INCIDENTE;
                    String mensajeCorreo = Notificaciones.MENSAJE_ASIGNAR_INCIDENTE;
                    mensajeCorreo = mensajeCorreo.toString().replaceFirst("%1", personaSoporte.getPersona().getNombres());
                    mensajeCorreo = mensajeCorreo.toString().replaceFirst("%2", incidente.getDescripcionIncidente());
                    System.out.println("hola sisinfo, listo para persistir a " + secPersonaSoporte.getValor().trim());
                    correoBean.enviarMail(correoReporta, asuntoCorreo, null, null, null, mensajeCorreo);
                    System.out.println("he enviado un correo a " + secPersonaSoporte.getValor().trim());
                }

                /* Respuesta. */
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ASIGNAR_INCIDENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ASIGNAR_INCIDENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * Da incidencias dada una persona de soporte.
     * @param xml
     * @return
     */
    public String darIncidentesXPersonaSoporte(String xml) {

        try {
            parser.leerXML(xml);
            Secuencia secIdPersonaSoporte = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            String estado = secIdPersonaSoporte.getValor();
            Collection<Incidente> incidentes = 
                    facadeIncidente.findByPersonaSoporte( new Long(secIdPersonaSoporte.getValor()) );
            Secuencia secIncidentes = pasarIncidentesASecuencia(incidentes);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secIncidentes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(IncidenteBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INCIDENTE_SISINFO_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ReporteExcepcionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
