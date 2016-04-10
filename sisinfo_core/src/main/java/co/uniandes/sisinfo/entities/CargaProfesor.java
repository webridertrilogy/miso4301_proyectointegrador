package co.uniandes.sisinfo.entities;


import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Ivan Melo
 * @version 1.0
 * @created 11-may-2010 12:13:45 p.m.
 */
@Entity
@Table(name = "CargaProfesor")
@NamedQueries({
    @NamedQuery(name = "CargaProfesor.findAll", query = "SELECT e FROM CargaProfesor e"),
    @NamedQuery(name = "CargaProfesor.findById", query = "SELECT e FROM CargaProfesor e WHERE e.id = :id"),
    @NamedQuery(name = "CargaProfesor.findByCorreo", query = "SELECT e FROM CargaProfesor e WHERE e.profesor.persona.correo = :correo"),
    @NamedQuery(name = "CargaProfesor.findByIdProfesorAndPeriodo", query = "SELECT e FROM CargaProfesor e WHERE e.profesor.id = :idProfesor AND periodoPlaneacion.periodo = :periodo "),
    @NamedQuery(name = "CargaProfesor.findByCorreoYPeriodo", query = "SELECT e FROM CargaProfesor e WHERE e.profesor.persona.correo = :correo AND periodoPlaneacion.periodo = :periodo"),
    @NamedQuery(name = "CargaProfesor.findByPeriodo", query = "SELECT e FROM CargaProfesor e WHERE periodoPlaneacion.periodo = :periodo")
})
public class CargaProfesor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "cargaProfesor")
    private Double cargaProfesor;
    @Column(name = "numero_cursos")
    private Integer numCursosPregradoMaestria;
    @Column(name = "num_publicaciones_plan")
    private Integer numeroPublicacionesPlaneadas;
    //conteo por tipo de publicacion
    @Column(name = "num_revistas")
    private Integer numRevistas;
    @Column(name = "num_congresos_interna")
    private Integer numCongresosInternacio;
    @Column(name = "num_congresos_nacion")
    private Integer numCongresosNacionales;
    @Column(name = "num_capitulos_libro")
    private Integer numCapitulosLibro;
    //---------------------------------
    @Column(name = "num_proyectos_financiados")
    private Integer numeroProyectosFinanciados;
    @Column(name = "num_estudiantes_pregrado")
    private Integer numeroEstudiantesPregrado;
    @Column(name = "num_estudiantes_Tesis1")
    private Integer numeroEstudiantesTesis1;
    @Column(name = "num_estudiantes_Tesis2")
    private Integer numeroEstudiantesTesis2;
    @Column(name = "num_estudiantes_Tesis2Pendiente")
    private Integer numeroEstudiantesTesis2Pendiente;
    @Column(name = "num_estudiantes_doctorado")
    private Integer numeroEstudiantesDoctorado;
    @Column(name = "carga_efectiva")
    private Double cargaEfectiva;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<OtrasActividades> otros;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private DescargaProfesor descarga;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<CursoPlaneado> cursos;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<Evento> eventos;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<ProyectoFinanciado> proyectosFinanciados;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<DireccionTesis> tesisAcargo;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<IntencionPublicacion> intencionPublicaciones;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Profesor profesor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private PeriodoPlaneacion periodoPlaneacion;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private NivelFormacion maximoNivelTesis;

    public CargaProfesor() {
    }

    public CargaProfesor(Long id, Double cargaProfesor, Integer numCursosPregradoMaestria, Integer numeroprivateacionesPlaneadas, Double cargaEfectiva, Collection<OtrasActividades> otros, DescargaProfesor descarga, Collection<CursoPlaneado> cursos, Collection<Evento> eventos, Collection<ProyectoFinanciado> proyectosFinanciados, Collection<DireccionTesis> tesisAcargo, Profesor profesor, PeriodoPlaneacion periodoPlaneacion, NivelFormacion maximoNivelTesis, Collection<IntencionPublicacion> intencionPublicaciones) {
        this.id = id;
        this.cargaProfesor = cargaProfesor;
        this.numCursosPregradoMaestria = numCursosPregradoMaestria;
        this.numeroPublicacionesPlaneadas = numeroprivateacionesPlaneadas;
        this.cargaEfectiva = cargaEfectiva;
        this.otros = otros;
        this.descarga = descarga;
        this.cursos = cursos;
        this.eventos = eventos;
        this.proyectosFinanciados = proyectosFinanciados;
        this.tesisAcargo = tesisAcargo;
        this.profesor = profesor;
        this.periodoPlaneacion = periodoPlaneacion;
        this.maximoNivelTesis = maximoNivelTesis;
        this.intencionPublicaciones = intencionPublicaciones;
        this.numeroEstudiantesTesis1 = 0;
        this.numeroEstudiantesPregrado = 0;
        this.numeroProyectosFinanciados = 0;
        numeroEstudiantesTesis2 = 0;
        numeroEstudiantesTesis2Pendiente = 0;
        numRevistas = 0;
        numCongresosInternacio = 0;
        numCongresosNacionales = 0;
        numCapitulosLibro = 0;
    }

    public CargaProfesor(Double cargaProfesor, Integer numCursosPregradoMaestria, Integer numeroprivateacionesPlaneadas, Double cargaEfectiva, Collection<OtrasActividades> otros, DescargaProfesor descarga, Collection<CursoPlaneado> cursos, Collection<Evento> eventos, Collection<ProyectoFinanciado> proyectosFinanciados, Collection<DireccionTesis> tesisAcargo, Profesor profesor, PeriodoPlaneacion periodoPlaneacion, NivelFormacion maximoNivelTesis, Collection<IntencionPublicacion> intencionPublicaciones) {
        this.cargaProfesor = cargaProfesor;
        this.numCursosPregradoMaestria = numCursosPregradoMaestria;
        this.numeroPublicacionesPlaneadas = numeroprivateacionesPlaneadas;
        this.cargaEfectiva = cargaEfectiva;
        this.otros = otros;
        this.descarga = descarga;
        this.cursos = cursos;
        this.eventos = eventos;
        this.proyectosFinanciados = proyectosFinanciados;
        this.tesisAcargo = tesisAcargo;
        this.profesor = profesor;
        this.periodoPlaneacion = periodoPlaneacion;
        this.maximoNivelTesis = maximoNivelTesis;
        this.intencionPublicaciones = intencionPublicaciones;
        this.numeroEstudiantesTesis1 = 0;
        this.numeroEstudiantesPregrado = 0;
        this.numeroProyectosFinanciados = 0;
        numeroEstudiantesTesis2 = 0;
        numeroEstudiantesTesis2Pendiente = 0;
        numeroEstudiantesDoctorado = 0;
        numRevistas = 0;
        numCongresosInternacio = 0;
        numCongresosNacionales = 0;
        numCapitulosLibro = 0;
    }

    public Double getCargaEfectiva() {
        return cargaEfectiva;
    }

    public void setCargaEfectiva(Double cargaEfectiva) {
        this.cargaEfectiva = cargaEfectiva;
    }

    public Double getCargaProfesor() {
        return cargaProfesor;
    }

    public void setCargaProfesor(Double cargaProfesor) {
        this.cargaProfesor = cargaProfesor;
    }

    public Collection<CursoPlaneado> getCursos() {
        return cursos;
    }

    public void setCursos(Collection<CursoPlaneado> cursos) {
        this.cursos = cursos;
    }

    public DescargaProfesor getDescarga() {
        return descarga;
    }

    public void setDescarga(DescargaProfesor descarga) {
        this.descarga = descarga;
    }

    public Collection<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(Collection<Evento> eventos) {
        this.eventos = eventos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumCursosPregradoMaestria() {
        return numCursosPregradoMaestria;
    }

    public void setNumCursosPregradoMaestria(Integer numCursosPregradoMaestria) {
        this.numCursosPregradoMaestria = numCursosPregradoMaestria;
    }

    public Integer getNumeroPublicacionesPlaneadas() {
        return numeroPublicacionesPlaneadas;
    }

    public void setNumeroPublicacionesPlaneadas(Integer numeroprivateacionesPlaneadas) {
        this.numeroPublicacionesPlaneadas = numeroprivateacionesPlaneadas;
    }

    public Collection<OtrasActividades> getOtros() {
        return otros;
    }

    public void setOtros(Collection<OtrasActividades> otros) {
        this.otros = otros;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Collection<ProyectoFinanciado> getProyectosFinanciados() {
        return proyectosFinanciados;
    }

    public void setProyectosFinanciados(Collection<ProyectoFinanciado> proyectosFinanciados) {
        this.proyectosFinanciados = proyectosFinanciados;
    }

    public Collection<DireccionTesis> getTesisAcargo() {
        return tesisAcargo;
    }

    public void setTesisAcargo(Collection<DireccionTesis> tesisAcargo) {
        this.tesisAcargo = tesisAcargo;
    }

    public PeriodoPlaneacion getPeriodoPlaneacion() {
        return periodoPlaneacion;
    }

    public void setPeriodoPlaneacion(PeriodoPlaneacion periodoPlaneacion) {
        this.periodoPlaneacion = periodoPlaneacion;
    }

    public NivelFormacion getMaximoNivelTesis() {
        return maximoNivelTesis;
    }

    public void setMaximoNivelTesis(NivelFormacion maximoNivelTesis) {
        this.maximoNivelTesis = maximoNivelTesis;
    }

    public Collection<IntencionPublicacion> getIntencionPublicaciones() {
        return intencionPublicaciones;
    }

    public void setIntencionPublicaciones(Collection<IntencionPublicacion> intencionPublicaciones) {
        this.intencionPublicaciones = intencionPublicaciones;
    }

    public Integer getNumeroEstudiantesTesis1() {
        return numeroEstudiantesTesis1;
    }

    public void setNumeroEstudiantesTesis1(Integer numeroEstudiantesMaestria) {
        this.numeroEstudiantesTesis1 = numeroEstudiantesMaestria;
    }

    public Integer getNumeroEstudiantesPregrado() {
        return numeroEstudiantesPregrado;
    }

    public void setNumeroEstudiantesPregrado(Integer numeroEstudiantesPregrado) {
        this.numeroEstudiantesPregrado = numeroEstudiantesPregrado;
    }

    public Integer getNumeroProyectosFinanciados() {
        return numeroProyectosFinanciados;
    }

    public void setNumeroProyectosFinanciados(Integer numeroProyectosFinanciados) {
        this.numeroProyectosFinanciados = numeroProyectosFinanciados;
    }

    public Integer getNumeroEstudiantesDoctorado() {
        return numeroEstudiantesDoctorado;
    }

    public void setNumeroEstudiantesDoctorado(Integer numeroEstudiantesDoctorado) {
        this.numeroEstudiantesDoctorado = numeroEstudiantesDoctorado;
    }

    public Integer getNumeroEstudiantesTesis2() {
        return numeroEstudiantesTesis2;
    }

    public void setNumeroEstudiantesTesis2(Integer numeroEstudiantesTesis2) {
        this.numeroEstudiantesTesis2 = numeroEstudiantesTesis2;
    }

    public Integer getNumeroEstudiantesTesis2Pendiente() {
        return numeroEstudiantesTesis2Pendiente;
    }

    public void setNumeroEstudiantesTesis2Pendiente(Integer numeroEstudiantesTesis2Pendiente) {
        this.numeroEstudiantesTesis2Pendiente = numeroEstudiantesTesis2Pendiente;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaProfesor)) {
            return false;
        }
        CargaProfesor other = (CargaProfesor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Integer getNumCapitulosLibro() {
        return numCapitulosLibro;
    }

    public void setNumCapitulosLibro(Integer numCapitulosLibro) {
        this.numCapitulosLibro = numCapitulosLibro;
    }

    public Integer getNumCongresosInternacio() {
        return numCongresosInternacio;
    }

    public void setNumCongresosInternacio(Integer numCongresosInternacio) {
        this.numCongresosInternacio = numCongresosInternacio;
    }

    public Integer getNumCongresosNacionales() {
        return numCongresosNacionales;
    }

    public void setNumCongresosNacionales(Integer numCongresosNacionales) {
        this.numCongresosNacionales = numCongresosNacionales;
    }

    public Integer getNumRevistas() {
        return numRevistas;
    }

    public void setNumRevistas(Integer numRevistas) {
        this.numRevistas = numRevistas;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.CargaProfesor[id=" + id + "]";
    }
}

