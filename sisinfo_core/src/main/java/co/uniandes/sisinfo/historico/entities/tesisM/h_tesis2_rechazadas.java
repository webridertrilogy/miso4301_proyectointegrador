/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.entities.tesisM;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ivan Melo
 */
@Entity
@Table(name = "h_tesis2_rechazadas")
@NamedQueries({
    @NamedQuery(name = "h_tesis2_rechazadas.findByCorreoEstudiante", query = "SELECT e FROM h_tesis2_rechazadas e WHERE e.correoEstudiante = :correo"),
    @NamedQuery(name = "h_tesis2_rechazadas.findByCorreoasesor", query = "SELECT e FROM h_tesis2_rechazadas e WHERE e.correoAsesor = :correo")
})
public class h_tesis2_rechazadas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "fechaCreacion")
    private Timestamp fechaCreacion;
    @Column(name = "fechaRechazo")
    private Timestamp fechaRechazo;
    @Column(name = "temaTesis")
    private String temaTesis;
    @Column(name = "nombresEstudiante")
    private String nombresEstudiante;
    @Column(name = "apellidosEstudiante")
    private String apellidosEstudiante;
    @Column(name = "correoEstudiante")
    private String correoEstudiante;
    @Column(name = "nombresAsesor")
    private String nombresAsesor;
    @Column(name = "apellidosAsesor")
    private String apellidosAsesor;
    @Column(name = "correoAsesor")
    private String correoAsesor;
    @Column(name = "rechazadoPor")
    private String rechazadoPor;
    @Column(name = "nombreSubarea")
    private String nombreSubarea;
    @Column(name = "semestre")
    private String semestre;

    public h_tesis2_rechazadas() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApellidosAsesor() {
        return apellidosAsesor;
    }

    public void setApellidosAsesor(String apellidosAsesor) {
        this.apellidosAsesor = apellidosAsesor;
    }

    public String getApellidosEstudiante() {
        return apellidosEstudiante;
    }

    public void setApellidosEstudiante(String apellidosEstudiante) {
        this.apellidosEstudiante = apellidosEstudiante;
    }

    public String getCorreoAsesor() {
        return correoAsesor;
    }

    public void setCorreoAsesor(String correoAsesor) {
        this.correoAsesor = correoAsesor;
    }

    public String getCorreoEstudiante() {
        return correoEstudiante;
    }

    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }

    public String getRechazadoPor() {
        return rechazadoPor;
    }

    public void setRechazadoPor(String rechazadoPor) {
        this.rechazadoPor = rechazadoPor;
    }

   

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(Timestamp fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    public String getNombreSubarea() {
        return nombreSubarea;
    }

    public void setNombreSubarea(String nombreSubarea) {
        this.nombreSubarea = nombreSubarea;
    }

    public String getNombresAsesor() {
        return nombresAsesor;
    }

    public void setNombresAsesor(String nombresAsesor) {
        this.nombresAsesor = nombresAsesor;
    }

    public String getNombresEstudiante() {
        return nombresEstudiante;
    }

    public void setNombresEstudiante(String nombresEstudiante) {
        this.nombresEstudiante = nombresEstudiante;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getTemaTesis() {
        return temaTesis;
    }

    public void setTemaTesis(String temaTesis) {
        this.temaTesis = temaTesis;
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
        if (!(object instanceof h_tesis2_rechazadas)) {
            return false;
        }
        h_tesis2_rechazadas other = (h_tesis2_rechazadas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.tesisM.h_tesis1_rechazadas[id=" + id + "]";
    }
}
