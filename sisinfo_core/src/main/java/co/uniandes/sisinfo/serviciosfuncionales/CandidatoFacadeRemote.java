/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Candidato;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author da-naran
 */
@Remote
public interface CandidatoFacadeRemote {

    void create(Candidato candidato);

    void edit(Candidato candidato);

    void remove(Candidato candidato);

    Candidato find(Object id);

    List<Candidato> findAll();

    Candidato findByCorreoYIDVotacion(String correo, String idVot);

    public Candidato findVotoEnBlancoYIDVotacion(String idVot);

}
