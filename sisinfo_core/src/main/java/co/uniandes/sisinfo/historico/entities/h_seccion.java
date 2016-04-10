/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author david
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "h_seccion.findByPeriodo", query = "SELECT s FROM h_seccion s WHERE s.periodo = :periodo"),
    @NamedQuery(name = "h_seccion.findByCorreoProfesor", query = "SELECT s FROM h_seccion s WHERE s.profesor.correo = :correo")
})
public class h_seccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String crn;

    private String horario;

    private String numeroSeccion;

    public Collection<h_archivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(Collection<h_archivo> archivos) {
        this.archivos = archivos;
    }

    private String periodo;

    @OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
    private h_profesor profesor;

    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
    private Collection<h_archivo> archivos;

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getNumeroSeccion() {
        return numeroSeccion;
    }

    public void setNumeroSeccion(String numeroSeccion) {
        this.numeroSeccion = numeroSeccion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public h_profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(h_profesor profesor) {
        this.profesor = profesor;
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
        if (!(object instanceof h_seccion)) {
            return false;
        }
        h_seccion other = (h_seccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.h_seccion[id=" + id + "]";
    }

}
