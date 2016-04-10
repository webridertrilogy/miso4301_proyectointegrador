package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ReservaInventario;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachade de la entidad ReservaInventario (Interface Remota)
 * @author Marcela Morales
 */
@Remote
public interface ReservaInventarioFacadeRemote {

    void create(ReservaInventario reservaInventario);

    void edit(ReservaInventario reservaInventario);

    void remove(ReservaInventario reservaInventario);

    ReservaInventario find(Object id);

    List<ReservaInventario> findAll();

    List<ReservaInventario> findRange(int[] range);

    int count();

    Collection<ReservaInventario> consultarReservasPorNombreLaboratorio(String nombre);

    Collection<ReservaInventario> consultarReservasPorNombreLaboratorioyEstado(String nombre, String estado);

    Collection<ReservaInventario> consultarReservasPorNombreLaboratorioyEstadoyRangoFechas(String nombre, String estado, Timestamp fechaInicial, Timestamp fechaFinal);

    Collection<ReservaInventario> consultarReservasByPersonaYEstado(String correo, String estado);

    Collection<ReservaInventario> consultarReservasAnterioresAFechaByEstado(Date fecha, String estado);

    Collection<ReservaInventario> consultarReservasPorEstado(String estado);

    Collection<ReservaInventario> consultarReservasPorRangoFechas(Timestamp fechaInicial, Timestamp fechaFinal);

}
