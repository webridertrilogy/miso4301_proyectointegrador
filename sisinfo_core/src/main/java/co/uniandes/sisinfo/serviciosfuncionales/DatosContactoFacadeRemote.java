package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DatosContacto;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad DatosContacto (Interface remota)
 * @author Germán Florez, Marcela Morales
 */
@Remote
public interface DatosContactoFacadeRemote {

    void create(DatosContacto datosContacto);

    void edit(DatosContacto datosContacto);

    void remove(DatosContacto datosContacto);

    DatosContacto find(Object id);

    List<DatosContacto> findAll();

    DatosContacto buscarContactoPorNombre(String nombre);
}
