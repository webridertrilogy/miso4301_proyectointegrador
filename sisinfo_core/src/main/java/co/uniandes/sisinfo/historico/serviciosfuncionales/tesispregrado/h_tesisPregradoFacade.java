package co.uniandes.sisinfo.historico.serviciosfuncionales.tesispregrado;

import co.uniandes.sisinfo.historico.entities.tesispregrado.h_tesisPregrado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios Fachada - Histórico de Proyecto de Grado
 * @author Marcela Morales
 */
@Stateless
public class h_tesisPregradoFacade implements h_tesisPregradoFacadeLocal, h_tesisPregradoFacadeRemote {

    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_tesisPregrado h_tesisPregrado) {
        em.persist(h_tesisPregrado);
    }

    public void edit(h_tesisPregrado h_tesisPregrado) {
        em.merge(h_tesisPregrado);
    }

    public void remove(h_tesisPregrado h_tesisPregrado) {
        em.remove(em.merge(h_tesisPregrado));
    }

    public h_tesisPregrado find(Object id) {
        return em.find(h_tesisPregrado.class, id);
    }

    public List<h_tesisPregrado> findAll() {
        return em.createQuery("select object(o) from h_tesisPregrado as o").getResultList();
    }

    public List<h_tesisPregrado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_tesisPregrado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_tesisPregrado as o").getSingleResult()).intValue();
    }

    public List<h_tesisPregrado> findByCorreoEstudiante(String correo) {
        return em.createNamedQuery("h_tesisPregrado.findByCorreoEstudiante").setParameter("correoEstudiante", correo).getResultList();
    }

    public List<h_tesisPregrado> findByCorreoAsesor(String correo) {
        return em.createNamedQuery("h_tesisPregrado.findByCorreoAsesor").setParameter("correoAsesor", correo).getResultList();
    }
    
    public List<h_tesisPregrado> findByEstado(String estado) {
        return em.createNamedQuery("h_tesisPregrado.findByEstado").setParameter("estado", estado).getResultList();
    }

    public List<h_tesisPregrado> findBySemestre(String semestre) {
        return em.createNamedQuery("h_tesisPregrado.findBySemestre").setParameter("semestre", semestre).getResultList();
    }

    public List<h_tesisPregrado> findByEstadoYSemestre(String semestre, String estado) {
        Query q = em.createNamedQuery("h_tesisPregrado.findByEstadoYSemestre");
        q.setParameter("semestre", semestre);
        q.setParameter("estado", estado);
        return q.getResultList();
    }

    public List<h_tesisPregrado> findByEstadoYSemestreYCorreoAsesor(String semestre, String estado, String correo) {
        Query q = em.createNamedQuery("h_tesisPregrado.findByEstadoYSemestreYCorreoAsesor");
        q.setParameter("semestre", semestre);
        q.setParameter("estado", estado);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }

    public List<h_tesisPregrado> findBySemestreYCorreoAsesor(String semestre, String correo) {
        Query q = em.createNamedQuery("h_tesisPregrado.findBySemestreYCorreoAsesor");
        q.setParameter("semestre", semestre);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }

    public List<h_tesisPregrado> findByEstadoYCorreoAsesor(String estado, String correo) {
        Query q = em.createNamedQuery("h_tesisPregrado.findByEstadoYCorreoAsesor");
        q.setParameter("estado", estado);
        q.setParameter("correoAsesor", correo);
        return q.getResultList();
    }
}
