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


import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import co.uniandes.sisinfo.entities.datosmaestros.Programa;
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
 * Servicios Entidad Programa
 */
@Stateless
@EJB(name = "ProgramaFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeLocal.class)
public class ProgramaFacade implements ProgramaFacadeLocal, ProgramaFacadeRemote {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Programa programa) {
        em.persist(programa);
    }

    @Override
    public void edit(Programa programa) {
        em.merge(programa);
    }

    @Override
    public void remove(Programa programa) {
        em.remove(em.merge(programa));
    }

    @Override
    public Programa find(Object id) {
        Programa programa = em.find(Programa.class, id);
        hibernateInitialize(programa);
        return  programa;
    }

    @Override
    public List<Programa> findAll() {
        List<Programa> programas = em.createQuery("select object(o) from Programa as o").getResultList();
        Iterator<Programa> iterator = programas.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return programas;
    }

    @Override
    public Programa findByNombre(String nombre){
        try {
            Programa aspirante = (Programa) em.createNamedQuery("Programa.findByNombre").setParameter("nombre", nombre).getSingleResult();
            hibernateInitialize(aspirante);
            return aspirante;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Programa aspirante =  (Programa) em.createNamedQuery("Programa.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
            hibernateInitialize(aspirante);
            return aspirante;
        }
   }

    /**
     * Inicializa el programa y sus colecciones
     * @param programa Programa
     */
    private void hibernateInitialize(Programa programa){
        Hibernate.initialize(programa);
    }

    public void removeAll(){
        List<Programa> programas = findAll();
        for (Programa programa : programas) {
            remove(programa);
        }
    }

    public Programa findByCodigo(String codigo) {
        try {
            Programa aspirante = (Programa) em.createNamedQuery("Programa.findByCodigo").setParameter("codigo", codigo).getSingleResult();
            hibernateInitialize(aspirante);
            return aspirante;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Programa aspirante =  (Programa) em.createNamedQuery("Programa.findByCodigo").setParameter("codigo", codigo).getResultList().get(0);
            hibernateInitialize(aspirante);
            return aspirante;
        }
    }
}
