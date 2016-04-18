/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;

import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface ListaNegraReservaCitasLocal {

    String crearListaNegraReservaCitas(String comando);

    String editarListaNegraReservaCitas(String comando);

    String eliminarListaNegraReservaCitas(String comando);

    String consultarListaNegraReservaCitas(String comando);

    boolean consultarEstudianteEnListaNegra(String correo);

    void agregarEstudianteAListaNegra(String correo,Timestamp fechaReserva);
    
}
