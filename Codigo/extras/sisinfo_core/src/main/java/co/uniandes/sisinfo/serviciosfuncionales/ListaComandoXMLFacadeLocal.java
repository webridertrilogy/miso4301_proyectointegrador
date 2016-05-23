/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ListaComandoXML;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface ListaComandoXMLFacadeLocal {

    void create(ListaComandoXML listaComandoXML);

    void edit(ListaComandoXML listaComandoXML);

    void remove(ListaComandoXML listaComandoXML);

    ListaComandoXML find(Object id);

    List<ListaComandoXML> findAll();

    /**
     * Consulta comandos por nombre.
     * @return
     */
    List<ListaComandoXML> findByNombre(String nombre);
}
