/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Pregunta;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Administrador
 */
@Remote
public interface PreguntaFacadeRemote {

    void create(Pregunta pregunta);

    void edit(Pregunta pregunta);

    void remove(Pregunta pregunta);

    Pregunta find(Object id);

    List<Pregunta> findAll();

    List<Pregunta> findRange(int[] range);

    int count();

    Pregunta findById (Long id);

}
