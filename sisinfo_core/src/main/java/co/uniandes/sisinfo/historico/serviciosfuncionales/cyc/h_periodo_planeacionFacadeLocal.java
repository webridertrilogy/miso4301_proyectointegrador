/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_periodo_planeacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_periodo_planeacionFacadeLocal {

    void create(h_periodo_planeacion h_periodo_planeacion);

    void edit(h_periodo_planeacion h_periodo_planeacion);

    void remove(h_periodo_planeacion h_periodo_planeacion);

    h_periodo_planeacion find(Object id);

    List<h_periodo_planeacion> findAll();

    List<h_periodo_planeacion> findRange(int[] range);

    int count();

    h_periodo_planeacion findByNombre(String nombre);

}
