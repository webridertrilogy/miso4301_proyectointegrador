/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ProyectoDeGrado;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Remote
public interface ProyectoDeGradoFacadeRemote {

    void create(ProyectoDeGrado proyectoDeGrado);

    void edit(ProyectoDeGrado proyectoDeGrado);

    void remove(ProyectoDeGrado proyectoDeGrado);

    ProyectoDeGrado find(Object id);

    List<ProyectoDeGrado> findAll();

    List<ProyectoDeGrado> findRange(int[] range);

    int count();

     Collection<ProyectoDeGrado> findByCorreoEstudiante(String correo);

    Collection<ProyectoDeGrado> findByTemaTesis(String tema);

    Collection<ProyectoDeGrado> findByCorreoAsesor(String correo);

    Collection<ProyectoDeGrado> findByEstado(String estado);

    Collection<ProyectoDeGrado> findByPeriodoYEstado(String idPeriodo, String estado);

    Collection<ProyectoDeGrado> findByPeriodoEstadoYRangoNota(String idPeriodo, String estado, String notaI, String notaF);

    Collection<ProyectoDeGrado> findByPeriodoTesis(String periodo);

    public abstract Collection<ProyectoDeGrado> findByEstadoYRangoNota(String estado, String notaI, String notaF);

}
