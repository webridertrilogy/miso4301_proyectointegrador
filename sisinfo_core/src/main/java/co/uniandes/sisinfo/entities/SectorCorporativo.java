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
@Table(name = "sector_corporativo")
@NamedQueries({
    @NamedQuery(name = "SectorCorporativo.findAll", query = "SELECT e FROM SectorCorporativo e ORDER BY e.nombre"),
    @NamedQuery(name = "SectorCorporativo.findById", query = "SELECT e FROM SectorCorporativo e WHERE e.id = :id"),
    @NamedQuery(name = "SectorCorporativo.findByNombre", query = "SELECT e FROM SectorCorporativo e WHERE e.nombre = :nombre")
})
public class SectorCorporativo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name = "id")
    private Long id;

     @Column(name = "nombre")
    private String nombre;


    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof SectorCorporativo)) {
            return false;
        }
        SectorCorporativo other = (SectorCorporativo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.SectorCorporativo[id=" + id + "]";
    }

}
