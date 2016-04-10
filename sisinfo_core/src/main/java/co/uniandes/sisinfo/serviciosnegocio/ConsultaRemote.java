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
 * Servicio de negocio: Administración de consultas
 */
@Remote
public interface ConsultaRemote {

    /**
     * Método que se encarga de verificar conflicto de horario en una sección
     * @param correo Correo del aspirante
     * @param crn_seccion CRN de la sección
     * @return true|false Resultado de la verificación de conflicto de horario
     */
    boolean verificarConflicto(String correo, String crn_seccion);

    /**
     * Método que se encarga de consultar el horario de un estudiante por login dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darHorarioEstudiantePorLogin(String comando);

    /**
     * Método que se encarga de modificar un horario dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String modificarHorario(String comando);

    /**
     * Método que se encarga de devolver el mensaje con las reglas de las monitorias
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    public String darMensajeReglasMonitorias(String xml);
}

