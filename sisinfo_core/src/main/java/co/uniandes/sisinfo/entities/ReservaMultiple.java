/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "ReservaMultiple")
@NamedQueries({
    @NamedQuery(name = "ReservaMultiple.findAll", query = "SELECT r FROM ReservaMultiple r"),
    @NamedQuery(name = "ReservaMultiple.findById", query = "SELECT r FROM ReservaMultiple r WHERE r.id = :id"),
    @NamedQuery(name = "ReservaMultiple.findReservaMultipleByPeriodicidad", query = "SELECT r FROM ReservaMultiple r WHERE r.periodicidad = :periodicidad"),
    @NamedQuery(name = "ReservaMultiple.findReservaMultipleByFinalizacionReservaMultiple", query = "SELECT r FROM ReservaMultiple r WHERE r.finalizacionReservaMultiple = :finalizacionReservaMultiple"),
    @NamedQuery(name = "ReservaMultiple.findReservaMultipleByReserva", query = "SELECT r FROM ReservaMultiple r LEFT JOIN r.reservas r2 WHERE r2.id = :id")
})
public class ReservaMultiple implements Serializable {

    private static final long serialVersionUID = 1L;
    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "periodicidad")
    private String periodicidad;
    @Column
    private Timestamp inicioReservaMultiple;
    @Column
    private Timestamp finalizacionReservaMultiple;
    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<ReservaInventario> reservas;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public ReservaMultiple() {
    }

    public ReservaMultiple(String periodicidad, Timestamp inicioReservaMultiple, Timestamp finalizacionReservaMultiple, Collection<ReservaInventario> reservas) {
        this.periodicidad = periodicidad;
        this.inicioReservaMultiple = inicioReservaMultiple;
        this.finalizacionReservaMultiple = finalizacionReservaMultiple;
        this.reservas = reservas;
    }

    public Timestamp getInicioReservaMultiple() {
        return inicioReservaMultiple;
    }

    public void setInicioReservaMultiple(Timestamp inicioReservaMultiple) {
        this.inicioReservaMultiple = inicioReservaMultiple;
    }

    public Timestamp getFinalizacionReservaMultiple() {
        return finalizacionReservaMultiple;
    }

    public void setFinalizacionReservaMultiple(Timestamp finalizacionReservaMultiple) {
        this.finalizacionReservaMultiple = finalizacionReservaMultiple;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public Collection<ReservaInventario> getReservas() {
        return reservas;
    }

    public void setReservas(Collection<ReservaInventario> reservas) {
        this.reservas = reservas;
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
        if (!(object instanceof ReservaMultiple)) {
            return false;
        }
        ReservaMultiple other = (ReservaMultiple) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ReservaMultiple[id=" + id + "]";
    }

    
}
