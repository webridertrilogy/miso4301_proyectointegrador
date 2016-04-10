/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_proyecto_financiado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_proyecto_financiadoFacadeLocal {

    void create(h_proyecto_financiado h_proyecto_financiado);

    void edit(h_proyecto_financiado h_proyecto_financiado);

    void remove(h_proyecto_financiado h_proyecto_financiado);

    h_proyecto_financiado find(Object id);

    List<h_proyecto_financiado> findAll();

    List<h_proyecto_financiado> findRange(int[] range);

    int count();

    h_proyecto_financiado findByName(String name);

    h_proyecto_financiado findByNombreEntidadDescripcionYPeriodo(String nombre, String entidad, String descripcion0, String periodo);

}
