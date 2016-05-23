/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author david
 */
@Local
public interface PeriodoLocal {

    String retornarPeriodosAcademicos(String comando);

    String retornarPeriodosRango(String comando);
}
