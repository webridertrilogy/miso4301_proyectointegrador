package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entidad que representa los datos de contacto de la persona que asistirá a la cita
 * @author German Florez, Marcela Morales
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "DatosContacto.findByNombre", query = "SELECT d FROM DatosContacto d WHERE d.nombre=:nombre")

})
public class DatosContacto implements Serializable {
    
    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 0xFA1L;

    //---------------------------------------
    // Constantes
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String nombre;
    @Column
    private String celular;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DatosContacto)) {
            return false;
        }
        DatosContacto other = (DatosContacto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.DatosContacto[id=" + id + "]";
    }
}
