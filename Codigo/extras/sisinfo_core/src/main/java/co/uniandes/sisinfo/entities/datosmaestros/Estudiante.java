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

import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoCuenta;

/**
 * @author Administrador
 * @version 1.0
 * @created 01-mar-2010 08:16:12 a.m.
 */
@Entity
@Table(name = "estudiante")
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT a FROM Estudiante a  WHERE a.activo= true AND a.persona.activo = true"),
    @NamedQuery(name = "Estudiante.findByTipoEstudiante", query = "SELECT a FROM Estudiante a WHERE a.tipoEstudiante.nombre = :tipo AND a.activo = true AND a.persona.activo = true"),
    @NamedQuery(name = "Estudiante.findById", query = "SELECT a FROM Estudiante a WHERE a.id = :id"),
    @NamedQuery(name = "Estudiante.findByCodigo", query = "SELECT a FROM Estudiante a WHERE a.persona.codigo = :codigo"),
    @NamedQuery(name = "Estudiante.findByCorreo", query = "SELECT a FROM Estudiante a WHERE a.persona.correo = :correo"),
    @NamedQuery(name = "Estudiante.findAllStudentsByApellidos", query = "SELECT a FROM Estudiante a WHERE a.persona.apellidos LIKE :apellidos"),
    @NamedQuery(name = "Estudiante.findAllStudentsByNombres", query = "SELECT a FROM Estudiante a WHERE a.persona.nombres LIKE :nombres")})
public class Estudiante implements Serializable {

    @Id
    @Column(name = "estudiante_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "avisarEmergenciaApellidos")
    private String avisarEmergenciaApellidos;
    @Column(name = "avisarEmergenciaNombres")
    private String avisarEmergenciaNombres;
    @Column(name = "banco")
    private String banco;
    @Column(name = "cuentaBancaria")
    private String cuentaBancaria;
    @Column(name = "telefonoEmergencia")
    private String telefonoEmergencia;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private TipoCuenta tipoCuenta;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Persona persona;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private InformacionAcademica informacion_Academica;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Programa programa;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Programa doblePrograma;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private NivelFormacion tipoEstudiante;
    @Column(name = "activo")
    private Boolean activo;



    public Estudiante() {
    }

    public String getAvisarEmergenciaApellidos() {
        return avisarEmergenciaApellidos;
    }

    public void setAvisarEmergenciaApellidos(String avisarEmergenciaApellidos) {
        this.avisarEmergenciaApellidos = avisarEmergenciaApellidos;
    }

    public String getAvisarEmergenciaNombres() {
        return avisarEmergenciaNombres;
    }

    public void setAvisarEmergenciaNombres(String avisarEmergenciaNombres) {
        this.avisarEmergenciaNombres = avisarEmergenciaNombres;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public InformacionAcademica getInformacion_Academica() {
        return informacion_Academica;
    }

    public void setInformacion_Academica(InformacionAcademica informacion_Academica) {
        this.informacion_Academica = informacion_Academica;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NivelFormacion getTipoEstudiante() {
        return tipoEstudiante;
    }

    public void setTipoEstudiante(NivelFormacion tipoEstudiante) {
        this.tipoEstudiante = tipoEstudiante;
    }

    public Programa getDoblePrograma() {
        return doblePrograma;
    }

    public void setDoblePrograma(Programa doblePrograma) {
        this.doblePrograma = doblePrograma;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    
}//end Estudiante

