/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.despachador.services;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import co.uniandes.sisinfo.serviciosnegocio.ConvocatoriaRemote;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author ale-osor
 */
@Stateless
public class DespachadorBean implements DespachadorRemote, DespachadorLocal {
   
    private ParserT parserBean;
    @EJB
    private ColaConvocatoriaLocal colaConvocatoriaBean;
    @EJB
    private ColaCarteleraLocal  colaCarteleraBean;
    @EJB
    private ColaListaNegraLocal colaListaNegraBean;
    @EJB
    private ColaPreseleccionLocal colaPreseleccionBean;
    @EJB
    private ColaConfirmacionLocal colaConfirmacionBean;
    @EJB
    private ColaConvenioLocal colaConvenioBean;
    @EJB
    private ColaSolicitudLocal  colaSolicitudBean;
    @EJB
    private ColaAspiranteLocal  colaAspiranteBean;
    @EJB
    private ColaArchivosLocal colaArchivosBean;
    @EJB
    private ColaConsultaLocal colaConsultaBean;
    @EJB
    private ColaCredencialLocal colaCredencialBean;
    @EJB
    private ColaReglaLocal colaReglaBean;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private ColaProfesorLocal colaProfesorBean;
    @EJB
    private ConvocatoriaRemote convocatoriaBean;

    private ServiceLocator serviceLocator;

    public DespachadorBean(){
        try {
            parserBean = new ParserT();
            serviceLocator = new ServiceLocator();
            Logger.getLogger("javax.resourceadapter.mqjmsra").setLevel(Level.WARNING);
            convocatoriaBean = (ConvocatoriaRemote) serviceLocator.getRemoteEJB(ConvocatoriaRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(DespachadorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public String resolverComando(String comandoXML) throws Exception {
        String respuesta = getConstanteBean().getConstante(Constantes.MSJ_PROCESO_INVALIDO);

        parserBean.leerXML(comandoXML);
        String nombreComando = parserBean.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
        String tipoComando = parserBean.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_TIPO_COMANDO));
        boolean convocatoriaAbierta=false;
        convocatoriaAbierta = convocatoriaBean.hayConvocatoriaAbierta();

        /*List<Convocatoria> convocatorias=convocatoriaFacade.findAll();
        for(Convocatoria c:convocatorias){
            if (c.getEstado().equals(getConstanteBean().getConstante(Constantes.ESTADO_ABIERTA))){
                convocatoriaAbierta=true;
            }
        }*/

        if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_INICIAR_PERIODO)) ||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ABRIR_CONVOCATORIA))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CERRAR_CONVOCATORIA))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_DATOS_CURSO)))
        {            
            respuesta = colaConvocatoriaBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CARGAR_CARTELERA)))
        {
            // Enviar a la cola de cartelera
            respuesta = colaCarteleraBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_A_LISTA_NEGRA))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_DE_LISTA_NEGRA)))
        {
            // Enviar a la cola de lista negra
            respuesta = colaListaNegraBean.resolverComando(comandoXML);            
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_PRESELECCION)))
        {
            // Enviar a la cola de preseleccion
            
                respuesta = colaPreseleccionBean.resolverComando(comandoXML);
            
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_PRESELECCION))){
            respuesta = colaPreseleccionBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_ESTUDIANTE))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SECCION))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_SOLICITUD_VERIFICACION)))
        {
            
                respuesta = colaConfirmacionBean.resolverComando(comandoXML);
            
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_DEPARTAMENTO))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_ESTUDIANTE))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_REGISTRAR_CONVENIO_PENDIENTE_RADICACION))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_RADICAR_CONVENIOS)))
        {
            // Enviar a la cola de convenio
            respuesta = colaConvenioBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ENVIAR_SOLICITUD))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_SOLICITUD))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CANCELAR_SOLICITUD_POR_CARGA)))
        {
            
                respuesta = colaSolicitudBean.resolverComando(comandoXML);
            
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_ACADEMICOS))||
                nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_ACTUALIZAR_DATOS_PERSONALES_Y_EMERGENCIA)))
        {
            //Enviar a la cola de aspirante
            respuesta = colaAspiranteBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONFIRMAR_SUBIDA_ARCHIVO)))
        {
            //Enviar a la cola de archivos
            respuesta = colaArchivosBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_MODIFICAR_HORARIO)))
        {
            //Enviar a la cola de comandos
            respuesta = colaConsultaBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_CREDENCIAL)))
        {
            respuesta = colaCredencialBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CREAR_REGLA)))
        {
            respuesta = colaReglaBean.resolverComando(comandoXML);
        }
        else if(nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_AGREGAR_PROFESOR)))
        {
            respuesta=colaProfesorBean.resolverComando(comandoXML);
        }

        else
        {
            respuesta = getConstanteBean().getConstante(Constantes.MSJ_PROCESO_INVALIDO);
        }

        return respuesta;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method" or "Web Service > Add Operation")
 
}
