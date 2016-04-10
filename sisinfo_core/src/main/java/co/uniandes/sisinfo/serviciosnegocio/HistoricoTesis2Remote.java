package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remota - Servicios de negocio para Históricos de Tesis 2
 * @author Marcela Morales
 */
@Remote
public interface HistoricoTesis2Remote {

    String pasarTesis2RechazadaAHistorico(String xml);

    String pasarTesis2RetiradasAHistorico(String xml);

    String pasarTesis2PerdidasAHistorico(String xml);

    String pasarTesis2TerminadasAHistorico(String xml);

    String consultarTesis2PorEstadoYSemestreYCorreoAsesor(String xml);

    public String establecerAprobacionParadigmaHistoricoTesis2(String comandoXML);



}
