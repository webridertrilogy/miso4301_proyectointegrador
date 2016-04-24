/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.io.File;
import java.security.MessageDigest;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.CampoAdicional;
import co.uniandes.sisinfo.entities.Cargo;
import co.uniandes.sisinfo.entities.Contacto;
import co.uniandes.sisinfo.entities.EventoExterno;
import co.uniandes.sisinfo.entities.InscripcionEventoExterno;
import co.uniandes.sisinfo.entities.Respuesta;
import co.uniandes.sisinfo.entities.SectorCorporativo;
import co.uniandes.sisinfo.entities.UsuarioEventos;
import co.uniandes.sisinfo.seguridad.Protector2;
import co.uniandes.sisinfo.serviciosfuncionales.CargoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ContactoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.EventoExternoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InscripcionEventoExternoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PreguntaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.SectorCorporativoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.UsuarioEventosFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.AccesoLDAP;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.DepartamentoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeLocal;

/**
 *
 * @author Asistente
 */
@Stateless
public class ContactoBean implements  ContactoBeanLocal {

    @EJB
    private ContactoFacadeLocal contactoFacade;
    @EJB
    private SectorCorporativoFacadeLocal sectorFacade;
    @EJB
    private PaisFacadeLocal paisFacade;
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private CorreoLocal correoBean;
    @EJB
    private CargoFacadeLocal cargoFacade;
    @EJB
    private EventoExternoFacadeLocal eventoExternoFacade;
    @EJB
    private InscripcionEventoExternoFacadeLocal inscripcionFacade;
    @EJB
    private TipoDocumentoFacadeLocal tipoDocumentoFacade;
    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    @EJB
    private UsuarioEventosFacadeLocal usuarioFacade;
    @EJB
    private PreguntaFacadeLocal preguntaFacade;
    private ServiceLocator serviceLocator;
    private ParserT parser;
    public ConversorContacto conversor;
    private AccesoLDAP ldap;

