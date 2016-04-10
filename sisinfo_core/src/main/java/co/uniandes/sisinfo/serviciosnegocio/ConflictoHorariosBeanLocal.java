package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 * Interface Local
 * Servicios de administración de conflictos de horario
 * @author German Florez, Marcela Morales
 */
@Local
public interface ConflictoHorariosBeanLocal {

    String crearPeticionConflictoHorario(String xml);

    String consultarPeticionesPorCorreo(String xml);

    String cancelarPeticionPorCorreo(String xml);

    String consultarCantidadPeticionesCursosISIS(String xml);

    String consultarPeticionesPorCodigoCursoYTipo(String xml);

    String actualizarEstadoYResolucionPeticiones(String xml);

    String consultarFechasConflictoHorario(String xml);

    String actualizarFechasConflictoHorario(String xml);

    String consultarProgramas(String xml);

    String consultarPeticionesResueltas(String xml);

    void eliminarPeticionesPorSeccion(Long id);
}
