    /*
 * Copyright 1997-2008 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.CorreoAuditoria;
import co.uniandes.sisinfo.entities.FiltroCorreo;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteLocal;
import co.uniandes.sisinfo.serviciosnegocio.CorreoSinEnviarBeanLocal;
import co.uniandes.sisinfo.serviciosnegocio.CredencialLocal;
import co.uniandes.sisinfo.serviciosnegocio.FiltroCorreoBeanLocal;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.NamingException;

/**
 * Servicios Correo
 */
@Stateless
@EJB(name = "CorreoBean", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal.class)
public class CorreoBean implements CorreoLocal {

    HashMap<String, String> escape = new HashMap<String, String>();
    //@EJB
    private CredencialLocal credencialBean;
    @EJB
    private CorreoAuditoriaFacadeLocal auditoriaLocal;
//  @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private FiltroCorreoFacadeLocal filtroCorreoFacade;
//    @EJB
//    private CorreoSinEnviarBeanLocal correoSinEnviarBean;
 //   @EJB
  //  private FiltroCorreoBeanLocal filtroCorreoBean;


    String to, subject = null, from = null,
            cc = null, bcc = null, url = null;
    String mailhost = null;
    String port = "587";
    String mailer = null;
    String file = null;
    String protocol = null, host = null, user = null, password = null;
    String record = null;	// name of folder in which to record mail
    int optind;
    boolean debug = true;
    private ServiceLocator serviceLocator;

