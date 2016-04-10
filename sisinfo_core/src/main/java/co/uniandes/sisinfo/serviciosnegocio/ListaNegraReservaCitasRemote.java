/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface ListaNegraReservaCitasRemote {

    String crearListaNegraReservaCitas(String comando);

    String editarListaNegraReservaCitas(String comando);

    String eliminarListaNegraReservaCitas(String comando);

    String consultarListaNegraReservaCitas(String comando);

    void manejoTimersListaNegraReservaCitas(String info);
    
}
