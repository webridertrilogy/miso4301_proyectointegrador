/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Candidato;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.Votacion;
import co.uniandes.sisinfo.entities.Votante;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CandidatoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaMultipleFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.VotacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.VotanteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author da-naran
 */
@Stateless
@EJB(name = "VotacionesBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.VotacionesLocal.class)
public class VotacionesBean implements VotacionesRemote, VotacionesLocal {

    public final static String NOMBRE_MODULO = "Votaciones";
    public final static String METODO_MANEJO_TIMERS = "manejoTimers";
    //---------------------------------------
    // Atributos
    //---------------------------------------
    private ParserT parser;
    @EJB
    private CandidatoFacadeLocal candidatoFacade;
    @EJB
    private VotanteFacadeLocal votanteFacade;
    @EJB
    private VotacionFacadeLocal votacionFacade;
    private ServiceLocator serviceLocator;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private TareaMultipleFacadeRemote tareaFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private TareaMultipleRemote tareaMultipleBean;
    @EJB
    private PeriodicidadFacadeRemote periodicidadFacade;
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    @EJB
    private TareaSencillaFacadeRemote tareaSencillaFacade;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de VotacionesBean
     */
    public VotacionesBean() {
        try {
            serviceLocator = new ServiceLocator();
            tareaFacade = (TareaMultipleFacadeRemote) serviceLocator.getRemoteEJB(TareaMultipleFacadeRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            tareaMultipleBean = (TareaMultipleRemote) serviceLocator.getRemoteEJB(TareaMultipleRemote.class);
            periodicidadFacade = (PeriodicidadFacadeRemote) serviceLocator.getRemoteEJB(PeriodicidadFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            tareaSencillaFacade = (TareaSencillaFacadeRemote) serviceLocator.getRemoteEJB(TareaSencillaFacadeRemote.class);
            parser = new ParserT();
        } catch (NamingException ex) {
            Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Genera una nueva votación
     * @param comando
     * @return
     */
    public String crearVotacion(String comando) {
        try {
            String respuesta = "";
            parser.leerXML(comando);
            Secuencia vot = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION));
            String nombre = vot.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            String descripcion = vot.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor();


            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fechaInicioDate = sdfHMS.parse(vot.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO)).getValor());
            String strFechaFin = vot.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN)).getValor();
            Date fechaFinDate = sdfHMS.parse(strFechaFin);


            Timestamp fechaI = new Timestamp(fechaInicioDate.getTime());
            Timestamp fechaF = new Timestamp(fechaFinDate.getTime());

            Votacion votacion = new Votacion();
            votacion.setNombre(nombre);
            votacion.setDescripcion(descripcion);
            votacion.setFechaInicio(fechaI);
            votacion.setFechaFin(fechaF);
            if (System.currentTimeMillis() >= fechaI.getTime()) {
                votacion.setAbierta(true);
            } else {
                votacion.setAbierta(false);
            }
            votacionFacade.create(votacion);

            //ajam

            HashSet<Candidato> candidatos = new HashSet<Candidato>();
            HashSet<Votante> votantes = new HashSet<Votante>();

            Secuencia secCandidatos = vot.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATOS));
            Collection<Secuencia> secsCand = secCandidatos.getSecuencias();
            for (Secuencia s : secsCand) {
                String correo = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();

                Persona pers = personaFacade.findByCorreo(correo);

                if (pers != null) {
                    Candidato candidato = new Candidato();
                    candidato.setPersona(pers);
                    candidato.setNumeroVotos(0);
                    candidato.setVotacion(votacion);
                    candidatos.add(candidato);
                    candidatoFacade.create(candidato);
                }

            }

            //Creamos como opción de voto El Voto En Blanco
            Candidato candidato = new Candidato();
            candidato.setPersona(null);
            candidato.setNumeroVotos(0);
            candidato.setVotacion(votacion);
            candidatos.add(candidato);

            Secuencia secVotantes = vot.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTES));
            Collection<Secuencia> secsVot = secVotantes.getSecuencias();
            for (Secuencia s : secsVot) {
                String correo = s.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();

                Persona pers = personaFacade.findByCorreo(correo);
                if (pers != null) {
                    Votante votante = new Votante();
                    votante.setPersona(pers);
                    votante.setYaVoto(false);
                    votante.setVotacion(votacion);
                    votantes.add(votante);
                    votanteFacade.create(votante);
                }
            }

            votacion = votacionFacade.find(votacion.getId());
            votacion.setCandidatos(candidatos);
            votacion.setVotantes(votantes);
            votacionFacade.edit(votacion);

            if (votacion.isAbierta()) {
                String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_VOTAR);
                String asunto = Notificaciones.ASUNTO_VOTAR;

                SimpleDateFormat sdfFormatoEsp = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));

                String fechaFinFormatoEsp = sdfFormatoEsp.format(votacion.getFechaFin());

                String mensaje = String.format(Notificaciones.MENSAJE_VOTAR, votacion.getNombre(), fechaFinFormatoEsp);
                String footer = Notificaciones.FOOTER_VOTAR;
                for (Votante v : votantes) {
                    //crear tarea al llegar timer
                    crearTareaVotar(v, tipo, asunto, mensaje, footer, votacion);
                }
            } else {
                    crearTimerAbrirVotacion(votacion);
            }
            //Se crea el timer que cerrará la votación y terminará todas las tareas
            Timestamp timeStamp = new Timestamp(fechaFinDate.getTime());
            timerGenerico.crearTimer2("co.uniandes.sisinfo.serviciosnegocio.VotacionesRemote", "cerrarVotacion", timeStamp, "" + votacion.getId(),
                    "Votaciones", this.getClass().getName(), "crearVotacion", "Este timer se crea cuando se crea una nueva votación para cerrar la votacion en la fecha indicada y termine todas las tareas asociadas");

            respuesta = getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_CREAR_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0001, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0001, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    /**
     * Realiza una votación: Suma un voto al candidato y cambia el booleano del votante.
     * @param comando
     * @return
     */
    public String votar(String comando) {
        try {
            //no revisa fechas o estado votacion
            boolean error = false;
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            getParser().leerXML(comando);
            Secuencia secVotacion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION));
            String idVotacion = secVotacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION)).getValor();
            Secuencia secCandidatos = secVotacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATOS));
            Secuencia secVotantes = secVotacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTES));
            Secuencia secVotante = secVotantes.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTE));
            Secuencia secCandidato = secCandidatos.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATO));
            String correoVotante = secVotante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();


            Long idV = Long.parseLong(idVotacion);
            Votacion votacion = votacionFacade.find(idV);
            if (votacion != null && !votacion.isAbierta()) {
                //no dejar votar
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_VOTAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0011, new LinkedList<Secuencia>());
            }

            String correoCandidato = null;
            Candidato candidato = null;
            Secuencia secCorreo = secCandidato.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null) {
                correoCandidato = secCandidato.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
                candidato = candidatoFacade.findByCorreoYIDVotacion(correoCandidato, idVotacion);
            } else {
                candidato = candidatoFacade.findVotoEnBlancoYIDVotacion(idVotacion);
            }

            Votante votante = votanteFacade.findByCorreoYIDVotacion(correoVotante, idVotacion);

            if (votante != null && candidato != null) {
                votante.setYaVoto(true);
                votanteFacade.edit(votante);
                HashMap<String, String> params = new HashMap();
                params.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION), idVotacion);
                tareaMultipleBean.realizarTareaPorCorreo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_VOTAR),
                        correoVotante, params);

            } else {
                error = true;
            }


            if (error) {
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_VOTAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0002, new ArrayList());
            } else {
                candidato.setNumeroVotos(candidato.getNumeroVotos() + 1);
                candidatoFacade.edit(candidato);

                // Si la persona que acabó de votar fue la última, se debe de cerrar la votación
                if (todosVotaron(idVotacion)) {
                    cerrarVotacion(idVotacion);
                }

                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_VOTAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0002, new ArrayList());
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_VOTAR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0003, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private boolean todosVotaron(String idVotacion) {
        boolean votaron = false;
        Votacion votacion = votacionFacade.find(Long.valueOf(idVotacion));
        int cantVotantes = votacion.getVotantes().size();
        int cantVotaron = 0;
        List<Votante> lista = votanteFacade.findByIdVotacion(idVotacion);
        for (Iterator<Votante> it = lista.iterator(); it.hasNext();) {
            Votante votante = it.next();
            if (votante.isYaVoto()) {
                cantVotaron++;
            }
        }
        if (cantVotaron == cantVotantes) {
            votaron = true;
        }
        return votaron;
    }

    /**
     * Retorna las personas que aún no han votado en alguna de las votaciones.
     * Más concretamente, devuelve votaciones activas que tengan personas sin
     * votar aún; las personas están contenidas dentro de cada votación.
     * activas. Recorre cada una de las votaciones.
     * @param comando
     * @return
     */
    public String darPersonasSinVotacion(String comando) {

        List<Votacion> votaciones = votacionFacade.findVotacionesActivas();
        Collection<Votante> sinVotar = new ArrayList<Votante>();
        Secuencia secVotaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACIONES), "");
        Secuencia secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");
        Secuencia secVotantes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTES), "");
        Secuencia secVotante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTE), "");
        for (Votacion v : votaciones) {
            Collection<Votante> votantes = v.getVotantes();
            for (Votante vot : votantes) {
                if (!vot.isYaVoto()) {
                    sinVotar.add(vot);
                }
            }
            if (!sinVotar.isEmpty()) {
                secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION), Long.toString(v.getId())));
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), v.getNombre()));
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), v.getDescripcion()));

                secVotantes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTES), "");

                for (Votante o : sinVotar) {
                    secVotante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTE), "");
                    secVotante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_COMPLETO), o.getPersona().getNombres() + o.getPersona().getApellidos()));
                    secVotante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), o.getPersona().getCorreo()));
                    secVotantes.agregarSecuencia(secVotante);
                }
                secVotacion.agregarSecuencia(secVotantes);
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), v.getFechaInicio().toString()));
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), v.getFechaFin().toString()));
                if (v.isAbierta()) {
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA)));
                } else {
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_CERRADA)));
                }
                secVotaciones.agregarSecuencia(secVotacion);
            }

        }
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        secuencias.add(secVotaciones);

        String respuesta;
        try {
            respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_PERSONAS_SIN_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0003, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_PERSONAS_SIN_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0004, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    /**
     * Retorna los resultados de las votaciones según el correo de la persona.
     * Únicamente muestra las votaciones en las que la persona se ve involucrada.
     * @param comando
     * @return
     */
    public String darVotacionesPorCorreo(String comando) {
        try {
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Collection<Votacion> votaciones = votacionFacade.findVotacionesPorCorreo(correo);
            if (votaciones != null) {
                Secuencia secVotaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACIONES), "");
                Secuencia secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");
                for (Votacion v : votaciones) {
                    secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");

                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION), Long.toString(v.getId())));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), v.getNombre()));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), v.getDescripcion()));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), v.getFechaInicio().toString()));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), v.getFechaFin().toString()));
                    if (v.isAbierta()) {
                        secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA)));
                    } else {
                        secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_CERRADA)));
                    }

                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CANDIDATOS), Integer.toString(v.getCandidatos().size())));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_VOTANTES), Integer.toString(v.getVotantes().size())));
                    int count = 0;
                    for (Votante vot : v.getVotantes()) {
                        if (vot.isYaVoto()) {
                            count++;
                        }
                    }
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTOS_REALIZADOS), Integer.toString(count)));
                    secVotaciones.agregarSecuencia(secVotacion);
                }
                secuencias.add(secVotaciones);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACIONES_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0004, new ArrayList());

            } else {
                secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACIONES), ""));
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACIONES_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0005, new ArrayList());
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACIONES_POR_CORREO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0005, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    /**
     * Igual que darVotacionesPorCorreo, únicamente que éste retorna todas las votaciones.
     * @param comando
     * @return
     */
    public String darVotaciones(String comando) {
        try {
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();

            Collection<Votacion> votaciones = votacionFacade.findAll();
            if (votaciones != null) {
                Secuencia secVotaciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACIONES), "");
                Secuencia secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");
                for (Votacion v : votaciones) {
                    secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");

                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION), Long.toString(v.getId())));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), v.getNombre()));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), v.getDescripcion()));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), v.getFechaInicio().toString()));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), v.getFechaFin().toString()));
                    if (v.isAbierta()) {
                        secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA)));
                    } else {
                        secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_CERRADA)));
                    }

                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_CANDIDATOS), Integer.toString(v.getCandidatos().size())));
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_VOTANTES), Integer.toString(v.getVotantes().size())));
                    int count = 0;
                    for (Votante vot : v.getVotantes()) {
                        if (vot.isYaVoto()) {
                            count++;
                        }
                    }
                    secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTOS_REALIZADOS), Integer.toString(count)));
                    secVotaciones.agregarSecuencia(secVotacion);
                }
                secuencias.add(secVotaciones);
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0006, new ArrayList());

            } else {
                secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACIONES), ""));
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0007, new ArrayList());
            }

        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACIONES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0006, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    /**
     * 
     * @param comando
     * @return
     */
    public String darVotacionesPorEstado(String comando) {
        return null;
    }

    /**
     * Dado un id retorna detalles de la votación correspondiente con la lista
     * de candidatos.
     * @param comando
     * @return
     */
    public String darVotacionConCandidatos(String comando) {
        try {
            getParser().leerXML(comando);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION)).getValor();
            Votacion votacion = votacionFacade.find(Long.parseLong(id));
            Secuencia secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION), id));
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), votacion.getNombre()));
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), votacion.getDescripcion()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaFin = sdf.format(votacion.getFechaFin());
            String fechaInicio = sdf.format(votacion.getFechaInicio());
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), fechaInicio));
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), fechaFin));
            Secuencia secCandidatos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATOS), "");

            for (Candidato c : votacion.getCandidatos()) {
                Persona p = c.getPersona();
                Secuencia secCandidato = null;
                if (p != null) {
                    secCandidato = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATO), "");
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), p.getNombres()));
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), p.getApellidos()));
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), p.getCorreo()));
                } else {
                    secCandidato = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATO), "");
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), getConstanteBean().getConstante(Constantes.NULL)));
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), getConstanteBean().getConstante(Constantes.NULL)));
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), getConstanteBean().getConstante(Constantes.NULL)));
                }

                secCandidatos.agregarSecuencia(secCandidato);
            }
            secVotacion.agregarSecuencia(secCandidatos);
            if (votacion.isAbierta()) {
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA)));
            } else {
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_CERRADA)));
            }
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secVotacion);
            String respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACION_CON_CANDIDATOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0008, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACION_CON_CANDIDATOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0007, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    /**
     * Retorna el número de votos de cada candidato, únicamente si la votación
     * está cerrada. De lo contrario manda únicamente los datos de los candidatos
     * sin el número de votos.
     * @param comando
     * @return
     */
    public String darResultadoVotacion(String comando) {
        try {
            getParser().leerXML(comando);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION)).getValor();
            Votacion votacion = votacionFacade.find(Long.parseLong(id));
            Secuencia secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), votacion.getNombre()));
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), votacion.getDescripcion()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaFin = sdf.format(votacion.getFechaFin());
            String fechaInicio = sdf.format(votacion.getFechaInicio());
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), fechaInicio));
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), fechaFin));
            Secuencia secCandidatos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATOS), "");

            for (Candidato c : votacion.getCandidatos()) {
                Persona p = c.getPersona();
                Secuencia secCandidato = null;
                if (p != null) {
                    secCandidato = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATO), "");
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), p.getNombres()));
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), p.getApellidos()));
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), p.getCorreo()));
                    if (!votacion.isAbierta()) {
                        secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_VOTOS), Integer.toString(c.getNumeroVotos())));
                    }
                } else {
                    secCandidato = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CANDIDATO), "");
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), getConstanteBean().getConstante(Constantes.NULL)));
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), getConstanteBean().getConstante(Constantes.NULL)));
                    secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), getConstanteBean().getConstante(Constantes.NULL)));
                    if (!votacion.isAbierta()) {
                        secCandidato.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_VOTOS), Integer.toString(c.getNumeroVotos())));
                    }
                }

                secCandidatos.agregarSecuencia(secCandidato);
            }
            secVotacion.agregarSecuencia(secCandidatos);
            if (votacion.isAbierta()) {
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA)));
            } else {
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_CERRADA)));
            }
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secVotacion);
            String respuesta = "";
            if (!votacion.isAbierta()) {
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0009, new ArrayList());
            } else {
                respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0010, new ArrayList());
            }
            return respuesta;
        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_RESULTADOS_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0008, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darEstadoVotantesPorIdVotacion(String comando) {
        try {
            getParser().leerXML(comando);
            String idVotacion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION)).getValor();
            Votacion votacion = votacionFacade.find(Long.parseLong(idVotacion));
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            if (votacion != null) {
                Secuencia secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), getConstanteBean().getConstante(Constantes.NULL));
                Collection<Votante> votantes = votacion.getVotantes();
                Secuencia secDescripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), votacion.getDescripcion());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFin = sdf.format(votacion.getFechaFin());
                String fechaInicio = sdf.format(votacion.getFechaInicio());
                Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), fechaFin);
                Secuencia secFechaInicio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), fechaInicio);
                Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), votacion.getNombre());
                Secuencia secIsAbierta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), votacion.isAbierta() ? Constantes.ESTADO_ABIERTA : Constantes.ESTADO_CERRADA);
                secVotacion.agregarSecuencia(secDescripcion);
                secVotacion.agregarSecuencia(secFechaInicio);
                secVotacion.agregarSecuencia(secFechaFin);
                secVotacion.agregarSecuencia(secNombre);
                secVotacion.agregarSecuencia(secIsAbierta);
                Secuencia secVotantes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTES), getConstanteBean().getConstante(Constantes.NULL));
                for (Iterator<Votante> it = votantes.iterator(); it.hasNext();) {
                    Votante votante = it.next();
                    Secuencia secVotante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTE), getConstanteBean().getConstante(Constantes.NULL));
                    Secuencia secIdVotante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTANTE), "" + votante.getId());
                    Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), votante.getPersona().getNombres());
                    Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), votante.getPersona().getApellidos());
                    Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), votante.getPersona().getCorreo());
                    Secuencia secIsYaVoto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_YA_VOTO), Boolean.toString(votante.isYaVoto()));
                    secVotante.agregarSecuencia(secIdVotante);
                    secVotante.agregarSecuencia(secNombres);
                    secVotante.agregarSecuencia(secApellidos);
                    secVotante.agregarSecuencia(secCorreo);
                    secVotante.agregarSecuencia(secIsYaVoto);
                    secVotantes.agregarSecuencia(secVotante);
                }
                secVotacion.agregarSecuencia(secVotantes);
                secuencias.add(secVotacion);
            }
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_ESTADO_VOTANTES_POR_ID_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0011, new LinkedList<Secuencia>());
        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_ESTADO_VOTANTES_POR_ID_VOTACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0009, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public String darVotacionConVotantes(String comando) {
        try {
            getParser().leerXML(comando);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION)).getValor();
            Votacion votacion = votacionFacade.find(id);
            Secuencia secVotacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTACION), "");
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), votacion.getNombre()));
            secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), votacion.getDescripcion()));
            Secuencia secVotantes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTES), "");

            for (Votante c : votacion.getVotantes()) {
                Persona p = c.getPersona();
                Secuencia secVotante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VOTANTE), "");
                secVotante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), p.getNombres()));
                secVotante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), p.getApellidos()));
                secVotante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), p.getCorreo()));
                secVotante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_YA_VOTO), Boolean.toString(c.isYaVoto())));

                secVotantes.agregarSecuencia(secVotante);
            }
            secVotacion.agregarSecuencia(secVotantes);
            if (votacion.isAbierta()) {
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA)));
            } else {
                secVotacion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), getConstanteBean().getConstante(Constantes.ESTADO_CERRADA)));
            }
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secVotacion);
            String respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACION_CON_CANDIDATOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.VOT_MSJ_0012, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            try {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_VOTACION_CON_CANDIDATOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.VOT_ERR_0010, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(VotacionesBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public void cerrarVotacion(String idVotacion) {
        long id = Long.parseLong(idVotacion);
        Votacion votacion = votacionFacade.find(id);
        if (votacion != null) {
            votacion.setAbierta(false);
            votacionFacade.edit(votacion);
        }

        //Cambia todas las tareas pendientes a vencidas
        HashMap<String, String> props = new HashMap();


        props.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION), idVotacion);

        String estadoVencida = getConstanteBean().getConstante(Constantes.ESTADO_TAREA_VENCIDA);
        Collection<TareaMultiple> listaTareas = tareaFacade.findByTipo(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_VOTAR));
        for (TareaMultiple tareaMultiple : listaTareas) {
            Collection<TareaSencilla> tareasSencillas = tareaMultiple.getTareasSencillas();
            for (TareaSencilla tareaSencilla : tareasSencillas) {
                Collection<Parametro> parametros = tareaSencilla.getParametros();
                for (Parametro parametro : parametros) {
                    if (parametro.getCampo().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION))
                            && parametro.getValor().equals(idVotacion)) {
                        tareaSencilla.setEstado(estadoVencida);
                        tareaSencillaFacade.edit(tareaSencilla);
                        break;
                    }
                }
            }
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    private void crearTareaVotar(Votante v, String tipo, String asunto, String mensaje, String footer, Votacion votacion) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Persona p = v.getPersona();

        String categoria = getConstanteBean().getConstante(Constantes.ROL_TODOS);

        HashMap<String, String> parametros = new HashMap<String, String>();
        parametros.put(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_VOTACION), Long.toString(votacion.getId()));
        String cmd = getConstanteBean().getConstante(Constantes.CMD_VOTAR);

        if (p.getNombres() != null) {
            mensaje = mensaje.replaceFirst("%", p.getNombres());
        } else {
            mensaje = mensaje.replaceFirst("%", "votante");
        }
        mensaje = mensaje.replaceFirst("%", votacion.getNombre());
        mensaje = mensaje.replaceFirst("%", votacion.getFechaFin().toString());


        String header = String.format(Notificaciones.HEADER_VOTAR, p.getNombres());
        String asuntoCreacion = Notificaciones.ASUNTO_VOTACION_CREADA;
        String mensajeCreacion = Notificaciones.MENSAJE_VOTACION_CREADA;
        mensajeCreacion = mensajeCreacion.replaceFirst("%", p.getNombres());
        mensajeCreacion = mensajeCreacion.replaceFirst("%", votacion.getNombre());
        mensajeCreacion = mensajeCreacion.replaceFirst("%", votacion.getDescripcion());
        mensajeCreacion = mensajeCreacion.replaceFirst("%", sdf.format(votacion.getFechaFin()));
        tareaMultipleBean.crearTareaPersona(mensaje, tipo, p.getCorreo(), true, header, footer, votacion.getFechaInicio(), votacion.getFechaFin(), cmd, categoria, parametros, asunto);
        //tareaBean.crearTarea(nombreV, descripcion, sdfHMS.format(fechaFinDate), p.getNombres() + " " + p.getApellidos(), categoria, p.getCorreo(), parametros, tipo, cmd, asuntoCreacion, mensajeCreacion, valPeriodicidad);
        correoBean.enviarMail(p.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
    }

    private void crearTimerAbrirVotacion(Votacion votacion) {
        String abrirVotacion = getConstanteBean().getConstante(Constantes.CMD_TIMER_ABRIR_VOTACION) + "-" + votacion.getId();
        timerGenerico.crearTimer2(VotacionesRemote.class.getCanonicalName(), METODO_MANEJO_TIMERS, votacion.getFechaInicio(), abrirVotacion, NOMBRE_MODULO, VotacionesBean.class.getName(), "crearTimerAbrirVotacion", "Timer creado para abrir una votación en su fecha de inicio");
    }

    public void manejoTimers(String parametro) {       
        String[] parametros = parametro.split("-");
        if (parametros[0].equals(getConstanteBean().getConstante(Constantes.CMD_TIMER_ABRIR_VOTACION))) {
            procedimientoAbrirVotacion(Long.parseLong(parametros[1]));
        }
    }

    private void procedimientoAbrirVotacion(Long id) {

        Votacion votacion = votacionFacade.find(id);
        if (votacion != null) {
            votacion.setAbierta(true);
            votacionFacade.edit(votacion);

            String tipo = getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_VOTAR);
            String asunto = Notificaciones.ASUNTO_VOTAR;
            SimpleDateFormat sdfHMS = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ' a las ' hh':'mm a ", new Locale("es"));

            String mensaje = String.format(Notificaciones.MENSAJE_VOTAR, votacion.getNombre(), sdfHMS.format(votacion.getFechaFin()));
            String footer = Notificaciones.FOOTER_VOTAR;
            Collection<Votante> votantes = votacion.getVotantes();
            for (Votante v : votantes) {
                //crear tarea al llegar timer
                crearTareaVotar(v, tipo, asunto, mensaje, footer, votacion);
            }
        }

    }
}
