/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package soporte;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Asistente
 */
public class OperacionGenerarConstantes implements OperacionSoporte{

    public String darFormato() {
        return "TAG_PARAM_CONSTANTE_1\nVAL_CONSTANTE_2\nCMD_CONSTANTE_3";
    }

    public String darNombre() {
        return "Generar constantes";
    }

    public ArrayList<ArchivoInformacion> aplicarOperacion(String entrada) {
        Scanner sc = new Scanner(new ByteArrayInputStream(entrada.getBytes()));
        String strJava = "";
        String strTxt = "";
        while(sc.hasNextLine()){
            String ln = sc.nextLine().trim();
            if(ln.isEmpty())
                continue;
            strJava+=("public final static String "+ln+" = \""+ln+"\"\n");
            strTxt+=("String;"+ln+";"+toConstantValue(ln)+";\n");
        }
        sc.close();
        ArchivoInformacion arJava = new ArchivoInformacion("Constantes.java",strJava);
        ArchivoInformacion arTxt = new ArchivoInformacion("Constantes.txt",strTxt);
        ArrayList<ArchivoInformacion> al = new ArrayList<ArchivoInformacion>();
        al.add(arJava);
        al.add(arTxt);
        return al;

    }


    static String toConstantValue(String cn){
        String val="";
        boolean upperCase = false;
        for (int i = 0; i < cn.length(); i++) {
            char c = cn.charAt(i);
            if(c=='_'){
                upperCase=true;
            }else if(c >= 'A' && c <= 'Z'){
                if(upperCase){
                    val+=(c+"");
                    upperCase=!upperCase;
                }else
                    val+=((char)(c-'A'+'a')+"");
            }else{
                if(!upperCase){
                    val+=(c+"");
                    upperCase=!upperCase;
                }else
                    val+=((char)(c-'a'+'A')+"");
            }
        }
        return val;
    }

}
