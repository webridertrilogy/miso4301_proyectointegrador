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



import co.uniandes.sisinfo.entities.datosmaestros.Facultad;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Entidad Facultad
 */
@Local
public interface FacultadFacadeLocal {

    /**
     * Crea la facultad que llega como parámetro
     * @param facultad Facultad a crear
     */
    void create(Facultad facultad);

    /**
     * Edita la facultad que llega como parámetro
     * @param facultad Facultad a editar
     */
    void edit(Facultad facultad);

    /**
     * Elimina la facultad que llega como parámetro
     * @param facultad Facultad a eliminar
     */
    void remove(Facultad facultad);

    /**
     * Retorna la facultad cuyo id llega como parámetro
     * @param id Id de la facultad
     * @return facultad cuyo id es el que llega como parámetro
     */
    Facultad find(Object id);

    /**
     * Retorna una lista con todas las facultades
     * @return lista de facultades
     */
    List<Facultad> findAll();

    /**
     * Retorna la facultad cuyo nombre llega como parámetro
     * @param nombre Nombre de la facultad
     * @return facultad cuyo nombre es el que llega como parámetro
     */
    Facultad findByNombre(String nombre);
}
