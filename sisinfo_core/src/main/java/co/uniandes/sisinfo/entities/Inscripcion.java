/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author im.melo33
 */
@Entity
@Table(name="inscripcion")
@NamedQueries({
    @NamedQuery(name = "Inscripcion.findByCreador", query = "SELECT i FROM Inscripcion i WHERE i.correoCreador=:correoCreador"),
    @NamedQuery(name = "Inscripcion.findAbiertas", query = "SELECT i FROM Inscripcion i WHERE i.abierta=true"),
    @NamedQuery(name = "Inscripcion.findCerradas", query = "SELECT i FROM Inscripcion i WHERE i.abierta=false"),
     @NamedQuery(name = "Inscripcion.findById", query = "SELECT i FROM Inscripcion i WHERE i.id=:id")

})
public class Inscripcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="creador")
    private String correoCreador;

    @Column(name="nombreInscripcion")
    private String nombreInscripcion;

    @Column(name="detallesInscripcion", length=10000)
    private String detallesInscripcion;

    @Column(name="fechaInicio")
    private Timestamp fechaInicio;

    @Column(name="fechaFin")
    private Timestamp fechaFin;

    @Column(name="fechaEvento")
    private Timestamp fechaEvento;

    @Column(name="lugarEvento")
    private String lugarEvento;

    @Column(name="abierta")
    private boolean abierta;

    @Column(name="correoNotificacion")
    private String correoNotificacion;





    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    private Collection<InscripcionAsistente> invitados;

    public Inscripcion(String correoCreador, String nombreInscripcion, String detallesInscripcion, Timestamp fechaInicio, Timestamp fechaFin, boolean abierta) {
        this.correoCreador = correoCreador;
        this.nombreInscripcion = nombreInscripcion;
        this.detallesInscripcion = detallesInscripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.abierta = abierta;
        invitados= new ArrayList<InscripcionAsistente>();
    }

    public Inscripcion() {
         invitados= new ArrayList<InscripcionAsistente>();
    }



    public String getCorreoCreador() {
        return correoCreador;
    }

    public String getDetallesInscripcion() {
        return detallesInscripcion;
    }

    public boolean isAbierta() {
        return abierta;
    }

   

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public String getNombreInscripcion() {
        return nombreInscripcion;
    }
    public Long getId() {
        return id;
    }

    public Collection <InscripcionAsistente> getInvitados() {
        return invitados;
    }

    public String getCorreoNotificacion() {
        return correoNotificacion;
    }

    public Timestamp getFechaEvento() {
        return fechaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    

    public void setCorreoNotificacion(String correoNotificacion) {
        this.correoNotificacion = correoNotificacion;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setCorreoCreador(String correoCreador) {
        this.correoCreador = correoCreador;
    }

    public void setDetallesInscripcion(String detallesInscripcion) {
        this.detallesInscripcion = detallesInscripcion;
    }

   
    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setNombreInscripcion(String nombreInscripcion) {
        this.nombreInscripcion = nombreInscripcion;
    }

    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }

    public void setInvitados(Collection<InscripcionAsistente> invitados) {
        this.invitados = invitados;
    }

    public void setFechaEvento(Timestamp fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
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
        if (!(object instanceof Inscripcion)) {
            return false;
        }
        Inscripcion other = (Inscripcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Inscripcion[id=" + id + "]";
    }

}
