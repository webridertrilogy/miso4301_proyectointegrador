/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DescargaProfesor;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface DescargaProfesorFacadeLocal {

    void create(DescargaProfesor descargaProfesor);

    void edit(DescargaProfesor descargaProfesor);

    void remove(DescargaProfesor descargaProfesor);

    DescargaProfesor find(Object id);

    List<DescargaProfesor> findAll();

    List<DescargaProfesor> findRange(int[] range);

    int count();

  

    DescargaProfesor findByNombre(String nombre);

}
