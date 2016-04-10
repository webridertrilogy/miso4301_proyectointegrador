/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface ReporteExcepcionesBeanLocal {

    void crearLogMensaje(String nombreBean,String nombreMetodo, String comandoRespuesta, Timestamp fecha,String comando, String xmlEntrada);

    String consultarExcepcionesPorEstado(String xml);

    String consultarExcepcionPorId(String xml);

    String solucionarExcepcion(String xml);

    String eliminarExcepcionSisinfo(String xml);

    String consultarExcepcionesSisinfo(String xml);

    
}
