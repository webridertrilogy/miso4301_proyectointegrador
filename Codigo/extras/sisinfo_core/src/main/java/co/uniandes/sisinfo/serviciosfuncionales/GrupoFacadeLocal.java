/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Grupo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author da-naran
 */
@Local
public interface GrupoFacadeLocal {

    void create(Grupo grupo);

    void edit(Grupo grupo);

    void remove(Grupo grupo);

    Grupo find(Object id);

    List<Grupo> findAll();

    Grupo findByNombre(String nombre);

    List<Grupo> findByCorreoDuenho(String correo);
}
