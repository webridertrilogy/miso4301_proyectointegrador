/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.uniandes.sisinfo.entities.Constante;
import co.uniandes.sisinfo.serviciosfuncionales.ConstanteFacadeLocal;

/**
 * Servicio de negocio: Administración de constantes
 */
@Stateless
public class ConstanteBean implements ConstanteLocal{
   
    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * ConstanteFacade
     */
    @EJB
    private ConstanteFacadeLocal constanteFacade;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ConstanteBean
     */
    public ConstanteBean() {
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public void crearConstante(String nombre, String tipo, String valor, String descripcion) {
        Constante constante = new Constante();
        constante.setNombre(nombre);
        constante.setTipo(tipo);
        constante.setValor(valor);
        constante.setDescripcion(descripcion);
        getConstanteFacade().create(constante);
    }

    @Override
    public String getConstante(String nombre) {
        Constante constante = getConstanteFacade().findByNombre(nombre);
//        String constante = getConstanteFacade().getValor(nombre);
        if (constante == null) {
            NullPointerException ex= new NullPointerException("Constante no encontrada: "+nombre);
            ex.printStackTrace();
            throw ex;
           //     return null;
        } else {
            return constante.getValor();
        }
    }

    /**
     * Retorna ConstanteFacade
     * @return constanteFacade ConstanteFacade
     */
    private ConstanteFacadeLocal getConstanteFacade() {
        return constanteFacade;
    }
}
