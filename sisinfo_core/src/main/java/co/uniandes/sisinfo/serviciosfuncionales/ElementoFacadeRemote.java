package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Elemento;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachade de la entidad Elemento (Interface Remota)
 * @author Marcela Morales
 */
@Remote
public interface ElementoFacadeRemote {

    void create(Elemento elemento);

    void edit(Elemento elemento);

    void remove(Elemento elemento);

    Elemento find(Object id);

    List<Elemento> findAll();

    List<Elemento> findRange(int[] range);

    int count();

}
