/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package llenarconstantes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author John Casallas
 * modificado por german
 * modificado por Ivan
 */
public class MainConstantes {

    //LOCAL
    static String bdLocal = "sisinfo";
    static String loginLocal = "root";
    static String passwordLocal = "mysql";
    static String urlLocal = "jdbc:mysql://localhost:3306/";//+ bd;
    static String bdPreProd3 = "preprod3";
    static String loginProd3 = "preprod3";
    static String bdProd2 = "preprod2";
    static String loginProd2 = "preprod2";
    static String bdProd1 = "preprod1";
    static String loginProd1 = "preprod1";
    static String bdProd = "prod";
    static String loginProd = "prod";
    static String loginProdHist="prodhist";
    static String loginPreProd1Hist="preprod1_hist";
    static String loginPreProd2Hist="preprod2_hist";
    static String loginPreProd3Hist="preprod3_hist";
    //  Preprod
    static String passwordPreProd = "5tgbnhy6";
    static String urlServidor = "jdbc:mysql://157.253.238.241:3306/";// + bd;
    //   Prod
    static String passwordProd = "4htasef128";
    static String passwordProdHist = "xdf7w921QSd1";
    static String passwordPreProd1Hist = "jgInhpsEnsAsepr";
    static String passwordPreProd2Hist = "QLYE49LCvsQYPlH";
    static String passwordPreProd3Hist = "ETD8e8PUfLaTd5U";
    static Connection conn;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Object[] possibilities = new Object[5];
            possibilities[0] = "Local-Desarrollo";
            possibilities[1] = "Produccion";
            possibilities[2] = "Preprod1";
            possibilities[3] = "Preprod2";
            possibilities[4] = "Preprod3";
            String s = (String) JOptionPane.showInputDialog(
                    null,
                    "Por favor indique si es desarrollo o pruebas:",
                    "Configuracion de Entorno",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "Local-Desarrollo");
            //  Boolean prueba = "Local-Desarrollo".equals(s) ? true : false;

            String login = "";

            if (s.equals("Local-Desarrollo")){
                FileReader fr = new FileReader("./data/usuariosSisinfo.txt");
                BufferedReader br = new BufferedReader(fr);
                ArrayList usrs = new ArrayList();
                String ln = br.readLine();
                while(ln!=null){
                    usrs.add(ln);
                    ln = br.readLine();
                }
                Object[] possibilities2 = usrs.toArray();
                login = (String) JOptionPane.showInputDialog(
                    null,
                    "Por favor indique su login:",
                    "Configuracion de Entorno",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities2,
                    possibilities2[0]);
            }else if(s.equals("Preprod1")){
                login = "preprod1";
            }else if(s.equals("Preprod2")){
                login = "preprod2";
            }else if(s.equals("Preprod3")){
                login = "preprod3";
            }

            String url = "";
            String usuario = "";
            String clave = "";

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            if (s.equals("Local-Desarrollo")) {
                url = urlLocal + bdLocal;
                usuario = loginLocal;
                clave = passwordLocal;
                conn = DriverManager.getConnection(urlLocal + bdLocal, loginLocal, passwordLocal);
            } else if (s.equals("Produccion")) {
                url = urlServidor + bdProd;
                usuario = loginProd;
                clave = passwordProd;
                conn = DriverManager.getConnection(urlServidor + bdProd, loginProd, passwordProd);
            } else if (s.equals("Preprod1")) {
                url = urlServidor + bdProd1;
                usuario = loginProd1;
                clave = passwordPreProd;
                conn = DriverManager.getConnection(urlServidor + bdProd1, loginProd1, passwordPreProd);
            } else if (s.equals("Preprod2")) {
                url = urlServidor + bdProd2;
                usuario = loginProd2;
                clave = passwordPreProd;
                conn = DriverManager.getConnection(urlServidor + bdProd2, loginProd2, passwordPreProd);
            } else if (s.equals("Preprod3")) {
                url = urlServidor + bdPreProd3;
                usuario = loginProd3;
                clave = passwordPreProd;
                conn = DriverManager.getConnection(urlServidor + bdPreProd3, loginProd3, passwordPreProd);
            }

