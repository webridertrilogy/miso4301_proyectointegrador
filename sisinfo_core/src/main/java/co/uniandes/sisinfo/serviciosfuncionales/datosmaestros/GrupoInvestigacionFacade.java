/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.GrupoInvestigacion;
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
 * @author da-naran
 */
@Stateless
public class GrupoInvestigacionFacade implements GrupoInvestigacionFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(GrupoInvestigacion grupoInvestigacion) {
        em.persist(grupoInvestigacion);
    }

    public void edit(GrupoInvestigacion grupoInvestigacion) {
        em.merge(grupoInvestigacion);
    }

    public void remove(GrupoInvestigacion grupoInvestigacion) {
        em.remove(em.merge(grupoInvestigacion));
    }

    public GrupoInvestigacion find(Object id) {
        GrupoInvestigacion gi= em.find(GrupoInvestigacion.class, id);
        hibernateInitialize(gi);
        return gi;
    }

    public List<GrupoInvestigacion> findAll() {
        return em.createQuery("select object(o) from GrupoInvestigacion as o").getResultList();
    }

    public List<GrupoInvestigacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from GrupoInvestigacion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from GrupoInvestigacion as o").getSingleResult()).intValue();
    }

    @Override
    public GrupoInvestigacion findById(Long id) {
        try {
            GrupoInvestigacion gi = (GrupoInvestigacion) em.createNamedQuery("GrupoInvestigacion.findById").setParameter("id", id).getSingleResult();
            hibernateInitialize(gi);
            return gi;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (GrupoInvestigacion) em.createNamedQuery("GrupoInvestigacion.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    @Override
    public GrupoInvestigacion findByNombre(String nombre) {
        try {
            GrupoInvestigacion gi= (GrupoInvestigacion) em.createNamedQuery("GrupoInvestigacion.findByNombre").setParameter("nombre", nombre).getSingleResult();
            hibernateInitialize(gi);
            return gi;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (GrupoInvestigacion) em.createNamedQuery("GrupoInvestigacion.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }
    }

     /**
     * Inicializa la sección y sus colecciones
     * @param seccion Sección
     */
    private void hibernateInitialize(GrupoInvestigacion grupoI) {
        Hibernate.initialize(grupoI);

        /*
        if(seccion.getProfesorPrincipal()!=null){
            Hibernate.initialize(seccion.getProfesorPrincipal().getPersona());
        }
        Hibernate.initialize(seccion.getProfesores());
      
       */
    }
}
