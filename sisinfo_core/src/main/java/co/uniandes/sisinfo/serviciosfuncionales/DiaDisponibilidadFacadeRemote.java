package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DiaDisponibilidad;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad DatosContacto (Interface remota)
 * @author Germán Florez, Marcela Morales
 */
@Remote
public interface DiaDisponibilidadFacadeRemote {

    void create(DiaDisponibilidad diaDisponibilidad);

    void edit(DiaDisponibilidad diaDisponibilidad);

    void remove(DiaDisponibilidad diaDisponibilidad);

    DiaDisponibilidad find(Object id);

    List<DiaDisponibilidad> findAll();

    void removeAll();
}
