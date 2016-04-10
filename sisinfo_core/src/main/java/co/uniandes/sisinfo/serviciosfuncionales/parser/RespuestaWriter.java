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
import java.util.Collection;
import javax.ejb.EJB;
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

/**
 *
 */
public class RespuestaWriter implements Serializable {

    /**
     * Documento principal
     */
    private Document documento;

    /**
     * Elemento raiz de la respuesta
     */
    private Element root;

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteLocal constanteBean;

    /**
     * Inicializa la instancia del escritor de respuestas.
     * @throws java.lang.Exception
     */
    public RespuestaWriter()throws Exception{
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        documento = builder.newDocument();
    }

    /**
     * Genera una respuesta en xml basado en una coleccion de secuencias y un <br>
     * identificador de comando
     * @param secuencias Lista de secuencias que componen la respuesta
     * @param identificador Identificador del comando que genero la respuesta
     * @return xml con la respuesta
     * @throws java.lang.Exception En caso de encontrar errores al parsear las <br>
     * secuencias a XML
     */
    public String generateRespuesta(Collection<Secuencia> secuencias,String nombreComando, String tipo, String idMensaje, Collection<Secuencia> parametros) throws Exception{
            root = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_RESULTADO));
            Element e = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_NOMBRE_COMANDO));
            e.setTextContent(nombreComando);
            root.appendChild(e);
            e = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_MENSAJE));
            Element child = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_TIPO_MENSAJE));
            child.setTextContent(tipo);
            e.appendChild(child);
            child = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_ID_MENSAJE));
            child.setTextContent(idMensaje);
            e.appendChild(child);
            child = documento.createElement(getConstanteBean().getConstante(Constantes.TAG_PARAMETROS_MENSAJE));
            for (Secuencia secuencia : parametros) {
                Element elementoParametro = interpretarSecuencia(secuencia);
                child.appendChild(elementoParametro);
            }
            e.appendChild(child);
            root.appendChild(e);
            for (Secuencia secuencia : secuencias) {
                root.appendChild(interpretarSecuencia(secuencia));
            }
            Source source = new DOMSource(root);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
    }

    /**
     * Metodo que interpreta una secuencia que recibe como parametro y retorna <br>
     * Un Element que contiene toda la informacion de la secuencia
     * @param sec secuencia a interpretar
     * @return Element con la informacion de la secuencia
     */
    private Element interpretarSecuencia(Secuencia sec){
        String nom = sec.getNombre();
        Element e = documento.createElement(nom);
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
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }
}
