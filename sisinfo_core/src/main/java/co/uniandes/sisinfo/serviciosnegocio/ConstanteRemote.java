/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicio de negocio: Administración de constantes
 */
@Remote
public interface ConstanteRemote {

    /**
     * Método que crea una nueva constante dada su información
     * @param nombre Nombre de la constante
     * @param tipo Tipo de la constante
     * @param valor Valor de la constante
     * @param descripcion Descripción de la constante
     */
    void crearConstante(String nombre, String tipo, String valor, String descripcion);

    /**
     * Método que retorna el valor de una constante dado su nombre
     * @param nombre Nombre de la constante
     * @return valor de la constante buscada, null en caso de no encontrarse
     */
    String getConstante(String nombre);
}
