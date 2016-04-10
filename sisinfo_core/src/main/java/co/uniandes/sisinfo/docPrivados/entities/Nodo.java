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
 * Entidad Nodo
 */
@Entity
@Table(name="nodo")
@NamedQueries({
    @NamedQuery(name = "Nodo.findById", query = "SELECT n FROM Nodo n WHERE n.id = :id"),
    @NamedQuery(name = "Nodo.findByPadreId", query = "SELECT n FROM Nodo n WHERE n.idPadre = :padreId"),
    @NamedQuery(name = "Nodo.findByDocumentoId", query = "SELECT n FROM Nodo n LEFT JOIN n.docs doc WHERE doc.id = :docId")
})
public class Nodo implements Serializable { 

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 1L;

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "idPadre", nullable=false)
    private Long idPadre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    @Column(name="nodos")
    private Collection<Nodo> nodos;

    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    @Column(name="documentos")
    private Collection<DocumentoPrivado> docs;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void agregarNodo(Nodo nuevoHijo){
        nodos.add(nuevoHijo);
    }

   public void agregarDoc(DocumentoPrivado nuevoHijo){
        docs.add(nuevoHijo);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<DocumentoPrivado> getDocs() {
        return docs;
    }

    public void setDocs(Collection<DocumentoPrivado> docs) {
        this.docs = docs;
    }

    public Long getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(Long idPadre) {
        this.idPadre = idPadre;
    }

    public Collection<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(Collection<Nodo> nodos) {
        this.nodos = nodos;
    }

    public void eliminarNodo(Nodo nodoAEliminar){
        this.nodos.remove(nodoAEliminar);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof Nodo)) {
            return false;
        }
        Nodo other = (Nodo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.docPrivados.entities.Nodo[id=" + id + "]";
    }
}
