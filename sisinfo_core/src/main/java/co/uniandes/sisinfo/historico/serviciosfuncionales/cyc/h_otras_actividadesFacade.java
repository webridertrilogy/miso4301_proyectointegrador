/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_otras_actividades;
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
public class h_otras_actividadesFacade implements h_otras_actividadesFacadeLocal, h_otras_actividadesFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_otras_actividades h_otras_actividades) {
        em.persist(h_otras_actividades);
    }

    public void edit(h_otras_actividades h_otras_actividades) {
        em.merge(h_otras_actividades);
    }

    public void remove(h_otras_actividades h_otras_actividades) {
        em.remove(em.merge(h_otras_actividades));
    }

    public h_otras_actividades find(Object id) {
        return em.find(h_otras_actividades.class, id);
    }

    public List<h_otras_actividades> findAll() {
        return em.createQuery("select object(o) from h_otras_actividades as o").getResultList();
    }

    public List<h_otras_actividades> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_otras_actividades as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_otras_actividades as o").getSingleResult()).intValue();
    }

}
