/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package soporte;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Asistente
 */
public class OperacionGenerarErrores implements OperacionSoporte{

    public String darFormato() {
        return "NOMBRE_ERROR_1\nDescripción error 1\nNOMBRE_ERROR_2\nDescripción error 2";
    }

    public String darNombre() {
        return "Generar errores";
    }

    public ArrayList<ArchivoInformacion> aplicarOperacion(String entrada) {
        Scanner sc = new Scanner(new ByteArrayInputStream(entrada.getBytes()));

        String strJava = "";
        String strResolver = "";
        String strProperties = "";
        String strMensajes = "";

        while(sc.hasNextLine()){
            String name = sc.nextLine();
            String desc = sc.nextLine();
            int params = countParams(desc);
            String strParams = buildParams(params);
            String strParamsResolver = buildParamsResolver(params);
            strJava+=("@Key(\""+name+"\")\n");
            strJava+=("String "+name+" ("+strParams+");\n");
            strResolver+=("\t\t\t}else if(codigo.equals(\""+name+"\")){\n");
            strResolver+=("\t\t\t\treturn error."+name+"("+strParamsResolver+");\n");
            strProperties+=(name+"="+desc+"\n");
            strMensajes+=("//"+desc+"\n");
            strMensajes+=("public final static String "+name+" = \""+name+"\";\n");
        }
        sc.close();
        ArchivoInformacion arJava = new ArchivoInformacion("ERROR.java",strJava);
        ArchivoInformacion arResolver = new ArchivoInformacion("ErrorResolver.java",strResolver);
        ArchivoInformacion arProperties = new ArchivoInformacion("Error.properties",strProperties);
        ArchivoInformacion arMensajes = new ArchivoInformacion("Mensajes.java",strMensajes);
        ArrayList<ArchivoInformacion> al = new ArrayList<ArchivoInformacion>();
        al.add(arJava);
        al.add(arResolver);
        al.add(arProperties);
        al.add(arMensajes);
        return al;
    }

    private static String buildParams(int params){
        String str = "";
        for (int i = 1;i<= params;i++) {
            if(!str.isEmpty())
                str+=",";
            str+="String param"+i;
        }
        return str;
    }

        private static String buildParamsResolver(int params){
        String str = "";
        for (int i = 0;i< params;i++) {
            if(!str.isEmpty())
                str+=",";
            str+="parametros.get("+i+")";
        }
        return str;
    }

    private static int countParams(String str){
        int params = 0;
        int state = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch(state){
                case 0:
                    if(c=='{')
                        state=1;
                    break;
                case 1:
                    if(c>='0'&&c<='9')
                        state=2;
                    else
                        state=0;
                    break;
                case 2:
                    if(c=='}')
                        params++;
                    state=0;
            }
        }
        return params;
    }

}
