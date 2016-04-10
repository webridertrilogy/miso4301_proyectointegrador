/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Votante;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author da-naran
 */
@Local
public interface VotanteFacadeLocal {

    void create(Votante votante);

    void edit(Votante votante);

    void remove(Votante votante);

    Votante find(Object id);

    List<Votante> findAll();

    List<Votante> findSinVotar();

    List<Votante> findByCorreo(String correo);

    Votante findByCorreoYIDVotacion(String correo, String idVot);

    List<Votante> findByIdVotacion(String idVotacion);

}
