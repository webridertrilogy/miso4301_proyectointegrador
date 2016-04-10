package co.edu.uniandes.sisinfo.ant;

import java.io.File;

/**
 * Sisinfo Runner
 * Hace clean and build de todos los proyectos Sisinfo
 * @author Marcela Morales
 */
public class SisinfoRunner {

    //----------------------------------------------------------------------
    // CONSTANTES
    //----------------------------------------------------------------------

    //Nombres de los proyectos
    public final static String[] PROYECTOS = {"DatosMaestros", "ServicioConstantes", "ServiciosEnrutacion", "ServiciosInfraestructura", "ServicioSoporteProcesos",
            "SisinfoAdministradorSisinfo", "Historico", "BolsaEmpleo", "CargaYCompromisoProf", "ConflictoHorarios", "ContactosCrm", "DocumentosPrivados",
            "InscripcionesGen", "MaterialBibliografico", "Monitorias", "PlaneacionAcademica", "ProyectoDeGradoPregrado", "Publicaciones", "ReservaCitas",
            "ReservasInventario", "SoporteSisinfo", "TesisMaestria", "Votaciones"};
    
    //Ubicación archivo build.xml y directorio base del proyecto
    public final static String BUILD_ROOT = "C:/Users/Asistente/Desktop/repoTotal/SisinfoBeans/%/build.xml";
    public final static String BUILD_BASEDIR = "C:/Users/Asistente/Desktop/repoTotal/SisinfoBeans/%/";
    
    //Targets: clean/default
    public final static String CLEAN_TARGET = "%-impl.clean";
    public final static String DEFAULT_TARGET = "%-impl.default";
    
    //----------------------------------------------------------------------
    // MAIN
    //----------------------------------------------------------------------
    public static void main(String[] args) {
        clean();
        build();
    }

    /**
     * Método para hacer clean de todos los proyectos
     */
    public static void clean(){
        for (String proyecto : PROYECTOS) {

            //Configuración de nombres
            String buildRoot = BUILD_ROOT.replace("%", proyecto);
            String buildBaseDir = BUILD_BASEDIR.replace("%", proyecto);
            String cleanTarget = CLEAN_TARGET.replace("%", proyecto);
            
            //Creación de archivos
            File buildFile = new File(buildRoot);
            File baseDirFile = new File(buildBaseDir);

            //Ejecución de clean and build
            AntRunner ant = new AntRunner(buildFile, baseDirFile, new SimpleBuildListener());
            ant.executeTarget(cleanTarget);
        }
    }

    /**
     * Método para hacer build de todos los proyectos
     */
    public static void build(){
        for (String proyecto : PROYECTOS) {

            //Configuración de nombres
            String buildRoot = BUILD_ROOT.replace("%", proyecto);
            String buildBaseDir = BUILD_BASEDIR.replace("%", proyecto);
            String defaultTarget = DEFAULT_TARGET.replace("%", proyecto);

            //Creación de archivos
            File buildFile = new File(buildRoot);
            File baseDirFile = new File(buildBaseDir);

            //Ejecución de clean and build
            AntRunner ant = new AntRunner(buildFile, baseDirFile, new SimpleBuildListener());
            ant.executeTarget(defaultTarget);
        }
    }
}
