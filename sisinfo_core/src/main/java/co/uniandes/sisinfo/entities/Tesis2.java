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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@Table(name = "tesis2")
@NamedQueries({
    @NamedQuery(name = "Tesis2.findBycorreoEstudiante", query = "SELECT i FROM Tesis2 i WHERE i.estudiante.persona.correo =:correoEstudiante"),
    @NamedQuery(name = "Tesis2.findByPeriodoTesis", query = "SELECT i FROM Tesis2 i WHERE  i.semestreInicio.periodo=:periodo"),
    @NamedQuery(name = "Tesis2.findBycorreoAsesor", query = "SELECT i FROM Tesis2 i WHERE i.asesor.persona.correo =:correo"),
    @NamedQuery(name = "Tesis2.findByEstadoTesis", query = "SELECT i FROM Tesis2 i WHERE i.estadoTesis =:estado"),
    @NamedQuery(name = "Tesis2.findByEstadoYPeriodoTesis", query = "SELECT i FROM Tesis2 i WHERE i.estadoTesis =:estado AND i.semestreInicio.id like :periodo"),
    @NamedQuery(name = "Tesis2.findByGrupoInvestigacion", query = "SELECT i FROM Tesis2 i WHERE i.subGrupoInvestigacion.nombreSubarea =:nombreGrupo"),
    @NamedQuery(name = "Tesis2.findByHorarioSustentacion", query = "SELECT i FROM Tesis2 i WHERE i.semestreInicio.periodo =:periodo OR i.semestreInicio.periodo =:periodoAnterior AND horarioSustentacion != null AND horarioSustentacion.fechaSustentacion "
    + "!=null ORDER BY  horarioSustentacion.fechaSustentacion ASC"),
    @NamedQuery(name = "Tesis2.findAllOrderBySemestre", query = "SELECT i FROM Tesis2 i ORDER BY  semestreInicio.periodo DESC"),
    @NamedQuery(name = "Tesis2.findByComentariosTesis", query = "SELECT i FROM Tesis2 i WHERE i.comentariosAsesor.size < 1 AND  i.semestreInicio.periodo =:periodo  order by asesor.persona.apellidos"),
    @NamedQuery(name = "Tesis2.findAsesoresByPeriodoEstado", query = "SELECT Distinct i.asesor.persona FROM Tesis2 i WHERE i.estadoTesis =:estadoTesis AND i.semestreInicio.periodo =:periodo  "),
    @NamedQuery(name = "Tesis2.findDetallesSustentacionByPeriodoEstado", query = "SELECT Distinct i FROM Tesis2 i WHERE  i.semestreInicio.periodo =:periodo AND i.horarioSustentacion.fechaSustentacion is not null "),
    @NamedQuery(name = "Tesis2.findByDiferentePeriodoTesis", query = "SELECT i FROM Tesis2 i WHERE  i.semestreInicio.periodo!=:periodo and i.estadoTesis=:estadoTesis  and (estaEnPendienteEspecial is null or estaEnPendienteEspecial=false)")
})
public class Tesis2 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fechaCreacion")
    private Timestamp fecha;
    @Column(name = "temaProyecto")
    private String temaProyecto;
    @Column(name = "fechaPrevistaTerminacion")
    private Timestamp fechaPrevistaTerminacion;
    @Column(name = "calificacion")
    private Double calificacion;
    @Column(name = "rutaArchivoAdjuntoInicioTesis")
    private String rutaArchivoAdjuntoInicioTesis;
    @Column(name = "rutaArticuloTesis")
    private String rutaArticuloTesis;
    @Column(name = "estadoTesis")
    private String estadoTesis;
    @Column(name = "estadoAnterior")
    private String estadoAnterior;
    @Column(name = "aprobadoAsesor")
    private String aprobadoAsesor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Profesor asesor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Estudiante estudiante;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private SubareaInvestigacion subGrupoInvestigacion;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private PeriodoTesis semestreInicio;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Collection<Profesor> juradoTesis;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Collection<JuradoExternoUniversidad> jurados;
    //seleccion de horario:
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HorarioSustentacionTesis horarioSustentacion;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<ComentarioTesis> comentariosAsesor;
    @Column(name = "estadoHorario")
    private String estadoHorario;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<CalificacionJurado> calificacionesJurados;
    @Column(name = "notaSustentacion")
    private Double notaSustentacion;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Collection<Coasesor> coasesor;
    @Column(name = "rutaArchivoPendienteEspecial")
    private String rutaArchivoSolicitudPendienteEspecial;
    @Column(name = "rutaArchivoIngresoExtemporal")
    private String rutaArchivoIngresoExtemporal;
    @Column(name = "comentsIngresoExtemporal", length = 5000)
    private String comentsIngresoExtemporal;
    @Column(name = "estaEnPendienteEspecial")
    private Boolean estaEnPendienteEspecial;

    @Column(name = "aprobadoParaParadigma")
    private Boolean aprobadoParaParadigma;

    public Tesis2() {
    }

    public Profesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Profesor asesor) {
        this.asesor = asesor;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
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

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Timestamp getFechaPrevistaTerminacion() {
        return fechaPrevistaTerminacion;
    }

    public void setFechaPrevistaTerminacion(Timestamp fechaPrevistaTerminacion) {
        this.fechaPrevistaTerminacion = fechaPrevistaTerminacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Profesor> getJuradoTesis() {
        return juradoTesis;
    }

    public void setJuradoTesis(Collection<Profesor> juradoTesis) {
        this.juradoTesis = juradoTesis;
    }

    public Collection<JuradoExternoUniversidad> getJurados() {
        return jurados;
    }

    public void setJurados(Collection<JuradoExternoUniversidad> jurados) {
        this.jurados = jurados;
    }

    public String getRutaArchivoAdjuntoInicioTesis() {
        return rutaArchivoAdjuntoInicioTesis;
    }

    public void setRutaArchivoAdjuntoInicioTesis(String rutaArchivoAdjuntoInicioTesis) {
        this.rutaArchivoAdjuntoInicioTesis = rutaArchivoAdjuntoInicioTesis;
    }

    public String getRutaArticuloTesis() {
        return rutaArticuloTesis;
    }

    public void setRutaArticuloTesis(String rutaArticuloTesis) {
        this.rutaArticuloTesis = rutaArticuloTesis;
    }

    public PeriodoTesis getSemestreInicio() {
        return semestreInicio;
    }

    public void setSemestreInicio(PeriodoTesis semestreInicio) {
        this.semestreInicio = semestreInicio;
    }

    public SubareaInvestigacion getSubareaInvestigacion() {
        return subGrupoInvestigacion;
    }

    public void setSubareaInvestigacion(SubareaInvestigacion subGrupoInvestigacion) {
        this.subGrupoInvestigacion = subGrupoInvestigacion;
    }

    public String getTemaProyecto() {
        return temaProyecto;
    }

    public void setTemaProyecto(String temaProyecto) {
        this.temaProyecto = temaProyecto;
    }

    public String getAprobadoAsesor() {
        return aprobadoAsesor;
    }

    public void setAprobadoAsesor(String aprobadoAsesor) {
        this.aprobadoAsesor = aprobadoAsesor;
    }

    public HorarioSustentacionTesis getHorarioSustentacion() {
        return horarioSustentacion;
    }

    public void setHorarioSustentacion(HorarioSustentacionTesis horarioSustentacion) {
        this.horarioSustentacion = horarioSustentacion;
    }

    public Collection<ComentarioTesis> getComentariosAsesor() {
        return comentariosAsesor;
    }

    public void setComentariosAsesor(Collection<ComentarioTesis> comentariosAsesor) {
        this.comentariosAsesor = comentariosAsesor;
    }

    public SubareaInvestigacion getSubGrupoInvestigacion() {
        return subGrupoInvestigacion;
    }

    public void setSubGrupoInvestigacion(SubareaInvestigacion subGrupoInvestigacion) {
        this.subGrupoInvestigacion = subGrupoInvestigacion;
    }

    public String getEstadoHorario() {
        return estadoHorario;
    }

    public void setEstadoHorario(String horarioAprobadoPorAsesor) {
        this.estadoHorario = horarioAprobadoPorAsesor;
    }

    public Collection<CalificacionJurado> getCalificacionesJurados() {
        return calificacionesJurados;
    }

    public void setCalificacionesJurados(Collection<CalificacionJurado> calificacionesJurados) {
        this.calificacionesJurados = calificacionesJurados;
    }

    public Double getNotaSustentacion() {
        return notaSustentacion;
    }

    public void setNotaSustentacion(Double notaSustentacion) {
        this.notaSustentacion = notaSustentacion;
    }

    public Collection<Coasesor> getCoasesor() {
        return coasesor;
    }

    public void setCoasesor(Collection<Coasesor> coasesor) {
        this.coasesor = coasesor;
    }

    public String getRutaArchivoSolicitudPendienteEspecial() {
        return rutaArchivoSolicitudPendienteEspecial;
    }

    public void setRutaArchivoSolicitudPendienteEspecial(String rutaArchivoSolicitudPendienteEspecial) {
        this.rutaArchivoSolicitudPendienteEspecial = rutaArchivoSolicitudPendienteEspecial;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getComentsIngresoExtemporal() {
        return comentsIngresoExtemporal;
    }

    public void setComentsIngresoExtemporal(String comentsIngresoExtemporal) {
        this.comentsIngresoExtemporal = comentsIngresoExtemporal;
    }

    public String getRutaArchivoIngresoExtemporal() {
        return rutaArchivoIngresoExtemporal;
    }

    public void setRutaArchivoIngresoExtemporal(String rutaArchivoIngresoExtemporal) {
        this.rutaArchivoIngresoExtemporal = rutaArchivoIngresoExtemporal;
    }

    public Boolean getEstaEnPendienteEspecial() {
        return estaEnPendienteEspecial;
    }

    public void setEstaEnPendienteEspecial(Boolean estaEnPendienteEspecial) {
        this.estaEnPendienteEspecial = estaEnPendienteEspecial;
    }

    public Boolean getAprobadoParaParadigma() {
        return aprobadoParaParadigma;
    }

    public void setAprobadoParaParadigma(Boolean aprobadoParaParadigma) {
        this.aprobadoParaParadigma = aprobadoParaParadigma;
    }

    
}
