package co.uniandes.sisinfo.entities;

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
 * @created 11-may-2010 12:13:45 p.m.
 */
@Entity
@Table(name = "CursoPlaneado")
@NamedQueries({
    @NamedQuery(name = "CursoPlaneado.findAll", query = "SELECT e FROM CursoPlaneado e"),
    @NamedQuery(name = "CursoPlaneado.findById", query = "SELECT e FROM CursoPlaneado e WHERE e.id = :id")
})
public class CursoPlaneado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre_curso")
    private String nombreCurso;
    @Column(name = "carga")
    private Double carga;
    @Column(name = "Observaciones", length=3000)
    private String observaciones;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private NivelFormacion nivelDelCurso;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private CargaProfesor profesor;

    public CursoPlaneado(Long id, String nombreCurso, Double carga, String observaciones, NivelFormacion nivelDelCurso, CargaProfesor profesor) {
        this.id = id;
        this.nombreCurso = nombreCurso;
        this.carga = carga;
        this.observaciones = observaciones;
        this.nivelDelCurso = nivelDelCurso;
        this.profesor = profesor;
    }

    public CursoPlaneado(String nombreCurso, Double carga, String observaciones, NivelFormacion nivelDelCurso, CargaProfesor profesor) {
        this.nombreCurso = nombreCurso;
        this.carga = carga;
        this.observaciones = observaciones;
        this.nivelDelCurso = nivelDelCurso;
        this.profesor = profesor;
    }

    public CursoPlaneado() {
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

    public NivelFormacion getNivelDelCurso() {
        return nivelDelCurso;
    }

    public void setNivelDelCurso(NivelFormacion nivelDelCurso) {
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

    public CargaProfesor getProfesor() {
        return profesor;
    }

    public void setProfesor(CargaProfesor profesor) {
        this.profesor = profesor;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CursoPlaneado)) {
            return false;
        }
        CursoPlaneado other = (CursoPlaneado) object;
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

