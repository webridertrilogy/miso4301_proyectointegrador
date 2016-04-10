/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.UsuarioEventos;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface UsuarioEventosFacadeLocal {

    void create(UsuarioEventos usuarioEventos);

    void edit(UsuarioEventos usuarioEventos);

    void remove(UsuarioEventos usuarioEventos);

    UsuarioEventos find(Object id);

    List<UsuarioEventos> findAll();

    List<UsuarioEventos> findRange(int[] range);

    int count();

    public UsuarioEventos findByCorreo(String correo);

    public UsuarioEventos findByHash(String hash);

}
