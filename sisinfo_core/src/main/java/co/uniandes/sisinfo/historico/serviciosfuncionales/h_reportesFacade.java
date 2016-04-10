/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.serviciosfuncionales.h_reportesFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.ReportesFacade;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;



/**
 *
 * @author Manuel
 */
@Stateless
public class h_reportesFacade implements h_reportesFacadeLocal,h_reportesFacadeRemote{

//    public final static String RUTA_REPORTES_HISTORICOS = "C:\\DepositoAfterDatosMaestros\\repoTotal\\SisinfoBeans\\CargaYCompromisoProf\\datosReportes\\NuevosReportes\\";
//    public final static String DRIVER = "com.mysql.jdbc.Driver";
//    public final static String CONNECTSTRING = "jdbc:mysql://localhost:3306/historico";
//    public final static String USER = "root";
//    public final static String PASSWORD = "mysql";
//    private String rutaReportes = "C:\\DepositoAfterDatosMaestros\\repoTotal\\SisinfoBeans\\CargaYCompromisoProf\\datosReportes\\NuevosReportes\\";

    public final static String RUTA_REPORTES_HISTORICOS = "/u1/especiales/sisinfo/servapp/prod/srcReportesJasper/";
    public final static String DRIVER = "com.mysql.jdbc.Driver";
    public final static String CONNECTSTRING = "jdbc:mysql://sistemas.uniandes.edu.co:3306/prodhist";
    public final static String USER = "prodhist";
    public final static String PASSWORD = "xdf7w921QSd1";
    private String rutaReportes = "/u1/especiales/sisinfo/servapp/prod/srcReportesJasper/";

    @Override
    public void setRutaReportes(String ruta) {
        rutaReportes = ruta;
    }
    private Connection darConexion() {
        try {
            String driver = DRIVER;
            String connectString = CONNECTSTRING;
            String user = USER;
            String password = PASSWORD;
            Class.forName(driver);
            return DriverManager.getConnection(connectString, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(h_reportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(h_reportesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String hacerReporteMonitoriasHistoricosPorCorreo(String correoEstudiante) {
        try {
            JasperReport jasperReport;
            JasperPrint jasperPrint;
            File destFileXLS = new File(rutaReportes, "reporteMonitoriasHistoricas.xls");
            destFileXLS.createNewFile();
            try {
                HashMap parameters = new HashMap();
                jasperReport = JasperCompileManager.compileReport(rutaReportes + "reportMonitoriasHistoricasEstudianteCorreo.jrxml");
                Connection conexion = darConexion();
                parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
                parameters.put("correoEstudiante", correoEstudiante);
                parameters.put("ReportTitle", "Monitorías en Históricos");
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexion);
                //Exportar Archivo a PDF
                //JasperExportManager.exportReportToPdfFile(jasperPrint, rutaReportes + "reporteInscritos.pdf");
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

    private void exportarAXLS(File destFileXLS, JasperPrint jasperPrint) {
        OutputStream outputfile = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            outputfile = new FileOutputStream(new File(destFileXLS.getAbsolutePath()));
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
}
