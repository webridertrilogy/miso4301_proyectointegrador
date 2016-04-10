/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package llenarListaBlanca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Juan Manuel Moreno B.
 */
public class MainListaBlanca {

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
    //  Preprod
    static String passwordPreProd = "5tgbnhy6";
    static String urlServidor = "jdbc:mysql://sisinfo.uniandes.edu.co:3306/";// + bd;
    //   Prod
    static String passwordProd = "4htasef128";
    static Connection conn;

    // Directorio archivos texto
    private static final String PATH_TXT = "data";

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

            String url = "";
            String usuario="";
            String clave="";

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            if (s.equals("Local-Desarrollo")) {
                url = urlLocal + bdLocal;
                usuario=loginLocal;
                clave=passwordLocal;
                conn = DriverManager.getConnection(urlLocal + bdLocal, loginLocal, passwordLocal);
            } else if (s.equals("Produccion")) {
                url = urlServidor + bdProd;
                usuario= loginProd;
                clave=passwordProd;
                conn = DriverManager.getConnection(urlServidor + bdProd, loginProd, passwordProd);
            } else if (s.equals("Preprod1")) {
                url = urlServidor + bdProd1;
                usuario=loginProd1;
                clave=passwordPreProd;
                conn = DriverManager.getConnection(urlServidor + bdProd1, loginProd1, passwordPreProd);
            } else if (s.equals("Preprod2")) {
                url = urlServidor + bdProd2;
                usuario=loginProd2;
                clave=passwordPreProd;
                conn = DriverManager.getConnection(urlServidor + bdProd2, loginProd2, passwordPreProd);
            } else if (s.equals("Preprod3")) {
                url = urlServidor + bdPreProd3;
                 usuario=loginProd3;
                clave=passwordPreProd;
                conn = DriverManager.getConnection(urlServidor + bdPreProd3, loginProd3, passwordPreProd);
            }

            if (conn != null) {
                
                System.out.println("Conexión a base de datos " + url + " ... Ok");
                System.out.println("Vaciando tabla");
                String queryVaciarTabla = "TRUNCATE TABLE `listaBlancaErroresSisinfo`";
                PreparedStatement stmt = conn.prepareStatement(queryVaciarTabla);
                stmt.executeUpdate();

                cargarErroresListaBlanca(usuario,clave,s);
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

    private static void cargarErroresListaBlanca(String usuario, String clave,String prueba) {

        System.out.println("Cargando constantes");

        try {
            
            File dirConstantes = new File(PATH_TXT);
            System.out.println(dirConstantes.getAbsolutePath());
            for (File txt : dirConstantes.listFiles()) {

                String nombre = txt.getName();
                System.out.println(nombre);
                if (nombre.startsWith("listaBlanca")) {

                    cargarArchivo(txt, usuario,clave,prueba);
                }
            }
            //  cargarArchivo(new File("C:/Users/Asistente/Desktop/repoTotal/SisinfoBeans/Monitorias/data/constantes.txt"));
            Class o = MainListaBlanca.class;
            System.out.println("<<<<<NOMBRE CANONICO:LOL>>>>>" + o.getCanonicalName());
        } catch (Exception ex) {
            System.out.println("Error 1 agregando los errores de lista blanca: " + ex.getMessage());
        }
    }

    private static void cargarArchivo(File archivo, String usuario, String clave, String prueba) {
        try {
            
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                
                String explicacion = linea.split(";")[0];
                String idError = linea.split(";")[1];
                agregar(explicacion, idError);
            }
            System.out.println("Carga archivo: " + archivo.getName());
        } catch (Exception ex) {
            System.out.println("Error 1 agregando los errores de lista blanca: " + ex.getMessage());
        }
    }
    static int id = 1;

    private static void agregar(String explicacion, String idError) {
        try {
            //conn
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO listaBlancaErroresSisinfo VALUES (?,?,?)");
            stmt.setInt(1, id++);
            stmt.setString(2, explicacion);
            stmt.setString(3, idError);
            stmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error 2 agregando los errores de lista blanca: " + ex.getMessage() + " Error: " + id + ", " + explicacion);
        }
    }
}
