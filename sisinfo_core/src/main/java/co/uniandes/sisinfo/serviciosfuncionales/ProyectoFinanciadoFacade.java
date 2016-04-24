/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CargaProfesor;
import co.uniandes.sisinfo.entities.ProyectoFinanciado;
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
public class ProyectoFinanciadoFacade implements ProyectoFinanciadoFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(ProyectoFinanciado proyectoFinanciado) {
        em.persist(proyectoFinanciado);
    }

    public void edit(ProyectoFinanciado proyectoFinanciado) {
        em.merge(proyectoFinanciado);
    }

    public void remove(ProyectoFinanciado proyectoFinanciado) {
        em.remove(em.merge(proyectoFinanciado));
    }

    public ProyectoFinanciado find(Object id) {
        ProyectoFinanciado proyectoFinanciado= em.find(ProyectoFinanciado.class, id);
        hibernateInitialize(proyectoFinanciado);
        return proyectoFinanciado;
    }

    public List<ProyectoFinanciado> findAll() {
        List<ProyectoFinanciado> lista= em.createQuery("select object(o) from ProyectoFinanciado as o").getResultList();
             for (ProyectoFinanciado proyectoFinanciado : lista) {
             hibernateInitialize(proyectoFinanciado);
        }
             return lista;

    }

    public List<ProyectoFinanciado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from ProyectoFinanciado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from ProyectoFinanciado as o").getSingleResult()).intValue();
    }

    public ProyectoFinanciado findByName(String nombre) {

        try {
            return (ProyectoFinanciado) em.createNamedQuery("ProyectoFinanciado.findByNombre").setParameter("nombre", nombre).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (ProyectoFinanciado) em.createNamedQuery("ProyectoFinanciado.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }
    }

    private void hibernateInitialize(CargaProfesor e) {
        Hibernate.initialize(e);
        Hibernate.initialize(e.getProfesor());
         if (e.getProfesor() != null) {
            Hibernate.initialize(e.getProfesor().getPersona());
        }
    }
    private void hibernateInitialize(ProyectoFinanciado p)
    {
        Hibernate.initialize(p);
        Collection<CargaProfesor> cargas= p.getProfesores();
         for (CargaProfesor cargaProfesor : cargas) {
            hibernateInitialize(cargaProfesor);
        }

    }




}
