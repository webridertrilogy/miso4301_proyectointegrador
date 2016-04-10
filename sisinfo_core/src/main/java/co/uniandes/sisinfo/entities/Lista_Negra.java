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
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidad Lista Negra
 */
@Entity
@Table(name = "lista_negra")
@NamedQueries({
    @NamedQuery(name = "Lista_Negra.findAspiranteByCodigo", query = "SELECT l from Lista_Negra l  WHERE l.codigo =:codigo"),
    @NamedQuery(name = "Lista_Negra.findAspiranteByCorreo", query = "SELECT l from Lista_Negra l  WHERE l.login =:correo"),
    @NamedQuery(name = "Lista_Negra.findAspirantesTemporales", query = "SELECT l from Lista_Negra l  WHERE l.temporal = true")
})
public class Lista_Negra implements Serializable {

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
    @Column(name = "login")
    private String login;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "fechaIngreso")
    private Date fechaIngreso;
    @Column(name = "razonIngreso")
    private String razonIngreso;
    @Column(name = "temporal")
    private boolean temporal;

    //---------------------------------------
    // Métodos
    //---------------------------------------

    public boolean isTemporal() {
        return temporal;
    }

    public void setTemporal(boolean temporal) {
        this.temporal = temporal;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonIngreso() {
        return razonIngreso;
    }

    public void setRazonIngreso(String razonIngreso) {
        this.razonIngreso = razonIngreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Lista_Negra)) {
            return false;
        }
        Lista_Negra other = (Lista_Negra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Lista_Negra[id=" + id + "]";
    }
}
