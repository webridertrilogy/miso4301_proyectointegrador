/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 * Servicios Entidad Tarea
 */
@Stateless
@EJB(name = "TareaSencillaFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeLocal.class)
public class TareaSencillaFacade implements TareaSencillaFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(TareaSencilla tarea) {
        em.persist(tarea);
    }

    @Override
    public void edit(TareaSencilla tarea) {
        em.merge(tarea);
    }

    @Override
    public void remove(TareaSencilla tarea) {
        em.remove(em.merge(tarea));
    }

    @Override
    public TareaSencilla find(Object id) {
        return em.find(TareaSencilla.class, id);
    }

    @Override
    public List<TareaSencilla> findAll() {
        List<TareaSencilla> tareas = em.createQuery("select object(o) from TareaSencilla as o").getResultList();
        for (TareaSencilla tarea : tareas) {
            inicializar(tarea);
        }
        return tareas;
    }

    @Override
    public TareaSencilla findById(Long id) {
        try {
            TareaSencilla tarea = (TareaSencilla) em.createNamedQuery("TareaSencilla.findById").setParameter("id", id).getSingleResult();
            inicializar(tarea);
            return tarea;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            TareaSencilla tarea = (TareaSencilla) em.createNamedQuery("TareaSencilla.findById").setParameter("id", id).getResultList().get(0);
            return tarea;
        }
    }

    @Override
    public Collection<TareaSencilla> findByTipo(String tipo){
        try {
            Collection<TareaSencilla> tareas =(Collection<TareaSencilla>) em.createNamedQuery("TareaSencilla.findByTipo").setParameter("tipo", tipo).getResultList();
            for(TareaSencilla tarea: tareas){
                inicializar(tarea);
            }
            return tareas;
        } catch (NoResultException nre) {
            return new ArrayList();
        }
    }

    private void inicializar(TareaSencilla tareaS){
        Hibernate.initialize(tareaS);
        for (Parametro parametro : tareaS.getParametros()) {
            Hibernate.initialize(parametro);
        }
    }

}
