package co.uniandes.sisinfo.historico.serviciosfuncionales.tesispregrado;

import co.uniandes.sisinfo.historico.entities.tesispregrado.h_tesisPregrado;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Fachada - Histórico de Proyecto de Grado
 * @author Marcela Morales
 */
@Remote
public interface h_tesisPregradoFacadeRemote {

    void create(h_tesisPregrado h_tesisPregrado);

    void edit(h_tesisPregrado h_tesisPregrado);

    void remove(h_tesisPregrado h_tesisPregrado);

    h_tesisPregrado find(Object id);

    List<h_tesisPregrado> findAll();

    List<h_tesisPregrado> findRange(int[] range);

    int count();

    List<h_tesisPregrado> findByCorreoEstudiante(String correo);

    List<h_tesisPregrado> findByCorreoAsesor(String correo);

    List<h_tesisPregrado> findByEstado(String estado);

    List<h_tesisPregrado> findBySemestre(String semestre);

    List<h_tesisPregrado> findByEstadoYSemestre(String semestre, String estado);

    List<h_tesisPregrado> findByEstadoYSemestreYCorreoAsesor(String semestre, String estado, String correo);
}
