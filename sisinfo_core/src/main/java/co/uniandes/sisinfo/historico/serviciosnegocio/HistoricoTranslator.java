/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosnegocio;

import co.uniandes.sisinfo.entities.Archivo;
import co.uniandes.sisinfo.entities.Aspirante;
import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import co.uniandes.sisinfo.entities.Periodo;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.TipoArchivo;
import co.uniandes.sisinfo.entities.datosmaestros.Curso;
import co.uniandes.sisinfo.entities.datosmaestros.Estudiante;
import co.uniandes.sisinfo.entities.datosmaestros.InformacionAcademica;
import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import co.uniandes.sisinfo.entities.datosmaestros.Profesor;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import co.uniandes.sisinfo.historico.entities.h_archivo;
import co.uniandes.sisinfo.historico.entities.h_curso;
import co.uniandes.sisinfo.historico.entities.h_informacionAcademica;
import co.uniandes.sisinfo.historico.entities.h_monitor;
import co.uniandes.sisinfo.historico.entities.h_monitoria;
import co.uniandes.sisinfo.historico.entities.h_profesor;
import co.uniandes.sisinfo.historico.entities.h_seccion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Juan Manuel Moreno B.
 */
public class HistoricoTranslator {

    /**
     * Convierte de curso de sisinfo a curso de historicos.
     */
    public static h_curso createh_cursoFromCurso(Curso curso) {
        h_curso hist_curso = new h_curso();

        hist_curso.setCodigo(curso.getCodigo());
        hist_curso.setNombre(curso.getNombre());
        hist_curso.setCreditos(curso.getCreditos());
        return hist_curso;
    }

    /**
     * Transfiere contenido de curso de sisinfo a curso de historicos.
     */
    public static h_curso fromCurso2h_curso(Curso curso, h_curso hist_curso) {
        
        hist_curso.setCodigo(curso.getCodigo()); // codigo y nombre pueden ser null.
        hist_curso.setNombre(curso.getNombre());
        hist_curso.setCreditos(curso.getCreditos());
        return hist_curso;
    }

    /**
     * Convierte de curso de sisinfo a curso de historicos.
     */
    public static h_seccion createh_seccionFromSeccion(Seccion seccion) {
        h_seccion hist_seccion = new h_seccion();

        hist_seccion.setCrn(seccion.getCrn()); // no es null.
        hist_seccion.setNumeroSeccion(String.valueOf(seccion.getNumeroSeccion()));
        return hist_seccion;
    }
    
    /**
     * Pasa secciones del curso de Sisinfo al curso de historicos.
     */
    public static void pasarSecciones(Curso curso, h_curso hist_curso, List<h_profesor> listH_Profesor, Periodo periodo) {

        Collection<h_seccion> collh_seccion = new ArrayList<h_seccion>();
        h_seccion hist_seccion = null;
        for (Seccion seccion : curso.getSecciones()) {

            hist_seccion = createh_seccionFromSeccion(seccion);

            /* Recorremos profesores de historicos
             * y agregamos el que coincida con seccion de Sisinfo
             * y agregamos periodo.
             */
            for (h_profesor hist_profesorTmp : listH_Profesor) {
                if (seccion.getProfesorPrincipal() != null &&
                        seccion.getProfesorPrincipal().getPersona().getCorreo().equals(
                            hist_profesorTmp.getCorreo())) {
                    // profesor puede ser null
                    hist_seccion.setProfesor(hist_profesorTmp);
                    hist_seccion.setPeriodo(periodo.getPeriodo());
                    break;
                }
            }
            collh_seccion.add(hist_seccion);
        }
        if (hist_curso.getSecciones() == null) {

            hist_curso.setSecciones(new ArrayList<h_seccion>());
        }
        hist_curso.getSecciones().addAll(collh_seccion);
    }

    /**
     * Convierte de profesor de sisinfo a profesor de historicos.
     */
    public static h_profesor fromProfesor2h_profesor(Profesor profesor, h_profesor hist_profesor) {

        if (profesor.getPersona() != null) {

            hist_profesor.setNombres(profesor.getPersona().getNombres());
            hist_profesor.setApellidos(profesor.getPersona().getApellidos());
            hist_profesor.setCorreo(profesor.getPersona().getCorreo());
        }
        if (profesor.getGrupoInvestigacion() != null) {

            hist_profesor.setGrupoInvestigacion(profesor.getGrupoInvestigacion().getNombre());
        }
        return hist_profesor;
    }

