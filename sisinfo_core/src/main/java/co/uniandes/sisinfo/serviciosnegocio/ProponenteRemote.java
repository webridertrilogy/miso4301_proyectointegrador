package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Servicios de administración de proponentes (Interface remota)
 * @author David Naranjo, Camilo Cortés, Marcela Morales
 */
@Remote
public interface ProponenteRemote {

    /**
     * Método que se encarga de actualizar un proponente dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String actualizarProponente(String comando);

    /**
     * Método que se encarga de consultar un proponente dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String consultarProponente(String comando);
    
    /**
     * Método que se encarga de consultar un proponente por correo dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String consultarProponentePorLogin(String comando);

    /**
     * Método que se encarga de consultar los proponentes dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String consultarProponentes(String comando);

    /**
     * Método que se encarga de crear un proponente dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String crearProponente(String comando);

    /**
     * Método que se encarga de eliminar un proponente dado el xml respectivo
     * @param xml Cadena XML en la cual se define el xml
     * @return Cadena XML con la respuesta del xml
     */
    String eliminarProponente(String comando);
}
