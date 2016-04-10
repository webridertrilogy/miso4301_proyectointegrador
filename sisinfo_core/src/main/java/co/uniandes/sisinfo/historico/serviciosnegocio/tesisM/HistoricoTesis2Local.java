package co.uniandes.sisinfo.historico.serviciosnegocio.tesisM;

import javax.ejb.Local;

/**
 * Interface Local - Servicios de negocio para Históricos de Tesis 2
 * @author Marcela Morales
 */
@Local
public interface HistoricoTesis2Local {

    String pasarTesis2RechazadaAHistorico(String xml);

    String pasarTesis2RetiradasAHistorico(String xml);

    String pasarTesis2PerdidasAHistorico(String xml);

    String pasarTesis2TerminadasAHistorico(String xml);

    public String establecerAprobacionParadigmaHistoricoTesis2(String comandoXML);


}
