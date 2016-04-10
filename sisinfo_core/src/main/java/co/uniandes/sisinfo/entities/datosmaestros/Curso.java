/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.entities.datosmaestros;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad Curso
 */
@Entity
@Table(name = "curso")
@NamedQueries({
    @NamedQuery(name = "Curso.findById", query = "SELECT c FROM Curso c WHERE c.id = :id"),
    @NamedQuery(name = "Curso.findByNombre", query = "SELECT c FROM Curso c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Curso.findByCodigo", query = "SELECT c FROM Curso c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Curso.findByCRN", query = "SELECT c FROM Curso c LEFT JOIN c.secciones sec WHERE sec.crn = :crn"),
    @NamedQuery(name = "Curso.contarCursos", query = "SELECT count(c) FROM Curso c")
})
public class Curso implements Serializable {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 1L;

    //---------------------------------------
    // Atributos
    //---------------------------------------

    /* Id del curso */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /* Nivel del curso */
    @Column(name = "nivel")
    private int nivel;

    /* Presencialidad del curso */
    @Column(name = "presencial")
    private boolean presencial;

    /* Código del curso */
    @Column(name = "codigo")
    private String codigo;

    /* Nombre del curso */
    @Column(name = "nombre")
    private String nombre;

    /* Créditos del curso */
    @Column(name = "creditos")
    private double creditos;

    /* Colección de secciones del curso */
    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<Seccion> secciones;

    /* Indica si el curso es requerido */
    @Column(name="requerido")
    private boolean requerido;

    /* Cursos relacionados: e. APO + Laboratorio */
    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<Curso> cursosRelacionados;

    /* Programa e. ISIS */
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Programa programa;

    /* Nivel de programa e. pregrado */
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private NivelFormacion nivelPrograma;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getCreditos() {
        return creditos;
    }

    public int getNivel() {
        return nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isPresencial() {
        return presencial;
    }

    public Collection<Seccion> getSecciones() {

        return secciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCreditos(double creditos) {
        this.creditos = creditos;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPresencial(boolean presencial) {
        this.presencial = presencial;
    }

    public void setSecciones(Collection<Seccion> secciones) {
        this.secciones = secciones;
    }

    public boolean isRequerido() {
        return requerido;
    }

    public void setRequerido(boolean requerido) {
        this.requerido = requerido;
    }

    public Collection<Curso> getCursosRelacionados() {
        return cursosRelacionados;
    }

    public void setCursosRelacionados(Collection<Curso> cursosRelacionados) {
        this.cursosRelacionados = cursosRelacionados;
    }

    public NivelFormacion getNivelPrograma() {
        return nivelPrograma;
    }

    public void setNivelPrograma(NivelFormacion nivelPrograma) {
        this.nivelPrograma = nivelPrograma;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Curso[id=" + id + "]";
    }
}
