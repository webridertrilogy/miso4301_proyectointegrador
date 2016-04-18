package co.uniandes.sisinfo.serviciosnegocio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.InformacionEmpresa;
import co.uniandes.sisinfo.entities.Proponente;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.EstudiantePosgradoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.HojaVidaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.InformacionEmpresaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.OfertaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ProponenteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TipoAsistenciaGraduadaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;

/**
 * Servicios de administración de proponentes
 * @author David Naranjo, Camilo Cortés, Marcela Morales
 */
@Stateless
@EJB(name = "ProponenteBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ProponenteLocal.class)
public class ProponenteBean implements ProponenteRemote, ProponenteLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    //Locales
    @EJB
    private ProponenteFacadeLocal proponenteFacade;
    @EJB
    private InformacionEmpresaFacadeLocal empresaFacade;
    @EJB
    private TipoAsistenciaGraduadaFacadeLocal tipoAsistenciaFacade;
    @EJB
    private InformacionEmpresaFacadeLocal informacionEmpresaFacade;
    @EJB
    private HojaVidaFacadeLocal hojaVidaFacade;
    @EJB
    private EstudiantePosgradoFacadeLocal estudiantePostgradoFacade;
    @EJB
    private OfertaFacadeLocal ofertaFacade;
    @EJB
    private InformacionAcademicaFacadeRemote informacionAcademicaFacade;
    //Remotos
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private EstudianteFacadeRemote estudianteFacade;
    @EJB
    private PeriodoFacadeRemote periodoFacade;
    @EJB
    private PaisFacadeRemote paisFacade;
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentoFacade;
    //Útiles
    private ServiceLocator serviceLocator;
    private ConstanteRemote constanteBean;
    private ParserT parser;
    private ConversorBolsaEmpleo conversor;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ProponenteBean
     */
    public ProponenteBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            conversor = new ConversorBolsaEmpleo(constanteBean, estudianteFacade, personaFacade, periodoFacade, paisFacade, tipoAsistenciaFacade, tipoDocumentoFacade, informacionAcademicaFacade, hojaVidaFacade, estudiantePostgradoFacade, proponenteFacade, ofertaFacade, informacionEmpresaFacade);
        } catch (NamingException ex) {
            Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public String actualizarProponente(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PROPONENTE));
            Proponente proponente = proponenteFacade.find(secId.getValorLong());
            if(proponente == null){
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0110, new ArrayList<Secuencia>());
            }

            //Información de la persona
            Persona persona = proponente.getPersona();
            Secuencia secNombres = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
            if(secNombres != null){
                persona.setNombres(secNombres.getValor());
            }
            Secuencia secApellidos = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
            if(secApellidos != null){
                persona.setApellidos(secApellidos.getValor());
            }
            Secuencia secTelefono = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO));
            if(secTelefono != null){
                persona.setTelefono(secTelefono.getValor());
            }
            Secuencia secCelular = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR));
            if(secCelular != null){
                persona.setCelular(secCelular.getValor());
            }
            Secuencia secExtension = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION));
            if(secExtension != null){
                persona.setExtension(secExtension.getValor());
            }
            Secuencia secCorreo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if(secCorreo != null){
                persona.setCorreo(secCorreo.getValor());
            }
            getPersonaFacade().edit(persona);

            //Información empresarial
            Secuencia secEsEmpresa = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ES_EMPRESA));
            if(secEsEmpresa != null && Boolean.parseBoolean(secEsEmpresa.getValor())){
                InformacionEmpresa empresa = proponente.getInformacionEmpresa();
                if(empresa == null){
                    empresa = new InformacionEmpresa();
                    getEmpresaFacade().create(empresa);
                }
                Secuencia secCargo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGO));
                if(secCargo != null){
                    empresa.setCargoContactoEmpresa(secCargo.getValor());
                }
                Secuencia secNomEmpresa = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_EMPRESA));
                if(secNomEmpresa != null){
                    empresa.setNombreEmpresa(secNomEmpresa.getValor());
                }
                getEmpresaFacade().edit(empresa);
                proponente.setInformacionEmpresa(empresa);
            } 
            getProponenteFacade().edit(proponente);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_OFERTA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0113, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarProponentePorLogin(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secLogin = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LOGIN));
            String correo = secLogin.getValor();
            if (!correo.contains("@")) {
                correo += getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
            }
            Proponente proponente = proponenteFacade.findByCorreo(correo);
            if(proponente == null){
                proponente = getConversor().crearProponenteAPartirDePersona(correo);
            }
            Secuencia secProponente = getConversor().crearSecuenciaProponente(proponente);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secProponente);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception e) {
            try {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0113, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarProponente(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PROPONENTE));
            Proponente proponente = proponenteFacade.find(secId.getValorLong());
            if(proponente == null){
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0110, new ArrayList<Secuencia>());
            }
            Secuencia secProponente = getConversor().crearSecuenciaProponente(proponente);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secProponente);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception e) {
            try {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0113, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarProponentes(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secTipo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO));
            Collection<Proponente> proponentes = proponenteFacade.findAll();
            if (secTipo.getValor().equals(getConstanteBean().getConstante(Constantes.VAL_TAG_EMPRESA))) {
                proponentes = proponenteFacade.findByTipoEmpresa();
            }
            Secuencia secProponentes = getConversor().crearSecuenciaProponentes(proponentes);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secProponentes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception e) {
            try {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0113, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String crearProponente(String comando) {
        try {
            getParser().leerXML(comando);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secProponente = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROPONENTE));
            getConversor().crearProponenteAPartirDeSecuencia(secProponente);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception e) {
            try {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0113, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String eliminarProponente(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PROPONENTE));
            Proponente prponente = proponenteFacade.find(secId.getValorLong());
            if (prponente == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0110, new ArrayList());
            }
            proponenteFacade.remove(prponente);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());

        } catch (Exception e) {
            try {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROPONENTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0113, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProponenteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    //---------------------------------------
    // Métodos privados
    //---------------------------------------
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private InformacionEmpresaFacadeLocal getEmpresaFacade() {
        return empresaFacade;
    }

    private PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private ProponenteFacadeLocal getProponenteFacade() {
        return proponenteFacade;
    }

    private ConversorBolsaEmpleo getConversor() {
        return new ConversorBolsaEmpleo(constanteBean, estudianteFacade, personaFacade, periodoFacade, paisFacade, tipoAsistenciaFacade, tipoDocumentoFacade, informacionAcademicaFacade, hojaVidaFacade, estudiantePostgradoFacade, proponenteFacade, ofertaFacade, informacionEmpresaFacade);
    }
}
