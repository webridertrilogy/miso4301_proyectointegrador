/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.util.JRSaver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Administrador
 */
@Stateless
@EJB(name = "ReportesFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.ReportesFacadeLocal.class)
public class ReportesFacade implements ReportesFacadeLocal, ReportesFacadeRemote {

    private String rutaReportes = "/u1/especiales/sisinfo/servapp/prod/srcReportesJasper/";
    private String rutaReportesHistoricos = "/u1/especiales/sisinfo/servapp/prod/srcReportesJasper/";
    public String RUTA_REPORTES_HISTORICOS_CARGA_Y_COMPROMISOS;// = "/u1/especiales/sisinfo/servapp/prod/srcReportesJasper/cargayCompHistoricos/";
    public final static String DRIVER = "com.mysql.jdbc.Driver";
    /*
     * CONSTANTES= VARIABLEs : para cambiar esos valores favor modificar en la BD las constantes:
     *  public static String CONFIGURACION_PARAM_PRUEBA = "CONFIGURACION_PARAM_PRUEBA";
    public static String CONFIGURACION_PARAM_CONNECTSTRING = "CONFIGURACION_PARAM_CONNECTSTRING";
    public static String CONFIGURACION_PARAM_USER = "CONFIGURACION_PARAM_USER";
    public static String CONFIGURACION_PARAM_PASSWORD = "CONFIGURACION_PARAM_PASSWORD";
     */
    public String CONNECTSTRING;// = "jdbc:mysql://sistemas.uniandes.edu.co:3306/prod";
    public String USER;// = "prod";
    public String PASSWORD;// = "4htasef128";
    public String CONNECTSTRING_HISTORICO;
    public String USER_HISTORICO;
    public String PASSWORD_HISTORICO;
    //PREPROD1
//    public final static String RUTA_REPORTES_HISTORICOS_CARGA_Y_COMPROMISOS = "/u1/especiales/sisinfo/servapp/preprod3/srcReportesJasper/cargayCompHistoricos/";
//    public final static String DRIVER = "com.mysql.jdbc.Driver";
//    public final static String CONNECTSTRING = "jdbc:mysql://sistemas.uniandes.edu.co:3306/preprod1";
//    public final static String USER = "preprod1";
//    public final static String PASSWORD = "5tgbnhy6";
//    private String rutaReportes = "/u1/especiales/sisinfo/servapp/preprod1/srcReportesJasper/";
////    public final static String RUTA_REPORTES_HISTORICOS_CARGA_Y_COMPROMISOS = "C:\\TEMP\\";
//    public final static String DRIVER = "com.mysql.jdbc.Driver";
//    public final static String CONNECTSTRING = "jdbc:mysql://localhost:3306/sisinfo";
//    public final static String USER = "root";
//    public final static String PASSWORD = "mysql";
//    private String rutaReportes = "C:\\TEMP\\";
    @PersistenceContext
    private EntityManager em;
    private ServiceLocator serviceLocator;
    /**
     * Reportes históricos Bean
     */
    @EJB
    private h_reportesFacadeRemote reportesHistoricos;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    public ReportesFacade() {
        try {
            serviceLocator = new ServiceLocator();
            reportesHistoricos = (h_reportesFacadeRemote) serviceLocator.getRemoteEJB(h_reportesFacadeRemote.class);
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            CONNECTSTRING = constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_CONNECTSTRING);
            USER = constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_USER);
            PASSWORD = constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_PASSWORD);
            CONNECTSTRING_HISTORICO = constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_CONNECTSTRING_HISTORICO);
            USER_HISTORICO = constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_USER_HISTORICO);
            PASSWORD_HISTORICO = constanteBean.getConstante(Constantes.CONFIGURACION_PARAM_PASSWORD_HISTORICO);
        } catch (NamingException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setRutaReportes(String ruta) {
        rutaReportes = ruta;
    }

    /**
     * Método que compila y persiste un reporte a disco, en la ruta padre del reporte dado como parámetro
     * @param rutaReporte ruta del archivo fuente del reporte
     * @return true si crea satisfactoriamente el archivo, false de lo contrario
     */
    public boolean compilarYPersistirReporte(String rutaReporte) {
        JasperReport jasperReport;
        String strJasperNomArchivo = rutaReporte.replaceAll(".jrxml", ".jasper");
        try {
            jasperReport = JasperCompileManager.compileReport(rutaReporte);
            JRSaver.saveObject(jasperReport, strJasperNomArchivo);
            File jasperCompilado = new File(strJasperNomArchivo);
            if (jasperCompilado.exists()) {
                //Se creó satisfactoriamente el archivo
                return true;
            } else {
                return false;
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void ejemploInclusionPDF() {
        try {
            List<InputStream> pdfs = new ArrayList<InputStream>();
            pdfs.add(new FileInputStream("c:\\temp\\1.pdf"));
            pdfs.add(new FileInputStream("c:\\temp\\2.pdf"));
            OutputStream output = new FileOutputStream("c:\\temp\\merge.pdf");
            concatPDFs(pdfs, output, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unirVariosPdfs(Collection<String> nombresPDFs, String nombreARchivoResultado) throws Exception {

        List<InputStream> pdfs = new ArrayList<InputStream>();
        for (String string : nombresPDFs) {
            pdfs.add(new FileInputStream(string));
        }
        OutputStream output = new FileOutputStream(nombreARchivoResultado);
        concatPDFs(pdfs, output, true);

    }

    //@Override
    public void hacerReporte() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteArchivos.xls");

            destFileXLS.createNewFile();
            try {
                HashMap parameters = new HashMap();
                //em.createQuery("SELECT p FROM Profesor p");
                //parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
                jasperReport = JasperCompileManager.compileReport(rutaReportes + "jasperreports_demo.jrxml");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
                /*JRXlsExporter exporter = new JRXlsExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporter.exportReport();*/
                JasperExportManager.exportReportToPdfFile(jasperPrint, "c:/temp/reports/simple_report.pdf");
            } catch (JRException e) {
                e.printStackTrace();
            }
            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport("C:/temp/reports/jasperreports_ArchivosProfesores.jrxml");
                String driver = "com.mysql.jdbc.Driver";
                String connectString = "jdbc:mysql://localhost:3306/sisinfo";
                String user = "root";
                String password = "mysql";
                Class.forName(driver);
                Connection conexion;
                conexion = DriverManager.getConnection(connectString, user, password);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, "c:/temp/reports/reporte_archivosProfesores.pdf");
                //eXPORTAR ARCHIVO A XLS
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                OutputStream outputfile = new FileOutputStream(new File(destFileXLS.getAbsolutePath()));
                // coding For Excel:
                JRXlsExporter exporterXLS = new JRXlsExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporterXLS.exportReport();
                try {
                    outputfile.write(output.toByteArray());
                    output.close();
                    outputfile.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("EXPORTANDO A XLS");
                //transaction.rollback();
                //session.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static Session crearSesion() {
        SessionFactory sessionFactory/* = new Configuration().configure().buildSessionFactory()*/;
        Configuration config = new Configuration();

        config.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");        //CAMBIO
        config.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/sisinfo");
        config.setProperty("hibernate.connection.username", "root");
        config.setProperty("hibernate.connection.password", "mysql");
        config.setProperty("hibernate.hbm2ddl.auto", "update");
        sessionFactory = config.buildSessionFactory();
        //sessionFactory = new Configuration().configure("C:\\temp\\hibernateReportes.cfg.xml").buildSessionFactory();;
        return sessionFactory.openSession();
    }

    @Override
    public String hacerReporteArchivosProfesores(String idArchivo, String tipoArchivo) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteArchivos.xls");
            destFileXLS.createNewFile();
            try {
                HashMap parameters = new HashMap();
                jasperReport = JasperCompileManager.compileReport(rutaReportes + "jasperreports_ArchivosProfesores_secciones_con_archivos.jrxml");
                //Se compila y persiste el reporte JASPER en disco
                compilarYPersistirReporte(rutaReportes + "jasperreports_ArchivosProfesores_secciones_con_archivos.jrxml");
                //
                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("idArchivo", Integer.parseInt(idArchivo));
                parameters.put("ReportTitle", "Secciones sin archivos de " + tipoArchivo);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporte_archivosProfesores.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReporteEstadoInscripcionInvitados(String idInscripcion) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteInscripcion.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteInscripcion.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("idInscripcion", Integer.parseInt(idInscripcion));
                parameters.put("ReportTitle", "Estado Inscripciones inscripción " + idInscripcion);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteInscripcion.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Connection darConexion() {
        try {
            String driver = DRIVER;
            String connectString = CONNECTSTRING;
            String user = USER;
            String password = PASSWORD;
            Class.forName(driver);
            System.out.println("Connect String:" + connectString);
            System.out.println("User:" + user);
            System.out.println("Pssword:" + password);
            return DriverManager.getConnection(connectString, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Connection darConexionHistorico() {
        try {
            String driver = DRIVER;
            String connectString = CONNECTSTRING_HISTORICO;
            String user = USER_HISTORICO;
            String password = PASSWORD_HISTORICO;
            Class.forName(driver);
            System.out.println("Connect String:" + connectString);
            System.out.println("User:" + user);
            System.out.println("Pssword:" + password);
            return DriverManager.getConnection(connectString, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReporteConfirmadosAInscripcion(String idInscripcion) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteInscritos.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteInscritos.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("idInscripcion", Integer.parseInt(idInscripcion));
                parameters.put("ReportTitle", "Lista Confirmados a inscripción " + idInscripcion);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteInscritos.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReporteEstadoSolicitudesMonitoresCursos() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteEstadoProcesoCursos.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportEstadoProcesoCursos.jrxml");

                Connection conexion = darConexion();

                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte Estado Proceso monitorías");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteEstadoProcesoCursos.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReporteEstadoSolicitudesMonitoresCursosPorEstado(String estado) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteEstadoProcesoCursos_porEstado_" + estado + ".xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportEstadoProcesoCursos_porEstado.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("estadoSolicitud", estado);
                parameters.put("ReportTitle", "Reporte por Estado de Cursos - " + estado);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteEstadoProcesoCursos_porEstado.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReporteNotasMonitores() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteNotasMonitores.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportNotasMonitores.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte Notas de monitores");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteNotasMonitores.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String hacerReporteSeccionesSinMonitor() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteSeccionesSinMonitor.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportSeccionesSinMonitor.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte Secciones sin monitor");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reportSeccionesSinMonitor.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String hacerReporteCursosConDobleMonitoria() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteCursosConDobleMonitoria.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportCursosConDobleMonitoria.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte monitores con doble monitoría");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reportCursosConDobleMonitoria.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String hacerReporteCursosConUnaMonitoria() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteCursosConUnaMonitoria.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportCursosConUnaMonitoria.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte monitores con una monitoría");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reportCursosConUnaMonitoria.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);

                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReporteMonitoresProfesor(String loginProfesor) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteMonitoresProfesor_" + loginProfesor + ".xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportMonitoresProfesor.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte monitores - " + loginProfesor);
                parameters.put("correoProfesor", loginProfesor);
                parameters.put("nombresProfesor", "");
                parameters.put("apellidosProfesor", "");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reportMonitoresProfesor_" + loginProfesor + ".pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReporteMonitorPorCriterios(String correoEstudiante, String codigoEstudiante, String nombresEstudiante, String apellidosEstudiante, String notaEstudiante, String operadorNota) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteMonitoriasEstudiante.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportMonitoriasEstudianteCorreo.jrxml");

                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte monitorías");
                parameters.put("codigoEstudiante", codigoEstudiante);
                parameters.put("correo", correoEstudiante);
                parameters.put("nombres", nombresEstudiante);
                parameters.put("apellidos", apellidosEstudiante);
                parameters.put("nota", notaEstudiante);
                if (operadorNota == null || operadorNota.equals("")) {
                    parameters.put("operadorNota", ">=");
                    parameters.put("nota", "0");
                } else if (operadorNota.equals("menor")) {
                    parameters.put("operadorNota", "<");
                } else if (operadorNota.equals("menorIgual")) {
                    parameters.put("operadorNota", "<=");
                } else if (operadorNota.equals("igual")) {
                    parameters.put("operadorNota", "=");
                } else if (operadorNota.equals("mayorIgual")) {
                    parameters.put("operadorNota", ">=");
                } else if (operadorNota.equals("mayor")) {
                    parameters.put("operadorNota", ">");
                }

                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                //JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reportMonitoriasEstudiante.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                //Llamar reporte de histórico
                reportesHistoricos.setRutaReportes(rutaReportes);
                String rutaArchivoHistorico = reportesHistoricos.hacerReporteMonitoriasHistoricosPorCorreo(correoEstudiante);
                File archivoHistorico = new File(rutaArchivoHistorico);

                //Hacer un ZIP
                Collection<File> archivosAComprimir = new ArrayList<File>();
                archivosAComprimir.add(archivoHistorico);
                archivosAComprimir.add(destFileXLS);
                int BUFFER = 2048;
                FileOutputStream dest = null;
                File archivoDestinoZip = new File(getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS) + "reporteMonitorias.zip");

                dest = new FileOutputStream(archivoDestinoZip);
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                byte data[] = new byte[BUFFER];
                for (File file : archivosAComprimir) {
                    BufferedInputStream origin;
                    //System.out.println("Adding: " + files[i]);
                    FileInputStream fi = new FileInputStream(file);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(file.getName());
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
                out.close();
                //Enviar el ZIP
                return archivoDestinoZip.getAbsolutePath().replaceAll("\\\\", "/");
                //transaction.rollback();
                //session.close();
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReportesContactosCrm() {

        JasperReport jasperReport;
        JasperPrint jasperPrintAux, jasperPrintFin;
        int BUFFER = 2048;
        FileOutputStream dest = null;
        File archivoDestino = new File(getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS) + "reporteSolsMatBib.zip");
        try {
            Collection<File> archivosAEnviar = new ArrayList<File>();

            //EXPORTAR ARCHIVO A XLS de solicitudes
            File destFileXLS1 = new File(rutaReportes, "reporteContactos.xls");
            destFileXLS1.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportContactosCrm.jrxml");

            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            parameters.put("ReportTitle", "Reporte Contactos");
            jasperPrintFin = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A XLS
            exportarAXLS(destFileXLS1, jasperPrintFin);
            archivosAEnviar.add(destFileXLS1);

            //EXPORTAR ARCHIVO A XLS de costos del Depto
            File destFileXLS2 = new File(rutaReportes, "Total_Contactos.xls");
            destFileXLS2.createNewFile();
            //---------------------------------------
            parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "subreporteTotalContactos.jrxml");

            //conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            jasperPrintAux = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            List paginas = jasperPrintAux.getPages();
            int i = 1;
            for (Object object : paginas) {
                jasperPrintFin.addPage(i++, (JRPrintPage) object);
            }

            //eXPORTAR ARCHIVO A XLS
            exportarAXLSPaginaPorSheet(destFileXLS2, jasperPrintFin);
            //archivosAEnviar.add(destFileXLS2);
            return (destFileXLS2.getAbsolutePath()).replaceAll("\\\\", "/");


        } catch (JRException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return archivoDestino.getAbsolutePath();
    }

    private void exportarAXLS(File destFileXLS, JasperPrint jasperPrint) {
        OutputStream outputfile = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            outputfile = new FileOutputStream(new File(destFileXLS.getAbsolutePath()));
            // coding For Excel:
//            Este bloque de codigo dejo de funcionar en la migracion de servidor en julio/2014 y se reemplazo el siguiente bloque
            
//            JRXlsExporter exporterXLS = new JRXlsExporter();
//            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
//            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//            exporterXLS.exportReport();

            JExcelApiExporter jExcelApiExporter = new JExcelApiExporter();
            jExcelApiExporter.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jasperPrint);
            jExcelApiExporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            jExcelApiExporter.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, output);
            jExcelApiExporter.setParameter(JExcelApiExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
            jExcelApiExporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            jExcelApiExporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            jExcelApiExporter.setParameter(JExcelApiExporterParameter.OFFSET_X, 0);
            jExcelApiExporter.setParameter(JExcelApiExporterParameter.OFFSET_Y, 0);
            jExcelApiExporter.exportReport();

            try {
                outputfile.write(output.toByteArray());
                output.close();
                outputfile.close();
            } catch (IOException ex) {
                System.out.println("Exception en exportarAXLS 1");
                Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (JRException ex) {
            System.out.println("Exception en exportarAXLS 2");
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            System.out.println("Exception en exportarAXLS 3");
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputfile.close();
            } catch (IOException ex) {
                System.out.println("Exception en exportarAXLS 4");
                Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void exportarAXLSPaginaPorSheet(File destFileXLS, JasperPrint jasperPrint) {
        OutputStream outputfile = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            outputfile = new FileOutputStream(new File(destFileXLS.getAbsolutePath()));
            // coding For Excel:
            JExcelApiExporter exporterXLS = new JExcelApiExporter();
            exporterXLS.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXLS.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, output);
            exporterXLS.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
            exporterXLS.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporterXLS.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporterXLS.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporterXLS.exportReport();
            try {
                outputfile.write(output.toByteArray());
                output.close();
                outputfile.close();
            } catch (IOException ex) {
                Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (JRException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputfile.close();
            } catch (IOException ex) {
                Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String hacerReporteMonitoresCursos(String curso) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reportCursoMonitorias.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportCursoMonitorias.jrxml");

                Connection conexion = darConexion();

                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte monitores");
                parameters.put("curso", curso);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reportCursoMonitorias.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");

            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String hacerReporteMonitoresCursosCupi2() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reportCursoMonitoriasCupi2.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportMonitoresCursosCupi2.jrxml");

                Connection conexion = darConexion();

                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte monitores");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                //JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reportMonitoresCursosCupi2.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");

            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String hacerReporteCargaYcompromisosDepto(Long idPeriodo, String nombrePeriodo, String rutaGuardado, String rutaHistoricos) {
        nombrePeriodo.trim();
        nombrePeriodo = nombrePeriodo.replaceAll(" ", "");
        String nombreArchivo = "REPORTECargaYCompromisos[" + nombrePeriodo + "].pdf";

        //1. si existe el reporte en historicos lo devuelve
        String rutaDelRerporteExistente = buscarReporteCargaYCompromisos(rutaHistoricos, nombreArchivo);
        if (rutaDelRerporteExistente != null) {
            return rutaDelRerporteExistente;
        } else {
            //2. si no existe lo crea en la ruta enviada como parametro.
            try {
                String rutaReportesTemp = rutaReportes;//rutaGuardado;// "C:/DepositoAfterDatosMaestros/repoTotal/SisinfoBeans/CargaYCompromisoProf/datosReportes/";
                compilarSubReportesCargaYCompromisos(rutaReportesTemp);
                String[] reportes = new String[7];
                reportes[0] = "CursosProfesores";//"subReportePag1CursosYCarga";
                reportes[1] = "cuadroTesisYPublicaciones";//"subReportePag2v3";
                reportes[2] = "ProyectosFinanciados";//"subReportePag3";
                reportes[3] = "reportePublicacionesPorProfesor";//"subReporteCargasProfesoresTCE";
                reportes[4] = "reporteGraficoProyectosFinanciados";//"graficoTCEPorGrupo";
                reportes[5] = "subReporteContribucionTesisPorProfesor";//"subReporteContribucionTesisPorProfesor";
                reportes[6] = "cuadroTesisEsperadas";//"subReportecontibTesisPorGrupo";
               /* reportes[7] = "";//"reporteProfEst-tesis";
                reportes[8] = //"subReporteGrafProfTCEyTesisDiscrimGuupo";
                reportes[9] = //"subReporteContribucionTesisEsperada";
                reportes[10] = //"SUbAportePublicacionesPorProfesor";
                reportes[11] = //"subAportePubGrafGrupos";*/
                HashMap paramBasicos = new HashMap();

                paramBasicos.put("SUBREPORT_DIR", rutaReportesTemp);
                paramBasicos.put("idPeriodo", idPeriodo);
                paramBasicos.put("nombrePeriodo", nombrePeriodo);
                paramBasicos.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
                ArrayList<JasperPrint> jasperPrint = new ArrayList<JasperPrint>();
                Connection conexion = darConexion();
                //----------AHORA CREAR LOS JASPER PRINTS------------------------
                for (int i = 0; i < reportes.length; i++) {
                    try {
                        if (reportes[i].equals("cuadroTesisEsperadas")) {
                            paramBasicos.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                        } else {
                            paramBasicos.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
                        }
                        JasperReport jasperReport = JasperCompileManager.compileReport(rutaReportesTemp + reportes[i] + ".jrxml");
                        JasperPrint print = JasperFillManager.fillReport(jasperReport, paramBasicos, conexion);
                        jasperPrint.add(print);
                    } catch (JRException ex) {
                        Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //------exportar reportes
                JRPdfExporter exporter = new JRPdfExporter();
                String rutaArchivo = rutaGuardado + nombreArchivo;
                exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, rutaArchivo);
                exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);

                exporter.exportReport();


                //------exportar reportes
//            JRAbstractExporter absE = new JRPdfExporter();

//            absE.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "c:/temp/REPORTECompromisos.pdf");
//            absE.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrint);
//            absE.exportReport();
                return rutaArchivo;
            } catch (JRException ex) {
                Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                return null;
            }
        }


    }

    public String hacerReporteCargaYcompromisosDeptoEstadoActual() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reportEstadoActualCargaYCompromisos.xls");

            destFileXLS.createNewFile();

            try {
                HashMap parameters = new HashMap();

                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportEstadoActualCargaYCompromisos.jrxml");

                Connection conexion = darConexion();

                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("ReportTitle", "Reporte Estado Actual Carga y Compromisos");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reportCargaYCompromisos.pdf");
                //eXPORTAR ARCHIVO A XLS
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");

            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    private boolean compilarSubReportesCargaYCompromisos(String ruta) {
        boolean todoEnOrden = true;
        String[] subReportes = new String[10];//8

        subReportes[0] = "subReporteCursosPreYMagProfesores";
        subReportes[1] = "subReporteCursosEspProf";
        subReportes[2] = "subOtrasActividades";

        subReportes[3] = "subReporteIdProyectosFInProfesor";
        subReportes[4] = "subReporteTotalPorTipoPublicacion";
        subReportes[5] = "subRerporteGranTotalPublicaciones";

        subReportes[6] = "reporteIndicadoresTesisProfesores";
        subReportes[7] = "subReporteTotalAsesoradosTesisPregradoProfesor";
        subReportes[8] = "subReporteTotalAsesoradosTesis1Profesor";
        subReportes[9] = "subReporteTotalAsesoradosTesis2Profesor";
        // subReportes[7] = "subReporteContribucionTesisPorProfesor";

        for (int i = 0; i < subReportes.length; i++) {
            todoEnOrden = todoEnOrden && compilarYPersistirReporte(ruta + subReportes[i] + ".jrxml");
        }
        return todoEnOrden;
    }

    private Connection darConexionTest() {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String connectString = "jdbc:mysql://localhost:3306/preprod3";
            String user = "preprod3";
            String password = "5tgbnhy6";
            Class.forName(driver);
            return DriverManager.getConnection(connectString, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String buscarReporteCargaYCompromisos(String rutaHistoricos, String nombreArchivo) {
        //verifica si el archivo existe,
        //de ser así retorna la ruta completa incluyendo el nombre
        String rutaArchivo = rutaHistoricos + nombreArchivo;
        File fArchivo = new File(rutaArchivo);
        if (fArchivo.exists()) {
            return rutaArchivo;
        }
        //en caso contrario retorna nulo
        return null;
    }

    /*
     * Reportes de TESIS
     *
     */
    public String hacerHojaParaCarpetaEstudiantesTesis1PorEstadoYsemestre(String semestre, String estado) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteDetalladoInscritosATesis1.xls");
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            String[] reportes = new String[2];
            reportes[0] = "reporteParaCarpetaEstudianteInscripcionTesis1";
            reportes[1] = "reporteParaCarpetasEstudiantesTesis1PorEstadoYsemestre";


            for (int i = 0; i < reportes.length; i++) {
                String string = reportes[i];
                //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                compilarYPersistirReporte(rutaReportes + string + ".jrxml");
            }
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteParaCarpetasEstudiantesTesis1PorEstadoYsemestre.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);

            parameters.put("fechaDesde", semestre);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("estado", estado);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "ListaEstudiantesTesis1_semestre_Y_DETALLES" + ".pdf");
            //eXPORTAR ARCHIVO A PDF
            //exportarAXLS(destFileXLS, jasperPrint);
            //return (rutaReportes + "ListaEstudiantesTesis1_semestre_Y_DETALLES" + ".pdf").replaceAll("\\\\", "/");
            exportarAXLS(destFileXLS, jasperPrint);
            return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");

            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String darReporteDetalladoEstudiantePorId(String idTesis) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteDetalladoInscritosATesis1.xls");
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            String[] reportes = new String[1];
            //  reportes[0] = "reporteListaEstudiantesTesisPorEstadoYSemestre";
            reportes[0] = "reporteParaCarpetaEstudianteInscripcionTesis1";

            for (int i = 0; i < reportes.length; i++) {
                String string = reportes[i];
                //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                compilarYPersistirReporte(rutaReportes + string + ".jrxml");
            }
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteParaCarpetaEstudianteInscripcionTesis1.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

            parameters.put("idSirve", idTesis);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "Tesis1_DETALLES_estudiante" + ".pdf");
            //eXPORTAR ARCHIVO A PDF
            //exportarAXLS(destFileXLS, jasperPrint);
            return (rutaReportes + "Tesis1_DETALLES_estudiante" + ".pdf").replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReprotesParaCarpetasEstudiantesTesis2(String semestre, String estado) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteDetalladoInscritosATesis1.xls");
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            String[] reportes = new String[2];
            reportes[0] = "reporteParaCarpetaTesis2";
            reportes[1] = "reportesParaCarpetaEstudiantesTesis2PorSemestreYEstado";


            for (int i = 0; i < reportes.length; i++) {
                String string = reportes[i];
                //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                compilarYPersistirReporte(rutaReportes + string + ".jrxml");
            }
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportesParaCarpetaEstudiantesTesis2PorSemestreYEstado.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);

            parameters.put("fechaDesde", semestre);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("estado", estado);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "ListaEstudiantesTesis2_semestre_Y_DETALLES" + ".pdf");
            //eXPORTAR ARCHIVO A PDF
            //exportarAXLS(destFileXLS, jasperPrint);
            //return (rutaReportes + "ListaEstudiantesTesis2_semestre_Y_DETALLES" + ".pdf").replaceAll("\\\\", "/");
            exportarAXLS(destFileXLS, jasperPrint);
            return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");

            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteParaCarpetatesis2Estudiante(String id) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteDetalladoInscritosATesis2.xls");
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            String[] reportes = new String[1];
            //  reportes[0] = "reporteListaEstudiantesTesisPorEstadoYSemestre";
            reportes[0] = "reporteParaCarpetaTesis2";

            for (int i = 0; i < reportes.length; i++) {
                String string = reportes[i];
                //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                compilarYPersistirReporte(rutaReportes + string + ".jrxml");
            }
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteParaCarpetaTesis2.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);

            parameters.put("idSirve", Long.parseLong(id));
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "Tesis2_DETALLES_estudiante" + ".pdf");
            //eXPORTAR ARCHIVO A PDF
            Collection<String> lista = new ArrayList<String>();
            lista.add(rutaReportes + "Tesis2_DETALLES_estudiante" + ".pdf");

            String nombreArchivoResultado = rutaReportes + "Tesis2_DETALLES_estudiante_" + id + "_.pdf";

            unirVariosPdfs(lista, nombreArchivoResultado);

            //exportarAXLS(destFileXLS, jasperPrint);
            return nombreArchivoResultado.replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Metodo que concatena los archivos PDF quer vengan en la lista dada como parámetro
     * @param streamOfPDFFiles lista de archivos PDF a concatenar
     * @param outputStream el flujo de salida en el que se persistirá el resultado de la concatenación
     * @param paginate
     */
    public void concatPDFs(List<InputStream> streamOfPDFFiles,
            OutputStream outputStream, boolean paginate) {

        Document document = new Document();
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Crear los flujos de lectura para cada uno de los archivos PDF
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Crear el flujo de escritura para el archivo de salida
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // tiene el PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Iterar sobre los PDF para adicionar cont de cada uno
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // crear páginas en la salida por cada página de los PDF fuente
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader,
                            pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);

                    // Paginacion
                    if (paginate) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
                                + currentPageNumber + " of " + totalPages, 520,
                                5, 0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public String hacerCartaTesisJuradoSustentacion(String tipo, String nombresEstudiante, String nombresJurado, Long idTesis) {
        try {

            double aleatorio = Math.random() * 987654;

            String[] reportes = new String[1];
            if (tipo.equals("ASESOR")) {
                reportes[0] = "calificacionAsesor";
            } else if (tipo.equals("JURADO_INTERNO")) {
                reportes[0] = "calificacionJuradoInterno";
            } else {
                reportes[0] = "calificacionJuradoExterno";
            }
            JasperReport jasperReport;
            JasperPrint jasperPrint;


            //---------1ero compilar reportes--------

            //  reportes[0] = "reporteListaEstudiantesTesisPorEstadoYSemestre";

            for (int i = 0; i < reportes.length; i++) {
                String string = reportes[i];
                //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                compilarYPersistirReporte(rutaReportes + string + ".jrxml");
            }
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + reportes[0] + ".jrxml");
            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
            parameters.put("idTesis", idTesis);
            parameters.put("nombreCandidato", nombresEstudiante);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            parameters.put("nombreJurado", nombresJurado);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + reportes[0] + aleatorio + ".pdf");
            //eXPORTAR ARCHIVO A PDF

            String nombreArchivoResultado = rutaReportes + reportes[0] + aleatorio + ".pdf";

            //exportarAXLS(destFileXLS, jasperPrint);
            return nombreArchivoResultado.replaceAll("\\\\", "/");
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteReservaCitas() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reservas.xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            compilarYPersistirReporte(rutaReportes + "reporteReservas" + ".jrxml");
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteReservas.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

            parameters.put("tituloReporte", "RESERVA DE CITAS DE COORDINACIÓN INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A PDF
            exportarAXLS(destFileXLS, jasperPrint);
            return (rutaReportes + "reservas" + ".xls").replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteConflictoHorarios() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteConflictoHorarios.xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            compilarYPersistirReporte(rutaReportes + "reporteConflictoHorarios" + ".jrxml");
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteConflictoHorarios.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A PDF
            exportarAXLS(destFileXLS, jasperPrint);
            return (rutaReportes + "reporteConflictoHorarios" + ".xls").replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteSolicitudesMaterialBibliograficoPorSolicitante(String correoSolicitante) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "solicitudesPorSolicitante" + correoSolicitante.split("@")[0] + ".xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            parameters.put("correoSolicitante", correoSolicitante);
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteSolicitudesMaterialBibliograficoPorSolicitante.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);

            parameters.put("tituloReporte", "Solicitudes de Material Bibiliográfico para " + correoSolicitante);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A PDF
            exportarAXLS(destFileXLS, jasperPrint);
            return (destFileXLS.getAbsolutePath()).replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteSolicitudesMaterialBibliografico() {
        //return hacerReporteSolicitudesMaterialBibliograficoIndividual_ConsolidadoCostosDepto();
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "solicitudesMaterialBibliografico.xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteSolicitudesMaterialBibliograficoDepto.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);


            parameters.put("tituloReporte", "Solicitudes de Material Bibiliográfico Departamento");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A PDF
            exportarAXLS(destFileXLS, jasperPrint);
            return (destFileXLS.getAbsolutePath()).replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteSolicitudesMaterialBibliograficoIndividual_ConsolidadoCostosDepto() {

        JasperReport jasperReport;
        JasperPrint jasperPrintAux, jasperPrintFin;
        int BUFFER = 2048;
        FileOutputStream dest = null;
        File archivoDestino = new File(getConstanteBean().getConstante(Constantes.RUTA_TEMPORAL_ADJUNTOS) + "reporteSolsMatBib.zip");
        try {
            Collection<File> archivosAEnviar = new ArrayList<File>();

            //EXPORTAR ARCHIVO A XLS de solicitudes
            File destFileXLS1 = new File(rutaReportes, "solicitudesMaterialBibliografico.xls");
            destFileXLS1.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteSolicitudesMaterialBibliograficoDepto.jrxml");

            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            parameters.put("tituloReporte", "Solicitudes de Material Bibiliográfico Departamento");
            jasperPrintFin = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A XLS
            exportarAXLS(destFileXLS1, jasperPrintFin);
            archivosAEnviar.add(destFileXLS1);

            //EXPORTAR ARCHIVO A XLS de costos del Depto
            File destFileXLS2 = new File(rutaReportes, "solicitudesMaterialBibliograficoCostos.xls");
            destFileXLS2.createNewFile();
            //---------------------------------------
            parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteSolicitudesMaterialBibliograficoConsolidadoLibrosProfesor.jrxml");

            //conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            parameters.put("tituloReporte", "Costos Solicitudes de Material Bibiliográfico Departamento");
            jasperPrintAux = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            List paginas = jasperPrintAux.getPages();
            int i = 1;
            for (Object object : paginas) {
                jasperPrintFin.addPage(i++, (JRPrintPage) object);
            }

            //eXPORTAR ARCHIVO A XLS
            exportarAXLSPaginaPorSheet(destFileXLS2, jasperPrintFin);
            //archivosAEnviar.add(destFileXLS2);
            return (destFileXLS2.getAbsolutePath()).replaceAll("\\\\", "/");


        } catch (JRException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return archivoDestino.getAbsolutePath();
    }

    public String hacerReporteTodasOfertasBolsaEmpleo() {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteOfertasBolsaEmpleo.xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteBolsaEmpleoOfertas.jrxml");

            Connection conexion = darConexion();

            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);


            parameters.put("tituloReporte", "Reporte Ofertas de Bolsa Empleo");
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A XLS
            exportarAXLS(destFileXLS, jasperPrint);
            return (destFileXLS.getAbsolutePath()).replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public String hacerReporteEstudiantesAceptadosEnTesisPregrado(String estado, String semestre) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteListaInscritosATesisPregrado.xls");
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            String[] reportes = new String[1];
            reportes[0] = "listaEstudiantesAceptadosTesisPregrado";
            for (int i = 0; i < reportes.length; i++) {
                String string = reportes[i];
                //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                compilarYPersistirReporte(rutaReportes + string + ".jrxml");
            }
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "listaEstudiantesAceptadosTesisPregrado.jrxml");
            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            parameters.put("periodo", semestre);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("estado", estado);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "ListaEstudiantesTesisPregrado_semestre_" + ".pdf");
            //eXPORTAR ARCHIVO A PDF
            //exportarAXLS(destFileXLS, jasperPrint);
            //return (rutaReportes + "ListaEstudiantesTesisPregrado_semestre_" + ".pdf").replaceAll("\\\\", "/");
            exportarAXLS(destFileXLS, jasperPrint);
            return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");

        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteNotasEstudiantesEnTesisPregrado(String estado, String semestre, String formatoReporte) {
        try {
            if (formatoReporte.equals("XLS")) {
                JasperReport jasperReport;
                JasperPrint jasperPrint;
                File destFileXLS = new File(rutaReportes, "reporteNotasTesisPregrado.xls");
                destFileXLS.createNewFile();
                //---------1ero compilar reportes--------
                String[] reportes = new String[1];
                reportes[0] = "reporteNotasTesisPregrado";
                for (int i = 0; i < reportes.length; i++) {
                    String string = reportes[i];
                    //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                    compilarYPersistirReporte(rutaReportes + string + ".jrxml");
                }
                //---------------------------------------
                HashMap parameters = new HashMap();
                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteNotasTesisPregrado.jrxml");
                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("periodo", semestre);
                parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
                parameters.put("estado", estado);
                parameters.put("SUBREPORT_DIR", rutaReportes);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteNotasTesisPregrado_semestre_" + ".xls");
                //eXPORTAR ARCHIVO A PDF
                //exportarAXLS(destFileXLS, jasperPrint);
                //return (rutaReportes + "reporteNotasTesisPregrado_semestre_" + ".pdf").replaceAll("\\\\", "/");
                exportarAXLS(destFileXLS, jasperPrint);
                return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
            } else {
                JasperReport jasperReport;
                JasperPrint jasperPrint;
                //---------------------------------------

                System.out.println(semestre);
                HashMap parameters = new HashMap();
                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteNotasTesisPregradoPdf.jrxml");
                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
                parameters.put("periodo", semestre);
                parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
                parameters.put("estado", estado);
                parameters.put("SUBREPORT_DIR", rutaReportes);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteNotasTesisPregrado_semestre" + ".pdf");
                //eXPORTAR ARCHIVO A PDF
                //   exportarAXLS(destFileXLS, jasperPrint);
                return (rutaReportes + "reporteNotasTesisPregrado_semestre" + ".pdf").replaceAll("\\\\", "/");
//                exportarAXLS(destFilePDF, jasperPrint);
//                return destFilePDF.getAbsolutePath().replaceAll("\\\\", "/");

            }
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    //REPORTES RESERVAS E INVENTARIO ----------------------------------------------------------------------------------------------------------------------

    public String hacerReporteDeVigenciaDeUsuarios(Long idLaboratorio) {
        //reporte_usuariosPorLaboratorio = nombre reporte
        //parametros:
        //idLaboratorio
        //nombreUniversidad
        //SUBREPORT_DIR

        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporte_Vigencia_usuarios_laboratorio.xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporte_usuariosPorLaboratorio.jrxml");
            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
            parameters.put("idLaboratorio", idLaboratorio);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A XLS
            exportarAXLS(destFileXLS, jasperPrint);
            return (destFileXLS.getAbsolutePath()).replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteUsoElementosLaboratorio(Long idLaboratorio) {
        //reporteUsoEquipoLaboratorio = nombre reporte
        //parametros:
      /*
         * <parameter name="idLaboratorio" class="java.lang.Long"/>
        <parameter name="nombreUniversidad" class="java.lang.String"/>
        <parameter name="SUBREPORT_DIR" class="java.lang.String"/>
         */
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporte_Uso_equipos_laboratorio.xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteUsoEquipoLaboratorio.jrxml");
            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
            parameters.put("idLaboratorio", idLaboratorio);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A XLS
            exportarAXLS(destFileXLS, jasperPrint);
            return (destFileXLS.getAbsolutePath()).replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteHistoriaUsoElementosLaboratorioConHistoria(Long idLaboratorio) {
        //reporteUsoEquipoLaboratorioDetalladoConHistoria = nombre reporte
        //parametros:
      /*
        <parameter name="idLaboratorio" class="java.lang.String"/>
        <parameter name="nombreUniversidad" class="java.lang.String"/>
        <parameter name="SUBREPORT_DIR" class="java.lang.String"/>
         */
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporte_Historia_Uso_equipos_laboratorio.xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteUsoEquipoLaboratorioDetalladoConHistoria.jrxml");
            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
            parameters.put("idLaboratorio", idLaboratorio);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A XLS
            exportarAXLS(destFileXLS, jasperPrint);
            return (destFileXLS.getAbsolutePath()).replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteEquiposLaboratorioNoDevueltos(Long idLaboratorio, String nombreLab, Timestamp fechaInicio) {
        //reporte]ElementosLaboratorioNoDevueltos = nombre reporte
        //parametros:
      /*
        <parameter name="idLaboratorio" class="java.lang.String"/>
        <parameter name="nombreLab" class="java.lang.String"/>
        <parameter name="nombreUniversidad" class="java.lang.String"/>
        <parameter name="SUBREPORT_DIR" class="java.lang.String"/>
        <parameter name="fechaActual" class="java.sql.Timestamp" isForPrompting="false"/>
         */
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporte_Equipos_Laboratorio_No_Devueltos.xls");
            System.out.println(destFileXLS.getAbsolutePath());
            destFileXLS.createNewFile();
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "ElementosLaboratorioNoDevueltos.jrxml");
            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
            parameters.put("idLaboratorio", idLaboratorio);
            parameters.put("nombreLab", nombreLab);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            parameters.put("fechaActual", fechaInicio);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //eXPORTAR ARCHIVO A XLS
            exportarAXLS(destFileXLS, jasperPrint);
            return (destFileXLS.getAbsolutePath()).replaceAll("\\\\", "/");
            //--------------
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteAsistenciasGraduadas(String semestre) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteAsistenciasGraduadas.xls");
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            String[] reportes = new String[1];
            reportes[0] = "reporteAsistenciasGraduadas";
            for (int i = 0; i < reportes.length; i++) {
                String string = reportes[i];
                //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                compilarYPersistirReporte(rutaReportes + string + ".jrxml");
            }
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteAsistenciasGraduadas.jrxml");
            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
            parameters.put("periodo", semestre);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteAsistenciasGraduadas_semestre_" + ".pdf");
            //eXPORTAR ARCHIVO A PDF
            //exportarAXLS(destFileXLS, jasperPrint);
            //return (rutaReportes + "reporteAsistenciasGraduadas_semestre_" + ".pdf").replaceAll("\\\\", "/");
            exportarAXLS(destFileXLS, jasperPrint);
            return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");

        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteNotasAsistenciasGraduadas(String semestre) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteNotasAsistenciasGraduadas.xls");
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            String[] reportes = new String[1];
            reportes[0] = "reporteNotasAsistenciasGraduadas";
            for (int i = 0; i < reportes.length; i++) {
                String string = reportes[i];
                //JasperCompileManager.compileReport(rutaReportes + string + ".jrxml");
                compilarYPersistirReporte(rutaReportes + string + ".jrxml");
            }
            //---------------------------------------
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport(rutaReportes + "reporteNotasAsistenciasGraduadas.jrxml");
            Connection conexion = darConexion();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
            parameters.put("periodo", semestre);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteNotasAsistenciasGraduadas_semestre_" + ".pdf");
            //eXPORTAR ARCHIVO A PDF
            //exportarAXLS(destFileXLS, jasperPrint);
            return (rutaReportes + "reporteNotasAsistenciasGraduadas_semestre_" + ".pdf").replaceAll("\\\\", "/");
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //FIN REPORTES RESERVAS E INVENTARIO-------------------------------------------------------------------------------------------------------------------
    public String hacerReporteConsolidadoNotasEstudiantesEnTesisPregrado(String semestre, String formatoReporte) {
        try {
            HashMap parameters = new HashMap();
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
            parameters.put("periodo", semestre);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            parameters.put("InfoConexion", generarConexionBDPrincipal());
            if (formatoReporte.equals("XLS")) {
                return generarReporteXLS(rutaReportesHistoricos, "reporte_ProyectoGrado_NotasTesis", "reporteProyectoGrado" + semestre, parameters, true);
            } else {
                return generarReportePDF(rutaReportesHistoricos, "reporte_ProyectoGrado_NotasTesis", "reporteProyectoGrado" + semestre, parameters, true);
            }
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerListadoEstudiantesTesis1PorSemestre(String semestre, String estado, String reporteSimple) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("periodo", semestre);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            parameters.put("InfoConexion", generarConexionBDHistoricos());
            String nombreReporte;
            if (!Boolean.valueOf(reporteSimple)) {
                nombreReporte = "reporte_TesisMaestria_ListaEstudiantesTesis1PorSemestre";
            } else {
                nombreReporte = "reporte_TesisMaestria_ListaEstudiantesTesis1PorSemestreSimple";
            }
            return generarReporteXLS(rutaReportesHistoricos, nombreReporte, "ReporteEstudiantesTesis1_" + semestre, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerListaTesis2PorSemestre(String semestre, String reporteSimple) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("periodo", semestre);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            parameters.put("InfoConexion", generarConexionBDHistoricos());
            String nombreReporte;
            if (!Boolean.valueOf(reporteSimple)) {
                nombreReporte = "reporte_TesisMaestria_ListaEstudiantesTesis2PorSemestre";
            } else {
                nombreReporte = "reporte_TesisMaestria_ListaEstudiantesTesis2PorSemestreSimple";
            }
            return generarReporteXLS(rutaReportesHistoricos, nombreReporte, "ReporteEstudiantesTesis2_" + semestre, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteDetalladoInscripcionSubareaEstudiante(String idInscripcionEstudiante) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("idSirve", idInscripcionEstudiante);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            String nombreReporte;
            nombreReporte = "subreporte_TesisMaestria_InscripcionesSubareaDetallesBasicosEstudiante";
            return generarReportePDF(rutaReportesHistoricos, nombreReporte, "ReporteDetalladoInscritoSubarea", parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteDetallesTodosLosInscritosDesde(String fechaDesde) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("fechaDesde", fechaDesde);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");
            parameters.put("SUBREPORT_DIR", rutaReportes);
            String nombreReporte;
            nombreReporte = "reporte_TesisMaestria_InscripcionesSubareaDesdeFechaConDetalle";
            return generarReporteXLS(rutaReportesHistoricos, nombreReporte, "ReporteDetalladoInscritosASubareaDesde" + fechaDesde, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReportenuevosInscritosASubareaDesde(String fechaDesde, String estado) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("fechaDesde", fechaDesde);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");//
            parameters.put("estadoSolicitud", estado);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            String nombreReporte;
            nombreReporte = "reporte_TesisMaestria_InscripcionesSubareaDesdeFecha";
            return generarReporteXLS(rutaReportesHistoricos, nombreReporte, "ReporteInscritosASubareaDesde" + fechaDesde, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteReservaInventarioLaboratorios(String fechaInicial, String fechaFinal) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("fechaInicial", fechaInicial);
            parameters.put("fechaFinal", fechaFinal);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            String nombreReporte;
            nombreReporte = "reporte_ReservaInventario_ReservasLaboratorioRangoFechas";
            return generarReporteXLS(rutaReportesHistoricos, nombreReporte, "ReporteReservasLaboratoriosDesde" + fechaInicial + "Hasta" + fechaFinal, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteReservaInventarioLaboratoriosRangoEstado(String fechaInicial, String fechaFinal, String estado) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("fechaInicial", fechaInicial);
            parameters.put("fechaFinal", fechaFinal);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            parameters.put("estado", estado);
            String nombreReporte;
            nombreReporte = "reporte_ReservaInventario_ReservasLaboratorioRangoFechasYEstado";
            return generarReporteXLS(rutaReportesHistoricos, nombreReporte, "Reporte" + estado + "LaboratoriosDesde" + fechaInicial + "Hasta" + fechaFinal, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteReservaInventarioLaboratoriosEstadistico(String fechaInicial, String fechaFinal) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("fechaInicial", fechaInicial);
            parameters.put("fechaFinal", fechaFinal);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            String nombreReporte;
            nombreReporte = "reporte_ReservaInventario_EstadisticasUso";
            return generarReporteXLS(rutaReportesHistoricos, nombreReporte, "ReporteEstadisticasUsoLaboratoriosDesde" + fechaInicial + "Hasta" + fechaFinal, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteReservasLaboratorioHorariosMasReservados(String fechaInicial, String fechaFinal) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("fechaInicial", fechaInicial);
            parameters.put("fechaFinal", fechaFinal);
            parameters.put("SUBREPORT_DIR", rutaReportes);
            String nombreReporte;
            nombreReporte = "reporte_ReservaInventario_HorariosMasReservados";
            return generarReporteXLS(rutaReportesHistoricos, nombreReporte, "ReporteEstadisticasHorariosMasUsadosDesde" + fechaInicial + "Hasta" + fechaFinal, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String generarReporteXLS(String ruta, String nombreReporte, String nombreArchivo, HashMap parameters, boolean historico) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(ruta, nombreArchivo + ".xls");
            destFileXLS.createNewFile();
            //---------1ero compilar reportes--------
            compilarYPersistirReporte(ruta + nombreReporte + ".jrxml");
            //---------------------------------------
            jasperReport = JasperCompileManager.compileReport(ruta + nombreReporte + ".jrxml");
            compilarSubreportes(ruta, jasperReport);
            Connection conexion;
            if (historico) {
                conexion = darConexionHistorico();
            } else {
                conexion = darConexion();
            }
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            //JasperExportManager.exportReportToPdfFile(jasperPrint, ruta + nombreReporte + ".xls");

            exportarAXLS(destFileXLS, jasperPrint);
            return destFileXLS.getAbsolutePath().replaceAll("\\\\", "/");
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String generarReportePDF(String ruta, String nombreReporte, String nombreArchivo, HashMap parameters, boolean historico) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            //---------------------------------------
            jasperReport = JasperCompileManager.compileReport(ruta + nombreReporte + ".jrxml");
            compilarSubreportes(ruta, jasperReport);
            Connection conexion;
            if (historico) {
                conexion = darConexionHistorico();
            } else {
                conexion = darConexion();
            }
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, ruta + nombreArchivo + ".pdf");
            return (ruta + nombreArchivo + ".pdf").replaceAll("\\\\", "/");
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retorna la conexion sobre la base de datos principal (sisinfo en local,
     * prod en produccion y preprodXX en preproduccionXX para pasar por parametro
     * al reporte de jasper).
     * @return El string de conexion
     */
    private Connection generarConexionBDPrincipal() throws Exception {
        return java.sql.DriverManager.getConnection(CONNECTSTRING, USER, PASSWORD);
    }

    /**
     * Retorna la conexion sobre la base de datos de historicos del ambiente
     * sobre el cual se este desplegando
     * @return El string de conexion
     */
    private Connection generarConexionBDHistoricos() throws Exception {
        return java.sql.DriverManager.getConnection(CONNECTSTRING_HISTORICO, USER_HISTORICO, PASSWORD_HISTORICO);
    }

    private void compilarSubreportes(final String ruta, JasperReport reporte) {
        JRElementsVisitor.visitReport(reporte, new JRVisitor() {

            public void visitBreak(JRBreak jrb) {
            }

            public void visitChart(JRChart jrc) {
            }

            public void visitCrosstab(JRCrosstab jrc) {
            }

            public void visitElementGroup(JRElementGroup jreg) {
            }

            public void visitEllipse(JREllipse jre) {
            }

            public void visitFrame(JRFrame jrf) {
            }

            public void visitImage(JRImage jri) {
            }

            public void visitLine(JRLine jrline) {
            }

            public void visitRectangle(JRRectangle jrr) {
            }

            public void visitStaticText(JRStaticText jrst) {
            }

            public void visitSubreport(JRSubreport jrs) {
                try {
                    String exp = jrs.getExpression().getText();
                    int init = exp.indexOf("\"");
                    int fin = exp.indexOf("\"", init + 1);
                    String fullName = exp.substring(init + 1, fin);
                    String name = fullName.substring(0, fullName.indexOf("."));
                    JasperCompileManager.compileReportToFile(ruta + name + ".jrxml",
                            ruta + name + ".jasper");
                    JasperReport jr = JasperCompileManager.compileReport(ruta + name + ".jrxml");
                    compilarSubreportes(ruta, jr);
                    System.out.println("Subreporte " + name + " compilado");
                } catch (Exception e) {
                    System.err.println("Error compilando subreporte");
                    e.printStackTrace();
                }
            }

            public void visitTextField(JRTextField jrtf) {
            }

            public void visitComponentElement(JRComponentElement jrce) {
            }

            public void visitGenericElement(JRGenericElement jrge) {
            }
        });
    }

    public String hacerReporteContactosCrmInscritosPorEvento(String idEvento, String nombreEvento) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("idEventoExterno", idEvento);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");//
            String nombreReporte;
            nombreReporte = "reporte_ContactosCrm_InscritosPorEvento";
            return generarReporteXLS(rutaReportes, nombreReporte, "Reporte inscritos a evento " + nombreEvento, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteContactosCrmInscritosPorTipoDeEvento(String idTipoEvento, String nombreTipoEvento) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("idTipoEvento", idTipoEvento);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");//
            String nombreReporte;
            nombreReporte = "reporte_ContactosCrm_InscritosPorTipoDeEvento";
            return generarReporteXLS(rutaReportes, nombreReporte, "Reporte inscritos a " + nombreTipoEvento, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteContactosCrmInscritosPorSector(String idSectorEvento, String nombreSectorEvento) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("idSectorEvento", idSectorEvento);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");//
            String nombreReporte;
            nombreReporte = "reporte_ContactosCrm_InscritosPorSector";
            return generarReporteXLS(rutaReportes, nombreReporte, "Reporte contactos sector " + nombreSectorEvento, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteContactosCrmInscritosPorCargo(String idCargoEvento, String nombreCargoEvento) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("idCargoEvento", idCargoEvento);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");//
            String nombreReporte;
            nombreReporte = "reporte_ContactosCrm_InscritosPorCargo";
            return generarReporteXLS(rutaReportes, nombreReporte, "Reporte contactos cargo " + nombreCargoEvento, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String hacerReporteContactosCrmInscritosPorCiudad(String idCiudad, String nombreCiudad) {
        try {
            HashMap parameters = new HashMap();
            parameters.put("idCiudad", idCiudad);
            parameters.put("nombreUniversidad", "DEPARTAMENTO DE INGENIERÍA DE SISTEMAS Y COMPUTACIÓN");//
            String nombreReporte;
            nombreReporte = "reporte_ContactosCrm_InscritosPorCiudad";
            return generarReporteXLS(rutaReportes, nombreReporte, "Reporte contactos ciudad " + nombreCiudad, parameters, false);
        } catch (Exception ex) {
            Logger.getLogger(ReportesFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
