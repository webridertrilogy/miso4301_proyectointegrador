/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Juan Manuel Moreno B.
 */
@Entity
@Table(name = "accionVencida")
public class AccionVencida implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Fecha acordada para la accion
     */
    @Column(name = "fechaAcordada")
    private Timestamp fechaAcordada;

    /**
     * Fecha en que se realizo la accion
     */
    @Column(name = "fechaEjecucion")
    private Timestamp fechaEjecucion;

    /**
     * Accion
     */
    @Column(name = "accion")
    private String accion;

    /**
     * Usuario
     */
    @Column(name = "usuario")
    private String usuario;

    /**
     * Proceso
     */
    @Column(name = "proceso")
    private String proceso;

    /**
     * Modulo
     */
    @Column(name = "modulo")
    private String modulo;

    /**
     * Nombre de comando
     */
    @Column(name = "comando")
    private String comando;
    
    /**
     * Informacion adicional
     */
    @Column(name = "infoAdicional")
    private String infoAdicional;

    /* Getter Setter */
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public Timestamp getFechaAcordada() {
        return fechaAcordada;
    }

    public void setFechaAcordada(Timestamp fechaAcordada) {
        this.fechaAcordada = fechaAcordada;
    }

    public Timestamp getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Timestamp fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof AccionVencida)) {
            return false;
        }
        AccionVencida other = (AccionVencida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.AccionVencida[id=" + id + "]";
    }

}
