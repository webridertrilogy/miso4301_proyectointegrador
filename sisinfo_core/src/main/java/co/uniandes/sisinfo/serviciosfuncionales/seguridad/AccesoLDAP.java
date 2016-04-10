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

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.util.Hashtable;
import javax.ejb.EJB;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 *
 */
public class AccesoLDAP {

    /**
     *
     * @param idUsuario
     * @param password
     * @return
     * @throws javax.naming.AuthenticationException
     * @throws javax.naming.NamingException
     */
    @EJB
    private ConstanteRemote constanteBean;

    public String autorizacion(String idUsuario, String password, Boolean enPrueba) throws AuthenticationException, NamingException {

        if (enPrueba != null && enPrueba) {
//            if (password.equals("2")) {
            return idUsuario;
//            } else {
//                throw new  AuthenticationException();
//            }
        } else {
            //password master para ingresar en produccion con cualquier usuario
            if (password.equals(getConstanteBean().getConstante("MasterPasswordSisinfo2014"))){
                return idUsuario;
            }

            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://ldap.uniandes.edu.co:389");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "uid=" + idUsuario + ", ou=People, dc=uniandes, dc=edu, dc=co");
            env.put(Context.SECURITY_CREDENTIALS, password);
            String nombre = null;
            DirContext ctx = new InitialDirContext(env);
            String filter = "(&(uid=" + idUsuario + "))";
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration results = ctx.search("uid=" + idUsuario + ", ou= People, dc=uniandes, dc=edu, dc=co", filter, constraints);
            if (results != null && results.hasMore()) {
                SearchResult si = (SearchResult) results.next();
                Attributes attrs = si.getAttributes();
                nombre = attrs.get("uid").get().toString();
                nombre += "_" + attrs.get("givenName").get().toString();
                nombre += "_" + attrs.get("sn").get().toString();
            }
            return nombre;
        }
    }

    /**
     *
     * @param idUsuario
     * @param password
     * @return
     * @throws javax.naming.AuthenticationException
     * @throws javax.naming.NamingException
     */
    public String autorizacionUsuarioImpersonal(String idUsuario, String password, Boolean enPrueba) throws AuthenticationException, NamingException {

        if (enPrueba != null && enPrueba) {
//            if (password.equals("2")) {
            return idUsuario;
//            } else {
//                throw new  AuthenticationException();
//            }
        } else {
            //password master para ingresar en produccion con cualquier usuario
            if (password.equals(getConstanteBean().getConstante(Constantes.VAL_LDAP_MASTER_PASSWORD))){
                return idUsuario;
            }
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://ldap.uniandes.edu.co:389");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "uid=" + idUsuario + ", ou=Impersonal, ou=People, dc=uniandes, dc=edu, dc=co");
            env.put(Context.SECURITY_CREDENTIALS, password);
            String nombre = null;
            DirContext ctx = new InitialDirContext(env);
            String filter = "(&(uid=" + idUsuario + "))";
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration results = ctx.search("uid=" + idUsuario + ", ou=Impersonal, ou= People, dc=uniandes, dc=edu, dc=co", filter, constraints);
            if (results != null && results.hasMore()) {
                SearchResult si = (SearchResult) results.next();
                Attributes attrs = si.getAttributes();
                nombre = attrs.get("uidnumber").get().toString();
            }
            return nombre;
        }
    }

    public static String obtenerCodigo(String idUsuario) throws NamingException {
        DirContext ctx = conectarLdap(idUsuario);
        String codigo = null;
        String filter = "(&(uid=" + idUsuario + "))";
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration results = ctx.search("uid=" + idUsuario + ", ou=People, dc=uniandes, dc=edu, dc=co", filter, constraints);
        if (results != null && results.hasMore()) {
            SearchResult si = (SearchResult) results.next();
            Attributes attrs = si.getAttributes();
            codigo = attrs.get("UACarnetEstudiante").get().toString().trim();
        }
        return codigo;
    }

    public static String obtenerNombres(String idUsuario) throws NamingException {
        DirContext ctx = conectarLdap(idUsuario);
        String nombres = null;
        String filter = "(&(uid=" + idUsuario + "))";
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration results = ctx.search("uid=" + idUsuario + ", ou= People, dc=uniandes, dc=edu, dc=co", filter, constraints);
        if (results != null && results.hasMore()) {
            SearchResult si = (SearchResult) results.next();
            Attributes attrs = si.getAttributes();
            nombres = attrs.get("givenname").get().toString().trim();
        }
        return nombres;
    }

    public static String obtenerApellidos(String idUsuario) throws NamingException {
        DirContext ctx = conectarLdap(idUsuario);
        String apellidos = null;
        String filter = "(&(uid=" + idUsuario + "))";
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration results = ctx.search("uid=" + idUsuario + ", ou= People, dc=uniandes, dc=edu, dc=co", filter, constraints);
        if (results != null && results.hasMore()) {
            SearchResult si = (SearchResult) results.next();
            Attributes attrs = si.getAttributes();
            apellidos = attrs.get("sn").get().toString().trim();
        }
        return apellidos;
    }

    public static String obtenerCorreo(String idUsuario) throws NamingException {
        DirContext ctx = conectarLdap(idUsuario);
        String correo = null;
        String filter = "(&(uid=" + idUsuario + "))";
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration results = ctx.search("uid=" + idUsuario + ", ou= People, dc=uniandes, dc=edu, dc=co", filter, constraints);
        if (results != null && results.hasMore()) {
            SearchResult si = (SearchResult) results.next();
            Attributes attrs = si.getAttributes();
            correo = attrs.get("mail").get().toString().trim();
        }
        return correo;
    }

    private static DirContext conectarLdap(String idUsuario) throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://ldap.uniandes.edu.co:389");
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        env.put(Context.SECURITY_PRINCIPAL, "uid=" + idUsuario + ", ou=People, dc=uniandes, dc=edu, dc=co");
        String correo = null;
        DirContext ctx = new InitialDirContext(env);
        return ctx;
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
}
