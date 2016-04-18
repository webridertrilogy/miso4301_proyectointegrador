/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CargaProfesor;
import co.uniandes.sisinfo.entities.PeriodoPlaneacion;
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
public class PeriodoPlaneacionFacade implements PeriodoPlaneacionFacadeLocal, PeriodoPlaneacionFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(PeriodoPlaneacion periodoPlaneacion) {
        em.persist(periodoPlaneacion);
    }

    public void edit(PeriodoPlaneacion periodoPlaneacion) {
        em.merge(periodoPlaneacion);
    }

    public void remove(PeriodoPlaneacion periodoPlaneacion) {
        em.remove(em.merge(periodoPlaneacion));
    }

    public PeriodoPlaneacion find(Object id) {
        PeriodoPlaneacion p = em.find(PeriodoPlaneacion.class, id);
        hibernateInitialize(p);
        return p;
    }

    public List<PeriodoPlaneacion> findAll() {
        List<PeriodoPlaneacion> lista = em.createQuery("select object(o) from PeriodoPlaneacion as o").getResultList();
        for (PeriodoPlaneacion periodoPlaneacion : lista) {
            hibernateInitialize(periodoPlaneacion);
        }
        return lista;
    }

    public List<PeriodoPlaneacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from PeriodoPlaneacion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from PeriodoPlaneacion as o").getSingleResult()).intValue();
    }

    public PeriodoPlaneacion darUltimoPeriodoPlaneacion() {
        Collection<PeriodoPlaneacion> temp = findAll();
        PeriodoPlaneacion temporal=null;
        for (PeriodoPlaneacion periodoPlaneacion : temp) {
            temporal=periodoPlaneacion;
        }
        return temporal;
    }

    private void hibernateInitialize(PeriodoPlaneacion p) {

        Hibernate.initialize(p);
        Collection<CargaProfesor> cargas = p.getCargaProfesores();
        for (CargaProfesor e : cargas) {
            Hibernate.initialize(e);
            Hibernate.initialize(e.getProfesor());
            if (e.getProfesor() != null) {
                Hibernate.initialize(e.getProfesor().getPersona());
            }
        }


    }

    public PeriodoPlaneacion findByNombre(String periodo) {
        try {
            PeriodoPlaneacion temp = (PeriodoPlaneacion) em.createNamedQuery("PeriodoPlaneacion.findByPeriodo").setParameter("periodo", periodo).getSingleResult();
            if (temp != null) {
                hibernateInitialize(temp);
            }
            return temp;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
             PeriodoPlaneacion temp = (PeriodoPlaneacion) em.createNamedQuery("PeriodoPlaneacion.findByPeriodo").setParameter("periodo", periodo).getResultList().get(0);
            if (temp != null) {
                hibernateInitialize(temp);
            }
            return temp;
        }


    }
}
