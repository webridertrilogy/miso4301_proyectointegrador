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

import java.util.Hashtable;

import javax.ejb.Local;

/**
 * Interface Local
 * Servicio de negocio: Credencial
 */
@Local
public interface CredencialLocal {

    /**
     * Crea una nuevas credenciales dado el usuario y la contraseña
     * @param usuario Nombre de usuario
     * @param contrasena Contraseña del usuario
     * @return true si se pudo crear la credencial o false si ya estaba ingresada
     */
    boolean crearCredencial(String comando);

    /**
     * Encripta la credencial dada
     * @param credencialesPlano Credenciales del usuario
     * @return las credenciales encriptadas del usuario
     */
    public Hashtable<byte[], byte[]> encriptarCredenciales(Hashtable<String, String> credencialesPlano);

    /**
     * Desencripta la credencial encriptada dada
     * @param credencial Credenciales encnriptadas
     * @return las credenciales planas
     */
    Hashtable<String, String> desencriptarCredenciales(Hashtable<byte[], byte[]> credencial);

    /**
     * Retorna la contraseña dado la cuenta del usuario
     * @param cuenta Nombre de la cuenta de usuario
     * @return contraseña de la cuenta de usuario
     */
    String darContrasena(String cuenta);
}
