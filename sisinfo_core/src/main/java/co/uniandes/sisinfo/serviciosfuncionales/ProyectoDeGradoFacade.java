/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ProyectoDeGrado;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 *
 * @author Ivan Mauricio Melo S, Paola Andres Gómez B
 */
@Stateless
public class ProyectoDeGradoFacade implements ProyectoDeGradoFacadeLocal, ProyectoDeGradoFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(ProyectoDeGrado proyectoDeGrado) {
        em.persist(proyectoDeGrado);
    }

    public void edit(ProyectoDeGrado proyectoDeGrado) {
        em.merge(proyectoDeGrado);
    }

    public void remove(ProyectoDeGrado proyectoDeGrado) {
        em.remove(em.merge(proyectoDeGrado));
    }

    public ProyectoDeGrado find(Object id) {
        return em.find(ProyectoDeGrado.class, id);
    }

    public List<ProyectoDeGrado> findAll() {
        return em.createQuery("select object(o) from ProyectoDeGrado as o").getResultList();
    }

    public List<ProyectoDeGrado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from ProyectoDeGrado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from ProyectoDeGrado as o").getSingleResult()).intValue();
    }

    public Collection<ProyectoDeGrado> findByCorreoEstudiante(String correo) {
        List<ProyectoDeGrado> temp = em.createNamedQuery("ProyectoDeGrado.findBycorreoEstudiante").setParameter("correoEstudiante", correo).getResultList();
        for (ProyectoDeGrado inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    private void hibernateInitialize(ProyectoDeGrado i) {
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

    public Collection<ProyectoDeGrado> findByTemaTesis(String tema) {
        List<ProyectoDeGrado> temp = em.createNamedQuery("ProyectoDeGrado.findByTemaTesis").setParameter("tema", tema).getResultList();
        for (ProyectoDeGrado inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<ProyectoDeGrado> findByCorreoAsesor(String correo) {
        List<ProyectoDeGrado> temp = em.createNamedQuery("ProyectoDeGrado.findBycorreoAsesor").setParameter("correoasesor", correo).getResultList();
        for (ProyectoDeGrado inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<ProyectoDeGrado> findByEstado(String estado) {
         List<ProyectoDeGrado> temp = em.createNamedQuery("ProyectoDeGrado.findByEstadoTesis").setParameter("estadoTesis", estado).getResultList();
        for (ProyectoDeGrado inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<ProyectoDeGrado> findByPeriodoTesis(String periodo) {
        List<ProyectoDeGrado> temp = em.createNamedQuery("ProyectoDeGrado.findByPeriodoTesis").setParameter("periodo", periodo).getResultList();
        for (ProyectoDeGrado proyectoGrado : temp) {
            hibernateInitialize(proyectoGrado);
        }
        return temp;
    }


    public Collection<ProyectoDeGrado> findByPeriodoYEstado(String idPeriodo, String estado) {
        List<ProyectoDeGrado> temp = em.createNamedQuery("ProyectoDeGrado.findByPeriodoEstado").setParameter("estadoTesis", estado).setParameter("idPeriodo", Long.valueOf(idPeriodo)).getResultList();
        for (ProyectoDeGrado proyectoGrado : temp) {
            hibernateInitialize(proyectoGrado);
        }
        return temp;
    }

    public Collection<ProyectoDeGrado> findByPeriodoEstadoYRangoNota(String idPeriodo, String estado, String notaI, String notaF) {
        List<ProyectoDeGrado> temp = em.createNamedQuery("ProyectoDeGrado.findByPeriodoEstadoRangoNota").setParameter("estadoTesis", estado).setParameter("idPeriodo", Long.valueOf(idPeriodo) ).setParameter("notaI", Double.valueOf(notaI)).setParameter("notaF", Double.valueOf(notaF)).getResultList();
        for (ProyectoDeGrado proyectoGrado : temp) {
            hibernateInitialize(proyectoGrado);
        }
        return temp;
    }

    public Collection<ProyectoDeGrado> findByEstadoYRangoNota(String estado, String notaI, String notaF) {
        List<ProyectoDeGrado> temp = em.createNamedQuery("ProyectoDeGrado.findByEstadoRangoNota").setParameter("estadoTesis", estado).setParameter("notaI", Double.valueOf(notaI)).setParameter("notaF", Double.valueOf(notaF)).getResultList();
        for (ProyectoDeGrado proyectoGrado : temp) {
            hibernateInitialize(proyectoGrado);
        }
        return temp;
    }




}
