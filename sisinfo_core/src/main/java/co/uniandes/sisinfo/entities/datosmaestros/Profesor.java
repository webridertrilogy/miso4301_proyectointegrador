package co.uniandes.sisinfo.entities.datosmaestros;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Administrador, Marcela Morales
 * Entidad Profesor
 */
@Entity
@Table(name = "profesor")
@NamedQueries({
    @NamedQuery(name = "Profesor.findById", query = "SELECT p FROM Profesor p WHERE p.id = :id"),
    @NamedQuery(name = "Profesor.findByCorreo", query = "SELECT p FROM Profesor p WHERE p.persona.correo = :correo"),
    @NamedQuery(name = "Profesor.findByTipo", query = "SELECT p FROM Profesor p WHERE p.tipo = :tipo AND p.activo= true AND p.persona.activo = true"),
    @NamedQuery(name = "Profesor.findByNivelPlanta", query = "SELECT p FROM Profesor p WHERE p.nivelPlanta.nombre = :nivelPlanta AND p.activo= true AND p.persona.activo = true"),
    @NamedQuery(name = "Profesor.findByALL", query = "SELECT p FROM Profesor p WHERE p.activo= true AND p.persona.activo = true")
})
public class Profesor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "oficina")
    private String oficina;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private GrupoInvestigacion grupoInvestigacion;
    @Column(name="tipo")
    private String tipo;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Persona persona;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private NivelFormacion nivelFormacion;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private NivelPlanta nivelPlanta;
    @Column(name="activo")
    private Boolean activo;

    public GrupoInvestigacion getGrupoInvestigacion() {
        return grupoInvestigacion;
    }

    public void setGrupoInvestigacion(GrupoInvestigacion grupoInvestigacion) {
        this.grupoInvestigacion = grupoInvestigacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public NivelFormacion getNivelFormacion() {
        return nivelFormacion;
    }

    public void setNivelFormacion(NivelFormacion nivelFormacion) {
        this.nivelFormacion = nivelFormacion;
    }

    public NivelPlanta getNivelPlanta() {
        return nivelPlanta;
    }

    public void setNivelPlanta(NivelPlanta nivelPlanta) {
        this.nivelPlanta = nivelPlanta;
    }

      public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
