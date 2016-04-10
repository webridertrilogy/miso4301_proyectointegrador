/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CursoTesis;
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
public class CursoTesisFacade implements CursoTesisFacadeLocal, CursoTesisFacadeRemote {
    @PersistenceContext(unitName = "TesisMaestriaPU")
    private EntityManager em;

    public void create(CursoTesis cursoTesis) {
        em.persist(cursoTesis);
    }

    public void edit(CursoTesis cursoTesis) {
        em.merge(cursoTesis);
    }

    public void remove(CursoTesis cursoTesis) {
        em.remove(em.merge(cursoTesis));
    }

    public CursoTesis find(Object id) {
        return em.find(CursoTesis.class, id);
    }

    public List<CursoTesis> findAll() {
        return em.createQuery("select object(o) from CursoTesis as o").getResultList();
    }

    public List<CursoTesis> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from CursoTesis as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from CursoTesis as o").getSingleResult()).intValue();
    }

}
