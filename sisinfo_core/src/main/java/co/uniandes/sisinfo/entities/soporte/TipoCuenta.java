package co.uniandes.sisinfo.entities.soporte;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Administrador
 * @version 1.0
 * @created 01-mar-2010 08:16:18 a.m.
 */
@Entity
@Table(name = "tipo_cuenta")
@NamedQueries({
    @NamedQuery(name = "TipoCuenta.findById", query = "SELECT p FROM TipoCuenta p WHERE p.id = :id"),
    @NamedQuery(name = "TipoCuenta.findByNombre", query = "SELECT p FROM TipoCuenta p WHERE p.nombre  LIKE :nombre")
})
public class TipoCuenta implements Serializable{

     @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tipo_cuenta_id")
	private Long id;

      @Column(name = "nombre")
	private String nombre;

	public TipoCuenta(){

	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	

}