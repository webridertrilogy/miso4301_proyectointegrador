/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 * Interface Local
 * Servicio de negocio: Administración de monitorías
 */
@Local
public interface MonitoriaLocal {

    /**
     * Método que se encarga de consultar las monitorías por sección dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    @Deprecated
    String darMonitoriasPorSeccion(String comando);

    String actualizarNotaMonitor(String root);

    String darDatosMonitoriaPorIdSolicitud(String xmlComando);

    double darMonitoresAsignados(String crn);

    double darCargaMonitoriasPorSeccion(String crn);

    boolean hayVacantes(String crn, double maximoMonitores);

    String darMonitoriasRealizadasPorCorreo(String comando);
}
