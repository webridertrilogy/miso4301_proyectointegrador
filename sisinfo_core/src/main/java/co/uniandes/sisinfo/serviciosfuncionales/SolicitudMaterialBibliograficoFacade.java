/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.SolicitudMaterialBibliografico;
import java.sql.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Marcela
 */
@Stateless
public class SolicitudMaterialBibliograficoFacade implements SolicitudMaterialBibliograficoFacadeLocal, SolicitudMaterialBibliograficoFacadeRemote {
	@PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(SolicitudMaterialBibliografico solicitudMaterialBibliografico) {
        em.persist(solicitudMaterialBibliografico);
    }

    @Override
    public void edit(SolicitudMaterialBibliografico solicitudMaterialBibliografico) {
        em.merge(solicitudMaterialBibliografico);
    }

    @Override
    public void remove(SolicitudMaterialBibliografico solicitudMaterialBibliografico) {
        em.remove(solicitudMaterialBibliografico);
    }

    @Override
    public SolicitudMaterialBibliografico find(Object id) {
        return em.find(SolicitudMaterialBibliografico.class, id);
    }

    @Override
    public List<SolicitudMaterialBibliografico> findAll() {
        return em.createQuery("select object(o) from SolicitudMaterialBibliografico as o").getResultList();
    }

    @Override
    public SolicitudMaterialBibliografico findById(Long id) {
        try {
            return (SolicitudMaterialBibliografico) em.createNamedQuery("SolicitudMaterialBibliografico.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (SolicitudMaterialBibliografico) em.createNamedQuery("SolicitudMaterialBibliografico.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    @Override
    public double findPrecioPromedioByRangoFecha(Date fechaInicio, Date fechaFin) {
        try {
            Query query = em.createNamedQuery("SolicitudMaterialBibliografico.findPrecioPromedioByRangoFecha");
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return Double.valueOf(query.getSingleResult().toString());
        } catch (NoResultException e) {
            return 0;
        } catch (NonUniqueResultException e) {
            Query query = em.createNamedQuery("SolicitudMaterialBibliografico.findPrecioPromedioByRangoFecha");
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return Double.valueOf(query.getResultList().get(0).toString());
        }
    }

    @Override
    public double findPrecioTotalByRangoFecha(Date fechaInicio, Date fechaFin) {
        try {
            Query query = em.createNamedQuery("SolicitudMaterialBibliografico.findPrecioTotalByRangoFecha");
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return Double.valueOf(query.getSingleResult().toString());
        } catch (NoResultException e) {
            return 0;
        } catch (NonUniqueResultException e) {
            Query query = em.createNamedQuery("SolicitudMaterialBibliografico.findPrecioTotalByRangoFecha");
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return Double.valueOf(query.getResultList().get(0).toString());
        }
    }

    @Override
    public List<SolicitudMaterialBibliografico> findyByEstado(String estado) {
        return (List<SolicitudMaterialBibliografico>) em.createNamedQuery("SolicitudMaterialBibliografico.findyByEstado").setParameter("estado", estado).getResultList();
    }

    @Override
    public List<SolicitudMaterialBibliografico> findByRangoFecha(Date fechaInicio, Date fechaFin) {
        Query query = em.createNamedQuery("SolicitudMaterialBibliografico.findByRangoFecha");
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return (List<SolicitudMaterialBibliografico>) query.getResultList();
    }

    @Override
    public List<SolicitudMaterialBibliografico> findByAnioPublicacion(int anio) {
        return (List<SolicitudMaterialBibliografico>) em.createNamedQuery("SolicitudMaterialBibliografico.findByAnioPublicacion").setParameter("anio", anio).getResultList();
    }

    @Override
    public List<SolicitudMaterialBibliografico> findyByProveedor(String proveedor) {
        return (List<SolicitudMaterialBibliografico>) em.createNamedQuery("SolicitudMaterialBibliografico.findyByProveedor").setParameter("proveedor", proveedor).getResultList();
    }

    @Override
    public List<SolicitudMaterialBibliografico> findByRangoPrecio(double precioInicio, double precioFin) {
        Query query = em.createNamedQuery("SolicitudMaterialBibliografico.findByRangoPrecio");
        query.setParameter("precioInicio", precioInicio);
        query.setParameter("precioFin", precioFin);
        return (List<SolicitudMaterialBibliografico>) query.getResultList();
    }

    @Override
    public List<SolicitudMaterialBibliografico> findyBySolicitante(String correoSolicitante) {
        Query query = em.createNamedQuery("SolicitudMaterialBibliografico.findyBySolicitante");
        query.setParameter("correo", correoSolicitante);
        
        return (List<SolicitudMaterialBibliografico>) query.getResultList();
    }

}
