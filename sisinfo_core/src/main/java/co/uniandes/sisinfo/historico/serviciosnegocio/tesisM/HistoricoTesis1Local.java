package co.uniandes.sisinfo.historico.serviciosnegocio.tesisM;

import javax.ejb.Local;

/**
 * Interface Local - Servicios de negocio para Históricos de Tesis 1
 * @author Marcela Morales
 */
@Local
public interface HistoricoTesis1Local {

    String pasarTesis1RechazadaAHistorico(String xml);

    String pasarTesis1RetiradasAHistorico(String xml);

    String pasarTesis1PerdidasAHistorico(String xml);

    String pasarTesis1TerminadasAHistorico(String xml);

    public String establecerAprobacionParadigmaHistoricoTesis1(String comandoXML);
}
