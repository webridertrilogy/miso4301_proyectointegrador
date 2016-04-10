/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TemaTesisPregrado;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Local
public interface TemaTesisPregradoFacadeLocal {

    void create(TemaTesisPregrado temaTesisPregrado);

    void edit(TemaTesisPregrado temaTesisPregrado);

    void remove(TemaTesisPregrado temaTesisPregrado);

    TemaTesisPregrado find(Object id);

    List<TemaTesisPregrado> findAll();

    List<TemaTesisPregrado> findRange(int[] range);

    int count();

    //TemaTesisPregrado findByNombreYCorreoAsesor(String nombre,String correo);

    TemaTesisPregrado findByNombreYCorreoAsesorYPeriodo(String nombre, String correo, String periodo);

    Collection<TemaTesisPregrado> findByCorreoAsesor(String correo);

    List<TemaTesisPregrado> findByPeriodoYCorreoAsesor(String correo, String periodo);
}
