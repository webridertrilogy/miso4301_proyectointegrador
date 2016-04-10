/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Archivo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ju-cort1
 */
@Local
public interface ArchivoFacadeLocal {

    void create(Archivo archivo);

    void edit(Archivo archivo);

    void remove(Archivo archivo);

    Archivo find(Object id);

    Archivo findById(Long id);

    List<Archivo> findAll();

    Archivo findBySeccionAndTipo(String crn, String tipo);
}
