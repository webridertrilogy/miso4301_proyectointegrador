/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeLocal;

/**
 *
 * @author Asistente
 */
@Stateless
@EJB(name = "IntegracionConflictoHorariosBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.IntegracionConflictoHorariosBeanLocal.class)
public class IntegracionConflictoHorariosBean implements  IntegracionConflictoHorariosBeanLocal {

    @EJB
    private RangoFechasBeanLocal rangoFechasBean;

    @EJB
    private SeccionFacadeLocal seccionFacade;

    @EJB
    private SolicitudFacadeLocal solicitudFacade;

    @EJB
    private PreseleccionLocal preseleccionBean;

    @EJB
    private ConstanteLocal constanteBean;

    @EJB
    private TareaMultipleFacadeLocal tareaMultipleFacade;

    @EJB
    private MonitoriaFacadeLocal monitoriaFacade;

    @EJB
    private ProfesorFacadeLocal profesorFacade;

    @EJB
    private ConvocatoriaLocal convocatoriaBean;

    @EJB
    private CursoFacadeLocal cursoFacade;

    private ServiceLocator serviceLocator;

    public IntegracionConflictoHorariosBean(){
//        try{
//            serviceLocator = new ServiceLocator();
//
//            seccionFacade = (SeccionFacadeLocal)serviceLocator.getLocalEJB(SeccionFacadeLocal.class);
//            constanteBean = (ConstanteLocal)serviceLocator.getLocalEJB(ConstanteLocal.class);
//            tareaMultipleFacade =  (TareaMultipleFacadeLocal)serviceLocator.getLocalEJB(TareaMultipleFacadeLocal.class);
//            profesorFacade = (ProfesorFacadeLocal)serviceLocator.getLocalEJB(ProfesorFacadeLocal.class);
//            cursoFacade = (CursoFacadeLocal)serviceLocator.getLocalEJB(CursoFacadeLocal.class);
//        }catch(Exception ex){
//            Logger.getLogger(IntegracionConflictoHorariosBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }

    @Override
    public void crearSeccion(String crn){
        Seccion s = seccionFacade.findByCRN(crn);
        rangoFechasBean.crearTareaProfesorPreseleccionarPorSeccion(s);
    }

    @Override
    public void eliminarSeccion(String crn){
        // Revierte las solicitudes asociadas a la seccion
        Seccion seccion = seccionFacade.findByCRN(crn);
        Collection<Solicitud> solicitudes = solicitudFacade.findByCrnSeccion(crn);
        for (Solicitud solicitud : solicitudes) {
            preseleccionBean.revertirPreseleccion(""+solicitud.getId(), constanteBean.getConstante(Constantes.ROL_COORDINACION),Notificaciones.MENSAJE_REVERSION_SOLICITUD_MOTIVO_SECCION_ELIMINADA,Notificaciones.MENSAJE_REVERSION_SOLICITUD_ASPIRANTE);
        }
        
        rangoFechasBean.realizarTareaSeccion(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO_PRESELECCION_MONITOR), crn);


    }

    @Override
    public void cambiarHorarioSeccion(String crn){
        // Revierte las solicitudes asociadas a la seccion
        Seccion seccion = seccionFacade.findByCRN(crn);
        Collection<Solicitud> solicitudes = solicitudFacade.findByCrnSeccion(crn);
        for (Solicitud solicitud : solicitudes) {
            preseleccionBean.revertirPreseleccion(""+solicitud.getId(), constanteBean.getConstante(Constantes.ROL_COORDINACION),Notificaciones.MENSAJE_REVERSION_SOLICITUD_MOTIVO_CAMBIO_HORARIO,Notificaciones.MENSAJE_REVERSION_SOLICITUD_ASPIRANTE);
        }

        // Regenera las tareas para el profesor
        rangoFechasBean.crearTareaProfesorPreseleccionarPorSeccion(seccion);
    }

    @Override
    public void cambiarNumeroMonitores(String crn){
        // Revierte las solicitudes asociadas a la seccion
        Seccion seccion = seccionFacade.findByCRN(crn);

        rangoFechasBean.crearTareaProfesorPreseleccionarPorSeccion(seccion);
    }

    @Override
    public void cambiarProfesor(String crn, String correoProfesor){
        Profesor profesor=profesorFacade.findByCorreo(correoProfesor);

        Seccion seccion = seccionFacade.findByCRN(crn);
         // Completa las tareas asociadas a el profesor y la seccion especifica

        rangoFechasBean.realizarTareaSeccion(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO_PRESELECCION_MONITOR), crn);
        rangoFechasBean.crearTareaProfesorPreseleccionarPorSeccion(seccion);
    }

    @Override
    public boolean validarCambioCaracteristicasMonitoria(String codigo){
        Collection<Solicitud> solicitudes = solicitudFacade.findByCurso(codigo);
        // Si existe al menos una monitoria preseleccionada, el cambio no puede realizarse
        for (Solicitud solicitud : solicitudes) {
            if(!solicitud.getEstadoSolicitud().equals(constanteBean.getConstante(Constantes.ESTADO_ASPIRANTE)))
                return false;
        }
        return true;
    }

    @Override
    public boolean validarCambioNumeroMonitores(String crn, double nuevoNumeroMonitores){

        // Revierte las solicitudes asociadas a la seccion
        Seccion seccion = seccionFacade.findByCRN(crn);
        if(seccion.getMaximoMonitores()< nuevoNumeroMonitores){
            return true;
        }
        Collection<MonitoriaAceptada> monitorias = monitoriaFacade.findByCRNSeccion(crn);
        double numMonitores = 0;
        for (MonitoriaAceptada monitoriaAceptada : monitorias) {
            double carga = monitoriaAceptada.getCarga();
            if(carga == Double.parseDouble(constanteBean.getConstante(Constantes.VAL_CARGA_MONITORIA_T1))){
                numMonitores++;
            }else{
                numMonitores+=0.5;
            }
        }
        return nuevoNumeroMonitores>=numMonitores;
    }

    @Override
    public boolean existeConvocatoriaAbierta(){
        return convocatoriaBean.hayConvocatoriaAbierta();
    }

 
}
