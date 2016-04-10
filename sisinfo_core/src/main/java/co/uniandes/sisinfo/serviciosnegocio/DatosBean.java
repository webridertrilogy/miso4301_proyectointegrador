/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.entities.soporte.Pais;
import co.uniandes.sisinfo.entities.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.soporte.Ciudad;
import co.uniandes.sisinfo.entities.soporte.Departamento;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.CiudadFacade;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.CiudadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.DepartamentoFacadeRemote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Administración de datos
 */
@Stateless
@EJB(name = "DatosBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.DatosLocal.class)
public class DatosBean implements DatosRemote, DatosLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * PaisFacade
     */
    @EJB
    private PaisFacadeRemote paisFacade;
    /**
     * TipoDocumentoFacade
     */
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentoFacade;
    /**
     * DepartamentoFacade
     */
    @EJB
    private DepartamentoFacadeRemote departamentoFacade;
    /**
     * CiudadFacade
     */
    @EJB
    private CiudadFacadeRemote ciudadFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    private ConversorDatosEventoExterno conversor;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de AlertaBean
     */
    public DatosBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            tipoDocumentoFacade = (TipoDocumentoFacadeRemote) serviceLocator.getRemoteEJB(TipoDocumentoFacadeRemote.class);
            departamentoFacade = (DepartamentoFacadeRemote) serviceLocator.getRemoteEJB(DepartamentoFacadeRemote.class);
            paisFacade = (PaisFacadeRemote) serviceLocator.getRemoteEJB(PaisFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(DatosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConversorDatosEventoExterno getConversor() {
        if (conversor == null) {
            conversor = new ConversorDatosEventoExterno(constanteBean);
        }
        return conversor;
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String darPaises(String xml) {
        String retorno = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
        try {
            try {
                getParser().leerXML(xml);
            } catch (Exception e) {
                try {
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_NACIONALIDADES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            }

            ArrayList<Pais> paises = (ArrayList<Pais>) getPaisFacade().findAll();

            if (paises.size() == 0) {
                try {
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_NACIONALIDADES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0051, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
            }

            //----Ordena el ArrayList de cursos
            Collections.sort(paises, new Comparator<Pais>() {

                public int compare(Pais o1, Pais o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });

            Secuencia secPaises = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAISES), "");
            Secuencia secPais = null;

            // Pone a Colombia de primero en la lista
            secPais = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), "Colombia");
            secPaises.agregarSecuencia(secPais);
            //Lista los demas paises
            for (Pais pais : paises) {
                if (pais.getNombre().equals("Colombia")) {
                    continue;
                }
                secPais = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), pais.getNombre());
                secPaises.agregarSecuencia(secPais);
            }

            secuencias.add(secPaises);
            retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_NACIONALIDADES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0052, parametros);
            return retorno;
        } catch (Exception ex) {
            try {
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_NACIONALIDADES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex2) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    /**
     * Retorna el parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna TipoDocumentoFacade
     * @return tipoDocumentoFacade TipoDocumentoFacade
     */
    private TipoDocumentoFacadeRemote getTipoDocumentoFacade() {
        return tipoDocumentoFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    /**
     * Retorna PaisFacade
     * @return paisFacade PaisFacade
     */
    private PaisFacadeRemote getPaisFacade() {
        return paisFacade;
    }

    @Override
    public String darDepartamentos(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            ArrayList<Departamento> departamentos = (ArrayList<Departamento>) departamentoFacade.findAll();
            //Ordena los departamento en orden alfabetico
            Collections.sort(departamentos, new Comparator<Departamento>() {

                public int compare(Departamento o1, Departamento o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });
            Secuencia secDepartamentos = getConversor().consultarDepartamentos(departamentos);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();

            secuencias.add(secDepartamentos);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DEPARTAMENTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public String darCiudadesPorDepartamento(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            // System.out.println(this.getClass()+"-"+getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            Secuencia secNombreDepartamento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            String nombreDepartamento = secNombreDepartamento.getValor();
            Departamento departamento = departamentoFacade.findDepartamentoByNombre(nombreDepartamento);

            Collection<Ciudad> ciudades = departamento.getCuidades();
            Secuencia secCiudades = getConversor().consultarCiudades(ciudades);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secCiudades);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_CIUDADES_POR_DEPARTAMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public String darTiposDocumento(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            Collection<TipoDocumento> tiposDocumento = tipoDocumentoFacade.findAll();
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secTiposDocumento = getConversor().consultarTiposDocumento(tiposDocumento);

            secuencias.add(secTiposDocumento);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_TIPOS_DOCUMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }
}
