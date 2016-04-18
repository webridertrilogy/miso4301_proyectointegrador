
package co.uniandes.sisinfo.serviciosnegocio;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.comun.constantes.Mensajes;
import co.uniandes.sisinfo.serviciosfuncionales.PeriodoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author david
 */
@Stateless
public class PeriodoBean implements PeriodoRemote, PeriodoLocal {

    @EJB
    private PeriodoFacadeLocal periodoFacade;

    private ConstanteRemote constanteBean;

    private ServiceLocator serviceLocator;

    private ParserT parser;

    public PeriodoBean(){
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(PeriodoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *  Este servicio retorna 5 periodos académicos en todo momento:
     *  Si es el año YYYY, debe retornar como periodos académicos, 
     *  dos períodos antes y dos períodos posteriores. Por ejemplo, 
     *  si el periodo actual es YYYY10: (YYYY-1)19, (YYYY-1)20, YYYY10, YYYY19, YYYY20.
     * @param comando
     * @return
     */
    public String retornarPeriodosAcademicos(String comando) {

        Date d=new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String periodoA=sdf.format(d);
        Integer anio=new Integer(periodoA.split("-")[0]);
        Integer mes=new Integer(periodoA.split("-")[1]);
        String[] periodos=new String[5];
        if(mes<=5){
            periodos[0]=anio-1+"19";
            periodos[1]=anio-1+"20";
            periodos[2]=anio+"10";
            periodos[3]=anio+"19";
            periodos[4]=anio+"20";
        }
        if(mes>5){
            periodos[0]=anio-1+"20";
            periodos[1]=anio+"10";
            periodos[2]=anio+"19";
            periodos[3]=anio+"20";
            periodos[4]=anio+1+"10";
        }
        if(mes>7){
            periodos[0]=anio+"10";
            periodos[1]=anio+"19";
            periodos[2]=anio+"20";
            periodos[3]=anio+1+"10";
            periodos[4]=anio+1+"19";
        }
        ArrayList<Secuencia> secuencias=new ArrayList<Secuencia>();
        Secuencia secPeriodos=new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS), "");
        for(int i=0;i<periodos.length;i++){
            secPeriodos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO),periodos[i]));
        }
        secuencias.add(secPeriodos);
        try {
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETORNAR_PERIODOS_ACADEMICOS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(PeriodoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String retornarPeriodosRango(String comando) {
        //sufijos de períodos
        String[] sufijoPeriodos = new String[]{"10", "19", "20"};

        //cálculo de fecha actual
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat sdfA = new SimpleDateFormat("yyyy");
        Integer anioActual = new Integer(sdfA.format(d));
        SimpleDateFormat sdfM = new SimpleDateFormat("MM");
        Integer mesActual = new Integer(sdfM.format(d));
        
        //calcula sufijo del período actual de acuerdo al mes
        int indiceSufijoPeriodoActual = 0;



        if (mesActual >= 8) { //desde agosto
            indiceSufijoPeriodoActual = 2;
        } else if(mesActual >= 6) { //desde junio
            indiceSufijoPeriodoActual = 1;
        }

        System.out.println("+++++++++++"+indiceSufijoPeriodoActual);

        try {
            getParser().leerXML(comando);

            //parámetros
            Secuencia secPerAnt =  getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS_ANTERIORES));
            Integer periodosAnt = secPerAnt.getValorInt();
            Secuencia secPerPost =  getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS_POSTERIORES));
            Integer periodosPost = secPerPost.getValorInt();
            Secuencia secIncluyeInter =  getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INCLUYE_INTERSEMESTRALES));
            Boolean incluyeInter = Boolean.valueOf(secIncluyeInter.getValor());

            //listado de salida
            ArrayList<String> periodos = new ArrayList<String>();

            //si NO se incluyen períodos intersemestrales y el semestre actual ES un intersemestral
            //se establece como semestre actual el primer semestre de año
            if (!incluyeInter  && indiceSufijoPeriodoActual == 1) {
                indiceSufijoPeriodoActual = 0;
            }
            System.out.println("*********************");
            //generación de períodos anteriores
            int i = 0;
            int iAnio = anioActual;
            int iPeriodoActual = indiceSufijoPeriodoActual;
            while (i <= periodosAnt) {
                //evita los períodos intersemestrales
                if (!incluyeInter && iPeriodoActual == 1) {
                    iPeriodoActual = 0;
                    continue;
                } else {
                    periodos.add(0, String.valueOf(iAnio).concat(sufijoPeriodos[iPeriodoActual]));
                }
                //se mueve un año atrás
                if (iPeriodoActual == 0) {
                    iAnio--;
                }
                iPeriodoActual = Math.abs(iPeriodoActual + sufijoPeriodos.length - 1) % sufijoPeriodos.length;
                i++;
            }

            //generación de períodos posteriores
            int j = 0;
            iAnio = anioActual;
            if(indiceSufijoPeriodoActual==2){
                iAnio++;
            }
            iPeriodoActual = (indiceSufijoPeriodoActual + 1) % sufijoPeriodos.length;
            while (j < periodosPost) {
                //evita los períodos intersemestrales
                if (!incluyeInter && iPeriodoActual == 1) {
                    iPeriodoActual = 2;
                    continue;
                } else {
                    periodos.add(String.valueOf(iAnio).concat(sufijoPeriodos[iPeriodoActual]));
                }
                //avanza un año
                System.out.println(iPeriodoActual+"-"+(sufijoPeriodos.length - 1));
                if (iPeriodoActual == (sufijoPeriodos.length - 1)) {
                    iAnio++;
                }
                iPeriodoActual = Math.abs(iPeriodoActual + 1) % sufijoPeriodos.length;
                j++;
            }

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secPeriodos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODOS), "");
            for(String periodo : periodos){
                secPeriodos.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO), periodo));
            }
            secuencias.add(secPeriodos);
        
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_RETORNAR_PERIODOS_RANGO), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), Mensajes.MSJ_0139, new ArrayList());
        } catch (Exception ex) {
            Logger.getLogger(PeriodoBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private ConstanteRemote getConstanteBean(){
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
}
