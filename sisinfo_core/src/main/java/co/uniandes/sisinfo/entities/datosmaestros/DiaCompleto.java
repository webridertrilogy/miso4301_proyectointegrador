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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "dia_completo")
@NamedQueries({
    @NamedQuery(name = "DiaCompleto.findAll", query = "SELECT d FROM DiaCompleto d"),
    @NamedQuery(name = "DiaCompleto.findById", query = "SELECT d FROM DiaCompleto d WHERE d.id = :id")
})
public class DiaCompleto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "dia_semana")
    private String dia_semana;
    @Column(name = "numero_dia")
    private int numero_dia;
    @Column(name = "horas", length=48)
    private String horas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public int getNumero_dia() {
        return numero_dia;
    }

    public void setNumero_dia(int numero_dia) {
        this.numero_dia = numero_dia;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
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
        if (!(object instanceof DiaCompleto)) {
            return false;
        }
        DiaCompleto other = (DiaCompleto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto[id=" + id + "]";
    }

}
