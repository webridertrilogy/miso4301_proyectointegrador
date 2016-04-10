/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DirectorioInterfacesPorRol;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface DirectorioInterfacesPorRolFacadeRemote {

    void create(DirectorioInterfacesPorRol directorioInterfacesPorRol);

    void edit(DirectorioInterfacesPorRol directorioInterfacesPorRol);

    void remove(DirectorioInterfacesPorRol directorioInterfacesPorRol);

    DirectorioInterfacesPorRol find(Object id);

    List<DirectorioInterfacesPorRol> findAll();

    List<DirectorioInterfacesPorRol> findRange(int[] range);

    int count();

    Collection<DirectorioInterfacesPorRol> buscarInterfacesActivasPorRol(String rol);

}
