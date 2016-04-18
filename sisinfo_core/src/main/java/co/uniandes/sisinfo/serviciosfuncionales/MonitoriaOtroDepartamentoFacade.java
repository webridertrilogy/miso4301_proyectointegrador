/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.MonitoriaOtroDepartamento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Asistente
 */
@Stateless
public class MonitoriaOtroDepartamentoFacade extends AbstractFacade<MonitoriaOtroDepartamento> implements MonitoriaOtroDepartamentoFacadeLocal, MonitoriaOtroDepartamentoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public MonitoriaOtroDepartamentoFacade() {
        super(MonitoriaOtroDepartamento.class);
    }

    @Override
    public List<MonitoriaOtroDepartamento> findByCodigoEstudiante(String codigo) {
        return (List<MonitoriaOtroDepartamento>) em.createNamedQuery("MonitoriaOtroDepartamento.findByCodigoEstudiante").setParameter("codigoEstudiante", codigo).getResultList();
    }

    public MonitoriaOtroDepartamento findByCodigoEstudianteAndCodigoCurso(String codigoEstudiante, String codigoCurso) {
        try {
            return (MonitoriaOtroDepartamento) em.createNamedQuery("MonitoriaOtroDepartamento.findByCodigoMateriaAndCodigoEstudiante").setParameter("codigoEstudiante", codigoEstudiante).setParameter("codigoCurso", codigoCurso).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (MonitoriaOtroDepartamento) em.createNamedQuery("MonitoriaOtroDepartamento.findByCodigoMateriaAndCodigoEstudiante").setParameter("codigoEstudiante", codigoEstudiante).setParameter("codigoCurso", codigoCurso).getResultList().get(0);
        }

    }


}
