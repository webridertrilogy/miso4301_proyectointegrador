/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TipoArchivo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ju-cort1
 */
@Stateless
public class TipoArchivoFacade implements TipoArchivoFacadeLocal {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(TipoArchivo tipoArchivo) {
        em.persist(tipoArchivo);
    }

    public void edit(TipoArchivo tipoArchivo) {
        em.merge(tipoArchivo);
    }

    public void remove(TipoArchivo tipoArchivo) {
        em.remove(em.merge(tipoArchivo));
    }

    public TipoArchivo find(Object id) {
        return em.find(TipoArchivo.class, id);
    }

    public List<TipoArchivo> findAll() {
        return em.createQuery("select object(o) from TipoArchivo as o").getResultList();
    }

    public TipoArchivo findByTipo(String tipo) {
        try
        {
            return (TipoArchivo) em.createNamedQuery("TipoArchivo.findByTipo").setParameter("tipo", tipo).getSingleResult();
         } catch (NoResultException e) {
            return null;
        }catch (NonUniqueResultException e1) {
            return (TipoArchivo)em.createNamedQuery("TipoArchivo.findByTipo").setParameter("tipo", tipo).getResultList().get(0);
        }
    }  
}
