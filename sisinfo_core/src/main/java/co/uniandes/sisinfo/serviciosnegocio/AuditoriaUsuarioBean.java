
package co.uniandes.sisinfo.serviciosnegocio;


import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.entities.AuditoriaUsuario;
import co.uniandes.sisinfo.serviciosfuncionales.AuditoriaUsuarioFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 *
 * @author Paola Gómez
 */
@Stateless
public class AuditoriaUsuarioBean implements AuditoriaUsuarioBeanRemote, AuditoriaUsuarioBeanLocal {

    @EJB
    private AuditoriaUsuarioFacadeLocal auditoriaUsuariosFacade;

    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;
    private ParserT parser;

    public AuditoriaUsuarioBean(){
        try {
            parser = new ParserT();
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(AuditoriaUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearRegistroAuditoriaUsuario(String usuarioActual, String rol, String comando, String parametros, Timestamp fecha, Boolean accionExitosa) {
        AuditoriaUsuario auditoria = new AuditoriaUsuario();
        auditoria.setUsuario(usuarioActual);
        auditoria.setRol(rol);
        auditoria.setComando(comando);
        auditoria.setFecha(fecha);
        auditoria.setParametros(parametros);
        auditoria.setAccionExitosa(accionExitosa);

        auditoriaUsuariosFacade.create(auditoria);
    }

    public void crearRegistroAuditoriaUsuario(String xml, Boolean accionExitosa) {
        try {
            parser.leerXML(xml);
            System.out.println("Esta en AuditoriaUsuarioBean en crearRegistroAuditoriaUsuario, xml tiene:\n\n" + xml + "\n\n");


            String parametros = "";

            if(xml.contains("<" + getConstanteBean().getConstante(Constantes.TAG_PARAMETROS) + ">") == true){
                int rangoI = xml.indexOf("<" + getConstanteBean().getConstante(Constantes.TAG_PARAMETROS) + ">") + ("<" + getConstanteBean().getConstante(Constantes.TAG_PARAMETROS) + ">").length();
                int rangoF = xml.indexOf("</" + getConstanteBean().getConstante(Constantes.TAG_PARAMETROS) + ">");
                parametros = xml.substring(rangoI, rangoF);
            }

            System.out.println("Esta en AuditoriaUsuarioBean en crearRegistroAuditoriaUsuario, parametros tiene :\n\n" + parametros + "\n\n");

            AuditoriaUsuario auditoria = new AuditoriaUsuario();
            auditoria.setUsuario(getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_USUARIO)));
            auditoria.setRol(getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_ROL)));
            auditoria.setComando(getParser().obtenerValor(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO)));
            auditoria.setFecha(new Timestamp(new Date().getTime()));
            auditoria.setParametros(parametros);
            auditoria.setAccionExitosa(accionExitosa);
            
            auditoriaUsuariosFacade.create(auditoria);
        } catch (Exception ex) {
            Logger.getLogger(AuditoriaUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String consultarAuditoriaUsuarioPorUsuarioFechaRolYComando(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);

            String usuario = "";
            String rol = "";
            String comandos = "";
            Date fechaDesde = new Date();
            Date fechaHasta = new Date();

            Secuencia secUsuario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            Secuencia secFechaDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
            Secuencia secFechaHasta = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
            Secuencia secRol = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ROL_AUDITORIA));
            Secuencia secComando = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO_AUDITORIA));

            boolean esBusquedaPorUsuario = secUsuario != null && secUsuario.getValor() != null && !secUsuario.getValor().equals("");
            boolean esBusquedaPorFecha = (secFechaDesde != null && secFechaDesde.getValor() != null && !secFechaDesde.getValor().equals("")) ||
                    (secFechaHasta != null && secFechaHasta.getValor() != null && !secFechaHasta.getValor().equals(""));
            boolean esBusquedaPorFechaDesde = secFechaDesde != null && secFechaDesde.getValor() != null && !secFechaDesde.getValor().equals("") ;
            boolean esBusquedaPorFechaHasta = secFechaHasta != null && secFechaHasta.getValor() != null && !secFechaHasta.getValor().equals("") ;
            boolean esBusquedaPorRol = secRol != null && secRol.getValor() != null && !secRol.getValor().equals("");
            boolean esBusquedaPorComando = secComando != null && secComando.getValor() != null && !secComando.getValor().equals("");

            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'COT' yyyy", new Locale("en"));

            usuario = (esBusquedaPorUsuario == true)?secUsuario.getValor():null;
            rol = (esBusquedaPorRol == true)?secRol.getValor():null;
            fechaDesde = (esBusquedaPorFechaDesde == true)?sdf.parse(secFechaDesde.getValor()):null;
            fechaHasta = (esBusquedaPorFechaHasta == true)?sdf.parse(secFechaHasta.getValor()):new Date();
            comandos = (esBusquedaPorComando == true)?secComando.getValor():null;

            List<AuditoriaUsuario> actividadesAuditoria = new ArrayList<AuditoriaUsuario>();

            if( !esBusquedaPorUsuario && !esBusquedaPorFechaDesde && !esBusquedaPorFechaHasta && !esBusquedaPorRol && !esBusquedaPorComando){
                actividadesAuditoria = auditoriaUsuariosFacade.findAll();
            }else{
                actividadesAuditoria = auditoriaUsuariosFacade.findByAny(usuario, rol, fechaDesde, fechaHasta, comandos);
            }

            Secuencia secActividadesAuditoria = getConversor().crearSecuenciaActividadesAuditoria(actividadesAuditoria);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secActividadesAuditoria);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_AUDITORIA_USUARIO_POR_USUARIO_FECHA_ROL_Y_COMANDO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(AuditoriaUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_AUDITORIA_USUARIO_POR_USUARIO_FECHA_ROL_Y_COMANDO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(AuditoriaUsuarioBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }


    public String consultarAuditoriaUsuarioActividad(String comandoXML) {
        try {
            getParser().leerXML(comandoXML);

            Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
            AuditoriaUsuario actividadAuditoria = auditoriaUsuariosFacade.find(secId.getValorLong());
            Secuencia secActividadAuditoria = getConversor().crearSecuenciaActividadAuditoria(actividadAuditoria);

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            secuencias.add(secActividadAuditoria);

            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_AUDITORIA_USUARIO_ACTIVIDAD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), null, new ArrayList());

        } catch (Exception ex) {
            try {
                Logger.getLogger(AuditoriaUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
                Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
                return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_CONSULTAR_AUDITORIA_USUARIO_ACTIVIDAD), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), Mensajes.COM_ERR_0003, new ArrayList());
            } catch (Exception ex1) {
                Logger.getLogger(AuditoriaUsuarioBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }


    private ConstanteRemote getConstanteBean(){
        return constanteBean;
    }

    private ConversorAuditoriaUsuario getConversor(){
        return new ConversorAuditoriaUsuario(constanteBean);
    }

    /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        return parser;
    }
}
