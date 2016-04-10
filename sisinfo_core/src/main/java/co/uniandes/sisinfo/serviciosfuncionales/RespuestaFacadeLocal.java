/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Respuesta;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface RespuestaFacadeLocal {

    void create(Respuesta respuesta);

    void edit(Respuesta respuesta);

    void remove(Respuesta respuesta);

    Respuesta find(Object id);

    List<Respuesta> findAll();

    List<Respuesta> findRange(int[] range);

    int count();

}
