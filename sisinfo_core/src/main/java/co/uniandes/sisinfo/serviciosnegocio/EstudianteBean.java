package co.uniandes.sisinfo.serviciosnegocio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoCuentaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;

/**
 * Servicio de negocio: Administración de estudiantes
 */
@Stateless
@EJB(name = "EstudianteBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.EstudianteBeanLocal.class)
public class EstudianteBean implements EstudianteBeanRemote, EstudianteBeanLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @EJB
    private EstudianteFacadeRemote estudianteFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private PaisFacadeRemote paisFacade;
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentoFacade;
    @EJB
    private ProgramaFacadeRemote programaFacade;
    @EJB
    private TipoCuentaFacadeRemote tipoCuentaFacade;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacade;
    @EJB
    private InformacionAcademicaFacadeRemote infoAcademicaFacade;
    @EJB
    private UsuarioFacadeRemote usuarioFacade;
    @EJB
    private RolFacadeRemote rolFacade;
    
    private ParserT parser;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de EstudianteBean
     */
    public EstudianteBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            estudianteFacade = (EstudianteFacadeRemote)    serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(EstudianteBean.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String consultarEsEstudiantePorCorreo(String comando) {
        try {
            parser.leerXML(comando);
            
            String correo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Estudiante estudiante = estudianteFacade.findByCorreo(correo);

            Boolean esEstudiante = false;
            if (estudiante != null) {
                esEstudiante = true;
            }
            Secuencia secuenciaRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), esEstudiante.toString());

            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            secuencias.add(secuenciaRespuesta);

            return parser.generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ES_ESTUDIANTE_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0133, secuencias);
        } catch (Exception e) {
            try {
                Logger.getLogger(EstudianteBean.class.getName()).log(Level.SEVERE, null, e);
                e.printStackTrace();
                return parser.generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ES_ESTUDIANTE_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0104, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(EstudianteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String crearEstudiante(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
            if (secEstudiante != null) {
                crearEstudiantePersona(secEstudiante);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_ESTUDIANTE_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_ESTUDIANTE_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_ESTUDIANTE_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void crearEstudiantePersona(Secuencia secEstudiante) {
        //Extrae la información de la secuencia
        Secuencia secInfoPersonal = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL));
        String correo = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
        String codigo = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE)).getValor();
        String nombres = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
        String apellidos = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
        String celular = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR)).getValor();
        String tipoDocumento = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_DOCUMENTO)).getValor();
        String documentoDeIdentidad = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO)).getValor();
        String correoAlterno = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO)).getValor();
        String fechaNacimiento = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO)).getValor();
        String ciudadNacimiento = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD)).getValor();
        String paisNacimiento = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS)).getValor();
        String direccion = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION)).getValor();
        String telefono = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO)).getValor();
        String extension = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION)).getValor();
        Boolean esExterno = Boolean.parseBoolean(secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ES_EXTERNO)).getValor());

        Secuencia secInfoEmergencia = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA));
        String emergenciaNombres = secInfoEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
        String emergenciaApellidos = secInfoEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
        String emergenciaTelefono = secInfoEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA)).getValor();

        Secuencia secInfoCuenta = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS));
        String banco = secInfoCuenta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO)).getValor();
        String cuentaBancaria = secInfoCuenta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA)).getValor();
        String tipoCuenta = secInfoCuenta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA)).getValor();

        Secuencia secInfoAcademica = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA));
        String programa = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA)).getValor();
        String doblePrograma = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOBLE_PROGRAMA)).getValor();
        String nivelPrograma = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION)).getValor();
        String creditosAprobados = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS)).getValor();
        String creditosCursados = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS)).getValor();
        String creditosSemestreActual = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL)).getValor();
        String promedioAntepenultimo = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES)).getValor();
        String promedioPenultimo = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES)).getValor();
        String promedioTotal = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL)).getValor();
        String promedioUltimo = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO)).getValor();
        String semestreSegunCreditos = secInfoAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS)).getValor();

        //Crea y persiste la persona
        Persona persona = getPersonaFacade().findByCorreo(correo);
        if (persona == null) {
            persona = new Persona();
            persona.setActivo(true);
            persona.setApellidos(apellidos);
            persona.setCelular(celular);
            persona.setCiudadNacimiento(ciudadNacimiento);
            persona.setCodigo(codigo);
            persona.setCorreo(correo);
            persona.setCorreoAlterno(correoAlterno);
            persona.setDireccionResidencia(direccion);
            persona.setExtension(extension);
            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", new Locale("en"));
            try {
                Date fecha = formatoDeFecha.parse(fechaNacimiento);
                persona.setFechaNacimiento(new java.sql.Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(EstudianteBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            persona.setNombres(nombres);
            persona.setNumDocumentoIdentidad(documentoDeIdentidad);
            persona.setPais(getPaisFacade().findByNombre(paisNacimiento));
            persona.setTelefono(telefono);
            persona.setTipoDocumento(getTipoDocumentoFacade().findByTipo(tipoDocumento));
            getPersonaFacade().create(persona);
        }

        //Crea y persiste el usuario
        Usuario usuario = getUsuarioFacade().findByLogin(correo);
        if(usuario == null && !esExterno){
            usuario = new Usuario();
            usuario.setEsPersona(true);
            usuario.setPersona(getPersonaFacade().findByCorreo(correo));
            Collection<Rol> roles = new ArrayList<Rol>();
            Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));
            roles.add(rol);
            usuario.setRoles(roles);
            getUsuarioFacade().create(usuario);
        }else if(usuario == null && esExterno){
            usuario = new Usuario();
            usuario.setEsPersona(true);
            usuario.setPersona(getPersonaFacade().findByCorreo(correo));
            Collection<Rol> roles = new ArrayList<Rol>();
            Rol rol1 = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE_EXTERNO));
            Rol rol2 = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));
            roles.add(rol1);
            roles.add(rol2);
            usuario.setRoles(roles);
            getUsuarioFacade().create(usuario);
        } else if(!esExterno){
            //Si ya existe el usuario, se agrega el rol únicamente
            Collection<Rol> roles = usuario.getRoles();
            Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));
            roles.add(rol);
            usuario.setRoles(roles);
            getUsuarioFacade().edit(usuario);
        } else if(esExterno){
            //Si ya existe el usuario, se agrega el rol únicamente
            Collection<Rol> roles = usuario.getRoles();
            Rol rol1 = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE_EXTERNO));
            Rol rol2 = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));
            roles.add(rol1);
            roles.add(rol2);
            usuario.setRoles(roles);
            getUsuarioFacade().edit(usuario);
        }

        //Crea y persiste al estudiante
        Estudiante estudiante = getEstudianteFacade().findByCorreo(correo);
        if (estudiante == null) {
            estudiante = new Estudiante();
            estudiante.setAvisarEmergenciaApellidos(emergenciaApellidos);
            estudiante.setAvisarEmergenciaNombres(emergenciaNombres);
            estudiante.setBanco(banco);
            estudiante.setActivo(Boolean.TRUE); 
            estudiante.setCuentaBancaria(cuentaBancaria);
            estudiante.setPersona(getPersonaFacade().findByCorreo(correo));
            estudiante.setPrograma(getProgramaFacade().findByNombre(programa));
            estudiante.setDoblePrograma(getProgramaFacade().findByNombre(doblePrograma));
            estudiante.setTelefonoEmergencia(emergenciaTelefono);
            if(nivelPrograma != null && !nivelPrograma.equals(""))
                estudiante.setTipoCuenta(getTipoCuentaFacade().findByName(tipoCuenta).get(0));
            if(tipoCuenta != null && !tipoCuenta.equals(""))
                estudiante.setTipoEstudiante(getNivelFormacionFacade().findByName(nivelPrograma));

            //Información académica
            InformacionAcademica informacionAcademica = new InformacionAcademica();
            informacionAcademica.setCreditosAprobados(Double.parseDouble(creditosAprobados));
            informacionAcademica.setCreditosCursados(Double.parseDouble(creditosCursados));
            informacionAcademica.setCreditosMonitoriasISISEsteSemestre(0.0);
            informacionAcademica.setCreditosSemestreActual(Double.parseDouble(creditosSemestreActual));
            informacionAcademica.setPromedioAntepenultipo(Double.parseDouble(promedioAntepenultimo));
            informacionAcademica.setPromedioPenultimo(Double.parseDouble(promedioPenultimo));
            informacionAcademica.setPromedioTotal(Double.parseDouble(promedioTotal));
            informacionAcademica.setPromedioUltimo(Double.parseDouble(promedioUltimo));
            informacionAcademica.setSemestreSegunCreditos(semestreSegunCreditos);
            getInfoAcademicaFacade().create(informacionAcademica);
            estudiante.setInformacion_Academica(getInfoAcademicaFacade().findByCodigoEstudiante(codigo));

            getEstudianteFacade().create(estudiante);
        }
    }
    
    public String consultarEstudiantes(String comando) {
        try {
            getParser().leerXML(comando);
            Collection<Estudiante> estudiantes = getEstudianteFacade().findAll();
            Secuencia secEstudiantes = consultarEstudiantes(estudiantes);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secEstudiantes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTES_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTES_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarEstudiante(String comando){
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Estudiante estudiante = getEstudianteFacade().findByCorreo(correo);
            if(estudiante == null){
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ASISTENCIA_ERR_0006, new ArrayList());
            }
            Secuencia secEstudiante = consultarEstudiante(estudiante);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secEstudiante);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_ESTUDIANTE_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarEstudiante(String comando){
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Estudiante estudiante = getEstudianteFacade().findByCorreo(correo);
            getEstudianteFacade().remove(estudiante);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ESTUDIANTE_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_ESTUDIANTE_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String actualizarEstadoActivoEstudiante(String comando){
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String activo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA)).getValor();
            if(!correo.contains("@uniandes.edu.co"))
                correo += "@uniandes.edu.co";
            Estudiante estudiante = getEstudianteFacade().findByCorreo(correo);
            if(estudiante != null){
                estudiante.setActivo(Boolean.parseBoolean(activo));
                getEstudianteFacade().edit(estudiante);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_ESTADO_ACTIVO_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_ESTADO_ACTIVO_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    //HELPERS
    private Secuencia consultarEstudiantes(Collection<Estudiante> estudiantes){
        Secuencia secEstudiantes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTES), "");
        for (Iterator<Estudiante> it = estudiantes.iterator(); it.hasNext();) {
            Estudiante estudiante = it.next();
            Secuencia secEstudiante = consultarEstudiante(estudiante);
            secEstudiantes.agregarSecuencia(secEstudiante);
        }
        return secEstudiantes;
    }

    private Secuencia consultarEstudiante(Estudiante estudiante){
        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        //Información personal
        Secuencia secInfoPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), "");
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ESTUDIANTE), (estudiante.getId() != null) ? estudiante.getId() + "" : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), (estudiante.getPersona() != null && estudiante.getPersona().getCorreo() != null) ? estudiante.getPersona().getCorreo() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), (estudiante.getPersona() != null && estudiante.getPersona().getCodigo() != null) ? estudiante.getPersona().getCodigo() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), (estudiante.getPersona() != null && estudiante.getPersona().getNombres() != null) ? estudiante.getPersona().getNombres() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), (estudiante.getPersona() != null && estudiante.getPersona().getApellidos() != null) ? estudiante.getPersona().getApellidos() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), (estudiante.getPersona() != null && estudiante.getPersona().getCelular() != null) ? estudiante.getPersona().getCelular() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_DOCUMENTO), (estudiante.getPersona() != null && estudiante.getPersona().getTipoDocumento() != null) ? estudiante.getPersona().getTipoDocumento().getTipo() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), (estudiante.getPersona() != null && estudiante.getPersona().getNumDocumentoIdentidad() != null) ? estudiante.getPersona().getNumDocumentoIdentidad() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO), (estudiante.getPersona() != null && estudiante.getPersona().getCorreoAlterno() != null) ? estudiante.getPersona().getCorreoAlterno() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), (estudiante.getPersona() != null && estudiante.getPersona().getFechaNacimiento() != null) ? estudiante.getPersona().getFechaNacimiento().toString() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD), (estudiante.getPersona() != null && estudiante.getPersona().getCiudadNacimiento() != null) ? estudiante.getPersona().getCiudadNacimiento() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), (estudiante.getPersona() != null && estudiante.getPersona().getPais() != null) ? estudiante.getPersona().getPais().getNombre() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION), (estudiante.getPersona() != null && estudiante.getPersona().getDireccionResidencia() != null) ? estudiante.getPersona().getDireccionResidencia() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO), (estudiante.getPersona() != null && estudiante.getPersona().getTelefono() != null) ? estudiante.getPersona().getTelefono() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION), (estudiante.getPersona() != null && estudiante.getPersona().getExtension() != null) ? estudiante.getPersona().getExtension() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA), (estudiante.getPersona() != null) ? estudiante.getPersona().isActivo() + "" : "false"));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), (estudiante.getActivo() != null) ? estudiante.getActivo() + "" : "false"));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ES_EXTERNO), "false"));
        secEstudiante.agregarSecuencia(secInfoPersonal);
        //Información de emergencia
        Secuencia secInfoEmergencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA), "");
        secInfoEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), (estudiante.getAvisarEmergenciaNombres() != null) ? estudiante.getAvisarEmergenciaNombres() : ""));
        secInfoEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), (estudiante.getAvisarEmergenciaApellidos() != null) ? estudiante.getAvisarEmergenciaApellidos() : ""));
        secInfoEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA), (estudiante.getTelefonoEmergencia() != null) ? estudiante.getTelefonoEmergencia() : ""));
        secEstudiante.agregarSecuencia(secInfoEmergencia);
        //Información de cuenta bancaria
        Secuencia secInfoCuenta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTROS), "");
        secInfoCuenta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO), (estudiante.getBanco() != null) ? estudiante.getBanco() : ""));
        secInfoCuenta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA), (estudiante.getCuentaBancaria() != null) ? estudiante.getCuentaBancaria() : ""));
        secInfoCuenta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA), (estudiante.getTipoCuenta() != null) ? estudiante.getTipoCuenta().getNombre() : ""));
        secEstudiante.agregarSecuencia(secInfoCuenta);
        //Información académica
        Secuencia secInfoAcademica = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), "");
        secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA), (estudiante.getPrograma() != null)? estudiante.getPrograma().getNombre() : ""));
        secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), (estudiante.getTipoEstudiante() != null) ? estudiante.getTipoEstudiante().getNombre() : ""));
        if(estudiante.getInformacion_Academica() != null){
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), estudiante.getInformacion_Academica().getCreditosAprobados()+""));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS), estudiante.getInformacion_Academica().getCreditosCursados()+""));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), estudiante.getInformacion_Academica().getCreditosSemestreActual()+""));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES), estudiante.getInformacion_Academica().getPromedioAntepenultipo()+""));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES), estudiante.getInformacion_Academica().getPromedioPenultimo()+""));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), estudiante.getInformacion_Academica().getPromedioPenultimo()+""));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO), estudiante.getInformacion_Academica().getPromedioUltimo()+""));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS), estudiante.getInformacion_Academica().getSemestreSegunCreditos()));
        }else{
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), "0.0"));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS), "0.0"));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), "0.0"));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES), "0.0"));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES), "0.0"));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), "0.0"));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO), "0.0"));
            secInfoAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS), "0.0"));
        }
        secEstudiante.agregarSecuencia(secInfoAcademica);
        return secEstudiante;
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        return (parser == null)? new ParserT() : parser;
    }

    private EstudianteFacadeRemote getEstudianteFacade() {
        return estudianteFacade;
    }

    private PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private PaisFacadeRemote getPaisFacade() {
        return paisFacade;
    }

    private TipoDocumentoFacadeRemote getTipoDocumentoFacade() {
        return tipoDocumentoFacade;
    }

    private ProgramaFacadeRemote getProgramaFacade() {
        return programaFacade;
    }

    private TipoCuentaFacadeRemote getTipoCuentaFacade() {
        return tipoCuentaFacade;
    }

    private NivelFormacionFacadeRemote getNivelFormacionFacade() {
        return nivelFormacionFacade;
    }

    private InformacionAcademicaFacadeRemote getInfoAcademicaFacade() {
        return infoAcademicaFacade;
    }

    private UsuarioFacadeRemote getUsuarioFacade() {
        return usuarioFacade;
    }

    private RolFacadeRemote getRolFacade() {
        return rolFacade;
    }
}
