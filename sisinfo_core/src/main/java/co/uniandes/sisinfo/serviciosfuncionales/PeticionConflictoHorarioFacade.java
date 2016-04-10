package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.PeticionConflictoHorario;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author German Florez, Marcela Morales, Paola Gómez
 * Servicios Fachada entidad PeticionConflictoHorarios
 */
@Stateless
public class PeticionConflictoHorarioFacade extends AbstractFacadeCH<PeticionConflictoHorario> implements PeticionConflictoHorarioFacadeLocal, PeticionConflictoHorarioFacadeRemote {

    @EJB
    private ConstanteRemote constanteBean;

    @PersistenceContext(unitName = "ConflictoHorariosPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public PeticionConflictoHorarioFacade() {
        super(PeticionConflictoHorario.class);
        try {
            ServiceLocator serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public PeticionConflictoHorario buscarPorEstudianteYSeccionDestino(String correoEstudiante, Long idSeccionDestino) {
        Query query = em.createNamedQuery("PeticionConflictoHorario.findByEstudianteYSeccionDestino");
        query = query.setParameter("correoEstudiante", correoEstudiante);
        query = query.setParameter("idSeccionDestino", idSeccionDestino);

        try {
            //No devuelve las peticiones con estado cancelado
            PeticionConflictoHorario peticion = (PeticionConflictoHorario) query.getSingleResult();
            if (getConstanteBean().getConstante(Constantes.VAL_CONFLICTO_HORARIO_ESTADO_CANCELADO).equals(peticion.getEstado())) {
                return null;
            }
            return peticion;

        } catch (NonUniqueResultException nure) {
            //No devuelve las peticiones con estado cancelado
            List<PeticionConflictoHorario> pets = query.getResultList();
            LinkedList<PeticionConflictoHorario> respuesta = new LinkedList<PeticionConflictoHorario>();
            for (PeticionConflictoHorario p : pets) {
                if (!getConstanteBean().getConstante(Constantes.VAL_CONFLICTO_HORARIO_ESTADO_CANCELADO).equals(p.getEstado())) {
                    respuesta.add(p);
                }
            }
            if(respuesta.isEmpty()){
                return null;
            }else{
                return respuesta.get(0);
            }
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Collection<PeticionConflictoHorario> buscarPorCorreo(String correo) {
        Query query = em.createNamedQuery("PeticionConflictoHorario.findByCorreo");
        query = query.setParameter("correo", correo);
        try {
            //No devuelve las peticiones con estado cancelado
            List<PeticionConflictoHorario> peticiones = query.getResultList();
            LinkedList<PeticionConflictoHorario> respuesta = new LinkedList<PeticionConflictoHorario>();
            for (PeticionConflictoHorario p : peticiones) {
                if (!getConstanteBean().getConstante(Constantes.VAL_CONFLICTO_HORARIO_ESTADO_CANCELADO).equals(p.getEstado())) {
                    respuesta.add(p);
                }
            }
            return respuesta;
        } catch (NoResultException nre) {
            return new ArrayList<PeticionConflictoHorario>();
        }
    }

    public PeticionConflictoHorario buscarPorIdYCorreo(String correo, Long id) {
        Query query = em.createNamedQuery("PeticionConflictoHorario.findByIdYCorreo");
        query = query.setParameter("correo", correo);
        query = query.setParameter("id", id);
        try {
            return (PeticionConflictoHorario) query.getSingleResult();
        } catch (NonUniqueResultException nure) {
            return (PeticionConflictoHorario) query.getResultList().get(0);
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Collection<PeticionConflictoHorario> buscarPorCodigoCurso(String codigo) {
        Query query = em.createNamedQuery("PeticionConflictoHorario.findByCodigoCurso");
        query = query.setParameter("codigo", codigo);
        try {
            return query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<PeticionConflictoHorario>();
        }
    }


    public Collection<PeticionConflictoHorario> buscarPorCodigoCursoYTipo(String codigo, String tipo, int posicionInicial, int resultadosMaximos) {
        Query query = em.createNamedQuery("PeticionConflictoHorario.findByCodigoCursoYTipo");
        if(posicionInicial != -1 && resultadosMaximos != -1){
            query.setFirstResult(posicionInicial);
            query.setMaxResults(resultadosMaximos);
        }
        query = query.setParameter("codigo", codigo);
        query = query.setParameter("tipo", tipo);
        try {
            return query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<PeticionConflictoHorario>();
        }
    }

    public Collection<PeticionConflictoHorario> buscarPeticionesResueltas() {
        Query query = em.createNamedQuery("PeticionConflictoHorario.findResueltas");
        try {
            return query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<PeticionConflictoHorario>();
        }
    }



    public Collection<PeticionConflictoHorario> buscarPorSeccion(Long id) {
        Query query = em.createNamedQuery("PeticionConflictoHorario.findByIdSeccion");
        query = query.setParameter("id", id);
        try {
            return query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<PeticionConflictoHorario>();
        }
    }

}
