/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "SubareaInvestigacion")
@NamedQueries({
    @NamedQuery(name = "SubareaInvestigacion.findByNombre", query = "SELECT e FROM SubareaInvestigacion e WHERE e.nombreSubarea = :nombre")
})
public class SubareaInvestigacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombreSubarea")
    private String nombreSubarea;
    @Column(name = "descripcion", length = 2000)
    private String descripcion;
    @ManyToOne(fetch = FetchType.LAZY,  cascade = {CascadeType.REFRESH })
    private Persona coordinadorSubarea;

    public Persona getCoordinadorSubarea() {
        return coordinadorSubarea;
    }

    public void setCoordinadorSubarea(Persona coordinadorSubarea) {
        this.coordinadorSubarea = coordinadorSubarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreSubarea() {
        return nombreSubarea;
    }

    public void setNombreSubarea(String nombreSubarea) {
        this.nombreSubarea = nombreSubarea;
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
        if (!(object instanceof SubareaInvestigacion)) {
            return false;
        }
        SubareaInvestigacion other = (SubareaInvestigacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.SubareaInvestigacion[id=" + id + "]";
    }
}
