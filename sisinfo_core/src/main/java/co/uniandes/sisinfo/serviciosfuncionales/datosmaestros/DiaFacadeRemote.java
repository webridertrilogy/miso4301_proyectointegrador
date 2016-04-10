/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Dia;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author da-naran
 */
@Remote
public interface DiaFacadeRemote {

    void create(Dia dia);

    void edit(Dia dia);

    void remove(Dia dia);

    Dia find(Object id);

    List<Dia> findAll();

    List<Dia> findRange(int[] range);

    int count();

}
