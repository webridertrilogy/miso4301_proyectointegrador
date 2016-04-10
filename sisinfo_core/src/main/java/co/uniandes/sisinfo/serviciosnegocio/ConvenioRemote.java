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
 * Interface Remote
 * Servicio de negocio: Administración de convenio
 */
@Remote
public interface ConvenioRemote {

    /**
     * Método que se encarga de registrar el convenio pendiente de la firma del estudiante dado el comando respectivo
     * El convenio queda a la espera de la firma del estudiante
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String registrarConvenioPendienteEstudiante(String xml);

    /**
     * Método que se encarga de registrar la firma del convenio (estudiante) dado el comando respectivo
     * El convenio queda a la espera de la firma del departamento
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String registrarConvenioPendienteDepartamento(String xml);

    /**
     * Método que se encarga de registrar la firma del convenio (departamento) dado el comando respectivo
     * El convenio queda a la espera de la radicación
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String registrarConvenioPendienteRadicacion(String xml);

    /**
     * Método que se encarga de radicar el convenio (recursos humanos) dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String radicarConvenio(String xml);

    /**
     * Método que se encarga de consultar las solicitudes en estado de convenio dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darConvenios(String xml);

    /**
     * Método que se encarga de consultar las solicitudes en estado de convenio sin información de horario dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darConveniosSecretaria(String xml);

    public String darSolicitudSecretaria(String xml);

    /**
     * Método que se encarga de registrar las firmas del convenio (estudiante) dado el comando respectivo
     * El convenio queda a la espera de la firma del departamento
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String registrarFirmasEstudiantes(String xml);

    String registrarFirmasDepartamento(String xml);
}
