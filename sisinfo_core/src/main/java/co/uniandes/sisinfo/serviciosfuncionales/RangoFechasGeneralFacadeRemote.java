package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.RangoFechasGeneral;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad RangoFechasGeneral (Remote)
 * @author Marcela Morales
 */
@Remote
public interface RangoFechasGeneralFacadeRemote {

    void create(RangoFechasGeneral rangoFechasGeneral);

    void edit(RangoFechasGeneral rangoFechasGeneral);

    void remove(RangoFechasGeneral rangoFechasGeneral);

    void removeAll();

    RangoFechasGeneral find(Object id);

    List<RangoFechasGeneral> findAll();

    RangoFechasGeneral findByNombre(String nombre);

    RangoFechasGeneral findById(Long id);
}
