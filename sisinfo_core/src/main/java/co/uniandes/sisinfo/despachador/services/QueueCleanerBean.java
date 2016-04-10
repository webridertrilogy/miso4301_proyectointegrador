/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.despachador.services;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author ale-osor
 */
@Stateless
public class QueueCleanerBean implements QueueCleanerRemote, QueueCleanerLocal {


    @Resource(mappedName = "ColaConvocatoria")
    private Queue colaConvocatoria;
    @Resource(mappedName = "ReplyColaConvocatoria")
    private Queue replyColaConvocatoria;

    @Resource(mappedName = "ColaCartelera")
    private Queue colaCartelera;
    @Resource(mappedName = "ReplyColaCartelera")
    private Queue replyColaCartelera;

    @Resource(mappedName = "ColaListaNegra")
    private Queue colaListaNegra;
    @Resource(mappedName = "ReplyColaListaNegra")
    private Queue replyColaListaNegra;

    @Resource(mappedName = "ColaPreseleccion")
    private Queue colaPreseleccion;
    @Resource(mappedName = "ReplyColaPreseleccion")
    private Queue replyColaPreseleccion;

    @Resource(mappedName = "ColaConfirmacion")
    private Queue colaConfirmacion;
    @Resource(mappedName = "ReplyColaConfirmacion")
    private Queue replyColaConfirmacion;

    @Resource(mappedName = "ColaConvenio")
    private Queue colaConvenio;
    @Resource(mappedName = "ReplyColaConvenio")
    private Queue replyColaConvenio;

    @Resource(mappedName = "ColaSolicitud")
    private Queue colaSolicitud;
    @Resource(mappedName = "ReplyColaSolicitud")
    private Queue replyColaSolicitud;

    @Resource(mappedName = "ColaAspirante")
    private Queue colaAspirante;
    @Resource(mappedName = "ReplyColaAspirante")
    private Queue replyColaAspirante;

    @Resource(mappedName = "ColaArchivos")
    private Queue colaArchivos;
    @Resource(mappedName = "ReplyColaArchivos")
    private Queue replyColaArchivos;

    @Resource(mappedName = "ColaConsulta")
    private Queue colaConsulta;
    @Resource(mappedName = "ReplyColaConsulta")
    private Queue replyColaConsulta;

    @Resource(mappedName = "ColaCredencial")
    private Queue colaCredencial;
    @Resource(mappedName = "ReplyColaCredencial")
    private Queue replyColaCredencial;

    @Resource(mappedName = "ColaRegla")
    private Queue colaRegla;
    @Resource(mappedName = "ReplyColaRegla")
    private Queue replyColaRegla;

    @Resource(mappedName = "ColaProfesor")
    private Queue colaProfesor;
    @Resource(mappedName = "ReplyColaProfesor")
    private Queue replyColaProfesor;



    @Resource(mappedName = "ColasFactory")
    private QueueConnectionFactory colasFactory;

    public void queueCleaner() {
        try {
            QueueConnection connection = colasFactory.createQueueConnection();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            int i = 0;
            
            MessageConsumer mcConvoc = session.createConsumer(colaConvocatoria);
            connection.start();
            Message m =mcConvoc.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaConvocatoria");
                m =mcConvoc.receiveNoWait();
            }
            if(mcConvoc!=null)
            {
                mcConvoc.close();
            }


            MessageConsumer mcRConvoc = session.createConsumer(replyColaConvocatoria);
            connection.start();
            m =mcRConvoc.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaConvocatoria");
                m =mcRConvoc.receiveNoWait();
            }
            if(mcRConvoc!=null)
            {
                mcRConvoc.close();
            }


