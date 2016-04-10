package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Encargado;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachade de la entidad Encargado (Interface Remota)
 * @author Marcela Morales
 */
@Remote
public interface EncargadoFacadeRemote {

    void create(Encargado encargado);

    void edit(Encargado encargado);

    void remove(Encargado encargado);

    Encargado find(Object id);

    List<Encargado> findAll();

    List<Encargado> findRange(int[] range);

    int count();

    Encargado findByCorreo(String correo);

}
