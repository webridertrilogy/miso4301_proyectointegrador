/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import java.util.List;

import javax.ejb.Local;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.Departamento;

/**
 *
 * @author Administrador
 */
@Local
public interface DepartamentoFacadeLocal {

    void create(Departamento departamento);

    void edit(Departamento departamento);

    void remove(Departamento departamento);

    Departamento find(Object id);

    List<Departamento> findAll();

    List<Departamento> findRange(int[] range);

    Departamento findDepartamentoByNombre (String nombre);

    int count();

}
