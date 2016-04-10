/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.bo;

/**
 *
 * @author Asistente
 */
public class AccionBO {

    private String nombre;

    private String descripcion;

    private String comando;

    private String seccion;

    public AccionBO(String nombre, String descripcion, String comando,String seccion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.comando = comando;
        this.seccion = seccion;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    


}
