/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ModuloSisinfo;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class ModuloSisinfoFacade extends AbstractFacade<ModuloSisinfo> implements ModuloSisinfoFacadeLocal, ModuloSisinfoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ModuloSisinfoFacade() {
        super(ModuloSisinfo.class);
    }

    public Collection<ModuloSisinfo> buscarModulosPublicos() {
        Collection <ModuloSisinfo> lista =  em.createNamedQuery("ModuloSisinfo.findByPublicos").getResultList();

          return lista;
    }



}
