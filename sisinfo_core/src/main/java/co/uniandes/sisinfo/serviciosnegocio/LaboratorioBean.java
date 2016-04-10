/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.bo.HoraBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.Autorizado;
import co.uniandes.sisinfo.entities.Encargado;
import co.uniandes.sisinfo.entities.HorarioDia;
import co.uniandes.sisinfo.entities.Laboratorio;
import co.uniandes.sisinfo.entities.ReservaInventario;
import co.uniandes.sisinfo.entities.ReservaMultiple;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import co.uniandes.sisinfo.serviciosfuncionales.EncargadoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.LaboratorioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReservaInventarioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ReservaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.ProfesorFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeRemote;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
public class LaboratorioBean implements LaboratorioBeanRemote, LaboratorioBeanLocal {

    private ServiceLocator serviceLocator;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private LaboratorioFacadeLocal laboratorioFacade;
    @EJB
    private ReservaInventarioFacadeLocal reservaFacade;
    @EJB
    private ReservaMultipleFacadeLocal reservaMultipleFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private EncargadoFacadeLocal encargadoFacade;
    @EJB
    private UsuarioFacadeRemote usuarioFacade;
    @EJB
    private RolFacadeRemote rolFacade;
    @EJB
    private ProfesorFacadeRemote profesorFacade;
    private ConversorReservaInventario conversor;
    private ParserT parser;

