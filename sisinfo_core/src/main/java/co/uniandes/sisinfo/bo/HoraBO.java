/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.bo;

/**
 *  Clase de ayuda para manejar los horarios disponibles de una reserva de
 *  laboratorio
 * @author Carlos Morales
 */
public class HoraBO {

    private int horaIncio;
    private int horaFin;
    private int minutoInicio;
    private int minutoFin;
    /**
     * Tipo de ocupacion para la hora. Si existe una reserva contiene los nombres
     * y apellidos del creador de la rserva y en caso de estar disponible contiene
     * la palabra "Libre"
     */
    private String tipo;

    public HoraBO(int horaIncio, int horaFin, int minutoInicio, int minutoFin) {
        this.horaIncio = horaIncio;
        this.horaFin = horaFin;
        this.minutoInicio = minutoInicio;
        this.minutoFin = minutoFin;


    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public int getHoraIncio() {
        return horaIncio;
    }

    public void setHoraIncio(int horaIncio) {
        this.horaIncio = horaIncio;
    }

    public int getMinutoFin() {
        return minutoFin;
    }

    public void setMinutoFin(int minutoFin) {
        this.minutoFin = minutoFin;
    }

    public int getMinutoInicio() {
        return minutoInicio;
    }

    public void setMinutoInicio(int minutoInicio) {
        this.minutoInicio = minutoInicio;
    }

    public int darTiempoInicio() {
        return horaIncio * 60 + minutoInicio;
    }

    public int darTiempoFin() {
        return horaFin * 60 + minutoFin;
    }

    @Override
    public String toString() {
        return horaIncio + ":" + minutoInicio + "-" + horaFin + ":" + minutoFin;
    }

    /**
     * Retorna el tipo de ocupación para esta Hora
     * @return Libre si la hora se encuentra disponible y una cadena de caracteres
     * con los nombres y apellidos del creador de la reserva si se encuentra ocupada
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Modifica el tipo de la hora.
     * @param tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
