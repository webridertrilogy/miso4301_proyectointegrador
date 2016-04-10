/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author david
 */
@Entity
public class h_informacionAcademica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
    private h_monitor monitor;

    private String periodo;

    private double promedioPenultimo;

    private double promedioUltimo;

    private double promedioTotal;

    private boolean actual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public double getPromedioPenultimo() {
        return promedioPenultimo;
    }

    public void setPromedioPenultimo(double promedioPenultimo) {
        this.promedioPenultimo = promedioPenultimo;
    }

    public double getPromedioTotal() {
        return promedioTotal;
    }

    public void setPromedioTotal(double promedioTotal) {
        this.promedioTotal = promedioTotal;
    }

    public double getPromedioUltimo() {
        return promedioUltimo;
    }

    public void setPromedioUltimo(double promedioUltimo) {
        this.promedioUltimo = promedioUltimo;
    }

    public h_monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(h_monitor monitor) {
        this.monitor = monitor;
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
        if (!(object instanceof h_informacionAcademica)) {
            return false;
        }
        h_informacionAcademica other = (h_informacionAcademica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.h_informacionAcademica[id=" + id + "]";
    }

}
