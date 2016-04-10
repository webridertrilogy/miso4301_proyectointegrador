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
import javax.persistence.Table;

/**
 * @author Ivan Melo
 * @version 1.0
 * @created 11-may-2010 12:13:45 p.m.
 */
@Entity
@Table(name = "h_curso_planeado")
@NamedQueries({
    @NamedQuery(name = "h_curso_planeado.findAll", query = "SELECT e FROM h_curso_planeado e"),
    @NamedQuery(name = "h_curso_planeado.findById", query = "SELECT e FROM h_curso_planeado e WHERE e.id = :id")
})
public class h_curso_planeado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre_curso")
    private String nombreCurso;
    @Column(name = "carga")
    private Double carga;
    @Column(name = "Observaciones", length=3000)
    private String observaciones;
     @Column(name = "nivelDelCurso")
    private String nivelDelCurso;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private h_cargaProfesor_cyc profesor;

    public h_curso_planeado(Long id, String nombreCurso, Double carga, String observaciones, String nivelDelCurso, h_cargaProfesor_cyc profesor) {
        this.id = id;
        this.nombreCurso = nombreCurso;
        this.carga = carga;
        this.observaciones = observaciones;
        this.nivelDelCurso = nivelDelCurso;
        this.profesor = profesor;
    }

    public h_curso_planeado(String nombreCurso, Double carga, String observaciones, String nivelDelCurso, h_cargaProfesor_cyc profesor) {
        this.nombreCurso = nombreCurso;
        this.carga = carga;
        this.observaciones = observaciones;
        this.nivelDelCurso = nivelDelCurso;
        this.profesor = profesor;
    }

    public h_curso_planeado() {
    }

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

    public String getNivelDelCurso() {
        return nivelDelCurso;
    }

    public void setNivelDelCurso(String nivelDelCurso) {
        this.nivelDelCurso = nivelDelCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public h_cargaProfesor_cyc getProfesor() {
        return profesor;
    }

    public void setProfesor(h_cargaProfesor_cyc profesor) {
        this.profesor = profesor;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof h_curso_planeado)) {
            return false;
        }
        h_curso_planeado other = (h_curso_planeado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.CursoPlaneado[id=" + id + "]";
    }
}//end CursoPlaneado

