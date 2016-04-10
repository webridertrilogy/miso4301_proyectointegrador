/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface LaboratorioBeanRemote {

    String consultarLaboratoriosAutorizados(String xml);

    String consultarLaboratorio(String String);

    String consultarLaboratorios(String xml);

    String consultarAutorizadoLaboratorio(String xml);

    String darHorarioDisponibleLaboratorio(String xml);

    String editarLaboratorio(String xml);

    String consultarLaboratoriosEncargado(String xml);

    String darOcupacionLaboratorio(String xml);

    String eliminarLaboratorio(String xml);

    String activarLaboratorio(String xml);

    String consultarLaboratoriosAdministrador(String xml);
}
