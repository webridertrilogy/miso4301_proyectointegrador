/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.Periodicidad;
import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Atributo;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Asistente
 */
public class ConversorServiciosSoporteProcesos {

    @EJB
    private ConstanteRemote constanteBean;

    public ConversorServiciosSoporteProcesos() {
        try{
            ServiceLocator serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ConversorServiciosSoporteProcesos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public Secuencia pasarSesionesASecuencia(Collection<Sesion> sesiones){
        Secuencia secSesiones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SESIONES), "");
        
        for (Sesion sesion : sesiones) {

            Secuencia sec = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SESION), "");

            Secuencia horario = pasarDiasCompletosASecuencia(sesion.getDias());
            sec.agregarSecuencia(horario);

            if (sesion.getSalon() != null) {
                Secuencia secSalon = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SALON), sesion.getSalon());
                sec.agregarSecuencia(secSalon);
            }            

            secSesiones.agregarSecuencia(sec);

        }
        return secSesiones;
    }

    /**
     * Pasa de dias completos a secuencia.
     * @param sesiones
     * @return
     */
    public Secuencia pasarDiasCompletosASecuencia(Collection<DiaCompleto> dias) {
        Secuencia horario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO), "");
        
        Secuencia sec = null;
        for (DiaCompleto diaCompleto : dias) {

            sec = pasarDiaCompletoASecuencia(diaCompleto);
            if (sec!=null)
                horario.agregarSecuencia(sec);
        }
        return horario;
    }

    /**
     * Pasa de dias completos a secuencia sin incluir las horas ocupadas por monitorias.
     * @param sesiones
     * @return
     */
    public Secuencia pasarDiasCompletosASecuenciaSinMonitorias(Collection<DiaCompleto> dias) {
        Secuencia horario = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO), "");

        Secuencia sec = null;
        for (DiaCompleto diaCompleto : dias) {

            sec = pasarDiaCompletoASecuenciaSinMonitorias(diaCompleto);
            if (sec!=null)
                horario.agregarSecuencia(sec);
        }
        return horario;
    }

        /**
     * Retorna una secuencia TAG_PARAM_FRANJAS_OCUPADAS con el dia que llega por
     * parametro o null si el dia no tiene franjas ocupadas
     * @param dia
     * @return
     */
    public Secuencia pasarDiaCompletoASecuencia(DiaCompleto dia){

        Secuencia secuenciaFranjas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FRANJAS_OCUPADAS),"");
        secuenciaFranjas.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_DIA),dia.getDia_semana()));
        String strHoras = dia.getHoras();
        char ant = '0';
        int index = 0;
        for (int i = 0; i < strHoras.length(); i++) {
            char c = strHoras.charAt(i);
            if (c != '0'){
                if(ant=='0'){
                    // Es el inicio de una franja
                    index = i;
                }
            }else{
                if (ant!='0'){
                    // Es el fin de una franja
                    secuenciaFranjas.agregarSecuencia(crearFranjaConInicioYFin(index, i));
                }
            }
            ant = c;
        }
        if(ant != '0')
            secuenciaFranjas.agregarSecuencia(crearFranjaConInicioYFin(index, strHoras.length()));

        return secuenciaFranjas;
    }

    /**
     * Retorna una secuencia TAG_PARAM_FRANJAS_OCUPADAS con el dia que llega por
     * parametro o null si el dia no tiene franjas ocupadas, sin considerar el
     * horario de las monitorias
     * @param dia
     * @return
     */
    public Secuencia pasarDiaCompletoASecuenciaSinMonitorias(DiaCompleto dia){
        
        Secuencia secuenciaFranjas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FRANJAS_OCUPADAS),"");
        secuenciaFranjas.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_DIA),dia.getDia_semana()));
        String strHoras = dia.getHoras();
        char ant = '0';
        int index = 0;
        for (int i = 0; i < strHoras.length(); i++) {
            char c = strHoras.charAt(i);
            if (c == '1'){
                if(ant!='1'){
                    // Es el inicio de una franja
                    index = i;
                }
            }else{
                if (ant=='1'){
                    // Es el fin de una franja
                    secuenciaFranjas.agregarSecuencia(crearFranjaConInicioYFin(index, i));
                }
            }
            ant = c;
        }
        if(ant == '1')
            secuenciaFranjas.agregarSecuencia(crearFranjaConInicioYFin(index, strHoras.length()));

        return secuenciaFranjas;
    }

    /**
     * Genera la secuencia de una franja ocupadaespecifica, basado en las horas
     * de inicio y fin de la franja
     * @param inicio
     * @param fin
     * @return
     */
    private Secuencia crearFranjaConInicioYFin(int inicio, int fin){
        Secuencia secuencia = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FRANJA_OCUPADA), "");
        int tInicio = inicio*30;
        int tFin = fin*30;
        secuencia.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_HORA_INICIO),""+tInicio/60));
        secuencia.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_MINUTO_INICIO),""+tInicio%60));
        secuencia.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_HORA_FIN),""+tFin/60));
        secuencia.agregarAtributo(new Atributo(getConstanteBean().getConstante(Constantes.ATR_MINUTO_FIN),""+tFin%60));
        
        return secuencia;
    }

    /**
     * Recibe una secuencia con el tag TAG_PARAM_FRANJAS_OCUPADAS y construye un
     * dia completo utilizando esta secuencia
     * @param secuencia
     * @return
     */
    public DiaCompleto pasarSecuenciaADiaCompleto(Secuencia secuencia){
        DiaCompleto diaCompleto = new DiaCompleto();
        StringBuilder bldr = new StringBuilder("");
        for (int i = 0; i < 48; i++) {
            bldr.append("0");
        }
        Collection<Atributo> atrs = secuencia.getAtributos();
        for (Atributo atributo : atrs) {
            if(atributo.getNombre().equals(getConstanteBean().getConstante(Constantes.ATR_DIA))){
                diaCompleto.setDia_semana(atributo.getValor());
            }
        }
        Collection<Secuencia> secuencias = secuencia.getSecuencias();
        for (Secuencia secFranja : secuencias) {
            Collection<Atributo> atrsFranja = secFranja.getAtributos();
            int hi = 0,hf = 0,mi = 0, mf = 0;
            for (Atributo atributo : atrsFranja) {
                if(atributo.getNombre().equals(getConstanteBean().getConstante(Constantes.ATR_HORA_INICIO))){
                    hi = Integer.parseInt(atributo.getValor().trim());
                }else if(atributo.getNombre().equals(getConstanteBean().getConstante(Constantes.ATR_HORA_FIN))){
                    hf = Integer.parseInt(atributo.getValor().trim());
                }else if(atributo.getNombre().equals(getConstanteBean().getConstante(Constantes.ATR_MINUTO_INICIO))){
                    mi = Integer.parseInt(atributo.getValor().trim());
                }else if(atributo.getNombre().equals(getConstanteBean().getConstante(Constantes.ATR_MINUTO_FIN))){
                    mf = Integer.parseInt(atributo.getValor().trim());
                }
            }
            int init = hi*2 + (mi/30);
            int fin = hf*2 + (mf/30);
            String horas = "";
            for (int i = init; i < fin; i++) {
                horas+="1";
            }
            bldr.replace(init, fin, horas);
        }
        diaCompleto.setHoras(bldr.toString());
        return diaCompleto;
    }

    /**
     *
     * @param secuencia
     * @return
     */
    public Collection<DiaCompleto> pasarSecuenciaADiasCompletos(Secuencia secuencia){
        System.out.println("Nombre secuencia pasarSecuenciaADiasCompletas "+secuencia.getNombre());
        Collection<DiaCompleto> diasCompletos = new ArrayList();
        Collection<Secuencia> secuencias = secuencia.getSecuencias();
        for (Secuencia secFranjas : secuencias) {
            diasCompletos.add(pasarSecuenciaADiaCompleto(secFranjas));
        }
        return diasCompletos;
    }

    public Secuencia pasarPeriodicidadesASecuencia(Collection<Periodicidad> periodicidades){
        Secuencia secPeriodicidades = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDADES),"");
        for (Periodicidad periodicidad : periodicidades) {
            Secuencia secPeriodicidad = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODICIDAD),"");
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE),periodicidad.getNombre());
            Secuencia secValor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR),periodicidad.getValor()+"");
            secPeriodicidad.agregarSecuencia(secNombre);
            secPeriodicidad.agregarSecuencia(secValor);
            secPeriodicidades.agregarSecuencia(secPeriodicidad);
        }
        return secPeriodicidades;
    }

}
