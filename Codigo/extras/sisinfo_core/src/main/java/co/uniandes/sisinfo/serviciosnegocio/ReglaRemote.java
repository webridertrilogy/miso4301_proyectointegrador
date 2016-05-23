/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.util.Collection;

import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicio de negocio: Administración de reglas
 */
@Remote
public interface ReglaRemote {

    /**
     * Método que se encarga de crear una regla dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String crearRegla(String xml);

    /**
     * Método que se encarga de eliminar una regla dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String eliminarRegla(String xml);

    /**
     * Método que se encarga de retornar todas las reglas existentes dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darReglas(String xml);

    /**
     * Método que se encarga de validar un enunciado de acuerdo a una regla dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String validarRegla(String xml);

    /**
     * Método que valida una regla dado su nombre y un valor a comparar
     * @param nombre Nombre de la regla
     * @param valorComparar Valor a comparar
     * @return True|False Resultado de la validación de la regla
     * @throws java.lang.Exception
     */
    boolean validarRegla(String nombre, String valorComparar);

    /**
     * Retorna una colección con los nombres de las reglas cuyos ids llegan como parámetro
     * @param idReglas Colección de ids de reglas
     * @return Colección con los nombres de las reglas
     */
    Collection<String> getNombresReglasPorIds(Collection<String> idReglas);
}
