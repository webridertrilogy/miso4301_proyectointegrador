/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_monitoria;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author david
 */
@Remote
public interface h_monitoriaFacadeRemote {

    void create(h_monitoria h_monitoria);

    void edit(h_monitoria h_monitoria);

    void remove(h_monitoria h_monitoria);

    h_monitoria find(Object id);

    List<h_monitoria> findAll();

    List<h_monitoria> findRange(int[] range);

    int count();

}
