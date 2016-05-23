/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.PeriodoPlaneacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface PeriodoPlaneacionFacadeLocal {

    void create(PeriodoPlaneacion periodoPlaneacion);

    void edit(PeriodoPlaneacion periodoPlaneacion);

    void remove(PeriodoPlaneacion periodoPlaneacion);

    PeriodoPlaneacion find(Object id);

    List<PeriodoPlaneacion> findAll();

    List<PeriodoPlaneacion> findRange(int[] range);

    int count();

    PeriodoPlaneacion darUltimoPeriodoPlaneacion();

    PeriodoPlaneacion findByNombre(String periodo);

}
