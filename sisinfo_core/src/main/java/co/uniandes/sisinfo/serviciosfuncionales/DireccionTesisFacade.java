/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CargaProfesor;
import co.uniandes.sisinfo.entities.DireccionTesis;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
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
public class DireccionTesisFacade implements DireccionTesisFacadeLocal, DireccionTesisFacadeRemote {

    @PersistenceContext(unitName = "CargaYCompromisoProfPU")
    private EntityManager em;

    public void create(DireccionTesis direccionTesis) {
        em.persist(direccionTesis);
    }

    public void edit(DireccionTesis direccionTesis) {
        em.merge(direccionTesis);
    }

    public void remove(DireccionTesis direccionTesis) {
        em.remove(em.merge(direccionTesis));
    }

    public DireccionTesis find(Object id) {
       DireccionTesis  dir= em.find(DireccionTesis.class, id);
        hibernateInitialize(dir);
            return dir;
    }

    public List<DireccionTesis> findAll() {
        List<DireccionTesis> dirs= em.createQuery("select object(o) from DireccionTesis as o").getResultList();
        for (DireccionTesis direccionTesis : dirs) {
             hibernateInitialize(direccionTesis);
        }
        return dirs;
    }

    public List<DireccionTesis> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from DireccionTesis as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from DireccionTesis as o").getSingleResult()).intValue();
    }

    public DireccionTesis findDireccionTesisByName(String name) {
        try {
            DireccionTesis  dir=(DireccionTesis) em.createNamedQuery("DireccionTesis.findByNombre").setParameter("nombre", name).getSingleResult();
            hibernateInitialize(dir);
            return dir;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
           DireccionTesis  dir= (DireccionTesis) em.createNamedQuery("DireccionTesis.findByNombre").setParameter("nombre", name).getResultList().get(0);
           hibernateInitialize(dir);
            return dir;
        }
    }

    private void hibernateInitialize(DireccionTesis e) {
        Hibernate.initialize(e);
        if(e.getEstadoTesis()!=null)
        Hibernate.initialize(e.getEstadoTesis());

        if (e.getAutorEstudiante() != null) {
            Hibernate.initialize(e.getAutorEstudiante());
            Hibernate.initialize(e.getAutorEstudiante().getPersona());
        }
        if (e.getNivelFormacionTesis() != null) {
            Hibernate.initialize(e.getNivelFormacionTesis());
        }
        if (e.getDirectorTesis() != null) {
            CargaProfesor carga = e.getDirectorTesis();
            Hibernate.initialize(carga);
            if (carga.getProfesor() != null) {
                Hibernate.initialize(carga.getProfesor());

                if (carga.getProfesor().getPersona() != null) {
                    Hibernate.initialize(carga.getProfesor().getPersona());
                }
            }

        }
    }
}
