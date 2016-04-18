/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import co.uniandes.sisinfo.bo.HoraBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.Autorizado;
import co.uniandes.sisinfo.entities.Elemento;
import co.uniandes.sisinfo.entities.Encargado;
import co.uniandes.sisinfo.entities.HorarioDia;
import co.uniandes.sisinfo.entities.Laboratorio;
import co.uniandes.sisinfo.entities.ReservaInventario;
import co.uniandes.sisinfo.entities.ReservaMultiple;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.EncargadoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.LaboratorioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReservaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.AccesoLDAP;

/**
 *
 * @author Asistente
 */
public class ConversorReservaInventario {

    private ConstanteRemote constanteBean;
    private LaboratorioFacadeLocal laboratorioFacade;
    private PersonaFacadeRemote personaFacade;
    private EncargadoFacadeLocal encargadoFacade;
    private SimpleDateFormat formatoFecha;
    private ReservaMultipleFacadeLocal reservaMultipleFacade;

    public ConversorReservaInventario(ConstanteRemote constanteBean, LaboratorioFacadeLocal laboratorioFacade, PersonaFacadeRemote personaFacade, EncargadoFacadeLocal encargadoFacadeLocal, ReservaMultipleFacadeLocal reservaMultipleFacade) {
        this.constanteBean = constanteBean;
        this.laboratorioFacade = laboratorioFacade;
        this.personaFacade = personaFacade;
        this.encargadoFacade = encargadoFacadeLocal;
        formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.reservaMultipleFacade = reservaMultipleFacade;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public Secuencia consultarLaboratorios(Collection<Laboratorio> laboratorios) {
        Secuencia secLabs = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LABORATORIOS), "");
        for (Laboratorio laboratorio : laboratorios) {
            secLabs.agregarSecuencia(consultarLaboratorio(laboratorio));
        }
        return secLabs;
    }

    public Secuencia consultarLaboratorio(Laboratorio laboratorio) {
        Secuencia secLab = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LABORATORIO), "");
        secLab.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_LABORATORIO), "" + laboratorio.getId()));
        secLab.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), laboratorio.getNombre()));
        secLab.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), laboratorio.getDescripcion()));
        secLab.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVABLE), laboratorio.getReservable().toString()));
        secLab.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVO), laboratorio.getActivo().toString()));

        secLab.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_SALA_SERVICIO), laboratorio.getNombreSalaServicio()));

        if (laboratorio.getCuentaInvitado() == null) {
            laboratorio.setCuentaInvitado(false);
        } else {
            secLab.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA_INVITADO), laboratorio.getCuentaInvitado().toString()));
        }
        secLab.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SALON), laboratorio.getSalon()));
        secLab.agregarSecuencia(consultarHorarios(laboratorio.getHorario()));
        secLab.agregarSecuencia(consultarElementos(laboratorio.getElementos()));
        secLab.agregarSecuencia(consultarAutorizados(laboratorio.getAutorizados()));
        secLab.agregarSecuencia(consultarEncargados(laboratorio.getEncargados()));
        return secLab;
    }

    public Secuencia consultarHorarios(Collection<HorarioDia> horarios) {
        Secuencia secHorario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIOS_LABORATORIO), "");
        for (HorarioDia horarioDia : horarios) {
            secHorario.agregarSecuencia(consultarHorario(horarioDia));
        }
        return secHorario;
    }

    public Secuencia consultarHorario(HorarioDia horario) {
        Secuencia secHorarioDia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO), "");
        secHorarioDia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_INICIO), horario.getHoraInicio() + ""));
        secHorarioDia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_FIN), horario.getHoraFin() + ""));
        secHorarioDia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_INICIO), horario.getMinutoInicio() + ""));
        secHorarioDia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_FIN), horario.getMinutoFin() + ""));
        secHorarioDia.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_DIA_RESERVA), horario.getNumeroDia() + ""));
        return secHorarioDia;
    }

    public Laboratorio consultarLaboratorio(Secuencia secLaboratorio) throws Exception {
        Laboratorio laboratorio = new Laboratorio();
        Secuencia secId = secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_LABORATORIO));
        if (secId != null) {
            laboratorio.setId(Long.parseLong(secId.getValor()));
        }

        laboratorio.setNombre(secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor());
        laboratorio.setDescripcion(secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor());
        laboratorio.setReservable(Boolean.parseBoolean(secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVABLE)).getValor()));
        laboratorio.setCuentaInvitado(Boolean.parseBoolean(secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA_INVITADO)).getValor()));
        laboratorio.setNombreSalaServicio(secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_SALA_SERVICIO)).getValor());
        laboratorio.setSalon(secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SALON)).getValor());
        Secuencia secActivo = secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVO));

        laboratorio.setActivo(secActivo == null || Boolean.parseBoolean(secActivo.getValor()));

        Secuencia secHorarios = secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIOS_LABORATORIO));
        Collection<HorarioDia> horarios = consultarHorarios(secHorarios);
        laboratorio.setHorario(horarios);

        laboratorio.setAutorizados(consultarAutorizados(secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUTORIZADOS))));
        laboratorio.setEncargados(consultarEncargados(secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENCARGADOS))));

        Secuencia secElemenots = secLaboratorio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ELEMENTOS_LABORATORIO));
        Collection<Elemento> elementos = consultarElementos(secElemenots);
        laboratorio.setElementos(elementos);

        return laboratorio;
    }

    public Collection<HorarioDia> consultarHorarios(Secuencia secuenciaHorarios) {
        Collection<HorarioDia> horarios = new ArrayList();
        Collection<Secuencia> secuenciasHorarios = secuenciaHorarios.getSecuencias();
        for (Secuencia secuenciaHorario : secuenciasHorarios) {
            horarios.add(consultarHorario(secuenciaHorario));
        }
        return horarios;
    }

    public HorarioDia consultarHorario(Secuencia secuenciaHorario) {
        HorarioDia horario = new HorarioDia();
        horario.setHoraFin(Integer.parseInt(secuenciaHorario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_FIN)).getValor()));
        horario.setHoraInicio(Integer.parseInt(secuenciaHorario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_INICIO)).getValor()));
        horario.setMinutoFin(Integer.parseInt(secuenciaHorario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_FIN)).getValor()));
        horario.setMinutoInicio(Integer.parseInt(secuenciaHorario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_INICIO)).getValor()));
        horario.setNumeroDia(secuenciaHorario.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_DIA_RESERVA)).getValorInt());
        return horario;
    }

    public Collection<Elemento> consultarElementos(Secuencia secElementos) {
        Collection<Elemento> elementos = new ArrayList();
        Collection<Secuencia> secuencias = secElementos.getSecuencias();

        for (Secuencia secuencia : secuencias) {
            elementos.add(consultarElemento(secuencia));
        }
        return elementos;
    }

    public Elemento consultarElemento(Secuencia secuencia) {
        Elemento elemento = new Elemento();
        elemento.setDescripcion(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor());
        elemento.setNombre(secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor());

        Secuencia secId = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ELEMENTO));
        if (secId != null) {
            elemento.setId(parseId(secId.getValor()));
        }
        return elemento;
    }

    public Secuencia consultarElemento(Elemento elemento) {
        Secuencia secElemento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ELEMENTO), "");
        secElemento.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ELEMENTO), "" + elemento.getId()));
        secElemento.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), elemento.getNombre()));
        secElemento.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), elemento.getDescripcion()));
        return secElemento;
    }

    public Secuencia consultarElementos(Collection<Elemento> elementos) {
        Secuencia secElementos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ELEMENTOS), "");
        for (Elemento elemento : elementos) {
            secElementos.agregarSecuencia(consultarElemento(elemento));
        }
        return secElementos;
    }

    public Secuencia consultarReservas(Collection<ReservaInventario> reservas) {
        Secuencia secReservas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAS_INVENTARIO), "");
        for (ReservaInventario reservaInventario : reservas) {
            secReservas.agregarSecuencia(consultarReserva(reservaInventario));
        }
        return secReservas;
    }

    public Secuencia consultarReserva(ReservaInventario reserva) {
        Secuencia secReserva = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVA_INVENTARIO), "");
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MOTIVO), reserva.getMotivo()));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_RESERVA), reserva.getEstado()));
        // secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPONSABLE_RESERVA), reserva.getResponsable()));
        //secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CREADOR),reserva.getCreador().getCorreo()));
        Persona responsable = personaFacade.findByCorreo(reserva.getResponsable());
        if (responsable != null) {
            secReserva.agregarSecuencia(consultarPersonaReservaInventario(responsable, getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPONSABLE_RESERVA)));
        } else {
            responsable = new Persona();
            responsable.setCorreo(reserva.getResponsable());
            secReserva.agregarSecuencia(consultarPersonaReservaInventario(responsable, getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPONSABLE_RESERVA)));

        }

        secReserva.agregarSecuencia(consultarPersonaReservaInventario(reserva.getCreador(), getConstanteBean().getConstante(Constantes.TAG_PARAM_CREADOR)));

        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_LABORATORIO), reserva.getLaboratorio().getNombre()));
        secReserva.agregarSecuencia(consultarElementos(reserva.getElementos()));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA), formatoFecha.format(reserva.getFechaReserva())));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), formatoFecha.format(reserva.getFechaInicio())));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), formatoFecha.format(reserva.getFechaFin())));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_RESERVA_INVENTARIO), "" + reserva.getId()));

        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENVIAR_RECORDATORIO), "" + reserva.isEnviarRecordatorio()));
        secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA_INVITADO), "" + reserva.isCuentaInvitado()));
        //ReservaMultipleFacade rmf = new ReservaMultipleFacade();
        ReservaMultiple reservaMultiple = reservaMultipleFacade.consultarReservasMultiplesPorReserva(reserva.getId());


        if (reservaMultiple != null) {
            secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ISMULTIPLE), "true"));
            secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ID), "" + reservaMultiple.getId()));
            secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_PERIODICIDAD), reservaMultiple.getPeriodicidad()));
            secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_INICIORESERVA), "" + reservaMultiple.getInicioReservaMultiple()));
            secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_FINALRESERVA), "" + reservaMultiple.getFinalizacionReservaMultiple()));
        } else {
            secReserva.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ISMULTIPLE), "false"));
        }
        return secReserva;
    }

    public Secuencia consultarPersonaReservaInventario(Persona p, String tipoPersona) {
        Secuencia sec = new Secuencia(tipoPersona, "");
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), p.getCorreo()));
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), p.getNombres()));
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), p.getApellidos()));
        return sec;
    }

    public Persona consultarPersonaReservaInventario(Secuencia sec) {
        return personaFacade.findByCorreo(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor());
    }

    public ReservaInventario consultarReserva(Secuencia secReserva) {
        ReservaInventario reservaInventario = new ReservaInventario();
        Secuencia secMotivo = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MOTIVO));
        reservaInventario.setMotivo(secMotivo == null ? "" : secMotivo.getValor());
        Secuencia secEstadoReserva = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_RESERVA));
        if (secEstadoReserva != null) {
            reservaInventario.setEstado(secEstadoReserva.getValor());
        }
        Secuencia secId = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_RESERVA_INVENTARIO));
        if (secId != null) {
            reservaInventario.setId(secId.getValor().isEmpty() ? null : Long.parseLong(secId.getValor()));
        }

        String correoCreador = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_CREADOR)).getValor();
        reservaInventario.setCreador(personaFacade.findByCorreo(correoCreador + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO)));

        String nombreLaboratorio = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_LABORATORIO)).getValor();
        Laboratorio laboratorio = laboratorioFacade.findLaboratorioPorNombre(nombreLaboratorio);
        reservaInventario.setLaboratorio(laboratorio);

        Secuencia secElementos = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ELEMENTOS_LABORATORIO));
        Collection<Elemento> elementos = new ArrayList();
        for (Secuencia secElemento : secElementos.getSecuencias()) {
            String nombreElemento = secElemento.getValor();
            for (Elemento elemento : laboratorio.getElementos()) {
                if (elemento.getNombre().equals(nombreElemento)) {
                    elementos.add(elemento);
                    break;
                }
            }
        }
        reservaInventario.setElementos(elementos);

        String enviaRecordatorio = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENVIAR_RECORDATORIO)).getValor();
        reservaInventario.setEnviarRecordatorio(Boolean.parseBoolean(enviaRecordatorio));

        String enviaCuentaInvitado = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUENTA_INVITADO)).getValor();
        reservaInventario.setCuentaInvitado(Boolean.parseBoolean(enviaCuentaInvitado));

        Secuencia secResponsable = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPONSABLE_RESERVA));
        if (secResponsable != null) {
            reservaInventario.setResponsable(secResponsable.getValor());
        } else {
            reservaInventario.setResponsable(correoCreador);
        }

        Secuencia secFechaReserva = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA));
        HoraBO hora = consultarHora(secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_RESERVA)));
        try {
            Date fechaReserva = formatoFecha.parse(secFechaReserva.getValor());
            Calendar calendarReserva = Calendar.getInstance();
            calendarReserva.setTime(fechaReserva);
            calendarReserva.set(Calendar.HOUR_OF_DAY, 0);
            calendarReserva.set(Calendar.MINUTE, 0);
            calendarReserva.set(Calendar.SECOND, 0);
            reservaInventario.setFechaReserva(new Timestamp(calendarReserva.getTimeInMillis()));
            Calendar calendarInicio = Calendar.getInstance();
            calendarInicio.setTime(fechaReserva);
            calendarInicio.set(Calendar.HOUR_OF_DAY, hora.getHoraIncio());
            calendarInicio.set(Calendar.MINUTE, hora.getMinutoInicio());
            calendarInicio.set(Calendar.SECOND, 0);
            reservaInventario.setFechaInicio(new Timestamp(calendarInicio.getTimeInMillis()));
            Calendar calendarFin = Calendar.getInstance();
            calendarFin.setTime(fechaReserva);
            calendarFin.set(Calendar.HOUR_OF_DAY, hora.getHoraFin());
            calendarFin.set(Calendar.MINUTE, hora.getMinutoFin());
            calendarFin.set(Calendar.SECOND, 0);
            reservaInventario.setFechaFin(new Timestamp(calendarFin.getTimeInMillis()));
        } catch (Exception e) {
            System.out.println(this.getClass() + "Error" + e.getMessage());
        }
        return reservaInventario;
    }

    public Collection<Persona> consultarPersonasReservaInventario(Secuencia secuencia) {
        Collection<Persona> personas = new ArrayList();
        for (Secuencia secPersona : secuencia.getSecuencias()) {
            personas.add(consultarPersonaReservaInventario(secPersona));
        }
        return personas;
    }

    public Collection<Encargado> consultarEncargados(Secuencia secuencia) throws Exception {
        Collection<Encargado> encargados = new ArrayList();
        for (Secuencia secHija : secuencia.getSecuencias()) {
            encargados.add(consultarEncargado(secHija));
        }
        return encargados;
    }

    public Encargado consultarEncargado(Secuencia sec) throws Exception {
        String strCorreo = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
        String login = strCorreo;
        if (!strCorreo.contains("@")) {
            strCorreo += constanteBean.getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
        } else {
            login = login.substring(0, login.indexOf("@"));
        }

        Encargado e = encargadoFacade.findByCorreo(strCorreo);
        if (e == null) {
            e = new Encargado();
            Persona persona = personaFacade.findByCorreo(strCorreo);
            if (persona == null) {
                persona = new Persona();
                persona.setNombres(AccesoLDAP.obtenerNombres(login));
                persona.setApellidos(AccesoLDAP.obtenerApellidos(login));
                persona.setCorreo(strCorreo);

                personaFacade.create(persona);
                persona = personaFacade.findByCorreo(strCorreo);
            }
            e.setPersona(persona);
        }
        return e;
    }

    public Secuencia consultarAutorizados(Collection<Autorizado> autorizados) {
        Secuencia secuenciaAutorizado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUTORIZADOS), "");
        for (Autorizado autorizado : autorizados) {
            secuenciaAutorizado.agregarSecuencia(consultarAutorizado(autorizado));
        }
        return secuenciaAutorizado;
    }

    public Secuencia consultarEncargados(Collection<Encargado> encargados) {
        Secuencia secuenciaEncargados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENCARGADOS), "");
        for (Encargado encargado : encargados) {
            secuenciaEncargados.agregarSecuencia(consultarPersona(encargado.getPersona()));
        }
        return secuenciaEncargados;
    }

    public Secuencia consultarPersona(Persona persona) {
        Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA), "");
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), persona.getCorreo()));
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), persona.getNombres()));
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), persona.getApellidos()));
        return sec;
    }

    public Secuencia consultarHora(HoraBO hora) {
        Secuencia secuenciaHora = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INTERVALO_DISPONIBLE), "");
        secuenciaHora.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_INICIO), "" + hora.getHoraIncio()));
        secuenciaHora.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_FIN), "" + hora.getHoraFin()));
        secuenciaHora.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_INICIO), "" + hora.getMinutoInicio()));
        secuenciaHora.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_FIN), "" + hora.getMinutoFin()));
        secuenciaHora.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO), "" + hora.getTipo()));
        return secuenciaHora;
    }

    public HoraBO consultarHora(Secuencia secHora) {
        int hi = Integer.parseInt(secHora.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_INICIO)).getValor());
        int hf = Integer.parseInt(secHora.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORA_FIN)).getValor());
        int mi = Integer.parseInt(secHora.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_INICIO)).getValor());
        int mf = Integer.parseInt(secHora.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MINUTO_FIN)).getValor());
        HoraBO hora = new HoraBO(hi, hf, mi, mf);
        return hora;
    }

    public Secuencia consultarHoras(Collection<HoraBO> horas) {
        Secuencia secuenciaHoras = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INTERVALOS_DISPONIBLES), "");
        for (HoraBO horaBO : horas) {
            secuenciaHoras.agregarSecuencia(consultarHora(horaBO));
        }
        return secuenciaHoras;
    }

    private Long parseId(String str) {
        return (str == null || str.isEmpty()) ? null : Long.parseLong(str);
    }

    public Collection<Autorizado> consultarAutorizados(Secuencia secuencia) {
        Collection<Autorizado> personas = new ArrayList();

        for (Secuencia secPersona : secuencia.getSecuencias()) {

            personas.add(consultarAutorizado(secPersona));

        }
        return personas;
    }

    public Autorizado consultarAutorizado(Secuencia secPersonas) {
        Autorizado autorizado = new Autorizado();
        Boolean secuenciaReservaTerceros = Boolean.parseBoolean(secPersonas.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVA_TERCEROS)).getValor());
        autorizado.setPuedeReservarATerceros(secuenciaReservaTerceros);
        autorizado.setPersona(personaFacade.findByCorreo(secPersonas.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor()));
        return autorizado;
    }

    public Secuencia consultarAutorizado(Autorizado autorizado) {
        Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUTORIZADO), "");
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), autorizado.getPersona().getCorreo()));
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), autorizado.getPersona().getNombres()));
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), autorizado.getPersona().getApellidos()));
        sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVA_TERCEROS), autorizado.getPuedeReservarATerceros().toString()));
        return sec;
    }

    public ReservaMultiple consultarReservaMultiple(Secuencia secReserva) throws ParseException {
        ReservaMultiple reservaMultiple = new ReservaMultiple();

        //Consulta de id
        Secuencia secIdReservaMultiple = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ID));
        if (secIdReservaMultiple != null) {
            reservaMultiple.setId(Long.parseLong(secIdReservaMultiple.getValor()));
        }

        //Consulta de periodicidad
        Secuencia secPeriodicidadReservaMultiple = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_PERIODICIDAD));
        if (secPeriodicidadReservaMultiple != null) {
            reservaMultiple.setPeriodicidad(secPeriodicidadReservaMultiple.getValor());
        }

        //Consulta de fecha de finalizacion
        Secuencia secFinalizacionReservaMultiple = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_FINALRESERVA));
        if (secFinalizacionReservaMultiple != null) {
            reservaMultiple.setFinalizacionReservaMultiple(new Timestamp(formatoFecha.parse(secFinalizacionReservaMultiple.getValor()).getTime()));
        }

        //Conuslta de fecha de inicio de la reserva
        Secuencia secInicioReservaMultiple = secReserva.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_INICIORESERVA));
        if (secInicioReservaMultiple != null) {
            reservaMultiple.setInicioReservaMultiple(new Timestamp(formatoFecha.parse(secInicioReservaMultiple.getValor()).getTime()));
        }

        return reservaMultiple;
    }

    public Secuencia consultarReservaMultiple(ReservaMultiple reservaMultiple) {
        Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE), "");
        if (reservaMultiple != null) {
            sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ISMULTIPLE), "true"));
            sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ID), "" + reservaMultiple.getId()));
            sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_PERIODICIDAD), reservaMultiple.getPeriodicidad()));
            sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_INICIORESERVA), "" + reservaMultiple.getInicioReservaMultiple()));
            sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_FINALRESERVA), "" + reservaMultiple.getFinalizacionReservaMultiple()));
        } else {
            sec.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESERVAMULTIPLE_ISMULTIPLE), "false"));
        }
        return sec;




    }
}
