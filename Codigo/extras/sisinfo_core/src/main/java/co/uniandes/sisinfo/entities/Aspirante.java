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

import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad Aspirante
 */
@Entity
@Table(name = "aspirante")
@NamedQueries({
    @NamedQuery(name = "Aspirante.findAll", query = "SELECT a FROM Aspirante a"),
    @NamedQuery(name = "Aspirante.findById", query = "SELECT a FROM Aspirante a WHERE a.id = :id"),
    @NamedQuery(name = "Aspirante.findByCodigo", query = "SELECT a FROM Aspirante a WHERE a.estudiante.persona.codigo = :codigo"),
    @NamedQuery(name = "Aspirante.findByCorreo", query = "SELECT a FROM Aspirante a WHERE a.estudiante.persona.correo = :correo"),
    @NamedQuery(name = "Aspirante.findAllStudentsByApellidos", query = "SELECT a FROM Aspirante a WHERE a.estudiante.persona.apellidos LIKE :apellidos"),
    @NamedQuery(name = "Aspirante.findAllStudentsByNombres", query = "SELECT a FROM Aspirante a WHERE a.estudiante.persona.nombres LIKE :nombres")})
public class Aspirante implements Serializable {

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
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Estudiante estudiante;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Horario_Disponible horario_disponible;
    @OneToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<MonitoriaOtroDepartamento> monitoriasOtrosDepartamentos;
    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    private Collection<MonitoriaRealizada> monitoriasRealizadas;


    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Horario_Disponible getHorario_disponible() {
        return horario_disponible;
    }

    public void setHorario_disponible(Horario_Disponible horario_disponible) {
        this.horario_disponible = horario_disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante persona) {
        this.estudiante = persona;
    }

    public Persona getPersona() {
        return estudiante.getPersona();
    }

    public Collection<MonitoriaOtroDepartamento> getMonitoriasOtrosDepartamentos() {
        return monitoriasOtrosDepartamentos;
    }

    public void setMonitoriasOtrosDepartamentos(Collection<MonitoriaOtroDepartamento> monitoriasOtrosDepartamentos) {
        this.monitoriasOtrosDepartamentos = monitoriasOtrosDepartamentos;
    }

    public Collection<MonitoriaRealizada> getMonitoriasRealizadas() {
        return monitoriasRealizadas;
    }

    public void setMonitoriasRealizadas(Collection<MonitoriaRealizada> monitoriasRealizadas) {
        this.monitoriasRealizadas = monitoriasRealizadas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Aspirante)) {
            return false;
        }
        Aspirante other = (Aspirante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Aspirante[id=" + id + "]";
    }
}
