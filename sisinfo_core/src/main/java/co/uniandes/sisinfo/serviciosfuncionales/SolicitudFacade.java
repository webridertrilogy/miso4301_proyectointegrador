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

import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.MonitoriaRealizada;
import co.uniandes.sisinfo.entities.Solicitud;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 * Servicios Entidad Solicitud
 */
@Stateless
@EJB(name = "SolicitudFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal.class)
public class SolicitudFacade implements SolicitudFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Solicitud solicitud) {
        em.persist(solicitud);
    }

    @Override
    public void edit(Solicitud solicitud) {
        //em.flush();
        em.merge(solicitud);
    }

    @Override
    public void remove(Solicitud solicitud) {
        em.remove(em.merge(solicitud));
    }

    @Override
    public Solicitud find(Object id) {
        Solicitud solicitud = em.find(Solicitud.class, id);
        hibernateInitialize(solicitud);
        return solicitud;
    }

    @Override
    public List<Solicitud> findAll() {
        List<Solicitud> solicitudes = em.createQuery("select object(o) from Solicitud as o").getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public Solicitud findById(Long id) {
        try {
            Solicitud solicitud = (Solicitud) em.createNamedQuery("Solicitud.findById").setParameter("id", id).getSingleResult();
            hibernateInitialize(solicitud);
            return solicitud;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Solicitud solicitud = (Solicitud) em.createNamedQuery("Solicitud.findById").setParameter("id", id).getResultList().get(0);
            hibernateInitialize(solicitud);
            return solicitud;
        }
    }

    @Override
    public List<Solicitud> findByCodigoEstudiante(String codigo) {
        List<Solicitud> solicitudes = (List<Solicitud>) em.createNamedQuery("Solicitud.findByCodigo").setParameter("codigo", codigo).getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByLogin(String correo) {
        List<Solicitud> solicitudes = (List<Solicitud>) em.createNamedQuery("Solicitud.findByLogin").setParameter("correo", correo).getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> tieneSolicitudesPorLogin(String correo) {
        List<Solicitud> solicitudes = (List<Solicitud>) em.createNamedQuery("Solicitud.findByLogin").setParameter("correo", correo).getResultList();
        /*Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }*/
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByEstado(String estado) {
        List<Solicitud> solicitudes = (List<Solicitud>) em.createNamedQuery("Solicitud.findByEstado").setParameter("estadoSolicitud", estado).getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByEstadoParaSecretaria(String estado) {
        List<Solicitud> solicitudes = (List<Solicitud>) em.createNamedQuery("Solicitud.findByEstado").setParameter("estadoSolicitud", estado).getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitializeSecretaria(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByNotEstado(String estado) {
        List<Solicitud> solicitudes = (List<Solicitud>) em.createNamedQuery("Solicitud.findByNotEstado").setParameter("estadoSolicitud", estado).getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByCrnSeccionT2(String crnSeccion1, String crnSeccion2){
        List<Solicitud> solicitudes = (List<Solicitud>) em.createNamedQuery("Solicitud.findByCrnSeccionT2").setParameter("crnSeccion1", crnSeccion1).setParameter("crnSeccion2", crnSeccion2).getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitializeCupi2(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByCrnSeccion(String estado) {
        List<Solicitud> solicitudes = (List<Solicitud>) em.createNamedQuery("Solicitud.findByCrnSeccion").setParameter("crnSeccion", estado).getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitializeCupi2(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByEstadoAndProfesorPrincipalSeccion(String estado, String correo) {
        Query q = em.createNamedQuery("Solicitud.findByEstadoAndProfesorPrincipalSeccion");
        q.setParameter("estadoSolicitud", estado);
        q.setParameter("correo", correo);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findSolicitudesPreseleccionadasPorSeccion(String estado, String crn) {
        Query q = em.createNamedQuery("Solicitud.findSolicitudesPreseleccionadasPorSeccion");
        q.setParameter("estadoSolicitud", estado);
        q.setParameter("crn", crn);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }



    /**
     * Inicializa la solicitud y sus colecciones
     * @param solicitud Solicitud
     */
    private void hibernateInitialize(Solicitud solicitud) {
        Hibernate.initialize(solicitud);
        Hibernate.initialize(solicitud.getEstudiante());
        Hibernate.initialize(solicitud.getEstudiante().getPersona());
        Hibernate.initialize(solicitud.getEstudiante().getPersona().getTipoDocumento());
        Hibernate.initialize(solicitud.getEstudiante().getHorario_disponible());
        Hibernate.initialize(solicitud.getEstudiante().getHorario_disponible().getDias_disponibles());
        Hibernate.initialize(solicitud.getEstudiante().getEstudiante().getInformacion_Academica());
        Hibernate.initialize(solicitud.getEstudiante().getEstudiante().getPrograma().getNombre());
        Hibernate.initialize(solicitud.getMonitoria_solicitada());
        Hibernate.initialize(solicitud.getMonitoria_solicitada().getCurso());
        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        for (Iterator<MonitoriaAceptada> it = monitorias.iterator(); it.hasNext();) {
            MonitoriaAceptada monitoria = it.next();
            Hibernate.initialize(monitoria);
            Hibernate.initialize(monitoria.getSeccion());
        }
        Collection<MonitoriaRealizada> monitoriasRealizadas = solicitud.getEstudiante().getMonitoriasRealizadas();
        Hibernate.initialize(monitoriasRealizadas);
        for (MonitoriaRealizada monitoriaRealizada : monitoriasRealizadas) {
            Hibernate.initialize(monitoriaRealizada);
        }
        Hibernate.initialize(solicitud.getResponsablePreseleccion());

    }

    private void hibernateInitializeCupi2(Solicitud solicitud){
        Hibernate.initialize(solicitud);
        Hibernate.initialize(solicitud.getEstudiante());
        //Hibernate.initialize(solicitud.getEstudiante().getHorario_disponible());
        //Hibernate.initialize(solicitud.getEstudiante().getHorario_disponible().getDias_disponibles());
        Hibernate.initialize(solicitud.getEstudiante().getEstudiante().getInformacion_Academica());
        Hibernate.initialize(solicitud.getMonitoria_solicitada());
        Hibernate.initialize(solicitud.getMonitoria_solicitada().getCurso());
        //Hibernate.initialize(solicitud.getMonitoria_solicitada().getSeccionesEscogidas());
        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        for (Iterator<MonitoriaAceptada> it = monitorias.iterator(); it.hasNext();) {
            MonitoriaAceptada monitoria = it.next();
            Hibernate.initialize(monitoria);
            //Hibernate.initialize(monitoria.getSeccion());
        }

        Hibernate.initialize(solicitud.getResponsablePreseleccion());
    }

    private void hibernateInitializeSecretaria(Solicitud solicitud){
        Hibernate.initialize(solicitud);
        Hibernate.initialize(solicitud.getEstudiante());
        //Hibernate.initialize(solicitud.getEstudiante().getHorario_disponible());
        //Hibernate.initialize(solicitud.getEstudiante().getHorario_disponible().getDias_disponibles());
        //Hibernate.initialize(solicitud.getEstudiante().getEstudiante().getInformacion_Academica());
        Hibernate.initialize(solicitud.getMonitoria_solicitada());
        Hibernate.initialize(solicitud.getMonitoria_solicitada().getCurso());
        Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
        for (Iterator<MonitoriaAceptada> it = monitorias.iterator(); it.hasNext();) {
            MonitoriaAceptada monitoria = it.next();
            Hibernate.initialize(monitoria);
            Hibernate.initialize(monitoria.getSeccion());
        }

        Hibernate.initialize(solicitud.getResponsablePreseleccion());
    }

    @Override
    public List<Solicitud> findByCodigoEstudianteAndSeccion(String codigo, String crn) {
        String query = "Solicitud.findByCodigoAndCrnSeccion";
        Query q = em.createNamedQuery(query);
        q.setParameter("codigoEstudiante", codigo);
        q.setParameter("crnSeccion", crn);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByCorreoEstudianteAndSeccion(String correo, String crn) {
        String query = "Solicitud.findByCorreoAndCrnSeccion";
        Query q = em.createNamedQuery(query);
        q.setParameter("correo", correo);
        q.setParameter("crnSeccion", crn);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByCodigoEstudianteAndCurso(String codigo, String codigoCurso) {
        String query = "Solicitud.findByCodigoEstudianteAndCurso";
        Query q = em.createNamedQuery(query);
        q.setParameter("codigoEstudiante", codigo);
        q.setParameter("codigoCurso", codigoCurso);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByCorreoEstudianteAndCurso(String correo, String codigoCurso) {
        String query = "Solicitud.findByCorreoEstudianteAndCurso";
        Query q = em.createNamedQuery(query);
        q.setParameter("correo", correo);
        q.setParameter("codigoCurso", codigoCurso);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findSolicitudesEnAspiracionPorSeccion(String estado, String crn) {
        Query q = em.createNamedQuery("Solicitud.findSolicitudesEnAspiracionPorSeccion");
        q.setParameter("estadoSolicitud", estado);
        q.setParameter("crn", crn);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByCurso(String codigoCurso) {
        String query = "Solicitud.findByCurso";
        Query q = em.createNamedQuery(query);
        q.setParameter("codigoCurso", codigoCurso);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findByCursoCupi2AndEstado(String estado,String codigoCurso) {
        String query = "Solicitud.findByCursoCupi2AndEstado";
        Query q = em.createNamedQuery(query);
        q.setParameter("codigoCurso", codigoCurso);
        q.setParameter("estadoSolicitud", estado);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    
    public List<Solicitud> findConveniosSecretaria() {
        String query = "Solicitud.findConveniosSecretaria";
        Query q = em.createNamedQuery(query);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitializeSecretaria(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findSolicitudesByDia(String dia, String hora){
        String query = "Solicitud.findSolicitudesByDia";
        Query q = em.createNamedQuery(query);
        q.setParameter("dia",dia);
        q.setParameter("horas",hora);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findSolicitudesByHorario(String[] dias, String[] horas){
        if(dias.length == 0){
            // Si no se especifica ninguna restriccion se devuelven todas las solicitudes
            return findAll();
        }
        List<Solicitud> solicitudes = findSolicitudesByDia(dias[0], horas[0]);
        for (int i = 1; i < dias.length; i++) {
            HashMap<Long,Boolean> solicitudesAnteriores = new HashMap<Long, Boolean>();
            for (Solicitud solicitud : solicitudes) {
                solicitudesAnteriores.put(solicitud.getId(), Boolean.TRUE);
            }
            String dia = dias[i];
            String hora = horas[i];
            solicitudes.clear();
            List<Solicitud> solicitudesNuevas = findSolicitudesByDia(dia, hora);
            for (Solicitud solicitud : solicitudesNuevas) {
                if(solicitudesAnteriores.get(solicitud.getId())!=null){
                    solicitudes.add(solicitud);
                }
            }

        }
        return solicitudes;       
    }

    @Override
    public List<Solicitud> findSolicitudesByDiaYCurso(String dia, String hora,String codigo){
        String query = "Solicitud.findSolicitudesByDiaYCurso";
        Query q = em.createNamedQuery(query);
        q.setParameter("dia",dia);
        q.setParameter("horas",hora);
        q.setParameter("codigoCurso", codigo);
        List<Solicitud> solicitudes = q.getResultList();
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            hibernateInitialize(iterator.next());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> findSolicitudesByHorarioYCurso(String[] dias, String[] horas, String codigoCurso){
        if(dias.length == 0){
            // Si no se especifica ninguna restriccion se devuelven todas las solicitudes
            return findByCurso(codigoCurso);
        }
        List<Solicitud> solicitudes = findSolicitudesByDiaYCurso(dias[0], horas[0],codigoCurso);
        for (int i = 1; i < dias.length; i++) {
            HashMap<Long,Boolean> solicitudesAnteriores = new HashMap<Long, Boolean>();
            for (Solicitud solicitud : solicitudes) {
                solicitudesAnteriores.put(solicitud.getId(), Boolean.TRUE);
            }
            String dia = dias[i];
            String hora = horas[i];
            solicitudes.clear();
            List<Solicitud> solicitudesNuevas = findSolicitudesByDia(dia, hora);
            for (Solicitud solicitud : solicitudesNuevas) {
                if(solicitudesAnteriores.get(solicitud.getId())!=null){
                    solicitudes.add(solicitud);
                }
            }

        }
        return solicitudes;




    }

    @Override
    public List<Solicitud> findSolicitudesResueltas() {
        Query query = em.createNamedQuery("Solicitud.findByEstado");
        String estado = "Asignado";
        query = query.setParameter("estadoSolicitud", estado);

        try {
            return query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<Solicitud>();
        }
    }
    
}
