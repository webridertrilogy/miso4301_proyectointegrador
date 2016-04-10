/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_curso;
import co.uniandes.sisinfo.historico.entities.h_seccion;
import java.util.Collection;
import java.util.Iterator;
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
 * @author david
 */
@Stateless
public class h_cursoFacade implements h_cursoFacadeLocal, h_cursoFacadeRemote {
    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_curso h_curso) {
        em.persist(h_curso);
    }

    public void edit(h_curso h_curso) {
        em.merge(h_curso);
    }

    public void remove(h_curso h_curso) {
        em.remove(em.merge(h_curso));
    }

    public h_curso find(Object id) {
        return em.find(h_curso.class, id);
    }

    public List<h_curso> findAll() {
        return em.createQuery("select object(o) from h_curso as o").getResultList();
    }

    public List<h_curso> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_curso as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_curso as o").getSingleResult()).intValue();
    }

    @Override
    public h_curso findByCRNSeccion(String crn) {
        try {
            h_curso curso = (h_curso) em.createNamedQuery("h_curso.findByCRN").setParameter("crn", crn).getSingleResult();
            hibernateInitialize(curso);
            return curso;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            h_curso curso = (h_curso) em.createNamedQuery("h_curso.findByCRN").setParameter("crn", crn).getResultList().get(0);
            hibernateInitialize(curso);
            return curso;
        }
    }

    public h_curso findBySeccionId(long id) {
        try {
            h_curso curso = (h_curso) em.createNamedQuery("h_curso.findBySeccionId").setParameter("id", id).getSingleResult();
            hibernateInitialize(curso);
            return curso;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            h_curso curso = (h_curso) em.createNamedQuery("h_curso.findById").setParameter("id", id).getResultList().get(0);
            hibernateInitialize(curso);
            return curso;
        }
    }


    /**
     * Inicializa el curso y sus colecciones
     * @param curso Curso
     */
    private void hibernateInitialize(h_curso curso) {
        Hibernate.initialize(curso);
        Collection<h_seccion> secciones = curso.getSecciones();
        Hibernate.initialize(secciones);
        for (Iterator<h_seccion> it = secciones.iterator(); it.hasNext();) {
            h_seccion seccion = it.next();
            Hibernate.initialize(seccion);
            Hibernate.initialize(seccion.getProfesor());
        }
    }


}
