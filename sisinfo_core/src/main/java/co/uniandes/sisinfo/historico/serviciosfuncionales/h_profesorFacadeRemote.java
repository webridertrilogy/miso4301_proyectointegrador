/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_profesor;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author david
 */
@Remote
public interface h_profesorFacadeRemote {

    void create(h_profesor h_profesor);

    void edit(h_profesor h_profesor);

    void remove(h_profesor h_profesor);

    h_profesor find(Object id);

    List<h_profesor> findAll();

    List<h_profesor> findRange(int[] range);

    int count();

}
