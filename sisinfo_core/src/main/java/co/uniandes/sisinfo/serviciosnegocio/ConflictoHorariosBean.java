package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.ConfiguracionFechasCH;
import co.uniandes.sisinfo.entities.PeticionConflictoHorario;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import co.uniandes.sisinfo.serviciosfuncionales.ConfiguracionFechasCHFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeticionConflictoHorarioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicios de administración de conflictos de horario
 * @author German Florez, Marcela Morales, Paola Gómez
 */
@Stateless
public class ConflictoHorariosBean implements ConflictoHorariosBeanRemote, ConflictoHorariosBeanLocal {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Remotos
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private CursoFacadeRemote cursoFacade;
    @EJB
    private ProgramaFacadeRemote programaFacade;
    //Locales
    @EJB
    private ConfiguracionFechasCHFacadeLocal configuracionFechasFacade;
    @EJB
    private PeticionConflictoHorarioFacadeLocal peticionConflictoHorarioFacade;
    @EJB
    private ConstanteRemote constanteBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConflictoHorariosBeanHelper conversor;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    public ConflictoHorariosBean() throws NamingException {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            programaFacade = (ProgramaFacadeRemote) serviceLocator.getRemoteEJB(ProgramaFacadeRemote.class);
            conversor = new ConflictoHorariosBeanHelper(getConstanteBean(), getCorreoBean());
        } catch (NamingException ex) {
            Logger.getLogger(ConflictoHorariosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------------------------------------------
    // MÉTODOS DE NEGOCIO
    //----------------------------------------------
    public String crearPeticionConflictoHorario(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secPeticion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PETICION_CONFLICTO_HORARIO));
            PeticionConflictoHorario peticion = getConversor().crearPeticionDesdeSecuencia(secPeticion);

            //Validaciones
            //1. Valida que la petición tenga un curso y una sección destino obligatoriamente
            if (peticion.getCursoDestino() == null || peticion.getDestino() == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_024, new LinkedList<Secuencia>());
            }

