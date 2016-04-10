/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import javax.ejb.Remote;

/**
 *
 * @author Manuel
 */
@Remote
public interface h_reportesFacadeRemote {

    void setRutaReportes(String ruta);

    String hacerReporteMonitoriasHistoricosPorCorreo(String correoEstudiante);
}
