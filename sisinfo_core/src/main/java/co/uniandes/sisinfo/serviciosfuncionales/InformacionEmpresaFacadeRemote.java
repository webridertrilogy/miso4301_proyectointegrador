package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.InformacionEmpresa;
import java.util.List;
import javax.ejb.Remote;

/**
 * Servicios fachada de la entidad Información Empresa (Interface remota)
 * @author Marcela Morales
 */
@Remote
public interface InformacionEmpresaFacadeRemote {

    void create(InformacionEmpresa informacionEmpresa);

    void edit(InformacionEmpresa informacionEmpresa);

    void remove(InformacionEmpresa informacionEmpresa);

    InformacionEmpresa find(Object id);

    List<InformacionEmpresa> findAll();
}
