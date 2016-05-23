/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author da-naran
 */
@Entity @Table(name="candidato")
//@PrimaryKeyJoinColumn(name="persona_id")
@NamedQueries({
    @NamedQuery(name = "Candidato.findByCorreo", query = "SELECT c FROM Candidato c WHERE c.persona.correo=:correo"),
    @NamedQuery(name = "Candidato.findByCorreoYIDVotacion", query= "SELECT c FROM Candidato c WHERE c.persona.correo=:correo AND c.votacion.id=:idVotacion"),
    @NamedQuery(name = "Candidato.findVotoEnBlancoYIDVotacion", query= "SELECT c FROM Candidato c WHERE c.persona IS NULL AND c.votacion.id=:idVotacion")
})
public class Candidato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Persona persona;

    @Column(name="numeroVotos")
    private int numeroVotos;

    @OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Votacion votacion;

    public int getNumeroVotos() {
        return numeroVotos;
    }

    public void setNumeroVotos(int numeroVotos) {
        this.numeroVotos = numeroVotos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Votacion getVotacion() {
        return votacion;
    }

    public void setVotacion(Votacion votacion) {
        this.votacion = votacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Candidato)) {
            return false;
        }
        Candidato other = (Candidato) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Candidato[id=" + getId() + "]";
    }

}
