package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Oferta;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada de la entidad Oferta (Interface local)
 * @author Marcela Morales
 */
@Local
public interface OfertaFacadeLocal {

    void create(Oferta oferta);

    void edit(Oferta oferta);

    void remove(Oferta oferta);

    Oferta find(Object id);

    List<Oferta> findAll();

    Oferta findyById(Long id);

    List<Oferta> findByEstado(String estado);
}
