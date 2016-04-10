package co.uniandes.sisinfo.historico.entities.cyc;

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
@Table(name = "h_cargaProfesor_cyc")
@NamedQueries({
    @NamedQuery(name = "h_cargaProfesor_cyc.findAll", query = "SELECT e FROM h_cargaProfesor_cyc e"),
    @NamedQuery(name = "h_cargaProfesor_cyc.findById", query = "SELECT e FROM h_cargaProfesor_cyc e WHERE e.id = :id"),
    @NamedQuery(name = "h_cargaProfesor_cyc.findByCorreo", query = "SELECT e FROM h_cargaProfesor_cyc e WHERE e.correo LIKE :correo"),
    @NamedQuery(name = "h_cargaProfesor_cyc.findByCorreoYPeriodo", query = "SELECT e FROM h_cargaProfesor_cyc e WHERE e.correo LIKE :correo AND e.periodoPlaneacion.periodo LIKE :periodo")
})
public class h_cargaProfesor_cyc implements Serializable {

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
    @Column(name = "maximoNivelTesis")
    private String maximoNivelTesis;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "correo")
    private String correo;
    @Column(name = "nombreDescarga")
    private String nombreDescarga;
    //relacioens
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<h_otras_actividades> otros;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<h_curso_planeado> cursos;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<h_evento> eventos;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<h_proyecto_financiado> proyectosFinanciados;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<h_direccion_tesis> tesisAcargo;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Collection<h_intencion_publicacion> intencionPublicaciones;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private h_periodo_planeacion periodoPlaneacion;

    public h_cargaProfesor_cyc() {
    }

    public h_cargaProfesor_cyc(Long id, Double cargaProfesor, String nombres, String apellidos, String correo, Collection<h_otras_actividades> otros, String descarga, Collection<h_curso_planeado> cursos, Collection<h_evento> eventos, Collection<h_proyecto_financiado> proyectosFinanciados, Collection<h_direccion_tesis> tesisAcargo, Collection<h_intencion_publicacion> intencionPublicaciones, h_periodo_planeacion periodoPlaneacion) {
        this.id = id;
        this.cargaProfesor = cargaProfesor;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.otros = otros;
        this.nombreDescarga = descarga;
        this.cursos = cursos;
        this.eventos = eventos;
        this.proyectosFinanciados = proyectosFinanciados;
        this.tesisAcargo = tesisAcargo;
        this.intencionPublicaciones = intencionPublicaciones;
        this.periodoPlaneacion = periodoPlaneacion;
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

    public h_cargaProfesor_cyc(Long id, Double cargaProfesor, Integer numCursosPregradoMaestria, Integer numeroPublicacionesPlaneadas, Integer numRevistas, Integer numCongresosInternacio, Integer numCongresosNacionales, Integer numCapitulosLibro, Integer numeroProyectosFinanciados, Integer numeroEstudiantesPregrado, Integer numeroEstudiantesTesis1, Integer numeroEstudiantesTesis2, Integer numeroEstudiantesTesis2Pendiente, Integer numeroEstudiantesDoctorado, Double cargaEfectiva, String maximoNivelTesis, String nombres, String apellidos, String correo, Collection<h_otras_actividades> otros, String descarga, Collection<h_curso_planeado> cursos, Collection<h_evento> eventos, Collection<h_proyecto_financiado> proyectosFinanciados, Collection<h_direccion_tesis> tesisAcargo, Collection<h_intencion_publicacion> intencionPublicaciones, h_periodo_planeacion periodoPlaneacion) {
        this.id = id;
        this.cargaProfesor = cargaProfesor;
        this.numCursosPregradoMaestria = numCursosPregradoMaestria;
        this.numeroPublicacionesPlaneadas = numeroPublicacionesPlaneadas;
        this.numRevistas = numRevistas;
        this.numCongresosInternacio = numCongresosInternacio;
        this.numCongresosNacionales = numCongresosNacionales;
        this.numCapitulosLibro = numCapitulosLibro;
        this.numeroProyectosFinanciados = numeroProyectosFinanciados;
        this.numeroEstudiantesPregrado = numeroEstudiantesPregrado;
        this.numeroEstudiantesTesis1 = numeroEstudiantesTesis1;
        this.numeroEstudiantesTesis2 = numeroEstudiantesTesis2;
        this.numeroEstudiantesTesis2Pendiente = numeroEstudiantesTesis2Pendiente;
        this.numeroEstudiantesDoctorado = numeroEstudiantesDoctorado;
        this.cargaEfectiva = cargaEfectiva;
        this.maximoNivelTesis = maximoNivelTesis;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.otros = otros;
        this.nombreDescarga = descarga;
        this.cursos = cursos;
        this.eventos = eventos;
        this.proyectosFinanciados = proyectosFinanciados;
        this.tesisAcargo = tesisAcargo;
        this.intencionPublicaciones = intencionPublicaciones;
        this.periodoPlaneacion = periodoPlaneacion;
    }
    //--------------------

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

    public Collection<h_curso_planeado> getCursos() {
        return cursos;
    }

    public void setCursos(Collection<h_curso_planeado> cursos) {
        this.cursos = cursos;
    }

    public String getDescarga() {
        return nombreDescarga;
    }

    public void setDescarga(String descarga) {
        this.nombreDescarga = descarga;
    }

    public Collection<h_evento> getEventos() {
        return eventos;
    }

    public void setEventos(Collection<h_evento> eventos) {
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

    public Collection<h_otras_actividades> getOtros() {
        return otros;
    }

    public void setOtros(Collection<h_otras_actividades> otros) {
        this.otros = otros;
    }

    public Collection<h_proyecto_financiado> getProyectosFinanciados() {
        return proyectosFinanciados;
    }

    public void setProyectosFinanciados(Collection<h_proyecto_financiado> proyectosFinanciados) {
        this.proyectosFinanciados = proyectosFinanciados;
    }

    public Collection<h_direccion_tesis> getTesisAcargo() {
        return tesisAcargo;
    }

    public void setTesisAcargo(Collection<h_direccion_tesis> tesisAcargo) {
        this.tesisAcargo = tesisAcargo;
    }

    public h_periodo_planeacion getPeriodoPlaneacion() {
        return periodoPlaneacion;
    }

    public void setPeriodoPlaneacion(h_periodo_planeacion periodoPlaneacion) {
        this.periodoPlaneacion = periodoPlaneacion;
    }

    public String getMaximoNivelTesis() {
        return maximoNivelTesis;
    }

    public void setMaximoNivelTesis(String maximoNivelTesis) {
        this.maximoNivelTesis = maximoNivelTesis;
    }

    public Collection<h_intencion_publicacion> getIntencionPublicaciones() {
        return intencionPublicaciones;
    }

    public void setIntencionPublicaciones(Collection<h_intencion_publicacion> intencionPublicaciones) {
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
        if (!(object instanceof h_cargaProfesor_cyc)) {
            return false;
        }
        h_cargaProfesor_cyc other = (h_cargaProfesor_cyc) object;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}

