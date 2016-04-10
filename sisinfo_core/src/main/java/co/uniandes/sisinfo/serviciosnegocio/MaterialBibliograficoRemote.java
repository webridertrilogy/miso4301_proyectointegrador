/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Marcela
 */
@Remote
public interface MaterialBibliograficoRemote {

    /**
     * Crea una nueva solicitud de compra de materia bibliografico. Adicionalmente
     * crea la tarea para que el director apruebe la solicitud y le envia un correo
     * al director informando sobre la solicitud
     * @param xml Comando xml
     * @return respuesta xml
     */
    public String crearSolicitudCompraMaterialBibliografico(String xml);

    public String editarSolicitudCompraMaterialBibliografico(String xml);
    
    public String eliminarSolicitudMaterialBibliografico(String xml);

    public String enviarAutorizacionCompraMaterialBibliografico(String xml);

    public String consultarSolicitudPorIdSolicitud(String xml);

    public String consultarSolicitudesSolicitante(String xml);

    public String consultarSolicitudesDepartamentoPorEstado(String xml);

    public String enviarConfirmacionCompraMaterialBibliograficoBiblioteca(String xml);

    public void enviarNotificacionAdquisicionesUltimoMes();

    String enviarConfirmacionLLegoMaterialABiblioteca(String String);

    void renovarTareasSolicitudMaterialBibliografico();
}
