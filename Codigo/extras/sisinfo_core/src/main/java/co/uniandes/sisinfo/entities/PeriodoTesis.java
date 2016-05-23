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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ivan Melo
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "PeriodoTesis.findById", query = "SELECT p FROM PeriodoTesis p WHERE p.id = :id"),
    @NamedQuery(name = "PeriodoTesis.findByPeriodo", query = "SELECT p FROM PeriodoTesis p WHERE p.periodo = :periodo"),
    @NamedQuery(name = "PeriodoTesis.findByActual", query = "SELECT p FROM PeriodoTesis p WHERE p.actual = :actual")
})
@Table(name = "periodo_tesis")
public class PeriodoTesis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "periodo", unique = true)
    private String periodo;
    @Column(name = "actual")
    private boolean actual;
    //aca fechas importantes:...
    @Column(name = "maxFechaInscripcionSubarea")
    private Timestamp maxFechaInscripcionSubarea;
    @Column(name = "maxFechaAprobacionInscripcionSubarea")
    private Timestamp maxFechaAprobacionInscripcionSubarea;
    @Column(name = "maxFechaAprobacionInscripcionSubareaCoordinacion")
    private Timestamp maxFechaAprobacionInscripcionSubareaCoordinacion;
    @Column(name = "maxFechaInscripcionT1")
    private Timestamp maxFechaInscripcionT1;
    @Column(name = "maxFechaAprobacionTesis1")
    private Timestamp maxFechaAprobacionTesis1;
    //-------------------
    @Column(name = "maxFechaAprobacionTesis1Coordinacion")
    private Timestamp maxFechaAprobacionTesis1Coordinacion;
    //-------------------
    @Column(name = "maxFechaSubirNotaTesis1")
    private Timestamp maxFechaSubirNotaTesis1;
    
    @Column(name = "maxFechaPedirPendienteTesis1")
    private Timestamp maxFechaPedirPendienteTesis1;

    @Column(name = "maxFechaLevantarPendienteTesis1")
    private Timestamp maxFechaLevantarPendienteTesis1;

    @Column(name = "fechaUltimaSolicitarTesis2")
    private Timestamp fechaUltimaSolicitarTesis2;

    @Column(name = "maxFechaAprobacionTesis2")
    private Timestamp maxFechaAprobacionTesis2;

    @Column(name = "fechaUltimaPendienteEspecialTesis2")
    private Timestamp fechaUltimaPendienteEspecialTesis2;

    @Column(name = "fechaUltimaReportarNotaReprobadaSSTesis2")
    private Timestamp fechaUltimaReportarNotaReprobadaSSTesis2;

    @Column(name = "fechaUltimaSustentarTesis2")
    private Timestamp fechaUltimaSustentarTesis2;

    @Column(name = "fechaMaximaPublicacionTemasTesis")
    private Timestamp fechaMaximaPublicacionTemasTesis;

    @Column(name = "fecha30Porciento")
    private Timestamp fechaDel30Porciento;

    @Column(name = "fechaMaximaRetiro")
    private Timestamp fechaMaximaRetiro;

    @Column(name = "fechaMaxSustentacionT2PendEspecial")
    private Timestamp fechaMaxSustentacionT2PendEspecial;

    @Column(name = "fechaMaximaSolicitarHorario")
    private Timestamp fechaMaximaSolicitarHorario;

    @Column(name = "fechaMaximaCalificarTesis2")
    private Timestamp fechaMaximaCalificarTesis2;

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
        if (!(object instanceof PeriodoTesis)) {
            return false;
        }
        PeriodoTesis other = (PeriodoTesis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.PeriodoTesis[id=" + id + "]";
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

    public Timestamp getMaxFechaInscripcionT1() {
        return maxFechaInscripcionT1;
    }

    public void setMaxFechaInscripcionT1(Timestamp maxFechaInscripcion) {
        this.maxFechaInscripcionT1 = maxFechaInscripcion;
    }

    public Timestamp getMaxFechaLevantarPendienteTesis1() {
        return maxFechaLevantarPendienteTesis1;
    }

    public Timestamp getMaxFechaAprobacionInscripcionSubarea() {
        return maxFechaAprobacionInscripcionSubarea;
    }

    public void setMaxFechaAprobacionInscripcionSubarea(Timestamp maxFechaAprobacionInscripcionSubarea) {
        this.maxFechaAprobacionInscripcionSubarea = maxFechaAprobacionInscripcionSubarea;
    }

    public Timestamp getMaxFechaAprobacionTesis1() {
        return maxFechaAprobacionTesis1;
    }

    public void setMaxFechaAprobacionTesis1(Timestamp maxFechaAprobacionTesis1) {
        this.maxFechaAprobacionTesis1 = maxFechaAprobacionTesis1;
    }

    public Timestamp getMaxFechaAprobacionTesis2() {
        return maxFechaAprobacionTesis2;
    }

    public void setMaxFechaAprobacionTesis2(Timestamp maxFechaAprobacionTesis2) {
        this.maxFechaAprobacionTesis2 = maxFechaAprobacionTesis2;
    }

    public Timestamp getMaxFechaInscripcionSubarea() {
        return maxFechaInscripcionSubarea;
    }

    public void setMaxFechaInscripcionSubarea(Timestamp maxFechaInscripcionSubarea) {
        this.maxFechaInscripcionSubarea = maxFechaInscripcionSubarea;
    }

    public void setMaxFechaLevantarPendienteTesis1(Timestamp maxFechaLevantarPendienteTesis1) {
        this.maxFechaLevantarPendienteTesis1 = maxFechaLevantarPendienteTesis1;
    }

    public Timestamp getMaxFechaPedirPendienteTesis1() {
        return maxFechaPedirPendienteTesis1;
    }

    public void setMaxFechaPedirPendienteTesis1(Timestamp maxFechaPedirPendienteTesis1) {
        this.maxFechaPedirPendienteTesis1 = maxFechaPedirPendienteTesis1;
    }

    public Timestamp getMaxFechaSubirNotaTesis1() {
        return maxFechaSubirNotaTesis1;
    }

    public void setMaxFechaSubirNotaTesis1(Timestamp maxFechaSubirNotaTesis1) {
        this.maxFechaSubirNotaTesis1 = maxFechaSubirNotaTesis1;
    }

    public Timestamp getFechaUltimaPendienteEspecialTesis2() {
        return fechaUltimaPendienteEspecialTesis2;
    }

    public void setFechaUltimaPendienteEspecialTesis2(Timestamp fechaUltimaPendienteEspecialTesis2) {
        this.fechaUltimaPendienteEspecialTesis2 = fechaUltimaPendienteEspecialTesis2;
    }

    public Timestamp getFechaUltimaReportarNotaReprobadaSSTesis2() {
        return fechaUltimaReportarNotaReprobadaSSTesis2;
    }

    public void setFechaUltimaReportarNotaReprobadaSSTesis2(Timestamp fechaUltimaReportarNotaReprobadaSSTesis2) {
        this.fechaUltimaReportarNotaReprobadaSSTesis2 = fechaUltimaReportarNotaReprobadaSSTesis2;
    }

    public Timestamp getFechaUltimaSolicitarTesis2() {
        return fechaUltimaSolicitarTesis2;
    }

    public void setFechaUltimaSolicitarTesis2(Timestamp fechaUltimaSolicitarTesis2) {
        this.fechaUltimaSolicitarTesis2 = fechaUltimaSolicitarTesis2;
    }

    public Timestamp getFechaUltimaSustentarTesis2() {
        return fechaUltimaSustentarTesis2;
    }

    public void setFechaUltimaSustentarTesis2(Timestamp fechaUltimaSustentarTesis2) {
        this.fechaUltimaSustentarTesis2 = fechaUltimaSustentarTesis2;
    }

    public Timestamp getFechaMaximaPublicacionTemasTesis() {
        return fechaMaximaPublicacionTemasTesis;
    }

    public void setFechaMaximaPublicacionTemasTesis(Timestamp fechaMaximaPublicacionTemasTesis) {
        this.fechaMaximaPublicacionTemasTesis = fechaMaximaPublicacionTemasTesis;
    }

    public Timestamp getFechaDel30Porciento() {
        return fechaDel30Porciento;
    }

    public void setFechaDel30Porciento(Timestamp fechaDel30Porciento) {
        this.fechaDel30Porciento = fechaDel30Porciento;
    }

    public Timestamp getMaxFechaAprobacionInscripcionSubareaCoordinacion() {
        return maxFechaAprobacionInscripcionSubareaCoordinacion;
    }

    public void setMaxFechaAprobacionInscripcionSubareaCoordinacion(Timestamp maxFechaAprobacionInscripcionSubareaCoordinacion) {
        this.maxFechaAprobacionInscripcionSubareaCoordinacion = maxFechaAprobacionInscripcionSubareaCoordinacion;
    }

    public Timestamp getMaxFechaAprobacionTesis1Coordinacion() {
        return maxFechaAprobacionTesis1Coordinacion;
    }

    public void setMaxFechaAprobacionTesis1Coordinacion(Timestamp maxFechaAprobacionTesis1Coordinacion) {
        this.maxFechaAprobacionTesis1Coordinacion = maxFechaAprobacionTesis1Coordinacion;
    }

    public Timestamp getFechaMaximaRetiro() {
        return fechaMaximaRetiro;
    }

    public void setFechaMaximaRetiro(Timestamp fechaMaximaRetiro) {
        this.fechaMaximaRetiro = fechaMaximaRetiro;
    }

    public Timestamp getFechaMaxSustentacionT2PendEspecial() {
        return fechaMaxSustentacionT2PendEspecial;
    }

    public void setFechaMaxSustentacionT2PendEspecial(Timestamp fechaMaxSustentacionT2PendEspecial) {
        this.fechaMaxSustentacionT2PendEspecial = fechaMaxSustentacionT2PendEspecial;
    }

    public Timestamp getFechaMaximaCalificarTesis2() {
        return fechaMaximaCalificarTesis2;
    }

    public void setFechaMaximaCalificarTesis2(Timestamp fechaMaximaCalificarTesis2) {
        this.fechaMaximaCalificarTesis2 = fechaMaximaCalificarTesis2;
    }

    public Timestamp getFechaMaximaSolicitarHorario() {
        return fechaMaximaSolicitarHorario;
    }

    public void setFechaMaximaSolicitarHorario(Timestamp fechaMaximaSolicitarHorario) {
        this.fechaMaximaSolicitarHorario = fechaMaximaSolicitarHorario;
    }
    
}
