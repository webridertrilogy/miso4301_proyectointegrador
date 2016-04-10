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

import co.uniandes.sisinfo.entities.AlertaMultiple;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 * Servicios Entidad Alerta
 */
@Stateless
public class AlertaMultipleFacade implements AlertaMultipleFacadeLocal,AlertaMultipleFacadeRemote {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(AlertaMultiple alerta) {
        em.persist(alerta);
        em.flush();
    }

    @Override
    public void edit(AlertaMultiple alerta) {
        em.merge(alerta);
    }

    @Override
    public void remove(AlertaMultiple alerta) {
        em.remove(em.merge(alerta));
    }

    @Override
    public AlertaMultiple find(Object id) {
        return em.find(AlertaMultiple.class, id);
    }

    @Override
    public List<AlertaMultiple> findAll() {
        return em.createQuery("select object(o) from AlertaMultiple as o").getResultList();
    }

    @Override
    public AlertaMultiple findByTipo(String tipo){
        try{
            return (AlertaMultiple)em.createNamedQuery("AlertaMultiple.findByTipo").setParameter("tipo", tipo).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }catch (NonUniqueResultException e) {
            return (AlertaMultiple) em.createNamedQuery("AlertaMultiple.findByTipo").setParameter("tipo", tipo).getResultList().get(0);

        }
    }

    public AlertaMultiple findById(long id) {
        try {
        return (AlertaMultiple) em.createNamedQuery("AlertaMultiple.findById").setParameter("id", id).getSingleResult();
       } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (AlertaMultiple) em.createNamedQuery("AlertaMultiple.findById").setParameter("id", id).getResultList().get(0);
          
        }
    }  

    private void inicializar(AlertaMultiple alerta){
        Hibernate.initialize(alerta);
        Hibernate.initialize(alerta.getPeriodicidad());
        for (TareaMultiple tarea : alerta.getTareas()) {
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
    }


}
