/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ReservaMultiple;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface ReservaMultipleFacadeRemote {

    void create(ReservaMultiple reservaMultiple);

    void edit(ReservaMultiple reservaMultiple);

    void remove(ReservaMultiple reservaMultiple);

    ReservaMultiple find(Object id);

    List<ReservaMultiple> findAll();

    List<ReservaMultiple> findRange(int[] range);

    int count();

    public co.uniandes.sisinfo.entities.ReservaMultiple findById(java.lang.Long id);

    public java.util.Collection<co.uniandes.sisinfo.entities.ReservaInventario> consultarReservasMultiplesPorPeriodicidad(java.lang.String periodicidad);

    public java.util.Collection<co.uniandes.sisinfo.entities.ReservaInventario> consultarReservasMultiplesPorFinalizacionReservaMultiple(java.util.Date finalizacionReservaMultiple);

    public co.uniandes.sisinfo.entities.ReservaMultiple consultarReservasMultiplesPorReserva(java.lang.Long id);
}
