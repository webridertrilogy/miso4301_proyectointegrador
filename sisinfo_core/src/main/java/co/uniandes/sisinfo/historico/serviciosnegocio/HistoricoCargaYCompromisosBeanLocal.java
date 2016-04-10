/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface HistoricoCargaYCompromisosBeanLocal {

    String consultarCargaPorCorreoPeriodo(String xml);

    String consultarPeriodos(String xml);

    String pasarPeriodoPlaneacionAHistoricos(String xml);

    String pasarCargasProfesoresAHistoricos(String xml);
}
