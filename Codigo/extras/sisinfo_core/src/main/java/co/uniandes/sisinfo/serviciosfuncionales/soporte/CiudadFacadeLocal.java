/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.Ciudad;

/**
 *
 * @author Administrador
 */
@Local
public interface CiudadFacadeLocal {

    void create(Ciudad ciudad);

    void edit(Ciudad ciudad);

    void remove(Ciudad ciudad);

    Ciudad find(Object id);

    List<Ciudad> findAll();

    List<Ciudad> findRange(int[] range);

    int count();

    Collection<Ciudad> findCiudadesByNombre(String nombre);
}
