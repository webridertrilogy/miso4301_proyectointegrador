/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CursoMaestria;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface CursoMaestriaFacadeRemote {

    void create(CursoMaestria cursoMaestria);

    void edit(CursoMaestria cursoMaestria);

    void remove(CursoMaestria cursoMaestria);

    CursoMaestria find(Object id);

    List<CursoMaestria> findAll();

    List<CursoMaestria> findRange(int[] range);

    int count();

    CursoMaestria findByNombre(String nombre);

    Collection<CursoMaestria>  findByClasificacion(String clasificacion);

}
