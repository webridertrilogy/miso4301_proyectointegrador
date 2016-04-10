/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TipoCampo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface TipoCampoFacadeLocal {

    void create(TipoCampo tipoCampo);

    void edit(TipoCampo tipoCampo);

    void remove(TipoCampo tipoCampo);

    TipoCampo find(Object id);

    List<TipoCampo> findAll();

    List<TipoCampo> findRange(int[] range);

    int count();

}
