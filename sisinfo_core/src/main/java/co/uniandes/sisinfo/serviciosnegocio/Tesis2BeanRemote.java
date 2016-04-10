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
public interface Tesis2BeanRemote {

    String crearSolicitudTesis2Estudiante(String xml);

    String crearSolicitudIngresoTesis2PermisosCoordinador(String xml);

    String aprobarSolicitudTesis2Asesor(String xml);

    String darSolicitudesTesis2Asesor(String xml);

    String colocarNotaTesis2(String xml);

    String modificarJuradosTesis2(String xml);

    String cambiarEstadoTesis2PendienteEspecial(String xml);

    String darDetallesTesis2(String String);

    @Deprecated
    String darSemestresCercanos(String xml);

    String guardarHorarioSustentacionTesis2(String String);

    String subirArticuloFinalizacionTesis2(String xml);

    String darTesisConHorarioSustentacion(String xml);

    String darTodasLasSolicitudesTesis2(String xml);

    String retirarTesis2(String xml);

    String agregarComentarioTesis2(String xml);

    String darComentariosPorTesis2(String xml);

    String aprobarHorarioSustentacionPorAsesor(String xml);

    String guardarSalonSustentacion(String xml);

    String modificarCoasesor(String xml);

    String pedirPendienteEspecialParaTesis2(String xml);

    void migrarTesisRechazada(Long tesisId);

    void migrarTesisRetiradas();

    void migrarTesisPerdidas();

    void migrarTesisTerminadas();

    String consultarTesisProfesorParaExternos(String xml);

    String comportamientoEmergenciaJuradosTesis(String xml);

    String reprobarTesis2(String xml);

    boolean migrarTesisRetirada(Long tesisId);

    boolean migrarTesisPerdida(Long tesisId);

    boolean migrarTesisTerminada(Long tesisId);

    public String darTesis2AMigrar(String xml);

    public String comportamientoEmergenciaMigrarTesis2(String xml);

    String darUbicacionHoraTesis2(String xml);

    String actualizarDetallesTesis2(String xml);

    String darDetallesUbicacionHoraTesis2(String xml);

    public void avisarAEstudianteProfesorYCoordinacionVencimientoTiempoSeleccionHorario(String periodo);

    public String confirmarTesis2EnBanner(String xml);

    public void recordatorioDirectorNotasFaltantes(String id, String vecesEjecutado);

     public String establecerAprobacionParadigma(String xml);
}
