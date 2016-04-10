/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.seguridad;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import co.uniandes.sisinfo.seguridad.Protector;

/**
 * Servicios de Autorización
 */
@Stateless
@EJB(name = "AutorizacionBean", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.seguridad.AutorizacionLocal.class)
public class AutorizacionBean implements AutorizacionRemote, AutorizacionLocal {

    private ParserT parser;
    @EJB
    private UsuarioFacadeRemote fachadaUsuario;
    private AccesoLDAP ldap;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private EstudianteFacadeRemote estudianteFacade;
    @EJB
    private ProfesorFacadeRemote profesorFacade;

    public AutorizacionBean() {
        try {
            serviceLocator = new ServiceLocator();
            fachadaUsuario = (UsuarioFacadeRemote) serviceLocator.getRemoteEJB(UsuarioFacadeRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            personaFacade = (PersonaFacadeRemote)serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            estudianteFacade = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    public void setParser(ParserT parser) {
        this.parser = parser;
    }

    public AccesoLDAP getLdap() {
        ldap = new AccesoLDAP();
        return ldap;
    }

    public void setLdap(AccesoLDAP ldap) {
        this.ldap = ldap;
    }

    public UsuarioFacadeRemote getFachadaUsuario() {
        return fachadaUsuario;
    }

    public void setFachadaUsuario(UsuarioFacadeRemote dachadaUsuario) {
        this.fachadaUsuario = dachadaUsuario;
    }

    @Override
    public String autorizarYDarDatosBasicosSesion(String xml) {
        Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, "getFachadaUsuario().findByLogin(login)");
        String ldapAns = null;
        String retorno = null;
        String correo = null;
        String login = null;
        String pass = null;
        String passEncriptado = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        try {
            try {
                getParser().leerXML(xml);
            } catch (Exception ex) {
                Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, "leerXML(xml)", ex);
            }
            String auxlogin = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            correo = Protector.decryptUser(auxlogin).substring(19);
            if(!correo.contains("@")){
                correo = correo + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
            }
            login = correo.split("@")[0];
            passEncriptado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTRASENHA)).getValor();
            pass = Protector.decryptPass(passEncriptado).substring(19);
            Usuario user = getFachadaUsuario().findByLogin(correo);
            Boolean enPrueba = Boolean.parseBoolean(constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_PRUEBA));
            if (user != null) {
                if (user.isPersona()) {
                    ldapAns = getLdap().autorizacion(login, pass, enPrueba);
                } else {
                    ldapAns = getLdap().autorizacionUsuarioImpersonal(login, pass, enPrueba);
                }
                if (ldapAns.length() != 0) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametroLogin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                        Atributo atrParametroLogin = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_USUARIO);
                        secParametroLogin.agregarAtributo(atrParametroLogin);
                        parametros.add(secParametroLogin);

