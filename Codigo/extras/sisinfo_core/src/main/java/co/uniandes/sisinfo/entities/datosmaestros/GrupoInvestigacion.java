package co.uniandes.sisinfo.entities.datosmaestros;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Administrador
 * @version 1.0
 * @created 01-mar-2010 08:16:15 a.m.
 */

@Entity
@Table(name = "grupoInvestigacion")
@NamedQueries({
    @NamedQuery(name = "GrupoInvestigacion.findById", query = "SELECT g FROM GrupoInvestigacion g WHERE g.id = :id"),
    @NamedQuery(name = "GrupoInvestigacion.findByNombre", query = "SELECT g FROM GrupoInvestigacion g WHERE g.nombre = :nombre")
})
public class GrupoInvestigacion implements Serializable {

    @Id
    @Column(name = "GrupoInvestigacion_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "descripcion", length=5000)
    private String descripcion;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "pagina")
    private String pagina;
   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Collection<Profesor>profesores;

   @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
   private Profesor coordinadorGrupo;


    public GrupoInvestigacion() {
    }

    public String getDescripcion() {
        return descripcion;
    }
      public Collection<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(Collection<Profesor> profesores) {
        this.profesores = profesores;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public Profesor getCoordinadorGrupo() {
        return coordinadorGrupo;
    }

    public void setCoordinadorGrupo(Profesor coordinadorGrupo) {
        this.coordinadorGrupo = coordinadorGrupo;
    }
    
}
