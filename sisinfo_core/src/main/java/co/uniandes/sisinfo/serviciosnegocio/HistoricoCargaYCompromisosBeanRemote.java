/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Ivan Melo
 */
@Remote
public interface HistoricoCargaYCompromisosBeanRemote {

    String consultarCargaPorCorreoPeriodo(String xml);

    String consultarPeriodos(String xml);

    String pasarPeriodoPlaneacionAHistoricos(String xml);

    String pasarCargasProfesoresAHistoricos(String xml);
}
