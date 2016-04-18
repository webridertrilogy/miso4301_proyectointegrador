/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;


import java.util.List;

import javax.ejb.Remote;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;

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
