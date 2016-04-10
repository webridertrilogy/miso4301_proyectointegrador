/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ComentarioTesisPregrado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Local
public interface ComentarioTesisPregradoFacadeLocal {

    void create(ComentarioTesisPregrado comentarioTesisPregrado);

    void edit(ComentarioTesisPregrado comentarioTesisPregrado);

    void remove(ComentarioTesisPregrado comentarioTesisPregrado);

    ComentarioTesisPregrado find(Object id);

    List<ComentarioTesisPregrado> findAll();

    List<ComentarioTesisPregrado> findRange(int[] range);

    int count();

}
