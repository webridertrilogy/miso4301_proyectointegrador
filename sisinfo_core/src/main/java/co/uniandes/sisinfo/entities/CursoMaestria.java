package co.uniandes.sisinfo.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "curso_maestria")
@NamedQueries({
    @NamedQuery(name = "CursoMaestria.findByNombre", query = "SELECT i FROM CursoMaestria i WHERE i.nombre =:nombre"),
    @NamedQuery(name = "CursoMaestria.findByClasificacion", query = "SELECT i FROM CursoMaestria i WHERE i.clasificacion =:clasificacion")
})
public class CursoMaestria implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descricpion;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "clasificacion")
    private String clasificacion;

    public CursoMaestria() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricpion() {
        return descricpion;
    }

    public void setDescricpion(String descricpion) {
        this.descricpion = descricpion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    
   
}
