/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.entities;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author im.melo33
 */
@Entity
@Table(name="inscripcionAsistente")
@NamedQueries({
    @NamedQuery(name = "InscripcionAsistente.findByCorreo", query = "SELECT i FROM InscripcionAsistente i WHERE i.persona.correo=:correo"),
    @NamedQuery(name = "InscripcionAsistente.findByHash", query = "SELECT i FROM InscripcionAsistente i WHERE i.hashInscrito=:hash"),
    @NamedQuery(name = "InscripcionAsistente.findByIdInscripcionYCorreoPersona", query = "SELECT i FROM InscripcionAsistente i WHERE i.inscripcion.id = :idInscripcion AND"
    + " i.persona.correo=:correo")

})
public class InscripcionAsistente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="otros", length=2000)
    private String otros;

    @Column(name="inscrito")
    private String inscrito;

    @OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Persona persona;

    @OneToOne(fetch=FetchType.LAZY,cascade=CascadeType.REFRESH)
    private Inscripcion inscripcion;

     @Column(name="hashInscrito")
    private String hashInscrito;

   

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    

    public InscripcionAsistente()    {
    }

    public InscripcionAsistente(Persona p, Inscripcion i)
    {
        persona=p;
        inscripcion=i;
    }

    public Long getId() {
        return id;
    }

   

    public String getInscrito() {
        return inscrito;
    }

    public String getOtros() {
        return otros;
    }


    public void setId(Long id) {
        this.id = id;
    }

  

    public void setInscrito(String inscrito) {
        this.inscrito = inscrito;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getHashInscrito() {
        return hashInscrito;
    }

    public void setHashInscrito(String hashInscrito) {
        this.hashInscrito = hashInscrito;
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
        if (!(object instanceof InscripcionAsistente)) {
            return false;
        }
        InscripcionAsistente other = (InscripcionAsistente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.InscripcionAsistente[id=" + id + "]";
    }

}
