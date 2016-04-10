/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Autorizado;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Admin
 */
@Remote
public interface AutorizadoFacadeRemote {

      void create(Autorizado autorizado);

    void edit(Autorizado autorizado);

    void remove(Autorizado autorizado);

    Autorizado find(Object id);

    List<Autorizado> findAll();

    List<Autorizado> findRange(int[] range);

    int count();

    Autorizado findAutorizadoByCorreo(String correo);
}
