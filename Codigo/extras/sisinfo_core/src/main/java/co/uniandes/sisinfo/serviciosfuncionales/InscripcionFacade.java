/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import  co.uniandes.sisinfo.entities.Inscripcion;
import co.uniandes.sisinfo.entities.InscripcionAsistente;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 *
 * @author im.melo33
 */
@Stateless
public class InscripcionFacade implements InscripcionFacadeLocal {
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Inscripcion inscripcion) {
        em.persist(inscripcion);
    }

    public void edit(Inscripcion inscripcion) {
        em.merge(inscripcion);
    }

    public void remove(Inscripcion inscripcion) {
        em.remove(inscripcion);
    }

    public Inscripcion find(Object id) {
        Inscripcion i = em.find(Inscripcion.class, id);
        hibernateInitialize(i);
        return i;
    }

    public List<Inscripcion> findAll() {
        List<Inscripcion> lista= em.createQuery("select object(o) from Inscripcion as o").getResultList();
        for (Inscripcion inscripcion : lista) {
            hibernateInitialize(inscripcion);
        }
        return lista;
    }

    public List<Inscripcion>getInscripcionesPorCreador(String correoC)
    {
         List<Inscripcion> lista=  em.createNamedQuery("Inscripcion.findByCreador").setParameter("correoCreador", correoC).getResultList();
        for (Inscripcion inscripcion : lista) {
            hibernateInitialize(inscripcion);
        }
         return lista;
    }


     private void hibernateInitialize(Inscripcion i) {
//        Hibernate.initialize(i);
//        Collection<InscripcionAsistente> incrit = i.getInvitados();
//        if(incrit!=null)
//        {
//            for (InscripcionAsistente inscripcionAsistente : incrit) {
//                Hibernate.initialize(inscripcionAsistente);
//                if(inscripcionAsistente.getPersona()!=null)
//                {
//                    Hibernate.initialize(inscripcionAsistente.getPersona());
//                }
//            }
//        }
    }
 
}
