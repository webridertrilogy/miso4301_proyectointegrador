/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author david
 */
@Remote
public interface PeriodoRemote {

    String retornarPeriodosAcademicos(String comando);

    String retornarPeriodosRango(String comando);
}
