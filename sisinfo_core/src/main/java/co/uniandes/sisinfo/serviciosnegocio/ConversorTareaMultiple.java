/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.bo.TareaPendienteVencidaBO;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.AlertaMultiple;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.entities.TareaSencilla;
import co.uniandes.sisinfo.entities.datosmaestros.Parametro;
import co.uniandes.sisinfo.serviciosfuncionales.AlertaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author Asistente
 */
public class ConversorTareaMultiple {

    private ConstanteRemote constanteBean;
    private AlertaMultipleFacadeLocal alertaMultipleFacade;
    private TareaMultipleLocal tareaMultipleBean;

    public ConversorTareaMultiple(ConstanteRemote constanteBean, AlertaMultipleFacadeLocal alertaMultipleFacade, TareaMultipleLocal tareaMultipleBean) {
        this.constanteBean = constanteBean;
        this.alertaMultipleFacade = alertaMultipleFacade;
        this.tareaMultipleBean = tareaMultipleBean;
    }

    public AlertaMultipleFacadeLocal getAlertaMultipleFacade() {
        return alertaMultipleFacade;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    /**
     * Retorna una colección de secuencias dada una colección de tareas
     * @param tareas Colección de tareas
     * @return Colección de secuencias
     */
    public Collection<Secuencia> getSecuenciasTareasEstado(Collection<TareaMultiple> tareas, String estado) {
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        Secuencia secuenciaTareas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TAREAS), "");
        Iterator<TareaMultiple> iterador = tareas.iterator();
        while (iterador.hasNext()) {
            TareaMultiple tarea = iterador.next();
            TareaSencilla tareaS = buscarTareaConEstado(tarea, estado);
            // Si no hay tarea sencilla entonces ninguna tarea tiene el estado buscado
            if (tareaS == null) {
                continue;
            }
            Secuencia secuenciaTarea = getSecuenciaTarea(tarea, buscarTareaConEstado(tarea, estado));
            secuenciaTareas.agregarSecuencia(secuenciaTarea);
        }
        secuencias.add(secuenciaTareas);
        return secuencias;
    }

    /**
     * Retorna la primera tarea sencilla  que cumple con el estado que viene por parametro
     * @param tarea Tarea multiple padre de la tarea sencilla
     * @param estado Estado buscado
     * @return La primera tarea sencilla con el estado buscado o null en caso de que no exista
     */
    private TareaSencilla buscarTareaConEstado(TareaMultiple tarea, String estado) {
        Collection<TareaSencilla> tareasSencilla = tarea.getTareasSencillas();
        for (TareaSencilla tareaSencilla : tareasSencilla) {
            if (estado == null || tareaSencilla.getEstado().equals(estado)) {
                return tareaSencilla;
            }
        }
        return null;
    }

    /**
     * Retorna una colección de secuencias dada una tarea
     * @param tarea Tarea
     * @return Colección de secuencias
     */
    public Secuencia getSecuenciaTarea(TareaMultiple tarea, TareaSencilla tareaSencilla) {

        AlertaMultiple alerta = alertaMultipleFacade.findByTipo(tarea.getTipo());
        Secuencia secuenciaTarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TAREA), "");
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TAREA), Long.toString(tareaSencilla.getId())));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), alerta.getNombre()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION), alerta.getDescripcion() + tareaMultipleBean.obtenerMensajeTareaDescripcion(tarea, getConstanteBean().getConstante(Constantes.ESTADO_TAREA_PENDIENTE))));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), tareaMultipleBean.calcularEstadoTarea(tarea)));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), tarea.getFechaInicio().toString()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), tarea.getFechaCaducacion().toString()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO), tarea.getTipo()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_COMANDO), tarea.getComando()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), tarea.getAsunto()));
        Secuencia secuenciaParametros = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PARAMETROS_TAREA), "");
        Iterator<Parametro> iterator = tareaSencilla.getParametros().iterator();
        while (iterator.hasNext()) {
            Parametro parametro = iterator.next();
            Secuencia secuenciaParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PARAMETRO_TAREA), "");
            secuenciaParametro.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.TAG_PARAM_CAMPO), parametro.getCampo()));
            secuenciaParametro.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), parametro.getValor()));
            secuenciaParametros.agregarSecuencia(secuenciaParametro);
        }
        secuenciaTarea.agregarSecuencia(secuenciaParametros);
        return secuenciaTarea;
    }

    public Properties convertirSecuenciaAParametros(Secuencia paramsTarea) {
        ArrayList<Secuencia> listaSecuencias = paramsTarea.getSecuencias();
        Iterator<Secuencia> iterador = listaSecuencias.iterator();
        Properties listaParametros = new Properties();
        while (iterador.hasNext()) {
            Secuencia sec = iterador.next();
            ArrayList<Atributo> listaAtributos = sec.getAtributos();
            listaParametros.put(listaAtributos.get(0).getValor(), listaAtributos.get(1).getValor());
        }
        return listaParametros;
    }

    // --------------------------------------------------------------------------------------
    /**
     * Metodo que retorna la secuencia del historial de tareas de una persona buscada por login, usada por la TareaMultipleBean.java
     * para responder a la solicitud de buscar el historial de tareas de una persona dado un login
     * @param tareas: el historial de tareas de un login dado
     * @return la secuencia de las tareas recibidas por parametro
     */
    public Collection<Secuencia> getSecuenciasHistorialTareas(Collection<TareaMultiple> tareas) {
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        Secuencia secuenciaTareas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TAREAS), "");
        Iterator<TareaMultiple> iterador = tareas.iterator();
        while (iterador.hasNext()) {
            TareaMultiple tareamultiple = iterador.next();

            Iterator<TareaSencilla> iteradorsencillo = tareamultiple.getTareasSencillas().iterator();
            while (iteradorsencillo.hasNext()) {
                TareaSencilla tareasencilla = iteradorsencillo.next();
                Secuencia secuenciaTarea = generarSecuenciaHistorialTareas(tareamultiple, tareasencilla);
                secuenciaTareas.agregarSecuencia(secuenciaTarea);
            }
        }
        secuencias.add(secuenciaTareas);
        return secuencias;
    }

    /**
     * Convierte una tarea en secuencia, asignando los parametros necesarios para retornar el historial de tareas
     * dado un login
     * @param tarea: la tarea multiple perteneciente al historial de tareas dado un login
     * @param tareaSencilla una tarea sencilla relacionada con la @tarea multiple perteneciente
     * al historial de tareas dado un login
     * @return la secuencia de la tarea sencilla recibida
     */
    public Secuencia generarSecuenciaHistorialTareas(TareaMultiple tarea, TareaSencilla tareaSencilla) {

        AlertaMultiple alerta = alertaMultipleFacade.findByTipo(tarea.getTipo());
        Secuencia secuenciaTarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TAREA), "");
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TAREA), Long.toString(tareaSencilla.getId())));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), alerta.getNombre()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION), alerta.getDescripcion() ));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), tareaSencilla.getEstado()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO), tarea.getFechaInicio().toString()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), tarea.getFechaCaducacion().toString()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO), tarea.getTipo()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_COMANDO), tarea.getComando()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASUNTO), tareaSencilla.getMensajeDescripcion()));
        return secuenciaTarea;
    }

    public Secuencia getSecuenciasTareasPendientes(Collection<TareaPendienteVencidaBO> tareas){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TAREAS),"");
        for(TareaPendienteVencidaBO tareaPendienteBO: tareas){
            Secuencia secuenciaTarea = getSecuenciaTareaPendiente(tareaPendienteBO);
            secuencia.agregarSecuencia(secuenciaTarea);
        }
        return secuencia;
    }

    public Secuencia getSecuenciaTareaPendiente(TareaPendienteVencidaBO tarea){
        Secuencia secuenciaTarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TAREA), "");
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TAREA), Long.toString(tarea.getIdTareaSencilla())));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION), tarea.getMensaje()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO), tarea.getTipo()));
        secuenciaTarea.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_COMANDO), tarea.getComando()));
        Secuencia secuenciaParametros = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PARAMETROS_TAREA), "");
        Iterator<Parametro> iterator = tarea.getParametros().iterator();
        while (iterator.hasNext()) {
            Parametro parametro = iterator.next();
            Secuencia secuenciaParametro = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PARAMETRO_TAREA), "");
            secuenciaParametro.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.TAG_PARAM_CAMPO), parametro.getCampo()));
            secuenciaParametro.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), parametro.getValor()));
            secuenciaParametros.agregarSecuencia(secuenciaParametro);
        }
        secuenciaTarea.agregarSecuencia(secuenciaParametros);
        return secuenciaTarea;
    }
}
