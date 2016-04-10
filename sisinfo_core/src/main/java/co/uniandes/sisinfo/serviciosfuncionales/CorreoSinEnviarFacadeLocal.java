/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CorreoSinEnviar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface CorreoSinEnviarFacadeLocal {

    void create(CorreoSinEnviar correoSinEnviar);

    void edit(CorreoSinEnviar correoSinEnviar);

    void remove(CorreoSinEnviar correoSinEnviar);

    CorreoSinEnviar find(Object id);

    List<CorreoSinEnviar> findAll();

    List<CorreoSinEnviar> findRange(int[] range);

    int count();

}
