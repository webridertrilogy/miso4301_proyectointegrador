/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.util.Collection;

import javax.ejb.EJB;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Ciudad;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Departamento;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Administrador
 */
public class ConversorDatosEventoExterno {

    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteLocal constanteBean;

    public ConversorDatosEventoExterno(ConstanteLocal constanteBean) {
        this.constanteBean = constanteBean;
    }

    /**
     * Retorna Parser
     * @return parser Parser
     */
    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    Secuencia consultarDepartamentos(Collection<Departamento> departamentos) {
        Secuencia secDepartamentos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DEPARTAMENTOS), "");
        for (Departamento departamento : departamentos) {
            secDepartamentos.agregarSecuencia(consultarDepartamento(departamento));
        }
        return secDepartamentos;
    }

    private Secuencia consultarDepartamento(Departamento departamento) {
        Secuencia secDepartamento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DEPARTAMENTO), "");

        if (departamento.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), departamento.getId().toString());
            secDepartamento.agregarSecuencia(secId);
        }

        if (departamento.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), departamento.getNombre());
            secDepartamento.agregarSecuencia(secNombre);
        }
/*
        System.out.println("---------Ciudades:  " + departamento.getCuidades());

        if (departamento.getCuidades() != null) {
            System.out.println("---------Ciudades2:  " + departamento.getCuidades());

            Secuencia secCiudades = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDADES), "");
            for (Ciudad ciudad : departamento.getCuidades()) {
                secCiudades.agregarSecuencia(consultarCiudad(ciudad));
            }
        }*/
        return secDepartamento;
    }

    Secuencia consultarCiudades(Collection<Ciudad> ciudades) {
        Secuencia secCiudades = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDADES), "");
        for (Ciudad ciudad : ciudades) {
            secCiudades.agregarSecuencia(consultarCiudad(ciudad));
        }
        return secCiudades;
    }

    private Secuencia consultarCiudad(Ciudad ciudad) {
        Secuencia secCiudad = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD), "");

        if (ciudad.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), ciudad.getId().toString());
            secCiudad.agregarSecuencia(secId);
        }

        if (ciudad.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), ciudad.getNombre());
            secCiudad.agregarSecuencia(secNombre);
        }
        return secCiudad;
    }

    Secuencia consultarTiposDocumento(Collection<TipoDocumento> tiposDocumento) {
        Secuencia secTiposDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_DOCUMENTO), "");
        for (TipoDocumento tipoDoc : tiposDocumento) {
            secTiposDocumento.agregarSecuencia(consultarTipoDocumento(tipoDoc));
        }
        return secTiposDocumento;
    }

    private Secuencia consultarTipoDocumento(TipoDocumento tipoDoc) {
        Secuencia secTipoDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), "");

        if (tipoDoc.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tipoDoc.getId().toString());
            secTipoDocumento.agregarSecuencia(secId);
        }

        if (tipoDoc.getTipo() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), tipoDoc.getTipo());
            secTipoDocumento.agregarSecuencia(secNombre);
        }

        if (tipoDoc.getDescripcion() != null) {
            Secuencia secDescripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESC_TIPO_DOCUMENTO), tipoDoc.getDescripcion());
            secTipoDocumento.agregarSecuencia(secDescripcion);
        }

        return secTipoDocumento;
    }
}
