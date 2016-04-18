package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_1;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios Fachada - Histórico de Tesis 
 * @author Marcela Morales
 */
@Stateless
public class h_tesis_1Facade implements h_tesis_1FacadeLocal, h_tesis_1FacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_tesis_1 h_tesis_1) {
        em.persist(h_tesis_1);
    }

    public void edit(h_tesis_1 h_tesis_1) {
        em.merge(h_tesis_1);
    }

    public void remove(h_tesis_1 h_tesis_1) {
        em.remove(em.merge(h_tesis_1));
    }

    public h_tesis_1 find(Object id) {
        return em.find(h_tesis_1.class, id);
    }

    public List<h_tesis_1> findAll() {
        return em.createQuery("select object(o) from h_tesis_1 as o").getResultList();
    }

    public List<h_tesis_1> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_tesis_1 as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_tesis_1 as o").getSingleResult()).intValue();
    }

    public List<h_tesis_1> findByCorreoEstudiante(String correo) {
        return em.createNamedQuery("h_tesis_1.findByCorreoEstudiante").setParameter("correo", correo).getResultList();
    }

    public List<h_tesis_1> findByCorreoAsesor(String correo) {
        return em.createNamedQuery("h_tesis_1.findByCorreoAsesor").setParameter("correoAsesor", correo).getResultList();
    }
    
    public List<h_tesis_1> findByEstado(String estado) {
        return em.createNamedQuery("h_tesis_1.findByEstado").setParameter("estado", estado).getResultList();
    }

    public List<h_tesis_1> findBySemestre(String semestre) {
        return em.createNamedQuery("h_tesis_1.findBySemestre").setParameter("semestre", semestre).getResultList();
    }

    public List<h_tesis_1> findByEstadoYSemestre(String semestre, String estado) {
        Query q = em.createNamedQuery("h_tesis_1.findByEstadoYSemestre");
        q.setParameter("semestre", semestre);
        q.setParameter("estado", estado);
        return q.getResultList();
    }

    public List<h_tesis_1> findByEstadoYSemestreYCorreoAsesor(String semestre, String estado, String correo) {
        Query q = em.createNamedQuery("h_tesis_1.findByEstadoYSemestreYCorreoAsesor");
        q.setParameter("semestre", semestre);
        q.setParameter("estado", estado);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }

    public List<h_tesis_1> findBySemestreYCorreoAsesor(String semestre, String correo) {
        Query q = em.createNamedQuery("h_tesis_1.findBySemestreYCorreoAsesor");
        q.setParameter("semestre", semestre);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }

    public List<h_tesis_1> findByEstadoYCorreoAsesor(String estado, String correo) {
        Query q = em.createNamedQuery("h_tesis_1.findByEstadoYCorreoAsesor");
        q.setParameter("estado", estado);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }
}
