/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Juan Manuel Moreno B.
 */
@Remote
public interface PersonaSoporteBeanRemote {

    /**
      * Consulta personas de soporte.
      */
    String getListPersonaSoporte(String xml);

    /**
      * Consulta personas de soporte por su Id.
      */
    String getPersonaSoportePorId(String xml);
}
