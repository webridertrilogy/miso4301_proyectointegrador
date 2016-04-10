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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Table(name = "InscripcionSubareaInvestigacion")
@NamedQueries({
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findBycorreoEstudiante", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.estudiante.persona.correo =:correoEstudiante"),
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findBycorreoEstudianteYEstado", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.estudiante.persona.correo =:correoEstudiante AND  i.estadoSolicitud =:estado"),
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findBycorreoAsesorYEstado", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.asesor.persona.correo =:correo AND  i.estadoSolicitud =:estado"),
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findBycorreoCoordinadorSubareaYEstado", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.subGrupoInvestigacion.coordinadorSubarea.correo =:correo AND  i.estadoSolicitud =:estado"),
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findBycorreoAsesor", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.asesor.persona.correo =:correoasesor"),
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findByCorreoDirectorSubArea", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.subGrupoInvestigacion.coordinadorSubarea.correo =:correoDirector"),
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findByGrupoInvestigacion", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.subGrupoInvestigacion.nombreSubarea =:nombreGrupo"),
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findByEstadoInscripcion", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.estadoSolicitud =:estado"),
    @NamedQuery(name = "InscripcionSubareaInvestigacion.findByPeriodoTesis1", query = "SELECT i FROM InscripcionSubareaInvestigacion i WHERE i.semestreInicioTesis1.periodo =:periodo"),
     @NamedQuery(name = "InscripcionSubareaInvestigacion.findByCoordinadoresSubAreaConEstado", query = "SELECT DISTINCT i.subGrupoInvestigacion.coordinadorSubarea FROM InscripcionSubareaInvestigacion i "
     + "WHERE i.estadoSolicitud =:estado"),
      @NamedQuery(name = "InscripcionSubareaInvestigacion.findByAsesoresSubAreaConEstado", query = "SELECT DISTINCT i.asesor.persona FROM InscripcionSubareaInvestigacion i "
     + "WHERE i.estadoSolicitud =:estado")
})
public class InscripcionSubareaInvestigacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fechaCreacion")
    private Timestamp fechaCreacion;
    @Column(name = "temaTesis")
    private String temaTesis;
//    @Column(name = "fechaTerminacion")
//    private Timestamp fechaTerminacion;
    @Column(name = "aprobadoSubArea")
    private String aprobadoSubArea;
    @Column(name = "aprobadoAsesor")
    private String aprobadoAsesor;
    @Column(name = "estadoSolicitud")
    private String estadoSolicitud;
    /* @Column(name = "calificacionTesis")
    private Integer calificacionTesis;
    @Column(name = "rutaArticuloTesis1")
    private String rutaArticuloTesis1;*/
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
    private Estudiante estudiante;
    @ManyToOne(fetch = FetchType.LAZY,  cascade = {CascadeType.REFRESH })
    private Profesor asesor;
    
    @OneToOne(fetch = FetchType.LAZY,  cascade = {CascadeType.REFRESH })
    private SubareaInvestigacion subGrupoInvestigacion;
    @OneToMany(fetch = FetchType.LAZY,  cascade = {CascadeType.MERGE , CascadeType.REFRESH ,CascadeType.REMOVE})
    @JoinTable(name="inscripcionSubareaInvestigacion_obligatorias")
    private Collection<CursoTesis> obligatorias;
    @OneToMany(fetch = FetchType.LAZY,  cascade = {CascadeType.MERGE , CascadeType.REFRESH,CascadeType.REMOVE })
    @JoinTable(name="inscripcionSubareaInvestigacion_subarea")
    private Collection<CursoTesis> subArea;
    @OneToOne(fetch = FetchType.LAZY, optional=true, cascade = {CascadeType.MERGE ,CascadeType.REFRESH,CascadeType.REMOVE })
    private CursoTesis otraSubArea;
    @OneToOne(fetch = FetchType.LAZY, optional=true,  cascade = {CascadeType.MERGE , CascadeType.REFRESH,CascadeType.REMOVE })
    private CursoTesis otraMaestria;
    @OneToMany(fetch = FetchType.LAZY,  cascade = {CascadeType.MERGE , CascadeType.REFRESH,CascadeType.REMOVE })
    @JoinTable(name="inscripcionSubareaInvestigacion_nivelatorios")
    private Collection<CursoTesis> nivelatorios;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private PeriodoTesis semestreInicioTesis1;

     @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private PeriodoTesis semestreInicioTesis2;

    public InscripcionSubareaInvestigacion() {
    }

    public String isAprobadoAsesor() {
        return aprobadoAsesor;
    }

    public void setAprobadoAsesor(String aprobadoAsesor) {
        this.aprobadoAsesor = aprobadoAsesor;
    }

    public String isAprobadoSubArea() {
        return aprobadoSubArea;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public void setAprobadoSubArea(String aprobadoSubArea) {
        this.aprobadoSubArea = aprobadoSubArea;
    }

    public Profesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Profesor asesor) {
        this.asesor = asesor;
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

//    public Timestamp getFechaTerminacion() {
//        return fechaTerminacion;
//    }
//
//    public void setFechaTerminacion(Timestamp fechaTerminacion) {
//        this.fechaTerminacion = fechaTerminacion;
//    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<CursoTesis> getNivelatorios() {
        return nivelatorios;
    }

    public void setNivelatorios(Collection<CursoTesis> nivelatorios) {
        this.nivelatorios = nivelatorios;
    }

    public Collection<CursoTesis> getObligatorias() {
        return obligatorias;
    }

    public void setObligatorias(Collection<CursoTesis> obligatorias) {
        this.obligatorias = obligatorias;
    }

    public CursoTesis getOtraMaestria() {
        return otraMaestria;
    }

    public void setOtraMaestria(CursoTesis otraMaestria) {
        this.otraMaestria = otraMaestria;
    }

    public CursoTesis getOtraSubArea() {
        return otraSubArea;
    }

    public void setOtraSubArea(CursoTesis otraSubArea) {
        this.otraSubArea = otraSubArea;
    }

    public Collection<CursoTesis> getSubArea() {
        return subArea;
    }

    public void setSubArea(Collection<CursoTesis> subArea) {
        this.subArea = subArea;
    }

    public SubareaInvestigacion getSubareaInvestigacion() {
        return subGrupoInvestigacion;
    }

    public void setSubareaInvestigacion(SubareaInvestigacion subGrupoInvestigacion) {
        this.subGrupoInvestigacion = subGrupoInvestigacion;
    }

    public String getTemaTesis() {
        return temaTesis;
    }

    public void setTemaTesis(String temaTesis) {
        this.temaTesis = temaTesis;
    }

    public PeriodoTesis getSemestreInicioTesis1() {
        return semestreInicioTesis1;
    }

    public void setSemestreInicioTesis1(PeriodoTesis semestreInicioTesis1) {
        this.semestreInicioTesis1 = semestreInicioTesis1;
    }

    public PeriodoTesis getSemestreInicioTesis2() {
        return semestreInicioTesis2;
    }

    public void setSemestreInicioTesis2(PeriodoTesis semestreInicioTesis2) {
        this.semestreInicioTesis2 = semestreInicioTesis2;
    }

    
}
