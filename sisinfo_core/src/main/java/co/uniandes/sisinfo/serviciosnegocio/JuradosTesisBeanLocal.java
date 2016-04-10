/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.entities.Tesis2;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface JuradosTesisBeanLocal {

    String consultarJuradosExternosPorCorreo(String xml);

    String actualizarJuradoExterno(String xml);

    String darEvaluacionJuradoPorHash(String xml);

    String guardarEvaluacionJuradoPorHash(String xml);

    void calcularNotaSustencion(Tesis2 t);
    
    
}
