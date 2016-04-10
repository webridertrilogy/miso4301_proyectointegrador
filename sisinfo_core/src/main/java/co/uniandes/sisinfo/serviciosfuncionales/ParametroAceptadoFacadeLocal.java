/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ParametroAceptado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author david
 */
@Local
public interface ParametroAceptadoFacadeLocal {

    void create(ParametroAceptado parametroAceptado);

    void edit(ParametroAceptado parametroAceptado);

    void remove(ParametroAceptado parametroAceptado);

    ParametroAceptado find(Object id);

    List<ParametroAceptado> findAll();

    List<ParametroAceptado> findByTipoTarea(java.lang.String tipo);

}
