/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 *
 * @author da-naran
 */
@Stateless
public class ProfesorFacade implements ProfesorFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(Profesor profesor) {
        em.persist(profesor);
    }

    public void edit(Profesor profesor) {
        em.merge(profesor);
    }

    public void remove(Profesor profesor) {
        em.remove(em.merge(profesor));
    }

    public Profesor find(Object id) {
        Profesor p= em.find(Profesor.class, id);
        hibernateInitialize(p);
        return p;
    }

    public List<Profesor> findAll() {
         try {
            List<Profesor> listaProfesores = (List<Profesor>) em.createNamedQuery("Profesor.findByALL").getResultList();
            for (Profesor profesor : listaProfesores) {
                hibernateInitialize(profesor);
            }
            return listaProfesores;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Profesor> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Profesor as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Profesor as o").getSingleResult()).intValue();
    }

     @Override
    public Profesor findById(Long id) {
        try {
            Profesor profesor = (Profesor) em.createNamedQuery("Profesor.findById").setParameter("id", id).getSingleResult();
            hibernateInitialize(profesor);
            return profesor;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Profesor profesor = (Profesor) em.createNamedQuery("Profesor.findById").setParameter("id", id).getResultList().get(0);
            hibernateInitialize(profesor);
            return profesor;
        }
    }

    @Override
    public Profesor findByCorreo(String correo) {
        try {
            Profesor profesor = (Profesor) em.createNamedQuery("Profesor.findByCorreo").setParameter("correo", correo).getSingleResult();
            hibernateInitialize(profesor);
            return profesor;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Profesor profesor = (Profesor) em.createNamedQuery("Profesor.findByCorreo").setParameter("correo", correo).getResultList().get(0);
            hibernateInitialize(profesor);
            return profesor;
        }
    }

    @Override
    public List<Profesor> findByTipo(String tipo) {
        try{List<Profesor> listaProfesores=(List<Profesor>) em.createNamedQuery("Profesor.findByTipo").setParameter("tipo", tipo).getResultList();
        for (Profesor profesor : listaProfesores) {
            hibernateInitialize(profesor);
        }
        return listaProfesores;}catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Profesor> findByNivelPlanta(String nivelPlanta) {
        try{List<Profesor> listaProfesores=(List<Profesor>) em.createNamedQuery("Profesor.findByNivelPlanta").setParameter("nivelPlanta", nivelPlanta).getResultList();
        for (Profesor profesor : listaProfesores) {
            hibernateInitialize(profesor);
        }
        return listaProfesores;}catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Inicializa el profesor y sus colecciones
     * @param profesor Profesor
     */
    private void hibernateInitialize(Profesor profesor) {
//        Hibernate.initialize(profesor);
//        Hibernate.initialize(profesor.getGrupoInvestigacion());
//        Hibernate.initialize(profesor.getPersona());
//        Hibernate.initialize(profesor.getNivelFormacion());
//        Hibernate.initialize(profesor.getNivelPlanta());
//        Hibernate.initialize(profesor.getPersona().getTipoDocumento());
//        if(profesor.getPersona().getTipoDocumento() != null)
//            Hibernate.initialize(profesor.getPersona().getTipoDocumento().getTipo());
//        Hibernate.initialize(profesor.getPersona().getPais());
//        if(profesor.getPersona().getPais() != null)
//            Hibernate.initialize(profesor.getPersona().getPais().getNombre());
    }

}
