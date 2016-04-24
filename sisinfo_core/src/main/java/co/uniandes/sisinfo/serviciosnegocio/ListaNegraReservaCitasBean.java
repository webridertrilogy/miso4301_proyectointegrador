/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.ListaNegraReservaCitas;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.ListaNegraReservaCitasFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Asistente
 */
@Stateless
public class ListaNegraReservaCitasBean implements  ListaNegraReservaCitasLocal {

    public final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.ListaNegraReservaCitasLocal";
    public final static String NOMBRE_MODULO = "ReservaCitas";
    public final static String METODO_TIMERS = "manejoTimersListaNegraReservaCitas";
    @EJB
    private ConstanteLocal constanteBean;
    private ParserT parser;
    private ServiceLocator serviceLocator;
    private ConversorReservaCitas conversor;
    @EJB
    private TimerGenericoBeanLocal timerGenerico;
    @EJB
    private ListaNegraReservaCitasFacadeLocal listaNegraReservaCitasFacade;
    @EJB
    private PersonaFacadeLocal personaFacade;

    public ListaNegraReservaCitasBean() {
//        try {
//            parser = new ParserT();
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            conversor = new ConversorReservaCitas(constanteBean);
//            timerGenerico = (TimerGenericoBeanLocal) serviceLocator.getLocalEJB(TimerGenericoBeanLocal.class);
//            personaFacade = (PersonaFacadeLocal) serviceLocator.getLocalEJB(PersonaFacadeLocal.class);
//        } catch (Exception e) {
//            Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, e);
//        }
    }

