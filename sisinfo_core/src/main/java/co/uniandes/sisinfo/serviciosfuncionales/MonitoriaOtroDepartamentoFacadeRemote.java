/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.MonitoriaOtroDepartamento;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface MonitoriaOtroDepartamentoFacadeRemote {

    void create(MonitoriaOtroDepartamento monitoriaOtroDepartamento);

    void edit(MonitoriaOtroDepartamento monitoriaOtroDepartamento);

    void remove(MonitoriaOtroDepartamento monitoriaOtroDepartamento);

    MonitoriaOtroDepartamento find(Object id);

    List<MonitoriaOtroDepartamento> findAll();

    List<MonitoriaOtroDepartamento> findRange(int[] range);

    int count();

    public List<MonitoriaOtroDepartamento> findByCodigoEstudiante(String codigo);

    public MonitoriaOtroDepartamento findByCodigoEstudianteAndCodigoCurso(String codigoEstudiante, String codigoCurso);

}
