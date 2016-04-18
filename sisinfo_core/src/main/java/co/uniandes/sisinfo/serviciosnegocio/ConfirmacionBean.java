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

import java.util.ArrayList;
import java.util.Collection;
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
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.Horario_Disponible;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.Monitoria_Solicitada;
import co.uniandes.sisinfo.entities.Regla;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.Horario_DisponibleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReglaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicio de negocio: Administración de Confirmación
 */
@Stateless
@EJB(name = "ConfirmacionBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ConfirmacionLocal.class)
public class ConfirmacionBean implements ConfirmacionRemote, ConfirmacionLocal {

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
     * CorreoBean
     */
    @EJB
    private CorreoRemote correoBean;
    /**
     * PreseleccionBean
     */
    @EJB
    private PreseleccionLocal preseleccionBean;
    /**
     * ReglaBean
     */
    @EJB
    private ReglaRemote reglaBean;
    /**
     * Horario_DisponibleFacade
     */
    @EJB
    private Horario_DisponibleFacadeLocal horario_disponibleFacade;
    /**
     * AspiranteFacade
     */
    @EJB
    private AspiranteFacadeLocal aspiranteFacade;
    /**
     * CursoFacade
     */
    @EJB
    private CursoFacadeRemote cursoFacade;
    /**
     * ReglaFacade
     */
    @EJB
    private ReglaFacadeRemote reglaFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private RangoFechasBeanLocal rangoBean;
    @EJB
    private SolicitudLocal solicitudBean;
    private ServiceLocator serviceLocator;
    private ConversorServiciosSoporteProcesos conversorServiciosSoporteProcesos;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ConfirmacionBean
     */
    public ConfirmacionBean() {
        try {
            serviceLocator = new ServiceLocator();
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            cursoFacade = (CursoFacadeRemote) serviceLocator.getRemoteEJB(CursoFacadeRemote.class);
            reglaFacade = (ReglaFacadeRemote) serviceLocator.getRemoteEJB(ReglaFacadeRemote.class);
            reglaBean = (ReglaRemote) serviceLocator.getRemoteEJB(ReglaRemote.class);
            conversorServiciosSoporteProcesos = new ConversorServiciosSoporteProcesos();
        } catch (NamingException ex) {
            Logger.getLogger(ConfirmacionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String confirmarSeccion(String xml) {
        try {
            ConversorGeneral conversorG = new ConversorGeneral(xml);
            getParser().leerXML(xml);
            String confirmacion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONFIRMACION)).getValor();
            String idSol = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            Long idSolicitud = Long.parseLong(idSol.trim());
            Solicitud s = solicitudFacade.findById(idSolicitud);
            String idTarea = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TAREA)).getValor();
            //Tarea tarea = getTareaFacade().findById(Long.parseLong(idTarea));
            rangoBean.realizarTareaSolicitud(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE), idSolicitud);
            /*Collection<Tarea> tareas = getTareaFacade().findByCorreoResponsableTipoYEstado(s.getEstudiante().getEstudiante().getPersona().getCorreo(),getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CONFIRMACION_ESTUDIANTE), getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE));
            for (Tarea tarea : tareas) {
            if (tarea != null) {
            getTareaBean().realizarTarea(tarea.getId());
            }
            }*/


            if (Boolean.parseBoolean(confirmacion)) {
                String resp = confirmarSeccionSolicitud(idSol.trim());
                rangoBean.crearTareaVerificarDatosEstudiantePorParteCoordinacion(Long.parseLong(idSol.trim()));
                //Borrar el timer de memoria y de la base de datos dado el identificador de la solicitud
                rangoBean.eliminarTimerConfirmacionEstudiante(idSolicitud);
                return resp;
            } else {
                //Borrar el timer de memoria y de la base de datos dado el identificador de la solicitud
                rangoBean.eliminarTimerConfirmacionEstudiante(idSolicitud);
                //Se recupera los datos del profesor y se manda correo
                boolean revertida = getPreseleccionBean().revertirPreseleccion(idSol, conversorG.obtenerResponsable(), null, Notificaciones.MENSAJE_REVERSION_SOLICITUD_ASPIRANTE);
                if (revertida) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0009, new LinkedList<Secuencia>());
                } else {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0010, new LinkedList<Secuencia>());
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ConfirmacionBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0053, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConfirmacionBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String actualizarSolicitudVerificacion(String xml) {
        try {
            ConversorGeneral conversorG = new ConversorGeneral(xml);
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            Secuencia secCorreo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secMensaje = secCorreo.obtenerSecuenciaHija(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE));
            String mensaje = secMensaje.getValor();
            Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
            if (solicitud == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, new LinkedList<Secuencia>());
            } else {
                if(solicitud.getEstadoSolicitud().equals(constanteBean.getConstante(Constantes.ESTADO_PENDIENTE_REGISTRO_CONVENIO)) ||
                        solicitud.getEstadoSolicitud().equals(constanteBean.getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_ESTUDIANTE)) ||
                        solicitud.getEstadoSolicitud().equals(constanteBean.getConstante(Constantes.ESTADO_PENDIENTE_FIRMA_CONVENIO_DEPARTAMENTO)) ||
                        solicitud.getEstadoSolicitud().equals(constanteBean.getConstante(Constantes.ESTADO_PENDIENTE_RADICACION)) ||
                        solicitud.getEstadoSolicitud().equals(constanteBean.getConstante(Constantes.ESTADO_ASIGNADO))){

                    //La solicitud ya ha sido verificada
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0034, new LinkedList<Secuencia>());
                }else if(solicitud.getEstadoSolicitud().equals(constanteBean.getConstante(Constantes.ESTADO_ASPIRANTE)) ||
                        solicitud.getEstadoSolicitud().equals(constanteBean.getConstante(Constantes.ESTADO_PENDIENTE_CONFIRMACION_ESTUDIANTE))){

                    //La solicitud ha sido revertida
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.MON_ERR_0035, new LinkedList<Secuencia>());
                }
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                //procesar la decisión tomada en coordinación
                Secuencia secEstado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO));
                String valorDecision = secEstado.getValor();
                if (valorDecision.equals("aceptar")) {
                    solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_REGISTRO_CONVENIO));
                    getSolicitudFacade().edit(solicitud);
                    //Se debe de crear la tarea de traer papeles
                    rangoBean.crearTareaTraerPapelesRegistrarConvenio(solicitud.getId());
                    String correoEstudiante = solicitud.getEstudiante().getEstudiante().getPersona().getCorreo();
                    String asunto = Notificaciones.ASUNTO_PAPELES_CONVENIO;
                    correoBean.enviarMail(correoEstudiante, asunto, null, null, null, mensaje);
                } else if (valorDecision.equals("rechazar")) {
                    getPreseleccionBean().revertirPreseleccion(idSolicitud, conversorG.obtenerResponsable(), null, mensaje);
                } else if (valorDecision.equals("eliminar")) {
                    getSolicitudBean().eliminarSolicitud(solicitud.getId(), conversorG.obtenerResponsable(), mensaje);
                }
                rangoBean.realizarTareaVerificacionDatosEstudianteCoordinacion(solicitud.getId());
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0098, new LinkedList<Secuencia>());
            }
            
        } catch (Exception e) {
            try {
                Logger.getLogger(ConfirmacionBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0009, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConfirmacionBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Método que confirma una sección dado el id de la solicitud
     * @param idSolicitud Id de la solicitud
     * @return String: XML con la respuesta a la confirmación
     * @throws java.lang.Exception
     */
    private String confirmarSeccionSolicitud(String idSolicitud) throws Exception {
        Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
        if (solicitud == null) {
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0053, new LinkedList<Secuencia>());
        } else {
            solicitud.setEstadoSolicitud(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_VERIFICACION_DATOS));
            getSolicitudFacade().edit(solicitud);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0009, new LinkedList<Secuencia>());
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
     * Retorna SolicitudBean
     * @return solicitudBean SolicitudBean
     */
    private SolicitudLocal getSolicitudBean() {
        return solicitudBean;
    }

    /**
     * Retorna PreseleccionBean
     * @return preseleccionBean PreseleccionBean
     */
    private PreseleccionLocal getPreseleccionBean() {
        return preseleccionBean;
    }

    /**
     * Retorna ReglaBean
     * @return reglaBean ReglaBean
     */
    private ReglaRemote getReglaBean() {
        return reglaBean;
    }

    /**
     * Retorna Horario_disponibleFacade
     * @return horario_disponibleFacade Horario_disponibleFacade
     */
    private Horario_DisponibleFacadeLocal getHorario_disponibleFacade() {
        return horario_disponibleFacade;
    }

    /**
     * Retorna ReglaFacade
     * @return reglaFacade ReglaFacade
     */
    private ReglaFacadeRemote getReglaFacade() {
        return reglaFacade;
    }

    /**
     * Retorna AspiranteFacade
     * @return aspiranteFacade AspiranteFacade
     */
    private AspiranteFacadeLocal getAspiranteFacade() {
        return aspiranteFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    /*
     *  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     * METODOS POR REVISAR EN MONITORIAS VERSION 2.0
     * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *  ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Método que actualiza la información académica de un aspirante
     * @param informacionAcademica Secuencia con la información académica del aspirante
     * @param aspirante Aspirante
     */
    private void actualizarInformacionAcademica(Secuencia informacionAcademica, Aspirante aspirante) {
        String semestreSegunCreditos = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_SEGUN_CREDITOS)).getValor();
        String promedioTotal = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_TOTAL)).getValor();
        String creditosSemestreActual = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_SEMESTRE_ACTUAL)).getValor();
        String creditosAprobados = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_APROBADOS)).getValor();
        String creditosCursados = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CREDITOS_CURSADOS)).getValor();
        String promedioUltimo = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_ULTIMO)).getValor();
        String promedioHaceDosSemestres = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_DOS_SEMESTRES)).getValor();
        String promedioHaceTresSemestres = informacionAcademica.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROMEDIO_HACE_TRES_SEMESTRES)).getValor();
        InformacionAcademica info = aspirante.getEstudiante().getInformacion_Academica();
        info.setCreditosAprobados(creditosAprobados != null ? Double.parseDouble(creditosAprobados) : 0);
        info.setCreditosCursados(creditosCursados != null ? Double.parseDouble(creditosCursados) : 0);
        info.setCreditosSemestreActual(creditosSemestreActual != null ? Double.parseDouble(creditosSemestreActual) : 0);
        info.setPromedioAntepenultipo(promedioHaceTresSemestres != null ? Double.parseDouble(promedioHaceTresSemestres) : 0);
        info.setPromedioPenultimo(promedioHaceDosSemestres != null ? Double.parseDouble(promedioHaceDosSemestres) : 0);
        info.setPromedioTotal(promedioTotal != null ? Double.parseDouble(promedioTotal) : 0);
        info.setPromedioUltimo(promedioUltimo != null ? Double.parseDouble(promedioUltimo) : 0);
        info.setSemestreSegunCreditos(semestreSegunCreditos != null ? semestreSegunCreditos : "0");
        getAspiranteFacade().edit(aspirante);
    }

    /**
     * Método que actualiza la información del horario del estudiante
     * @param horario Secuencia con la información del horario
     * @param aspirante Aspirante
     */
    private void actualizarInformacionHorario(Aspirante aspirante) {
        Horario_Disponible horarioDisponible = aspirante.getHorario_disponible();
        if (horarioDisponible == null) {
            horarioDisponible = new Horario_Disponible();
            getHorario_disponibleFacade().create(horarioDisponible);
        }

        Secuencia secuenciaHorario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO));
        Collection<DiaCompleto> diasDisponibles = conversorServiciosSoporteProcesos.pasarSecuenciaADiasCompletos(secuenciaHorario);

        String horaVacia = "";
        for (int i = 0; i < 48; i++) {
            horaVacia += "0";
        }

        boolean[] diasAgregados = new boolean[7];
        String[] nombresDias = new String[]{"L", "M", "I", "J", "V", "S", "D"};
        for (DiaCompleto diaCompleto : diasDisponibles) {
            String diaSemana = diaCompleto.getDia_semana();
            for (int i = 0; i < nombresDias.length; i++) {
                String string = nombresDias[i];
                if (string.equals(diaSemana)) {
                    diasAgregados[i] = true;
                }
            }
        }
        for (int i = 0; i < diasAgregados.length; i++) {
            if (!diasAgregados[i]) {
                DiaCompleto dia = new DiaCompleto();
                dia.setDia_semana(nombresDias[i]);
                dia.setHoras(horaVacia);
                diasDisponibles.add(dia);
            }
        }
        horarioDisponible.setDias_disponibles(diasDisponibles);
        getHorario_disponibleFacade().edit(horarioDisponible);
    }

    /**
     * Valida la información del aspirante de acuerdo a las reglas de negocio
     * @param aspirante Aspirante
     * @param monitoria_Solicitada Monitoria solicitada
     * @return Colección con el nombre de las reglas que fallaron
     */
    private Collection<Secuencia> validarInformacion(Solicitud solicitud) {
        Collection<Secuencia> secuencias = new LinkedList<Secuencia>();
        secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD), Long.toString(solicitud.getId())));
        Secuencia verificacionesReglas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VERIFICACIONES_REGLAS), getConstanteBean().getConstante(Constantes.NULL));
        Aspirante aspirante = solicitud.getEstudiante();
        Monitoria_Solicitada monitoria_Solicitada = solicitud.getMonitoria_solicitada();
        double promedio = aspirante.getEstudiante().getInformacion_Academica().getPromedioTotal();
        double nota = monitoria_Solicitada.getNota_materia();
        String banco = aspirante.getEstudiante().getBanco();
        String cuenta = aspirante.getEstudiante().getCuentaBancaria();
        String tipoCuenta = "";
        if (aspirante.getEstudiante().getTipoCuenta() != null) {
            tipoCuenta = aspirante.getEstudiante().getTipoCuenta().getNombre();
        }
        double creditos = aspirante.getEstudiante().getInformacion_Academica().getCreditosSemestreActual() - Double.valueOf(getConstanteBean().getConstante(Constantes.VAL_CREDITOS_MONITORIA_T1T));
        verificacionesReglas.agregarSecuencia(getSecuenciaValidacion(getConstanteBean().getConstante(Constantes.REGLA_PROMEDIO_TOTAL), Double.toString(promedio)));
        // esta regla es igual que regla_creditos_semestre_actual
        verificacionesReglas.agregarSecuencia(getSecuenciaValidacion(getConstanteBean().getConstante(Constantes.REGLA_NOTA_MATERIA), Double.toString(nota)));
        verificacionesReglas.agregarSecuencia(getSecuenciaValidacion(getConstanteBean().getConstante(Constantes.REGLA_BANCO_NO_VACIO_NULO), banco));
        verificacionesReglas.agregarSecuencia(getSecuenciaValidacion(getConstanteBean().getConstante(Constantes.REGLA_CUENTA_NO_VACIA_NULA), cuenta));
        verificacionesReglas.agregarSecuencia(getSecuenciaValidacion(getConstanteBean().getConstante(Constantes.REGLA_TIPO_CUENTA_NO_VACIO_NULO), tipoCuenta));
        verificacionesReglas.agregarSecuencia(getSecuenciaValidacion(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL), Double.toString(creditos)));
        secuencias.add(verificacionesReglas);
        return secuencias;
    }

    /**
     * Método que hace la verificación de reglas de negocio y que retorna las razones por las cuales la verificación pudo haber fallado
     * @param solicitud La solicitud que se va a verificar
     * @return la lista de razones por la cuales la solicitud no pasó la verificación
     */
    private Collection<String> verificarReglasYDeterminarRazones(Solicitud solicitud) {
        Collection<String> listaRazonesRechazo = new ArrayList<String>();
        Aspirante aspirante = solicitud.getEstudiante();
        Monitoria_Solicitada monitoria_Solicitada = solicitud.getMonitoria_solicitada();
        double nota = monitoria_Solicitada.getNota_materia();
        double promedio = aspirante.getEstudiante().getInformacion_Academica().getPromedioTotal();
        int creditosMonitoriasFijas = 0;

        int creditosSolicitudActual = 0;
        Iterator<MonitoriaAceptada> iteSolicitudActual = solicitud.getMonitorias().iterator();
        if (solicitud.getMonitorias().size() == 2) {
            creditosSolicitudActual += iteSolicitudActual.next().getCarga();
            creditosSolicitudActual += iteSolicitudActual.next().getCarga();
        } else if (solicitud.getMonitorias().size() == 1) {
            creditosSolicitudActual += iteSolicitudActual.next().getCarga();
        }

        List<Solicitud> lista = solicitudFacade.findByLogin(aspirante.getPersona().getCorreo());
        Iterator<Solicitud> iteSolicitud = lista.iterator();
        while (iteSolicitud.hasNext()) {
            Solicitud sol = iteSolicitud.next();
            if (sol.getEstadoSolicitud().equals(getConstanteBean().getConstante(Constantes.ESTADO_PENDIENTE_VERIFICACION_DATOS))) {
                Iterator<MonitoriaAceptada> iteMon = sol.getMonitorias().iterator();
                while (iteMon.hasNext()) {
                    creditosMonitoriasFijas += iteMon.next().getCarga();
                }
            }
        }

        double creditos = aspirante.getEstudiante().getInformacion_Academica().getCreditosSemestreActual().doubleValue() + aspirante.getEstudiante().getInformacion_Academica().getCreditosMonitoriasISISEsteSemestre().doubleValue() - creditosMonitoriasFijas + creditosSolicitudActual;

        boolean pasaReglaValNotaMateria;
        if (nota == -1) {
            pasaReglaValNotaMateria = true;
        } else {
            pasaReglaValNotaMateria = getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_NOTA_MATERIA), Double.toString(nota));
            if (!pasaReglaValNotaMateria) {
                String razonValorNota = "- La nota mínima del curso que se vio debe ser igual o mayor que 4.0 [ELIMINAR]";
                listaRazonesRechazo.add(razonValorNota);
            }
        }

        if (promedio >= Double.parseDouble(getConstanteBean().getConstante(Constantes.PROMEDIO_MINIMO_EXTRACREDITOS))) {
            boolean pasaReglaNotaTotal = getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_PROMEDIO_TOTAL), Double.toString(promedio));

            if (!pasaReglaNotaTotal) {
                String razonNotaTotal = "- El promedio acumulado debe ser mayor o igual a 3.5 [ELIMINAR]";
                listaRazonesRechazo.add(razonNotaTotal);
            }
            boolean pasaReglaCreditosSemestreActual = getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL), Double.toString(creditos));
            if (!pasaReglaCreditosSemestreActual) {
                String razonCreditosSem = "- Se excede el número máximo de créditos que se pueden ver para este semestre";
                listaRazonesRechazo.add(razonCreditosSem);
            }
        } else {
            boolean pasaReglaNotaTotal = getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_PROMEDIO_TOTAL), Double.toString(promedio));
            if (!pasaReglaNotaTotal) {
                String razonNotaTotal = "- El promedio acumulado debe ser mayor o igual a 3.75 [ELIMINAR]";
                listaRazonesRechazo.add(razonNotaTotal);
            }
            boolean pasaReglaCreditosSemActualPromInferiorCuatro = getReglaBean().validarRegla(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO), Double.toString(creditos));
            if (!pasaReglaCreditosSemActualPromInferiorCuatro) {
                String razonPromedioCreditos = "- Si su promedio acumulado es menor que 4.0, el máximo de créditos que puede tomar es de 20.5 incluida(s) monitoría(s)";
                listaRazonesRechazo.add(razonPromedioCreditos);
            }
        }
        return listaRazonesRechazo;
    }

    /**
     * Retorna una secuencia con el resultado de la validación de la información respecto a las reglas del negocio
     * @param nombreRegla Nombre de la regla
     * @param valorComparar Valor a comparar
     * @return Secuencia con el resultado de la validación
     */
    private Secuencia getSecuenciaValidacion(String nombreRegla, String valorComparar) {
        Regla reglaValidar = getReglaFacade().findByNombre(nombreRegla);
        Secuencia secuenciaValidacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VERIFICACION_REGLA), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaValidacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_REGLA), Long.toString(reglaValidar.getId())));
        secuenciaValidacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), reglaValidar.getNombre()));
        secuenciaValidacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), valorComparar));
        if (getReglaBean().validarRegla(nombreRegla, valorComparar)) {
            secuenciaValidacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESULTADO_REGLA), getConstanteBean().getConstante(Constantes.TRUE)));
        } else {
            secuenciaValidacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESULTADO_REGLA), getConstanteBean().getConstante(Constantes.FALSE)));
        }
        secuenciaValidacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION), reglaValidar.getDescripcion()));
        return secuenciaValidacion;
    }

    @Override
    public String determinarResultadoVerificacion(String xml) {
        try {
            getParser().leerXML(xml);
            String idSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();


            Solicitud solicitud = getSolicitudFacade().findById(Long.parseLong(idSolicitud));
            if (solicitud == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DETERMINAR_RESULTADO_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0072, new LinkedList<Secuencia>());
            } else {
                Aspirante aspirante = solicitud.getEstudiante();
                Secuencia informacionAcademica = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_ACADEMICA));
                actualizarInformacionAcademica(informacionAcademica, aspirante);
                Secuencia informacionMonitoria = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MONITORIA_SOLICITADA));
                Secuencia cursoVisto = informacionMonitoria.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO));
                double notaMateria = Double.parseDouble(cursoVisto.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_MATERIA)).getValor());
                solicitud.getMonitoria_solicitada().setNota_materia(notaMateria);
                getSolicitudFacade().edit(solicitud);
                //Se actualiza el horario
                //actualizarInformacionHorario(aspirante);

                Collection<String> listaRazonesNoPasaVerificacion = verificarReglasYDeterminarRazones(solicitud);
                Secuencia secHayConflicto = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HAY_CONFLICTO));
                boolean hayConflictoVerifHorario = Boolean.parseBoolean(secHayConflicto.getValor());//verificarConflictoVerificacion(solicitud);
                if (hayConflictoVerifHorario) {
                    listaRazonesNoPasaVerificacion.add("- El horario ingresado entra conflicto con el horario de la sección solicitada.");
                }
                getAspiranteFacade().edit(aspirante);
                Collection<Secuencia> secuencias = validarInformacion(solicitud);
                if (listaRazonesNoPasaVerificacion.size() > 0) {
                    //NO Pasa la verificación LA SOLICITUD
                    getSolicitudFacade().edit(solicitud);
                    //Se manda un correo para que el estudiante traiga los papeles
                    String para = aspirante.getPersona().getCorreo();
                   

                    String accionSolicitud = "rechazar";
                    Collection<String> listaRazones = new ArrayList();

                    for (String razonRechazo : listaRazonesNoPasaVerificacion) {
                        if (razonRechazo.contains("[ELIMINAR]")) {
                            accionSolicitud = "eliminar";
                            razonRechazo = razonRechazo.trim().substring(0, razonRechazo.length()-10);
                        }
                        listaRazones.add(razonRechazo);
                    }
                    listaRazonesNoPasaVerificacion = listaRazones;


                    String mensajeCorreoAceptado = crearMensajeRechazoMonitoria(solicitud, aspirante, listaRazonesNoPasaVerificacion);
                    String asuntoCorreoRechazado = Notificaciones.ASUNTO_RECHAZO_SOLICITUD_VERIFICACION_DATOS;
                    //mensaje oficial deberia ser rechazar o eliminar

                    Secuencia secuenciaCorreos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREOS), "");

                    Secuencia secuenciaCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), "");
                    secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), accionSolicitud));
                    secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), asuntoCorreoRechazado));
                    secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE), mensajeCorreoAceptado));
                    secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FORZADA), Boolean.TRUE.toString()));
                    secuenciaCorreos.agregarSecuencia(secuenciaCorreo);
                    //si la accion es eliminar se repite otro correo igual para rechazar
                    if (accionSolicitud.equals("eliminar")) {

                        secuenciaCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), "");
                        secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), "rechazar"));
                        secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), asuntoCorreoRechazado));
                        secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE), mensajeCorreoAceptado));
                        secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FORZADA), Boolean.FALSE.toString()));
                        secuenciaCorreos.agregarSecuencia(secuenciaCorreo);
                    } //si no se crea un mensaje generico para eliminar
                    else {
                        String asuntoEliminarSol = Notificaciones.ASUNTO_RECHAZO_SOLICITUD_VERIFICACION_DATOS;
                        String mensajeCorreoEliminar = crearMensajeElimnarSolicitud(solicitud, aspirante);
                        secuenciaCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), "");
                        secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), "eliminar"));
                        secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), asuntoEliminarSol));
                        secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE), mensajeCorreoEliminar));
                        secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FORZADA), Boolean.FALSE.toString()));
                        secuenciaCorreos.agregarSecuencia(secuenciaCorreo);
                    }
                    //ahora agregar el otro msj

                    String asuntoCorreoAceptado = Notificaciones.ASUNTO_PAPELES_CONVENIO;
                    mensajeCorreoAceptado = crearMensajeEnvioPapelesConvenio(solicitud, aspirante);
                    Secuencia secuenciaCorreo2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), "");
                    secuenciaCorreo2.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), "aceptar"));
                    secuenciaCorreo2.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), asuntoCorreoAceptado));
                    secuenciaCorreo2.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE), mensajeCorreoAceptado));
                    secuenciaCorreo2.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FORZADA), Boolean.FALSE.toString()));
                    //-----

                    secuenciaCorreos.agregarSecuencia(secuenciaCorreo2);

                    //----------------


                    secuencias.add(secuenciaCorreos);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DETERMINAR_RESULTADO_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0098, new LinkedList<Secuencia>());
                } else {
                    //Pasa la verificación sin novedades y es ACEPTADA LA SOLICITUD
                    String asuntoCorreoAceptado = Notificaciones.ASUNTO_PAPELES_CONVENIO;
                    String mensajeCorreoAceptado = crearMensajeEnvioPapelesConvenio(solicitud, aspirante);


                    Secuencia secuenciaCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), "");
                    secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), "aceptar"));
                    secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), asuntoCorreoAceptado));
                    secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE), mensajeCorreoAceptado));
                    secuenciaCorreo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FORZADA), Boolean.TRUE.toString()));
                    //-----
                    Secuencia secuenciaCorreos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREOS), "");
                    secuenciaCorreos.agregarSecuencia(secuenciaCorreo);
                    //-----
                    //segundo mensaje:

                    String mensajeCorreoRechazado = crearMensajeRechazoMonitoria(solicitud, aspirante, listaRazonesNoPasaVerificacion);
                    String asuntoCorreoRechazado = Notificaciones.ASUNTO_RECHAZO_SOLICITUD_VERIFICACION_DATOS;
                    Secuencia secuenciaCorreo2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), "");
                    secuenciaCorreo2.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), "rechazar"));
                    secuenciaCorreo2.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), asuntoCorreoRechazado));
                    secuenciaCorreo2.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE), mensajeCorreoRechazado));
                    secuenciaCorreo2.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FORZADA), Boolean.FALSE.toString()));
                    secuenciaCorreos.agregarSecuencia(secuenciaCorreo2);

                    //-msj eliminar:
                    String mensajeCorreoEliminar = crearMensajeElimnarSolicitud(solicitud, aspirante);
                    asuntoCorreoRechazado = Notificaciones.ASUNTO_RECHAZO_SOLICITUD_VERIFICACION_DATOS;
                    Secuencia secuenciaCorreo3 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), "");
                    secuenciaCorreo3.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), "eliminar"));
                    secuenciaCorreo3.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), asuntoCorreoRechazado));
                    secuenciaCorreo3.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE), mensajeCorreoEliminar));
                    secuenciaCorreo3.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FORZADA), Boolean.FALSE.toString()));
                    secuenciaCorreos.agregarSecuencia(secuenciaCorreo3);


                    secuencias.add(secuenciaCorreos);
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DETERMINAR_RESULTADO_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0098, new LinkedList<Secuencia>());
                }
            }

        } catch (Exception e) {
            try {
                Logger.getLogger(ConfirmacionBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0009, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ConfirmacionBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private String crearMensajeEnvioPapelesConvenio(Solicitud solicitud, Aspirante aspirante) {
        String mensajeCorreoAceptado = Notificaciones.MENSAJE_PAPELES;
        String nombreEstudiante = aspirante.getPersona().getNombres() + " " + aspirante.getPersona().getApellidos();

        //Se manda un correo para que el estudiante traiga los papeles
        int cantMonitorias = solicitud.getMonitorias().size();
        String detalleMonitorias = "";
        if (cantMonitorias == 1) {
            MonitoriaAceptada mon = solicitud.getMonitorias().iterator().next();
            Seccion sec = mon.getSeccion();
            int numeroSeccion = sec.getNumeroSeccion();
            String crnSeccion = sec.getCrn();
            Curso curso = cursoFacade.findByCRNSeccion(crnSeccion);
            detalleMonitorias +=  curso.getCodigo() + " " + curso.getNombre() + " - " + numeroSeccion;
        } else if (cantMonitorias == 2) {
            Iterator<MonitoriaAceptada> iterator = solicitud.getMonitorias().iterator();
            MonitoriaAceptada mon1 = iterator.next();
            MonitoriaAceptada mon2 = iterator.next();

            Seccion sec1 = mon1.getSeccion();
            int numeroSeccion1 = sec1.getNumeroSeccion();
            String crnSeccion1 = sec1.getCrn();
            Curso curso = cursoFacade.findByCRNSeccion(crnSeccion1);
            detalleMonitorias += curso.getCodigo() + " " + curso.getNombre() + " - " + numeroSeccion1 + "<br>";

            Seccion sec2 = mon2.getSeccion();
            int numeroSeccion2 = sec2.getNumeroSeccion();
            detalleMonitorias += curso.getCodigo() + " " + curso.getNombre() + " - " + numeroSeccion2;
        }        
        mensajeCorreoAceptado = String.format(mensajeCorreoAceptado,nombreEstudiante,detalleMonitorias);
        return mensajeCorreoAceptado;
    }

    private String crearMensajeRechazoMonitoria(Solicitud solicitud, Aspirante aspirante, Collection<String> listaRazonesNoPasaVerificacion) {

        String mensajeCorreoAceptado = Notificaciones.MENSAJE_RECHAZO_SOLICITUD_VERIFICACION_DATOS;
        String nombreEstudiante = aspirante.getPersona().getNombres() + " " + aspirante.getPersona().getApellidos();
        String mensaje = mensajeCorreoAceptado.replaceFirst("%1", nombreEstudiante);
        String mensajeCursos = "";
        Collection<MonitoriaAceptada> mons = solicitud.getMonitorias();
        for (MonitoriaAceptada monitoria : mons) {
            String crn = monitoria.getSeccion().getCrn();
            String numSeccion = "" + monitoria.getSeccion().getNumeroSeccion();
            String nombreCurso = monitoria.getSolicitud().getMonitoria_solicitada().getCurso().getNombre();
            String codigoCurso = monitoria.getSolicitud().getMonitoria_solicitada().getCurso().getCodigo();
            if (mensajeCursos.equals("")) {
                mensajeCursos += "Curso: " + codigoCurso + " - " + nombreCurso + " Sección " + numSeccion + " CRN " + crn;
            } else {
                mensajeCursos += ", Curso: " + codigoCurso + " - " + nombreCurso + " Sección " + numSeccion + " CRN " + crn;
            }
        }
        mensaje = mensaje.replaceFirst("%2", mensajeCursos + " ");

        String mensajeRazonesRechazo = "";
        String accionSolicitud = "rechazar";

        for (String razonRechazo : listaRazonesNoPasaVerificacion) {
            if (razonRechazo.contains("ELIMINAR")) {
                accionSolicitud = "eliminar";
                razonRechazo.replaceFirst("ELIMINAR", "");
            }
            mensajeRazonesRechazo += razonRechazo + "<br>";
        }

        mensaje = mensaje.replaceFirst("%3", mensajeRazonesRechazo);
        return mensaje;
    }

    private String crearMensajeElimnarSolicitud(Solicitud solicitud, Aspirante aspirante) {


        String mensajeCorreoAceptado = Notificaciones.MENSAJE_RECHAZO_Y_ELIMINAR_SOLICITUD_VERIFICACION_DATOS;
        String nombreEstudiante = aspirante.getPersona().getNombres() + " " + aspirante.getPersona().getApellidos();
        String mensaje = mensajeCorreoAceptado.replaceFirst("%1", nombreEstudiante);
        String mensajeCursos = "";
        Collection<MonitoriaAceptada> mons = solicitud.getMonitorias();
        for (MonitoriaAceptada monitoria : mons) {
            String crn = monitoria.getSeccion().getCrn();
            String numSeccion = "" + monitoria.getSeccion().getNumeroSeccion();
            String nombreCurso = monitoria.getSolicitud().getMonitoria_solicitada().getCurso().getNombre();
            String codigoCurso = monitoria.getSolicitud().getMonitoria_solicitada().getCurso().getCodigo();
            if (mensajeCursos.equals("")) {
                mensajeCursos += "Curso: " + codigoCurso + " - " + nombreCurso + " Sección " + numSeccion + " CRN " + crn;
            } else {
                mensajeCursos += ", Curso: " + codigoCurso + " - " + nombreCurso + " Sección " + numSeccion + " CRN " + crn;
            }
        }
        mensaje = mensaje.replaceFirst("%2", mensajeCursos + " ");

        return mensaje;

    }
}
