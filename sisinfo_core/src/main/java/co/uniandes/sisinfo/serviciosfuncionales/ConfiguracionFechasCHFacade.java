package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacadeCH;
import co.uniandes.sisinfo.entities.ConfiguracionFechasCH;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author German Florez, Marcela Morales
 * Servicios fachada de la entidad ConfiguracionFechasCH
 */
@Stateless
public class ConfiguracionFechasCHFacade extends AbstractFacadeCH<ConfiguracionFechasCH> implements ConfiguracionFechasCHFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionFechasCHFacade() {
        super(ConfiguracionFechasCH.class);
    }
}
