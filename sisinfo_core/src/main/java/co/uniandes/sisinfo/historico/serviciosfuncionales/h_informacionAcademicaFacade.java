/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_informacionAcademica;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author david
 */
@Stateless
public class h_informacionAcademicaFacade implements h_informacionAcademicaFacadeLocal, h_informacionAcademicaFacadeRemote {
    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_informacionAcademica h_informacionAcademica) {
        em.persist(h_informacionAcademica);
    }

    public void edit(h_informacionAcademica h_informacionAcademica) {
        em.merge(h_informacionAcademica);
    }

    public void remove(h_informacionAcademica h_informacionAcademica) {
        em.remove(em.merge(h_informacionAcademica));
    }

    public h_informacionAcademica find(Object id) {
        return em.find(h_informacionAcademica.class, id);
    }

    public List<h_informacionAcademica> findAll() {
        return em.createQuery("select object(o) from h_informacionAcademica as o").getResultList();
    }

    public List<h_informacionAcademica> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_informacionAcademica as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_informacionAcademica as o").getSingleResult()).intValue();
    }

}
