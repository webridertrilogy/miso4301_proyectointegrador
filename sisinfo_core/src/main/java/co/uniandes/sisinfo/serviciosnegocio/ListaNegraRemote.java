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
 * Servicio de negocio: Administración de lista negra
 */
@Remote
public interface ListaNegraRemote {

    /**
     * Método que se encarga de consultar si un estudiante se encuentra en la lista negra dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String estaEnListaNegra(String comando);

    /**
     * Método que se encarga de consultar si un estudiante se encuentra en la lista negra por login dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String estaEnListaNegraLogin(String comando);

    /**
     * Método que se encarga de agregar un estudiante a la lista negra dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String agregarAListaNegra(String comando);

    /**
     * Método que se encarga de eliminar un estudiante a la lista negra dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String eliminarDeListaNegra(String login);

    /**
     * Método que se encarga de consultar la lista negra dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darListaNegra(String comando);

    /**
     * Método que se encarga de eliminar los temporales de lista negra
     */
    void eliminarTemporalesDeListaNegra();

    public void agregarAListaNegraPorNota(String correo);

    boolean estaEnListaNegraPorCorreo(String correo);
}
