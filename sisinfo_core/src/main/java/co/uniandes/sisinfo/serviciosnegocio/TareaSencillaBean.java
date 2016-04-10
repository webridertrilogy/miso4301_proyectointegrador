
/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Carlos Morales
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;


import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.AlertaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.TareaSencillaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.seguridad.RolFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Administración de tareas sencillas
 */
@Stateless
@EJB(name = "TareaSencillaBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.TareaMultipleLocal.class)
public class TareaSencillaBean implements TareaSencillaLocal, TareaSencillaRemote {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private AlertaMultipleFacadeLocal alertaMultipleFacade;
    @EJB
    private AlertaMultipleLocal alertaMultipleBean;
    @EJB
    private RolFacadeLocal rolFacade;
    @EJB
    private TareaSencillaFacadeLocal tareaSencillaFacade;

    private ServiceLocator serviceLocator;

    private ConversorTareaMultiple conversorTareaMultiple;

    public TareaSencillaBean()
    {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(TareaSencillaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public TareaSencilla crearTareaSencilla(String mensaje,HashMap<String,String> parametros,String tipo){
        TareaSencilla ts = new TareaSencilla();
        ts.setEstado(constanteBean.getConstante(Constantes.ESTADO_TAREA_PENDIENTE));
        ts.setMensajeCorreo(mensaje);
        ts.setMensajeDescripcion(mensaje);
        Collection<Parametro> params = new ArrayList();
        for (String key : parametros.keySet()) {
            Parametro p = new Parametro();
            p.setCampo(key);
            p.setValor(parametros.get(key));
            params.add(p);
        }
        ts.setParametros(params);
        ts.setTipo(tipo);
        tareaSencillaFacade.create(ts);
        return ts;
    }

    @Override
    public void realizarTareaPorId(Long id){
        TareaSencilla tarea = tareaSencillaFacade.findById(id);
        tarea.setEstado(constanteBean.getConstante(Constantes.ESTADO_TAREA_TERMINADA));
        tareaSencillaFacade.edit(tarea);
    }

   

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
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

    public void realizarTareasPorTipoYParametros(String tipo, Properties listaParametros){
        Collection<TareaSencilla> tareasSencillas = tareaSencillaFacade.findByTipo(tipo);

        for (TareaSencilla tarea : tareasSencillas) {
            boolean esTareaBuscada = false;
            int cantidadParametros = listaParametros.size();
            Collection<Parametro> parametros = tarea.getParametros();
            Iterator<Parametro> iteradorTarea = parametros.iterator();
            int contador = 0;
            boolean continuar = true;
            while (iteradorTarea.hasNext() && continuar) {
                Parametro parametro = iteradorTarea.next();
                if (listaParametros.containsKey(parametro.getCampo().toString()) && listaParametros.getProperty(parametro.getCampo())!=null && listaParametros.getProperty(parametro.getCampo()).equals(parametro.getValor())) {
                    contador++;
                } else {
                    continuar = false;
                }
            }
            if (contador == cantidadParametros) {
                esTareaBuscada = true;
            }
            if(esTareaBuscada){
                tarea.setEstado(getConstanteBean().getConstante(Constantes.ESTADO_TAREA_TERMINADA));
                tareaSencillaFacade.edit(tarea);
            }
        }

    }

}
