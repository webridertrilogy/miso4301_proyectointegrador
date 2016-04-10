/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_cargaProfesor_cyc;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_cargaProfesor_cycFacadeLocal {

    void create(h_cargaProfesor_cyc h_cargaProfesor_cyc);

    void edit(h_cargaProfesor_cyc h_cargaProfesor_cyc);

    void remove(h_cargaProfesor_cyc h_cargaProfesor_cyc);

    h_cargaProfesor_cyc find(Object id);

    List<h_cargaProfesor_cyc> findAll();

    List<h_cargaProfesor_cyc> findRange(int[] range);

    int count();

    h_cargaProfesor_cyc findCargaByCorreoProfesorYNombrePeriodo(String correo, String nombrePeriodo);

    List< h_cargaProfesor_cyc> findByCorreo(String correo);

    

 

}
