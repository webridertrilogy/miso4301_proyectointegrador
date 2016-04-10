/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Archivo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 * Servicio Facade de Archivo
 */
@Stateless
public class ArchivoFacade implements ArchivoFacadeLocal, ArchivoFacadeRemote {
    @PersistenceContext
    private EntityManager em;

    public void create(Archivo archivo) {
        em.persist(archivo);
    }

    public void edit(Archivo archivo) {
        em.merge(archivo);
    }

    public void remove(Archivo archivo) {
        em.remove(em.merge(archivo));
    }

    public Archivo find(Object id) {
        return em.find(Archivo.class, id);
    }

    public List<Archivo> findAll() {

        List<Archivo> archivos = em.createQuery("select object(o) from Archivo as o").getResultList();
        for(Archivo archivo : archivos) {

            hibernateInitialize(archivo);
        }
        return archivos;
    }

    @Override
    public Archivo findBySeccionAndTipo(String crn, String tipo) {
        
        Query q = em.createNamedQuery("Archivo.findBySeccionAndTipo");
        try
        {            
            q.setParameter("crn", crn);
            q.setParameter("tipo", tipo);
            return (Archivo) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }catch (NonUniqueResultException e1) {
            return (Archivo) q.getResultList().get(0);
        }
    }

    @Override
    public Archivo findById(Long id) {

        Query q = em.createNamedQuery("Archivo.findById");
        try
        {
            q.setParameter("id", id);
            return (Archivo) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }catch (NonUniqueResultException e1) {
            return (Archivo) q.getResultList().get(0);
        }
    }

    public List<Archivo> findBySeccion (String crn) {

        Query q = em.createNamedQuery("Archivo.findBySeccion");
        try
        {
            q.setParameter("crn", crn);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Inicializa la sección y sus colecciones
     * @param seccion Sección
     */
    private void hibernateInitialize(Archivo archivo) {
        
        Hibernate.initialize(archivo);
        Hibernate.initialize(archivo.getSeccion().getCrn());
    }
}
