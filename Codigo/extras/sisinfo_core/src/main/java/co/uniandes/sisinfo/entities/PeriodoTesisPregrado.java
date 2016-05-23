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
 * Entidad Periodo de Proyecto de Grado
 * @author Ivan Mauricio Melo S, Marcela Morales
 */
@Entity
@Table(name = "periodo_tesis_pregrado")
@NamedQueries({
    @NamedQuery(name = "PeriodoTesisPregrado.findById", query = "SELECT p FROM PeriodoTesisPregrado p WHERE p.id = :id"),
    @NamedQuery(name = "PeriodoTesisPregrado.findByPeriodo", query = "SELECT p FROM PeriodoTesisPregrado p WHERE p.nombre = :periodo")
})
public class PeriodoTesisPregrado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="nombre")
    private String nombre;
    @Column(name = "publicacionTemasTesis")
    private Timestamp publicacionTemasTesis;
    @Column(name = "inscripcionTesisEstudiante")
    private Timestamp inscripcionTesisEstudiante;
    @Column(name = "acesorAceptePy")
    private Timestamp acesorAceptePy;
    @Column(name = "envioPropuestaPyEstud")
    private Timestamp envioPropuestaPyEstud;
    @Column(name = "validacionAsesorPropuestaDeProyecto")
    private Timestamp validacionAsesorPropuestaDeProyecto;
    @Column(name = "apreciacionCualitativa")
    private Timestamp apreciacionCualitativa;
    @Column(name = "entregaPoster")
    private Timestamp entregaPoster;
    @Column(name = "darVistoBuenoPoster")
    private Timestamp darVistoBuenoPoster;
    @Column(name = "rubricaABET")
    private Timestamp rubricaABET;
    @Column(name = "reporteNotas")
    private Timestamp reporteNotas;
    @Column(name = "informeRetiro")
    private Timestamp informeRetiro;
    //Pendiente
    @Column(name = "pedirPendiente")
    private Timestamp pedirPendiente;
    @Column(name = "levantarPendiente")
    private Timestamp levantarPendiente;
    @Column(name = "entregaPosterPendiente")
    private Timestamp entregaPosterPendiente;
    @Column(name = "darVistoBuenoPosterPendiente")
    private Timestamp darVistoBuenoPosterPendiente;
    //PendienteEspecial
    @Column(name = "pedirPendienteEspecial")
    private Timestamp pedirPendienteEspecial;
    @Column(name = "levantarPendienteEspecial")
    private Timestamp levantarPendienteEspecial;
    @Column(name = "entregaPosterPendienteEspecial")
    private Timestamp entregaPosterPendienteEspecial;
    @Column(name = "darVistoBuenoPosterPendienteEspecial")
    private Timestamp darVistoBuenoPosterPendienteEspecial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getAcesorAceptePy() {
        return acesorAceptePy;
    }

    public void setAcesorAceptePy(Timestamp acesorAceptePy) {
        this.acesorAceptePy = acesorAceptePy;
    }

    public Timestamp getApreciacionCualitativa() {
        return apreciacionCualitativa;
    }

    public void setApreciacionCualitativa(Timestamp apreciacionCualitativa) {
        this.apreciacionCualitativa = apreciacionCualitativa;
    }

    public Timestamp getDarVistoBuenoPoster() {
        return darVistoBuenoPoster;
    }

    public void setDarVistoBuenoPoster(Timestamp darVistoBuenoPoster) {
        this.darVistoBuenoPoster = darVistoBuenoPoster;
    }

    public Timestamp getEntregaPoster() {
        return entregaPoster;
    }

    public void setEntregaPoster(Timestamp entregaPoster) {
        this.entregaPoster = entregaPoster;
    }

    public Timestamp getEnvioPropuestaPyEstud() {
        return envioPropuestaPyEstud;
    }

    public void setEnvioPropuestaPyEstud(Timestamp envioPropuestaPyEstud) {
        this.envioPropuestaPyEstud = envioPropuestaPyEstud;
    }

    public Timestamp getInscripcionTesisEstudiante() {
        return inscripcionTesisEstudiante;
    }

    public void setInscripcionTesisEstudiante(Timestamp inscripcionTesisEstudiante) {
        this.inscripcionTesisEstudiante = inscripcionTesisEstudiante;
    }

    public Timestamp getLevantarPendienteEspecial() {
        return levantarPendienteEspecial;
    }

    public void setLevantarPendienteEspecial(Timestamp levantarPendienteEspecial) {
        this.levantarPendienteEspecial = levantarPendienteEspecial;
    }

    public Timestamp getPedirPendienteEspecial() {
        return pedirPendienteEspecial;
    }

    public void setPedirPendienteEspecial(Timestamp pedirPendienteEspecial) {
        this.pedirPendienteEspecial = pedirPendienteEspecial;
    }

    public Timestamp getPublicacionTemasTesis() {
        return publicacionTemasTesis;
    }

    public void setPublicacionTemasTesis(Timestamp publicacionTemasTesis) {
        this.publicacionTemasTesis = publicacionTemasTesis;
    }

    public Timestamp getReporteNotas() {
        return reporteNotas;
    }

    public void setReporteNotas(Timestamp reporteNotas) {
        this.reporteNotas = reporteNotas;
    }

    public Timestamp getRubricaABET() {
        return rubricaABET;
    }

    public void setRubricaABET(Timestamp rubricaABET) {
        this.rubricaABET = rubricaABET;
    }

    public Timestamp getValidacionAsesorPropuestaDeProyecto() {
        return validacionAsesorPropuestaDeProyecto;
    }

    public void setValidacionAsesorPropuestaDeProyecto(Timestamp validacionAsesorPropuestaDeProyecto) {
        this.validacionAsesorPropuestaDeProyecto = validacionAsesorPropuestaDeProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Timestamp getInformeRetiro() {
        return informeRetiro;
    }

    public void setInformeRetiro(Timestamp informeRetiro) {
        this.informeRetiro = informeRetiro;
    }

    public Timestamp getLevantarPendiente() {
        return levantarPendiente;
    }

    public void setLevantarPendiente(Timestamp levantarPendiente) {
        this.levantarPendiente = levantarPendiente;
    }

    public Timestamp getPedirPendiente() {
        return pedirPendiente;
    }

    public void setPedirPendiente(Timestamp pedirPendiente) {
        this.pedirPendiente = pedirPendiente;
    }

    public Timestamp getDarVistoBuenoPosterPendiente() {
        return darVistoBuenoPosterPendiente;
    }

    public void setDarVistoBuenoPosterPendiente(Timestamp darVistoBuenoPosterPendienteEspecial) {
        this.darVistoBuenoPosterPendiente = darVistoBuenoPosterPendienteEspecial;
    }

    public Timestamp getEntregaPosterPendiente() {
        return entregaPosterPendiente;
    }

    public void setEntregaPosterPendiente(Timestamp entregaPosterPendienteEspecial) {
        this.entregaPosterPendiente = entregaPosterPendienteEspecial;
    }

    public Timestamp getDarVistoBuenoPosterPendienteEspecial() {
        return darVistoBuenoPosterPendienteEspecial;
    }

    public void setDarVistoBuenoPosterPendienteEspecial(Timestamp darVistoBuenoPosterPendienteEspecial) {
        this.darVistoBuenoPosterPendienteEspecial = darVistoBuenoPosterPendienteEspecial;
    }

    public Timestamp getEntregaPosterPendienteEspecial() {
        return entregaPosterPendienteEspecial;
    }

    public void setEntregaPosterPendienteEspecial(Timestamp entregaPosterPendienteEspecial) {
        this.entregaPosterPendienteEspecial = entregaPosterPendienteEspecial;
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
        if (!(object instanceof PeriodoTesisPregrado)) {
            return false;
        }
        PeriodoTesisPregrado other = (PeriodoTesisPregrado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.PeriodoTesisPregrado[id=" + id + "]";
    }
}
