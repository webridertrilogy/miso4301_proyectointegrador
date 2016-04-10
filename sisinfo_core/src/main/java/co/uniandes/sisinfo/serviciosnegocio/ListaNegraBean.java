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
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.Lista_Negra;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.serviciosfuncionales.AspiranteFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.Lista_NegraFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.AccesoLDAP;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Administración de lista negra
 */
@Stateless
@EJB(name = "ListaNegraBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ListaNegraLocal.class)
public class ListaNegraBean implements ListaNegraRemote, ListaNegraLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Lista_NegraFacade
     */
    @EJB
    private Lista_NegraFacadeLocal lista_NegraFacade;
    /**
     * Parser
     */
    private ParserT parser;
    /**
     *
     */
    @EJB
    private AspiranteFacadeLocal aspiranteFacade;
    /**
     * SolicitudFacade
     */
    @EJB
    private SolicitudFacadeLocal solicitudFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ListaNegraBean
     */
    public ListaNegraBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB( ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String estaEnListaNegra(String comando) {
        try {
            getParser().leerXML(comando);
        } catch (Exception e) {
            try {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_LISTA_NEGRA_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, new ArrayList());
            } catch (Exception ex) {
                Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        try {
            String codigo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE)).getValor();
            Lista_Negra lista_negra = getLista_NegraFacade().findEstudianteByCodigo(codigo);
            String respuesta = "";
            String ln = "";
            Collection<Secuencia> secuencias = new Vector<Secuencia>();
            Secuencia s = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), codigo);
            s.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_CODIGO_ESTUDIANTE));
            secuencias.add(s);
            if (lista_negra == null) {
                ln = Mensajes.MSJ_0033;
                s = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.FALSE));
            } else {
                s = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.TRUE));
                ln = Mensajes.MSJ_0022;
            }
            Collection<Secuencia> secuenciasRespuesta = new ArrayList();
            secuenciasRespuesta.add(s);
            respuesta = getParser().generarRespuesta(secuenciasRespuesta, getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_LISTA_NEGRA_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), ln, secuencias);
            return respuesta;
        } catch (Exception ex) {
            try {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_LISTA_NEGRA_POR_CODIGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0039, new ArrayList());
            } catch (Exception e) {
                Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, e);
                return null;
            }
        }
    }

    @Override
    public String estaEnListaNegraLogin(String comando) {
        try {
            getParser().leerXML(comando);
        } catch (Exception e) {
            try {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_LISTA_NEGRA_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0001, new ArrayList());
            } catch (Exception ex) {
                Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        try {
            String tagCorreo = getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO);
            String correo = getParser().obtenerSecuencia(tagCorreo).getValor();
            Lista_Negra lista_negra = getLista_NegraFacade().findEstudianteByCorreo(correo);
            String respuesta = "";
            Collection<Secuencia> secuencias = new Vector<Secuencia>();
            Secuencia s = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), correo);
            s.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE));
            secuencias.add(s);
            String ln = "";
            if (lista_negra == null) {
                ln = Mensajes.MSJ_0034;
                s = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.FALSE));
            } else {
                ln = Mensajes.MSJ_0023;
                s = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.TRUE));
            }
            Collection<Secuencia> secuenciasRespuesta = new ArrayList();
            secuenciasRespuesta.add(s);
            respuesta = getParser().generarRespuesta(secuenciasRespuesta, getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_LISTA_NEGRA_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), ln, secuencias);
            return respuesta;
        } catch (Exception ex) {
            try {
                return getParser().generarRespuesta(new ArrayList(), getConstanteBean().getConstante(Constantes.CMD_ESTA_EN_LISTA_NEGRA_POR_LOGIN), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0039, new ArrayList());
            } catch (Exception e) {
                Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, e);
                return null;
            }
        }
    }

    public boolean estaEnListaNegraPorCorreo(String correo) {
        Lista_Negra listaNegra = getLista_NegraFacade().findEstudianteByCorreo(correo);
        return (listaNegra != null)?true:false;
    }

    @Override
    public String agregarAListaNegra(String comando) {
        Collection<Secuencia> secuencias = new Vector<Secuencia>();
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String login = correo.split("@")[0];
            String motivo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MOTIVO)).getValor();
            Secuencia secuenciaTemporal = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMPORAL));
            boolean temporal = false;
            if (secuenciaTemporal != null) {
                temporal = Boolean.valueOf(secuenciaTemporal.getValor());
            }
            //Verifica que el estudiante 'exista' en nuestra base de datos
            /*Aspirante asp=afl.findByCorreo(correo);
            if(asp==null){
            Secuencia sec=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE, login);
            sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE, Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE));
            secuencias.add(sec);
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_A_LISTA_NEGRA, getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE, Mensajes.MSJ_0071, secuencias);
            }*/
            Lista_Negra forajido = new Lista_Negra();
            Calendar c = Calendar.getInstance();
            forajido.setFechaIngreso(new Date(c.getTimeInMillis()));
            forajido.setRazonIngreso(motivo);
            try{
                String codigo = AccesoLDAP.obtenerCodigo(login);
                forajido.setCodigo(codigo);
            }catch(Exception e){
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_A_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0095, secuencias);
            }
            
            forajido.setLogin(correo);
            forajido.setTemporal(temporal);
            Lista_Negra esta = getLista_NegraFacade().findEstudianteByCorreo(correo);
            if (esta != null) {
                forajido = esta;
                forajido.setTemporal(temporal);
                forajido.setFechaIngreso(new Date(c.getTimeInMillis()));
                forajido.setRazonIngreso(motivo);
                getLista_NegraFacade().edit(forajido);

                Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
                sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE));
                secuencias.add(sec);
                String respuesta = getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_A_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0024, secuencias);
                return respuesta;
            }
            getLista_NegraFacade().edit(forajido);
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
            sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE));
            secuencias.add(sec);
            return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_AGREGAR_A_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0024, secuencias);
        } catch (Exception ex) {
            Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @Override
    public void agregarAListaNegraPorNota(String correo)
    {
        Lista_Negra esta = getLista_NegraFacade().findEstudianteByCorreo(correo);
        if(esta == null)
        {
            Lista_Negra estudiante = new Lista_Negra();
            estudiante.setLogin(correo);
            String login = correo.split("@")[0];
            try {
                estudiante.setCodigo(AccesoLDAP.obtenerCodigo(login));
            } catch (NamingException ex) {
                Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            Calendar c = Calendar.getInstance();
            estudiante.setFechaIngreso(new Date(c.getTimeInMillis()));
            estudiante.setTemporal(true);
            estudiante.setRazonIngreso("Nota inferior a 2.5 en última monitoría");
            getLista_NegraFacade().create(estudiante);
        }
        
    }

    @Override
    public String eliminarDeListaNegra(String comando) {
        try {
            getParser().leerXML(comando);
            String correo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
            String login = correo.split("@")[0];
            Lista_Negra redimido = getLista_NegraFacade().findEstudianteByCorreo(correo);
            //Respuesta del comando
            Collection<Secuencia> secuencias = new Vector<Secuencia>();
            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO_MENSAJE), login);
            sec.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_LOGIN_ESTUDIANTE));
            secuencias.add(sec);
            String respuesta = null;
            if (redimido != null) {
                Aspirante asp = getAspiranteFacade().findByCorreo(correo);
                if (asp != null) {
                    //Si tiene solicitudes se cancelan
                    Collection<Solicitud> sols = getSolicitudFacade().findByLogin(correo);
                    for (Iterator<Solicitud> it = sols.iterator(); it.hasNext();) {
                        Solicitud solicitud = it.next();
                        getSolicitudFacade().remove(solicitud);
                    }
                }
                getLista_NegraFacade().remove(redimido);
                respuesta = getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_DE_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0025, secuencias);
            } else {
                respuesta = getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_DE_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0089, secuencias);

            }
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, ex);
            try {
                return getParser().generarRespuesta(new Vector(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_DE_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.ERR_0041, new Vector());
            } catch (Exception ex1) {
                Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public String darListaNegra(String comando) {
        try {
            String respuesta = "";
            List<Lista_Negra> lista = getLista_NegraFacade().findAll();
            Collection<Secuencia> secuencias = new Vector<Secuencia>();
            Secuencia estudiantes = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTES), "");
            for (Lista_Negra ln : lista) {
                Secuencia estudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
                Secuencia informacionPersonal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_PERSONAL), "");
                String login = ln.getLogin().split("@")[0];
                //Aspirante asp=afl.findByCodigo(ln.getCodigo());
                informacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), AccesoLDAP.obtenerNombres(login)));
                informacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), AccesoLDAP.obtenerApellidos(login)));
                informacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), AccesoLDAP.obtenerCodigo(login)));
                informacionPersonal.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), ln.getLogin()));
                estudiante.agregarSecuencia(informacionPersonal);
                estudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_MOTIVO), ln.getRazonIngreso()));
                estudiante.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_EN_LISTA_NEGRA), ln.getFechaIngreso().toString()));
                estudiantes.agregarSecuencia(estudiante);
            }
            secuencias.add(estudiantes);
            respuesta = getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_LISTA_NEGRA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0048, new Vector());
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(ListaNegraBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @Override
    public void eliminarTemporalesDeListaNegra() {
        Collection<Lista_Negra> temporales = getLista_NegraFacade().findEstudiantesTemporales();
        for (Lista_Negra lista_Negra : temporales) {
            Date fechaIngreso = lista_Negra.getFechaIngreso();
            long tiempoActual = System.currentTimeMillis();
            long tiempoIngreso = fechaIngreso.getTime();
            double diferenciaMeses = (tiempoActual - tiempoIngreso) / (2.592 * Math.pow(10, 9));
            double valNumeroMesesEnListaNegra = Double.parseDouble(getConstanteBean().getConstante(Constantes.VAL_NUMERO_MESES_EN_LISTA_NEGRA));
            if (diferenciaMeses >= valNumeroMesesEnListaNegra) {
                getLista_NegraFacade().remove(lista_Negra);
            }
        }
    }

    /**
     * Retorna Lista_NegraFacade
     * @return lista_NegraFacade Lista_NegraFacade
     */
    private Lista_NegraFacadeLocal getLista_NegraFacade() {
        return lista_NegraFacade;
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

    /**
     * Retorna SolicitudFacade
     * @return solicitudFacade SolicitudFacade
     */
    private SolicitudFacadeLocal getSolicitudFacade() {
        return solicitudFacade;
    }
}
