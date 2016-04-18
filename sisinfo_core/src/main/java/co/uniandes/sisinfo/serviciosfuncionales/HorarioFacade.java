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


import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios Entidad Horario
 */
@Stateless
@EJB(name = "HorarioFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.HorarioFacadeLocal.class)
public class HorarioFacade implements HorarioFacadeLocal, HorarioFacadeRemote {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Sesion horario) {
        em.persist(horario);
    }

    @Override
    public void edit(Sesion horario) {
        em.merge(horario);
    }

    @Override
    public void remove(Sesion horario) {
        em.remove(em.merge(horario));
    }

    @Override
    public Sesion find(Object id) {
        return em.find(Sesion.class, id);
    }

    @Override
    public List<Sesion> findAll() {
        return em.createQuery("select object(o) from Horario as o").getResultList();
    }
}
