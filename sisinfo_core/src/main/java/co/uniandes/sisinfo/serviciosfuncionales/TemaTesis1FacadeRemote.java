/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TemaTesis1;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Remote
public interface TemaTesis1FacadeRemote {

    void create(TemaTesis1 temaTesis1);

    void edit(TemaTesis1 temaTesis1);

    void remove(TemaTesis1 temaTesis1);

    TemaTesis1 find(Object id);

    List<TemaTesis1> findAll();

    List<TemaTesis1> findRange(int[] range);

    int count();

    List<TemaTesis1> findByCorreoAsesor(String correo);

    List<TemaTesis1> findByNombreTema(String temaTesis);

    List<TemaTesis1> findByPeriodoTesis(String semestre);

}
