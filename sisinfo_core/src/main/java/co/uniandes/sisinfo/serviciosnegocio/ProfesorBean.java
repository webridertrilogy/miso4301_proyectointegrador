/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.GrupoInvestigacion;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import co.uniandes.sisinfo.entities.datosmaestros.NivelPlanta;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.nucleo.services.NucleoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.GrupoInvestigacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelPlantaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;

/**
 * Servicio de negocio: Administración de profesores
 */
@Stateless
@EJB(name = "ProfesorBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ProfesorLocal.class)
public class ProfesorBean implements ProfesorRemote, ProfesorLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------   
    @EJB
    private ProfesorFacadeRemote profesorFacade;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private UsuarioFacadeRemote usuarioFacade;
    @EJB
    private RolFacadeRemote rolFacade;
    @EJB
    private GrupoInvestigacionFacadeRemote grupoInvestigacionFacade;
    @EJB
    private NivelFormacionFacadeRemote nivelFormacionFacade;
    @EJB
    private NivelPlantaFacadeRemote nivelPlantaFacade;
    @EJB
    private NucleoRemote nucleoBean;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private PaisFacadeRemote paisFacade;
    @EJB
    private TipoDocumentoFacadeRemote documentoFacade;
    @EJB
    private TareaMultipleFacadeRemote tareaFacade;
    @EJB
    private EstudianteFacadeLocal estudianteFacade;
    @EJB
    private CorreoRemote correoBean;
    private ServiceLocator serviceLocator;
    private ParserT parser;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ProfesorBean
     */
    public ProfesorBean() {
        try {
            serviceLocator = new ServiceLocator();
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
            usuarioFacade = (UsuarioFacadeRemote) serviceLocator.getRemoteEJB(UsuarioFacadeRemote.class);
            rolFacade = (RolFacadeRemote) serviceLocator.getRemoteEJB(RolFacadeRemote.class);
            grupoInvestigacionFacade = (GrupoInvestigacionFacadeRemote) serviceLocator.getRemoteEJB(GrupoInvestigacionFacadeRemote.class);
            nivelFormacionFacade = (NivelFormacionFacadeRemote) serviceLocator.getRemoteEJB(NivelFormacionFacadeRemote.class);
            nivelPlantaFacade = (NivelPlantaFacadeRemote) serviceLocator.getRemoteEJB(NivelPlantaFacadeRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            tareaFacade = (TareaMultipleFacadeRemote) serviceLocator.getRemoteEJB(TareaMultipleFacadeRemote.class);
            nucleoBean = (NucleoRemote) serviceLocator.getRemoteEJB(NucleoRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);

        } catch (NamingException ex) {
            Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String agregarProfesor(String xml) {
        try {
            //Se lee el comando
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secuenciaProfesor = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
            String nombres = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
            String apellidos = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
            String correo = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String telefonoResidencia = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA)).getValor();
            String celular = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR)).getValor();
            String catedra = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA)).getValor();
            String extension = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION)).getValor();
            String oficina = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OFICINA)).getValor();
            String nombreGrupoInvestigacion = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION)).getValor();
            String nivelFormacion = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION)).getValor();
            String nivelPlanta = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PLANTA)).getValor();

            //Se verifica que el profesor no se encuentre en la base de datos buscando por su correo
            Profesor temp = profesorFacade.findByCorreo(correo);

            //Si no existe, se agrega a la base de datos
            if (temp == null) {
                agregarProfesor(correo, nombres, apellidos, celular, extension, oficina, catedra, telefonoResidencia, nombreGrupoInvestigacion, nivelFormacion, nivelPlanta);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0127, new LinkedList<Secuencia>());
            } //Si el profesor ya existe se manda un mensaje de error
            else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0026, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0083, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    @Override
    public String agregarProfesores(String xml) {
        try {
            //Se lee el comando
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secuenciaProfesores = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESORES));
            Iterator<Secuencia> iterator = secuenciaProfesores.getSecuencias().iterator();
            while (iterator.hasNext()) {
                Secuencia secuenciaProfesor = iterator.next();
                String nombres = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
                String apellidos = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
                String correo = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
                String telefonoResidencia = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA)).getValor();
                String celular = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR)).getValor();
                String catedra = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA)).getValor();
                String extension = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION)).getValor();
                String oficina = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OFICINA)).getValor();
                String nombreGrupoInvestigacion = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION)).getValor();

                //Se verifica que el profesor no se encuentre en la base de datos buscando por su correo
                Profesor temp = profesorFacade.findByCorreo(correo);
                //Si no existe, se agrega a la base de datos
                if (temp == null) {
                    agregarProfesor(correo, nombres, apellidos, celular, extension, oficina, catedra, telefonoResidencia, nombreGrupoInvestigacion, "", "");
                }
            }
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROFESORES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0163, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROFESORES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0014, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private void agregarProfesor(String correo, String nombres, String apellidos, String celular, String extension, String oficina, String catedra, String telefonoResidencia, String nombreGrupoInvestigacion, String nivelFormacion, String nivelPlanta) {
        //Se crea un nuevo profesor
        String sufijo = getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
        if (!correo.contains(sufijo)) {
            correo = correo + sufijo;
        }
        Profesor profesor = new Profesor();
        Persona persona = personaFacade.findByCorreo(correo);
        if (persona == null) {
            persona = new Persona();
        }
        persona.setActivo(true);
        persona.setCorreo(correo);
        persona.setNombres(nombres);
        persona.setApellidos(apellidos);
//        profesor.setSecciones(new LinkedList<Seccion>());
        persona.setCelular(celular);
        persona.setExtension(extension);
        profesor.setOficina(oficina);
        Rol rol = null;
        if (Boolean.parseBoolean(catedra)) {
            profesor.setTipo(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA));
            rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA));
        } else {
            profesor.setTipo(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA));
            rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA));
        }
        persona.setTelefono(telefonoResidencia);
        GrupoInvestigacion grupoInvestigacion = getGrupoInvestigacionFacade().findByNombre(nombreGrupoInvestigacion);
        profesor.setGrupoInvestigacion(grupoInvestigacion);

        NivelFormacion anivelFormacion = nivelFormacionFacade.findByName(nivelFormacion);
        profesor.setNivelFormacion(anivelFormacion);
        NivelPlanta anivelPlanta = nivelPlantaFacade.findByNombre(nivelPlanta);
        profesor.setNivelPlanta(anivelPlanta);
        //Se persiste el profesor
        personaFacade.edit(persona);
        profesor.setPersona(personaFacade.findByCorreo(correo));
        profesor.setActivo(Boolean.TRUE);
        getProfesorFacade().create(profesor);

        //Se actualiza el grupo de investigación
        grupoInvestigacion = getGrupoInvestigacionFacade().findByNombre(nombreGrupoInvestigacion);
        if (grupoInvestigacion != null) {
            Collection<Profesor> profesores = grupoInvestigacion.getProfesores();
            profesores.add(profesor);
            grupoInvestigacion.setProfesores(profesores);
            getGrupoInvestigacionFacade().edit(grupoInvestigacion);
        }

        //Se busca al usuario con este correo
        Usuario usuario = getUsuarioFacade().findByLogin(correo);
        //Si el usuario es nulo, se crea un nuevo usuario
        if (usuario == null) {
            //Se agrega un nuevo usuario con el identificador de este profesor

            usuario = new Usuario();
            Persona p = personaFacade.findByCorreo(correo);
            if (p == null) {
                p = new Persona();
            }
            p.setCorreo(correo);
            p.setNombres(nombres);
            p.setApellidos(apellidos);

            usuario.setPersona(p);
            usuario.setEsPersona(true);

            Collection<Rol> listaRoles = new ArrayList<Rol>();
            listaRoles.add(rol);
            usuario.setRoles(listaRoles);
            getUsuarioFacade().create(usuario);
        } //De lo contrario se agrega el rol del profesor al usuario
        else {
            Collection<Rol> listaRoles = usuario.getRoles();
            listaRoles.add(rol);
            usuario.setRoles(listaRoles);
            getUsuarioFacade().edit(usuario);
        }


        //Se agrega un proponente si este profesor es de planta
        if (!Boolean.parseBoolean(catedra)) {

            //Se crea un comando para insertar un proponente
            Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), nombres);
            Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), apellidos);
            Secuencia secCargo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CARGO), getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA));
            Secuencia secTelefono = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO), getConstanteBean().getConstante(Constantes.VAL_NUMERO_UNIVERSIDAD));
            Secuencia secCelular = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), celular);
            Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), correo);
            Secuencia secExtension = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION), extension);
            Secuencia secEsEmpresa = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ES_EMPRESA), "false");
            Secuencia secEmpresa = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EMPRESA), "");

            ArrayList<Secuencia> secuencias = new ArrayList();
            secuencias.add(secNombres);
            secuencias.add(secApellidos);
            secuencias.add(secCargo);
            secuencias.add(secTelefono);
            secuencias.add(secCelular);
            secuencias.add(secCorreo);
            secuencias.add(secExtension);
            secuencias.add(secEsEmpresa);
            secuencias.add(secEmpresa);

            String comando;
            try {
                comando = getParser().crearComando(getConstanteBean().getConstante(Constantes.CMD_CREAR_PROPONENTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA), getParser().obtenerValor(getConstanteBean().getConstante(Constantes.ROL_COORDINACION)), secuencias);
                nucleoBean.resolverComando(comando);
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public String actualizarDatosProfesor(String xml) {
        try {
            getParser().leerXML(xml);
            //Obtiene los datos a actualizar del profesor
            Secuencia secuenciaProfesor = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
            String nombres = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
            String apellidos = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
            String correo = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String correoNuevo = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_NUEVO)).getValor();
            String telefonoResidencia = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA)).getValor();
            String celular = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR)).getValor();
            String extension = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION)).getValor();
            String oficina = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OFICINA)).getValor();
            String catedra = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA)).getValor();
            String nombreGrupoInvestigacion = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION)).getValor();
            String nivelFormacion = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION)).getValor();
            String nivelPlanta = secuenciaProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PLANTA)).getValor();
            //Busca el profesor
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            if (profesor == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0085, new LinkedList<Secuencia>());
            }
            //Si el profesor existe, busca a la persona
            Persona persona = getPersonaFacade().findByCorreo(correo);
            if (persona == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0085, new LinkedList<Secuencia>());
            }
            Usuario usuario = getUsuarioFacade().findByLogin(correo);
            if (usuario == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0085, new LinkedList<Secuencia>());
            }
            //Actualiza la información de la persona
            persona.setNombres(nombres);
            persona.setApellidos(apellidos);
            persona.setTelefono(telefonoResidencia);
            persona.setCelular(celular);
            persona.setExtension(extension);
            getPersonaFacade().edit(persona);
            //Actualiza la información del profesor
            profesor.setOficina(oficina);
            if (Boolean.parseBoolean(catedra)) {
                profesor.setTipo(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA));
            } else {
                profesor.setTipo(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA));
                NivelPlanta aNivelPlanta = getNivelPlantaFacade().findByNombre(nivelPlanta);
                profesor.setNivelPlanta(aNivelPlanta);
            }
            GrupoInvestigacion grupoInvestigacion = getGrupoInvestigacionFacade().findByNombre(nombreGrupoInvestigacion);
            profesor.setGrupoInvestigacion(grupoInvestigacion);
            NivelFormacion aNivelFormacion = getNivelFormacionFacade().findByName(nivelFormacion);
            profesor.setNivelFormacion(aNivelFormacion);
            profesor.setPersona(persona);
            profesor.setActivo(Boolean.TRUE);
            getProfesorFacade().edit(profesor);
            //Actualiza el rol y las tareas asociadas
            Collection<Rol> roles = usuario.getRoles();
            Rol rolCatedra = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA));
            Rol rolPlanta = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA));
            Collection<TareaMultiple> tareas = tareaFacade.findByCorreo(correo);
            if (Boolean.parseBoolean(catedra)) {
                //Actualiza a rol catedra
                if (roles.contains(rolPlanta)) {
                    roles.remove(rolPlanta);
                }
                if (!roles.contains(rolCatedra)) {
                    roles.add(rolCatedra);
                }
                //Actualiza las tareas a rol catedra
                for (TareaMultiple tarea : tareas) {
                    tarea.setRol(rolCatedra);
                    tareaFacade.edit(tarea);
                }
            } else {
                //Actualiza a rol planta
                if (roles.contains(rolCatedra)) {
                    roles.remove(rolCatedra);
                }
                if (!roles.contains(rolPlanta)) {
                    roles.add(rolPlanta);
                }
                //Actualiza las tareas a rol planta
                for (TareaMultiple tarea : tareas) {
                    tarea.setRol(rolPlanta);
                    tareaFacade.edit(tarea);
                }
            }
            usuario.setRoles(roles);
            getUsuarioFacade().edit(usuario);

            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0128, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0079, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    @Override
    public String eliminarDatosProfesor(String xml) {
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            if (profesor == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0161, new LinkedList<Secuencia>());
            } else {
                getProfesorFacade().remove(profesor);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0162, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0011, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarDatosProfesores(String xml) {
        try {
            getParser().leerXML(xml);
            ArrayList<Profesor> profesores = new ArrayList<Profesor>();
            Collection<Profesor> profesoresBD = getProfesorFacade().findAll();
            //Elimina los profesores donde el nombre sea Null
            for (Profesor profesor : profesoresBD) {
                if (profesor.getPersona() != null && profesor.getPersona().getNombres() != null) {
                    profesores.add(profesor);
                }
            }
            //Organiza la lista de profesores
            Collections.sort(profesores, new Comparator<Profesor>() {
                public int compare(Profesor o1, Profesor o2) {
                    return o1.getPersona().getNombres().compareTo(o2.getPersona().getNombres());
                }
            });
            //Crea la secuencia
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaProfesores = getSecuenciaProfesores(profesores);
            secuencias.add(secuenciaProfesores);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_PROFESORES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0129, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_PROFESORES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0077, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarGruposInvestigacion(String xml) {
        try {
            getParser().leerXML(xml);
            List<GrupoInvestigacion> grupos = getGrupoInvestigacionFacade().findAll();
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaGrupos = getSecuenciaGrupos(grupos);
            secuencias.add(secuenciaGrupos);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPOS_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0178, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPOS_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0030, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarDatosProfesor(String xml) {
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String sufijo = getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
            if (!correo.contains(sufijo)) {
                correo += sufijo;
            }
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            if (profesor == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0161, new LinkedList<Secuencia>());
            } else {
                Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
                Secuencia secuenciaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), profesor.getPersona().getNombres()));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), profesor.getPersona().getApellidos()));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), profesor.getPersona().getCorreo()));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA), profesor.getPersona().getTelefono()));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), profesor.getPersona().getCelular()));
                if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA))) {
                    secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA), "true"));
                } else {
                    secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA), "false"));
                }
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION), profesor.getPersona().getExtension()));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OFICINA), profesor.getOficina()));
                GrupoInvestigacion grupoInvestigacion = profesor.getGrupoInvestigacion();
                Secuencia secuenciaGrupo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.NULL));
                if (grupoInvestigacion == null) {
                    secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), getConstanteBean().getConstante(Constantes.NULL)));
                    secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION), getConstanteBean().getConstante(Constantes.NULL)));
                    secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAGINA), getConstanteBean().getConstante(Constantes.NULL)));
                } else {
                    secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), grupoInvestigacion.getNombre()));
                    secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION), grupoInvestigacion.getDescripcion()));
                    secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAGINA), grupoInvestigacion.getPagina()));
                }
                secuenciaProfesor.agregarSecuencia(secuenciaGrupo);
                secuencias.add(secuenciaProfesor);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0151, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0123, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Método que retorna una secuencia dada una colección de profesores
     * @param profesores Colección de profesores
     * @return Secuencia generada
     */
    private Secuencia getSecuenciaProfesores(Collection<Profesor> profesores) {
        Secuencia secuenciaProfesores = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESORES), getConstanteBean().getConstante(Constantes.NULL));
        Iterator<Profesor> iterator = profesores.iterator();
        while (iterator.hasNext()) {
            Profesor profesor = iterator.next();
            Secuencia secuenciaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
            secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), profesor.getPersona().getNombres()));
            secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), profesor.getPersona().getApellidos()));
            secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION), profesor.getPersona().getExtension()));
            secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), profesor.getPersona().getCorreo()));
            secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OFICINA), profesor.getOficina()));
            secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA), profesor.getPersona().getTelefono()));
            secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), profesor.getPersona().getCelular()));
            secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), profesor.getActivo().toString()));
            if (profesor.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA))) {
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA), "true"));
            } else {
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA), "false"));
            }

            GrupoInvestigacion grupoInvestigacion = profesor.getGrupoInvestigacion();
            if (grupoInvestigacion == null) {
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.NULL)));
            } else {
                Secuencia secGrupoInvestigacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), "");
                secGrupoInvestigacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), grupoInvestigacion.getNombre()));
                secuenciaProfesor.agregarSecuencia(secGrupoInvestigacion);
            }

            NivelFormacion nivelFormacion = profesor.getNivelFormacion();
            if (nivelFormacion == null) {
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), getConstanteBean().getConstante(Constantes.NULL)));
            } else {
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), nivelFormacion.getNombre()));
            }

            NivelPlanta nivelPlanta = profesor.getNivelPlanta();
            if (nivelPlanta == null) {
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PLANTA), getConstanteBean().getConstante(Constantes.NULL)));
            } else {
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PLANTA), nivelPlanta.getNombre()));
            }
            secuenciaProfesores.agregarSecuencia(secuenciaProfesor);
        }
        return secuenciaProfesores;
    }

    private Secuencia getSecuenciaGrupos(Collection<GrupoInvestigacion> grupos) {
        Secuencia secuenciaGrupos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPOS_INVESTIGACION), getConstanteBean().getConstante(Constantes.NULL));
        Iterator<GrupoInvestigacion> iterator = grupos.iterator();
        while (iterator.hasNext()) {
            GrupoInvestigacion grupo = iterator.next();
            Secuencia secuenciaGrupo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.NULL));
            secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO_INVESTIGACION), Long.toString(grupo.getId())));
            secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), grupo.getNombre()));
            secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION), grupo.getDescripcion()));
            secuenciaGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAGINA), grupo.getPagina()));
            secuenciaGrupos.agregarSecuencia(secuenciaGrupo);
        }
        return secuenciaGrupos;
    }

    @Deprecated
    @Override
    public String agregarGrupoInvestigacion(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaGrupo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION));
            String nombre = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            String descripcion = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION)).getValor();
            String pagina = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAGINA)).getValor();
            GrupoInvestigacion grupoTemp = getGrupoInvestigacionFacade().findByNombre(nombre);
            if (grupoTemp == null) {
                agregarGrupoInvestigacion(nombre, descripcion, pagina);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0164, new LinkedList<Secuencia>());
            } else {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0165, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0015, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    @Override
    public String agregarGruposInvestigacion(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaGrupos = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPOS_INVESTIGACION));
            Iterator<Secuencia> iterator = secuenciaGrupos.getSecuencias().iterator();
            while (iterator.hasNext()) {
                Secuencia secuenciaGrupo = iterator.next();
                String nombre = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
                String descripcion = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION)).getValor();
                String pagina = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAGINA)).getValor();

                GrupoInvestigacion grupoTemp = getGrupoInvestigacionFacade().findByNombre(nombre);
                if (grupoTemp == null) {
                    agregarGrupoInvestigacion(nombre, descripcion, pagina);
                }
            }
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_GRUPOS_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0166, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_GRUPOS_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0017, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private void agregarGrupoInvestigacion(String nombre, String descripcion, String pagina) {
        GrupoInvestigacion grupoInvestigacion = new GrupoInvestigacion();
        grupoInvestigacion.setNombre(nombre);
        grupoInvestigacion.setDescripcion(descripcion);
        grupoInvestigacion.setPagina(pagina);
        grupoInvestigacion.setProfesores(new LinkedList<Profesor>());
        getGrupoInvestigacionFacade().create(grupoInvestigacion);
    }

    @Deprecated
    @Override
    public String eliminarGrupoInvestigacion(String xml) {
        try {
            getParser().leerXML(xml);
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            GrupoInvestigacion grupo = getGrupoInvestigacionFacade().findByNombre(nombre);
            if (grupo == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0167, new LinkedList<Secuencia>());
            } else {
                getGrupoInvestigacionFacade().remove(grupo);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0168, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0019, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    @Override
    public String actualizarDatosGrupoInvestigacion(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaGrupo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION));
            String nombre = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            String descripcion = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION)).getValor();
            String pagina = secuenciaGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAGINA)).getValor();
            GrupoInvestigacion grupo = getGrupoInvestigacionFacade().findByNombre(nombre);
            if (grupo == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0169, new LinkedList<Secuencia>());
            } else {
                grupo.setDescripcion(descripcion);
                grupo.setPagina(pagina);
                getGrupoInvestigacionFacade().edit(grupo);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0170, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0022, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Deprecated
    @Override
    public String consultarProfesorPerteneceAGrupo(String xml) {
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION)).getValor();
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            if (profesor == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESOR_PERTENECE_A_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0171, new LinkedList<Secuencia>());
            } else {
                GrupoInvestigacion grupo = profesor.getGrupoInvestigacion();
                boolean resultado = false;
                if (grupo != null) {
                    resultado = grupo.getNombre().equals(nombre);
                }
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERTENECE), Boolean.toString(resultado)));
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESOR_PERTENECE_A_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0172, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESOR_PERTENECE_A_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0025, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarProfesoresGrupo(String xml) {
        try {
            getParser().leerXML(xml);
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION)).getValor();
            GrupoInvestigacion grupo = getGrupoInvestigacionFacade().findByNombre(nombre);
            if (grupo == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0173, new LinkedList<Secuencia>());
            } else {
                Collection<Profesor> profesores = grupo.getProfesores();
                Collection<Profesor> profesoresActivos = new ArrayList<Profesor>();
                for (Profesor profesor : profesores) {
                    if (profesor.getActivo() != null && profesor.getActivo().equals(Boolean.TRUE)) {
                        profesoresActivos.add(profesor);
                    }
                }
                Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
                Secuencia secuenciaProfesores = getSecuenciaProfesores(profesores);
                secuencias.add(secuenciaProfesores);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0174, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_GRUPO_INVESTIGACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0027, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String consultarProfesoresPorTipo(String xml) {
        try {
            getParser().leerXML(xml);
            String profesorCatedra = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CATEDRA)).getValor();
            ArrayList<Profesor> profesores = new ArrayList<Profesor>();//(ArrayList<Profesor>) profesorFacade.findByTipo(profesorCatedra);
            Collection<Profesor> profesoresBD = profesorFacade.findByTipo(profesorCatedra);
            //Elimina los profesores donde el nombre sea Null
            for (Profesor profesor : profesoresBD) {
                if (profesor.getPersona() != null && profesor.getPersona().getNombres() != null) {
                    profesores.add(profesor);
                }
            }
            //Organiza la lista de profesores
            Collections.sort(profesores, new Comparator<Profesor>() {
                public int compare(Profesor o1, Profesor o2) {
                    return o1.getPersona().getApellidos().compareTo(o2.getPersona().getApellidos());
                }
            });

            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            Secuencia secuenciaProfesores = getSecuenciaProfesores(profesores);
            secuencias.add(secuenciaProfesores);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_TIPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0177, new LinkedList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_TIPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0028, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ProfesorBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    //MÉTODOS CRUD PROFESOR - PERSONA
    public String crearProfesor(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secProfesor = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
            if (secProfesor != null) {
                crearProfesorPersona(secProfesor);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_PROFESOR_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_PROFESOR_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_PROFESOR_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private void crearProfesorPersona(Secuencia secProfesor) {
        Secuencia secInfoPersonal = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL));
        if (secInfoPersonal != null) {
            Secuencia correo = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (correo != null) {

                //Verifica si la persona existe y la crea de ser necesario.
                Persona persona = getPersonaFacade().findByCorreo(correo.getValor());
                Boolean existePersona = true;
                if (persona == null) {
                    existePersona = false;
                    persona = new Persona();
                }

                persona.setActivo(true);
                Secuencia apellidos = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
                if (apellidos != null) {
                    persona.setApellidos(apellidos.getValor());
                }
                Secuencia celular = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR));
                if (celular != null) {
                    persona.setCelular(celular.getValor());
                }
                Secuencia ciudadNacimiento = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD));
                if (ciudadNacimiento != null) {
                    persona.setCiudadNacimiento(ciudadNacimiento.getValor());
                }
                Secuencia codigo = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE));
                if (codigo != null) {
                    persona.setCodigo(codigo.getValor());
                }
                persona.setCorreo(correo.getValor());
                Secuencia correoAlterno = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO));
                if (correoAlterno != null) {
                    persona.setCorreoAlterno(correoAlterno.getValor());
                }
                Secuencia direccion = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION));
                if (direccion != null) {
                    persona.setDireccionResidencia(direccion.getValor());
                }
                Secuencia extension = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION));
                if (extension != null) {
                    persona.setExtension(extension.getValor());
                }

                Secuencia fechaNacimiento = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO));
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy/MM/dd");
                if (fechaNacimiento != null && fechaNacimiento.getValor() != null && !fechaNacimiento.getValor().isEmpty()) {
                    try {
                        Date fecha = formatoDeFecha.parse(fechaNacimiento.getValor());
                        persona.setFechaNacimiento(new java.sql.Timestamp(fecha.getTime()));
                    } catch (ParseException ex) {
                        Logger.getLogger(EstudianteBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                Secuencia nombres = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
                if (nombres != null) {
                    persona.setNombres(nombres.getValor());
                }
                Secuencia documentoDeIdentidad = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO));
                if (documentoDeIdentidad != null) {
                    persona.setNumDocumentoIdentidad(documentoDeIdentidad.getValor());
                }
                Secuencia paisNacimiento = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS));
                if (paisNacimiento != null) {
                    persona.setPais(getPaisFacade().findByNombre(paisNacimiento.getValor()));
                }
                Secuencia telefono = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO));
                if (telefono != null) {
                    persona.setTelefono(telefono.getValor());
                }
                Secuencia tipoDocumento = secInfoPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_DOCUMENTO));
                if (tipoDocumento != null) {
                    persona.setTipoDocumento(getDocumentoFacade().findByTipo(tipoDocumento.getValor()));
                }

                if (existePersona) {
                    getPersonaFacade().edit(persona);
                } else {
                    getPersonaFacade().create(persona);
                }

                //si el estudiante está activo, le agrega el rol de Estudiante
                Collection<Rol> rolesIniciales = new ArrayList();
                Estudiante estudiante = estudianteFacade.findByCorreo(correo.getValor());
                if (estudiante != null && estudiante.getActivo()) {
                    Rol rolestudiante = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE));
                    rolesIniciales.add(rolestudiante);
                }

                //Crea y persiste el usuario
                Usuario usuario = getUsuarioFacade().findByLogin(correo.getValor());
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setEsPersona(true);
                    usuario.setPersona(getPersonaFacade().findByCorreo(correo.getValor()));
                    Collection<Rol> roles = rolesIniciales;

                    Secuencia secInfoProfesor = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTRO));
                    if (secInfoProfesor != null) {
                        Secuencia tipoProfesor = secInfoProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO));
                        if (tipoProfesor != null) {
                            if (tipoProfesor.getValor().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA))) {
                                Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA));
                                roles.add(rol);
                            } else if (tipoProfesor.getValor().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))) {
                                Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA));
                                roles.add(rol);
                            }
                        }
                    }
                    usuario.setRoles(roles);
                    getUsuarioFacade().create(usuario);
                } else {
                    //Si ya existe el usuario, se agrega el rol únicamente
                    Collection<Rol> roles = usuario.getRoles();
                    roles.addAll(rolesIniciales);
                    Secuencia secInfoProfesor = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTRO));
                    if (secInfoProfesor != null) {
                        Secuencia tipoProfesor = secInfoProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO));
                        if (tipoProfesor != null) {
                            if (tipoProfesor.getValor().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA))) {
                                Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA));
                                roles.add(rol);
                            } else if (tipoProfesor.getValor().equalsIgnoreCase(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA))) {
                                Rol rol = getRolFacade().findByRol(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA));
                                roles.add(rol);
                            }
                        }
                        usuario.setRoles(roles);
                        getUsuarioFacade().edit(usuario);
                    }
                }

                //Crea y persiste al profesor
                Profesor profesor = getProfesorFacade().findByCorreo(correo.getValor());
                if (profesor == null) {
                    profesor = new Profesor();

                    Secuencia secInfoProfesor = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTRO));
                    if (secInfoProfesor != null) {
                        Secuencia tipoProfesor = secInfoProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO));
                        if (tipoProfesor != null) {
                            profesor.setTipo(tipoProfesor.getValor());
                        }
                        Secuencia nivelFormacion = secInfoProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION));
                        if (nivelFormacion != null) {
                            profesor.setNivelFormacion(getNivelFormacionFacade().findByName(nivelFormacion.getValor()));
                        }
                        Secuencia nivelPlanta = secInfoProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PLANTA));
                        if (nivelPlanta != null) {
                            profesor.setNivelPlanta(getNivelPlantaFacade().findByNombre(nivelPlanta.getValor()));
                        }
                        Secuencia grupoInvestigacion = secInfoProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION));
                        if (grupoInvestigacion != null) {
                            profesor.setGrupoInvestigacion(getGrupoInvestigacionFacade().findByNombre(grupoInvestigacion.getValor()));
                        }
                        Secuencia oficina = secInfoProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OFICINA));
                        if (oficina != null) {
                            profesor.setOficina(oficina.getValor());
                        }
                        profesor.setPersona(getPersonaFacade().findByCorreo(correo.getValor()));
                        profesor.setActivo(true);
                        getProfesorFacade().create(profesor);
                    }
                }
                //se envia el correo de bienvenida al profesor
                correoBean.enviarMail(correo.getValor(), Notificaciones.ASUNTO_BIENVENIDA_PROFESOR_SISINFO, null, null, null, Notificaciones.MENSAJE_BIENVENIDA_PROFESOR_SISINFO);

            }
        }
    }

    public String consultarProfesores(String comando) {
        try {
            getParser().leerXML(comando);
            Collection<Profesor> profesores = getProfesorFacade().findAll();
            Secuencia secProfesores = consultarProfesores(profesores);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secProfesores);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESORES_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String consultarProfesor(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            Secuencia secProfesor = consultarProfesor(profesor);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secProfesor);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESOR_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROFESOR_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String eliminarProfesor(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            getProfesorFacade().remove(profesor);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROFESOR_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_PROFESOR_PERSONA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String actualizarEstadoActivoProfesor(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String activo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA)).getValor();
            if (!correo.contains("@uniandes.edu.co")) {
                correo += "@uniandes.edu.co";
            }
            Profesor profesor = getProfesorFacade().findByCorreo(correo);
            if (profesor != null) {
                profesor.setActivo(Boolean.parseBoolean(activo));
                getProfesorFacade().edit(profesor);
            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_ESTADO_ACTIVO_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_ESTADO_ACTIVO_PROFESOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    //HELPERS
    private Secuencia consultarProfesores(Collection<Profesor> profesores) {
        Secuencia secProfesores = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESORES), "");
        for (Iterator<Profesor> it = profesores.iterator(); it.hasNext();) {
            Profesor profesor = it.next();
            Secuencia secProfesor = consultarProfesor(profesor);
            secProfesores.agregarSecuencia(secProfesor);
        }
        return secProfesores;
    }

    private Secuencia consultarProfesor(Profesor profesor) {
        Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
        //Información personal
        Secuencia secInfoPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), "");
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), profesor.getPersona().getCorreo()));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), (profesor.getPersona().getCodigo() != null) ? profesor.getPersona().getCodigo() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), (profesor.getPersona().getNombres() != null) ? profesor.getPersona().getNombres() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), (profesor.getPersona().getApellidos() != null) ? profesor.getPersona().getApellidos() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), (profesor.getPersona().getCelular() != null) ? profesor.getPersona().getCelular() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_DOCUMENTO), (profesor.getPersona().getTipoDocumento() != null) ? profesor.getPersona().getTipoDocumento().getTipo() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), (profesor.getPersona().getNumDocumentoIdentidad() != null) ? profesor.getPersona().getNumDocumentoIdentidad() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO), (profesor.getPersona().getCorreoAlterno() != null) ? profesor.getPersona().getCorreoAlterno() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), (profesor.getPersona().getFechaNacimiento() != null) ? profesor.getPersona().getFechaNacimiento().toString() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD), (profesor.getPersona().getCiudadNacimiento() != null) ? profesor.getPersona().getCiudadNacimiento() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), (profesor.getPersona().getPais() != null) ? profesor.getPersona().getPais().getNombre() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION), (profesor.getPersona().getDireccionResidencia() != null) ? profesor.getPersona().getDireccionResidencia() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO), (profesor.getPersona().getTelefono() != null) ? profesor.getPersona().getTelefono() : ""));
        secInfoPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION), (profesor.getPersona().getExtension() != null) ? profesor.getPersona().getExtension() : ""));
        secProfesor.agregarSecuencia(secInfoPersonal);

        Secuencia secInfoProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_OTRO), "");
        secInfoProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO), profesor.getTipo()));
        secInfoProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), (profesor.getNivelFormacion() != null) ? profesor.getNivelFormacion().getNombre() : ""));
        secInfoProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_PLANTA), (profesor.getNivelPlanta() != null) ? profesor.getNivelPlanta().getNombre() : ""));
        secInfoProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), (profesor.getGrupoInvestigacion() != null) ? profesor.getGrupoInvestigacion().getNombre() : ""));
        secInfoProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OFICINA), (profesor.getOficina() != null) ? profesor.getOficina() : ""));
        secProfesor.agregarSecuencia(secProfesor);

        return secProfesor;
    }

    /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna ProfesorFacade
     * @return profesorFacade ProfesorFacade
     */
    private ProfesorFacadeRemote getProfesorFacade() {
        return profesorFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private GrupoInvestigacionFacadeRemote getGrupoInvestigacionFacade() {
        return grupoInvestigacionFacade;
    }

    private RolFacadeRemote getRolFacade() {
        return rolFacade;
    }

    private UsuarioFacadeRemote getUsuarioFacade() {
        return usuarioFacade;
    }

    private NivelFormacionFacadeRemote getNivelFormacionFacade() {
        return nivelFormacionFacade;
    }

    private NivelPlantaFacadeRemote getNivelPlantaFacade() {
        return nivelPlantaFacade;
    }

    private PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private TipoDocumentoFacadeRemote getDocumentoFacade() {
        return documentoFacade;
    }

    private PaisFacadeRemote getPaisFacade() {
        return paisFacade;
    }
}
