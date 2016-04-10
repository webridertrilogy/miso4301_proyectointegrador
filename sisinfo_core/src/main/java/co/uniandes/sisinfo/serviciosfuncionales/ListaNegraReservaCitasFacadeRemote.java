/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ListaNegraReservaCitas;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface ListaNegraReservaCitasFacadeRemote {

    void create(ListaNegraReservaCitas listaNegraReservaCitas);

    void edit(ListaNegraReservaCitas listaNegraReservaCitas);

    void remove(ListaNegraReservaCitas listaNegraReservaCitas);

    ListaNegraReservaCitas find(Object id);

    List<ListaNegraReservaCitas> findAll();

    List<ListaNegraReservaCitas> findRange(int[] range);

    int count();

    ListaNegraReservaCitas findByCorreo(String correo);

}
