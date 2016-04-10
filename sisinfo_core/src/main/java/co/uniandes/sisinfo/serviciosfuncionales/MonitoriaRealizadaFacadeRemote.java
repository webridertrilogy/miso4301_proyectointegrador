/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.MonitoriaRealizada;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author david
 */
@Remote
public interface MonitoriaRealizadaFacadeRemote {

    void create(MonitoriaRealizada monitoriaRealizada);

    void edit(MonitoriaRealizada monitoriaRealizada);

    void remove(MonitoriaRealizada monitoriaRealizada);

    MonitoriaRealizada find(Object id);

    List<MonitoriaRealizada> findAll();

    List<MonitoriaRealizada> findRange(int[] range);

    int count();

}
