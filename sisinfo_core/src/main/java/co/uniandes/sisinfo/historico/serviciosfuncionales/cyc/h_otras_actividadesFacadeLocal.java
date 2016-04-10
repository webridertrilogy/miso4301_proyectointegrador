/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_otras_actividades;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_otras_actividadesFacadeLocal {

    void create(h_otras_actividades h_otras_actividades);

    void edit(h_otras_actividades h_otras_actividades);

    void remove(h_otras_actividades h_otras_actividades);

    h_otras_actividades find(Object id);

    List<h_otras_actividades> findAll();

    List<h_otras_actividades> findRange(int[] range);

    int count();

}
