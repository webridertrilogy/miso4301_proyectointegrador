/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.seguridad;

import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios de Usuario
 */
@Local
public interface UsuarioFacadeLocal {

    /**
     * Crea el usuario que llega como parámetro
     * @param usuario Usuario a crear
     */
    void create(Usuario usuario);

    /**
     * Edita el usuario que llega como parámetro
     * @param usuario Usuario a editar
     */
    void edit(Usuario usuario);

    /**
     * Elimina el usuario que llega como parámetro
     * @param usuario Usuario a eliminar
     */
    void remove(Usuario usuario);

    /**
     * Retorna el usuario cuyo id llega como parámetro
     * @param id Id del usuario
     * @return usuario cuyo id es el que llega como parámetro
     */
    Usuario find(Object id);

    /**
     * Retorna una lista con todos los usuarios
     * @return lista de usuarios
     */
    List<Usuario> findAll();

    /**
     * Retorna el usuario cuyo login llega como parámetro
     * @param login Login del usuario
     * @return usuario cuyo login es el que llega como parámetro
     */
    Usuario findByLogin(String login);

    List<Usuario> findByRol(String rol);
}
