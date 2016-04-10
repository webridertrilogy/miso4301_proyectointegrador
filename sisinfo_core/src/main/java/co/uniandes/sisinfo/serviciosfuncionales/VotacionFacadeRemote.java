/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Votacion;
import co.uniandes.sisinfo.entities.Votante;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author da-naran
 */
@Remote
public interface VotacionFacadeRemote {

    void create(Votacion votacion);

    void edit(Votacion votacion);

    void remove(Votacion votacion);

    Votacion find(Object id);

    List<Votacion> findAll();

    List<Votacion> findVotacionesActivas();

    List<Votacion> findVotacionesPorCorreo(String correo);

    public Votante findByCorreoYIDVotacion(String correo, String idVot);

    List<Votante> findByIdVotacion(String idVotacion);

}
