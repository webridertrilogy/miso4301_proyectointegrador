/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Manuel
 */
@Entity
@Table(name = "TimerAuditoria")
public class TimerAuditoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="fechaEjecucionTimer")
    private Timestamp fechaEjecucionTimer;

    @Column(name="direccionInterfaz")
    private String direccionInterfaz;

    @Column(name="nombreMetodoALlamar")
    private String nombreMetodoALLamar;

    @Column(name="fechaFin")
    private Timestamp fechaFin;

    @Column(name="infoTimer")
    private String infoTimer;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccionInterfaz() {
        return direccionInterfaz;
    }

    public void setDireccionInterfaz(String direccionInterfaz) {
        this.direccionInterfaz = direccionInterfaz;
    }

    public Timestamp getFechaEjecucionTimer() {
        return fechaEjecucionTimer;
    }

    public void setFechaEjecucionTimer(Timestamp fechaEjecucionTimer) {
        this.fechaEjecucionTimer = fechaEjecucionTimer;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getInfoTimer() {
        return infoTimer;
    }

    public void setInfoTimer(String infoTimer) {
        this.infoTimer = infoTimer;
    }

    public String getNombreMetodoALLamar() {
        return nombreMetodoALLamar;
    }

    public void setNombreMetodoALLamar(String nombreMetodoALLamar) {
        this.nombreMetodoALLamar = nombreMetodoALLamar;
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
        if (!(object instanceof TimerAuditoria)) {
            return false;
        }
        TimerAuditoria other = (TimerAuditoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.TimerAuditoria[id=" + id + "]";
    }

}
