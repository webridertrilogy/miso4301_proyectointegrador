package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.AsistenciaGraduada;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada de la entidad AsistenciaGraduada (interface local)
 * @author Marcela Morales
 */
@Local
public interface AsistenciaGraduadaFacadeLocal {

    void create(AsistenciaGraduada asistenciaGraduada);

    void edit(AsistenciaGraduada asistenciaGraduada);

    void remove(AsistenciaGraduada asistenciaGraduada);

    AsistenciaGraduada find(Object id);

    List<AsistenciaGraduada> findAll();

    AsistenciaGraduada findById(Long id);

    List<AsistenciaGraduada> findByPeriodo(String periodo);

    List<AsistenciaGraduada> findByCorreoProfesor(String correoProfesor);

    List<AsistenciaGraduada> findByCorreoEstudiante(String correoEstudiante);

    AsistenciaGraduada findByPeriodoYCorreoEstudiante(String periodo, String correoEstudiante);
}
