/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package llenarconstantes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Juan Manuel Moreno B.
 */
public class InventarioConstantes {

    /**
     * Path ambiente de trabajo.
     */
    public final String FOLDER_SISINFO = "Z:/Trabajo/Versiones Sisinfo/Prueba Tareas Pendientes/SisinfoCommon/";

    /**
     * Path Constantes.
     */
    public final String FOLDER_CONSTANTES = FOLDER_SISINFO + "Constantes/";

    /**
     * Path Constantes.
     */
    public final String FOLDER_CONSTANTES_DATA = FOLDER_CONSTANTES + "data/Constantes/";

    /**
     * Lista de contantes Java
     */
    private List<String> listConstantJava;

    /**
     * Lista de contantes texto
     */
    private List<String> listConstantTxt;

    /*
     * Verifica constantes entre archivos.
     */
    public void verConstantes(File source, File destiny) {

        System.out.println("RESULTADO:\nConstantes que están en " + source.getName() + " pero no en " + destiny.getName());
        try {

            BufferedReader in = new BufferedReader(new FileReader(source));
            String line = in.readLine();
            BuscarConstante busca = new BuscarConstante();
            String[] block = line.split(";");
            System.out.println("en busca de " + block[1]);
            busca.buscarEnArchivo(destiny, block[1]);
            for (BuscarConstante.resultado rest : busca.getFaltantes()) {

                System.out.println(rest.toString());
            }
            for (BuscarConstante.resultado rest : busca.getResultados()) {

                System.out.println(rest.toString());
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Extrae las constantes de una clase Java
     */
    public void fromJava2Constant(File java) {

        try {

            BufferedReader in= new BufferedReader(new FileReader(java));
            String line;
            String[] blocks = null;
            StringBuilder filtered = null;
            String[] names = null;
            while((line = in.readLine())!=null){

                blocks = line.split("public static final String ");
                if ( !(blocks.length > 1) ) {

                    blocks = line.trim().split("public final static String ");
                }
                if (blocks.length > 1) {

                    filtered = new StringBuilder();
                    for (int i = 0; i < blocks.length; i++) {

                        filtered.append(blocks[i]);
                    }
                    names = filtered.toString().trim().split("=");
                    if (names.length > 1) {

                        if (this.listConstantJava == null) {

                            this.listConstantJava = new ArrayList<String>();
                        }
                        this.listConstantJava.add(names[0].trim());
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Extrae las constantes de un archivo de constantes
     */
    public void fromTxt2Constant(File txt) {

        try {

            BufferedReader in= new BufferedReader(new FileReader(txt));
            String line;
            String[] blocks = null;
            StringBuilder filtered = null;
            String[] names = null;
            while((line = in.readLine())!=null){

                blocks = line.split(";");
                if (blocks.length > 1) {

                    if (this.listConstantTxt == null) {

                        this.listConstantTxt = new ArrayList<String>();
                    }
                    this.listConstantTxt.add(blocks[1].trim());
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Da la lista de contenidos que estan en una lista y no en la otra
     * @param args
     */
    private List<String> getListOwnNoOther(List<String> own, List<String> other) {
        List<String> listShort = null;
        
        boolean found = false;
        for (String anown : own) {

            found = false;
            for (String another : other) {

                if (anown.equals(another)) {

                    found = true;
                    break;
                }
            }
            if (!found) {

                if (listShort == null) {

                    listShort = new ArrayList<String>();
                }
                listShort.add(anown);
            }
        }
        return listShort;
    }

    /**
     * Muestra una lista.
     */
    private void showList(List<String> aList) {

        if (aList != null && !aList.isEmpty()) {

            for (String content : aList) {

                System.out.println(content);
            }
        }
    }
    
    /**
     * Compara constantes entre dos archivos.
     */
    public void compare(File java, File txt) {

        if (this.listConstantJava == null) {

            this.fromJava2Constant(java);
        }
        if (this.listConstantTxt == null) {

            this.fromTxt2Constant(txt);
        }
        System.out.println("\nCONSTANTES SOLO EN  " + txt.getName() + ":");
        this.showList(this.getListOwnNoOther(listConstantTxt, listConstantJava));

        // Filtramos constantes Java.
        this.listConstantJava = this.getListOwnNoOther(listConstantJava, listConstantTxt);
    }
    
    /**
     * Realiza todoe l proceso de comparar
     */
    public void doAll() {

        String[] archivos = {
            "constantes.txt",
            "constantesAdmonSisinfo.txt",
            "constantesCargaYCompromisos.txt",
            "constantesConfiguracion.txt",
            "constantesConflictoHorarios.txt",
            "constantesDeprecated.txt",
            "constantesInventarioYReservas.txt",
            "constantesProyectoDeGrado.txt",
            "constantesReservas.txt",
            "constantesRoles.txt",
            "constantesSoporteSisinfo.txt",
            "constantesTesis.txt"};
        for (int i = 0; i < archivos.length; i++) {

            this.listConstantTxt = null;
            this.compare(
               new File(FOLDER_CONSTANTES + "src/co/uniandes/sisinfo/comun/constantes/Constantes.java"),
               new File(FOLDER_CONSTANTES_DATA + archivos[i]));
        }
        System.out.println("\nCONSTANTES SOLO EN  Constantes.java:");
        this.showList(this.listConstantJava);

    }

    /**
     * Main.
     */
    public static void main(String args[]){

       InventarioConstantes inventario = new InventarioConstantes();
       inventario.doAll();
    }
}
