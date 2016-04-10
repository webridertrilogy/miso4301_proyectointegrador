package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.HojaVida;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada de la entidad HojaVida (interface local)
 * @author Marcela Morales
 */
@Local
public interface HojaVidaFacadeLocal {

    void create(HojaVida hojaVida);

    void edit(HojaVida hojaVida);

    void remove(HojaVida hojaVida);

    HojaVida find(Object id);

    List<HojaVida> findAll();
}
