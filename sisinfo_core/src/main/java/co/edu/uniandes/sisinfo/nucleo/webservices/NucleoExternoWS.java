package co.edu.uniandes.sisinfo.nucleo.webservices;

import co.uniandes.sisinfo.nucleo.services.NucleoExternoLocal;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ejb.Stateless;

/**
 * Servicios web del núcleo externo
 * @author Marcela Morales
 */
@WebService()
@Stateless()
public class NucleoExternoWS {

    //-------------------------------------------------
    // Referencias a beans de servicios
    //-------------------------------------------------
    @EJB
    private NucleoExternoLocal nucleoExterno;

    //-------------------------------------------------
    // Operaciones
    //-------------------------------------------------
    /**
     * Servicio web que se encarga de resolver un comando dado en xml
     * @param comandoXML - cadena en la cual se define el comando
     * @return String: cadena de respuesta de acuerdo al comando dado
     * @throws java.lang.Exception
     */
    @WebMethod(operationName = "resolverComandoExterno")
    public String resolverComandoExterno(@WebParam(name = "comandoXML")
    String comandoXML) throws Exception {
        return nucleoExterno.resolverComandoExterno(comandoXML);
    }
}
