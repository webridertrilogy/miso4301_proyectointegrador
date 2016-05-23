/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Entity
public class CategoriaCriterioJurado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombreCategoriaCriterio")
    private String nombreCategoriaCriterio;
    @Column(name = "porcentajeCategoria")
    private Double porcentajeCategoria;
    @Column(name = "ordenCategoria")
    private Integer ordenCategoria;
    @Column(name = "descripcion", length = 5000)
    private String descripcion;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Collection<CalificacionCriterio> calCriterios;
    @Column(name = "comentario", length = 5000)
    private String comentario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof CategoriaCriterioJurado)) {
            return false;
        }
        CategoriaCriterioJurado other = (CategoriaCriterioJurado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.CategoriaCriterioJurado[id=" + id + "]";
    }

    public Collection<CalificacionCriterio> getCalCriterios() {
        return calCriterios;
    }

    public void setCalCriterios(Collection<CalificacionCriterio> calCriterios) {
        this.calCriterios = calCriterios;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCategoriaCriterio() {
        return nombreCategoriaCriterio;
    }

    public void setNombreCategoriaCriterio(String nombreCategoriaCriterio) {
        this.nombreCategoriaCriterio = nombreCategoriaCriterio;
    }

    public Integer getOrdenCategoria() {
        return ordenCategoria;
    }

    public void setOrdenCategoria(Integer ordenCategoria) {
        this.ordenCategoria = ordenCategoria;
    }

    public Double getPorcentajeCategoria() {
        return porcentajeCategoria;
    }

    public void setPorcentajeCategoria(Double porcentajeCategoria) {
        this.porcentajeCategoria = porcentajeCategoria;
    }

    
}
