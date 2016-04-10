/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ComandoXML;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Remote
public interface ComandoXMLFacadeRemote {

    void create(ComandoXML comandoXML);

    void edit(ComandoXML comandoXML);

    void remove(ComandoXML comandoXML);

    ComandoXML find(Object id);

    List<ComandoXML> findAll();
}
