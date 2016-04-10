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

import co.uniandes.sisinfo.entities.Lista_Negra;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 * Servicios Entidad Lista Negra
 */
@Stateless
@EJB(name = "Lista_NegraFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.Lista_NegraFacadeLocal.class)
public class Lista_NegraFacade implements Lista_NegraFacadeLocal, Lista_NegraFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Lista_Negra lista_Negra) {
        em.persist(lista_Negra);
    }

    @Override
    public void edit(Lista_Negra lista_Negra) {
        em.merge(lista_Negra);
    }

    @Override
    public void remove(Lista_Negra lista_Negra) {
        em.remove(em.merge(lista_Negra));
    }

    @Override
    public Lista_Negra find(Object id) {
        return em.find(Lista_Negra.class, id);
    }

    @Override
    public List<Lista_Negra> findAll() {
        return em.createQuery("select object(o) from Lista_Negra as o").getResultList();
    }

    @Override
    public Lista_Negra findEstudianteByCodigo(String codigo) {
        try {
            return (Lista_Negra) em.createNamedQuery("Lista_Negra.findAspiranteByCodigo").setParameter("codigo", codigo).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Lista_Negra) em.createNamedQuery("Lista_Negra.findAspiranteByCodigo").setParameter("codigo", codigo).getResultList().get(0);
        }
    }

    public Lista_Negra findEstudianteByCorreo(String correo) {
        try {
            return (Lista_Negra) em.createNamedQuery("Lista_Negra.findAspiranteByCorreo").setParameter("correo", correo).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Lista_Negra) em.createNamedQuery("Lista_Negra.findAspiranteByCorreo").setParameter("correo", correo).getResultList().get(0);
        }
    }


    public Collection<Lista_Negra> findEstudiantesTemporales() {
        return (Collection<Lista_Negra>) em.createNamedQuery("Lista_Negra.findAspirantesTemporales").getResultList();

    }


}
