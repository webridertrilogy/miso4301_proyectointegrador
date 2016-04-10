/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.FiltroCorreo;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface FiltroCorreoFacadeLocal {

    void create(FiltroCorreo filtroCorreo);

    void edit(FiltroCorreo filtroCorreo);

    void remove(FiltroCorreo filtroCorreo);

    FiltroCorreo find(Object id);

    List<FiltroCorreo> findAll();

    List<FiltroCorreo> findRange(int[] range);

    int count();

}
