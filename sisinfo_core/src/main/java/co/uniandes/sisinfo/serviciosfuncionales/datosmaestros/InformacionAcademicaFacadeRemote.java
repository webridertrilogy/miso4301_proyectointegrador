/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface InformacionAcademicaFacadeRemote {

    void create(InformacionAcademica informacionAcademica);

    void edit(InformacionAcademica informacionAcademica);

    void remove(InformacionAcademica informacionAcademica);

    InformacionAcademica find(Object id);

    List<InformacionAcademica> findAll();

    List<InformacionAcademica> findRange(int[] range);

    int count();

    public InformacionAcademica findByCodigoEstudiante(String codigo);

}
