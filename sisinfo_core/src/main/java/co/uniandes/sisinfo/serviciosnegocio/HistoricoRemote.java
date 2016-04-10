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
 * Servicio de negocio: Históricos
 */
@Remote
public interface HistoricoRemote {

    String consultarMonitoriasEnHistorico(String comando);

    String pasarMonitoriasAHistoricos(String comando);

    String darArchivosProfesorPorPeriodoEnHistorico(String comando);

    String darInfoArchivoHistorico(String comando);

    Collection<String> darPeriodosPlaneacionAcademicaHistoricos();
}
