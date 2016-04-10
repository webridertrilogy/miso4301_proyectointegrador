/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.bo;

import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import java.util.Collection;

/**
 * Clase utilizada para representar una tarea vencida que aun puede ser completada
 * @author Carlos Morales
 */
public class TareaPendienteVencidaBO {

    private Long idTareaSencilla;

    private String tipo;

    private String mensaje;

    private String comando;

    private Collection<Parametro> parametros;

    public TareaPendienteVencidaBO(Long idTareaSencilla, String tipo, String mensaje, String comando, Collection<Parametro> parametros) {
        this.idTareaSencilla = idTareaSencilla;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.comando = comando;
        this.parametros = parametros;
    }


    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getIdTareaSencilla() {
        return idTareaSencilla;
    }

    public void setIdTareaSencilla(Long idTareaSencilla) {
        this.idTareaSencilla = idTareaSencilla;
    }

    public Collection<Parametro> getParametros() {
        return parametros;
    }

    public void setParametros(Collection<Parametro> parametros) {
        this.parametros = parametros;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }





}
