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
@Entity @Table(name="votante")
@NamedQueries({
    @NamedQuery(name = "Votante.findSinVotar", query = "SELECT v FROM Votante v WHERE v.yaVoto=false"),
    @NamedQuery(name = "Votante.findByCorreo", query = "SELECT v FROM Votante v WHERE v.persona.correo=:correo"),
    @NamedQuery(name = "Votante.findByCorreoYIDVotacion", query= "SELECT v FROM Votante v WHERE v.persona.correo=:correo AND v.votacion.id=:idVotacion"),
    @NamedQuery(name = "Votante.findByIdVotacion", query = "SELECT v FROM Votante v WHERE v.votacion.id =:idVotacion")
})
public class Votante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Persona persona;

    @OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Votacion votacion;

    @Column(name="yaVoto")
    private boolean yaVoto;

  

    public boolean isYaVoto() {
        return yaVoto;
    }

    public void setYaVoto(boolean yaVoto) {
        this.yaVoto = yaVoto;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Votante)) {
            return false;
        }
        Votante other = (Votante) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Votante[id=" + getId() + "]";
    }

}
