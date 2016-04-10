/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.NivelPlanta;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface NivelPlantaFacadeLocal {

    void create(NivelPlanta nivelPlanta);

    void edit(NivelPlanta nivelPlanta);

    void remove(NivelPlanta nivelPlanta);

    NivelPlanta find(Object id);

    List<NivelPlanta> findAll();

    List<NivelPlanta> findRange(int[] range);

    int count();

    NivelPlanta findByNombre(String name);

}
