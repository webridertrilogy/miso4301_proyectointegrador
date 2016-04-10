/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.nucleo.services;

import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Núcleo
 */
@Local
public interface NucleoLocal {

    /**
     * Método que se encarga de resolver un comando dado en xml
     * @param comandoXML - cadena en la cual se define el comando
     * @return String: cadena de respuesta de acuerdo al comando dado
     * @throws java.lang.Exception
     */
    String resolverComando(String comandoXML) throws Exception;
}
