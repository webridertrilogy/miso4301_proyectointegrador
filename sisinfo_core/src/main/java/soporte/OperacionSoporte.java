/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package soporte;

import java.util.ArrayList;

/**
 *
 * @author Asistente
 */
public interface OperacionSoporte {

    public String darFormato();

    public String darNombre();

    public ArrayList<ArchivoInformacion> aplicarOperacion(String entrada);

}
