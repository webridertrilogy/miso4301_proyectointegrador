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

import javax.ejb.Local;

/**
 * Interface Local
 * Servicio de negocio: Administración de convocatoria
 */
@Local
public interface ConvocatoriaLocal {

    /**
     * Método que se encarga de iniciar un periodo dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String iniciarPeriodo(String xml);

    /**
     * Método que se encarga de abrir la convocatoria dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String abrirConvocatoria(String xml);

    /**
     * Método que se encarga de cerrar la convocatoria dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String cerrarConvocatoria(String xml);

    public void cerrarConvocatoriaActual();

    /**
     * Método que se encarga de abrir la convocatoria dado el periodo académico
     * @param periodoAcademico Periodo academico
     * @return true|false indica si la convocatoria fué abierta con éxito
     */
    @Deprecated
    boolean abrirConvocatoriaPeriodo(String periodoAcademico);

    /**
     * Método que se encarga de modificar los datos de las secciones dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String modificarDatosSecciones(String comando);

    boolean hayConvocatoriaAbierta( );

    String determinarSiHayConvocatoriaAbierta(String comando);
}
