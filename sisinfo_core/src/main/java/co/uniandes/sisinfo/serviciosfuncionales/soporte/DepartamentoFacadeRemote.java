/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import java.util.List;

import javax.ejb.Remote;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.Departamento;

/**
 *
 * @author Administrador
 */
@Remote
public interface DepartamentoFacadeRemote {

    void create(Departamento departamento);

    void edit(Departamento departamento);

    void remove(Departamento departamento);

    Departamento find(Object id);

    List<Departamento> findAll();

    List<Departamento> findRange(int[] range);

    Departamento findDepartamentoByNombre(String nombre);

    int count();
}
