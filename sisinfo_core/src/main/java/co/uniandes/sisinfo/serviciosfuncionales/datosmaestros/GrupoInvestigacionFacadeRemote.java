/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.GrupoInvestigacion;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author da-naran
 */
@Remote
public interface GrupoInvestigacionFacadeRemote {

    void create(GrupoInvestigacion grupoInvestigacion);

    void edit(GrupoInvestigacion grupoInvestigacion);

    void remove(GrupoInvestigacion grupoInvestigacion);

    GrupoInvestigacion find(Object id);

    List<GrupoInvestigacion> findAll();

    List<GrupoInvestigacion> findRange(int[] range);

    int count();

    public GrupoInvestigacion findById(Long id);

    public GrupoInvestigacion findByNombre(String nombre);

}
