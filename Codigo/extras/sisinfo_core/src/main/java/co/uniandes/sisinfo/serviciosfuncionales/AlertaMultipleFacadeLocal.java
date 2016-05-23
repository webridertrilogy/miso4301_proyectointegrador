/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.AlertaMultiple;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author lm.morales70
 */
@Local
public interface AlertaMultipleFacadeLocal {

    void create(AlertaMultiple alerta);

    void edit(AlertaMultiple alerta);

    void remove(AlertaMultiple alerta);

    AlertaMultiple find(Object id);

    List<AlertaMultiple> findAll();

    AlertaMultiple findByTipo(String tipo);

}
