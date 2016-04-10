/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_intencion_publicacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_intencion_publicacionFacadeLocal {

    void create(h_intencion_publicacion h_intencion_publicacion);

    void edit(h_intencion_publicacion h_intencion_publicacion);

    void remove(h_intencion_publicacion h_intencion_publicacion);

    h_intencion_publicacion find(Object id);

    List<h_intencion_publicacion> findAll();

    List<h_intencion_publicacion> findRange(int[] range);

    int count();

    public h_intencion_publicacion findByTituloObservacionesTipo(String tituloPublicacion, String observaciones, String tipoPublicacion);

}
