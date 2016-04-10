package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Categoria;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachade de la entidad Categoria (Interface Local)
 * @author Marcela Morales
 */
@Local
public interface CategoriaFacadeLocal {

    void create(Categoria categoria);

    void edit(Categoria categoria);

    void remove(Categoria categoria);

    Categoria find(Object id);

    List<Categoria> findAll();

    List<Categoria> findRange(int[] range);

    int count();

}
