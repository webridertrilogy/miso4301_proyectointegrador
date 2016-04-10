/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import java.io.Serializable;
import java.sql.Date;
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
 * Entidad Solicitud de Material Bibliográfico
 */
@Entity
@Table(name = "solicitudMaterialBibliografico")
@NamedQueries({
    @NamedQuery(name = "SolicitudMaterialBibliografico.findById", query = "SELECT s FROM SolicitudMaterialBibliografico s WHERE s.id = :id"),
    @NamedQuery(name = "SolicitudMaterialBibliografico.findPrecioPromedioByRangoFecha", query = "SELECT AVG(m.precio) FROM SolicitudMaterialBibliografico s JOIN s.materialBibliografico m WHERE s.fechaSolicitud >= :fechaInicio AND s.fechaSolicitud <= :fechaFin"),
    @NamedQuery(name = "SolicitudMaterialBibliografico.findPrecioTotalByRangoFecha", query = "SELECT SUM(m.precio) FROM SolicitudMaterialBibliografico s JOIN s.materialBibliografico m WHERE s.fechaSolicitud >= :fechaInicio AND s.fechaSolicitud <= :fechaFin"),
    @NamedQuery(name = "SolicitudMaterialBibliografico.findyBySolicitante", query = "SELECT s FROM SolicitudMaterialBibliografico s JOIN s.profesor p WHERE p.persona.correo = :correo"),
    @NamedQuery(name = "SolicitudMaterialBibliografico.findyByEstado", query = "SELECT s FROM SolicitudMaterialBibliografico s WHERE s.estado = :estado"),
    @NamedQuery(name = "SolicitudMaterialBibliografico.findByRangoFecha", query = "SELECT s FROM SolicitudMaterialBibliografico s WHERE s.fechaSolicitud = :fechaInicio AND s.fechaSolicitud <= :fechaFin"),
    @NamedQuery(name = "SolicitudMaterialBibliografico.findByAnioPublicacion", query = "SELECT s FROM SolicitudMaterialBibliografico s JOIN s.materialBibliografico m WHERE m.anio = :anio"),
    @NamedQuery(name = "SolicitudMaterialBibliografico.findyByProveedor", query = "SELECT s FROM SolicitudMaterialBibliografico s JOIN s.materialBibliografico m WHERE m.proveedor = :proveedor"),
    @NamedQuery(name = "SolicitudMaterialBibliografico.findByRangoPrecio", query = "SELECT s FROM SolicitudMaterialBibliografico s JOIN s.materialBibliografico m WHERE m.precio >= :precioInicio AND m.precio <= :precioFin")
})                                                                          
public class SolicitudMaterialBibliografico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "fechaSolicitud")
    private Date fechaSolicitud;
    @Column(name = "fechaModificacionDirector")
    private Date fechaModificacionDirector;
    @Column(name = "fechaEnvioABibliotecaid")
    private Date fechaEnvioABiblioteca;
    @Column(name = "estado")
    private String estado;
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private MaterialBibliografico materialBibliografico;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY, cascade = javax.persistence.CascadeType.MERGE)
    private Profesor profesor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaEnvioABiblioteca() {
        return fechaEnvioABiblioteca;
    }

    public void setFechaEnvioABiblioteca(Date fechaEnvioABiblioteca) {
        this.fechaEnvioABiblioteca = fechaEnvioABiblioteca;
    }

    public Date getFechaModificacionDirector() {
        return fechaModificacionDirector;
    }

    public void setFechaModificacionDirector(Date fechaModificacionDirector) {
        this.fechaModificacionDirector = fechaModificacionDirector;
    }

    public Date getFechaSolicitudProfesor() {
        return fechaSolicitud;
    }

    public void setFechaSolicitudProfesor(Date fechaSolicitudProfesor) {
        this.fechaSolicitud = fechaSolicitudProfesor;
    }

    public MaterialBibliografico getMaterialBibliografico() {
        return materialBibliografico;
    }

    public void setMaterialBibliografico(MaterialBibliografico materialBibliografico) {
        this.materialBibliografico = materialBibliografico;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
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
        if (!(object instanceof SolicitudMaterialBibliografico)) {
            return false;
        }
        SolicitudMaterialBibliografico other = (SolicitudMaterialBibliografico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.SolicitudMaterialBibliografico[id=" + id + "]";
    }

}
