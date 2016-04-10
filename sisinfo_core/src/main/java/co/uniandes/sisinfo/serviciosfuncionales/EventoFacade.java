/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Evento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Asistente
 */
@Stateless
public class EventoFacade implements EventoFacadeLocal, EventoFacadeRemote {
    @PersistenceContext(unitName = "CargaYCompromisoProfPU")
    private EntityManager em;

    public void create(Evento evento) {
        em.persist(evento);
    }

    public void edit(Evento evento) {
        em.merge(evento);
    }

    public void remove(Evento evento) {
        em.remove(em.merge(evento));
    }

    public Evento find(Object id) {
        return em.find(Evento.class, id);
    }

    public List<Evento> findAll() {
        return em.createQuery("select object(o) from Evento as o").getResultList();
    }

    public List<Evento> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Evento as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Evento as o").getSingleResult()).intValue();
    }

}
