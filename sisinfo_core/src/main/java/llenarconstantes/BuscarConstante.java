/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package llenarconstantes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author German Florez
 * Uniandes
 */
public class BuscarConstante extends JFrame {

    ArrayList<resultado> resultados = new ArrayList<resultado>();

    ArrayList<resultado> faltantes = new ArrayList<resultado>();

    public ArrayList<resultado> getFaltantes() {
        return faltantes;
    }

    public void setFaltantes(ArrayList<resultado> faltantes) {
        this.faltantes = faltantes;
    }
    
    public ArrayList<resultado> getResultados() {
        return resultados;
    }

    public void setResultados(ArrayList<resultado> resultados) {
        this.resultados = resultados;
    }
    static String ruta = "../Constantes/data/Constantes";

    public BuscarConstante() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        

    }

    void buscarConstante(String c) {
        resultados=new ArrayList<resultado>();
        File dir = new File(ruta);
        for (File f : dir.listFiles()) {
            buscarEnArchivo(f, c);
        }
    }

    void buscarEnArchivo(File f, String constante) {
        try {
            BufferedReader in= new BufferedReader(new FileReader(f));
            String ln;
            int i=0;
            while((ln=in.readLine())!=null){
                String ns[]=ln.split(";");
                if(ns[0].equals(constante)||ns[1].equals(constante)||ns[2].equals(constante)){
                    resultados.add(new resultado(ln, f.getName(), i));
                } else {

                    faltantes.add(new resultado(ln, f.getName(), i));
                }
                i++;
            }
        } catch (Exception e) {
        }
    }

    static class resultado {

        String archivo;
        int linea;
        String constante;

        public resultado(String c,String archivo, int linea) {
            this.archivo = archivo;
            this.linea = linea;
            constante=c;
        }

        public String toString() {
            return constante+" "+archivo+" linea: "+linea;
        }

    }

    public static void main(String ar[]){
        String texto = JOptionPane.showInputDialog(null,"Seleccione la constante que desea buscar");
        BuscarConstante b= new BuscarConstante();
        b.buscarConstante("VAL_CORREO_DIRECTOR_MB");
        for(resultado r:b.resultados){
            System.out.println(r);
        }
    }

}
