package co.uniandes.sisinfo.entities.datosmaestros;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author David Naranjo, Marcela Morales
 * Entidad Sección
 */
@Entity
@Table(name = "seccion")
@NamedQueries({
    @NamedQuery(name = "Seccion.findAll", query = "SELECT s FROM Seccion s"),
    @NamedQuery(name = "Seccion.findById", query = "SELECT s FROM Seccion s WHERE s.id = :id"),
    @NamedQuery(name = "Seccion.findByCRN", query = "SELECT s FROM Seccion s WHERE s.crn = :crn"),
    @NamedQuery(name = "Seccion.findByNumeroSeccion", query = "SELECT s FROM Seccion s WHERE s.numeroSeccion = :numeroSeccion"),
    @NamedQuery(name = "Seccion.findByCorreoProfesor", query = "SELECT s FROM Seccion s WHERE s.profesorPrincipal.persona.correo = :correo"),
    @NamedQuery(name = "Seccion.contarSecciones", query = "SELECT count(s) FROM Seccion s")
})
public class Seccion implements Serializable {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 1L;

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Basic(optional = false)
    @Column(name = "crn", unique = true)
    private String crn;
    @Basic(optional = false)
    @Column(name = "numeroSeccion")
    private int numeroSeccion;
    @Basic(optional = false)
    @Column(name = "maximoMonitores")
    private double maximoMonitores;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Sesion> horarios;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Profesor> profesores;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Profesor profesorPrincipal;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public int getNumeroSeccion() {
        return numeroSeccion;
    }

    public void setNumeroSeccion(int numeroSeccion) {
        this.numeroSeccion = numeroSeccion;
    }

    public Collection<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(Collection<Profesor> profesores) {
        this.profesores = profesores;
    }

    public Collection<Sesion> getHorarios() {
        return horarios;
    }

    public void setHorarios(Collection<Sesion> horarios) {
        this.horarios = horarios;
    }

    public double getMaximoMonitores() {
        return maximoMonitores;
    }

    public void setMaximoMonitores(double maximoMonitores) {
        this.maximoMonitores = maximoMonitores;
    }

    public Profesor getProfesorPrincipal() {
        return profesorPrincipal;
    }

    public void setProfesorPrincipal(Profesor profesorPrincipal) {
        this.profesorPrincipal = profesorPrincipal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Seccion)) {
            return false;
        }
        Seccion other = (Seccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Seccion[id=" + id + "]";
    }
}
