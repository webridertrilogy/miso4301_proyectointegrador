/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configuraciondeploy;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
public class Configuracion {

    private String archivoConstantesHistoricos;
    private String nombreConfiguracion;
    private String config_sunresources_Enrutacion;
    private String config_sunresources_Historicos;
    private String nombreDataSourceBD;
    private String nombreDSHistoricos;

    public Configuracion() {
    }

    public String getConfig_sunresources_Enrutacion() {
        return config_sunresources_Enrutacion;
    }

    public void setConfig_sunresources_Enrutacion(String config_sunresources_Enrutacion) {
        this.config_sunresources_Enrutacion = config_sunresources_Enrutacion;
    }

    public String getConfig_sunresources_Historicos() {
        return config_sunresources_Historicos;
    }

    public void setConfig_sunresources_Historicos(String config_sunresources_Historicos) {
        this.config_sunresources_Historicos = config_sunresources_Historicos;
    }

    public String getNombreConfiguracion() {
        return nombreConfiguracion;
    }

    public void setNombreConfiguracion(String nombreConfiguracion) {
        this.nombreConfiguracion = nombreConfiguracion;
    }

    public String getNombreDSHistoricos() {
        return nombreDSHistoricos;
    }

    public void setNombreDSHistoricos(String nombreDSHistoricos) {
        this.nombreDSHistoricos = nombreDSHistoricos;
    }

    public String getNombreDataSourceBD() {
        return nombreDataSourceBD;
    }

    public void setNombreDataSourceBD(String nombreDataSourceBD) {
        this.nombreDataSourceBD = nombreDataSourceBD;
    }

    public String getArchivoConstantesHistoricos() {
        return archivoConstantesHistoricos;
    }

    public void setArchivoConstantesHistoricos(String archivoConstantesHistoricos) {
        this.archivoConstantesHistoricos = archivoConstantesHistoricos;
    }

    public Configuracion(String nombreConfiguracion, String config_sunresources_Enrutacion, String config_sunresources_Historicos, String nombreDataSourceBD, String nombreDSHistoricos, String archivoConstantesHistoricos) {
        this.nombreConfiguracion = nombreConfiguracion;
        this.config_sunresources_Enrutacion = config_sunresources_Enrutacion;
        this.config_sunresources_Historicos = config_sunresources_Historicos;
        this.nombreDataSourceBD = nombreDataSourceBD;
        this.nombreDSHistoricos = nombreDSHistoricos;
        this.archivoConstantesHistoricos = archivoConstantesHistoricos;
    }
}
