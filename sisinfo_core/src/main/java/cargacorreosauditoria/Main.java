package cargacorreosauditoria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcela Morales
 */
public class Main {

    public void crearConexion () {
        Connection con = null;
        try {
            con = MSSqlAdapter.getSingleton().getConnection();
        } catch (MSSqlAdapterException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
             try {
                if(con!=null)
                    con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) {
        Main dao = new Main();
        dao.crearConexion();
    }
}
