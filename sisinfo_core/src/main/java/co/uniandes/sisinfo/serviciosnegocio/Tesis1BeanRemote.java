/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Remote
public interface Tesis1BeanRemote {

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

    void migrarTesisRechazada(Long tesisId);

    void migrarTesisRetiradas();

    void migrarTesisPerdidas();

    //void migrarTesisTerminadas();
    String consultaExternaTesis1(String xml);

    boolean migrarTesisRetirada(Long tesisId);

    boolean migrarTesisPerdida(Long tesisId);

    public String darTesis1AMigrar(String xml);

    public String comportamientoEmergenciaMigrarTesis1(String xml);

    public String confirmarTesis1EnBanner(String xml);

    public String establecerAprobacionParadigma(String xml);
}
