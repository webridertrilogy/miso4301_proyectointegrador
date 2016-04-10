package co.uniandes.sisinfo.historico.entities.cyc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Ivan Melo
 * @version 1.0
 * @created 11-may-2010 13:13:45 p.m.
 */
@Entity
@Table(name = "h_direccion_tesis")
@NamedQueries({
    @NamedQuery(name = "h_direccion_tesis.findAll", query = "SELECT e FROM h_direccion_tesis e"),
    @NamedQuery(name = "h_direccion_tesis.findById", query = "SELECT e FROM h_direccion_tesis e WHERE e.id = :id"),
    @NamedQuery(name = "h_direccion_tesis.findByNombre", query = "SELECT e FROM h_direccion_tesis e WHERE e.titulo LIKE :nombre")
})
public class h_direccion_tesis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "titulo_tesis")
    private String titulo;
    @Column(name = "observaciones", length=3000)
    private String observaciones;
    @Column(name = "nivelEstadoTesis")
    private String nivelEstadoTesis;
    @Column(name = "nivelFormacionTesis")
    private String nivelFormacionTesis;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private h_cargaProfesor_cyc directorTesis;
    @Column(name = "nombreEstudiante")
    private String nombreEstudiante;
    @Column(name = "apellidoEstudiante")
    private String apellidoEstudiante;
    @Column(name = "correoEstudiante")
    private String correoEstudiante;

    public h_direccion_tesis() {
    }

    public h_direccion_tesis(Long id, String titulo, String observaciones, String nivelEstadoTesis, String nivelFormacionTesis, h_cargaProfesor_cyc directorTesis, String nombreEstudiante, String apellidoEstudiante, String correoEstudiante) {
        this.id = id;
        this.titulo = titulo;
        this.observaciones = observaciones;
        this.nivelEstadoTesis = nivelEstadoTesis;
        this.nivelFormacionTesis = nivelFormacionTesis;
        this.directorTesis = directorTesis;
        this.nombreEstudiante = nombreEstudiante;
        this.apellidoEstudiante = apellidoEstudiante;
        this.correoEstudiante = correoEstudiante;
    }

    public String getApellidoEstudiante() {
        return apellidoEstudiante;
    }

    public void setApellidoEstudiante(String apellidoEstudiante) {
        this.apellidoEstudiante = apellidoEstudiante;
    }

    public String getCorreoEstudiante() {
        return correoEstudiante;
    }

    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }

    public String getNivelEstadoTesis() {
        return nivelEstadoTesis;
    }

    public void setNivelEstadoTesis(String nivelEstadoTesis) {
        this.nivelEstadoTesis = nivelEstadoTesis;
    }

    public String getNivelFormacionTesis() {
        return nivelFormacionTesis;
    }

    public void setNivelFormacionTesis(String nivelFormacionTesis) {
        this.nivelFormacionTesis = nivelFormacionTesis;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public h_cargaProfesor_cyc getDirectorTesis() {
        return directorTesis;
    }

    public void setDirectorTesis(h_cargaProfesor_cyc directorTesis) {
        this.directorTesis = directorTesis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof h_direccion_tesis)) {
            return false;
        }
        h_direccion_tesis other = (h_direccion_tesis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.DireccionTesis[id=" + id + "]";
    }
}//end DireccionTesis

