package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.EstudiantePosgrado;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad EstudiantePosgrado (interface remota(
 * @author Marcela Morales
 */
@Remote
public interface EstudiantePosgradoFacadeRemote {

    void create(EstudiantePosgrado estudiantePosgrado);

    void edit(EstudiantePosgrado estudiantePosgrado);

    void remove(EstudiantePosgrado estudiantePosgrado);

    EstudiantePosgrado find(Object id);

    List<EstudiantePosgrado> findAll();

    List<EstudiantePosgrado> findRange(int[] range);

    int count();

    EstudiantePosgrado findByCorreo(String correo);
}
