package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios de carga de grupo
 * @author Marcela Morales
 */
@Remote
public interface CargaGrupoBeanRemote {

    String cargarGrupo(String xml);

    String cargarEstudiantesMatriculados(String xml);

    String cargarEstudiantes(String xml);

    String cargarProfesores(String xml);
}
