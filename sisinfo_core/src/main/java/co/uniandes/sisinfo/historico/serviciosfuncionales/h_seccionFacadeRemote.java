/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_seccion;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author david
 */
@Remote
public interface h_seccionFacadeRemote {

    void create(h_seccion h_seccion);

    void edit(h_seccion h_seccion);

    void remove(h_seccion h_seccion);

    h_seccion find(Object id);

    List<h_seccion> findAll();

    List<h_seccion> findRange(int[] range);

    int count();

    List<h_seccion> findByCorreoProfesor(String correo);

    List<h_seccion> findByPeriodo(String periodo);
}
