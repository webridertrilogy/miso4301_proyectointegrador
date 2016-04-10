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


import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import java.util.Collection;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicio de negocio: Administración de cursos
 */
@Local
public interface CursoLocal {

    /**
     * Método que se encarga de retornar todos los cursos con vacantes dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darCursosConVacantes(String xml);

    /**
     * Método que se encarga de retornar todos las secciones con vacantes dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSeccionesConVacantesPorCurso(String xml);

    /**
     * Método que se encarga de retornar todas las facultades dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darFacultades(String xml);

    /**
     * Método que se encarga de retornar todos los programas de una facultad dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darProgramasFacultad(String xml);

    /**
     * Método que se encarga de retornar todos las secciones con vacantes de un curso dado su código
     * @param codigoCurso Código del curso
     * @return Colección de secciones con vacantes
     */
    Collection<Seccion> darSeccionesConVacantesPorCodigoCurso(String codigoCurso);

    /**
     * Método que se encarga de consultar todas las secciones de un profesor por login dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSeccionesPorLoginProfesor(String xml);

    /**
     * Método que se encarga de retornar todos los programas dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darProgramas(String xml);

    /**
     * Método que se encarga de retornar todas las facultades con sus programas dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darFacultadesConProgramas(String xml);

    /**
     * Método que se encarga de consultar todos los cursos dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darCursos(String comando);

    /**
     * Método que se encarga de consultar los cursos del Rol Cupi2
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darCursosCupi2(String comando);

    /**
     * Retorna el numero de monitores asignados a un curso especificado por un codigo especifico
     * @param codigo Código del curso a buscar
     * @return número de monitores asignados a dicho curso
     */
    double darNumeroMonitoresAsignados(String codigo);

    /**
     * Retorna el numero maximo de monitores de un curso, sumando los maximos de cada una de sus secciones
     * @param codigo Codigo del curso a buscar
     * @return numero maximo de monitores de dicho curso
     */
    double darNumeroMaximoMonitores(String codigo);

    /**
     * Método que se encarga de consultar la información de un curso dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String getCurso(String comando);

    /**
     * Método que se encarga de consultar la información de una sección dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSeccion(String comando);

    /**
     * Método que se encarga de consultar todos los cursos del semestre actual dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darCursosSemestreActual(String comando);

    /**
     * Método que se encarga de consultar todos los cursos pendientes por monitores dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darCursosPendientesPorMonitores(String comando);

    String cambiarCantidadMonitoresSeccion(String comando);

    /**
     * Método que se encarga de modificar los datos de un curso dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String modificarDatosCurso(String xml);

    String darSeccionesPorLoginProfesorBasico(String xml);

}
