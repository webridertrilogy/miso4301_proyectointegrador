/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.uniandes.sisinfo.docPrivados.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidad Documento Privado
 */
@Entity
@Table(name = "documentoPrivado")
@NamedQueries({
    @NamedQuery(name = "DocumentoPrivado.findByIsActive", query = "SELECT d FROM DocumentoPrivado d WHERE d.activo = :activo")
})
public class DocumentoPrivado implements Serializable {

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

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "fecha")
    private Timestamp fecha;

    @Column(name = "ruta")
    private String ruta;

    @ManyToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<PalabraClave> palabrasClave;

    @Column(name = "tipoMime")
    private String tipoMime;

    @Column(name = "idPadre")
    private Long idPadre;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Publicador publicador;

    @Column(name = "activo")
    private Boolean activo;

    //---------------------------------------
    // Métodos
    //---------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public Long getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(Long idPadre) {
        this.idPadre = idPadre;
    }

    public Collection<PalabraClave> getPalabrasClave() {
        return palabrasClave;
    }

    public String getRuta() {
        return ruta;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public void setPalabrasClave(Collection<PalabraClave> palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Publicador getPublicador() {
        return publicador;
    }

    public void setPublicador(Publicador publicador) {
        this.publicador = publicador;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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
        if (!(object instanceof DocumentoPrivado)) {
            return false;
        }
        DocumentoPrivado other = (DocumentoPrivado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.docPrivados.entities.DocumentoPrivado[id=" + id + "]";
    }
}
