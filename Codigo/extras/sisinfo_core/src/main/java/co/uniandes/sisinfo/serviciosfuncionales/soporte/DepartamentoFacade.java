/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.Ciudad;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Departamento;

/**
 *
 * @author Administrador
 */
@Stateless
public class DepartamentoFacade extends AbstractFacade<Departamento> implements DepartamentoFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public Departamento findDepartamentoByNombre(String nombre) {
        try {
            Departamento temp = (Departamento) em.createNamedQuery("Departamento.findDepartamentoByNombre").setParameter("nombre", nombre).getSingleResult();
            hibernateInitializeDepartamento(temp);
            return temp;
        } catch (NoResultException e) {
            return null;
        }

    }

    public DepartamentoFacade() {
        super(Departamento.class);
    }

    private void hibernateInitializeDepartamento(Departamento departamento) {
        Hibernate.initialize(departamento);
        Collection<Ciudad> ciudades = departamento.getCuidades();
        for (Ciudad ciudad : ciudades) {
            Hibernate.initialize(ciudad);
        }
    }
}
