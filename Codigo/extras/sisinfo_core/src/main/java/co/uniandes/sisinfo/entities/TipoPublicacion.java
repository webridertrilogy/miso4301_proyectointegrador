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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ivan Melo
 */
@Entity
@Table(name= "tipopublicacion")
@NamedQueries({
    @NamedQuery(name = "TipoPublicacion.findById", query = "SELECT r FROM TipoPublicacion r WHERE r.id = :id"),
    @NamedQuery(name = "TipoPublicacion.findByTipoPublicacion", query = "SELECT r FROM TipoPublicacion r WHERE r.tipoPublicacion = :tipoPublicacion")
})
public class TipoPublicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="tipo_publicacion")
    private String tipoPublicacion;

    public TipoPublicacion() {
    }

    public TipoPublicacion(Long id, String tipoPublicacion) {
        this.id = id;
        this.tipoPublicacion = tipoPublicacion;
    }

    public TipoPublicacion(String tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }

    public String getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(String tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }
    
    


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
        if (!(object instanceof TipoPublicacion)) {
            return false;
        }
        TipoPublicacion other = (TipoPublicacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.TipoPublicacion[id=" + id + "]";
    }

}
