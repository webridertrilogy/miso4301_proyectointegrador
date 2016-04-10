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
public interface JuradosTesisBeanRemote {

    String consultarJuradosExternosPorCorreo(String xml);

    String actualizarJuradoExterno(String xml);

    String darEvaluacionJuradoPorHash(String xml);

    String guardarEvaluacionJuradoPorHash(String xml);
    
}
