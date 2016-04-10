/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.NivelTesis;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface NivelTesisFacadeRemote {

    void create(NivelTesis nivelTesis);

    void edit(NivelTesis nivelTesis);

    void remove(NivelTesis nivelTesis);

    NivelTesis find(Object id);

    List<NivelTesis> findAll();

    List<NivelTesis> findRange(int[] range);

    int count();

}
