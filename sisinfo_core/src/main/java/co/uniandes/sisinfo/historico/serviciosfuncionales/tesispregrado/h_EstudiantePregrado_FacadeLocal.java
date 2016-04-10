package co.uniandes.sisinfo.historico.serviciosfuncionales.tesispregrado;


import co.uniandes.sisinfo.historico.entities.tesispregrado.h_estudiante_pregrado;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Fachada - Histórico de Estudiante Pregrado
 * @author Paola Gómez
 */
@Local
public interface h_EstudiantePregrado_FacadeLocal {

    void create(h_estudiante_pregrado h_estudiante_pregrado);

    void edit(h_estudiante_pregrado h_estudiante_pregrado);

    void remove(h_estudiante_pregrado h_estudiante_pregrado);

    h_estudiante_pregrado find(Object id);

    List<h_estudiante_pregrado> findAll();

    List<h_estudiante_pregrado> findRange(int[] range);

    int count();

    h_estudiante_pregrado findByCorreoEstudiante(String correo);

    List<h_estudiante_pregrado> findByCorreoProfesor(String correo);
}
