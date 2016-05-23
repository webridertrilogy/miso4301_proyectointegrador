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
 * @author Asistente
 */
@Entity
@Table(name = "Nivel_Tesis")
@NamedQueries({
    @NamedQuery(name = "NivelTesis.findAll", query = "SELECT e FROM NivelTesis e"),

    @NamedQuery(name = "NivelTesis.findByNivelTesis", query = "SELECT e FROM NivelTesis e WHERE e.nivelTesis = :nivelTesis")
})
public class NivelTesis implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="nivelTesis")
    private String nivelTesis;

    public NivelTesis() {
    }

    public NivelTesis(String nivelTesis) {
        this.nivelTesis = nivelTesis;
    }

    public NivelTesis(Long id, String nivelTesis) {
        this.id = id;
        this.nivelTesis = nivelTesis;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNivelTesis() {
        return nivelTesis;
    }

    public void setNivelTesis(String nivelTesis) {
        this.nivelTesis = nivelTesis;
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
        if (!(object instanceof NivelTesis)) {
            return false;
        }
        NivelTesis other = (NivelTesis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.NivelTesis[id=" + id + "]";
    }

}
