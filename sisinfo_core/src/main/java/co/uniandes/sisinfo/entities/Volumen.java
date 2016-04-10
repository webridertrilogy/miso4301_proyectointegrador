/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name="volumen")
public class Volumen implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "volumen")
    private int volumen;

    @Column(name = "numeroVolumen")
    private double numeroVolumen;
    
    @Column(name = "numeroEjemplares")
    private int numeroEjemplares;

    public Long getId() {
        return id;
    }

    public int getNumeroEjemplares() {
        return numeroEjemplares;
    }

    public double getNumeroVolumen() {
        return numeroVolumen;
    }

    public int getVolumen() {
        return volumen;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeroEjemplares(int numeroEjemplares) {
        this.numeroEjemplares = numeroEjemplares;
    }

    public void setNumeroVolumen(double numeroVolumen) {
        this.numeroVolumen = numeroVolumen;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Volumen)) {
            return false;
        }
        Volumen other = (Volumen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Volumen[id=" + id + "]";
    }

}
