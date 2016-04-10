/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities.cyc;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "h_intencion_publicacion")
@NamedQueries({
    @NamedQuery(name = "h_intencion_publicacion.findByTituloObservacionesTipo", query = "SELECT i FROM h_intencion_publicacion i WHERE i.tituloPublicacion LIKE :titulo AND i.observaciones LIKE :observaciones"
    + " AND i.tipoPublicacion LIKE :tipo")})
public class h_intencion_publicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="titulo_publicacion")
    private String tituloPublicacion;

     @Column(name="observaciones", length=3000)
    private String observaciones;
    @Column(name="tipoPublicacion")
    private String tipoPublicacion;

    @ManyToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<h_cargaProfesor_cyc> coAutores;

    public h_intencion_publicacion() {
    }

    public h_intencion_publicacion(Long id, String tituloPublicacion,String observaciones, String tipoPublicacion, Collection<h_cargaProfesor_cyc> coAutores) {
        this.id = id;
        this.tituloPublicacion = tituloPublicacion;
        this.tipoPublicacion = tipoPublicacion;
        this.coAutores = coAutores;
      this.observaciones=observaciones;
    }

    public h_intencion_publicacion(String tituloPublicacion,String observaciones, String tipoPublicacion, Collection<h_cargaProfesor_cyc> coAutores) {
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

    public Collection<h_cargaProfesor_cyc> getCoAutores() {
        return coAutores;
    }

    public void setCoAutores(Collection<h_cargaProfesor_cyc> coAutores) {
        this.coAutores = coAutores;
    }

    

   

    public String getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(String tipoPublicacion) {
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
        if (!(object instanceof h_intencion_publicacion)) {
            return false;
        }
        h_intencion_publicacion other = (h_intencion_publicacion) object;
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
