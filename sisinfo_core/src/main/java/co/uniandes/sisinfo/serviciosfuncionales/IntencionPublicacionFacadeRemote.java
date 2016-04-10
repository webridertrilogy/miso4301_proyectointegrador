/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.IntencionPublicacion;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface IntencionPublicacionFacadeRemote {

    void create(IntencionPublicacion intencionPublicacion);

    void edit(IntencionPublicacion intencionPublicacion);

    void remove(IntencionPublicacion intencionPublicacion);

    IntencionPublicacion find(Object id);

    List<IntencionPublicacion> findAll();

    List<IntencionPublicacion> findRange(int[] range);

    int count();

}
