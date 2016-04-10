package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TipoAsistenciaGraduada;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada de la entidad TipoAsistenciaGraduada (interface local)
 * @author Marcela Morales
 */
@Local
public interface TipoAsistenciaGraduadaFacadeLocal {

    void create(TipoAsistenciaGraduada tipoAsistenciaGraduada);

    void edit(TipoAsistenciaGraduada tipoAsistenciaGraduada);

    void remove(TipoAsistenciaGraduada tipoAsistenciaGraduada);

    TipoAsistenciaGraduada find(Object id);

    List<TipoAsistenciaGraduada> findAll();

    TipoAsistenciaGraduada findById(Long id);

    TipoAsistenciaGraduada findByTipo(String tipo);
}
