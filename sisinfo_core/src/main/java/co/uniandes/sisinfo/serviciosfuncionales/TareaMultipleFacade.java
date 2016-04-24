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

import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 * Servicios Entidad Tarea
 */
@Stateless
@EJB(name = "TareaMultipleFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeLocal.class)
public class TareaMultipleFacade implements TareaMultipleFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(TareaMultiple tarea) {
        em.persist(tarea);
    }

    @Override
    public void edit(TareaMultiple tarea) {
        em.merge(tarea);
    }

    @Override
    public void remove(TareaMultiple tarea) {
        em.remove(em.merge(tarea));
    }

    @Override
    public TareaMultiple find(Object id) {
        return em.find(TareaMultiple.class, id);
    }

    @Override
    public List<TareaMultiple> findAll() {
        List<TareaMultiple> tareas = em.createQuery("select object(o) from TareaMultiple as o").getResultList();
        return tareas;
    }

    @Override
    public TareaMultiple findById(Long id) {
        try {
            TareaMultiple tarea = (TareaMultiple) em.createNamedQuery("TareaMultiple.findById").setParameter("id", id).getSingleResult();
            Hibernate.initialize(tarea);
            return tarea;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            TareaMultiple tarea = (TareaMultiple) em.createNamedQuery("TareaMultiple.findById").setParameter("id", id).getResultList().get(0);
            return tarea;
        }
    }

    @Override
    public Collection<TareaMultiple> findByCorreoYTipo(String tipo,String correo){
        try{
            Collection<TareaMultiple> tareas = (Collection<TareaMultiple>) em.createNamedQuery("TareaMultiple.findByCorreoYTipo").setParameter("tipo", tipo).setParameter("correo", correo).getResultList();
            for (TareaMultiple tareaMultiple : tareas) {
                initialize(tareaMultiple);
            }
            return tareas;
        }catch(NoResultException nre){
            return new ArrayList();
        }
    }

    @Override
    public Collection<TareaMultiple> findByRolYTipo(String tipo, String rol){
        try {
            Collection<TareaMultiple> tareas = (Collection<TareaMultiple>) em.createNamedQuery("TareaMultiple.findByRolYTipo").setParameter("tipo", tipo).setParameter("rol", rol).getResultList();
            for (TareaMultiple tareaMultiple : tareas) {
                initialize(tareaMultiple);
            }
            return tareas;
        } catch (NoResultException nre) {
            return new ArrayList();
        }
    }

    @Override
    public Collection<TareaMultiple> findByRolYTipoNoPersonal(String tipo, String rol){
        try {
            Collection<TareaMultiple> tareas =(Collection<TareaMultiple>) em.createNamedQuery("TareaMultiple.findByRolYTipoNoPersonal").setParameter("tipo", tipo).setParameter("rol", rol).getResultList();
            for (TareaMultiple tareaMultiple : tareas) {
                initialize(tareaMultiple);
            }
            return tareas;
        } catch (NoResultException nre) {
            return new ArrayList();
        }
    }

    @Override
    public Collection<TareaMultiple> findByTipo(String tipo){
        try {
            Collection<TareaMultiple> tareas = (Collection<TareaMultiple>) em.createNamedQuery("TareaMultiple.findByTipo").setParameter("tipo", tipo).getResultList();
            for (TareaMultiple tareaMultiple : tareas) {
                initialize(tareaMultiple);
            }
            return tareas;
        } catch (NoResultException nre) {
            return new ArrayList();
        }
    }

    @Override
    public Collection<TareaMultiple> findByCorreo(String correo){
        try{
            Collection<TareaMultiple> tareas = (Collection<TareaMultiple>) em.createNamedQuery("TareaMultiple.findByCorreo").setParameter("correo", correo).getResultList();
            for (TareaMultiple tareaMultiple : tareas) {
                initialize(tareaMultiple);
            }
            return tareas;
        }catch(NoResultException nre){
            return new ArrayList();
        }
    }

    @Override
    public Collection<TareaMultiple> findByRol(String rol){
        try {
            Collection<TareaMultiple> tareas = (Collection<TareaMultiple>) em.createNamedQuery("TareaMultiple.findByRol").setParameter("rol", rol).getResultList();
            for (TareaMultiple tareaMultiple : tareas) {
                initialize(tareaMultiple);
            }
            return tareas;
        } catch (NoResultException nre) {
            return new ArrayList();
        }
    }

    @Override
    public Collection<TareaMultiple> findByRolNoPersonal(String rol){
        try {
            Collection<TareaMultiple> tareas = (Collection<TareaMultiple>) em.createNamedQuery("TareaMultiple.findByRolNoPersonal").setParameter("rol", rol).getResultList();
            for (TareaMultiple tareaMultiple : tareas) {
                initialize(tareaMultiple);
            }
            return tareas;
        } catch (NoResultException nre) {
            return new ArrayList();
        }
    }

    @Override
    public List<TareaMultiple> findByCorreoYFechaAntesDeCaducacion(String correo, Timestamp fecha, String rol ) {
        Query query = em.createNamedQuery("TareaMultiple.findByCorreoYFechaAntesDeCaducacion");
        query.setParameter("correo", correo);
        query.setParameter("fecha", fecha);
        query.setParameter("rol", rol);
        List<TareaMultiple> tareas = query.getResultList();
        for (TareaMultiple tareaMultiple : tareas) {
            initialize(tareaMultiple);
        }
        return tareas;
    }

    @Override
    public List<TareaMultiple> findByRolNoPersonalYFechaAntesDeCaducacion(Timestamp fecha, String rol ) {
        Query query = em.createNamedQuery("TareaMultiple.findByRolNoPersonalYFechaAntesDeCaducacion");
        query.setParameter("fecha", fecha);
        query.setParameter("rol", rol);
        List<TareaMultiple> tareas = query.getResultList();
        for (TareaMultiple tareaMultiple : tareas) {
            initialize(tareaMultiple);
        }
        return tareas;
    }



    private void initialize(TareaMultiple tarea){
        Hibernate.initialize(tarea);
        Hibernate.initialize(tarea.getPersona());
        Hibernate.initialize(tarea.getRol());
        for (TareaSencilla tareaS : tarea.getTareasSencillas()) {
            Hibernate.initialize(tareaS);
            for (Parametro parametro : tareaS.getParametros()) {
                Hibernate.initialize(parametro);
            }
        }
    }

    public Collection<TareaMultiple> findByEstadoTareaSencilla(String estado) {
        Query query = em.createNamedQuery("TareaMultiple.findByEstadoTareaSencilla");
        query = query.setParameter("estado", estado);
        List<TareaMultiple> tareas = query.getResultList();
        for (TareaMultiple tareaMultiple : tareas) {
            initialize(tareaMultiple);
        }
        return tareas;
    }

    public Collection<TareaMultiple> findByCorreoRolAndEstadoTareaSencilla(String estado, String correo, String rol) {
        Query query = em.createNamedQuery("TareaMultiple.findByCorreoAndRolAndEstadoTareaSencilla");
        query = query.setParameter("estado", estado);
        query = query.setParameter("correo", correo);
        query = query.setParameter("rol", rol);
        List<TareaMultiple> tareas = query.getResultList();
        for (TareaMultiple tareaMultiple : tareas) {
            initialize(tareaMultiple);
        }
        return tareas;
    }

    public Collection<TareaMultiple> findByRolAndEstadoTareaSencillaNoPersonal(String estado, String rol) {
        Query query = em.createNamedQuery("TareaMultiple.findByRolAndEstadoTareaSencillaNoPersonal");
        query = query.setParameter("estado", estado);
        query = query.setParameter("rol", rol);
        List<TareaMultiple> tareas = query.getResultList();
        for (TareaMultiple tareaMultiple : tareas) {
            initialize(tareaMultiple);
        }
        return tareas;
    }



}
