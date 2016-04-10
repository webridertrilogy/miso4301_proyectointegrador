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

import co.uniandes.sisinfo.entities.datosmaestros.*;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad Tarea
 */
@Entity
@Table(name="tareaMultiple")
@NamedQueries({
    @NamedQuery(name = "TareaMultiple.findById", query = "SELECT tm FROM TareaMultiple tm WHERE tm.id = :id"),
    @NamedQuery(name = "TareaMultiple.findByTipo", query = "SELECT tm FROM TareaMultiple tm WHERE tm.tipo = :tipo"),
    @NamedQuery(name = "TareaMultiple.findByRolYTipo", query = "SELECT tm FROM TareaMultiple tm WHERE tm.tipo = :tipo AND tm.rol.rol = :rol"),
    @NamedQuery(name = "TareaMultiple.findByRolYTipoNoPersonal", query = "SELECT tm FROM TareaMultiple tm WHERE tm.tipo = :tipo AND tm.rol.rol = :rol AND tm.personal = false"),
    @NamedQuery(name = "TareaMultiple.findByCorreoYTipo", query = "SELECT tm FROM TareaMultiple tm WHERE tm.tipo = :tipo AND tm.persona.correo = :correo"),
    @NamedQuery(name = "TareaMultiple.findByRol", query = "SELECT tm FROM TareaMultiple tm WHERE tm.rol.rol = :rol"),
    @NamedQuery(name = "TareaMultiple.findByCorreo", query = "SELECT tm FROM TareaMultiple tm WHERE tm.persona.correo = :correo"),
    @NamedQuery(name = "TareaMultiple.findByRolNoPersonal", query = "SELECT tm FROM TareaMultiple tm WHERE tm.rol.rol = :rol and tm.personal = false"),
    @NamedQuery(name = "TareaMultiple.findByCorreoYFechaAntesDeCaducacion", query = "SELECT tm FROM TareaMultiple tm WHERE tm.persona.correo = :correo AND tm.fechaCaducacion >= :fecha AND (tm.rol.rol = :rol OR tm.rol.rol = 'todos') AND tm.tipo IN (SELECT am.tipo FROM AlertaMultiple am WHERE am.activa = true)"),
    @NamedQuery(name = "TareaMultiple.findByRolNoPersonalYFechaAntesDeCaducacion", query = "SELECT tm FROM TareaMultiple tm WHERE tm.personal = false AND tm.fechaCaducacion >= :fecha AND tm.rol.rol = :rol AND tm.tipo IN (SELECT am.tipo FROM AlertaMultiple am WHERE am.activa = true)"),
    @NamedQuery(name = "TareaMultiple.findByEstadoTareaSencilla", query = "SELECT tm FROM TareaMultiple tm LEFT JOIN tm.tareasSencillas ts WHERE ts.estado = :estado"),
    @NamedQuery(name = "TareaMultiple.findByCorreoAndRolAndEstadoTareaSencilla", query = "SELECT distinct tm FROM TareaMultiple tm LEFT JOIN tm.tareasSencillas ts WHERE tm.persona.correo = :correo AND ts.estado = :estado AND tm.rol.rol = :rol"),
    @NamedQuery(name = "TareaMultiple.findByRolAndEstadoTareaSencillaNoPersonal", query = "SELECT distinct tm FROM TareaMultiple tm LEFT JOIN tm.tareasSencillas ts WHERE tm.rol.rol = :rol AND tm.personal = false AND ts.estado = :estado")
})
public class TareaMultiple implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="header", length = 5000)
    private String header;

    @Column(name="footer", length = 5000)
    private String footer;
    
    @Column(name="fechaInicio")
    private Timestamp fechaInicio;

    @Column(name="fechaCaducacion")
    private Timestamp fechaCaducacion;

    @Column(name="personal")
    private boolean personal;

    @Column(name="tipo")
    private String tipo;

    @Column(name="comando")
    private String comando;

    @Column(name="asunto")
    private String asunto;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Persona persona;

    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Rol rol;

    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<TareaSencilla> tareasSencillas;

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    

    public Timestamp getFechaCaducacion() {
        return fechaCaducacion;
    }

    public void setFechaCaducacion(Timestamp fechaCaducacion) {
        this.fechaCaducacion = fechaCaducacion;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public boolean isPersonal() {
        return personal;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Collection<TareaSencilla> getTareasSencillas() {
        return tareasSencillas;
    }

    public void setTareasSencillas(Collection<TareaSencilla> tareasSencillas) {
        this.tareasSencillas = tareasSencillas;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TareaMultiple)) {
            return false;
        }
        TareaMultiple other = (TareaMultiple) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.TareaMultiple[id=" + id + "]";
    }
}