    /**
     * Convierte de aspirante de sisinfo a monitor de historicos.
     */
    public static h_monitor fromAspirante2h_monitor(Aspirante aspirante, h_monitor hist_monitor, Periodo periodo) {

        Persona persona = aspirante.getEstudiante().getPersona(); // La informaci{on de la persoan se agreg{o y valid{o al crear la solicitud.
        hist_monitor.setNombre(persona.getNombres());
        hist_monitor.setApellidos(persona.getApellidos());
        hist_monitor.setCodigo(persona.getCodigo());
        hist_monitor.setCorreo(persona.getCorreo());
        hist_monitor.setDocumentoIdentidad(persona.getNumDocumentoIdentidad());
        hist_monitor.setTipoDocumento(persona.getTipoDocumento().getTipo());
        hist_monitor.setProgramaFacultad(aspirante.getEstudiante().getPrograma().getNombre());
        return hist_monitor;
    }

    /**
     * Convierte informacion academica de sisinfo a historicos.
     */
    public static h_informacionAcademica fromInformacionAcademica2h_informacionAcademica(
            InformacionAcademica informacionAcademica, Periodo periodo) {

        h_informacionAcademica hist_informacionAcademica = new h_informacionAcademica();
        // Los promedios en base pueden ser null pero en el objeto informacionAcademica son 0.
        hist_informacionAcademica.setPromedioPenultimo(informacionAcademica.getPromedioPenultimo());
        hist_informacionAcademica.setPromedioUltimo(informacionAcademica.getPromedioUltimo());
        hist_informacionAcademica.setPromedioTotal(informacionAcademica.getPromedioTotal());
        hist_informacionAcademica.setPeriodo(periodo.getPeriodo());
        return hist_informacionAcademica;
    }

    /**
     * Convierte de monitoria de sisinfo a monitoria de historicos.
     */
    public static h_monitoria fromMonitoria2h_monitoria(
            Solicitud solicitud, MonitoriaAceptada monitoriaAceptada, Periodo periodo, h_monitor hist_monitor, h_seccion hist_seccion) {
        h_monitoria hist_monitoria = new h_monitoria();

        hist_monitoria.setCreditos(monitoriaAceptada.getCarga());
        hist_monitoria.setTipoMonitoria(monitoriaAceptada.getCarga() == 2 ? "T1" : "T2");
        hist_monitoria.setFechaCreacion(solicitud.getFechaCreacion());
        hist_monitoria.setFechaRadicacion(solicitud.getFechaRadicacion());
        hist_monitoria.setNota(monitoriaAceptada.getNota());
        hist_monitoria.setFechaRadicacion(solicitud.getFechaRadicacion());
        hist_monitoria.setNumeroRadicacion(solicitud.getNumeroRadicacion());
        hist_monitoria.setPeriodo(periodo.getPeriodo());
        hist_monitoria.setMonitor(hist_monitor);
        hist_monitoria.setSeccion(hist_seccion);
        return hist_monitoria;
    }

    /**
     * Convierte de archivo a h_archivo.
     */
    public static h_archivo fromArchivo2h_archivo(Archivo archivo, List<TipoArchivo> listTipoArchivo, List<h_seccion> listh_seccion, Periodo periodo) {

         h_archivo hist_archivo = new h_archivo();
         hist_archivo.setRuta(archivo.getRuta());
         hist_archivo.setTipoMIME(archivo.getTipoMime());
         TipoArchivo tipoArchivo = null;
         
         for (TipoArchivo tipoArchivoTmp : listTipoArchivo) {

             if (tipoArchivoTmp.getId().longValue() ==
                     archivo.getTipoArchivo().getId().longValue()) { // Mismo Id.

                 tipoArchivo = tipoArchivoTmp;
                 break;
             }
         }
         hist_archivo.setTipoArchivo(tipoArchivo.getTipo());
         h_seccion hist_seccion = null;
         for (h_seccion hist_seccionTmp : listh_seccion) {

             if (archivo.getSeccion() != null &&
                     archivo.getSeccion().getCrn() != null ) {

                 if (hist_seccionTmp.getCrn().equals(archivo.getSeccion().getCrn()) &&
                         hist_seccionTmp.getPeriodo().equals(periodo.getPeriodo())) {

                     hist_seccion = hist_seccionTmp;
                     break;
                 }
             }
         }
         hist_archivo.setSeccion(hist_seccion);
         hist_archivo.setPeriodo(periodo.getPeriodo());
         return hist_archivo;
    }
}
