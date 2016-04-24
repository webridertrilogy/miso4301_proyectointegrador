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

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteLocal;
import java.io.StringWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class ComandoWriter implements Serializable{

    /**
     * Documento principal
     */
    private Document documento;

    /**
     * Elemento raiz del comando
     */
    private Element root;

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteLocal constanteBean;

    /**
     * Crea un nuevo comando en el documento actual
     * @throws java.lang.Exception
     */
    public void crearComando()throws Exception{
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        documento = builder.newDocument();
        root = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_COMANDO));
        documento.appendChild(root);
    }

    /**
     * Cambia el nombre del comando al que se recibe por parametro
     * @param nombre Nuevo nombre del comando
     */
    public void setNombreComando(String nombre){
        NodeList nodes = root.getElementsByTagName(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
        if(nodes.getLength() > 0){
            nodes.item(0).setTextContent(nombre);
        }else{
            Element elemento = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
            elemento.setTextContent(nombre);
            root.appendChild(elemento);
        }
    }

    /**
     * Cambia el rol del comando al que se recibe por parametro
     * @param rol Nuevo rol del comando
     */
   public void setRol(String rol){
        NodeList nodes = root.getElementsByTagName(getConstanteBean().getConstante(Constantes.TAG_ROL));
        if(nodes.getLength() > 0){
            nodes.item(0).setTextContent(rol);
        }else{
            Element elemento = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_ROL));
            elemento.setTextContent(rol);
            root.appendChild(elemento);
        }
    }

    /**
     * Cambia el usuario del comando al que se recibe por parametro
     * @param usuario Nuevo usuario del comando
     */
   public void setUsuario(String usuario){
        NodeList nodes = root.getElementsByTagName(getConstanteBean().getConstante(Constantes.TAG_USUARIO));
        if(nodes.getLength() > 0){
            nodes.item(0).setTextContent(usuario);
        }else{
            Element elemento = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_USUARIO));
            elemento.setTextContent(usuario);
            root.appendChild(elemento);
        }
    }

   /**
    * Cambia el tipo del comando al que se recibe por parametro
    * @param tipo Nuevo tipo del comando
    */
    public void setTipoComando(String tipo){
        NodeList nodes = root.getElementsByTagName(getConstanteBean().getConstante(Constantes.TAG_TIPO_COMANDO));
        if(nodes.getLength() > 0){
            nodes.item(0).setTextContent(tipo);
        }else{
            Element elemento = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_TIPO_COMANDO));
            elemento.setTextContent(tipo);
            root.appendChild(elemento);
        }
    }

    /**
     * Metodo que interpreta una secuencia que recibe como parametro y retorna <br>
     * un Element que contiene toda la informacion de la secuencia
     * @param sec secuencia a interpretar
     * @return Element con la informacion de la secuencia
     */
    private Element interpretarSecuencia(Secuencia sec){
         System.out.println("aaa="+ sec.toString());
        Element e = documento.createElement(sec.getNombre());
        e.setTextContent(sec.getValor());
        ArrayList<Atributo> atributos = sec.getAtributos();
        for (Atributo atributo : atributos) {
            e.setAttribute(atributo.getNombre(), atributo.getValor());
        }
        ArrayList<Secuencia> secuencias = sec.getSecuencias();
        for (Secuencia secuencia : secuencias) {
            Element el = interpretarSecuencia(secuencia);
            e.appendChild(el);
        }
        return e;
    }

    /**
     * Agrega una lista de secuencias a los parametros del comando
     * @param secuencias Lista de secuencias a agregar
     */
    public void agregarParametros(ArrayList<Secuencia> secuencias){
        NodeList nodes = root.getElementsByTagName(getConstanteBean().getConstante(Constantes.TAG_PARAMETROS));
        Node nodo = null;
        if(nodes.getLength() > 0){
            nodo = nodes.item(0);
        }else{
            Element elemento = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_PARAMETROS));
            root.appendChild(elemento);
            nodo = elemento;
        }
        for (Secuencia secuencia : secuencias) {
            Element parametro = interpretarSecuencia(secuencia);
            nodo.appendChild(parametro);
        }
    }

    /**
     * Genera un comando basado en la informacion almacenada
     * @return xml con el comando
     * @throws java.lang.Exception en caso de encontrar errores con la traduccion
     */
    public String generateComando() throws Exception{
            Source source = new DOMSource(root);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
    }

        /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }
}
