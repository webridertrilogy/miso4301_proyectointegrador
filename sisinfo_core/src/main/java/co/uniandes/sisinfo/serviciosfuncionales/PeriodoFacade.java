/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;

import co.uniandes.sisinfo.entities.Periodo;

/**
 * Servicios Entidad Periodo
 */
@Stateless
@EJB(name = "PeriodoFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeLocal.class)
public class PeriodoFacade implements PeriodoFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Periodo periodo) {
        em.persist(periodo);
    }

    @Override
    public void edit(Periodo periodo) {
        em.merge(periodo);
    }

    @Override
    public void remove(Periodo periodo) {
        em.remove(em.merge(periodo));
    }

    @Override
    public Periodo find(Object id) {
        Periodo periodo = em.find(Periodo.class, id);
        hibernateInitialize(periodo);
        return periodo;
    }

    @Override
    public List<Periodo> findAll() {
        List<Periodo> periodos = em.createQuery("select object(o) from Periodo as o").getResultList();
        Iterator<Periodo> iterator = periodos.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return periodos;
    }

    @Override
    public Periodo findByPeriodo(String periodoAcademico) {
        try {
            Periodo periodo = (Periodo) em.createNamedQuery("Periodo.findByPeriodo").setParameter("periodo", periodoAcademico).getSingleResult();
            hibernateInitialize(periodo);
            return periodo;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Periodo periodo = (Periodo) em.createNamedQuery("Periodo.findByPeriodo").setParameter("periodo", periodoAcademico).getResultList().get(0);
            hibernateInitialize(periodo);
            return periodo;
        }
    }

    @Override
    public Periodo findActual() {
        try {
            Periodo periodo = (Periodo) em.createNamedQuery("Periodo.findByActual").setParameter("actual", true).getSingleResult();
            hibernateInitialize(periodo);
            return periodo;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return null;
        }

    }

    /**
     * Inicializa el periodo y sus colecciones
     * @param periodo Periodo
     */
    private void hibernateInitialize(Periodo periodo){
        Hibernate.initialize(periodo);
        Hibernate.initialize(periodo.getConvocatoria());
    }
}
