/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_evento;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_eventoFacadeLocal {

    void create(h_evento h_evento);

    void edit(h_evento h_evento);

    void remove(h_evento h_evento);

    h_evento find(Object id);

    List<h_evento> findAll();

    List<h_evento> findRange(int[] range);

    int count();

}
