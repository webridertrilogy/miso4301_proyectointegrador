/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import java.util.Collection;
import javax.persistence.ManyToMany;
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
@Table(name = "Proyecto_Financiado")
@NamedQueries({
    @NamedQuery(name = "ProyectoFinanciado.findById", query = "SELECT r FROM ProyectoFinanciado r WHERE r.id = :id"),
    @NamedQuery(name = "ProyectoFinanciado.findByNombre", query = "SELECT r FROM ProyectoFinanciado r WHERE r.nombre = :nombre")
})
public class ProyectoFinanciado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre_Proyecto")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<CargaProfesor> profesores;
     @Column(name = "entidad_financiadora", length=3000)
    private String entidadFinanciadora;

    public ProyectoFinanciado(Long id, String nombre, String descripcion, Collection<CargaProfesor> profesores,String entidadFinanciadora) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesores = profesores;
        this.entidadFinanciadora=entidadFinanciadora;
    }

    public ProyectoFinanciado(String nombre, String descripcion, Collection<CargaProfesor> profesores,String entidadFinanciadora) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesores = profesores;
        this.entidadFinanciadora=entidadFinanciadora;
    }

    public ProyectoFinanciado() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Collection<CargaProfesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(Collection<CargaProfesor> profesores) {
        this.profesores = profesores;
    }

    public String getEntidadFinanciadora() {
        return entidadFinanciadora;
    }

    public void setEntidadFinanciadora(String entidadFinanciadora) {
        this.entidadFinanciadora = entidadFinanciadora;
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
        if (!(object instanceof ProyectoFinanciado)) {
            return false;
        }
        ProyectoFinanciado other = (ProyectoFinanciado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ProyectoFinanciado[id=" + id + "]";
    }
}
