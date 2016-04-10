/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Credencial;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JuanCamilo
 */
@Local
public interface CredencialFacadeLocal {

    void create(Credencial credencial);

    void edit(Credencial credencial);

    void remove(Credencial credencial);

    Credencial find(Object id);

    List<Credencial> findAll();

    public Credencial findByCuenta(String cuenta);

    void removeAll();

}
