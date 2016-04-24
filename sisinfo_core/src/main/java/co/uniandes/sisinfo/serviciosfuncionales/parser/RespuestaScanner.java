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
import java.io.StringReader;
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
public class RespuestaScanner implements Serializable{

    /**
     * Documento principal
     */
    private Document document;

    /**
     * Lee un XML y retorna una secuencia con toda la informacion del xml
     * @param xml Xml que se desea leer
     * @return Secuencia con la informacion del xml
     * @throws java.lang.Exception en caso de encontrar errores con la traduccion
     */
    public Secuencia leerXML(String xml) throws Exception{
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse (new InputSource(new StringReader(xml)));
        this.document = document;
        return visitDocument();
    }

    /**
     * Analiza un documento xml de una respuesta y retorna la secuencia <br>
     * que lo representa
     * @return
     */
    private Secuencia visitDocument() {
        org.w3c.dom.Element element = document.getDocumentElement();
        if ((element != null) && element.getTagName().equals("resultado")) {
            return generarSecuencia(element);
        }else{
            return null;
        }
    }

    /**
     * Genera una secuencia basado en un nodo, transfiriendo su nombre, valor, <br>
     * atributos y secuencias hijas a la nueva secuencia
     * @param e Nodo a analizar
     * @return Secuencia con la informacion del nodo
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
            secuencia.agregarSecuencia(generarSecuencia(node));
        }
        return secuencia;
    }
}
