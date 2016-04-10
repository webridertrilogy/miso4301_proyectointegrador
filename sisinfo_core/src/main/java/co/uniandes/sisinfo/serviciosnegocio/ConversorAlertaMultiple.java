/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.AlertaMultiple;
import co.uniandes.sisinfo.entities.TareaMultiple;
import co.uniandes.sisinfo.serviciosfuncionales.AlertaMultipleFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodicidadFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Asistente
 */
public class ConversorAlertaMultiple {

    private ConstanteRemote constanteBean;

    private ConversorTareaMultiple conversorTareaMultiple;

    AlertaMultipleFacadeLocal alertaMultipleFacade;

    PeriodicidadFacadeLocal periodicidadFacade;
    
    TareaMultipleLocal tareaMultipleBean;

    TimerGenericoBeanRemote timerGenericoBeanRemote;

    public ConversorAlertaMultiple(ConstanteRemote constanteBean,AlertaMultipleFacadeLocal alertaMultipleFacade,TareaMultipleLocal tareaMultipleBean,PeriodicidadFacadeLocal periodicidadFacade,TimerGenericoBeanRemote timerGenericoRemote){
        this.constanteBean = constanteBean;
        this.alertaMultipleFacade = alertaMultipleFacade;
        this.tareaMultipleBean = tareaMultipleBean;
        this.periodicidadFacade = periodicidadFacade;
        this.timerGenericoBeanRemote = timerGenericoRemote;
        conversorTareaMultiple = new ConversorTareaMultiple(constanteBean, alertaMultipleFacade, tareaMultipleBean);
    }



    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public Secuencia getSecuenciaAlertas(List<AlertaMultiple> alertas) {
        Secuencia secuenciaAlertas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTAS), getConstanteBean().getConstante(Constantes.NULL));
        Iterator<AlertaMultiple> iterator = alertas.iterator();
        while (iterator.hasNext()) {
            AlertaMultiple alerta = iterator.next();
            Secuencia secuenciaAlerta = getSecuenciaAlerta(alerta);
            secuenciaAlertas.agregarSecuencia(secuenciaAlerta);
        }
        return secuenciaAlertas;
    }

    public Secuencia getSecuenciaAlerta(AlertaMultiple alerta){
        Secuencia secuenciaAlerta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTA), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ALERTA), Long.toString(alerta.getId())));
        Collection<TareaMultiple> tareas = alerta.getTareas();
        Collection<Secuencia> secuenciasTareas = conversorTareaMultiple.getSecuenciasTareasEstado(tareas, null);
        secuenciaAlerta.agregarSecuencia(secuenciasTareas.iterator().next());
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA), alerta.getTipo()));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDAD), Long.toString(alerta.getPeriodicidad().getValor())));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA), Boolean.toString(alerta.isActiva())));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENVIA_CORREO), Boolean.toString(alerta.isEnviaCorreo())));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERMITE_PENDIENTE), Boolean.toString(alerta.getPermitePendiente()==null?false:alerta.getPermitePendiente())));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), alerta.getNombre()));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), alerta.getDescripcion()));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO), alerta.getComando()));
        boolean existeBD = timerGenericoBeanRemote.timerExisteEnBD(alerta.getIdTimer());
        boolean existeMemoria = timerGenericoBeanRemote.timerExisteEnMemoria(alerta.getIdTimer());
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXISTE_EN_BD),Boolean.toString(existeBD)));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXISTE_EN_MEMORIA), Boolean.toString(existeMemoria)));
        return secuenciaAlerta;
    }

    public Secuencia getSecuenciaAlertasSinTareas(List<AlertaMultiple> alertas) {
        Secuencia secuenciaAlertas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTAS), getConstanteBean().getConstante(Constantes.NULL));
        Iterator<AlertaMultiple> iterator = alertas.iterator();
        while (iterator.hasNext()) {
            AlertaMultiple alerta = iterator.next();
            Secuencia secuenciaAlerta = getSecuenciaAlertaSinTareas(alerta);
            secuenciaAlertas.agregarSecuencia(secuenciaAlerta);
        }
        return secuenciaAlertas;
    }

    public Secuencia getSecuenciaAlertaSinTareas(AlertaMultiple alerta){
        Secuencia secuenciaAlerta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ALERTA), getConstanteBean().getConstante(Constantes.NULL));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_ALERTA), Long.toString(alerta.getId())));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ALERTA), alerta.getTipo()));


        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDAD), Long.toString(alerta.getPeriodicidad().getValor())));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA), Boolean.toString(alerta.isActiva())));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENVIA_CORREO), Boolean.toString(alerta.isEnviaCorreo())));
        boolean existeBD,existeMemoria;
        existeBD = timerGenericoBeanRemote.timerExisteEnBD(alerta.getIdTimer());
        existeMemoria = timerGenericoBeanRemote.timerExisteEnMemoria(alerta.getIdTimer());
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXISTE_EN_BD),Boolean.toString(existeBD)));
        secuenciaAlerta.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXISTE_EN_MEMORIA), Boolean.toString(existeMemoria)));
        return secuenciaAlerta;
    }

    /**
     * Crea una lista de entities de Alerta dadas unas secuencias.
     */
    public List<AlertaMultiple> pasarSecuenciasAAlertas(ArrayList<Secuencia> secuenciasAlertas) {
        List<AlertaMultiple> alertas = new ArrayList<AlertaMultiple>();

        AlertaMultiple alerta = null;
        for (Secuencia tmpSecAlerta : secuenciasAlertas) {

            alerta = pasarSecuenciaAAlerta(tmpSecAlerta);
            alertas.add(alerta);
        }
        return alertas;
    }
    
    /**
     * Crea un entity de Alerta dada una secuencia.
     */
    public AlertaMultiple pasarSecuenciaAAlerta(Secuencia secAlerta) {
        AlertaMultiple alerta = new AlertaMultiple();

        Collection<Secuencia> secuencias = secAlerta.getSecuencias();
        for (Secuencia tmpSec : secuencias) {

            if(tmpSec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_ACTIVA))){

                alerta.setActiva(Boolean.parseBoolean(tmpSec.getValor()));
            } else if(tmpSec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO))){

                alerta.setTipo(tmpSec.getValor());
            } else if(tmpSec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE))){

                alerta.setNombre(tmpSec.getValor());

            } else if(tmpSec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION))){

                alerta.setDescripcion(tmpSec.getValor());
            } else if(tmpSec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDAD))){
                alerta.setPeriodicidad(getPeriodicidadFacade().findByNombre(tmpSec.getValor()));
            } else if(tmpSec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMANDO))){

                alerta.setComando(tmpSec.getValor());
            } else if(tmpSec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_ENVIAR_CORREO_ALERTA))){

                alerta.setEnviaCorreo(Boolean.parseBoolean(tmpSec.getValor()));
            } else if(tmpSec.getNombre().equals(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERMITE_PENDIENTE))){

                alerta.setPermitePendiente(Boolean.parseBoolean(tmpSec.getValor()));
            }
        }
        return alerta;
    }

    /**
     * Retorna PeriodicidadFacade
     * @return periodicidadFacade PeriodicidadFacade
     */
    private PeriodicidadFacadeLocal getPeriodicidadFacade() {
        return periodicidadFacade;
    }

    public void setPeriodicidadFacade(PeriodicidadFacadeLocal periodicidadFacade) {
        this.periodicidadFacade = periodicidadFacade;
    }



}
