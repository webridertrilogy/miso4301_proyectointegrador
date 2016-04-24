/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteLocal;

/**
 * Servicios Timer Profesor
 */
@Stateless
@EJB(name = "TimerProfesorBean", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.TimerProfesorLocal.class)
public class TimerProfesorBean implements  TimerProfesorLocal {

    @Resource
    private SessionContext ctx;

    private ParserT parser;
    @EJB
    private SolicitudFacadeLocal solicitudFacade;

//    @EJB
//    private NucleoLocal nucleo;

    @EJB
    private ConstanteLocal constanteBean;
    private ServiceLocator serviceLocator;

    public TimerProfesorBean() {
//    try {
//            parser = new ParserT();
//            serviceLocator = new ServiceLocator();
//            //nucleo = (NucleoLocal) serviceLocator.getLocalEJB(NucleoLocal.class);
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(TimerProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public Date crearTimer(String idSolicitud, String crn1, String crn2) {
        TimerService timerService = ctx.getTimerService();
        String mensaje = idSolicitud + ":" + crn1;
        if (crn2 != null && !crn2.trim().equals("")) {
            mensaje += ":" + crn2;
        }
        Timer t = timerService.createTimer(new Date(Long.valueOf(getConstanteBean().getConstante(Constantes.TIMER_TIMEOUT_CONFIRMACION_PROFESOR)).longValue() + System.currentTimeMillis()), mensaje);
        return t.getNextTimeout();
    }

    @Timeout
    private void confirmarProfesor(Timer t) {
        try {
            System.out.println("empece timer");
            String[] info = t.getInfo().toString().split(":");
            String idSolicitud = info[0];
            String crn = info[1];
            Solicitud solicitud = solicitudFacade.findById(Long.parseLong(idSolicitud));
            Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
            //Se revisa si la monitoria de la solicitud corresponde a la seccion del profesor
            boolean valida = false;
            for (MonitoriaAceptada monitoria : monitorias) {
                Seccion seccion = monitoria.getSeccion();
                valida = valida || seccion.getCrn().equals(crn);
            }
            // En caso de que la monitoria sea T2 busca la segunda seccion
            if (valida && info.length > 2) {
                crn = info[2];
                valida = false;
                for (MonitoriaAceptada monitoria : monitorias) {
                    Seccion seccion = monitoria.getSeccion();
                    valida = valida || seccion.getCrn().equals(crn);
                }
            }
            //Se revisa que la monitoria no haya sido confirmada anteriormente
            boolean vigente = solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_CONFIRMACION_PROFESOR));
            ArrayList secuencias = new ArrayList();
            Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), idSolicitud);
            secuencias.add(secuencia);
            secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONFIRMACION),"true");
            secuencias.add(secuencia);
            String xml;
            //Si la monitoria aun es valida envia el mensaje de confirmacion
            if (valida && vigente) {
                xml = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_PROCESO), getConstanteBean().getConstante(Constantes.ROL_COORDINACION), secuencias);
                System.out.println("(TimerProfesorBean) xml" + xml);
               // nucleo.resolverComando(xml);
            }
            System.out.println("(TimerProfesorBean) acabe timer " + valida + "-" + vigente);
        } catch (Exception e) {
            Logger.getLogger(TimerProfesorBean.class.getName()).log(Level.SEVERE, "Error realizando la confirmacion", e);
        }
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }
}
