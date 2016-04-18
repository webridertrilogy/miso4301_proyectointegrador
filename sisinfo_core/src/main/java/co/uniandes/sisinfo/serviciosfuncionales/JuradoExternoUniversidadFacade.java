/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.JuradoExternoUniversidad;
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
public class JuradoExternoUniversidadFacade implements JuradoExternoUniversidadFacadeLocal, JuradoExternoUniversidadFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(JuradoExternoUniversidad juradoExternoUniversidad) {
        em.persist(juradoExternoUniversidad);
    }

    public void edit(JuradoExternoUniversidad juradoExternoUniversidad) {
        em.merge(juradoExternoUniversidad);
    }

    public void remove(JuradoExternoUniversidad juradoExternoUniversidad) {
        em.remove(em.merge(juradoExternoUniversidad));
    }

    public JuradoExternoUniversidad find(Object id) {
        return em.find(JuradoExternoUniversidad.class, id);
    }

    public List<JuradoExternoUniversidad> findAll() {
        return em.createQuery("select object(o) from JuradoExternoUniversidad as o").getResultList();
    }

    public List<JuradoExternoUniversidad> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from JuradoExternoUniversidad as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from JuradoExternoUniversidad as o").getSingleResult()).intValue();
    }

    public JuradoExternoUniversidad findByCorreo(String correo) {
         Query q = em.createNamedQuery("JuradoExternoUniversidad.findBycorreo").setParameter("correo", correo);
        try {
            JuradoExternoUniversidad temp = (JuradoExternoUniversidad) q.getSingleResult();

            return temp;
        } catch (NonUniqueResultException e) {
            return (JuradoExternoUniversidad) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }



}
