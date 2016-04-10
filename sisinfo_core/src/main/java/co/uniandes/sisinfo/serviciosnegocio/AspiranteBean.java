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

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.MonitoriaOtroDepartamento;
import co.uniandes.sisinfo.entities.Monitoria_Solicitada;


import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.soporte.Pais;
import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import co.uniandes.sisinfo.entities.soporte.TipoCuenta;
import co.uniandes.sisinfo.entities.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;



import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoCuentaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Administración de Aspirantes
 */
@Stateless
@EJB(name = "AspiranteBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.AspiranteLocal.class)
public class AspiranteBean implements AspiranteRemote, AspiranteLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * AspiranteFacade
     */
    @EJB
    private AspiranteFacadeLocal aspiranteFacade;
    /**
     * SolicitudFacade
     */
    @EJB
    private SolicitudFacadeLocal solicitudFacade;
    /**
     * PaisFacade
     */
    @EJB
    private PaisFacadeRemote paisFacade;
    /**
     * ProgramaFacade
     */
    @EJB
    private ProgramaFacadeRemote programaFacade;
    /**
     * TipoDocumentoFacade
     */
    @EJB
    private TipoDocumentoFacadeRemote tipoDocumentofacade;
    @EJB
    private TipoCuentaFacadeRemote tipoCuentaFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private EstudianteFacadeRemote estudianteFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    @EJB
    private ReglaRemote reglaBean;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de AspiranteBean 
     */
    public AspiranteBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB( ConstanteRemote.class);
            tipoDocumentofacade = (TipoDocumentoFacadeRemote) serviceLocator.getRemoteEJB(TipoDocumentoFacadeRemote.class);
            tipoCuentaFacade = (TipoCuentaFacadeRemote) serviceLocator.getRemoteEJB(TipoCuentaFacadeRemote.class);
            paisFacade = (PaisFacadeRemote) serviceLocator.getRemoteEJB(PaisFacadeRemote.class);
            programaFacade = (ProgramaFacadeRemote) serviceLocator.getRemoteEJB( ProgramaFacadeRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB( PersonaFacadeRemote.class);
            estudianteFacade = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            reglaBean = (ReglaRemote) serviceLocator.getRemoteEJB(ReglaRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String darDatosPersonales(String comando) {
        String respuesta = "";
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Aspirante estudiante = getAspiranteFacade().findByCorreo(correo);
            if(estudiante==null){
                Estudiante e=estudianteFacade.findByCorreo(correo);
                if(e!=null){
                    estudiante=new Aspirante();
                    estudiante.setEstudiante(e);
                }

            }
            if (estudiante == null) {
                ArrayList<Secuencia> parametros = new ArrayList<Secuencia>();
                parametros.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE),correo));
                respuesta = getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_PERSONALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0148, parametros);
                return respuesta;
            } else {
                Collection<Secuencia> secuencias = new Vector();
                Secuencia info = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.NULL));
                boolean editable = true;
                Collection<Solicitud> solicitudes = getSolicitudFacade().findByLogin(correo);
                for (Iterator<Solicitud> it = solicitudes.iterator(); it.hasNext();) {
                    Solicitud solicitud = it.next();
                    for (int i = 0; i < Constantes.JERARQUIA_ESTADOS.length; i++) {
                        String estado1 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[i]);
                        if (estado1.equals(solicitud.getEstadoSolicitud())) {
                            for (int j = 0; j < Constantes.JERARQUIA_ESTADOS.length; j++) {
                                String estado2 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[j]);
                                if (estado2.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_REGISTRO_CONVENIO))) {
                                    if (i <= j) {
                                        editable &= true;
                                    } else {
                                        editable &= false;
                                    }
                                }
                            }
                        }
                    }
                }
                if (editable) {
                    info.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), getConstanteBean().getConstante(Constantes.TRUE)));
                } else {
                    info.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), getConstanteBean().getConstante(Constantes.FALSE)));
                }
                Secuencia tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudiante.getPersona().getNombres());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudiante.getPersona().getApellidos());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), estudiante.getPersona().getCodigo());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudiante.getPersona().getCorreo());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD), getConstanteBean().getConstante(Constantes.NULL));
                tag.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_NOMBRE_FACULTAD), (estudiante.getEstudiante().getPrograma()!=null)?estudiante.getEstudiante().getPrograma().getFacultad().getNombre():getConstanteBean().getConstante(Constantes.NULL)));
                Secuencia secProg = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), (estudiante.getEstudiante().getPrograma()!=null)?estudiante.getEstudiante().getPrograma().getNombre():getConstanteBean().getConstante(Constantes.NULL));
                tag.agregarSecuencia(secProg);
                info.agregarSecuencia(tag);

                if (estudiante.getPersona().getFechaNacimiento() != null) {
                    Timestamp fechaNac = estudiante.getPersona().getFechaNacimiento();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), sdf.format(fechaNac));
                    info.agregarSecuencia(tag);
                } else {
                    tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO), getConstanteBean().getConstante(Constantes.NULL));
                    info.agregarSecuencia(tag);
                }
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), estudiante.getPersona().getNumDocumentoIdentidad());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), (estudiante.getPersona().getTipoDocumento() != null)?estudiante.getPersona().getTipoDocumento().getDescripcion():getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA), estudiante.getPersona().getDireccionResidencia());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), (estudiante.getPersona().getPais()!=null)?estudiante.getPersona().getPais().getNombre():getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA), estudiante.getPersona().getTelefono());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_NACIMIENTO), estudiante.getPersona().getCiudadNacimiento());
                info.agregarSecuencia(tag);
                Secuencia tagInfoEmergencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA), getConstanteBean().getConstante(Constantes.NULL));
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudiante.getEstudiante().getAvisarEmergenciaNombres());
                tagInfoEmergencia.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudiante.getEstudiante().getAvisarEmergenciaApellidos());
                tagInfoEmergencia.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA), estudiante.getEstudiante().getTelefonoEmergencia());
                tagInfoEmergencia.agregarSecuencia(tag);
                info.agregarSecuencia(tagInfoEmergencia);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), estudiante.getPersona().getCelular());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO), estudiante.getEstudiante().getBanco());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA), estudiante.getEstudiante().getCuentaBancaria());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA), (estudiante.getEstudiante().getTipoCuenta() != null)?estudiante.getEstudiante().getTipoCuenta().getNombre():getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                secuencias.add(info);
                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), estudiante.getPersona().getCodigo());
                sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CODIGO_ESTUDIANTE));
                Collection<Secuencia> secuenciasRespuesta = new Vector();
                secuenciasRespuesta.add(sec);
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_PERSONALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0003, secuenciasRespuesta);
                return respuesta;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_PERSONALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex1);
                return "";
            }
            return respuesta;
        }
    }

    @Override
    public String darDatosAcademicos(String xml) {
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Aspirante estudiante = getAspiranteFacade().findByCorreo(correo);
            String respuesta = null;
            if (estudiante == null) {

                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), correo);
                sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE));
                Collection<Secuencia> secuencias = new Vector();
                Collection<Secuencia> atts = new Vector();
                atts.add(sec);
                Secuencia info = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), getConstanteBean().getConstante(Constantes.NULL));
                secuencias.add(info);
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0002, atts);
                return respuesta;
            } else {
                Collection<Secuencia> secuencias = new Vector();
                Secuencia info = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), "");
                boolean editable = true;
                Collection<Solicitud> solicitudes = getSolicitudFacade().findByLogin(correo);
                boolean acabe = false;
                for (Iterator<Solicitud> it = solicitudes.iterator(); it.hasNext() && !acabe;) {
                    Solicitud solicitud = it.next();
                    for (int i = 0; i < Constantes.JERARQUIA_ESTADOS.length && !acabe; i++) {
                        String estado1 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[i]);
                        if (estado1.equals(solicitud.getEstadoSolicitud())) {
                            for (int j = 0; j < Constantes.JERARQUIA_ESTADOS.length; j++) {
                                String estado2 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[j]);
                                if (estado2.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_VERIFICACION_DATOS))) {
                                    if (i <= j) {
                                        editable &= true;
                                        acabe = true;
                                    } else {
                                        editable &= false;
                                    }
                                }
                            }
                        }
                    }
                }
                if (editable) {
                    info.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), getConstanteBean().getConstante(Constantes.TRUE)));
                } else {
                    info.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), getConstanteBean().getConstante(Constantes.FALSE)));
                }
                Secuencia tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS), (estudiante.getEstudiante().getInformacion_Academica()!=null)?estudiante.getEstudiante().getInformacion_Academica().getSemestreSegunCreditos():getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS), (estudiante.getEstudiante().getInformacion_Academica()!=null)?Double.toString(estudiante.getEstudiante().getInformacion_Academica().getCreditosAprobados()):getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS), (estudiante.getEstudiante().getInformacion_Academica()!=null)?Double.toString(estudiante.getEstudiante().getInformacion_Academica().getCreditosCursados()):getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), (estudiante.getEstudiante().getInformacion_Academica()!=null)?Double.toString(estudiante.getEstudiante().getInformacion_Academica().getCreditosSemestreActual()):getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES), (estudiante.getEstudiante().getInformacion_Academica()!=null)?Double.toString(estudiante.getEstudiante().getInformacion_Academica().getPromedioAntepenultipo()):getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES), (estudiante.getEstudiante().getInformacion_Academica()!=null)?Double.toString(estudiante.getEstudiante().getInformacion_Academica().getPromedioPenultimo()):getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO), (estudiante.getEstudiante().getInformacion_Academica()!=null)?Double.toString(estudiante.getEstudiante().getInformacion_Academica().getPromedioUltimo()):getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL), (estudiante.getEstudiante().getInformacion_Academica()!=null)?Double.toString(estudiante.getEstudiante().getInformacion_Academica().getPromedioTotal()):getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);

                // Nivel estudios.
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_FORMACION), (estudiante.getEstudiante().getInformacion_Academica()!=null)?estudiante.getEstudiante().getInformacion_Academica().getNivelEstudios():getConstanteBean().getConstante(Constantes.NULL));
                info.agregarSecuencia(tag);

                secuencias.add(info);
                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), correo);
                sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE));
                Collection<Secuencia> secuenciasRespuesta = new Vector();
                secuenciasRespuesta.add(sec);
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0002, secuenciasRespuesta);
                return respuesta;
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darDatosEmergencia(String xml) {
        String respuesta = null;
        String correo = null;
        String login = null;
        Collection<Secuencia> secuencias = new Vector();
        try {
            getParser().leerXML(xml);
            correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            login = correo.split("@")[0];
            Estudiante estudiante = getEstudianteFacade().findByCorreo(correo);
            if (estudiante == null) {

                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE));
                secuencias.add(sec);
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                Collection secsResp = new Vector<Secuencia>();
                Secuencia info = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA), getConstanteBean().getConstante(Constantes.NULL));
                secsResp.add(info);
                respuesta = getParser().generarRespuesta(secsResp, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_EMERGENCIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0039, parametros);
                return respuesta;
            } else {
                Secuencia info = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA), getConstanteBean().getConstante(Constantes.NULL));

                boolean editable = true;
                Collection<Solicitud> solicitudes = getSolicitudFacade().findByLogin(correo);
                for (Iterator<Solicitud> it = solicitudes.iterator(); it.hasNext();) {
                    Solicitud solicitud = it.next();
                    for (int i = 0; i < Constantes.JERARQUIA_ESTADOS.length; i++) {
                        String estado1 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[i]);
                        if (estado1.equals(solicitud.getEstadoSolicitud())) {
                            for (int j = 0; j < Constantes.JERARQUIA_ESTADOS.length; j++) {
                                String estado2 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[j]);
                                if (estado2.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_VERIFICACION_DATOS))) {
                                    if (i <= j) {
                                        editable &= true;
                                    } else {
                                        editable &= false;
                                    }
                                }
                            }
                        }
                    }
                }
                if (editable) {
                    info.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), getConstanteBean().getConstante(Constantes.TRUE)));
                } else {
                    info.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), getConstanteBean().getConstante(Constantes.FALSE)));
                }
                
                Secuencia tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudiante.getAvisarEmergenciaNombres());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudiante.getAvisarEmergenciaApellidos());
                info.agregarSecuencia(tag);
                tag = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA), estudiante.getTelefonoEmergencia());
                info.agregarSecuencia(tag);
                secuencias.add(info);
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_EMERGENCIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0039, parametros);
                return respuesta;
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_EMERGENCIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0058, parametros);
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    e.printStackTrace();
                    respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_EMERGENCIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, new ArrayList());
                    return respuesta;
                } catch (Exception ex2) {
                    Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex2);
                    return null;
                }
            }
        }
    }

    public String darMonitoriasOtrosDeptos(String comando) {
        try {
            Collection<Secuencia> secuencias = new Vector();
            String respuesta = null;
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String login = correo.split("@")[0];
            System.out.println("inside darMonitoriasOtrosDeptos correo es " + correo);
            Aspirante aspirante = aspiranteFacade.findByCorreo(correo);
            if (aspirante == null) {

                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_OTROS_DEPTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList());
                return respuesta;
            }
            else
            {
                Secuencia info = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIAS_OTRO_DEPTOS), getConstanteBean().getConstante(Constantes.NULL));
                boolean editable = true;
                Collection<Solicitud> solicitudes = getSolicitudFacade().findByLogin(correo);
                for (Iterator<Solicitud> it = solicitudes.iterator(); it.hasNext();) {
                    Solicitud solicitud = it.next();
                    for (int i = 0; i < Constantes.JERARQUIA_ESTADOS.length; i++) {
                        String estado1 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[i]);
                        if (estado1.equals(solicitud.getEstadoSolicitud())) {
                            for (int j = 0; j < Constantes.JERARQUIA_ESTADOS.length; j++) {
                                String estado2 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[j]);
                                if (estado2.equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_VERIFICACION_DATOS))) {
                                    if (i <= j) {
                                        editable &= true;
                                    } else {
                                        editable &= false;
                                    }
                                }
                            }
                        }
                    }
                }
                if (editable) {
                    info.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), getConstanteBean().getConstante(Constantes.TRUE)));
                } else {
                    info.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_EDITABLE), getConstanteBean().getConstante(Constantes.FALSE)));
                }

                Collection<MonitoriaOtroDepartamento> lista = aspirante.getMonitoriasOtrosDepartamentos();
                Iterator<MonitoriaOtroDepartamento> ite = lista.iterator();
                while(ite.hasNext())
                {
                    Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_OTRO_DEPTO), getConstanteBean().getConstante(Constantes.NULL));
                    MonitoriaOtroDepartamento mon = (MonitoriaOtroDepartamento)ite.next();
                    Secuencia codCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO),mon.getCodigoCurso());
                    Secuencia nomCurso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), mon.getNombreCurso());
                    Secuencia tagProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                    Secuencia nomProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), mon.getNombreProfesor());
                    Secuencia perAcad = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_ACADEMICO), mon.getPeriodo());
                    tagProfesor.agregarSecuencia(nomProfesor);
                    secuencia.agregarSecuencia(codCurso);
                    secuencia.agregarSecuencia(nomCurso);
                    secuencia.agregarSecuencia(tagProfesor);
                    secuencia.agregarSecuencia(perAcad);
                    info.agregarSecuencia(secuencia);
                }
                secuencias.add(info);
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_OTROS_DEPTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new LinkedList());
                return respuesta;
            }
        } catch(Exception e) {
            try {
                Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_MONITORIAS_OTROS_DEPTOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    @Override
    public String actualizarDatosAcademicos(String comando) {
        String respuesta = null;
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Aspirante estudiante = getAspiranteFacade().findByCorreo(correo);
            if (estudiante == null) {

                respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0071, new ArrayList<Secuencia>());
                return respuesta;
            } else {
                Secuencia informacionAcademica = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA));
                String semestreSegunCreditos = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS)).getValor();
                String promedioTotal = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL)).getValor();
                String creditosSemestreActual = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL)).getValor();
                String creditosAprobados = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS)).getValor();
                String creditosCursados = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS)).getValor();
                String promedioUltimo = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO)).getValor();
                String promedioHaceDosSemestres = (informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES))!=null)?informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES)).getValor():null;
                String promedioHaceTresSemestres = (informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES))!=null)?informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES)).getValor():null;
                InformacionAcademica informacion_academica = estudiante.getEstudiante().getInformacion_Academica();
                informacion_academica.setCreditosAprobados(Double.parseDouble(creditosAprobados));
                informacion_academica.setCreditosCursados(Double.parseDouble(creditosCursados));
                informacion_academica.setCreditosSemestreActual(Double.parseDouble(creditosSemestreActual));
                if(promedioHaceTresSemestres!=null)
                {
                    informacion_academica.setPromedioAntepenultipo(Double.parseDouble(promedioHaceTresSemestres));
                }
                if(promedioHaceDosSemestres != null)
                {
                    informacion_academica.setPromedioPenultimo(Double.parseDouble(promedioHaceDosSemestres));
                }
                informacion_academica.setPromedioTotal(Double.parseDouble(promedioTotal));
                informacion_academica.setPromedioUltimo(Double.parseDouble(promedioUltimo));
                informacion_academica.setSemestreSegunCreditos(semestreSegunCreditos);
                Estudiante e = estudiante.getEstudiante();
                e.setInformacion_Academica(informacion_academica);
                boolean pasoVerificacion=true;

                List<Solicitud> listaSols=getSolicitudFacade().findByLogin(correo.split("@")[0]);
                for (Solicitud solicitud : listaSols) {
                    solicitud.getEstudiante().setEstudiante(e);
                    pasoVerificacion&=verificarReglas(solicitud);
                }
                if (pasoVerificacion) {
                    getEstudianteFacade().edit(e);
                    respuesta = getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0038, new ArrayList<Secuencia>());
                }
                else{
                    respuesta = getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.ERR_0133, new ArrayList<Secuencia>());
                }
                return respuesta;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0058, new ArrayList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex);
                return respuesta;
            }
        }
    }


    private boolean verificarReglas(Solicitud solicitud) {
        Aspirante aspirante = solicitud.getEstudiante();
        Monitoria_Solicitada monitoria_Solicitada = solicitud.getMonitoria_solicitada();
        double nota = monitoria_Solicitada.getNota_materia();
        double promedio = aspirante.getEstudiante().getInformacion_Academica().getPromedioTotal();
        double creditos = aspirante.getEstudiante().getInformacion_Academica().getCreditosSemestreActual() - Double.valueOf(getConstanteBean().getConstante(Constantes.VAL_CREDITOS_MONITORIA_T1T));

        boolean valNota;
        if (nota == -1) {
            valNota = true;
        } else {
            valNota = getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_NOTA_MATERIA), Double.toString(nota));
        }

        if (promedio >= Double.parseDouble(getConstanteBean().getConstante(Constantes.PROMEDIO_MINIMO_EXTRACREDITOS))) {
            return getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_PROMEDIO_TOTAL), Double.toString(promedio))
                    && getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL), Double.toString(creditos))
                    && valNota;
        } else {
            return getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_PROMEDIO_TOTAL), Double.toString(promedio))
                    && getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO), Double.toString(creditos))
                    && valNota;
        }
    }

    @Override
    public String actualizarDatosPersonalesYEmergencia(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Aspirante estudiante = getAspiranteFacade().findByCorreo(correo);
            String respuesta = null;
            if (estudiante == null) {
                /*ArrayList<Secuencia> parametros = new ArrayList<Secuencia>();
                parametros.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE),correo));
                respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PERSONALES_Y_EMERGENCIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0148, parametros);*/
                respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PERSONALES_Y_EMERGENCIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0071, new ArrayList<Secuencia>());
                return respuesta;
            } else {
                Secuencia informacionPersonal = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL));
                String nombres = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
                String apellidos = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
                String fechaNacimiento = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_NACIMIENTO)).getValor();
                String documento = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO)).getValor();
                String tipoDocumentoStr = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO)).getValor();
                String direccionResidencia = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_RESIDENCIA)).getValor();
                String nombrePais = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS)).getValor();
                Pais pais = getPaisFacade().findByNombre(nombrePais);
                String lugarNacimiento = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LUGAR_NACIMIENTO)).getValor();
                String telResidencia = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_RESIDENCIA)).getValor();
                String celular = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR)).getValor();
                String banco = (informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO)) != null)? informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_BANCO)).getValor():null;
                String cuenta = (informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA)) != null)? informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA)).getValor():null;
                String tipoCuenta = (informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA)) != null)?informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CUENTA)).getValor():null;
                Secuencia secuenciaFacultad = informacionPersonal.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD));
                String nombrePrograma = secuenciaFacultad.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO)).getValor();
                Programa programaAcademico = getProgramaFacade().findByNombre(nombrePrograma);
                Secuencia informacionEmergencia = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_EMERGENCIA));
                String apellidosEmergencia = informacionEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
                String nombresEmergencia = informacionEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
                String telefonoEmergencia = informacionEmergencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO_EMERGENCIA)).getValor();

                Persona p = estudiante.getPersona();
                Estudiante e = estudiante.getEstudiante();
                p.setApellidos(apellidos);
                if(banco != null)
                {
                    e.setBanco(banco);
                }
                p.setCelular(celular);
                if(cuenta != null)
                {
                    e.setCuentaBancaria(cuenta);
                }
                p.setDireccionResidencia(direccionResidencia);
                p.setNumDocumentoIdentidad(documento);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp time = new Timestamp(format.parse(fechaNacimiento).getTime());
                p.setFechaNacimiento(time);
                p.setNombres(nombres);
                p.setPais(pais);
                p.setCiudadNacimiento(lugarNacimiento);
                e.setPrograma(programaAcademico);
                p.setTelefono(telResidencia);

                if(tipoCuenta != null)
                {
                    List<TipoCuenta> tipoC = getTipoCuentaFacade().findByName(tipoCuenta);
                    if (!tipoC.isEmpty()) {
                        e.setTipoCuenta(tipoC.get(0));
                    }
                }
                TipoDocumento tipoDocumento = getTipoDocumentofacade().findByDescripcion(tipoDocumentoStr);
                p.setTipoDocumento(tipoDocumento);
                e.setAvisarEmergenciaApellidos(apellidosEmergencia);
                e.setAvisarEmergenciaNombres(nombresEmergencia);
                e.setTelefonoEmergencia(telefonoEmergencia);

                getPersonaFacade().edit(p);
                e.setPersona(p);
                getEstudianteFacade().edit(e);
                getAspiranteFacade().edit(estudiante);


                respuesta = getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PERSONALES_Y_EMERGENCIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0041, new ArrayList<Secuencia>());
                return respuesta;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, e);
            try {
                String respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PERSONALES_Y_EMERGENCIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0061, new ArrayList<Secuencia>());
                return respuesta;
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(AspiranteBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

        }
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
     * Retorna AspiranteFacade
     * @return aspiranteFacade AspiranteFacade
     */
    private AspiranteFacadeLocal getAspiranteFacade() {
        return aspiranteFacade;
    }

    /**
     * Retorna TipoDocumentoFacade
     * @return tipoDocumentoFacade TipoDocumentoFacade
     */
    private TipoDocumentoFacadeRemote getTipoDocumentofacade() {
        return tipoDocumentofacade;
    }

    /**
     * Retorna PaisFacade
     * @return paisFacade PaisFacade
     */
    private PaisFacadeRemote getPaisFacade() {
        return paisFacade;
    }

    /**
     * Retorna ProgramaFacade
     * @return programaFacade ProgramaFacade
     */
    private ProgramaFacadeRemote getProgramaFacade() {
        return programaFacade;
    }

    /**
     * Retorna ReglaBean
     * @return reglaBean ReglaBean
     */
    private ReglaRemote getReglaBean() {
        return reglaBean;
    }
    
    /**
     * Retorna SolicitudFacade
     * @return solicitudFacade SolicitudFacade
     */
    private SolicitudFacadeLocal getSolicitudFacade() {
        return solicitudFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public TipoCuentaFacadeRemote getTipoCuentaFacade() {
        return tipoCuentaFacade;
    }

    public EstudianteFacadeRemote getEstudianteFacade() {
        return estudianteFacade;
    }

    public PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }
}
