/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.AlertaMultiple;
import co.uniandes.sisinfo.entities.TimerGenerico;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.serviciosfuncionales.AlertaMultipleFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TimerGenericoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeRemote;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Asistente
 */
@Stateless
//@EJB(name = "AdministradorSisinfoBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.AdministradorSisinfoBeanLocal.class)
public class AdministradorSisinfoBean implements AdministradorSisinfoBeanRemote, AdministradorSisinfoBeanLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    //@EJB
    //private AlertaMultipleRemote alertaMultipleBean;
    @EJB
    private TimerGenericoBeanRemote timerGenericoBean;
    @EJB
    private UsuarioFacadeRemote usuarioFacade;
    @EJB
    private AlertaMultipleFacadeRemote alertaMultipleFacade;
    @EJB
    private CorreoRemote correoBean;
    @Resource
    private SessionContext ctx;
    @PersistenceContext
    private EntityManager em;
    @EJB
    private TimerGenericoFacadeRemote timerGenericoFacade;
    @EJB
    private ConstanteRemote constanteBean;

    private ServiceLocator serviceLocator;
    public String tablaAlertas = "";
    public String tablaTimers = "";

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de AdministradorSisinfoBean
     */
    public AdministradorSisinfoBean() throws NamingException {
        serviceLocator = new ServiceLocator();
        //alertaMultipleBean =  (AlertaMultipleRemote) serviceLocator.getRemoteEJB(AlertaMultipleRemote.class);
        timerGenericoBean = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
        timerGenericoFacade = (TimerGenericoFacadeRemote) serviceLocator.getRemoteEJB(TimerGenericoFacadeRemote.class);
        alertaMultipleFacade = (AlertaMultipleFacadeRemote) serviceLocator.getRemoteEJB(AlertaMultipleFacadeRemote.class);
        usuarioFacade = (UsuarioFacadeRemote) serviceLocator.getRemoteEJB(UsuarioFacadeRemote.class);
        correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
        constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);

    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    /**
     * Envia el correo al administrador de Sisinfo con el estaod del sistema y renueva el timer
     * @param x
     */
    public void enviarCorreoDiagnosticoSisinfo(String x) {

        alertasSinTimerAsociado();
        timersEnBDyMemoria();
        String asunto = Notificaciones.ASUNTO_DIAGNOSTICO_SISINFO;
        String mensaje = Notificaciones.MENSAJE_DIAGNOSTICO_SISINFO;
        mensaje = mensaje.replaceFirst("%1", tablaAlertas);
        mensaje = mensaje.replaceFirst("%2", tablaTimers);

        //Se envia el correo

        List<Usuario> admin = usuarioFacade.findByRol(getConstanteBean().getConstante(Constantes.ROL_ADMINISTRADOR_SISINFO));
        for (Usuario usuario : admin) {
            correoBean.enviarMail(usuario.getPersona().getCorreo(), asunto, null, null, null, mensaje);

        }

        //renueva el timer para dentro de dos dias
        timerGenericoBean.crearTimerDiagnosticoSisinfo();

    }

    /**
     * Crea una tabla html con 
     * @return
     */
    private void alertasSinTimerAsociado() {
        // Revisa que las alertas tengan un timer asociado en la BD y en memoria
        List<AlertaMultiple> alertas = alertaMultipleFacade.findAll();
        boolean alertasConProblemas = false; // verifica que exista al menos una alerta sin timer para enviar el correo, o de lo contrario no se envia
        tablaAlertas = "<table><table border='1'><tr><th>Tipo</th>"
                + "<th>Nombre Alerta </th>"
                + "<th>Timer en BD</th>"
                + "<th>Timer en Memoria</th>"
                + "</tr>";

        for (AlertaMultiple alerta : alertas) {
            boolean existeBD = timerGenericoBean.timerExisteEnBD(alerta.getIdTimer());
            boolean existeMemoria = timerGenericoBean.timerExisteEnMemoria(alerta.getIdTimer());
            if (!existeBD || !existeMemoria) {
                alertasConProblemas = true;
                tablaAlertas += "<tr><td>" + alerta.getTipo() + "</td><td>" + alerta.getNombre() + "</td><td>" + existeBD + "</td><td>" + existeMemoria + "</td></tr>";
            }
        }
        tablaAlertas += "</table>";

        if (!alertasConProblemas) {
            tablaAlertas = "Todas las alertas se encuentran con un timer asociado";
        }

    }

    private void timersEnBDyMemoria() {
        int timersEnMemoriaFaltantesEnBD = timerGenericoBean.consultarTimersEnMemoriaFaltantesEnBD();
        int timersEnBDFaltantesEnMemoria = timerGenericoBean.consultarTimersEnBDFaltantesEnMemoria();
        tablaTimers = "<table><table border='1'>"
                + "<tr>"
                + "<th>Timer</th>"
                + "<th>Cantidad</th>"
                + "</tr>"
                + "<tr>"
                + "<td>Timer en memoria, faltantes en la BD</td>"
                + "<td>"+timersEnMemoriaFaltantesEnBD+"</td>"
                + "</tr>"
                + "<tr>"
                + "<td>Timer en la BD, faltantes en memoria</td>"
                + "<td>"+timersEnBDFaltantesEnMemoria+"</td>"
                + "</tr>"
                + "</table>";
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    

    
}
