/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.seguridad;

import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author lj.bautista31
 */
@Remote
public interface RolFacadeRemote {

    void create(Rol rol);

    void edit(Rol rol);

    void remove(Rol rol);

    Rol find(Object id);

    List<Rol> findAll();

    Rol findByRol(String rol);

    Rol findByDescripcion(String descripcion);

}
