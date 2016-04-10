/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
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
 * @author Asistente
 */
@Stateless
public class EstudianteFacade implements EstudianteFacadeLocal, EstudianteFacadeRemote {

    @PersistenceContext(unitName = "DatosMaestrosPU")
    private EntityManager em;

    public void create(Estudiante estudiante) {
        em.persist(estudiante);
    }

    public void edit(Estudiante estudiante) {
        em.merge(estudiante);
    }

    public void remove(Estudiante estudiante) {
        em.remove(em.merge(estudiante));
    }

    public Estudiante find(Object id) {
        Estudiante e = em.find(Estudiante.class, id);
        hibernateInitialize(e);
        return e;
    }

    public List<Estudiante> findAll() {
          List<Estudiante> list =  em.createNamedQuery("Estudiante.findAll").getResultList();
        for (Estudiante estudiante : list) {
            hibernateInitialize(estudiante);
        }
        return list;
    }

    public List<Estudiante> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Estudiante as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Estudiante as o").getSingleResult()).intValue();
    }

    public Estudiante findByCorreo(String correo) {

        try {
            Estudiante estudiante = (Estudiante) em.createNamedQuery("Estudiante.findByCorreo").setParameter("correo", correo).getSingleResult();
            hibernateInitialize(estudiante);
            return estudiante;
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        }
    }
    
     public List<Estudiante> findByTipo(String tipo) {
        List<Estudiante> estudiantes= em.createNamedQuery("Estudiante.findByTipoEstudiante").setParameter("tipo", tipo).getResultList();
         for (Estudiante estudiante : estudiantes) {
             hibernateInitialize(estudiante);
         }
         return estudiantes;
    }



    public Estudiante findByCodigo(String codigo) {
        try {
            Estudiante estudiante = (Estudiante) em.createNamedQuery("Estudiante.findByCodigo").setParameter("correo", codigo).getSingleResult();
            hibernateInitialize(estudiante);
            return estudiante;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Inicializa la sección y sus colecciones
     * @param seccion Sección
     */
    private void hibernateInitialize(Estudiante e) {
        if (e != null) {
            Hibernate.initialize(e);
            if(e.getTipoCuenta() != null)
                Hibernate.initialize(e.getTipoCuenta());
            if(e.getInformacion_Academica() != null)
                Hibernate.initialize(e.getInformacion_Academica());
            if(e.getPersona() != null){
                Hibernate.initialize(e.getPersona());
                if(e.getPersona().getTipoDocumento() != null)
                    Hibernate.initialize(e.getPersona().getTipoDocumento());
                if(e.getPersona().getPais() != null)
                    Hibernate.initialize(e.getPersona().getPais());
            }
            if(e.getPrograma() != null){
                Hibernate.initialize(e.getPrograma());
                if(e.getPrograma().getFacultad() != null)
                    Hibernate.initialize(e.getPrograma().getFacultad());
            }
            Hibernate.initialize(e.getTipoEstudiante());
        }
    }
}
