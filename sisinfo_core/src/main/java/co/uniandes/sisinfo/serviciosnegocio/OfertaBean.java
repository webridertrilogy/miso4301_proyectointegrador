package co.uniandes.sisinfo.serviciosnegocio;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.EstudiantePosgrado;
import co.uniandes.sisinfo.entities.HojaVida;
import co.uniandes.sisinfo.entities.Oferta;
import co.uniandes.sisinfo.entities.Proponente;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.EstudiantePosgradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.HojaVidaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InformacionEmpresaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.OfertaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ProponenteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TipoAsistenciaGraduadaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeLocal;

/**
 * Servicios de administración de ofertas de empleo
 * @author Camilo Cortés, John Casallas, Marcela Morales
 */
@Stateless
@EJB(name = "OfertaBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.OfertaLocal.class)
public class OfertaBean implements  OfertaLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    //Remotos
    @EJB
    private EstudianteFacadeLocal estudianteFacade;
    @EJB
    private PersonaFacadeLocal personaFacade;
    @EJB
    private PeriodoFacadeLocal periodoFacade;
    @EJB
    private PaisFacadeLocal paisFacade;
    @EJB
    private TipoDocumentoFacadeLocal tipoDocumentoFacade;
    @EJB
    private InformacionAcademicaFacadeLocal informacionAcademicaFacade;
    //Locales
    @EJB
    private TipoAsistenciaGraduadaFacadeLocal tipoAsistenciaFacade;
    @EJB
    private HojaVidaFacadeLocal hojaVidaFacade;
    @EJB
    private EstudiantePosgradoFacadeLocal estudiantePostgradoFacade;
    @EJB
    private ProponenteFacadeLocal proponenteFacade;
    @EJB
    private OfertaFacadeLocal ofertaFacade;
    @EJB
    private InformacionEmpresaFacadeLocal informacionEmpresaFacade;
    //Útiles
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private TimerGenericoBeanLocal timerGenerico;
    @EJB
    private CorreoLocal correoBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorBolsaEmpleo conversor;

    //---------------------------------------
    // Constantes
    //---------------------------------------
    public final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.OfertaLocal";
    public final static String NOMBRE_MODULO = "BolsaEmpleo";
    
    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de OfertaBean
     */
    public OfertaBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            timerGenerico = (TimerGenericoBeanLocal) serviceLocator.getLocalEJB(TimerGenericoBeanLocal.class);
//            correoBean = (CorreoLocal) serviceLocator.getLocalEJB(CorreoLocal.class);
//            parser = new ParserT();
//            periodoFacade = (PeriodoFacadeLocal) serviceLocator.getLocalEJB(PeriodoFacadeLocal.class);
//            personaFacade = (PersonaFacadeLocal) serviceLocator.getLocalEJB(PersonaFacadeLocal.class);
//            estudianteFacade = (EstudianteFacadeLocal) serviceLocator.getLocalEJB(EstudianteFacadeLocal.class);
//            paisFacade = (PaisFacadeLocal) serviceLocator.getLocalEJB(PaisFacadeLocal.class);
//            tipoDocumentoFacade = (TipoDocumentoFacadeLocal) serviceLocator.getLocalEJB(TipoDocumentoFacadeLocal.class);
//            informacionAcademicaFacade = (InformacionAcademicaFacadeLocal) serviceLocator.getLocalEJB(InformacionAcademicaFacadeLocal.class);
//            conversor = new ConversorBolsaEmpleo(constanteBean, estudianteFacade, personaFacade, periodoFacade, paisFacade, tipoAsistenciaFacade, tipoDocumentoFacade, informacionAcademicaFacade, hojaVidaFacade, estudiantePostgradoFacade, proponenteFacade, ofertaFacade, informacionEmpresaFacade);
//        } catch (NamingException ex) {
//            Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String actualizarOferta(String xml) {
        try {
            getParser().leerXML(xml);
            String idOferta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_OFERTA)).getValor();
            Oferta oferta = getOfertaFacade().findyById(Long.parseLong(idOferta));
            if (oferta != null) {
                Secuencia secNombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
                if(secNombre != null){
                    oferta.setNombre(secNombre.getValor());
                }
                Secuencia secTitulo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO));
                if(secTitulo != null){
                    oferta.setTitulo(secTitulo.getValor());
                }
                Secuencia secDescripcion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION));
                if(secDescripcion != null){
                    oferta.setDescripcion(secDescripcion.getValor());
                }
                Secuencia secDireccionWeb = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_WEB));
                if(secDireccionWeb != null){
                    oferta.setDireccionWeb(secDireccionWeb.getValor());
                }
                Secuencia secFechaFinOferta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
                if(secFechaFinOferta != null){
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date fechaFin = sdf.parse(secFechaFinOferta.getValor());
                        oferta.setFechaFinOferta(new Timestamp(fechaFin.getTime()));
                    } catch (ParseException ex) {
                        Logger.getLogger(ConversorBolsaEmpleo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                Secuencia secPeriodoVinculacion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_VINCULACION));
                if(secPeriodoVinculacion != null){
                    oferta.setPeriodoVinculacion(secPeriodoVinculacion.getValor());
                }
                Secuencia secRequisitos = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REQUISITOS));
                if(secRequisitos != null){
                    oferta.setRequisitos(secRequisitos.getValor());
                }
                Secuencia secCorreoContacto = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CONTACTO));
                if(secCorreoContacto != null){
                    oferta.setCorreoContacto(secCorreoContacto.getValor());
                }
                getOfertaFacade().edit(oferta);

                //-----------------------> INICIO TIMERS
                //Se creea/actualiza el timer para avisarle al profesor que la solicitud se va a eliminar
                getTimerGenerico().eliminarTimerPorParametroExterno("ofertaVencimiento-" + idOferta);
                Timestamp fechaTimer = oferta.getFechaFinOferta();
                getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, "notificarProfesorVencimientoOferta", fechaTimer, "ofertaVencimiento-" + oferta.getId(),
                    NOMBRE_MODULO, this.getClass().getName(), "actualizarOferta", "Este timer se crea porque se actualizo la oferta y el momento de avisarle al profesor que la oferta va a vencer cambia");
                // Se crea/actualiza el timer de eliminar oferta por vencimiento
                getTimerGenerico().eliminarTimerPorParametroExterno("oferta-" + idOferta);
                getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, "eliminarOfertaPorVencimiento", fechaTimer, "oferta-" + oferta.getId(),
                    NOMBRE_MODULO, this.getClass().getName(), "actualizarOferta", "Este timer se crea porque se actualizo la oferta y el momento en el cuál se eliminará cambia");
                //-----------------------> FIN TIMERS

                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0140, new LinkedList<Secuencia>());
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0112, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0113, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarOferta(String xml) {
        try {
            getParser().leerXML(xml);
            String idOferta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_OFERTA)).getValor();
            Oferta oferta = getOfertaFacade().findyById(Long.parseLong(idOferta));
            if (oferta != null) {
                Secuencia secuenciaOferta = getConversor().crearSecuenciaOferta(oferta);
                Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
                secuencias.add(secuenciaOferta);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0142, secuencias);
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0112, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0115, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarOfertas(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secEstado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_OFERTA));
            Collection<Oferta> ofertas = getOfertaFacade().findByEstado(secEstado.getValor());
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaOfertas = getConversor().crearSecuenciaOfertas(ofertas);
            secuencias.add(secuenciaOfertas);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0143, secuencias);
        } catch (Exception e) {
            try {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0116, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarOfertasProponente(String xml) {
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Proponente proponente = getProponenteFacade().findByCorreo(correo);
            if (proponente != null) {
                Persona p = proponente.getPersona();
                Collection<Oferta> ofertas = proponente.getOfertas();
                Secuencia secuenciaOfertas = getConversor().crearSecuenciaOfertas(ofertas);
                Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
                secuencias.add(secuenciaOfertas);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTAS_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0144, secuencias);
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTAS_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0118, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_OFERTAS_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0117, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String crearOferta(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secOferta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OFERTA));
            String idProponente = secOferta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PROPONENTE)).getValor();
            Proponente proponente = getProponenteFacade().findById(Long.parseLong(idProponente));
            if (proponente != null) {
                Oferta oferta = getConversor().crearOfertaDesdeSecuencia(secOferta);
                oferta.setEstado(getConstanteBean().getConstante(Constantes.CTE_OFERTA_VIGENTE));
                Collection<Oferta> ofertas = proponente.getOfertas();
                ofertas.add(oferta);
                proponente.setOfertas(ofertas);
                getProponenteFacade().edit(proponente);

                //-----------------------> INICIO TIMERS
                //Se crea un timer para avisarle al profesor que la solicitud se va a eliminar
                Calendar calendario = Calendar.getInstance();
                calendario.setTime(oferta.getFechaFinOferta());
                calendario.set(Calendar.DAY_OF_YEAR, calendario.get(Calendar.DAY_OF_YEAR) - 1);
                Timestamp fechaNotificacionProfesor = new Timestamp(calendario.getTimeInMillis());
                getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, "notificarProfesorVencimientoOferta", fechaNotificacionProfesor, "ofertaVencimiento-" + oferta.getId(),
                    NOMBRE_MODULO, this.getClass().getName(), "crearOferta", "Este timer se crea porque se creo la oferta y el momento de avisarle al profesor que la oferta va a vencer debe establecerse");
                //Se crea un timer para eliminar la oferta en la fechaFinOferta
                Timestamp fechaTimer = new Timestamp((oferta.getFechaFinOferta()).getTime());
                 getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, "eliminarOfertaPorVencimiento", fechaTimer, "oferta-" + oferta.getId(),
                    NOMBRE_MODULO, this.getClass().getName(), "crearOferta", "Este timer se crea porque se actualizo la oferta y el momento en el cual se eliminara se debe establecer");
                //-----------------------> FIN TIMERS
                //Envía un correo informando que hay una nueva oferta de empleo
                String mensajeCompleto = String.format(Notificaciones.MENSAJE_CREACION_NUEVA_OFERTA, oferta.getTitulo(), oferta.getNombre(), oferta.getCorreoContacto());

                
                String[] correos = getConstanteBean().getConstante(Constantes.VAL_CORREOS_NOTIFICAR_OFERTA).split(":");
                for (String correo : correos) {
                    getCorreoBean().enviarMail(correo, Notificaciones.ASUNTO_CREACION_NUEVA_OFERTA, null, null, null, mensajeCompleto);
                }
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0145, new LinkedList<Secuencia>());
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0118, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0119, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

