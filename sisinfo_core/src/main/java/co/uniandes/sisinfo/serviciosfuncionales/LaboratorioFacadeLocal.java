package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Laboratorio;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachade de la entidad Laboratorio (Interface Local)
 * @author Marcela Morales
 */
@Local
public interface LaboratorioFacadeLocal {

    void create(Laboratorio laboratorio);

    void edit(Laboratorio laboratorio);

    void remove(Laboratorio laboratorio);

    Laboratorio find(Object id);

    List<Laboratorio> findAll();

    List<Laboratorio> findRange(int[] range);

    int count();

    public Collection<Laboratorio> findLaboratoriosAutorizados(String correo);

    Laboratorio findLaboratorioPorNombre(String nombre);

    public Collection<Laboratorio> findLaboratoriosEncargado(String id);

    public Collection<Laboratorio> findLaboratoriosActivos();

    Collection<Laboratorio> findLaboratoriosActivosYReservables();
}
