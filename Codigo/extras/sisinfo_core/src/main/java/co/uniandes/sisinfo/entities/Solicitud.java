/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.util.Collection;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad Solicitud
 */
@Entity
@Table(name = "solicitud")
@NamedQueries({
    @NamedQuery(name = "Solicitud.findAll", query = "SELECT s FROM Solicitud s"),
    @NamedQuery(name = "Solicitud.findById", query = "SELECT s FROM Solicitud s WHERE s.id = :id"),
    @NamedQuery(name = "Solicitud.findByCodigo", query = "SELECT s FROM Solicitud s WHERE s.estudiante.estudiante.persona.codigo = :codigo"),
    @NamedQuery(name = "Solicitud.findByCodigoAndId", query = "SELECT s FROM Solicitud s WHERE s.estudiante.estudiante.persona.codigo = :codigo AND s.id = :id"),
    @NamedQuery(name = "Solicitud.findByCodigoAndCrnSeccion", query = "SELECT s FROM Solicitud s LEFT JOIN s.monitorias m WHERE s.estudiante.estudiante.persona.codigo = :codigoEstudiante AND m.seccion.crn = :crnSeccion"),
    @NamedQuery(name = "Solicitud.findByCorreoAndCrnSeccion", query = "SELECT s FROM Solicitud s LEFT JOIN s.monitorias m WHERE s.estudiante.estudiante.persona.correo = :correo AND m.seccion.crn = :crnSeccion"),
    @NamedQuery(name = "Solicitud.findByCrnSeccion", query = "SELECT s FROM Solicitud s LEFT JOIN s.monitorias m WHERE m.seccion.crn = :crnSeccion"),
    @NamedQuery(name = "Solicitud.findByCurso", query = "SELECT s FROM Solicitud s WHERE s.monitoria_solicitada.curso.codigo = :codigoCurso"),
    @NamedQuery(name = "Solicitud.findByCorreoEstudianteAndCurso", query = "SELECT s FROM Solicitud s WHERE s.estudiante.estudiante.persona.correo = :correo AND s.monitoria_solicitada.curso.codigo = :codigoCurso"),
    @NamedQuery(name = "Solicitud.findByCodigoEstudianteAndCurso", query = "SELECT s FROM Solicitud s WHERE s.estudiante.estudiante.persona.codigo = :codigoEstudiante AND s.monitoria_solicitada.curso.codigo = :codigoCurso"),
    @NamedQuery(name = "Solicitud.findByLogin", query = "SELECT s FROM Solicitud s WHERE s.estudiante.estudiante.persona.correo = :correo"),
    @NamedQuery(name = "Solicitud.findByEstado", query = "SELECT s FROM Solicitud s WHERE s.estadoSolicitud = :estadoSolicitud"),
    @NamedQuery(name = "Solicitud.findByCursoCupi2AndEstado", query = "SELECT s FROM Solicitud s WHERE s.monitoria_solicitada.curso.codigo = :codigoCurso AND s.estadoSolicitud = :estadoSolicitud"),
    @NamedQuery(name = "Solicitud.findByEstadoAndProfesorPrincipalSeccion", query = "SELECT s FROM Solicitud s LEFT JOIN s.monitorias m WHERE s.estadoSolicitud = :estadoSolicitud AND m.seccion.profesorPrincipal.persona.correo = :correo"),
    @NamedQuery(name = "Solicitud.findByEstadoAndSeccion", query = "SELECT s FROM Solicitud s LEFT JOIN s.monitorias m WHERE s.estadoSolicitud = :estadoSolicitud AND m.seccion.crn = :crn"),
    @NamedQuery(name = "Solicitud.findSolicitudesPreseleccionadasPorSeccion", query = "SELECT s FROM Solicitud s LEFT JOIN s.monitorias m WHERE s.estadoSolicitud = :estadoSolicitud AND m.solicitud.id = s.id AND m.seccion.crn = :crn"),
    @NamedQuery(name = "Solicitud.findSolicitudesEnAspiracionPorSeccion", query = "SELECT s FROM Solicitud s LEFT JOIN s.monitorias m WHERE s.estadoSolicitud = :estadoSolicitud AND m.seccion.crn = :crn"),
    @NamedQuery(name = "Solicitud.findByCrnSeccionT2", query = "SELECT s FROM Solicitud s  LEFT JOIN s.monitorias m  WHERE (m.seccion.crn = :crnSeccion1 OR m.seccion.crn = :crnSeccion2) AND m.carga = 0.5 GROUP BY s.estudiante.id HAVING COUNT(s.estudiante.id)=2"),
    @NamedQuery(name = "Solicitud.findByNotEstado", query = "SELECT s FROM Solicitud s WHERE s.estadoSolicitud != :estadoSolicitud"),
    @NamedQuery(name = "Solicitud.findConveniosSecretaria", query = "SELECT s FROM Solicitud s WHERE s.estadoSolicitud = 'PendienteRegistroConvenio' OR s.estadoSolicitud = 'PendienteFirmaConvenioEstudiante' OR s.estadoSolicitud = 'PendienteFirmaConvenioDepartamento' OR s.estadoSolicitud = 'PendienteRadicacion'"),
    @NamedQuery(name = "Solicitud.findSolicitudesByDia", query = "SELECT s FROM Solicitud s "
    + "LEFT JOIN s.estudiante.horario_disponible.dias d1 "
    + " WHERE d1.horas LIKE :horas AND "
            + " d1.dia_semana LIKE :dia"),
    @NamedQuery(name = "Solicitud.findSolicitudesByDiaYCurso", query = "SELECT s FROM Solicitud s "
    + "LEFT JOIN s.estudiante.horario_disponible.dias d1 "
    + " WHERE d1.horas LIKE :horas AND "
            + " d1.dia_semana LIKE :dia AND s.monitoria_solicitada.curso.codigo = :codigoCurso")
})
public class Solicitud implements Serializable {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 1L;

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "estado")
    private String estadoSolicitud;
    @Column(name = "fechaCreacion")
    private Timestamp fechaCreacion;

    @Column(name="responsablePreseleccion")
    private String responsablePreseleccion;
    /**
     * Esta relacion esta presente para el caso en el que se solicite una
     * monitoria y se asignen 2 monitorias Tipo T2
     */

    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<MonitoriaAceptada> monitorias;
    @OneToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Monitoria_Solicitada monitoria_solicitada;
    @ManyToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Aspirante estudiante;

    @Column(name = "fechaRadicacion")
    private Timestamp fechaRadicacion;
    @Column(name = "numeroRadicacion")
    private String numeroRadicacion;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aspirante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Aspirante estudiante) {
        this.estudiante = estudiante;
    }

    public Monitoria_Solicitada getMonitoria_solicitada() {
        return monitoria_solicitada;
    }

    public void setMonitoria_solicitada(Monitoria_Solicitada monitoria_solicitada) {
        this.monitoria_solicitada = monitoria_solicitada;
    }

    public Collection<MonitoriaAceptada> getMonitorias() {
        return monitorias;
    }

    public void setMonitorias(Collection<MonitoriaAceptada> monitoria) {
        this.monitorias = monitoria;
    }


    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public String getResponsablePreseleccion() {
        return responsablePreseleccion;
    }

    public void setResponsablePreseleccion(String responsablePreseleccion) {
        this.responsablePreseleccion = responsablePreseleccion;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaRadicacion() {
        return fechaRadicacion;
    }

    public void setFechaRadicacion(Timestamp fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }

    public String getNumeroRadicacion() {
        return numeroRadicacion;
    }

    public void setNumeroRadicacion(String numeroRadicacion) {
        this.numeroRadicacion = numeroRadicacion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Solicitud)) {
            return false;
        }
        Solicitud other = (Solicitud) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Solicitud[id=" + id + "]";
    }
}
