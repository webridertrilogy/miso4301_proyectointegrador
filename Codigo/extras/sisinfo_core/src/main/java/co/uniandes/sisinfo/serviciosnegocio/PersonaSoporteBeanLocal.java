package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author Juan Manuel Moreno B.
 */
@Local
public interface PersonaSoporteBeanLocal {

    /**
      * Consulta personas de soporte.
      */
    String getListPersonaSoporte(String xml);

    /**
      * Consulta personas de soporte por su Id.
      */
    String getPersonaSoportePorId(String xml);
}
