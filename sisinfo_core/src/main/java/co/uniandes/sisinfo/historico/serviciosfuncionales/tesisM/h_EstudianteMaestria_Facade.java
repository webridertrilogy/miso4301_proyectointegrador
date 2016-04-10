package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_estudiante_maestria;
import co.uniandes.sisinfo.historico.entities.tesisM.h_inscripcion_subarea;
import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_1;
import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_2;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 * Servicios Fachada - Histórico de Estudiante Maestria
 * @author Paola Gómez
 */
@Stateless
public class h_EstudianteMaestria_Facade implements h_EstudianteMaestria_FacadeLocal, h_EstudianteMaestria_FacadeRemote {

    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_estudiante_maestria h_estudiante_maestria) {
        em.persist(h_estudiante_maestria);
    }

    public void edit(h_estudiante_maestria h_estudiante_maestria) {
        em.merge(h_estudiante_maestria);
    }

    public void remove(h_estudiante_maestria h_estudiante_maestria) {
        em.remove(em.merge(h_estudiante_maestria));
    }

    public h_estudiante_maestria find(Object id) {
        return em.find(h_estudiante_maestria.class, id);
    }

    public List<h_estudiante_maestria> findAll() {
        return em.createQuery("select object(o) from h_estudiante_maestria as o").getResultList();
    }

    public List<h_estudiante_maestria> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_estudiante_maestria as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_estudiante_maestria as o").getSingleResult()).intValue();
    }

    public h_estudiante_maestria findByCorreoEstudiante(String correo) {
        try {
            h_estudiante_maestria estudiante = (h_estudiante_maestria) em.createNamedQuery("h_estudiante_maestria.findByCorreoEstudiante").setParameter("correo", correo).getSingleResult();
            if(estudiante!=null)
                hibernateInitialize(estudiante);
            //hibernateInitialize(estudiante);
            return estudiante;
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        }
        
    }

    public List<h_estudiante_maestria> findByProfesor(String correoProfesor) {

        return em.createNamedQuery("h_estudiante_maestria.findByProfesor").setParameter("correo", correoProfesor).getResultList();
    }

    private void hibernateInitialize(h_estudiante_maestria hEst) {
        Hibernate.initialize(hEst);

        if(hEst.getTesis1()!=null){
            Hibernate.initialize(hEst.getTesis1());
            for (h_tesis_1 tesis1 : hEst.getTesis1()) {
                Hibernate.initialize(tesis1);
            }
        }

        if(hEst.getTesis2()!=null){
            Hibernate.initialize(hEst.getTesis2());
            for (h_tesis_2 tesis2 : hEst.getTesis2()) {
                Hibernate.initialize(tesis2);
            }
        }

        if(hEst.getInscripcionSubarea()!=null){
            Hibernate.initialize(hEst.getInscripcionSubarea());
            for (h_inscripcion_subarea hInsc : hEst.getInscripcionSubarea()) {
                Hibernate.initialize(hInsc);
            }
        }


    }
}
