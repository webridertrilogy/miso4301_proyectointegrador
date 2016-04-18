/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Dia;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;


/**
 *
 * @author da-naran
 */
@Stateless
public class SeccionFacade implements SeccionFacadeLocal, SeccionFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;
    
    public void create(Seccion seccion) {
        em.persist(seccion);
    }

    public void edit(Seccion seccion) {
        em.merge(seccion);
    }

    public void remove(Seccion seccion) {
        em.remove(em.merge(seccion));
    }

    public Seccion find(Object id) {
        return em.find(Seccion.class, id);
    }

    public List<Seccion> findAll() {
        List<Seccion> secciones= em.createQuery("select object(o) from Seccion as o").getResultList();
        for(Seccion s:secciones){
            hibernateInitialize(s);
        }
        return secciones;
    }

    public List<Seccion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Seccion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Seccion as o").getSingleResult()).intValue();
    }

    @Override
    public Seccion findByCRN(String crn) {
        try {
            Seccion seccion = (Seccion) em.createNamedQuery("Seccion.findByCRN").setParameter("crn", crn).getSingleResult();
            hibernateInitialize(seccion);
            return seccion;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Seccion seccion = (Seccion) em.createNamedQuery("Seccion.findByCRN").setParameter("crn", crn).getResultList().get(0);
            hibernateInitialize(seccion);
            return seccion;
        }
    }

    @Override
    @Deprecated
    public boolean hayVacantes(String crnSeccion) {
        Seccion seccion = findByCRN(crnSeccion);
        if (seccion == null) {
            return false;
        }
        return true;

        /*Query q=em.createNamedQuery("Seccion.darMonitoriasSecciones");
        q.setParameter("crn", seccion.getCrn());
        List<Monitoria> monitorias=q.getResultList();
        double sumaCargas=0;
        for(Monitoria m:monitorias){
        double carga=m.getCarga();
        double valCargaMonitoriaT1 = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1));
        if(carga==valCargaMonitoriaT1){
        sumaCargas+=1;
        }else{
        sumaCargas+=0.5;
        }
        }
        //System.out.println("max:"+seccion.getMaximoMonitores()+", sum:"+sumaCargas);
        double vacantes=seccion.getMaximoMonitores()-sumaCargas;
        if(vacantes>0){
        return true;
        }else{
        return false;
        }*/

    }

    @Override
    public Seccion findById(Long id) {
        try {
            Seccion seccion = (Seccion) em.createNamedQuery("Seccion.findById").setParameter("id", id).getSingleResult();
            hibernateInitialize(seccion);
            return seccion;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Seccion seccion = (Seccion) em.createNamedQuery("Seccion.findById").setParameter("id", id).getResultList().get(0);
            hibernateInitialize(seccion);
            return seccion;
        }
    }

    /**
     * Inicializa la sección y sus colecciones
     * @param seccion Sección
     */
    private void hibernateInitialize(Seccion seccion) {
        Hibernate.initialize(seccion);
        if(seccion != null){
            
            if(seccion.getHorarios() != null){
                Hibernate.initialize(seccion.getHorarios());
                for(Sesion s: seccion.getHorarios()){
                    Hibernate.initialize(s.getDias());
                }
            }
            //Hibernate.initialize(seccion.getMonitorias());
            Hibernate.initialize(seccion.getProfesorPrincipal());
            if(seccion.getProfesorPrincipal()!=null){
                Hibernate.initialize(seccion.getProfesorPrincipal().getPersona());
            }

           Collection<Profesor> profesoresRelacionados = seccion.getProfesores();
           Hibernate.initialize(profesoresRelacionados);
           if(profesoresRelacionados != null){
               for(Iterator<Profesor> itProfesor = profesoresRelacionados.iterator(); itProfesor.hasNext();){
                   Profesor profesorRelacionado = itProfesor.next();
                   Hibernate.initialize(profesorRelacionado);
                   if(profesorRelacionado != null){
                        Hibernate.initialize(profesorRelacionado.getPersona());
                   }
               }
            }
        }
    }

    @Override
    public List<Seccion> findByCorreoProfesor(String correo) {

        List<Seccion> secciones = em.createNamedQuery("Seccion.findByCorreoProfesor").setParameter("correo", correo).getResultList();
        Iterator<Seccion> iterator = secciones.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return secciones;
    }

    @Deprecated
    public long contarSeccionesConMonitoresCompletos() {
//        try{
//            Long entero = (Long)em.createNamedQuery("Seccion.contarSeccionesConMonitoresCompletos").getSingleResult();
//            return entero;
//        }catch(NoResultException e){
//            return -1;
//        }catch(NonUniqueResultException e){
//            Long entero = (Long)em.createNamedQuery("Seccion.contarSeccionesConMonitoresCompletos").getResultList().get(0);
//            return entero;
//        }
        return 0;
    }

    @Deprecated
    public long contarSeccionesSinMonitores() {
//                try{
//            Long entero = (Long)em.createNamedQuery("Seccion.contarSeccionesSinMonitores").getSingleResult();
//            return entero;
//        }catch(NoResultException e){
//            return -1;
//        }catch(NonUniqueResultException e){
//            Long entero = (Long)em.createNamedQuery("Seccion.contarSeccionesSinMonitores").getResultList().get(0);
//            return entero;
//        }
        return 0;
    }

    public long contarSecciones() {
        try {
            Long entero = (Long) em.createNamedQuery("Seccion.contarSecciones").getSingleResult();
            return entero;
        } catch (NoResultException e) {
            return -1;
        } catch (NonUniqueResultException e) {
            Long entero = (Long) em.createNamedQuery("Seccion.contarSecciones").getResultList().get(0);
            return entero;
        }
    }

    @Deprecated
    public long contarSeccionesRequierenMonitores() {
//                 try{
//            Long entero = (Long)em.createNamedQuery("Seccion.contarSeccionesRequierenMonitores").getSingleResult();
//            return entero;
//        }catch(NoResultException e){
//            return -1;
//        }catch(NonUniqueResultException e){
//            Long entero = (Long)em.createNamedQuery("Seccion.contarSeccionesRequierenMonitores").getResultList().get(0);
//            return entero;
//        }
        return 0;
    }

    public void removeAll(){
        List<Seccion> secciones = findAll();
        for (Seccion seccion : secciones) {
            remove(seccion);
        }
    }
}
