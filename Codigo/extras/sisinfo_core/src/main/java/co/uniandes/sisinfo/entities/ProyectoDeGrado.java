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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entidad Proyecto de Grado
 * @author Ivan Mauricio Melo S, Marcela Morales, Paola Gómez
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "ProyectoDeGrado.findBycorreoEstudiante", query = "SELECT i FROM ProyectoDeGrado i WHERE i.estudiante.persona.correo =:correoEstudiante"),
    @NamedQuery(name = "ProyectoDeGrado.findBycorreoAsesor", query = "SELECT i FROM ProyectoDeGrado i WHERE i.asesor.persona.correo =:correoasesor"),
    @NamedQuery(name = "ProyectoDeGrado.findByPeriodoTesis", query = "SELECT i FROM ProyectoDeGrado i WHERE i.semestreIniciacion.nombre =:periodo"),
    @NamedQuery(name = "ProyectoDeGrado.findByEstadoTesis", query = "SELECT i FROM ProyectoDeGrado i WHERE i.estadoTesis =:estadoTesis"),
    @NamedQuery(name = "ProyectoDeGrado.findByPeriodoEstado", query = "SELECT i FROM ProyectoDeGrado i WHERE i.estadoTesis like :estadoTesis AND i.semestreIniciacion.id like :idPeriodo "),
    @NamedQuery(name = "ProyectoDeGrado.findByPeriodoEstadoRangoNota", query = "SELECT i FROM ProyectoDeGrado i WHERE i.estadoTesis like :estadoTesis AND i.semestreIniciacion.id like :idPeriodo AND i.calificacionTesis >=:notaI AND i.calificacionTesis <=:notaF"),
    @NamedQuery(name = "ProyectoDeGrado.findByEstadoRangoNota", query = "SELECT i FROM ProyectoDeGrado i WHERE i.estadoTesis like :estadoTesis AND i.calificacionTesis >=:notaI AND i.calificacionTesis <=:notaF"),
    @NamedQuery(name = "ProyectoDeGrado.findByTemaTesis", query = "SELECT i FROM ProyectoDeGrado i WHERE i.temaTesis =:tema")
})
@Table(name = "proyecto_de_grado_tesis")
public class ProyectoDeGrado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fechaCreacion")
    private Timestamp fechaCreacion;
    @Column(name = "temaTesis")
    private String temaTesis;
    @Column(name = "aprobadoAsesor")
    private String aprobadoAsesor;
    @Column(name = "calificacionTesis")
    private Double calificacionTesis;
    @Column(name = "rutaArticuloTesis1")
    private String rutaArticuloTesis1;
    @Column(name = "rutaPosterTesis")
    private String rutaPosterTesis;
    @Column(name = "rutaABET")
    private String rutaABET;
    @Column(name = "estadoTesis")
    private String estadoTesis;
    @Column(name = "estadoPoster")
    private Boolean estadoPoster;
    @Column(name = "rutaArchivoPropuesta")
    private String rutaArchivoPropuesta;
    @Column(name = "rutaArchivoPendienteEspecial")
    private String rutaArchivoPendienteEspecial;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Estudiante estudiante;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Profesor asesor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private PeriodoTesisPregrado semestreIniciacion;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<ComentarioTesisPregrado> comentariosAsesor;

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
        if (!(object instanceof ProyectoDeGrado)) {
            return false;
        }
        ProyectoDeGrado other = (ProyectoDeGrado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ProyectoDeGrado[id=" + id + "]";
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

    public Double getCalificacionTesis() {
        return calificacionTesis;
    }

    public void setCalificacionTesis(Double calificacionTesis) {
        this.calificacionTesis = calificacionTesis;
    }

    public Collection<ComentarioTesisPregrado> getComentariosAsesor() {
        return comentariosAsesor;
    }

    public void setComentariosAsesor(Collection<ComentarioTesisPregrado> comentariosAsesor) {
        this.comentariosAsesor = comentariosAsesor;
    }

    public String getRutaABET() {
        return rutaABET;
    }

    public void setRutaABET(String rutaABET) {
        this.rutaABET = rutaABET;
    }

    public String getRutaPosterTesis() {
        return rutaPosterTesis;
    }

    public void setRutaPosterTesis(String rutaPosterTesis) {
        this.rutaPosterTesis = rutaPosterTesis;
    }

    public PeriodoTesisPregrado getSemestreIniciacion() {
        return semestreIniciacion;
    }

    public void setSemestreIniciacion(PeriodoTesisPregrado semestreIniciacion) {
        this.semestreIniciacion = semestreIniciacion;
    }

    public String getEstadoTesis() {
        return estadoTesis;
    }

    public void setEstadoTesis(String estadoTesis) {
        this.estadoTesis = estadoTesis;
    }

    public Boolean getEstadoPoster() {
        return estadoPoster;
    }

    public void setEstadoPoster(Boolean estadoPoster) {
        this.estadoPoster = estadoPoster;
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

    public String getRutaArticuloTesis1() {
        return rutaArticuloTesis1;
    }

    public void setRutaArticuloTesis1(String rutaArticuloTesis1) {
        this.rutaArticuloTesis1 = rutaArticuloTesis1;
    }

    public String getTemaTesis() {
        return temaTesis;
    }

    public void setTemaTesis(String temaTesis) {
        this.temaTesis = temaTesis;
    }

    public String getRutaArchivoPropuesta() {
        return rutaArchivoPropuesta;
    }

    public void setRutaArchivoPropuesta(String rutaArchivoPropuesta) {
        this.rutaArchivoPropuesta = rutaArchivoPropuesta;
    }

    public String getRutaArchivoPendienteEspecial() {
        return rutaArchivoPendienteEspecial;
    }

    public void setRutaArchivoPendienteEspecial(String rutaArchivoPendienteEspecial) {
        this.rutaArchivoPendienteEspecial = rutaArchivoPendienteEspecial;
    }

}
