/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configuraciondeploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author Manuel
 */
public class MainCopiarJars {

    
    private static void copiarArchivo(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage() + " en el directorio especificado.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int numModulosCopiados=0;
        //Explorar Carpetas de módulos
        File f = new File("../../SisinfoBeans/");
        File carpDist = new File("../../SisinfoBeans/dist/");
        if(!carpDist.exists()){
            carpDist.mkdirs();
        }

        File[] carpsModulos = f.listFiles();
        boolean encontroDist=false;
        for (int i = 0; i < carpsModulos.length; i++) {
            File carpModulo = carpsModulos[i];
            //Se omiten módulos deprecados o que no corresponden
            if(!carpModulo.getName().equalsIgnoreCase("NucleoNegocio") && !carpModulo.getName().contains("svn")){
                File[] carpsModulo=carpModulo.listFiles();
                for (int j = 0; j < carpsModulo.length; j++) {
                    encontroDist=false;
                    File carpsDist = carpsModulo[j];
                    //Buscar carpetas de JARS
                    if(carpsDist.getName().equals("dist")){
                        File[] file =carpsDist.listFiles();
                        for (int k = 0; k < file.length && !encontroDist; k++) {
                            File file1 = file[k];
                            if(file1.getName().endsWith(".jar") || file1.getName().endsWith(".JAR")){
                                //Dejar de Iterar en carpetas de módulo
                                System.out.println("COPIANDO: "+file1.getName());
                                copiarArchivo(file1.getAbsolutePath(), carpDist.getAbsolutePath()+"\\"+file1.getName());
                                System.out.println("COPIADO A: "+carpDist.getAbsolutePath()+"\\"+file1.getName());
                                encontroDist=true;
                                ++numModulosCopiados;
                            }
                        }
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Número de Módulos copiados: "+numModulosCopiados);
    }
}