    public CorreoBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            credencialBean = (CredencialLocal) serviceLocator.getLocalEJB(CredencialLocal.class);
//            llenarHash();
//        } catch (NamingException ex) {
//            Logger.getLogger(CorreoBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void enviarMail(String para, String asunto, String cc, String cco, String archivo, String mensaje) {
//        FiltroCorreo filtro = filtroCorreoBean.evaluarFiltros(para, asunto, cc, cco, archivo, mensaje);
//        if(filtro == null){
//            // Si el mensaje no se ajusta a ningun filtro, entonces se envia normalmente
//            enviarMailDirecto(para, asunto, cc, cco, archivo, mensaje,null);
//        }else{
//            // Si el mensaje se ajusta a alguno de los filtros, entonces se debe cambiar el destinatario o guardarlo en la lista de correos sin enviar
//            if(filtro.getRedireccion()==null){
//               // correoSinEnviarBean.agregarCorreo(para, asunto, cc, cco, archivo, mensaje);
//            }else{
//                enviarMailDirecto(filtro.getRedireccion(), asunto, cc, cco, archivo, mensaje,null);
//            }            
//        }      
    }

    
    public void enviarMail(String para, String asunto, String cc, String cco, String archivo, String mensaje,String imagen) {
//        FiltroCorreo filtro = filtroCorreoBean.evaluarFiltros(para, asunto, cc, cco, archivo, mensaje);
//        if(filtro == null){
//            // Si el mensaje no se ajusta a ningun filtro, entonces se envia normalmente
//            enviarMailDirecto(para, asunto, cc, cco, archivo, mensaje,imagen);
//        }else{
//            // Si el mensaje se ajusta a alguno de los filtros, entonces se debe cambiar el destinatario o guardarlo en la lista de correos sin enviar
//            if(filtro.getRedireccion()==null){
//            //    correoSinEnviarBean.agregarCorreo(para, asunto, cc, cco, archivo, mensaje);
//            }else{
//                enviarMailDirecto(filtro.getRedireccion(), asunto, cc, cco, archivo, mensaje,imagen);
//            }
//
//        }
    }

    private void enviarMailDirecto(String para, String asunto, String cc, String cco, String archivo, String mensaje,String imagen) {
        mensaje = procesarCaracteres(mensaje);
        /*Sección de auditoría*/
        CorreoAuditoria ca=new CorreoAuditoria();
        ca.setAsunto(asunto);
        ca.setDestinatarios(para);
        ca.setDestinatariosCC(cc);
        ca.setDestinatariosCCO(cco);
        String mensajeAuditoria=mensaje;
        Timestamp fechaEnvio=new Timestamp(System.currentTimeMillis());
        ca.setFechaEnvio(fechaEnvio);
        if(mensajeAuditoria.length()>450000){
            mensajeAuditoria=mensajeAuditoria.substring(0, 450000-1);
        }
        ca.setMensaje(mensajeAuditoria);
        ca.setNombreAdjunto(archivo);
        ca.setEnviado(false);
        try{
            auditoriaLocal.create(ca);
        }
        catch(Exception e){
            Logger.getLogger(CorreoAuditoriaFacade.class.getName()).log(Level.SEVERE, null, e);
        }

        try {

            Boolean enPrueba = Boolean.parseBoolean(constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_PRUEBA));
            if (enPrueba != null && enPrueba) {
                
                mensaje = "para:"+para+"<br>"+"cc:"+cc+"<br>"+"cco:"+cco+"<br>"+mensaje;
                asunto = "[PruebaEventos] "+asunto;
                para = constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_CORREO_PRUEBA);
                //para = "sisinfopreprod1@yahoo.com";
                cc = "";
                cco = "";


                System.out.println("ENTRO EN PRUEBA Y ASIGNO CORREOS CON ASUNTO CAMBIADO");

            } else {
                //para = "imau88@gmail.com";
                //cc = "lmarcela.morales@gmail.com";
                //cco = "olucim@gmail.com";

                System.out.println("NO ENTRO EN PRUEBA Y HABRIAN SALIDO DE VERDAD");
            }

            mailhost = getConstanteBean().getConstante(Constantes.CONFIG_MAIL_HOST);
            mailer = getConstanteBean().getConstante(Constantes.CONFIG_MAIL_MAILER);

            port = getConstanteBean().getConstante(Constantes.CONFIG_MAIL_HOST_PORT);

            to = para;
            subject = asunto;
            this.cc = cc;
            bcc = cco;
            file = archivo;


            //Initialize the JavaMail Session.
            Properties props = System.getProperties();
            //if (mailhost != null) {
            //  props.put("mail.smtp.host", mailhost);
            //}
            //props.put("mail.smtp.auth", "true");
            //props.put("mail.smtp.ssl.enable", "true");
            //props.put("mail.smtp.port", "25");
            //props.put("mail.smtps.port", "25");

          /*  props.put("mail.smtp.host", "smtp.uniandes.edu.co");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.starttls.enable", "true");*/

            //TODO:para probar...
            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.starttls.enable", "true");


            // Get a Session object
            //Session session = Session.getInstance(props, null);
            //if (debug) {
            //  session.setDebug(true);
            //}

            String cuentaMailUser = getConstanteBean().getConstante(Constantes.CONFIG_MAIL_USER);
            String contrasena = credencialBean.darContrasena(cuentaMailUser);

            //TODO: agregue:+"@uniandes.edu.co"
            MailAuthenticator auth = new MailAuthenticator(cuentaMailUser+"@uniandes.edu.co", contrasena);
            Session session = Session.getInstance(props, auth);


            //Construct the message and send it.
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(cuentaMailUser + Notificaciones.DOMINIO_CUENTA_UNIANDES));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            if (cc != null) {
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(cc, false));
            }
            if (bcc != null) {
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(bcc, false));
            }

            msg.setSubject(subject);
            String text = mensaje;

            if (file != null || imagen != null) {
                // Attach the specified file.
                // We need a multipart message to hold the attachment.
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setContent(text, "text/html");
                MimeMultipart mp = new MimeMultipart("related");
                mp.addBodyPart(mbp1);
                if(imagen!=null && !imagen.trim().isEmpty()){
                    MimeBodyPart imageBodyPart = new MimeBodyPart();
                    File fileImagen = new File(imagen);
                    DataSource source = new FileDataSource(fileImagen);
                    imageBodyPart.setDataHandler(new DataHandler(source));
                    imageBodyPart.setFileName(fileImagen.getName());
                    imageBodyPart.setHeader("Content-ID", "<imagen>");
                    imageBodyPart.setDisposition(MimeBodyPart.INLINE);
                    mp.addBodyPart(imageBodyPart);
                }
                if(file!=null){
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.attachFile(file);
                    mp.addBodyPart(mbp2);
                }
                msg.setContent(mp);
            } else {
                // If the desired charset is known, you can use
                // setText(text, charset)
                msg.setContent(text, "text/html");
            }


            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            // send the thing off

            Transport.send(msg);
            //Transport tr = session.getTransport("smtps");
            //System.out.println("MailHost:" + mailhost);
            //System.out.println("MailUser:"+ cuentaMailUser);
            //System.out.println("MailPasswd:"+contrasena);
            //tr.connect(mailhost, cuentaMailUser, contrasena);
            //tr.sendMessage(msg, msg.getAllRecipients());
            //tr.close();

            System.out.println("\nMail was sent successfully.");
            ca.setEnviado(true);


            //Save a copy of the message, if requested.

            if (record != null) {
                // Get a Store object
                Store store = null;
                if (url != null) {
                    URLName urln = new URLName(url);
                    store = session.getStore(urln);
                    store.connect();
                } else {
                    if (protocol != null) {
                        store = session.getStore(protocol);
                    } else {
                        store = session.getStore();
                    }

                    // Connect
                    if (host != null || user != null || password != null) {
                        store.connect(host, user, password);
                    } else {
                        store.connect();
                    }
                }

                // Get record Folder.  Create if it does not exist.
                Folder folder = store.getFolder(record);
                if (folder == null) {
                    System.err.println("Can't get record folder.");
                    System.exit(1);
                }
                if (!folder.exists()) {
                    folder.create(Folder.HOLDS_MESSAGES);
                }

                Message[] msgs = new Message[1];
                msgs[0] = msg;
                folder.appendMessages(msgs);

                System.out.println("Mail was recorded successfully.");
                ca.setEnviado(true);
            }

            // Reporat que el correo ha sido enviado.
            if (ca.getEnviado()) {

               try{
                    auditoriaLocal.create(ca);
                }
                catch(Exception e){
                    Logger.getLogger(CorreoAuditoriaFacade.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void llenarHash() {
        escape.put("¡", "&iexcl;");
        escape.put("¿", "&iquest;");
        escape.put("à", "&agrave;");
        escape.put("á", "&aacute;");
        escape.put("À", "&Agrave;");
        escape.put("Á", "&Aacute;");
        escape.put("â", "&acirc;");
        escape.put("ä", "&auml;");
        escape.put("Ä", "&Auml;");
        escape.put("Â", "&Acirc;");
        escape.put("ç", "&ccedil;");
        escape.put("Ç", "&Ccedil;");
        escape.put("é", "&eacute;");
        escape.put("É", "&Eacute;");
        escape.put("è", "&egrave;");
        escape.put("È", "&Egrave;");
        escape.put("ê", "&ecirc;");
        escape.put("Ê", "&Ecirc;");
        escape.put("ë", "&euml;");
        escape.put("Ë", "&Euml;");
        escape.put("ï", "&iuml;");
        escape.put("Ï", "&Iuml;");
        escape.put("í", "&iacute;");
        escape.put("Í", "&Iacute;");
        escape.put("ì", "&igrave;");
        escape.put("Ì", "&Igrave;");
        escape.put("ô", "&ocirc;");
        escape.put("Ô", "&Ocirc;");
        escape.put("ö", "&ouml;");
        escape.put("ó", "&oacute;");
        escape.put("Ó", "&Oacute;");
        escape.put("ò", "&ograve;");
        escape.put("Ò", "&Ograve;");
        escape.put("Ö", "&Ouml;");
        escape.put("ù", "&ugrave;");
        escape.put("ú", "&uacute;");
        escape.put("Ú", "&Uacute;");
        escape.put("Ù", "&Ugrave;");
        escape.put("û", "&ucirc;");
        escape.put("Û", "&Ucirc;");
        escape.put("ü", "&uuml;");
        escape.put("Ü", "&Uuml;");
        escape.put("ñ", "&ntilde;");
        escape.put("Ñ", "&Ntilde;");
    }

    private String procesarCaracteres(String mensaje) {
        String mensajeProcesado = mensaje;
        Set<String> caractereS = escape.keySet();
        for (String caracter : caractereS) {
            if (mensajeProcesado.contains(caracter)) {
                mensajeProcesado = mensajeProcesado.replaceAll(caracter, escape.get(caracter));
            }
        }
        return mensajeProcesado;
    }

    class MyPasswordAuthenticator extends Authenticator {

        String user;
        String pw;

        public MyPasswordAuthenticator(String username, String password) {
            super();
            this.user = username;
            this.pw = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pw);
        }
    }

    /**
     * envia un correo a listas de correo
     * @param para: Collection<String> con las direcciones a las que se va a enviar
     * @param asunto: asunto del correo
     * @param cc: Collection<String> con las direcciones a las que se va a enviar copia
     * @param cco: Collection<String> con las direcciones a las que se va a enviar copia oculta
     * @param nombresArchivos: nombre de los archivos que se van a adjuntar(con su ruta)
     * @param mensaje: mensaje a enviar
     * @param correoReplyTO: String direccion de reply to, si no se desea colocar entonces null
     */
    public void enviarMailLista(Collection<String> para, String asunto, Collection<String> cc, Collection<String> cco, Collection<String> nombresArchivos, String mensaje, String correoReplyTO) {
        //Comentado para pruebas (es mejor que no envíe correos)
        int maxCorreos = 40;
        mensaje = procesarCaracteres(mensaje);

        /*Sección de auditoría*/
        CorreoAuditoria ca=new CorreoAuditoria();
        ca.setAsunto(asunto);
        String paraX="";
        for (String string : para) {
            paraX+=string;
        }
        ca.setDestinatarios(paraX);
        String ccX="";
        for (String string : cc) {
            ccX+=string;
        }
        ca.setDestinatariosCC(ccX);
        String ccoX="";
        for (String string : cco) {
            ccoX+=string;
        }
        ca.setDestinatariosCCO(ccoX);
        String mensajeAuditoria=mensaje;
        Timestamp fechaEnvio=new Timestamp(System.currentTimeMillis());
        ca.setFechaEnvio(fechaEnvio);
        if(mensajeAuditoria.length()>450000){
            mensajeAuditoria=mensajeAuditoria.substring(0, 450000-1);
        }
        ca.setMensaje(mensajeAuditoria);
        ca.setNombreAdjunto("");
        try{
            auditoriaLocal.create(ca);
        }
        catch(Exception e){
            Logger.getLogger(CorreoAuditoriaFacade.class.getName()).log(Level.SEVERE, null, e);
        }


        try {
            Boolean enPrueba = Boolean.parseBoolean(constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_PRUEBA));
            if (enPrueba != null && enPrueba) {
                String paraStr = "";
                for (String string : para) {
                    paraStr+=string;
                }
                String ccStr="";
                for (String string : cc) {
                    ccStr+=string;
                }
                String ccoStr = "";
                for (String string : cco) {
                    ccoStr+=string;
                }
                mensaje = "para:"+paraStr+"<br>"+"cc:"+ccStr+"<br>"+"cco:"+ccoStr+"<br>"+mensaje;
                para = new ArrayList<String>();
                cc = new ArrayList<String>();
                cco = new ArrayList<String>();
                para.add(constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_CORREO_PRUEBA));
                //para = "sisinfopreprod1@yahoo.com";

                System.out.println("SISINFO ESTA EN PRUEBA MSJ ENVIADO  A DESARROLLO");
            } else {
//                para = new ArrayList<String>();
//                cc = new ArrayList<String>();
//                cco = new ArrayList<String>();
//                para.add("olucim@gmail.com");
//                para.add("lm.morales70@uniandes.edu.co");
//                para.add("yovasxp@gmail.com");
//                cco.add("imau88@gmail.com");

                System.out.println("SISINFO NO ESTA EN PRUEBA MSJ ENVIADO  A TODOS");
            }



            mailhost = getConstanteBean().getConstante(Constantes.CONFIG_MAIL_HOST);
            mailer = getConstanteBean().getConstante(Constantes.CONFIG_MAIL_MAILER);
            port = getConstanteBean().getConstante(Constantes.CONFIG_MAIL_HOST_PORT);

            // to = para;
            subject = asunto;


            //Initialize the JavaMail Session.
            Properties props = System.getProperties();
            //if (mailhost != null) {
            //  props.put("mail.smtp.host", mailhost);
            //}
            //props.put("mail.smtp.auth", "true");
            //props.put("mail.smtp.ssl.enable", "true");
            //props.put("mail.smtp.port", "25");
            //props.put("mail.smtps.port", "25");

            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.starttls.enable", "true");

            // Get a Session object
            //Session session = Session.getInstance(props, null);
            //if (debug) {
            //  session.setDebug(true);
            //}

            String cuentaMailUser = getConstanteBean().getConstante(Constantes.CONFIG_MAIL_USER);
            String contrasena = credencialBean.darContrasena(cuentaMailUser);

            MailAuthenticator auth = new MailAuthenticator(cuentaMailUser, contrasena);
            Session session = Session.getInstance(props, auth);


            //Construct the message and send it.
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(cuentaMailUser + Notificaciones.DOMINIO_CUENTA_UNIANDES));


            //----------------------
            if (para != null) {

                String cadenaCorreos = "";
                int k = 0;
                for (String correo : para) {
                    cadenaCorreos += correo + ",";
                    k++;
                }
                cadenaCorreos = cadenaCorreos.substring(0, cadenaCorreos.length() - 1);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cadenaCorreos));
            }
            //--------------------------

            if (cc != null) {
                String cadenaCorreos = "";
                for (String correo : cc) {
                    cadenaCorreos += correo + ",";
                }
                if (!cc.isEmpty()) {
                    cadenaCorreos = cadenaCorreos.substring(0, cadenaCorreos.length() - 1);
                }
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cadenaCorreos));
            }
            if (cco != null) {
                String cadenaCorreos = "";
                for (Iterator<String> it = cco.iterator(); it.hasNext();) {
                    String correo = it.next();
                    cadenaCorreos += correo + ",";
                }
                cadenaCorreos = cadenaCorreos.substring(0, cadenaCorreos.length() - 1);
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(cadenaCorreos));
            }
            //cambia el reply to
            if (correoReplyTO != null) {
                InternetAddress[] replyTO = new InternetAddress[1];
                replyTO[0] = new InternetAddress(correoReplyTO);
                msg.setReplyTo(replyTO);
            }

            msg.setSubject(subject);

            String text = mensaje;

            if (nombresArchivos != null && !nombresArchivos.isEmpty()) {
                // Attach the specified file.
                // We need a multipart message to hold the attachment.
                MimeBodyPart parteTextoMsj = new MimeBodyPart();
                parteTextoMsj.setContent(text, "text/html");
                //-------AGREGAR ATTACHMENT---------
                //se agrega cada adjunto a una colleccion de mimebodyparts
                Collection<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();
                for (String nombreArchivo : nombresArchivos) {
                    MimeBodyPart mbp2 = new MimeBodyPart();

                    DataSource source = new FileDataSource(nombreArchivo);

                    mbp2.setDataHandler(new DataHandler(source));
                    File f = new File(nombreArchivo);
                    mbp2.setFileName(f.getName());
                    attachments.add(mbp2);
                }
                //------se agregan los diferentes pedasos del correo en un multipart--------------
                MimeMultipart msjCompleto = new MimeMultipart();
                msjCompleto.addBodyPart(parteTextoMsj);
                for (MimeBodyPart archivoAdjunto : attachments) {
                    msjCompleto.addBodyPart(archivoAdjunto);
                }
                msg.setContent(msjCompleto);
            } else {
                // If the desired charset is known, you can use
                // setText(text, charset)
                msg.setContent(text, "text/html");
            }

            //  setHTMLContent(msg);

            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            if (cco==null || ( cco.size()>0 && cco.size() < maxCorreos)) {
                Transport.send(msg);
                System.out.println("\nMail was sent successfully.");
                ca.setEnviado(true);
            } else {
                //armar paquetes de correos
                int numPaquetes = cco.size() / maxCorreos;
                Object[] listaCorreosCco = cco.toArray();
                int k = 0;
                for (int i = 0; i < numPaquetes; i++) {
                    String cadenaCorreos = "";
                    for (; k < (i + 1) * maxCorreos; k++) {
                        String correo = (String) listaCorreosCco[k];
                        cadenaCorreos += correo + ",";
                    }
                    cadenaCorreos = cadenaCorreos.substring(0, cadenaCorreos.length() - 1);
                    msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(cadenaCorreos));
                    Transport.send(msg);
                    System.out.println("\nMail was sent successfully. Package "+(i+1));
                    ca.setEnviado(true);
                }
                //El resto
                String cadenaCorreosFaltan = "";
                for (int n = k; n < cco.size(); n++) {
                    String correo = (String) listaCorreosCco[n];
                    cadenaCorreosFaltan += correo + ",";
                }
                cadenaCorreosFaltan = cadenaCorreosFaltan.substring(0, cadenaCorreosFaltan.length() - 1);
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(cadenaCorreosFaltan));
                Transport.send(msg);
                System.out.println("\nMail was sent successfully. (Remainder)");
                ca.setEnviado(true);
            }

            //Save a copy of the message, if requested.

            if (record != null) {
                // Get a Store object
                Store store = null;
                if (url != null) {
                    URLName urln = new URLName(url);
                    store = session.getStore(urln);
                    store.connect();
                } else {
                    if (protocol != null) {
                        store = session.getStore(protocol);
                    } else {
                        store = session.getStore();
                    }

                    // Connect
                    if (host != null || user != null || password != null) {
                        store.connect(host, user, password);
                    } else {
                        store.connect();
                    }
                }

                // Get record Folder.  Create if it does not exist.
                Folder folder = store.getFolder(record);
                if (folder == null) {
                    System.err.println("Can't get record folder.");
                    System.exit(1);
                }
                if (!folder.exists()) {
                    folder.create(Folder.HOLDS_MESSAGES);
                }

                Message[] msgs = new Message[1];
                msgs[0] = msg;
                folder.appendMessages(msgs);

                System.out.println("Mail was recorded successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set a single part html content.
    // Sending data of any type is similar.
    public static void setHTMLContent(Message msg) throws MessagingException, IOException {

        String html = "<html><body>"
                + msg.getContent().toString()
                + "</body></html>";

        // HTMLDataSource is an inner class
        msg.setDataHandler(new DataHandler(new HTMLDataSource(html)));
    }

    public ConstanteLocal getConstanteBean() {
        return null;//constanteBean;
    }

    /*
     * Inner class to act as a JAF datasource to send HTML e-mail content
     */
    static class HTMLDataSource implements DataSource {

        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        // Return html string in an InputStream.
        // A new stream must be returned each time.
        public InputStream getInputStream() throws IOException {
            if (html == null) {
                throw new IOException("Null HTML");
            }
            return new ByteArrayInputStream(html.getBytes());
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        public String getContentType() {
            return "text/html";
        }

        public String getName() {
            return "JAF text/html dataSource to send e-mail only";
        }
    }
}
