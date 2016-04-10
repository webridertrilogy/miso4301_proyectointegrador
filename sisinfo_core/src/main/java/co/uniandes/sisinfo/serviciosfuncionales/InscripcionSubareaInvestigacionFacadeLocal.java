/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.InscripcionSubareaInvestigacion;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface InscripcionSubareaInvestigacionFacadeLocal {

    void create(InscripcionSubareaInvestigacion tesis1);

    void edit(InscripcionSubareaInvestigacion tesis1);

    void remove(InscripcionSubareaInvestigacion tesis1);

    InscripcionSubareaInvestigacion find(Object id);

    List<InscripcionSubareaInvestigacion> findAll();

    List<InscripcionSubareaInvestigacion> findRange(int[] range);

    int count();

    List<InscripcionSubareaInvestigacion> findByCorreoCoordinador(String xml);

    List<InscripcionSubareaInvestigacion> findByCorreoAsesor(String correo);

    List<InscripcionSubareaInvestigacion> findByEstadoInscripcion(String estado);

    InscripcionSubareaInvestigacion findByCorreoEstudiante(String correo);

    InscripcionSubareaInvestigacion findByEstadoYCorreoEstudiante(String correo, String estado);

    List<InscripcionSubareaInvestigacion> findByCorreoCoordinadorYEstado(String correo, String estado);

    List<InscripcionSubareaInvestigacion> findByCorreoAsesorYestado(String correo, String estado);

    List<InscripcionSubareaInvestigacion> findByPeriodoTesis1(String periodo);

    Collection<Persona> findByCoordinadoresSubAreaConEstado(String estado);

    Collection<Persona> findByAsesoresSubAreaConEstado(String estado);

}
