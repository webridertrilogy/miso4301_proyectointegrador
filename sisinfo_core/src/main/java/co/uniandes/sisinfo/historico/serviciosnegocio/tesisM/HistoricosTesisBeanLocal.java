/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosnegocio.tesisM;

import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface HistoricosTesisBeanLocal {

    String darHistoricoEstudiantesTesis(String xml);

    String darHistoricoEstudianteTesis(String xml);

    String darHistoricosEstudiantesTesisMaestriaProfesor(String xml);
}
