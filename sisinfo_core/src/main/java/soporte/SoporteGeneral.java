/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package soporte;

/**
 *
 * @author Asistente
 */
public class SoporteGeneral {

    public OperacionSoporte darOperacion(String nombre){
        if(nombre.equals("Generar Constantes")){
            return new OperacionGenerarConstantes();
        }else if(nombre.equals("Generar Errores")){
            return new OperacionGenerarErrores();
        }
        return null;
    }


}
