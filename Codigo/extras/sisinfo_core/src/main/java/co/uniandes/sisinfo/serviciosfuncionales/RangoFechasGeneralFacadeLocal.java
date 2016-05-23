package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.RangoFechasGeneral;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada de la entidad RangoFechasGeneral (Local)
 * @author Marcela Morales
 */
@Local
public interface RangoFechasGeneralFacadeLocal {

    void create(RangoFechasGeneral rangoFechasGeneral);

    void edit(RangoFechasGeneral rangoFechasGeneral);

    void remove(RangoFechasGeneral rangoFechasGeneral);

    void removeAll();

    RangoFechasGeneral find(Object id);

    List<RangoFechasGeneral> findAll();

    RangoFechasGeneral findByNombre(String nombre);

    RangoFechasGeneral findById(Long id);
}
