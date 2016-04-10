/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CategoriaProyectoDeGrado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Stateless
public class CategoriaProyectoDeGradoFacade implements CategoriaProyectoDeGradoFacadeLocal, CategoriaProyectoDeGradoFacadeRemote {
    @PersistenceContext(unitName = "ProyectoDeGradoPregradoPU")
    private EntityManager em;

    public void create(CategoriaProyectoDeGrado categoriaProyectoDeGrado) {
        em.persist(categoriaProyectoDeGrado);
    }

    public void edit(CategoriaProyectoDeGrado categoriaProyectoDeGrado) {
        em.merge(categoriaProyectoDeGrado);
    }

    public void remove(CategoriaProyectoDeGrado categoriaProyectoDeGrado) {
        em.remove(em.merge(categoriaProyectoDeGrado));
    }

    public CategoriaProyectoDeGrado find(Object id) {
        return em.find(CategoriaProyectoDeGrado.class, id);
    }

    public List<CategoriaProyectoDeGrado> findAll() {
        return em.createQuery("select object(o) from CategoriaProyectoDeGrado as o").getResultList();
    }

    public List<CategoriaProyectoDeGrado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from CategoriaProyectoDeGrado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from CategoriaProyectoDeGrado as o").getSingleResult()).intValue();
    }

}
