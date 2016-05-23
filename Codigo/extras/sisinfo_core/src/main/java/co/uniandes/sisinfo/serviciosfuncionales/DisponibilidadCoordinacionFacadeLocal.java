package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DisponibilidadCoordinacion;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada de la entidad DisponibilidadCoordinacion (Interface local)
 * @author Germán Florez, Marcela Morales
 */
@Local
public interface DisponibilidadCoordinacionFacadeLocal {

    void create(DisponibilidadCoordinacion disponibilidadCoordinacion);

    void edit(DisponibilidadCoordinacion disponibilidadCoordinacion);

    void remove(DisponibilidadCoordinacion disponibilidadCoordinacion);

    DisponibilidadCoordinacion find(Object id);

    List<DisponibilidadCoordinacion> findAll();

    void removeAll();
}
