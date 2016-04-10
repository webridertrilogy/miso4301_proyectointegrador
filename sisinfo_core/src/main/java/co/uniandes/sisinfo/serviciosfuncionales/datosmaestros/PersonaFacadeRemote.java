/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;


import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.util.List;
import javax.ejb.Remote;
import javax.naming.NamingException;

/**
 *
 * @author da-naran
 */
@Remote
public interface PersonaFacadeRemote {

    void create(Persona persona);

    void edit(Persona persona);

    void refresh(Persona persona);

    void remove(Persona persona);

    Persona find(Object id);

    List<Persona> findAll();

    Persona findByCorreo(String correo);

    Persona findLikeCorreo(String correo);

    void flush();

     public void crearPersonaPorLogin(String login) throws NamingException ;

}
