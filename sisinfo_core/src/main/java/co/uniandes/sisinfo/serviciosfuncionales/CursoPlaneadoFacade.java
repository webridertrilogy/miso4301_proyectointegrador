/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CursoPlaneado;
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
public class CursoPlaneadoFacade implements CursoPlaneadoFacadeLocal, CursoPlaneadoFacadeRemote {
    @PersistenceContext(unitName = "CargaYCompromisoProfPU")
    private EntityManager em;

    public void create(CursoPlaneado cursoPlaneado) {
        em.persist(cursoPlaneado);
    }

    public void edit(CursoPlaneado cursoPlaneado) {
        em.merge(cursoPlaneado);
    }

    public void remove(CursoPlaneado cursoPlaneado) {
        em.remove(em.merge(cursoPlaneado));
    }

    public CursoPlaneado find(Object id) {
        return em.find(CursoPlaneado.class, id);
    }

    public List<CursoPlaneado> findAll() {
        return em.createQuery("select object(o) from CursoPlaneado as o").getResultList();
    }

    public List<CursoPlaneado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from CursoPlaneado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from CursoPlaneado as o").getSingleResult()).intValue();
    }

}
