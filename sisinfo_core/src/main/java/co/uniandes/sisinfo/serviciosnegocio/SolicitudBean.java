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
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.Horario_Disponible;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.MonitoriaOtroDepartamento;
import co.uniandes.sisinfo.entities.MonitoriaRealizada;
import co.uniandes.sisinfo.entities.Monitoria_Solicitada;
import co.uniandes.sisinfo.entities.Regla;


import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;

import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.DiaCompletoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.Horario_DisponibleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaOtroDepartamentoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaRealizadaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.Monitoria_SolicitadaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReglaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;

import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.EstudianteFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.InformacionAcademicaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.NivelFormacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProgramaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoCuentaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;

import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
 * Servicio de negocio: Administración de solicitudes
 */
@Stateless
@EJB(name = "SolicitudBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.SolicitudLocal.class)
public class SolicitudBean implements SolicitudRemote, SolicitudLocal {

    private enum ValidacionInformacion {

        VALIDACION_EXITOSA,
        NUMERO_MONITORIAS, NIVEL_MATERIA, NUMERO_CREDITOS, NOTA_MATERIA, PROMEDIO_TOTAL
    };
    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Logger
     */
    private Logger log = Logger.getLogger(SolicitudBean.class.getName());
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
     * AspiranteFacade
     */
    @EJB
    private AspiranteFacadeLocal aspiranteFacade;
    /**
     * Horario_DisponibleFacade
     */
    @EJB
    private Horario_DisponibleFacadeLocal horario_DisponibleFacade;
    /**
     * Informacion_AcademicaFacade
     */
    @EJB
    private InformacionAcademicaFacadeRemote informacion_AcademicaFacade;
    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeRemote cursoFacade;
    /**
     * ReglaBean
     */
    @EJB
    private ReglaRemote reglaBean;
    /**
     * MonitoriaOtroDepartamentoFacade
     */
    @EJB
    private MonitoriaOtroDepartamentoFacadeLocal monitoriaOtrosDepartamentosFacade;
    /**
     * SeccionFacade
     */
    @EJB
    private SeccionFacadeRemote seccionFacade;
    /**
     * ConsultaBean
     */
    @EJB
    private ConsultaLocal consultaBean;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    /**
     * PreseleccionBean
     */
    @EJB
    private PreseleccionLocal preseleccionBean;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private EstudianteFacadeRemote estudianteFacade;
    @EJB
    private TipoCuentaFacadeRemote tipoCuentaFacade;
    private ServiceLocator serviceLocator;
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private RangoFechasBeanLocal rangoFechasBean;
    @EJB
    private ConvocatoriaRemote convocatoriaBean;
    @EJB
    private ListaNegraLocal listaNegraBean;
    @EJB
    private Monitoria_SolicitadaFacadeLocal monitoriaSolicitadaFacade;
    @EJB
    private MonitoriaRealizadaFacadeLocal monitoriaRealizadaFacade;
    @EJB
    private DiaCompletoFacadeRemote diaCompletoFacade;
    @EJB
    private ReglaFacadeRemote reglaFacade;

    public DiaCompletoFacadeRemote getDiaCompletoFacade() {
        return diaCompletoFacade;

    }

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de SolicitudBean
     */
    public SolicitudBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);

            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            estudianteFacade = (EstudianteFacadeRemote) serviceLocator.getRemoteEJB(EstudianteFacadeRemote.class);
            tipoCuentaFacade = (TipoCuentaFacadeRemote) serviceLocator.getRemoteEJB(TipoCuentaFacadeRemote.class);

            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            informacion_AcademicaFacade = (InformacionAcademicaFacadeRemote) serviceLocator.getRemoteEJB(InformacionAcademicaFacadeRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            reglaBean = (ReglaRemote) serviceLocator.getRemoteEJB(ReglaRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            convocatoriaBean = (ConvocatoriaRemote) serviceLocator.getRemoteEJB(ConvocatoriaRemote.class);
            diaCompletoFacade = (DiaCompletoFacadeRemote) serviceLocator.getRemoteEJB(DiaCompletoFacadeRemote.class);
            reglaFacade = (ReglaFacadeRemote) serviceLocator.getRemoteEJB(ReglaFacadeRemote.class);
        } catch (NamingException ex) {
            ex.printStackTrace();
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String darSolicitudes(String comando) {
        String retorno = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        try {
            try {
                getParser().leerXML(comando);
            } catch (Exception e) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            }
            Collection<Solicitud> solicitudes = getSolicitudFacade().findByNotEstado(getConstanteBean().getConstante(Constantes.ESTADO_INVALIDA));
            Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), getConstanteBean().getConstante(Constantes.NULL));
            if (solicitudes.size() != 0) {
                for (Iterator<Solicitud> iter = solicitudes.iterator(); iter.hasNext();) {
                    Solicitud solicitud = iter.next();
                    secSolicitudes.agregarSecuencia(new ConversorSolicitud().darSecuenciaSolicitudSinHorario(solicitud));
                }
                secuencias.add(secSolicitudes);
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0056, parametros);
                    return retorno;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0057, parametros);
                    return retorno;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                        return retorno;
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0074, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno;
    }

    @Override
    public String darSolicitudesMonitoresCandidatosT2(String comando) {
        String retorno = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        try {
            try {
                getParser().leerXML(comando);
            } catch (Exception e) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_CANDIDATOS_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            }
            Secuencia secSecciones = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
            Collection<Secuencia> secsSeccion = secSecciones.getSecuencias();

            //
            int i = 0;
            String crnSeccion1 = "", crnSeccion2 = "";
            for (Iterator<Secuencia> it = secsSeccion.iterator(); it.hasNext(); i++) {
                Secuencia secuenciaSeccion = it.next();
                if (secuenciaSeccion.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION))) {
                    Secuencia secCrnSeccion = secuenciaSeccion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION));
                    if (crnSeccion1.equals("")) {
                        crnSeccion1 = secCrnSeccion.getValor();
                    } else {
                        crnSeccion2 = secCrnSeccion.getValor();
                    }
                }
            }
            Collection<Solicitud> solicitudesT2 = new ArrayList<Solicitud>();
            Collection<Solicitud> solicitudes = getSolicitudFacade().findByCursoCupi2AndEstado(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE), getConstanteBean().getConstante(Constantes.VAL_CODIGO_LAB_APOI));
            if (solicitudes.size() == 0) {
                solicitudes = getSolicitudFacade().findByCursoCupi2AndEstado(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE), getConstanteBean().getConstante(Constantes.VAL_CODIGO_APOI));
            }
            for (Solicitud solicitud : solicitudes) {
                String correoAspirante = solicitud.getEstudiante().getPersona().getCorreo();
                //Verificar que no haya repetidos
                boolean yaEsta = false;
                for (Solicitud temp : solicitudesT2) {
                    if (temp.getEstudiante().getEstudiante().getPersona().getCorreo().equals(correoAspirante)) {
                        yaEsta = true;
                    }
                }
                if (yaEsta) {
                    continue;
                }
                //Si la solicitud no estaba aun, continúa con el análisis
                double creditosTotales = solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosSemestreActual().doubleValue();
                Double creditosMon = solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getCreditosMonitoriasISISEsteSemestre();
                if (creditosMon != null) {
                    double creditosMonitorias = creditosMon.doubleValue();
                    if (creditosMonitorias == 4 || creditosMonitorias == 3) {
                        continue;
                    }
                    creditosTotales += creditosMon.doubleValue();
                }

                if (solicitud.getEstudiante().getEstudiante().getInformacion_Academica().getPromedioTotal() < 4) {
                    boolean cumple = reglaBean.validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO), "" + creditosTotales);
                    if (!cumple) {
                        continue;
                    }
                } else {
                    boolean cumple = reglaBean.validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL), "" + creditosTotales);
                    if (!cumple) {
                        continue;
                    }
                }


                if (!getConsultaBean().verificarConflicto(correoAspirante, crnSeccion1)) {
                    if (!crnSeccion2.equals("")) {
                        if (!getConsultaBean().verificarConflicto(correoAspirante, crnSeccion2)) {
                            solicitudesT2.add(solicitud);
                        }
                    } else {
                        solicitudesT2.add(solicitud);
                    }
                }

            }
            //Armar las secuencias
            Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), getConstanteBean().getConstante(Constantes.NULL));
            if (!solicitudesT2.isEmpty()) {
                for (Iterator<Solicitud> iter = solicitudesT2.iterator(); iter.hasNext();) {
                    Solicitud solicitud = iter.next();
                    secSolicitudes.agregarSecuencia(new ConversorSolicitud().darSecuenciaSolicitudCupi2(solicitud));
                }
                secuencias.add(secSolicitudes);
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_CANDIDATOS_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0056, parametros);
                    return retorno;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_CANDIDATOS_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_CANDIDATOS_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0057, parametros);
                    return retorno;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_CANDIDATOS_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                        return retorno;
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (Exception e) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                e.printStackTrace();
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_CANDIDATOS_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0074, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_CANDIDATOS_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno;
    }

    @Override
    public String darSolicitudesPreseleccionadasPorSeccion(
            String xml) {
        String respuesta = null;
        String crn = null;
        Collection<Secuencia> secuencia = new LinkedList<Secuencia>();
        try {
            try {
                getParser().leerXML(xml);
            } catch (Exception e) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return respuesta;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            }
            Secuencia secSeccion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
            Secuencia secCRN = secSeccion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION));
            crn = secCRN.getValor();
            Seccion seccion = getSeccionFacade().findByCRN(crn);
            if (seccion == null) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), crn);
                    Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CRN_SECCION);
                    secParametro.agregarAtributo(atrParametro);
                    parametros.add(secParametro);
                    respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0101, parametros);
                    return respuesta;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                        return respuesta;
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                String[] jerarquia = Constantes.JERARQUIA_ESTADOS;
                Collection<Solicitud> solicitudes = new Vector<Solicitud>();
                //Aquí asumo que el estado de aspirante esta en el indice 0.
                for (int i = 1; i < jerarquia.length; i++) {
                    String estado = getConstanteBean().getConstante(jerarquia[i]);
                    solicitudes.addAll(getSolicitudFacade().findSolicitudesPreseleccionadasPorSeccion(estado, crn));
                }
                ConversorSolicitud conversor = new ConversorSolicitud();
                if (solicitudes.size() != 0) {
                    secuencia.add(secSeccion);
                    Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), getConstanteBean().getConstante(Constantes.NULL));
                    for (Iterator<Solicitud> iter = solicitudes.iterator(); iter.hasNext();) {
                        Solicitud solicitud = iter.next();
                        /*Secuencia secSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
                        Secuencia idSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), solicitud.getId().toString());
                        secSolicitud.agregarSecuencia(idSolicitud);
                        Secuencia fechaApProf = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud());
                        secSolicitud.agregarSecuencia(fechaApProf);
                        Aspirante aspirante = solicitud.getEstudiante();
                        Secuencia secInformacionPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.NULL));
                        secInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), aspirante.getPersona().getNombres()));
                        secInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), aspirante.getPersona().getApellidos()));
                        secSolicitud.agregarSecuencia(secInformacionPersonal);
                        Secuencia secTipo = null;
                        Collection<MonitoriaAceptada> listaMonitorias = solicitud.getMonitorias();
                        if(listaMonitorias.size() == 1)
                        {
                        MonitoriaAceptada mon = listaMonitorias.iterator().next();
                        if(mon.getCarga() == 2)
                        {
                        secTipo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_TIPO_MONITORIA_T1));
                        }
                        else
                        {
                        secTipo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_TIPO_MONITORIA_T2));
                        }
                        }
                        else if(listaMonitorias.size() == 2)
                        {
                        secTipo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_TIPO_MONITORIA_T2));
                        }



                        secSolicitud.agregarSecuencia(secTipo);*/
                        Secuencia secSolicitud = conversor.darSecuenciaSolicitudCompleta(solicitud);
                        secSolicitudes.agregarSecuencia(secSolicitud);
                    }
                    secuencia.add(secSolicitudes);
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), crn);
                        Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CRN_SECCION);
                        secParametro.agregarAtributo(atrParametro);
                        parametros.add(secParametro);
                        respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0046, parametros);
                        return respuesta;
                    } catch (Exception ex) {
                        try {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                            return respuesta;
                        } catch (Exception ex2) {
                            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), crn);
                        Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CRN_SECCION);
                        secParametro.agregarAtributo(atrParametro);
                        parametros.add(secParametro);
                        respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0045, parametros);
                        return respuesta;
                    } catch (Exception ex) {
                        try {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                            return respuesta;
                        } catch (Exception ex2) {
                            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (Exception e) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), crn);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CRN_SECCION);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0066, parametros);
                return respuesta;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_PRESELECCIONADAS_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return respuesta;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return respuesta;
    }

    @Override
    public String darSolicitudesEnAspiracionPorSeccion(
            String xml) {
        String respuesta = null;
        String crn = null;
        Collection<Secuencia> secuencia = new LinkedList<Secuencia>();
        try {
            try {
                getParser().leerXML(xml);
            } catch (Exception e) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return respuesta;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
            }
            Secuencia secCRN = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION));
            crn = secCRN.getValor();
            Seccion seccion = getSeccionFacade().findByCRN(crn);
            if (seccion == null) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), crn);
                    Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CRN_SECCION);
                    secParametro.agregarAtributo(atrParametro);
                    parametros.add(secParametro);
                    respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0101, parametros);
                    return respuesta;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                        return respuesta;
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //Collection<Solicitud> solicitudes = getSolicitudFacade().findSolicitudesEnAspiracionPorSeccion(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE), crn);
                String[] dias;
                String[] horas;
                String horaVacia = "";
                HashMap<String, String> mapaHoras = new HashMap<String, String>();
                for (int i = 0; i < 48; i++) {
                    horaVacia += "_";
                }
                Collection<Sesion> sesiones = seccion.getHorarios();
                for (Sesion sesion : sesiones) {
                    Collection<DiaCompleto> diasCompletos = sesion.getDias();
                    for (DiaCompleto diaCompleto : diasCompletos) {
                        String hora = mapaHoras.get(diaCompleto.getDia_semana());
                        if (hora == null) {
                            hora = horaVacia;
                        }
                        char[] horaArray = hora.toCharArray();
                        char[] horaDia = diaCompleto.getHoras().toCharArray();
                        for (int i = 0; i < horaDia.length; i++) {
                            // Para cada horario ocupado del dia el horario del aspirante debe estar libre
                            if (horaDia[i] != '0') {
                                horaArray[i] = '0';
                            }
                        }
                        mapaHoras.put(diaCompleto.getDia_semana(), new String(horaArray));
                    }
                }
                dias = mapaHoras.keySet().toArray(new String[]{});
                horas = new String[dias.length];
                for (int i = 0; i < dias.length; i++) {
                    horas[i] = mapaHoras.get(dias[i]);
                }
                Curso cu = getCursoFacade().findByCRNSeccion(crn);
                Collection<Solicitud> solicitudes;
                if (cu.isPresencial()) {
                    solicitudes = getSolicitudFacade().findSolicitudesByHorarioYCurso(dias, horas, cu.getCodigo());
                } else {
                    solicitudes = getSolicitudFacade().findByCurso(cu.getCodigo());
                }
                if (solicitudes.size() != 0) {
                    Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), getConstanteBean().getConstante(Constantes.NULL));
                    for (Iterator<Solicitud> iter = solicitudes.iterator(); iter.hasNext();) {
                        Solicitud solicitud = iter.next();
                        Aspirante aspirante = solicitud.getEstudiante();

                        if (!solicitud.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE))) {
                            continue;
                        }
                        double creditosTotales = aspirante.getEstudiante().getInformacion_Academica().getCreditosSemestreActual().doubleValue();
                        if (aspirante.getEstudiante().getInformacion_Academica().getCreditosMonitoriasISISEsteSemestre() != null) {
                            double creditosMonitorias = aspirante.getEstudiante().getInformacion_Academica().getCreditosMonitoriasISISEsteSemestre().doubleValue();
                            if (creditosMonitorias == 4 || creditosMonitorias == 3) {
                                continue;
                            }

                            creditosTotales += creditosMonitorias;
                        }
                        if (!reglaBean.validarRegla(getConstanteBean().getConstante(Constantes.REGLA_PUEDE_EXTRACREDITARSE), "" + aspirante.getEstudiante().getInformacion_Academica().getPromedioTotal())) {
                            boolean cumple = reglaBean.validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO), "" + creditosTotales);
                            if (!cumple) {
                                continue;
                            }
                        } else {
                            boolean cumple = reglaBean.validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL), "" + creditosTotales);
                            if (!cumple) {
                                continue;
                            }
                        }

                        Secuencia secSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD), getConstanteBean().getConstante(Constantes.NULL));
                        Secuencia idSolicitud = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), solicitud.getId().toString());
                        secSolicitud.agregarSecuencia(idSolicitud);
                        Secuencia fechaApProf = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), solicitud.getEstadoSolicitud());
                        secSolicitud.agregarSecuencia(fechaApProf);
                        Secuencia secInformacionPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), getConstanteBean().getConstante(Constantes.NULL));
                        secInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), aspirante.getPersona().getNombres()));
                        secInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), aspirante.getPersona().getApellidos()));
                        secInformacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), aspirante.getPersona().getCorreo()));
                        Secuencia facultad = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FACULTAD), "");
                        facultad.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_NOMBRE_FACULTAD), aspirante.getEstudiante().getPrograma() != null ? aspirante.getEstudiante().getPrograma().getFacultad().getNombre() : ""));
                        facultad.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_ACADEMICO), aspirante.getEstudiante().getPrograma() != null ? aspirante.getEstudiante().getPrograma().getNombre() : ""));
                        secInformacionPersonal.agregarSecuencia(facultad);
                        secSolicitud.agregarSecuencia(secInformacionPersonal);
                        Secuencia cursoVisto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO), "");
                        double notaD = solicitud.getMonitoria_solicitada().getNota_materia();
                        String nota = "";
                        if (notaD == -1) {
                            nota = getConstanteBean().getConstante(Constantes.NO_APLICA);
                        } else {
                            nota = Double.toString(notaD);
                        }
                        cursoVisto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_MATERIA), nota));
                        Secuencia secPeriodoMateria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), solicitud.getMonitoria_solicitada().getPeriodoAcademicoEnQueSeVio());
                        Secuencia secProfesorConQuienVioMateria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CON_QUIEN_LA_VIO), solicitud.getMonitoria_solicitada().getProfesorConQuienLaVio());
                        cursoVisto.agregarSecuencia(secPeriodoMateria);
                        cursoVisto.agregarSecuencia(secProfesorConQuienVioMateria);

                        secSolicitud.agregarSecuencia(cursoVisto);
                        Secuencia info = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA), "");
                        info.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL), Double.toString(aspirante.getEstudiante().getInformacion_Academica().getCreditosSemestreActual())));
                        secSolicitud.agregarSecuencia(info);
                        Secuencia monitoriaSolicitada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA), "");
                        Secuencia curso = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO), "");
                        Curso c = solicitud.getMonitoria_solicitada().getCurso();
                        curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_CURSO), c.getCodigo()));
                        curso.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CURSO), c.getNombre()));
                        Collection<Seccion> seccionesCurso = c.getSecciones();
                        Secuencia secciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), "");
                        for (Seccion s : seccionesCurso) {
                            if (s.getCrn().equals(crn)) {
                                Secuencia secseccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), "");
                                secseccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), Integer.toString(s.getNumeroSeccion())));
                                secseccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), s.getCrn()));
                                secseccion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MAX_CANTIDAD_MONITORES), Double.toString(s.getMaximoMonitores())));
                                secciones.agregarSecuencia(secseccion);
                            }
                        }
                        curso.agregarSecuencia(secciones);
                        monitoriaSolicitada.agregarSecuencia(curso);
                        secSolicitud.agregarSecuencia(monitoriaSolicitada);
                        secSolicitudes.agregarSecuencia(secSolicitud);
                    }
                    secuencia.add(secSolicitudes);
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), crn);
                        Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CRN_SECCION);
                        secParametro.agregarAtributo(atrParametro);
                        parametros.add(secParametro);
                        respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0054, parametros);
                        return respuesta;
                    } catch (Exception ex) {
                        try {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                            return respuesta;
                        } catch (Exception ex2) {
                            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), crn);
                        Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CRN_SECCION);
                        secParametro.agregarAtributo(atrParametro);
                        parametros.add(secParametro);
                        respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0053, parametros);
                        return respuesta;
                    } catch (Exception ex) {
                        try {
                            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                            respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                            return respuesta;
                        } catch (Exception ex2) {
                            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                        }
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (Exception e) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), crn);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CRN_SECCION);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                e.printStackTrace();
                respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0069, parametros);
                return respuesta;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    respuesta = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_EN_ASPIRACION_POR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return respuesta;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return respuesta;
    }

    @Override
    public String darSolicitudPorId(String xml) {
        String retorno = null;
        Collection<Secuencia> secuencia = new ArrayList<Secuencia>();
        try {
            getParser().leerXML(xml);
        } catch (Exception ex) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex2) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
        Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(id));
        if (solicitud == null) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), id);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    ex.printStackTrace();
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                ex.printStackTrace();
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

            try {

                secuencia.add(new ConversorSolicitud().darSecuenciaSolicitudCompleta(solicitud));
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametroId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), id);
                Atributo atrParametroId = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD);
                secParametroId.agregarAtributo(atrParametroId);
                parametros.add(secParametroId);
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0030, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUD_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    ex.printStackTrace();
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                ex.printStackTrace();
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno;
    }

    @Deprecated
    @Override
    public String darSolicitudesEstudiantePorCodigo(String xml) {
        log.log(Level.INFO, SolicitudBean.class + "-" + "darSolicitudesEstudiantePorCodigo");
        String retorno = null;
        Collection<Secuencia> secuencia = new ArrayList<Secuencia>();
        try {
            getParser().leerXML(xml);
        } catch (Exception ex) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex2) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        String codigo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE)).getValor();
        ArrayList<Secuencia> secuenciasSolicitudes = new ArrayList<Secuencia>();
        Aspirante aspirante = getAspiranteFacade().findByCodigo(codigo);
        if (aspirante == null) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), codigo);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CODIGO_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0071, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        List<Solicitud> solicitudes = getSolicitudFacade().findByCodigoEstudiante(codigo);
        if (solicitudes.size() == 0) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getNombres());
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRES_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getApellidos());
                atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_APELLIDOS_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0094, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
            }
        }
        Iterator iterator = solicitudes.iterator();
        try {

            ConversorSolicitud conversor = new ConversorSolicitud();
            while (iterator.hasNext()) {
                Solicitud solicitud = (Solicitud) iterator.next();
                Secuencia secuenciaSolicitud = conversor.darSecuenciaSolicitudSimple(solicitud);
                secuenciasSolicitudes.add(secuenciaSolicitud);
            }
            Secuencia secuenciaSolicitudes = new Secuencia(new ArrayList<Atributo>(), secuenciasSolicitudes);
            secuenciaSolicitudes.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            secuencia.add(secuenciaSolicitudes);

            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
            Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getNombres());
            Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRES_ESTUDIANTE);
            secParametro.agregarAtributo(atrParametro);
            parametros.add(secParametro);
            secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getApellidos());
            atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_APELLIDOS_ESTUDIANTE);
            secParametro.agregarAtributo(atrParametro);
            parametros.add(secParametro);
            retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0029, parametros);
            return retorno;
        } catch (Exception ex) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex2) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    @Override
    public String tieneSolicitudesPorLogin(String xml) {
        try {
            getParser().leerXML(xml);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            String login = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LOGIN)).getValor();
            String correo = login + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
            List<Solicitud> solicitudes = getSolicitudFacade().tieneSolicitudesPorLogin(correo);
            Secuencia secTieneSolicitudes = null;
            if (solicitudes.size() != 0) {
                secTieneSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIENE_SOLICITUDES), "true");
            } else {
                secTieneSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIENE_SOLICITUDES), "false");
            }
            secuencias.add(secTieneSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_TIENE_SOLICITUDES_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0193, new LinkedList<Secuencia>());
        } catch (Exception ex) {
            try {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_TIENE_SOLICITUDES_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0132, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private int darSolicitudesEstudiantePorLogin(String correo, String codigoCurso) {
        List<Solicitud> solicitudes = getSolicitudFacade().findByLogin(correo);
        if (solicitudes.size() == 0) {
            return 0;
        }
        Iterator iterator = solicitudes.iterator();
        int contadorCurso = 0;
        while (iterator.hasNext()) {
            Solicitud solicitud = (Solicitud) iterator.next();
            String codigoCursoSolicitud = solicitud.getMonitoria_solicitada().getCurso().getCodigo();
            if (codigoCursoSolicitud.equals(codigoCurso)) {
                contadorCurso++;
            }
        }
        return contadorCurso;
    }

    @Override
    public String darSolicitudesEstudiantePorLogin(String xml) {
        String retorno = null;
        Collection<Secuencia> secuencia = new ArrayList<Secuencia>();
        try {
            getParser().leerXML(xml);
        } catch (Exception ex) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex2) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
        String login = correo.split("@")[0];
        ArrayList<Secuencia> secuenciasSolicitudes = new ArrayList<Secuencia>();
        Aspirante aspirante = getAspiranteFacade().findByCorreo(correo);
        if (aspirante == null) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), login);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                secuencia.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), getConstanteBean().getConstante(Constantes.NULL)));
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0029, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        List<Solicitud> solicitudes = getSolicitudFacade().findByLogin(correo);
        if (solicitudes.isEmpty()) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getNombres());
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRES_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getApellidos());
                atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_APELLIDOS_ESTUDIANTE);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), getConstanteBean().getConstante(Constantes.NULL));
                secuencia.add(secSolicitudes);
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0094, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Iterator iterator = solicitudes.iterator();
        try {

            ConversorSolicitud conversor = new ConversorSolicitud();
            while (iterator.hasNext()) {
                Solicitud solicitud = (Solicitud) iterator.next();
                Secuencia secuenciaSolicitud = conversor.darSecuenciaSolicitudSimple(solicitud);
                secuenciasSolicitudes.add(secuenciaSolicitud);
            }
            Secuencia secuenciaSolicitudes = new Secuencia(new ArrayList<Atributo>(), secuenciasSolicitudes);
            secuenciaSolicitudes.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            secuencia.add(secuenciaSolicitudes);

            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
            Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getNombres());
            Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRES_ESTUDIANTE);
            secParametro.agregarAtributo(atrParametro);
            parametros.add(secParametro);
            secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getApellidos());
            atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_APELLIDOS_ESTUDIANTE);
            secParametro.agregarAtributo(atrParametro);
            parametros.add(secParametro);



            retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0029, parametros);
            return retorno;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex2) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    @Override
    public String cancelarSolicitudPorCarga(String xml) {
        ConversorGeneral conversorG = new ConversorGeneral(xml);
        String retorno = null;
        String idSolicitud = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        try {
            getParser().leerXML(xml);
        } catch (Exception e) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex3) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex3);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            Solicitud solicitud = getSolicitudFacade().find(new Long(idSolicitud));
            if (solicitud == null) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), idSolicitud);
                    Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD);
                    secParametro.agregarAtributo(atrParametro);
                    parametros.add(secParametro);
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, parametros);
                    return retorno;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                        return retorno;
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                terminarTareaRegistrarConvenioEstudiante(idSolicitud);
                getPreseleccionBean().revertirPreseleccion(idSolicitud, conversorG.obtenerResponsable(), null, Notificaciones.MENSAJE_REVERSION_SOLICITUD_ASPIRANTE);
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), idSolicitud);
                    Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD);
                    secParametro.agregarAtributo(atrParametro);
                    parametros.add(secParametro);
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0055, parametros);
                    return retorno;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), idSolicitud);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0070, parametros);
                return retorno;
            } catch (Exception ex2) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex3) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex3);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
        return retorno;
    }

    private void terminarTareaRegistrarConvenioEstudiante(String idSolicitud) {
        String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REGISTRAR_CONVENIO_ESTUDIANTE);
        rangoFechasBean.realizarTareaSolicitud(tipo, Long.parseLong(idSolicitud));

    }

    @Override
    public String crearsolicitud(String xml) {
        String retorno = null;
        Aspirante aspirante = null;
        Monitoria_Solicitada monitoriaSolicitada = null;
        ConversorMonitorias conversor = new ConversorMonitorias();
        // Obtener permisos del conversor
        boolean permisos = conversor.tienePermisosEspeciales(xml);

        if (!permisos && !convocatoriaBean.hayConvocatoriaAbierta()) {
            try {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0142, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {

            // Obtener solicitud del conversor
            Solicitud solicitud = conversor.pasarComandoASolicitud(xml);

            aspirante = conversor.pasarComandoAAspirante(xml);
            solicitud.setEstudiante(aspirante);

            String correo = aspirante.getEstudiante().getPersona().getCorreo();

            //Si está en lista negra no se puede registrar la solicitud
            boolean estaListaNegra = listaNegraBean.estaEnListaNegraPorCorreo(correo);
            if (!permisos && estaListaNegra) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0029, new LinkedList<Secuencia>());
            }

            monitoriaSolicitada = conversor.pasarComandoAMonitoriaSolicitada(xml);
            solicitud.setMonitoria_solicitada(monitoriaSolicitada);

            Curso cursoSolicitud = monitoriaSolicitada.getCurso();
            int cantidadSolicitudes = darSolicitudesEstudiantePorLogin(correo, cursoSolicitud.getCodigo());

            // TODO Agregar constante que modele el numero de solicitudes para un determinado curso
            if (!permisos && cantidadSolicitudes >= 2) {
                retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0140, new Vector());
                return retorno;
            }

            Aspirante aspiranteAntiguo = aspiranteFacade.findByCorreo(correo);
            if (aspiranteAntiguo == null) {
                aspiranteAntiguo = new Aspirante();
                aspiranteAntiguo.setEstudiante(aspirante.getEstudiante());
                getAspiranteFacade().create(aspiranteAntiguo);
            } else {
                Collection<MonitoriaOtroDepartamento> monitoriasOtroDpto = aspiranteAntiguo.getMonitoriasOtrosDepartamentos();
                aspiranteAntiguo.setMonitoriasOtrosDepartamentos(new LinkedList<MonitoriaOtroDepartamento>());
                Horario_Disponible horarioDisponible = aspiranteAntiguo.getHorario_disponible();
                aspiranteAntiguo.setHorario_disponible(null);
                Collection<MonitoriaRealizada> monitoriaRealizada = aspiranteAntiguo.getMonitoriasRealizadas();
                aspiranteAntiguo.setMonitoriasRealizadas(new LinkedList<MonitoriaRealizada>());
                Estudiante estudianteAntiguo = aspiranteAntiguo.getEstudiante();
                InformacionAcademica infoAcademica = estudianteAntiguo.getInformacion_Academica();
                estudianteAntiguo.setInformacion_Academica(null);
                getEstudianteFacade().edit(estudianteAntiguo);
                getAspiranteFacade().edit(aspiranteAntiguo);
                for (MonitoriaOtroDepartamento monitoriaOtroDpto : monitoriasOtroDpto) {
                    getMonitoriaOtrosDepartamentosFacade().remove(monitoriaOtroDpto);
                }
                if (horarioDisponible != null) {
                    getHorario_DisponibleFacade().remove(horarioDisponible);
                }
                for (MonitoriaRealizada monitoriaRealizada1 : monitoriaRealizada) {
                    getMonitoriaRealizadaFacade().remove(monitoriaRealizada1);
                }
                if (infoAcademica != null) {
                    getInformacion_AcademicaFacade().remove(infoAcademica);
                }
            }

            getMonitoriaSolicitadaFacade().create(monitoriaSolicitada);

            Collection<MonitoriaOtroDepartamento> monitoriasOtrosDptos = aspirante.getMonitoriasOtrosDepartamentos();
            for (MonitoriaOtroDepartamento monitoriaOtroDepartamento : monitoriasOtrosDptos) {
                getMonitoriaOtrosDepartamentosFacade().create(monitoriaOtroDepartamento);
            }
            Horario_Disponible horario = aspirante.getHorario_disponible();
            // Verificar que tenga horario para cada uno de los dias
            Collection<DiaCompleto> dias = horario.getDias_disponibles();
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_LUNES), dias);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_MARTES), dias);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_MIERCOLES), dias);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_JUEVES), dias);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_VIERNES), dias);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_SABADO), dias);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_DOMINGO), dias);

            Collection<MonitoriaRealizada> monitoriasRealizadas = aspirante.getMonitoriasRealizadas();
            InformacionAcademica infoAcademica = aspirante.getEstudiante().getInformacion_Academica();
            getInformacion_AcademicaFacade().create(infoAcademica);
            Estudiante estudianteAntiguo = aspirante.getEstudiante();
            estudianteAntiguo.setInformacion_Academica(infoAcademica);
            getEstudianteFacade().edit(estudianteAntiguo);

            aspiranteAntiguo.setHorario_disponible(horario);
            aspiranteAntiguo.setMonitoriasOtrosDepartamentos(monitoriasOtrosDptos);
            aspiranteAntiguo.setMonitoriasRealizadas(monitoriasRealizadas);
            aspiranteAntiguo.setEstudiante(estudianteAntiguo);

            getHorario_DisponibleFacade().create(horario);
            getAspiranteFacade().edit(aspiranteAntiguo);

            if (!permisos && cursoSolicitud.isPresencial()) {
                // Si el curso es presencial se deben revisar que el horario coincida con al menos una seccion
                Collection<Seccion> seccionesCurso = cursoSolicitud.getSecciones();
                boolean horarioCompatible = false;
                for (Seccion seccion : seccionesCurso) {
                    if (!getConsultaBean().verificarConflicto(correo, seccion.getCrn())) {
                        horarioCompatible = true;
                        break;
                    }
                }
                if (!horarioCompatible) {
                    retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0092, new LinkedList<Secuencia>());
                    return retorno;
                }
            }
            if (!permisos) {
                ValidacionInformacion validacion = validarInformacion(solicitud.getEstudiante(), solicitud.getMonitoria_solicitada());
                if (validacion != ValidacionInformacion.VALIDACION_EXITOSA) {
                    switch (validacion) {
                        case NIVEL_MATERIA:
                            retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0145, new Vector());
                            break;
                        case NOTA_MATERIA:
                            retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0147, new Vector());
                            break;
                        case NUMERO_CREDITOS:
                            Regla reglaNumCreditos = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL));
                            String creditosSemestreActual = reglaNumCreditos.getValor();
                            reglaNumCreditos = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_PUEDE_EXTRACREDITARSE));
                            String promedioExtracreditos = reglaNumCreditos.getValor();
                            reglaNumCreditos = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO));
                            String creditosSemestrePromedioInferior = reglaNumCreditos.getValor();
                            Collection<Secuencia> parametrosNumCreditos = conversor.generarParametros(creditosSemestreActual, promedioExtracreditos, creditosSemestrePromedioInferior);

                            retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0144, parametrosNumCreditos);
                            break;
                        case NUMERO_MONITORIAS:
                            retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0146, new Vector());
                            break;
                        case PROMEDIO_TOTAL:
                            Regla regla = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_PROMEDIO_TOTAL));
                            Collection<Secuencia> parametros = conversor.generarParametros(regla.getValor());
                            retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0143, parametros);
                            break;
                        default:
                            retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0126, new Vector());
                            break;
                    }

                    return retorno;
                }
            }

            String estado = getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE);
            solicitud.setEstudiante(aspiranteAntiguo);
            solicitud.setEstadoSolicitud(estado);
            solicitud.setMonitoria_solicitada(monitoriaSolicitada);
            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
            solicitud.setFechaCreacion(fechaActual);
            solicitud.setMonitorias(new LinkedList<MonitoriaAceptada>());

            getSolicitudFacade().create(solicitud);
            try {
                Collection<Secuencia> parametros = conversor.crearParametrosMensaje(aspirante.getPersona().getNombres(), aspirante.getPersona().getApellidos(), monitoriaSolicitada.getCurso().getNombre());

                retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0028, parametros);
                return retorno;
            } catch (Exception ex) {
                try {
                    Collection<Secuencia> parametros = new ArrayList<Secuencia>();
                    retorno = getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex2) {
                    ex.printStackTrace();
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
            try {

                Collection<Secuencia> parametros = conversor.crearParametrosMensaje(aspirante.getPersona().getNombres(), aspirante.getPersona().getApellidos(), monitoriaSolicitada.getCurso().getNombre());
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0047, parametros);
                return retorno;
            } catch (Exception ex2) {
                ex2.printStackTrace();
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    ex2.printStackTrace();
                    return retorno;
                } catch (Exception ex3) {
                    ex3.printStackTrace();
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex3);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    @Override
    public String eliminarSolicitud(String xml) {
        ConversorGeneral conversorG = new ConversorGeneral(xml);
        String retorno = null;
        String idSolicitud = null;
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        try {
            getParser().leerXML(xml);
        } catch (Exception e) {
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                return retorno;
            } catch (Exception ex3) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex3);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            Solicitud solicitud = getSolicitudFacade().find(new Long(idSolicitud));
            if (solicitud == null) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), idSolicitud);
                    Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD);
                    secParametro.agregarAtributo(atrParametro);
                    parametros.add(secParametro);
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, parametros);
                    return retorno;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                        return retorno;
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                eliminarSolicitud(solicitud.getId(), conversorG.obtenerResponsable(), Notificaciones.MENSAJE_REVERSION_SOLICITUD_ASPIRANTE);
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), idSolicitud);
                    Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD);
                    secParametro.agregarAtributo(atrParametro);
                    parametros.add(secParametro);
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0031, parametros);
                    return retorno;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), idSolicitud);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0049, parametros);
                return retorno;
            } catch (Exception ex2) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    retorno = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    return retorno;
                } catch (Exception ex3) {
                    Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex3);
                }
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
        return retorno;
    }

    @Override
    public void eliminarSolicitud(Long idSolicitud, String rolResponsable, String mensaje) {
        try {
            Solicitud solicitud = getSolicitudFacade().find(new Long(idSolicitud));

            //Revertir preselecciones
            getPreseleccionBean().revertirPreseleccion(solicitud.getId().toString(), rolResponsable, null, mensaje);

            solicitud = getSolicitudFacade().find(new Long(idSolicitud));
            getSolicitudFacade().remove(solicitud);
        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String darSolicitudesPorEstado(String xml) {
        String retorno = null;
        try {
            try {
                getParser().leerXML(xml);
            } catch (Exception ex) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            String estado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)).getValor();

            ArrayList<Secuencia> secuenciasSolicitudes = new ArrayList<Secuencia>();
            List<Solicitud> solicitudes = getSolicitudFacade().findByEstado(estado);
            if (solicitudes.size() == 0) {
                Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), estado);
                Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ESTADO_SOLICITUD);
                secParametro.agregarAtributo(atrParametro);
                parametros.add(secParametro);
                retorno = getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0099, new ArrayList<Secuencia>());
                return retorno;
            }
            Iterator iterator = solicitudes.iterator();
            ConversorSolicitud conversor = new ConversorSolicitud();
            while (iterator.hasNext()) {
                Solicitud solicitud = (Solicitud) iterator.next();
                Secuencia secuenciaSolicitud = conversor.darSecuenciaSolicitudCompleta(solicitud);
                secuenciasSolicitudes.add(secuenciaSolicitud);
            }
            Secuencia secuenciaSolicitudes = new Secuencia(new ArrayList<Atributo>(), secuenciasSolicitudes);
            secuenciaSolicitudes.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
            Collection<Secuencia> secuencia = new ArrayList<Secuencia>();
            secuencia.add(secuenciaSolicitudes);
            try {
                retorno = getParser().generarRespuesta(secuencia, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0036, new ArrayList<Secuencia>());
                return retorno;
            } catch (Exception ex) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception nre) {
            try {
                nre.printStackTrace();
                retorno = getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_POR_ESTADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0056, new ArrayList<Secuencia>());
                return retorno;
            } catch (Exception ex) {
                Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, nre);
        }
        return retorno;
    }

    /**
     * Valida la información de acuerdo a las reglas de negocio
     * @param aspirante Aspirante
     * @param monitoria_Solicitada Monitoria solicitada
     * @return true|false resultado de la validación
     */
    private ValidacionInformacion validarInformacion(Aspirante aspirante, Monitoria_Solicitada monitoria_Solicitada) {

        // Se valida el numero de monitorias que tiene el aspirante
        int numSolicitudes = 0;
        Collection<Solicitud> solicitudes = solicitudFacade.findByLogin(aspirante.getPersona().getCorreo());
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.getMonitorias().size() > 0) {
                numSolicitudes++;
            }
        }
        if (!getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_NUMERO_MAXIMO_MONITORIAS), Integer.toString(numSolicitudes))) {
            return ValidacionInformacion.NUMERO_MONITORIAS;
        }

        // Se valida que la nota de la materia cumpla con la regla en caso de haber visto la materia
        double nota = monitoria_Solicitada.getNota_materia();
        if (nota != -1.0 && !getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_NOTA_MATERIA), Double.toString(nota))) {
            return ValidacionInformacion.NOTA_MATERIA;
        }

        // Se valida que el nivel de la materia y del estudiante sean el mismo
        NivelFormacion nivelCurso = monitoria_Solicitada.getCurso().getNivelPrograma();
        NivelFormacion nivelAsp = aspirante.getEstudiante().getTipoEstudiante();

        //Si la monitoria a la que aplica es de especializacion, el estudiante puede tener cualquier nivel, de lo
        //contratio el estudiante debe tener el mismo nivel de la monitoria a la que aplica
        if ((!nivelCurso.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NIVEL_ESPECIALIZACION)))&&(!nivelCurso.getNombre().equals(nivelAsp.getNombre()))) {
            return ValidacionInformacion.NIVEL_MATERIA;
        }



        double promedio = aspirante.getEstudiante().getInformacion_Academica().getPromedioTotal();
        double creditos = aspirante.getEstudiante().getInformacion_Academica().getCreditosSemestreActual() + aspirante.getEstudiante().getInformacion_Academica().getCreditosMonitoriasISISEsteSemestre();
        if (getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_PUEDE_EXTRACREDITARSE), Double.toString(promedio))) {
            if (!getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL), Double.toString(creditos))) {
                return ValidacionInformacion.NUMERO_CREDITOS;
            }
        } else {
            if (!getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO), Double.toString(creditos))) {
                return ValidacionInformacion.NUMERO_CREDITOS;
            }
        }

        if (!getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_PROMEDIO_TOTAL), Double.toString(promedio))) {
            return ValidacionInformacion.PROMEDIO_TOTAL;
        }

        return ValidacionInformacion.VALIDACION_EXITOSA;
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
     * Retorna SolicitudFacade
     * @return solicitudFacade SolicitudFacade
     */
    private SolicitudFacadeLocal getSolicitudFacade() {
        return solicitudFacade;
    }

    /**
     * Retorna AspiranteFacade
     * @return aspiranteFacade AspiranteFacade
     */
    private AspiranteFacadeLocal getAspiranteFacade() {
        return aspiranteFacade;
    }

    /**
     * Retorna Horario_DisponibleFacade
     * @return horario_DisponibleFacade Horario_DisponibleFacade
     */
    private Horario_DisponibleFacadeLocal getHorario_DisponibleFacade() {
        return horario_DisponibleFacade;
    }

    /**
     * Retorna Informacion_AcademicaFacade
     * @return informacion_AcademicaFacade Informacion_AcademicaFacade
     */
    private InformacionAcademicaFacadeRemote getInformacion_AcademicaFacade() {
        return informacion_AcademicaFacade;
    }

    /**
     * Retorna CursoFacade
     * @return cursoFacade CursoFacade
     */
    private CursoFacadeRemote getCursoFacade() {
        return cursoFacade;
    }

    /**
     * Retorna Regla
     * @return regla Regla
     */
    private ReglaRemote getReglaBean() {
        return reglaBean;
    }

    /**
     * Retorna MonitoriaOtroDepartamentoFacade
     * @return monitoriaOtrosDepartamentosFacade MonitoriaOtroDepartamentoFacade
     */
    private MonitoriaOtroDepartamentoFacadeLocal getMonitoriaOtrosDepartamentosFacade() {
        return monitoriaOtrosDepartamentosFacade;
    }

    /**
     * Retorna SeccionFacade
     * @return seccionFacade SeccionFacade
     */
    private SeccionFacadeRemote getSeccionFacade() {
        return seccionFacade;
    }

    /**
     * Retorna ConsultaBean
     * @return consultaBean ConsultaBean
     */
    private ConsultaLocal getConsultaBean() {
        return consultaBean;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private PreseleccionLocal getPreseleccionBean() {
        return preseleccionBean;
    }

    /**
     * Devuelve las solicitudes de personas que no han sido seleccionadas para 
     * ninguna monitoría, y que se hayan postulado a APOI, APOII y Datos.
     * @param comando
     * @return
     */
    @Override
    @Deprecated
    public String darSolicitudesSinMonitoria(String comando) {

        Collection<Solicitud> solicitudes = getSolicitudFacade().findByEstado(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE));
        Collection<Solicitud> solicitudesAprobadas = new ArrayList<Solicitud>();
        Collection<Solicitud> solicitudesAQuitar = new ArrayList<Solicitud>();
        boolean encontro = false;
        String estado = getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE);
        //Lleno una colección con las solicitudes que han sido preseleccionadas/aceptadas o
        //están más adelante en el proceso.
        for (int i = 0; i < Constantes.JERARQUIA_ESTADOS.length && !encontro; i++) {
            String estado1 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[i]);
            if (estado1.equals(estado)) {
                encontro = true;
                while (i < Constantes.JERARQUIA_ESTADOS.length) {
                    String estado2 = getConstanteBean().getConstante(Constantes.JERARQUIA_ESTADOS[i]);
                    solicitudesAprobadas.addAll(getSolicitudFacade().findByEstado(estado2));
                    i++;
                }
            }
        }
        //Quito todas las solicitudes de personas que ya les ha sido aceptada una monitoria
        for (Solicitud s : solicitudes) {
            for (Solicitud t : solicitudesAprobadas) {
                if (s.getEstudiante().getPersona().getCorreo().equals(t.getEstudiante().getPersona().getCorreo())) {
                    solicitudesAQuitar.add(s);
                }
            }
        }
        solicitudes.removeAll(solicitudesAQuitar);

        //Ahora quito las que no son de APOI,APOII o Datos.
        solicitudesAQuitar = new ArrayList<Solicitud>();
        for (Solicitud s : solicitudes) {
            String codigo = s.getMonitoria_solicitada().getCurso().getCodigo();
            boolean pertenece = codigo.equals(getConstanteBean().getConstante(Constantes.VAL_CODIGO_APOI));
            pertenece = pertenece || codigo.equals(getConstanteBean().getConstante(Constantes.VAL_CODIGO_APOII));
            pertenece = pertenece || codigo.equals(getConstanteBean().getConstante(Constantes.VAL_CODIGO_ESTRUCTURAS));
            if (!pertenece) {
                solicitudesAQuitar.add(s);
            }
        }
        solicitudes.removeAll(solicitudesAQuitar);

        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), getConstanteBean().getConstante(Constantes.NULL));
        String respuesta = "";
        try {

            ConversorSolicitud conversor = new ConversorSolicitud();
            if (solicitudes.size() != 0) {
                for (Iterator<Solicitud> iter = solicitudes.iterator(); iter.hasNext();) {
                    Solicitud solicitud = iter.next();
                    secSolicitudes.agregarSecuencia(conversor.darSecuenciaSolicitudSimple(solicitud));
                }
                secuencias.add(secSolicitudes);
            }
            respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_SIN_MONITORIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return respuesta;
    }

    @Override
    public String darSeccionesAPOIPorCorreo(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Aspirante asp = getAspiranteFacade().findByCorreo(correo);
            if (asp != null) {
                Curso apoI = getCursoFacade().findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_APOI));
                Collection<Seccion> secciones = apoI.getSecciones();
                Collection<Seccion> seccionesSirven = new ArrayList<Seccion>();
                for (Seccion s : secciones) {
                    if (!getConsultaBean().verificarConflicto(asp.getPersona().getCorreo(), s.getCrn())) {
                        seccionesSirven.add(s);
                    }
                }

                ArrayList<Secuencia> secuenciasSecciones = new ArrayList<Secuencia>();
                Iterator<Seccion> iterador = seccionesSirven.iterator();
                while (iterador.hasNext()) {
                    Seccion seccion = iterador.next();
                    ArrayList<Secuencia> secuenciasSeccion = new ArrayList<Secuencia>();
                    ArrayList<Secuencia> secuenciasProfesor = new ArrayList<Secuencia>();
                    secuenciasProfesor.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), seccion.getProfesorPrincipal().getPersona().getNombres() + " " + seccion.getProfesorPrincipal().getPersona().getApellidos()));
                    Secuencia secuenciaProfesor = new Secuencia(new ArrayList<Atributo>(), secuenciasProfesor);
                    secuenciaProfesor.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
                    secuenciasSeccion.add(secuenciaProfesor);
                    secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + seccion.getNumeroSeccion()));
                    secuenciasSeccion.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn()));
                    Secuencia secuenciaSeccion = new Secuencia(new ArrayList<Atributo>(), secuenciasSeccion);
                    secuenciaSeccion.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION));
                    secuenciasSecciones.add(secuenciaSeccion);
                }
                Secuencia secuenciaSecciones = new Secuencia(new ArrayList<Atributo>(), secuenciasSecciones);
                secuenciaSecciones.setNombre(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES));
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                secuencias.add(secuenciaSecciones);

                String respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_APOI_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
                return respuesta;

            } else {
                return "";
            }

        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String actualizarSolicitud(String xml) {
        try {
            String respuesta = "";
            getParser().leerXML(xml);
            Secuencia secSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD));
            String idSolicitud = secSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            Solicitud solicitud = solicitudFacade.findById(Long.parseLong(idSolicitud));
            Solicitud solicitudNueva = new Solicitud();
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            if (solicitud != null) {
                Secuencia secCursoVisto = secSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO));
                String notaMateria = secCursoVisto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_MATERIA)).getValor();
                String profesorConQuienVio = secCursoVisto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR_CON_QUIEN_LA_VIO)).getValor();
                String periodoEnQueLaVio = secCursoVisto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();

                solicitudNueva.setEstudiante(solicitud.getEstudiante());
                Monitoria_Solicitada monSolNueva = new Monitoria_Solicitada();
                monSolNueva.setNota_materia(Double.parseDouble(notaMateria));
                solicitudNueva.setMonitoria_solicitada(monSolNueva);

                if (secCursoVisto != null) {
                    if (verificarReglas(solicitudNueva)) {
                        solicitud.getMonitoria_solicitada().setProfesorConQuienLaVio(profesorConQuienVio);
                        solicitud.getMonitoria_solicitada().setPeriodoAcademicoEnQueSeVio(periodoEnQueLaVio);
                        solicitud.getMonitoria_solicitada().setNota_materia(Double.parseDouble(notaMateria));
                        getSolicitudFacade().edit(solicitud);
                        respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0096, new ArrayList());
                    } else {
                        respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0134, new ArrayList());
                    }

                }

                return respuesta;

            } else {
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SECCIONES_APOI_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0141, new ArrayList());
                return respuesta;
            }

        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String consultarMonitoresT2(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), getConstanteBean().getConstante(Constantes.NULL));
            Collection<Solicitud> listaSolicitudes = solicitudFacade.findByNotEstado(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE));
            Iterator<Solicitud> iterator = listaSolicitudes.iterator();
            ConversorSolicitud conversor = new ConversorSolicitud();

            while (iterator.hasNext()) {
                Solicitud solicitud = iterator.next();
                Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();

                Iterator<MonitoriaAceptada> iteratorMon = monitorias.iterator();
                if (monitorias.size() == 1) {
                    MonitoriaAceptada mon = iteratorMon.next();
                    if (mon.getCarga() == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T2))) {
                        Monitoria_Solicitada monSolicitada = solicitud.getMonitoria_solicitada();
                        String cursosACargoCupi2 = getConstanteBean().getConstante(Constantes.CTE_MONITORIAS_CURSOS_MONITOR_A_CARGO_CUPI2);

                        if (cursosACargoCupi2.contains(monSolicitada.getCurso().toString())) {
                            Secuencia secSolicitud = conversor.darSecuenciaSolicitudMonitorT2(solicitud, mon.getSeccion(), null);
                            secSolicitudes.agregarSecuencia(secSolicitud);
                        }
                    }
                } else {
                    MonitoriaAceptada mon1 = iteratorMon.next();
                    MonitoriaAceptada mon2 = iteratorMon.next();
                    Seccion sec1 = mon1.getSeccion();
                    Seccion sec2 = mon2.getSeccion();
                    Secuencia secSolicitud = conversor.darSecuenciaSolicitudMonitorT2(solicitud, sec1, sec2);
                    secSolicitudes.agregarSecuencia(secSolicitud);
                }
            }

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_MONITORES_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String consultarSeccionesSinMonitorT2(String comando) {
        try {
            getParser().leerXML(comando);
            Collection<Seccion> listaSeccionesConMonitor = new LinkedList<Seccion>();
            //**********Primero se recuperan las secciones con monitor t2
            Collection<Solicitud> listaSolicitudes = solicitudFacade.findByNotEstado(getConstanteBean().getConstante(Constantes.ESTADO_ASPIRANTE));
            Iterator<Solicitud> iterator = listaSolicitudes.iterator();
            while (iterator.hasNext()) {
                Solicitud solicitud = iterator.next();
                Collection<MonitoriaAceptada> monitorias = solicitud.getMonitorias();
                Iterator<MonitoriaAceptada> iteratorMon = monitorias.iterator();
                if (monitorias.size() == 1) {
                    MonitoriaAceptada mon = iteratorMon.next();
                    if (mon.getCarga() == Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_CARGA_MONITORIA_T2))) {
                        listaSeccionesConMonitor.add(mon.getSeccion());
                    }
                } else {
                    MonitoriaAceptada mon1 = iteratorMon.next();
                    MonitoriaAceptada mon2 = iteratorMon.next();
                    Seccion sec1 = mon1.getSeccion();
                    Seccion sec2 = mon2.getSeccion();
                    listaSeccionesConMonitor.add(sec1);
                    listaSeccionesConMonitor.add(sec2);
                }
            }

            //*************Segundo se recuperan todas las secciones del curso de apo1
            Curso apo1 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_APOI));
            if (apo1 == null) {
                // Si el curso de APO1 no fue incluido en la cartelera, entonces el laboratorio debe ser usado para los cursos de apo1
                apo1 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_LAB_APOI));
            }else{
                // Si el curso de APO1 fue incluido en la cartelera pero no se le asignaron monitores, entonces el laboratorio debe ser usado para los cursos de apo1
                Seccion s = apo1.getSecciones().iterator().next();
                if(s.getMaximoMonitores()==0)
                    apo1 = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_LAB_APOI));
            }

            Collection<Seccion> listaSeccionesAPO1 = apo1.getSecciones();
            
            //se recupera el curso de apo con honores
