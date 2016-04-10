package co.uniandes.sisinfo.historico.serviciosnegocio;

import javax.ejb.Local;

/**
 * Interface Local - Servicios de negocio para Históricos de Inscripción Subarea
 * @author Paola Gómez
 */
@Local
public interface HistoricoInscripcionSubareaInvestLocal {

    String pasarInscripcionSubAreaInvestigacionAHistorico(String xml);

}
