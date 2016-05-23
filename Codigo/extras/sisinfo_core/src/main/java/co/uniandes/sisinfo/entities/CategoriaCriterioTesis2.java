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
public class CategoriaCriterioTesis2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="nombre")
    private String nombre;
    @Column(name="descripcion" , length=5000)
    private String descripcion;
    @OneToMany(fetch = FetchType.LAZY,  cascade = {CascadeType.ALL })
    private Collection<CriterioCalificacion> criterios;
    @Column(name="ordenCategoria" , unique=true)
    private Integer ordenCategoria;
    @Column(name="porcentaCategoria")
    private Double porcentaCategoria;


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
        if (!(object instanceof CategoriaCriterioTesis2)) {
            return false;
        }
        CategoriaCriterioTesis2 other = (CategoriaCriterioTesis2) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.CategoriaCriterioTesis2[id=" + id + "]";
    }

    public Collection<CriterioCalificacion> getCriterios() {
        return criterios;
    }

    public void setCriterios(Collection<CriterioCalificacion> criterios) {
        this.criterios = criterios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaCriterioTesis2() {
    }

    public Integer getOrdenCategoria() {
        return ordenCategoria;
    }

    public void setOrdenCategoria(Integer ordenCategoria) {
        this.ordenCategoria = ordenCategoria;
    }

    public Double getPorcentaCategoria() {
        return porcentaCategoria;
    }

    public void setPorcentaCategoria(Double porcentaCategoria) {
        this.porcentaCategoria = porcentaCategoria;
    }
}
