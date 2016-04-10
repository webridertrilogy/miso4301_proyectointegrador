/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.bo.AccionBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Autorizado;
import co.uniandes.sisinfo.entities.Elemento;
import co.uniandes.sisinfo.entities.Encargado;
import co.uniandes.sisinfo.entities.HorarioDia;
import co.uniandes.sisinfo.entities.Laboratorio;
import co.uniandes.sisinfo.entities.ReservaInventario;
import co.uniandes.sisinfo.entities.ReservaMultiple;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.EncargadoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.LaboratorioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReservaInventarioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReservaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Asistente
 */
@Stateless
public class ReservaInventarioBean implements ReservaInventarioBeanRemote, ReservaInventarioBeanLocal {

    private final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.ReservaInventarioBeanRemote";
    private final static String NOMBRE_METODO_TIMER = "manejoTimersReservaInventario";
    private ServiceLocator serviceLocator;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ReservaInventarioFacadeLocal reservaInventarioFacade;
    @EJB
    private LaboratorioFacadeLocal laboratorioFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private EncargadoFacadeLocal encargadoFacade;
    @EJB
    private ReservaMultipleFacadeLocal reservaMultipleFacade;
    @EJB
    private CorreoRemote correoBean;
    private ConversorReservaInventario conversor;
    private TimerGenericoBeanRemote timerGenericoBean;
    private ParserT parser;

