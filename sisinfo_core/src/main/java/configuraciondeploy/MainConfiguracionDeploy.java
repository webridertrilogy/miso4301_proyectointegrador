/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configuraciondeploy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
public class MainConfiguracionDeploy {

    //final static String DIRECCION_SERVIDOR = "172.24.40.216";
    final static String DIRECCION_SERVIDOR = "sisinfo.uniandes.edu.co";

    private static Collection<File> buscarArchivosSunresourcesNoHistoricos() {
        //../../SisinfoBeans/ServiciosEnrutacion/setup/sun-resources.xml
        Collection<File> archivosPersis = new ArrayList<File>();
        File f = new File("../../SisinfoBeans/");
        File[] beans = f.listFiles();
        for (File file : beans) {
            if (file.isDirectory()) {
                File[] carpetasBeans = file.listFiles();//nombre carpeta bean
                for (File file1 : carpetasBeans) {
                    if (file1.isDirectory() && file1.getName().equals("setup")) {
                        try {
                            //  System.out.println("ARchivo= " + file1.getPath());
                            String bauu = file1.getCanonicalPath() + "\\" + "sun-resources.xml";
                            // System.out.println("Archivo encontrado=" + bauu);
                            File elArchivo = new File(bauu);
                            if (elArchivo.exists()) {
                                archivosPersis.add(elArchivo);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(MainConfiguracionDeploy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        return archivosPersis;
    }

    private void cambiarConfiguracionServidor(File archivoconfigEnrutacion, String config_sunresources_Enrutacion) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new FileOutputStream(archivoconfigEnrutacion));
        pw.print(config_sunresources_Enrutacion);
        pw.close();
        System.out.println("Sobreescribió el archivo=" + archivoconfigEnrutacion.getAbsolutePath());
    }
    private Collection<Configuracion> configuraciones;

    public MainConfiguracionDeploy() {
        configuraciones = new ArrayList<Configuracion>();
        if (true) {
            String nombreConfiguracion = "Prod";
            String config_sunresources_Enrutacion =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"prod\" object-type=\"user\" pool-name=\"mysql_prod_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"20\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlXADataSource\" fail-all-connections=\"true\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"true\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_prod_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.XADataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + " <property name=\"User\" value=\"prod\"/>"
                    + "<property name=\"Password\" value=\"4htasef128\"/>"
                    + "<property name=\"serverName\" value=\""+DIRECCION_SERVIDOR+"\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"prod\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/prod\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";
            String config_sunresources_Historicos =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"Historico\" object-type=\"user\" pool-name=\"mysql_historico_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"0\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" connection-validation-method=\"auto-commit\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlXADataSource\" fail-all-connections=\"false\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"false\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_historico_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.XADataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "  <property name=\"User\" value=\"prodhist\"/>"
                    + "  <property name=\"Password\" value=\"xdf7w921QSd1\"/>"
                    + "  <property name=\"serverName\" value=\""+DIRECCION_SERVIDOR+"\"/>"
                    + "  <property name=\"portNumber\" value=\"3306\"/>"
                    + "  <property name=\"databaseName\" value=\"prodhist\"/>"
                    + "  <property name=\"URL\" value=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/historico\"/>"
                    + "  <property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";
            String nombreDataSource = "prod";
            String nombreDSHistoricos = "Historico";
            String archivoConstantes = "package co.uniandes.sisinfo.talend;\n"
                    + "/**  @author ArchivoConfig */\n"
                    + "public class ConstantesHistoricos {\n"
                    + "\tpublic final static String CONEXION_PROD=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/prod?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_PROD=\"prod\";\n"
                    + "\tpublic final static String USER_PROD=\"prod\";\n"
                    + "\tpublic final static String PASS_PROD=\"4htasef128\";\n"
                    + "\tpublic final static String CONEXION_HISTORICO=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/prodhist?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_HISTORICO=\"prodhist\";\n"
                    + "\tpublic final static String USER_HISTORICO=\"prodhist\";\n"
                    + "\tpublic final static String PASS_HISTORICO=\"xdf7w921QSd1\";\n"
                    + "}";


            Configuracion c = new Configuracion(nombreConfiguracion, config_sunresources_Enrutacion, config_sunresources_Historicos, nombreDataSource, nombreDSHistoricos, archivoConstantes);
            configuraciones.add(c);

        }
        if (true) {
            String nombreConfiguracion = "Local-Desarrollo";
            String config_sunresources_Enrutacion =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"sisinfoDS\" object-type=\"user\" pool-name=\"mysql_sisinfo_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"20\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlXADataSource\" fail-all-connections=\"true\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"true\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_sisinfo_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.XADataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "<property name=\"User\" value=\"root\"/>"
                    + "<property name=\"Password\" value=\"mysql\"/>"
                    + "<property name=\"serverName\" value=\"localhost\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"sisinfo\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://localhost:3306/sisinfo\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";
            String config_sunresources_Historicos =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"Historico\" object-type=\"user\" pool-name=\"mysql_historico_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"0\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" connection-validation-method=\"auto-commit\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlXADataSource\" fail-all-connections=\"false\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"false\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_historico_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.XADataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "<property name=\"User\" value=\"root\"/>"
                    + "<property name=\"Password\" value=\"mysql\"/>"
                    + "<property name=\"serverName\" value=\"localhost\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"historico\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://localhost:3306/historico\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";

            String nombreDataSource = "sisinfoDS";
            String nombreDSHistoricos = "Historico";

            String archivoConstantes = "package co.uniandes.sisinfo.talend;\n"
                    + "/**  @author ArchivoConfig */\n"
                    + "public class ConstantesHistoricos {\n"
                    + "\tpublic final static String CONEXION_PROD = \"jdbc:mysql://localhost:3306/sisinfo?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_PROD = \"sisinfo\";\n"
                    + "\tpublic final static String USER_PROD = \"root\";\n"
                    + "\tpublic final static String PASS_PROD = \"mysql\";\n"
                    + "\tpublic final static String CONEXION_HISTORICO = \"jdbc:mysql://localhost:3306/historico?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_HISTORICO = \"historico\";\n"
                    + "\tpublic final static String USER_HISTORICO = \"root\";\n"
                    + "\tpublic final static String PASS_HISTORICO = \"mysql\";\n"
                    + "}";

            Configuracion c = new Configuracion(nombreConfiguracion, config_sunresources_Enrutacion, config_sunresources_Historicos, nombreDataSource, nombreDSHistoricos, archivoConstantes);
            configuraciones.add(c);

        }
        //preprod 1
        if (true) {
            String nombreConfiguracion = "Preprod1";
            String config_sunresources_Enrutacion =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"preprod1\" object-type=\"user\" pool-name=\"mysql_preprod1_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"20\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlDataSource\" fail-all-connections=\"true\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"true\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_preprod1_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.DataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "<property name=\"User\" value=\"preprod1\"/>"
                    + "<property name=\"Password\" value=\"5tgbnhy6\"/>"
                    + "<property name=\"serverName\" value=\""+DIRECCION_SERVIDOR+"\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"preprod1\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod1\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";
            String config_sunresources_Historicos =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"preprod1_historicos\" object-type=\"user\" pool-name=\"mysql_historico_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"0\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" connection-validation-method=\"auto-commit\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlXADataSource\" fail-all-connections=\"false\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"false\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_historico_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.XADataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "<property name=\"User\" value=\"preprod1_hist\"/>"
                    + "<property name=\"Password\" value=\"jgInhpsEnsAsepr\"/>"
                    + "<property name=\"serverName\" value=\""+DIRECCION_SERVIDOR+"\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"preprod1_historicos\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod1_historicos\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";

            String nombreDataSource = "preprod1";
            String nombreDSHistoricos = "preprod1_historicos";

            String archivoConstantes = "package co.uniandes.sisinfo.talend;\n"
                    + "/**  @author ArchivoConfig */\n"
                    + "public class ConstantesHistoricos {\n"
                    + "\tpublic final static String CONEXION_PROD=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod1?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_PROD=\"preprod1\";\n"
                    + "\tpublic final static String USER_PROD=\"preprod1\";\n"
                    + "\tpublic final static String PASS_PROD=\"5tgbnhy6\";\n"
                    + "\tpublic final static String CONEXION_HISTORICO=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod1_historicos?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_HISTORICO=\"preprod1_historicos\";\n"
                    + "\tpublic final static String USER_HISTORICO=\"preprod1_hist\";\n"
                    + "\tpublic final static String PASS_HISTORICO=\"jgInhpsEnsAsepr\";\n"
                    + "}";

            Configuracion c = new Configuracion(nombreConfiguracion, config_sunresources_Enrutacion, config_sunresources_Historicos, nombreDataSource, nombreDSHistoricos, archivoConstantes);
            configuraciones.add(c);

        }

        //preprod 2
        if (true) {
            String nombreConfiguracion = "Preprod2";
            String config_sunresources_Enrutacion =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"preprod2\" object-type=\"user\" pool-name=\"mysql_preprod2_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"20\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlDataSource\" fail-all-connections=\"true\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"true\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_preprod2_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.DataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "<property name=\"User\" value=\"preprod2\"/>"
                    + "<property name=\"Password\" value=\"5tgbnhy6\"/>"
                    + "<property name=\"serverName\" value=\""+DIRECCION_SERVIDOR+"\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"preprod2\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod2\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";
            String config_sunresources_Historicos =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"preprod2_historicos\" object-type=\"user\" pool-name=\"mysql_historico_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"0\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" connection-validation-method=\"auto-commit\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlXADataSource\" fail-all-connections=\"false\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"false\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_historico_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.XADataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "<property name=\"User\" value=\"preprod2_hist\"/>"
                    + "<property name=\"Password\" value=\"QLYE49LCvsQYPlH\"/>"
                    + "<property name=\"serverName\" value=\""+DIRECCION_SERVIDOR+"\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"preprod2_historicos\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod2_historicos\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";

            String nombreDataSource = "preprod2";
            String nombreDSHistoricos = "preprod2_historicos";

            String archivoConstantes = "package co.uniandes.sisinfo.talend;\n"
                    + "/**  @author ArchivoConfig */\n"
                    + "public class ConstantesHistoricos {\n"
                    + "\tpublic final static String CONEXION_PROD=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod2?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_PROD=\"preprod2\";\n"
                    + "\tpublic final static String USER_PROD=\"preprod2\";\n"
                    + "\tpublic final static String PASS_PROD=\"5tgbnhy6\";\n"
                    + "\tpublic final static String CONEXION_HISTORICO=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod2_historicos?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_HISTORICO=\"preprod2_historicos\";\n"
                    + "\tpublic final static String USER_HISTORICO=\"preprod2_hist\";\n"
                    + "\tpublic final static String PASS_HISTORICO=\"QLYE49LCvsQYPlH\";\n"
                    + "}";

            Configuracion c = new Configuracion(nombreConfiguracion, config_sunresources_Enrutacion, config_sunresources_Historicos, nombreDataSource, nombreDSHistoricos, archivoConstantes);
            configuraciones.add(c);

        }

        //preprod 3
        if (true) {
            String nombreConfiguracion = "Preprod3";
            String config_sunresources_Enrutacion =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"preprod3\" object-type=\"user\" pool-name=\"mysql_preprod3_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"20\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlDataSource\" fail-all-connections=\"true\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"true\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_preprod3_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.DataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "<property name=\"User\" value=\"preprod3\"/>"
                    + "<property name=\"Password\" value=\"5tgbnhy6\"/>"
                    + "<property name=\"serverName\" value=\""+DIRECCION_SERVIDOR+"\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"preprod3\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod3\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";
            String config_sunresources_Historicos =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<!DOCTYPE resources PUBLIC \"-//Sun Microsystems, Inc.//DTD Application Server 9.0 Resource Definitions //EN\" \"http://www.sun.com/software/appserver/dtds/sun-resources_1_3.dtd\">"
                    + "<resources>"
                    + "<jdbc-resource enabled=\"true\" jndi-name=\"preprod3_historicos\" object-type=\"user\" pool-name=\"mysql_historico_rootPool\"/>"
                    + "<jdbc-connection-pool allow-non-component-callers=\"false\" associate-with-thread=\"false\" connection-creation-retry-attempts=\"0\" connection-creation-retry-interval-in-seconds=\"10\" connection-leak-reclaim=\"false\" connection-leak-timeout-in-seconds=\"0\" connection-validation-method=\"auto-commit\" datasource-classname=\"com.mysql.jdbc.jdbc2.optional.MysqlXADataSource\" fail-all-connections=\"false\" idle-timeout-in-seconds=\"300\" is-connection-validation-required=\"false\" is-isolation-level-guaranteed=\"true\" lazy-connection-association=\"false\" lazy-connection-enlistment=\"false\" match-connections=\"false\" max-connection-usage-count=\"0\" max-pool-size=\"32\" max-wait-time-in-millis=\"60000\" name=\"mysql_historico_rootPool\" non-transactional-connections=\"false\" pool-resize-quantity=\"2\" res-type=\"javax.sql.XADataSource\" statement-timeout-in-seconds=\"-1\" steady-pool-size=\"8\" validate-atmost-once-period-in-seconds=\"0\" wrap-jdbc-objects=\"false\">"
                    + "<property name=\"User\" value=\"preprod3_hist\"/>"
                    + "<property name=\"Password\" value=\"ETD8e8PUfLaTd5U\"/>"
                    + "<property name=\"serverName\" value=\""+DIRECCION_SERVIDOR+"\"/>"
                    + "<property name=\"portNumber\" value=\"3306\"/>"
                    + "<property name=\"databaseName\" value=\"preprod3_historicos\"/>"
                    + "<property name=\"URL\" value=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod3_historicos\"/>"
                    + "<property name=\"driverClass\" value=\"com.mysql.jdbc.Driver\"/>"
                    + "</jdbc-connection-pool>"
                    + "</resources>";

            String nombreDataSource = "preprod3";
            String nombreDSHistoricos = "preprod3_historicos";

            String archivoConstantes = "package co.uniandes.sisinfo.talend;\n"
                    + "/**  @author ArchivoConfig */\n"
                    + "public class ConstantesHistoricos {\n"
                    + "\tpublic final static String CONEXION_PROD=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod3?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_PROD=\"preprod3\";\n"
                    + "\tpublic final static String USER_PROD=\"preprod3\";\n"
                    + "\tpublic final static String PASS_PROD=\"5tgbnhy6\";\n"
                    + "\tpublic final static String CONEXION_HISTORICO=\"jdbc:mysql://"+DIRECCION_SERVIDOR+":3306/preprod3_historicos?noDatetimeStringSync=true\";\n"
                    + "\tpublic final static String DB_HISTORICO=\"preprod3_historicos\";\n"
                    + "\tpublic final static String USER_HISTORICO=\"preprod3_hist\";\n"
                    + "\tpublic final static String PASS_HISTORICO=\"\";\n"
                    + "}";

            Configuracion c = new Configuracion(nombreConfiguracion, config_sunresources_Enrutacion, config_sunresources_Historicos, nombreDataSource, nombreDSHistoricos, archivoConstantes);
            configuraciones.add(c);

        }
    }

    public Configuracion darConfiguracion(String nombre) throws Exception {
        for (Configuracion configuracion : configuraciones) {
            if (configuracion.getNombreConfiguracion().equalsIgnoreCase(nombre)) {
                return configuracion;
            }
        }
        throw new Exception("No existe la configuración seleccionada");
    }

    public Collection<File> buscarPersistences() {
        Collection<File> archivosPersis = new ArrayList<File>();
        File f = new File("../../SisinfoBeans/");
        File[] beans = f.listFiles();
        for (File file : beans) {
            if (file.isDirectory()) {
                File[] carpetasBeans = file.listFiles();//nombre carpeta bean
                for (File file1 : carpetasBeans) {
                    if (file1.isDirectory() && file1.getName().equals("src")) {
                        try {
                            //  System.out.println("ARchivo= " + file1.getPath());
                            String bauu = file1.getCanonicalPath() + "\\" + "conf" + "\\" + "persistence.xml";
                            // System.out.println("Archivo encontrado=" + bauu);
                            File elArchivo = new File(bauu);
                            if (elArchivo.exists()) {
                                archivosPersis.add(elArchivo);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(MainConfiguracionDeploy.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            }
        }
        return archivosPersis;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            MainConfiguracionDeploy m = new MainConfiguracionDeploy();
            Collection<File> archivos = m.buscarPersistences();
            System.out.println("Tamanho lista=" + archivos.size());
            Object[] possibilities = m.cargarConfiguraciones();
            String s = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione la configuración:",
                    "Configuración de Entorno",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "Local-Desarrollo");

            if ((s != null) && (s.length() > 0)) {
                Configuracion c = m.darConfiguracion(s);
                for (File file : archivos) {
                    System.out.println(file.getAbsolutePath());
                    m.configurarArchivoPersistence(file, c);
                }
                System.out.println("Terminó de escribir archivos configuraciones");
//                File archivoconfigEnrutacion = new File("../../SisinfoBeans/ServiciosEnrutacion/setup/sun-resources.xml");
//                m.cambiarConfiguracionServidor(archivoconfigEnrutacion, c.getConfig_sunresources_Enrutacion());
                Collection<File> archivosConfigBDTrans = buscarArchivosSunresourcesNoHistoricos();
                for (File file : archivosConfigBDTrans) {
                    m.cambiarConfiguracionServidor(file, c.getConfig_sunresources_Enrutacion());
                }
                File archivoconfigHistoricos = new File("../../SisinfoBeans/Historico/setup/sun-resources.xml");
                m.cambiarConfiguracionServidor(archivoconfigHistoricos, c.getConfig_sunresources_Historicos());

                //Configurar Archivo de constantes de historicos
                File archivoConstantesHistoricos = new File("../../SisinfoBeans/Historico/src/java/co/uniandes/sisinfo/talend/ConstantesHistoricos.java");
                PrintWriter pw = new PrintWriter(archivoConstantesHistoricos);
                pw.println(c.getArchivoConstantesHistoricos());
                pw.close();

            }
            //C:\AAAAAAAASisinfo\trunk\repoTotal\SisinfoBeans\Historico\setup\sun-resources.xml



        } catch (Exception ex) {
            Logger.getLogger(MainConfiguracionDeploy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void configurarConstantesHistoricos(File file, Configuracion c) {
    }

    private void configurarArchivoPersistence(File file, Configuracion c) {
        try {
            //editar el pedaso que me importa...
            //leer cada linea si no es la que me importa lo dejo igual, si si es la que me importa la cambio
            //reescribir el archivo;

            BufferedReader lector = new BufferedReader(new FileReader(file));
            Collection<String> lineas = new ArrayList<String>();
            String linea = lector.readLine().trim();
            boolean esHistoricos = file.getAbsolutePath().contains("Historico") ? true : false;
            while (linea != null) {
                if (!linea.startsWith("<!--")) {
                    if (linea.contains("jta-data-source")) {
                        if (!esHistoricos) {
                            System.out.println("Encontró la línea= " + linea + "\n y la reemplazó por: " + "<jta-data-source>" + c.getNombreDataSourceBD() + "</jta-data-source>");
                            linea = "<jta-data-source>" + c.getNombreDataSourceBD() + "</jta-data-source>";

                        } else {
                            System.out.println("Encontró la línea= " + linea + "\n y la reemplazó por: " + "<jta-data-source>" + c.getNombreDSHistoricos() + "</jta-data-source>");
                            linea = "<jta-data-source>" + c.getNombreDSHistoricos() + "</jta-data-source>";
                        }
                    }

                    lineas.add(linea);
                }
                linea = lector.readLine();
            }
            lector.close();
            //ahora reescribir archivo
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            for (String string : lineas) {
                pw.println(string);
            }
            pw.close();
            System.out.println("Sobreescribió el archivo=" + file.getName());

        } catch (Exception ex) {
            Logger.getLogger(MainConfiguracionDeploy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Object[] cargarConfiguraciones() {
        Object[] configs = new Object[configuraciones.size()];
        int i = 0;
        for (Configuracion configuracion : configuraciones) {
            configs[i] = configuracion.getNombreConfiguracion();
            i++;
        }
        return configs;

    }
}
