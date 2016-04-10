package co.uniandes.sisinfo.entities;


import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Ivan Melo
 * @version 1.0
 * @created 11-may-2010 13:13:45 p.m.
 */
@Entity
@Table(name = "DireccionTesis")
@NamedQueries({
    @NamedQuery(name = "DireccionTesis.findAll", query = "SELECT e FROM DireccionTesis e"),
    @NamedQuery(name = "DireccionTesis.findById", query = "SELECT e FROM DireccionTesis e WHERE e.id = :id"),
     @NamedQuery(name = "DireccionTesis.findByNombre", query = "SELECT e FROM DireccionTesis e WHERE e.titulo = :nombre")
})
public class DireccionTesis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "titulo_tesis")
    private String titulo;
    @Column(name = "observaciones", length=3000)
    private String observaciones;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private NivelTesis nivelEstadoTesis;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private NivelFormacion nivelFormacionTesis;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private CargaProfesor directorTesis;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Estudiante autorEstudiante;

    public DireccionTesis() {
    }

    public DireccionTesis(Long id, String titulo, String observaciones, NivelTesis nivelEstadoTesis, NivelFormacion nivelDeTesis, CargaProfesor directorTesis, Estudiante autorEstudiante) {
        this.id = id;
        this.titulo = titulo;
        this.observaciones = observaciones;
        this.nivelEstadoTesis = nivelEstadoTesis;
        this.nivelFormacionTesis = nivelDeTesis;
        this.directorTesis = directorTesis;
        this.autorEstudiante = autorEstudiante;
    }

    public DireccionTesis(String titulo, String observaciones, NivelTesis nivelTesis, NivelFormacion nivelEstadoTesis, CargaProfesor directorTesis, Estudiante autorEstudiante) {
        this.titulo = titulo;
        this.observaciones = observaciones;
        this.nivelEstadoTesis = nivelTesis;
        this.nivelFormacionTesis = nivelEstadoTesis;
        this.directorTesis = directorTesis;
        this.autorEstudiante = autorEstudiante;
    }

    public Estudiante getAutorEstudiante() {
        return autorEstudiante;
    }

    public void setAutorEstudiante(Estudiante autorEstudiante) {
        this.autorEstudiante = autorEstudiante;
    }

    public CargaProfesor getDirectorTesis() {
        return directorTesis;
    }

    public void setDirectorTesis(CargaProfesor directorTesis) {
        this.directorTesis = directorTesis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NivelFormacion getNivelFormacionTesis() {
        return nivelFormacionTesis;
    }

    public void setNivelFormacionTesis(NivelFormacion nivelDeTesis) {
        this.nivelFormacionTesis = nivelDeTesis;
    }

    public NivelTesis getEstadoTesis() {
        return nivelEstadoTesis;
    }

    public void setEstadoTesis(NivelTesis nivelTesis) {
        this.nivelEstadoTesis = nivelTesis;
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
        if (!(object instanceof DireccionTesis)) {
            return false;
        }
        DireccionTesis other = (DireccionTesis) object;
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
