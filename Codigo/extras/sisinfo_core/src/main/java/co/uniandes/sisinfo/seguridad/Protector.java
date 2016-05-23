/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.seguridad;

import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;

/**
 * @author Cindy
 */
public class Protector {

    private static final byte[] keyValue = new byte[]{'|', '(', ')', '|', 'I', '{', '}', '*', 'e', 'c', '=', '+', '_', '$', 'e', '%'};

    public static String decryptUser(String valorADesencriptar) throws Exception {
        TripleDesCipher cipher = new TripleDesCipher();
        cipher.setKey(keyValue);
        String dec = "";
        String value = valorADesencriptar.split("@")[0];
        try {
            dec = cipher.decrypt(value);
        } catch (DataLengthException e) {
            System.out.print("murio server");
        }
        return dec;
    }

    public static String decryptPass(String valorADesencriptar) throws Exception {
        TripleDesCipher cipher = new TripleDesCipher();
        cipher.setKey(keyValue);
        String dec = "";
        try {
            dec = cipher.decrypt(valorADesencriptar);
        } catch (DataLengthException e) {
            System.out.print("murio server");
        }
        return dec;
    }
}
