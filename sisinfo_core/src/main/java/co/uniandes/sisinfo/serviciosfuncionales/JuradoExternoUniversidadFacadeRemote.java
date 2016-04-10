/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.JuradoExternoUniversidad;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface JuradoExternoUniversidadFacadeRemote {

    void create(JuradoExternoUniversidad juradoExternoUniversidad);

    void edit(JuradoExternoUniversidad juradoExternoUniversidad);

    void remove(JuradoExternoUniversidad juradoExternoUniversidad);

    JuradoExternoUniversidad find(Object id);

    List<JuradoExternoUniversidad> findAll();

    List<JuradoExternoUniversidad> findRange(int[] range);

    int count();

    JuradoExternoUniversidad findByCorreo(String correo);

}
