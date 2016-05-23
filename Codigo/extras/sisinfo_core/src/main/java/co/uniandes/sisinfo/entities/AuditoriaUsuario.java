/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Paola Gómez
 */
/*
 *
    @NamedQuery(name = "CorreoAuditoria.findByDestinatariosYFecha", query = "SELECT c FROM CorreoAuditoria c WHERE c.destinatarios like :correo OR c.destinatariosCC like :correo OR c.destinatariosCCO like :correo AND c.fechaEnvio >= :fechaInicio AND c.fechaEnvio <= :fechaFin"),
    @NamedQuery(name = "CorreoAuditoria.findByDestinatariosYAsunto", query = "SELECT c FROM CorreoAuditoria c WHERE c.destinatarios like :correo OR c.destinatariosCC like :correo OR c.destinatariosCCO like :correo AND c.asunto like :asunto"),
    @NamedQuery(name = "CorreoAuditoria.findByAsuntoYFecha", query = "SELECT c FROM CorreoAuditoria c WHERE c.asunto like :asunto AND c.fechaEnvio >= :fechaInicio AND c.fechaEnvio <= :fechaFin")
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "AuditoriaUsuario.findByUsuario", query = "SELECT c FROM AuditoriaUsuario c WHERE c.usuario like :correo"),
    @NamedQuery(name = "AuditoriaUsuario.findByFecha", query = "SELECT c FROM AuditoriaUsuario c WHERE c.fecha >= :fechaInicio AND c.fecha <= :fechaFin"),
    @NamedQuery(name = "AuditoriaUsuario.findByRol", query = "SELECT c FROM AuditoriaUsuario c WHERE c.rol like :rol")
})
@Table(name = "AuditoriaUsuario")
public class AuditoriaUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="usuario")
    private String usuario;

    @Column(name="rol")
    private String rol;

    @Column(name="fecha")
    private Timestamp fecha;

    @Column(name="comando")
    private String comando;
    
    @Column(name="parametros", length = 2000)
    private String parametros;

    @Column(name="accionExitosa")
    private Boolean accionExitosa;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public Boolean getAccionExitosa() {
        return accionExitosa;
    }

    public void setAccionExitosa(Boolean accionExitosa) {
        this.accionExitosa = accionExitosa;
    }



    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
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
        if (!(object instanceof AuditoriaUsuario)) {
            return false;
        }
        AuditoriaUsuario other = (AuditoriaUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.AuditoriaUsuario[id=" + id + "]";
    }

}
