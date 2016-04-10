/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.serviciosnegocio.tesisP;

import co.uniandes.sisinfo.historico.serviciosnegocio.tesisM.HistoricoTesis1;
import co.uniandes.sisinfo.historico.serviciosnegocio.tesisM.*;
import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.historico.entities.tesispregrado.h_estudiante_pregrado;
import co.uniandes.sisinfo.historico.entities.tesispregrado.h_tesisPregrado;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.NamingException;

/**
 *
 * @author Paola Andrea Gomez Barreto
 */
public class ConversorTesisPregradoH {

    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;


    public ConversorTesisPregradoH() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ConversorTesisPregradoH.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public Secuencia pasarEstudiantesPregradoHistoricoASecuenciaLigera(Collection<h_estudiante_pregrado> estudiantesHistorico) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTES), "");

        for (h_estudiante_pregrado estudianteHistorico : estudiantesHistorico) {
            Secuencia secTesis = pasarEstudiantePregradoHistoricoASecuenciaLigera(estudianteHistorico);
            secPrincipal.agregarSecuencia(secTesis);
        }

        return secPrincipal;
    }

    public Secuencia pasarEstudiantePregradoHistoricoASecuenciaLigera(h_estudiante_pregrado estudianteHistorico){
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");

        if (estudianteHistorico != null) {

            if (estudianteHistorico.getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), estudianteHistorico.getId().toString());
                secPrincipal.agregarSecuencia(secId);
            }

            if (estudianteHistorico.getCodigo() != null) {
                Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), estudianteHistorico.getCodigo());
                secPrincipal.agregarSecuencia(secCodigo);
            }

            if (estudianteHistorico.getCorreo() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudianteHistorico.getCorreo());
                secPrincipal.agregarSecuencia(secCorreo);
            }

            if (estudianteHistorico.getNombres() != null) {
                Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudianteHistorico.getNombres());
                secPrincipal.agregarSecuencia(secNombres);
            }

            if (estudianteHistorico.getApellidos() != null) {
                Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudianteHistorico.getApellidos());
                secPrincipal.agregarSecuencia(secApellidos);
            }


            if (estudianteHistorico.getCorreoAsesor() != null) {
                Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");

                Secuencia secCorreoAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudianteHistorico.getCorreoAsesor());
                secAsesor.agregarSecuencia(secCorreoAsesor);

                if (estudianteHistorico.getNombresAsesor() != null) {
                    Secuencia secNombresAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudianteHistorico.getNombresAsesor());
                    secAsesor.agregarSecuencia(secNombresAsesor);
                }

                if (estudianteHistorico.getApellidosAsesor() != null) {
                    Secuencia secApellidosAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudianteHistorico.getApellidosAsesor());
                    secAsesor.agregarSecuencia(secApellidosAsesor);
                }

                secPrincipal.agregarSecuencia(secAsesor);
            }
            
            if ( estudianteHistorico.getInfoPerdida() != null   ) {
                Secuencia secInfoPerdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_PERDIDA), estudianteHistorico.getInfoPerdida().toString());
                secPrincipal.agregarSecuencia(secInfoPerdida);
            }else{
                Boolean valorTemp = false;
                Secuencia secInfoPerdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_PERDIDA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoPerdida);
            }
            
            if (  estudianteHistorico.getInfoRetirada() != null   ) {
                Secuencia secInfoRetirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_RETIRADA), estudianteHistorico.getInfoRetirada().toString());
                secPrincipal.agregarSecuencia(secInfoRetirada);
            }else{
                Boolean valorTemp = false;
                Secuencia secInfoRetirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_RETIRADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoRetirada);
            }

            if (  estudianteHistorico.getInfoRechazada() != null   ) {
                Secuencia secInfoRechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_RECHAZADA), estudianteHistorico.getInfoRechazada().toString());
                secPrincipal.agregarSecuencia(secInfoRechazada);
            }else{
                Boolean valorTemp = false;
                Secuencia secInfoRechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_RECHAZADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoRechazada);
            }


            if (  estudianteHistorico.getInfoTerminada() != null   ) {
                Secuencia secInfoTerminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_TERMINADA), estudianteHistorico.getInfoTerminada().toString());
                secPrincipal.agregarSecuencia(secInfoTerminada);
            }else{
                Boolean valorTemp = false;
                Secuencia secInfoTerminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_TERMINADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoTerminada);
            }     

        }
        
        return secPrincipal;
    }

    public Secuencia pasarEstudianteHistoricoASecuenciaCompleta(h_estudiante_pregrado estudianteHistorico){
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");

        if (estudianteHistorico != null) {

            if (estudianteHistorico.getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), estudianteHistorico.getId().toString());
                secPrincipal.agregarSecuencia(secId);
            }

            if (estudianteHistorico.getCodigo() != null) {
                Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), estudianteHistorico.getCodigo());
                secPrincipal.agregarSecuencia(secCodigo);
            }

            if (estudianteHistorico.getCorreo() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudianteHistorico.getCorreo());
                secPrincipal.agregarSecuencia(secCorreo);
            }

            if (estudianteHistorico.getNombres() != null) {
                Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudianteHistorico.getNombres());
                secPrincipal.agregarSecuencia(secNombres);
            }

            if (estudianteHistorico.getApellidos() != null) {
                Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudianteHistorico.getApellidos());
                secPrincipal.agregarSecuencia(secApellidos);
            }

            if (estudianteHistorico.getCorreoAsesor() != null) {
                Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");

                Secuencia secCorreoAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudianteHistorico.getCorreoAsesor());
                secAsesor.agregarSecuencia(secCorreoAsesor);

                if (estudianteHistorico.getNombresAsesor() != null) {
                    Secuencia secNombresAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudianteHistorico.getNombresAsesor());
                    secAsesor.agregarSecuencia(secNombresAsesor);
                }

                if (estudianteHistorico.getApellidosAsesor() != null) {
                    Secuencia secApellidosAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudianteHistorico.getApellidosAsesor());
                    secAsesor.agregarSecuencia(secApellidosAsesor);
                }

                secPrincipal.agregarSecuencia(secAsesor);
            }


            if (estudianteHistorico.getTesisPregrado() != null) {
                if (!estudianteHistorico.getTesisPregrado().isEmpty()) {
                    Secuencia secTesises = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");

                    Collection<h_tesisPregrado> tesises  = estudianteHistorico.getTesisPregrado();

                    for(h_tesisPregrado tesis : tesises){
                        Secuencia secTesis = pasarTesisPregradoASecuencia(tesis);
                        secTesises.agregarSecuencia(secTesis);
                    }

                    secPrincipal.agregarSecuencia(secTesises);
                }
            }

            if ( estudianteHistorico.getInfoPerdida() != null   ) {
                Secuencia secInfoPerdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_PERDIDA), estudianteHistorico.getInfoPerdida().toString());
                secPrincipal.agregarSecuencia(secInfoPerdida);
            }else{
                Boolean valorTemp = false;
                Secuencia secInfoPerdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_PERDIDA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoPerdida);
            }

            if (  estudianteHistorico.getInfoRetirada() != null   ) {
                Secuencia secInfoRetirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_RETIRADA), estudianteHistorico.getInfoRetirada().toString());
                secPrincipal.agregarSecuencia(secInfoRetirada);
            }else{
                Boolean valorTemp = false;
                Secuencia secInfoRetirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_RETIRADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoRetirada);
            }

            if (  estudianteHistorico.getInfoRechazada() != null   ) {
                Secuencia secInfoRechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_RECHAZADA), estudianteHistorico.getInfoRechazada().toString());
                secPrincipal.agregarSecuencia(secInfoRechazada);
            }else{
                Boolean valorTemp = false;
                Secuencia secInfoRechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_RECHAZADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoRechazada);
            }

            if (  estudianteHistorico.getInfoTerminada() != null   ) {
                Secuencia secInfoTerminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_TERMINADA), estudianteHistorico.getInfoTerminada().toString());
                secPrincipal.agregarSecuencia(secInfoTerminada);
            }else{
                Boolean valorTemp = false;
                Secuencia secInfoTerminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_PREGRADO_TERMINADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoTerminada);
            }

        }

        return secPrincipal;
    }

    public Secuencia pasarTesisPregradoASecuencia(h_tesisPregrado tesis){
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Secuencia secTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESIS), "");

        if (tesis.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
            secTesis.agregarSecuencia(secId);
        }

        if (tesis.getTemaTesis() != null) {
            Secuencia secTema = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTemaTesis());
            secTesis.agregarSecuencia(secTema);
        }else{
            Secuencia secTema = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), "");
            secTesis.agregarSecuencia(secTema);
        }

        if (tesis.getFechaCreacion() != null) {
            Secuencia secFechaCreacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(tesis.getFechaCreacion().getTime())));
            secTesis.agregarSecuencia(secFechaCreacion);
        }

        if (tesis.getFechaFin() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), sdfHMS.format(new Date(tesis.getFechaCreacion().getTime())));
            secTesis.agregarSecuencia(secFechaFin);
        }

        if (tesis.getRutaABET() != null) {
            Secuencia secRutaArticulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_ABET), tesis.getRutaABET());
            secTesis.agregarSecuencia(secRutaArticulo);
        }

        if (tesis.getRutaPosterTesis() != null) {
            Secuencia secRutaPoster = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_POSTER), tesis.getRutaPosterTesis());
            secTesis.agregarSecuencia(secRutaPoster);
        }

        if (tesis.getCalificacionTesis() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), tesis.getCalificacionTesis().toString());
            secTesis.agregarSecuencia(secNota);
        }

        if (tesis.getCorreoAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");

            Secuencia secCorreoAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis.getCorreoAsesor());
            secAsesor.agregarSecuencia(secCorreoAsesor);

            if (tesis.getNombresAsesor() != null) {
                Secuencia secNombresAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis.getNombresAsesor());
                secAsesor.agregarSecuencia(secNombresAsesor);
            }

            if (tesis.getApellidosAsesor() != null) {
                Secuencia secApellidosAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis.getApellidosAsesor());
                secAsesor.agregarSecuencia(secApellidosAsesor);
            }

            secTesis.agregarSecuencia(secAsesor);
        }        

        if (tesis.getSemestre() != null) {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), tesis.getSemestre());
            secTesis.agregarSecuencia(secSemestre);
        }

        if (tesis.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis.getEstado());
            secTesis.agregarSecuencia(secEstado);
        }

        if (tesis.getComentariosAsesor() != null) {
            Secuencia secComentarios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_TESIS), tesis.getComentariosAsesor());
            secTesis.agregarSecuencia(secComentarios);
        }

        return secTesis;
    }



    //********** METODOS PARA HISTORICO TESIS PREGRADO *************

    //Métodos auxiliares para paso de secuencias a entidades históricas

    public h_tesisPregrado pasarSecuenciaATesisPregradoHistorica(Secuencia sec){
        h_tesisPregrado tesis = new h_tesisPregrado();
        Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            tesis.setId(id);
        }
        Secuencia secFechaCreacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION));
        if (secFechaCreacion != null) {
            String fechaCreacion = secFechaCreacion.getValor();
            try {
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = formatoDeFecha.parse(fechaCreacion);
                tesis.setFechaCreacion(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Secuencia secFechaFin = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
        if (secFechaFin != null) {
            String fechaFin = secFechaFin.getValor();
            try {
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = formatoDeFecha.parse(fechaFin);
                tesis.setFechaFin(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Secuencia secTemaTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));
        if (secTemaTesis != null) {
            tesis.setTemaTesis(secTemaTesis.getValor());
        }
        Secuencia secRutaArticulo = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_ABET));
        if (secRutaArticulo != null) {
            tesis.setRutaABET(secRutaArticulo.getValor());
        }
        Secuencia secRutaPoster = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_POSTER));
        if (secRutaPoster != null) {
            tesis.setRutaPosterTesis(secRutaPoster.getValor());
        }
        Secuencia secNota = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
        if (secNota != null) {
            tesis.setCalificacionTesis(Double.parseDouble(secNota.getValor()));
        }
        Secuencia secEstudiante = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
        if (secEstudiante != null) {
            pasarSecuenciaAEstudiante(secEstudiante, tesis);
        }
        Secuencia secAsesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));
        if (secAsesor != null) {
            pasarSecuenciaAProfesor(secAsesor, tesis);
        }
        Secuencia secSemestre = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
        if (secSemestre != null) {
            tesis.setSemestre( pasarSecuenciaAPeriodo(secSemestre) );
        }
        Secuencia secEstadoTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));
        if (secEstadoTesis != null) {
            tesis.setEstado(secEstadoTesis.getValor());
        }
        Secuencia secComentarios = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_TESIS));
        if (secComentarios != null) {
            tesis.setComentariosAsesor( pasarSecuenciaAComentarios(secComentarios) );
        }

        return tesis;
    }

    public void pasarSecuenciaAEstudiante(Secuencia secEstudiante, h_tesisPregrado tesis) {
        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            tesis.setNombresEstudiante(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            tesis.setApellidosEstudiante(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            tesis.setCorreoEstudiante(secCorreo.getValor());
        }
    }

    public h_estudiante_pregrado pasarSecuenciaAEstudiante(Secuencia secEstudiante) {
        h_estudiante_pregrado estudianteMaestria = new h_estudiante_pregrado();

        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            estudianteMaestria.setNombres(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            estudianteMaestria.setApellidos(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            estudianteMaestria.setCorreo(secCorreo.getValor());
        }
        return estudianteMaestria;
    }

    public h_estudiante_pregrado pasarSecuenciaAEstudianteSimple(Secuencia secEstudiante) {
        h_estudiante_pregrado estudiantePregrado = new h_estudiante_pregrado();

        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            estudiantePregrado.setNombres(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            estudiantePregrado.setApellidos(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            estudiantePregrado.setCorreo(secCorreo.getValor());
        }
        Secuencia secCodigo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE));
        if (secCodigo != null) {
            estudiantePregrado.setCodigo(secCodigo.getValor());
        }

        return estudiantePregrado;
    }

    public void pasarSecuenciaAProfesor(Secuencia secuencia, h_tesisPregrado p) {
        Secuencia secProfesor = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
        Secuencia secNombre = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            p.setNombresAsesor(secNombre.getValor());
        }
        Secuencia secApp = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            p.setApellidosAsesor(secApp.getValor());
        }
        Secuencia secCorreo = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            p.setCorreoAsesor(secCorreo.getValor());
        }
    }

    public String pasarSecuenciaAPeriodo(Secuencia secSemestre) {
        Secuencia secPeriodo = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secPeriodo != null) {
            return secPeriodo.getValor();
        }
        return null;
    }

    public String pasarSecuenciaAComentarios(Secuencia secComentarios) {
        Secuencia sec = secComentarios.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO));
        if (sec != null) {
            return sec.getValor();
        }
        return null;
    }


    public String pasarSecuenciaAEstudianteCorreo(Secuencia secEstudiante) {
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            return secCorreo.getValor();
        }

        return null;
    }


    //Métodos auxiliares para paso de entidades históricas a secuencias

    public Secuencia pasarHistoricoTesisesPregradoASecuencia(List<h_tesisPregrado> tesises) {
        Secuencia secTesises = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");
        for (h_tesisPregrado tesis : tesises) {
            Secuencia secTesis = pasarHistoricoTesisPregradoASecuencia(tesis);
            secTesises.agregarSecuencia(secTesis);
        }
        return secTesises;
    }

    public Secuencia pasarHistoricoTesisPregradoASecuencia(h_tesisPregrado tesis) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESIS), "");
        if (tesis.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }
        if (tesis.getFechaCreacion() != null) {
            Secuencia secFechaCreacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(tesis.getFechaCreacion().getTime())));
            secPrincipal.agregarSecuencia(secFechaCreacion);
        }
        if (tesis.getFechaFin() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), sdfHMS.format(new Date(tesis.getFechaFin().getTime())));
            secPrincipal.agregarSecuencia(secFechaFin);
        }
        if (tesis.getTemaTesis() != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTemaTesis());
            secPrincipal.agregarSecuencia(secTemaTesis);
        }
        if (tesis.getRutaABET() != null) {
            Secuencia secRutaABET = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_ABET), tesis.getRutaABET());
            secPrincipal.agregarSecuencia(secRutaABET);
        }
        if (tesis.getRutaPosterTesis() != null) {
            Secuencia secRutaPoster = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARCHIVO_POSTER), tesis.getRutaPosterTesis());
            secPrincipal.agregarSecuencia(secRutaPoster);
        }
        if (tesis.getCalificacionTesis() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), tesis.getCalificacionTesis().toString());
            secPrincipal.agregarSecuencia(secNota);
        }
        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        if (tesis.getNombresEstudiante() != null) {
            Secuencia secNombresEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis.getNombresEstudiante());
            secEstudiante.agregarSecuencia(secNombresEstudiante);
        }
        if (tesis.getApellidosEstudiante() != null) {
            Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis.getApellidosEstudiante());
            secEstudiante.agregarSecuencia(secApellidosEstudiante);
        }
        if (tesis.getCorreoEstudiante() != null) {
            Secuencia secCorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis.getCorreoEstudiante());
            secEstudiante.agregarSecuencia(secCorreoEstudiante);
        }
        secPrincipal.agregarSecuencia(secEstudiante);
        Secuencia secAsesorTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
        Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
        if (tesis.getNombresEstudiante() != null) {
            Secuencia secNombresProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis.getNombresAsesor());
            secProfesor.agregarSecuencia(secNombresProfesor);
        }
        if (tesis.getApellidosEstudiante() != null) {
            Secuencia secApellidosProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis.getApellidosAsesor());
            secProfesor.agregarSecuencia(secApellidosProfesor);
        }
        if (tesis.getCorreoEstudiante() != null) {
            Secuencia secCorreoProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis.getCorreoAsesor());
            secProfesor.agregarSecuencia(secCorreoProfesor);
        }
        secAsesorTesis.agregarSecuencia(secProfesor);
        secPrincipal.agregarSecuencia(secAsesorTesis);
        if (tesis.getSemestre() != null) {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), tesis.getSemestre());
            secPrincipal.agregarSecuencia(secSemestre);
        }
        if (tesis.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis.getEstado());
            secPrincipal.agregarSecuencia(secEstado);
        }
        if (tesis.getComentariosAsesor() != null) {
            Secuencia secComentarios = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIOS_TESIS), tesis.getComentariosAsesor());
            secPrincipal.agregarSecuencia(secComentarios);
        }
        return secPrincipal;
    }



    //********** METODOS AUXILIARES DE RETORNO DE ATRIBUTOS *************

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

}
