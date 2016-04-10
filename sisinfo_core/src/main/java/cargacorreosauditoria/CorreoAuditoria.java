package cargacorreosauditoria;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Marcela Morales
 */
public class CorreoAuditoria implements Serializable{

    private Long id;
    private String asunto;
    private String mensaje;
    private Timestamp fechaEnvio;
    private String destinatarios;
    private String destinatariosCC;
    private String destinatariosCCO;
    private String nombreAdjunto;

    public CorreoAuditoria() { }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String toString(){
        return this.id+"[CorreoAuditoria]";
    }
}
