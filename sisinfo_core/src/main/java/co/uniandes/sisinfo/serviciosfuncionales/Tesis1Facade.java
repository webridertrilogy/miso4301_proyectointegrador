/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Tesis1;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 *
 * @author Asistente
 */
@Stateless
public class Tesis1Facade implements Tesis1FacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(Tesis1 tesis1) {
        em.persist(tesis1);
    }

    public void edit(Tesis1 tesis1) {
        em.merge(tesis1);
    }

    public void remove(Tesis1 tesis1) {
        em.remove(em.merge(tesis1));
    }

    public Tesis1 find(Object id) {
        return em.find(Tesis1.class, id);
    }

    public List<Tesis1> findAll() {
        return em.createQuery("select object(o) from Tesis1 as o").getResultList();
    }

    public List<Tesis1> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Tesis1 as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Tesis1 as o").getSingleResult()).intValue();
    }

    public Collection<Tesis1> findByCorreoAsesor(String correo) {
        List<Tesis1> temp = em.createNamedQuery("Tesis1.findBycorreoAsesor").setParameter("correoasesor", correo).getResultList();
        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    private void hibernateInitialize(Tesis1 i) {
        Hibernate.initialize(i);
        if (i.getAsesor() != null) {
            Hibernate.initialize(i.getAsesor());
            Hibernate.initialize(i.getAsesor().getPersona());
        }
        if (i.getEstudiante() != null) {
            Hibernate.initialize(i.getEstudiante());
            Hibernate.initialize(i.getEstudiante().getPersona());

        }
        if (i.getSemestreIniciacion() != null) {
            Hibernate.initialize(i.getSemestreIniciacion());
            Hibernate.initialize(i.getSemestreIniciacion());
        }
    }

    public Collection<Tesis1> findByCorreoEstudiante(String correo) {
        List<Tesis1> temp = em.createNamedQuery("Tesis1.findBycorreoEstudiante").setParameter("correoEstudiante", correo).getResultList();
        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis1> findByEstadoTesis(String estado) {
        List<Tesis1> temp = em.createNamedQuery("Tesis1.findByEstadoTesis").setParameter("estadoTesis", estado).getResultList();
        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis1> findByPeriodoTesis(String periodo) {
        List<Tesis1> temp = em.createNamedQuery("Tesis1.findByPeriodoTesis").setParameter("periodo", periodo).getResultList();

        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis1> findByEstadoYPeriodoTesis(String estado, String idPeriodo) {
        Query query = em.createNamedQuery("Tesis1.findByEstadoYPeriodoTesis");
        query.setParameter("estado", estado);
        query.setParameter("idPeriodo", Long.valueOf(idPeriodo));

        List<Tesis1> temp = query.getResultList();

        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }

        return temp;
    }


    public Collection<Tesis1> findByTemaTesis(String tematesis) {
         List<Tesis1> temp = em.createNamedQuery("Tesis1.findByTemaTesis").setParameter("tema", tematesis).getResultList();
        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis1> findBySincomentariosTesis(String periodo) {
//           List<Tesis1> temp = em.createNamedQuery("Tesis1.findByComentariosTesis").setParameter("periodo", periodo).getResultList();
//        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
//            hibernateInitialize(inscripcionSubareaInvestigacion);
//        }
//        return temp;
    	return null;
    }

    public Collection<Tesis1> findByPeriodoEstadoAsesor(String periodo, String estado, String correoAsesor) {
       List<Tesis1> temp = em.createNamedQuery("Tesis1.findByPeriodoEstadoAsesor").setParameter("estadoTesis", estado).setParameter("periodo", periodo).setParameter("correoasesor", correoAsesor).getResultList();
        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
        /*
         * semestreIniciacion.periodo =:periodo "
      + "AND i.asesor.persona.correo =:correoasesor
         */
    }

    public Collection<Persona> findAsesoresByPeriodoEstado(String periodo, String estado) {
        List<Persona> temp = em.createNamedQuery("Tesis1.findAsesoresByPeriodoEstado").setParameter("estadoTesis", estado).setParameter("periodo", periodo).getResultList();     
        return temp;
    }

    public Collection<Tesis1> findByPeriodoYEstado(String periodo, String estado) {
        List<Tesis1> temp = em.createNamedQuery("Tesis1.findByPeriodoEstado").setParameter("estadoTesis", estado).setParameter("periodo", periodo).getResultList();
        for (Tesis1 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }









   


}
