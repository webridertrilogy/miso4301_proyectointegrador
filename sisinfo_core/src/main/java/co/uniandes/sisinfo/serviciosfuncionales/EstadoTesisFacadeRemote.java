/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.EstadoTesis;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface EstadoTesisFacadeRemote {

    void create(EstadoTesis estadoTesis);

    void edit(EstadoTesis estadoTesis);

    void remove(EstadoTesis estadoTesis);

    EstadoTesis find(Object id);

    List<EstadoTesis> findAll();

    List<EstadoTesis> findRange(int[] range);

    int count();

}
