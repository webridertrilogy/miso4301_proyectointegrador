/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CursoTesis;
import co.uniandes.sisinfo.entities.InscripcionSubareaInvestigacion;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.util.Collection;
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
public class InscripcionSubareaInvestigacionFacade implements InscripcionSubareaInvestigacionFacadeLocal, InscripcionSubareaInvestigacionFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(InscripcionSubareaInvestigacion tesis1) {
        em.persist(tesis1);
    }

    public void edit(InscripcionSubareaInvestigacion tesis1) {
        em.merge(tesis1);
    }

    public void remove(InscripcionSubareaInvestigacion tesis1) {
        em.remove(em.merge(tesis1));
    }

    public InscripcionSubareaInvestigacion find(Object id) {
        InscripcionSubareaInvestigacion i = em.find(InscripcionSubareaInvestigacion.class, id);
        hibernateInitialize(i);
        return i;
    }

    public List<InscripcionSubareaInvestigacion> findAll() {
        List<InscripcionSubareaInvestigacion> lista = em.createQuery("select object(o) from InscripcionSubareaInvestigacion as o").getResultList();
        for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : lista) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return lista;
    }

    public List<InscripcionSubareaInvestigacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Tesis1 as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        List<InscripcionSubareaInvestigacion> lista = q.getResultList();
        for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : lista) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return lista;
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Tesis1 as o").getSingleResult()).intValue();
    }

    public List<InscripcionSubareaInvestigacion> findByCorreoCoordinador(String correo) {
        List<InscripcionSubareaInvestigacion> temp = em.createNamedQuery("InscripcionSubareaInvestigacion.findByCorreoDirectorSubArea").setParameter("correoDirector", correo).getResultList();
        for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public List<InscripcionSubareaInvestigacion> findByCorreoAsesor(String correo) {
        List<InscripcionSubareaInvestigacion> temp = em.createNamedQuery("InscripcionSubareaInvestigacion.findBycorreoAsesor").setParameter("correoasesor", correo).getResultList();
        for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public List<InscripcionSubareaInvestigacion> findByEstadoInscripcion(String estado) {
        List<InscripcionSubareaInvestigacion> temp = em.createNamedQuery("InscripcionSubareaInvestigacion.findByEstadoInscripcion").setParameter("estadoSolicitud", estado).getResultList();
        for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    private void hibernateInitialize(InscripcionSubareaInvestigacion i) {
        Hibernate.initialize(i);
        if (i.getAsesor() != null) {
            Hibernate.initialize(i.getAsesor());
            Hibernate.initialize(i.getAsesor().getPersona());
        }
//        if (i.getCoordinadorSubArea() != null) {
//            Hibernate.initialize(i.getCoordinadorSubArea());
//            Hibernate.initialize(i.getCoordinadorSubArea().getPersona());
//        }
        if (i.getEstudiante() != null) {
            Hibernate.initialize(i.getEstudiante());
            Hibernate.initialize(i.getEstudiante().getPersona());
        }
        if (i.getSubareaInvestigacion() != null) {
            Hibernate.initialize(i.getSubareaInvestigacion());
            Hibernate.initialize(i.getSubareaInvestigacion().getCoordinadorSubarea());
//            Hibernate.initialize(i.getSubGrupoInvestigacion().getCoordinadorGrupo().getPersona());
        }
        if (i.getObligatorias() != null) {
            Collection<CursoTesis> cursos = i.getObligatorias();
            for (CursoTesis object : cursos) {
                Hibernate.initialize(object);
            }
        }
        if (i.getSubArea() != null) {
            Collection<CursoTesis> cursos = i.getSubArea();
            for (CursoTesis object : cursos) {
                Hibernate.initialize(object);
            }
        }
        if (i.getNivelatorios() != null) {
            Collection<CursoTesis> cursos = i.getNivelatorios();
            for (CursoTesis object : cursos) {
                Hibernate.initialize(object);
            }
        }
        if (i.getOtraMaestria() != null) {
            Hibernate.initialize(i.getOtraMaestria());
        }
        if (i.getOtraSubArea() != null) {
            Hibernate.initialize(i.getOtraSubArea());
        }

    }

    public InscripcionSubareaInvestigacion findByCorreoEstudiante(String correo) {
       
         try {
            InscripcionSubareaInvestigacion subarea = (InscripcionSubareaInvestigacion) em.createNamedQuery("InscripcionSubareaInvestigacion.findBycorreoEstudiante").setParameter("correoEstudiante", correo).getSingleResult();
            hibernateInitialize(subarea);
            return subarea;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            InscripcionSubareaInvestigacion subarea = (InscripcionSubareaInvestigacion) em.createNamedQuery("InscripcionSubareaInvestigacion.findBycorreoEstudiante").setParameter("correoEstudiante", correo).getResultList().get(0);
            hibernateInitialize(subarea);
            return subarea;
        }
    }

    public InscripcionSubareaInvestigacion findByEstadoYCorreoEstudiante(String correo, String estado) {
        Query q = em.createNamedQuery("InscripcionSubareaInvestigacion.findBycorreoEstudianteYEstado").setParameter("correoEstudiante", correo).setParameter("estado", estado);
        try {
            InscripcionSubareaInvestigacion temp = (InscripcionSubareaInvestigacion) q.getSingleResult();
            if (temp != null) {
                hibernateInitialize(temp);
            }

            return temp;
        } catch (NonUniqueResultException e) {
            return (InscripcionSubareaInvestigacion) q.getResultList().get(0);
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<InscripcionSubareaInvestigacion> findByCorreoCoordinadorYEstado(String correo, String estado) {
         List<InscripcionSubareaInvestigacion> temp = em.createNamedQuery("InscripcionSubareaInvestigacion.findBycorreoCoordinadorSubareaYEstado").setParameter("correo", correo).setParameter("estado", estado).getResultList();
        for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public List<InscripcionSubareaInvestigacion> findByCorreoAsesorYestado(String correo, String estado) {
        List<InscripcionSubareaInvestigacion> temp = em.createNamedQuery("InscripcionSubareaInvestigacion.findBycorreoAsesorYEstado").setParameter("correo", correo).setParameter("estado", estado).getResultList();
        for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public List<InscripcionSubareaInvestigacion> findByPeriodoTesis1(String periodo) {
        List<InscripcionSubareaInvestigacion> temp = em.createNamedQuery("InscripcionSubareaInvestigacion.findByPeriodoTesis1").setParameter("periodo", periodo).getResultList();
        for (InscripcionSubareaInvestigacion inscripcionSubareaInvestigacion : temp) {
            hibernateInitialize(inscripcionSubareaInvestigacion);
        }
        return temp;
    }

    public Collection<Persona> findByCoordinadoresSubAreaConEstado(String estado) {
       List<Persona> temp = em.createNamedQuery("InscripcionSubareaInvestigacion.findByCoordinadoresSubAreaConEstado").setParameter("estado", estado).getResultList();
        for (Persona i : temp) {
           Hibernate.initialize(i);
        }
        return temp;
    }

    public Collection<Persona> findByAsesoresSubAreaConEstado(String estado) {
      
         List<Persona> temp = em.createNamedQuery("InscripcionSubareaInvestigacion.findByAsesoresSubAreaConEstado").setParameter("estado", estado).getResultList();
        for (Persona i : temp) {
           Hibernate.initialize(i);
        }
        return temp;
    }




    


}
