/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities.cyc;

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
@Table(name = "h_periodo_planeacion")
@NamedQueries({
    @NamedQuery(name = "h_periodo_planeacion.findByPeriodo", query = "SELECT e FROM h_periodo_planeacion e  WHERE e.periodo  LIKE :periodo")
})
public class h_periodo_planeacion implements Serializable {
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
    private Collection<h_cargaProfesor_cyc> cargaProfesores;

    private boolean actual;

    public h_periodo_planeacion(Long id, String periodo, Timestamp fechaInicio, Timestamp fechaFin, Collection<h_cargaProfesor_cyc> cargaProfesores, boolean actual) {
        this.id = id;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cargaProfesores = cargaProfesores;
        this.actual = actual;
    }

    public h_periodo_planeacion(String periodo, Timestamp fechaInicio, Timestamp fechaFin, Collection<h_cargaProfesor_cyc> cargaProfesores, boolean actual) {
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

    public Collection<h_cargaProfesor_cyc> getCargaProfesores() {
        return cargaProfesores;
    }

    public void setCargaProfesores(Collection<h_cargaProfesor_cyc> cargaProfesores) {
        this.cargaProfesores = cargaProfesores;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public h_periodo_planeacion(String periodo, Collection<h_cargaProfesor_cyc> cargaProfesores) {
        this.periodo = periodo;
        this.cargaProfesores = cargaProfesores;
    }

    public h_periodo_planeacion() {
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
        if (!(object instanceof h_periodo_planeacion)) {
            return false;
        }
        h_periodo_planeacion other = (h_periodo_planeacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.cyc[id=" + id + "]";
    }

}
