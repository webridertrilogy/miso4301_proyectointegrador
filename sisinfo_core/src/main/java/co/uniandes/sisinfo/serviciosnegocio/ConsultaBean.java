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
import co.uniandes.sisinfo.entities.RangoFechas;
import co.uniandes.sisinfo.entities.Regla;
import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.MonitoriaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReglaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.CursoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Administración de consultas
 */
@Stateless
@EJB(name = "ConsultaBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ConsultaLocal.class)
public class ConsultaBean implements ConsultaRemote, ConsultaLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * SeccionFacade
     */
    @EJB
    private SeccionFacadeRemote seccionFacade;
    /**
     * AspiranteFacade
     */
    @EJB
    private AspiranteFacadeLocal aspiranteFacade;
    /**
     * Parser
     */
    private ParserT parser;
    /**
     *  MonitoriaFacade
     */
    @EJB
    private MonitoriaFacadeLocal monitoriaFacade;
    /**
     *  RangoFechas
     */
    @EJB
    private RangoFechasBeanLocal rangosFechasBean;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    @EJB
    private ReglaFacadeRemote reglaFacade;

    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ConsultaBean
     */
    public ConsultaBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
            reglaFacade = (ReglaFacadeRemote) serviceLocator.getRemoteEJB(ReglaFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ConsultaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------

    @Override
    public boolean verificarConflicto(String correo, String crn_seccion) {
        Seccion sec = getSeccionFacade().findByCRN(crn_seccion);
        Collection<Sesion> horarioSeccion = sec.getHorarios();
        Aspirante asp = getAspiranteFacade().findByCorreo(correo);
        // Si el aspirante no existe, no hay conflicto
        if (asp == null) {
            return false;
        }
        Horario_Disponible horarioEstudiante = asp.getHorario_disponible();

        for (Sesion sesion : horarioSeccion) {
            if (verificarConflictoSesionHorario(sesion, horarioEstudiante))
                return true;
        }
        return false;
    }

    @Override
    public String darHorarioEstudiantePorLogin(String comando) {
        String respuesta = null;
        String login = null;
        Collection<Secuencia> secuencias = new Vector<Secuencia>();
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            login = correo.split("@")[0];
            Aspirante aspirante = getAspiranteFacade().findByCorreo(correo);
            if (aspirante == null) {
                try {
                    Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                    Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), login);
                    Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE);
                    secParametro.agregarAtributo(atrParametro);
                    parametros.add(secParametro);
                    respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HORARIO_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0071, parametros);
                    return respuesta;
                } catch (Exception ex) {
                    try {
                        Collection<Secuencia> parametros = new LinkedList<Secuencia>();
                        respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HORARIO_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, parametros);
                    } catch (Exception ex2) {
                        Logger.getLogger(SolicitudBean.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                }
            }
            Collection<Secuencia> parametros = new LinkedList<Secuencia>();
            Secuencia secParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), login);
            Atributo atrParametro = new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE);
            secParametro.agregarAtributo(atrParametro);
            parametros.add(secParametro);
            Collection<Secuencia> seqHorario = darHorarioEstudiante(aspirante);
            respuesta = getParser().generarRespuesta(seqHorario, getConstanteBean().getConstante(Constantes.CMD_DAR_HORARIO_ESTUDIANTE_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0010, parametros);
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ConsultaBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public String modificarHorario(String comando) {
        
        try {
            //Verificar que los plazos sean el A o el B para permitir la modificación de horarios
            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
            RangoFechas rangoVerificacion = rangosFechasBean.consultarRangoFechaPorNombre(getConstanteBean().getConstante(Constantes.VAL_RANGO_FECHAS_GENERAL));
            Timestamp fechaFinVerificacion = rangoVerificacion.getFechaFin();
            if(fechaActual.after(fechaFinVerificacion))
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0073, new Vector());
                
            getParser().leerXML(comando);
            ConversorServiciosSoporteProcesos conversor = new ConversorServiciosSoporteProcesos();
            Collection<DiaCompleto> diasCompletos = conversor.pasarSecuenciaADiasCompletos(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO)));
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Aspirante aspirante = getAspiranteFacade().findByCorreo(correo);
            if(aspirante==null)
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0071, new Vector());
            Horario_Disponible hd = new Horario_Disponible();
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_LUNES), diasCompletos);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_MARTES), diasCompletos);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_MIERCOLES), diasCompletos);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_JUEVES), diasCompletos);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_VIERNES), diasCompletos);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_SABADO), diasCompletos);
            verificarExisteDia(getConstanteBean().getConstante(Constantes.VAL_ATR_DIA_DOMINGO), diasCompletos);
            hd.setDias_disponibles(diasCompletos);
            aspirante.setHorario_disponible(hd);


            //Para modificar el horario
            getAspiranteFacade().edit(aspirante);

            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_HORARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0114, new Vector());

        } catch (Exception ex) {
            Logger.getLogger(ConsultaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Retorna una colección de secuencias con el horario de un aspirante dado
     * @param aspirante Aspirante
     * @return Colección de secuencias con la respuesta a la consulta
     */
    private Collection<Secuencia> darHorarioEstudiante(Aspirante aspirante) {

        Collection<DiaCompleto> dias = aspirante.getHorario_disponible().getDias_disponibles();
        ConversorServiciosSoporteProcesos conversor = new ConversorServiciosSoporteProcesos();
        Secuencia seqHorario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO), getConstanteBean().getConstante(Constantes.NULL));
        Collection<Secuencia> secuencias = new Vector<Secuencia>();
        if (!dias.isEmpty()) {
            seqHorario = conversor.pasarDiasCompletosASecuenciaSinMonitorias(dias);
            secuencias.add(seqHorario);
        }
        
        // Consultamos horario ocupado del aspirante por monitorias.
        List<MonitoriaAceptada> listAceptada =
                getMonitoriaFacade().findByCodigoEstudiante(aspirante.getPersona().getCodigo());
        if (!listAceptada.isEmpty()) {

            seqHorario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIOS_MONITORIAS), getConstanteBean().getConstante(Constantes.NULL));
            for (MonitoriaAceptada aceptada : listAceptada) {

                seqHorario.agregarSecuencia(
                        conversor.pasarSesionesASecuencia(
                            aceptada.getSeccion().getHorarios()));
            }
            secuencias.add(seqHorario);
        }
        return secuencias;
    }

    public boolean verificarConflictoSesionHorario(Sesion sesion, Horario_Disponible horarioDisponible){
        Collection<DiaCompleto> diasSesion = sesion.getDias();
        Collection<DiaCompleto> diasHorario = horarioDisponible.getDias_disponibles();
        HashMap<String,DiaCompleto> map = new HashMap();
        for (DiaCompleto diaCompleto : diasHorario) {
            map.put(diaCompleto.getDia_semana(),diaCompleto);
        }
        for (DiaCompleto diaCompleto : diasSesion) {
            DiaCompleto dia2 = map.get(diaCompleto.getDia_semana());
            if(dia2 != null && verificarConflictoEntreDias(diaCompleto, dia2))
                return true;
        }
        return false;
    }

    public boolean verificarConflictoEntreDias(DiaCompleto dia1, DiaCompleto dia2){
        // Si los dos dias no son en el mismo dia de la semana no hay conflicto
        if(!dia1.getDia_semana().equals(dia2.getDia_semana())){
            return false;
        }
        String horas1 = dia1.getHoras();
        String horas2 = dia2.getHoras();
        for (int i = 0; i < horas1.length(); i++) {
            char h1 = horas1.charAt(i);
            char h2 = horas2.charAt(i);
            // Si los dos dias tienen una hora ocupada en comun entonces hay conflicto
            if(h1 != '0' && h2 != '0')
                return true;
        }
        return false;
    }

    /**
     * Retorna AspiranteFacade
     * @return aspiranteFacade AspiranteFacade
     */
    private AspiranteFacadeLocal getAspiranteFacade() {
        return aspiranteFacade;
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
     * Retorna SeccionFacade
     * @return seccionFacade SeccionFacade
     */
    private SeccionFacadeRemote getSeccionFacade() {
        return seccionFacade;
    }

    /**
     * Retorna MonitoriaFacade
     * @return monitoriaFacade MonitoriaFacade
     */
    private MonitoriaFacadeLocal getMonitoriaFacade() {
        return monitoriaFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }



    private void verificarExisteDia(String diaSemana, Collection<DiaCompleto> dias){

        boolean found = false;
        for (DiaCompleto diaCompleto : dias) {
            if(diaCompleto.getDia_semana().equals(diaSemana)){
                found = true;
                break;
            }
        }
        if(!found){
            String horas = "";
            for (int i = 0; i < 48; i++) {
                horas+="0";
            }
            DiaCompleto dia = new DiaCompleto();
            dia.setHoras(horas);
            dia.setDia_semana(diaSemana);
            dias.add(dia);
        }
    }

    private String darMensajeReglasMonitorias(){
        String respuesta= "";
        Regla reglaPromedio = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_PROMEDIO_TOTAL));
        Regla reglaNumeroMonitorias = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_NUMERO_MAXIMO_MONITORIAS));
        Regla reglaExtracreditos = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_PUEDE_EXTRACREDITARSE));
        Regla reglaInferior = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL_PROMEDIO_INFERIOR_CUATRO));
        Regla reglasuperior = reglaFacade.findByNombre(getConstanteBean().getConstante(Constantes.REGLA_CREDITOS_SEMESTRE_ACTUAL));
        respuesta = String.format(Notificaciones.MENSAJE_REGLAS_MONITORIAS,reglaPromedio.getValor(),reglaNumeroMonitorias.getValor(),reglaExtracreditos.getValor(),reglaInferior.getValor(),reglasuperior.getValor());

        return respuesta;

    }

    @Override
    public String darMensajeReglasMonitorias(String xml){
        try{
            getParser().leerXML(xml);
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MENSAJE_REGLAS),darMensajeReglasMonitorias());
            Collection<Secuencia> secuencias = new ArrayList();
            secuencias.add(sec);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_MENSAJE_REGLAS_MONITORIAS),  getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0195, new Vector());
        } catch (Exception ex) {
            Logger.getLogger(ConsultaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
