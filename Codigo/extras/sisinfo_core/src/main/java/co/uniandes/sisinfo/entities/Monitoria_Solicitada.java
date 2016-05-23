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

import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad Monitoría Solicitada
 */
@Entity
@Table(name = "monitoria_solicitada")
public class Monitoria_Solicitada implements Serializable {

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
    private Curso curso;
    @Column(name = "nota_materia")
    private double nota_materia;
    @Column(name = "periodo_Academico_En_Que_Se_Vio")
    private String periodoAcademicoEnQueSeVio;
    @Column(name = "profesor_Con_Quien_La_Vio")
    private String profesorConQuienLaVio;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getNota_materia() {
        return nota_materia;
    }

    public void setNota_materia(double nota_materia) {
        this.nota_materia = nota_materia;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getProfesorConQuienLaVio() {
        return profesorConQuienLaVio;
    }

    public void setProfesorConQuienLaVio(String profesorConQuienLaVio) {
        this.profesorConQuienLaVio = profesorConQuienLaVio;
    }

    public String getPeriodoAcademicoEnQueSeVio() {
        return periodoAcademicoEnQueSeVio;
    }

    public void setPeriodoAcademicoEnQueSeVio(String periodoAcademicoEnQueSeVio) {
        this.periodoAcademicoEnQueSeVio = periodoAcademicoEnQueSeVio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Monitoria_Solicitada)) {
            return false;
        }
        Monitoria_Solicitada other = (Monitoria_Solicitada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Monitoria_Solicitada[id=" + id + "]";
    }
}
