/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.MaterialBibliografico;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Marcela
 */
@Remote
public interface MaterialBibliograficoFacadeRemote {

    void create(MaterialBibliografico materialBibliografico);

    void edit(MaterialBibliografico materialBibliografico);

    void remove(MaterialBibliografico materialBibliografico);

    MaterialBibliografico find(Object id);

    List<MaterialBibliografico> findAll();

}
