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

import co.uniandes.sisinfo.entities.Lista_Negra;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Entidad Lista Negra
 */
@Local
public interface Lista_NegraFacadeLocal {

    /**
     * Crea la lista negra que llega como parámetro
     * @param lista_Negra Lista negra a crear
     */
    void create(Lista_Negra lista_Negra);

    /**
     * Edita la lista negra que llega como parámetro
     * @param lista_Negra Lista negra a editar
     */
    void edit(Lista_Negra lista_Negra);

    /**
     * Elimina la lista negra que llega como parámetro
     * @param lista_Negra Lista negra a eliminar
     */
    void remove(Lista_Negra lista_Negra);

    /**
     * Retorna la lista negra cuyo id llega como parámetro
     * @param id Id de la lista negra
     * @return lista negra cuyo id es el que llega como parámetro
     */
    Lista_Negra find(Object id);

    /**
     * Retorna una lista con todas las listas negras
     * @return lista de listas negras
     */
    List<Lista_Negra> findAll();

    /**
     * Retorna la lista negra dado el correo del estudiante
     * @param correo Correo del estudiante
     * @return lista negra
     */
    Lista_Negra findEstudianteByCodigo(String codigo);

    /**
     * Retorna la lista negra dado el código del estudiante
     * @param codigo Código del estudiante
     * @return lista negra
     */
    Lista_Negra findEstudianteByCorreo(String correo);

    Collection<Lista_Negra> findEstudiantesTemporales();
}
