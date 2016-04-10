/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author david
 */
@Entity
public class h_monitoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double creditos;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaRadicacion;

    private double nota;

    private String numeroRadicacion;

    private String periodo;

    private String tipoMonitoria;

    @OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
    private h_seccion seccion;

    @ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
    private h_monitor monitor;

    public Long getId() {
        return id;
    }

    public h_monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(h_monitor monitor) {
        this.monitor = monitor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCreditos() {
        return creditos;
    }

    public void setCreditos(double creditos) {
        this.creditos = creditos;
    }

    public h_seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(h_seccion seccion) {
        this.seccion = seccion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaRadicacion() {
        return fechaRadicacion;
    }

    public void setFechaRadicacion(Date fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getNumeroRadicacion() {
        return numeroRadicacion;
    }

    public void setNumeroRadicacion(String numeroRadicacion) {
        this.numeroRadicacion = numeroRadicacion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getTipoMonitoria() {
        return tipoMonitoria;
    }

    public void setTipoMonitoria(String tipoMonitoria) {
        this.tipoMonitoria = tipoMonitoria;
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
        if (!(object instanceof h_monitoria)) {
            return false;
        }
        h_monitoria other = (h_monitoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.h_monitoria[id=" + id + "]";
    }

}
