/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.AuditoriaUsuario;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Paola Gómez
 */
@Stateless
public class AuditoriaUsuarioFacade implements AuditoriaUsuarioFacadeLocal,AuditoriaUsuarioFacadeRemote{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(AuditoriaUsuario audit) {
        em.persist(audit);
    }

    @Override
    public void edit(AuditoriaUsuario audit) {
        em.merge(audit);
    }

    @Override
    public void remove(AuditoriaUsuario audit) {
        em.remove(em.merge(audit));
    }

    @Override
    public AuditoriaUsuario find(Object id) {
        return em.find(AuditoriaUsuario.class, id);
    }

    @Override
    public List<AuditoriaUsuario> findAll() {
        return em.createQuery("select object(o) from AuditoriaUsuario as o").getResultList();
    }

    public List<AuditoriaUsuario> findByUsuario(String correo) {
        return em.createNamedQuery("AuditoriaUsuario.findByUsuario").setParameter("correo", correo).getResultList();
    }

    public List<AuditoriaUsuario> findByFecha(Date fechaInicio, Date fechaFin) {
        return em.createNamedQuery("AuditoriaUsuario.findByFecha").setParameter("fechaInicio", fechaInicio).setParameter("fechaFin", fechaFin).getResultList();
    }

    public List<AuditoriaUsuario> findByRol(String rol) {
        return em.createNamedQuery("AuditoriaUsuario.findByRol").setParameter("rol", rol).getResultList();
    }

    public List<AuditoriaUsuario> findByAny(String usuario, String rol, Date fechaInicio, Date fechaFin, String comandos) {

        String q = "select c from AuditoriaUsuario c where ";

        if(usuario != null){
            q += "c.usuario like :correo";
        }
        if (rol != null){
            q += (usuario != null)? " AND " : "";
            q += "c.rol like :rol";
        }
        if (comandos != null){
            q += (usuario != null || rol != null)? " AND " : "";
            q += "c.comando in (:comandos)";
        }
        if (fechaInicio != null){
            q += (usuario != null || rol != null || comandos != null)? " AND " : "";
            q += "c.fecha >= :fechaInicio";
        }
        if (fechaFin != null){
            q += (usuario != null || rol != null || comandos != null || fechaInicio != null)? " AND " : "";
            q += "c.fecha <= :fechaFin";
        }

        Query query = em.createQuery(q);

        if(usuario != null){
            query.setParameter("correo", usuario);
        }
        if (rol != null){
            query.setParameter("rol", rol);
        }
        if (comandos != null){
            Collection<String> cmds = Arrays.asList(comandos.split(","));
            query.setParameter("comandos", cmds);
        }
        if (fechaInicio != null){
            query.setParameter("fechaInicio", fechaInicio);
        }
        if (fechaFin != null){
            query.setParameter("fechaFin", fechaFin);
        }

        return query.getResultList();
    }


}
