/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities.datosmaestros;

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
 * Entidad NivelPlanta
 */
@Entity
@Table(name = "nivelPlanta")
@NamedQueries({
    @NamedQuery(name = "NivelPlanta.findByNombre", query = "SELECT n FROM NivelPlanta n WHERE n.nombre LIKE :nombre")
})
public class NivelPlanta implements Serializable {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 1L;
    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    private String nombre;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NivelPlanta)) {
            return false;
        }
        NivelPlanta other = (NivelPlanta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.datosmaestros.NivelPlanta[id=" + id + "]";
    }
}
