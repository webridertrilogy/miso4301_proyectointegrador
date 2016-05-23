/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author da-naran
 */
@Local
public interface ProfesorFacadeLocal {

    void create(Profesor profesor);

    void edit(Profesor profesor);

    void remove(Profesor profesor);

    Profesor find(Object id);

    List<Profesor> findAll();

    List<Profesor> findRange(int[] range);

    int count();

    /**
     * Retorna el profesor cuyo id llega como parámetro
     * @param id Id del profesor
     * @return profesor cuyo id es el que llega como parámetro
     */
    Profesor findById(Long id);

    /**
     * Retorna el profesor cuyo correo llega como parámetro
     * @param correo Correo del profesor
     * @return profesor cuyo correo es el que llega como parámetro
     */
    Profesor findByCorreo(String correo);

    List<Profesor> findByTipo(String tipo);

    List<Profesor> findByNivelPlanta(String nivelPlanta);
}
