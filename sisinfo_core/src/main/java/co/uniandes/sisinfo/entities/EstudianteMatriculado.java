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
 * Entidad que representa a un estudiante matriculado
 * @author Marcela Morales
 */
@Entity
@Table(name = "estudiante_matriculado")
@NamedQueries({
    @NamedQuery(name = "EstudianteMatriculado.findByCarnet", query = "SELECT e FROM EstudianteMatriculado e WHERE e.carnet =:carnet")
})
public class EstudianteMatriculado implements Serializable {

    //----------------------------------------------
    // CONSTANTES
    //----------------------------------------------
    private static final long serialVersionUID = 1L;

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="semestre")
    private String semestre;
    @Column(name="carnet")
    private String carnet;
    @Column(name="apellidos")
    private String apellidos;
    @Column(name="nombres")
    private String nombres;
    @Column(name="programa")
    private String programa;
    @Column(name="doblePrograma")
    private String doblePrograma;
    @Column(name="documentoDeIdentidad")
    private String documentoDeIdentidad;
    @Column(name="fechaNacimiento")
    private Timestamp fechaNacimiento;
    @Column(name="sexo")
    private String sexo;
    @Column(name="direccion")
    private String direccion;
    @Column(name="telefono")
    private String telefono;
    @Column(name="situacionAcademica")
    private String situacionAcademica;
    @Column(name="creditosTomados")
    private double creditosTomados;
    @Column(name="creditosAprobados")
    private double creditosAprobados;
    @Column(name="creditosPGA")
    private double creditosPGA;
    @Column(name="promedioAcumulado")
    private double promedioAcumulado;
    @Column(name="creditosTransferencia")
    private double creditosTransferencia;
    @Column(name="ultimoSemestreCursado")
    private String ultimoSemestreCursado;
    @Column(name="creditosSemestreTomados")
    private double creditosSemestreTomados;
    @Column(name="creditosSemestreAprobados")
    private double creditosSemestreAprobados;
    @Column(name="creditosSemestrePGA")
    private double creditosSemestrePGA;
    @Column(name="promedioSemestre")
    private double promedioSemestre;
    @Column(name="SSC")
    private double SSC;
    @Column(name="email")
    private String email;
    @Column(name="creditosSemestreActual")
    private double creditosSemestreActual;

    //----------------------------------------------
    // MÉTODOS
    //----------------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSSC() {
        return SSC;
    }

    public void setSSC(double SSC) {
        this.SSC = SSC;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public double getCreditosAprobados() {
        return creditosAprobados;
    }

    public void setCreditosAprobados(double creditosAprobados) {
        this.creditosAprobados = creditosAprobados;
    }

    public double getCreditosPGA() {
        return creditosPGA;
    }

    public void setCreditosPGA(double creditosPGA) {
        this.creditosPGA = creditosPGA;
    }

    public double getCreditosSemestreActual() {
        return creditosSemestreActual;
    }

    public void setCreditosSemestreActual(double creditosSemestreActual) {
        this.creditosSemestreActual = creditosSemestreActual;
    }

    public double getCreditosSemestreAprobados() {
        return creditosSemestreAprobados;
    }

    public void setCreditosSemestreAprobados(double creditosSemestreAprobados) {
        this.creditosSemestreAprobados = creditosSemestreAprobados;
    }

    public double getCreditosSemestrePGA() {
        return creditosSemestrePGA;
    }

    public void setCreditosSemestrePGA(double creditosSemestrePGA) {
        this.creditosSemestrePGA = creditosSemestrePGA;
    }

    public double getCreditosSemestreTomados() {
        return creditosSemestreTomados;
    }

    public void setCreditosSemestreTomados(double creditosSemestreTomados) {
        this.creditosSemestreTomados = creditosSemestreTomados;
    }

    public double getCreditosTomados() {
        return creditosTomados;
    }

    public void setCreditosTomados(double creditosTomados) {
        this.creditosTomados = creditosTomados;
    }

    public double getCreditosTransferencia() {
        return creditosTransferencia;
    }

    public void setCreditosTransferencia(double creditosTransferencia) {
        this.creditosTransferencia = creditosTransferencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDoblePrograma() {
        return doblePrograma;
    }

    public void setDoblePrograma(String doblePrograma) {
        this.doblePrograma = doblePrograma;
    }

    public String getDocumentoDeIdentidad() {
        return documentoDeIdentidad;
    }

    public void setDocumentoDeIdentidad(String documentoDeIdentidad) {
        this.documentoDeIdentidad = documentoDeIdentidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Timestamp fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public double getPromedioAcumulado() {
        return promedioAcumulado;
    }

    public void setPromedioAcumulado(double promedioAcumulado) {
        this.promedioAcumulado = promedioAcumulado;
    }

    public double getPromedioSemestre() {
        return promedioSemestre;
    }

    public void setPromedioSemestre(double promedioSemestre) {
        this.promedioSemestre = promedioSemestre;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getSituacionAcademica() {
        return situacionAcademica;
    }

    public void setSituacionAcademica(String situacionAcademica) {
        this.situacionAcademica = situacionAcademica;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUltimoSemestreCursado() {
        return ultimoSemestreCursado;
    }

    public void setUltimoSemestreCursado(String ultimoSemestreCursado) {
        this.ultimoSemestreCursado = ultimoSemestreCursado;
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
        if (!(object instanceof EstudianteMatriculado)) {
            return false;
        }
        EstudianteMatriculado other = (EstudianteMatriculado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.uniandes.sisinfo.entities.Estudiante[id=" + id + "]";
    }
}
