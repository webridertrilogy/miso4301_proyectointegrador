/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import co.uniandes.sisinfo.entities.ParametroAceptado;

/**
 *
 * @author david
 */
@Stateless
public class ParametroAceptadoFacade implements ParametroAceptadoFacadeLocal {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(ParametroAceptado parametroAceptado) {
        em.persist(parametroAceptado);
    }

    public void edit(ParametroAceptado parametroAceptado) {
        em.merge(parametroAceptado);
    }

    public void remove(ParametroAceptado parametroAceptado) {
        em.remove(em.merge(parametroAceptado));
    }

    public ParametroAceptado find(Object id) {
        return em.find(ParametroAceptado.class, id);
    }

    public List<ParametroAceptado> findAll() {
        return em.createQuery("select object(o) from ParametroAceptado as o").getResultList();
    }

    public List<ParametroAceptado> findByTipoTarea(String tipo) {
        try{
            return em.createNamedQuery("ParametroAceptado.findByTipoAlerta").setParameter("tipoAlerta", tipo).getResultList();
        }catch(NoResultException nre){
            return null;
        }
    }



}
