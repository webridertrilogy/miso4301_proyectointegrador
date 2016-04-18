package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;

/**
 * Entidad que representa a un estudiante de postgrado
 * @author Ivan Melo, Marcela Morales
 */
@Entity
@Table(name = "estudiante_posgrado")
@NamedQueries({
    @NamedQuery(name = "EstudiantePosgrado.findAll", query = "SELECT e FROM EstudiantePosgrado e"),
    @NamedQuery(name = "EstudiantePosgrado.findById", query = "SELECT e FROM EstudiantePosgrado e WHERE e.id = :id"),
    @NamedQuery(name = "EstudiantePosgrado.findByCorreo", query = "SELECT e FROM EstudiantePosgrado e WHERE e.estudiante.persona.correo = :correo")
    })
public class EstudiantePosgrado implements Serializable {

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
    @Column(name = "esExterno")
    private Boolean esExterno;
    @Column(name = "fechaGraduacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaGraduacion;
    @Column(name = "nombreUniversidad")
    private String universidadPregrado;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "ciudad")
    private String ciudadUniversidadPregrado;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Pais paisUniversidadPregrado;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Estudiante estudiante;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private HojaVida hojaVida;
    @Column(name = "esPrimerSemestreMaestria")
    private Boolean esPrimerSemestreMaestria;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCiudadUniversidadPregrado() {
        return ciudadUniversidadPregrado;
    }

    public void setCiudadUniversidadPregrado(String ciudadUniversidadPregrado) {
        this.ciudadUniversidadPregrado = ciudadUniversidadPregrado;
    }

    public Boolean isEsExterno() {
        return esExterno;
    }

    public void setEsExterno(Boolean esExterno) {
        this.esExterno = esExterno;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Date getFechaGraduacion() {
        return fechaGraduacion;
    }

    public void setFechaGraduacion(Date fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    public Pais getPaisUniversidadPregrado() {
        return paisUniversidadPregrado;
    }

    public void setPaisUniversidadPregrado(Pais paisUniversidadPregrado) {
        this.paisUniversidadPregrado = paisUniversidadPregrado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUniversidadPregrado() {
        return universidadPregrado;
    }

    public void setUniversidadPregrado(String universidadPregrado) {
        this.universidadPregrado = universidadPregrado;
    }

    public Boolean getEsPrimerSemestreMaestria() {
        return esPrimerSemestreMaestria;
    }

    public void setEsPrimerSemestreMaestria(Boolean esPrimerSemestreMaestria) {
        this.esPrimerSemestreMaestria = esPrimerSemestreMaestria;
    }

    public HojaVida getHojaVida() {
        return hojaVida;
    }

    public void setHojaVida(HojaVida hojaVida) {
        this.hojaVida = hojaVida;
    }

    public Boolean getEsExterno() {
        return esExterno;
    }
     
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstudiantePosgrado)) {
            return false;
        }
        EstudiantePosgrado other = (EstudiantePosgrado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.EstudiantePosgrado[id=" + id + "]";
    }
}
