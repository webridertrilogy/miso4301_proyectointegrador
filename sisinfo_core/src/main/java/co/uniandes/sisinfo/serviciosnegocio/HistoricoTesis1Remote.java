package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remota - Servicios de negocio para Históricos de Tesis 1
 * @author Marcela Morales
 */
@Remote
public interface HistoricoTesis1Remote {

    String pasarTesis1RechazadaAHistorico(String xml);

    String pasarTesis1RetiradasAHistorico(String xml);

    String pasarTesis1PerdidasAHistorico(String xml);

    String pasarTesis1TerminadasAHistorico(String xml);

    public String establecerAprobacionParadigmaHistoricoTesis1(String comandoXML);


}
