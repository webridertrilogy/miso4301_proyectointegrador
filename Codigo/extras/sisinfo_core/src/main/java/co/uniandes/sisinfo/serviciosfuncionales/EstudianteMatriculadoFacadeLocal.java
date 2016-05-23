package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.EstudianteMatriculado;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Fachada de la entidad EstudianteMatriculado
 * @author Marcela Morales
 */
@Local
public interface EstudianteMatriculadoFacadeLocal {

    void create(EstudianteMatriculado estudiante);

    void edit(EstudianteMatriculado estudiante);

    void remove(EstudianteMatriculado estudiante);

    EstudianteMatriculado find(Object id);

    List<EstudianteMatriculado> findAll();

    List<EstudianteMatriculado> findRange(int[] range);

    int count();

    EstudianteMatriculado findByCarnet(String carnet);
}
