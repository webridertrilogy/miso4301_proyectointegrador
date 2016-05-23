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
 * Entidad Alerta
 */
@Entity
@Table(name="alertaMultiple")
@NamedQueries({
    @NamedQuery(name = "AlertaMultiple.findById", query = "SELECT am FROM AlertaMultiple am WHERE am.id = :id"),
    @NamedQuery(name = "AlertaMultiple.findByTipo", query = "SELECT am FROM AlertaMultiple am WHERE am.tipo = :tipo")
})
public class AlertaMultiple implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="tipo")
    private String tipo;

    @Column(name="nombre")
    private String nombre;

    @Column(name="descripcion", length = 5000)
    private String descripcion;

    @Column(name="comando")
    private String comando;

    @OneToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Periodicidad periodicidad;

    @Column(name="activa")
    private boolean activa;

    @Column(name="enviaCorreo")
    private boolean enviaCorreo;

    @Column(name="idTimer")
    private Long idTimer;    

    @Column(name="permitePendiente")
    private Boolean permitePendiente;

    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<TareaMultiple> tareas;

    public Long getIdTimer() {
        return idTimer;
    }

    public void setIdTimer(Long idTimer) {
        this.idTimer = idTimer;
    }



    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public Collection<TareaMultiple> getTareas() {
        return tareas;
    }

    public void setTareas(Collection<TareaMultiple> tareas) {
        this.tareas = tareas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isEnviaCorreo() {
        return enviaCorreo;
    }

    public void setEnviaCorreo(boolean enviaCorreo) {
        this.enviaCorreo = enviaCorreo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Periodicidad getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(Periodicidad periodicidad) {
        this.periodicidad = periodicidad;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AlertaMultiple)) {
            return false;
        }
        AlertaMultiple other = (AlertaMultiple) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.AlertaMultiple[id=" + id + "]";
    }

    public Boolean getPermitePendiente() {
        return permitePendiente;
    }

    public void setPermitePendiente(Boolean permitePendiente) {
        this.permitePendiente = permitePendiente;
    }
    
}
