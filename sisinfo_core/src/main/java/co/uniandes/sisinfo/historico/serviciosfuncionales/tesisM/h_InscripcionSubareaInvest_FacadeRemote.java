package co.uniandes.sisinfo.historico.serviciosfuncionales.tesisM;

import co.uniandes.sisinfo.historico.entities.tesisM.h_inscripcion_subarea;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Fachada - Histórico de Inscripción Subarea
 * @author Paola Gómez
 */
@Remote
public interface h_InscripcionSubareaInvest_FacadeRemote {

    void create(h_inscripcion_subarea h_inscripcion_subarea);

    void edit(h_inscripcion_subarea h_inscripcion_subarea);

    void remove(h_inscripcion_subarea h_inscripcion_subarea);

    h_inscripcion_subarea find(Object id);

    List<h_inscripcion_subarea> findAll();

    List<h_inscripcion_subarea> findRange(int[] range);

    int count();

}
