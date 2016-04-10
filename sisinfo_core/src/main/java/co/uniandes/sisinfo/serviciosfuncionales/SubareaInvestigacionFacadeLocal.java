/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.SubareaInvestigacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface SubareaInvestigacionFacadeLocal {

    void create(SubareaInvestigacion subareaInvestigacion);

    void edit(SubareaInvestigacion subareaInvestigacion);

    void remove(SubareaInvestigacion subareaInvestigacion);

    SubareaInvestigacion find(Object id);

    List<SubareaInvestigacion> findAll();

    List<SubareaInvestigacion> findRange(int[] range);

    int count();

    SubareaInvestigacion findByNombreSubarea(String nombre);

}
