/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.util.Collection;
import javax.persistence.ManyToMany;

/**
 *
 * @author Ivan Melo
 */
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "intencion_Publicacion")
public class IntencionPublicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="titulo_publicacion")
    private String tituloPublicacion;

     @Column(name="observaciones", length=3000)
    private String observaciones;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private TipoPublicacion tipoPublicacion;

    @ManyToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<CargaProfesor> coAutores;

    public IntencionPublicacion() {
    }

    public IntencionPublicacion(Long id, String tituloPublicacion,String observaciones, TipoPublicacion tipoPublicacion, Collection<CargaProfesor> coAutores) {
        this.id = id;
        this.tituloPublicacion = tituloPublicacion;
        this.tipoPublicacion = tipoPublicacion;
        this.coAutores = coAutores;
      this.observaciones=observaciones;
    }

    public IntencionPublicacion(String tituloPublicacion,String observaciones, TipoPublicacion tipoPublicacion, Collection<CargaProfesor> coAutores) {
        this.tituloPublicacion = tituloPublicacion;
        this.tipoPublicacion = tipoPublicacion;
        this.coAutores = coAutores;
        this.observaciones=observaciones;
    }

   


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<CargaProfesor> getCoAutores() {
        return coAutores;
    }

    public void setCoAutores(Collection<CargaProfesor> coAutores) {
        this.coAutores = coAutores;
    }

    

   

    public TipoPublicacion getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(TipoPublicacion tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }

    public String getTituloPublicacion() {
        return tituloPublicacion;
    }

    public void setTituloPublicacion(String tituloPublicacion) {
        this.tituloPublicacion = tituloPublicacion;
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
        if (!(object instanceof IntencionPublicacion)) {
            return false;
        }
        IntencionPublicacion other = (IntencionPublicacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.IntencionPublicacion[id=" + id + "]";
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    
}
