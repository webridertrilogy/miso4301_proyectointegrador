/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.SectorCorporativo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface SectorCorporativoFacadeLocal {

    void create(SectorCorporativo sectorCorporativo);

    void edit(SectorCorporativo sectorCorporativo);

    void remove(SectorCorporativo sectorCorporativo);

    SectorCorporativo find(Object id);

    List<SectorCorporativo> findAll();

    List<SectorCorporativo> findRange(int[] range);

    int count();

    SectorCorporativo findByNombre(String nombre);

}
