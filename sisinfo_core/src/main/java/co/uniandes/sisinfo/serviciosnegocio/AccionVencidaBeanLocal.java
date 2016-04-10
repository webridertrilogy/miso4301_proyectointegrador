/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.entities.AccionVencida;
import java.sql.Timestamp;
import javax.ejb.Local;

/**
 * @author Juan Manuel Moreno B.
 */
@Local
public interface AccionVencidaBeanLocal {

    /**
     * Guarda una accion vencida
     * @param xml
     * @return
     */
    void guardarAccionVencida(String xml);

    /**
     * Guarda una accion vencida
     * @return
     */
    void guardarAccionVencida(
            Timestamp fechaAcordada, Timestamp fechaEjecucion,
            String accion, String usuario,
            String proceso, String modulo,
            String comando, String infoAdicional);

    /**
     * Da lista de acciones vencidas
     * @param xml
     * @return
     */
    String darListaAccionVencida(String xml);
}
