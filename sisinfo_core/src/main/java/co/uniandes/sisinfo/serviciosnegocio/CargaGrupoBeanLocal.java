package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 * Interface Local CargaBean
 * @author Marcela Morales
 */
@Local
public interface CargaGrupoBeanLocal {

    String cargarGrupo(String xml);

    String cargarEstudiantesMatriculados(String xml);

    String cargarEstudiantes(String xml);

    String cargarProfesores(String xml);
}
