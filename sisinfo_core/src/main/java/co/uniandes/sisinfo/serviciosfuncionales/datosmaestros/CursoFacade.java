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

import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import java.util.ArrayList;
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
 * Servicios Entidad Curso
 */
@Stateless
@EJB(name = "CursoFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeLocal.class)
public class CursoFacade implements CursoFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Curso curso) {
        em.persist(curso);
    }

    @Override
    public void edit(Curso curso) {
        em.merge(curso);
    }

    @Override
    public void remove(Curso curso) {
        em.remove(em.merge(curso));
    }

    @Override
    public Curso find(Object id) {
        Curso curso = (Curso) em.find(Curso.class, id);
        hibernateInitialize(curso);
        return curso;
    }

    @Override
    public List<Curso> findAll() {
        List<Curso> cursos = (List<Curso>) em.createQuery("select object(o) from Curso as o").getResultList();
        Iterator<Curso> iterator = cursos.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return cursos;
    }

    @Override
    public Curso findByCodigo(String codigo) {
        try {
            Curso curso = (Curso) em.createNamedQuery("Curso.findByCodigo").setParameter("codigo", codigo).getSingleResult();
            hibernateInitialize(curso);
            return curso;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Curso curso = (Curso) em.createNamedQuery("Curso.findByCodigo").setParameter("codigo", codigo).getResultList().get(0);
            hibernateInitialize(curso);
            return curso;
        }
    }

    @Override
    public Curso findById(Long id) {
        try {
            Curso curso = (Curso) em.createNamedQuery("Curso.findById").setParameter("id", id).getSingleResult();
            hibernateInitialize(curso);
            return curso;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Curso curso = (Curso) em.createNamedQuery("Curso.findById").setParameter("id", id).getResultList().get(0);
            hibernateInitialize(curso);
            return curso;
        }
    }

    @Override
    public Curso findByCRNSeccion(String crn) {
        try {
            Curso curso = (Curso) em.createNamedQuery("Curso.findByCRN").setParameter("crn", crn).getSingleResult();
            hibernateInitialize(curso);
            return curso;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Curso curso = (Curso) em.createNamedQuery("Curso.findByCRN").setParameter("crn", crn).getResultList().get(0);
            hibernateInitialize(curso);
            return curso;
        }
    }

    @Override
    public Curso findByNombre(String nombre) {
        try {
            Curso curso = (Curso) em.createNamedQuery("Curso.findByNombre").setParameter("nombre", nombre).getSingleResult();
            hibernateInitialize(curso);
            return curso;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Curso curso = (Curso) em.createNamedQuery("Curso.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
            hibernateInitialize(curso);
            return curso;
        }
    }

    /**
     * Inicializa el curso y sus colecciones
     * @param curso Curso
     */
    private void hibernateInitialize(Curso curso) {
//        Hibernate.initialize(curso);
//        Hibernate.initialize(curso.getNivelPrograma());
//        Collection<Seccion> secciones = curso.getSecciones();
//        Hibernate.initialize(secciones);
//        for (Iterator<Seccion> it = secciones.iterator(); it.hasNext();) {
//            Seccion seccion = it.next();
//            Hibernate.initialize(seccion);
//
//            if(seccion.getHorarios() != null){
//                Hibernate.initialize(seccion.getHorarios());
//                for (Iterator<Sesion> it1 = seccion.getHorarios().iterator(); it1.hasNext();) {
//                    Sesion sesion = it1.next();
//                    Hibernate.initialize(sesion);
//                    Hibernate.initialize(sesion.getDias());
//                    for (Iterator<DiaCompleto> it2 = sesion.getDias().iterator(); it2.hasNext();) {
//                        DiaCompleto dia = it2.next();
//                        Hibernate.initialize(dia);
//                    }
//                }
//            }
//
//            if(seccion.getProfesorPrincipal() != null){
//                Hibernate.initialize(seccion.getProfesorPrincipal());
//                Hibernate.initialize(seccion.getProfesorPrincipal().getPersona());
//            }
//
//            Collection<Profesor> profesoresRelacionados = seccion.getProfesores();
//            Hibernate.initialize(profesoresRelacionados);
//            for(Iterator<Profesor> itProfesor = profesoresRelacionados.iterator(); itProfesor.hasNext();){
//               Profesor profesorRelacionado = itProfesor.next();
//               Hibernate.initialize(profesorRelacionado);
//               if(profesorRelacionado != null){
//                    Hibernate.initialize(profesorRelacionado.getPersona());
//               }
//            }
//        }
//        Collection<Curso> cursosRelacionados = curso.getCursosRelacionados();
//        Hibernate.initialize(cursosRelacionados);
//        for(Iterator<Curso> itCurso = cursosRelacionados.iterator(); itCurso.hasNext();){
//            Curso cursoRelacionado = itCurso.next();
//            Hibernate.initialize(cursoRelacionado);
//        }
//        Hibernate.initialize(curso.getPrograma());
//        Hibernate.initialize(curso.getNivelPrograma());
    }

    public long contarCursos() {
        try {
            Long entero = (Long) em.createNamedQuery("Curso.contarCursos").getSingleResult();
            return entero;
        } catch (NoResultException e) {
            return -1;
        } catch (NonUniqueResultException e) {
            Long entero = (Long) em.createNamedQuery("Curso.contarCursos").getResultList().get(0);
            return entero;
        }
    }

    public Curso findByCodigoSinProfesor(String codigo) {
        try {
            Curso curso = (Curso) em.createNamedQuery("Curso.findByCodigo").setParameter("codigo", codigo).getSingleResult();
            hibernateInitializeSinProfesor(curso);
            return curso;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Curso curso = (Curso) em.createNamedQuery("Curso.findByCodigo").setParameter("codigo", codigo).getResultList().get(0);
            hibernateInitializeSinProfesor(curso);
            return curso;
        }
    }

    /**
     * Inicializa el curso y sus colecciones
     * @param curso Curso
     */
    private void hibernateInitializeSinProfesor(Curso curso) {
        Hibernate.initialize(curso);
        Collection<Seccion> secciones = curso.getSecciones();
        Hibernate.initialize(secciones);
        for (Iterator<Seccion> it = secciones.iterator(); it.hasNext();) {
            Seccion seccion = it.next();
            Hibernate.initialize(seccion);
        }
    }

    public void removeAll(){
        List<Curso> objs= findAll();
        for (Curso curso : objs) {
            curso.setSecciones(new ArrayList<Seccion>());
            curso.setCursosRelacionados(new ArrayList<Curso>());
            curso.setNivelPrograma(null);
            curso.setPrograma(null);
            remove(curso);
        }
    }
}
