/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import co.uniandes.sisinfo.entities.soporte.TipoCuenta;
import co.uniandes.sisinfo.entities.soporte.TipoCuenta;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface TipoCuentaFacadeLocal {

    void create(TipoCuenta tipoCuenta);

    void edit(TipoCuenta tipoCuenta);

    void remove(TipoCuenta tipoCuenta);

    TipoCuenta find(Object id);

    List<TipoCuenta> findAll();

    List<TipoCuenta> findRange(int[] range);

    int count();

    public java.util.List<co.uniandes.sisinfo.entities.soporte.TipoCuenta> findByName(java.lang.String name);

}
