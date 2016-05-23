/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import co.uniandes.sisinfo.entities.SubareaInvestigacion;

/**
 *
 * @author Asistente
 */
@Stateless
public class SubareaInvestigacionFacade implements SubareaInvestigacionFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(SubareaInvestigacion subareaInvestigacion) {
        em.persist(subareaInvestigacion);
    }

    public void edit(SubareaInvestigacion subareaInvestigacion) {
        em.merge(subareaInvestigacion);
    }

    public void remove(SubareaInvestigacion subareaInvestigacion) {
        em.remove(em.merge(subareaInvestigacion));
    }

    public SubareaInvestigacion find(Object id) {
        return em.find(SubareaInvestigacion.class, id);
    }

    public List<SubareaInvestigacion> findAll() {
        return em.createQuery("select object(o) from SubareaInvestigacion as o").getResultList();
    }

    public List<SubareaInvestigacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from SubareaInvestigacion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from SubareaInvestigacion as o").getSingleResult()).intValue();
    }

    public SubareaInvestigacion findByNombreSubarea(String nombre) {
        try {
            SubareaInvestigacion subarea = (SubareaInvestigacion) em.createNamedQuery("SubareaInvestigacion.findByNombre").setParameter("nombre", nombre).getSingleResult();
            hibernateInitialize(subarea);
            return subarea;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            SubareaInvestigacion subarea = (SubareaInvestigacion) em.createNamedQuery("SubareaInvestigacion.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
            hibernateInitialize(subarea);
            return subarea;
        }
    }

    /**
     * Inicializa la subarea y su director
     * @param SubareaInvestigacion SubareaInvestigacion
     */
    private void hibernateInitialize(SubareaInvestigacion subarea) {
        Hibernate.initialize(subarea);
        if (subarea.getCoordinadorSubarea() != null) {
            Hibernate.initialize(subarea.getCoordinadorSubarea());
        }
    }
}
