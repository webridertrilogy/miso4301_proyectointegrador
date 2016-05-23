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
 * Servicio de negocio: Administración de profesores
 */
@Local
public interface ProfesorLocal {

    /**
     * Método que se encarga de agregar un profesor dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String agregarProfesor(String xml);

    /**
     * Método que se encarga de actualizar los datos de un profesor dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String actualizarDatosProfesor(String xml);

    /**
     * Método que se encarga de eliminar los datos de un profesor dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String eliminarDatosProfesor(String xml);

    /**
     * Método que se encarga de consultar la información de los profesores dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarDatosProfesores(String xml);

    String consultarDatosProfesor(String xml);

    String agregarProfesores(String xml);

    String agregarGrupoInvestigacion(String xml);

    String agregarGruposInvestigacion(String xml);

    String eliminarGrupoInvestigacion(String xml);

    String actualizarDatosGrupoInvestigacion(String xml);

    String consultarProfesorPerteneceAGrupo(String xml);

    String consultarProfesoresGrupo(String xml);

    String consultarProfesoresPorTipo(String xml);

    String consultarGruposInvestigacion(String xml);

    //CRUD
    String crearProfesor(String xml);
    String consultarProfesores(String xml);
    String consultarProfesor(String xml);
    String eliminarProfesor(String xml);

    String actualizarEstadoActivoProfesor(String comando);
}
