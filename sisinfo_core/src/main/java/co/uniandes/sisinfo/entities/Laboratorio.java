package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entidad que representa a un Laboratorio
 * @author Marcela Morales
 */
@Entity
@Table(name = "Laboratorio")
@NamedQueries({
    @NamedQuery(name = "Laboratorio.findAll", query = "SELECT l FROM Laboratorio l"),
    @NamedQuery(name = "Laboratorio.findById", query = "SELECT l FROM Laboratorio l WHERE l.id = :id"),
    @NamedQuery(name = "Laboratorio.findLaboratoriosAutorizados", query = "SELECT l FROM Laboratorio l LEFT JOIN l.autorizados aut WHERE aut.persona.correo = :correo and l.activo = true"),
    @NamedQuery(name = "Laboratorio.findLaboratorioPorNombre", query = "SELECT l FROM Laboratorio l WHERE l.nombre = :nombre and l.activo = true"),
    @NamedQuery(name = "Laboratorio.findLaboratoriosEncargado", query = "SELECT l FROM Laboratorio l LEFT JOIN l.encargados enc WHERE enc.persona.correo = :correo and l.activo = true"),
    @NamedQuery(name = "Laboratorio.findLaboratoriosActivos", query = "SELECT l FROM Laboratorio l WHERE l.activo = true "),
    @NamedQuery(name = "Laboratorio.findLaboratoriosActivosYReservables", query = "SELECT l FROM Laboratorio l WHERE l.activo = true AND l.reservable = true")
})
public class Laboratorio implements Serializable {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion",length=3000)
    private String descripcion;
    @Column(name = "salon")
    private String salon;

    @Column(name = "nombreSalaServicio")
    private String nombreSalaServicio;

    @Column(name = "reservable")
    private Boolean reservable;
    @Column(name = "cuentaInvitado")
    private Boolean cuentaInvitado;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<Elemento> elementos;
    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<HorarioDia> horario;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH})
    private Collection<Encargado> encargados;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
    private Collection<Autorizado> autorizados;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Laboratorio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getReservable() {
        return reservable;
    }

    public void setReservable(Boolean reservable) {
        this.reservable = reservable;
    }

    public Boolean getCuentaInvitado() {
        return cuentaInvitado;
    }

    public void setCuentaInvitado(Boolean cuentaInvitado) {
        this.cuentaInvitado = cuentaInvitado;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    public String getNombreSalaServicio() {
        return nombreSalaServicio;
    }

    public void setNombreSalaServicio(String nombreSalaServicio) {
        this.nombreSalaServicio = nombreSalaServicio;
    }

   

    public Collection<Autorizado> getAutorizados() {
        return autorizados;
    }

    public void setAutorizados(Collection<Autorizado> autorizados) {
        this.autorizados = autorizados;
    }

    public Collection<Elemento> getElementos() {
        return elementos;
    }

    public void setElementos(Collection<Elemento> elementos) {
        this.elementos = elementos;
    }

    public Collection<Encargado> getEncargados() {
        return encargados;
    }

    public void setEncargados(Collection<Encargado> encargados) {
        this.encargados = encargados;
    }

    public Collection<HorarioDia> getHorario() {
        return horario;
    }

    public void setHorario(Collection<HorarioDia> horario) {
        this.horario = horario;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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
        if (!(object instanceof Laboratorio)) {
            return false;
        }
        Laboratorio other = (Laboratorio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Laboratorio[id=" + id + "]";
    }
}
