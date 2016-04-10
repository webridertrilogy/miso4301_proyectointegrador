/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author ju-cort1
 */
@Remote
public interface DocumentoPrivadoRemote {

    /**
     * Persiste los metadatos de un documento privado y retorna el identificador
     * del documento así como la ruta base del directorio dónde se debe guardar
     * el documento físico.
     * @param xmlComando El comando para subir los metadatos de un documento privado.
     * Los parámetros no son nulos.
     * @return un xml con la respuesta al comando especificando el identificador
     * del documento así como la ruta base del directorio dónde se debe guardar
     * el documento físico o un mensaje de error
     */
    String subirMetadatosDocumentoPrivado(String xmlComando);

    /**
     * Actualiza los metadatos de un documento privado y retorna un mensaje
     * indicando la correcta actualización de estos.
     * @param xmlComando El comando para actualizar los metadatos de un documento privado.
     * Los parámetros no son nulos.
     * @return un xml con un mensaje indicando la correcta actualización o un
     * identificador de error.
     */
    String actualizarMetadatosDocumentoPrivado(String xmlComando);

    /**
     * Confirma la subidad de un documento privado, actualizando la ruta del
     * documento privado y el formato mime. Si el documento actual tiene una ruta
     * de documento y esta es diferente a la que entra por parámetro entonces
     * se debe borrar el documento físico y actualizar la nueva ruta del documento.
     * @param xmlComando El comando para confirmar la subida de un documento
     * y actualizar los datos. Los parámetros no son nulos.
     * @return un xml con un mensaje indicando la correcta actualización o un
     * identificador de error.
     */
    String confirmarSubidaDocumentoPrivado(String xmlComando);

    /**
     * Consulta los datos de un documento privado
     * @param xmlComando El comando para consultar los datos de un documento
     * privado. El parámetro no es nulo.
     * @return un xml con los datos de un documento privado o un identificador
     * de error.
     */
    String consultarDatosDocumentoPrivado(String xmlComando);

    /**
     * Consulta los datos de todos los documentos privados presentes en el
     * sistema
     * @param xmlComando El comando para consultar los datos de todos los
     * documentos privados presentes en el sistema. Los parámetros no son nulos.
     * Si un documento no tiene ruta registrada, se procede a borrarlo del sistema.
     * @return un xml con los datos de todos los documentos privados.
     */
    String consultarDocumentosPrivados(String xmlComando);

    /**
     * Retorna información relevante para descargar un documento.
     * @param xmlComando El comando para retornar información relevante para
     * la descarga. El parametro no es nulo
     * @return un xml con información relevante para descargar un documento
     */
    String darInfoDescargaDocumentoPrivado(String xmlComando);

    /**
     * Eliminar un documento privado del sistema
     * @param xmlComando El comando para eliminar un documento privado. El
     * parámetro no es nulo
     * @return un xml confirmando la correcta eliminación del documento o un
     * mensaje de error.
     */
    String eliminarDocumentoPrivado(String xmlComando);
    
    /**
     * Retorna el árbol de documentos
     * @param xmlComando El comando para devolver el árbol. El parámetro no es nulo
     * @return un xml con la información del árbol de documentos
     */
    String darArbolDocumentos(String xmlComando);

    /**
     * Retorna el árbol de documentos
     * @param xmlComando El comando para devolver el árbol. El parámetro no es nulo
     * @return un xml con la información del árbol de documentos
     */
    String editarNodoArbolDocumentos(String xmlComando);

        /**
     * Retorna el árbol de documentos
     * @param xmlComando El comando para devolver el árbol. El parámetro no es nulo
     * @return un xml con la información del árbol de documentos
     */
    String agregarNodoArbolDocumentos(String xmlComando);

        /**
     * Retorna el árbol de documentos
     * @param xmlComando El comando para devolver el árbol. El parámetro no es nulo
     * @return un xml con la información del árbol de documentos
     */
    String eliminarNodoArbolDocumentos(String xmlComando);

        /**
     * Retorna el árbol de documentos
     * @param xmlComando El comando para devolver el árbol. El parámetro no es nulo
     * @return un xml con la información del árbol de documentos
     */
    String eliminarArbolDocumentos(String xmlComando);
}
