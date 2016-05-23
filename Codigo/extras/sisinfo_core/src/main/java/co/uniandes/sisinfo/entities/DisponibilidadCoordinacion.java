package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entidad que representa la disponibilidad de coordinación
 * @author German Florez, Marcela Morales
 */
@Entity
public class DisponibilidadCoordinacion implements Serializable {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    public static String[] nombreDias = {"lunes","martes","miercoles","jueves","viernes"};
    private static final long serialVersionUID = 1L;

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<DiaDisponibilidad> disponibilidadDias;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<DiaDisponibilidad> getDisponibilidadDias() {
        return disponibilidadDias;
    }

    public void setDisponibilidadDias(Collection<DiaDisponibilidad> disponibilidadDias) {
        this.disponibilidadDias = disponibilidadDias;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DisponibilidadCoordinacion)) {
            return false;
        }
        DisponibilidadCoordinacion other = (DisponibilidadCoordinacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.DisponibilidadCoordinacion[id=" + id + "]";
    }
}
