package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.PeticionConflictoHorario;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface PeticionConflictoHorarioFacadeRemote {

    void create(PeticionConflictoHorario peticionConflictoHorario);

    void edit(PeticionConflictoHorario peticionConflictoHorario);

    void remove(PeticionConflictoHorario peticionConflictoHorario);

    void removeAll();

    PeticionConflictoHorario find(Object id);

    List<PeticionConflictoHorario> findAll();

    List<PeticionConflictoHorario> findRange(int[] range);

    int count();

    PeticionConflictoHorario buscarPorEstudianteYSeccionDestino(String correoEstudiante, Long idSeccionDestino);

    Collection<PeticionConflictoHorario> buscarPorCorreo(String correo);

    PeticionConflictoHorario buscarPorIdYCorreo(String correo, Long id);

    Collection<PeticionConflictoHorario> buscarPorCodigoCurso(String codigo);

    Collection<PeticionConflictoHorario> buscarPorCodigoCursoYTipo(String codigo, String tipo, int posicionInicial, int resultadosMaximos);

    Collection<PeticionConflictoHorario> buscarPeticionesResueltas();

    Collection<PeticionConflictoHorario> buscarPorSeccion(Long id);
}
