/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CategoriaCriterioJurado;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Remote
public interface CategoriaCriterioJuradoFacadeRemote {

    void create(CategoriaCriterioJurado categoriaCriterioJurado);

    void edit(CategoriaCriterioJurado categoriaCriterioJurado);

    void remove(CategoriaCriterioJurado categoriaCriterioJurado);

    CategoriaCriterioJurado find(Object id);

    List<CategoriaCriterioJurado> findAll();

    List<CategoriaCriterioJurado> findRange(int[] range);

    int count();

}
