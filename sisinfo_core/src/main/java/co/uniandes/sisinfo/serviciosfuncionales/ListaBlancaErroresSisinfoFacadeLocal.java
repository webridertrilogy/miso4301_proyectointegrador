/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ListaBlancaErroresSisinfo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface ListaBlancaErroresSisinfoFacadeLocal {

    void create(ListaBlancaErroresSisinfo listaBlancaErroresSisinfo);

    void edit(ListaBlancaErroresSisinfo listaBlancaErroresSisinfo);

    void remove(ListaBlancaErroresSisinfo listaBlancaErroresSisinfo);

    ListaBlancaErroresSisinfo find(Object id);

    List<ListaBlancaErroresSisinfo> findAll();

    List<ListaBlancaErroresSisinfo> findRange(int[] range);

    int count();

    ListaBlancaErroresSisinfo findByIdError(String idError);

}
