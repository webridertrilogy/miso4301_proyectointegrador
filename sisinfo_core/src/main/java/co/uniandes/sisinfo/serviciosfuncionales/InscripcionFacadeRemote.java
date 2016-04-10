/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import javax.ejb.Remote;
import  co.uniandes.sisinfo.entities.Inscripcion;
import java.util.List;

/**
 *
 * @author im.melo33
 */
@Remote
public interface InscripcionFacadeRemote
{
    void create(Inscripcion inscripcion);
    void edit(Inscripcion inscripcion);
    void remove(Inscripcion inscripcion);
    Inscripcion find(Object id);
    List<Inscripcion> findAll();
    List<Inscripcion>getInscripcionesPorCreador(String correoC);
}
