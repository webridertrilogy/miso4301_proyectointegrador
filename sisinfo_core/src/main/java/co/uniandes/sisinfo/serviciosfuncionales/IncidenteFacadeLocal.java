/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Incidente;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface IncidenteFacadeLocal {

    void create(Incidente incidente);

    void edit(Incidente incidente);

    void remove(Incidente incidente);

    Incidente find(Object id);

    List<Incidente> findAll();

    List<Incidente> findRange(int[] range);

    int count();

    Incidente findByDescripcionyFecha(String descripcionIncidente, Timestamp fechaIncidente);

    Collection<Incidente> findByCorreoReportante(String correo);

    Collection<Incidente> findByEstado(String estado);

    Collection<Incidente> findByNoBorrado();

    /**
     * Consulta incidentes por el Id de la persona de soporte.
     * @return
     */
    List<Incidente> findByPersonaSoporte(Long idPersonaSoporte);
}
