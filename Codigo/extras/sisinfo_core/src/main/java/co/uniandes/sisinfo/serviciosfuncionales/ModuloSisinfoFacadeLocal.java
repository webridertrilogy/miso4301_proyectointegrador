/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ModuloSisinfo;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface ModuloSisinfoFacadeLocal {

    void create(ModuloSisinfo moduloSisinfo);

    void edit(ModuloSisinfo moduloSisinfo);

    void remove(ModuloSisinfo moduloSisinfo);

    ModuloSisinfo find(Object id);

    List<ModuloSisinfo> findAll();

    List<ModuloSisinfo> findRange(int[] range);

    int count();

    Collection<ModuloSisinfo> buscarModulosPublicos();

}
