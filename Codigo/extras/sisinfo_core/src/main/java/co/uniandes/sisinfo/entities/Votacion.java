/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.Table;

/**
 *
 * @author da-naran
 */
@Entity
@Table(name="votacion")
@NamedQueries({
    @NamedQuery(name = "Votacion.findVotacionesActivas", query = "SELECT v FROM Votacion v WHERE v.abierta=true"),
    @NamedQuery(name = "Votacion.findVotacionesPorCorreo", query = "SELECT v FROM Votacion v LEFT JOIN v.votantes vot WHERE vot.persona.correo = :correo")
})
public class Votacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="nombre")
    private String nombre;

    @Column(name="fechaInicio")
    private Timestamp fechaInicio;

    @Column(name="fechaFin")
    private Timestamp fechaFin;

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="abierta")
    private boolean abierta;

    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    private Collection<Votante> votantes;

    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    private Collection<Candidato> candidatos;

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
        if (!(object instanceof Votacion)) {
            return false;
        }
        Votacion other = (Votacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Candidato> getCandidatos() {
        return candidatos;
    }

    public void setCandidatos(Collection<Candidato> candidatos) {
        this.candidatos = candidatos;
    }

    public Collection<Votante> getVotantes() {
        return votantes;
    }

    public void setVotantes(Collection<Votante> votantes) {
        this.votantes = votantes;
    }

    public boolean isAbierta() {
        return abierta;
    }

    public void setAbierta(boolean estado) {
        this.abierta = estado;
    }



    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Votacion[id=" + id + "]";
    }

}
