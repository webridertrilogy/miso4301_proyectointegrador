/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad que representa a un proponente
 * @author Marcela Morales
 */
@Entity
@Table(name = "proponente")
@NamedQueries({
    @NamedQuery(name = "Proponente.findAll", query = "SELECT p FROM Proponente p"),
    @NamedQuery(name = "Proponente.findById", query = "SELECT p FROM Proponente p WHERE p.id = :id"),
    @NamedQuery(name = "Proponente.findByCorreo", query = "SELECT p FROM Proponente p WHERE p.persona.correo = :correo"),
    @NamedQuery(name = "Proponente.findByTipoEmpresa", query = "SELECT p FROM Proponente p WHERE p.esEmpresa = true"),
    @NamedQuery(name = "Proponente.findByIdOferta", query = "SELECT p FROM Proponente p LEFT JOIN p.ofertas ofe WHERE ofe.id = :id")
    })
public class Proponente implements Serializable {

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
    @Column(name = "esEmpresa")
    private Boolean esEmpresa;
    @OneToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<Oferta> ofertas;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private InformacionEmpresa informacionEmpresa;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Persona persona;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isEsEmpresa() {
        return esEmpresa;
    }

    public void setEsEmpresa(Boolean esEmpresa) {
        this.esEmpresa = esEmpresa;
    }

    public InformacionEmpresa getInformacionEmpresa() {
        return informacionEmpresa;
    }

    public void setInformacionEmpresa(InformacionEmpresa informacionEmpresa) {
        this.informacionEmpresa = informacionEmpresa;
    }

    public Collection<Oferta> getOfertas() {
        return ofertas;
    }
      public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public void setOfertas(Collection<Oferta> ofertas) {
        this.ofertas = ofertas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Proponente)) {
            return false;
        }
        Proponente other = (Proponente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Proponente[id=" + id + "]";
    }
}
