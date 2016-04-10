package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 * Interface Local
 * Servicios de administración de cursos y secciones
 * @author German Florez, Marcela Morales
 */
@Local
interface CarteleraCursosBeanLocal {

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
