package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_inscripcion_subarea;
import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_2;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios Fachada - Histórico de Inscripción Subarea
 * @author Paola Gómez
 */
@Stateless
public class h_InscripcionSubareaInvest_Facade implements h_InscripcionSubareaInvest_FacadeLocal, h_InscripcionSubareaInvest_FacadeRemote {

    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_inscripcion_subarea h_inscripcion_subarea) {
        em.persist(h_inscripcion_subarea);
    }

    public void edit(h_inscripcion_subarea h_inscripcion_subarea) {
        em.merge(h_inscripcion_subarea);
    }

    public void remove(h_inscripcion_subarea h_inscripcion_subarea) {
        em.remove(em.merge(h_inscripcion_subarea));
    }

    public h_inscripcion_subarea find(Object id) {
        return em.find(h_inscripcion_subarea.class, id);
    }

    public List<h_inscripcion_subarea> findAll() {
        return em.createQuery("select object(o) from h_inscripcion_subarea as o").getResultList();
    }

    public List<h_inscripcion_subarea> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_inscripcion_subarea as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_inscripcion_subarea as o").getSingleResult()).intValue();
    }

}
