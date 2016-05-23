package co.uniandes.sisinfo.entities.datosmaestros;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoDocumento;

/**
 * @author Ivan Melo
 * @version 1.0
 * @created 01-mar-2010 08:16:14 a.m.
 */
@Entity
@Table(name="persona")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(name = "Persona.findByCorreo", query = "SELECT p FROM Persona p WHERE p.correo=:correo"),
    @NamedQuery(name = "Persona.findLikeCorreo", query = "SELECT p FROM Persona p WHERE p.correo like :correo"),
    @NamedQuery(name = "Persona.findByNombres", query = "SELECT p FROM Persona p WHERE p.nombres=:nombres"),
    @NamedQuery(name = "Persona.findByApellidos", query = "SELECT p FROM Persona p WHERE p.apellidos=:apellidos"),
     @NamedQuery(name = "Persona.findByCodigo", query = "SELECT p FROM Persona p WHERE p.codigo=:codigo")

})
public class Persona implements Serializable {

    @Id
    @Column(name = "persona_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "celular")
    private String celular;
    @Column(name = "ciudadNacimiento")
    private String ciudadNacimiento;
    @Column(name = "codigo")
    private String codigo;
   @Column(name = "correo", unique = true)
    private String correo;
    @Column(name = "direccionResidencia")
    private String direccionResidencia;
    @Column(name = "extension")
    private String extension;
    @Column(name = "fechaNacimiento")
    private Timestamp fechaNacimiento;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "numeroDocumento")
    private String numDocumentoIdentidad;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "activo")
    private boolean activo;
    @Column(name = "correoAlterno")
    private String correoAlterno;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private TipoDocumento tipoDocumento;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Pais pais;
  

    public Persona() {
    }

    public String getTelefono() {
        return telefono;
    }

    

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCiudadNacimiento() {
        return ciudadNacimiento;
    }

    public void setCiudadNacimiento(String ciudadNacimiento) {
        this.ciudadNacimiento = ciudadNacimiento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoAlterno() {
        return correoAlterno;
    }


    public void setCorreoAlterno(String correoAlterno) {
        this.correoAlterno = correoAlterno;
    }

    public String getDireccionResidencia() {
        return direccionResidencia;
    }

    public void setDireccionResidencia(String direccionResidencia) {
        this.direccionResidencia = direccionResidencia;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Timestamp getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Timestamp fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNumDocumentoIdentidad() {
        return numDocumentoIdentidad;
    }

    public void setNumDocumentoIdentidad(String numDocumentoIdentidad) {
        this.numDocumentoIdentidad = numDocumentoIdentidad;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

   
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Persona[id=" + id + "]";
    }
}
