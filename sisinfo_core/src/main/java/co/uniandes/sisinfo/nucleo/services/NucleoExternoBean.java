package co.uniandes.sisinfo.nucleo.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteLocal;
import co.uniandes.sisinfo.serviciosnegocio.ProyectoDeGradoBeanLocal;
import co.uniandes.sisinfo.serviciosnegocio.ReporteExcepcionesBeanLocal;
import co.uniandes.sisinfo.serviciosnegocio.Tesis1BeanLocal;
import co.uniandes.sisinfo.serviciosnegocio.Tesis2BeanLocal;

/**
 * Servicios Núcleo Externo
 * @author Marcela Morales
 */
@Stateless
public class NucleoExternoBean implements NucleoExternoLocal {

    private ParserT parserBean;
   
    @EJB
    private ConstanteLocal constanteBean;
    @EJB
    private ProyectoDeGradoBeanLocal proyectoGradoBean;
    @EJB
    private Tesis1BeanLocal tesis1Bean;
    @EJB
    private Tesis2BeanLocal tesis2Bean;
    @EJB
    private ReporteExcepcionesBeanLocal reporteExcepcionesBean;

    public NucleoExternoBean() {
        parserBean = new ParserT();
    }

    @Override
    public String resolverComandoExterno(String comandoXML) throws Exception {
        System.out.println("------------>NUCLEOBEAN EXTERNO COMANDO: " + comandoXML);

        String respuesta = getConstanteBean().getConstante(Constantes.MSJ_TIPO_COMANDO_INVALIDO);
        parserBean.leerXML(comandoXML);
        String nombreComando = parserBean.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
        String tipoComando = parserBean.obtenerValor(getConstanteBean().getConstante(Constantes.TAG_TIPO_COMANDO));
        try {
            if (!nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_DATOS_BASICOS_SESION))) 
                Logger.getLogger(NucleoExternoBean.class.getName()).log(Level.INFO, comandoXML);
        } catch (Exception e) { }

        if (tipoComando.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_CONSULTA))) {
            respuesta = enRutarComandoConsulta(nombreComando, comandoXML);
        } else if (tipoComando.equals(getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_CMD_PROCESO))) {
            parserBean.leerXML(comandoXML);
//            respuesta = despachadorBean.resolverComando(comandoXML);
//            reportarALogExcepciones(despachadorBean.getClass().getName(), "resolverComando", respuesta, new Timestamp(new Date().getTime()), nombreComando , comandoXML);
        } else {
            respuesta = getConstanteBean().getConstante(Constantes.MSJ_TIPO_COMANDO_INVALIDO);
        }
        System.out.println("------------>NUCLEOBEAN COMANDO EXTERNO RESP: " + respuesta);
        try {
            Logger.getLogger(NucleoExternoBean.class.getName()).log(Level.INFO, respuesta);
        } catch (Exception e) { }
        return respuesta;
    }

    /**
     * Enruta el comando de consulta y retorna la respuesta de acuerdo al servicio pedido
     * @param nombreComando Nombre del comando
     * @param comandoXML Comando en XML
     * @return Respuesta al servicio
     * @throws java.lang.Exception
     */
    private String enRutarComandoConsulta(String nombreComando, String comandoXML) throws Exception {
        String respuesta = getConstanteBean().getConstante(Constantes.MSJ_CONSULTA_INVALIDA);
        Timestamp fa = new Timestamp(new Date().getTime());
        if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_PROYECTOS_DE_GRADO_PARA_EXTERNOS))) {
            respuesta = proyectoGradoBean.consultarProyectosDeGradoParaExternos(comandoXML);
            reportarALogExcepciones(proyectoGradoBean.getClass().getCanonicalName(), "consultarProyectosDeGradoParaExternos", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS1_EXTERNOS))) {
            respuesta = tesis1Bean.consultaExternaTesis1(comandoXML);
            reportarALogExcepciones(tesis1Bean.getClass().getCanonicalName(), "consultaExternaTesis1", respuesta, fa, nombreComando, comandoXML);
        } else if (nombreComando.equals(getConstanteBean().getConstante(Constantes.CMD_DAR_SOLICITUDES_TESIS_2_ASESOR_CONSULTA_EXTERNOS))) {
            respuesta = tesis2Bean.consultarTesisProfesorParaExternos(comandoXML);
            reportarALogExcepciones(tesis2Bean.getClass().getCanonicalName(), "consultarTesisProfesorParaExternos", respuesta, fa, nombreComando, comandoXML);
        } else {
            System.err.println("COMANDO EXTERNO NO ENCONTRADO: " + nombreComando);
            respuesta = getConstanteBean().getConstante(Constantes.MSJ_CONSULTA_INVALIDA);
            reportarALogExcepciones(constanteBean.getClass().getCanonicalName(), "getConstante", respuesta, fa, nombreComando, comandoXML);
        }
        return respuesta;
    }

    private void reportarALogExcepciones(String nombreBean, String nombreMetodo, String respuesta, Timestamp fa, String nombreComanddo , String xmlEntrada) {
        System.out.println("Entro a reportar log excepciones");
        reporteExcepcionesBean.crearLogMensaje(nombreBean, nombreMetodo, respuesta, fa, nombreComanddo, xmlEntrada);
    }

    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }
}
