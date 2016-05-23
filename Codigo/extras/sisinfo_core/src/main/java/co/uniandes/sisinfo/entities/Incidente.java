/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Ivan Melo
 */
@Entity

@Table(name = "Incidente")
@NamedQueries({
    @NamedQuery(name = "Incidente.findByDescripcionYFecha", query = "SELECT i FROM Incidente i WHERE i.descripcionIncidente =:descripcionIncidente AND i.fechaIncidente =:fechaIncidente AND i.eliminado = false "),
    @NamedQuery(name = "Incidente.findByCorreoReportante", query = "SELECT i FROM Incidente i WHERE i.reportadoPor.correo =:correo "),//AND i.eliminado = false 
     @NamedQuery(name = "Incidente.findByEstado", query = "SELECT i FROM Incidente i WHERE i.estadoIncidente =:estado AND i.eliminado = false "),
     @NamedQuery(name = "Incidente.findByNOBorrado", query = "SELECT i FROM Incidente i WHERE  i.eliminado = false "),
     @NamedQuery(name = "Incidente.findByPersonaSoporte", query = "SELECT i FROM Incidente i LEFT JOIN i.personaSoporte p WHERE p.id = :idPersonaSoporte")
})
public class Incidente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fechaIncidente")
    private Timestamp fechaIncidente;
    @Column(name = "moduloIncididente")
    private String moduloIncididente;
    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Persona reportadoPor;
    @Column(name="descripcionIncidente"  , length=5000)
    private String descripcionIncidente;
  
    @Column(name = "estadoIncidente")
    private String estadoIncidente;
    @Column(name = "fechaSolucion")
    private Timestamp fechaSolucion;
    @Column(name = "descripcionSolucion"  , length=5000)
    private String descripcionSolucion;
    @Column(name = "solucionado")
    private Boolean solucionado;

    @Column(name = "eliminado")
    private Boolean eliminado;

    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private PersonaSoporte personaSoporte;

    public PersonaSoporte getPersonaSoporte() {
        return personaSoporte;
    }

    public void setPersonaSoporte(PersonaSoporte personaSoporte) {
        this.personaSoporte = personaSoporte;
    }
    
    public String getDescripcionSolucion() {
        return descripcionSolucion;
    }

    public void setDescripcionSolucion(String descripcionSolucion) {
        this.descripcionSolucion = descripcionSolucion;
    }

    public String getEstadoIncidente() {
        return estadoIncidente;
    }

    public void setEstadoIncidente(String estadoIncidente) {
        this.estadoIncidente = estadoIncidente;
    }

    public Timestamp getFechaIncidente() {
        return fechaIncidente;
    }

    public void setFechaIncidente(Timestamp fechaIncidente) {
        this.fechaIncidente = fechaIncidente;
    }

    public Timestamp getFechaSolucion() {
        return fechaSolucion;
    }

    public void setFechaSolucion(Timestamp fechaSolucion) {
        this.fechaSolucion = fechaSolucion;
    }

  

    public String getModuloIncididente() {
        return moduloIncididente;
    }

    public void setModuloIncididente(String moduloIncididente) {
        this.moduloIncididente = moduloIncididente;
    }

    public Persona getReportadoPor() {
        return reportadoPor;
    }

    public void setReportadoPor(Persona reportadoPor) {
        this.reportadoPor = reportadoPor;
    }

    public Boolean getSolucionado() {
        return solucionado;
    }

    public void setSolucionado(Boolean solucionado) {
        this.solucionado = solucionado;
    }

    public String getDescripcionIncidente() {
        return descripcionIncidente;
    }

    public void setDescripcionIncidente(String descripcionIncidente) {
        this.descripcionIncidente = descripcionIncidente;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }


    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Incidente)) {
            return false;
        }
        Incidente other = (Incidente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Incidente[id=" + id + "]";
    }
}
