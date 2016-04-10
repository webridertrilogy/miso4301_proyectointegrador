/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.bo.excepciones;

/**
 *
 * @author cf.morales46
 */
public class PeriodoInvalidoException extends Exception {

    /**
     * Creates a new instance of <code>PeriodoInvalidoException</code> without detail message.
     */
    public PeriodoInvalidoException() {
    }


    /**
     * Constructs an instance of <code>PeriodoInvalidoException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PeriodoInvalidoException(String msg) {
        super(msg);
    }
}