    public ReservaInventarioBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
            timerGenericoBean = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);

        } catch (NamingException ex) {
            Logger.getLogger(ReservaInventarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public void setConstanteBean(ConstanteRemote constanteBean) {
        this.constanteBean = constanteBean;
    }

    public ParserT getParser() {
        return parser;
    }

    public void setParser(ParserT parser) {
        this.parser = parser;
    }

    public ConversorReservaInventario getConversor() {
        if (conversor == null) {
            conversor = new ConversorReservaInventario(constanteBean, laboratorioFacade, personaFacade, encargadoFacade, reservaMultipleFacade);
        }
        return conversor;
    }

    @Override
    public String consultarReservasLaboratorio(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String nombreLaboratorio = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_LABORATORIO)).getValor();
            Collection<ReservaInventario> reservas = reservaInventarioFacade.consultarReservasPorNombreLaboratorioyEstado(nombreLaboratorio, getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE));
            Secuencia secLab = getConversor().consultarReservas(reservas);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secLab);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public String crearReserva(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);

            // Se extrae la informacion de la reserva, los elementos reservados y el horario
            Secuencia secReserva = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVA_INVENTARIO));
            ReservaInventario reservaInventario = getConversor().consultarReserva(secReserva);

            //------------Si es Reserva Multiple ------//
            // revisa si es una reserva multiple

            boolean isReservaMultiple = Boolean.parseBoolean(secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ISMULTIPLE)).getValor());
            //si es una reserva multiple. extrae los datos de la reserva multiple
            if (isReservaMultiple) {
                ReservaMultiple reservaMultiple = getConversor().consultarReservaMultiple(secReserva);
                return crearReservaMultiple(reservaInventario, reservaMultiple);
            }

            //----------Si es Reserva Sencilla--------------------//

            //Se verifica que la reserva a crear sea valida y no se cruce con alguna otra
            String validacion = validarReserva(reservaInventario, null);
            if (!validacion.equals("")) {
                return validacion;
            }

            // Se completan los datos de la reserva y se persiste en el sistema
            guardarReserva(reservaInventario);

            //genera respuesta
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    /**
     * Verifica que una cierta reserva se encuentre dentro de los horarios establecidos para el laboratorio
     * @param reserva La reserva a vreificar
     * @param laboratorio El laboratorio sobre el cual se esta realizando la reserva
     * @return
     */
    private boolean revisarReservaEnLaboratorio(ReservaInventario reserva) {
        Laboratorio laboratorio = reserva.getLaboratorio();

        Collection<HorarioDia> horariosLab = laboratorio.getHorario();

        int tiempoInicio = reserva.getFechaInicio().getHours() * 60 + reserva.getFechaInicio().getMinutes();
        int tiempoFin = reserva.getFechaFin().getHours() * 60 + reserva.getFechaFin().getMinutes();
        for (HorarioDia horarioDia : horariosLab) {

            if (horarioDia.getNumeroDia() != reserva.getFechaReserva().getDay()) {
                continue;
            }
            int horarioInicio = horarioDia.getHoraInicio() * 60 + horarioDia.getMinutoInicio();
            int horarioFin = horarioDia.getHoraFin() * 60 + horarioDia.getMinutoFin();

            if ((horarioInicio <= tiempoInicio) && (tiempoFin <= horarioFin)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica que no existan conflictos entre una reserva nueva y un conjunto de reservas ya existentes en cuestion
     * de horarios
     * @param reservaNueva La nueva reserva
     * @param reservaMultiple: Es la reservaMultiple a la cual pertenece la reservaNueva. En caso de ser una reserva sencilla, este parametro es null
     * @param reservas Las reservas ya existentes
     * @return Retorna "" (vacio) si no existe ningun conflicto con las reservas ya existente. De lo contrario, retorna la fecha de inicio y fin
     * de la primera reserva con la que encuentra que tiene conflicto
     */
    private String revisarHorarioReserva(ReservaInventario reservaNueva, ReservaMultiple reservaMultiple, Collection<ReservaInventario> reservas) {
        for (ReservaInventario reserva : reservas) {
            if (reservaNueva.getId() != null && reserva.getId().equals(reservaNueva.getId())) {
                continue;
            }
            ReservaMultiple rm = reservaMultipleFacade.consultarReservasMultiplesPorReserva(reserva.getId());
            if ((reservaMultiple != null) && (rm != null) && (rm.getId().equals(reservaMultiple.getId()))) {
                continue;
            }

            long inicio1 = reservaNueva.getFechaInicio().getTime();
            long inicio2 = reserva.getFechaInicio().getTime();
            long fin1 = reservaNueva.getFechaFin().getTime();
            long fin2 = reserva.getFechaFin().getTime();

            if (fin1 <= inicio2 || fin2 <= inicio1) {
                //no se cruzan
            } else {
                DateFormat dia = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat hora = new SimpleDateFormat("HH:mm");

                return "el " + dia.format(reserva.getFechaInicio()) + " desde " + hora.format(reserva.getFechaInicio()) + "h hasta " + hora.format(reserva.getFechaFin()) + "h";
            }
        }
        //Retorna "" si no existe ningun conflicto
        return "";
    }

    public String consultarReservasPersona(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String correoPersona = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CREADOR)).getValor() + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
            String estado = getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE);
            Collection<ReservaInventario> reservas = reservaInventarioFacade.consultarReservasByPersonaYEstado(correoPersona, estado);
            Secuencia secLab = getConversor().consultarReservas(reservas);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secLab);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String cancelarReserva(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            Long idReserva = Long.valueOf(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_RESERVA_INVENTARIO)).getValor());
            ReservaInventario reserva = reservaInventarioFacade.find(idReserva);

            //------se busca y elimina el timer de recordatorio-----
            Long idTimerRecordatorio = timerGenericoBean.darIdTimer(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RECORDAR_RESERVA) + "-" + reserva.getId());
            if (idTimerRecordatorio != null) {
                timerGenericoBean.eliminarTimer(idTimerRecordatorio);
            }
            //------se busca y elimina el timer de adminosis------
            Long idTimerAdmonsis = timerGenericoBean.darIdTimer(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_CUENTAS_USUARIO) + "-" + reserva.getId());
            if (idTimerAdmonsis != null) {
                timerGenericoBean.eliminarTimer(idTimerAdmonsis);
            }

            reserva.setEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_CANCELADA));
            reservaInventarioFacade.edit(reserva);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVA_INVENTARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String consultarReserva(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String idReserva = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_RESERVA_INVENTARIO)).getValor();
            ReservaInventario reserva = reservaInventarioFacade.find(Long.valueOf(idReserva));
            Secuencia secReserva = getConversor().consultarReserva(reserva);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secReserva);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVA_INVENTARIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String consultarRangoReservas(String xml) {

        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String strLaboratorio = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_LABORATORIO)).getValor();
            String strFechaInicial = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO)).getValor();
            String strFechaFinal = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN)).getValor();
            String estado = getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE);

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Timestamp fechaI = new Timestamp(formatter.parse(strFechaInicial).getTime());
            Timestamp fechaF = new Timestamp(formatter.parse(strFechaFinal).getTime());

            Collection<ReservaInventario> reservas = reservaInventarioFacade.consultarReservasPorNombreLaboratorioyEstadoyRangoFechas(strLaboratorio, estado, fechaI, fechaF);
            Secuencia secReserva = getConversor().consultarReservas(reservas);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secReserva);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RANGO_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public void manejoTimersReservaInventario(String info) {
        String[] partes = info.split("-");
        if (partes[0].equals(getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_CUENTAS_USUARIO))) {
            Long idReserva = Long.parseLong(partes[1]);
            notificarAdmonsis(idReserva);
        } else if (partes[0].equals(getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_COMPLETAR_RESERVAS))) {
            eliminarTimersCompletarReservas();
            completarReservasVencidas();
            regenerarTimerCompletarReservas();
            Calendar fecha = Calendar.getInstance();
            fecha.add(Calendar.DATE, 1);
            regenerarTimersAdmonsis(fecha.getTime());
            //crear timers de recordatorio diario
            crearTimersRecordatorioReservas();

        } else if (partes[0].equals(getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RECORDAR_RESERVA))) {
            Long idReserva = Long.parseLong(partes[1]);
            enviarRecordatorioReserva(idReserva);
        } else if (partes[0].equals(getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RESERVAS_DEL_DIA))) {
            eliminarTimersCorreoReservasDia();
            enviarCorreoReservasDia();
            regenerarTimerCorreoReservasDia();
        }
    }

    /**
     * Regenera todos los timers para habilitar la cuenta de invitado para las reservas
     * de la fecha que viene por parametro.
     * @param fecha Fecha en la cual se regeneraran los timers
     */
    private void regenerarTimersAdmonsis(Date fecha) {
        Calendar cInicio = Calendar.getInstance();
        cInicio.setTime(fecha);
        cInicio.set(Calendar.HOUR_OF_DAY, 1);
        cInicio.set(Calendar.MINUTE, 0);
        cInicio.set(Calendar.SECOND, 0);
        cInicio.set(Calendar.MILLISECOND, 0);
        Calendar cFin = Calendar.getInstance();
        cFin.setTime(fecha);
        cFin.set(Calendar.HOUR_OF_DAY, 23);
        cFin.set(Calendar.MINUTE, 59);
        cFin.set(Calendar.SECOND, 59);
        cFin.set(Calendar.MILLISECOND, 0);
        Collection<ReservaInventario> reservas = reservaInventarioFacade.consultarReservasPorRangoFechas(new Timestamp(cInicio.getTimeInMillis()), new Timestamp(cFin.getTimeInMillis()));
        for (ReservaInventario reservaInventario : reservas) {
            if (reservaInventario.getLaboratorio().getCuentaInvitado() && reservaInventario.isCuentaInvitado() && reservaInventario.getEstado().equals(getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE))) {
                eliminarTimersNotificarAdmonsis("" + reservaInventario.getId());
                regenerarTimerNotificarAdmonsis(
                        reservaInventario);
            }
        }
    }

    private void notificarAdmonsis(Long id) {
        //Build parameter string

        String[] dias = new String[8];
        dias[1] = "d";
        dias[2] = "l";
        dias[3] = "m";
        dias[4] = "mi";
        dias[5] = "j";
        dias[6] = "v";
        dias[7] = "s";
        String data = "lab=";
        ReservaInventario reserva = reservaInventarioFacade.find(id);
        data = data + reserva.getLaboratorio().getNombreSalaServicio() + "&";
        Calendar c = Calendar.getInstance();
        c.setTime(reserva.getFechaReserva());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        data = data + "dia=" + dias[dayOfWeek] + "&";
        c.setTime(reserva.getFechaInicio());
        int horainicio = c.get(Calendar.HOUR_OF_DAY);
        data = data + "horainicio=" + horainicio + "&";
        c.setTime(reserva.getFechaFin());
        int horafin = c.get(Calendar.HOUR_OF_DAY);
        int minutosFin = c.get(Calendar.MINUTE);
        data = data + "horafin=" + (horafin + (minutosFin == 0 ? 0 : 1)) + "&";
        String correo = reserva.getCreador().getCorreo();
        data = data + "login=" + correo.substring(0, correo.length() - getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO).length());
        try {
            String method = getConstanteBean().getConstante(Constantes.VAL_METODO_HTTP_SERVICIO_ADMONSIS);
            if (method.equals(getConstanteBean().getConstante(Constantes.VAL_METODO_HTTP_POST))) {
                System.out.println("Sent data:\n" + data + "\n using POST");
                // Send the request
                URL url = new URL(getConstanteBean().getConstante(Constantes.VAL_URL_SERVICIO_ADMONSIS));
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                //write parameters
                writer.write(data);
                writer.flush();

                // Get the response
                StringBuilder answer = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line);
                }
                writer.close();
                reader.close();

                //Output the response
                System.out.println("Received data:\n" + answer.toString());
            } else if (method.equals(getConstanteBean().getConstante(Constantes.VAL_METODO_HTTP_GET))) {
                System.out.println("Sent data:\n" + data + "\n using GET");
                // Send the request
                URL url = new URL(getConstanteBean().getConstante(Constantes.VAL_URL_SERVICIO_ADMONSIS) + "?" + data);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                // Get the response
                StringBuilder answer = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line);
                }
                reader.close();

                //Output the response
                System.out.println("Received data:\n" + answer.toString());
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String cancelarGrupoReservas(String xml) {

        try {
            getParser().leerXML(xml);
            Secuencia secuenciaReservas = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_RESERVAS));
            for (Secuencia secReserva : secuenciaReservas.getSecuencias()) {
                Secuencia id = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_RESERVA));
                ReservaInventario reserva = reservaInventarioFacade.find(id.getValorLong());
                if (reserva == null) {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO), Mensajes.RESERVA_ERR_007, new ArrayList<Secuencia>());
                }
                reserva.setEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_CANCELADA));
                //   reservaInventarioFacade.edit(reserva);