//            Curso apoConHonores = cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_APO_CON_HONORES));
//            if (apoConHonores.getSecciones().iterator().next().getMaximoMonitores()==0){
//                apoConHonores =cursoFacade.findByCodigo(getConstanteBean().getConstante(Constantes.VAL_CODIGO_LAB_APO_CON_HONORES));
//            }
//            //si el curso de apo con honores fue abierto y tiene secciones, estan se agregan a la lista de secciones "listaSeccionesAPO1"
//            if (apoConHonores!= null ){
//                for (Seccion seccion : apoConHonores.getSecciones()) {
//                    listaSeccionesAPO1.add(seccion);
//                }
//            }

            //***************Tercero se "restan" a las secciones del curso de apo1, las seccion que tienen monitor. Al final la contenedora listaSeccionesAPO1
            //tiene las secciones sin monitor
            ArrayList<String> listaCrn = new ArrayList<String>();
            for (Seccion seccionConMonitor : listaSeccionesConMonitor) {
                for (Seccion seccionNormal : listaSeccionesAPO1) {
                    if (seccionNormal.getCrn().equals(seccionConMonitor.getCrn())) {
                        listaCrn.add(seccionNormal.getCrn());
                    }
                }
            }
            for (String crn : listaCrn) {
                Seccion seccionBuscada = null;
                boolean encontrado = false;
                for (Iterator<Seccion> it = listaSeccionesAPO1.iterator(); it.hasNext() && !encontrado;) {
                    Seccion seccion = it.next();
                    if (seccion.getCrn().equals(crn)) {
                        seccionBuscada = seccion;
                        encontrado = true;
                    }
                }
                listaSeccionesAPO1.remove(seccionBuscada);
            }

            //*************** Cuarto se crea la respuesta del comando
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secSecciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES), getConstanteBean().getConstante(Constantes.NULL));
            for (Seccion seccion : listaSeccionesAPO1) {
                Secuencia secSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCION), getConstanteBean().getConstante(Constantes.NULL));
                Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), getConstanteBean().getConstante(Constantes.NULL));
                Persona personaProfesor = seccion.getProfesorPrincipal().getPersona();
                Secuencia secNombreProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), personaProfesor.getNombres() + " " + personaProfesor.getApellidos());
                Secuencia secNumeroSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_SECCION), "" + seccion.getNumeroSeccion());
                Secuencia secCrnSeccion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CRN_SECCION), seccion.getCrn());

                secProfesor.agregarSecuencia(secNombreProfesor);
                secSeccion.agregarSecuencia(secProfesor);
                secSeccion.agregarSecuencia(secNumeroSeccion);
                secSeccion.agregarSecuencia(secCrnSeccion);

                secSecciones.agregarSecuencia(secSeccion);
            }
            secuencias.add(secSecciones);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_SECCIONES_SIN_MONITOR_T2), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Agrega el horario de una seccion a un aspirante
     * @param crn Crn de la seccion
     * @param correo Correo del aspirante
     */
    @Override
    public void agregarHorarioSeccionAAspirante(String crn, String correo) {
        Collection<Sesion> sesiones = getSeccionFacade().findByCRN(crn).getHorarios();
        Horario_Disponible horario = getAspiranteFacade().findByCorreo(correo).getHorario_disponible();
        HashMap<String, DiaCompleto> mapa = new HashMap<String, DiaCompleto>();
        for (DiaCompleto diaCompleto : horario.getDias_disponibles()) {
            mapa.put(diaCompleto.getDia_semana(), diaCompleto);
        }
        String horasDiaVacio = "";
        for (int i = 0; i < 48; i++) {
            horasDiaVacio += "0";
        }
        for (Sesion sesion : sesiones) {
            Collection<DiaCompleto> diasCompletos = sesion.getDias();
            for (DiaCompleto diaCompleto : diasCompletos) {
                DiaCompleto diaAspirante = mapa.get(diaCompleto.getDia_semana());
                if (diaAspirante == null) {
                    diaAspirante = new DiaCompleto();
                    diaAspirante.setDia_semana(diaCompleto.getDia_semana());
                    diaAspirante.setHoras(horasDiaVacio);
                    Collection<DiaCompleto> dias = horario.getDias_disponibles();
                    dias.add(diaAspirante);
                    getDiaCompletoFacade().create(diaAspirante);
                    horario.setDias_disponibles(dias);
                    getHorario_DisponibleFacade().edit(horario);
                }
                char[] horasAspirante = diaAspirante.getHoras().toCharArray();
                char[] horasSeccion = diaCompleto.getHoras().toCharArray();
                for (int i = 0; i < horasSeccion.length; i++) {
                    if (horasSeccion[i] == '1' && horasAspirante[i] == '0') {
                        horasAspirante[i] = '2';
                    }
                }

                diaAspirante.setHoras(new String(horasAspirante));
            }
        }
        for (DiaCompleto diaCompleto : horario.getDias_disponibles()) {
            getDiaCompletoFacade().edit(diaCompleto);
        }
        getHorario_DisponibleFacade().edit(horario);
    }

    @Override
    public void removerHorarioSeccionAAspirante(String crn, String correo) {
        Collection<Sesion> sesiones = getSeccionFacade().findByCRN(crn).getHorarios();
        Horario_Disponible horario = getAspiranteFacade().findByCorreo(correo).getHorario_disponible();
        HashMap<String, DiaCompleto> mapa = new HashMap<String, DiaCompleto>();
        for (DiaCompleto diaCompleto : horario.getDias_disponibles()) {
            mapa.put(diaCompleto.getDia_semana(), diaCompleto);
        }
        for (Sesion sesion : sesiones) {
            Collection<DiaCompleto> diasCompletos = sesion.getDias();
            for (DiaCompleto diaCompleto : diasCompletos) {
                DiaCompleto diaAspirante = mapa.get(diaCompleto.getDia_semana());
                char[] horasAspirante = diaAspirante.getHoras().toCharArray();
                char[] horasSeccion = diaCompleto.getHoras().toCharArray();
                for (int i = 0; i < horasSeccion.length; i++) {
                    //if(horasSeccion[i] == '1' && horasAspirante[i] == '2'){
                    if (horasSeccion[i] == '1') {
                        horasAspirante[i] = '0';
                    }
                }
                diaAspirante.setHoras(new String(horasAspirante));
            }
        }
        for (DiaCompleto diaCompleto : horario.getDias_disponibles()) {
            getDiaCompletoFacade().edit(diaCompleto);
        }
    }

    @Override
    public String consultarSolicitudes(String comando) {
        try {
            getParser().leerXML(comando);
            Collection<Solicitud> listaSolicitudes = solicitudFacade.findAll();
            Secuencia secSolicitudes = new ConversorSolicitud().darSecuenciaSolicitudes(listaSolicitudes);

            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secSolicitudes);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_MONITORIAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String consultarSolicitudesResueltas(String xml) {
        try {
            ConversorSolicitud conversor = new ConversorSolicitud();

            getParser().leerXML(xml);
            Secuencia secSolicitudes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES), "");

            Collection<Solicitud> solicitudes = getSolicitudFacade().findSolicitudesResueltas();
            secSolicitudes = conversor.crearSecuenciasSolicitudesResueltas(solicitudes);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secSolicitudes);

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_RESUELTAS_SECRETARIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.CONFLICTOS_MSJ_007, secuencias);
        } catch (Exception e) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_RESUELTAS_SECRETARIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0056, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private void verificarExisteDia(String diaSemana, Collection<DiaCompleto> dias) {

        boolean found = false;
        for (DiaCompleto diaCompleto : dias) {
            if (diaCompleto.getDia_semana().equals(diaSemana)) {
                found = true;
                break;
            }
        }
        if (!found) {
            String horas = "";
            for (int i = 0; i < 48; i++) {
                horas += "0";
            }
            DiaCompleto dia = new DiaCompleto();
            dia.setHoras(horas);
            dia.setDia_semana(diaSemana);
            dias.add(dia);
        }
    }

    private TipoCuentaFacadeRemote getTipoCuentaFacade() {
        return tipoCuentaFacade;
    }

    private PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private EstudianteFacadeRemote getEstudianteFacade() {
        return estudianteFacade;
    }

    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    public MonitoriaRealizadaFacadeLocal getMonitoriaRealizadaFacade() {
        return monitoriaRealizadaFacade;
    }

    public Monitoria_SolicitadaFacadeLocal getMonitoriaSolicitadaFacade() {
        return monitoriaSolicitadaFacade;
    }
}
