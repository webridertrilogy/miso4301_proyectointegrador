/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_cargaProfesor_cyc;
import co.uniandes.sisinfo.historico.entities.cyc.h_direccion_tesis;
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
public class h_direccion_tesisFacade implements h_direccion_tesisFacadeLocal, h_direccion_tesisFacadeRemote {
    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_direccion_tesis h_direccion_tesis) {
        em.persist(h_direccion_tesis);
    }

    public void edit(h_direccion_tesis h_direccion_tesis) {
        em.merge(h_direccion_tesis);
    }

    public void remove(h_direccion_tesis h_direccion_tesis) {
        em.remove(em.merge(h_direccion_tesis));
    }

    public h_direccion_tesis find(Object id) {
        h_direccion_tesis bau= em.find(h_direccion_tesis.class, id);
        hibernateInitialize(bau);
        return bau;
    }

    public List<h_direccion_tesis> findAll() {
        List<h_direccion_tesis> bau= em.createQuery("select object(o) from h_direccion_tesis as o").getResultList();
        for (h_direccion_tesis object : bau) {
            hibernateInitialize(object);
        }
        return bau;
    }

    public List<h_direccion_tesis> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_direccion_tesis as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_direccion_tesis as o").getSingleResult()).intValue();
    }

    public h_direccion_tesis findDireccionTesisByName(String name) {
       try {
            h_direccion_tesis  dir=(h_direccion_tesis) em.createNamedQuery("h_direccion_tesis.findByNombre").setParameter("nombre", name).getSingleResult();
            hibernateInitialize(dir);
            return dir;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
           h_direccion_tesis  dir= (h_direccion_tesis) em.createNamedQuery("h_direccion_tesis.findByNombre").setParameter("nombre", name).getResultList().get(0);
           hibernateInitialize(dir);
            return dir;
        }
    }

    private void hibernateInitialize(h_direccion_tesis e) {
        Hibernate.initialize(e);


        if (e.getDirectorTesis() != null) {
            h_cargaProfesor_cyc carga = e.getDirectorTesis();
            Hibernate.initialize(carga);
        }
    }




    /*
     *  

    
     */

}
