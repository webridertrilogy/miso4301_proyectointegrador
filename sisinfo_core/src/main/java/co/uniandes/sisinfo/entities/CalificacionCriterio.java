/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Entity
public class CalificacionCriterio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "valor")
    private Double valor;
    @Column(name = "nombreCriterioCalificacion")
    private String nombre;
    @Column(name = "descripcionCriterioCalificacion" ,length=5000)
    private String descripcion;
    @Column(name = "pesoCriterioCalificacion")
    private Double peso;
    @Column(name = "ordenCriterio")
    private Integer ordenCriterio;
    @Column(name="comentario", length=5000)
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
        if (!(object instanceof CalificacionCriterio)) {
            return false;
        }
        CalificacionCriterio other = (CalificacionCriterio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.CalificacionCriterio[id=" + id + "]";
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
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
    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public CalificacionCriterio() {
    }


    public Integer getOrdenCriterio() {
        return ordenCriterio;
    }

    public void setOrdenCriterio(Integer ordenCriterio) {
        this.ordenCriterio = ordenCriterio;
    }

   

//    public Double getPorcentajeCategoria() {
//        return porcentajeCategoria;
//    }
//
//    public void setPorcentajeCategoria(Double porcentajeCategoria) {
//        this.porcentajeCategoria = porcentajeCategoria;
//    }



    
}