//                SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE dd 'de' MMMMM 'de' yyyy ", new Locale("es"));
//                SimpleDateFormat sdf2 = new SimpleDateFormat("hh':'mm a");
//                String asuntoCreacion = Notificaciones.ASUNTO_CANCELACION_RESERVA_GRUPO;
//                String nombresPersona = reserva.getPersona().getNombres() + " " + reserva.getPersona().getApellidos();
//                String mensajeCreacion = String.format(Notificaciones.MENSAJE_CANCELACION_RESERVA_GRUPO, nombresPersona, sdf1.format(reserva.getFecha()), sdf2.format(reserva.getInicio()), sdf2.format(reserva.getFin()),motivo.getValor());
//                getCorreoBean().enviarMail(reserva.getPersona().getCorreo(), asuntoCreacion, null, null, null, mensajeCreacion);
            }
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ReservaInventarioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservaInventarioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String marcarGrupoReservas(String xml) {

        try {
            getParser().leerXML(xml);
            Secuencia secuenciaReservas = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_RESERVAS));
            for (Secuencia secReserva : secuenciaReservas.getSecuencias()) {
                Secuencia id = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_RESERVA));
                ReservaInventario reserva = reservaInventarioFacade.find(id.getValorLong());
                if (reserva == null) {
                    return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO), Mensajes.RESERVA_ERR_007, new ArrayList<Secuencia>());
                }
                reserva.setEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_INCUMPLIDA));
            }
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ReservaInventarioBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CANCELAR_GRUPO_RESERVAS_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReservaInventarioBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private Date construirFechaTimerCorreoReservasDia() {
        Calendar fechaTimer = Calendar.getInstance();
        fechaTimer.add(Calendar.DATE, 1);
        fechaTimer.set(Calendar.HOUR_OF_DAY, 6);
        fechaTimer.set(Calendar.MINUTE, 0);
        return fechaTimer.getTime();
    }

    private Date construirFechaTimerCompletarReservas() {
        Calendar fechaTimer = Calendar.getInstance();
        fechaTimer.add(Calendar.DATE, 1);
        fechaTimer.set(Calendar.HOUR_OF_DAY, 23);
        fechaTimer.set(Calendar.MINUTE, 30);
        return fechaTimer.getTime();
    }

    private void eliminarTimersCorreoReservasDia() {
        timerGenericoBean.eliminarTimers(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RESERVAS_DEL_DIA));
    }

    private void regenerarTimerCorreoReservasDia() {
        timerGenericoBean.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER,
                new Timestamp(construirFechaTimerCorreoReservasDia().getTime()),
                getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RESERVAS_DEL_DIA),
                "ReservaInventario",
                this.getClass().getName(),
                "regenerarTimerCorreoReservasDia",
                "Este timer se crea para enviar un correo a los encargados de cada sala informando cuales son las reservas que se encuentran vigentes para ese dia");
    }

    private void eliminarTimersCompletarReservas() {
        timerGenericoBean.eliminarTimers(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_COMPLETAR_RESERVAS));
    }

    private void eliminarTimersRecordatorioReserva(String idReserva) {
        timerGenericoBean.eliminarTimers(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RECORDAR_RESERVA) + "-" + idReserva);
    }

    private void eliminarTimersNotificarAdmonsis(String idReserva) {
        timerGenericoBean.eliminarTimers(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_CUENTAS_USUARIO) + "-" + idReserva);
    }

    private void regenerarTimerRecordatorioReserva(ReservaInventario reserva) {
        Date fecha = reserva.getFechaInicio();
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(Calendar.HOUR, -2);
        if (!timerGenericoBean.existeTimerCompletamenteIgual(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(c.getTimeInMillis()), getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RECORDAR_RESERVA) + "-" + reserva.getId())) {
            System.out.println(timerGenericoBean.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER,
                    new Timestamp(c.getTimeInMillis()),
                    getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RECORDAR_RESERVA) + "-" + reserva.getId(),
                    "ReservaInventario",
                    this.getClass().getName(),
                    "regenerarTimerRecordatorioReserva",
                    "Este timer se crea para recordarle a un usuario que tiene una reserva pendiente en el sistema"));
        }
    }

    private void regenerarTimerCompletarReservas() {
        timerGenericoBean.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER,
                new Timestamp(construirFechaTimerCompletarReservas().getTime()),
                getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_COMPLETAR_RESERVAS),
                "ReservaInventario",
                this.getClass().getName(),
                "regenerarTimerCompletarReservas",
                "Este timer se crea para cambiar el estado de las reservas vigentes a cumplidas siemre y cuando la fecha ya se haya cumplido");
    }

    private void regenerarTimerNotificarAdmonsis(ReservaInventario reserva) {
        Date fecha = reserva.getFechaInicio();
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(Calendar.HOUR, -1);
        if (!timerGenericoBean.existeTimerCompletamenteIgual(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, new Timestamp(c.getTimeInMillis()), getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_CUENTAS_USUARIO) + "-" + reserva.getId())) {
            timerGenericoBean.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER,
                    new Timestamp(c.getTimeInMillis()),
                    getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_CUENTAS_USUARIO) + "-" + reserva.getId(),
                    "ReservaInventario",
                    this.getClass().getName(),
                    "timerNotificarAdmonsis",
                    "Este timer se crea para notificar el sistema de admonsis sobre la reserva");
        }
    }

    private void enviarCorreoReservasDia() {
        Calendar calendarInicial = Calendar.getInstance();

        Calendar calendarFinal = Calendar.getInstance();
        calendarFinal.set(Calendar.HOUR, 23);
        calendarFinal.set(Calendar.MINUTE, 55);

        HashMap<String, String> mensajes = new HashMap<String, String>();
        Collection<Laboratorio> laboratorios = laboratorioFacade.findAll();
        for (Laboratorio laboratorio : laboratorios) {
            Collection<Encargado> encargados = laboratorio.getEncargados();
            Collection<ReservaInventario> reservas = reservaInventarioFacade.consultarReservasPorNombreLaboratorioyEstadoyRangoFechas(laboratorio.getNombre(),
                    getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE),
                    new Timestamp(calendarInicial.getTimeInMillis()),
                    new Timestamp(calendarFinal.getTimeInMillis()));
            String msjReserva = "";
            for (ReservaInventario reservaInventario : reservas) {
                Collection<Elemento> elementos = reservaInventario.getElementos();
                String msjElementos = "";
                for (Elemento elemento : elementos) {
                    msjElementos += elemento.getNombre() + ",";
                }
                if (msjElementos.isEmpty()) {
                    msjElementos = "Ninguno";
                } else {
                    msjElementos = msjElementos.substring(0, msjElementos.length() - 1);
                }
                String strResponsable = reservaInventario.getResponsable();
                if (strResponsable == null) {
                    strResponsable = reservaInventario.getCreador().getCorreo();
                } else {
                    if (!strResponsable.contains("@")) {
                        strResponsable += getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
                    }
                    Persona pResponsable = personaFacade.findByCorreo(strResponsable);
                    if (pResponsable != null) {
                        strResponsable = pResponsable.getNombres() + " " + pResponsable.getApellidos();
                    }
                }
                SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");
                msjReserva += String.format(Notificaciones.MENSAJE_RESERVAS_VIGENTES_RESERVA, strResponsable, formatHora.format(reservaInventario.getFechaInicio()), formatHora.format(reservaInventario.getFechaFin()), msjElementos);
            }
            if (reservas.isEmpty()) {
                continue;
            }
            String msjSala = String.format(Notificaciones.MENSAJE_RESERVAS_VIGENTES_SALA, laboratorio.getNombre(), msjReserva);
            for (Encargado encargado : encargados) {
                String msj = mensajes.get(encargado.getPersona().getCorreo());
                if (msj != null) {
                    msj += msjSala;
                } else {
                    msj = msjSala;
                }
                mensajes.put(encargado.getPersona().getCorreo(), msj);
            }
        }
        Collection<String> keys = mensajes.keySet();
        for (String string : keys) {
            String mensajeCorreo = String.format(Notificaciones.MENSAJE_RESERVAS_DEL_DIA, mensajes.get(string));
            correoBean.enviarMail(string, Notificaciones.ASUNTO_RESERVAS_DEL_DIA, null, null, null, mensajeCorreo);
        }

    }

    private void completarReservasVencidas() {
        Collection<ReservaInventario> reservas = reservaInventarioFacade.consultarReservasAnterioresAFechaByEstado(new Date(), getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE));
        for (ReservaInventario reservaInventario : reservas) {
            if (reservaInventario.getFechaFin().before(new Date())) {
                reservaInventario.setEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_CUMPLIDA));
                reservaInventarioFacade.edit(reservaInventario);
            }
        }
    }

    private void enviarRecordatorioReserva(long id) {
        ReservaInventario reserva = reservaInventarioFacade.find(id);
        String correoResponsable = reserva.getResponsable();
        if (correoResponsable == null) {
            correoResponsable = reserva.getCreador().getCorreo();
        }
        String nombreSala = reserva.getLaboratorio().getNombre();
        String nombrePersona = correoResponsable;
        if (!correoResponsable.contains("@")) {
            correoResponsable += getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
        }
        Persona persona = personaFacade.findByCorreo(correoResponsable);
        if (persona != null) {
            nombrePersona = persona.getNombres();
        }
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
        String strFecha = sdfFecha.format(new Date(reserva.getFechaReserva().getTime()));
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        String strHoraInicio = sdfHora.format(new Date(reserva.getFechaInicio().getTime()));
        String strHoraFin = sdfHora.format(new Date(reserva.getFechaFin().getTime()));
        String asunto = String.format(Notificaciones.ASUNTO_RECORDATORIO_RESERVA_INVENTARIO, nombreSala);
        String mensaje = String.format(Notificaciones.MENSAJE_RECORDATORIO_RESERVA_INVENTARIO, nombrePersona, nombreSala, strFecha, strHoraInicio, strHoraFin);
        correoBean.enviarMail(correoResponsable, asunto, null, null, null, mensaje);
    }

    public Collection<AccionBO> darAcciones(String rol, String login) {
        Collection<AccionBO> acciones = new ArrayList<AccionBO>();
        if (!login.contains("@")) {
            login += getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
        }
        if (rol.equals(getConstanteBean().getConstante(Constantes.ROL_ADMINISTRADOR_RESERVA_INVENTARIO))) {
            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_CREAR_LABORATORIO,
                    Notificaciones.DESCRIPCION_ACCION_CREAR_LABORATORIO,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_COMANDO_CREAR_LABORATORIO),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_RESERVA_INVENTARIO)));
            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_CONSULTAR_LABORATORIOS,
                    Notificaciones.DESCRIPCION_ACCION_CONSULTAR_LABORATORIOS,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_COMANDO_CONSULTAR_LABORATORIOS),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_RESERVA_INVENTARIO)));
        } else if (rol.equals(getConstanteBean().getConstante(Constantes.ROL_ENCARGADO_LABORATORIO))) {
            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_MARCAR_RESERVAS,
                    Notificaciones.DESCRIPCION_ACCION_MARCAR_RESERVAS,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_COMANDO_MARCAR_RESERVAS),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_RESERVA_INVENTARIO)));
            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_CANCELAR_RESERVAS,
                    Notificaciones.DESCRIPCION_ACCION_CANCELAR_RESERVAS,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_COMANDO_CANCELAR_RESERVAS),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_RESERVA_INVENTARIO)));
        }
        Collection<Laboratorio> labs = laboratorioFacade.findAll();
        boolean autorizado = false;
        CicloLabs:
        for (Laboratorio laboratorio : labs) {
            Collection<Autorizado> autorizados = laboratorio.getAutorizados();
            for (Autorizado aut : autorizados) {
                if (aut.getPersona().getCorreo().equals(login)) {
                    autorizado = true;
                    break CicloLabs;
                }
            }
        }
        if (autorizado || rol.equals(getConstanteBean().getConstante(Constantes.ROL_ADMINISTRADOR_RESERVA_INVENTARIO))
                || rol.equals(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_PLANTA)) || rol.equals(getConstanteBean().getConstante(Constantes.ROL_PROFESOR_CATEDRA))) {
            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_CONSULTAR_MIS_RESERVAS,
                    Notificaciones.DESCRIPCION_ACCION_CONSULTAR_MIS_RESERVAS,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_COMANDO_CONSULTAR_MIS_RESERVAS),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_RESERVA_INVENTARIO)));
            acciones.add(new AccionBO(
                    Notificaciones.NOMBRE_ACCION_CONSULTAR_HORARIO_RESERVA_INVENTARIO,
                    Notificaciones.DESCRIPCION_ACCION_CONSULTAR_HORARIO_RESERVA_INVENTARIO,
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_COMANDO_CONSULTAR_HORARIO_RESERVA_INVENTARIO),
                    getConstanteBean().getConstante(Constantes.VAL_ACCION_SECCION_RESERVA_INVENTARIO)));
        }
        return acciones;
    }

    /**
     * Crea una serie de reservas a partir de una reserva inicial, con una cierta periodicidad y hasta una fecha especifica
     * @param reservaInventario: la reserva inicial a partir de la cual se crean las demas reservas
     * @param reservaMultiple: Contiene la informacion de la reserva multiple a crear/editar (fecha inicio, fin, periodicidad)
     * @return Comando de respuesta con el mensaje de exito de la creacion de las reservas
     * @throws Exception
     */
    private String crearReservaMultiple(ReservaInventario reservaInventario, ReservaMultiple reservaMultiple) throws Exception {

        String respuesta = null;

        //Se verifica que la fecha de inicio sea menor a la fecha final de la reserva multiple, de lo contrario envia un mensaje de error
        if (reservaInventario.getFechaReserva().after(reservaMultiple.getFinalizacionReservaMultiple())) {
            Vector<Secuencia> params = new Vector<Secuencia>();
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_INVENTARIO_ERR_0008, params);
        }

        //-----Se crean todas las reservas segun la periodicidad y hasta la fecha establecida ----//
        Collection<ReservaInventario> reservas = new ArrayList<ReservaInventario>();
        reservas = crearListadeReservas(reservaInventario, reservaMultiple.getFinalizacionReservaMultiple(), reservaMultiple.getPeriodicidad());

        //------Se verifican que todas las reservas que se deben crear sean validas y no se crucen ------///
        for (ReservaInventario reserva : reservas) {
            String validacion = validarReserva(reserva, reservaMultiple);
            if (!validacion.equals("")) {
                return validacion;
            }
        }

        //------busca la reserva con la primera fecha
        Timestamp inicioReservas = reservaInventario.getFechaReserva();
        for (ReservaInventario reserva : reservas) {
            if (reserva.getFechaReserva().before(inicioReservas)) {
                inicioReservas = reserva.getFechaReserva();
            }
        }
        //-----si se esta editando una reserva multiple,
        //se eliminan las reservas antiguas para luego crear las nuevas
        if (reservaMultiple.getId() != null) {
            Collection<ReservaInventario> reservasAntiguas = reservaMultipleFacade.findById(reservaMultiple.getId()).getReservas();
            if (!reservasAntiguas.isEmpty()) {
                for (ReservaInventario reserv : reservasAntiguas) {
                    eliminarReserva(reserv);
                }
            }
        }

        //------Se crean las reservas ------//
        for (ReservaInventario reserva : reservas) {
            // Se completan los datos de la reserva y se persiste en el sistema
            guardarReserva(reserva);
        }
        //-------Se crea la reservaMultiple a la cual perteneceran las reservas
        reservaMultiple.setReservas(reservas);
        if (reservaMultiple.getId() == null) {
            reservaMultipleFacade.create(reservaMultiple);
        } else {
            reservaMultipleFacade.edit(reservaMultiple);
        }

        ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
        respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        return respuesta;
    }

    /**
     * Esta funcion crea todas las reservas de una reserva multiple, a partir de una reserva inicial, la fecha hasta donde se deben generar las
     * reservas y la periodicidad de la misma
     * @param reservaInicial: La primera reserva a partir de la cual se generan las demas reservas (copias con diferente fecha)
     * @param finalizacionReservas:  La fecha hasta la cual se generan las reservas (incluyendo la fecha de finalizacion)
     * @param periodicidadReservaMultiple: se generan dependiendo de la periodiciadad. Ej. diariamente, semanalmente, mensualmente, etc.
     * @return Una lista de las reservas creadas segun los parametros.
     */
    private List<ReservaInventario> crearListadeReservas(ReservaInventario reservaInicial, Date finalizacionReservas, String periodicidadReservaMultiple) {

        List<ReservaInventario> reservas = new ArrayList<ReservaInventario>();

        Date fechaReserva = reservaInicial.getFechaReserva();
        Date fechaini = reservaInicial.getFechaInicio();
        Date fechafin = reservaInicial.getFechaFin();
        while (!fechaReserva.after(finalizacionReservas)) {

            //se copian los parametros de la reserva inicial
            ReservaInventario reserva = new ReservaInventario();
            //reserva.setId(reservaInicial.getId());
            reserva.setMotivo(reservaInicial.getMotivo());
            reserva.setResponsable(reservaInicial.getResponsable());
            reserva.setEstado(reservaInicial.getEstado());
            reserva.setCreador(reservaInicial.getCreador());
            reserva.setLaboratorio(reservaInicial.getLaboratorio());
            reserva.setElementos(reservaInicial.getElementos());
            reserva.setEnviarRecordatorio(reservaInicial.isEnviarRecordatorio());
            reserva.setCuentaInvitado(reservaInicial.isCuentaInvitado());

            //se le asigna la fecha a la reserva
            reserva.setFechaReserva(new Timestamp(fechaReserva.getTime()));
            reserva.setFechaInicio(new Timestamp(fechaini.getTime()));
            reserva.setFechaFin(new Timestamp(fechafin.getTime()));

            //Si la periodicidad es diaria de lunes a viernes
            if (periodicidadReservaMultiple.equals("Diariamente (Lunes - Viernes)")) {

                fechaReserva.setDate(fechaReserva.getDate() + 1);
                fechaini.setDate(fechaini.getDate() + 1);
                fechafin.setDate(fechafin.getDate() + 1);
                //si el dia de la fecha es un sabado, se pasa para el lunes
                if (fechaReserva.getDay() == 6) {
                    fechaReserva.setDate(fechaReserva.getDate() + 2);
                    fechaini.setDate(fechaini.getDate() + 2);
                    fechafin.setDate(fechafin.getDate() + 2);
                }
                //si el dia de la fecha es un domingo, se pasa para el lunes
                if (fechaReserva.getDay() == 0) {
                    fechaReserva.setDate(fechaReserva.getDate() + 1);
                    fechaini.setDate(fechaini.getDate() + 1);
                    fechafin.setDate(fechafin.getDate() + 1);
                } //En caso de que la reserva multiple inicie un sabado, esta primera reserva se ignora
                if (reserva.getFechaReserva().getDay() == 6) {
                    continue;
                }

                // Si la periodicidad es diaria de lunes a sabado
            } else if (periodicidadReservaMultiple.equals("Diariamente (Lunes - Sabado)")) {
                fechaReserva.setDate(fechaReserva.getDate() + 1);
                fechaini.setDate(fechaini.getDate() + 1);
                fechafin.setDate(fechafin.getDate() + 1);

                //si el dia de la fecha es un domingo, se pasa para el lunes
                if (fechaReserva.getDay() == 0) {
                    fechaReserva.setDate(fechaReserva.getDate() + 1);
                    fechaini.setDate(fechaini.getDate() + 1);
                    fechafin.setDate(fechafin.getDate() + 1);
                }

                //Si la periodicidad es semanal
            } else if (periodicidadReservaMultiple.equals("Semanalmente")) {
                fechaReserva.setDate(fechaReserva.getDate() + 7);
                fechaini.setDate(fechaini.getDate() + 7);
                fechafin.setDate(fechafin.getDate() + 7);
                if (fechaReserva.getDay() == 0) {
                    fechaReserva.setDate(fechaReserva.getDate() + 1);
                    fechaini.setDate(fechaini.getDate() + 1);
                    fechafin.setDate(fechafin.getDate() + 1);
                }
                //Si la periodicidad es mensual se crea una reserva cada 4 semanas
            } else if (periodicidadReservaMultiple.equals("Mesualmente")) {

                fechaReserva.setDate(fechaReserva.getDate() + 28);
                fechaini.setDate(fechaini.getDate() + 28);
                fechafin.setDate(fechafin.getDate() + 28);
            }
            //Se debe validar la fecha de la reserva debido a que una reserva podria caer un domingo y ser movida para el siguiente lunes, pero el lunes podria
            //no ser un dia no valido, es decir que excede la fecha de finalizacion de una reserva multiple
            if (reserva.getFechaReserva().getTime() <= finalizacionReservas.getTime()) {
                System.out.println("reserva id " + reserva.getId() + "  " + reserva.getFechaReserva() + "  -  " + reserva.getFechaInicio() + " hasta " + reserva.getFechaFin());
                reservas.add(reserva);
            }
        }
        return reservas;
    }

    /**
     * Valida que una reserva sea correcta (que no se cruce con otras reservas, que la fecha sea valida, etc)
     * @param reserva: La reserva que se desea validad
     * @param reservaMultiple: Es la reservaMultiple a la cual pertenece la reserva. En caso de ser una reserva sencilla, este parametro es null
     * @return: de tipo String. Si la reserva no cumple con alguna de las condiciones, se retorna el comando con el mensaje de error correspondiente,
     * de lo contrarion, si la reserva cumple con todas las condiciones este retorna un String "" (vacio)
     * @throws Exception
     */
    private String validarReserva(ReservaInventario reserva, ReservaMultiple reservaMultiple) throws Exception {

        // Revisar que el laboratorio exista. Si el laboratorio no existe se lanza un mensaje de error.
        if (reserva.getLaboratorio() == null) {
            Vector<Secuencia> params = new Vector<Secuencia>();
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_INVENTARIO_ERR_0001, params);
        } // Se verifica que el laboratorio seleccionado sea reservable
        if (!reserva.getLaboratorio().getReservable() && !getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL)).equals(getConstanteBean().getConstante(Constantes.ROL_ADMINISTRADOR_RESERVA_INVENTARIO))) {
            Vector<Secuencia> params = new Vector<Secuencia>();
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_INVENTARIO_ERR_0006, params);
        } // La reserva debe realizarse para una fecha posterior a la actual
        if (reserva.getFechaInicio().before(new Date())) {
            Vector<Secuencia> params = new Vector<Secuencia>();
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_INVENTARIO_ERR_0004, params);
        }
        String nombreLaboratorio = reserva.getLaboratorio().getNombre();
        //Se consultan las reservas vigentes para ese laboratorio
        Collection<ReservaInventario> reservasFacade = reservaInventarioFacade.consultarReservasPorNombreLaboratorioyEstado(nombreLaboratorio, getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE));
        // Si alguna reserva vigente se cruza con la nueva reserva, entonces esta no puede realizarse
        String conflicto = revisarHorarioReserva(reserva, reservaMultiple, reservasFacade);
        if (!conflicto.equals("")) {
            Vector<Secuencia> params = new Vector<Secuencia>();
            params.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), conflicto));
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_INVENTARIO_ERR_0002, params);
        } // Se verifica que la nueva reserva se encuentre localizada en un espacio valido dentro del horario
        // del laboratorio
        if (!revisarReservaEnLaboratorio(reserva)) {
            Vector<Secuencia> params = new Vector<Secuencia>();
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_CREAR_RESERVA_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_INVENTARIO_ERR_0003, params);
        }
        return "";
    }

    /**
     * Guarda una reserva en el sistema. En caso de que haya sido solicitado, se regenera
     * el timer de enviar recordatorio.
     * En caso de que no existan, se regeneran los timers de reservas del dia y
     * completar reservas.
     * @param reserva: La reserva que se desea guardar
     */
    private void guardarReserva(ReservaInventario reserva) {

        System.out.println("guardarReserva");
        // Se completan los datos de la reserva y se persiste en el sistema
        reserva.setEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE));
        if (reserva.getId() == null) {
            reservaInventarioFacade.create(reserva);
        } else {
            reservaInventarioFacade.edit(reserva);
        }
        eliminarTimersRecordatorioReserva("" + reserva.getId());
        eliminarTimersNotificarAdmonsis("" + reserva.getId());

        if (!timerGenericoBean.existeTimerCompletamenteIgual(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RESERVAS_DEL_DIA))) {
            regenerarTimerCorreoReservasDia();
        }
        if (!timerGenericoBean.existeTimerCompletamenteIgual(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_COMPLETAR_RESERVAS))) {
            regenerarTimerCompletarReservas();
        }
        // Si la reserva se crea en el mismo dia en el que si hizo la reserva, entonces es necesario crear el timer de cuenta de invitado manualmente
        if (reserva.getFechaReserva().before(new Date()) && reserva.getLaboratorio().getCuentaInvitado() && reserva.isCuentaInvitado()) {
            regenerarTimerNotificarAdmonsis(reserva);
        }
        //Si la reserva se crea en el mismo dia en el que si hizo la reserva, Se crea el timer para enviar recordatorio en caso de ser necesario
        if (reserva.getFechaReserva().before(new Date()) && reserva.isEnviarRecordatorio()) {
            regenerarTimerRecordatorioReserva(reserva);
        }
    }

    /**
     * Elimina una reserva de la base de datos y sus timers asociados si existen (timer de recordatorio y de notificacion admonsis)
     * @param reserva: La reserva que se desea eliminar.
     */
    private void eliminarReserva(ReservaInventario reserva) {

        //------se busca y elimina el timer de recordatorio-----
        Long idTimerRecordatorio = timerGenericoBean.darIdTimer(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_RECORDAR_RESERVA) + "-" + reserva.getId());
        if (idTimerRecordatorio != null) {
            timerGenericoBean.eliminarTimer(idTimerRecordatorio);
        }
        //------se busca y elimina el timer de adminosis------
        Long idTimerAdmonsis = timerGenericoBean.darIdTimer(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, getConstanteBean().getConstante(Constantes.VAL_RESERVA_INVENTARIO_TIMER_CUENTAS_USUARIO) + "-" + reserva.getId());
        if (idTimerAdmonsis != null) {
            timerGenericoBean.eliminarTimer(idTimerAdmonsis);
        }
        //------se elimina la reserva-------
        reservaInventarioFacade.remove(reserva);
    }

    /**
     * A partir de un id de una reserva, busca la reserva multiple a la que existe en caso de que pertenesca a alguna
     * @param comandoXML: el id de la reserva sencilla
     * @return: Si existe una reserva multiple la retorna, de lo contrario retorna false 
     */
    public String isMultiple(String comandoXML) {
        String respuesta = null;
        try {
            getParser().leerXML(comandoXML);
            String idReserva = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_RESERVA_INVENTARIO)).getValor();
            ReservaMultiple reservaMultiple = reservaMultipleFacade.consultarReservasMultiplesPorReserva(Long.parseLong(idReserva));

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(getConversor().consultarReservaMultiple(reservaMultiple));
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_RESERVA_ISMULTIPLE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    /**
     * Cancela todas las reserva que se encuentran asociadas a esta reserva multiple
     * @param comandoXML el id de la reserva multiple a la que se le cancelaran sus reservas
     * @return
     */
    public String cancelarReservaMultiple(String comandoXML) {
        String respuesta = null;
        try {
            getParser().leerXML(comandoXML);
            Long idReservaMultiple = Long.valueOf(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ID)).getValor());
            System.out.println(idReservaMultiple);
            ReservaMultiple reservaMultiple = reservaMultipleFacade.findById(idReservaMultiple);
            if (reservaMultiple != null) {
                for (ReservaInventario reserva : reservaMultiple.getReservas()) {
                    reserva.setEstado(getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_CANCELADA));
                }
                reservaMultipleFacade.edit(reservaMultiple);
            }
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CANCELAR_RESERVAMULTIPLE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    /**
     * Consulta todas las reservas vigentes para el siguiente dia y les crea un timer de recordatorio
     */
    private void crearTimersRecordatorioReservas() {

        Date manana = new Date();
        manana.setDate(manana.getDate() + 1);
        Collection<ReservaInventario> reservas = reservaInventarioFacade.consultarReservasAnterioresAFechaByEstado(manana, getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE));
        if (reservas != null) {
            for (ReservaInventario reserva : reservas) {
                if (reserva.isEnviarRecordatorio()) {
                    regenerarTimerRecordatorioReserva(reserva);
                }
            }
        }


    }
}
