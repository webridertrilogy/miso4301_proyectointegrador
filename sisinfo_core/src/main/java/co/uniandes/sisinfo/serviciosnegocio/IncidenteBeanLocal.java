/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface IncidenteBeanLocal {

    String reportarIncidente(String xml);

    String consultarIncidentePorId(String xml);

    String consultarIncidentePorCorreoReportado(String xml);

    String consultarIncidentesPorEstado(String xml);

    String modificarEstadoIncidente(String xml);

    String EliminarIncidente(String xml);

    String darIncidentesSoporte(String String);

    String consultarModulosPublicosSisinfo(String xml);

    /// TODO: Crear firma metodo asignarIncidente y probarlo nuevamente.
    /**
     * Da incidencias dada una persona de soporte.
     * @param xml
     * @return
     */
    String darIncidentesXPersonaSoporte(String xml);
}
