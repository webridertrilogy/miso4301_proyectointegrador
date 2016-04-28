/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosfuncionales.ReportesFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Administrador
 */
@Stateless
@EJB(name = "ReportesBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ReportesLocal.class)
public class ReportesBean implements ReportesLocal {

    /**
     * ArchivoFacade
     */
    @EJB
    private CargaYCompromisosBeanLocal cycLocal;
    /**
     * ArchivoFacade
     */
    @EJB
    private ReportesFacadeLocal reportesFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteLocal constanteBean;
    /**
     * EstudiantePostgrado
     */
   
    private ServiceLocator serviceLocator;
    private ParserT parser;
    //---------------------------------------
    // Constructor
    //---------------------------------------

    /**
     * Constructor de ArchivosBean
     */
    public ReportesBean() {
//        serviceLocator = new ServiceLocator();
//        try {
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//            estPostgradoLocal = (EstudiantePostgradoLocal) serviceLocator.getLocalEJB(EstudiantePostgradoLocal.class);
//            cycLocal = (CargaYCompromisosBeanLocal) serviceLocator.getLocalEJB(CargaYCompromisosBeanLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(ReportesBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public String hacerReporte(String comandoXML) {
        try {
            //Hacer Set de la ruta de reportes
            String ruta = getConstanteBean().getConstante(Constantes.RUTA_REPORTES_JASPER);
            reportesFacade.setRutaReportes(ruta);
            getParser().leerXML(comandoXML);
            String tipoReporte = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_REPORTE)).getValor();
            String rutaReporte = "";
            if (tipoReporte.equals("reporte_archivos_profesores")) {
                String idArchivo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_ARCHIVO)).getValor();
                String tipoArchivo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_ARCHIVO)).getValor();
                rutaReporte = reportesFacade.hacerReporteArchivosProfesores(idArchivo, tipoArchivo);
            } else if (tipoReporte.equals("reporte_estado_inscripciones_invitados")) {
                String idInscripcion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION)).getValor();
                rutaReporte = reportesFacade.hacerReporteEstadoInscripcionInvitados(idInscripcion);
            } else if (tipoReporte.equals("reporte_confirmados_inscripcion")) {
                String idInscripcion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_INSCRIPCION)).getValor();
                rutaReporte = reportesFacade.hacerReporteConfirmadosAInscripcion(idInscripcion);
            } else if (tipoReporte.equals("reporte_estado_solicitudes_monitores_cursos")) {
                rutaReporte = reportesFacade.hacerReporteEstadoSolicitudesMonitoresCursos();
            } else if (tipoReporte.equals("reporte_solicitudes_monitores_cursos_por_estado")) {
                String estadoSolicitud = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)).getValor();
                rutaReporte = reportesFacade.hacerReporteEstadoSolicitudesMonitoresCursosPorEstado(estadoSolicitud);
            } else if (tipoReporte.equals("reporte_notas_monitores")) {
                rutaReporte = reportesFacade.hacerReporteNotasMonitores();
            } else if (tipoReporte.equals("reporte_secciones_sin_monitor")) {
                rutaReporte = reportesFacade.hacerReporteSeccionesSinMonitor();
            } else if (tipoReporte.equals("reporte_cursos_doble_monitoria")) {
                rutaReporte = reportesFacade.hacerReporteCursosConDobleMonitoria();
            } else if (tipoReporte.equals("reporte_cursos_una_monitoria")) {
                rutaReporte = reportesFacade.hacerReporteCursosConUnaMonitoria();
            } else if (tipoReporte.equals("reporte_monitores_profesor")) {
                String loginProfesor = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LOGIN)).getValor();
                rutaReporte = reportesFacade.hacerReporteMonitoresProfesor(loginProfesor);
            } else if (tipoReporte.equals("reporte_monitores_criterios")) {
                String codigoEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE)).getValor();
                String correoEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
                String nombresEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor();
                String apellidosEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor();
                String notaEstudiante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA)).getValor();
                String operadorNota = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERADOR)).getValor();
                rutaReporte = reportesFacade.hacerReporteMonitorPorCriterios(correoEstudiante, codigoEstudiante, nombresEstudiante, apellidosEstudiante, notaEstudiante, operadorNota);
            } else if (tipoReporte.equals("reporte_cupi2_monitores_cursos")) {
                rutaReporte = reportesFacade.hacerReporteMonitoresCursosCupi2();
            } else if (tipoReporte.equals("reporte_contactos_crm")) {
                rutaReporte = reportesFacade.hacerReportesContactosCrm();
            } else if (tipoReporte.equals("reporte_cargaycompromisos_periodo")) {
                //Realizar actualización en línea de indicadores
                cycLocal.actualizarIndicadoresCarga();

                //Seguir con el proceso normal
                Secuencia secPeriodo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO_PLANEACION));
                Secuencia secIdPeriodo = secPeriodo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
                Long idPeriodo = Long.parseLong(secIdPeriodo.getValor().trim());
                String nombreP = secPeriodo.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PERIODO)).getValor();
                String rutaHistoricos = getConstanteBean().getConstante(Constantes.RUTA_REPORTES_HISTORICOS_CARGA_Y_COMPROMISOS);
                rutaReporte = reportesFacade.hacerReporteCargaYcompromisosDepto(idPeriodo, nombreP, ruta, rutaHistoricos);
            } else if (tipoReporte.equals("reporte_cargaycompromisos_estadoactual")) {
                rutaReporte = reportesFacade.hacerReporteCargaYcompromisosDeptoEstadoActual();
            } else if (tipoReporte.equals("reporte_reserva_citas_general")) {
                rutaReporte = reportesFacade.hacerReporteReservaCitas();
            } else if (tipoReporte.equals("reporte_conflicto_horarios")) {
                rutaReporte = reportesFacade.hacerReporteConflictoHorarios();
            } /**
             * Reportes Bolsa Empleo
             */
            else if (tipoReporte.equals("reporte_bolsa_hojas_vida")) {
                //rutaReporte = reportesFacade.hacerReporteHojasDeVidaEstudiantes();
               // rutaReporte = estPostgradoLocal.hacerReporteTodasHojasDeVida(comandoXML);
            } else if (tipoReporte.equals("reporte_bolsa_ofertas_totales")) {
                rutaReporte = reportesFacade.hacerReporteTodasOfertasBolsaEmpleo();
            } /**
             * REportes de Tesis
             */
            else if (tipoReporte.equals("reporte_tesis_inscripcion_subarea_todos_sin_detalle")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA));
                if (secDesde != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = sdf.parse(secDesde.getValor().trim());
                    rutaReporte = reportesFacade.hacerReportenuevosInscritosASubareaDesde(sdf.format(d), getConstanteBean().getConstante(Constantes.CTE_INSCRIPCION_APROBADA));
                }
            } else if (tipoReporte.equals("reporte_tesis_inscripcion_subarea_todos_con_detalle")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA));
                if (secDesde != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = sdf.parse(secDesde.getValor().trim());
                    rutaReporte = reportesFacade.hacerReporteDetallesTodosLosInscritosDesde(sdf.format(d));
                }
            } else if (tipoReporte.equals("reporte_tesis_inscripcion_subarea_estudiante_por_id")) {
                Secuencia secId = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
                if (secId != null) {
                    rutaReporte = reportesFacade.hacerReporteDetalladoInscripcionSubareaEstudiante(secId.getValor().trim());
                }
            } //Tesis 1 Reportes:
            //1.lista:semestre,estado
            else if (tipoReporte.equals("reporte_tesis_1_todos_Lista_sin_detalle")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
                Secuencia secReporteSimple = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REPORTE_SIMPLE));

                if (secDesde != null) {
                    String estado = getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO);
                    String reporteSimple = secReporteSimple.getValor();
                    String semestre = secDesde.getValor().trim();

                    rutaReporte = reportesFacade.hacerListadoEstudiantesTesis1PorSemestre(semestre, estado,reporteSimple);
                }
            } // 2. detalles_todos: semestre estado
            else if (tipoReporte.equals("reporte_tesis_1_todos_Lista_con_detalle")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));

                if (secDesde != null) {
                    String estado = getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO);
                    String semestre = secDesde.getValor().trim();

                    rutaReporte = reportesFacade.hacerHojaParaCarpetaEstudiantesTesis1PorEstadoYsemestre(semestre, estado);
                }
            } //3.detalles_!:idTesis
            else if (tipoReporte.equals("reporte_tesis_1_estudiante_por_id")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
                if (secDesde != null) {
                    rutaReporte = reportesFacade.darReporteDetalladoEstudiantePorId(secDesde.getValor().trim());
                }
            } else if (tipoReporte.equals("reporte_tesis_2_todos_Lista_sin_detalle")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
                Secuencia secReporteSimple = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_REPORTE_SIMPLE));

                  if (secDesde != null) {
                    String semestre = secDesde.getValor().trim();
                    String reporteSimple = secReporteSimple.getValor();
               
                    rutaReporte = reportesFacade.hacerListaTesis2PorSemestre(semestre, reporteSimple);
                }
            } // 2. detalles_todos: semestre estado
            else if (tipoReporte.equals("reporte_tesis_2_todos_Lista_con_detalle")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));

                if (secDesde != null) {
                    String estado = getConstanteBean().getConstante(Constantes.CTE_TESIS_EN_CURSO);
                    String semestre = secDesde.getValor().trim();

                    rutaReporte = reportesFacade.hacerReprotesParaCarpetasEstudiantesTesis2(semestre, estado);
                }
            } //3.detalles_!:idTesis
            else if (tipoReporte.equals("reporte_tesis_2_estudiante_por_id")) {
                System.out.println("Llegó aquí");
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));

                if (secDesde != null /*&& secArchivoInicio != null*/) {
                    rutaReporte = reportesFacade.hacerReporteParaCarpetatesis2Estudiante(secDesde.getValor().trim());

                }
            } //Reportes Solicitud Material Bibliografico
            else if (tipoReporte.equals("reporte_solicitudes_material_bibliografico_solicitante")) {
                String correoSolicitante = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor();
                rutaReporte = reportesFacade.hacerReporteSolicitudesMaterialBibliograficoPorSolicitante(correoSolicitante);
            } else if (tipoReporte.equals("reporte_solicitudes_material_bibliografico")) {
                rutaReporte = reportesFacade.hacerReporteSolicitudesMaterialBibliografico();
            } else if (tipoReporte.equals("reporte_solicitudes_material_bibliografico_consolidado_costos")) {
                rutaReporte = reportesFacade.hacerReporteSolicitudesMaterialBibliograficoIndividual_ConsolidadoCostosDepto();
            } else if (tipoReporte.equals("reporte_solicitudes_material_bibliografico")) {
                rutaReporte = reportesFacade.hacerReporteSolicitudesMaterialBibliografico();
            } //Reportes Tesis Pregrado
            else if (tipoReporte.equals("reporte_tesis_pregrado_todos_aceptados_asesor")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
                if (secDesde != null) {
                    String estado = getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA);
                    String semestre = secDesde.getValor().trim();
                    rutaReporte = reportesFacade.hacerReporteEstudiantesAceptadosEnTesisPregrado(estado, semestre);
                }
            } else if (tipoReporte.equals("reporte_notas_tesis_pregrado")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
                if (secDesde != null) {
                    String estado = getConstanteBean().getConstante(Constantes.CTE_TESIS_ACEPTADA_ASESOR_ESPERANDO_DOCUMENTO_PROPUESTA);
                    String formatoReporte = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FORMATO_REPORTE)).getValor();
                    String semestre = secDesde.getValor().trim();
                    //rutaReporte = reportesFacade.hacerReporteNotasEstudiantesEnTesisPregrado(estado, semestre,formatoReporte);
                    rutaReporte = reportesFacade.hacerReporteConsolidadoNotasEstudiantesEnTesisPregrado(semestre,formatoReporte);
                }
            } //Reportes Asistencias Graduadas
            else if (tipoReporte.equals("reporte_asistencias_graduadas")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
                if (secDesde != null) {
                    String semestre = secDesde.getValor().trim();
                    rutaReporte = reportesFacade.hacerReporteAsistenciasGraduadas(semestre);
                }
            } else if (tipoReporte.equals("reporte_notas_asistencias_graduadas")) {
                Secuencia secDesde = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
                if (secDesde != null) {
                    String semestre = secDesde.getValor().trim();
                    System.out.println("ReportesBean.java:PERIODO:" + semestre);
                    rutaReporte = reportesFacade.hacerReporteNotasAsistenciasGraduadas(semestre);
                }
            } //Reportes Reservas e Inventario:
            else if (tipoReporte.equals("reporte_hacer_reporte_de_vigencia_de_usuarios")) {
                Secuencia secidLAb = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
                Long idL = Long.parseLong(secidLAb.getValor());
                rutaReporte = reportesFacade.hacerReporteDeVigenciaDeUsuarios(idL);

            } else if (tipoReporte.equals("hacer_reporte_uso_elementos_laboratorio")) {
                Secuencia secidLAb = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
                Long idL = Long.parseLong(secidLAb.getValor());
                rutaReporte = reportesFacade.hacerReporteUsoElementosLaboratorio(idL);

            } else if (tipoReporte.equals("hacer_reporte_historia_uso_elementos_laboratorio_con_historia")) {
                Secuencia secidLAb = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
                Long idL = Long.parseLong(secidLAb.getValor());
                rutaReporte = reportesFacade.hacerReporteHistoriaUsoElementosLaboratorioConHistoria(idL);
            } //hacerReporteEquiposLaboratorioNoDevueltos(Long idLaboratorio, String nombreLab, Timestamp fechaInicio)
            else if (tipoReporte.equals("hacer_reporte_equipos_laboratorio_no_devueltos")) {
                Secuencia secidLAb = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
                Long idL = Long.parseLong(secidLAb.getValor());
                String nombreLab = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
                //fecha:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Secuencia fechaInicio = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
                Date d = sdf.parse(fechaInicio.getValor());
                Timestamp fi = new Timestamp(d.getTime());
                rutaReporte = reportesFacade.hacerReporteEquiposLaboratorioNoDevueltos(idL, nombreLab, fi);
            } else if (tipoReporte.equals("reporteReservasLaboratorioRango")) {
                Secuencia fechaInicial = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
                Secuencia fechaFinal = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
                rutaReporte = reportesFacade.hacerReporteReservaInventarioLaboratorios(fechaInicial.getValor(), fechaFinal.getValor());

            } else if (tipoReporte.equals("reporteReservasLaboratorioRangoEstado")) {
                Secuencia fechaInicial = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
                Secuencia fechaFinal = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
                Secuencia estado = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_RESERVA));
                rutaReporte = reportesFacade.hacerReporteReservaInventarioLaboratoriosRangoEstado(fechaInicial.getValor(), fechaFinal.getValor(), getConstanteBean().getConstante(estado.getValor()));

            } else if (tipoReporte.equals("reporteReservasLaboratorioEstadistico")) {
                Secuencia fechaInicial = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
                Secuencia fechaFinal = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
                rutaReporte = reportesFacade.hacerReporteReservaInventarioLaboratoriosEstadistico(fechaInicial.getValor(), fechaFinal.getValor());

            } else if (tipoReporte.equals("reporteReservasLaboratorioHorariosMasReservados")) {
                Secuencia fechaInicial = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_INICIO));
                Secuencia fechaFinal = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
                rutaReporte = reportesFacade.hacerReporteReservasLaboratorioHorariosMasReservados(fechaInicial.getValor(),fechaFinal.getValor());
            }else if (tipoReporte.equals("reporte_ContactosCrm_InscritosPorEvento")) {
                Secuencia idEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
                Secuencia nombreEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
                rutaReporte = reportesFacade.hacerReporteContactosCrmInscritosPorEvento(idEvento.getValor(),nombreEvento.getValor());
            }else if (tipoReporte.equals("reporte_ContactosCrm_InscritosPorTipoDeEvento")) {
                Secuencia idTipoEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
                Secuencia nombreTipoEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
                rutaReporte = reportesFacade.hacerReporteContactosCrmInscritosPorTipoDeEvento(idTipoEvento.getValor(),nombreTipoEvento.getValor());
            }else if (tipoReporte.equals("reporte_ContactosCrm_InscritosPorSector")) {
                Secuencia idSectorEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
                Secuencia nombreSectorEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
                rutaReporte = reportesFacade.hacerReporteContactosCrmInscritosPorSector(idSectorEvento.getValor(),nombreSectorEvento.getValor());
            }else if (tipoReporte.equals("reporte_ContactosCrm_InscritosPorCargo")) {
                Secuencia idCargoEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
                Secuencia nombreCargoEvento = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
                rutaReporte = reportesFacade.hacerReporteContactosCrmInscritosPorCargo(idCargoEvento.getValor(),nombreCargoEvento.getValor());
            }else if (tipoReporte.equals("reporte_ContactosCrm_InscritosPorCiudad")) {
                Secuencia idCiudad = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
                Secuencia nombreCiudad = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
                rutaReporte = reportesFacade.hacerReporteContactosCrmInscritosPorCiudad(idCiudad.getValor(),nombreCiudad.getValor());
            }

            ArrayList<Secuencia> secuencias = new ArrayList<Secuencia>();
            Secuencia secuenciaRutaReporte = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA), rutaReporte);
            secuencias.add(secuenciaRutaReporte);
            return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_GENERAR_REPORTES), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), "El reporte fue generado exitosamente", new LinkedList<Secuencia>());
        } catch (Exception ex) {
            Logger.getLogger(ReportesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }
}