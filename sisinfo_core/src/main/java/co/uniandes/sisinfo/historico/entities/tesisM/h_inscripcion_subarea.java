/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities.tesisM;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.CascadeType;
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
 *
 * @author Asistente
 */
@Entity
/*@Table(name = "h_inscripcion_subarea")
@NamedQueries({
    @NamedQuery(name = "h_inscripcion_subarea.findByCorreoEstudiante", query = "SELECT e FROM h_inscripcion_subarea e WHERE e.estudiante.correo = :correo")
})
 *
 */
public class h_inscripcion_subarea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="nombreSubarea")
    private String nombreSubarea;
    @Column(name = "fechaCreacion")
    private Timestamp fechaCreacion;
    @OneToMany(cascade=CascadeType.ALL)
    private Collection<h_curso_tomado> cursos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<h_curso_tomado> getCursos() {
        return cursos;
    }

    public void setCursos(Collection<h_curso_tomado> cursos) {
        this.cursos = cursos;
    }

    public String getNombreSubarea() {
        return nombreSubarea;
    }

    public void setNombreSubarea(String nombreSubarea) {
        this.nombreSubarea = nombreSubarea;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    


    /*
    public h_estudiante_maestria getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(h_estudiante_maestria estudiante) {
        this.estudiante = estudiante;
    }
    */
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof h_inscripcion_subarea)) {
            return false;
        }
        h_inscripcion_subarea other = (h_inscripcion_subarea) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.tesisM.h_inscripcion_subarea[id=" + id + "]";
    }

}
