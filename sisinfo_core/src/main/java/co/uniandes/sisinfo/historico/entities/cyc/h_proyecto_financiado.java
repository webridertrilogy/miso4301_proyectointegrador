/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.entities.cyc;

import java.util.Collection;
import javax.persistence.ManyToMany;
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
 * @author Asistente
 */
@Entity
@Table(name = "h_proyecto_financiado")
@NamedQueries({
    @NamedQuery(name = "h_proyecto_financiado.findById", query = "SELECT r FROM h_proyecto_financiado r WHERE r.id = :id"),
    @NamedQuery(name = "h_proyecto_financiado.findByNombre", query = "SELECT r FROM h_proyecto_financiado r WHERE r.nombre LIKE :nombre"),
    @NamedQuery(name = "h_proyecto_financiado.findByNombreEntidadDescripcionYPeriodo", query = "SELECT r FROM h_proyecto_financiado r WHERE r.nombre LIKE :nombre AND r.entidadFinanciadora LIKE :entidad "
    + "AND r.descripcion LIKE :descripcion AND r.periodo LIKE :periodo")
})
public class h_proyecto_financiado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre_Proyecto")
    private String nombre;
    @Column(name = "descripcion", length = 3000)
    private String descripcion;
    @ManyToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<h_cargaProfesor_cyc> profesores;
    @Column(name = "entidad_financiadora")
    private String entidadFinanciadora;
    @Column(name = "periodo")
    private String periodo;

    public h_proyecto_financiado(Long id, String nombre, String descripcion, Collection<h_cargaProfesor_cyc> profesores, String entidadFinanciadora, String periodo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesores = profesores;
        this.entidadFinanciadora = entidadFinanciadora;
        this.periodo=periodo;
    }

    public h_proyecto_financiado(String nombre, String descripcion, Collection<h_cargaProfesor_cyc> profesores, String entidadFinanciadora, String periodo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesores = profesores;
        this.entidadFinanciadora = entidadFinanciadora;
        this.periodo=periodo;
    }

    public h_proyecto_financiado() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<h_cargaProfesor_cyc> getProfesores() {
        return profesores;
    }

    public void setProfesores(Collection<h_cargaProfesor_cyc> profesores) {
        this.profesores = profesores;
    }

    public String getEntidadFinanciadora() {
        return entidadFinanciadora;
    }

    public void setEntidadFinanciadora(String entidadFinanciadora) {
        this.entidadFinanciadora = entidadFinanciadora;
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
        if (!(object instanceof h_proyecto_financiado)) {
            return false;
        }
        h_proyecto_financiado other = (h_proyecto_financiado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ProyectoFinanciado[id=" + id + "]";
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }


}
