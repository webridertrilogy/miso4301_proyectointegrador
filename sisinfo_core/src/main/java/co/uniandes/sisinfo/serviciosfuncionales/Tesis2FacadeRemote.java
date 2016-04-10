/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Tesis2;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface Tesis2FacadeRemote {

    void create(Tesis2 tesis2);

    void edit(Tesis2 tesis2);

    void remove(Tesis2 tesis2);

    Tesis2 find(Object id);

    List<Tesis2> findAll();

    List<Tesis2> findRange(int[] range);

    int count();

    Collection<Tesis2> findByCorreoAsesor(String correo);

    Collection<Tesis2> findByCorreoEstudiante(String correo);

    Collection<Tesis2> findByEstadoTesis(String estado);

    Collection<Tesis2> findByPeriodo(String periodo);

    Collection<Tesis2> findByHorarioSustentacion(String periodo);

    Collection<Tesis2> findAllOrderBySemestre();

    Collection<Tesis2> findBySincomentariosTesis(String periodo);

    Collection<Persona> findAsesoresByEstadoYPeriodo(String estado, String periodo);

    Collection<Tesis2> findByEstadoYPeriodoTesis(String estado, String periodo);

    Collection<Tesis2> findByDiferentePeriodoTesis(String periodo,String estadoTesis);

}
