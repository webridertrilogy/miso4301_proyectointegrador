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

import co.uniandes.sisinfo.entities.Monitoria_Solicitada;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios Entidad Monitoría Solicitada
 */
@Stateless
@EJB(name = "Monitoria_SolicitadaFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.Monitoria_SolicitadaFacadeLocal.class)
public class Monitoria_SolicitadaFacade implements Monitoria_SolicitadaFacadeLocal, Monitoria_SolicitadaFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Monitoria_Solicitada monitoria_Solicitada) {
        em.persist(monitoria_Solicitada);
    }

    @Override
    public void edit(Monitoria_Solicitada monitoria_Solicitada) {
        em.merge(monitoria_Solicitada);
    }

    @Override
    public void remove(Monitoria_Solicitada monitoria_Solicitada) {
        em.remove(em.merge(monitoria_Solicitada));
    }

    @Override
    public Monitoria_Solicitada find(Object id) {
        return em.find(Monitoria_Solicitada.class, id);
    }

    @Override
    public List<Monitoria_Solicitada> findAll() {
        return em.createQuery("select object(o) from Monitoria_Solicitada as o").getResultList();
    }
}
