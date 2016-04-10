/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities.tesispregrado;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "h_estudiante_pregrado")
@NamedQueries({
    @NamedQuery(name = "h_estudiante_pregrado.findByCorreoEstudiante", query = "SELECT e FROM h_estudiante_pregrado e WHERE correo like :correo"),
    @NamedQuery(name = "h_estudiante_pregrado.findByCorreoProfesor", query = "SELECT e FROM h_estudiante_pregrado e WHERE correoAsesor like :correo")
})
public class h_estudiante_pregrado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name= "nombres")
    private String nombres;
    @Column(name= "apellidos")
    private String apellidos;
    @Column(name= "correo")
    private String correo;
    @Column(name= "codigo")
    private String codigo;
    @Column(name= "nombresAsesor")
    private String nombresAsesor;
    @Column(name= "apellidosAsesor")
    private String apellidosAsesor;
    @Column(name= "correoAsesor")
    private String correoAsesor;
    @Column(name= "infoPerdida")
    private Boolean infoPerdida;
    @Column(name= "infoRechazada")
    private Boolean infoRechazada;
    @Column(name= "infoRetirada")
    private Boolean infoRetirada;
    @Column(name= "infoTerminada")
    private Boolean infoTerminada;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<h_tesisPregrado> tesisPregrado;

    public h_estudiante_pregrado() {
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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidosAsesor() {
        return apellidosAsesor;
    }

    public void setApellidosAsesor(String apellidosAsesor) {
        this.apellidosAsesor = apellidosAsesor;
    }

    public String getCorreoAsesor() {
        return correoAsesor;
    }

    public void setCorreoAsesor(String correoAsesor) {
        this.correoAsesor = correoAsesor;
    }

    public String getNombresAsesor() {
        return nombresAsesor;
    }

    public void setNombresAsesor(String nombresAsesor) {
        this.nombresAsesor = nombresAsesor;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInfoPerdida() {
        return infoPerdida;
    }

    public void setInfoPerdida(Boolean infoPerdida) {
        this.infoPerdida = infoPerdida;
    }

    public Boolean getInfoRechazada() {
        return infoRechazada;
    }

    public void setInfoRechazada(Boolean infoRechazada) {
        this.infoRechazada = infoRechazada;
    }

    public Boolean getInfoRetirada() {
        return infoRetirada;
    }

    public void setInfoRetirada(Boolean infoRetirada) {
        this.infoRetirada = infoRetirada;
    }

    public Boolean getInfoTerminada() {
        return infoTerminada;
    }

    public void setInfoTerminada(Boolean infoTerminada) {
        this.infoTerminada = infoTerminada;
    }

    public Collection<h_tesisPregrado> getTesisPregrado() {
        return tesisPregrado;
    }

    public void setTesisPregrado(Collection<h_tesisPregrado> tesisPregrado) {
        this.tesisPregrado = tesisPregrado;
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
        if (!(object instanceof h_estudiante_pregrado)) {
            return false;
        }
        h_estudiante_pregrado other = (h_estudiante_pregrado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.historico.entities.tesisM.h_estudiante_maestria[id=" + id + "]";
    }

}
