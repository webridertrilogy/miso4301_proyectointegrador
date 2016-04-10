/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_proyecto_financiado;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface h_proyecto_financiadoFacadeRemote {

    void create(h_proyecto_financiado h_proyecto_financiado);

    void edit(h_proyecto_financiado h_proyecto_financiado);

    void remove(h_proyecto_financiado h_proyecto_financiado);

    h_proyecto_financiado find(Object id);

    List<h_proyecto_financiado> findAll();

    List<h_proyecto_financiado> findRange(int[] range);

    int count();

    h_proyecto_financiado findByName(String name);

}
