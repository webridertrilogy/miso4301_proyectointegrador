package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entidad que representa a un rango de fechas
 * @author Marcela Morales
 */
@Entity
@Table(name = "RangoFechasGeneral")
@NamedQueries({
    @NamedQuery(name = "RangoFechasGeneral.findById", query = "SELECT a FROM RangoFechasGeneral a WHERE id=:id"),
    @NamedQuery(name = "RangoFechasGeneral.findByNombre", query = "SELECT a FROM RangoFechasGeneral a WHERE nombre=:nombre")
})
public class RangoFechasGeneral implements Serializable {

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
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fechaInicial")
    private Timestamp fechaInicial;
    @Column(name = "fechaFinal")
    private Timestamp fechaFinal;
    @Column(name = "idTimerInicial")
    private Long idTimerInicial;
    @Column(name = "idTimerFinal")
    private Long idTimerFinal;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Timestamp fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Timestamp getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Timestamp fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Long getIdTimerFinal() {
        return idTimerFinal;
    }

    public void setIdTimerFinal(Long idTimerFinal) {
        this.idTimerFinal = idTimerFinal;
    }

    public Long getIdTimerInicial() {
        return idTimerInicial;
    }

    public void setIdTimerInicial(Long idTimerInicial) {
        this.idTimerInicial = idTimerInicial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof RangoFechasGeneral)) {
            return false;
        }
        RangoFechasGeneral other = (RangoFechasGeneral) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.RangoFechasGeneral[id=" + id + "]";
    }
}
