/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.SolicitudFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.datosmaestros.SeccionFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.NamingException;

/**
 *
 * @author Juan Manuel Moreno B.
 */
public class ConversorPreseleccion {

    /**
     * Parser
     */
    private ParserT parser;

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    /**
     *  ServiceLocator
     */
    private ServiceLocator serviceLocator;

    /**
     * SolicitudFacade
     */
    @EJB
    private SolicitudFacadeRemote solicitudFacade;

    /**
     * SeccionFacade
     */
    @EJB
    private SeccionFacadeRemote seccionFacade;

    private Hashtable<String,Collection> seccionesSolicitud;

    /**
     * Constructor de ConversorPreseleccion que recibe un comando
     */
    public ConversorPreseleccion(String xml) throws Exception {

        try {
            seccionesSolicitud = new Hashtable<String, Collection>();
            initConversor();
            getParser().leerXML(xml);
        } catch (NamingException ex) {

            Logger.getLogger(PreseleccionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Inicializa las caracteristicas
     */
    public void initConversor() throws NamingException {

        serviceLocator = new ServiceLocator();
        constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        solicitudFacade = (SolicitudFacadeRemote) serviceLocator.getRemoteEJB(SolicitudFacadeRemote.class);
        seccionFacade = (SeccionFacadeRemote) serviceLocator.getRemoteEJB(SeccionFacadeRemote.class);
    }
    
    /**
     * Da una solicitud dado un comando XML para monitorias T1.
     */
    public Solicitud pasarComandoASolicitudT1() throws Exception {        
        Solicitud solicitud = solicitudFacade.findById(pasarComandoAIdSolicitudT1());
        return solicitud;
    }

    public long pasarComandoAIdSolicitudT1() throws Exception {
        Secuencia secuenciaSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD));
        long idSolicitud =
                Long.parseLong(secuenciaSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor());
        return idSolicitud;
    }

    /**
     * Da una solicitud dado un comando XML.
     */
    public String pasarComandoAResponsable() throws Exception {

        return getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_ROL));
    }

    /**
     * Da un contenedor de solicitudes dado un comando XML para monitorias T2.
     */
    public Collection<Solicitud> pasarComandoASolicitudT2() throws Exception {
        Collection<Solicitud> solicitudes = new ArrayList<Solicitud>();
        
        Secuencia seqSolicitudes = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
        Secuencia seqSolicitud = null;
        Solicitud solicitud = null;
        long idSolicitud = 0;
        for (Secuencia seq : seqSolicitudes.getSecuencias()) {

            seqSolicitud = seq.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD));
            idSolicitud = Long.parseLong(
                    seqSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor());
            solicitud = solicitudFacade.findById(idSolicitud);
            solicitudes.add(solicitud);
        }
        return solicitudes;
    }

    /**
     * Da un contenedor de secciones dado el id de una solicitud.
     */
    public Collection<Seccion> pasarComandoASecciones(String idSolicitud) throws Exception {
        Collection<Seccion> secciones = new ArrayList<Seccion>();
        Secuencia seqSolicitudes = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUDES));
        if(seqSolicitudes!=null){
            Collection<Secuencia> solicitudes = seqSolicitudes.getSecuencias();
            for (Secuencia secuenciaSolicitud : solicitudes) {
                String id = secuenciaSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
                if(idSolicitud.equals(id)){
                    return pasarSecuenciaASecciones(secuenciaSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES)));
                }
            }
        }else{
            Secuencia secuenciaSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SOLICITUD));
            String id = secuenciaSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_ID_SOLICITUD)).getValor();
            if(idSolicitud.equals(id)){
                return pasarSecuenciaASecciones(secuenciaSolicitud.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES)));
            }
        }
        System.out.println("NADA");
        return new ArrayList();
        /*
        ArrayList<Secuencia> seqSecciones =
                getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECCIONES)).getSecuencias();
        Seccion seccion = null;
        for(Secuencia seqSeccion : seqSecciones) { // Constantes.TAG_PARAM_SECCION

            seccion = seccionFacade.findByCRN(seqSeccion.getValor());
            secciones.add(seccion);
        }

        return secciones;*/
    }

    /**
     * Convierte una secuencia de solicitud a una coleccion de sesiones.
     * La secuencia a transformar debe tener un tag SECCIONES
     * @param s
     * @return
     */
    private Collection<Seccion> pasarSecuenciaASecciones(Secuencia secuenciaSolicitud){
        Collection<Seccion> secciones = new ArrayList<Seccion>();
        ArrayList<Secuencia> seqSecciones = secuenciaSolicitud.getSecuencias();
        Seccion seccion = null;
        for(Secuencia seqSeccion : seqSecciones) { // Constantes.TAG_PARAM_SECCION

            seccion = seccionFacade.findByCRN(seqSeccion.getValor());
            secciones.add(seccion);
        }

        return secciones;
    }

    /**
     * Da una respuesta acerca de que una solicitud no existe.
     */
    public Collection<Secuencia> darSecuenciaRespuestaSolicitudNoExiste(String idSolicitud) {
        Collection<Secuencia> secuencias = new Vector<Secuencia>();

        Secuencia seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), idSolicitud);
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_ID_SOLICITUD));
        secuencias.add(seqRespuesta);
        return secuencias;
    }

    /**
     * Da una respuesta acerca de que no hay vacantes.
     */
    public Collection<Secuencia> darSecuenciaRespuestaNoHayVacantes(int numeroSeccion, Profesor profesor) {
        Collection<Secuencia> secuencias = new Vector<Secuencia>();

        Secuencia seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), Integer.toString(numeroSeccion));
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NUMERO_SECCION));
        secuencias.add(seqRespuesta);
        seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), profesor.getPersona().getNombres());
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRE_COMPLETO_PROFESOR));
        secuencias.add(seqRespuesta);
        return secuencias;
    }

    /**
     * Da una respuesta acerca de que hay conflicto de horario.
     */
    public Collection<Secuencia> darSecuenciaRespuestaConflictoHorario(Aspirante aspirante) {
        Collection<Secuencia> secuencias = new Vector<Secuencia>();

        Secuencia seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getNombres());
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRES_ESTUDIANTE));
        secuencias.add(seqRespuesta);
        seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getApellidos());
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_APELLIDOS_ESTUDIANTE));
        secuencias.add(seqRespuesta);
        return secuencias;
    }

    /**
     * Da una respuesta acerca de preseleccion exitosa.
     */
    public Collection<Secuencia> darSecuenciaRespuestaPreseleccionar(Aspirante aspirante, Seccion seccion) {
        Collection<Secuencia> secuencias = new Vector<Secuencia>();

        Secuencia seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getNombres());
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRES_ESTUDIANTE));
        secuencias.add(seqRespuesta);
        seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), aspirante.getPersona().getApellidos());
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_APELLIDOS_ESTUDIANTE));
        secuencias.add(seqRespuesta);
        seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), Integer.toString(seccion.getNumeroSeccion()));
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NUMERO_SECCION));
        secuencias.add(seqRespuesta);
        seqRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAMETRO), seccion.getProfesorPrincipal().getPersona().getNombres());
        seqRespuesta.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_ID_PARAMETRO_MENSAJE), Mensajes.VAL_ATR_PARAMETRO_NOMBRE_COMPLETO_PROFESOR));
        secuencias.add(seqRespuesta);
        return secuencias;
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
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
}
