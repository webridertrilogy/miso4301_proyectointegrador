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

import java.io.StringReader;
import java.io.Serializable;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 */
public class ComandoScanner implements Serializable{

    /**
     * org.w3c.dom.Document document
     */
    Document document;

    /**
     * Tabla de hash con la informacion propia del comando
     */
    private Hashtable<String,String> hash;

    /**
     * Tabla de hash con la informacion de los parametros del comando
     */
    private Hashtable<String,Secuencia> hashParametros;

    /**
     * Lee un xml que contiene la informacion de un comando y guarda la informacion del mismo dentro de la instancia del parser
     * @param xml String con el xml que contiene la informacion del comando
     * @throws java.lang.Exception
     */
    public void leerXML(String xml) throws Exception{
        //System.out.println("esta en ComandoScanner servicio constantes en leerXML");
        hash = new Hashtable<String,String>();
        hashParametros = new Hashtable<String, Secuencia>();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse (new InputSource(new StringReader(xml)));
        this.document = document;
        visitDocument();
    }

    /**
     * Retorna el valor de alguno de los elementos del comando
     * <b>pre:</b> El metodo leerXML fue llamado sobre un xml de un comando valido
     * @param nombre El nombre del elemento del comando que se desea conocer (nombreComando, tipoComando o rol)
     * @return El valor para el elemento del comando.
     */
    public String darValor(String nombre){
        //System.out.println("Estoy en ComandoScanner servicio constante en darValor con nombre:\t" + nombre + "\n");
        return hash.get(nombre);
    }

    /**
     * Obtiene la secuencia con toda la informacion de un parametro especifico del comando, <br>
     * identificado por el valor que se recibe como parametro
     * @param nombre El nombre de la secuencia
     * @returnLa secuencia con toda la informacion de un parametro
     */
    public Secuencia darSecuencia(String nombre){
        return hashParametros.get(nombre);
    }

    /**
     * Navega por la estructura del documento ubicando los tags de "comando"
     */
    private void visitDocument() {
        //System.out.println("esta en ComandoScanner servicio constantes en visitDocument");
        org.w3c.dom.Element element = document.getDocumentElement();
        if ((element != null) && element.getTagName().equals("comando")) {
            visitElement_comando(element);
        }
    }

    /**
     * Navega por un elemento de nombre "comando", visitando todos sus elementos<br>
     * hijos
     */
    void visitElement_comando(org.w3c.dom.Element element) {
        //System.out.println("esta en ComandoScanner servicio constantes en visitElement_comando");
        // <comando>
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    if (nodeElement.getTagName().equals("nombreComando")) {
                        visitElement_nombreComando(nodeElement);
                    }
                    if (nodeElement.getTagName().equals("parametros")) {
                        visitElement_parametros(nodeElement);
                    }
                    if (nodeElement.getTagName().equals("rol")) {
                        visitElement_rol(nodeElement);
                    }
                    if (nodeElement.getTagName().equals("tipoComando")) {
                        visitElement_tipoComando(nodeElement);
                    }
                    if (nodeElement.getTagName().equals("usuario")) {
                        visitElement_usuario(nodeElement);
                    }
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    break;
            }
        }
    }

    /**
     * Navega un elemento de tipo nombreComando y guarda su informacion en la tabla correspondiente
     */
    void visitElement_nombreComando(org.w3c.dom.Element element) {
        hash.put("nombreComando", element.getTextContent());
    }

    /**
     * Navega un elemento de tipo parametros y guarda toda su informacion en la tabla correspondiente
     */
    void visitElement_parametros(org.w3c.dom.Element element) {
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    hashParametros.put(node.getNodeName(),generarSecuencia(nodeElement));
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    break;
            }
        }
    }

    /**
     * Navega un elemento de tipo rol y guarda su informacion en la tabla correspondiente
     */
    void visitElement_rol(org.w3c.dom.Element element) {
        hash.put("rol", element.getTextContent());
    }

    /**
     * Navega un elemento de tipo usuario y guarda su informacion en la tabla correspondiente
     */
    void visitElement_usuario(org.w3c.dom.Element element) {
        //System.out.println("Estoy en ComandoScanner servicio constantes en visitElement_usuario con valor de: " + element.getTextContent());
        hash.put("usuario", element.getTextContent());
        //System.out.println("Estoy en ComandoScanner servicio constantes en visitElement_usuario y en la tabla hash usuario tiene el valor de: " + hash.get("usuario") + "\n\n");
    }


    /**
     * Navega un elemento de tipo tipoComando y guarda su informacion en la tabla correspondiente
     */
    void visitElement_tipoComando(org.w3c.dom.Element element) {
        hash.put("tipoComando", element.getTextContent());
    }

    /**
     * Genera una secuencia a partir de un nodo
     * @param e El nodo usado para generar la secuencia
     * @return La secuencia con la informacion del nodo
     */
    private Secuencia generarSecuencia(Node e){
        Secuencia secuencia = new Secuencia(e.getNodeName(),e.getTextContent());
        NodeList nodes = e.getChildNodes();
        NamedNodeMap attributes = e.getAttributes();
        if(attributes!=null){
            for (int i = 0; i < attributes.getLength(); i++) {
                Node node = attributes.item(i);
                Atributo att = new Atributo(node.getNodeName(),node.getNodeValue());
                secuencia.agregarAtributo(att);
            }
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if(!node.getNodeName().startsWith("#")){
                secuencia.agregarSecuencia(generarSecuencia(node));
            }
        }
        return secuencia;
    }
}
