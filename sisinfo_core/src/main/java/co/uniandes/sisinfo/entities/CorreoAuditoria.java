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

/**
 * Entidad Correo de Auditoría
 * @author Manuel Rodríguez, Marcela Morales
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "CorreoAuditoria.findByDestinatarios", query = "SELECT c FROM CorreoAuditoria c WHERE c.destinatarios like :correo OR c.destinatariosCC like :correo OR c.destinatariosCCO like :correo"),
    @NamedQuery(name = "CorreoAuditoria.findByDestinatariosYFecha", query = "SELECT c FROM CorreoAuditoria c WHERE (c.destinatarios like :correo OR c.destinatariosCC like :correo OR c.destinatariosCCO like :correo) AND (c.fechaEnvio >= :fechaInicio AND c.fechaEnvio <= :fechaFin)"),
    @NamedQuery(name = "CorreoAuditoria.findByDestinatariosYAsunto", query = "SELECT c FROM CorreoAuditoria c WHERE (c.destinatarios like :correo OR c.destinatariosCC like :correo OR c.destinatariosCCO like :correo) AND (c.asunto like :asunto)"),
    @NamedQuery(name = "CorreoAuditoria.findByDestinatariosFechaYAsunto", query = "SELECT c FROM CorreoAuditoria c WHERE (c.destinatarios like :correo OR c.destinatariosCC like :correo OR c.destinatariosCCO like :correo) AND (c.asunto like :asunto) AND (c.fechaEnvio >= :fechaInicio AND c.fechaEnvio <= :fechaFin)"),
    @NamedQuery(name = "CorreoAuditoria.findByFecha", query = "SELECT c FROM CorreoAuditoria c WHERE c.fechaEnvio >= :fechaInicio AND c.fechaEnvio <= :fechaFin"),
    @NamedQuery(name = "CorreoAuditoria.findByAsunto", query = "SELECT c FROM CorreoAuditoria c WHERE c.asunto like :asunto"),
    @NamedQuery(name = "CorreoAuditoria.findByAsuntoYFecha", query = "SELECT c FROM CorreoAuditoria c WHERE c.asunto like :asunto AND c.fechaEnvio >= :fechaInicio AND c.fechaEnvio <= :fechaFin")
})
public class CorreoAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "asunto")
    private String asunto;
    @Column(name = "mensaje", length = 450000)
    private String mensaje;
    @Column(name = "fecha")
    private Timestamp fechaEnvio;
    @Column(name = "destinatarios", length = 214123)
    private String destinatarios;
    @Column(name = "destinatarioscc", length = 214123)
    private String destinatariosCC;
    @Column(name = "destinatarioscco", length = 214123)
    private String destinatariosCCO;
    @Column(name = "nombreAdjunto", length = 214123)
    private String nombreAdjunto;
    @Column(name = "enviado")
    private Boolean enviado;

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getDestinatariosCC() {
        return destinatariosCC;
    }

    public void setDestinatariosCC(String destinatariosCC) {
        this.destinatariosCC = destinatariosCC;
    }

    public String getDestinatariosCCO() {
        return destinatariosCCO;
    }

    public void setDestinatariosCCO(String destinatariosCCO) {
        this.destinatariosCCO = destinatariosCCO;
    }

    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Timestamp fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombreAdjunto() {
        return nombreAdjunto;
    }

    public void setNombreAdjunto(String nombreAdjunto) {
        this.nombreAdjunto = nombreAdjunto;
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
        if (!(object instanceof CorreoAuditoria)) {
            return false;
        }
        CorreoAuditoria other = (CorreoAuditoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.CorreoAuditoria[id=" + id + "]";
    }
}
