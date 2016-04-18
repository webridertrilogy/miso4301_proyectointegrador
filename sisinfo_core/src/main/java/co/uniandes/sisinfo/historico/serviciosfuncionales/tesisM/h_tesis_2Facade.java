package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_2;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios Fachada - Histórico de Tesis 2
 * @author Marcela Morales
 */
@Stateless
public class h_tesis_2Facade implements h_tesis_2FacadeLocal, h_tesis_2FacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_tesis_2 h_tesis_2) {
        em.persist(h_tesis_2);
    }

    public void edit(h_tesis_2 h_tesis_2) {
        em.merge(h_tesis_2);
    }

    public void remove(h_tesis_2 h_tesis_2) {
        em.remove(em.merge(h_tesis_2));
    }

    public h_tesis_2 find(Object id) {
        return em.find(h_tesis_2.class, id);
    }

    public List<h_tesis_2> findAll() {
        return em.createQuery("select object(o) from h_tesis_2 as o").getResultList();
    }

    public List<h_tesis_2> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_tesis_2 as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_tesis_2 as o").getSingleResult()).intValue();
    }

    public List<h_tesis_2> findByCorreoEstudiante(String correo) {
        return em.createNamedQuery("h_tesis_2.findByCorreoEstudiante").setParameter("correo", correo).getResultList();
    }

    public List<h_tesis_2> findByCorreoAsesor(String correo) {
        return em.createNamedQuery("h_tesis_2.findByCorreoAsesor").setParameter("correoAsesor", correo).getResultList();
    }
    
    public List<h_tesis_2> findByEstado(String estado) {
        return em.createNamedQuery("h_tesis_2.findByEstado").setParameter("estado", estado).getResultList();
    }

    public List<h_tesis_2> findBySemestre(String semestre) {
        return em.createNamedQuery("h_tesis_2.findBySemestre").setParameter("semestre", semestre).getResultList();
    }

    public List<h_tesis_2> findByEstadoYSemestre(String semestre, String estado) {
        Query q = em.createNamedQuery("h_tesis_2.findByEstadoYSemestre");
        q.setParameter("semestre", semestre);
        q.setParameter("estado", estado);
        return q.getResultList();
    }

    public List<h_tesis_2> findByEstadoYSemestreYCorreoAsesor(String semestre, String estado, String correo) {
        Query q = em.createNamedQuery("h_tesis_2.findByEstadoYSemestreYCorreoAsesor");
        q.setParameter("semestre", semestre);
        q.setParameter("estado", estado);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }

    public List<h_tesis_2> findBySemestreYCorreoAsesor(String semestre, String correo) {
        Query q = em.createNamedQuery("h_tesis_2.findBySemestreYCorreoAsesor");
        q.setParameter("semestre", semestre);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }

    public List<h_tesis_2> findByEstadoYCorreoAsesor(String estado, String correo) {
        Query q = em.createNamedQuery("h_tesis_2.findByEstadoYCorreoAsesor");
        q.setParameter("estado", estado);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }
}