            MessageConsumer mcCartelera = session.createConsumer(colaCartelera);
            connection.start();
            m =mcCartelera.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaCartelera");
                m =mcCartelera.receiveNoWait();
            }
            if(mcCartelera!=null)
            {
                mcCartelera.close();
            }

            MessageConsumer mcRCartelera = session.createConsumer(replyColaCartelera);
            connection.start();
            m =mcRCartelera.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaCartelera");
                m =mcRCartelera.receiveNoWait();
            }
            if(mcRCartelera!=null)
            {
                mcRCartelera.close();
            }

            MessageConsumer mcLNegra = session.createConsumer(colaListaNegra);
            connection.start();
            m =mcLNegra.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaListaNegra");
                m =mcLNegra.receiveNoWait();
            }
            if(mcLNegra!=null)
            {
                mcLNegra.close();
            }

            MessageConsumer mcRLNegra = session.createConsumer(replyColaListaNegra);
            connection.start();
            m =mcRLNegra.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaListaNegra");
                m =mcRLNegra.receiveNoWait();
            }
            if(mcRLNegra!=null)
            {
                mcRLNegra.close();
            }


            MessageConsumer mcPres = session.createConsumer(colaPreseleccion);
            connection.start();
            m =mcPres.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaPreseleccion");
                m =mcPres.receiveNoWait();
            }
            if(mcPres!=null)
            {
                mcPres.close();
            }


            MessageConsumer mcRPres = session.createConsumer(replyColaPreseleccion);
            connection.start();
            m =mcRPres.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaPreseleccion");
                m =mcRPres.receiveNoWait();
            }
            if(mcRPres!=null)
            {
                mcRPres.close();
            }


            MessageConsumer mcConfi= session.createConsumer(colaConfirmacion);
            connection.start();
            m =mcConfi.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaConfirmacion");
                m =mcConfi.receiveNoWait();
            }
            if(mcConfi!=null)
            {
                mcConfi.close();
            }

            MessageConsumer mcRConfi= session.createConsumer(replyColaConfirmacion);
            connection.start();
            m =mcRConfi.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaConfirmacion");
                m =mcRConfi.receiveNoWait();
            }
            if(mcRConfi!=null)
            {
                mcRConfi.close();
            }
            

            MessageConsumer mcConve= session.createConsumer(colaConvenio);
            connection.start();
            m =mcConve.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaConvenio");
                m =mcConve.receiveNoWait();
            }
            if(mcConve!=null)
            {
                mcConve.close();
            }


            MessageConsumer mcRConve= session.createConsumer(replyColaConvenio);
            connection.start();
            m =mcRConve.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaConvenio");
                m =mcRConve.receiveNoWait();
            }
            if(mcRConve!=null)
            {
                mcRConve.close();
            }

            MessageConsumer mcSoli= session.createConsumer(colaSolicitud);
            connection.start();
            m =mcSoli.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaSolicitud");
                m =mcSoli.receiveNoWait();
            }
            if(mcSoli!=null)
            {
                mcSoli.close();
            }
            

            MessageConsumer mcRSoli= session.createConsumer(replyColaSolicitud);
            connection.start();
            m =mcRSoli.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaSolicitud");
                m =mcRSoli.receiveNoWait();
            }
            if(mcRSoli!=null)
            {
                mcRSoli.close();
            }
            

            MessageConsumer mcAspir= session.createConsumer(colaAspirante);
            connection.start();
            m =mcAspir.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaAspirante");
                m =mcAspir.receiveNoWait();
            }
            if(mcAspir!=null)
            {
                mcAspir.close();
            }

            MessageConsumer mcRAspir= session.createConsumer(replyColaAspirante);
            connection.start();
            m =mcRAspir.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaAspirante");
                m =mcRAspir.receiveNoWait();
            }
            if(mcRAspir!=null)
            {
                mcRAspir.close();
            }

            MessageConsumer mcArchi= session.createConsumer(colaArchivos);
            connection.start();
            m =mcArchi.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaArchivos");
                m =mcArchi.receiveNoWait();
            }
            if(mcArchi!=null)
            {
                mcArchi.close();
            }

            MessageConsumer mcRArchi= session.createConsumer(replyColaArchivos);
            connection.start();
            m =mcRArchi.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaArchivos");
                m =mcRArchi.receiveNoWait();
            }
            if(mcRArchi!=null)
            {
                mcRArchi.close();
            }

            MessageConsumer mcCred= session.createConsumer(colaCredencial);
            connection.start();
            m = mcCred.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaCredencial");
                m =mcCred.receiveNoWait();
            }
            if(mcCred!=null)
            {
                mcCred.close();
            }

            MessageConsumer mcRCred= session.createConsumer(replyColaCredencial);
            connection.start();
            m = mcRCred.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaCredencial");
                m = mcRCred.receiveNoWait();
            }
            if(mcRCred!=null)
            {
                mcRCred.close();
            }

            MessageConsumer mcRegla= session.createConsumer(colaRegla);
            connection.start();
            m = mcRegla.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaRegla");
                m =mcRegla.receiveNoWait();
            }
            if(mcRegla!=null)
            {
                mcRegla.close();
            }

            MessageConsumer mcRRegla= session.createConsumer(replyColaRegla);
            connection.start();
            m = mcRRegla.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaRegla");
                m = mcRRegla.receiveNoWait();
            }
            if(mcRRegla!=null)
            {
                mcRRegla.close();
            }

            MessageConsumer mcConsu= session.createConsumer(colaConsulta);
            connection.start();
            m =mcConsu.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaConsulta");
                m =mcConsu.receiveNoWait();
            }
            if(mcConsu!=null)
            {
                mcConsu.close();
            }

            MessageConsumer mcRConsu= session.createConsumer(replyColaConsulta);
            connection.start();
            m =mcRConsu.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaArchivos");
                m =mcRConsu.receiveNoWait();
            }
            if(mcRConsu!=null)
            {
                mcRConsu.close();
            }

            MessageConsumer mcProfesor= session.createConsumer(colaProfesor);
            connection.start();
            m =mcProfesor.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ColaProfesor");
                m =mcProfesor.receiveNoWait();
            }
            if(mcProfesor!=null)
            {
                mcProfesor.close();
            }

            MessageConsumer mcRProfesor= session.createConsumer(replyColaProfesor);
            connection.start();
            m =mcRProfesor.receiveNoWait();
            while(m!=null){
                i++;
                String texto = ((TextMessage)m).getText();
                System.out.println("Borrado el mensaje: "+ texto+ " de la cola ReplyColaProfesor");
                m =mcRProfesor.receiveNoWait();
            }
            if(mcRProfesor!=null)
            {
                mcRProfesor.close();
            }


            System.out.println("");
            System.out.println("Se borraron: "+ i + " mensajes.");



            if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
            }
            }
            if (connection != null) {
               // connection.close();
            }

        } catch (JMSException ex) {
            Logger.getLogger(QueueCleanerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method" or "Web Service > Add Operation")
 
}
