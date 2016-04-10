/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.EventoExterno;
import co.uniandes.sisinfo.entities.InscripcionEventoExterno;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Administrador
 */
@Remote
public interface EventoExternoFacadeRemote {

    void create(EventoExterno eventoExterno);

    void edit(EventoExterno eventoExterno);

    void remove(EventoExterno eventoExterno);

    EventoExterno find(Object id);

    List<EventoExterno> findAll();

    List<EventoExterno> findRange(int[] range);

    Collection<InscripcionEventoExterno> findInscritosByIdEvento(Long idEvento);

    Collection<EventoExterno> findEventosByIdInscripcion(Long id);

    Collection<EventoExterno> findEventosByIdCategoria(Long id);

    int count();

    Collection<InscripcionEventoExterno> findInscritoByIdEventoAndIdContacto(Long idEvento, Long idContacto);

    EventoExterno findEventosByTitulo(String titulo);

    Collection<EventoExterno> findEventosByEstado(String estado);
}
