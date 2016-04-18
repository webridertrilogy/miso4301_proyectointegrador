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

import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.MonitoriaOtroDepartamento;
import co.uniandes.sisinfo.entities.MonitoriaRealizada;
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
 * Servicios Entidad Aspirante
 */
@Stateless
@EJB(name = "AspiranteFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeLocal.class, mappedName = "AspiranteFacade")
public class AspiranteFacade implements AspiranteFacadeRemote, AspiranteFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Aspirante aspirante) {
        em.persist(aspirante);
    }

    @Override
    public void edit(Aspirante aspirante) {
        em.merge(aspirante);
    }

    @Override
    public void remove(Aspirante aspirante) {
        em.remove(em.merge(aspirante));
    }

    @Override
    public Aspirante find(Object id) {
        Aspirante aspirante = em.find(Aspirante.class, id);
        hibernateInitialize(aspirante);
        return aspirante;
    }

    @Override
    public List<Aspirante> findAll() {
        List<Aspirante> aspirantes = em.createQuery("select object(o) from Aspirante as o").getResultList();
        Iterator<Aspirante> iterator = aspirantes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return aspirantes;
    }

    @Override
    public Aspirante findByCodigo(String codigo) {
        try {
            Aspirante aspirante = (Aspirante) em.createNamedQuery("Aspirante.findByCodigo").setParameter("codigo", codigo).getSingleResult();
            hibernateInitialize(aspirante);
            return aspirante;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Aspirante aspirante = (Aspirante) em.createNamedQuery("Aspirante.findByCodigo").setParameter("codigo", codigo).getResultList().get(0);
            hibernateInitialize(aspirante);
            return aspirante;
        }
    }

    @Override
    public Aspirante findByCorreo(String correo) {
        try {
            Aspirante aspirante = (Aspirante) em.createNamedQuery("Aspirante.findByCorreo").setParameter("correo", correo).getSingleResult();
            hibernateInitialize(aspirante);
            return aspirante;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Aspirante aspirante = (Aspirante) em.createNamedQuery("Aspirante.findByCorreo").setParameter("correo", correo).getResultList().get(0);
            hibernateInitialize(aspirante);
            return aspirante;
        }
    }

    /**
     * Inicializa el aspirante y sus colecciones
     * @param aspirante Aspirante
     */
    private void hibernateInitialize(Aspirante aspirante) {
        Hibernate.initialize(aspirante);
        Hibernate.initialize(aspirante.getPersona().getTipoDocumento());
        Hibernate.initialize(aspirante.getHorario_disponible());
        if (aspirante.getHorario_disponible() != null) {
            if (aspirante.getHorario_disponible().getDias_disponibles() != null) {
                Hibernate.initialize(aspirante.getHorario_disponible().getDias_disponibles());
                Hibernate.initialize(aspirante.getHorario_disponible().getDias_disponibles().iterator());
            }
        }
        Hibernate.initialize(aspirante.getEstudiante().getInformacion_Academica());
        Hibernate.initialize(aspirante.getEstudiante().getTipoEstudiante());
        Hibernate.initialize(aspirante.getPersona().getPais());
        Collection<MonitoriaOtroDepartamento> monitoriasOD = aspirante.getMonitoriasOtrosDepartamentos();
        Hibernate.initialize(monitoriasOD);

        for (Iterator<MonitoriaOtroDepartamento> it = monitoriasOD.iterator(); it.hasNext();) {
            MonitoriaOtroDepartamento monitoriaOtroDepartamento = it.next();
            Hibernate.initialize(monitoriaOtroDepartamento);
        }
        Collection<MonitoriaRealizada> monitoriasRealizadas = aspirante.getMonitoriasRealizadas();
        Hibernate.initialize(monitoriasRealizadas);
        for (MonitoriaRealizada monitoriaRealizada : monitoriasRealizadas) {
            Hibernate.initialize(monitoriaRealizada);
        }
    }
}