            //2. Valida que el tipo de solicitud sea válido
            if (!(peticion.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_CAMBIO_CONFLICTO_HORARIO))) &&
                    !(peticion.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_ADICION_CONFLICTO_HORARIO))) &&
                    !(peticion.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_RETIRO_CONFLICTO_HORARIO)))){
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_029, new LinkedList<Secuencia>());
            }

            //3. Valida que la petición tenga un curso y una sección origen si es de tipo cambio
            if (peticion.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_CAMBIO_CONFLICTO_HORARIO))) {
                if (peticion.getCursoOrigen() == null || peticion.getOrigen() == null) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_025, new LinkedList<Secuencia>());
                }
            }
            //4. Valida que no exista una petición con la misma sección de destino para el mismo estudiante
            //PeticionConflictoHorario peticionExiste = getPeticionConflictoHorarioFacade().buscarPorEstudianteYSeccionDestino(peticion.getEstudiante().getPersona().getCorreo(), peticion.getDestino().getId());
            //if (peticionExiste != null) {
            //    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_002, new LinkedList<Secuencia>());
            //}
            //5. Valida que esté entre las fechas y que haya una fecha configurada
            if(getConfiguracionFechasFacade().findAll().isEmpty()){
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_028, new LinkedList<Secuencia>());
            }
            peticion.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ConfiguracionFechasCH configuracionFechas = getConfiguracionFechasFacade().findAll().get(0);
            if (peticion.getFechaCreacion().before(configuracionFechas.getInicioConflictos()) || peticion.getFechaCreacion().after(configuracionFechas.getFinConflictos())) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_004, new LinkedList<Secuencia>());
            }

            //Actualiza la información y persiste la petición
            peticion.setEstado(getConstanteBean().getConstante(Constantes.VAL_CONFLICTO_HORARIO_ESTADO_PENDIENTE));
            peticion.setFechaResolucion(null);
            peticion.setComentariosResolucion(null);
            peticion.setId(null);
            getPeticionConflictoHorarioFacade().create(peticion);

            //Notifica la creación de la petición de conflicto de horario
            String mensajeCreacion = Notificaciones.MENSAJE_CONFIRMACION_CONFLICTO_HORARIO;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", peticion.getEstudiante().getPersona().getNombres() + " " + peticion.getEstudiante().getPersona().getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", getConversor().getPeticionHTML(peticion));
            //getCorreoBean().enviarMail(peticion.getEstudiante().getPersona().getCorreo(), Notificaciones.ASUNTO_CONFIRMACION_CONFLICTO_HORARIO, null, null, null, mensajeCreacion);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_PETICION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_009, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarPeticionesPorCorreo(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
            Estudiante estudiante = getConversor().crearEstudianteDesdeSecuencia(secEstudiante);

            //Consulta las peticiones de acuerdo al correo del estudiante
            Collection<PeticionConflictoHorario> peticiones = getPeticionConflictoHorarioFacade().buscarPorCorreo(estudiante.getPersona().getCorreo());
            Secuencia secPeticiones = getConversor().crearSecuenciaPeticiones(peticiones);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secPeticiones);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_CONFLICTO_HORARIO_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_005, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_CONFLICTO_HORARIO_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_013, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String cancelarPeticionPorCorreo(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secPeticion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PETICION_CONFLICTO_HORARIO));
            PeticionConflictoHorario peticion = getConversor().crearPeticionDesdeSecuencia(secPeticion);

            //Se busca la petición y se cambia el estado a cancelado
            PeticionConflictoHorario peticionExiste = getPeticionConflictoHorarioFacade().buscarPorIdYCorreo(peticion.getEstudiante().getPersona().getCorreo(), peticion.getId());
            if (peticionExiste.getFechaResolucion() != null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PETICION_CONFLICTO_HORARIO_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_006, new LinkedList<Secuencia>());
            }
            peticionExiste.setEstado(getConstanteBean().getConstante(Constantes.VAL_CONFLICTO_HORARIO_ESTADO_CANCELADO));
            peticionExiste.setFechaResolucion(new Timestamp(System.currentTimeMillis()));
            getPeticionConflictoHorarioFacade().edit(peticionExiste);

            //Notifica la cancelación de la petición de conflicto de horario
            String mensajeCreacion = Notificaciones.MENSAJE_CANCELACION_CONFLICTO_HORARIO;
            mensajeCreacion = mensajeCreacion.replaceFirst("%", peticionExiste.getEstudiante().getPersona().getNombres() + " " + peticionExiste.getEstudiante().getPersona().getApellidos());
            mensajeCreacion = mensajeCreacion.replaceFirst("%", getConversor().getPeticionHTML(peticionExiste));
            getCorreoBean().enviarMail(peticionExiste.getEstudiante().getPersona().getCorreo(), Notificaciones.ASUNTO_CANCELACION_CONFLICTO_HORARIO, null, null, null, mensajeCreacion);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PETICION_CONFLICTO_HORARIO_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_006, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PETICION_CONFLICTO_HORARIO_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_014, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }




    public String consultarCantidadPeticionesCursosISIS(String xml) {
        try {
            getParser().leerXML(xml);
            //Tipos de petición de conflicto
            String tipoAdicion = getConstanteBean().getConstante(Constantes.VAL_ADICION_CONFLICTO_HORARIO);
            String tipoRetiro = getConstanteBean().getConstante(Constantes.VAL_RETIRO_CONFLICTO_HORARIO);
            String tipoCambio = getConstanteBean().getConstante(Constantes.VAL_CAMBIO_CONFLICTO_HORARIO);

            //Consulta las peticiones de tipo adición, retiro y cambio para cada uno de los cursos de ISIS
            Secuencia secPeticionesCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PETICIONES_CONFLICTO_HORARIO), "");
            Collection<Curso> cursos = getCursoFacade().findAll();
            for (Curso curso : cursos) {
                int totalAdicion = getPeticionConflictoHorarioFacade().buscarPorCodigoCursoYTipo(curso.getCodigo(), tipoAdicion, -1, -1).size();
                int totalRetiro = getPeticionConflictoHorarioFacade().buscarPorCodigoCursoYTipo(curso.getCodigo(), tipoRetiro, -1, -1).size();
                int totalCambio = getPeticionConflictoHorarioFacade().buscarPorCodigoCursoYTipo(curso.getCodigo(), tipoCambio, -1, -1).size();

                Secuencia secCurso = getConversor().crearSecuenciaCantidadPeticionesPorCurso(curso, totalAdicion, totalRetiro, totalCambio);
                secPeticionesCursos.agregarSecuencia(secCurso);
            }

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secPeticionesCursos);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_MATERIAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_007, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_MATERIAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_015, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }


    public String consultarPeticionesResueltas(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secPeticiones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PETICIONES_CONFLICTO_HORARIO), "");
            
            Collection<PeticionConflictoHorario> peticiones = getPeticionConflictoHorarioFacade().buscarPeticionesResueltas();

            secPeticiones = getConversor().crearSecuenciaPeticiones(peticiones);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secPeticiones);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_RESUELTAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_007, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_RESUELTAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_015, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }




    public String consultarPeticionesPorCodigoCursoYTipo(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secuenciaPeticion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PETICION_CONFLICTO_HORARIO));
            PeticionConflictoHorario peticion = getConversor().crearPeticionDesdeSecuencia(secuenciaPeticion);
            Secuencia secPosicionInicial = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_INICIO));
            Secuencia secResultadosMaximos = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_CANTIDAD));

            //Consulta las peticiones de tipo adición, retiro y cambio para el curso dado
            int posicionInicial = (secPosicionInicial != null)? secPosicionInicial.getValorInt() : -1;
            int resultadosMaximos = (secResultadosMaximos != null)? secResultadosMaximos.getValorInt() : -1;

            Collection<PeticionConflictoHorario> peticiones = getPeticionConflictoHorarioFacade().buscarPorCodigoCursoYTipo(peticion.getCursoDestino().getCodigo(), peticion.getTipo(), posicionInicial, resultadosMaximos);
            Secuencia secPeticiones = getConversor().crearSecuenciaPeticiones(peticiones);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secPeticiones);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_POR_MATERIA_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_008, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PETICIONES_MATERIAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_016, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String actualizarEstadoYResolucionPeticiones(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secPeticiones = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PETICIONES_CONFLICTO_HORARIO));
            Collection<PeticionConflictoHorario> peticiones = getConversor().crearPeticionesDesdeSecuencia(secPeticiones);
            
            for (PeticionConflictoHorario peticion : peticiones) {
                //Se busca y actualiza la petición
                PeticionConflictoHorario peticionExiste = getPeticionConflictoHorarioFacade().buscarPorIdYCorreo(peticion.getEstudiante().getPersona().getCorreo(), peticion.getId());
                if (peticionExiste == null) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_PETICIONES_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_017, new LinkedList<Secuencia>());
                }

                peticionExiste.setComentariosResolucion(peticion.getComentariosResolucion());
                peticionExiste.setEstado(peticion.getEstado());
                peticionExiste.setFechaResolucion(new Timestamp(System.currentTimeMillis()));

                getPeticionConflictoHorarioFacade().edit(peticionExiste);

                //Notifica la actualización de la petición de conflicto de horario
                String mensajeCreacion = Notificaciones.MENSAJE_RESOLUCION_CONFLICTO_HORARIO;
                mensajeCreacion = mensajeCreacion.replaceFirst("%", peticionExiste.getEstudiante().getPersona().getNombres() + " " + peticionExiste.getEstudiante().getPersona().getApellidos());
                mensajeCreacion = mensajeCreacion.replaceFirst("%", getConversor().getPeticionHTML(peticionExiste));
                getCorreoBean().enviarMail(peticionExiste.getEstudiante().getPersona().getCorreo(), Notificaciones.ASUNTO_RESOLUCION_CONFLICTO_HORARIO, null, null, null, mensajeCreacion);
            }

            ArrayList<Secuencia> params = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_PETICIONES_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_009, params);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_PETICIONES_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_018, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarFechasConflictoHorario(String xml) {
        try {
            getParser().leerXML(xml);
            
            //Valida que haya una fecha configurada
            if(getConfiguracionFechasFacade().findAll().isEmpty()){
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_FECHAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_028, new LinkedList<Secuencia>());
            }
            
            //Consulta la configuración
            ConfiguracionFechasCH cofiguracion = getConfiguracionFechasFacade().findAll().get(0);
            Secuencia secConfiguracion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_FECHA_CREACION_CONFLICTO_HORARIO), "");
            secConfiguracion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_INICIO_FECHAS_CONFLICTO_HORARIO), Long.toString(cofiguracion.getInicioConflictos().getTime())));
            secConfiguracion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_FIN_FECHAS_CONFLICTO_HORARIO), Long.toString(cofiguracion.getFinConflictos().getTime())));

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secConfiguracion);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_FECHAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_012, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_FECHAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_022, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String actualizarFechasConflictoHorario(String xml) {
        try {
            getParser().leerXML(xml);
            //Se extraen los parámetros
            Secuencia secuencia = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_FECHA_CREACION_CONFLICTO_HORARIO));
            
            //Elimina todas las configuraciones previas y crea una nueva
            getConfiguracionFechasFacade().removeAll();
            ConfiguracionFechasCH configuracion = new ConfiguracionFechasCH();
            SimpleDateFormat sdfHMS = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'COT' yyyy", new Locale("en"));
            Date fechaInicio = sdfHMS.parse(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_INICIO_FECHAS_CONFLICTO_HORARIO)).getValor());
            Date fechaFin = sdfHMS.parse(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_FIN_FECHAS_CONFLICTO_HORARIO)).getValor());
            configuracion.setInicioConflictos(new Timestamp(fechaInicio.getTime()));
            configuracion.setFinConflictos(new Timestamp(fechaFin.getTime()));
            getConfiguracionFechasFacade().create(configuracion);

            ArrayList<Secuencia> params = new ArrayList<Secuencia>();
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_FECHAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_013, params);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_FECHAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_023, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarProgramas(String xml) {
        try {
            getParser().leerXML(xml);

            //Consulta todos los programas
            Collection<Programa> programas = getProgramaFacade().findAll();
            Secuencia secProgramas = getConversor().crearSecuenciaProgramas(programas);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secProgramas);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROGRAMAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_012, secuencias);

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROGRAMAS_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.CONFLICTOS_ERR_022, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }


    public void eliminarPeticionesPorSeccion(Long id) {
        try {

            Collection<PeticionConflictoHorario> peticionesConflictoHorario =  peticionConflictoHorarioFacade.buscarPorSeccion(id);

            String asunto = Notificaciones.ASUNTO_PETICION_ELIMINADA_POR_SECCION_ELIMINADA;

            for (PeticionConflictoHorario p : peticionesConflictoHorario) {

                if(p.getFechaResolucion() == null){
                    
                    String mensaje = Notificaciones.MENSAJE_PETICION_ELIMINADA_POR_SECCION_ELIMINADA;
                    mensaje = mensaje.replace("%1", p.getEstudiante().getPersona().getNombres() + " " + p.getEstudiante().getPersona().getApellidos());

                    if( p.getOrigen() == null){
                        if( p.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_ADICION_CONFLICTO_HORARIO)) ){
                            //mandar correo de asociado a la adicion
                            mensaje = mensaje.replace("%2", "ADICION para");
                        }else{
                            //mandar correo de asociado a retiro
                            mensaje = mensaje.replace("%2", "RETIRO para");
                        }

                        mensaje = mensaje.replace("%3", p.getDestino().getCrn());
                        mensaje = mensaje.replace("%4", p.getCursoDestino().getNombre());
                    }else{
                            if(p.getOrigen().getId().equals(id)){
                                //mandar correo asociado a cambio con la seccion de origen a cancelar
                                mensaje = mensaje.replace("%2", "CAMBIO desde");
                                mensaje = mensaje.replace("%3", p.getOrigen().getCrn());
                                mensaje = mensaje.replace("%4", p.getCursoOrigen().getNombre());
                            }else{
                                //mandar correo asociado a cambio con la seccion destino a cancelar
                                mensaje = mensaje.replace("%2", "CAMBIO a");
                                mensaje = mensaje.replace("%3", p.getDestino().getCrn());
                                mensaje = mensaje.replace("%4", p.getCursoDestino().getNombre());
                            }
                    }

                    correoBean.enviarMail(p.getEstudiante().getPersona().getCorreo(), asunto, null, null, null, mensaje);
                }

                peticionConflictoHorarioFacade.remove(p);
            }

        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                //return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SECCION_CONFLICTO_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                //return null;
            }
        }
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private ConfiguracionFechasCHFacadeLocal getConfiguracionFechasFacade() {
        return configuracionFechasFacade;
    }

    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    private CursoFacadeRemote getCursoFacade() {
        return cursoFacade;
    }

    private ConflictoHorariosBeanHelper getConversor() {
        return conversor;
    }

    private PeticionConflictoHorarioFacadeLocal getPeticionConflictoHorarioFacade() {
        return peticionConflictoHorarioFacade;
    }

    private ProgramaFacadeRemote getProgramaFacade() {
        return programaFacade;
    }
}
