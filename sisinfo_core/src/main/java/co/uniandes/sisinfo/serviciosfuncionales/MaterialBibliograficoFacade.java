/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.MaterialBibliografico;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Marcela
 */
@Stateless
public class MaterialBibliograficoFacade implements MaterialBibliograficoFacadeLocal {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(MaterialBibliografico materialBibliografico) {
        em.persist(materialBibliografico);
    }

    public void edit(MaterialBibliografico materialBibliografico) {
        em.merge(materialBibliografico);
    }

    public void remove(MaterialBibliografico materialBibliografico) {
        em.remove(em.merge(materialBibliografico));
    }

    public MaterialBibliografico find(Object id) {
        return em.find(MaterialBibliografico.class, id);
    }

    public List<MaterialBibliografico> findAll() {
        return em.createQuery("select object(o) from MaterialBibliografico as o").getResultList();
    }

}
