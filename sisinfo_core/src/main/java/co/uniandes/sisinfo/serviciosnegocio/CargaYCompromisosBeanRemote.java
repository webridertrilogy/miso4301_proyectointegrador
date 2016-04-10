/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface CargaYCompromisosBeanRemote {

    String inicarProcesoCarga(String xml);

    void manejarTimerCYC(String parametro);

    String consultarCargaProfesor(String xml);

    String agregarCurso(String xml);

    String agregarIntencionPublicacion(String xml);

    String agregarAsesoriaTesis(String xml);

    String agregarProyectoFinanciado(String xml);

    String agregarOtraActividad(String xml);

    String agregarDescarga(String xml);

    String terminarCargaProfesor(String xml);

    String eliminarCursoCargaYCompromisos(String xml);

    String eliminarPublicacion(String xml);

    String eliminarAsesoriaTesisCYC(String xml);

    String eliminarProyectosFinanciados(String xml);

    String eliminarOtrasActividades(String xml);

    String editarCurso(String xml);

    String editarPublicacion(String xml);

    String editarDireccionTesis(String xml);

    String editarProyectoFinanciado(String xml);

    String editarOtraActividad(String xml);

    String editarDescargaProfesor(String xml);

    String darTiposDePublicacion(String xml);

    String darNivelesTesis(String xml);

    String darTiposDeDescargaProfesor(String xml);

    String consultarCargaProfesorPorCorreoYNombrePeriodo(String xml);

    String darPeriodosPlaneacion(String xml);

    String darCargaUltimoPeriodoProfesor(String xml);

    String eliminarProfesorBaseDatos(String xml);

    String darProyectosFinanciados(String xml);

    String vincularProfesorAProyectosFinanciados(String xml);

    void actualizarIndicadoresCarga();

    String crearCargaVaciaAProfesor(String xml);

    String migrarCargasPorFinPeriodo(String xml);
    
}
