/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author Marcela
 */
@Local
public interface MaterialBibliograficoLocal {

    public String crearSolicitudCompraMaterialBibliografico(String xml);

    public String editarSolicitudCompraMaterialBibliografico(String xml);

    public String eliminarSolicitudMaterialBibliografico(String xml);

    public String enviarAutorizacionCompraMaterialBibliografico(String xml);

    public String consultarSolicitudesSolicitante(String xml);

    public String consultarSolicitudesDepartamentoPorEstado(String xml);

    public String enviarConfirmacionCompraMaterialBibliograficoBiblioteca(String xml);

    public void enviarNotificacionAdquisicionesUltimoMes();

    String enviarConfirmacionLLegoMaterialABiblioteca(String String);

    void renovarTareasSolicitudMaterialBibliografico();
}
