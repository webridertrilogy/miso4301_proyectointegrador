/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.SolicitudMaterialBibliografico;
import java.sql.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Marcela
 */
@Local
public interface SolicitudMaterialBibliograficoFacadeLocal {

    void create(SolicitudMaterialBibliografico solicitudMaterialBibliografico);

    void edit(SolicitudMaterialBibliografico solicitudMaterialBibliografico);

    void remove(SolicitudMaterialBibliografico solicitudMaterialBibliografico);

    SolicitudMaterialBibliografico find(Object id);

    List<SolicitudMaterialBibliografico> findAll();

    SolicitudMaterialBibliografico findById(Long id);

    double findPrecioPromedioByRangoFecha(Date fechaInicio, Date fechaFin);

    double findPrecioTotalByRangoFecha(Date fechaInicio, Date fechaFin);

    List<SolicitudMaterialBibliografico> findyBySolicitante(String correoSolicitante);

    List<SolicitudMaterialBibliografico> findyByEstado(String estado);

    List<SolicitudMaterialBibliografico> findByRangoFecha(Date fechaInicio, Date fechaFin);

    List<SolicitudMaterialBibliografico> findByAnioPublicacion(int anio);
    
    List<SolicitudMaterialBibliografico> findyByProveedor(String proveedor);
    
    List<SolicitudMaterialBibliografico> findByRangoPrecio(double precioInicio, double precioFin);
}