    public String crearListaNegraReservaCitas(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secuenciaLista = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_NEGRA_RESERVA_CITAS));
            ListaNegraReservaCitas lista = getConversor().crearListaNegraReservaCitas(secuenciaLista);
            Timestamp hoy = new Timestamp(new Date().getTime());
            if (hoy.after(lista.getFechaVencimiento())) {
                // La fecha de vencimiento es anterior a la fecha actual
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_010, new LinkedList<Secuencia>());
            }
            // Se crea el timer para sacar a la persona de la lista negra
            Long idTimer = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, METODO_TIMERS, lista.getFechaVencimiento(), "listaNegraReservaCitas:" + lista.getPersona().getCorreo(),
                    NOMBRE_MODULO, this.getClass().getName(), "eliminarEstudianteListaNegraReservaCitas", "Este timer se crea para eliminar a un estudiante de la lista negra una vez llega el vencimiento de esta fecha");
            lista.setIdTimer(idTimer);
            listaNegraReservaCitasFacade.create(lista);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String editarListaNegraReservaCitas(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secuenciaLista = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_NEGRA_RESERVA_CITAS));
            ListaNegraReservaCitas listaNueva = getConversor().crearListaNegraReservaCitas(secuenciaLista);
            Timestamp hoy = new Timestamp(new Date().getTime());
            if (hoy.after(listaNueva.getFechaVencimiento())) {
                // La fecha de vencimiento es anterior a la fecha actual
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_010, new LinkedList<Secuencia>());
            }

            ListaNegraReservaCitas lista = listaNegraReservaCitasFacade.findByCorreo(listaNueva.getPersona().getCorreo());

            if(lista== null){
                // El estudiante no se encuentra en la lista negra
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_011, new LinkedList<Secuencia>());
            }

            // Se elimina el timer anterior
            getTimerGenerico().eliminarTimer(lista.getIdTimer());

            // Se crea el timer para sacar a la persona de la lista negra
            long idTimer = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, METODO_TIMERS, listaNueva.getFechaVencimiento(), "listaNegraReservaCitas:" + listaNueva.getPersona().getCorreo(),
                    NOMBRE_MODULO, this.getClass().getName(), "eliminarEstudianteListaNegraReservaCitas", "Este timer se crea para eliminar a un estudiante de la lista negra una vez llega el vencimiento de esta fecha");

            lista.setFechaIngreso(listaNueva.getFechaIngreso());
            lista.setFechaVencimiento(listaNueva.getFechaVencimiento());
            lista.setIdTimer(idTimer);
            lista.setRazon(listaNueva.getRazon());
            //listaNegraReservaCitasFacade.create(listaNueva);
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, e);
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String eliminarListaNegraReservaCitas(String comando) {
        try {
            getParser().leerXML(comando);
            Secuencia secLista = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_NEGRA_RESERVA_CITAS));
            String correo = secLista.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            ListaNegraReservaCitas lista = listaNegraReservaCitasFacade.findByCorreo(correo);
            if(lista != null){
                long idTimer =lista.getIdTimer();
                if(idTimer != -1)
                    timerGenerico.eliminarTimer(idTimer);
                listaNegraReservaCitasFacade.remove(lista);
            } else
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_ERR_011, new LinkedList<Secuencia>());
            return getParser().generarRespuesta(new ArrayList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, e);
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String consultarListaNegraReservaCitas(String comando) {
        try {
            Collection<ListaNegraReservaCitas> listas = listaNegraReservaCitasFacade.findAll();
            Secuencia secuenciaListas = getConversor().crearSecuenciaListasNegrasReservaCitas(listas);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secuenciaListas);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, e);
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LISTA_NEGRA_RESERVA_CITAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0002, new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ListaNegraReservaCitasBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    public void setConstanteBean(ConstanteLocal constanteBean) {
        this.constanteBean = constanteBean;
    }

    public ConversorReservaCitas getConversor() {
        return conversor;
    }

    public void setConversor(ConversorReservaCitas conversor) {
        this.conversor = conversor;
    }

    public ParserT getParser() {
        return parser;
    }

    public void setParser(ParserT parser) {
        this.parser = parser;
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public ListaNegraReservaCitasFacadeLocal getListaNegraReservaCitasFacade() {
        return listaNegraReservaCitasFacade;
    }

    public void setListaNegraReservaCitasFacade(ListaNegraReservaCitasFacadeLocal listaNegraReservaCitasFacade) {
        this.listaNegraReservaCitasFacade = listaNegraReservaCitasFacade;
    }

    public TimerGenericoBeanLocal getTimerGenerico() {
        return timerGenerico;
    }

    public void setTimerGenerico(TimerGenericoBeanLocal timerGenerico) {
        this.timerGenerico = timerGenerico;
    }

    public boolean consultarEstudianteEnListaNegra(String correo) {
        ListaNegraReservaCitas lnrc = listaNegraReservaCitasFacade.findByCorreo(correo);
        return lnrc!=null;
    }

    public void manejoTimersListaNegraReservaCitas(String info) {
        System.out.println("Ingreso a Manejo Timers Reserva citas: "+info);
        String[] spInfo = info.split(":");
        String correo = spInfo[1];
        ListaNegraReservaCitas listaNegra = listaNegraReservaCitasFacade.findByCorreo(correo);
        if(listaNegra != null)
            listaNegraReservaCitasFacade.remove(listaNegra);
    }

    @Override
    public void agregarEstudianteAListaNegra(String correo,Timestamp fechaReserva) {
        ListaNegraReservaCitas listaNegraAnterior = listaNegraReservaCitasFacade.findByCorreo(correo);
        if(listaNegraAnterior!=null)
            listaNegraReservaCitasFacade.remove(listaNegraAnterior);
        ListaNegraReservaCitas listaNegra = new ListaNegraReservaCitas();
        Persona estudiante = personaFacade.findByCorreo(correo);
        listaNegra.setPersona(estudiante);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String razon = String.format(Notificaciones.RAZON_INGRESO_AUTOMATICO_LISTA_NEGRA_RESERVA_CITAS,sdf.format(fechaReserva));
        listaNegra.setRazon(razon);
        listaNegra.setFechaIngreso(new Timestamp(new Date().getTime()));
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(getConstanteBean().getConstante(Constantes.VAL_RESERVA_CITAS_NUMERO_DIAS_EN_LISTA_NEGRA)));

        listaNegra.setFechaVencimiento(new Timestamp(calendar.getTimeInMillis()));

        // Se crea el timer para sacar a la persona de la lista negra
        Long idTimer = getTimerGenerico().crearTimer2(RUTA_INTERFAZ_REMOTA, "manejoTimersListaNegraReservaCitas", listaNegra.getFechaVencimiento(), "listaNegraReservaCitas:" + correo,
                    NOMBRE_MODULO, this.getClass().getName(), "eliminarEstudianteListaNegraReservaCitas", "Este timer se crea para eliminar a un estudiante de la lista negra una vez llega el vencimiento de esta fecha");
        listaNegra.setIdTimer(idTimer);
        listaNegraReservaCitasFacade.create(listaNegra);




    }



}
