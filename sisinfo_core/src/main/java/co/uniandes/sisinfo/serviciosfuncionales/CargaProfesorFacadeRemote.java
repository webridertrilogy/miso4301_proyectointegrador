/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CargaProfesor;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface CargaProfesorFacadeRemote {

    void create(CargaProfesor cargaProfesor);

    void edit(CargaProfesor cargaProfesor);

    void remove(CargaProfesor cargaProfesor);

    CargaProfesor find(Object id);

    List<CargaProfesor> findAll();

    List<CargaProfesor> findRange(int[] range);

    int count();

    List<CargaProfesor> findByCorreo(String correo);

    List<CargaProfesor> findByidProfesorYPeriodo(Long idProfesor, String periodoCarga);

    CargaProfesor findCargaByCorreoProfesorYNombrePeriodo(String correo, String nombrePeriodo);

    void flushear();

    public List<CargaProfesor> findByPeriodo(String periodoCarga);

}
