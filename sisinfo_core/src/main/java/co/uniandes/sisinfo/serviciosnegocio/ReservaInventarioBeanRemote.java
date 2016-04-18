/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.util.Collection;

import javax.ejb.Remote;

import co.uniandes.sisinfo.bo.AccionBO;

/**
 *
 * @author Asistente
 */
@Remote
public interface ReservaInventarioBeanRemote {

    public String consultarReservasLaboratorio(String xml);

    String crearReserva(String xml);

    String consultarReservasPersona(String xml);

    String cancelarReserva(String xml);

    String consultarReserva(String xml);

    void manejoTimersReservaInventario(String info);

    String consultarRangoReservas(String xml);

    String cancelarGrupoReservas(String xml);

    String marcarGrupoReservas(String xml);
    

    Collection<AccionBO> darAcciones(String rol,String login);

    public String isMultiple(String comandoXML);

    public String cancelarReservaMultiple(String comandoXML);

}
