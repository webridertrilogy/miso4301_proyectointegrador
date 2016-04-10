/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.bo.AccionBO;
import java.util.Collection;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Melo
 */
@Remote
public interface TesisBeanRemote {

    String darSemestresTesis(String xml);

    void manejoTimmersTesisMaestria(String comando);

    String establecerFechasPeriodo(String xml);

    String darConfiguracionPeriodoTesisPorNombre(String xml);

    String agregarTemaTesis(String xml);

    String darTemasDeTesis(String xml);

    String darTemasDeTesisPorAsesor(String xml);

    String eliminarTemaTesisAsesor(String xml);

    String darTemadeTesisPorId(String xml);

    Collection<AccionBO> darAcciones(String rol,String login);


    public void crearTareasTesis();
}
