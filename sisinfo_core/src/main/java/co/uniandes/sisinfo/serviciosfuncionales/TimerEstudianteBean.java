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
import co.uniandes.sisinfo.nucleo.services.NucleoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteLocal;

/**
 * Servicios Timer Estudiante
 */
@Stateless
@EJB(name = "TimerEstudianteBean", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.TimerEstudianteLocal.class)
public class TimerEstudianteBean implements  TimerEstudianteLocal {

    @Resource
    private SessionContext ctx;

    private ParserT parser;

    @EJB
    private SolicitudFacadeLocal solicitudFacade;

    @EJB
    private NucleoLocal nucleo;

    @EJB
    private ConstanteLocal constanteBean;
    private ServiceLocator serviceLocator;

    public TimerEstudianteBean() {
//            try {
//            parser = new ParserT();
//            serviceLocator = new ServiceLocator();
//           // nucleo = (NucleoLocal) serviceLocator.getLocalEJB(NucleoLocal.class);
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
        Timer t = timerService.createTimer(new Date(Long.valueOf(getConstanteBean().getConstante(Constantes.TIMER_TIMEOUT_CONFIRMACION_ESTUDIANTE)).longValue() + System.currentTimeMillis()), mensaje);
        return t.getNextTimeout();
    }

    @Timeout
    private void confirmarEstudiante(Timer t) {
        try {
            //TODO tratar el caso cuando el estudiante esta confirmando dos secciones
            String[] info = t.getInfo().toString().split(":");
            String idSolicitud = info[0];
            String crn = info[1];
            Solicitud solicitud = solicitudFacade.findById(Long.parseLong(idSolicitud));
            Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
            //Se revisa si la monitoria de la solicitud corresponde a la seccion del estudiante
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
            boolean vigente = solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE));
            ArrayList secuencias = new ArrayList();
            Secuencia secuencia = new Secuencia("idSolicitud", idSolicitud);
            secuencias.add(secuencia);
            String xml;
            //Si la monitoria aun es valida envia el mensaje de cancelacion
            if (valida && vigente) {
                xml = parser.crearComando(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PRESELECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_PROCESO), getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE), secuencias);
                System.out.println("(TimerEstudianteBean) xml-" + xml);
                nucleo.resolverComando(xml);
            }
            System.out.println("(TimerEstudianteBean) Acabe timer estudiante:" + valida + "-" + vigente);
        } catch (Exception e) {
            Logger.getLogger(TimerEstudianteBean.class.getName()).log(Level.SEVERE, "Error realizando la confirmacion", e);
        }
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }
}
