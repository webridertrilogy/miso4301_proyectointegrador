/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.parser;

import java.io.Serializable;

/**
 * Clase que representa a un Atributo
 */
public class Atributo implements Serializable {

    /**
     * Nombre del atributo
     */
    private String nombre;
    /**
     * Valor del atributo
     */
    private String valor;

    /**
     * Construye un atributo basado en su nombre y su valor
     * @param nombre Nombre del atributo
     * @param valor Valor del atributo
     */
    public Atributo(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    /**
     * Retorna el nombre del atributo
     * @return nombre del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre del atributo usando el nombre que recibe por parametro
     * @param nombre Nuevo nombre del atributo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna el valor del atributo
     * @return valor del atributo
     */
    public String getValor() {
        return valor;
    }

    /**
     * Modifica el valor del atributo usando el valor que recibe por parametro
     * @param valor Nuevo valor del atributo
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
}
