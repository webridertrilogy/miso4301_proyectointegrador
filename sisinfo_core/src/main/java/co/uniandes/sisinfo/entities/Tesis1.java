package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;


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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Table(name = "Tesis1")
@NamedQueries({
    @NamedQuery(name = "Tesis1.findBycorreoEstudiante", query = "SELECT i FROM Tesis1 i WHERE i.estudiante.persona.correo =:correoEstudiante"),
    @NamedQuery(name = "Tesis1.findBycorreoAsesor", query = "SELECT i FROM Tesis1 i WHERE i.asesor.persona.correo =:correoasesor"),
    @NamedQuery(name = "Tesis1.findByPeriodoTesis", query = "SELECT i FROM Tesis1 i WHERE i.semestreIniciacion.periodo =:periodo"),
    @NamedQuery(name = "Tesis1.findByEstadoTesis", query = "SELECT i FROM Tesis1 i WHERE i.estadoTesis =:estadoTesis"),
    @NamedQuery(name = "Tesis1.findByEstadoYPeriodoTesis", query = "SELECT i FROM Tesis1 i WHERE i.estadoTesis =:estado AND i.semestreIniciacion.id like :idPeriodo"),
    @NamedQuery(name = "Tesis1.findByTemaTesis", query = "SELECT i FROM Tesis1 i WHERE i.temaTesis =:tema"),
    @NamedQuery(name = "Tesis1.findByComentariosTesis", query = "SELECT i FROM Tesis1 i WHERE i.comentariosAsesor.size < 1 AND i.semestreIniciacion.periodo LIKE :periodo order by asesor.persona.apellidos"),
    @NamedQuery(name = "Tesis1.findByPeriodoEstadoAsesor", query = "SELECT i FROM Tesis1 i WHERE i.estadoTesis =:estadoTesis AND i.semestreIniciacion.periodo =:periodo "
    + "AND i.asesor.persona.correo =:correoasesor "),
    @NamedQuery(name = "Tesis1.findByPeriodoEstado", query = "SELECT i FROM Tesis1 i WHERE i.estadoTesis like :estadoTesis AND i.semestreIniciacion.periodo like :periodo "),
    @NamedQuery(name = "Tesis1.findAsesoresByPeriodoEstado", query = "SELECT Distinct i.asesor.persona FROM Tesis1 i WHERE i.estadoTesis =:estadoTesis AND i.semestreIniciacion.periodo =:periodo  ")
})
public class Tesis1 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fechaCreacion")
    private Timestamp fechaCreacion;
    @Column(name = "temaTesis")
    private String temaTesis;
    @Column(name = "fechaTerminacion")
    private Timestamp fechaTerminacion;
    @Column(name = "aprobadoAsesor")
    private String aprobadoAsesor;
    @Column(name = "calificacionTesis")
    private String calificacionTesis;
    @Column(name = "rutaArticuloTesis1")
    private String rutaArticuloTesis1;
    @Column(name = "rutaCartaPendienteTesis1")
    private String rutaCartaPendienteTesis1;
    @Column(name = "estadoTesis")
    private String estadoTesis;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Estudiante estudiante;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Profesor asesor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private SubareaInvestigacion subareaInvestigacion;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private PeriodoTesis semestreIniciacion;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<ComentarioTesis> comentariosAsesor;
    @Column(name = "comentsIngresoExtemporal", length = 5000)
    private String comentsIngresoExtemporal;
    @Column(name = "aprobadoParaParadigma")
    private Boolean aprobadoParaParadigma;

    public Tesis1() {
    }

    public String isAprobadoAsesor() {
        return aprobadoAsesor;
    }

    public void setAprobadoAsesor(String aprobadoAsesor) {
        this.aprobadoAsesor = aprobadoAsesor;
    }

    public Profesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Profesor asesor) {
        this.asesor = asesor;
    }

    public String getCalificacionTesis() {
        return calificacionTesis;
    }

    public void setCalificacionTesis(String calificacionTesis) {
        this.calificacionTesis = calificacionTesis;
    }

    public String getEstadoTesis() {
        return estadoTesis;
    }

    public void setEstadoTesis(String estadoTesis) {
        this.estadoTesis = estadoTesis;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaTerminacion() {
        return fechaTerminacion;
    }

    public void setFechaTerminacion(Timestamp fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRutaArticuloTesis1() {
        return rutaArticuloTesis1;
    }

    public void setRutaArticuloTesis1(String rutaArticuloTesis1) {
        this.rutaArticuloTesis1 = rutaArticuloTesis1;
    }

    public PeriodoTesis getSemestreIniciacion() {
        return semestreIniciacion;
    }

    public void setSemestreIniciacion(PeriodoTesis semestreIniciacion) {
        this.semestreIniciacion = semestreIniciacion;
    }

    public SubareaInvestigacion getSubareaInvestigacion() {
        return subareaInvestigacion;
    }

    public void setSubareaInvestigacion(SubareaInvestigacion subGrupoInvestigacion) {
        this.subareaInvestigacion = subGrupoInvestigacion;
    }

    public String getTemaTesis() {
        return temaTesis;
    }

    public void setTemaTesis(String temaTesis) {
        this.temaTesis = temaTesis;
    }

    public Collection<ComentarioTesis> getComentariosAsesor() {
        return comentariosAsesor;
    }

    public void setComentariosAsesor(Collection<ComentarioTesis> comentariosAsesor) {
        this.comentariosAsesor = comentariosAsesor;
    }

    public String getRutaCartaPendienteTesis1() {
        return rutaCartaPendienteTesis1;
    }

    public void setRutaCartaPendienteTesis1(String rutaCartaPendienteTesis1) {
        this.rutaCartaPendienteTesis1 = rutaCartaPendienteTesis1;
    }

    public String getComentsIngresoExtemporal() {
        return comentsIngresoExtemporal;
    }

    public void setComentsIngresoExtemporal(String comentsIngresoExtemporal) {
        this.comentsIngresoExtemporal = comentsIngresoExtemporal;
    }

    public Boolean getAprobadoParaParadigma() {
        return aprobadoParaParadigma;
    }

    public void setAprobadoParaParadigma(Boolean aprobadoParaParadigma) {
        this.aprobadoParaParadigma = aprobadoParaParadigma;
    }

    
}
