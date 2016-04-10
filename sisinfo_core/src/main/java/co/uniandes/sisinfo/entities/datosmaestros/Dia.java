/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities.datosmaestros;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entidad Día
 */
@Entity
@Table(name = "dia_disponible")
public class Dia implements Serializable {

    //---------------------------------------
    // Constantes
    //---------------------------------------
    private static final long serialVersionUID = 1L;

    //---------------------------------------
    // Atributos
    //---------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "dia_semana")
    private String dia_semana;
    @OneToMany(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL)
    private Collection<Franja> franjas_libre;
    @Column(name = "sumaHorario")
    private int sumaHorario;

    //---------------------------------------
    // Métodos
    //---------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public Collection<Franja> getFranjas_libre() {
        return franjas_libre;
    }

    public void setFranjas_libre(Collection<Franja> franjas_libre) {
        this.franjas_libre = franjas_libre;
        if (franjas_libre != null) {
            int[] sumaCreada=crearSuma();

            setSumaHorario(sumaCreada);
        }
    }

    public int getSumaHorario() {
        return sumaHorario;
    }

    public void setSumaHorario(int[] sumaHorario) {
        int bits = sumaHorario[27];
        String a=""+bits;
        for (int i = 26; i > -1; i--) {
            bits += Math.pow(2, 27 - i) * sumaHorario[i];
            a=a.concat(Integer.toString(sumaHorario[i]));
        }

        this.sumaHorario = bits;
        reconstruirFranjas();
    }

    public void setSuma(int suma) {
        this.sumaHorario = suma;
        reconstruirFranjas();
    }

    private void reconstruirFranjas(){

        int suma=sumaHorario;
        Collection<Franja> nuevasFranjas = new Vector<Franja>();
        Franja f=new Franja();
        int[] arreglo = new int[28];
        String confS="";
        for (int c = 27; c >-1 ; c--) {
            int valor = ((suma & 134217728) == 0 ? 0 : 1);
            if(valor==1){
                f=new Franja();
                if((c%2)==0){
                    f.setHora_inicio(7+(c/2));
                    f.setHora_fin(7+(c/2));
                    f.setMinuto_inicio(0);
                    f.setMinuto_fin(30);
                }else{
                    f.setHora_inicio(7+((c-1)/2));
                    f.setHora_fin(8+((c-1)/2));
                    f.setMinuto_inicio(30);
                    f.setMinuto_fin(0);
                }
                nuevasFranjas.add(f);
            }
            confS = confS.concat("" + valor);
            arreglo[c] = valor;
            suma <<= 1;

        }
        franjas_libre=nuevasFranjas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Dia)) {
            return false;
        }
        Dia other = (Dia) object;
        if (this.getDia_semana().equals(other.getDia_semana())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Dia_Disponible[id=" + id + "]";
    }

    public int[] crearSuma() {
        Collection<Franja> franjas = this.getFranjas_libre();
        Iterator<Franja> it = franjas.iterator();
        Franja franja = null;
        int hi = 0;
        int hf = 0;
        int mi = 0;
        int mf = 0;
        int[] suma = new int[28];
        int indice = 0;

        while (it.hasNext()) {
            franja = it.next();

            hi = franja.getHora_inicio();
            hf = franja.getHora_fin();
            mi = franja.getMinuto_inicio();
            mf = franja.getMinuto_fin();
            indice = 27 - ((hi - 7) * 2);

            if (hi == hf) {
                if (mi < 30) {
                    suma[indice] = 1;
                }
                if (mf > 30) {
                    suma[indice - 1] = 1;
                }
            } else {
                for (int i = hi; i < hf; i++) {
                    suma[indice] = 1;
                    suma[indice - 1] = 1;
                    indice = indice - 2;
                }
                if (mi > 29) {
                    suma[27 - ((hi - 7) * 2)] = 0;
                }
                if (mf > 0) {
                    suma[indice] = 1;
                    if (mf > 30) {
                        suma[indice - 1] = 1;
                    }
                }

            }

        }
        return suma;
    }
}