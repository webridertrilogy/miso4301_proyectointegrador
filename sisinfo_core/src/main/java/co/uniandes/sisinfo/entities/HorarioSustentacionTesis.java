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
 * @author Ivan Melo
 */
@Entity
@Table(name = "horario_sustentacion_tesis")
public class HorarioSustentacionTesis implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fechaSustentacion")
    private Timestamp fechaSustentacion;

    @Column(name = "videoConferencia")
    private String videoConferencia;

    @Column(name = "videoConferenciaSkyPe")
    private String videoConferenciaSkyPe;

     @Column(name = "salonSustentacion")
    private String salonSustentacion;

    public HorarioSustentacionTesis() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFechaSustentacion() {
        return fechaSustentacion;
    }

    public void setFechaSustentacion(Timestamp fechaSustentacion) {
        this.fechaSustentacion = fechaSustentacion;
    }

    public String getSalonSustentacion() {
        return salonSustentacion;
    }

    public void setSalonSustentacion(String salonSustentacion) {
        this.salonSustentacion = salonSustentacion;
    }

    public String getVideoConferencia() {
        return videoConferencia;
    }

    public void setVideoConferencia(String videoConferencia) {
        this.videoConferencia = videoConferencia;
    }

    public String getVideoConferenciaSkyPe() {
        return videoConferenciaSkyPe;
    }

    public void setVideoConferenciaSkyPe(String videoConferenciaSkyPe) {
        this.videoConferenciaSkyPe = videoConferenciaSkyPe;
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
        if (!(object instanceof HorarioSustentacionTesis)) {
            return false;
        }
        HorarioSustentacionTesis other = (HorarioSustentacionTesis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.HorarioSustentacionTesis[id=" + id + "]";
    }

}
