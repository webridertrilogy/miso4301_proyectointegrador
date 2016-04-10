package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remota - Servicios de negocio para Históricos de Proyectos de Grado
 * @author Ivan Mauricio Melo S, Marcela Morales, Paola Gomez
 */
@Remote
public interface HistoricoTesisPregradoRemote {

    String pasarTesisPregradoRechazadaAHistorico(String xml);

    String pasarTesisPregradoRetiradaAHistorico(String xml);

    String pasarTesisPregradoTerminadaAHistorico(String xml);

    String pasarTesisPregradoPerdidaAHistorico(String xml);

    String darHistoricoEstudiantesTesisPregrado(String xml);

    String darHistoricoEstudianteTesisPregrado(String xml);

    String darHistoricoEstudiantesTesisPregradoProfesor(String comandoXML);
}
