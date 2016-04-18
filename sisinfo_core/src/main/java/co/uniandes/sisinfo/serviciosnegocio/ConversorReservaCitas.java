package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.DatosContacto;
import co.uniandes.sisinfo.entities.DiaDisponibilidad;
import co.uniandes.sisinfo.entities.DisponibilidadCoordinacion;
import co.uniandes.sisinfo.entities.ListaNegraReservaCitas;
import co.uniandes.sisinfo.entities.Reserva;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.DatosContactoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.DiaDisponibilidadFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.DisponibilidadCoordinacionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 * Conversor del módulo de Asistencias Graduadas y Bolsa de Empleo
 * @author Marcela Morales
 */
public class ConversorReservaCitas {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    //Útil
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ServiceLocator serviceLocator;
    //Servicios
    @EJB
    private DatosContactoFacadeRemote datosContacto;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private DisponibilidadCoordinacionFacadeRemote disponibilidadCoordinacionFacade;
    @EJB
    private DiaDisponibilidadFacadeRemote diaFacade;

    //----------------------------------------------
    // CONSTRUCTOR
    //----------------------------------------------

    /**
     * Conversor del módulo de reserva de citas
     * @param constanteBean Referencia a los servicios de las constantes
     */
    public ConversorReservaCitas(ConstanteRemote constanteBean) {
        try {
            serviceLocator = new ServiceLocator();
            this.constanteBean = constanteBean;
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            datosContacto = (DatosContactoFacadeRemote) serviceLocator.getRemoteEJB(DatosContactoFacadeRemote.class);
            disponibilidadCoordinacionFacade = (DisponibilidadCoordinacionFacadeRemote) serviceLocator.getRemoteEJB(DisponibilidadCoordinacionFacadeRemote.class);
            diaFacade = (DiaDisponibilidadFacadeRemote) serviceLocator.getRemoteEJB(DiaDisponibilidadFacadeRemote.class);

        } catch (NamingException ex) {
            Logger.getLogger(ConversorReservaCitas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A SECUENCIAS
    //----------------------------------------------
     /**
     * Crea una secuencia dado un conjunto de reservas
     * @param reservas Colección de reservas
     * @return Secuencia construída a partir de la colección de reservas dada
     */
    public Secuencia construirSecuenciaReservas(Collection<Reserva> reservas) {
        Secuencia secReservas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_RESERVAS), "");
        for (Reserva reserva : reservas) {
            Secuencia secReserva = construirSecuenciaReserva(reserva);
            secReservas.agregarSecuencia(secReserva);
        }
        return secReservas;
    }

    /**
     * Crea una secuencia dada una reserva
     * @param reserva Reserva
     * @return Secuencia construída a partir de la reserva dada
     */
    public Secuencia construirSecuenciaReserva(Reserva reserva) {
        Secuencia secReserva = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_RESERVA), "");

