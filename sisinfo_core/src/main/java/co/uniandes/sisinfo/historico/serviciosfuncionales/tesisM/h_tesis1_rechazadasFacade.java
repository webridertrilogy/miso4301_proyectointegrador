/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis1_rechazadas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ivan Melo
 */
@Stateless
public class h_tesis1_rechazadasFacade implements h_tesis1_rechazadasFacadeLocal, h_tesis1_rechazadasFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_tesis1_rechazadas h_tesis1_rechazadas) {
        em.persist(h_tesis1_rechazadas);
    }

    public void edit(h_tesis1_rechazadas h_tesis1_rechazadas) {
        em.merge(h_tesis1_rechazadas);
    }

    public void remove(h_tesis1_rechazadas h_tesis1_rechazadas) {
        em.remove(em.merge(h_tesis1_rechazadas));
    }

    public h_tesis1_rechazadas find(Object id) {
        return em.find(h_tesis1_rechazadas.class, id);
    }

    public List<h_tesis1_rechazadas> findAll() {
        return em.createQuery("select object(o) from h_tesis1_rechazadas as o").getResultList();
    }

    public List<h_tesis1_rechazadas> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_tesis1_rechazadas as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_tesis1_rechazadas as o").getSingleResult()).intValue();
    }

    public List<h_tesis1_rechazadas> findByCorreoEstudiante(String correo) {

         List<h_tesis1_rechazadas> temp = em.createNamedQuery("h_tesis1_rechazadas.findByCorreoEstudiante").setParameter("correo", correo).setParameter("correo", correo).getResultList();

        return temp;

    }

    public List<h_tesis1_rechazadas> findByCorreoAsesor(String correo) {
         List<h_tesis1_rechazadas> temp = em.createNamedQuery("h_tesis1_rechazadas.findByCorreoasesor").setParameter("correo", correo).setParameter("correo", correo).getResultList();

        return temp;
    }





}
