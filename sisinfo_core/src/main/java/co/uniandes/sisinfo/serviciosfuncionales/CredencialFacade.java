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

import co.uniandes.sisinfo.entities.Credencial;
import co.uniandes.sisinfo.serviciosfuncionales.CredencialFacadeRemote;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 * Servicio Entidad Credencial
 */
@Stateless
@EJB(name = "CredencialFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.CredencialFacadeLocal.class)
public class CredencialFacade implements CredencialFacadeLocal, CredencialFacadeRemote {
    @PersistenceContext
    private EntityManager em;

    public void create(Credencial credencial) {
        em.persist(credencial);
    }

    public void edit(Credencial credencial) {
        em.merge(credencial);
    }

    public void remove(Credencial credencial) {
        em.remove(em.merge(credencial));
    }

    public Credencial find(Object id) {
        return em.find(Credencial.class, id);
    }

    public List<Credencial> findAll() {
        return em.createQuery("select object(o) from Credencial as o").getResultList();
    }

    public Credencial findByCuenta(String cuenta) {
        try{
        return (Credencial) em.createNamedQuery("Credencial.findByCuenta").setParameter("cuenta", cuenta).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Credencial) em.createNamedQuery("Credencial.findByCuenta").setParameter("cuenta", cuenta).getResultList().get(0);

        }
    }

    public void removeAll() {
        List<Credencial> lista = findAll();
        for (Iterator<Credencial> it = lista.iterator(); it.hasNext();) {
            Credencial credencial = it.next();
            remove(credencial);
        }
    }



}
