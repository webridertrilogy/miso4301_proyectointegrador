/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author david
 */
@Entity
@NamedQueries({@NamedQuery (name="h_monitor.findByCorreo",query="Select h from h_monitor h where h.correo=:correo")})
public class h_monitor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String apellidos;

    private String codigo;

    private String correo;

    private String nombre;

    private String documentoIdentidad;

    private String programaFacultad;

    private String tipoDocumento;

    @OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
    private Collection<h_informacionAcademica> informacionAcademica;

    public Collection<h_informacionAcademica> getInformacionAcademica() {
        return informacionAcademica;
    }

    public void setInformacionAcademica(Collection<h_informacionAcademica> informacionAcademica) {
        this.informacionAcademica = informacionAcademica;
    }

    public Collection<h_monitoria> getMonitorias() {
        return monitorias;
    }

    public void setMonitorias(Collection<h_monitoria> monitorias) {
        this.monitorias = monitorias;
    }

    @OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
    private Collection<h_monitoria> monitorias;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProgramaFacultad() {
        return programaFacultad;
    }

    public void setProgramaFacultad(String programaFacultad) {
        this.programaFacultad = programaFacultad;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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
        if (!(object instanceof h_monitor)) {
            return false;
        }
        h_monitor other = (h_monitor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.h_monitor[id=" + id + "]";
    }

}
