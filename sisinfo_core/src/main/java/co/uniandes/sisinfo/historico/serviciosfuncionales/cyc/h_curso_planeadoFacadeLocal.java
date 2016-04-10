/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_curso_planeado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_curso_planeadoFacadeLocal {

    void create(h_curso_planeado h_curso_planeado);

    void edit(h_curso_planeado h_curso_planeado);

    void remove(h_curso_planeado h_curso_planeado);

    h_curso_planeado find(Object id);

    List<h_curso_planeado> findAll();

    List<h_curso_planeado> findRange(int[] range);

    int count();

}
