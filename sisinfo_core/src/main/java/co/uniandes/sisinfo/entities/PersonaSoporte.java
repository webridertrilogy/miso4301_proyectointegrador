/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autor: Juan Manuel Moreno B.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad PersonaSoporte
 */
@Entity
@Table(name = "personasoporte")
@NamedQueries({
    @NamedQuery(name = "PersonaSoporte.findByEmail", query = "SELECT i FROM PersonaSoporte i WHERE i.persona.correo =:correo")
})
public class PersonaSoporte implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<Incidente> incidentes;

    @OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Persona persona;

    /* Getter Setter */
    
    public Collection<Incidente> getIncidentes() {
        return incidentes;
    }

    public void setIncidentes(Collection<Incidente> incidentes) {
        this.incidentes = incidentes;
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
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PersonaSoporte)) {
            return false;
        }
        PersonaSoporte other = (PersonaSoporte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.serviciosfuncionales.seguridad.PersonaSoporte[id=" + id + ", correo=" + persona.getCorreo() + "]";
    }
}
