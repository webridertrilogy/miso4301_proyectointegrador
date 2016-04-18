package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * @author German Florez, Marcela Morales, Paola Gómez
 * Entidad Petición de conflicto de horario
 */
@Entity(name = "peticionconflictohorario")
@NamedQueries({
    @NamedQuery(name = "PeticionConflictoHorario.findByEstudianteYSeccionDestino", query = "SELECT p FROM peticionconflictohorario p WHERE p.estudiante.persona.correo=:correoEstudiante and p.destino.id=:idSeccionDestino"),
    @NamedQuery(name = "PeticionConflictoHorario.findByCorreo", query = "SELECT p FROM peticionconflictohorario p WHERE p.estudiante.persona.correo=:correo"),
    @NamedQuery(name = "PeticionConflictoHorario.findByIdYCorreo", query = "SELECT p FROM peticionconflictohorario p WHERE p.estudiante.persona.correo=:correo and p.id=:id"),
    @NamedQuery(name = "PeticionConflictoHorario.findByCodigoCurso", query = "SELECT p FROM peticionconflictohorario p WHERE p.cursoDestino.codigo=:codigo"),
    @NamedQuery(name = "PeticionConflictoHorario.findByCodigoCursoYTipo", query = "SELECT p FROM peticionconflictohorario p WHERE p.cursoDestino.codigo=:codigo and p.tipo=:tipo and p.fechaResolucion is null"),
    @NamedQuery(name = "PeticionConflictoHorario.findResueltas", query = "SELECT p FROM peticionconflictohorario p WHERE p.fechaResolucion is not null"),
    @NamedQuery(name = "PeticionConflictoHorario.findByIdSeccion", query = "SELECT p FROM peticionconflictohorario p WHERE p.origen.id=:id or p.destino.id=:id")
})
public class PeticionConflictoHorario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Estudiante estudiante;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Seccion origen;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Seccion destino;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Curso cursoOrigen;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Curso cursoDestino;
    @Column
    private String estado;
    @Column
    private Timestamp fechaCreacion;
    @Column
    private Timestamp fechaResolucion;
    @Column(length=5000)
    private String comentariosResolucion;
    @Column(length=5000)
    private String comentarios;
    @Column
    private String tipo;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Timestamp getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Timestamp fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public Seccion getDestino() {
        return destino;
    }

    public void setDestino(Seccion destino) {
        this.destino = destino;
    }

    public Seccion getOrigen() {
        return origen;
    }

    public void setOrigen(Seccion origen) {
        this.origen = origen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComentariosResolucion() {
        return comentariosResolucion;
    }

    public void setComentariosResolucion(String comentariosResolucion) {
        this.comentariosResolucion = comentariosResolucion;
    }

    public Curso getCursoDestino() {
        return cursoDestino;
    }

    public void setCursoDestino(Curso cursoDestino) {
        this.cursoDestino = cursoDestino;
    }

    public Curso getCursoOrigen() {
        return cursoOrigen;
    }

    public void setCursoOrigen(Curso cursoOrigen) {
        this.cursoOrigen = cursoOrigen;
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
        if (!(object instanceof PeticionConflictoHorario)) {
            return false;
        }
        PeticionConflictoHorario other = (PeticionConflictoHorario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.PeticionConflictoHorario[id=" + id + "]";
    }
}
