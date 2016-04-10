package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Proponente;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad Proponente (Interface remota)
 * @author Marcela Morales
 */
@Remote
public interface ProponenteFacadeRemote {

    void create(Proponente proponente);

    void edit(Proponente proponente);

    void remove(Proponente proponente);

    Proponente find(Object id);

    List<Proponente> findAll();

    List<Proponente> findByTipoEmpresa();

    Proponente findByIdOferta(Long id);

    Proponente findById(Long id);

    Proponente findByCorreo(String correo);
}
