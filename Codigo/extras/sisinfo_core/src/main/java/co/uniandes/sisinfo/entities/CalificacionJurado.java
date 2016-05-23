/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Entity
@Table(name = "calificacion_jurado")
@NamedQueries({
    @NamedQuery(name = "CalificacionJurado.findByHash", query = "SELECT i FROM CalificacionJurado i WHERE i.hash =:hash")})
public class CalificacionJurado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "hashh")
    private String hash;
    @Column(name = "fechaCalificacion")
    private Timestamp fechaCalificacion;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    private JuradoExternoUniversidad juradoExterno;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    private Profesor juradoInterno;
    @Column(name = "notaJurado")
    private Double notaJurado;
//    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
//    private Collection<CalificacionCriterio> calCriterios;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Collection<CategoriaCriterioJurado> categoriaCriteriosJurado;
    @Column(name = "terminado")
    private Boolean terminado;
    @Column(name = "rolJurado")
    private String rolJurado;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    private Tesis2 tesisCalificada;
     @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    private Coasesor coasesor;

     private Boolean cancelada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionJurado)) {
            return false;
        }
        CalificacionJurado other = (CalificacionJurado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.CalificacionJurado[id=" + id + "]";
    }

//    public Collection<CalificacionCriterio> getCalCriterios() {
//        return calCriterios;
//    }
//
//    public void setCalCriterios(Collection<CalificacionCriterio> calCriterios) {
//        this.calCriterios = calCriterios;
//    }

    public Timestamp getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(Timestamp fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public JuradoExternoUniversidad getJuradoExterno() {
        return juradoExterno;
    }

    public void setJuradoExterno(JuradoExternoUniversidad juradoExterno) {
        this.juradoExterno = juradoExterno;
    }

    public Profesor getJuradoInterno() {
        return juradoInterno;
    }

    public void setJuradoInterno(Profesor juradoInterno) {
        this.juradoInterno = juradoInterno;
    }

    public Double getNotaJurado() {
        return notaJurado;
    }

    public void setNotaJurado(Double notaJurado) {
        this.notaJurado = notaJurado;
    }

    public Boolean getTerminado() {
        return terminado;
    }

    public void setTerminado(Boolean terminado) {
        this.terminado = terminado;
    }

    public String getRolJurado() {
        return rolJurado;
    }

    public void setRolJurado(String rolJurado) {
        this.rolJurado = rolJurado;
    }

    public CalificacionJurado() {
    }

    public Tesis2 getTesisCalificada() {
        return tesisCalificada;
    }

    public void setTesisCalificada(Tesis2 tesisCalificada) {
        this.tesisCalificada = tesisCalificada;
    }

    public Collection<CategoriaCriterioJurado> getCategoriaCriteriosJurado() {
        return categoriaCriteriosJurado;
    }

    public void setCategoriaCriteriosJurado(Collection<CategoriaCriterioJurado> calCriterios) {
        this.categoriaCriteriosJurado = calCriterios;
    }

    public Coasesor getCoasesor() {
        return coasesor;
    }

    public void setCoasesor(Coasesor coasesor) {
        this.coasesor = coasesor;
    }

    public Boolean getCancelada() {
        return cancelada;
    }

    public void setCancelada(Boolean cancelada) {
        this.cancelada = cancelada;
    }
}
