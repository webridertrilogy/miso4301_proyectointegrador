/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CampoAdicional;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface CampoAdicionalFacadeLocal {

    void create(CampoAdicional campoAdicional);

    void edit(CampoAdicional campoAdicional);

    void remove(CampoAdicional campoAdicional);

    CampoAdicional find(Object id);

    List<CampoAdicional> findAll();

    List<CampoAdicional> findRange(int[] range);

    int count();

}