        Calendar fechaReserva = Calendar.getInstance();
        fechaReserva.setTime(reserva.getFecha());
        Secuencia fecha = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CITA), "");
        fecha.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIA_CITA), fechaReserva.get(Calendar.DATE) + ""));
        fecha.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MES_CITA), fechaReserva.get(Calendar.MONTH) + ""));
        fecha.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANO_CITA), (fechaReserva.get(Calendar.YEAR)) + ""));
        secReserva.agregarSecuencia(fecha);

        Calendar fechaInicio = Calendar.getInstance();
        fechaInicio.setTime(reserva.getInicio());
        Secuencia inicio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INICIO_CITA), "");
        inicio.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_CITA), fechaInicio.get(Calendar.HOUR_OF_DAY) + ""));
        inicio.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_CITA), fechaInicio.get(Calendar.MINUTE) + ""));
        secReserva.agregarSecuencia(inicio);

        Calendar fechaFin = Calendar.getInstance();
        fechaFin.setTime(reserva.getFin());
        Secuencia fin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FIN_CITA), "");
        fin.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_CITA), fechaFin.get(Calendar.HOUR_OF_DAY) + ""));
        fin.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_CITA), fechaFin.get(Calendar.MINUTE) + ""));
        secReserva.agregarSecuencia(fin);

        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MOTIVO_CITA), reserva.getMotivo()));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_CITA), reserva.getComentarios()));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_CITA), reserva.getPrograma()));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_CITA), reserva.getEstado()));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_ID_RESERVA), reserva.getId() + ""));

        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CONTACTO), reserva.getContacto().getNombre()));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR_CONTACTO), reserva.getContacto().getCelular()));

        Secuencia usuario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_USUARIO), "");
        usuario.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LOGIN), reserva.getPersona().getCorreo()));
        secReserva.agregarSecuencia(usuario);

        return secReserva;
    }

    /**
     * Crea una secuencia dado un conjunto de disponiblidades de coordinación
     * @param disponibilidades Disponibilidades de coordinación
     * @return Secuencia construída a partir del conjunto de disponibilidades dada
     */
    public Secuencia crearSecuenciaDisponibilidadesCoordinacion(Collection<DisponibilidadCoordinacion> disponibilidades){
        Secuencia secDisponibilidades = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO), "");
        for (DisponibilidadCoordinacion disponibilidad : disponibilidades) {
            Secuencia secDisponibilidad = crearSecuenciaDisponibilidadesCoordinacion(disponibilidad);
            secDisponibilidades.agregarSecuencia(secDisponibilidad);
        }
        return secDisponibilidades;
    }

    /**
     * Crea una secuencia dada una disponiblidad de coordinación
     * @param disponibilidad Disponibilidad de coordinación
     * @return Secuencia construída a partir de la disponibilidad dada
     */
    public Secuencia crearSecuenciaDisponibilidadesCoordinacion(DisponibilidadCoordinacion disponibilidad){
        Secuencia secDisponibilidad = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO_CITAS), "");
        Collection<DiaDisponibilidad> diasbd = disponibilidad.getDisponibilidadDias();
        for (DiaDisponibilidad dia : diasbd) {
            Secuencia secDia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIA_CITA), "");
            secDia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_DIA_CITA), dia.getNumeroDia() + ""));

            Secuencia secInicio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INICIO_CITA), "");
            secInicio.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_CITA), dia.getHoraInicio() + ""));
            secInicio.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_CITA), dia.getMinutoInicio() + ""));
            secDia.agregarSecuencia(secInicio);

            Secuencia secFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FIN_CITA), "");
            secFin.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_CITA), dia.getHoraFin() + ""));
            secFin.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_CITA), dia.getMinutoFin() + ""));
            secDia.agregarSecuencia(secFin);

            secDisponibilidad.agregarSecuencia(secDia);
        }
        return secDisponibilidad;
    }

    //----------------------------------------------
    // MÉTODOS PARA CONVERSIÓN A ENTIDADES
    //----------------------------------------------
    /**
     * Crea una reserva a partir de una secuencia
     * @param secReserva Secuencia
     * @return Reserva construída a partir de la secuencia dada
     */
    public Reserva crearReservaDesdeSecuencia(Secuencia secReserva) {
        Reserva reserva = new Reserva();
        //Información de la reserva
        Secuencia secFecha = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CITA));
        Secuencia secDia = secFecha.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIA_CITA));
        Secuencia secMes = secFecha.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MES_CITA));
        Secuencia secAnho = secFecha.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ANO_CITA));
        Secuencia secInicio = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INICIO_CITA));
        Secuencia secHoraInicio = secInicio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_CITA));
        Secuencia secMinutoInicio = secInicio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_CITA));
        Secuencia secFin = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FIN_CITA));
        Secuencia secHoraFin = secFin.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_CITA));
        Secuencia secMinutoFin = secFin.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_CITA));
        if (secAnho != null && secMes != null && secDia != null) {
            int anoInicio = Integer.parseInt(secAnho.getValor());
            int mesInicio = Integer.parseInt(secMes.getValor());
            int diaInicio = Integer.parseInt(secDia.getValor());
            Calendar calendario = Calendar.getInstance();
            calendario.clear();
            calendario.set(anoInicio , mesInicio, diaInicio);
            Timestamp fecha = new Timestamp(calendario.getTimeInMillis());
            reserva.setFecha(fecha);

            if(secHoraFin != null && secMinutoFin != null && secHoraInicio != null && secMinutoInicio != null){
                int horaInicio = Integer.parseInt(secHoraInicio.getValor());
                int minutoInicio = Integer.parseInt(secMinutoInicio.getValor());
                calendario = Calendar.getInstance();
                calendario.clear();
                calendario.set(anoInicio , mesInicio, diaInicio, horaInicio, minutoInicio);
                Timestamp fechaInicio = new Timestamp(calendario.getTimeInMillis());
                reserva.setInicio(fechaInicio);

                int horaFin = secHoraFin.getValorInt();
                int minutoFin = Integer.parseInt(secMinutoFin.getValor());
                calendario = Calendar.getInstance();
                calendario.clear();
                calendario.set(anoInicio , mesInicio, diaInicio, horaFin, minutoFin);
                Timestamp fechaFin = new Timestamp(calendario.getTimeInMillis());
                reserva.setFin(fechaFin);
            }
        }
        Secuencia secMotivo = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MOTIVO_CITA));
        if (secMotivo != null) {
            reserva.setMotivo(secMotivo.getValor());
        }
        Secuencia secPrograma = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROGRAMA_CITA));
        if (secPrograma != null) {
            reserva.setPrograma(secPrograma.getValor());
        }
        Secuencia secComentarios = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_CITA));
        if (secComentarios != null) {
            reserva.setComentarios(secComentarios.getValor());
        }
        reserva.setEstado(getConstanteBean().getConstante(Constantes.VAL_RESERVA_PENDIENTE));
        reserva.setId(null);

        //Información de la persona que asistirá a la cita (datos de contacto)
        Secuencia secNombre = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CONTACTO));
        Secuencia secCelular = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR_CONTACTO));
        DatosContacto contacto = buscarDatosDeContactoPorNombre(secNombre.getValor());
        if (contacto == null) {
            contacto = new DatosContacto();
            getDatosContacto().create(contacto);
        }
        contacto.setNombre(secNombre.getValor());
        contacto.setCelular(secCelular.getValor());
        getDatosContacto().edit(contacto);
        reserva.setContacto(contacto);

        //Información de la persona que reservó la cita
        Secuencia secUsuario = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LOGIN));
        if(secUsuario != null){
            Persona persona = buscarPersonaPorLogin(secUsuario.getValor());
            reserva.setPersona(persona);
            if(contacto.getNombre().equals(persona.getNombres() + " " + persona.getApellidos())) {
                if(contacto.getCelular() != null && !contacto.getCelular().trim().equals("")) {
                    persona.setCelular(contacto.getCelular());
                    getPersonaFacade().edit(persona);
                }
            }
        }
        return reserva;
    }

    /**
     * Crea un conjunto de disponibilidades a partir de una secuencia
     * @param secDisponibilidad Secuencia
     */
    public void crearDisponibilidadesCoordinacionDesdeSecuencia(Secuencia secDisponibilidad) {
        for (Secuencia secuencia : secDisponibilidad.getSecuencias()) {
            crearDisponibilidadCoordinacion(secuencia);
        }
    }

    /**
     * Crea una disponibilidad a partir de una secuencia
     * @param secDisponibilidad  Secuencia
     */
    public void crearDisponibilidadCoordinacion(Secuencia secDisponibilidad) {
        DisponibilidadCoordinacion disponibilidad = new DisponibilidadCoordinacion();
        disponibilidad.setDisponibilidadDias(new ArrayList<DiaDisponibilidad>());
        disponibilidad.setId(null);
        crearDiasDisponibilidadCoordinacion(secDisponibilidad, disponibilidad);
    }

    /**
     * Crea un conjunto de dias de disponibilidad a partir de una secuencia y los agrega a una disponibilidad
     * @param secDisponibilidad Secuencia
     * @param disponibilidad Disponibilidad dueña de los días
     */
    public void crearDiasDisponibilidadCoordinacion(Secuencia secDisponibilidad, DisponibilidadCoordinacion disponibilidad){
        for (Secuencia secDia : secDisponibilidad.getSecuencias()) {
            DiaDisponibilidad dia = new DiaDisponibilidad();
            Secuencia secInicio = secDia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INICIO_CITA));
            if(secInicio != null){
                Secuencia secHoraInicio = secInicio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_CITA));
                if(secHoraInicio != null)
                    dia.setHoraInicio(Integer.parseInt(secHoraInicio.getValor()));
                Secuencia secMinutoInicio = secInicio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_CITA));
                if(secMinutoInicio != null)
                    dia.setMinutoInicio(Integer.parseInt(secMinutoInicio.getValor()));
            }
            Secuencia secFin = secDia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FIN_CITA));
            if(secFin != null){
                Secuencia secHoraHoraFin = secFin.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_CITA));
                if(secHoraHoraFin != null)
                    dia.setHoraFin(Integer.parseInt(secHoraHoraFin.getValor()));
                Secuencia secMinutoFin = secFin.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_CITA));
                if(secMinutoFin != null)
                    dia.setMinutoFin(Integer.parseInt(secMinutoFin.getValor()));
            }
            Secuencia secnumdia = secDia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_DIA_CITA));
            if(secnumdia != null)
                dia.setNumeroDia(Integer.parseInt(secnumdia.getValor()));
            dia.setId(null);
            //getDiaFacade().create(dia);
            Collection<DiaDisponibilidad> dias = disponibilidad.getDisponibilidadDias();
            dias.add(dia);
            disponibilidad.setDisponibilidadDias(dias);
            
        }
        getDisponibilidadCoordinacionFacade().create(disponibilidad);
    }

    //----------------------------------------------
    // MÉTODOS DE CONSULTA DE ENTIDADES
    //----------------------------------------------
    /**
     * Retorna datos de contacto dado un nombre
     * @param nombre Nombre
     * @return Datos de contacto cuyo nombre es igual al dado
     */
    public DatosContacto buscarDatosDeContactoPorNombre(String nombre) {
        return getDatosContacto().buscarContactoPorNombre(nombre);
    }

    /**
     * Retorna a una persona dado su login
     * @param login Login de la persona
     * @return Persona cuyo login es igual al login dado
     */
    public Persona buscarPersonaPorLogin(String login) {
        return getPersonaFacade().findLikeCorreo(login);
    }

    /**
     * Retorna a la persona secretario de coordinación
     * @return Persona secreatario de coordinación
     */
    public Persona buscarSecretarioCoordinacion() {
        String email = getConstanteBean().getConstante(Constantes.VAL_CORREO_SECRETARIO_COORDINACION_RESERVAS);
        return buscarPersonaPorLogin(email);
    }

    //----------------------------------------------
    // MÉTODOS PRIVADOS
    //----------------------------------------------
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private DatosContactoFacadeRemote getDatosContacto() {
        return datosContacto;
    }

    private PersonaFacadeRemote getPersonaFacade() {
        return personaFacade;
    }

    private DisponibilidadCoordinacionFacadeRemote getDisponibilidadCoordinacionFacade() {
        return disponibilidadCoordinacionFacade;
    }

    private DiaDisponibilidadFacadeRemote getDiaFacade() {
        return diaFacade;
    }

    public Secuencia crearSecuenciaListaNegraReservaCitas(ListaNegraReservaCitas listaNegra){
        Secuencia secListaNegra = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_NEGRA_RESERVA_CITAS),"");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        secListaNegra.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RAZON),listaNegra.getRazon()));
        secListaNegra.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_VENCIMIENTO),sdf.format(listaNegra.getFechaVencimiento())));
        secListaNegra.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INGRESO),sdf.format(listaNegra.getFechaIngreso())));
        secListaNegra.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO),listaNegra.getPersona().getCorreo()));
        secListaNegra.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES),listaNegra.getPersona().getNombres()));
        secListaNegra.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS),listaNegra.getPersona().getApellidos()));
        return secListaNegra;
    }

    public Secuencia crearSecuenciaListasNegrasReservaCitas(Collection<ListaNegraReservaCitas> listas){
        Secuencia secListasNegras = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTAS_NEGRAS_RESERVA_CITAS),"");
        for (ListaNegraReservaCitas listaNegraReservaCitas : listas) {
            Secuencia secListaNegra = crearSecuenciaListaNegraReservaCitas(listaNegraReservaCitas);
            secListasNegras.agregarSecuencia(secListaNegra);
        }
        return secListasNegras;
    }

    public ListaNegraReservaCitas crearListaNegraReservaCitas(Secuencia secuencia){
        ListaNegraReservaCitas lista = new ListaNegraReservaCitas();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String razon = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RAZON)).getValor();
        Date fechaIngreso, fechaVencimiento;
        try{
            fechaVencimiento = sdf.parse(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_VENCIMIENTO)).getValor());
        }catch(Exception e){
            fechaVencimiento = new Date();
        }
        try{
            fechaIngreso = sdf.parse(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INGRESO)).getValor());
        }catch(Exception e){
            fechaIngreso = new Date();
        }
        String correo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
        lista.setRazon(razon);
        lista.setPersona(personaFacade.findByCorreo(correo));
        lista.setFechaIngreso(new Timestamp(fechaIngreso.getTime()));
        lista.setFechaVencimiento(new Timestamp(fechaVencimiento.getTime()));
        return lista;
    }

}
