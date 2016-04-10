/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.CascadeType;
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
@Table(name = "Periodo_Planeacion")
@NamedQueries({
    @NamedQuery(name = "PeriodoPlaneacion.findByPeriodo", query = "SELECT e FROM PeriodoPlaneacion e  WHERE e.periodo= :periodo")    
})
public class PeriodoPlaneacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="periodo")
    private String periodo;

   @Column(name="fechaInicio")
    private Timestamp fechaInicio;

    @Column(name="fechaFin")
    private Timestamp fechaFin;

    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    private Collection<CargaProfesor> cargaProfesores;

    private boolean actual;

    public PeriodoPlaneacion(Long id, String periodo, Timestamp fechaInicio, Timestamp fechaFin, Collection<CargaProfesor> cargaProfesores, boolean actual) {
        this.id = id;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cargaProfesores = cargaProfesores;
        this.actual = actual;
    }

    public PeriodoPlaneacion(String periodo, Timestamp fechaInicio, Timestamp fechaFin, Collection<CargaProfesor> cargaProfesores, boolean actual) {
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cargaProfesores = cargaProfesores;
        this.actual = actual;
    }

   
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<CargaProfesor> getCargaProfesores() {
        return cargaProfesores;
    }

    public void setCargaProfesores(Collection<CargaProfesor> cargaProfesores) {
        this.cargaProfesores = cargaProfesores;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public PeriodoPlaneacion(String periodo, Collection<CargaProfesor> cargaProfesores) {
        this.periodo = periodo;
        this.cargaProfesores = cargaProfesores;
    }

    public PeriodoPlaneacion() {
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
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
        if (!(object instanceof PeriodoPlaneacion)) {
            return false;
        }
        PeriodoPlaneacion other = (PeriodoPlaneacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.PeriodoPlaneacion[id=" + id + "]";
    }

}
