/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package soporte;

/**
 *
 * @author Asistente
 */
public class ArchivoInformacion {

    private String nombre;

    private String contenido;

    public ArchivoInformacion(String nombre, String contenido) {
        this.nombre = nombre;
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




}
