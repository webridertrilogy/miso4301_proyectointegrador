/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_curso_planeado;
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
public class h_curso_planeadoFacade implements h_curso_planeadoFacadeLocal, h_curso_planeadoFacadeRemote {
    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_curso_planeado h_curso_planeado) {
        em.persist(h_curso_planeado);
    }

    public void edit(h_curso_planeado h_curso_planeado) {
        em.merge(h_curso_planeado);
    }

    public void remove(h_curso_planeado h_curso_planeado) {
        em.remove(em.merge(h_curso_planeado));
    }

    public h_curso_planeado find(Object id) {
        return em.find(h_curso_planeado.class, id);
    }

    public List<h_curso_planeado> findAll() {
        return em.createQuery("select object(o) from h_curso_planeado as o").getResultList();
    }

    public List<h_curso_planeado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_curso_planeado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_curso_planeado as o").getSingleResult()).intValue();
    }

}
