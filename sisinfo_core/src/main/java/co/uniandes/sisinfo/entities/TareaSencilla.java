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

import co.uniandes.sisinfo.entities.datosmaestros.*;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad Tarea
 */
@Entity
@Table(name="tareaSencilla")
@NamedQueries({
    @NamedQuery(name = "TareaSencilla.findById", query = "SELECT ts FROM TareaSencilla ts WHERE ts.id = :id"),
    @NamedQuery(name = "TareaSencilla.findByTipo", query = "SELECT ts FROM TareaSencilla ts WHERE ts.tipo = :tipo")
})
public class TareaSencilla implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="mensajeCorreo", length=5000 )
    private String mensajeCorreo;

    @Column(name="mensajeDescripcion", length=5000 )
    private String mensajeDescripcion;

    @Column(name="estado")
    private String estado;

    @Column(name="tipo")
    private String tipo;

    @Column(name="ejecucionesPendientes")
    private Integer ejecucionesPendientes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Parametro> parametros;

    public String getMensajeCorreo() {
        return mensajeCorreo;
    }

    public void setMensajeCorreo(String mensajeCorreo) {
        this.mensajeCorreo = mensajeCorreo;
    }

    public String getMensajeDescripcion() {
        return mensajeDescripcion;
    }

    public void setMensajeDescripcion(String mensajeDescripcion) {
        this.mensajeDescripcion = mensajeDescripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Parametro> getParametros() {
        return parametros;
    }

    public void setParametros(Collection<Parametro> parametros) {
        this.parametros = parametros;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getEjecucionesPendientes() {
        return ejecucionesPendientes;
    }

    public void setEjecucionesPendientes(Integer ejecucionesPendientes) {
        this.ejecucionesPendientes = ejecucionesPendientes;
    }
   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TareaSencilla)) {
            return false;
        }
        TareaSencilla other = (TareaSencilla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.TareaMultiple[id=" + id + "]";
    }
}
