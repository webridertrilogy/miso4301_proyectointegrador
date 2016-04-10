/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.despachador.services;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author JuanCamilo
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ColaCredencialBean implements ColaCredencialLocal {

    @Resource(mappedName = "ColaCredencial")
    private Queue colaCredencial;

    @Resource
    private UserTransaction userTransaction;

    @Resource(mappedName = "ReplyColaCredencial")
    private Queue replyColaCredencial;

    @Resource(mappedName = "ColasFactory")
    private QueueConnectionFactory colasFactory;


    public String resolverComando(String comandoXML) {
        String respuesta = "";
        try {
            respuesta = enviarMensajeACola(comandoXML);
        } catch (JMSException ex) {
            Logger.getLogger(ColaArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    private String enviarMensajeACola(String comandoXML)throws JMSException{
        String respuesta = "";
        try {

            QueueConnection connection = null;
            QueueSession session = null;

            userTransaction.begin();

            connection = colasFactory.createQueueConnection();
            connection.start();
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            TextMessage messageRequested = session.createTextMessage();
            messageRequested.setText(comandoXML);
            String idMessageRequested = UUID.randomUUID().toString();
            messageRequested.setJMSCorrelationID(idMessageRequested);
            messageRequested.setJMSReplyTo(replyColaCredencial);

            MessageProducer messageProducer = session.createProducer(colaCredencial);

            messageProducer.send(messageRequested);

            if(messageProducer!=null)
                messageProducer.close();

            if (session != null)
                session.close();

            if (connection != null)
                connection.close();

            userTransaction.commit();



            userTransaction.begin();

            connection = colasFactory.createQueueConnection();
            connection.start();
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer messageConsumer = session.createConsumer(replyColaCredencial);

            Message messageReplied = messageConsumer.receive();
            respuesta = ((TextMessage) messageReplied).getText();
            String idMessageReplied = messageReplied.getJMSCorrelationID();

            if(messageConsumer!=null)
                messageConsumer.close();

            if (session!=null)
                session.close();

            if (connection!=null)
                connection.close();

            userTransaction.commit();

            MessageProducer messageProducerReply = null;

            while (!idMessageReplied.equals(idMessageRequested)) {

                userTransaction.begin();

                connection = colasFactory.createQueueConnection();
                connection.start();
                session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                messageProducerReply = session.createProducer(replyColaCredencial);
                messageProducerReply.send(messageReplied);

                if(messageProducerReply!=null)
                    messageProducerReply.close();

                if (session!=null)
                    session.close();

                if (connection!=null)
                    connection.close();

                userTransaction.commit();


                userTransaction.begin();

                connection = colasFactory.createQueueConnection();
                connection.start();
                session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                messageConsumer = session.createConsumer(replyColaCredencial);
                messageReplied = messageConsumer.receive();
                idMessageReplied = messageReplied.getJMSCorrelationID();
                respuesta = ((TextMessage) messageReplied).getText();

                if(messageConsumer!=null)
                    messageConsumer.close();

                if (session!=null)
                    session.close();

                if (connection!=null)
                    connection.close();

                userTransaction.commit();
            }


        } catch (RollbackException ex) {
            Logger.getLogger(ColaArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(ColaArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(ColaArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ColaArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ColaArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ColaArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(ColaArchivosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            return respuesta;
        }

    }

    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method" or "Web Service > Add Operation")
 
}
