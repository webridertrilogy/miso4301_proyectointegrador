/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities.tesisM;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Asistente
 */
@Entity
public class h_detalleCalificacion_tesis_2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="categoriaNombre")
    private String categoriaNombre;
    @Column(name="categoriaDescripcion")
    private String categoriaDescripcion;
    @Column(name="categoriaPorcentaje")
    private Double categoriaPorcentaje;
    @Column(name="criterioNombre")
    private String criterioNombre;
    @Column(name="criterioDescripcion", length=5000)
    private String criterioDescripcion;
    @Column(name="criterioPeso")
    private Double criterioPeso;
    @Column(name="criterioComentario", length=5000)
    private String criterioComentario;
    @Column(name="criterioValor")
    private Double criterioValor;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoriaDescripcion() {
        return categoriaDescripcion;
    }

    public void setCategoriaDescripcion(String categoriaDescripcion) {
        this.categoriaDescripcion = categoriaDescripcion;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Double getCategoriaPorcentaje() {
        return categoriaPorcentaje;
    }

    public void setCategoriaPorcentaje(Double categoriaPorcentaje) {
        this.categoriaPorcentaje = categoriaPorcentaje;
    }

    public String getCriterioDescripcion() {
        return criterioDescripcion;
    }

    public void setCriterioDescripcion(String criterioDescripcion) {
        this.criterioDescripcion = criterioDescripcion;
    }

    public String getCriterioNombre() {
        return criterioNombre;
    }

    public void setCriterioNombre(String criterioNombre) {
        this.criterioNombre = criterioNombre;
    }

    public Double getCriterioPeso() {
        return criterioPeso;
    }

    public void setCriterioPeso(Double criterioPeso) {
        this.criterioPeso = criterioPeso;
    }

    public String getCriterioComentario() {
        return criterioComentario;
    }

    public void setCriterioComentario(String criterioComentario) {
        this.criterioComentario = criterioComentario;
    }

    public Double getCriterioValor() {
        return criterioValor;
    }

    public void setCriterioValor(Double criterioValor) {
        this.criterioValor = criterioValor;
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
        if (!(object instanceof h_detalleCalificacion_tesis_2)) {
            return false;
        }
        h_detalleCalificacion_tesis_2 other = (h_detalleCalificacion_tesis_2) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.tesisM.h_detalleCalificacion_tesis_2[id=" + id + "]";
    }

}
