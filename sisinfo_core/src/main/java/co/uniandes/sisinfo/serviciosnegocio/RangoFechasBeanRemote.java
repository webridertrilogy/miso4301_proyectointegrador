/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import javax.ejb.Remote;

/**
 *
 * @author Administrador
 */
@Remote
public interface RangoFechasBeanRemote  {

    public boolean esRangoValido(String nombre);

    void eliminarRangosFechas();

    String rangoIniciado(String comando);
    
    String crearRangosFechas(String comando);

    void crearTareasConfirmacionMonitoria();

    void crearTareasTraerPapelesRegistrarConvenio();

    String editarRangosFechas(String comando);

    Timestamp darFechaInicialRangoPorNombre(String nombre);

    Timestamp darFechaFinalRangoPorNombre(String nombre);

    String consultarRangos(String comando);

    boolean rangosCreados();

    public String esRangoValidoXML(String xml);

    void abrirConvocatoria();

    void completarTareasPendientes(String responsable, String tipo, String idSolicitud);

    void manejoTimersMonitorias(String info);

    void crearTareaProfesorPreseleccionarPorSeccion(String crn);
}
