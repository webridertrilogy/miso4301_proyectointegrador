/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Monitoria_Solicitada;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Entidad Monitoría Solicitada
 */
@Remote
public interface Monitoria_SolicitadaFacadeRemote {

    /**
     * Crea la monitoría solicitada que llega como parámetro
     * @param monitoria_Solicitada Monitoría solicitada a crear
     */
    void create(Monitoria_Solicitada monitoria_Solicitada);

    /**
     * Edita la monitoría solicitada que llega como parámetro
     * @param monitoria_Solicitada Monitoría solicitada a editar
     */
    void edit(Monitoria_Solicitada monitoria_Solicitada);

    /**
     * Elimina la monitoría solicitada que llega como parámetro
     * @param monitoria_Solicitada Monitoría solicitada a eliminar
     */
    void remove(Monitoria_Solicitada monitoria_Solicitada);

    /**
     * Retorna la monitoría solicitada cuyo id llega como parámetro
     * @param id Id de la monitoría solicitada
     * @return monitoría solicitada cuyo id es el que llega como parámetro
     */
    Monitoria_Solicitada find(Object id);

    /**
     * Retorna una lista con todas las monitorías solicitadas
     * @return lista de monitorías solicitadas
     */
    List<Monitoria_Solicitada> findAll();
}
