/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Contacto;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ivan melo
 */
@Stateless
public class ContactoFacade implements ContactoFacadeLocal, ContactoFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(Contacto contacto) {
        if (contacto.getFechaActualizacion() == null) {
            contacto.setFechaActualizacion(new Date(System.currentTimeMillis()));
        }
        em.persist(contacto);
    }

    public void edit(Contacto contacto) {
        if (contacto.getFechaActualizacion() == null) {
            contacto.setFechaActualizacion(new Date(System.currentTimeMillis()));
        }
        em.merge(contacto);
    }

    public void remove(Contacto contacto) {
        em.remove(em.merge(contacto));
        //  em.remove(contacto);
    }

    public Contacto find(Object id) {
        return em.find(Contacto.class, id);
    }

    public List<Contacto> findAll() {
        return em.createQuery("select object(o) from Contacto as o").getResultList();
    }

    public List<Contacto> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Contacto as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Contacto as o").getSingleResult()).intValue();
    }

    public Contacto findContactoByCorreo(String correo) {
        try {
            return (Contacto) em.createNamedQuery("Contacto.findByCorreo").setParameter("correo", correo).getSingleResult();
        } catch (NonUniqueResultException nure) {
            return (Contacto) em.createNamedQuery("Contacto.findByCorreo").setParameter("correo", correo).getResultList().get(0);
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<Contacto> findContactosValidos() {
        try {
            return em.createNamedQuery("Contacto.findAllContactosValidos").getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    /**
     * Construye una consulta a partir de los parametros que no sean nulos
     * @return Los contactos encontrados
     */
    public Collection<Contacto> findContactos(String nombre, String apellidos, String id, String cargo, String empresa, String ciudad, String celular, String correo, String sector, java.util.Date fecha) {
        try {

            String jpql = "SELECT e FROM Contacto e WHERE e.correo is not null ";
            if (nombre != null && !nombre.equals("")) {
                jpql += " AND e.nombres like  '%" + nombre + "%'";
            }
            if (apellidos != null && !apellidos.equals("")) {
                jpql += " AND e.apellidos like  '%" + apellidos + "%'";
            }
            if (id != null && !id.equals("")) {
                jpql += " AND e.numeroIdentificacion like  '%" + id + "%'";
            }
            if (cargo != null && !cargo.equals("")) {
                jpql += " AND e.cargo.nombre like  '%" + cargo + "%'";
            }
            if (empresa != null && !empresa.equals("")) {
                jpql += " AND e.Empresa like  '%" + empresa + "%'";
            }
            if (ciudad != null && !ciudad.equals("")) {
                jpql += " AND e.ciudad like  '%" + ciudad + "%'";
            }
            if (celular != null && !celular.equals("")) {
                jpql += " AND e.celular like  '%" + celular + "%'";
            }
            if (correo != null && !correo.equals("")) {
                jpql += " AND e.correo like  '%" + correo + "%'";
            }
            if (sector != null && !sector.equals("")) {
                jpql += " AND e.sector.nombre like  '%" + sector + "%'";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (fecha != null) {
                jpql += " AND e.fechaActualizacion like '" + sdf.format(fecha) + "'";
            }

            return em.createQuery(jpql).getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
