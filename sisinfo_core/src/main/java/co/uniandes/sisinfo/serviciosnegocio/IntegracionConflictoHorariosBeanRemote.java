/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Carlos Morales
 */
@Remote
public interface IntegracionConflictoHorariosBeanRemote {
 
    /**
     * Metodo que realiza los cambios necesarios sobre el modulo de monitorias 
     * ante la creacion de una seccion. El metodo genera las tareas correspondientes
     * a la nueva seccion creada para la seleccion de monitores
     * <b>pre:</b> La seccion con el crn dado por parametro ya ha sido creada
     * @param crn Crn de la nueva seccion
     */
    void crearSeccion(String crn);

    /**
     * Metodo que realiza los cambios necesarios sobre el modulo de monitorias
     * ante la eliminacion de una seccion. El metodo completa las tareas
     * correspondientes a la seleccion de monitores para la seccion eliminada
     * y cancela las solicitudes asociadas a esta seccion
     * <b>pre:</b> La seccion con el crn dado por parametro no ha sido eliminada
     * @param crn Crn de la seccion
     */
    void eliminarSeccion(String crn);

    /**
     * Metodo que realiza los cambios necesarios sobre el modulo de monitorias
     * ante el cambio de horario de una seccion. El metodo regenera las tareas
     * correspondientes a la seleccion de monitores para la seccion y cancela las
     * solicitudes asociadas a esta seccion
     * @param crn Crn de la seccion
     */
    void cambiarHorarioSeccion(String crn);

    /**
     * Metodo que realiza los cambios necesarios sobre el modulo de monitorias
     * ante el cambio del numero de monitores de una seccion. El metodo regenera
     * las tareas correspondientes a la nueva seccion creada para la seleccion de monitores
     * y cancela las solicitudes asociadas a esta seccion
     * <b>pre:</b> La seccion con el crn dado por parametro ya ha sido modificada
     * @param crn Crn de la seccion
     */
    void cambiarNumeroMonitores(String crn);

    /**
     * Metodo que realiza los cambios necesarios sobre el modulo de monitorias
     * ante el cambio del profesor de una seccion. El metodo regenera las tareas
     * correspondientes a la seleccion de monitores para la seccion y completa
     * las tareas asociadas al profesor anterior de la seccion
     * <b>pre:</b> La seccion con el crn dado por parametro ya ha sido modificada
     * @param crn Crn de la seccion
     * @param correoProfesorAntiguo correo del profesor anterior
     */
    void cambiarProfesor(String crn, String correoProfesorAntiguo);

    /**
     * Metodo que valida si el numero de monitores de una seccion puede o no ser
     * cambiado basandose en la cantidad de solicitudes que actualmente se
     * encuentran en proceso para la seccion
     * @param crn Crn de la seccion
     * @param nuevoNumeroMonitores El nuevo numero de monitores de la seccion
     * @return True si la seccion puede ser modificada, false en caso contrario
     */
    boolean validarCambioNumeroMonitores(String crn, double nuevoNumeroMonitores);

    /**
     * Metodo que indica si existe o no una convocatoria abierta
     * @return True si existe una convocatoria abierta, false en caso contrario
     */
    public boolean existeConvocatoriaAbierta();

    /**
     * Método que indica si se pueden realizar cambios sobre la monitoria de un curso especifico
     * @param codigo Codigo del curso que se desea modificar
     * @return True en caso de que pueda ser modificado, false en caso contrario
     */
    boolean validarCambioCaracteristicasMonitoria(String codigo);

}
