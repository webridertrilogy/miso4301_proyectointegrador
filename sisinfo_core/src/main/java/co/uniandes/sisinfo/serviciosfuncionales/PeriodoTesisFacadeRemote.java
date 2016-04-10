/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.PeriodoTesis;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface PeriodoTesisFacadeRemote {

    void create(PeriodoTesis periodoTesis);

    void edit(PeriodoTesis periodoTesis);

    void remove(PeriodoTesis periodoTesis);

    PeriodoTesis find(Object id);

    List<PeriodoTesis> findAll();

    List<PeriodoTesis> findRange(int[] range);

    int count();

    PeriodoTesis findByPeriodo(String nombre);

    PeriodoTesis findActual();

}
