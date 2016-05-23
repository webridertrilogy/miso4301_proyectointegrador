/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CargaProfesor;
import co.uniandes.sisinfo.entities.DireccionTesis;
import co.uniandes.sisinfo.entities.IntencionPublicacion;
import co.uniandes.sisinfo.entities.ProyectoFinanciado;
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
public class CargaProfesorFacade implements CargaProfesorFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(CargaProfesor cargaProfesor) {
        em.persist(cargaProfesor);
    }

    public void edit(CargaProfesor cargaProfesor) {
        em.merge(cargaProfesor);
    }

    public void remove(CargaProfesor cargaProfesor) {
        em.remove(em.merge(cargaProfesor));
    }

    public CargaProfesor find(Object id) {
        CargaProfesor cargaProfesor = em.find(CargaProfesor.class, id);
        hibernateInitialize(cargaProfesor);
        return cargaProfesor;
    }

    public List<CargaProfesor> findAll() {
        List<CargaProfesor> listaCarga = em.createQuery("select object(o) from CargaProfesor as o").getResultList();
        for (CargaProfesor cargaProfesor : listaCarga) {
            hibernateInitialize(cargaProfesor);
        }
        return listaCarga;
    }

    public List<CargaProfesor> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from CargaProfesor as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from CargaProfesor as o").getSingleResult()).intValue();
    }

    public List<CargaProfesor> findByCorreo(String correo) {
        List<CargaProfesor> temp = em.createNamedQuery("CargaProfesor.findByCorreo").setParameter("correo", correo).getResultList();
        for (CargaProfesor cargaProfesor : temp) {
         //   hibernateInitialize(cargaProfesor);
        }
        return temp;
    }

    private void hibernateInitialize(CargaProfesor e) {
//        Hibernate.initialize(e);
//        Hibernate.initialize(e.getProfesor());
//        Hibernate.initialize(e.getCursos());
//        Hibernate.initialize(e.getTesisAcargo());
//        Hibernate.initialize(e.getIntencionPublicaciones());
//        Hibernate.initialize(e.getEventos());
//        Hibernate.initialize(e.getOtros());
//        if (e.getProfesor() != null) {
//            Hibernate.initialize(e.getProfesor().getPersona());
//        }
//        if (e.getTesisAcargo() != null) {
//            Collection<DireccionTesis> col = e.getTesisAcargo();
//            for (DireccionTesis direccionTesis : col) {
//                Hibernate.initialize(direccionTesis);
//            }
//        }
//        if (e.getIntencionPublicaciones() != null) {
//            Collection<IntencionPublicacion> col = e.getIntencionPublicaciones();
//            for (IntencionPublicacion direccionTesis : col) {
//                Hibernate.initialize(direccionTesis);
//                if (direccionTesis.getCoAutores() != null) {
//                    Collection<CargaProfesor> coautores = direccionTesis.getCoAutores();
//                    for (CargaProfesor cargaProfesor : coautores) {
//                        Hibernate.initialize(cargaProfesor);
//                    }
//                }
//            }
//        }
//        if (e.getProyectosFinanciados() != null) {
//            Collection<ProyectoFinanciado> col = e.getProyectosFinanciados();
//            for (ProyectoFinanciado direccionTesis : col) {
//                Hibernate.initialize(direccionTesis);
//                if (direccionTesis.getProfesores() != null) {//TODO: solo para test
//                    Collection<CargaProfesor> coautores = direccionTesis.getProfesores();
//                    for (CargaProfesor cargaProfesor : coautores) {
//                        Hibernate.initialize(cargaProfesor);
//                    }
//                }
//            }
//        }
//        if (e.getDescarga() != null) {
//            Hibernate.initialize(e.getDescarga());
//        }


    }

    public List<CargaProfesor> findByidProfesorYPeriodo(Long idProfesor, String periodoCarga) {

        List<CargaProfesor> temp = em.createNamedQuery("CargaProfesor.findByIdProfesorAndPeriodo").setParameter("idProfesor", idProfesor).setParameter("periodo", periodoCarga).getResultList();
        for (CargaProfesor cargaProfesor : temp) {
            hibernateInitialize(cargaProfesor);
        }
        return temp;
    }

    public List<CargaProfesor> findByPeriodo(String periodoCarga) {

        List<CargaProfesor> temp = em.createNamedQuery("CargaProfesor.findByPeriodo").setParameter("periodo", periodoCarga).getResultList();
        for (CargaProfesor cargaProfesor : temp) {
            hibernateInitialize(cargaProfesor);
        }
        return temp;
    }

    public CargaProfesor findCargaByCorreoProfesorYNombrePeriodo(String correo, String nombrePeriodo) {
        List<CargaProfesor> temp = em.createNamedQuery("CargaProfesor.findByCorreoYPeriodo").setParameter("correo", correo).setParameter("periodo", nombrePeriodo).getResultList();
        for (CargaProfesor cargaProfesor : temp) {
            if (cargaProfesor != null) {
                hibernateInitialize(cargaProfesor);
                return cargaProfesor;
            }
        }
        return null;
    }

    public void flushear() {
        em.flush();
    }



}
