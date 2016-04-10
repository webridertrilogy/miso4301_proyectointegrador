/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CursoPlaneado;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface CursoPlaneadoFacadeRemote {

    void create(CursoPlaneado cursoPlaneado);

    void edit(CursoPlaneado cursoPlaneado);

    void remove(CursoPlaneado cursoPlaneado);

    CursoPlaneado find(Object id);

    List<CursoPlaneado> findAll();

    List<CursoPlaneado> findRange(int[] range);

    int count();

}
