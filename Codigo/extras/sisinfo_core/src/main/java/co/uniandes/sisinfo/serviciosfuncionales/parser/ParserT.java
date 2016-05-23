/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.parser;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author sil-de
 */
public class ParserT {

        //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private Parser parser;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ParserBean
     */
    public ParserT(){
        parser = new Parser();
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------

    public String crearComando(String nombreComando, String tipo, String rol, ArrayList<Secuencia> secuencias) throws Exception{
        return parser.crearComando(nombreComando, tipo, rol, secuencias);
    }


    public void leerXML(String xml) throws Exception{
        //System.out.println("esta en ParserT servicio constantes en leerXML");
        parser.leerXML(xml);
    }


    public String obtenerValor(String nombre) {
        //System.out.println("esta en ParserT servicio constantes en obtenerValor");
        return parser.obtenerValor(nombre);
    }


    public Secuencia obtenerSecuencia(String nombre) {
        return parser.obtenerSecuencia(nombre);
    }

    public String generarRespuesta(Collection<Secuencia> secuencias,String nombreComando, String tipo, String idMensaje, Collection<Secuencia> parametros)throws Exception{
        return parser.crearRespuesta(secuencias, nombreComando, tipo, idMensaje, parametros);
    }


    public Secuencia obtenerPrimeraSecuencia(String nombre){
        return parser.obtenerPrimeraSecuencia(nombre);
    }

    public Secuencia leerRespuesta(String xml) throws Exception{
        return parser.leerRespuesta(xml);
    }

    public String generarRespuesta(Collection<Secuencia> secuencias,String nombreComando)throws Exception
    {
       return parser.crearRespuesta(secuencias, nombreComando, "TEMP", "TEMP", new ArrayList());
    }
}
