/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

import co.uniandes.sisinfo.entities.Tesis1;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface Tesis1BeanLocal {

    String agregarSolicitudTesis1(String xml);

    String aprobarORechazarAsesoriaTesis(String xml);

    String darEstadoSolicitudTesis1(String xml);

    String darSolicitudesTesis1ParaProfesor(String xml);

    String darSolicitudTesisParaEstudiante(String xml);

    String agregarSolicitudTesis1Director(String xml);

    String cambiarEstadoTesis1(String xml);

    String subirNotaTesis1(String xml);

    String darSolicitudesAceptadasTesis1(String xml);

    String darTesisPorAprobarCoordinadorMaestria(String sml);

    String aprobarTesis1CoordinacionMaestria(String xml);

    String aprobarRechazarPendienteTesis1(String xml);

    String retirarTesis1(String xml);

    String agregarComentario30PorcientoTesis1(String xml);

    String darComentariosPorTesis1(String xml);

    void crearTareaCalificarTesis1(String string);

    void crearTareaCalificarTesis1Pendiente(String string);

    void migrarTesisRechazada(Long tesisId);

    void migrarTesisRetiradas();

    void migrarTesisPerdidas();

    //void migrarTesisTerminadas();
    String consultaExternaTesis1(String xml);

    boolean migrarTesisRetirada(Long tesisId);

    boolean migrarTesisPerdida(Long tesisId);

    String darTesis1AMigrar(String xml);

    String comportamientoEmergenciaMigrarTesis1(String xml);

    void crearTareaAprobarTesisCoordinadorMaestria(Tesis1 tesis1);

    void crearTareaAsesorAprobacionTesis1(Tesis1 tesis1);

    void crearTareaCoordinadorAprobarPendienteTesis1(Tesis1 tesis1);

    void creatTareaTesis1CalificarATiempo(Tesis1 tesis1);

    void creatTareaCalificarTesis1Pendiente(Tesis1 tesis1);

    public String establecerAprobacionParadigma(String xml);
}
