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
 * Entidad Periodo
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Periodo.findById", query = "SELECT p FROM Periodo p WHERE p.id = :id"),
    @NamedQuery(name = "Periodo.findByPeriodo", query = "SELECT p FROM Periodo p WHERE p.periodo = :periodo"),
    @NamedQuery(name= "Periodo.findByActual", query = "SELECT p FROM Periodo p WHERE p.actual = :actual")
})
@Table(name = "periodo")
public class Periodo implements Serializable {

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

    @Column(name = "periodo", unique = true)
    private String periodo;

    @OneToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Convocatoria convocatoria;

    @Column(name="actual")
    private boolean actual;

//    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
//    private Collection<SolicitudMaterialBibliografico> solicitudesMatBib;
    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }


    public Convocatoria getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

//    public Collection<SolicitudMaterialBibliografico> getSolicitudesMatBib() {
//        return solicitudesMatBib;
//    }
//
//    public void setSolicitudesMatBib(Collection<SolicitudMaterialBibliografico> solicitudesMatBib) {
//        this.solicitudesMatBib = solicitudesMatBib;
//    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Periodo)) {
            return false;
        }
        Periodo other = (Periodo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Periodo[id=" + id + "]";
    }
}