public void eliminarOfertaPorVencimiento(String idOferta) {
        try {
            String partes[] = idOferta.split("-");
            Oferta oferta = getOfertaFacade().findyById(Long.parseLong(partes[1]));
            if (oferta != null) {
                oferta.setEstado(getConstanteBean().getConstante(Constantes.CTE_OFERTA_VENCIDA));
                getOfertaFacade().edit(oferta);
            }
        } catch (Exception e) {
            String mensaje = String.format(Notificaciones.MENSAJE_ELIMINAR_OFERTA_POR_VENCIMIENTO_SISINFO, idOferta, e.getMessage());
            getCorreoBean().enviarMail(Constantes.VAL_CORREO_SOPORTE_SISINFO, Notificaciones.ASUNTO_ELIMINAR_OFERTA_POR_VENCIMIENTO_SISINFO, null, null, null, mensaje);
        }
    }

    public void notificarProfesorVencimientoOferta(String idOferta) {
        try {
            String partes[] = idOferta.split("-");
            Oferta oferta = getOfertaFacade().findyById(Long.parseLong(partes[1]));
            if (oferta != null) {
                Proponente proponente = getProponenteFacade().findByIdOferta(oferta.getId());
                if (proponente != null) {
                    String correo = proponente.getPersona().getCorreo();
                    String nombreProponente = proponente.getPersona().getNombres() + " " + proponente.getPersona().getApellidos();
                    String nombre = oferta.getNombre();
                    String titulo = oferta.getTitulo();
                    String descripcion = oferta.getDescripcion();
                    String mensaje = String.format(Notificaciones.MENSAJE_NOTIFICAR_VENCIMIENTO_OFERTA, nombreProponente, nombre, titulo, descripcion);
                    getCorreoBean().enviarMail(correo, Notificaciones.ASUNTO_NOTIFICAR_VENCIMIENTO_OFERTA, null, null, null, mensaje);
                }
            }
        } catch (Exception e) {
            String mensaje = String.format(Notificaciones.MENSAJE_NOTIFICAR_VENCIMIENTO_OFERTA_SISINFO, idOferta, e.getMessage());
            getCorreoBean().enviarMail(Constantes.VAL_CORREO_SOPORTE_SISINFO, Notificaciones.ASUNTO_NOTIFICAR_VENCIMIENTO_OFERTA, null, null, null, mensaje);
        }
    }

    @Override
    public String eliminarOferta(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secOferta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_OFERTA));
            System.out.println("******************************************* ID OFERTA: "+secOferta.getValorLong());
            Oferta oferta = getOfertaFacade().findyById(secOferta.getValorLong());
            System.out.println("******************************************* ID OFERTA: "+oferta.getId());
            if (oferta != null) {
                Proponente proponente = getProponenteFacade().findByIdOferta(secOferta.getValorLong());
                if (proponente != null) {
                    Collection<Oferta> ofertas = proponente.getOfertas();
                    System.out.println("******************************************* ID OFERTA: "+ofertas);
                    System.out.println("******************************************* ID OFERTA: "+ofertas.size());
                    ofertas.remove(oferta);
                    System.out.println("******************************************* ID OFERTA: "+ofertas.size());
                    proponente.setOfertas(ofertas);
                    getProponenteFacade().edit(proponente);
                }
                getOfertaFacade().remove(oferta);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0146, new LinkedList<Secuencia>());
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0112, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0120, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String aplicarAOferta(String xml) {
        try {
            getParser().leerXML(xml);
            String idOferta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_OFERTA)).getValor();
            String correoAspirante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String rutaArchivo = crearHojaVidaEstudiante(correoAspirante);
            if (rutaArchivo == null) {
                Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
                secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.HOJA_DE_VIDA_VACIA_BOLSA_EMPLEO), Boolean.toString(true)));
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APLICAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.BOLSA_EMPLEO_MSJ_0002, secuencias);
            }
            Oferta oferta = getOfertaFacade().findyById(Long.parseLong(idOferta));
            if (oferta != null) {
                String to = oferta.getCorreoContacto();
                String mensaje = String.format(Notificaciones.MENSAJE_ASPIRANTE_APLICA_A_OFERTA, "Profesor(a)", oferta.getNombre(), oferta.getPeriodoVinculacion());
                getCorreoBean().enviarMail(to, Notificaciones.ASUNTO_ASPIRANTE_APLICA_A_OFERTA, null, null, rutaArchivo, mensaje);
                Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
                secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.HOJA_DE_VIDA_VACIA_BOLSA_EMPLEO), Boolean.toString(false)));
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APLICAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.BOLSA_EMPLEO_MSJ_0001, secuencias);
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APLICAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.BOLSA_EMPLEO_MSJ_0003, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_APLICAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.BOLSA_EMPLEO_MSJ_0003, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(OfertaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private String crearHojaVidaEstudiante(String correo) {
        String ruta = null;
        try {
            EstudiantePosgrado estudiantePosgrado = getEstudiantePostgradoFacade().findByCorreo(correo);
            if (estudiantePosgrado != null) {
                HojaVida hv = estudiantePosgrado.getHojaVida();
                if (hv != null) {
                    if (hv.getContenido() != null) {
                        Reader htmlreader = null;
                        try {
                            Document pdfDocument = new Document();
                            String encabezadoHTML = new String();
                            encabezadoHTML += "<h2>Hoja de Vida</h2><h4>" + estudiantePosgrado.getEstudiante().getPersona().getNombres() + " " + estudiantePosgrado.getEstudiante().getPersona().getApellidos() + "</h4><br />";
                            encabezadoHTML += darContenidoInfoPersonal(estudiantePosgrado);
                            htmlreader = new BufferedReader(new StringReader(encabezadoHTML + hv.getContenido()));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            PdfWriter.getInstance(pdfDocument, baos);
                            pdfDocument.open();
                            StyleSheet styles = new StyleSheet();
                            ArrayList arrayElementList = HTMLWorker.parseToList(htmlreader, styles);
                            for (int i = 0; i < arrayElementList.size(); ++i) {
                                Element e = (Element) arrayElementList.get(i);
                                pdfDocument.add(e);
                            }
                            pdfDocument.close();
                            byte[] bs = baos.toByteArray();
                            String rutaHojasVida = getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS);
                            File pdfFile = new File(rutaHojasVida + estudiantePosgrado.getEstudiante().getPersona().getCorreo().split("@")[0] + ".pdf");
                            FileOutputStream out = new FileOutputStream(pdfFile);
                            out.write(bs);
                            out.close();
                            ruta = pdfFile.getAbsolutePath();
                            System.out.println("RUTA HOJA DE VIDA="+ruta);
                            htmlreader.close();
                        } catch (IOException ex) {
                            Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (DocumentException ex) {
                            Logger.getLogger(EstudiantePostgradoBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return ruta;
    }

    private String darContenidoInfoPersonal(EstudiantePosgrado estudiantePosgrado) {
        String contenido = "";
        if(estudiantePosgrado != null){
            if(estudiantePosgrado.getEstudiante() != null){
                if(estudiantePosgrado.getEstudiante().getPersona() != null){
                    //Extrae la información personal del estudiante
                    String nombres = estudiantePosgrado.getEstudiante().getPersona().getNombres();
                    String apellidos = estudiantePosgrado.getEstudiante().getPersona().getApellidos();
                    String nacionalidad = estudiantePosgrado.getEstudiante().getPersona().getCiudadNacimiento();
                    String tipoDocumento = estudiantePosgrado.getEstudiante().getPersona().getTipoDocumento().getTipo();
                    String numDocumento = estudiantePosgrado.getEstudiante().getPersona().getNumDocumentoIdentidad();
                    String telefono = estudiantePosgrado.getEstudiante().getPersona().getTelefono();
                    String celular = estudiantePosgrado.getEstudiante().getPersona().getCelular();
                    String correo = estudiantePosgrado.getEstudiante().getPersona().getCorreo();

                    //La agrega al contenido
                    contenido += "<h5><b>Información Personal</b></h5>";
                    contenido += (nombres != null) ? "<h5>Nombre Completo: " + nombres : "";
                    contenido += (apellidos != null) ? " " + apellidos: "</h5>";
                    contenido += (nacionalidad != null) ? "<h5>Nacionalidad: " + nacionalidad + "</h5>" : "";
                    contenido += (tipoDocumento != null) ? "<h5>Documento: " + tipoDocumento : "";
                    contenido += (numDocumento != null) ? " - " + numDocumento + "</h5>" : "";
                    contenido += (telefono != null) ? "<h5>Teléfono fijo: " + telefono + "</h5>": "";
                    contenido += (celular != null) ? "<h5>Celular: " + celular + "</h5>" : "";
                    contenido += (correo != null) ? "<h5>Correo: " + correo + "</h5>" : "";
                }
                //Extrae la información académica
                String universidad = estudiantePosgrado.getUniversidadPregrado();
                String titulo = estudiantePosgrado.getTitulo();
                String fechaGrado = (estudiantePosgrado.getFechaGraduacion() != null) ? estudiantePosgrado.getFechaGraduacion().toString() : null;
                String ciudad = estudiantePosgrado.getCiudadUniversidadPregrado();
                String pais = (estudiantePosgrado.getPaisUniversidadPregrado() != null) ? estudiantePosgrado.getPaisUniversidadPregrado().getNombre() : null;
                String promedio = null;
                if(estudiantePosgrado.getEstudiante() != null && estudiantePosgrado.getEstudiante().getInformacion_Academica() != null
                        && estudiantePosgrado.getEstudiante().getInformacion_Academica().getPromedioTotal() != null)
                    promedio = estudiantePosgrado.getEstudiante().getInformacion_Academica().getPromedioTotal().toString();

                //La agrega al contenido
                contenido += "<h5><b><br />Información Académica</b></h5>";
                contenido += (universidad != null) ? "<h5>Nombre Universidad: " + universidad + "</h5>" : "";
                contenido += (titulo != null) ? "<h5>Titulo otorgado: " + titulo + "</h5>" : "";
                contenido += (fechaGrado != null) ? "<h5>Fecha de graduacion: " + fechaGrado + "</h5>" : "";
                contenido += (ciudad != null) ? "<h5>Ciudad de graduación: " + ciudad : "";
                contenido += (pais != null) ? " - " + pais + "</h5>" : "";
                contenido += (promedio != null) ? "<h5>Promedio: " + promedio + "</h5><br />" : "";
            }
        }
        return contenido;
    }

    //---------------------------------------
    // Métodos privados
    //---------------------------------------
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    private OfertaFacadeLocal getOfertaFacade() {
        return ofertaFacade;
    }

    private ProponenteFacadeLocal getProponenteFacade() {
        return proponenteFacade;
    }

    private CorreoLocal getCorreoBean() {
        return correoBean;
    }

    private EstudiantePosgradoFacadeLocal getEstudiantePostgradoFacade() {
        return estudiantePostgradoFacade;
    }

    private TimerGenericoBeanLocal getTimerGenerico() {
        return timerGenerico;
    }

    private ConversorBolsaEmpleo getConversor() {
        return new ConversorBolsaEmpleo(constanteBean, estudianteFacade, personaFacade, periodoFacade, paisFacade, tipoAsistenciaFacade, tipoDocumentoFacade, informacionAcademicaFacade, hojaVidaFacade, estudiantePostgradoFacade, proponenteFacade, ofertaFacade, informacionEmpresaFacade);
    }
}
