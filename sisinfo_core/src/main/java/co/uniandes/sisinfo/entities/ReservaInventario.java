package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
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
import javax.persistence.Table;

/**
 * Entidad que representa a una Reserva de inventario
 * @author Marcela Morales
 */
@Entity
@Table(name = "ReservaInventario")
@NamedQueries({
    @NamedQuery(name = "ReservaInventario.findAll", query = "SELECT r FROM ReservaInventario r"),
    @NamedQuery(name = "ReservaInventario.findById", query = "SELECT r FROM ReservaInventario r WHERE r.id = :id"),
    @NamedQuery(name = "ReservaInventario.findReservasByNombreLaboratorio", query = "SELECT r FROM ReservaInventario r WHERE r.laboratorio.nombre = :nombre"),
    @NamedQuery(name = "ReservaInventario.findReservasByNombreLaboratorioAndEstado", query = "SELECT r FROM ReservaInventario r WHERE r.laboratorio.nombre = :nombre AND r.estado = :estado"),
    @NamedQuery(name = "ReservaInventario.findReservasByNombreLaboratorioAndEstadoAndRangoFechas", query = "SELECT r FROM ReservaInventario r WHERE r.laboratorio.nombre = :nombre AND r.estado = :estado AND r.fechaInicio BETWEEN :fechaInicial AND :fechaFinal"),
    @NamedQuery(name = "ReservaInventario.findReservasAnterioresAFechaByEstado", query = "SELECT r FROM ReservaInventario r WHERE r.estado = :estado AND r.fechaReserva <= :fecha"),
    @NamedQuery(name = "ReservaInventario.findReservasByPersonaAndEstado", query = "SELECT r FROM ReservaInventario r WHERE r.creador.correo = :correo AND r.estado = :estado"),
    @NamedQuery(name = "ReservaInventario.findReservasByEstado", query = "SELECT r FROM ReservaInventario r WHERE r.estado = :estado"),
    @NamedQuery(name = "ReservaInventario.findReservasByRangoFechas", query = "SELECT r FROM ReservaInventario r WHERE r.fechaInicio BETWEEN :fechaInicial AND :fechaFinal")
    
})
public class ReservaInventario implements Serializable {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "responsable")
    private String responsable;
    @Column
    private String estado;
    @ManyToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Persona creador;
    @ManyToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Laboratorio laboratorio;
    @ManyToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<Elemento> elementos;
    @Column
    private Timestamp fechaReserva;
    @Column
    private Timestamp fechaInicio;
    @Column
    private Timestamp fechaFin;
    @Column
    private boolean enviarRecordatorio;
    @Column
    private boolean cuentaInvitado;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public ReservaInventario() {
    }

    public Persona getCreador() {
        return creador;
    }

    public void setCreador(Persona creador) {
        this.creador = creador;
    }

    public Collection<Elemento> getElementos() {
        return elementos;
    }

    public void setElementos(Collection<Elemento> elementos) {
        this.elementos = elementos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Timestamp fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public boolean isEnviarRecordatorio() {
        return enviarRecordatorio;
    }

    public void setEnviarRecordatorio(boolean enviarRecordatorio) {
        this.enviarRecordatorio = enviarRecordatorio;
    }

    public boolean isCuentaInvitado() {
        return cuentaInvitado;
    }

    public void setCuentaInvitado(boolean cuentaInvitado) {
        this.cuentaInvitado = cuentaInvitado;
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
        if (!(object instanceof ReservaInventario)) {
            return false;
        }
        ReservaInventario other = (ReservaInventario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ReservaInventario[id=" + id + "]";
    }
}
