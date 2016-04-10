/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Paola Andrea Gómez Barreto
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicio de negocio: Administración de comandos auditoría
 */
@Remote
public interface ComandoAuditoriaBeanRemote {

    /**
     * Método que crea una nueva constante auditable dada su información
     * @param nombre Nombre de la comando auditable
     * @param auditable Comando auditable habilitado o no
     */
    void crearComandoAuditoria(String nombre, Boolean auditable);

    /**
     * Método que retorna el valor de una constante auditable dado su nombre
     * @param nombre Nombre de la cconstante auditable
     * @return estado de la constante auditable buscada (true o false), null en caso de no encontrarse
     */
    Boolean getComandoAuditoriaEstado(String nombre);
}
