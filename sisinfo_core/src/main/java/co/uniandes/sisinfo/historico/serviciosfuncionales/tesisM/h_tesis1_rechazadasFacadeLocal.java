/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis1_rechazadas;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface h_tesis1_rechazadasFacadeLocal {

    void create(h_tesis1_rechazadas h_tesis1_rechazadas);

    void edit(h_tesis1_rechazadas h_tesis1_rechazadas);

    void remove(h_tesis1_rechazadas h_tesis1_rechazadas);

    h_tesis1_rechazadas find(Object id);

    List<h_tesis1_rechazadas> findAll();

    List<h_tesis1_rechazadas> findRange(int[] range);

    int count();

   List<h_tesis1_rechazadas> findByCorreoEstudiante(String correo);

   List<h_tesis1_rechazadas> findByCorreoAsesor(String correo);

}
