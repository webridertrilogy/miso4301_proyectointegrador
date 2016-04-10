/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.despachador.messageBeans;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosnegocio.ConfirmacionLocal;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 *
 * @author ale-osor
 */
@MessageDriven(mappedName = "ColaConfirmacion", activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "endpointPoolMaxSize",propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "endpointExceptionRedeliveryAttempts", propertyValue = "0"),
        @ActivationConfigProperty(propertyName = "sendUndeliverableMsgsToDMQ", propertyValue = "false"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    })
public class ConfirmacionMessageBean implements MessageListener {

    @EJB
    private ConfirmacionLocal confirmacionBean;
    private ParserT parserBean;
    @EJB
    private ConstanteRemote constanteBean;

    @Resource(mappedName = "ColasFactory")
    private QueueConnectionFactory colasFactory;

    private ServiceLocator serviceLocator;
    
    public ConfirmacionMessageBean() {
        try {
            parserBean = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ConfirmacionMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessage(Message message) {
        String comandoXML ="";
        try {
            boolean redelivered = message.getJMSRedelivered();

            comandoXML = ((TextMessage) message).getText();

            Destination replyDestination = message.getJMSReplyTo();

            if(!redelivered)
            {
                if(replyDestination!=null){

                QueueConnection connection = colasFactory.createQueueConnection();

                QueueSession session = (QueueSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                MessageProducer replyProducer = session.createProducer(replyDestination);

                TextMessage replyMessage = session.createTextMessage();

                replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());

                String respuesta = "";

                try {
                    parserBean.leerXML(comandoXML);
                } catch (Exception ex) {
                    Logger.getLogger(ConvocatoriaMessageBean.class.getName()).log(Level.SEVERE, null, ex);
                }

                String nombreComando = parserBean.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
//                if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_ESTUDIANTE)))
//                {
//                    respuesta = confirmacionBean.confirmarEstudiante(comandoXML);
//                }
//                else
                    if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION)))
                {
                    respuesta = confirmacionBean.confirmarSeccion(comandoXML);
                }
                else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION)))
                {
                    respuesta = confirmacionBean.actualizarSolicitudVerificacion(comandoXML);
                }

                replyMessage.setText(respuesta);

                replyProducer.send(replyMessage);

                if(replyProducer!=null)
                    replyProducer.close();
                if (session != null)
                    session.close();
                if (connection != null)
                    connection.close();
            }
            }
        } catch (JMSException ex) {
            Logger.getLogger(ConvocatoriaMessageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
    
}
