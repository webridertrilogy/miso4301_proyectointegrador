package co.uniandes.sisinfo.entities.datosmaestros;

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
 * @created 01-mar-2010 08:16:20 a.m.
 */
@Entity
@Table(name = "informacion_academica")
@NamedQueries({
    @NamedQuery(name = "InformacionAcademica.findById", query = "SELECT g FROM InformacionAcademica g WHERE g.id = :id"),
     @NamedQuery(name = "InformacionAcademica.findByCodigo", query = "SELECT e.informacion_Academica FROM Estudiante e WHERE e.persona.codigo = :codigo")

})
public class InformacionAcademica implements Serializable {

    @Id
    @Column(name = "informacionAcademica_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "creditosAprobados")
    private Double creditosAprobados;
    @Column(name = "creditosCursados")
    private Double creditosCursados;
    @Column(name = "creditosSemestreActual")
    private Double creditosSemestreActual;
    @Column(name = "promedioPenultimo")
    private Double promedioPenultimo;
    @Column(name = "promedioTotal")
    private Double promedioTotal;
    @Column(name = "promedioUltimo")
    private Double promedioUltimo;
    @Column(name = "semestreSegunCreditos")
    private String semestreSegunCreditos;
    @Column(name = "promedioAntepenultipo")
    private Double promedioAntepenultipo;
    @Column(name = "creditosMonitoriasISISEsteSemestre")
    private Double creditosMonitoriasISISEsteSemestre;

    /**
     * Nivel estudios.
     */
    @Column(name = "nivelEstudios")
    private String nivelEstudios;

    public InformacionAcademica() {
    }

    public Double getCreditosAprobados() {
        return creditosAprobados;
    }

    public void setCreditosAprobados(Double creditosAprobados) {
        this.creditosAprobados = creditosAprobados;
    }

    public Double getCreditosCursados() {
        return creditosCursados;
    }

    public void setCreditosCursados(Double creditosCursados) {
        this.creditosCursados = creditosCursados;
    }

    public Double getCreditosSemestreActual() {
        return creditosSemestreActual;
    }

    public void setCreditosSemestreActual(Double creditosSemestreActual) {
        this.creditosSemestreActual = creditosSemestreActual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPromedioAntepenultipo() {
        return promedioAntepenultipo;
    }

    public void setPromedioAntepenultipo(Double promedioAntepenultipo) {
        this.promedioAntepenultipo = promedioAntepenultipo;
    }

    public Double getPromedioPenultimo() {
        return promedioPenultimo;
    }

    public void setPromedioPenultimo(Double promedioPenultimo) {
        this.promedioPenultimo = promedioPenultimo;
    }

    public Double getPromedioTotal() {
        return promedioTotal;
    }

    public void setPromedioTotal(Double promedioTotal) {
        this.promedioTotal = promedioTotal;
    }

    public Double getPromedioUltimo() {
        return promedioUltimo;
    }

    public void setPromedioUltimo(Double promedioUltimo) {
        this.promedioUltimo = promedioUltimo;
    }

    public String getSemestreSegunCreditos() {
        return semestreSegunCreditos;
    }

    public void setSemestreSegunCreditos(String semestreSegunCreditos) {
        this.semestreSegunCreditos = semestreSegunCreditos;
    }

    public Double getCreditosMonitoriasISISEsteSemestre() {
        return creditosMonitoriasISISEsteSemestre;
    }

    public void setCreditosMonitoriasISISEsteSemestre(Double creditosMonitoriasISISEsteSemestre) {
        this.creditosMonitoriasISISEsteSemestre = creditosMonitoriasISISEsteSemestre;
    }

    public String getNivelEstudios() {
        return nivelEstudios;
    }

    public void setNivelEstudios(String nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
    }
}
