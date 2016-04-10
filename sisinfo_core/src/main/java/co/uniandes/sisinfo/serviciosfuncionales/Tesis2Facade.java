/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.Tesis2;
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
public class Tesis2Facade implements Tesis2FacadeLocal, Tesis2FacadeRemote {
    @PersistenceContext(unitName = "TesisMaestriaPU")
    private EntityManager em;

    public void create(Tesis2 tesis2) {
        em.persist(tesis2);
    }

    public void edit(Tesis2 tesis2) {
        em.merge(tesis2);
    }

    public void remove(Tesis2 tesis2) {
        em.remove(em.merge(tesis2));
    }

    public Tesis2 find(Object id) {
        return em.find(Tesis2.class, id);
    }

    public List<Tesis2> findAll() {
        return em.createQuery("select object(o) from Tesis2 as o").getResultList();
    }

    public List<Tesis2> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Tesis2 as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Tesis2 as o").getSingleResult()).intValue();
    }

    public Collection<Tesis2> findByCorreoAsesor(String correo) {
         List<Tesis2> temp = em.createNamedQuery("Tesis2.findBycorreoAsesor").setParameter("correo", correo).getResultList();
        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

     private void hibernateInitialize(Tesis2 i) {
        Hibernate.initialize(i);
        if(i.getAsesor()!=null)
        {
            Hibernate.initialize(i.getAsesor());
            Hibernate.initialize(i.getAsesor().getPersona());

        }
        if(i.getEstudiante()!=null)
        {
            Hibernate.initialize(i.getEstudiante());
            Hibernate.initialize(i.getEstudiante().getPersona());

        }
         if(i.getSemestreInicio()!=null)
        {
            Hibernate.initialize(i.getSemestreInicio());
            Hibernate.initialize(i.getSemestreInicio());

        }
        if(i.getSemestreInicio()!=null)
        {
            Hibernate.initialize(i.getSemestreInicio());
        }
        if(i.getHorarioSustentacion()!=null)
        {
               Hibernate.initialize(i.getHorarioSustentacion());
        }
    }

    public Collection<Tesis2> findByCorreoEstudiante(String correo) {
        List<Tesis2> temp = em.createNamedQuery("Tesis2.findBycorreoEstudiante").setParameter("correoEstudiante", correo).getResultList();
        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis2> findByEstadoTesis(String estado) {
       List<Tesis2> temp = em.createNamedQuery("Tesis2.findByEstadoTesis").setParameter("estado", estado).getResultList();
        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis2> findByPeriodo(String periodo) {
       List<Tesis2> temp = em.createNamedQuery("Tesis2.findByPeriodoTesis").setParameter("periodo", periodo).getResultList();
        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis2> findByEstadoYPeriodoTesis(String estado, String idPeriodo) {
        Query query = em.createNamedQuery("Tesis2.findByEstadoYPeriodoTesis");
        query.setParameter("estado", estado);
        query.setParameter("periodo", Long.valueOf(idPeriodo));

        List<Tesis2> temp = query.getResultList();

        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        
        return temp;
    }

    public Collection<Tesis2> findDetallesSustentacionByPeriodoEstado(String periodo) {
        Query query = em.createNamedQuery("Tesis2.findDetallesSustentacionByPeriodoEstado");
        query.setParameter("periodo", periodo);

        List<Tesis2> temp = query.getResultList();
        System.out.println("Encontre:"+temp.size());
        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }

        return temp;
    }



    public Collection<Tesis2> findByHorarioSustentacion(String periodo) {
        String periodoAnterior;
        int periodoActual = Integer.parseInt(periodo);
        int periodoAnt = periodoActual - 10;
        periodoAnterior = String.valueOf(periodoAnt);
        Query query = em.createNamedQuery("Tesis2.findByHorarioSustentacion");
        query.setParameter("periodo", periodo);
        query.setParameter("periodoAnterior", periodoAnterior);
        List<Tesis2> temp = query.getResultList();
        System.out.print(temp.size());
        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis2> findAllOrderBySemestre() {
        List<Tesis2> temp = em.createNamedQuery("Tesis2.findAllOrderBySemestre").getResultList();
        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Tesis2> findBySincomentariosTesis(String periodo) {
       List<Tesis2> temp = em.createNamedQuery("Tesis2.findByComentariosTesis").setParameter("periodo", periodo).getResultList();
        for (Tesis2 inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Persona> findAsesoresByEstadoYPeriodo(String estado, String periodo) {
      
        Collection<Persona> temp = em.createNamedQuery("Tesis2.findAsesoresByPeriodoEstado").setParameter("periodo", periodo).setParameter("estadoTesis", estado).getResultList();
        for (Persona i : temp) {
            Hibernate.initialize(i);
        }
        return temp;

    }

    public Collection<Tesis2> findByDiferentePeriodoTesis(String periodo, String estadoTesis) {
       List<Tesis2> temp = em.createNamedQuery("Tesis2.findByDiferentePeriodoTesis").setParameter("periodo", periodo).setParameter("estadoTesis", estadoTesis).getResultList();
        for (Tesis2 tesis2 : temp) {
            hibernateInitialize(tesis2);
        }
        return temp;
    }










    

}
