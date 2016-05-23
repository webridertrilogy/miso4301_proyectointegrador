/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import java.io.Serializable;
import java.util.Collection;
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
 * Entidad Horario Disponible
 */
@Entity
@Table(name = "horario_disponible")
@NamedQueries({
    @NamedQuery(name = "Horario_Disponible.findAll", query = "SELECT h FROM Horario_Disponible h"),
    @NamedQuery(name = "Horario_Disponible.findById", query = "SELECT h FROM Horario_Disponible h WHERE h.id = :id"),
@NamedQuery(name = "Horario_Disponible.findByCodigoEstudiante", query = "SELECT a.horario_disponible FROM Aspirante a WHERE a.estudiante.persona.codigo = :codigo")})
public class Horario_Disponible implements Serializable {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 1L;

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<DiaCompleto> dias;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<DiaCompleto> getDias_disponibles() {
        return dias;
    }

    public void setDias_disponibles(Collection<DiaCompleto> dias_disponibles) {
        this.dias = dias_disponibles;
    }


    //-----------------------------------------------------------------
    // Metodos
    //-----------------------------------------------------------------
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Horario_Disponible)) {
            return false;
        }
        Horario_Disponible other = (Horario_Disponible) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Horario_Disponible[id=" + id + "]";
    }
}
