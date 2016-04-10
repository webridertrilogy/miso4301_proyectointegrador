/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_monitor;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author david
 */
@Remote
public interface h_monitorFacadeRemote {

    void create(h_monitor h_monitor);

    void edit(h_monitor h_monitor);

    void remove(h_monitor h_monitor);

    h_monitor find(Object id);

    List<h_monitor> findAll();

    List<h_monitor> findRange(int[] range);

    int count();

    h_monitor findByCorreo(String correo);

}
