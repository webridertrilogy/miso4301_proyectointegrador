package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Servicios de administración de reservas de citas con La Coordinación (Interface remota)
 * @author German Florez, Marcela Morales
 */
@Remote
public interface ReservasBeanRemote {

    /**
     * Método que se encarga de crear una cita dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String reservarCita(String xml);

    /**
     * Método que se encarga de consultar las reservas dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarReservas(String xml);

    /**
     * Método que se encarga de persistir el horario de coordinación dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String guardarHorarioCoordinacion(String xml);

    /**
     * Método que se encarga de consultar el horario de coordinación dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarHorarioCoordinacion(String xml);

    /**
     * Método que se encarga de cancelar una reserva dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String cancelarReserva(String xml);

    /**
     * Método que se encarga de consultar las reservas de una persona dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarReservasPersona(String xml);

    /**
     * Método que se encarga de consultar las reservas actuales dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarReservasActuales(String xml);

    /**
     * Método que se encarga de modificar la información de una reserva dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String modificarReserva(String xml);

    /**
     * Método que se encarga de consultar los datos de un contacto dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarDatosContacto(String xml);

    /**
     * Método que se encarga de consultar las reservas del día dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarReservasDia(String xml);

    /**
     * Método que se encarga de guardar el estado de las reservas dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String guardarEstadoReservas(String xml);

    /**
     * Método que se encarga de notificar al estudiante que tiene una reserva
     * Este método es llamado por un timer
     * @param idReserva Id de la reserva
     */
    void manejoTimersReserva(String idReserva);

    /**
     * Metodo que trae un grupo de reservas de un rango de fechas
     * @param xml
     * @return
     */
    String consultarRangoReservas (String xml);
    
    /**
     * Metodo que cancela un grupo de reservas 
     * @param xml
     * @return
     */
    String cancelarGrupoReservas(String xml);
}
