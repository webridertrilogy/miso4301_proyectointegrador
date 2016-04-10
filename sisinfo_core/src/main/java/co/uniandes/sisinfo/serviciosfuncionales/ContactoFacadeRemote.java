/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Contacto;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface ContactoFacadeRemote {

    void create(Contacto contacto);

    void edit(Contacto contacto);

    void remove(Contacto contacto);

    Contacto find(Object id);

    List<Contacto> findAll();

    List<Contacto> findRange(int[] range);

    int count();

    Contacto findContactoByCorreo(String correo);

    List<Contacto> findContactosValidos();

    public Collection<Contacto> findContactos(String nombre, String apellidos, String id, String cargo, String empresa, String ciudad, String celular, String correo, String sector, Date fecha);

}
