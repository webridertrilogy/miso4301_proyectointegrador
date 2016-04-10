/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.entities.cyc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "h_evento")
public class h_evento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre_evento")
    private String nombre;
    @Column(name = "observaciones", length=3000)
    private String observaciones;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private h_cargaProfesor_cyc profesor;

    public h_evento(Long id, String nombre, String observaciones, h_cargaProfesor_cyc profesor) {
        this.id = id;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.profesor = profesor;
    }

    public h_evento(String nombre, String observaciones, h_cargaProfesor_cyc profesor) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.profesor = profesor;
    }

    public h_evento() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public h_cargaProfesor_cyc getProfesor() {
        return profesor;
    }

    public void setProfesor(h_cargaProfesor_cyc profesor) {
        this.profesor = profesor;
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
        if (!(object instanceof h_evento)) {
            return false;
        }
        h_evento other = (h_evento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Evento[id=" + id + "]";
    }
}
