/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import co.uniandes.sisinfo.entities.soporte.TipoDocumento;
import co.uniandes.sisinfo.entities.soporte.TipoDocumento;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author lj.bautista31
 */
@Remote
public interface TipoDocumentoFacadeRemote {

    void create(TipoDocumento tipoDocumento);

    void edit(TipoDocumento tipoDocumento);

    void remove(TipoDocumento tipoDocumento);

    TipoDocumento find(Object id);

    List<TipoDocumento> findAll();

    TipoDocumento findByTipo(String tipo);

    TipoDocumento findByDescripcion(String descripcion);

}
