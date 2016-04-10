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

import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios Entidad Monitoría
 */
@Stateless
@EJB(name = "MonitoriaFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.MonitoriaFacadeLocal.class)
public class MonitoriaFacade implements MonitoriaFacadeLocal,MonitoriaFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(MonitoriaAceptada monitoria) {
        em.persist(monitoria);
    }

    @Override
    public void edit(MonitoriaAceptada monitoria) {
        em.flush();
        em.merge(monitoria);
    }

    @Override
    public void remove(MonitoriaAceptada monitoria) {
        em.remove(em.merge(monitoria));
    }

    @Override
    public MonitoriaAceptada find(Object id) {
        return em.find(MonitoriaAceptada.class, id);
    }

    @Override
    public List<MonitoriaAceptada> findAll() {
        return em.createQuery("select object(o) from MonitoriaAceptada as o").getResultList();
    }

    public List<MonitoriaAceptada> findByCodigoEstudiante(String codigo) {
        try {
            List<MonitoriaAceptada> monitorias = (List<MonitoriaAceptada>) em.createNamedQuery("MonitoriaAceptada.findByCodigoEstudiante").setParameter("codigo", codigo).getResultList();

            return monitorias;            
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<MonitoriaAceptada> findByCRNSeccion(String crn) {
        try {
            List<MonitoriaAceptada> monitorias = (List<MonitoriaAceptada>) em.createNamedQuery("MonitoriaAceptada.findByCRNSeccion").setParameter("crn", crn).getResultList();

            return monitorias;
        } catch (NoResultException e) {
            return null;
        }
    }

    public MonitoriaAceptada findByCRNYCorreo(String crn, String correo) {
        Query query = em.createNamedQuery("MonitoriaAceptada.findByCRNYCorreo");
        query.setParameter("crn", crn);
        query.setParameter("correo", correo);
        try {
            return (MonitoriaAceptada) query.getSingleResult();
        }catch(NonUniqueResultException e){
            return (MonitoriaAceptada) query.getResultList().get(0);
        }catch(NoResultException e){
            return null;
        }
    }

    public MonitoriaAceptada findBySolicitud(long id) {
        Query query = em.createNamedQuery("MonitoriaAceptada.findBySolicitud");
        query.setParameter("id", id);
        try {
            return (MonitoriaAceptada) query.getSingleResult();
        }catch(NonUniqueResultException e){
            return (MonitoriaAceptada) query.getResultList().get(0);
        }catch(NoResultException e){
            return null;
        }
    }


}
