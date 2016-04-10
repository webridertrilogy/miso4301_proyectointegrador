package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidad que representa a una asistencia graduada
 * @author Marcela Morales
 */
@Entity
@Table(name = "AsistenciaGraduada")
@NamedQueries({
    @NamedQuery(name = "AsistenciaGraduada.findById", query = "SELECT a FROM AsistenciaGraduada a WHERE id=:id"),
    @NamedQuery(name = "AsistenciaGraduada.findByPeriodo", query = "SELECT a FROM AsistenciaGraduada a WHERE periodo.periodo=:periodo"),
    @NamedQuery(name = "AsistenciaGraduada.findByCorreoProfesor", query = "SELECT a FROM AsistenciaGraduada a WHERE encargado.correo=:correo"),
    @NamedQuery(name = "AsistenciaGraduada.findByCorreoEstudiante", query = "SELECT a FROM AsistenciaGraduada a WHERE estudiante.persona.correo=:correo"),
    @NamedQuery(name = "AsistenciaGraduada.findByPeriodoYCorreoEstudiante", query = "SELECT a FROM AsistenciaGraduada a WHERE periodo.periodo=:periodo AND estudiante.persona.correo=:correo")
})
public class AsistenciaGraduada implements Serializable {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 1L;

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Periodo periodo;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Persona encargado;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Estudiante estudiante;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private TipoAsistenciaGraduada tipo;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private TipoAsistenciaGraduada subtipo;
    @Column(name = "infoTipo")
    private String infoTipo;
    @Column(name = "nota")
    private Double nota;
    @Column(name = "observaciones")
    private String observaciones;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Persona getEncargado() {
        return encargado;
    }

    public void setEncargado(Persona encargado) {
        this.encargado = encargado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public TipoAsistenciaGraduada getTipo() {
        return tipo;
    }

    public void setTipo(TipoAsistenciaGraduada tipo) {
        this.tipo = tipo;
    }

    public TipoAsistenciaGraduada getSubtipo() {
        return subtipo;
    }

    public void setSubtipo(TipoAsistenciaGraduada subtipo) {
        this.subtipo = subtipo;
    }

    public String getInfoTipo() {
        return infoTipo;
    }

    public void setInfoTipo(String infoTipo) {
        this.infoTipo = infoTipo;
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
        if (!(object instanceof AsistenciaGraduada)) {
            return false;
        }
        AsistenciaGraduada other = (AsistenciaGraduada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.AsistenciaGraduada[id=" + id + "]";
    }
}
