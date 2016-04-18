package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;

/**
 * Servicios de consulta y edición de datos de usuarios
 * @author Marcela Morales
 */
@Stateless
public class ConsultaRolBean implements ConsultaRolBeanRemote, ConsultaRolBeanLocal {
    
    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private RolFacadeRemote rolFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private UsuarioFacadeRemote usuarioFacade;
    @EJB
    private PaisFacadeRemote paisFacade;
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentoFacade;
    private ParserT parser;
    private ServiceLocator serviceLocator;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public ConsultaRolBean(){
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            rolFacade = (RolFacadeRemote) serviceLocator.getRemoteEJB(RolFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            usuarioFacade= (UsuarioFacadeRemote) serviceLocator.getRemoteEJB(UsuarioFacadeRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            paisFacade = (PaisFacadeRemote) serviceLocator.getRemoteEJB(PaisFacadeRemote.class);
            tipoDocumentoFacade = (TipoDocumentoFacadeRemote) serviceLocator.getRemoteEJB(TipoDocumentoFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ConsultaRolBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------------------------------------------
    // MÉTODOS
    //----------------------------------------------
    public String consultarConstantesRoles(String xml) {
        try {
            getParser().leerXML(xml);
            List<Rol> roles = getRolFacade().findAll();
            Secuencia secuenciaRoles = crearSecuenciaRoles(roles);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secuenciaRoles);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ROLES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ROL_MSJ_001, new ArrayList());
        } catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ROLES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ROL_ERR_001, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarUsuarios(String xml){
        try {
            getParser().leerXML(xml);
            List<Usuario> usuarios = getUsuarioFacade().findAll();
            Secuencia secuenciaUsuarios = crearSecuenciaUsuarios(usuarios);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secuenciaUsuarios);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_USUARIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_001, new ArrayList());
        } catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_USUARIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.USER_ERR_001, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarUsuario(String xml){
        try {
            getParser().leerXML(xml);
            Long idUsuario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValorLong();
            Usuario usuario = getUsuarioFacade().find(idUsuario);

            Secuencia secuenciaUsuario = crearSecuenciaUsuario(usuario);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secuenciaUsuario);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_001, new ArrayList());
        } catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.USER_ERR_001, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String guardarRolUsuarios(String xml){
        try {
            getParser().leerXML(xml);
            Secuencia secUsuarios = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIOS));
            ArrayList<Secuencia> secuenciasUsuarios = secUsuarios.getSecuencias();
            for (Secuencia secuencia : secuenciasUsuarios) {
                //Usuario
                String idUsuario = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL)).getValor();
                Usuario usuario = getUsuarioFacade().find(Long.parseLong(idUsuario));
                //Roles nuevos del usuario
                Collection<Rol> roles = new ArrayList<Rol>();
                Secuencia secRoles = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROLES));
                ArrayList<Secuencia> secuenciasRoles = secRoles.getSecuencias();
                for(Secuencia secuencia1 : secuenciasRoles){
                    String nombreRol = secuencia1.getValor();
                    Rol rol = getRolFacade().findByDescripcion(nombreRol);
                    roles.add(rol);
                }
                usuario.setRoles(roles);
                //Estado activo nuevo del usuario
                Secuencia secActiva = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA));
                if(secActiva != null && usuario.getPersona() != null){
                    Persona persona = usuario.getPersona();
                    persona.setActivo(Boolean.parseBoolean(secActiva.getValor()));
                    personaFacade.edit(persona);
                    usuario.setPersona(persona);
                }
                getUsuarioFacade().edit(usuario);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_GUARDAR_CAMBIOS_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_002, new ArrayList());
        } catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_CAMBIOS_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.USER_ERR_002, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String crearUsuario(String xml){
        try {
            getParser().leerXML(xml);
            Secuencia secUsuario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO));
            Secuencia secCorreo = secUsuario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            
            //Creación de usuario
            Usuario usuario = getUsuarioFacade().findByLogin(secCorreo.getValor());
            if(usuario == null){
                usuario = new Usuario();
                usuario.setEsPersona(false);
                usuario.setPersona(null);
                usuario.setRoles(new ArrayList<Rol>());
            }
            
            //Adición de roles
            Secuencia secRoles = secUsuario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROLES));
            if(secRoles != null){
                Collection<Rol> roles = new ArrayList<Rol>();
                ArrayList<Secuencia> secuenciasRoles = secRoles.getSecuencias();
                for(Secuencia secuencia : secuenciasRoles){
                    String nombreRol = secuencia.getValor();
                    Rol rol = getRolFacade().findByDescripcion(nombreRol);
                    roles.add(rol);
                }
                usuario.setRoles(roles);
            }

            //Validación: ¿El tipo de usuario es persona?
            String esPersona = secUsuario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ES_PERSONA)).getValor();
            if(Boolean.parseBoolean(esPersona)){
                //Creación de persona
                boolean personaExistente = false;
                Persona persona = getPersonaFacade().findByCorreo(secCorreo.getValor());
                if(persona != null)
                    personaExistente = true;
                else
                    persona = new Persona();

                Secuencia secPersona = secUsuario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA));
                if(secPersona != null){
                    Secuencia secEsActivo = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA));
                    if(secEsActivo != null){
                        persona.setActivo(Boolean.parseBoolean(secEsActivo.getValor()));
                    }
                    Secuencia secApellidos = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
                    if(secApellidos != null){
                        persona.setApellidos(secApellidos.getValor());
                    }
                    Secuencia secCelular = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR));
                    if(secCelular != null){
                        persona.setCelular(secCelular.getValor());
                    }
                    Secuencia secCiudadNacimiento = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD));
                    if(secCiudadNacimiento != null){
                        persona.setCiudadNacimiento(secCiudadNacimiento.getValor());
                    }
                    Secuencia secCodigo = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE));
                    if(secCodigo != null){
                        persona.setCodigo(secCodigo.getValor());
                    }
                    Secuencia secCorreoPersona = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
                    if(secCorreoPersona != null){
                        persona.setCorreo(secCorreoPersona.getValor());
                    }
                    Secuencia secCorreoAlterno = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO));
                    if(secCorreoAlterno != null){
                        persona.setCorreoAlterno(secCorreoAlterno.getValor());
                    }
                    Secuencia secDireccionResidencia = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA));
                    if(secDireccionResidencia != null){
                        persona.setDireccionResidencia(secDireccionResidencia.getValor());
                    }
                    Secuencia secExtension = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION));
                    if(secExtension != null){
                        persona.setExtension(secExtension.getValor());
                    }
                    Secuencia secFechaNacimiento = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO));
                    if(secFechaNacimiento != null){
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", new Locale("en"));
                        Date fecha = sdf.parse(secFechaNacimiento.getValor());
                        persona.setFechaNacimiento(new Timestamp(fecha.getTime()));
                    }
                    Secuencia secNombres = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
                    if(secNombres != null){
                        persona.setNombres(secNombres.getValor());
                    }
                    Secuencia secTelefono = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO));
                    if(secTelefono != null){
                        persona.setTelefono(secTelefono.getValor());
                    }
                    Secuencia secNumDocumentoIdentidad = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO));
                    if(secNumDocumentoIdentidad != null){
                        persona.setNumDocumentoIdentidad(secNumDocumentoIdentidad.getValor());
                    }
                    Secuencia secTipoDocumento = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO));
                    if(secTipoDocumento != null){
                        TipoDocumento tipo = getTipoDocumentoFacade().findByDescripcion(secTipoDocumento.getValor());
                        persona.setTipoDocumento(tipo);
                    }
                    Secuencia secNombrePais = secPersona.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS));
                    if(secNombrePais != null){
                        Pais pais = getPaisFacade().findByNombre(secNombrePais.getValor());
                        persona.setPais(pais);
                    }
                }

                if(personaExistente)
                    getPersonaFacade().edit(persona);
                else
                    getPersonaFacade().create(persona);
                
                //Asignación de persona a usuario
                Persona personaBuscada = getPersonaFacade().findByCorreo(persona.getCorreo());
                usuario.setEsPersona(true);
                usuario.setPersona(personaBuscada);
            }
            //Creación de usuario
            getUsuarioFacade().create(usuario);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_003, new ArrayList());
        } catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_USUARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.USER_ERR_003, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarConstantesPais(String xml) {
        try {
            getParser().leerXML(xml);
            List<Pais> paises = getPaisFacade().findAll();
            Secuencia secuenciaPaises = crearSecuenciaPaises(paises);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secuenciaPaises);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PAISES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_004, new ArrayList());
        } catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PAISES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.USER_ERR_004, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarConstantesTipoDocumento(String xml) {
        try {
            getParser().leerXML(xml);
            List<TipoDocumento> tipos = getTipoDocumentoFacade().findAll();
            Secuencia secuenciaTipos = crearSecuenciaTiposDocumento(tipos);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secuenciaTipos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_DOCUMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.USER_MSJ_005, new ArrayList());
        } catch (Exception e) {
             try {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, e);
                String respuesta = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_TIPOS_DOCUMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.USER_ERR_005, new LinkedList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(CargaGrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    //----------------------------------------------
    // MÉTODOS PARA CREACIÓN DE SECUENCIAS
    //----------------------------------------------
    private Secuencia crearSecuenciaRoles(List<Rol> roles){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROLES), "");
        Iterator<Rol> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Rol rol = iterator.next();
            Secuencia secRol = crearSecuenciaRol(rol);
            secuencia.agregarSecuencia(secRol);
        }
        return secuencia;
    }

    private Secuencia crearSecuenciaRol(Rol rol){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL), "");
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), rol.getId().toString()));
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL), rol.getRol()));
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), rol.getDescripcion()));
        return secuencia;
    }

    private Secuencia crearSecuenciaUsuarios(List<Usuario> usuarios){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIOS), "");
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            Secuencia secUsuario = crearSecuenciaUsuario(usuario);
            secuencia.agregarSecuencia(secUsuario);
        }
        return secuencia;
    }

    private Secuencia crearSecuenciaUsuario(Usuario usuario){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO), "");
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), usuario.getId().toString()));
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ES_PERSONA), Boolean.toString(usuario.isPersona())));
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA), (usuario.getPersona() != null && (Boolean) usuario.getPersona().isActivo() != null) ? Boolean.toString(usuario.getPersona().isActivo()) : "false"));
        if(usuario.isPersona() && usuario.getPersona() != null){
            secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), usuario.getPersona().getNombres()));
            secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), usuario.getPersona().getApellidos()));
            secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), usuario.getPersona().getCodigo()));
            secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), usuario.getPersona().getCorreo()));
        }else{
            secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), ""));
            secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), ""));
            secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), ""));
            secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), ""));
        }
        Secuencia secRoles = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROLES), "");
        Iterator<Rol> iter = usuario.getRoles().iterator();
        while (iter.hasNext()) {
            secRoles.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL), iter.next().getDescripcion()));
        }
        secuencia.agregarSecuencia(secRoles);
        return secuencia;
    }

    private Secuencia crearSecuenciaPaises(List<Pais> paises){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAISES), "");
        Iterator<Pais> iterator = paises.iterator();
        while (iterator.hasNext()) {
            Pais pais = iterator.next();
            Secuencia secPais = crearSecuenciaPais(pais);
            secuencia.agregarSecuencia(secPais);
        }
        return secuencia;
    }

    private Secuencia crearSecuenciaPais(Pais pais){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), "");
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), pais.getId().toString()));
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), pais.getNombre()));
        return secuencia;
    }

    private Secuencia crearSecuenciaTiposDocumento(List<TipoDocumento> tiposDocumento){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_DOCUMENTO), "");
        Iterator<TipoDocumento> iterator = tiposDocumento.iterator();
        while (iterator.hasNext()) {
            TipoDocumento tipoDocumento = iterator.next();
            Secuencia secTipoDocumento = crearSecuenciaTipoDocumento(tipoDocumento);
            secuencia.agregarSecuencia(secTipoDocumento);
        }
        return secuencia;
    }

    private Secuencia crearSecuenciaTipoDocumento(TipoDocumento tipoDocumento){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), "");
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tipoDocumento.getId().toString()));
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO), tipoDocumento.getTipo()));
        secuencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), tipoDocumento.getDescripcion()));
        return secuencia;
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        return parser;
    }

    private RolFacadeRemote getRolFacade() {
        return rolFacade;
    }

    private PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private UsuarioFacadeRemote getUsuarioFacade() {
        return usuarioFacade;
    }

    private PaisFacadeRemote getPaisFacade() {
        return paisFacade;
    }

    private TipoDocumentoFacadeRemote getTipoDocumentoFacade() {
        return tipoDocumentoFacade;
    }
}
