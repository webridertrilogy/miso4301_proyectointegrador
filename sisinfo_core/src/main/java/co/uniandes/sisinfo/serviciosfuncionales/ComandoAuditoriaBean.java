/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Paola Andrea Gómez Barreto
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.entities.ComandoAuditoria;

/**
 * Servicio de negocio: Administración de comandos auditoría
 */
@Stateless
public class ComandoAuditoriaBean implements ComandoAuditoriaBeanLocal{
   
    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * ComandoAuditoriaBeanFacade
     */
    @EJB
    private ComandoAuditoriaFacadeLocal comandoAuditoriaFacade;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ComandoAuditoriaBean
     */
    public ComandoAuditoriaBean() {
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public void crearComandoAuditoria(String nombre, Boolean auditable) {
        ComandoAuditoria comandoAuditoria = new ComandoAuditoria();
        comandoAuditoria.setNombre(nombre);
        comandoAuditoria.setAuditable(auditable);
        getConstanteFacade().create(comandoAuditoria);
    }

    @Override
    public Boolean getComandoAuditoriaEstado(String nombre) {
        ComandoAuditoria comandoAuditoria = getConstanteFacade().findByNombre(nombre);
        if (comandoAuditoria == null) {
            return false;
        } else {
            return comandoAuditoria.getAuditable();
        }
    }

    /**
     * Retorna comandoAuditoriaFacade
     * @return comandoAuditoriaFacade comandoAuditoriaFacade
     */
    private ComandoAuditoriaFacadeLocal getConstanteFacade() {
        return comandoAuditoriaFacade;
    }
}