    public ContactoBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            paisFacade = (PaisFacadeLocal) serviceLocator.getLocalEJB(PaisFacadeLocal.class);
//            parser = new ParserT();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            correoBean = (CorreoLocal) serviceLocator.getLocalEJB(CorreoLocal.class);
//            tipoDocumentoFacade = (TipoDocumentoFacadeLocal) serviceLocator.getLocalEJB(TipoDocumentoFacadeLocal.class);
//            departamentoFacade = (DepartamentoFacadeLocal) serviceLocator.getLocalEJB(DepartamentoFacadeLocal.class);
//
//        } catch (NamingException ex) {
//            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public AccesoLDAP getLdap() {
        if (ldap == null) {
            ldap = new AccesoLDAP();
        }
        return ldap;
    }

    public ConversorContacto getConversor() {
//        if (conversor == null) {
//            conversor = new ConversorContacto(cargoFacade, paisFacade, sectorFacade, tipoDocumentoFacade, departamentoFacade, preguntaFacade);
//        }
        return conversor;
    }

    public String consultarInscritos(String xml) {
        String respuesta = null;
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia secIdEventoExterno = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
            Long idEventoExterno = Long.parseLong(secIdEventoExterno.getValor());
            Collection<InscripcionEventoExterno> inscripciones = eventoExternoFacade.findInscritosByIdEvento(idEventoExterno);

            //se extrae el contacto de cada inscripcion
            Collection<Contacto> inscritos = new ArrayList();
            for (InscripcionEventoExterno i : inscripciones) {
                inscritos.add(i.getContacto());
            }

            Secuencia secCategoriasEventoExterno;
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();

            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INSCRITOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String consultarContactos(String comandoXml) {
        try {
            Secuencia secInformacionContactos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS), "");
            Collection<Contacto> contactos = getContactoFacade().findContactosValidos();
            for (Contacto c : contactos) {
                Secuencia secinfoCon = getConversor().getInfoContacto(c);
                secInformacionContactos.agregarSecuencia(secinfoCon);
            }
            ArrayList<Secuencia> sec = new ArrayList<Secuencia>();
            sec.add(secInformacionContactos);
            return getParser().generarRespuesta(sec, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());

            } catch (Exception ex1) {
                Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarContacto(String comandoXml) {
        try {
            parser.leerXML(comandoXml);
            //------ obtener datos del xml...
            Secuencia sec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CONTACTO));
            Long idContacto = Long.parseLong(sec.getValor().trim());
            Contacto c = getContactoFacade().find(idContacto);

            Secuencia secInformacionContactos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS), "");
            Secuencia secContacto = getConversor().getInfoContacto(c);
            secInformacionContactos.agregarSecuencia(secContacto);
            ArrayList<Secuencia> secs = new ArrayList<Secuencia>();
            secs.add(secInformacionContactos);
            return getParser().generarRespuesta(secs, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String eliminarContacto(String xml) {
        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia sec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CONTACTO));
            Long idContacto = Long.parseLong(sec.getValor().trim());
            Contacto c = getContactoFacade().find(idContacto);


            //se obtienen todas las inscripciones relacionadas hechas por el contacto que se quiere eliminar
            Collection<InscripcionEventoExterno> inscripcionesParaBorrar = getInscripcionFacade().findInscripcionesByIdContacto(idContacto);


            for (InscripcionEventoExterno i : inscripcionesParaBorrar) {
                //se obtienen los eventos relacionados con cada inscripción
                Collection<EventoExterno> eventosConInscripcion = getEventoFacade().findEventosByIdInscripcion(i.getId());
                for (EventoExterno event : eventosConInscripcion) {
                    Collection<InscripcionEventoExterno> inscripcionesRelacionadas = event.getInscripciones();
                    inscripcionesRelacionadas.remove(i);
                    event.setInscripciones(inscripcionesRelacionadas);
                    getEventoFacade().edit(event);
                }
                getInscripcionFacade().remove(i);
            }
            getContactoFacade().remove(c);

            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String editarContacto(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secInfoContactos = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS));
            Contacto nuevo = getConversor().getInfoContacto(secInfoContactos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTO)));
            Contacto actual = getContactoFacade().find(nuevo.getId());
            Contacto actualizado = actualizarContacto(actual, nuevo);
            Contacto enBD = getContactoFacade().findContactoByCorreo(actualizado.getCorreo());
            if (enBD.getId() != actual.getId()) {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0002, new ArrayList());
            }
            getContactoFacade().edit(actual);
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String agregarContacto(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secInfoContactos = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS));
            Contacto c = getConversor().getInfoContacto(secInfoContactos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTO)));
            Contacto temp = contactoFacade.findContactoByCorreo(c.getCorreo());

            //Si el contacto ya existe
            if (temp != null) {
                temp = actualizarContacto(temp, c);
                getContactoFacade().edit(temp);
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
            }

            getContactoFacade().create(c);
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String enviarCorreo(String xml) {
        try {
            String rutaUploads = getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS);
            if (!rutaUploads.endsWith("/")) {
                rutaUploads += "/";
            }
            parser.leerXML(xml);
            Secuencia secInfoContactos = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CORREO));
            Secuencia secDirsCorreo = secInfoContactos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCIONES_CORREO));
            Collection<Secuencia> correos = secDirsCorreo.getSecuencias();
            Collection<String> destinatarios = new ArrayList<String>();
            for (Secuencia secuencia : correos) {
                String correo = secuencia.getValor();
                destinatarios.add(correo);
            }
            String msj = (secInfoContactos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE)) != null) ? secInfoContactos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE)).getValor() : "";
            String asunto = secInfoContactos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO)).getValor();
            Secuencia archivos = secInfoContactos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ARCHIVOS));
            Collection<String> rutas = new ArrayList<String>();

            if (archivos != null) {
                Collection<Secuencia> rutasArchivos = archivos.getSecuencias();
                for (Secuencia secuencia : rutasArchivos) {
                    System.out.println(rutaUploads + secuencia.getValor());
                    rutas.add(rutaUploads + secuencia.getValor());
                }
            }

            // enviar el mail en si...
            String correoReplyTOCrm = getConstanteBean().getConstante(Constantes.CORREO_REPLY_TO_CRM);
            correoBean.enviarMailLista(new ArrayList<String>(), asunto, new ArrayList<String>(), destinatarios, rutas, msj, correoReplyTOCrm);

            //: borrar archivos del servidor...
            borrarArchivos(rutas);
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_CORREO_CONTACTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());

        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String olvidoContrasena(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secUsuario = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO));
            UsuarioEventos usuarioTemp = new UsuarioEventos();

            if (secUsuario != null) {
                //se guarda en usuarioTemp el usuario que en el XML
                usuarioTemp = getConversor().getUsuarioEventoExterno(secUsuario);
            }

            //con el correo del usuario que viene en el XML se busca el usuario completo para 
            UsuarioEventos usuarioExistente = usuarioFacade.findByCorreo(usuarioTemp.getCorreo());
            if (usuarioExistente != null) {
                usuarioExistente.setHashUrl(crearHashExterno(usuarioExistente));
                //usuarioExistente.setActivo(false);
                usuarioFacade.edit(usuarioExistente);

                ArrayList<Secuencia> listaSecuencias = new ArrayList<Secuencia>();
                Secuencia secUsarioEvento = getConversor().getUsuarioEvento(usuarioExistente);
                listaSecuencias.add(secUsarioEvento);

                //Envía un correo confirmando la creación de la reserva (en caso de que sea estudiante)
                String asuntoCreacion = Notificaciones.ASUNTO_OLVIDO_CONTRASENA_USUARIO_EXTERNO;
                String url = String.format(getConstanteBean().getConstante(Constantes.VAL_LINK_OLVIDO_CONTRASENIA), "", usuarioExistente.getHashUrl());
                String mensajeCreacion = String.format(Notificaciones.MENSAJE_OLVIDO_CONTRASENA_USUARIO_EXTERNO, url);

                getCorreoBean().enviarMail(usuarioTemp.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_OLVIDO_CONTRASENA_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, listaSecuencias);
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_OLVIDO_CONTRASENA_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0004, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    public ContactoFacadeLocal getContactoFacade() {
        return contactoFacade;
    }

    public InscripcionEventoExternoFacadeLocal getInscripcionFacade() {
        return inscripcionFacade;
    }

    public PaisFacadeLocal getPaisFacade() {
        return paisFacade;
    }

    public SectorCorporativoFacadeLocal getSectorFacade() {
        return sectorFacade;
    }

    public ParserT getParser() {
        return parser;
    }

    public UsuarioEventosFacadeLocal getUsuarioFacade() {
        return usuarioFacade;
    }

    public String consultarSectoresCorporativos(String comandoXml) {

        try {
            Secuencia secSectoresCorporativos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECTORES_CORPORATIVOS), "");
            Collection<SectorCorporativo> sectores = getSectorFacade().findAll();
            for (SectorCorporativo sectorCorporativo : sectores) {
                Secuencia sector = getConversor().consultarSectorCorporativo(sectorCorporativo);
                secSectoresCorporativos.agregarSecuencia(sector);
            }

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secSectoresCorporativos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECTORES_CORPORATIVOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void borrarArchivos(Collection<String> rutasArchivos) {
        String rutaUploads = getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS);

        for (String string : rutasArchivos) {
            File file = new File(rutaUploads + string);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public String borrarArchivo(String xml) {

        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            Secuencia sec = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO));
            if (sec != null) {
                String nombre = sec.getValor();
                String rutaUploads = getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS);
                File file = new File(rutaUploads + nombre);
                if (file.exists()) {
                    file.delete();
                } else {
                    System.out.println("no encontro el archivo con ruta F=" + rutaUploads);
                }
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ARCHIVO_ADJUNTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
            } else {
                return null;
            }

        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String darCargos(String xml) {

        try {
            parser.leerXML(xml);
            //------ obtener datos del xml...
            ArrayList<Secuencia> sec = new ArrayList<Secuencia>();
            Collection<Cargo> cargos = cargoFacade.findAll();
            Secuencia secInformacionCargos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CARGOS), "");

            for (Cargo c : cargos) {
                Secuencia secinfoCon = getConversor().consultarCargo(c);
                secInformacionCargos.agregarSecuencia(secinfoCon);
            }
            sec.add(secInformacionCargos);
            return getParser().generarRespuesta(sec, getConstanteBean().getConstante(Constantes.CMD_DAR_CARGOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String agregarInscripcion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secInfoContactos = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS));
            Secuencia secEvento = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
            Secuencia secRespuestas = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPUESTAS));
            Collection<Respuesta> respuestas = getConversor().consultarRespuestas(secRespuestas);
            Contacto contactoNuevo = getConversor().getInfoContacto(secInfoContactos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTO)));
            Contacto temp = contactoFacade.findContactoByCorreo(contactoNuevo.getCorreo());

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            //Si el evento no existe devuelve error.
            EventoExterno evento = getEventoFacade().find(Long.parseLong(secEvento.getValor()));
            if (evento == null) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0007, new ArrayList());
            }
            Collection<InscripcionEventoExterno> inscripciones = evento.getInscripciones();
            //si el evento está abierto y tiene cupo disponible
            Collection<InscripcionEventoExterno> inscritos = eventoExternoFacade.findInscritoByIdEventoAndIdContacto(evento.getId(), temp.getId());

            if (!inscritos.isEmpty()) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0006, new ArrayList());
            } else if (evento.getEstado().equals(getConstanteBean().getConstante(Constantes.VAL_ESTADO_EVENTO_CERRADO))) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0149, new ArrayList());
            } else if (inscripciones.size() >= evento.getCupo()) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0159, new ArrayList());
            } else if (evento.getFechaLimiteInscripciones().before(currentTime)) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0009, new ArrayList());
            }

            InscripcionEventoExterno inscripcion = new InscripcionEventoExterno();
            if (temp != null) {
                UsuarioEventos usu = usuarioFacade.findByCorreo(temp.getCorreo());
                if (usu != null) {
                    usu.setHashUrl("");
                    usuarioFacade.edit(usu);
                }
                temp = actualizarContacto(temp, contactoNuevo);
                contactoFacade.edit(temp);
                inscripcion.setContacto(temp);
                inscripcion.setRespuestas(respuestas);
                inscripciones.add(inscripcion);
                evento.setInscripciones(inscripciones);
                eventoExternoFacade.edit(evento);
            } else {
                contactoFacade.create(contactoNuevo);
                inscripcion.setContacto(contactoNuevo);
                inscripcion.setRespuestas(respuestas);
                inscripciones.add(inscripcion);
                evento.setInscripciones(inscripciones);
                eventoExternoFacade.edit(evento);
            }

            //enviando correo de incripción éxitosa.
            enviarCorreoConfirmacionEvento(evento, contactoNuevo);

            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private void enviarCorreoConfirmacionEvento(EventoExterno evento, Contacto contactoNuevo) {
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        String camposAdicionales = "";

        //String que representa los campos adicionales
        for (CampoAdicional campo : evento.getCamposAdicionales()) {
            camposAdicionales = camposAdicionales + "<b>" + campo.getNombre() + ":</b> " + campo.getValor() + "<br />";
        }

        String adjunto = evento.getRutaImagen();
        if (adjunto != null && adjunto.trim().isEmpty()) {
            adjunto = null;
        } else if (adjunto != null && !adjunto.trim().isEmpty()) {
            camposAdicionales += "<img src=\"cid:imagen\">";
        }
        String mensajeCorreo = String.format(Notificaciones.MENSAJE_INSCRIPCION_EVENTO_EXITOSA, contactoNuevo.getNombres(), evento.getTitulo(), evento.getDescripcion(), sdfFecha.format(evento.getFechaHoraInicio()), sdfHora.format(evento.getFechaHoraInicio()), camposAdicionales);
        getCorreoBean().enviarMail(contactoNuevo.getCorreo(),
                Notificaciones.ASUNTO_INSCRIPCION_EVENTO_EXITOSA,
                null, null,
                null,
                mensajeCorreo);
    }

    private Contacto actualizarContacto(Contacto actual, Contacto nuevo) {

        actual.setNombres(nuevo.getNombres());
        actual.setApellidos(nuevo.getApellidos());
        actual.setTipoDocumento(nuevo.getTipoDocumento());
        actual.setNumeroIdentificacion(nuevo.getNumeroIdentificacion());
        actual.setEmpresa(nuevo.getEmpresa());
        actual.setSector(nuevo.getSector());
        actual.setCargo(nuevo.getCargo());
        actual.setCorreoAlterno(nuevo.getCorreoAlterno());
        actual.setPais(nuevo.getPais());
        actual.setDepartamento(nuevo.getDepartamento());
        actual.setCiudad(nuevo.getCiudad());
        actual.setDireccion(nuevo.getDireccion());
        actual.setCelular(nuevo.getCelular());
        actual.setTelefono(nuevo.getTelefono());
        actual.setExtension(nuevo.getExtension());
        actual.setFax(nuevo.getFax());
        actual.setFechaActualizacion(new Date(System.currentTimeMillis()));
        actual.setIndicativo(nuevo.getIndicativo());
        actual.setObservaciones(nuevo.getObservaciones());
        actual.setOtroCargo(nuevo.getOtroCargo());
        actual.setNumeroIdentificacion(nuevo.getNumeroIdentificacion());
        actual.setPaginaWeb(nuevo.getPaginaWeb());

        return actual;
    }

    public EventoExternoFacadeLocal getEventoFacade() {
        return eventoExternoFacade;
    }

    public String loginPublico(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secUsuario = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO));
            UsuarioEventos usuarioEvento = getConversor().getUsuarioEventoExterno(secUsuario);
            UsuarioEventos temp = usuarioFacade.findByCorreo(usuarioEvento.getCorreo());
            Boolean enPrueba = Boolean.parseBoolean(constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_PRUEBA));
            if (usuarioEvento.getCorreo().endsWith(getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO))) {
                String pswd = Protector2.decrypt(usuarioEvento.getContrasena(), "SALT");
                try {
                    //valida en el LDAP que el usuario exista
                    getLdap().obtenerCorreo(usuarioEvento.getCorreo().substring(0, usuarioEvento.getCorreo().indexOf("@")));
                    //Valida si la contraseña es correcta
                    getLdap().autorizacion(usuarioEvento.getCorreo().substring(0, usuarioEvento.getCorreo().indexOf("@")), pswd, enPrueba);
                    ArrayList<Secuencia> listaSecuencias = new ArrayList<Secuencia>();
                    Contacto c = contactoFacade.findContactoByCorreo(usuarioEvento.getCorreo());
                    if (c == null) {
                        c = new Contacto();
                        c.setCorreo(usuarioEvento.getCorreo());
                        contactoFacade.create(c);
                    }
                    usuarioEvento.setContacto(c);
                    Secuencia secUsarioEvento = getConversor().getUsuarioEvento(usuarioEvento);
                    listaSecuencias.add(secUsarioEvento);
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_LOGIN_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, listaSecuencias);
                } catch (AuthenticationException ae) {
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_LOGIN_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0051, new ArrayList());
                } catch (NamingException ne) {
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_LOGIN_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0051, new ArrayList());
                }

            }
            if (temp != null) {
                if (temp.getContrasena().equals(usuarioEvento.getContrasena()) || enPrueba) {
                    if (!temp.isActivo()) {
                        return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_LOGIN_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0003, new ArrayList());
                    }
                    ArrayList<Secuencia> listaSecuencias = new ArrayList<Secuencia>();
                    Secuencia secUsarioEvento = getConversor().getUsuarioEvento(temp);
                    listaSecuencias.add(secUsarioEvento);
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_LOGIN_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, listaSecuencias);
                }
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_LOGIN_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0051, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String registrarUsuarioPublico(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secUsuario = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO));
            Secuencia secEvento = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
            UsuarioEventos usuarioEvento = getConversor().getUsuarioEventoExterno(secUsuario);
            UsuarioEventos usuarioTemp = usuarioFacade.findByCorreo(usuarioEvento.getCorreo());

            if (usuarioTemp == null) {
                usuarioEvento.setActivo(false);
                usuarioEvento.setHashUrl(crearHashExterno(usuarioEvento));
                Contacto contacto = contactoFacade.findContactoByCorreo(usuarioEvento.getCorreo());
                if (contacto == null) {
                    contacto = new Contacto();
                    contacto.setCorreo(usuarioEvento.getCorreo());
                }

                usuarioEvento.setContacto(contacto);
                usuarioFacade.create(usuarioEvento);
                ArrayList<Secuencia> listaSecuencias = new ArrayList<Secuencia>();
                Secuencia secUsarioEvento = getConversor().getUsuarioEvento(usuarioEvento);
                listaSecuencias.add(secUsarioEvento);

                //Envía un correo confirmando la creación de la reserva (en caso de que sea estudiante)
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ", new Locale("es"));
                //  String nombrePersona = reserva.getPersona().getNombres() + " " + reserva.getPersona().getApellidos();
                String asuntoCreacion = Notificaciones.ASUNTO_REGISTRO_USUARIO_EVENTO_EXTERNO;
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh':'mm a");
                String url = String.format(getConstanteBean().getConstante(Constantes.VAL_LINK_REGISTRO_USUARIO), secEvento.getValor(), usuarioEvento.getHashUrl(), "" + System.currentTimeMillis());

                String mensajeCreacion = String.format(Notificaciones.MENSAJE_REGISTRO_USUARIO_EVENTO_EXTERNO, url);

                getCorreoBean().enviarMail(usuarioEvento.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_001, listaSecuencias);
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0002, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String activarUsuarioPublico(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secHash = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_ACTIVAR_USUARIO));
            UsuarioEventos usuario = new UsuarioEventos();
            // Se revisa que el hash que viene por parametro no sea nulo
            if (secHash.getValor() != null && !secHash.getValor().equals("")) {
                usuario = getUsuarioFacade().findByHash(secHash.getValor());
            } else {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTIVAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0011, new ArrayList());
            }
            if (usuario != null) {
                usuario.setActivo(true);
                usuario.setHashUrl("");
                getUsuarioFacade().edit(usuario);
                ArrayList<Secuencia> listaSecuencias = new ArrayList<Secuencia>();
                Secuencia secUsarioEvento = getConversor().getUsuarioEvento(usuario);
                listaSecuencias.add(secUsarioEvento);
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTIVAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_001, listaSecuencias);
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTIVAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0011, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String ConsultarUsuarioHashPublico(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secHash = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HASH_ACTIVAR_USUARIO));
            UsuarioEventos usuario = getUsuarioFacade().findByHash(secHash.getValor());

            if (usuario != null) {
                ArrayList<Secuencia> listaSecuencias = new ArrayList<Secuencia>();
                Secuencia secUsarioEvento = getConversor().getUsuarioEvento(usuario);
                listaSecuencias.add(secUsarioEvento);
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_USUARIO_HASH_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_001, listaSecuencias);

            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTIVAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0002, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String cambiarContrasena(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secUsuario = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO));
            UsuarioEventos usuarioEvento = getConversor().getUsuarioEventoExterno(secUsuario);
            UsuarioEventos temp = usuarioFacade.findByCorreo(usuarioEvento.getCorreo());
            if (temp != null) {
                temp.setActivo(true);
                temp.setContrasena(usuarioEvento.getContrasena());
                temp.setHashUrl("");
                usuarioFacade.edit(temp);
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_CONTRASENA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_001, new ArrayList());
            }
            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CAMBIAR_CONTRASENA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0002, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public CorreoLocal getCorreoBean() {
        return correoBean;
    }

    private String crearHashExterno(UsuarioEventos e) {
        try {
            String stringHash = e.getCorreo().split("@")[0] + "__" + e.getId();
            byte[] bytesCadena = stringHash.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesCadena);
            String cadenaHash = toHexadecimal(thedigest);
            return cadenaHash;
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String toHexadecimal(byte[] digest) {

        String hash = "";
        for (byte aux : digest) {

            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;

    }

    public String inscribirUsuariosVIP(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secEvento = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));

            Secuencia secContactos = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTOS));
            Collection<Secuencia> listaSecuenciasContactos = secContactos.getSecuencias();

            Collection<Long> idsContactos = new ArrayList();
            for (Secuencia secuencia : listaSecuenciasContactos) {
                idsContactos.add(Long.parseLong(secuencia.getValor()));
            }

            EventoExterno evento = eventoExternoFacade.find(secEvento.getValorLong());

            if (evento == null) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0007, new ArrayList());
            }
            if (evento.getCupo() < evento.getInscripciones().size() + idsContactos.size()) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0008, new ArrayList());
            }
            if (!evento.getEstado().equals(getConstanteBean().getConstante(Constantes.VAL_ESTADO_EVENTO_ABIERTO))) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0009, new ArrayList());
            }
            Collection<InscripcionEventoExterno> inscripcionesEvento = evento.getInscripciones();
            for (Long idContacto : idsContactos) {
                Collection<InscripcionEventoExterno> inscripcionesContacto = eventoExternoFacade.findInscritoByIdEventoAndIdContacto(evento.getId(), idContacto);
                if (!inscripcionesContacto.isEmpty()) {
                    InscripcionEventoExterno inscripcion = inscripcionesContacto.iterator().next();
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), inscripcion.getContacto().getCorreo());
                    //agrega el parámetro a la lista
                    parametros.add(secParametro);
                    return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_USUARIO_PUBLICO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0010, parametros);
                }
            }

            for (Long idContacto : idsContactos) {
                Contacto c = contactoFacade.find(idContacto);
                if (c != null) {
                    InscripcionEventoExterno inscripcion = new InscripcionEventoExterno();
                    inscripcion.setContacto(c);
                    inscripcionesEvento.add(inscripcion);
                    //Correo de inscripción exitosa
                    enviarCorreoConfirmacionEvento(evento, c);
                }
            }

            evento.setInscripciones(inscripcionesEvento);
            eventoExternoFacade.edit(evento);

            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_INSCRIBIR_USUARIOS_VIP), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String consultarContactosLight(String comandoXML) {
        try {
            Secuencia secInformacionContactos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS), "");
            Collection<Contacto> contactos = getContactoFacade().findContactosValidos();
            for (Contacto c : contactos) {
                Secuencia secinfoCon = getConversor().getInfoContactoLight(c);
                secInformacionContactos.agregarSecuencia(secinfoCon);
            }
            ArrayList<Secuencia> sec = new ArrayList<Secuencia>();
            sec.add(secInformacionContactos);
            return getParser().generarRespuesta(sec, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS_LIGHT), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS_LIGHT), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darInformacionArchivoTerminosYCondiciones(String xml) {
        try {
            Secuencia secInformacionContactos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA), getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_TERMINOS_Y_CONDICIONES));
            Secuencia secMime = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MIME), "application/pdf");
            ArrayList<Secuencia> sec = new ArrayList<Secuencia>();
            sec.add(secInformacionContactos);
            sec.add(secMime);
            return getParser().generarRespuesta(sec, getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_ARCHIVO_TERMINOS_Y_CONDICIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_INFO_ARCHIVO_TERMINOS_Y_CONDICIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarInscripcion(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secEventoId = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
            String correo = secCorreo.getValor();
            Long idEvento = Long.parseLong(secEventoId.getValor());

            EventoExterno evento = eventoExternoFacade.find(idEvento);
            if (evento == null) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0007, new ArrayList());
            }

            InscripcionEventoExterno inscripcionARemover = null;
            Collection<InscripcionEventoExterno> inscripciones = evento.getInscripciones();
            for (InscripcionEventoExterno inscripcionEventoExterno : inscripciones) {
                Contacto c = inscripcionEventoExterno.getContacto();
                if (c.getCorreo().equals(correo)) {
                    inscripcionARemover = inscripcionEventoExterno;
                }
            }

            if (inscripcionARemover == null) {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CRM_ERR_0012, new ArrayList());
            }

            evento.getInscripciones().remove(inscripcionARemover);
            eventoExternoFacade.edit(evento);
            inscripcionFacade.remove(inscripcionARemover);

            return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_INSCRIPCION_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_001, new ArrayList());

        } catch (Exception ex) {
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    /**
     * Consulta los contactos, los cuales son filtrados de acuerdo a la informacion de nombre,apellido,id,cargo,empresa,ciudad,celular
     * correo,sector o fecha de actualizacion. Si todos los valores anteriores son null, se retornaran todos los contactos almacenados
     * @param comandoXml
     * @return Los contactos encontrados de acuerdo a los parametros solicidatos
     */
    public String consultarContactosFiltrados(String comandoXml) {
        try {

            getParser().leerXML(comandoXml);
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_NOMBRE)).getValor();
            String apellidos = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_APELLIDOS)).getValor();
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_ID)).getValor();
            String cargo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_CARGO)).getValor();
            String empresa = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_EMPRESA)).getValor();
            String ciudad = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_CIUDAD)).getValor();
            String celular = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_CELULAR)).getValor();
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_CORREO)).getValor();
            String sector = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_SECTOR)).getValor();
            java.util.Date fecha = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_FECHA)).getValor() != null && !getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_FECHA)).getValor().equals("")) {
                fecha = sdf.parse(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_FECHA)).getValor());
            }

            Collection<Contacto> contactos = getContactoFacade().findContactos(nombre, apellidos, id, cargo, empresa, ciudad, celular, correo, sector, fecha);

            Secuencia secInformacionContactos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS), "");
            for (Contacto c : contactos) {
                Secuencia secinfoCon = getConversor().getInfoContacto(c);
                secInformacionContactos.agregarSecuencia(secinfoCon);
            }

            ArrayList<Secuencia> sec = new ArrayList<Secuencia>();
            sec.add(secInformacionContactos);
            return getParser().generarRespuesta(sec, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS_CON_PARAMETROS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());
        } catch (Exception ex) {
            try {
                Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CONTACTOS_CON_PARAMETROS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0001, new ArrayList());

            } catch (Exception ex1) {
                Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarInscritrosEventoExterno(String comandoXml) {


        String respuesta = null;
        try {
            getParser().leerXML(comandoXml);
            //------ obtener datos del xml...
            Long idEventoExterno = Long.parseLong(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_IDEVENTO)).getValor());
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_NOMBRE)).getValor();
            String apellidos = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_APELLIDOS)).getValor();
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_ID)).getValor();
            String cargo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_CARGO)).getValor();
            String empresa = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_EMPRESA)).getValor();
            String ciudad = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_CIUDAD)).getValor();
            String celular = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_CELULAR)).getValor();
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_CORREO)).getValor();
            String sector = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_SECTOR)).getValor();
            java.util.Date fecha = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_FECHA)).getValor() != null && !getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_FECHA)).getValor().equals("")) {
                fecha = sdf.parse(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTACTO_FECHA)).getValor());
            }

            //-----obtiene los contactos a partide los parametros y el evento ---
            Collection<InscripcionEventoExterno> inscripciones = new ArrayList();
            inscripciones = eventoExternoFacade.findContactosEventoExterno(idEventoExterno, nombre, apellidos, id, cargo, empresa, ciudad, celular, correo, sector, fecha);
            //-----arma la secuencia----
            Secuencia secInscritosEventoExterno = getConversor().consultarInscripciones(inscripciones);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secInscritosEventoExterno);

            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_INSCRITOS_EVENTO_EXTERNO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ContactoBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }
}
