package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Proponente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 * Servicios fachada de la entidad Proponente
 * @author Marcela Morales
 */
@Stateless
public class ProponenteFacade implements ProponenteFacadeLocal, ProponenteFacadeRemote {
    @PersistenceContext
    private EntityManager em;

    public void create(Proponente proponente) {
        em.persist(proponente);
    }

    public void edit(Proponente proponente) {
        em.merge(proponente);
    }

    public void remove(Proponente proponente) {
        em.remove(em.merge(proponente));
    }

    public Proponente find(Object id) {
        return em.find(Proponente.class, id);
    }

    public List<Proponente> findAll() {
        return em.createQuery("select object(o) from Proponente as o").getResultList();
    }

    public List<Proponente> findByTipoEmpresa(){
        return em.createNamedQuery("Proponente.findByTipoEmpresa").getResultList();
    }

    public Proponente findByIdOferta(Long id){
        try {
            Proponente proponente = (Proponente) em.createNamedQuery("Proponente.findByIdOferta").setParameter("id", id).getSingleResult();
            Hibernate.initialize(proponente);
            if(proponente.getPersona() != null){
                Hibernate.initialize(proponente.getPersona());
                Hibernate.initialize(proponente.getInformacionEmpresa());
                if(proponente.getInformacionEmpresa() != null){
                    Hibernate.initialize(proponente.getInformacionEmpresa().getCargoContactoEmpresa());
                }
            }
            return proponente;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Proponente) em.createNamedQuery("Proponente.findByIdOferta").setParameter("id", id).getResultList().get(0);
        }
    }

    public Proponente findById(Long id){
        try {
            return (Proponente) em.createNamedQuery("Proponente.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Proponente) em.createNamedQuery("Proponente.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    public Proponente findByCorreo(String correo){
        try {
            return (Proponente) em.createNamedQuery("Proponente.findByCorreo").setParameter("correo", correo).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Proponente) em.createNamedQuery("Proponente.findByCorreo").setParameter("correo", correo).getResultList().get(0);
        }
    }
}
