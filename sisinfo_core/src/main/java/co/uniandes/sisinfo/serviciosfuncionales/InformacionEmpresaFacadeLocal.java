package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.InformacionEmpresa;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada de la entidad Información Empresa (Interface local)
 * @author Marcela Morales
 */
@Local
public interface InformacionEmpresaFacadeLocal {

    void create(InformacionEmpresa informacionEmpresa);

    void edit(InformacionEmpresa informacionEmpresa);

    void remove(InformacionEmpresa informacionEmpresa);

    InformacionEmpresa find(Object id);

    List<InformacionEmpresa> findAll();
}
