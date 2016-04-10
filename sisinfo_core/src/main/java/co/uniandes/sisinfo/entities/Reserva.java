package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Entidad que representa a una Reserva
 * @author German Florez, Marcela Morales
 */
@Entity
@Table(name = "reservas")
@NamedQueries({
    @NamedQuery(name = "Reserva.findById", query = "SELECT r FROM Reserva r WHERE r.id=:id"),
    @NamedQuery(name = "Reserva.DeleteById", query = "delete from Reserva r WHERE r.id=:id"),
    @NamedQuery(name = "Reserva.findByCorreo", query = "Select r from Reserva r where r.persona.correo=:correo"),
    @NamedQuery(name = "Reserva.findLikeCorreo", query = "Select r from Reserva r where r.persona.correo like :correo"),
    @NamedQuery(name = "Reserva.findLikeCorreoYFecha", query = "Select r from Reserva r where r.persona.correo like :correo and r.inicio>=:fecha"),
    @NamedQuery(name = "Reserva.findLikeCorreoYEstado", query = "Select r from Reserva r where r.persona.correo like :correo and r.estado=:estado"),
    @NamedQuery(name = "Reserva.findByEstado", query = "Select r from Reserva r where r.estado=:estado"),
    @NamedQuery(name = "Reserva.findByFecha", query = "Select r from Reserva r where r.fecha=:fecha order by r.inicio"),
    //Busca si una reserva se intersecta con un rango de horas inicio, fin
    @NamedQuery(name = "Reserva.findByInterseccionHoras", query = "Select r from Reserva r where r.fecha=:fecha  AND r.estado NOT LIKE \'Cancelado por Coordinación\' AND((r.inicio>:inicio and r.fin<:fin) or (r.fin>:inicio and r.fin<:fin) or (:inicio>r.inicio and :inicio<r.fin) or (:fin>r.inicio and :fin<r.fin) or (r.inicio=:inicio and r.fin=:fin))"),
    @NamedQuery(name = "Reserva.findByRango", query = "Select r from Reserva r where r.fecha>=:inicio and r.fecha<=:fin AND r.estado NOT LIKE \'Cancelado por Coordinación\'"),
    @NamedQuery(name = "Reserva.findAll", query = "Select r from Reserva r where r.fecha>=:inicio and r.fecha<=:fin AND r.estado NOT LIKE \'Cancelado por Coordinación\'")
})
public class Reserva implements Serializable {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "comentarios")
    private String comentarios;
    @Column(name = "programa")
    private String programa;
    @Column
    private String motivo;
    @Column(name = "fecha")
    private Timestamp fecha;
    @Column
    private Timestamp inicio;
    @Column
    private Timestamp fin;
    @OneToOne
    private DatosContacto contacto;
    @ManyToOne
    private Persona persona;
    @Column
    private String estado;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Reserva() {
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public DatosContacto getContacto() {
        return contacto;
    }

    public void setContacto(DatosContacto contacto) {
        this.contacto = contacto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Timestamp getFin() {
        return fin;
    }

    public void setFin(Timestamp fin) {
        this.fin = fin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getInicio() {
        return inicio;
    }

    public void setInicio(Timestamp inicio) {
        this.inicio = inicio;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
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
        if (!(object instanceof Reserva)) {
            return false;
        }
        Reserva other = (Reserva) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Reserva[id=" + id + "]";
    }
}
