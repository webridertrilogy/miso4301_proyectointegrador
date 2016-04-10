package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Categoria;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachade de la entidad Categoria (Interface Remota)
 * @author Marcela Morales
 */
@Remote
public interface CategoriaFacadeRemote {

    void create(Categoria categoria);

    void edit(Categoria categoria);

    void remove(Categoria categoria);

    Categoria find(Object id);

    List<Categoria> findAll();

    List<Categoria> findRange(int[] range);

    int count();

}
