/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_informacionAcademica;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author david
 */
@Remote
public interface h_informacionAcademicaFacadeRemote {

    void create(h_informacionAcademica h_informacionAcademica);

    void edit(h_informacionAcademica h_informacionAcademica);

    void remove(h_informacionAcademica h_informacionAcademica);

    h_informacionAcademica find(Object id);

    List<h_informacionAcademica> findAll();

    List<h_informacionAcademica> findRange(int[] range);

    int count();

}
