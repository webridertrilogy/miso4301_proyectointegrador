package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DisponibilidadCoordinacion;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad DisponibilidadCoordinacion (Interface remota)
 * @author Germán Florez, Marcela Morales
 */
@Remote
public interface DisponibilidadCoordinacionFacadeRemote {

    void create(DisponibilidadCoordinacion disponibilidadCoordinacion);

    void edit(DisponibilidadCoordinacion disponibilidadCoordinacion);

    void remove(DisponibilidadCoordinacion disponibilidadCoordinacion);

    DisponibilidadCoordinacion find(Object id);

    List<DisponibilidadCoordinacion> findAll();

    void removeAll();
}
