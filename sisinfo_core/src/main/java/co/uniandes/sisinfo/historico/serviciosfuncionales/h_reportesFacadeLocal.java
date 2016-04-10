/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.serviciosfuncionales;

import javax.ejb.Local;

/**
 *
 * @author Manuel
 */
@Local
public interface h_reportesFacadeLocal {

    void setRutaReportes(String ruta);

    String hacerReporteMonitoriasHistoricosPorCorreo(String correoEstudiante);
}
