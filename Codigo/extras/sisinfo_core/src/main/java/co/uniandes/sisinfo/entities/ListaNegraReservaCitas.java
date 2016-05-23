/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "listaNegraReservaCitas")
@NamedQueries({
    @NamedQuery(name ="ListaNegraReservaCitas.findByCorreo",query = "SELECT lnrc FROM ListaNegraReservaCitas lnrc WHERE lnrc.persona.correo = :correo")
})
public class ListaNegraReservaCitas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Persona persona;

    @Column(name="razon", length = 5000)
    private String razon;

    @Column(name="fechaIngreso")
    private Timestamp fechaIngreso;

    @Column(name="fechaVencimiento")
    private Timestamp fechaVencimiento;

    @Column(name="idTimer")
    private long idTimer;

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
        if (!(object instanceof ListaNegraReservaCitas)) {
            return false;
        }
        ListaNegraReservaCitas other = (ListaNegraReservaCitas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ListaNegraReservaCitas[id=" + id + "]";
    }

    public Timestamp getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Timestamp fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Timestamp getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Timestamp fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public long getIdTimer() {
        return idTimer;
    }

    public void setIdTimer(long idTimer) {
        this.idTimer = idTimer;
    }

    

}
