/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Grupo;
import co.uniandes.sisinfo.serviciosfuncionales.GrupoFacadeRemote;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author da-naran
 */
@Stateless
public class GrupoFacade implements GrupoFacadeLocal, GrupoFacadeRemote {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Grupo grupo) {
        em.persist(grupo);
    }

    public void edit(Grupo grupo) {
        em.merge(grupo);
    }

    public void remove(Grupo grupo) {
        em.remove(em.merge(grupo));
    }

    public Grupo find(Object id) {
        return em.find(Grupo.class, id);
    }

    public List<Grupo> findAll() {
        return em.createQuery("select object(o) from Grupo as o").getResultList();
    }

    public Grupo findByNombre(String nombre) {
        try
        {
            Grupo grupo = (Grupo) em.createNamedQuery("Grupo.findByNombre").setParameter("nombre", nombre).getSingleResult();
            return grupo;
        }catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Grupo grupo = (Grupo) em.createNamedQuery("Grupo.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
            return grupo;
        }
    }

    public List<Grupo> findByCorreoDuenho(String correo) {

          List<Grupo> grupos= em.createNamedQuery("Grupo.findByCorreoDuenho").setParameter("correo", correo).getResultList();
            return grupos;

    }


}
