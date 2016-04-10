/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author david
 */
@Local
public interface HistoricoLocal {

    String consultarMonitoriasEnHistorico(String comando);

    String pasarMonitoriasAHistoricos(String comando);

    String darArchivosProfesorPorPeriodoEnHistorico(String comando);

    String darInfoArchivoHistorico(String comando);
}
