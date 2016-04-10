/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_archivo;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author david
 */
@Remote
public interface h_archivoFacadeRemote {

    void create(h_archivo h_archivo);

    void edit(h_archivo h_archivo);

    void remove(h_archivo h_archivo);

    h_archivo find(Object id);

    List<h_archivo> findAll();

    List<h_archivo> findRange(int[] range);

    int count();

    h_archivo findBySeccionAndTipo(String crn, String tipo);

    List<String> findPeriodos();
}
