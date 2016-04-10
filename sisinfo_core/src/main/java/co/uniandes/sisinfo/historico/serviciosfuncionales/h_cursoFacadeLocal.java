/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_curso;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author david
 */
@Local
public interface h_cursoFacadeLocal {

    void create(h_curso h_curso);

    void edit(h_curso h_curso);

    void remove(h_curso h_curso);

    h_curso find(Object id);

    List<h_curso> findAll();

    List<h_curso> findRange(int[] range);

    int count();

    h_curso findByCRNSeccion(String crn);

    public abstract h_curso findBySeccionId(long id);


}
