/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;


import co.uniandes.sisinfo.entities.datosmaestros.Facultad;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 * Entidad Facultad
 */
@Stateless
@EJB(name = "FacultadFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.FacultadFacadeLocal.class)
public class FacultadFacade implements FacultadFacadeLocal, FacultadFacadeRemote {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Facultad facultad) {
        em.persist(facultad);
    }

    @Override
    public void edit(Facultad facultad) {
        em.merge(facultad);
    }

    @Override
    public void remove(Facultad facultad) {
        em.remove(em.merge(facultad));
    }

    @Override
    public Facultad find(Object id) {
        return em.find(Facultad.class, id);
    }

  

    @Override
    public Facultad findByNombre(String nombre) {
        try {

            Facultad f= (Facultad)em.createNamedQuery("Facultad.findByNombre").setParameter("nombre", nombre).getSingleResult();
            Hibernate.initialize(f.getProgramas());
            return f;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Facultad facultad =  (Facultad) em.createNamedQuery("Facultad.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
            hibernateInitialize(facultad);
            return facultad;
        }
    }

    /**
     * Inicializa la facultad y sus colecciones
     * @param facultad Facultad
     */
    private void hibernateInitialize(Facultad facultad){
        Hibernate.initialize(facultad);
        Hibernate.initialize(facultad.getProgramas());
    }
@Override
    public List<Facultad> findAll() {
        List<Facultad> facultades = em.createQuery("select object(o) from Facultad as o").getResultList();
        Iterator<Facultad> iterator = facultades.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return facultades;
    }

   
}
