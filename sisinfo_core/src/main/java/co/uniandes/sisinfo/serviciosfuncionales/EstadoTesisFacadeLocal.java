/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.EstadoTesis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface EstadoTesisFacadeLocal {

    void create(EstadoTesis estadoTesis);

    void edit(EstadoTesis estadoTesis);

    void remove(EstadoTesis estadoTesis);

    EstadoTesis find(Object id);

    List<EstadoTesis> findAll();

    List<EstadoTesis> findRange(int[] range);

    int count();

}
