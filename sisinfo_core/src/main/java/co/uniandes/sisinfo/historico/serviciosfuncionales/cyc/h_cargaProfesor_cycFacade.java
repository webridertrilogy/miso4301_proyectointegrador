/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_cargaProfesor_cyc;
import co.uniandes.sisinfo.historico.entities.cyc.h_direccion_tesis;
import co.uniandes.sisinfo.historico.entities.cyc.h_intencion_publicacion;
import co.uniandes.sisinfo.historico.entities.cyc.h_proyecto_financiado;
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
public class h_cargaProfesor_cycFacade implements h_cargaProfesor_cycFacadeLocal, h_cargaProfesor_cycFacadeRemote {
    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_cargaProfesor_cyc h_cargaProfesor_cyc) {
        em.persist(h_cargaProfesor_cyc);
    }

    public void edit(h_cargaProfesor_cyc h_cargaProfesor_cyc) {
        em.merge(h_cargaProfesor_cyc);
    }

    public void remove(h_cargaProfesor_cyc h_cargaProfesor_cyc) {
        em.remove(em.merge(h_cargaProfesor_cyc));
    }

    public h_cargaProfesor_cyc find(Object id) {
        h_cargaProfesor_cyc t= em.find(h_cargaProfesor_cyc.class, id);
        hibernateInitialize(t);
        return t;
    }

    public List<h_cargaProfesor_cyc> findAll() {
         List<h_cargaProfesor_cyc> temp= em.createQuery("select object(o) from h_cargaProfesor_cyc as o").getResultList();
         for (h_cargaProfesor_cyc profesor_cyc : temp) {
            hibernateInitialize(profesor_cyc);
        }
         return temp;
    }

    public List<h_cargaProfesor_cyc> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_cargaProfesor_cyc as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_cargaProfesor_cyc as o").getSingleResult()).intValue();
    }

    public h_cargaProfesor_cyc findCargaByCorreoProfesorYNombrePeriodo(String correo, String nombrePeriodo) {
         List<h_cargaProfesor_cyc> temp = em.createNamedQuery("h_cargaProfesor_cyc.findByCorreoYPeriodo").setParameter("correo", correo).setParameter("periodo", nombrePeriodo).getResultList();
        for (h_cargaProfesor_cyc cargaProfesor : temp) {
            if (cargaProfesor != null) {
                hibernateInitialize(cargaProfesor);
                return cargaProfesor;
            }
        }
        return null;
    }
      private void hibernateInitialize(h_cargaProfesor_cyc e) {
        Hibernate.initialize(e);
          System.out.println("carga=" + e.getNombres() + " "+ e.getApellidos()+ " id="+ e.getId());
        Hibernate.initialize(e.getCursos());
        Hibernate.initialize(e.getTesisAcargo());
        Hibernate.initialize(e.getIntencionPublicaciones());
        Hibernate.initialize(e.getEventos());
        Hibernate.initialize(e.getOtros());
       
        if (e.getTesisAcargo() != null) {
            Collection<h_direccion_tesis> col = e.getTesisAcargo();
            for (h_direccion_tesis direccionTesis : col) {
                Hibernate.initialize(direccionTesis);
            }
        }
        if (e.getIntencionPublicaciones() != null) {
            Collection<h_intencion_publicacion> col = e.getIntencionPublicaciones();
            for (h_intencion_publicacion direccionTesis : col) {
                Hibernate.initialize(direccionTesis);
                if (direccionTesis.getCoAutores() != null) {
                    Collection<h_cargaProfesor_cyc> coautores = direccionTesis.getCoAutores();
                    for (h_cargaProfesor_cyc cargaProfesor : coautores) {
                        Hibernate.initialize(cargaProfesor);
                    }
                }
            }
        }
        if (e.getProyectosFinanciados() != null) {
            Collection<h_proyecto_financiado> col = e.getProyectosFinanciados();
            for (h_proyecto_financiado direccionTesis : col) {
                Hibernate.initialize(direccionTesis);
                if (direccionTesis.getProfesores() != null) {//TODO: solo para test
                    Collection<h_cargaProfesor_cyc> coautores = direccionTesis.getProfesores();
                    for (h_cargaProfesor_cyc cargaProfesor : coautores) {
                        Hibernate.initialize(cargaProfesor);
                    }
                }
            }
        }
       


    }

    public List< h_cargaProfesor_cyc> findByCorreo(String correo) {
         List<h_cargaProfesor_cyc> temp = em.createNamedQuery("h_cargaProfesor_cyc.findByCorreo").setParameter("correo", correo).getResultList();
        for (h_cargaProfesor_cyc cargaProfesor : temp) {
            hibernateInitialize(cargaProfesor);
        }
        return temp;
    }





    



}
