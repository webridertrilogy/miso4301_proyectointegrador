/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Entity
@Table(name = "tema_tesis_pregrado")
@NamedQueries({
    @NamedQuery(name = "TemaTesisPregrado.findBycorreoAsesor", query = "SELECT i FROM TemaTesisPregrado i WHERE i.asesor.persona.correo =:correoasesor"),
    @NamedQuery(name = "TemaTesisPregrado.findByNombreTemaTesisYCorreoAsesor", query = "SELECT i FROM TemaTesisPregrado i WHERE i.nombreTema =:nombre AND i.asesor.persona.correo=:correo"),
    @NamedQuery(name = "TemaTesisPregrado.findByNombreTemaTesisYCorreoAsesorYPeriodo", query = "SELECT i FROM TemaTesisPregrado i WHERE i.nombreTema =:nombre AND i.asesor.persona.correo=:correo AND i.periodo.nombre=:periodo"),
    @NamedQuery(name = "TemaTesisPregrado.findByPeriodoYCorreoAsesor", query = "SELECT i FROM TemaTesisPregrado i WHERE i.asesor.persona.correo =:correoasesor AND i.periodo.nombre =:periodo")
})
public class TemaTesisPregrado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombreTema")
    private String nombreTema;
    @Column(name = " descripcion", length = 5000)
    private String descripcion;
    @Column(name = "areasInteres")
    private String areasInteres;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private CategoriaProyectoDeGrado categoria;
    @Column(name = "numeroEstudiantes")
    private Integer numeroEstudiantes;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Profesor asesor;
    @ManyToOne
    private PeriodoTesisPregrado periodo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreasInteres() {
        return areasInteres;
    }

    public void setAreasInteres(String areasInteres) {
        this.areasInteres = areasInteres;
    }

    public Profesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Profesor asesor) {
        this.asesor = asesor;
    }

    public CategoriaProyectoDeGrado getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProyectoDeGrado categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreTema() {
        return nombreTema;
    }

    public void setNombreTema(String nombreTema) {
        this.nombreTema = nombreTema;
    }

    public Integer getNumeroEstudiantes() {
        return numeroEstudiantes;
    }

    public void setNumeroEstudiantes(Integer numeroEstudiantes) {
        this.numeroEstudiantes = numeroEstudiantes;
    }

    public PeriodoTesisPregrado getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoTesisPregrado periodo) {
        this.periodo = periodo;
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
        if (!(object instanceof TemaTesisPregrado)) {
            return false;
        }
        TemaTesisPregrado other = (TemaTesisPregrado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.TemaTesisPregrado[id=" + id + "]";
    }
}
