/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CategoriaEventoExterno;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface CategoriaEventoExternoFacadeLocal {

    void create(CategoriaEventoExterno categoriaEventoExterno);

    void edit(CategoriaEventoExterno categoriaEventoExterno);

    void remove(CategoriaEventoExterno categoriaEventoExterno);

    CategoriaEventoExterno find(Object id);

    List<CategoriaEventoExterno> findAll();

    List<CategoriaEventoExterno> findRange(int[] range);

    public CategoriaEventoExterno findByNombre(String nombre);

    int count();

}
