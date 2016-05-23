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
 * @author Ivan Mauricio Melo Suarez
 */
@Entity
@Table(name = "ListaBlancaErroresSisinfo")
@NamedQueries({
    @NamedQuery(name = "ListaBlancaErroresSisinfo.findByidError", query = "SELECT i FROM ListaBlancaErroresSisinfo i WHERE i.idError =:idError ")

})
public class ListaBlancaErroresSisinfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name= "idError")
    private String idError;
    @Column(name= "explicacion")
    private String explicacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    public String getIdError() {
        return idError;
    }

    public void setIdError(String idError) {
        this.idError = idError;
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
        if (!(object instanceof ListaBlancaErroresSisinfo)) {
            return false;
        }
        ListaBlancaErroresSisinfo other = (ListaBlancaErroresSisinfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ListaBlancaErroresSisinfo[id=" + id + ", error=" + idError + ", explicacion=" + explicacion + "]";
    }
}
