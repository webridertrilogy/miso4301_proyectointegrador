/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CursoTesis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface CursoTesisFacadeLocal {

    void create(CursoTesis cursoTesis);

    void edit(CursoTesis cursoTesis);

    void remove(CursoTesis cursoTesis);

    CursoTesis find(Object id);

    List<CursoTesis> findAll();

    List<CursoTesis> findRange(int[] range);

    int count();

}
