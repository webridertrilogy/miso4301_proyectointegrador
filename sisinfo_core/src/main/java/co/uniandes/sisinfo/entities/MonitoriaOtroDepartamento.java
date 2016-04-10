/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.io.Serializable;
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
 * @author lj.bautista31
 */
@Entity
@Table(name= "monitoriaOtroDepartamento")
@NamedQueries({
    @NamedQuery(name = "MonitoriaOtroDepartamento.findByCodigoMateriaAndCodigoEstudiante", query = "SELECT m FROM Aspirante a LEFT JOIN a.monitoriasOtrosDepartamentos m WHERE a.estudiante.persona.codigo = :codigoEstudiante AND m.codigoCurso = :codigoCurso"),
    @NamedQuery(name = "MonitoriaOtroDepartamento.findByCodigoEstudiante", query = "SELECT m FROM Aspirante a LEFT JOIN a.monitoriasOtrosDepartamentos m WHERE a.estudiante.persona.codigo = :codigoEstudiante")
})
public class MonitoriaOtroDepartamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "codigoCurso")
    private String codigoCurso;

    @Column(name = "nombreCurso")
    private String nombreCurso;
    
    @Column(name = "periodo")
    private String periodo;

    @Column(name = "nombreProfesor")
    private String nombreProfesor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonitoriaOtroDepartamento)) {
            return false;
        }
        MonitoriaOtroDepartamento other = (MonitoriaOtroDepartamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.MonitoriaOtroDepartamento[id=" + id + "]";
    }

}
