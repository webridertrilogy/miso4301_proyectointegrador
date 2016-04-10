package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_1;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Fachada - Histórico de Tesis
 * @author Marcela Morales
 */
@Local
public interface h_tesis_1FacadeLocal {

    void create(h_tesis_1 h_tesis_1);

    void edit(h_tesis_1 h_tesis_1);

    void remove(h_tesis_1 h_tesis_1);

    h_tesis_1 find(Object id);

    List<h_tesis_1> findAll();

    List<h_tesis_1> findRange(int[] range);

    int count();

    List<h_tesis_1> findByCorreoEstudiante(String correo);

    List<h_tesis_1> findByCorreoAsesor(String correo);

    List<h_tesis_1> findByEstado(String estado);

    List<h_tesis_1> findBySemestre(String semestre);

    List<h_tesis_1> findByEstadoYSemestre(String semestre, String estado);

    List<h_tesis_1> findByEstadoYSemestreYCorreoAsesor(String semestre, String estado, String correo);

    List<h_tesis_1> findByEstadoYCorreoAsesor(String estado, String correo);

    List<h_tesis_1> findBySemestreYCorreoAsesor(String semestre, String correo);
}
