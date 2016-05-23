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
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Entidad Parámetro
 */
@Entity
@Table(name="timerGenerico")
@NamedQueries({
    @NamedQuery(name = "TimerGenerico.findById", query = "SELECT a FROM TimerGenerico a WHERE a.id = :id "),
    @NamedQuery(name = "TimerGenerico.findByNombreMetodoALlamar", query = "SELECT a FROM TimerGenerico a WHERE a.nombreMetodoALLamar = :nombreMetodo"),
    @NamedQuery(name = "TimerGenerico.findByParametroExterno", query = "SELECT a FROM TimerGenerico a WHERE a.infoTimer = :infoTimer"),
    @NamedQuery(name = "TimerGenerico.findByTodosLosParametros", query = "SELECT a FROM TimerGenerico a WHERE a.nombreMetodoALLamar = :nombreMetodo AND a.direccionInterfaz = :direccionInterfaz AND a.infoTimer = :infoTimer AND  a.fechaFin = :FechaFin"),
    @NamedQuery(name = "TimerGenerico.findByDireccionNombreYParametro", query = "SELECT a FROM TimerGenerico a WHERE a.direccionInterfaz = :direccionInterfaz AND a.nombreMetodoALLamar = :nombreMetodoALLamar AND a.infoTimer = :infoTimer"),
    @NamedQuery(name = "TimerGenerico.findByDireccionInterfaz", query = "SELECT a FROM TimerGenerico a WHERE a.direccionInterfaz = :direccionInterfaz")
})
public class TimerGenerico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="direccionInterfaz")
    private String direccionInterfaz;

    @Column(name="nombreMetodoALlamar")
    private String nombreMetodoALLamar;

    @Column(name="FechaFin")
    private Timestamp fechaFin;

    @Column(name="infoTimer")
    private String infoTimer;

    @Column(name="moduloQuienLoCrea")
    private String moduloQuienLoCrea;

    @Column(name="beanQuienLoCrea")
    private String beanQuienLoCrea;

    @Column(name="metodoQuienLoCrea")
    private String metodoQuienLoCrea;
    
    @Column(name="fechaCreacion")
    private Timestamp fechaCreacion;

    @Column(name="descripcionTimer")
    private String descripcionTimer;


    public TimerGenerico() {
    }

    public TimerGenerico(String direccionInterfaz, String nombreMetodoALLamar, Timestamp fechaFin, String infoTimer,
                         String moduloQuienLoCrea, String beanQuienLoCrea, String metodoQuienLoCrea, Timestamp fechaCreacion, String descripcionTimer) {
        this.direccionInterfaz = direccionInterfaz;
        this.nombreMetodoALLamar = nombreMetodoALLamar;
        this.fechaFin = fechaFin;
        this.infoTimer = infoTimer;
        
        this.moduloQuienLoCrea = moduloQuienLoCrea;
        this.beanQuienLoCrea = beanQuienLoCrea;
        this.metodoQuienLoCrea = metodoQuienLoCrea;
        this.fechaCreacion = fechaCreacion;
        this.descripcionTimer = descripcionTimer;
    }

   
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccionInterfaz() {
        return direccionInterfaz;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public String getInfoTimer() {
        return infoTimer;
    }

    public String getNombreMetodoALLamar() {
        return nombreMetodoALLamar;
    }

    public void setDireccionInterfaz(String direccionInterfaz) {
        this.direccionInterfaz = direccionInterfaz;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setInfoTimer(String infoTimer) {
        this.infoTimer = infoTimer;
    }

    public void setNombreMetodoALLamar(String nombreMetodoALLamar) {
        this.nombreMetodoALLamar = nombreMetodoALLamar;
    }

    public String getBeanQuienLoCrea() {
        return beanQuienLoCrea;
    }

    public void setBeanQuienLoCrea(String beanQuienLoCrea) {
        this.beanQuienLoCrea = beanQuienLoCrea;
    }

    public String getDescripcionTimer() {
        return descripcionTimer;
    }

    public void setDescripcionTimer(String descripcionTimer) {
        this.descripcionTimer = descripcionTimer;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getMetodoQuienLoCrea() {
        return metodoQuienLoCrea;
    }

    public void setMetodoQuienLoCrea(String metodoQuienLoCrea) {
        this.metodoQuienLoCrea = metodoQuienLoCrea;
    }

    public String getModuloQuienLoCrea() {
        return moduloQuienLoCrea;
    }

    public void setModuloQuienLoCrea(String moduloQuienLoCrea) {
        this.moduloQuienLoCrea = moduloQuienLoCrea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TimerGenerico)) {
            return false;
        }
        TimerGenerico other = (TimerGenerico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Parametro[id=" + id + "]";
    }
}
