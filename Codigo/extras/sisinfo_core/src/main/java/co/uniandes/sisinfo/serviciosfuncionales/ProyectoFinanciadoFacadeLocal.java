/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ProyectoFinanciado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface ProyectoFinanciadoFacadeLocal {

    void create(ProyectoFinanciado proyectoFinanciado);

    void edit(ProyectoFinanciado proyectoFinanciado);

    void remove(ProyectoFinanciado proyectoFinanciado);

    ProyectoFinanciado find(Object id);

    List<ProyectoFinanciado> findAll();

    List<ProyectoFinanciado> findRange(int[] range);

    int count();

    ProyectoFinanciado findByName(String nombre);

}