                        boolean esEstudiante = false;
                        ArrayList<Secuencia> secuenciasRoles = new ArrayList<Secuencia>();
                        Collection<Rol> listaRoles = user.getRoles();
                        for (Iterator<Rol> it = listaRoles.iterator(); it.hasNext();) {
                            Rol rol = it.next();
                            Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), rol.getRol());
                            if (rol.getRol().equals(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE))) {
                                esEstudiante = true;
                            }

                            secuenciasRoles.add(secuenciaRol);
                        }
                        Secuencia secuenciaRoles = new Secuencia(new ArrayList<Atributo>(), secuenciasRoles);
                        secuenciaRoles.setNombre(getConstanteBean().getConstante(Constantes.TAG_ROLES));
                        secuencias.add(secuenciaRoles);

                        if (!esEstudiante) {
                            Secuencia secuenciaNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), user.getPersona().getNombres());
                            secuencias.add(secuenciaNombres);
                            Secuencia secuenciaApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), user.getPersona().getApellidos());
                            secuencias.add(secuenciaApellidos);
                        } else {
                            Secuencia secuenciaNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), AccesoLDAP.obtenerNombres(login));
                            secuencias.add(secuenciaNombres);
                            Secuencia secuenciaApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), AccesoLDAP.obtenerApellidos(login));
                            secuencias.add(secuenciaApellidos);
                        }

                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0032, parametros);

                        // Creo un estudiante si no existe
                        //estudianteBean.crearEstudianteVacio(correo);

                        return retorno;
                    }  catch (CommunicationException comme){
                        try{
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.SESION_ERR_001, parametros);
                            return retorno;
                        } catch (Exception ex2) {
                            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                    }catch (Exception ex) {
                        Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                            return retorno;
                        } catch (Exception ex2) {
                            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                    }
                } else {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametroLogin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                        Atributo atrParametroLogin = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_USUARIO);
                        secParametroLogin.agregarAtributo(atrParametroLogin);
                        parametros.add(secParametroLogin);

                        Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                        ArrayList<Secuencia> secuenciasRoles = new ArrayList<Secuencia>();
                        secuenciasRoles.add(secuenciaRol);
                        Secuencia secuenciaRoles = new Secuencia(new ArrayList<Atributo>(), secuenciasRoles);
                        secuenciaRoles.setNombre(getConstanteBean().getConstante(Constantes.TAG_ROLES));
                        secuencias.add(secuenciaRoles);

                        //secuencias.add(secuenciaRol);
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0095, parametros);
                        return retorno;
                    } catch (Exception ex) {
                        Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                            return retorno;
                        } catch (Exception ex2) {
                            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                    }
                }
            } else {
                //Si el usuario no existe...
                ldapAns = getLdap().autorizacion(login, pass, enPrueba);
                if (ldapAns.length() != 0) {

                    Collection<String> rolesAAsignar = new ArrayList<String>();
                    //¿Es persona? : La persona existe pero no tiene un usuario asociado
                    Persona persona = personaFacade.findByCorreo(correo);
                    if(persona != null){
                        //¿Es estudiante y/o profesor? : si es persona, puede ser estudiante o profesor
                        Estudiante estudiante = estudianteFacade.findByCorreo(correo);
                        Profesor profesor = profesorFacade.findByCorreo(correo);
                        //Validaciones
                        if(estudiante != null)
                            rolesAAsignar.add(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));
                        if(profesor != null)
                            rolesAAsignar.add(getConstanteBean().getConstante(Constantes.ROL_PROFESOR));
                        if(estudiante == null && profesor == null)
                            rolesAAsignar.add(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE_EXTERNO));
                    }else{
                        rolesAAsignar.add(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE_EXTERNO));
                    }
                    
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametroLogin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                        Atributo atrParametroLogin = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_USUARIO);
                        secParametroLogin.agregarAtributo(atrParametroLogin);
                        parametros.add(secParametroLogin);

                        //Recorre los roles asignados anteriormente (estudiante y/o profesor, o estudiante externo)
                        ArrayList<Secuencia> secuenciasRoles = new ArrayList<Secuencia>();
                        for (Iterator<String> it = rolesAAsignar.iterator(); it.hasNext();) {
                            String rol = it.next();
                            Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), rol);
                            secuenciasRoles.add(secuenciaRol);
                        }
                        Secuencia secuenciaRoles = new Secuencia(new ArrayList<Atributo>(), secuenciasRoles);
                        secuenciaRoles.setNombre(getConstanteBean().getConstante(Constantes.TAG_ROLES));
                        secuencias.add(secuenciaRoles);

                        //Secuencia secuenciaNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), ldapAns.split("_")[1]);
                        Secuencia secuenciaNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), AccesoLDAP.obtenerNombres(login));
                        secuencias.add(secuenciaNombres);
                        //Secuencia secuenciaApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), ldapAns.split("_")[2]);
                        Secuencia secuenciaApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), AccesoLDAP.obtenerApellidos(login));
                        secuencias.add(secuenciaApellidos);
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0032, parametros);
                        return retorno;
                    } catch (CommunicationException comme) {
                        try{
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.SESION_ERR_001, parametros);
                            return retorno;
                        } catch (Exception ex2) {
                            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                     }
                    catch (Exception ex) {
                        Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0051, parametros);
                            return retorno;
                        } catch (Exception ex2) {
                            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                    }

                } else {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametroLogin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                        Atributo atrParametroLogin = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_USUARIO);
                        secParametroLogin.agregarAtributo(atrParametroLogin);
                        parametros.add(secParametroLogin);

                        //Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                        //secuencias.add(secuenciaRol);

                        Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                        ArrayList<Secuencia> secuenciasRoles = new ArrayList<Secuencia>();
                        secuenciasRoles.add(secuenciaRol);
                        Secuencia secuenciaRoles = new Secuencia(new ArrayList<Atributo>(), secuenciasRoles);
                        secuenciaRoles.setNombre(getConstanteBean().getConstante(Constantes.TAG_ROLES));
                        secuencias.add(secuenciaRoles);

                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0095, parametros);
                        return retorno;
                    } catch (Exception ex) {
                        Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                            return retorno;
                        } catch (Exception ex2) {
                            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                    }
                }
            }
        } catch (AuthenticationException ex) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametroLogin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                Atributo atrParametroLogin = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_USUARIO);
                secParametroLogin.agregarAtributo(atrParametroLogin);
                parametros.add(secParametroLogin);

                //Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                //secuencias.add(secuenciaRol);

                Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                ArrayList<Secuencia> secuenciasRoles = new ArrayList<Secuencia>();
                secuenciasRoles.add(secuenciaRol);
                Secuencia secuenciaRoles = new Secuencia(new ArrayList<Atributo>(), secuenciasRoles);
                secuenciaRoles.setNombre(getConstanteBean().getConstante(Constantes.TAG_ROLES));
                secuencias.add(secuenciaRoles);

                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0051, parametros);
                return retorno;
            } catch (Exception ex4) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex4);
            }
        } catch (NameNotFoundException ex) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametroLogin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                Atributo atrParametroLogin = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_USUARIO);
                secParametroLogin.agregarAtributo(atrParametroLogin);
                parametros.add(secParametroLogin);

                //Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                //secuencias.add(secuenciaRol);

                Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                ArrayList<Secuencia> secuenciasRoles = new ArrayList<Secuencia>();
                secuenciasRoles.add(secuenciaRol);
                Secuencia secuenciaRoles = new Secuencia(new ArrayList<Atributo>(), secuenciasRoles);
                secuenciaRoles.setNombre(getConstanteBean().getConstante(Constantes.TAG_ROLES));
                secuencias.add(secuenciaRoles);

                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0051, parametros);
                return retorno;
            } catch (Exception ex4) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex4);
            }
        }catch (Exception ex) {
            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametroLogin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                Atributo atrParametroLogin = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_USUARIO);
                secParametroLogin.agregarAtributo(atrParametroLogin);
                parametros.add(secParametroLogin);

                //Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                //secuencias.add(secuenciaRol);

                Secuencia secuenciaRol = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ROL), getConstanteBean().getConstante(Constantes.ROL_INDETERMINADO));
                ArrayList<Secuencia> secuenciasRoles = new ArrayList<Secuencia>();
                secuenciasRoles.add(secuenciaRol);
                Secuencia secuenciaRoles = new Secuencia(new ArrayList<Atributo>(), secuenciasRoles);
                secuenciaRoles.setNombre(getConstanteBean().getConstante(Constantes.TAG_ROLES));
                secuencias.add(secuenciaRoles);

                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex4) {
                Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex4);
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
            }
        }
        return null;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        if (constanteBean == null) {
            try {
                ServiceLocator serviceLocator = new ServiceLocator();
                constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);

            } catch (NamingException ex) {
                Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 

        return constanteBean;
    }

    public String estaRegistradoEnLDAP(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String login = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            boolean registrado=false;
            try{
                AccesoLDAP.obtenerNombres(login);
                registrado = true;
            } catch (CommunicationException comme){
                try{
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.SESION_ERR_001, parametros);
                } catch (Exception ex2) {
                    Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
            }catch(Exception e){
                registrado = false;
            }

            Secuencia secRegistrado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REGISTRADO),Boolean.toString(registrado));
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secRegistrado);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ESTA_REGISTRADO_LDAP), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(AutorizacionBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
  
    }


}
