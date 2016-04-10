package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_2;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Fachada - Histórico de Tesis 2
 * @author Marcela Morales
 */
@Local
public interface h_tesis_2FacadeLocal {

    void create(h_tesis_2 h_tesis_2);

    void edit(h_tesis_2 h_tesis_2);

    void remove(h_tesis_2 h_tesis_2);

    h_tesis_2 find(Object id);

    List<h_tesis_2> findAll();

    List<h_tesis_2> findRange(int[] range);

    int count();

    List<h_tesis_2> findByCorreoEstudiante(String correo);

    List<h_tesis_2> findByCorreoAsesor(String correo);

    List<h_tesis_2> findByEstado(String estado);

    List<h_tesis_2> findBySemestre(String semestre);

    List<h_tesis_2> findByEstadoYSemestre(String semestre, String estado);

    List<h_tesis_2> findByEstadoYSemestreYCorreoAsesor(String semestre, String estado, String correo);

    List<h_tesis_2> findByEstadoYCorreoAsesor(String estado, String correo);

    List<h_tesis_2> findBySemestreYCorreoAsesor(String semestre, String correo);
}
