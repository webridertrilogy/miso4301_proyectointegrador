package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.HojaVida;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad HojaVida (interface remota)
 * @author Marcela Morales
 */
@Remote
public interface HojaVidaFacadeRemote {

    void create(HojaVida hojaVida);

    void edit(HojaVida hojaVida);

    void remove(HojaVida hojaVida);

    HojaVida find(Object id);

    List<HojaVida> findAll();
}
