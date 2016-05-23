/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TipoPublicacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface TipoPublicacionFacadeLocal {

    void create(TipoPublicacion tipoPublicacion);

    void edit(TipoPublicacion tipoPublicacion);

    void remove(TipoPublicacion tipoPublicacion);

    TipoPublicacion find(Object id);

    List<TipoPublicacion> findAll();

    List<TipoPublicacion> findRange(int[] range);

    int count();

    TipoPublicacion findByTipoPublicacion(String tipoPublicacion);

}
