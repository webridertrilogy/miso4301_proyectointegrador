/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_seccion;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 *
 * @author david
 */
@Stateless
public class h_seccionFacade implements h_seccionFacadeLocal, h_seccionFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_seccion h_seccion) {
        em.persist(h_seccion);
    }

    public void edit(h_seccion h_seccion) {
        em.merge(h_seccion);
    }

    public void remove(h_seccion h_seccion) {
        em.remove(em.merge(h_seccion));
    }

    public h_seccion find(Object id) {
        return em.find(h_seccion.class, id);
    }

    public List<h_seccion> findAll() {
        return em.createQuery("select object(o) from h_seccion as o").getResultList();
    }

    public List<h_seccion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_seccion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_seccion as o").getSingleResult()).intValue();
    }

    public List<h_seccion> findByCorreoProfesor(String correo) {
        List<h_seccion> secciones = em.createNamedQuery("h_seccion.findByCorreoProfesor").setParameter("correo", correo).getResultList();
        Iterator<h_seccion> iterator = secciones.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return secciones;
    }

    public List<h_seccion> findByPeriodo(String periodo) {
        List<h_seccion> secciones = em.createNamedQuery("h_seccion.findByPeriodo").setParameter("periodo", periodo).getResultList();
        Iterator<h_seccion> iterator = secciones.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return secciones;
    }

    /**
     * Inicializa la sección y sus colecciones
     * @param seccion Sección
     */
    private void hibernateInitialize(h_seccion seccion) {
        Hibernate.initialize(seccion);
        Hibernate.initialize(seccion.getProfesor());
    }
}
