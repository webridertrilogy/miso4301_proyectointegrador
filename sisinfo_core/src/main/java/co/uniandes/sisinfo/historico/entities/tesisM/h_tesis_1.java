package co.uniandes.sisinfo.historico.entities.tesisM;

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
 * Histórico de Tesis 1
 * @author Marcela Morales
 */
@Entity
@Table(name = "h_tesis_1")
@NamedQueries({
    @NamedQuery(name = "h_tesis_1.findByCorreoEstudiante", query = "SELECT e FROM h_tesis_1 e WHERE e.correoEstudiante = :correo"),
    @NamedQuery(name = "h_tesis_1.findByCorreoAsesor", query = "SELECT e FROM h_tesis_1 e WHERE e.correoAsesor = :correo"),
    @NamedQuery(name = "h_tesis_1.findByEstado", query = "SELECT e FROM h_tesis_1 e WHERE e.estado = :estado"),
    @NamedQuery(name = "h_tesis_1.findBySemestre", query = "SELECT e FROM h_tesis_1 e WHERE e.semestre = :semestre"),
    @NamedQuery(name = "h_tesis_1.findByEstadoYSemestre", query = "SELECT e FROM h_tesis_1 e WHERE e.estado = :estado AND e.semestre = :semestre"),
    @NamedQuery(name = "h_tesis_1.findByEstadoYCorreoAsesor", query = "SELECT e FROM h_tesis_1 e WHERE e.estado = :estado AND e.correoAsesor = :correo"),
    @NamedQuery(name = "h_tesis_1.findBySemestreYCorreoAsesor", query = "SELECT e FROM h_tesis_1 e WHERE e.semestre = :semestre AND e.correoAsesor = :correo"),
    @NamedQuery(name = "h_tesis_1.findByEstadoYSemestreYCorreoAsesor", query = "SELECT e FROM h_tesis_1 e WHERE e.estado = :estado AND e.semestre = :semestre AND e.correoAsesor = :correo")
})
public class h_tesis_1 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fechaCreacion")
    private Timestamp fechaCreacion;
    @Column(name = "fechaFin")
    private Timestamp fechaFin;
    @Column(name = "tema")
    private String tema;
    @Column(name = "rutaArticulo")
    private String rutaArticulo;
    @Column(name = "nota")
    private Double nota;
    @Column(name = "notaAprobado")
    private String notaAprobado;
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
    @Column(name = "subareaInvestigacion")
    private String subareaInvestigacion;

    @Column(name = "aprobadoParaParadigma")
    private Boolean aprobadoParaParadigma;

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

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getRutaArticulo() {
        return rutaArticulo;
    }

    public void setRutaArticulo(String rutaArticulo) {
        this.rutaArticulo = rutaArticulo;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getSubareaInvestigacion() {
        return subareaInvestigacion;
    }

    public void setSubareaInvestigacion(String subareaInvestigacion) {
        this.subareaInvestigacion = subareaInvestigacion;
    }

    public String getNotaAprobado() {
        return notaAprobado;
    }

    public void setNotaAprobado(String notaAprobado) {
        this.notaAprobado = notaAprobado;
    }

    public Boolean getAprobadoParaParadigma() {
        return aprobadoParaParadigma;
    }

    public void setAprobadoParaParadigma(Boolean aprobadoParaParadigma) {
        this.aprobadoParaParadigma = aprobadoParaParadigma;
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
        if (!(object instanceof h_tesis_1)) {
            return false;
        }
        h_tesis_1 other = (h_tesis_1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_1[id=" + id + "]";
    }
}
