package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remote
 * Servicios de administración de cursos y secciones
 * @author German Florez, Marcela Morales
 */
@Remote
public interface CarteleraCursosBeanRemote {

    String consultarCursosISIS(String xml);

    String consultarSeccionesPorCodigoCurso(String xml);

    String crearSeccionACurso(String xml);

    String editarDatosSeccion(String xml);

    String eliminarSeccion(String xml);

    String consultarSeccion(String xml); 

    String cargarCarteleraWeb(String xml);

    String consultarProfesoresISIS(String xml);

    String crearCurso(String comandoXML);
}
