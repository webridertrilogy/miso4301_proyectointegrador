/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Contacto;
import co.uniandes.sisinfo.entities.EventoExterno;
import co.uniandes.sisinfo.entities.InscripcionEventoExterno;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface EventoExternoFacadeLocal {

    void create(EventoExterno eventoExterno);

    void edit(EventoExterno eventoExterno);

    void remove(EventoExterno eventoExterno);

    EventoExterno find(Object id);

    List<EventoExterno> findAll();

    List<EventoExterno> findRange(int[] range);

    Collection<InscripcionEventoExterno> findInscritosByIdEvento(Long idEvento);

    Collection<EventoExterno> findEventosByIdInscripcion(Long id);

    Collection<EventoExterno> findEventosByIdCategoria(Long id);

    Collection<InscripcionEventoExterno> findInscritoByIdEventoAndIdContacto(Long idEvento, Long idContacto);

    int count();

    EventoExterno findEventosByTitulo(String titulo);

    Collection<InscripcionEventoExterno> findContactosEventoExterno(Long idEvento, String nombre, String apellidos, String id, String cargo, String empresa, String ciudad, String celular, String correo, String sector, java.util.Date fecha);

    Collection<EventoExterno> findEventosByEstado(String estado);
}
