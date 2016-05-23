/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;


import java.util.Collection;

import javax.ejb.Local;

import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;

/**
 *
 * @author Asistente
 */
@Local
public interface TesisBeanLocal {

    String darSemestresTesis(String xml);

    void manejoTimmersTesisMaestria(String comando);

    String establecerFechasPeriodo(String xml);

    String darConfiguracionPeriodoTesisPorNombre(String xml);

    String agregarTemaTesis(String xml);

    String darTemasDeTesis(String xml);

    String darTemasDeTesisPorAsesor(String xml);

    String eliminarTemaTesisAsesor(String xml);

    String darTemadeTesisPorId(String xml);

    Collection<Estudiante> darEstudiantesPendientesPorTesis();
}
