/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities.datosmaestros;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidad Franja
 */
@Entity
@Table(name = "franja_libre")
public class Franja implements Serializable {

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
    @Column(name = "hora_inicio")
    private int hora_inicio;
    @Column(name = "minuto_inicio")
    private int minuto_inicio;
    @Column(name = "hora_fin")
    private int hora_fin;
    @Column(name = "minuto_fin")
    private int minuto_fin;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(int hora_fin) {
        this.hora_fin = hora_fin;
    }

    public int getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(int hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public int getMinuto_fin() {
        return minuto_fin;
    }

    public void setMinuto_fin(int minuto_fin) {
        this.minuto_fin = minuto_fin;
    }

    public int getMinuto_inicio() {
        return minuto_inicio;
    }

    public void setMinuto_inicio(int minuto_inicio) {
        this.minuto_inicio = minuto_inicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Franja)) {
            return false;
        }
        Franja other = (Franja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Franja_Libre[id=" + id + "]";
    }
}