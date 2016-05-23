/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.parser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class Secuencia implements Serializable {

    /**
     * Lista con los atributos de la secuencia
     */
    private ArrayList<Atributo> atributos;
    /**
     * Lista con las secuencias contenidas dentro de la secuencia
     */
    private ArrayList<Secuencia> secuencias;
    /**
     * Nombre de la secuencia
     */
    private String nombre;
    /**
     * Valor de la secuencia
     */
    private String valor;

    /**
     * Constructor de la secuencia dado un nombre y un valor. Inicializa las listas de atributos <br>
     * y secuencias como vacias
     * @param nombre Nombre de la secuencia
     * @param valor Valor de la secuencia
     */
    public Secuencia(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
        atributos = new ArrayList();
        secuencias = new ArrayList();

    }

    /**
     * Agrega un atributo a la lista de atributos de esta secuencia
     * @param att Atributo a agregar
     */
    public void agregarAtributo(Atributo att) {
        atributos.add(att);
    }

    /**
     * Agrega una secuencia a la lista de secuencias
     * @param sec Secuencia a agregar
     */
    public void agregarSecuencia(Secuencia sec) {
        secuencias.add(sec);
    }

    /**
     * Retorna el nombre de la secuencia
     * @return nombre de la secuencia
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre de la secuencia
     * @param nombre Nuevo nombre de la secuencia
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna el valor de la secuencia
     * @return valor de la secuencia
     */
    public String getValor() {
        return valor;
    }

    /**
     * Retorna el valor de la secuencia
     * @return valor de la secuencia
     */
    public int getValorInt() {
        return Integer.parseInt(valor);
    }

    public long getValorLong() {
        return Long.parseLong(valor);
    }

    public double getValorDouble() {
        return Double.parseDouble(valor);
    }

    /**
     * Modifica el valor de la secuencia
     * @param valor Nuevo valor de la secuencia
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * Construye una secuencia basada en una lista de atributos y una lista de secuencias
     * @param atributos Lista de atributos de la nueva secuencia
     * @param secuencias Lista de secuencias de la nueva secuencia
     */
    public Secuencia(ArrayList<Atributo> atributos, ArrayList<Secuencia> secuencias) {
        this.atributos = atributos;
        this.secuencias = secuencias;
    }

    /**
     * Retorna la lista de atributos de esta secuencia
     * @return la lista de atributos de la secuencia
     */
    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    /**
     * Modifica la lista de atributos de la secuencia
     * @param atributos Nueva lista de atributos de la secuencia
     */
    public void setAtributos(ArrayList<Atributo> atributos) {
        this.atributos = atributos;
    }

    /**
     * Retorna la lista de secuencias
     * @return Lista de secuencias
     */
    public ArrayList<Secuencia> getSecuencias() {
        return secuencias;
    }

    /**
     * Modifica la lista de secuencias
     * @param secuencias Nueva lista de secuencias
     */
    public void setSecuencias(ArrayList<Secuencia> secuencias) {
        this.secuencias = secuencias;
    }

    /**
     * Retorna la representacion en String de esta secuencia mostrando su nombre<br>
     * y su valor
     * @return Representacion en String de esta secuencia
     */
    @Override
    public String toString() {
        return nombre + ":" + valor;
    }

    /**
     * Busca dentro de su lista una secuencia que coincida con el nombre que viene<br>
     * por parametro y retorna dicha secuencia.
     * @param nombre Nombre de la secuencia buscada
     * @return Secuencia con el nombre especificado o null si no se encontro
     */
    public Secuencia obtenerPrimeraSecuencia(String nombre) {
        if (this.nombre.equals(nombre)) {
            return this;
        }
        for (Secuencia secuencia : secuencias) {
            Secuencia resultado = secuencia.obtenerPrimeraSecuencia(nombre);
            if (resultado != null) {
                return resultado;
            }
        }
        return null;
    }

    /**
     * Busca dentro de su lista una secuencia que coincida con el nombre que viene<br>
     * por parametro y retorna dicha secuencia.
     * Este metodo no es recursivo, solo busca en el siguiente nivel de secuencias.
     * @param nombre Nombre de la secuencia buscada
     * @return Secuencia con el nombre especificado o null si no se encontro
     */
    public Secuencia obtenerSecuenciaHija(String nombre) {
        for (Secuencia secuencia : secuencias) {
            if (nombre.equals(secuencia.nombre)) {
                return secuencia;
            }
        }
        return null;
    }

    /**
     * Busca dentro de su lista una secuencia la primera secuencia que tenga como valor en un atributo el valor por parÃ¡metro
     * @param valorAtributo Valor del atributo que se busca
     * @return Secuencia con un atributo cuyo valor es el dado por parÃ¡metro o null si no se encontro
     */
    public Secuencia obtenerPrimeraSecuenciaConAtributo(String valorAtributo) {
        boolean existeValor = false;
        for (Atributo atributo : atributos) {
            existeValor = atributo.getValor().equals(valorAtributo);
            if (existeValor) {
                return this;
            }
        }
        for (Secuencia secuencia : secuencias) {
            Secuencia resultado = secuencia.obtenerPrimeraSecuenciaConAtributo(valorAtributo);
            if (resultado != null) {
                return resultado;
            }
        }
        return null;
    }

    public void print() {
        print(0);
    }

    private void print(int tab) {
        for (int i = 0; i < tab; i++) {
            System.out.print('\t');
        }
        System.out.println("<" + nombre + ">" + valor);
        for (Secuencia s : secuencias) {
            s.print(tab + 1);
        }
        System.out.println("</" + nombre + ">");
    }
}
