/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;

import javax.ejb.Remote;

/**
 *
 * @author im.melo33
 */
@Remote
public interface TimerGenericoBeanRemote {

    /**
     * metodo que borra todos los timers genericos del sistema
     */
    void borrarTodosLosTimers();

    /**
     * metodo que crea un timer generico, el cual al vencerse llamara al metodo de la interfaz (remota) que se especifique
     * el metodo debe recibir un solo parametro el cual es un string donde esta la informacion que necesite
     *
     * @param direccionInterfazRemotaBean: direccion de la interfaz remota EJ: co.edu.uniandes.sisinfo.serviciosFuncionales.XXXRemote
     * @param nombreMetodoALLamar nombre del metodo que recibe como unico parametro un string y sepa que hacer cuando se venza el timer
     * @param fechaFin: fecha en la cual se debe activar el timer
     * @param infoTimer: la informacion que se debe devolver del timer deberia ser un string
     * @return
     */
    Long crearTimer(String direccionInterfazRemotaBean, String nombreMetodoALLamar, Timestamp fechaFin, String infoTimer);

    Long crearTimer2(String direccionInterfazRemotaBean, String nombreMetodoALLamar, Timestamp fechaFin, String infoTimer,
            String moduloQuienLoCrea, String beanQuienLoCrea, String metodoQuienLoCrea, String descripcionTimer);

    /**
     * metodo que reinicia todos  los timers de la BD
     */
    void recargarTimersDeBD();

    /**
     * metodo que reinicia un timer sin agregarlo a la BD
     * @param id del timer
     * @param direccionInterfazRemotaBean
     * @param nombreMetodoALLamar
     * @param fechaFin
     * @param infoTimer
     * @return
     */
    String recrearTimer(Long id, String direccionInterfazRemotaBean, String nombreMetodoALLamar, Timestamp fechaFin, String infoTimer);

    void eliminarTimer(Long idTimer);

    /**
     * metodo que elimina un timer por el parametro externo asociado
     * @param parametro: el parametro ingresado al crear el timer
     */
    void eliminarTimerPorParametroExterno(String parametro);

    void recargarTimerDeBDPorMetodoALlamar(String metodoALlamar, Timestamp fechaFin) throws Exception;

    String recargarTimers(String comando);

    String consultarTimers(String comando);

    String arreglarTimers(String parameter);

    boolean existeTimerCompletamenteIgual(String dirInterfaz, String nombreMetdo, Timestamp fecha, String paramExterno);

    boolean existeTimerCompletamenteIgual(String dirInterfaz, String nombreMetdo, String paramExterno);

    Long darIdTimer(String direccionInterfaz, String nombreMetodo, String parametroExterno);

    String consultarTimer(String comando);

    String editarTimer(String comando);

    String detenerTimerEnMemoria(String comando);

    String eliminarTimer(String comando);

    String ejecutarTimerEnMemoria(String comando);

    public boolean timerExisteEnMemoria(Long id);

    public boolean timerExisteEnBD(Long id);

    void eliminarTimersPorDireccionInterfaz(String interfaz);

    void eliminarTimers(String direccionInterfaz, String nombreMetodo, String infoAdicional);

    void crearTimerDiagnosticoSisinfo();

    public int consultarTimersEnMemoriaFaltantesEnBD();

    public int consultarTimersEnBDFaltantesEnMemoria();
}
