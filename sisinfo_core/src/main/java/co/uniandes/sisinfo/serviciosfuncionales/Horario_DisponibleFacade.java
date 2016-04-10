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


import co.uniandes.sisinfo.entities.Horario_Disponible;
import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import java.util.Collection;
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
 * Servicios Entidad Horario Disponible
 */
@Stateless
@EJB(name = "Horario_DisponibleFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.Horario_DisponibleFacadeRemote.class)
public class Horario_DisponibleFacade implements Horario_DisponibleFacadeLocal, Horario_DisponibleFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Horario_Disponible horario_Disponible) {
        em.persist(horario_Disponible);
    }

    @Override
    public void edit(Horario_Disponible horario_Disponible) {
        em.merge(horario_Disponible);
    }

    @Override
    public void remove(Horario_Disponible horario_Disponible) {
        em.remove(em.merge(horario_Disponible));
    }

    @Override
    public Horario_Disponible find(Object id) {
        return em.find(Horario_Disponible.class, id);
    }

    @Override
    public List<Horario_Disponible> findAll() {
        return em.createQuery("select object(o) from Horario_Disponible as o").getResultList();
    }

    @Override
    public Horario_Disponible findByCodigoEstudiante(String codigo) {

        try{
            Horario_Disponible hd= (Horario_Disponible) em.createNamedQuery("Horario_Disponible.findByCodigoEstudiante").setParameter("codigo", codigo).getSingleResult();
            Hibernate.initialize(hd);
            Collection<DiaCompleto> dias = hd.getDias_disponibles();
            Hibernate.initialize(dias);
            for (Iterator<DiaCompleto> it = dias.iterator(); it.hasNext();) {
                DiaCompleto object = it.next();
                Hibernate.initialize(object);
            }
            return hd;
        } catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return (Horario_Disponible)em.createNamedQuery("Horario_Disponible.findByCodigoEstudiante").setParameter("codigo", codigo).getResultList().get(0);
        }

    }
}
