/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TipoArchivo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ju-cort1
 */
@Local
public interface TipoArchivoFacadeLocal {

    void create(TipoArchivo tipoArchivo);

    void edit(TipoArchivo tipoArchivo);

    void remove(TipoArchivo tipoArchivo);

    TipoArchivo find(Object id);

    List<TipoArchivo> findAll();

    TipoArchivo findByTipo(String tipo);

}
