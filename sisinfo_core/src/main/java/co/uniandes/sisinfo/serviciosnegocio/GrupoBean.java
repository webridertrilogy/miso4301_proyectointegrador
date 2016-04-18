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
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.comun.constantes.Notificaciones;
import co.uniandes.sisinfo.entities.Grupo;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.serviciosfuncionales.CorreoRemote;
import co.uniandes.sisinfo.serviciosfuncionales.GrupoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.PersonaFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author da-naran
 */
@Stateless
public class GrupoBean implements GrupoRemote, GrupoLocal {

    //----CONSTANTES-------------
    private final static String RUTA_INTERFAZ_REMOTA = "co.uniandes.sisinfo.serviciosnegocio.TesisBeanRemote";
    private final static String NOMBRE_METODO_TIMER = "manejoTimmerGrupoBean";
    //---------------------------------------
    // Atributos
    //---------------------------------------
    private ParserT parser;
    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    @EJB
    private GrupoFacadeLocal grupoFacade;
    @EJB
    private PersonaFacadeRemote personaFacade;
    @EJB
    private TimerGenericoBeanRemote timerGenerico;
    @EJB
    private CorreoRemote correoBean;

    public GrupoBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            timerGenerico = (TimerGenericoBeanRemote) serviceLocator.getRemoteEJB(TimerGenericoBeanRemote.class);
            personaFacade = (PersonaFacadeRemote) serviceLocator.getRemoteEJB(PersonaFacadeRemote.class);
            parser = new ParserT();
            correoBean = (CorreoRemote) serviceLocator.getRemoteEJB(CorreoRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String crearGrupo(String comando) {
        try {
            parser.leerXML(comando);
            Secuencia secGrupo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO));
            if (secGrupo != null) {
                Grupo g = pasarSecuenciaAGrupo(secGrupo);
                grupoFacade.create(g);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CREAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }

    }

