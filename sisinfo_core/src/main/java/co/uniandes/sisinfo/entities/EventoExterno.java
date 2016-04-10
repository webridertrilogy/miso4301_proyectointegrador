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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "evento_externo")
@NamedQueries({
    @NamedQuery(name = "EventoExterno.findInscritosByIdEvento", query = "SELECT e.inscripciones FROM EventoExterno e WHERE e.id=:id"),
    @NamedQuery(name = "EventoExterno.findEventosByIdInscripcion", query = "SELECT e from EventoExterno e left join e.inscripciones i where i.id = :id"),
    @NamedQuery(name = "EventoExterno.findEventosByIdCategoria", query = "SELECT e from EventoExterno e  WHERE e.categoria.id = :id"),
    @NamedQuery(name = "EventoExterno.findEventosByTitulo", query = "SELECT e from EventoExterno e  WHERE e.titulo= :titulo"),
    @NamedQuery(name = "EventoExterno.findEventosByEstado", query = "SELECT e FROM EventoExterno e WHERE e.estado=:estado"),
    @NamedQuery(name = "EventoExterno.findInscritoByIdEventoAndIdContacto", query = "SELECT i FROM EventoExterno e left join e.inscripciones i WHERE e.id=:idEvento AND i.contacto.id =:idContacto")
})
public class EventoExterno implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descripcion", length = 5000)
    private String descripcion;
    @Column(name = "fechaLimiteInscripciones")
    private Timestamp fechaLimiteInscripciones;
    @Column(name = "fechaHoraInicio")
    private Timestamp fechaHoraInicio;
    @Column(name = "rutaImagen")
    private String rutaImagen;
    @Column(name = "cupo")
    private Integer cupo;
    @Column(name = "estado")
    private String estado;
    @Column(name = "linkInscripcion")
    private String linkInscripcion;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<CampoAdicional> camposAdicionales;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private CategoriaEventoExterno categoria;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Collection<Pregunta> preguntas;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Collection<InscripcionEventoExterno> inscripciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<CampoAdicional> getCamposAdicionales() {
        return camposAdicionales;
    }

    public void setCamposAdicionales(Collection<CampoAdicional> camposAdicionales) {
        this.camposAdicionales = camposAdicionales;
    }

    public CategoriaEventoExterno getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEventoExterno categoria) {
        this.categoria = categoria;
    }

    public Integer getCupo() {
        return cupo;
    }

    public void setCupo(Integer cupo) {
        this.cupo = cupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Timestamp fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Collection<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(Collection<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public Collection<InscripcionEventoExterno> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(Collection<InscripcionEventoExterno> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLinkInscripcion() {
        return linkInscripcion;
    }

    public void setLinkInscripcion(String linkInscripcion) {
        this.linkInscripcion = linkInscripcion;
    }

    public Timestamp getFechaLimiteInscripciones() {
        return fechaLimiteInscripciones;
    }

    public void setFechaLimiteInscripciones(Timestamp fechaFin) {
        this.fechaLimiteInscripciones = fechaFin;
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
        if (!(object instanceof EventoExterno)) {
            return false;
        }
        EventoExterno other = (EventoExterno) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Evento[id=" + id + "]";
    }
}
