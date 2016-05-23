package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.PersonaSoporte;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Juan Manuel Moreno B.
 */
@Local
public interface PersonaSoporteFacadeLocal {

    void create(PersonaSoporte personaSoporte);

    void edit(PersonaSoporte personaSoporte);

    void remove(PersonaSoporte personaSoporte);

    PersonaSoporte find(Object id);

    List<PersonaSoporte> findAll();

    List<PersonaSoporte> findRange(int[] range);

    int count();

    /**
     * Encuentra una persona de soporte por su correo.
     * @param id
     * @return
     */
     PersonaSoporte findByEmail(String email);
}
