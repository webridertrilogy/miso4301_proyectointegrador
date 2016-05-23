/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Entidad Curso
 */
@Local
public interface CursoFacadeLocal {

    /**
     * Crea el curso que llega como parámetro
     * @param curso Curso a crear
     */
    void create(Curso curso);

    /**
     * Edita el curso que llega como parámetro
     * @param curso Curso a editar
     */
    void edit(Curso curso);

    /**
     * Eliminar el curso que llega como parámetro
     * @param curso Curso a eliminar
     */
    void remove(Curso curso);

    /**
     * Retorna el curso cuyo id llega como parámetro
     * @param id Id del curso
     * @return curso cuyo id es el que llega como parámetro
     */
    Curso find(Object id);

    /**
     * Retorna una lista con todos los cursos
     * @return lista de cursos
     */
    List<Curso> findAll();

    /**
     * Retorna el curso cuyo código llega como parámetro
     * @param codigo Código del curso
     * @return curso cuyo código es el que llega como parámetro
     */
    Curso findByCodigo(String codigo);

    /**
     * Retorna el curso cuyo id llega como parámetro
     * @param id Id del curso
     * @return curso cuyo id es el que llega como parámetro
     */
    Curso findById(Long id);

    Curso findByCRNSeccion(String crn);

    long contarCursos();

    Curso findByCodigoSinProfesor(String codigo);

    void removeAll();

    Curso findByNombre(String nombreRelacionado);
}
