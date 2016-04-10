/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CategoriaCriterioTesis2;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface CategoriaCriterioTesis2FacadeLocal {

    void create(CategoriaCriterioTesis2 categoriaCriterioTesis2);

    void edit(CategoriaCriterioTesis2 categoriaCriterioTesis2);

    void remove(CategoriaCriterioTesis2 categoriaCriterioTesis2);

    CategoriaCriterioTesis2 find(Object id);

    List<CategoriaCriterioTesis2> findAll();

    List<CategoriaCriterioTesis2> findRange(int[] range);

    int count();

}
