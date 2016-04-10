package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.HorarioDia;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachade de la entidad HorarioDia (Interface Remota)
 * @author Marcela Morales
 */
@Remote
public interface HorarioDiaFacadeRemote {

    void create(HorarioDia horarioDia);

    void edit(HorarioDia horarioDia);

    void remove(HorarioDia horarioDia);

    HorarioDia find(Object id);

    List<HorarioDia> findAll();

    List<HorarioDia> findRange(int[] range);

    int count();

}
