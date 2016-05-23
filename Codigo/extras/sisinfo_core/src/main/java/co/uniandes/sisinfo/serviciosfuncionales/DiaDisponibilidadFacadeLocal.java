package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DiaDisponibilidad;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada de la entidad DatosContacto (Interface local)
 * @author Germán Florez, Marcela Morales
 */
@Local
public interface DiaDisponibilidadFacadeLocal {

    void create(DiaDisponibilidad diaDisponibilidad);

    void edit(DiaDisponibilidad diaDisponibilidad);

    void remove(DiaDisponibilidad diaDisponibilidad);

    DiaDisponibilidad find(Object id);

    List<DiaDisponibilidad> findAll();

    void removeAll();
}
