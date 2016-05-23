package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author German Florez, Marcela Morales
 * Entidad para configuración de fechas de conflicto de horario
 */
@Entity(name="configuracionfechasch")
public class ConfiguracionFechasCH implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Timestamp inicioConflictos;
    @Column
    private Timestamp finConflictos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFinConflictos() {
        return finConflictos;
    }

    public void setFinConflictos(Timestamp finConflictos) {
        this.finConflictos = finConflictos;
    }

    public Timestamp getInicioConflictos() {
        return inicioConflictos;
    }

    public void setInicioConflictos(Timestamp inicioConflictos) {
        this.inicioConflictos = inicioConflictos;
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
        if (!(object instanceof ConfiguracionFechasCH)) {
            return false;
        }
        ConfiguracionFechasCH other = (ConfiguracionFechasCH) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ConfiguracionFechasCH[id=" + id + "]";
    }
}
