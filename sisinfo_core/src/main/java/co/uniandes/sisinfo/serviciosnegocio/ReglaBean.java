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

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.Regla;
import co.uniandes.sisinfo.serviciosfuncionales.ReglaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.ParserT;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ReglaRemote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.NamingException;

/**
 * Servicio de negocio: Administración de reglas
 */
@Stateless
@EJB(name = "ReglaBean", beanInterface = co.uniandes.sisinfo.serviciosnegocio.ReglaLocal.class)
public class ReglaBean implements ReglaRemote, ReglaLocal {

    //---------------------------------------
    // Atributos
    //---------------------------------------
    /**
     * Parser
     */
    private ParserT parser;
    /**
     * ReglaFacade
     */
    @EJB
    private ReglaFacadeLocal reglaFacade;
    /**
     *  ConstanteBean
     */
    @EJB
    private ConstanteRemote constanteBean;

    private ServiceLocator serviceLocator;

    //---------------------------------------
    // Constructor
    //---------------------------------------
    /**
     * Constructor de ReglaBean
     */
    public ReglaBean() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            } catch (NamingException ex) {
            Logger.getLogger(ReglaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //---------------------------------------
    // Métodos
    //---------------------------------------
    @Override
    public String crearRegla(String root) {
        try {
            getParser().leerXML(root);
            Regla regla = new Regla();
            String descripcion = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION)).getValor();
            String operador = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERADOR)).getValor();
            String tipo = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO)).getValor();
            String valor = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR)).getValor();
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            regla.setDescripcion(descripcion);
            regla.setOperador(operador);
            regla.setTipo(tipo);
            regla.setValor(valor);
            regla.setNombre(nombre);
            getReglaFacade().create(regla);
            return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_REGLA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), "La regla fué creada exitosamente", new LinkedList<Secuencia>());
        } catch (Exception e) {
            try {
                Logger.getLogger(ReglaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_CREAR_REGLA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), "La regla no fué creada con éxito, intente nuevamente", new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReglaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String eliminarRegla(String root) {
        try {
            getParser().leerXML(root);
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            Regla regla = getReglaFacade().findByNombre(nombre);
            if (regla == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_REGLA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), "No se encontró al regla a eliminar, intente nuevamente", new LinkedList<Secuencia>());
            } else {
                getReglaFacade().remove(regla);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_REGLA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), "La regla fué eliminada exitosamente", new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ReglaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_ELIMINAR_REGLA), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), "La regla no fué eliminada con éxito, intente nuevamente", new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReglaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String darReglas(String root) {
        try {
            getParser().leerXML(root);
            Collection<Regla> reglas = getReglaFacade().findAll();
            if (reglas.size() == 0) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_REGLAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), "No se encontraron reglas", new LinkedList<Secuencia>());
            } else {
                Collection<Secuencia> secuencias = getSecuenciasReglas(reglas);
               return getParser().generarRespuesta(secuencias, getConstanteBean().getConstante(Constantes.CMD_DAR_REGLAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), "La consulta de reglas se realizó exitosamente", new LinkedList<Secuencia>());
            }
        } catch (Exception e) {
            try {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_DAR_REGLAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), "La consulta de reglas no se realizó exitosamente, intente nuevamente", new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReglaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public String validarRegla(String root) {
        try {
            getParser().leerXML(root);
            
            String nombre = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor();
            String valorComparar = getParser().obtenerSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR)).getValor();
            Regla regla = getReglaFacade().findByNombre(nombre);
            if (regla == null) {
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_VALIDAR_REGLAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), "No se encontró la regla para validación", new LinkedList<Secuencia>());
            } else {
                boolean validacion = validarRegla(nombre, valorComparar);
                if (!validacion) {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_VALIDAR_REGLAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), "Regla validada con éxito, los datos son incorrectos", new LinkedList<Secuencia>());
                } else {
                    return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_VALIDAR_REGLAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_MENSAJE), "Regla validada con éxito, los datos son correctos", new LinkedList<Secuencia>());
                }
            }
        } catch (Exception e) {
            try {
                Logger.getLogger(ReglaBean.class.getName()).log(Level.SEVERE, null, e);
                return getParser().generarRespuesta(new LinkedList<Secuencia>(), getConstanteBean().getConstante(Constantes.CMD_VALIDAR_REGLAS), getConstanteBean().getConstante(Constantes.VAL_TAG_TIPO_MENSAJE_ERROR), "La validación no fué realizada con éxito, intente nuevamente", new LinkedList<Secuencia>());
            } catch (Exception ex) {
                Logger.getLogger(ReglaBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

    @Override
    public boolean validarRegla(String nombre, String valorComparar) {
        Regla regla = getReglaFacade().findByNombre(nombre);
        if (regla != null) {
            String tipo = regla.getTipo();
            String operador = regla.getOperador();
            String valor = regla.getValor();
            if (tipo.equals(getConstanteBean().getConstante(Constantes.OP_NUMERICO))) {
                if (operador.equals(getConstanteBean().getConstante(Constantes.OP_MENOR_QUE))) {
                    return Double.parseDouble(valorComparar) < Double.parseDouble(valor);
                } else if (operador.equals(getConstanteBean().getConstante(Constantes.OP_MAYOR_QUE))) {
                    return Double.parseDouble(valorComparar) > Double.parseDouble(valor);
                } else if (operador.equals(getConstanteBean().getConstante(Constantes.OP_MENOR_O_IGUAL))) {
                    return Double.parseDouble(valorComparar) <= Double.parseDouble(valor);
                } else if (operador.equals(getConstanteBean().getConstante(Constantes.OP_MAYOR_O_IGUAL))) {
                    return Double.parseDouble(valorComparar) >= Double.parseDouble(valor);
                } else if (operador.equals(getConstanteBean().getConstante(Constantes.OP_IGUAL))) {
                    return Double.parseDouble(valorComparar) == Double.parseDouble(valor);
                } else if (operador.equals(getConstanteBean().getConstante(Constantes.OP_DIFERENTE))) {
                    return Double.parseDouble(valorComparar) != Double.parseDouble(valor);
                }
            } else if (tipo.equals(getConstanteBean().getConstante(Constantes.TIPO_CADENA_CARACETERES))) {
                if(valorComparar == null){
                    return false;
                }else{
                    return !valorComparar.equals("");
                }
            }
        }
        
        return false;
    }

    @Override
    public Collection<String> getNombresReglasPorIds(Collection<String> idReglas) {
        Collection<String> reglas = new LinkedList<String>();
        Iterator<String> iterator = idReglas.iterator();
        while (iterator.hasNext()) {
            String idRegla = iterator.next();
            Regla regla = getReglaFacade().findById(Long.parseLong(idRegla));
            reglas.add(regla.getNombre());
        }
        return reglas;
    }

    /**
     * Retorna una colección de secuencias dada una colección de reglas
     * @param reglas Colección de reglas
     * @return Colección de secuencias
     */
    private Collection<Secuencia> getSecuenciasReglas(Collection<Regla> reglas) {
        Collection<Secuencia> secuencias = new ArrayList<Secuencia>();
        Secuencia secuenciaReglas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_REGLAS), getConstanteBean().getConstante(Constantes.NULL));
        Iterator<Regla> iterador = reglas.iterator();
        while (iterador.hasNext()) {
            Regla regla = iterador.next();
            Secuencia secuenciaRegla = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_REGLA), getConstanteBean().getConstante(Constantes.NULL));
            secuenciaRegla.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_DESCRIPCION), regla.getDescripcion()));
            secuenciaRegla.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OPERADOR), regla.getOperador()));
            secuenciaRegla.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), regla.getValor()));
            secuenciaRegla.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO), regla.getTipo()));
            secuenciaRegla.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), regla.getValor()));
            secuenciaRegla.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR), regla.getValor()));
            secuenciaRegla.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_REGLA), Long.toString(regla.getId())));
            secuenciaReglas.agregarSecuencia(secuenciaRegla);
        }
        secuencias.add(secuenciaReglas);
        return secuencias;
    }

    /**
     * Retorna Parser
     * @return parser Parser
     */
    private ParserT getParser() {
        if(parser == null){
            parser = new ParserT();
        }
        return parser;
    }

    /**
     * Retorna ReglaFacade
     * @return reglaFacade ReglaFacade
     */
    private ReglaFacadeLocal getReglaFacade() {
        return reglaFacade;
    }

    /**
     * Retorna ConstanteBean
     * @return constanteBean ConstanteBean
     */
    private ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
}
