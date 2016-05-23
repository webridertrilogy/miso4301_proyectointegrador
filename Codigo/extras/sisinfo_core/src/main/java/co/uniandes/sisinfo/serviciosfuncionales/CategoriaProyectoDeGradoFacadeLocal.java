/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CategoriaProyectoDeGrado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Local
public interface CategoriaProyectoDeGradoFacadeLocal {

    void create(CategoriaProyectoDeGrado categoriaProyectoDeGrado);

    void edit(CategoriaProyectoDeGrado categoriaProyectoDeGrado);

    void remove(CategoriaProyectoDeGrado categoriaProyectoDeGrado);

    CategoriaProyectoDeGrado find(Object id);

    List<CategoriaProyectoDeGrado> findAll();

    List<CategoriaProyectoDeGrado> findRange(int[] range);

    int count();

}
