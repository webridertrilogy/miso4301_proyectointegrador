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
 * Servicio de negocio: Administración de archivos
 */
@Local
public interface ArchivosLocal {

    /**
     * Método que se encarga de confirmar la subida de un archivo dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String confirmarSubidaArchivo(String root);

    String darArchivosProfesor(String comando);

    String darInfoArchivo(String comando);
    
    String darRutaDirectorioArchivo(String comando);

    String confirmarReemplazoArchivo(String comando);

    String darArchivosProfesorPorPeriodo(String comando);

    /**
     * Método que se encarga de actualizar la nota de un monitor dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
  
    String comportamientoInicioTareaPrograma(String xml);

    String comportamientoInicioTarea30Porciento(String xml);

    String comportamientoInicioTareaCierreCursos(String xml);

    String comportamientoFinRangoFechasSubirPrograma(String xml);

    String comportamientoFinRangoFechas30Porciento(String xml);

    String comportamientoFinRangoFechasCierreCursos(String xml);
}
