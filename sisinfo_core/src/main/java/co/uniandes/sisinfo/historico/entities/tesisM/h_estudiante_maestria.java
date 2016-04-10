/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.entities.tesisM;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name = "h_estudiante_maestria")
@NamedQueries({
    @NamedQuery(name = "h_estudiante_maestria.findByCorreoEstudiante", query = "SELECT e FROM h_estudiante_maestria e WHERE correo like :correo"),
    @NamedQuery(name = "h_estudiante_maestria.findByProfesor", query = "SELECT e FROM h_estudiante_maestria e WHERE correoAsesor like :correo")
})
public class h_estudiante_maestria implements Serializable {
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
    @Column(name= "infoT1Perdida")
    private Boolean infoT1Perdida;
    @Column(name= "infoT1Rechazada")
    private Boolean infoT1Rechazada;
    @Column(name= "infoT1Retirada")
    private Boolean infoT1Retirada;
    @Column(name= "infoT1Terminada")
    private Boolean infoT1Terminada;
    @Column(name= "infoT2Perdida")
    private Boolean infoT2Perdida;
    @Column(name= "infoT2Rechazada")
    private Boolean infoT2Rechazada;
    @Column(name= "infoT2Retirada")
    private Boolean infoT2Retirada;
    @Column(name= "infoT2Terminada")
    private Boolean infoT2Terminada;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<h_inscripcion_subarea> inscripcionSubarea;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<h_tesis_1> tesis1;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<h_tesis_2> tesis2;

    public h_estudiante_maestria() {
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

    public Collection<h_inscripcion_subarea> getInscripcionSubarea() {
        return inscripcionSubarea;
    }

    public void setInscripcionSubarea(Collection<h_inscripcion_subarea> inscripcionSubarea) {
        this.inscripcionSubarea = inscripcionSubarea;
    }

    public Collection<h_tesis_1> getTesis1() {
        return tesis1;
    }

    public void setTesis1(Collection<h_tesis_1> tesis1) {
        this.tesis1 = tesis1;
    }

    public Collection<h_tesis_2> getTesis2() {
        return tesis2;
    }

    public void setTesis2(Collection<h_tesis_2> tesis2) {
        this.tesis2 = tesis2;
    }

    public Boolean getInfoT1Perdida() {
        return infoT1Perdida;
    }

    public void setInfoT1Perdida(Boolean infoT1Perdida) {
        this.infoT1Perdida = infoT1Perdida;
    }

    public Boolean getInfoT1Rechazada() {
        return infoT1Rechazada;
    }

    public void setInfoT1Rechazada(Boolean infoT1Rechazada) {
        this.infoT1Rechazada = infoT1Rechazada;
    }

    public Boolean getInfoT1Retirada() {
        return infoT1Retirada;
    }

    public void setInfoT1Retirada(Boolean infoT1Retirada) {
        this.infoT1Retirada = infoT1Retirada;
    }

    public Boolean getInfoT1Terminada() {
        return infoT1Terminada;
    }

    public void setInfoT1Terminada(Boolean infoT1Terminada) {
        this.infoT1Terminada = infoT1Terminada;
    }

    public Boolean getInfoT2Perdida() {
        return infoT2Perdida;
    }

    public void setInfoT2Perdida(Boolean infoT2Perdida) {
        this.infoT2Perdida = infoT2Perdida;
    }

    public Boolean getInfoT2Rechazada() {
        return infoT2Rechazada;
    }

    public void setInfoT2Rechazada(Boolean infoT2Rechazada) {
        this.infoT2Rechazada = infoT2Rechazada;
    }

    public Boolean getInfoT2Retirada() {
        return infoT2Retirada;
    }

    public void setInfoT2Retirada(Boolean infoT2Retirada) {
        this.infoT2Retirada = infoT2Retirada;
    }

    public Boolean getInfoT2Terminada() {
        return infoT2Terminada;
    }

    public void setInfoT2Terminada(Boolean infoT2Terminada) {
        this.infoT2Terminada = infoT2Terminada;
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
        if (!(object instanceof h_estudiante_maestria)) {
            return false;
        }
        h_estudiante_maestria other = (h_estudiante_maestria) object;
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
