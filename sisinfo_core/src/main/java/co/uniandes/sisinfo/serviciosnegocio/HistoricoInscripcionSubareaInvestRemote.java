package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remote - Servicios de negocio para Históricos de Inscripción Subarea
 * @author Paola Gómez
 */
@Remote
public interface HistoricoInscripcionSubareaInvestRemote {

    String pasarInscripcionSubAreaInvestigacionAHistorico(String xml);

}
