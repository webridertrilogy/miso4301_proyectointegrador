/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Asistente
 */
@Stateless
public class InformacionAcademicaFacade implements InformacionAcademicaFacadeLocal, InformacionAcademicaFacadeRemote {
    @PersistenceContext(unitName = "DatosMaestrosPU")
    private EntityManager em;

    public void create(InformacionAcademica informacionAcademica) {
        em.persist(informacionAcademica);
    }

    public void edit(InformacionAcademica informacionAcademica) {
        em.merge(informacionAcademica);
    }

    public void remove(InformacionAcademica informacionAcademica) {
        em.remove(em.merge(informacionAcademica));
    }

    public InformacionAcademica find(Object id) {
        return em.find(InformacionAcademica.class, id);
    }

    public List<InformacionAcademica> findAll() {
        return em.createQuery("select object(o) from InformacionAcademica as o").getResultList();
    }

    public List<InformacionAcademica> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from InformacionAcademica as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from InformacionAcademica as o").getSingleResult()).intValue();
    }

    public InformacionAcademica findByCodigoEstudiante(String codigo) {
         try {

            InformacionAcademica f= (InformacionAcademica)em.createNamedQuery("InformacionAcademica.findByCodigo").setParameter("codigo", codigo).getSingleResult();

            return f;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            InformacionAcademica facultad =  (InformacionAcademica) em.createNamedQuery("InformacionAcademica.findByCodigo").setParameter("codigo", codigo).getResultList().get(0);

            return facultad;
        }
    }

}
