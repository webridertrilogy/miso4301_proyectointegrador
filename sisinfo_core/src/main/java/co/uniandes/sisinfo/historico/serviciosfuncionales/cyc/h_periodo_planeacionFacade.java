/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_cargaProfesor_cyc;
import co.uniandes.sisinfo.historico.entities.cyc.h_periodo_planeacion;

import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 *
 * @author Asistente
 */
@Stateless
public class h_periodo_planeacionFacade implements h_periodo_planeacionFacadeLocal, h_periodo_planeacionFacadeRemote {

    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_periodo_planeacion h_periodo_planeacion) {
        em.persist(h_periodo_planeacion);
    }

    public void edit(h_periodo_planeacion h_periodo_planeacion) {
        em.merge(h_periodo_planeacion);
    }

    public void remove(h_periodo_planeacion h_periodo_planeacion) {
        em.remove(em.merge(h_periodo_planeacion));
    }

    public h_periodo_planeacion find(Object id) {
        h_periodo_planeacion temp = em.find(h_periodo_planeacion.class, id);
        hibernateInitialize(temp);
        return temp;
    }

    public List<h_periodo_planeacion> findAll() {
        List<h_periodo_planeacion> bau = em.createQuery("select object(o) from h_periodo_planeacion as o").getResultList();
        for (h_periodo_planeacion object : bau) {
            hibernateInitialize(object);
        }
        return bau;
    }

    public List<h_periodo_planeacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_periodo_planeacion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_periodo_planeacion as o").getSingleResult()).intValue();
    }

    private void hibernateInitialize(h_periodo_planeacion p) {

        Hibernate.initialize(p);
        Collection<h_cargaProfesor_cyc> cargas = p.getCargaProfesores();
        if (cargas != null) {
            for (h_cargaProfesor_cyc e : cargas) {
                Hibernate.initialize(e);
            }
        }
    }

    public h_periodo_planeacion findByNombre(String periodo) {
        try {
            h_periodo_planeacion temp = (h_periodo_planeacion) em.createNamedQuery("h_periodo_planeacion.findByPeriodo").setParameter("periodo", periodo).getSingleResult();
            if (temp != null) {
                hibernateInitialize(temp);
            }
            return temp;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            h_periodo_planeacion temp = (h_periodo_planeacion) em.createNamedQuery("h_periodo_planeacion.findByPeriodo").setParameter("periodo", periodo).getResultList().get(0);
            if (temp != null) {
                hibernateInitialize(temp);
            }
            return temp;
        }
    }
}
