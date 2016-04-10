package co.uniandes.sisinfo.historico.entities.tesispregrado;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Histórico de Proyectos de Grado
 * @author Marcela Morales
 */
@Entity
@Table(name = "h_tesisPregrado")
@NamedQueries({
    @NamedQuery(name = "h_tesisPregrado.findByCorreoEstudiante", query = "SELECT e FROM h_tesisPregrado e WHERE e.correoEstudiante = :correo"),
    @NamedQuery(name = "h_tesisPregrado.findByCorreoAsesor", query = "SELECT e FROM h_tesisPregrado e WHERE e.correoAsesor = :correo"),
    @NamedQuery(name = "h_tesisPregrado.findByEstado", query = "SELECT e FROM h_tesisPregrado e WHERE e.estado = :estado"),
    @NamedQuery(name = "h_tesisPregrado.findBySemestre", query = "SELECT e FROM h_tesisPregrado e WHERE e.semestre = :semestre"),
    @NamedQuery(name = "h_tesisPregrado.findByEstadoYSemestre", query = "SELECT e FROM h_tesisPregrado e WHERE e.estado = :estado AND e.semestre = :semestre"),
    @NamedQuery(name = "h_tesisPregrado.findByEstadoYCorreoAsesor", query = "SELECT e FROM h_tesisPregrado e WHERE e.estado = :estado AND e.correoAsesor = :correo"),
    @NamedQuery(name = "h_tesisPregrado.findBySemestreYCorreoAsesor", query = "SELECT e FROM h_tesisPregrado e WHERE e.semestre = :semestre AND e.correoAsesor = :correo"),
    @NamedQuery(name = "h_tesisPregrado.findByEstadoYSemestreYCorreoAsesor", query = "SELECT e FROM h_tesisPregrado e WHERE e.estado = :estado AND e.semestre = :semestre AND e.correoAsesor = :correo")
})
public class h_tesisPregrado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fechaCreacion")
    private Timestamp fechaCreacion;
    @Column(name = "fechaFin")
    private Timestamp fechaFin;
    @Column(name = "temaTesis")
    private String temaTesis;
    @Column(name = "rutaPosterTesis")
    private String rutaPosterTesis;
    @Column(name = "calificacionTesis")
    private Double calificacionTesis;
    @Column(name = "rutaABET")
    private String rutaABET;
    @Column(name = "nombresEstudiante")
    private String nombresEstudiante;
    @Column(name = "apellidosEstudiante")
    private String apellidosEstudiante;
    @Column(name = "correoEstudiante")
    private String correoEstudiante;
    @Column(name = "nombresAsesor")
    private String nombresAsesor;
    @Column(name = "apellidosAsesor")
    private String apellidosAsesor;
    @Column(name = "correoAsesor")
    private String correoAsesor;
    @Column(name = "semestre")
    private String semestre;
    @Column(name = "estado")
    private String estado;
    @Column(name = "comentariosAsesor" , length=5000 )
    private String comentariosAsesor;

    public String getApellidosAsesor() {
        return apellidosAsesor;
    }

    public void setApellidosAsesor(String apellidosAsesor) {
        this.apellidosAsesor = apellidosAsesor;
    }

    public String getApellidosEstudiante() {
        return apellidosEstudiante;
    }

    public void setApellidosEstudiante(String apellidosEstudiante) {
        this.apellidosEstudiante = apellidosEstudiante;
    }

    public Double getCalificacionTesis() {
        return calificacionTesis;
    }

    public void setCalificacionTesis(Double calificacionTesis) {
        this.calificacionTesis = calificacionTesis;
    }

    public String getComentariosAsesor() {
        return comentariosAsesor;
    }

    public void setComentariosAsesor(String comentariosAsesor) {
        this.comentariosAsesor = comentariosAsesor;
    }
    
    public String getCorreoAsesor() {
        return correoAsesor;
    }

    public void setCorreoAsesor(String correoAsesor) {
        this.correoAsesor = correoAsesor;
    }

    public String getCorreoEstudiante() {
        return correoEstudiante;
    }

    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombresAsesor() {
        return nombresAsesor;
    }

    public void setNombresAsesor(String nombresAsesor) {
        this.nombresAsesor = nombresAsesor;
    }

    public String getNombresEstudiante() {
        return nombresEstudiante;
    }

    public void setNombresEstudiante(String nombresEstudiante) {
        this.nombresEstudiante = nombresEstudiante;
    }

    public String getRutaABET() {
        return rutaABET;
    }

    public void setRutaABET(String rutaABET) {
        this.rutaABET = rutaABET;
    }

    public String getRutaPosterTesis() {
        return rutaPosterTesis;
    }

    public void setRutaPosterTesis(String rutaPosterTesis) {
        this.rutaPosterTesis = rutaPosterTesis;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getTemaTesis() {
        return temaTesis;
    }

    public void setTemaTesis(String temaTesis) {
        this.temaTesis = temaTesis;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof h_tesisPregrado)) {
            return false;
        }
        h_tesisPregrado other = (h_tesisPregrado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.tesispregrado.h_tesis_pregrado[id=" + id + "]";
    }
}