    @Deprecated
    public String removerPersonasDeGrupo(String comando) {
        try {
            parser.leerXML(comando);
            Secuencia secIdGrupo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO));
            if (secIdGrupo != null) {
                Long idGrupo = Long.parseLong(secIdGrupo.getValor().trim());
                Grupo g = grupoFacade.find(idGrupo);
                Collection<Persona> vacio = new ArrayList<Persona>();
                g.setPersonas(vacio);
                grupoFacade.edit(g);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_VACIAR_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
                return respuesta;
            } else {
                throw new Exception("error en xml:removerPersonasDeGrupo-GrupoBean ");
            }
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_VACIAR_GRUPO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception e) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, e);
                return "ERROR";
            }
        }
    }

    public String eliminarGrupo(String comando) {
        try {
            parser.leerXML(comando);
            Secuencia idGrupo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO));
            Secuencia secCorreoOrden = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (idGrupo != null && secCorreoOrden != null) {
                Long id = Long.parseLong(idGrupo.getValor().trim());
                String correo = secCorreoOrden.getValor();
                Grupo g = grupoFacade.find(id);

                if (correo.equals(g.getDuenho().getCorreo())) {
                    grupoFacade.remove(g);
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.GRUPOS_PERSONAS_0001, new ArrayList());
                    return respuesta;
                } else {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.GRUPOS_PERSONAS_0001, new ArrayList());
                }

            }
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception e) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, e);
                return "ERROR";
            }
        }

    }

    public String darGrupos(String comando) {

        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        Collection<Grupo> grupos = grupoFacade.findAll();
        Secuencia secGrupos = pasarGruposASecuencia(grupos);
        secuencias.add(secGrupos);
        try {
            String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_GRUPOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                secuencias = new ArrayList<Secuencia>();
                String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_GRUPOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new ArrayList());
                return respuesta;
            } catch (Exception e) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, e);
                return "ERROR";
            }
        }
    }

    public String darGrupoPorNombre(String comando) {
        try {
            //Se extraen los parámetros del comando
            parser.leerXML(comando);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            String nombre = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            Grupo g = grupoFacade.findByNombre(nombre);
            Secuencia secGrupo = pasarGrupoASecuencia(g);
            secuencias.add(secGrupo);
            String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPO_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return parser.generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPO_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    public String darGrupoPorID(String comando) {
        try {
            //Se extraen los parámetros del comando
            parser.leerXML(comando);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            Long idGrupo = Long.parseLong(parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO)).getValor());
            Grupo g = grupoFacade.find(idGrupo);
            Secuencia secGrupo = pasarGrupoASecuencia(g);
            secuencias.add(secGrupo);
            String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return parser.generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPO_POR_ID), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    private Secuencia pasarPersonaASecuencia(Persona p) {
        Secuencia secPersona = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONA), "");
        secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), p.getNombres()));
        secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), p.getApellidos()));
        secPersona.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), p.getCorreo()));

        return secPersona;
    }

    public Secuencia pasarGrupoASecuencia(Grupo g) {
        Secuencia secGrupo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO), "");
        secGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO), Long.toString(g.getId())));
        secGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), g.getNombre()));
        secGrupo.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), g.getDescripcion() != null ? g.getDescripcion() : " "));
        Persona duenho = g.getDuenho();
        if (duenho != null) {
            Secuencia duenhoPersona = pasarPersonaASecuencia(duenho);
            Secuencia secDuenhoGrupo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DUEHNO_GRUPO), null);
            secDuenhoGrupo.agregarSecuencia(duenhoPersona);
            secGrupo.agregarSecuencia(secDuenhoGrupo);
        } else {
            duenho = new Persona();
            duenho.setNombres("");
            duenho.setApellidos("");
            duenho.setCorreo("default");
            Secuencia duenhoPersona = pasarPersonaASecuencia(duenho);
            Secuencia secDuenhoGrupo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DUEHNO_GRUPO), null);
            secDuenhoGrupo.agregarSecuencia(duenhoPersona);
            secGrupo.agregarSecuencia(secDuenhoGrupo);
        }

        Collection<Persona> personas = g.getPersonas();
        Secuencia secPersonas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONAS), "");
        for (Persona p : personas) {
            Secuencia secPersona = pasarPersonaASecuencia(p);
            secPersonas.agregarSecuencia(secPersona);
        }
        secGrupo.agregarSecuencia(secPersonas);
        return secGrupo;
    }

    public ParserT getParser() {
        return parser;
    }

    public String darGruposPorDuenho(String xml) {
        try {
            //Se extraen los parámetros del comando
            parser.leerXML(xml);
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            String correo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            Collection<Grupo> grupos = grupoFacade.findByCorreoDuenho(correo);
            Secuencia secGrupos = pasarGruposASecuencia(grupos);
            secuencias.add(secGrupos);
            String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPO_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.COM_MSJ_0001, new ArrayList());
            return respuesta;
        } catch (Exception ex) {
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return parser.generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_GRUPO_POR_NOMBRE), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private Secuencia pasarGruposASecuencia(Collection<Grupo> grupos) {
        Secuencia secGrupos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPOS), "");
        for (Grupo g : grupos) {
            Secuencia secGrupo = pasarGrupoASecuencia(g);
            secGrupos.agregarSecuencia(secGrupo);
        }
        return secGrupos;
    }

    public String editarGrupoPersonas(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secGrupo = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO));
            if (secGrupo != null) {
                Grupo g = pasarSecuenciaAGrupo(secGrupo);
                Grupo enBD = grupoFacade.find(g.getId());
                if (!g.getDuenho().getCorreo().equals(enBD.getDuenho().getCorreo())) {
                    Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                    return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.GRUPOS_PERSONAS_0002, new ArrayList());
                }
                grupoFacade.edit(g);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            } else {
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            }
        } catch (Exception ex) {
            Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_EDITAR_GRUPO_PERSONAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    private Grupo pasarSecuenciaAGrupo(Secuencia secGrupo) {
        Grupo g = new Grupo();
        Secuencia secIdGrupo = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO));
        if (secIdGrupo != null) {
            Long idGrupo = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GRUPO)).getValorLong();
            g.setId(idGrupo);
        }
        Secuencia secNombre = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secNombre != null) {
            String nombreGrupo = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            g.setNombre(nombreGrupo);
        }
        Secuencia secDescripcion = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
        if (secDescripcion != null) {
            String descripcion = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor();
            g.setDescripcion(descripcion);
        } else {
            g.setDescripcion("");
        }

        Secuencia secDuenho = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DUEHNO_GRUPO));
        if (secDuenho != null) {
            Secuencia secCorreo = secDuenho.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Persona p = personaFacade.findByCorreo(secCorreo.getValor());
            g.setDuenho(p);
        }
        Secuencia secPersonas = secGrupo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERSONAS));
        Collection<Secuencia> secuenciasPersonas = secPersonas.getSecuencias();
        Collection<Persona> integrantes = new ArrayList<Persona>();
        for (Secuencia secuencia : secuenciasPersonas) {
            Secuencia secCorreo = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Persona p = personaFacade.findByCorreo(secCorreo.getValor());
            integrantes.add(p);
        }
        g.setPersonas(integrantes);
        return g;
    }

    public String programarBorradoGrupos(String xml) {
        try {
            parser.leerXML(xml);
            Secuencia secFechaEliminacion = parser.obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA));
            SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sdfHMS.parse(secFechaEliminacion.getValor());
            if (d.before(new Date())) {
                //error la fecha no puede haber pasado

                return parser.generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIGURAR_TIMMER_ELIMINAR_GRUPOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new LinkedList<Secuencia>());


            }
            Timestamp fechaEliminar = new Timestamp(d.getTime());
            String mensajeAsociado = getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPOS_PERSONA_POR_FINAL_PERIODO);
            //1ero eliminar timer anterior de borrar grupos
            timerGenerico.eliminarTimerPorParametroExterno(mensajeAsociado);
            //2 crear timer otra vez de borrado grupos
            timerGenerico.crearTimer2(RUTA_INTERFAZ_REMOTA, NOMBRE_METODO_TIMER, fechaEliminar, mensajeAsociado,
                        "ServicioSoporteProcesos", this.getClass().getName(), "programarBorradoGrupos", "Este timer se crea para programar el eliminar grupos al finalizar el periodo");
            Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
            String respuesta = parser.generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONFIGURAR_TIMMER_ELIMINAR_GRUPOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());
            return respuesta;

        } catch (Exception ex) {
            try {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex);
                return parser.generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CONFIGURAR_TIMMER_ELIMINAR_GRUPOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0001, new LinkedList<Secuencia>());
            } catch (Exception ex1) {
                Logger.getLogger(GrupoBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public void manejoTimmerGrupoBean(String comando) {
        if (comando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_GRUPOS_PERSONA_POR_FINAL_PERIODO))) {
            eliminarTodosLosGrupos();
        }
    }

    private void eliminarTodosLosGrupos() {

        Collection<Grupo> grupos = grupoFacade.findAll();
        for (Grupo grupo : grupos) {
            grupoFacade.remove(grupo);
        }
        String asuntoCreacion = Notificaciones.ASUNTO_GRUPOS_PERSONA_ELIMINADOS_POR_FECHA_VENCIMIENTO;
        String mensajeCreacion = Notificaciones.MENSAJE_GRUPOS_PERSONA_ELIMINADOS_POR_FECHA_VENCIMIENTO;
        correoBean.enviarMail("sisinfo@uniandes.edu.co", asuntoCreacion, null, null, null, mensajeCreacion);
    }
}
