package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;


import co.uniandes.sisinfo.historico.entities.tesisM.h_estudiante_maestria;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Fachada - Histórico de Estudiante Maestria
 * @author Paola Gómez
 */
@Local
public interface h_EstudianteMaestria_FacadeLocal {

    void create(h_estudiante_maestria h_estudiante_maestria);

    void edit(h_estudiante_maestria h_estudiante_maestria);

    void remove(h_estudiante_maestria h_estudiante_maestria);

    h_estudiante_maestria find(Object id);

    List<h_estudiante_maestria> findAll();

    List<h_estudiante_maestria> findRange(int[] range);

    int count();

    h_estudiante_maestria findByCorreoEstudiante(String correo);

    List<h_estudiante_maestria> findByProfesor(String correoProfesor);
}
