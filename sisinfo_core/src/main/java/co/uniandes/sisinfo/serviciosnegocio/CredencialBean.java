/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.Credencial;
import co.uniandes.sisinfo.serviciosfuncionales.CredencialFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;

/**
 * Servicio de negocio: Credencial
 */
@Stateless
@EJB(name = "CredencialBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.CredencialLocal.class)
public class CredencialBean implements  CredencialLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * CredencialFacade
     */
    @EJB
    private CredencialFacadeLocal credencialFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteLocal constanteBean;

    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de CredencialBean
     */
    public CredencialBean()
    {
//        try {
//            serviceLocator = new ServiceLocator();
//            constanteBean = (ConstanteLocal) serviceLocator.getLocalEJB(ConstanteLocal.class);
//        } catch (NamingException ex) {
//            Logger.getLogger(CredencialBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public boolean crearCredencial(String comando) {
        try {
            getParser().leerXML(comando);
            String usuario = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO)).getValor().split("@")[0];
            String contrasena = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CONTRASENHA)).getValor();
            boolean fuePosible = false;
            Credencial temp = getCredencialFacade().findByCuenta(usuario);
            if (temp == null) {
                Credencial credencial = new Credencial();
                credencial.setCuenta(usuario);
                Hashtable<String, String> info = new Hashtable<String, String>();
                info.put(usuario, contrasena);
                Hashtable<byte[], byte[]> infoEncrip = encriptarCredenciales(info);
                Enumeration<byte[]> enum1 = infoEncrip.keys();
                byte[] usuarioByte = null;
                while (enum1.hasMoreElements()) {
                    usuarioByte = (byte[]) enum1.nextElement();
                    credencial.setUsuario(usuarioByte);
                }
                byte[] contrasenaByte = infoEncrip.get(usuarioByte);
                credencial.setContrasena(contrasenaByte);
                getCredencialFacade().create(credencial);
                fuePosible = true;
            }
            return fuePosible;
        } catch (Exception ex) {
            Logger.getLogger(CredencialBean.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Hashtable<byte[], byte[]> encriptarCredenciales(Hashtable<String, String> credencialesPlano) {
        Hashtable<byte[], byte[]> datos = new Hashtable<byte[], byte[]>();
        try {
            //Generador de llaves usando el algoritmo de encripción de llaves simétrica DES
            KeyGenerator keygen = KeyGenerator.getInstance(getConstanteBean().getConstante(Constantes.KEY_GENERATOR_DES));
            SecretKey desKey = keygen.generateKey();
            //Escoge el cifrador DES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance(getConstanteBean().getConstante(Constantes.CIPHER_PKCS5PADDING));
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            // Encripta la contraseña en un arreglos de bytes
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(credencialesPlano);
            byte[] byteArray = (byte[]) (byteStream.toByteArray());
            objectStream.flush();
            byte[] outTable = cipher.doFinal(byteArray);
            ByteArrayOutputStream baOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(baOut);
            objectOut.writeObject(desKey);
            byte[] desKeyBA = (byte[]) (baOut.toByteArray());
            objectOut.flush();
            datos.put(desKeyBA, outTable);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return datos;
    }

    @Override
    public Hashtable<String, String> desencriptarCredenciales(Hashtable<byte[], byte[]> credencial) {
        Hashtable<String, String> datosPlanos = new Hashtable<String, String>();
        try {
            // Parsea un arreglo de bytes para construir una llave
            byte[] algorithmKey = (byte[]) credencial.keys().nextElement();
            ByteArrayInputStream baIn1 = new ByteArrayInputStream(algorithmKey);
            ObjectInputStream objectIn1 = new ObjectInputStream((InputStream) baIn1);
            Object result2 = objectIn1.readObject();
            SecretKey llave = (SecretKey) result2;
            //Escoge el cifrador DES/ECB/PKCS5Padding
            Cipher cifrado = Cipher.getInstance(getConstanteBean().getConstante(Constantes.CIPHER_PKCS5PADDING));
            cifrado.init(Cipher.DECRYPT_MODE, llave);
            // Construye los datos desencriptados en una tabla de hash
            byte[] enc_byte = (byte[]) credencial.get(algorithmKey);
            byte[] b = cifrado.doFinal(enc_byte);
            ByteArrayInputStream baIn2 = new ByteArrayInputStream(b);
            ObjectInputStream objectIn2 = new ObjectInputStream((InputStream) baIn2);
            Object result = objectIn2.readObject();
            datosPlanos = (Hashtable<String, String>) result;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return datosPlanos;
    }

    @Override
    public String darContrasena(String cuenta) {
        Credencial credencial = getCredencialFacade().findByCuenta(cuenta);
        byte[] usuarioEncrip = credencial.getUsuario();
        byte[] contrasenaEncrip = credencial.getContrasena();
        Hashtable<byte[], byte[]> encrip = new Hashtable<byte[], byte[]>();
        encrip.put(usuarioEncrip, contrasenaEncrip);
        Hashtable<String, String> infoDesencrip = desencriptarCredenciales(encrip);
        String contrasena = "";
        Collection<String> valores = infoDesencrip.values();
        for (Iterator it = valores.iterator(); it.hasNext();) {
            contrasena = (String) it.next();
        }
        return contrasena;
    }

    /**
     * Retorna el parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if (parser == null) {
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    /**
     * Retorna CredencialFacade
     * @return credencialFacade CredencialFacade
     */
    private CredencialFacadeLocal getCredencialFacade() {
        return credencialFacade;
    }
}
