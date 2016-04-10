/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author david
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "h_curso.findByCRN", query = "SELECT c FROM h_curso c LEFT JOIN c.secciones sec WHERE sec.crn = :crn"),
    @NamedQuery(name = "h_curso.findBySeccionId", query = "SELECT c FROM h_curso c LEFT JOIN c.secciones sec WHERE sec.id = :id")
})
public class h_curso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String codigo;

    private double creditos;

    private String nombre;

    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
    private Collection<h_seccion> secciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getCreditos() {
        return creditos;
    }

    public void setCreditos(double creditos) {
        this.creditos = creditos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<h_seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(Collection<h_seccion> secciones) {
        this.secciones = secciones;
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
        if (!(object instanceof h_curso)) {
            return false;
        }
        h_curso other = (h_curso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.h_curso[id=" + id + "]";
    }

}
