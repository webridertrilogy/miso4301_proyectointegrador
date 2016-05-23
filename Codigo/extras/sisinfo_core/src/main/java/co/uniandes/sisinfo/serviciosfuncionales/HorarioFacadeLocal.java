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


import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Entidad Horario
 */
@Local
public interface HorarioFacadeLocal {

    /**
     * Crea la sesión que llega como parámetro
     * @param sesion Sesión a crear
     */
    void create(Sesion sesion);

    /**
     * Edita la sesión que llega como parámetro
     * @param sesion Sesión a editar
     */
    void edit(Sesion sesion);

    /**
     * Elimina la sesión que llega como parámetro
     * @param sesion Sesión a eliminar
     */
    void remove(Sesion sesion);

    /**
     * Retorna la sesión cuyo id llega como parámetro
     * @param id Id de la sesión
     * @return sesión cuyo id es el que llega como parámetro
     */
    Sesion find(Object id);

    /**
     * Retorna una lista con todas las sesiones
     * @return lista de sesiones
     */
    List<Sesion> findAll();
}
