/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entidad Material Bibliográfico
 */
@Entity
@Table(name = "materialBibliografico")
@NamedQueries({
    @NamedQuery(name = "MaterialBibliografico.findById", query = "SELECT m FROM MaterialBibliografico m WHERE m.id = :id")
})
public class MaterialBibliografico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "autor")
    private String autor;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "ISBNoISSN")
    private String ISBNoISSN;
    @Column(name = "editorial")
    private String editorial;
    @Column(name = "anio")
    private Integer anio;
    @OneToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<Volumen> volumenes;
    @Column(name = "precio")
    private Double precio;
    @Column(name = "proveedor")
    private String proveedor;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "nivelFormacion")
    private String nivelFormacion;
    @Column(name = "tematica")
    private String tematica;
    @Column(name="cantidadTotal")
    private Integer cantidadTotal;
    @Column(name="textoGuia")
    private Boolean textoGuia;

    @Column(name="cursoTextoGuia")
    private String cursoTextoGuia;

    public Long getId() {
        return id;
    }

    public void setVolumenes(Collection<Volumen> volumenes) {
        this.volumenes = volumenes;
    }

    public Collection<Volumen> getVolumenes() {
        return volumenes;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getISBNoISSN() {
        return ISBNoISSN;
    }

    public void setISBNoISSN(String ISBNoISSN) {
        this.ISBNoISSN = ISBNoISSN;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getNivelFormacion() {
        return nivelFormacion;
    }

    public void setNivelFormacion(String nivelFormacion) {
        this.nivelFormacion = nivelFormacion;
    }

    

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getTematica() {
        return tematica;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Integer cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public Boolean isTextoGuia() {
        return textoGuia;
    }

    public void setTextoGuia(Boolean textoGuia) {
        this.textoGuia = textoGuia;
    }

    public String getCursoTextoGuia() {
        return cursoTextoGuia;
    }

    public void setCursoTextoGuia(String cursoTextoGuia) {
        this.cursoTextoGuia = cursoTextoGuia;
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
        if (!(object instanceof MaterialBibliografico)) {
            return false;
        }
        MaterialBibliografico other = (MaterialBibliografico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.MaterialBibliografico[id=" + id + "]";
    }
}
