package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Encargado;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachade de la entidad Encargado (Interface Local)
 * @author Marcela Morales
 */
@Local
public interface EncargadoFacadeLocal {

    void create(Encargado encargado);

    void edit(Encargado encargado);

    void remove(Encargado encargado);

    Encargado find(Object id);

    List<Encargado> findAll();

    List<Encargado> findRange(int[] range);

    int count();

    Encargado findByCorreo(String correo);

}
