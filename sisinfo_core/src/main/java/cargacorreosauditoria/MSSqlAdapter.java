package cargacorreosauditoria;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adaptador para conexión a la base de datos
 * @author Marcela Morales
 */
public class MSSqlAdapter {

    private static MSSqlAdapter singleton;
    public static final String DB = "sisinfo";

    private MSSqlAdapter() {

    }

    public static MSSqlAdapter getSingleton() {
        if (singleton == null)
            singleton = new MSSqlAdapter();
        return singleton;
    }

    public Connection getConnection () throws MSSqlAdapterException {
        System.out.println("Conectado a la bd: "+DB);
        Connection conexion = null;
        String url = "jdbc:mysql://localhost:3306/"+ DB;
        String usuario = "root";
        String password = "mysql";

        //Carga del driver
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Creando la conexion a la BD
            conexion  = DriverManager.getConnection(url,usuario,password); //Conexion con validacion:
        } catch (InstantiationException ex) {
            System.err.println(ex.getMessage());
            Logger.getLogger(MSSqlAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            System.err.println(ex.getMessage());
            Logger.getLogger(MSSqlAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.lang.ClassNotFoundException ex) {
            System.err.print("Problemas al cargar el driver ");
            System.err.println(ex.getMessage());
            throw new MSSqlAdapterException("No fue posible establecer la conexión",ex);
        } catch (SQLException exc) {
            System.err.println(exc.getMessage());
            throw new MSSqlAdapterException("No fue posible establecer la conexión",exc);
        }
        return conexion;
    }
}