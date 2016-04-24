/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TemaTesis1;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;


/**
 *
 * @author Ivan Mauricio Melo S
 */
@Stateless
public class TemaTesis1Facade implements TemaTesis1FacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(TemaTesis1 temaTesis1) {
        em.persist(temaTesis1);
    }

    public void edit(TemaTesis1 temaTesis1) {
        em.merge(temaTesis1);
    }

    public void remove(TemaTesis1 temaTesis1) {
        em.remove(em.merge(temaTesis1));
    }

    public TemaTesis1 find(Object id) {
        TemaTesis1 t= em.find(TemaTesis1.class, id);
        hibernateInitialize(t);
        return t;
    }

    public List<TemaTesis1> findAll() {
        List<TemaTesis1> list= em.createQuery("select object(o) from TemaTesis1 as o").getResultList();
        for (TemaTesis1 temaTesis1 : list) {
            hibernateInitialize(temaTesis1);
        }
        return list;
    }

    public List<TemaTesis1> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from TemaTesis1 as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        List<TemaTesis1> l = q.getResultList();
        for (TemaTesis1 temaTesis1 : l) {
            hibernateInitialize(temaTesis1);
        }
        return l;
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from TemaTesis1 as o").getSingleResult()).intValue();
    }

    private void hibernateInitialize(TemaTesis1 i) {
//        Hibernate.initialize(i);
//        if (i.getAsesor() != null) {
//            Hibernate.initialize(i.getAsesor());
//            Hibernate.initialize(i.getAsesor().getPersona());
//        }
    }

    public List<TemaTesis1> findByCorreoAsesor(String correo) {
        List<TemaTesis1> lista= em.createNamedQuery("TemaTesis1.findBycorreoAsesor").setParameter("correoasesor", correo).getResultList();
        for (TemaTesis1 temaTesis1 : lista) {
            hibernateInitialize(temaTesis1);
        }
        return lista;
    }

    public List<TemaTesis1> findByNombreTema(String temaTesis) {
        List<TemaTesis1> lista= em.createNamedQuery("TemaTesis1.findByNombreTemaTesis").setParameter("nombre", temaTesis).getResultList();
        for (TemaTesis1 temaTesis1 : lista) {
            hibernateInitialize(temaTesis1);
        }
        return lista;
    }



    public List<TemaTesis1> findByPeriodoTesis(String semestre) {
        List<TemaTesis1> lista= em.createNamedQuery("TemaTesis1.findBySemestreTesis").setParameter("semestre", semestre).getResultList();
        for (TemaTesis1 temaTesis1 : lista) {
            hibernateInitialize(temaTesis1);
        }
        return lista;
    }








}
