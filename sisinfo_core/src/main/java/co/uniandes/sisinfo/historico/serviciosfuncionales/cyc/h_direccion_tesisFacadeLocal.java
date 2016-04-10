/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_direccion_tesis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_direccion_tesisFacadeLocal {

    void create(h_direccion_tesis h_direccion_tesis);

    void edit(h_direccion_tesis h_direccion_tesis);

    void remove(h_direccion_tesis h_direccion_tesis);

    h_direccion_tesis find(Object id);

    List<h_direccion_tesis> findAll();

    List<h_direccion_tesis> findRange(int[] range);

    int count();

    h_direccion_tesis findDireccionTesisByName(String name);

}
