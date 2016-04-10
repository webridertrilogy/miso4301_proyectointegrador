/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.PeriodoTesisPregrado;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Remote
public interface PeriodoTesisPregradoFacadeRemote {

    void create(PeriodoTesisPregrado periodoTesisPregrado);

    void edit(PeriodoTesisPregrado periodoTesisPregrado);

    void remove(PeriodoTesisPregrado periodoTesisPregrado);

    PeriodoTesisPregrado find(Object id);

    List<PeriodoTesisPregrado> findAll();

    List<PeriodoTesisPregrado> findRange(int[] range);

    int count();

    PeriodoTesisPregrado findByPeriodo(String periodo);

}
