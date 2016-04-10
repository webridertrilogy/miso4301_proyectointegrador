package co.uniandes.sisinfo.historico.serviciosnegocio.tesisP;

import javax.ejb.Local;

/**
 * Interface Remota - Servicios de negocio para Históricos de Proyectos de Grado
 * @author Ivan Mauricio Melo S, Marcela Morales, Paola Gómez
 */
@Local
public interface HistoricoTesisPregradoLocal {

    String pasarTesisPregradoRechazadaAHistorico(String xml);

    String pasarTesisPregradoRetiradaAHistorico(String xml);

    String pasarTesisPregradoTerminadaAHistorico(String xml);

    String pasarTesisPregradoPerdidaAHistorico(String xml);

    String darHistoricoEstudiantesTesisPregrado(String xml);

    String darHistoricoEstudianteTesisPregrado(String xml);

    String darHistoricoEstudiantesTesisPregradoProfesor(String comandoXML);
}
