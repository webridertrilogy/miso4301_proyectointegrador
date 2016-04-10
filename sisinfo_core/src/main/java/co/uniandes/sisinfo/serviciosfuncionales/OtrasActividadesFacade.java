/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.OtrasActividades;
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
public class OtrasActividadesFacade implements OtrasActividadesFacadeLocal, OtrasActividadesFacadeRemote {
    @PersistenceContext(unitName = "CargaYCompromisoProfPU")
    private EntityManager em;

    public void create(OtrasActividades otrasActividades) {
        em.persist(otrasActividades);
    }

    public void edit(OtrasActividades otrasActividades) {
        em.merge(otrasActividades);
    }

    public void remove(OtrasActividades otrasActividades) {
        em.remove(em.merge(otrasActividades));
    }

    public OtrasActividades find(Object id) {
        return em.find(OtrasActividades.class, id);
    }

    public List<OtrasActividades> findAll() {
        return em.createQuery("select object(o) from OtrasActividades as o").getResultList();
    }

    public List<OtrasActividades> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from OtrasActividades as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from OtrasActividades as o").getSingleResult()).intValue();
    }

}
