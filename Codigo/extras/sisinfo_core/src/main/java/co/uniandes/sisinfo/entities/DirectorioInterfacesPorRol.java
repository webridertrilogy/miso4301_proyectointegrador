/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import java.io.Serializable;
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
import javax.persistence.Table;

/**
 *
 * @author Asistente
 */
@Entity
@Table(name="DirectorioInterfacesPorRol")
@NamedQueries({
    @NamedQuery(name = "DirectorioInterfacesPorRol.findByRol", query = "SELECT d FROM DirectorioInterfacesPorRol d WHERE d.rol.rol like :rol and d.activo = true")
})
public class DirectorioInterfacesPorRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name= "activo")
    private Boolean activo;

    @Column(name = "direccionInterfaz")
    private String direccionInterfaz;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Rol rol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getDireccionInterfaz() {
        return direccionInterfaz;
    }

    public void setDireccionInterfaz(String direccionInterfaz) {
        this.direccionInterfaz = direccionInterfaz;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
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
        if (!(object instanceof DirectorioInterfacesPorRol)) {
            return false;
        }
        DirectorioInterfacesPorRol other = (DirectorioInterfacesPorRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.DirectorioInterfacesPorRol[id=" + id + "]";
    }

}
