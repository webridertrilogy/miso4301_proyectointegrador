/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoLocal;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicio de negocio: Administración de convenio
 */
@Stateless
@EJB(name = "ConvenioBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ConvenioLocal.class)
public class ConvenioBean implements  ConvenioLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * SolicitudFacade
     */
    @EJB
    private SolicitudFacadeLocal solicitudFacade;
    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeLocal cursoFacade;
    /**
     * MonitoriaFacade
     */
    @EJB
    private MonitoriaFacadeLocal monitoriaFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteLocal constanteBean;

    /**
     * CorreoBean
     */
    @EJB
    private CorreoLocal correoBean;
    @EJB
    private RangoFechasBeanLocal rangoFechasBean;


    private ServiceLocator serviceLocator;
    //---------------------------------------
    // Constructor
    //---------------------------------------

    /**
     * Constructor de ConvenioBean
     */
    public ConvenioBean() {
//        try {
//            serviceLocator = new ServiceLocator();
//            correoBean = (CorreoLocal) serviceLocator.getLocalEJB(CorreoLocal.class);
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            cursoFacade = (CursoFacadeLocal) serviceLocator.getLocalEJB(CursoFacadeLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String registrarConvenioPendienteEstudiante(String xml) {
        try {
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();

            Solicitud sol = solicitudFacade.findById(Long.parseLong(idSolicitud.trim()));
            rangoFechasBean.completarTareasPendientes(sol.getEstudiante().getEstudiante().getPersona().getCorreo(),getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_CONVENIO_ESTUDIANTE),idSolicitud);

            Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
            if (solicitud == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, new LinkedList<Secuencia>());
            } else {
                //Mandar correo contacto profesor y estudiante
                String correoEstudiante = solicitud.getEstudiante().getPersona().getCorreo();
                String nombreEstudiante = solicitud.getEstudiante().getPersona().getNombres() + " " + solicitud.getEstudiante().getPersona().getApellidos();
                for (MonitoriaAceptada mon : solicitud.getMonitorias()) {
                    String correoProfesor = mon.getSeccion().getProfesorPrincipal().getPersona().getCorreo();
                    String nombreProfesor = mon.getSeccion().getProfesorPrincipal().getPersona().getNombres() + " " + mon.getSeccion().getProfesorPrincipal().getPersona().getApellidos();
                    String numeroSeccion = "" + mon.getSeccion().getNumeroSeccion();
                    String nombreCurso = getCursoFacade().findByCRNSeccion(mon.getSeccion().getCrn()).getNombre();
                    String s = Notificaciones.MENSAJE_CONTACTO;
                    String tipoMonitoria = (mon.getCarga() == Integer.parseInt(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T1))) ? "T1" : "T2";
                    String mensaje = String.format(s, tipoMonitoria, nombreEstudiante, "" + numeroSeccion, nombreCurso, nombreProfesor);


                    getCorreoBean().enviarMail(correoEstudiante + ", " + correoProfesor, Notificaciones.ASUNTO_CONTACTO, null, null, null, mensaje);
                }
                                                                                

                //Mandar correo pasar a firmar solicitud
                getCorreoBean().enviarMail(correoEstudiante, Notificaciones.ASUNTO_FIRMA, null, null, null, Notificaciones.MENSAJE_FIRMA);

                solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE));
                //Editar el estado de la solicitud
                getSolicitudFacade().edit(solicitud);
                rangoFechasBean.crearTareaRegistarFirmaConveniosEstudiantes(Long.parseLong(idSolicitud));
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0012, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_ESTUDIANTE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0013, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private void terminarTareaRegistrarConvenioEstudiante(String idSolicitud) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_CONVENIO_ESTUDIANTE);
        rangoFechasBean.realizarTareaSolicitud(tipo, Long.parseLong(idSolicitud));
    }



    @Override
    public String registrarConvenioPendienteDepartamento(String xml) {
        try {
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_FIRMA_CONVENIO_ESTUDIANTE);
            rangoFechasBean.realizarTareaSolicitud(tipo, Long.parseLong(idSolicitud));
                        
            Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
            if (solicitud == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_DEPARTAMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, new LinkedList<Secuencia>());
            } else {
                solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO));
                getSolicitudFacade().edit(solicitud);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_DEPARTAMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0012, new LinkedList<Secuencia>());
            }
            

        } catch (Exception e) {
            try {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_DEPARTAMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0013, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String registrarConvenioPendienteRadicacion(String xml) {
        try {
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
   
            Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
            if (solicitud == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_RADICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, new LinkedList<Secuencia>());
            } else {
                solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_RADICACION));
                //Editar el estado de la solicitud
                getSolicitudFacade().edit(solicitud);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_RADICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0012, new LinkedList<Secuencia>());
            }
            
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_RADICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0013, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String radicarConvenio(String xml) {
        try {
            getParser().leerXML(xml);
        
            Secuencia secuenciaSolicitudes = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            String fechaRadicacion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_RADICACION)).getValor();
            String numeroRadicacion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_RADICACION)).getValor();
            ArrayList<Secuencia> secuenciasSolicitudes = secuenciaSolicitudes.getSecuencias();
            Iterator<Secuencia> iterator = secuenciasSolicitudes.iterator();
            while (iterator.hasNext()) {
                String idSolicitud = iterator.next().getValor();
                Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
                solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_ASIGNADO));
                solicitud.setFechaRadicacion(Timestamp.valueOf(fechaRadicacion));
                solicitud.setNumeroRadicacion(numeroRadicacion);
                getSolicitudFacade().edit(solicitud);          
            }


            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RADICAR_CONVENIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0012, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                e.printStackTrace();
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RADICAR_CONVENIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0012, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darConvenios(String xml) {
        try {
            getParser().leerXML(xml);
            Collection<Solicitud> solicitudes = new LinkedList<Solicitud>();
            solicitudes.addAll(getSolicitudFacade().findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO)));
            solicitudes.addAll(getSolicitudFacade().findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE)));
            solicitudes.addAll(getSolicitudFacade().findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_RADICACION)));
            solicitudes.addAll(getSolicitudFacade().findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_REGISTRO_CONVENIO)));
            ArrayList<Secuencia> secuenciasSolicitudes = new ArrayList<Secuencia>();
            Iterator iterator = solicitudes.iterator();
            while (iterator.hasNext()) {
                Solicitud solicitud = (Solicitud) iterator.next();
                Secuencia secuenciaSolicitud = getSecuenciaSolicitud(solicitud);
                secuenciasSolicitudes.add(secuenciaSolicitud);
            }
            Secuencia secuenciaSolicitudes = new Secuencia(new ArrayList<Atributo>(), secuenciasSolicitudes);
            secuenciaSolicitudes.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            Collection<Secuencia> secuencia = new ArrayList<Secuencia>();
            secuencia.add(secuenciaSolicitudes);
            Collection secuencias = new LinkedList();
            secuencias.add(secuenciaSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_CONVENIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0050, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_CONVENIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0068, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darConveniosSecretaria(String xml) {
        try {
            getParser().leerXML(xml);
            Collection<Solicitud> solicitudes = getSolicitudFacade().findConveniosSecretaria();
            ArrayList<Secuencia> secuenciasSolicitudes = new ArrayList<Secuencia>();
            Iterator iterator = solicitudes.iterator();
            while (iterator.hasNext()) {
                Solicitud solicitud = (Solicitud) iterator.next();
                Secuencia secuenciaSolicitud = getSecuenciaSolicitudSecretaria(solicitud);
                secuenciasSolicitudes.add(secuenciaSolicitud);
            }
            Secuencia secuenciaSolicitudes = new Secuencia(new ArrayList<Atributo>(), secuenciasSolicitudes);
            secuenciaSolicitudes.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            Collection<Secuencia> secuencia = new ArrayList<Secuencia>();
            secuencia.add(secuenciaSolicitudes);
            Collection secuencias = new LinkedList();
            secuencias.add(secuenciaSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_POR_ESTADO_SECRETARIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0050, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_POR_ESTADO_SECRETARIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0068, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Metodo que retorna una secuencia para armar la respuesta de los comandos relacionados con solicitud
     * @param solicitud - Solicitud a partir de la cual se creara una secuencia
     * @return Secuencia: retorna la secuencia creada a partir de la solicitud
     */
    private Secuencia getSecuenciaSolicitud(Solicitud solicitud) {
        Secuencia secuenciaSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId())));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud()));
        Secuencia secuenciaInformacionPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getEstudiante().getPersona().getNombres()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getEstudiante().getPersona().getApellidos()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), solicitud.getEstudiante().getPersona().getCodigo()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), solicitud.getEstudiante().getPersona().getCorreo()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), solicitud.getEstudiante().getPersona().getFechaNacimiento().toString()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), solicitud.getEstudiante().getPersona().getNumDocumentoIdentidad()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), solicitud.getEstudiante().getPersona().getTipoDocumento().getTipo()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA), solicitud.getEstudiante().getPersona().getDireccionResidencia()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), solicitud.getEstudiante().getPersona().getPais().getNombre()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA), solicitud.getEstudiante().getPersona().getTelefono()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), solicitud.getEstudiante().getPersona().getCelular()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO), solicitud.getEstudiante().getEstudiante().getBanco()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA), solicitud.getEstudiante().getEstudiante().getCuentaBancaria()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA), (solicitud.getEstudiante().getEstudiante().getTipoCuenta() != null)?solicitud.getEstudiante().getEstudiante().getTipoCuenta().getNombre():""));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), solicitud.getEstudiante().getEstudiante().getPrograma().getNombre()));
        secuenciaSolicitud.agregarSecuencia(secuenciaInformacionPersonal);
        Secuencia secuenciaInformacionEmergencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaInformacionEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getEstudiante().getEstudiante().getAvisarEmergenciaNombres()));
        secuenciaInformacionEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getEstudiante().getEstudiante().getAvisarEmergenciaApellidos()));
        secuenciaInformacionEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA), solicitud.getEstudiante().getEstudiante().getTelefonoEmergencia()));
        secuenciaSolicitud.agregarSecuencia(secuenciaInformacionEmergencia);
        Secuencia secuenciaInformacionAcademica = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS), solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getSemestreSegunCreditos()));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioTotal())));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosSemestreActual())));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosAprobados())));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosCursados())));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioUltimo())));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioPenultimo())));
        secuenciaInformacionAcademica.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES), Double.toString(solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioAntepenultipo())));
        secuenciaSolicitud.agregarSecuencia(secuenciaInformacionAcademica);
        Secuencia secuenciaMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA), getConstanteBean().getConstante(Constantes.NULL));
        Secuencia secuenciaCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), getConstanteBean().getConstante(Constantes.NULL));
        /*Collection<Seccion> secciones = solicitud.getMonitoria_solicitada().getSeccionesEscogidas();
        Iterator<Seccion> iterator = secciones.iterator();
        int monitorias = 0;
        while (iterator.hasNext()) {
            Seccion seccion = iterator.next();
            MonitoriaAceptada monitoria = getMonitoriaFacade().findByCRNYCorreo(seccion.getCrn(), solicitud.getEstudiante().getPersona().getCorreo());
            if (monitoria != null) {
                monitorias++;
                Curso curso = getCursoFacade().findByCRNSeccion(seccion.getCrn());
                Secuencia secuenciaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
                secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
                secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
                Secuencia secuenciaSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secuenciaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getPersona().getNombres()));
                secuenciaSeccion.agregarSecuencia(secuenciaProfesor);
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(seccion.getNumeroSeccion())));
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
                secuenciaCurso.agregarSecuencia(secuenciaSeccion);
                secuenciaCursos.agregarSecuencia(secuenciaCurso);
            }
        }
        secuenciaMonitoria.agregarSecuencia(secuenciaCursos);

         if (monitorias == 1) {
            secuenciaMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), "T1"));
        } else {
            secuenciaMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), "T2"));
        }
         *
         */
        secuenciaSolicitud.agregarSecuencia(secuenciaMonitoria);
        return secuenciaSolicitud;
    }

    /**
     * Metodo que retorna una secuencia para armar la respuesta de los comandos relacionados con solicitud
     * @param solicitud - Solicitud a partir de la cual se creara una secuencia
     * @return Secuencia: retorna la secuencia creada a partir de la solicitud
     */
    private Secuencia getSecuenciaSolicitudSecretaria(Solicitud solicitud) {
        Secuencia secuenciaSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId())));
        secuenciaSolicitud.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud()));
        Secuencia secuenciaInformacionPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getEstudiante().getPersona().getNombres()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getEstudiante().getPersona().getApellidos()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), solicitud.getEstudiante().getPersona().getCodigo()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), solicitud.getEstudiante().getPersona().getCorreo()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), solicitud.getEstudiante().getPersona().getFechaNacimiento().toString()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), solicitud.getEstudiante().getPersona().getNumDocumentoIdentidad()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), solicitud.getEstudiante().getPersona().getTipoDocumento().getTipo()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA), solicitud.getEstudiante().getPersona().getDireccionResidencia()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), solicitud.getEstudiante().getPersona().getPais().getNombre()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA), solicitud.getEstudiante().getPersona().getTelefono()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), solicitud.getEstudiante().getPersona().getCelular()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO), solicitud.getEstudiante().getEstudiante().getBanco()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA), solicitud.getEstudiante().getEstudiante().getCuentaBancaria()));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA), (solicitud.getEstudiante().getEstudiante().getTipoCuenta() != null)?solicitud.getEstudiante().getEstudiante().getTipoCuenta().getNombre():""));
        secuenciaInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), solicitud.getEstudiante().getEstudiante().getPrograma().getNombre()));
        secuenciaSolicitud.agregarSecuencia(secuenciaInformacionPersonal);
        Secuencia secuenciaInformacionEmergencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaInformacionEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), solicitud.getEstudiante().getEstudiante().getAvisarEmergenciaNombres()));
        secuenciaInformacionEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), solicitud.getEstudiante().getEstudiante().getAvisarEmergenciaApellidos()));
        secuenciaInformacionEmergencia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA), solicitud.getEstudiante().getEstudiante().getTelefonoEmergencia()));
        secuenciaSolicitud.agregarSecuencia(secuenciaInformacionEmergencia);
                
        Secuencia secuenciaMonitoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA), getConstanteBean().getConstante(Constantes.NULL));
        Secuencia secuenciaCursos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS), getConstanteBean().getConstante(Constantes.NULL));
        Collection<Seccion> secciones = new ArrayList<Seccion>();
        Collection<MonitoriaAceptada> listaMonitorias = solicitud.getMonitorias();
        Iterator<MonitoriaAceptada> iteradorMonitorias = listaMonitorias.iterator();
        while(iteradorMonitorias.hasNext())
        {
            MonitoriaAceptada mon = iteradorMonitorias.next();
            secciones.add(mon.getSeccion());
        }        
        Iterator<Seccion> iterator = secciones.iterator();
        int monitorias = 0;
        while (iterator.hasNext()) {
            Seccion seccion = iterator.next();
            MonitoriaAceptada monitoria = getMonitoriaFacade().findByCRNYCorreo(seccion.getCrn(), solicitud.getEstudiante().getPersona().getCorreo());
            if (monitoria != null) {
                monitorias++;
                Curso curso = getCursoFacade().findByCRNSeccion(seccion.getCrn());
                Secuencia secuenciaCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), getConstanteBean().getConstante(Constantes.NULL));
                secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), curso.getCodigo()));
                secuenciaCurso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), curso.getNombre()));
                Secuencia secuenciaSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secuenciaProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                secuenciaProfesor.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getPersona().getNombres()));
                secuenciaSeccion.agregarSecuencia(secuenciaProfesor);
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(seccion.getNumeroSeccion())));
                secuenciaSeccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
                secuenciaCurso.agregarSecuencia(secuenciaSeccion);
                secuenciaCursos.agregarSecuencia(secuenciaCurso);
            }
        }
        secuenciaMonitoria.agregarSecuencia(secuenciaCursos);
        if (monitorias == 1) {
            secuenciaMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), "T1"));
        } else {
            secuenciaMonitoria.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), "T2"));
        }
        secuenciaSolicitud.agregarSecuencia(secuenciaMonitoria);
        return secuenciaSolicitud;
    }

    @Override
    public String darSolicitudSecretaria(String xml)
    {
        try {
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            Solicitud solicitud = solicitudFacade.findById(Long.valueOf(idSolicitud));
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            if(solicitud != null)
            {
                Secuencia secuencia = getSecuenciaSolicitud(solicitud);
                secuencias.add(secuencia);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_SECRETARIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList<Secuencia>());
            }
            else
            {
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_SECRETARIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_SECRETARIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0141, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Retorna el Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna SolicitudFacade
     * @return solicitudFacade SolicitudFacade
     */
    private SolicitudFacadeLocal getSolicitudFacade() {
        return solicitudFacade;
    }

    /**
     * Retorna CursoFacade
     * @return cursoFacade CursoFacade
     */
    private CursoFacadeLocal getCursoFacade() {
        return cursoFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    /**
     * Retorna MonitoriaFacade
     * @return monitoriaFacade MonitoriaFacade
     */
    private MonitoriaFacadeLocal getMonitoriaFacade() {
        return monitoriaFacade;
    }


    /**
     * Retorna CorreoBean
     * @return correoBean CorreoBean
     */
    private CorreoLocal getCorreoBean() {
        return correoBean;
    }

    
    public String registrarFirmasEstudiantes(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secSolicitudes = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            for (Secuencia secSolicitud : secSolicitudes.getSecuencias()) {
                String idSolicitud = secSolicitud.getValor();
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_FIRMA_CONVENIO_ESTUDIANTE);
                rangoFechasBean.realizarTareaSolicitud(tipo, Long.parseLong(idSolicitud));
                Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
                if (solicitud != null) {
                    solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO));
                    getSolicitudFacade().edit(solicitud);
                }
            }
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_FIRMAS_ESTUDIANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_FIRMAS_ESTUDIANTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0013, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    
    public String registrarFirmasDepartamento(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secSolicitudes = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            for (Secuencia secSolicitud : secSolicitudes.getSecuencias()) {
                String idSolicitud = secSolicitud.getValor();
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_CONVENIO_FIRMA_DEPARTAMENTO);
                rangoFechasBean.realizarTareaSolicitud(tipo, Long.parseLong(idSolicitud));
                Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
                if (solicitud != null) {
                    solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_RADICACION));
                    getSolicitudFacade().edit(solicitud);
                }
            }
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_FIRMAS_DEPARTAMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_FIRMAS_DEPARTAMENTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0013, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConvenioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }


}
