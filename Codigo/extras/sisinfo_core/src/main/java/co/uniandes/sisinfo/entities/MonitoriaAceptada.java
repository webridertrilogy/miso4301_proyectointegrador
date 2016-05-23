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

import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad Monitoría
 */
@Entity
@Table(name = "monitoriaAceptada")
@NamedQueries({
    @NamedQuery(name = "MonitoriaAceptada.findAll", query = "SELECT m FROM MonitoriaAceptada m"),
    @NamedQuery(name = "MonitoriaAceptada.findById", query = "SELECT m FROM MonitoriaAceptada m WHERE m.id = :id"),
    @NamedQuery(name = "MonitoriaAceptada.findByCodigoEstudiante", query = "SELECT m FROM MonitoriaAceptada m WHERE m.solicitud.estudiante.estudiante.persona.codigo = :codigo"),
    @NamedQuery(name = "MonitoriaAceptada.findByCorreoEstudiante", query = "SELECT m FROM MonitoriaAceptada m WHERE m.solicitud.estudiante.estudiante.persona.correo = :correo"),
    @NamedQuery(name = "MonitoriaAceptada.findByCRNSeccion", query = "SELECT m FROM MonitoriaAceptada m WHERE m.seccion.crn = :crn"),
    @NamedQuery(name = "MonitoriaAceptada.findByCRNYCorreo", query = "SELECT m FROM MonitoriaAceptada m WHERE m.seccion.crn = :crn AND m.solicitud.estudiante.estudiante.persona.correo = :correo"),
    @NamedQuery(name = "MonitoriaAceptada.findBySolicitud", query = "SELECT m FROM MonitoriaAceptada m WHERE m.solicitud.id = :id")
})

public class MonitoriaAceptada implements Serializable {

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
    @Column(name = "carga")
    private Double carga;
    @OneToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Solicitud solicitud;
    @OneToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Seccion seccion;
    @Column(name = "nota")
    private double nota;
    @Column(name ="fecha")
    private Timestamp fechaCreacion;
    //---------------------------------------
    // Métodos
    //---------------------------------------

    public Double getCarga() {
        return carga;
    }

    public void setCarga(Double carga) {
        this.carga = carga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSecciones(Seccion seccion) {
        this.seccion = seccion;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonitoriaAceptada)) {
            return false;
        }
        MonitoriaAceptada other = (MonitoriaAceptada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Monitoria[id=" + id + "]";
    }
}
