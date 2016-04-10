/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.OtrasActividades;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface OtrasActividadesFacadeLocal {

    void create(OtrasActividades otrasActividades);

    void edit(OtrasActividades otrasActividades);

    void remove(OtrasActividades otrasActividades);

    OtrasActividades find(Object id);

    List<OtrasActividades> findAll();

    List<OtrasActividades> findRange(int[] range);

    int count();

}
