package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidad que representa a un Autorizado
 * @author Marcela Morales
 */
@Entity
@Table(name = "Autorizado")
@NamedQueries({
    @NamedQuery(name = "Autorizado.findByCorreo", query = "SELECT e FROM Autorizado e WHERE e.persona.correo=:correo")
})
public class Autorizado implements Serializable {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Persona persona;

    @Column(name="puedeReservarATerceros")
    private Boolean puedeReservarATerceros;
    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Autorizado() {
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

    public Boolean getPuedeReservarATerceros() {
        return puedeReservarATerceros;
    }

    public void setPuedeReservarATerceros(Boolean puedeReservarATerceros) {
        this.puedeReservarATerceros = puedeReservarATerceros;
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
        if (!(object instanceof Autorizado)) {
            return false;
        }
        Autorizado other = (Autorizado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Autorizado[id=" + id + "]";
    }
}
