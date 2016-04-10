/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Servicios de administración de ofertas de empleo (Interface remota)
 * @author Camilo Cortés, John Casallas, Marcela Morales
 */
@Remote
public interface OfertaRemote {

    /**
     * Método que se encarga de actualizar una oferta dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String actualizarOferta(String xml);

    /**
     * Método que se encarga de consultar una oferta dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String consultarOferta(String xml);

    /**
     * Método que se encarga de consultar todas las ofertas dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String consultarOfertas(String xml);

    /**
     * Método que se encarga de consultar todas las ofertas de un proponente dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String consultarOfertasProponente(String xml);

    /**
     * Método que se encarga de crear una oferta dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String crearOferta(String xml);

    /**
     * Método que se encarga de eliminar una oferta dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String eliminarOferta(String xml);

    /**
     * Método que se encarga de eliminar una oferta por vencimiento
     * @param idOferta Id de la oferta a eliminar
     */
    void eliminarOfertaPorVencimiento(String idOferta);

    /**
     * Método que se encarga de notificar la eliminación de una oferta por vencimiento
     * @param idOferta Id de la oferta a notificar
     */
    void notificarProfesorVencimientoOferta(String idOferta);

    /**
     * Método que se encarga de aplicar a una oferta dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String aplicarAOferta(String xml);
}
