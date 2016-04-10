/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Tesis1;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface Tesis1FacadeRemote {

    void create(Tesis1 tesis1);

    void edit(Tesis1 tesis1);

    void remove(Tesis1 tesis1);

    Tesis1 find(Object id);

    List<Tesis1> findAll();

    List<Tesis1> findRange(int[] range);

    int count();

    Collection<Tesis1> findByCorreoAsesor(String correo);

    Collection<Tesis1> findByCorreoEstudiante(String xml);

    Collection<Tesis1> findByEstadoTesis(String estado);

    Collection<Tesis1> findByPeriodoTesis(String periodo);

    Collection<Tesis1> findByTemaTesis(String tematesis);

    Collection<Tesis1> findBySincomentariosTesis(String periodo);

    Collection<Tesis1> findByPeriodoEstadoAsesor(String periodo, String estado, String correoAsesor);

    Collection<Persona> findAsesoresByPeriodoEstado(String periodo, String estado);

    Collection<Tesis1> findByPeriodoYEstado(String periodo, String estado);

    Collection<Tesis1> findByEstadoYPeriodoTesis(String estado, String periodo);

  

}
