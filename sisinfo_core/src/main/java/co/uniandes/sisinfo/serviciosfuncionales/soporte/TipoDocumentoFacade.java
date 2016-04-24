/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoDocumento;

/**
 *
 * @author lj.bautista31
 */
@Stateless
public class TipoDocumentoFacade implements TipoDocumentoFacadeLocal {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(TipoDocumento tipoDocumento) {
        em.persist(tipoDocumento);
    }

    public void edit(TipoDocumento tipoDocumento) {
        em.merge(tipoDocumento);
    }

    public void remove(TipoDocumento tipoDocumento) {
        em.remove(em.merge(tipoDocumento));
    }

    public TipoDocumento find(Object id) {
        return em.find(TipoDocumento.class, id);
    }

    public List<TipoDocumento> findAll() {
        return em.createQuery("select object(o) from TipoDocumento as o").getResultList();
    }

    @Override
    public TipoDocumento findByTipo(String tipo) {
        try {
            TipoDocumento tipoDocumento = (TipoDocumento) em.createNamedQuery("TipoDocumento.findByTipo").setParameter("tipo", tipo).getSingleResult();
            hibernateInitialize(tipoDocumento);
            Hibernate.initialize(tipoDocumento.getTipo());
            return tipoDocumento;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            TipoDocumento aspirante = (TipoDocumento) em.createNamedQuery("TipoDocumento.findByTipo").setParameter("tipo", tipo).getResultList().get(0);
            hibernateInitialize(aspirante);
            return aspirante;
        }
    }



    private void hibernateInitialize(TipoDocumento td)
    {
        Hibernate.initialize(td);
    }

    public TipoDocumento findByDescripcion(String descripcion) {
        try {
            TipoDocumento tipoDocumento = (TipoDocumento) em.createNamedQuery("TipoDocumento.findByDescripcion").setParameter("descripcion", descripcion).getSingleResult();
            //hibernateInitialize(tipoDocumento);
            return tipoDocumento;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            TipoDocumento aspirante = (TipoDocumento) em.createNamedQuery("TipoDocumento.findByDescripcion").setParameter("descripcion", descripcion).getResultList().get(0);
            hibernateInitialize(aspirante);
            return aspirante;
        }
    }

}
