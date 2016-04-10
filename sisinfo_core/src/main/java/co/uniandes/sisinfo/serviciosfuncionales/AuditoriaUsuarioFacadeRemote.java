/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.AuditoriaUsuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Paola Gómez
 */
@Remote
public interface AuditoriaUsuarioFacadeRemote {
    void create(AuditoriaUsuario audit);

    void edit(AuditoriaUsuario audit);

    void remove(AuditoriaUsuario audit);

    AuditoriaUsuario find(Object id);

    List<AuditoriaUsuario> findAll();

    List<AuditoriaUsuario> findByUsuario(String correo);

    List<AuditoriaUsuario> findByFecha(Date fechaInicio, Date fechaFin);

    List<AuditoriaUsuario> findByRol(String rol);

    List<AuditoriaUsuario> findByAny(String usuario, String rol, Date fechaInicio, Date fechaFin, String comandos);
}
