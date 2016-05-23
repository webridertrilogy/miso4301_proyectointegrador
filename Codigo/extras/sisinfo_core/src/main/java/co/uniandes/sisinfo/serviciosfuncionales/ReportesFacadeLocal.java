/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import java.sql.Timestamp;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface ReportesFacadeLocal {

    String hacerReporteArchivosProfesores(String idArchivo,String tipoArchivo);

    String hacerReporteEstadoInscripcionInvitados(String idInscripcion);

    String hacerReporteConfirmadosAInscripcion(String idInscripcion);

    String hacerReporteEstadoSolicitudesMonitoresCursos();

    String hacerReporteEstadoSolicitudesMonitoresCursosPorEstado(String idSolicitud);

    String hacerReporteNotasMonitores();

    String hacerReporteSeccionesSinMonitor();

    String hacerReporteCursosConDobleMonitoria();

    String hacerReporteCursosConUnaMonitoria();

    String hacerReporteMonitoresProfesor(String loginProfesor);

    String hacerReporteMonitorPorCriterios(String correoEstudiante, String codigoEstudiante, String nombresEstudiante, String apellidosEstudiante, String notaEstudiante, String operadorNota);

    String hacerReporteMonitoresCursos(String cursos);

    String hacerReporteMonitoresCursosCupi2();

    String hacerReportesContactosCrm();

    String hacerReporteCargaYcompromisosDeptoEstadoActual();

    void setRutaReportes(String ruta);

    String hacerReporteCargaYcompromisosDepto(Long idPeriodo, String nombrePeriodo, String rutaGuardado, String rutaHistoricos);

    String hacerReportenuevosInscritosASubareaDesde(String fechaDesde, String estado);

    String hacerReporteDetallesTodosLosInscritosDesde(String fechaDesde);

    String hacerReporteDetalladoInscripcionSubareaEstudiante(String idInscripcionEstudiante);

    String hacerListadoEstudiantesTesis1PorSemestre(String semestre, String estado,String reporteSimple);

    String hacerHojaParaCarpetaEstudiantesTesis1PorEstadoYsemestre(String semestre, String estado);

    String darReporteDetalladoEstudiantePorId(String idTesis);

    String hacerListaTesis2PorSemestre(String semestre,String reporteSimple);

    String hacerReprotesParaCarpetasEstudiantesTesis2(String semestre, String estado);

    String hacerReporteParaCarpetatesis2Estudiante(String id);

    String hacerCartaTesisJuradoSustentacion(String tipo, String nombresEstudiante, String nombresJurado, Long idTesis);

    String hacerReporteReservaCitas();

    String hacerReporteSolicitudesMaterialBibliograficoPorSolicitante(String correoSolicitante);

    String hacerReporteSolicitudesMaterialBibliograficoIndividual_ConsolidadoCostosDepto();

    String hacerReporteSolicitudesMaterialBibliografico();

    String hacerReporteTodasOfertasBolsaEmpleo();

    String hacerReporteConflictoHorarios();

    //PROYECTO DE GRADO
    String hacerReporteEstudiantesAceptadosEnTesisPregrado(String estado, String semestre);
    String hacerReporteNotasEstudiantesEnTesisPregrado(String estado, String semestre,String formatoReporte);
    //REPORTES RESERVAS E INVENTTARIO

    String hacerReporteDeVigenciaDeUsuarios(Long idLaboratorio);String hacerReporteUsoElementosLaboratorio(Long idLaboratorio) ;
    String hacerReporteHistoriaUsoElementosLaboratorioConHistoria(Long idLaboratorio) ;
    String hacerReporteEquiposLaboratorioNoDevueltos(Long idLaboratorio, String nombreLab, Timestamp fechaInicio) ;
    String hacerReporteReservaInventarioLaboratorios(String fechaInicial, String fechaFinal) ;
    String hacerReporteReservaInventarioLaboratoriosRangoEstado(String fechaInicial, String fechaFinal,String Estado) ;
    String hacerReporteReservaInventarioLaboratoriosEstadistico(String fechaInicial, String fechaFinal) ;
    String hacerReporteReservasLaboratorioHorariosMasReservados(String fechaInicial,String fechaFinal);
    //ASISTENCIAS GRADUADAS
    String hacerReporteAsistenciasGraduadas(String semestre);
    String hacerReporteNotasAsistenciasGraduadas(String semestre);

    //Reportes Proyecto Grado
    String hacerReporteConsolidadoNotasEstudiantesEnTesisPregrado(String semestre, String formatoReporte);

    String hacerReporteContactosCrmInscritosPorEvento(String idEvento, String nombreEvento);

    String hacerReporteContactosCrmInscritosPorTipoDeEvento(String idTipoEvento, String nombreTipoEvento);

    String hacerReporteContactosCrmInscritosPorSector(String idSectorEvento,String nombreSectorEvento);

    String hacerReporteContactosCrmInscritosPorCargo(String idCargoEvento,String nombreCargoEvento);

    String hacerReporteContactosCrmInscritosPorCiudad (String idCiudad,String nombreCiudad);
}
