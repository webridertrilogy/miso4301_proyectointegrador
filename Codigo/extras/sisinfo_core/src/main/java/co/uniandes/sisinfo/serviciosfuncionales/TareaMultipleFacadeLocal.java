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

import co.uniandes.sisinfo.entities.TareaMultiple;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Entidad Tarea
 */
@Local
public interface TareaMultipleFacadeLocal {

    void create(TareaMultiple tarea);

    void edit(TareaMultiple tarea);

    void remove(TareaMultiple tarea);

    TareaMultiple find(Object id);

    List<TareaMultiple> findAll();

    TareaMultiple findById(Long id);

    Collection<TareaMultiple> findByCorreoYTipo(String tipo,String correo);

    Collection<TareaMultiple> findByRolYTipo(String tipo, String rol);

    Collection<TareaMultiple> findByCorreo(String correo);

    Collection<TareaMultiple> findByRol(String rol);

    Collection<TareaMultiple> findByRolNoPersonal(String rol);

    Collection<TareaMultiple> findByRolYTipoNoPersonal(String tipo, String rol);

    Collection<TareaMultiple> findByTipo(String tipo);

    public abstract List<TareaMultiple> findByCorreoYFechaAntesDeCaducacion(String correo, Timestamp fecha, String rol );

    List<TareaMultiple> findByRolNoPersonalYFechaAntesDeCaducacion(Timestamp fecha, String rol );

    Collection<TareaMultiple> findByEstadoTareaSencilla(String estado);

    Collection<TareaMultiple> findByCorreoRolAndEstadoTareaSencilla(String estado, String correo,String rol);

    Collection<TareaMultiple> findByRolAndEstadoTareaSencillaNoPersonal(String estado,String rol);



}
