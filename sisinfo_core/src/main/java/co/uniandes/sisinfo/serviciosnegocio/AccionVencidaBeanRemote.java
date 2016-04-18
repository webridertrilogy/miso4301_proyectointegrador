/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;

import javax.ejb.Remote;

/**
 * @author Juan Manuel Moreno B.
 */
@Remote
public interface AccionVencidaBeanRemote {

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