    public LaboratorioBean() {
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            usuarioFacade = (UsuarioFacadeRemote) serviceLocator.getRemoteEJB(UsuarioFacadeRemote.class);
            rolFacade = (RolFacadeRemote) serviceLocator.getRemoteEJB(RolFacadeRemote.class);
            profesorFacade = (ProfesorFacadeRemote) serviceLocator.getRemoteEJB(ProfesorFacadeRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ParserT getParser() {
        return parser;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public ConversorReservaInventario getConversor() {
        if (conversor == null) {
            conversor = new ConversorReservaInventario(constanteBean, laboratorioFacade, personaFacade, encargadoFacade, null);
        }
        return conversor;
    }

    @Override
    public String consultarLaboratoriosAutorizados(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            if(!correo.contains("@")){
                correo = correo + getConstanteBean().getConstante(Constantes.TAG_PARAM_SUFIJO_CORREO);
            }
            // Si el correo corresponde a un profesor de planta, entonces esta autorizado para realizar reservas en cualquier lab
            Profesor p = profesorFacade.findByCorreo(correo);
            boolean esProfesor = p != null && (p.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_PLANTA)) || p.getTipo().equals(getConstanteBean().getConstante(Constantes.VAL_TIPO_PROFESOR_CATEDRA)));
            // Si el correo corresponde a un administrador, entonces esta autorizado para realizar reservas en cualquier lab
            // Se debe revisar que el correo corresponda a un usuario, ya que los estudiantes no tienen usuario
            Usuario u = usuarioFacade.findByLogin(correo);
            boolean esAdmin = false;
            if (u != null) {
                Collection<Rol> roles = u.getRoles();
                for (Rol rol : roles) {
                    if (rol.getRol().equals(getConstanteBean().getConstante(Constantes.ROL_ADMINISTRADOR_RESERVA_INVENTARIO))) {
                        esAdmin = true;
                        break;
                    }
                }
            }
            Collection<Laboratorio> laboratorios;
            if (esAdmin) {
                laboratorios = laboratorioFacade.findLaboratoriosActivos();
            } else if (esProfesor) {
                laboratorios = laboratorioFacade.findLaboratoriosActivosYReservables();
            } else {
                laboratorios = laboratorioFacade.findLaboratoriosAutorizados(correo);
            }
            Secuencia secLabs = getConversor().consultarLaboratorios(laboratorios);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secLabs);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIOS_AUTORIZADOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public String consultarLaboratorio(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String id = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_LABORATORIO)).getValor();
            Laboratorio laboratorio = laboratorioFacade.find(id);
            Secuencia secLab = getConversor().consultarLaboratorio(laboratorio);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secLab);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public String consultarAutorizadoLaboratorio(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_LABORATORIO)).getValor();
            Laboratorio laboratorio = laboratorioFacade.findLaboratorioPorNombre(nombre);
            Collection<Autorizado> autorizados = laboratorio.getAutorizados();
            Boolean autorizado = false;
            for (Autorizado persona : autorizados) {
                autorizado = autorizado || persona.getPersona().getCorreo().equals(correo);
                if (autorizado) {
                    break;
                }
            }
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_AUTORIZADO), autorizado.toString()));
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_AUTORIZADO_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public String consultarLaboratorios(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            Collection<Laboratorio> laboratorios = laboratorioFacade.findLaboratoriosActivos();
            Secuencia secLabs = getConversor().consultarLaboratorios(laboratorios);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secLabs);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String consultarLaboratoriosAdministrador(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            Collection<Laboratorio> laboratorios = laboratorioFacade.findAll();
            Secuencia secLabs = getConversor().consultarLaboratorios(laboratorios);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secLabs);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIOS_ADMINISTRADOR), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    @Override
    public String darHorarioDisponibleLaboratorio(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_LABORATORIO)).getValor();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
            Date fecha = sdf.parse(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA)).getValor());
            // Se obtiene el id de la reserva que sera excluido de la consulta. Si el parametro no se encuentra definido entonces se ignora
            Secuencia secIdReserva = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_RESERVA_INVENTARIO));
            String idReserva = secIdReserva != null ? secIdReserva.getValor() : null;
            Calendar calendarFechaInicio = Calendar.getInstance();
            calendarFechaInicio.setTime(fecha);
            int diaSemana = calendarFechaInicio.get(Calendar.DAY_OF_WEEK);
            calendarFechaInicio.set(Calendar.HOUR_OF_DAY, 0);
            calendarFechaInicio.set(Calendar.MINUTE, 0);
            calendarFechaInicio.set(Calendar.SECOND, 0);
            calendarFechaInicio.set(Calendar.MILLISECOND, 0);
            Laboratorio lab = laboratorioFacade.findLaboratorioPorNombre(nombre);
            Collection<ReservaInventario> reservas = reservaFacade.consultarReservasPorNombreLaboratorioyEstado(nombre, getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE));


            if (idReserva != null && !idReserva.equals("null")) {

                //Se elimina la reserva de la lista para que no exista conflicto con ella misma
                ReservaInventario ri = reservaFacade.find(Long.parseLong(idReserva));
                if (ri != null) {
                    reservas.remove(ri);
                }

                //Se eliminan todas las reservas pertenecientes a la reserva multiple para eliminar el conflicto de horas
                ReservaMultiple reservaMultiple = reservaMultipleFacade.consultarReservasMultiplesPorReserva(Long.parseLong(idReserva));
                if (reservaMultiple != null) {
                    if (!reservaMultiple.getReservas().isEmpty() || reservaMultiple.getReservas() != null) {
                        for (ReservaInventario rm : reservaMultiple.getReservas()) {
                            reservas.remove(rm);
                        }
                    }
                }

            }


            ArrayList<HoraBO> horas = darHorarioLaboratorio(lab, diaSemana, reservas, calendarFechaInicio);
            for (int i = 0; i < horas.size(); i++) {
                HoraBO horaBO = horas.get(i);
                if (!horaBO.getTipo().equals("Libre")) {
                    horas.remove(i);
                    i--;
                }

            }
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(getConversor().consultarHoras(horas));
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HORARIO_DISPONIBLE_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    private ArrayList<HoraBO> construirHoras(HorarioDia horario) {
        int minutosIntervalo = 30;
        ArrayList<HoraBO> horas = new ArrayList<HoraBO>();
        int inicio = horario.getHoraInicio() * 60 + horario.getMinutoInicio();
        // Se realiza una aproximacion de la hora inicial hacia el intervalo
        inicio = inicio / minutosIntervalo;
        inicio = inicio * minutosIntervalo;
        int fin = horario.getHoraFin() * 60 + horario.getMinutoFin();
        for (int i = inicio; i < fin; i += 30) {
            HoraBO hora = new HoraBO(i / 60, (i + minutosIntervalo) / 60, i % 60, (i + minutosIntervalo) % 60);
            hora.setTipo("Libre");
            horas.add(hora);
        }
        return horas;
    }

    public String editarLaboratorio(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            Secuencia secuenciaLab = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LABORATORIO));
            Laboratorio lab = getConversor().consultarLaboratorio(secuenciaLab);
            if (lab.getId() == null) {
                Laboratorio labAnterior = laboratorioFacade.findLaboratorioPorNombre(lab.getNombre());
                if (labAnterior != null) {
                    Vector<Secuencia> params = new Vector<Secuencia>();
                    return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_EDITAR_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.RESERVA_INVENTARIO_ERR_0007, params);
                }

                laboratorioFacade.create(lab);
            } else {
                laboratorioFacade.edit(lab);
            }

            Collection<Encargado> encargados = lab.getEncargados();
            for (Encargado encargado : encargados) {
                Boolean necesitaRol = true;
                String correo = encargado.getPersona().getCorreo();
                //correo.replace("@uniandes.edu.co", "");
                Usuario usuario = usuarioFacade.findByLogin(correo);
                boolean nuevoUsuario = false;
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setRoles(new ArrayList<Rol>());
                    usuario.setEsPersona(true);
                    usuario.setPersona(personaFacade.findByCorreo(correo));
                    nuevoUsuario = true;
                }

                Collection<Rol> roles = usuario.getRoles();

                for (Rol rol : roles) {
                    if (rol.getRol().equals(constanteBean.getConstante(Constantes.ROL_ENCARGADO_LABORATORIO))) {
                        necesitaRol = false;
                        break;
                    }
                }
                if (necesitaRol) {
                    Rol nuevoRol = rolFacade.findByRol(constanteBean.getConstante(Constantes.ROL_ENCARGADO_LABORATORIO));
                    roles.add(nuevoRol);
                    usuario.setRoles(roles);
                    if (nuevoUsuario) {
                        usuarioFacade.create(usuario);
                    } else {
                        usuarioFacade.edit(usuario);
                    }
                }
            }
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String consultarLaboratoriosEncargado(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Collection<Laboratorio> laboratorios = laboratorioFacade.findLaboratoriosEncargado(correo);
            Secuencia secLabs = getConversor().consultarLaboratorios(laboratorios);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secLabs);
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_LABORATORIOS_ENCARGADO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    public String darOcupacionLaboratorio(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_LABORATORIO)).getValor();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
            Date fecha = sdf.parse(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA)).getValor());
            Calendar calendarFechaInicio = Calendar.getInstance();
            calendarFechaInicio.setTime(fecha);
            int diaSemana = calendarFechaInicio.get(Calendar.DAY_OF_WEEK);
            calendarFechaInicio.set(Calendar.HOUR_OF_DAY, 0);
            calendarFechaInicio.set(Calendar.MINUTE, 0);
            calendarFechaInicio.set(Calendar.SECOND, 0);
            calendarFechaInicio.set(Calendar.MILLISECOND, 0);
            Laboratorio lab = laboratorioFacade.findLaboratorioPorNombre(nombre);
            Collection<ReservaInventario> reservas = reservaFacade.consultarReservasPorNombreLaboratorioyEstado(nombre, getConstanteBean().getConstante(Constantes.VAL_ESTADO_RESERVA_INVENTARIO_VIGENTE));
            ArrayList<HoraBO> horas = darHorarioLaboratorio(lab, diaSemana, reservas, calendarFechaInicio);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(getConversor().consultarHoras(horas));
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_HORARIO_OCUPADO_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }

    private ArrayList<HoraBO> darHorarioLaboratorio(Laboratorio lab, int diaSemana, Collection<ReservaInventario> reservas, Calendar calendarFechaInicio) {
        ArrayList<HoraBO> horas = new ArrayList();
        for (HorarioDia horario : lab.getHorario()) {
            if (horario.getNumeroDia() + 1 != diaSemana) {
                continue;
            }
            horas.addAll(construirHoras(horario));
            for (ReservaInventario reserva : reservas) {
                if (!reserva.getFechaReserva().equals(new Timestamp(calendarFechaInicio.getTime().getTime()))) {
                    continue;
                }
                Calendar calendarReservaInicio = Calendar.getInstance();
                calendarReservaInicio.setTime(reserva.getFechaInicio());
                Calendar calendarReservaFin = Calendar.getInstance();
                calendarReservaFin.setTime(reserva.getFechaFin());
                int minutosInicio = calendarReservaInicio.get(Calendar.MINUTE) + calendarReservaInicio.get(Calendar.HOUR_OF_DAY) * 60;
                int minutosFin = calendarReservaFin.get(Calendar.MINUTE) + calendarReservaFin.get(Calendar.HOUR_OF_DAY) * 60;
                for (int i = 0; i < horas.size(); i++) {
                    HoraBO horaBO = horas.get(i);
                    if (horaBO.darTiempoInicio() >= minutosInicio && horaBO.darTiempoInicio() < minutosFin
                            || horaBO.darTiempoFin() > minutosInicio && horaBO.darTiempoFin() <= minutosFin) {
                        horas.get(i).setTipo("Reserva:</br>" + reserva.getCreador().getNombres() + " " + reserva.getCreador().getApellidos()+"</br>Motivo:</br>" + reserva.getMotivo());
                        // i--;
                    } /*else {
                    horas.get(i).setTipo("Libre");
                    }*/
                }
            }
        }
        return horas;
    }

    public String activarLaboratorio(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String idLaboratorio = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_LABORATORIO)).getValor();
            Boolean activo = Boolean.parseBoolean(getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVO)).getValor());
            Laboratorio laboratorio = laboratorioFacade.find(Long.parseLong(idLaboratorio));
            laboratorio.setActivo(activo);
            laboratorioFacade.edit(laboratorio);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }

    }

    public String eliminarLaboratorio(String xml) {
        String respuesta = null;
        try {
            getParser().leerXML(xml);
            String idLaboratorio = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_LABORATORIO)).getValor();
            Laboratorio laboratorio = laboratorioFacade.find(Long.parseLong(idLaboratorio));
            Collection<ReservaInventario> reservas = reservaFacade.consultarReservasPorNombreLaboratorio(laboratorio.getNombre());
            for (ReservaInventario reservaInventario : reservas) {
                reservaFacade.remove(reservaInventario);
            }
            laboratorioFacade.remove(laboratorio);
            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_LABORATORIO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0001, new ArrayList<Secuencia>());
            return respuesta;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(LaboratorioBean.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }

    }
}
