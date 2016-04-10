/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface DiaCompletoFacadeRemote {

    void create(DiaCompleto diaCompleto);

    void edit(DiaCompleto diaCompleto);

    void remove(DiaCompleto diaCompleto);

    DiaCompleto find(Object id);

    List<DiaCompleto> findAll();

    List<DiaCompleto> findRange(int[] range);

    int count();

}
