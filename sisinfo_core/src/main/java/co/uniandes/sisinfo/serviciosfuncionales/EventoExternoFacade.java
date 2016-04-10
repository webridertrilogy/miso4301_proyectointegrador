/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Contacto;
import co.uniandes.sisinfo.entities.EventoExterno;
import co.uniandes.sisinfo.entities.InscripcionEventoExterno;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class EventoExternoFacade extends AbstractFacade<EventoExterno> implements EventoExternoFacadeLocal, EventoExternoFacadeRemote {

    @PersistenceContext(unitName = "ContactosCrmPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public EventoExternoFacade() {
        super(EventoExterno.class);
    }

    public Collection<InscripcionEventoExterno> findInscritosByIdEvento(Long id) {
        List<InscripcionEventoExterno> temp = em.createNamedQuery("EventoExterno.findInscritosByIdEvento").setParameter("id", id).getResultList();
        return temp;
    }

    public Collection<EventoExterno> findEventosByIdInscripcion(Long id) {
        Collection<EventoExterno> temp = em.createNamedQuery("EventoExterno.findEventosByIdInscripcion").setParameter("id", id).getResultList();
        return temp;
    }

    public Collection<EventoExterno> findEventosByIdCategoria(Long id) {
        Collection<EventoExterno> temp = em.createNamedQuery("EventoExterno.findEventosByIdCategoria").setParameter("id", id).getResultList();
        return temp;
    }

    public Collection<InscripcionEventoExterno> findInscritoByIdEventoAndIdContacto(Long idEvento, Long idContacto) {
        Query q = em.createNamedQuery("EventoExterno.findInscritoByIdEventoAndIdContacto");
        q = q.setParameter("idEvento", idEvento);
        q = q.setParameter("idContacto", idContacto);
        return q.getResultList();
    }

    public EventoExterno findEventosByTitulo(String titulo) {
        Query nq = em.createNamedQuery("EventoExterno.findEventosByTitulo");
        nq.setParameter("titulo", titulo);
        try {
            return (EventoExterno) nq.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return (EventoExterno) nq.getResultList().get(0);
        }

    }

    public Collection<InscripcionEventoExterno> findContactosEventoExterno(Long idEvento,String nombre, String apellidos, String id, String cargo, String empresa, String ciudad, String celular, String correo, String sector, java.util.Date fecha) {

        try {
            String jpql = "SELECT i FROM EventoExterno e left join e.inscripciones i left join i.contacto c where e.id = "+ idEvento + " ";
            if (nombre != null && !nombre.equals("")) {
                jpql += " AND c.nombres like  '%" + nombre + "%'";
            }
            if (apellidos != null && !apellidos.equals("")) {
                jpql += " AND c.apellidos like  '%" + apellidos + "%'";
            }
            if (id != null && !id.equals("")) {
                jpql += " AND c.numeroIdentificacion like  '%" + id + "%'";
            }
            if (cargo != null && !cargo.equals("")) {
                jpql += " AND c.cargo.nombre like  '%" + cargo + "%'";
            }
            if (empresa != null && !empresa.equals("")) {
                jpql += " AND c.Empresa like  '%" + empresa + "%'";
            }
            if (ciudad != null && !ciudad.equals("")) {
                jpql += " AND c.ciudad like  '%" + ciudad + "%'";
            }
            if (celular != null && !celular.equals("")) {
                jpql += " AND c.celular like  '%" + celular + "%'";
            }
            if (correo != null && !correo.equals("")) {
                jpql += " AND c.correo like  '%" + correo + "%'";
            }
            if (sector != null && !sector.equals("")) {
                jpql += " AND c.sector.nombre like  '%" + sector + "%'";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (fecha != null) {
                jpql += " AND c.fechaActualizacion like '" + sdf.format(fecha) + "'";
            }
            return em.createQuery(jpql).getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public Collection<EventoExterno> findEventosByEstado(String estado) {
        Collection<EventoExterno> temp = em.createNamedQuery("EventoExterno.findEventosByEstado").setParameter("estado", estado).getResultList();
        return temp;

    }
}
