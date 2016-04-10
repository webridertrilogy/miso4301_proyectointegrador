/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class Parser implements Serializable{

    private ComandoScanner scanner;

    private Secuencia secuencia;

    /**
     * Crea un comando basado en un nombre, un tipo, un rol y una lista de secuencias que lo componen
     * @param nombreComando El nombre del comando
     * @param tipo El tipo del comando (proceso o consulta)
     * @param rol El rol del usuario que esta ejecutando en el proceso
     * @param secuencias La lista de secuencias que componen los parametros del comando
     * @return xml con la descripcion del comando
     * @throws java.lang.Exception
     */
    public String crearComando(String nombreComando, String tipo, String rol, ArrayList<Secuencia> secuencias) throws Exception{
        ComandoWriter cw = new ComandoWriter();
        cw.crearComando();
        cw.setNombreComando(nombreComando);
        cw.setRol(rol);
        cw.setTipoComando(tipo);
        cw.agregarParametros(secuencias);
        return cw.generateComando();
    }

    public String crearComandoAuditoria(String nombreComando, String tipo, String rol, String usuario, ArrayList<Secuencia> secuencias) throws Exception{
        ComandoWriter cw = new ComandoWriter();
        cw.crearComando();
        cw.setNombreComando(nombreComando);
        cw.setRol(rol);
        cw.setUsuario(usuario);
        cw.setTipoComando(tipo);
        cw.agregarParametros(secuencias);
        return cw.generateComando();

    }

    /**
     * Lee un xml que contiene la informacion de un comando y guarda la informacion del mismo dentro de la instancia del parser
     * @param xml String con el xml que contiene la informacion del comando
     * @throws java.lang.Exception
     */
    public Secuencia leerRespuesta(String xml) throws Exception{
        RespuestaScanner scanner = new RespuestaScanner();
        secuencia = scanner.leerXML(xml);
        return secuencia;
    }

    /**
     * Obtiene la primera secuencia con el nombre dado por parametro para una respuesta que fue creada previamente.<br>
     * En caso de haber multiples secuencias que tengan el mismo nombre retornara la primera que encuentre
     * <b>Pre:</b> Previamente se llamo al metodo crear res
     * @param nombre Nombre que debe tener la secuencia que se busca
     * @return La secuencia que tenga el nombre especificado por parametro
     */
    public Secuencia obtenerPrimeraSecuencia(String nombre){
        return secuencia.obtenerPrimeraSecuencia(nombre);
    }
    
    /**
     * Genera una respuesta basado en una coleccion de secuencias y un identificador del <br>
     * comando que genero dicha respuesta
     * @param secuencias Las secuencias que componen la respuesta
     * @param identificador El identificador del comando que genero la respuesta
     * @return un xml con toda la informacion de la respuesta
     * @throws java.lang.Exception
     */
    public String crearRespuesta(Collection<Secuencia> secuencias,String nombreComando, String tipo, String idMensaje, Collection<Secuencia> parametros) throws Exception{
        RespuestaWriter rw = new RespuestaWriter();
        return rw.generateRespuesta(secuencias, nombreComando, tipo, idMensaje, parametros);
    }

    /**
     * Lee un xml que contiene la informacion de un comando y guarda la informacion del mismo dentro de la instancia del parser
     * @param xml String con el xml que contiene la informacion del comando
     * @throws java.lang.Exception
     */
    public void leerXML(String xml) throws Exception{
        //System.out.println("esta en Parser servicio constantes en leerXML");
        scanner = new ComandoScanner();
        scanner.leerXML(xml);
    }

    /**
     * Obtiene el valor de alguno de los elementos del comando
     * <b>pre:</b> El metodo leerXML fue llamado sobre un xml de un comando valido
     * @param nombre El nombre del elemento del comando que se desea conocer (nombreComando, tipoComando o rol)
     * @return El valor para el elemento del comando.
     */
    public String obtenerValor(String nombre) {
        //System.out.println("esta en Parser servicio constantes en obtenerValor");
        return scanner.darValor(nombre);
    }
    
    /**
     * Obtiene la secuencia con toda la informacion de un parametro especifico del comando, <br>
     * identificado por el valor que se recibe como parametro
     * @param nombre El nombre de la secuencia
     * @returnLa secuencia con toda la informacion de un parametro
     */
    public Secuencia obtenerSecuencia(String nombre) {
        return scanner.darSecuencia(nombre);
    }
}
