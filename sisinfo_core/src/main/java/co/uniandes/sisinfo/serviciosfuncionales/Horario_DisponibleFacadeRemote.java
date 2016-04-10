/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Horario_Disponible;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Entidad Horario Disponible
 */
@Remote
public interface Horario_DisponibleFacadeRemote {

    /**
     * Crea el horario disponible que llega como parámetro
     * @param horario_Disponible Horario disponible a crear
     */
    void create(Horario_Disponible horario_Disponible);

    /**
     * Edita el horario disponible que llega como parámetro
     * @param horario_Disponible Horario disponible a editar
     */
    void edit(Horario_Disponible horario_Disponible);

    /**
     * Elimina el horario disponible que llega como parámetro
     * @param horario_Disponible Horario disponible a eliminar
     */
    void remove(Horario_Disponible horario_Disponible);

    /**
     * Retorna el horario disponible cuyo id llega como parámetro
     * @param id Id del horario disponible
     * @return horario disponible cuyo id es el que llega como parámetro
     */
    Horario_Disponible find(Object id);

    /**
     * Retorna una lista con todos los horarios disponibles
     * @return lista de horarios disponibles
     */
    List<Horario_Disponible> findAll();

    /**
     * Retorna el horario disponible cuyo código llega como parámetro
     * @param codigo Código del horario disponible
     * @return horario disponible cuyo código es el que llega como parámetro
     */
    Horario_Disponible findByCodigoEstudiante(String codigo);
}
