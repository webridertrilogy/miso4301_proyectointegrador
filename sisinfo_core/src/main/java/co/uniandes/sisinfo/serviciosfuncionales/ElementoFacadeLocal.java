package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Elemento;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachade de la entidad Elemento (Interface Local)
 * @author Marcela Morales
 */
@Local
public interface ElementoFacadeLocal {

    void create(Elemento elemento);

    void edit(Elemento elemento);

    void remove(Elemento elemento);

    Elemento find(Object id);

    List<Elemento> findAll();

    List<Elemento> findRange(int[] range);

    int count();

}
