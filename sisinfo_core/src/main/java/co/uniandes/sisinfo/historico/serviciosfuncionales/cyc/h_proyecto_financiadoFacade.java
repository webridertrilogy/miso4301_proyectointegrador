/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_cargaProfesor_cyc;
import co.uniandes.sisinfo.historico.entities.cyc.h_proyecto_financiado;
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
public class h_proyecto_financiadoFacade implements h_proyecto_financiadoFacadeLocal, h_proyecto_financiadoFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_proyecto_financiado h_proyecto_financiado) {
        em.persist(h_proyecto_financiado);
    }

    public void edit(h_proyecto_financiado h_proyecto_financiado) {
        em.merge(h_proyecto_financiado);
    }

    public void remove(h_proyecto_financiado h_proyecto_financiado) {
        em.remove(em.merge(h_proyecto_financiado));
    }

    public h_proyecto_financiado find(Object id) {
        return em.find(h_proyecto_financiado.class, id);
    }

    public List<h_proyecto_financiado> findAll() {
        return em.createQuery("select object(o) from h_proyecto_financiado as o").getResultList();
    }

    public List<h_proyecto_financiado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_proyecto_financiado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_proyecto_financiado as o").getSingleResult()).intValue();
    }

    public h_proyecto_financiado findByName(String nombre) {
        try {
            h_proyecto_financiado bau = (h_proyecto_financiado) em.createNamedQuery("h_proyecto_financiado.findByNombre").setParameter("nombre", nombre).getSingleResult();
            hibernateInitialize(bau);
            return bau;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            h_proyecto_financiado bau = (h_proyecto_financiado) em.createNamedQuery("h_proyecto_financiado.findByNombre").setParameter("nombre", nombre).getResultList();
            hibernateInitialize(bau);
            return bau;


        }
    }

    private void hibernateInitialize(h_proyecto_financiado p) {
        Hibernate.initialize(p);
        Collection<h_cargaProfesor_cyc> cargas = p.getProfesores();
        for (h_cargaProfesor_cyc profesor_cyc : cargas) {
            Hibernate.initialize(profesor_cyc);
        }
       

    }

    public h_proyecto_financiado findByNombreEntidadDescripcionYPeriodo(String nombre, String entidad, String descripcion0, String periodo) {
         try {
            h_proyecto_financiado bau = (h_proyecto_financiado) em.createNamedQuery("h_proyecto_financiado.findByNombreEntidadDescripcionYPeriodo").setParameter("nombre", nombre)
                    .setParameter("entidad", entidad).setParameter("descripcion", descripcion0).setParameter("periodo", periodo).getSingleResult();
            hibernateInitialize(bau);
            return bau;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            h_proyecto_financiado bau = (h_proyecto_financiado) em.createNamedQuery("h_proyecto_financiado.findByNombre").setParameter("nombre", nombre).getResultList();
            hibernateInitialize(bau);
            return bau;
        }
    }
}
