/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.GrupoInvestigacion;
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
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Entity
@Table(name = "tema_tesis_1")
@NamedQueries({
      @NamedQuery(name = "TemaTesis1.findBycorreoAsesor", query = "SELECT i FROM TemaTesis1 i WHERE i.asesor.persona.correo =:correoasesor"),
    @NamedQuery(name = "TemaTesis1.findByNombreTemaTesis", query = "SELECT i FROM TemaTesis1 i WHERE i.nombreTema =:nombre"),
      @NamedQuery(name = "TemaTesis1.findBySemestreTesis", query = "SELECT i FROM TemaTesis1 i WHERE i.periodo.periodo =:semestre")

})
public class TemaTesis1 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="nombreTema")
    private String nombreTema;
    @Column(name = " descripcion", length=5000)
    private String descripcion;
    @Column(name="numeroEstudiantes")
    private Integer numeroEstudiantes;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Profesor asesor;
    @ManyToOne
    private PeriodoTesis periodo;
    @Column(name="areasInteres", length=5000)
    private String areasInteres;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private SubareaInvestigacion subarea;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private GrupoInvestigacion grupoInvestigacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Profesor asesor) {
        this.asesor = asesor;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TemaTesis1)) {
            return false;
        }
        TemaTesis1 other = (TemaTesis1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getAreasInteres() {
        return areasInteres;
    }

    public void setAreasInteres(String areasInteres) {
        this.areasInteres = areasInteres;
    }

    public PeriodoTesis getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoTesis periodo) {
        this.periodo = periodo;
    }

    public SubareaInvestigacion getSubarea() {
        return subarea;
    }

    public void setSubarea(SubareaInvestigacion subarea) {
        this.subarea = subarea;
    }

    public GrupoInvestigacion getGrupoInvestigacion() {
        return grupoInvestigacion;
    }

    public void setGrupoInvestigacion(GrupoInvestigacion grupoInvestigacion) {
        this.grupoInvestigacion = grupoInvestigacion;
    }

    

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.TemaTesis1[id=" + id + "]";
    }

}
