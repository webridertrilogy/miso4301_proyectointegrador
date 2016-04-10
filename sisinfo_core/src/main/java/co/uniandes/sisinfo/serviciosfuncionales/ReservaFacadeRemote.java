package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Reserva;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad Reserva (Interface remota)
 * @author Marcela Morales
 */
@Remote
public interface ReservaFacadeRemote {

    void create(Reserva reserva);

    void edit(Reserva reserva);

    void remove(Reserva reserva);

    Reserva find(Object id);

    List<Reserva> findAll();

    List<Reserva> buscarReservasActualesPorLogin(String correo);

    List<Reserva> buscarReservasPorLogin(String correo);

    List<Reserva> buscarReservasRango(Timestamp inicio, Timestamp fin);

    Reserva buscarReservasInterseccionHoras(Timestamp fecha, Timestamp inicio, Timestamp fin);

    List<Reserva> buscarReservasFecha(Timestamp fecha);

    List<Reserva> buscarReservasPorCorreoYEstado(String correo, String estado);

    List<Reserva> buscarReservasPorEstado(String estado);
}
