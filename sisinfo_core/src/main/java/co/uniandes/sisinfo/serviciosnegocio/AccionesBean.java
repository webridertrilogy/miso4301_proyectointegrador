/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.bo.AccionBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.DirectorioInterfacesPorRol;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.DirectorioInterfacesPorRolFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Asistente
 */
@Stateless
public class AccionesBean implements AccionesBeanRemote, AccionesBeanLocal {
    
    private ParserT parser;

    private ServiceLocator serviceLocator;

    private ConversorAcciones conversorAcciones;

    @EJB
    private ConstanteRemote constanteBean;

    @EJB
    private CorreoRemote correoBean;

    @EJB
    private DirectorioInterfacesPorRolFacadeLocal directorioInterfacesPorRolFacade;

    public AccionesBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            conversorAcciones = new ConversorAcciones(constanteBean);
        } catch (NamingException ex) {
            Logger.getLogger(CorreoSinEnviarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String darAcciones(String xml) {
        try {
            getParser().leerXML(xml);
            long tmp = System.currentTimeMillis();
            String strRol = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL)).getValor();
            String strLogin = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LOGIN)).getValor();
            if(!strLogin.contains("@"))
                strLogin+=getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
            Collection<DirectorioInterfacesPorRol> directorios = directorioInterfacesPorRolFacade.buscarInterfacesActivasPorRol(strRol);
            Collection<AccionBO> acciones = new ArrayList();
            for (DirectorioInterfacesPorRol directorioInterfacesPorRol : directorios) {
                try{
                    Class claseInterfaz = Class.forName(directorioInterfacesPorRol.getDireccionInterfaz());
                    Method m = claseInterfaz.getMethod(getConstanteBean().getConstante(Constantes.VAL_NOMBRE_METODO_DAR_ACCIONES), String.class, String.class);
                    Object instance = serviceLocator.getRemoteEJB(Class.forName(directorioInterfacesPorRol.getDireccionInterfaz()));
                    Collection results = (Collection)m.invoke(instance, strRol,strLogin);
                    acciones.addAll(results);
                }catch(NoSuchMethodException nsme){
                    reportarErrorAccion("El método "+getConstanteBean().getConstante(Constantes.VAL_NOMBRE_METODO_DAR_ACCIONES)+
                            " no fue encontrado en la interfaz "+directorioInterfacesPorRol.getDireccionInterfaz());
                    directorioInterfacesPorRol.setActivo(false);
                    directorioInterfacesPorRolFacade.edit(directorioInterfacesPorRol);
                }catch(IllegalArgumentException iare){
                    reportarErrorAccion("La cantidad de parámetros especificados en el método "+getConstanteBean().getConstante(Constantes.VAL_NOMBRE_METODO_DAR_ACCIONES)+
                            " de la interfaz "+directorioInterfacesPorRol.getDireccionInterfaz()+" no concuerda con la cantidad de parámetros esperados. (Se esperaban dos parámetros, uno para el rol y otro para el login)");
                    directorioInterfacesPorRol.setActivo(false);
                    directorioInterfacesPorRolFacade.edit(directorioInterfacesPorRol);
                }catch(IllegalAccessException iace){
                    reportarErrorAccion("La visibilidad del método "+getConstanteBean().getConstante(Constantes.VAL_NOMBRE_METODO_DAR_ACCIONES)+
                            " de la interfaz "+directorioInterfacesPorRol.getDireccionInterfaz()+" no permite realizar llamados");
                    directorioInterfacesPorRol.setActivo(false);
                    directorioInterfacesPorRolFacade.edit(directorioInterfacesPorRol);
                }
            }
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(getConversorAcciones().crearSecuenciaAcciones(acciones));
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_ACCIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_ACCIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(CorreoAuditoriaBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void reportarErrorAccion(String causa){
        String destinatario = getConstanteBean().getConstante(Constantes.VAL_CORREO_SOPORTE_SISINFO);
        String mensaje = String.format(Notificaciones.MENSAJE_ERROR_OBTENIENDO_ACCIONES,causa);
        correoBean.enviarMail(destinatario, Notificaciones.ASUNTO_ERROR_OBTENIENDO_ACCIONES, null, null, null, mensaje);
    }

    public ParserT getParser() {
        return parser;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ConversorAcciones getConversorAcciones() {
        return conversorAcciones;
    }




 
}
