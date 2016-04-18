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

import co.uniandes.sisinfo.entities.Convocatoria;
import co.uniandes.sisinfo.serviciosfuncionales.ConvocatoriaFacadeRemote;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios Entidad Convocatoria
 */
@Stateless
@EJB(name = "ConvocatoriaFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.ConvocatoriaFacadeLocal.class)
public class ConvocatoriaFacade implements ConvocatoriaFacadeLocal, ConvocatoriaFacadeRemote {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Convocatoria convocatoria) {
        em.persist(convocatoria);
    }

    @Override
    public void edit(Convocatoria convocatoria) {
        em.merge(convocatoria);
    }

    @Override
    public void remove(Convocatoria convocatoria) {
        em.remove(em.merge(convocatoria));
    }

    @Override
    public Convocatoria find(Object id) {
        return em.find(Convocatoria.class, id);
    }

    @Override
    public List<Convocatoria> findAll() {
        return em.createQuery("select object(o) from Convocatoria as o").getResultList();
    }
}
