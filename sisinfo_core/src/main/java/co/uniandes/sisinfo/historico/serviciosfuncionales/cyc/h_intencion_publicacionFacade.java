/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_intencion_publicacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Asistente
 */
@Stateless
public class h_intencion_publicacionFacade implements h_intencion_publicacionFacadeLocal, h_intencion_publicacionFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_intencion_publicacion h_intencion_publicacion) {
        em.persist(h_intencion_publicacion);
    }

    public void edit(h_intencion_publicacion h_intencion_publicacion) {
        em.merge(h_intencion_publicacion);
    }

    public void remove(h_intencion_publicacion h_intencion_publicacion) {
        em.remove(em.merge(h_intencion_publicacion));
    }

    public h_intencion_publicacion find(Object id) {
        return em.find(h_intencion_publicacion.class, id);
    }

    public List<h_intencion_publicacion> findAll() {
        return em.createQuery("select object(o) from h_intencion_publicacion as o").getResultList();
    }

    public List<h_intencion_publicacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_intencion_publicacion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_intencion_publicacion as o").getSingleResult()).intValue();
    }

    public h_intencion_publicacion findByTituloObservacionesTipo(String tituloPublicacion, String observaciones, String tipoPublicacion)
    {
       Query q = em.createNamedQuery("h_intencion_publicacion.findByTituloObservacionesTipo").setParameter("titulo",tituloPublicacion).setParameter("observaciones",observaciones).setParameter("tipo", tipoPublicacion);
        try {
            h_intencion_publicacion temp = (h_intencion_publicacion) q.getSingleResult();
            return temp;
        } catch (NonUniqueResultException e) {
            return (h_intencion_publicacion) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }

}
