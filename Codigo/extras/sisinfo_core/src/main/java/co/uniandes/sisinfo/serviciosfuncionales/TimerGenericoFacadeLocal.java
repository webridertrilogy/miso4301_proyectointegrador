/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TimerGenerico;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface TimerGenericoFacadeLocal {

    void create(TimerGenerico timerGenerico);

    void edit(TimerGenerico timerGenerico);

    void remove(TimerGenerico timerGenerico);

    TimerGenerico find(Object id);

    List<TimerGenerico> findAll();

    List<TimerGenerico> findRange(int[] range);

    int count();

}
