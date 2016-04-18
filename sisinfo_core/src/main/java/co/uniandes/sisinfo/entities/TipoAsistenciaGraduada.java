package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entidad que representa a un tipo de asistencia graduada
 * @author Marcela Morales
 */
@Entity
@Table(name = "TipoAsistenciaGraduada")
@NamedQueries({
    @NamedQuery(name = "TipoAsistenciaGraduada.findById", query = "SELECT a FROM TipoAsistenciaGraduada a WHERE a.id=:id"),
    @NamedQuery(name = "TipoAsistenciaGraduada.findByTipo", query = "SELECT a FROM TipoAsistenciaGraduada a WHERE a.tipo=:tipo")
})
public class TipoAsistenciaGraduada implements Serializable {

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
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Collection<TipoAsistenciaGraduada> subtipos;
    @Column(name = "infoRequerida")
    private String infoRequerida;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<TipoAsistenciaGraduada> getSubtipos() {
        return subtipos;
    }

    public void setSubtipos(Collection<TipoAsistenciaGraduada> subtipos) {
        this.subtipos = subtipos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInfoRequerida() {
        return infoRequerida;
    }

    public void setInfoRequerida(String infoRequerida) {
        this.infoRequerida = infoRequerida;
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
        if (!(object instanceof TipoAsistenciaGraduada)) {
            return false;
        }
        TipoAsistenciaGraduada other = (TipoAsistenciaGraduada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.TipoAsistenciaGraduada[id=" + id + "]";
    }
}
