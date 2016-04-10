package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ConfiguracionFechasCH;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ConfiguracionFechasCHFacadeLocal {

    void create(ConfiguracionFechasCH configuracionFechasCH);

    void edit(ConfiguracionFechasCH configuracionFechasCH);

    void remove(ConfiguracionFechasCH configuracionFechasCH);

    void removeAll();

    ConfiguracionFechasCH find(Object id);

    List<ConfiguracionFechasCH> findAll();

    List<ConfiguracionFechasCH> findRange(int[] range);

    int count();
}
