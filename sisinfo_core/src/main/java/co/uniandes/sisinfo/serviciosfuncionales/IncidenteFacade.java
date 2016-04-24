
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.Incidente;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class IncidenteFacade extends AbstractFacade<Incidente> implements IncidenteFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public IncidenteFacade() {
        super(Incidente.class);
    }

    public Incidente findByDescripcionyFecha(String descripcionIncidente, Timestamp fechaIncidente) {
        try {
            Incidente i = (Incidente) em.createNamedQuery("Incidente.findByDescripcionYFecha").setParameter("descripcionIncidente", descripcionIncidente).setParameter("fechaIncidente", fechaIncidente).getSingleResult();
            hibernateInitialize(i);
            return i;
        } catch (NonUniqueResultException e) {
            List<Incidente> list= em.createNamedQuery("Incidente.findByDescripcionYGecha").setParameter("descripcionIncidente", descripcionIncidente).setParameter("fechaIncidente", fechaIncidente).getResultList();
            Incidente i = list.get(0);
            hibernateInitialize(i);
            return i;
        } catch (NoResultException e) {
            return null;
        }
    }

     private void hibernateInitialize(Incidente i) {
        Hibernate.initialize(i);
        if (i.getReportadoPor() != null) {
            Hibernate.initialize(i.getReportadoPor());
            Hibernate.initialize(i.getPersonaSoporte());
        }
    }

     public Collection<Incidente> findByCorreoReportante(String correo){
          Collection <Incidente> lista =  em.createNamedQuery("Incidente.findByCorreoReportante").setParameter("correo", correo).getResultList();
          for (Incidente incidente : lista) {
             hibernateInitialize(incidente);
         }
          return lista;
     }

     public Collection<Incidente> findByEstado(String estado){
           Collection <Incidente> lista =  em.createNamedQuery("Incidente.findByEstado").setParameter("estado", estado).getResultList();
          for (Incidente incidente : lista) {
             hibernateInitialize(incidente);
         }
          return lista;
     }

    public Collection<Incidente> findByNoBorrado() {
        Collection <Incidente> lista =  em.createNamedQuery("Incidente.findByNOBorrado").getResultList();
          for (Incidente incidente : lista) {
             hibernateInitialize(incidente);
         }
          return lista;
    }

    /**
     * Consulta incidentes por el Id de la persona de soporte.
     * @return
     */
    public List<Incidente> findByPersonaSoporte(Long idPersonaSoporte) {

         List <Incidente> lista =  em.createNamedQuery("Incidente.findByPersonaSoporte").setParameter("idPersonaSoporte", idPersonaSoporte).getResultList();
          for (Incidente incidente : lista) {
             hibernateInitialize(incidente);
         }
          return lista;
    }
}
