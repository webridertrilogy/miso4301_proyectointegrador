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
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "otras_actividades")
public class OtrasActividades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion", length=3000)
    private String descripcion;
    @Column(name = "dedicacion_semanal")
    private Double dedicacionSemanal;

    /*@ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private CargaProfesor cargaProfesor;*/

    public OtrasActividades(Long id, String nombre, String descripcion, Double dedicacionSemanal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dedicacionSemanal = dedicacionSemanal;
    }

    public OtrasActividades(String nombre, String descripcion, Double dedicacionSemanal) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dedicacionSemanal = dedicacionSemanal;
    }

    public OtrasActividades() {
    }

    public Double getDedicacionSemanal() {
        return dedicacionSemanal;
    }

    public void setDedicacionSemanal(Double dedicacionSemanal) {
        this.dedicacionSemanal = dedicacionSemanal;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtrasActividades)) {
            return false;
        }
        OtrasActividades other = (OtrasActividades) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Otros[id=" + id + "]";
    }
}
