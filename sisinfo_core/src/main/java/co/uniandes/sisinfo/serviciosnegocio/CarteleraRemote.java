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

import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicio de negocio: Administración de carga
 */
@Remote
public interface CarteleraRemote {

    /**
     * Método que se encarga de cargar la cartelera de cursos dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String cargarCartelera(String xml);
}
