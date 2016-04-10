/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.seguridad;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;

/**
 *
 */
public class AutorizacionSistema {

    private AccesoLDAP ldap;

    public AutorizacionSistema() {
    }

    public AccesoLDAP getLdap() {
        return ldap;
    }

    public void setLdap(AccesoLDAP ldap) {
        this.ldap = ldap;
    }

    public void autorizar(String login, String pass) {
        String ldapAns;
        try {
            ldapAns = ldap.autorizacion(login, pass,false);
        } catch (AuthenticationException ex) {
            Logger.getLogger(AutorizacionSistema.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(AutorizacionSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
