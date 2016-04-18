/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Entity//findByComandoFechaMetodo "comando", comando).setParameter("nombreMetodo", nombreMetodo).setParameter("fecha", fecha).getResultList();
@Table(name = "excepcionsisinfo")
@NamedQueries({
    @NamedQuery(name = "ExcepcionSisinfo.findByComandoFechaMetodo", query = "SELECT i FROM ExcepcionSisinfo i WHERE "
    + "i.comandoSisinfo  =:comando AND i.metodoSisinfo =:nombreMetodo AND i.fechaError =:fecha "),
    @NamedQuery(name = "ExcepcionSisinfo.findByEstado", query = "SELECT i FROM ExcepcionSisinfo i WHERE "
    + "i.solucionado  =:estado AND i.eliminado = false"),
    @NamedQuery(name = "ExcepcionSisinfo.findByNoBorradas", query = "SELECT i FROM ExcepcionSisinfo i WHERE i.eliminado = false")})
public class ExcepcionSisinfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "moduloSisinfo")
    private String moduloSisinfo;
    @Column(name = "metodoSisinfo")
    private String metodoSisinfo;
    @Column(name = "comandoSisinfo")
    private String comandoSisinfo;
    @Column(name = "respuesta"  , length=5000)
    private String respuesta;
    @Column(name = "fechaError")
    private Timestamp fechaError;
    @Column(name = "descripcionErrorPorSoporte")
    private String descripcionErrorPorSoporte;
    @Column(name = "solucionado")
    private Boolean solucionado;
    @Column(name = "fechaSolucion")
    private Timestamp fechaSolucion;
    @Column(name = "eliminado")
    private Boolean eliminado;
    @Column(name="xmlEntrada" , length=5000)
    private String xmlEntrada;

    public String getComandoSisinfo() {
        return comandoSisinfo;
    }

    public void setComandoSisinfo(String comandoSisinfo) {
        this.comandoSisinfo = comandoSisinfo;
    }

    public String getDescripcionErrorPorSoporte() {
        return descripcionErrorPorSoporte;
    }

    public void setDescripcionErrorPorSoporte(String descripcionErrorPorSoporte) {
        this.descripcionErrorPorSoporte = descripcionErrorPorSoporte;
    }

    public Timestamp getFechaError() {
        return fechaError;
    }

    public void setFechaError(Timestamp fechaError) {
        this.fechaError = fechaError;
    }

    public String getMetodoSisinfo() {
        return metodoSisinfo;
    }

    public void setMetodoSisinfo(String metodoSisinfo) {
        this.metodoSisinfo = metodoSisinfo;
    }

    public String getModuloSisinfo() {
        return moduloSisinfo;
    }

    public void setModuloSisinfo(String moduloSisinfo) {
        this.moduloSisinfo = moduloSisinfo;
    }

    public Boolean getSolucionado() {
        return solucionado;
    }

    public void setSolucionado(Boolean solucionado) {
        this.solucionado = solucionado;
    }

    public Timestamp getFechaSolucion() {
        return fechaSolucion;
    }

    public void setFechaSolucion(Timestamp fechaSolucion) {
        this.fechaSolucion = fechaSolucion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getXmlEntrada() {
        return xmlEntrada;
    }

    public void setXmlEntrada(String xmlEntrada) {
        this.xmlEntrada = xmlEntrada;
    }
    

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
        if (!(object instanceof ExcepcionSisinfo)) {
            return false;
        }
        ExcepcionSisinfo other = (ExcepcionSisinfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.ExcepcionSisinfo[id=" + id + "]";
    }
}
