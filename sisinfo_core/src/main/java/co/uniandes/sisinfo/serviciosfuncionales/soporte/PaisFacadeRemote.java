/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;


import co.uniandes.sisinfo.entities.soporte.Pais;
import co.uniandes.sisinfo.entities.soporte.Pais;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author lj.bautista31
 */
@Remote
public interface PaisFacadeRemote {

    void create(Pais pais);

    void edit(Pais pais);

    void remove(Pais pais);

    Pais find(Object id);

    List<Pais> findAll();

    Pais findByNombre(String nombre);

}
