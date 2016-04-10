package cargacorreosauditoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcela Morales
 */
public class DaoCorreoAuditoria {

    public List<CorreoAuditoria> consultarTodos() {
        System.out.println("CONSULTANDO TODOS");
        List<CorreoAuditoria> result = new ArrayList<CorreoAuditoria>();
        try {
            Connection con = MSSqlAdapter.getSingleton().getConnection();
            PreparedStatement st = con.prepareStatement(
                    "select c.id, c.asunto, c.destinatarios, c.destinatarioscc, c.destinatarioscco, c.fecha, c.mensaje, c.nombreAdjunto from CorreoAuditoria c;");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                CorreoAuditoria correoAuditoria = new CorreoAuditoria();
                correoAuditoria.setId(rs.getLong(1));
                correoAuditoria.setAsunto(rs.getString(2));
                correoAuditoria.setDestinatarios(rs.getString(3));
                correoAuditoria.setDestinatariosCC(rs.getString(4));
                correoAuditoria.setDestinatariosCCO(rs.getString(5));
                correoAuditoria.setFechaEnvio(rs.getTimestamp(6));
                correoAuditoria.setMensaje(rs.getString(7));
                correoAuditoria.setNombreAdjunto(rs.getString(8));
                result.add(correoAuditoria);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoCorreoAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MSSqlAdapterException ex) {
            ex.printStackTrace();
            Logger.getLogger(DaoCorreoAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }

        return eliminarCorreosRepetidos(result);
    }

    private static List<CorreoAuditoria> eliminarCorreosRepetidos(List<CorreoAuditoria> correos) {
        List<CorreoAuditoria> correosSinRepetidos = new ArrayList<CorreoAuditoria>();
        for (CorreoAuditoria correoAuditoria : correos) {
            if (correosSinRepetidos.isEmpty()) {
                correosSinRepetidos.add(correoAuditoria);
            } else {
                boolean encontro = false;
                for (CorreoAuditoria correoAuditoria1 : correosSinRepetidos) {
                    //Variables útiles para la validación
                    boolean asuntosIguales = correoAuditoria.getAsunto().equals(correoAuditoria1.getAsunto());
                    boolean destinatariosIguales = correoAuditoria.getDestinatarios().equals(correoAuditoria1.getDestinatarios());
                    //Validación
                    if (asuntosIguales && destinatariosIguales) {
                        encontro = true;
                    }
                }
                if (!encontro) {
                    correosSinRepetidos.add(correoAuditoria);
                }
            }
        }
        return correosSinRepetidos;
    }

    public static void main(String[] args) {
        //Consulta todos los correos
        DaoCorreoAuditoria dao = new DaoCorreoAuditoria();
        List<CorreoAuditoria> correosAuditoria = dao.consultarTodos();

        //Impresión para revisión de funcionalidad
        System.out.println(correosAuditoria);
        for (CorreoAuditoria correoAuditoria : correosAuditoria) {
            System.out.println("*" + correoAuditoria.getId() + " - "
                    + correoAuditoria.getAsunto() + " - "
                    + correoAuditoria.getDestinatarios() + " - "
                    + correoAuditoria.getDestinatariosCC() + " - "
                    + correoAuditoria.getDestinatariosCCO() + " - "
                    + correoAuditoria.getMensaje() + " - "
                    + correoAuditoria.getNombreAdjunto() + " - "
                    + correoAuditoria.getFechaEnvio() + " - ");
        }
    }
}
