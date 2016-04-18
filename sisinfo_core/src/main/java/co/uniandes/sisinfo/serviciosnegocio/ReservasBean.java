package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
import co.uniandes.sisinfo.entities.DatosContacto;
import co.uniandes.sisinfo.entities.DiaDisponibilidad;
import co.uniandes.sisinfo.entities.DisponibilidadCoordinacion;
import co.uniandes.sisinfo.entities.Reserva;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.DatosContactoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.DiaDisponibilidadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.DisponibilidadCoordinacionFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReservaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Servicios de administración de reservas de citas con La Coordinación
 * @author German Florez, Marcela Morales
 */
@Stateless
public class ReservasBean implements ReservasBeanLocal, ReservasBeanRemote {

    //----------------------------------------------
    // CONSTANTES
    //----------------------------------------------
    public static final int MAXIMA_DURACION_CITA_MINUTOS = 20;
    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Locales
    @EJB
    private ReservaFacadeLocal reservaFacade;
    @EJB
    private DatosContactoFacadeLocal contactoFacade;
    @EJB
    private DisponibilidadCoordinacionFacadeLocal disponibilidadCoordinacionFacade;
    @EJB
    private DiaDisponibilidadFacadeLocal diaFacade;
    @EJB
    private ListaNegraReservaCitasLocal listaNegraReservaCitasLocal;
    //Útiles
    @EJB
    private CorreoRemote correoBean;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    @EJB
    private ConstanteRemote constanteBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorReservaCitas conversor;
    //---------------------------------------
    // Constantes
    //---------------------------------------
    public final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.ReservasBeanRemote";
    public final static String NOMBRE_MODULO = "ReservaCitas";

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------
    /**
     * Constructor de ReservasBean
     */
    public ReservasBean() throws NamingException {
        try {
            parser = new ParserT();
            this.serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            conversor = new ConversorReservaCitas(constanteBean);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------------------------------------------
    // MÉTODOS
    //----------------------------------------------
    public String reservarCita(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaReserva = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVA));
            Reserva reserva = getConversor().crearReservaDesdeSecuencia(secuenciaReserva);
            String rol = getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_ROL));
            List<Reserva> reservas = getReservaFacade().buscarReservasPorCorreoYEstado(reserva.getPersona().getCorreo(), getConstanteBean().getConstante(Constantes.VAL_RESERVA_PENDIENTE));

            //Valida que el usuario se un estudiante y que no tenga dos reservas activas para el mismo semestre

            for (Reserva reserva1 : reservas) {
                System.out.println("*********IdReserva:  "+reserva1.getId());
            }

            if (!reservas.isEmpty() && getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE).equals(rol)) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RESERVAR_CITA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_008, new LinkedList<Secuencia>());
            }

            if (listaNegraReservaCitasLocal.consultarEstudianteEnListaNegra(reserva.getPersona().getCorreo()) && getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE).equals(rol)) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RESERVAR_CITA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_012, new LinkedList<Secuencia>());
            }

            try {
                validarReserva(reserva, rol);
            } catch (Exception e) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RESERVAR_CITA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), e.getMessage(), new LinkedList<Secuencia>());
            }
            getReservaFacade().create(reserva);

            //Envía un correo confirmando la creación de la reserva (en caso de que sea estudiante)
            if (getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE).equals(rol)) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ", new Locale("es"));
                String nombrePersona = reserva.getPersona().getNombres() + " " + reserva.getPersona().getApellidos();
                String asuntoCreacion = Notificaciones.ASUNTO_CONFIRMACION_RESERVA_CITA;
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh':'mm a");
                String mensajeCreacion = String.format(Notificaciones.MENSAJE_CONFIRMACION_RESERVA_CITA, nombrePersona, sdf1.format(reserva.getFecha()), sdf2.format(reserva.getInicio()), sdf2.format(reserva.getFin()), reserva.getMotivo(), reserva.getPrograma());
                getCorreoBean().enviarMail(reserva.getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }

            //-----------------------> INICIO TIMERS
            //Se creea/actualiza el timer para avisarle al estudiante que tiene una cita
            getTimerGenerico().eliminarTimerPorParametroExterno("reservaCita-" + reserva.getId());
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(reserva.getInicio());
            calendar.add(Calendar.DATE, -1);
            getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, "manejoTimersReserva", new Timestamp(calendar.getTimeInMillis()), "reservaCita-" + reserva.getId(),
                    NOMBRE_MODULO, this.getClass().getName(), "reservarCita", "Este timer se crea para recordarle al estudiante que tiene una cita con La Coordinación");
            //-----------------------> FIN TIMERS

            // Reviso que exista el timer para completar las reservas
            if (!getTimerGenerico().existeTimerCompletamenteIgual(RUTA_INTERFAZ_REMOTA, "manejoTimersReserva", darFechaManana(), "completarCitas")) {
                refrescarTimerCompletarCita();
            }
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RESERVAR_CITA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_RESERVAR_CITA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public void notificarEstudianteReserva(String id) {
        try {
            Reserva reserva = getReservaFacade().find(Long.parseLong(id));
            if (reserva != null) {
                Persona persona = reserva.getPersona();
                if (persona != null) {
                    String correo = persona.getCorreo();
                    String nombreCompleto = persona.getNombres() + " " + persona.getApellidos();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:SS");
                    String strInicio = sdf.format(new Date(reserva.getInicio().getTime()));
                    String strFin = sdf.format(new Date(reserva.getInicio().getTime()));
                    String fecha = reserva.getFecha().toString() + " de " + strInicio + " a " + strFin;
                    String motivo = reserva.getMotivo();
                    String mensaje = String.format(Notificaciones.MENSAJE_NOTIFICAR_ESTUDIANTE_CITA_COORDINACION, nombreCompleto, fecha, motivo);
                    getCorreoBean().enviarMail(correo, Notificaciones.ASUNTO_NOTIFICAR_ESTUDIANTE_CITA_COORDINACION, null, null, null, mensaje);
                }
            }
        } catch (Exception e) {
            String mensaje = String.format(Notificaciones.MENSAJE_NOTIFICAR_ESTUDIANTE_CITA_COORDINACION_SISINFO, id, e.getMessage());
            getCorreoBean().enviarMail(Constantes.VAL_CORREO_SOPORTE_SISINFO, Notificaciones.ASUNTO_NOTIFICAR_ESTUDIANTE_CITA_COORDINACION_SISINFO, null, null, null, mensaje);
        }
    }

    @Override
    public void manejoTimersReserva(String info) {
        System.out.println(this.getClass() + "Manejo timers reserva " + info);
        String partes[] = info.split("-");
        if (partes[0].equals("reservaCita")) {

            notificarEstudianteReserva(partes[1]);
        } else if (partes[0].equals("completarCitas")) {
            completarCitasPendientes();
            refrescarTimerCompletarCita();
        }
    }

    private void completarCitasPendientes() {
        Collection<Reserva> reservas = reservaFacade.buscarReservasPorEstado(getConstanteBean().getConstante(Constantes.VAL_RESERVA_PENDIENTE));
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        Date fechaHoy = calendar.getTime();
        for (Reserva reserva : reservas) {
            // Se asume que todas las reservas cuya fecha ha expirado han sido cumplidas
            if (reserva.getFecha().before(fechaHoy)) {
                reserva.setEstado(getConstanteBean().getConstante(Constantes.VAL_RESERVA_CUMPLIDA));
            }
        }

    }

    private void refrescarTimerCompletarCita() {
        getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, "manejoTimersReserva", darFechaManana(), "completarCitas", "ReservaCitas", "ReservasBean", "refrescarTimerCompletarCitas", "Timer usado para completar las citas despues de que han ocurrido");
    }

    /**
     * Valida la reserva según las reglas de negocio dadas
     * 1. Valida que la reserva no se efectúe los días sábados o domingos
     * 2. Valida que la reserva se encuentre dentro del horario de coordinación
     * 3. Verifica que no haya una reserva en la franja solicitada
     * 4. Valida la anterioridad de la reserva (1 día antes, antes de medio dia)
     * Si alguna validación falla, lanza una excepción con el mensaje correspondiente
     * @param reserva Reserva a validar
     */
    private void validarReserva(Reserva reserva, String rol) throws Exception {
        Calendar fechaReserva = Calendar.getInstance();
        fechaReserva.setTime(reserva.getFecha());
        Calendar fechaHoy = Calendar.getInstance();
        fechaHoy.setTime(new Date());
        List<DiaDisponibilidad> diasDisponibles = getDiaFacade().findAll();

        //1. Valida que la reserva no se efectúe los días sábados o domingos
        if (fechaReserva.get(Calendar.DAY_OF_WEEK) == 0 || fechaReserva.get(Calendar.DAY_OF_WEEK) == 6) {
            throw new Exception(Mensajes.RESERVA_ERR_002);
        }

        //2. Valida que la reserva se encuentre dentro del horario de coordinación
        boolean coincide = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        Calendar fechaInicio = Calendar.getInstance();
        fechaInicio.setTimeInMillis(reserva.getInicio().getTime());
        Calendar fechaFin = Calendar.getInstance();
        fechaFin.setTimeInMillis(reserva.getFin().getTime());
        int horaInicio = fechaInicio.get(Calendar.HOUR_OF_DAY);
        int horaFin = fechaFin.get(Calendar.HOUR_OF_DAY);
        int minutoInicio = fechaInicio.get(Calendar.MINUTE);
        int minutoFin = fechaFin.get(Calendar.MINUTE);

        for (DiaDisponibilidad dia : diasDisponibles) {
            if (fechaReserva.get(Calendar.DAY_OF_WEEK) != dia.getNumeroDia() + 2) {
                // El dia de la reserva no coincide con el de la franja
                continue;
            }

            // La reserva esta dentro de la franja horaria?
            if (horaInicio >= dia.getHoraInicio() && horaFin <= dia.getHoraFin()) {

                // Si las horas de inicio son iguales, verifico que los minutos sean consistentes
                if (horaInicio == dia.getHoraInicio()) {
                    if (minutoInicio < dia.getMinutoInicio()) {
                        continue;
                    }
                }
                // Si las horas de fin son iguales, verifico que los minutos sean consistentes
                if (horaFin == dia.getHoraFin()) {
                    if (minutoFin > dia.getMinutoFin()) {
                        continue;
                    }
                }
                // Si cumple todas las condiciones propuestas, la reserva es valida dentro de los horarios definidos
                coincide = true;
                break;
            }
        }
        if (!coincide) {
            throw new Exception(Mensajes.RESERVA_ERR_002);
        }
        //3. Verifica que no haya una reserva en la franja solicitada
        Reserva replica = getReservaFacade().buscarReservasInterseccionHoras(reserva.getFecha(), reserva.getInicio(), reserva.getFin());
        if (replica != null) {
            throw new Exception(Mensajes.RESERVA_ERR_003);
        }

        //4. Valida la anterioridad de la reserva (1 día antes, antes de medio dia)
        int diferenciaAnho = fechaReserva.get(Calendar.YEAR) - fechaHoy.get(Calendar.YEAR);
        int diferenciaMes = fechaReserva.get(Calendar.MONTH) - fechaHoy.get(Calendar.MONTH);
        int diferenciaDia = fechaReserva.get(Calendar.DATE) - fechaHoy.get(Calendar.DATE);

        if (reserva.getFecha().before(new Date())) {
            //La cita se solicitó para un tiempo pasado
            throw new Exception(Mensajes.RESERVA_ERR_004);
        }
        if (getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE).equals(rol)) {
            if (diferenciaAnho >= 0 && diferenciaMes >= 0) {
                if (diferenciaDia == 0) {
                    throw new Exception(Mensajes.RESERVA_ERR_005);
                } else if (diferenciaDia == 1) {
                    if (fechaHoy.get(Calendar.HOUR_OF_DAY) > 12) {
                        throw new Exception(Mensajes.RESERVA_ERR_006);
                    }
                }
            }
        }
    }

    public String consultarReservas(String xml) {
        try {
            Collection<Reserva> reservas = getReservaFacade().findAll();
            Secuencia secReservas = getConversor().construirSecuenciaReservas(reservas);

            ArrayList<Secuencia> secuenciasRespuesta = new ArrayList<Secuencia>();
            secuenciasRespuesta.add(secReservas);
            return getParser().generarRespuesta(secuenciasRespuesta, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String guardarHorarioCoordinacion(String xml) {
        try {
            getParser().leerXML(xml);
            //Elimina el horario existente
            getDisponibilidadCoordinacionFacade().removeAll();
            getDiaFacade().removeAll();

            //Crea el nuevo horario
            Secuencia secuenciahorario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO));
            getConversor().crearDisponibilidadesCoordinacionDesdeSecuencia(secuenciahorario);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_HORARIO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_HORARIO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarHorarioCoordinacion(String xml) {
        try {
            Collection<DisponibilidadCoordinacion> disponibilidades = getDisponibilidadCoordinacionFacade().findAll();
            Secuencia secuenciahorario = getConversor().crearSecuenciaDisponibilidadesCoordinacion(disponibilidades);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secuenciahorario);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_HORARIO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_HORARIO_COORDINACION), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String cancelarReserva(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaReserva = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVA));
            Secuencia secId = secuenciaReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_RESERVA));

            Reserva reserva = getReservaFacade().find(secId.getValorLong());
            if (reserva == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_007, new ArrayList<Secuencia>());
            }
            //Valida que la cancelación sea máximo un día antes de la reserva
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reserva.getFecha().getTime());
            int diaReserva = calendar.get(Calendar.DATE);
            Calendar hoy = Calendar.getInstance();
            hoy.setTimeInMillis(System.currentTimeMillis());
            int diaHoy = hoy.get(Calendar.DATE);
            calendar.add(Calendar.DATE, -1);
            if (hoy.after(calendar)) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_009, new ArrayList<Secuencia>());
            }
            Persona secretario = getConversor().buscarSecretarioCoordinacion();
            Persona persona = reserva.getPersona();
            getReservaFacade().remove(reserva);

            int numDiasNotificacion;
            try {
                String diasNotificacion = getConstanteBean().getConstante(Constantes.VAL_RESERVA_CITAS_NUMERO_DIAS_NOTIFICACION);
                numDiasNotificacion = Integer.parseInt(diasNotificacion);
            } catch (NumberFormatException nre) {
                numDiasNotificacion = 7;// Se asume una semana por defecto
            }


            String rol = getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_ROL));
            if (getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE).equals(rol) && diaReserva - diaHoy <= numDiasNotificacion) {
                String asuntoCreacion = Notificaciones.ASUNTO_CANCELACION_RESERVA;
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ", new Locale("es"));
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh':'mm a");
                String nombresSecretario = secretario.getNombres() + " " + secretario.getApellidos();
                String nombresPersona = persona.getNombres() + " " + persona.getApellidos();
                String mensajeCreacion = String.format(Notificaciones.MENSAJE_CANCELACION_RESERVA, nombresSecretario, nombresPersona, sdf1.format(reserva.getFecha()), sdf2.format(reserva.getInicio()), sdf2.format(reserva.getFin()));
                getCorreoBean().enviarMail(secretario.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarReservasPersona(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaLogin = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LOGIN));
            Collection<Reserva> reservas = getReservaFacade().buscarReservasActualesPorLogin(secuenciaLogin.getValor());
            System.out.println("tamaño" + reservas.size() + "tamaño");
            Secuencia secReservas = getConversor().construirSecuenciaReservas(reservas);

            ArrayList<Secuencia> respuesta = new ArrayList<Secuencia>();
            respuesta.add(secReservas);
            return getParser().generarRespuesta(respuesta, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarReservasActuales(String xml) {
        try {
            getParser().leerXML(xml);

            Calendar calHoy = Calendar.getInstance();
            calHoy.setTime(new Date());

            Calendar calInicio = Calendar.getInstance();
            calInicio.set(calHoy.get(Calendar.YEAR), calHoy.get(Calendar.MONTH) - 1, 0, 0, 0);
            Timestamp inicio = new Timestamp(calInicio.getTimeInMillis());

            Calendar calFin = Calendar.getInstance();
            calFin.set(calHoy.get(Calendar.YEAR), calHoy.get(Calendar.MONTH) + 1, 0, 0, 0);
            Timestamp fin = new Timestamp(calFin.getTimeInMillis());

            Collection<Reserva> reservas = getReservaFacade().buscarReservasRango(inicio, fin);
            Secuencia secReservas = getConversor().construirSecuenciaReservas(reservas);

            ArrayList<Secuencia> respuesta = new ArrayList<Secuencia>();
            respuesta.add(secReservas);
            return getParser().generarRespuesta(respuesta, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_ACTUALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_ACTUALES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String modificarReserva(String xml) {
        try {
            getParser().leerXML(xml);

            Secuencia secuenciaReserva = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVA));
            Secuencia id = secuenciaReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_RESERVA));
            Reserva reserva = getReservaFacade().find(Long.valueOf(id.getValor()));
            if (reserva == null) {
                return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_RESERVA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_007, new ArrayList<Secuencia>());
            }

            Secuencia motivo = secuenciaReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MOTIVO_CITA));
            if (motivo != null) {
                reserva.setMotivo(motivo.getValor());
            }
            Secuencia programa = secuenciaReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_CITA));
            if (programa != null) {
                reserva.setPrograma(programa.getValor());
            }
            Secuencia comentarios = secuenciaReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_CITA));
            if (comentarios != null) {
                reserva.setComentarios(comentarios.getValor());
            }

            DatosContacto contacto = reserva.getContacto();
            Secuencia nombre = secuenciaReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CONTACTO));
            if (nombre != null) {
                contacto.setNombre(nombre.getValor());
            }
            Secuencia celular = secuenciaReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR_CONTACTO));
            if (celular != null) {
                contacto.setCelular(celular.getValor());
            }
            getContactoFacade().edit(contacto);
            reserva.setContacto(contacto);
            getReservaFacade().edit(reserva);

            int numDiasNotificacion;
            try {
                String diasNotificacion = getConstanteBean().getConstante(Constantes.VAL_RESERVA_CITAS_NUMERO_DIAS_NOTIFICACION);
                numDiasNotificacion = Integer.parseInt(diasNotificacion);
            } catch (NumberFormatException nre) {
                numDiasNotificacion = 7;// Se asume una semana por defecto
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reserva.getFecha().getTime());
            int diaReserva = calendar.get(Calendar.DATE);
            Calendar hoy = Calendar.getInstance();
            hoy.setTimeInMillis(System.currentTimeMillis());
            int diaHoy = hoy.get(Calendar.DATE);


            Persona secretario = getConversor().buscarSecretarioCoordinacion();
            Persona persona = reserva.getPersona();

            String rol = getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_ROL));
            if (getConstanteBean().getConstante(Constantes.ROL_ESTUDIANTE).equals(rol) && diaReserva - diaHoy <= numDiasNotificacion) {
                String asuntoCreacion = Notificaciones.ASUNTO_CANCELACION_RESERVA;
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ", new Locale("es"));
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh':'mm a");
                String nombresSecretario = secretario.getNombres() + " " + secretario.getApellidos();
                String nombresPersona = persona.getNombres() + " " + persona.getApellidos();
                String mensajeCreacion = String.format(Notificaciones.MENSAJE_CANCELACION_RESERVA, nombresSecretario, nombresPersona, sdf1.format(reserva.getFecha()), sdf2.format(reserva.getInicio()), sdf2.format(reserva.getFin()));
                getCorreoBean().enviarMail(secretario.getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }

            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_RESERVA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_RESERVA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

    }

    public String consultarDatosContacto(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia usuario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO));
            Secuencia secuenciaNombreCompleto = usuario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CONTACTO));
            Secuencia secuenciaLogin = usuario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LOGIN));

            String correo = secuenciaLogin.getValor() + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
            DatosContacto datosContacto = getContactoFacade().buscarContactoPorNombre(secuenciaNombreCompleto.getValor());
            if (datosContacto == null) {
                Persona persona = getConversor().buscarPersonaPorLogin(correo);
                if (persona != null) {
                    datosContacto = new DatosContacto();
                    datosContacto.setNombre(secuenciaNombreCompleto.getValor());
                    datosContacto.setCelular(persona.getCelular());
                    getContactoFacade().create(datosContacto);
                }
            }
            Secuencia secUsuario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO), "");
            secUsuario.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR_CONTACTO), datosContacto.getCelular()));
            secUsuario.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CONTACTO), datosContacto.getNombre()));

            ArrayList<Secuencia> respuesta = new ArrayList<Secuencia>();
            respuesta.add(secUsuario);
            return getParser().generarRespuesta(respuesta, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_DATOS_CONTACTO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarReservasDia(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia fecha = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA));
            Secuencia ano = fecha.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANO_CITA));
            Secuencia mes = fecha.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MES_CITA));
            Secuencia dia = fecha.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIA_CITA));

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(ano.getValorInt(), mes.getValorInt(), dia.getValorInt());
            Timestamp fechaTS = new Timestamp(calendar.getTimeInMillis());
            Collection<Reserva> reservas = getReservaFacade().buscarReservasFecha(fechaTS);
            Secuencia secReservas = getConversor().construirSecuenciaReservas(reservas);

            ArrayList<Secuencia> respuesta = new ArrayList<Secuencia>();
            respuesta.add(secReservas);
            return getParser().generarRespuesta(respuesta, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_DIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_DIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String guardarEstadoReservas(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaReservas = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_RESERVAS));
            for (Secuencia secReserva : secuenciaReservas.getSecuencias()) {
                Secuencia estado = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_CITA));
                Secuencia id = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_RESERVA));
                Reserva reserva = getReservaFacade().find(id.getValorLong());
                if (reserva == null) {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_007, new ArrayList<Secuencia>());
                }
                reserva.setEstado(estado.getValor());
                getReservaFacade().edit(reserva);
                if (estado.getValor().equals(getConstanteBean().getConstante(Constantes.VAL_RESERVA_NO_CUMPLIDA))) {
                    //Se agrega el estudiante a lista negra
                    listaNegraReservaCitasLocal.agregarEstudianteAListaNegra(reserva.getPersona().getCorreo(), reserva.getFecha());
                }
            }
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    /**
     * Retorna la fecha de mañana a las 0:00 para ser usada por el timer
     * @return
     */
    private Timestamp darFechaManana() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public String consultarRangoReservas(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia fechaI = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
            Secuencia ano = fechaI.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANO_CITA));
            Secuencia mes = fechaI.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MES_CITA));
            Secuencia dia = fechaI.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIA_CITA));

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(ano.getValorInt(), mes.getValorInt(), dia.getValorInt());
            Timestamp fechaITS = new Timestamp(calendar.getTimeInMillis());

            Secuencia fechaF = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
            ano = fechaF.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANO_CITA));
            mes = fechaF.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MES_CITA));
            dia = fechaF.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIA_CITA));

            calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(ano.getValorInt(), mes.getValorInt(), dia.getValorInt());
            Timestamp fechaFTS = new Timestamp(calendar.getTimeInMillis());


            Collection<Reserva> reservas = getReservaFacade().buscarReservasRango(fechaITS, fechaFTS);
            Secuencia secReservas = getConversor().construirSecuenciaReservas(reservas);

            ArrayList<Secuencia> respuesta = new ArrayList<Secuencia>();
            respuesta.add(secReservas);
            return getParser().generarRespuesta(respuesta, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());

        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_DIA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ADM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String cancelarGrupoReservas(String xml) {
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaReservas = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_RESERVAS));
            Secuencia motivo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MOTIVO_CANCELACION));
            for (Secuencia secReserva : secuenciaReservas.getSecuencias()) {
                Secuencia id = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_RESERVA));
                Reserva reserva = getReservaFacade().find(id.getValorLong());
                if (reserva == null) {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_007, new ArrayList<Secuencia>());
                }
                reserva.setEstado("Cancelado por Coordinación");
                getReservaFacade().edit(reserva);
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ", new Locale("es"));
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh':'mm a");
                String asuntoCreacion = Notificaciones.ASUNTO_CANCELACION_RESERVA_GRUPO;
                String nombresPersona = reserva.getPersona().getNombres() + " " + reserva.getPersona().getApellidos();
                String mensajeCreacion = String.format(Notificaciones.MENSAJE_CANCELACION_RESERVA_GRUPO, nombresPersona, sdf1.format(reserva.getFecha()), sdf2.format(reserva.getInicio()), sdf2.format(reserva.getFin()),motivo.getValor());
                getCorreoBean().enviarMail(reserva.getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);

            }
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_GUARDAR_ESTADO_RESERVAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    //----------------------------------------------
    // ATRIBUTOS
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

    private DatosContactoFacadeLocal getContactoFacade() {
        return contactoFacade;
    }

    private CorreoRemote getCorreoBean() {
        return correoBean;
    }

    private DiaDisponibilidadFacadeLocal getDiaFacade() {
        return diaFacade;
    }

    private DisponibilidadCoordinacionFacadeLocal getDisponibilidadCoordinacionFacade() {
        return disponibilidadCoordinacionFacade;
    }

    private ReservaFacadeLocal getReservaFacade() {
        return reservaFacade;
    }

    private ConversorReservaCitas getConversor() {
        if (conversor == null) {
            conversor = new ConversorReservaCitas(getConstanteBean());
        }
        return conversor;
    }

    private TimerGenericoBeanRemote getTimerGenerico() {
        return timerGenerico;
    }
}
