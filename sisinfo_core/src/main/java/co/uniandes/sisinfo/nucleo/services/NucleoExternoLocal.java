package co.uniandes.sisinfo.nucleo.services;

import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Núcleo Externo
 * @author Marcela Morales
 */
@Local
public interface NucleoExternoLocal {

    /**
     * Método que se encarga de resolver un comando dado en xml
     * @param comandoXML - cadena en la cual se define el comando
     * @return String: cadena de respuesta de acuerdo al comando dado
     * @throws java.lang.Exception
     */
    String resolverComandoExterno(String comandoXML) throws Exception;
}
