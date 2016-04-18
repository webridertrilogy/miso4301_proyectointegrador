/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author da-naran
 */
@Stateless
public class PersonaBean implements PersonaRemote, PersonaLocal {

    private final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.TesisBeanRemote";

    @EJB
    private ConstanteRemote constanteBean;

    @EJB
    private PersonaFacadeRemote personaFacade;

    private ParserT parser;
    private ServiceLocator serviceLocator;

    public PersonaBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            personaFacade = (PersonaFacadeRemote)serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
        } catch (Exception e) {
            Logger.getLogger(PersonaBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public String crearPersona(String comando) {
        return null;
    }

    public String editarPersona(String comando) {
        return null;
    }

    public String darPersonaPorCorreo(String comando) {
        try {
            parser.leerXML(comando);
            Secuencia secCorreo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null) {
                String correo = secCorreo.getValor();
                if(!correo.contains("@"))
                    correo += "@uniandes.edu.co";
                Persona persona = personaFacade.findByCorreo(correo);
                if (persona != null) {
                    Secuencia secPersona = pasarPersonaASecuencia(persona);
                    ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
                    secuencias.add(secPersona);
                    return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PERSONA_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                } else {
                    ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PERSONA_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.PERSONA_ERR_0001, new ArrayList<Secuencia>());
                }
            } else {
                throw new Exception("Error- xml mal formado en metodo darPersonaPorCorreo : PersonaBean");
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private Secuencia pasarPersonaASecuencia(Persona p) {

         SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Secuencia secPersona = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA), null);
        if (p.getId() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), (p.getId() != null) ? p.getId().toString() : ""));
        }

        if (p.getNombres() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), (p.getNombres() != null) ? p.getNombres() : ""));
        }

        if (p.getApellidos() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), (p.getApellidos() != null) ? p.getApellidos() : ""));
        }
        if (p.getCorreo() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), (p.getCorreo() != null) ? p.getCorreo() : ""));
        }
        if (p.getCelular() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), (p.getCelular() != null) ? p.getCelular() : ""));
        }
        if (p.getCiudadNacimiento() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD), (p.getCiudadNacimiento() != null) ? p.getCiudadNacimiento() : ""));
        }
        if (p.getCodigo() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), (p.getCodigo() != null) ? p.getCodigo() : ""));
        }
        if (p.getDireccionResidencia() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION), (p.getDireccionResidencia() != null) ? p.getDireccionResidencia() : ""));
        }
        if (p.getExtension() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION), (p.getExtension() != null) ? p.getExtension() : ""));
        }
        if (p.getFechaNacimiento() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), (p.getFechaNacimiento() != null) ? sdf.format(p.getFechaNacimiento()) : ""));
        }
        if (p.getNumDocumentoIdentidad() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO), (p.getNumDocumentoIdentidad() != null) ? p.getNumDocumentoIdentidad() : ""));
        }
        if (p.getTelefono() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO), (p.getTelefono() != null) ? p.getTelefono() : ""));
        }
        if ((Boolean)p.isActivo() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA), ((Boolean) p.isActivo() != null) ? Boolean.toString(p.isActivo()) : ""));
        }
        if (p.getCorreoAlterno() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO), (p.getCorreoAlterno() != null) ? p.getCorreoAlterno() : ""));
        }
        if (p.getTipoDocumento() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), (p.getTipoDocumento() != null && p.getTipoDocumento().getTipo() != null) ? p.getTipoDocumento().getTipo() : ""));
        }
        if (p.getPais() != null) {
            secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), (p.getPais() != null && p.getPais().getNombre() != null) ? p.getPais().getNombre() : ""));
        }
        return secPersona;
    }
}
