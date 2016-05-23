package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 * Servicios de administración de rangos de fechas generales (local)
 * @author Marcela Morales
 */
@Local
public interface RangoFechasGeneralLocal {

    /**
     * Método que se encarga de crear un rango de fechas general dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String crearRangoFechasGeneral(String xml);

    /**
     * Método que se encarga de editar un rango de fechas general dado el id dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String editarRangoFechasGeneral(String xml);

    /**
     * Método que se encarga de consultar un rango de fechas por nombre dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarRangoFechasGeneralPorNombre(String xml);

    /**
     * Método que se encarga de consultar un rango de fechas por id dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarRangoFechasGeneralPorId(String xml);

    /**
     * Método que se encarga de consultar todos los rangos de fechas dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarRangosFechasGeneral(String xml);

    /**
     * Método que se encarga de eliminar un rango de fecha general dado su id dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String eliminarRangoFechasGeneral(String xml);

    /**
     * Método que se encarga de eliminar todos los rangos de fechas general dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String eliminarRangosFechasGeneral(String xml);

    /**
     * Método que se encarga de consultar los tipos de rangos existentes dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarTiposRangoFechasGeneral(String xml);

    /**
     * Método para el manejo de timers de rangos de fechas generales
     * @param parametros Parámetros del timer, generalmente el nombre del comando respetivo
     */
    void manejoTimersRangoFechasGeneral(String parametros);

    /**
     * Método que se encarga de editar los rangos de fechas generales dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String editarRangosFechasGeneral(String xml);
}
