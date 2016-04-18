/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import java.util.List;

import javax.ejb.Remote;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoCuenta;

/**
 *
 * @author Asistente
 */
@Remote
public interface TipoCuentaFacadeRemote {

    void create(TipoCuenta tipoCuenta);

    void edit(TipoCuenta tipoCuenta);

    void remove(TipoCuenta tipoCuenta);

    TipoCuenta find(Object id);

    List<TipoCuenta> findAll();

    List<TipoCuenta> findRange(int[] range);

    int count();

   
    public java.util.List<TipoCuenta> findByName(java.lang.String name);

}
