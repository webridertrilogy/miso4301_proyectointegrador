package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Laboratorio;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachade de la entidad Laboratorio (Interface Remota)
 * @author Marcela Morales
 */
@Remote
public interface LaboratorioFacadeRemote {

    void create(Laboratorio laboratorio);

    void edit(Laboratorio laboratorio);

    void remove(Laboratorio laboratorio);

    Laboratorio find(Object id);

    List<Laboratorio> findAll();

    List<Laboratorio> findRange(int[] range);

    int count();

    Collection<Laboratorio> findLaboratoriosAutorizados(String correo);

    Laboratorio findLaboratorioPorNombre(String nombre);

    Collection<Laboratorio> findLaboratoriosActivosYReservables();

  //  Collection<Laboratorio> findLaboratoriosActivos();

}
