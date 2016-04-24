/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.Collection;
import javax.ejb.Stateless;
import co.uniandes.sisinfo.entities.InscripcionAsistente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 *
 * @author im.melo33
 */
@Stateless
public class InscripcionAsistenteFacade implements  InscripcionAsistenteFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(InscripcionAsistente inscripcion) {
        em.persist(inscripcion);
    }

    public void edit(InscripcionAsistente inscripcion) {
        em.merge(inscripcion);
    }

    public void remove(InscripcionAsistente inscripcion) {
        em.remove(em.merge(inscripcion));
    }

    public void refresh(InscripcionAsistente inscrAsis) {
        em.refresh(inscrAsis);
    }

    public InscripcionAsistente find(Object id) {
        InscripcionAsistente inscrAsist = em.find(InscripcionAsistente.class, id);
        hibernateInitialize(inscrAsist);
        return inscrAsist;
    }

    public List<InscripcionAsistente> findAll() {
        return em.createQuery("select object(o) from InscripcionAsistente as o").getResultList();
    }

    @Override
    public InscripcionAsistente findByHash(String hashConfirm) {
        try {
            InscripcionAsistente inscrAsist = (InscripcionAsistente) em.createNamedQuery("InscripcionAsistente.findByHash").setParameter("hash", hashConfirm).getSingleResult();
            hibernateInitialize(inscrAsist);
            return inscrAsist;
        } catch (NoResultException e) {
            return null;
        }

    }

    public Collection<InscripcionAsistente> darInscripcionesAsistentePorCorreo(String correo) {
        List<InscripcionAsistente> lista = em.createNamedQuery("InscripcionAsistente.findByCorreo").setParameter("correo", correo).getResultList();
        for (InscripcionAsistente inscripcionAsistente : lista) {
            hibernateInitialize(inscripcionAsistente);
        }
        return lista;
    }

    public InscripcionAsistente findByIdInscripcionYCorreoPersona(String correo, Long idInscripcion) {
        try {
            InscripcionAsistente inscrAsist = (InscripcionAsistente) em.createNamedQuery("InscripcionAsistente.findByIdInscripcionYCorreoPersona").setParameter("idInscripcion", idInscripcion).setParameter("correo", correo).getSingleResult();
            hibernateInitialize(inscrAsist);
            return inscrAsist;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e1) {
            List<InscripcionAsistente> lista = em.createNamedQuery("InscripcionAsistente.findByIdInscripcionYCorreoPersona").setParameter("idInscripcion", idInscripcion).setParameter("correo", correo).getResultList();
            return lista.get(0);
        }
    }

    private void hibernateInitialize(InscripcionAsistente i) {
        Hibernate.initialize(i);
        if(i != null){
            if (i.getPersona() != null) {
                Hibernate.initialize(i.getPersona());
            }
        }
    }
}
