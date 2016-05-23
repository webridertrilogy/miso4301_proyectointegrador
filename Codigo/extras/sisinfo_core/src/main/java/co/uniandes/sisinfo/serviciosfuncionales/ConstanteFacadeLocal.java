/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Constante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JuanCamilo
 */
@Local
public interface ConstanteFacadeLocal {

    void create(Constante constante);

    void edit(Constante constante);

    void remove(Constante constante);

    Constante find(Object id);

    List<Constante> findAll();

    Constante findById(Long id);

    Constante findByNombre(String nombre);

//    String getValor(String nombre);
}