            if (conn != null) {
                System.out.println("Conexión a base de datos " + url + " ... Ok");
                System.out.println("Vaciando tabla");
                String queryVaciarTabla = "TRUNCATE TABLE `constante`";
                PreparedStatement stmt = conn.prepareStatement(queryVaciarTabla);
                stmt.executeUpdate();

                cargarConstantes(usuario, clave, s,login);
                conn.close();
            } else {
                throw new Exception("ERROR al cargar constantes");
            }
        } catch (SQLException ex) {
            System.out.println("Hubo un problema al intentar conectarse con la base de datos ");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private static void cargarConstantes(String usuario, String clave, String prueba,String login) {
        //String queryVaciarTabla = "CREATE TABLE `constante`";
        System.out.println("Cargando constantes");

        try {
            File dirConstantes = new File("../Constantes/data/Constantes");
            for (File txt : dirConstantes.listFiles()) {
                String nombre = txt.getName();
                if (nombre.startsWith("constante")) {
                    System.out.println("cargar archivo" + nombre);
                    cargarArchivo(txt, usuario, clave, prueba,login);
                }
            }
            //  cargarArchivo(new File("C:/Users/Asistente/Desktop/repoTotal/SisinfoBeans/Monitorias/data/constantes.txt"));
            Class o = MainConstantes.class;
            System.out.println("<<<<<NOMBRE CANONICO:LOL>>>>>" + o.getCanonicalName());
        } catch (Exception ex) {
            System.out.println("Error 1 agregando las constantes: " + ex.getMessage());
        }
    }

    private static void cargarArchivo(File archivo, String usuario, String clave, String prueba,String login) {
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {

                String tipo = linea.split(";")[0];
                String nombre = linea.split(";")[1];
                String valor = linea.split(";")[2];

                if (!prueba.equals("Produccion")) {
                    if (nombre.equals("CONFIGURACION_PARAM_PRUEBA")) {
                        valor = "TRUE";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("VAL_RUTABASE_DIRECTORIO_DOCPRIVADOS")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/docPrivados/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("VAL_RUTABASE_DIRECTORIO_ARCHIVOS")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/sisinfoUploads";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_REPORTES_HISTORICOS_CARGA_Y_COMPROMISOS")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/srcReportesJasper/cargayCompHistoricos/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_ARCHIVO_TEXTO_PROPUESTA_TESIS2")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesis/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_ARCHIVO_ARTICULO_TESIS1")) {

                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesis/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_ARCHIVO_CARTA_PENDIENTE_TESIS1")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesis/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_ARCHIVO_ARTICULO_TESIS2")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesis/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("CTE_TESIS2_RUTA_PENDIENTES_ESPECIALES")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesis/pendientesEspeciales/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_ARCHIVO_DOCUMENTACION_PENDIENTE_ESPECIAL_T2")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesis/pendientesEspeciales/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_ARCHIVO_EXTEMPORANEO_TESIS2")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesis/ArchivosExtemporales/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("TAG_PARAM_RUTA_POSTER")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesispregrado/rutaPoster/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("TAG_PARAM_RUTA_ABET")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesispregrado/rutaABET/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_AFICHES_TESIS_PREGRADO")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesispregrado/afiches/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_ARCHIVOS_TESIS_PREGRADO")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/tesispregrado/propuestas/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_ARCHIVO_ABET")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/abet/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("RUTA_AFICHES_TESIS_PREGRADO")) {
                        valor = "/u1/especiales/sisinfo/servapp/" + usuario + "/afiches/";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }
                    if (nombre.equals("CONFIGURACION_PARAM_CORREO_PRUEBA")){
                        valor = "pruebassisinfouniandes+"+login+"@gmail.com";
                        System.out.println("CAMBIO LA CONSTANTE:" + nombre + "->" + valor);
                    }

                } else {
                    if (nombre.equals("CONFIGURACION_PARAM_PRUEBA")) {
                        valor = "FALSE";
                    }
                }
                //aca depende de si es local o algun preprod
                if (nombre.equals("CONFIGURACION_PARAM_CONNECTSTRING")) {
                    String tmp = usuario;
                    if (usuario.endsWith("root")) {
                        tmp = "sisinfo";
                    }
                    valor = "jdbc:mysql://localhost:3306/" + tmp;
                }
                if (nombre.equals("CONFIGURACION_PARAM_USER")) {
                    valor = usuario;
                }
                if (nombre.equals("CONFIGURACION_PARAM_PASSWORD")) {
                    valor = clave;
                }
                if (nombre.equals("CONFIGURACION_PARAM_CONNECTSTRING_HISTORICO")) {
                    String tmp = usuario;
                    if(usuario.equals(loginLocal)){
                        tmp = "historico";
                    }else if(usuario.equals(loginProd1)){
                        tmp = "preprod1_historicos";
                    }else if(usuario.equals(loginProd2)){
                        tmp = "preprod2_historicos";
                    }else if(usuario.equals(loginProd3)){
                        tmp = "preprod3_historicos";
                    }else if(usuario.equals(loginProd)){
                        tmp = "prodhist";
                    }
                    valor = "jdbc:mysql://localhost:3306/" + tmp;
                }
                if (nombre.equals("CONFIGURACION_PARAM_USER_HISTORICO")) {
                    if(usuario.equals(loginLocal)){
                        valor = usuario;
                    }else if(usuario.equals(loginProd1)){
                        valor = loginPreProd1Hist;
                    }else if(usuario.equals(loginProd2)){
                        valor = loginPreProd2Hist;
                    }else if(usuario.equals(loginProd3)){
                        valor = loginPreProd3Hist;
                    }else if(usuario.equals(loginProd)){
                        valor = loginProdHist;
                    }
                }
                if (nombre.equals("CONFIGURACION_PARAM_PASSWORD_HISTORICO")) {
                    if(usuario.equals(loginLocal)){
                        valor = clave;
                    }else if(usuario.equals(loginProd1)){
                        valor = passwordPreProd1Hist;
                    }else if(usuario.equals(loginProd2)){
                        valor = passwordPreProd2Hist;
                    }else if(usuario.equals(loginProd3)){
                        valor = passwordPreProd3Hist;
                    }else if(usuario.equals(loginProd)){
                        valor = passwordProdHist;
                    }
                }
                //aca otras constantes que van a variar segun si es algo de prueba o verdad:

                agregar(tipo, nombre, valor);
            }
            System.out.println("Carga archivo: " + archivo.getName());
        } catch (Exception ex) {
            System.out.println("Error 1 agregando las constantes: " + ex.getMessage());
        }
    }
    static int id = 1;

    private static void agregar(String tipo, String nombre, String valor) {
        try {
            //conn
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO constante VALUES (?,?,?,?,?)");
            stmt.setInt(1, id++);
            stmt.setString(2, "");
            stmt.setString(3, nombre);
            stmt.setString(4, tipo);
            stmt.setString(5, valor);
            //
            stmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error: agregando:" + nombre + "--" + tipo + "--" + valor);
            System.out.println("Error 2 agregando las constantes: " + ex.getMessage() + " Constante: " + nombre);
        }
    }
}
